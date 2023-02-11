package com.roche.connect.imm.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.Assay;
import com.roche.connect.common.dpcr_analyzer.DPCRAnalyzerRunStatus;
import com.roche.connect.common.dpcr_analyzer.DPCRProcessStepName;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUSampleDetails;
import com.roche.connect.common.dpcr_analyzer.QueryMessage;
import com.roche.connect.common.dpcr_analyzer.QueryResponseMessage;
import com.roche.connect.common.dpcr_analyzer.QueryResponseSample;
import com.roche.connect.common.dpcr_analyzer.ReagentAndConsumablesConstants;
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMORUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMQueryMessage;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.imm.utils.IMMConstants;
import com.roche.connect.imm.utils.RestClient;
import com.roche.connect.imm.utils.UrlConstants;

@Service
public class DPCRAnalyzerMessageService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Value("${connect.adm_host_url}")
	private String admHostUrl;

	@Value("${connect.dpcr_analyzer_adaptor_host_url}")
	private String dpcrAnalyzerHostUrl;

	@Value("${connect.wfm_host_url}")
	private String wfmHostUrl;

	@Value("${connect.runID}")
	private String runIDPrefix;

	@Autowired
	private RmmIntegrationService rmmIntegrationService;
	
	@Autowired
	private AssayIntegrationService assayIntegrationService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private SimpleDateFormat formatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);

	public void sendPositiveACKMessage(String messageControlId, String token) throws IOException {

		try {
			AcknowledgementMessage ackMessage = new AcknowledgementMessage();
			ackMessage.setMessageControlId(messageControlId);
			ackMessage.setStatus("AA");
			ackMessage.setDateTimeMessageGenerated(formatter.format(new Date()));

			logger.info("send Positive ACK message to Adaptor: " + objectMapper.writeValueAsString(ackMessage));
			RestClient.post(dpcrAnalyzerHostUrl + UrlConstants.DPCR_ANALYZER_ADAPTOR_ACK_URL, ackMessage, token,
					null);
		} catch (IOException e) {
			logger.error("Failed to send postive ACK message", e);
			throw e;
		}
	}

	public void sendNegativeACKMessage(String messageControlId, String errorCode, String errorDescription,
			NotificationGroupType messageGroup, List<String> paramList, String token) {

		try {
			AcknowledgementMessage ackMessage = new AcknowledgementMessage();
			ackMessage.setMessageControlId(messageControlId);
			ackMessage.setStatus("AR");
			ackMessage.setErrorCode(errorCode);
			ackMessage.setErrorDescription(errorDescription);
			ackMessage.setDateTimeMessageGenerated(formatter.format(new Date()));

			logger.info("send Negative ACK message to Adaptor: " + objectMapper.writeValueAsString(ackMessage));
			RestClient.post(dpcrAnalyzerHostUrl + UrlConstants.DPCR_ANALYZER_ADAPTOR_ACK_URL, ackMessage, token,
					null);

			if (messageGroup == null || paramList == null || paramList.isEmpty())
				return;

			AdmNotificationService.sendNotification(messageGroup, paramList, token,admHostUrl+UrlConstants.ADM_NOTIFICATION_URL);

		} catch (IOException | HMTPException e) {
			logger.error("Failed to send Negative ACK message", e);
		}
	}

	public long saveRunResult(final QueryMessage queryMessage, String runId, String token)
			throws IOException, ParseException {

		try {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			RunResultsDTO runResultsDTO = rmmIntegrationService.getRunResults(queryMessage.getDeviceId(),
					DPCRProcessStepName.DPCR_ANALYZER, DPCRAnalyzerRunStatus.NOT_STARTED, token);

			if (runResultsDTO != null) {
				runResultsDTO.setUpdatedBy(IMMConstants.SYSTEM_STR);
			} else {

				runResultsDTO = new RunResultsDTO();
				runResultsDTO.setCreatedBy(IMMConstants.SYSTEM_STR);
				runResultsDTO.setCreatedDateTime(now);
				runResultsDTO.setDeviceId(queryMessage.getDeviceId());
				runResultsDTO.setProcessStepName(DPCRProcessStepName.DPCR_ANALYZER);
				runResultsDTO.setAssayType(AssayType.NIPT_DPCR);
				runResultsDTO.setRunStatus(DPCRAnalyzerRunStatus.NOT_STARTED);
			}

			runResultsDTO.setUpdatedDateTime(now);
			runResultsDTO.setDeviceRunId(runId);
			runResultsDTO.setRunStartTime(
					new Timestamp(formatter.parse(queryMessage.getDateTimeMessageGenerated()).getTime()));
			setRunResultsDetails(queryMessage, runResultsDTO);

			return rmmIntegrationService.saveRunResult(runResultsDTO, token);
		} catch (IOException | ParseException e) {
			logger.error("Failed to get/save RunResult message to RMM", e);
			throw e;
		}
	}

	private void setRunResultsDetails(final QueryMessage queryMessage, RunResultsDTO runResultsDTO) {

		Optional<RunResultsDetailDTO> optional = runResultsDTO.getRunResultsDetail().stream()
				.filter(runDetail -> runDetail.getAttributeName().equals(IMMConstants.DPCR_ANALYZER_PLATE_ID_LIST_STR))
				.findFirst();

		RunResultsDetailDTO runResultsDetailDTO = null;
		String plateIdListStr = queryMessage.getPlateId().stream().collect(Collectors.joining(","));
		if (optional.isPresent()) {
			runResultsDetailDTO = optional.get();
			runResultsDetailDTO.setUpdatedBy(IMMConstants.ADMIN_USER_STR);
			runResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
			runResultsDetailDTO.setAttributeValue(plateIdListStr);

		} else {
			runResultsDetailDTO = new RunResultsDetailDTO();
			runResultsDetailDTO.setCreatedBy(IMMConstants.ADMIN_USER_STR);
			runResultsDetailDTO.setCreatedDateTime(Timestamp.from(Instant.now()));
			runResultsDetailDTO.setAttributeName(IMMConstants.DPCR_ANALYZER_PLATE_ID_LIST_STR);
			runResultsDetailDTO.setAttributeValue(plateIdListStr);

			runResultsDTO.getRunResultsDetail().add(runResultsDetailDTO);
		}
	}
	
	public void sendOMLMessage(QueryMessage queryMessage, String runId, List<SampleResultsDTO> sampleResultsList,
			String token) {

		try {

			List<DeviceTestOptionsDTO> deviceTestOptionsList = assayIntegrationService.findDeviceTestOptions(
					AssayType.NIPT_DPCR, DeviceType.DPCR_ANALYZER, DPCRProcessStepName.DPCR_ANALYZER, token);

			logger.info("DeviceTestOption list from AMM: " + objectMapper.writeValueAsString(deviceTestOptionsList));
			DeviceTestOptionsDTO deviceTestOptions = null;
			if (!deviceTestOptionsList.isEmpty()) {
				deviceTestOptions = deviceTestOptionsList.get(0);
			}

			QueryResponseMessage rspMessage = new QueryResponseMessage();
			rspMessage.setMessageType(MessageType.DPCR_ANALYZER_OML);
			rspMessage.setMessageControlId(queryMessage.getMessageControlId());
			rspMessage.setRunId(runId);
			rspMessage.setSamples(getQueryResponseSample(sampleResultsList, deviceTestOptions));

			logger.info("send OMLMessage to Adaptor: " + objectMapper.writeValueAsString(rspMessage));
			RestClient.post(dpcrAnalyzerHostUrl + UrlConstants.DPCR_ANALYZER_ADAPTOR_OML_URL, rspMessage, token, null);
		} catch (IOException e) {
			logger.error("Failed to send OMLMessage to Adaptor", e);
		}
	}

	private List<QueryResponseSample> getQueryResponseSample(List<SampleResultsDTO> sampleResultsList, DeviceTestOptionsDTO deviceTestOptions){
		
		List<QueryResponseSample> rspSampleList = new ArrayList<>();
		for (SampleResultsDTO sample : sampleResultsList) {

			QueryResponseSample rspSample = new QueryResponseSample();
			rspSample.setAccessioningId(sample.getAccesssioningId());
			rspSample.setContainerId(sample.getOutputContainerId());
			rspSample.setPosition(sample.getOutputContainerPosition());
			rspSample.setStatus("Success");

			Assay assay = new Assay();
			Collection<SampleReagentsAndConsumablesDTO> reagentsAndConsumables = sample
					.getSampleReagentsAndConsumables();
			boolean f1 = false;
			boolean f2 = false;
			boolean f3 = false;
			for (SampleReagentsAndConsumablesDTO reagent : reagentsAndConsumables) {

				if (StringUtils.isNotBlank(reagent.getAttributeName())
						&& reagent.getAttributeName().equals(ReagentAndConsumablesConstants.KIT)) {

					String[] split = reagent.getAttributeValue().split(";");
					if (split.length > 0) {
						assay.setKit(split[0]);
						f1 = true;
					}
				}

				if (StringUtils.isNotBlank(reagent.getAttributeName())
						&& reagent.getAttributeName().equals(ReagentAndConsumablesConstants.MASTER_MIX)) {

					String[] split = reagent.getAttributeValue().split(";");
					if (split.length > 0) {
						assay.setMasterMix(split[0]);
						f2 = true;
					}
				}

				if (StringUtils.isNotBlank(reagent.getAttributeName())
						&& reagent.getAttributeName().equals(ReagentAndConsumablesConstants.PI)) {
					rspSample.setPlateIntegator(reagent.getAttributeValue());
					f3 = true;

				}

				if (f1 && f2 && f3)
					break;
			}

			Collection<SampleResultsDetailDTO> sampleResultsDetail = sample.getSampleResultsDetail();

			if (sampleResultsDetail != null) {

				for (SampleResultsDetailDTO sampleDetail : sampleResultsDetail) {

					if (StringUtils.isNotBlank(sampleDetail.getAttributeName())
							&& sampleDetail.getAttributeName().equals(ReagentAndConsumablesConstants.PI)) {
						rspSample.setPlateIntegator(sampleDetail.getAttributeValue());
						break;
					}
				}
			}

			if (rspSample.getPlateIntegator() == null)
				rspSample.setPlateIntegator("");

			if (deviceTestOptions != null) {
				assay.setName(deviceTestOptions.getTestProtocol());
				assay.setVersion(deviceTestOptions.getAnalysisPackageName());
			}

			rspSample.setAssay(Arrays.asList(assay));
			rspSampleList.add(rspSample);
		}
		
		return rspSampleList;
	}
	
	public boolean sendWFMQueryMessage(QueryMessage queryMessage, List<SampleResultsDTO> sampleResultsList,
			long runResultId, String token) throws IOException {

		try {
			
			for (SampleResultsDTO sample : sampleResultsList) {

				WFMQueryMessage wfmQueryMessage = new WFMQueryMessage();
				wfmQueryMessage.setProcessStepName(DPCRProcessStepName.DPCR_ANALYZER);
				wfmQueryMessage.setDeviceId(queryMessage.getDeviceId());
				wfmQueryMessage.setAccessioningId(sample.getAccesssioningId());
				wfmQueryMessage.setContainerId(sample.getOutputContainerId());
				wfmQueryMessage.setContainerPosition(sample.getOutputContainerPosition());
				wfmQueryMessage.setRunResultsId(runResultId);
				wfmQueryMessage.setMessageType(MessageType.DPCR_ANALYZER_QBP);

				logger.info("Sending Query Message to WFM, JSON:" + objectMapper.writeValueAsString(wfmQueryMessage));
				Response response = RestClient.post(wfmHostUrl + UrlConstants.WFM_DPCR_ANALYZER_QUERY_URL,
						wfmQueryMessage, token, null);
				if (response.getStatus() != HttpStatus.SC_OK) {
					logger.info("dPCR Query message - Failure from WFM, responseCode: "
							+ response.getStatus());
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			logger.error("Failed to send Query message to WFM", e);
			throw e;
		}
	}
	
	public boolean sendWFMESUMessage(ESUMessage esuMessage, RunResultsDTO runResultsDTO, String token) throws IOException {
		
		try {
			logger.info("Sending ESU Message to WFM - Run results from RMM, JSON:" + objectMapper.writeValueAsString(runResultsDTO));
			
			Collection<SampleResultsDTO> sampleResults = null;
			boolean isInprogress = false;
			if (esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.IN_PROGRESS)) {
				sampleResults = traceSampleResults(runResultsDTO, true, token);
				isInprogress = true;
			} else {
				sampleResults = runResultsDTO.getSampleResults();
			}

			for (SampleResultsDTO sample : sampleResults) {

				WFMESUMessage wfmESUMessage = new WFMESUMessage();
				wfmESUMessage.setAccessioningId(sample.getAccesssioningId());
				wfmESUMessage.setMessageType(MessageType.DPCR_ANALYZER_ESU);
				wfmESUMessage.setProcessStepName(DPCRProcessStepName.DPCR_ANALYZER);
				wfmESUMessage.setDeviceId(esuMessage.getDeviceId());
				wfmESUMessage.setRunResultId(Long.toString(runResultsDTO.getRunResultId()));
				if (isInprogress) {
					wfmESUMessage.setInputContainerId(sample.getOutputContainerId());
				} else {
					wfmESUMessage.setInputContainerId(sample.getInputContainerId());
					if (StringUtils.isNotBlank(sample.getFlag()))
						wfmESUMessage.setFlag(sample.getFlag());
				}
				wfmESUMessage.setDateTimeMessageGenerated(esuMessage.getDateTimeMessageGenerated());
                
				if (esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.IN_PROGRESS)) {
					wfmESUMessage.setStatus(DPCRAnalyzerRunStatus.IN_PROGRESS);
				} else if (esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.ID_PROCESSED)) {
					wfmESUMessage.setStatus(DPCRAnalyzerRunStatus.PASSED);
				} else if (esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.SS_ABORTED)) {
					wfmESUMessage.setStatus(DPCRAnalyzerRunStatus.ABORTED);
				}

				logger.info("Sending ESU Message to WFM, JSON:" + objectMapper.writeValueAsString(wfmESUMessage));
				Response response = RestClient.post(wfmHostUrl + UrlConstants.WFM_DPCR_ANALYZER_ESU_URL, wfmESUMessage, token, null);
				if (response.getStatus() != HttpStatus.SC_OK) {
					
					logger.info("dPCR ESU message - Failure from WFM, responseCode: "
							+ response.getStatus());
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			logger.error("Failed to send ESU message to WFM", e);
			throw e;
		}
	}
	
	public Collection<SampleResultsDTO> traceSampleResults(RunResultsDTO runResultsDTO, boolean backTrackAlways,
			String token) throws IOException {

		if (backTrackAlways || runResultsDTO.getSampleResults().isEmpty()) {

			Collection<SampleResultsDTO> sampleResults = new ArrayList<>();
			Optional<RunResultsDetailDTO> runDetailOptional = runResultsDTO.getRunResultsDetail().stream().filter(
					runDetail -> runDetail.getAttributeName().equals(IMMConstants.DPCR_ANALYZER_PLATE_ID_LIST_STR))
					.findFirst();

			if (runDetailOptional.isPresent()) {
				RunResultsDetailDTO runDetail = runDetailOptional.get();
				List<String> plateIdList = Arrays.asList(runDetail.getAttributeValue().split(","));

				for (String plateId : plateIdList) {
					sampleResults.addAll(
							rmmIntegrationService.getSampleResults(null, null, plateId, null, null, null, null, token));
				}
			}

			return sampleResults;
		} else {
			return runResultsDTO.getSampleResults();
		}
	}

	public boolean sendWFMORUMessage(ORUMessage oruMessage, RunResultsDTO runResultsDTO, String token) throws IOException {

		try {

			boolean flag = false;
			if(runResultsDTO.getSampleResults().isEmpty()) 
				flag = true;
			
			Collection<SampleResultsDTO> sampleResults = traceSampleResults(runResultsDTO, false, token);
			for (ORUSampleDetails sample : oruMessage.getOruSampleDetails()) {

				if (sample.getAccessioningId() == null)
					continue;

				Optional<SampleResultsDTO> sampleResult = sampleResults.stream()
						.filter(s -> s.getAccesssioningId().equals(sample.getAccessioningId())).findFirst();

				if (sampleResult.isPresent()) {

					WFMORUMessage wfmORUMessage = new WFMORUMessage();
					wfmORUMessage.setAccessioningId(sample.getAccessioningId());
					wfmORUMessage.setMessageType(MessageType.DPCR_ANALYZER_ORU);
					wfmORUMessage.setProcessStepName(DPCRProcessStepName.DPCR_ANALYZER);
					wfmORUMessage.setDeviceId(oruMessage.getDeviceId());
					wfmORUMessage.setRunResultId(String.valueOf(runResultsDTO.getRunResultId()));
					if (StringUtils.isNotBlank(sample.getFlag()))
						wfmORUMessage.setFlag(sample.getFlag());
					
					wfmORUMessage.setAssayList(sample.getAssayList());
					wfmORUMessage.setPartitionEngineList(sample.getPartitionEngineList());
					
					if (flag) {
						wfmORUMessage.setStatus(DPCRAnalyzerRunStatus.IN_PROGRESS);
						wfmORUMessage.setInputContainerId(sampleResult.get().getOutputContainerId());
					} else {
						wfmORUMessage.setStatus(sampleResult.get().getStatus());
						wfmORUMessage.setInputContainerId(sampleResult.get().getInputContainerId());
					}

					logger.info("Sending ORU Message to WFM, JSON:" + objectMapper.writeValueAsString(wfmORUMessage));
					Response response = RestClient.post(wfmHostUrl + UrlConstants.WFM_DPCR_ANALYZER_ORU_URL,
							wfmORUMessage, token, null);
					if (response.getStatus() != HttpStatus.SC_OK) {
						logger.info("dPCR ORU message - Failure from WFM, responseCode: "
								+ response.getStatus());
						return false;
					}
				}
			}
			return true;
		} catch (IOException e) {
			logger.error("Failed to send ORU message to WFM", e);
			throw e;
		}
	}

}
