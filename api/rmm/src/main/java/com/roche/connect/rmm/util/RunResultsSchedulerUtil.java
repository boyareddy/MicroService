package com.roche.connect.rmm.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.enums.NiptDeviceType;
import com.roche.connect.common.util.SampleStatus;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.readrepository.RunResultsReadRepository;
import com.roche.connect.rmm.services.AssayIntegrationService;
import com.roche.connect.rmm.writerepository.RunResultsWriteRepository;

@Component
public class RunResultsSchedulerUtil {

	/** The login url. */
	@Value("${pas.authenticate_url}")
	private String loginUrl;

	/** The login entity. */
	@Value("${pas.login_entity}")
	private String loginEntity;

	/** The login entity. */
	@Value("${scheduler.mp24TimeInMin}")
	private String mp24TimeInMin;

	public String getMp24TimeInMin() {
		return mp24TimeInMin;
	}

	public void setMp24TimeInMin(String mp24TimeInMin) {
		this.mp24TimeInMin = mp24TimeInMin;
	}

	@Value("${scheduler.lp24TimeInMin}")
	private String lp24TimeInMin;

	@Autowired
	RunResultsReadRepository runResultsReadRepository;

	@Autowired
	RunResultsWriteRepository runResultsWriteRepository;

	@Autowired
	AssayIntegrationService assayIntegrationService;

	private static String token = null;
	
	private static final String PASSED = "passed";

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Scheduled(cron = "${scheduler.cron_time}")

	public void udpateRunResultStatus() {
		
		List<ProcessStepActionDTO> processStepList = null;

		try {

			if (token == null) {
				token = getToken();
			}

			processStepList = assayIntegrationService.getProcessStepAction(AssayType.NIPT_HTP,
					NiptDeviceType.MP24.getText(), token);

			if (processStepList == null) {
				token = getToken();
				processStepList = assayIntegrationService.getProcessStepAction(AssayType.NIPT_HTP,
						NiptDeviceType.MP24.getText(), token);
			}

			ProcessStepActionDTO optionalMp24PSADTO = processStepList.stream().filter(e -> e.getProcessStepSeq() == 1)
					.findFirst().orElseThrow(() -> new HMTPException("Missing Process step name for MP24"));

			Timestamp maxTimeStamp = Timestamp
					.from(Instant.now().minus(Integer.parseInt(mp24TimeInMin), ChronoUnit.MINUTES));

			List<RunResults> runResults = runResultsReadRepository
					.findRunResultByProcessStepNameByRunStatusAndRunStartTime(optionalMp24PSADTO.getProcessStepName(),
							SampleStatus.INPROGRESS.getText(), maxTimeStamp);

			runResults.forEach(e -> {

				try {
					logger.info("Updating Run Result status Run Id: " + e.getId());
					e.setRunStatus(getRunResultStatusBySampleStatus(
							e.getSampleResults().stream().collect(Collectors.toList())));
					ThreadSessionManager.currentUserSession().setAccessorCompanyId(e.getCompany().getId());
					runResultsWriteRepository.save(e);
					logger.info("Updated Run Result status Run Id: " + e.getId() + ", Status to: " + e.getRunStatus());
				} catch (HMTPException e1) {
					logger.info("Exception on updating status" + e1.getMessage());
				}
			});

		} catch (HMTPException e1) {
			logger.info("Exception on Scheduling: " + e1.getMessage());
		}
	}

