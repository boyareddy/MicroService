package com.roche.connect.imm.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.constant.StatusMessage;
import com.roche.connect.common.mp96.AdaptorACKMessage;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.mp96.OULSampleResultMessage;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.util.RunStatusConstants;
import com.roche.connect.common.util.SampleStatus;
import com.roche.connect.imm.utils.IMMConstants;

@Service
public class MP96MessageService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Value("${connect.mp96_adaptor_host_url}")
	private String mp96AdaptorHostUrl;
	
	@Autowired
	private MP96AsyncMessageService mp96AsyncMessageService;

	@Autowired
	private OrderIntegrationService orderIntegrationService;

	@Autowired
	private RmmIntegrationService rmmIntegrationService;

	public Response processQueryMessage(QueryMessage queryMessage) {

		try {
			List<ContainerSamplesDTO> containerSamples = orderIntegrationService.getDPCRContainerSamples();

			if (containerSamples == null || containerSamples.isEmpty())
				return Response.status(HttpStatus.SC_NO_CONTENT).build();

			logger.info("containerSamplesDTOList size: " + containerSamples.size());

			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
			mp96AsyncMessageService.performAsyncQueryMessageRequest(queryMessage, containerSamples, token);

			return Response.status(HttpStatus.SC_ACCEPTED).build();
		} catch (Exception e) {
			logger.info("Failed to get containerSamples List from OMM.", e);
			return Response.status(HttpStatus.SC_FAILED_DEPENDENCY).build();
		}

	}
	
	public Response processACKMessage(AdaptorACKMessage adaptorACKMessage) {

		try {
			List<ContainerSamplesDTO> containerSamples = orderIntegrationService
					.getDPCRContainerSamples(adaptorACKMessage.getDeviceRunId());

			if(containerSamples.isEmpty())
				return Response.status(HttpStatus.SC_NO_CONTENT).build();
			
			for (ContainerSamplesDTO s : containerSamples) {
				s.setDeviceID(adaptorACKMessage.getDeviceId());
				s.setStatus(StatusMessage.MP96_SENT_TO_DEVICE);
			}
			
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
			mp96AsyncMessageService.performAsyncACKMessage(adaptorACKMessage, containerSamples, token);

		} catch (Exception e) {
			logger.error("Error while processing DPCR ACK message. ", e);
		}

		return Response.status(HttpStatus.SC_ACCEPTED).build();

	}

	public Response processOULMessage(OULRunResultMessage oulRunResultMessage) {

		try {

			Timestamp now = new Timestamp(System.currentTimeMillis());
			RunResultsDTO runResultsDTO = rmmIntegrationService.getRunResultsAndSampleResults(
					oulRunResultMessage.getRunId(), MessageType.MP96_NAEXTRACTION, oulRunResultMessage.getDeviceId());
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());

			if (runResultsDTO == null) {

				boolean isRunValid = orderIntegrationService.isRunIDValid(oulRunResultMessage.getRunId());
				if (!isRunValid) {
					mp96AsyncMessageService.sendNegativeACKMessage(oulRunResultMessage, token);
					return Response.status(HttpStatus.SC_ACCEPTED).build();
				}

				runResultsDTO = insertRunResult(oulRunResultMessage);
				
				if (runResultsDTO.getRunResultId() <= 0) {
					logger.info("Failed to save Run Result in RMM");
					return Response.status(HttpStatus.SC_FAILED_DEPENDENCY).build();
				}
			} else {
				
				if (isRunMovedNextState(oulRunResultMessage)) {
					logger.info("MP96 SSU is received, after LP24 SSU is processed");
					mp96AsyncMessageService.sendNegativeACKMessage(oulRunResultMessage, token);
					return Response.status(HttpStatus.SC_ACCEPTED).build();
				}

				if (isMP96StatusChanged(oulRunResultMessage, runResultsDTO)) {
					logger.info("MP96 varying status, going to send negative ACK");
					mp96AsyncMessageService.sendNegativeACKMessage(oulRunResultMessage, token);
					return Response.status(HttpStatus.SC_ACCEPTED).build();
				}

				runResultsDTO.setUpdatedBy(IMMConstants.SYSTEM_STR);
				runResultsDTO.setUpdatedDateTime(now);
				runResultsDTO.setRunStatus(RunStatusConstants.MP96_PENDING);
				rmmIntegrationService.updateRunResult(runResultsDTO);
			}

			mp96AsyncMessageService.performAsyncOULMessage(oulRunResultMessage, runResultsDTO, token);
		} catch (Exception e) {
			logger.error("Error while processing dPCR OUL message. ", e);
		}

		return Response.status(HttpStatus.SC_ACCEPTED).build();

	}

	private boolean isRunMovedNextState(OULRunResultMessage oulRunResultMessage) throws IOException {

		if (!oulRunResultMessage.getOulSampleResultMessage().isEmpty()) {

			OULSampleResultMessage sampleResultMessage = oulRunResultMessage.getOulSampleResultMessage().get(0);

			if (sampleResultMessage != null) {
				List<SampleResultsDTO> existingSampleResults = rmmIntegrationService.getSampleResults(null, null, null,
						null, sampleResultMessage.getOutputContainerId(), null, null);

				return (existingSampleResults != null && !existingSampleResults.isEmpty());
			}
		}

		return false;
	}

	private boolean isMP96StatusChanged(OULRunResultMessage oulRunResultMessage, RunResultsDTO runResultsDTO) {
		return ((runResultsDTO.getRunStatus().equalsIgnoreCase("Completed")
				&& oulRunResultMessage.getRunResultStatus().equalsIgnoreCase("Failed"))
				|| (runResultsDTO.getRunStatus().equalsIgnoreCase("Failed")
						&& oulRunResultMessage.getRunResultStatus().equalsIgnoreCase("Passed")));
	}
	
	private RunResultsDTO insertRunResult(OULRunResultMessage oulRunResultMessage) throws IOException, ParseException {

		Timestamp now = new Timestamp(System.currentTimeMillis());

		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setCreatedBy(IMMConstants.SYSTEM_STR);
		runResultsDTO.setComments(oulRunResultMessage.getRunComments());
		runResultsDTO.setCreatedDateTime(now);
		runResultsDTO.setUpdatedDateTime(now);
		runResultsDTO.setDeviceId(oulRunResultMessage.getDeviceId());
		runResultsDTO.setDeviceRunId(oulRunResultMessage.getRunId());
		runResultsDTO.setProcessStepName(MessageType.MP96_NAEXTRACTION);
		runResultsDTO.setRunStatus(SampleStatus.PENDING.getText());
		runResultsDTO.setAssayType(AssayType.NIPT_DPCR);

		if (!oulRunResultMessage.getOulSampleResultMessage().isEmpty()) {

			OULSampleResultMessage oulSampleResultMessage = oulRunResultMessage.getOulSampleResultMessage().get(0);
			if (oulSampleResultMessage != null) {

				runResultsDTO.setOperatorName(oulSampleResultMessage.getOperator());
				SimpleDateFormat formatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);
				Timestamp runStartTime = new Timestamp(
						formatter.parse(oulSampleResultMessage.getRunStartTime()).getTime());
				Timestamp runEndTime = new Timestamp(formatter.parse(oulSampleResultMessage.getRunEndTime()).getTime());
				runResultsDTO.setRunStartTime(runStartTime);
				runResultsDTO.setRunCompletionTime(runEndTime);
				runResultsDTO.setRunReagentsAndConsumables(runReagentAndCounsumable(oulSampleResultMessage));
			}
		}

		long runResultId = rmmIntegrationService.saveRunResult(runResultsDTO);
		runResultsDTO.setRunResultId(runResultId);
		
		return runResultsDTO;
	}
	
	private List<RunReagentsAndConsumablesDTO> runReagentAndCounsumable(OULSampleResultMessage oulSampleResultMessage)
			throws ParseException {
		List<RunReagentsAndConsumablesDTO> consumablesDTOList = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		
		
		RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
		runReagentsAndConsumablesDTO.setAttributeName("Internal Control");
		runReagentsAndConsumablesDTO.setAttributeValue(Stream.of((Optional.ofNullable(oulSampleResultMessage.getLotNo()).orElse("")),
       		Optional.ofNullable(oulSampleResultMessage.getBarcode()).orElse(""),
       		Optional.ofNullable(oulSampleResultMessage.getVolume()).orElse(""),
       		Optional.ofNullable(new Timestamp(formatter.parse(oulSampleResultMessage.getExpDate()).getTime()).toString()).orElse("")).collect(Collectors.joining(";")));
		runReagentsAndConsumablesDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
		runReagentsAndConsumablesDTO.setCreatedBy(IMMConstants.SYSTEM_STR);
		consumablesDTOList.add(runReagentsAndConsumablesDTO);
                       
		RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO1 = new RunReagentsAndConsumablesDTO();
		runReagentsAndConsumablesDTO1.setAttributeName("Reagents");
		runReagentsAndConsumablesDTO1.setAttributeValue(Stream.of((Optional.ofNullable(oulSampleResultMessage.getReagentKitName()).orElse("")),
       		(Optional.ofNullable(oulSampleResultMessage.getReagentVesion()).orElse(""))).collect(Collectors.joining(";")));
		runReagentsAndConsumablesDTO1.setUpdatedDateTime(Timestamp.from(Instant.now()));
		runReagentsAndConsumablesDTO.setCreatedBy(IMMConstants.SYSTEM_STR);
       consumablesDTOList.add(runReagentsAndConsumablesDTO1);

		return consumablesDTOList;
	}
}