	@Scheduled(cron = "${scheduler.cron_time}")
	public void udpateRunResultStatusForLP24() {
		logger.info("Time: " + lp24TimeInMin + " In" + Integer.parseInt(lp24TimeInMin));
		logger.info("Scheduler Update Run Result Status For LP24() -> Start of Scheduler Execution"
				+ Date.from(Instant.now()));

		List<ProcessStepActionDTO> processStepList = null;

		try {

			if (token == null) {
				token = getToken();
			}

			processStepList = assayIntegrationService.getProcessStepAction(AssayType.NIPT_DPCR,
					NiptDeviceType.LP24.getText(), token);

			if (processStepList == null) {
				token = getToken();
				processStepList = assayIntegrationService.getProcessStepAction(AssayType.NIPT_DPCR,
						NiptDeviceType.LP24.getText(), token);
			}

			ProcessStepActionDTO optionalLp24PSADTO = processStepList.stream().filter(e -> e.getProcessStepSeq() == 2)
					.findFirst().orElseThrow(() -> new HMTPException("Missing Process step name for LP24"));

			Timestamp maxTimeStamp = Timestamp
					.from(Instant.now().minus(Integer.parseInt(lp24TimeInMin), ChronoUnit.MINUTES));

			List<RunResults> runResults = runResultsReadRepository
					.findRunResultByProcessStepNameByRunStatusAndRunStartTime(optionalLp24PSADTO.getProcessStepName(),
							SampleStatus.INPROGRESS.getText(), maxTimeStamp);

			runResults.forEach(e -> {
				try {
					logger.info("Updating Run Result status Run Id: " + e.getId());
					e.setRunStatus(getRunResultStatusBySampleStatusAndByFlag(
							e.getSampleResults().stream().collect(Collectors.toList())));
					ThreadSessionManager.currentUserSession().setAccessorCompanyId(e.getCompany().getId());
					runResultsWriteRepository.save(e);
					logger.info("Updated Run Result status Run Id: " + e.getId() + ", Status to: " + e.getRunStatus());
				} catch (Exception e1) {
					logger.info("Exception on updating status" + e1.getMessage());
				}
			});

		} catch (HMTPException e1) {
			logger.info("Exception on Scheduling: " + e1.getMessage());
		}
	}

	public String getRunResultStatusBySampleStatus(List<SampleResults> sampleResultsDTO) throws HMTPException {

		if (sampleResultsDTO != null && !sampleResultsDTO.isEmpty()) {

			String sampleResult = sampleResultsDTO.stream().map(e -> e.getResult()).reduce("", String::concat)
					.toLowerCase();
			
			String flaggedStatus =
                sampleResultsDTO.stream().map(e -> StringUtils.isNotBlank(e.getFlag()) ? e.getFlag() : "")
                    .reduce("", String::concat).toLowerCase();

			if (sampleResult.contains("inprogress") || sampleResult.contains("flagged")
					|| (sampleResult.contains(PASSED) && sampleResult.contains("aborted"))
					 || (StringUtils.isNotBlank(flaggedStatus) && sampleResult.contains(PASSED))) {
				return SampleStatus.COMPLETED_WITH_FLAGS.getText();
			} else if (sampleResult.contains(PASSED)) {
				return SampleStatus.COMPLETED.getText();
			} else if (sampleResult.contains("aborted")) {
				return SampleStatus.ABORTED.getText();
			} else {
				throw new HMTPException("Invalid Run status");
			}
		} else {
			throw new HMTPException("Run status can't able to generate, Status are empty");
		}
	}

	public String getRunResultStatusBySampleStatusAndByFlag(List<SampleResults> sampleResultsDTO) throws HMTPException {

		if (sampleResultsDTO != null && !sampleResultsDTO.isEmpty()) {

			String sampleResult = sampleResultsDTO.stream().map(SampleResults::getResult).reduce("", String::concat)
					.toLowerCase();

			String flaggedStatus = sampleResultsDTO.stream()
					.map(e -> StringUtils.isNotBlank(e.getFlag()) ? e.getFlag() : "").reduce("", String::concat)
					.toLowerCase();

			if (sampleResult.contains("inprogress") || sampleResult.contains("flagged")
					|| (sampleResult.contains(PASSED)
							&& (sampleResult.contains("flagged") || sampleResult.contains("failed")))
					|| (StringUtils.isNotBlank(flaggedStatus) && sampleResult.contains(PASSED))) {
				return SampleStatus.COMPLETED_WITH_FLAGS.getText();
			} else if (sampleResult.contains("failed")) {
				return SampleStatus.FAILED.getText();
			} else if (sampleResult.contains(PASSED)) {
				return SampleStatus.COMPLETED.getText();
			} else {
				throw new HMTPException("Invalid Run status");
			}
		} else {
			throw new HMTPException("Run status can't able to generate, Status are empty");
		}
	}

	public String getToken() {

		Builder builder = RestClientUtil.getBuilder(loginUrl, null);
		Entity<String> entity = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
		token = builder.post(entity, String.class);

		return token;
	}
}
