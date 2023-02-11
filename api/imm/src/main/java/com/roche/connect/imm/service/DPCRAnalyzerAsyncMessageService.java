package com.roche.connect.imm.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.Constants;
import com.roche.connect.common.dpcr_analyzer.DPCRAnalyzerRunStatus;
import com.roche.connect.common.dpcr_analyzer.DPCRProcessStepName;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ErrorCode;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.dpcr_analyzer.QueryMessage;
import com.roche.connect.common.dpcr_analyzer.WFMAcknowledgementMessage;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.exception.InvalidDataException;
import com.roche.connect.imm.utils.IMMConstants;
import com.roche.connect.imm.utils.UrlConstants;

import net.sf.ehcache.util.concurrent.ConcurrentHashMap;

@Component
public class DPCRAnalyzerAsyncMessageService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Value("${connect.wfm_host_url}")
	private String wfmHosturl;

	@Value("${connect.adm_host_url}")
	private String admHostUrl;
	
	@Autowired
	private OrderIntegrationService orderIntegrationService;

	@Autowired
	private RmmIntegrationService rmmIntegrationService;

	@Autowired
	private WFMIntegrationService wfmIntegrationService;

	@Autowired
	private DPCRAnalyzerMessageService dpcrAnalyzerMessageService;

	@Autowired
	private DeviceRunService deviceRunService;

	public static final int MAX_ALLOWED_PLATE_COUNT = 12;

	private SimpleDateFormat formatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);

	@Async
	public void performAsyncQueryMessage(final QueryMessage queryMessage, final String token) {

		try {

			if (queryMessage.getPlateId() == null || queryMessage.getPlateId().isEmpty()
					|| queryMessage.getPlateId().size() > MAX_ALLOWED_PLATE_COUNT
					|| queryMessage.getPlateId().stream().anyMatch(StringUtils::isBlank)) {

				logger.info(" Plate invalid – error code as 200");
				List<String> paramList = new LinkedList<>();
				paramList.add("");
				paramList.add(queryMessage.getDeviceId());
				dpcrAnalyzerMessageService.sendNegativeACKMessage(queryMessage.getMessageControlId(),
						ErrorCode.PLATE_INVALID_CODE, ErrorCode.PLATE_INVALID_DESC,
						NotificationGroupType.INVALID_PLATE_ID_DPCR_DESC, paramList, token);
				return;
			}

			final CopyOnWriteArrayList<SampleResultsDTO> sampleResultsList = new CopyOnWriteArrayList<>();
			Optional<String> optional = queryMessage.getPlateId().stream().filter(plateId -> {

				try {
					// Check plateId is already processed
					List<SampleResultsDTO> existingSampleResults = rmmIntegrationService.getSampleResults(null, null,
							null, null, plateId, null, null, token);

					if (!existingSampleResults.isEmpty()) {
						List<String> paramList = new LinkedList<>();
						paramList.add(plateId);
						paramList.add(queryMessage.getDeviceId());
						logger.info("Plate belongs to multiple samples  – error code as 201");
						dpcrAnalyzerMessageService.sendNegativeACKMessage(queryMessage.getMessageControlId(),
								ErrorCode.PLATE_MULTIPLE_SAMPLES_CODE, ErrorCode.PLATE_MULTIPLE_SAMPLES_DESC,
								NotificationGroupType.DUPLICATE_PLATE_ID_DPCR_DESC, paramList, token);
						return true;
					}

					// Back tracking
					List<SampleResultsDTO> sampleResults = rmmIntegrationService.getSampleResults(null, null, plateId,
							null, null, null, null, token);

					// Plate - does not have any sample for the plate and location
					if (sampleResults.isEmpty()) {

						List<String> paramList = new LinkedList<>();
						paramList.add(plateId);
						paramList.add(queryMessage.getDeviceId());
						logger.info("Plate incomplete – error code as 203");
						dpcrAnalyzerMessageService.sendNegativeACKMessage(queryMessage.getMessageControlId(),
								ErrorCode.PLATE_INCOMPLETE_CODE, ErrorCode.PLATE_INCOMPLETE_DESC,
								NotificationGroupType.INCOMPLETE_PLATE_DPCR_DESC, paramList, token);
						return true;
					}

					sampleResultsList.addAll(sampleResults);
					final Map<String, Integer> plateLaneCount = new ConcurrentHashMap<>();
					final Map<String, Integer> plateCount = new ConcurrentHashMap<>();
					sampleResults.stream().forEach(s -> {
						String k1 = s.getOutputContainerId() + s.getOutputContainerPosition();
						if (plateLaneCount.containsKey(k1))
							plateLaneCount.put(k1, plateLaneCount.get(k1) + 1);
						else
							plateLaneCount.put(k1, 1);

						String k2 = s.getOutputContainerId();
						if (plateCount.containsKey(k2))
							plateCount.put(k2, plateCount.get(k2) + 1);
						else
							plateCount.put(k2, 1);
					});

					// Plate+lane contains multiple accessioningId
					boolean isExceededPlateLaneCount = plateLaneCount.values().stream().anyMatch(v -> (v > 1));

					// Check Plate contains more than 8 lanes
					boolean isExceededPlateCount = plateCount.values().stream().anyMatch(c -> (c > 8));

					if (isExceededPlateLaneCount || isExceededPlateCount) {
						logger.info("Plate belongs to multiple samples  – error code as 201");

						List<String> paramList = new LinkedList<>();
						paramList.add(plateId);
						paramList.add(queryMessage.getDeviceId());
						dpcrAnalyzerMessageService.sendNegativeACKMessage(queryMessage.getMessageControlId(),
								ErrorCode.PLATE_MULTIPLE_SAMPLES_CODE, ErrorCode.PLATE_MULTIPLE_SAMPLES_DESC,
								NotificationGroupType.DUPLICATE_PLATE_ID_DPCR_DESC, paramList, token);
						return true;
					}

					SampleResultsDTO invalidSampleOrder = sampleResults.stream().filter(sample -> {

						List<OrderDTO> list = null;
						try {
							list = orderIntegrationService.getOrder(sample.getAccesssioningId(), token);
						} catch (Exception e) {
							return true;
						}

						return (list == null || list.isEmpty());

					}).findFirst().orElse(null);

					if (invalidSampleOrder != null) {
						logger.info("No order found – error code as 202");
						List<String> paramList = new LinkedList<>();
						paramList.add(invalidSampleOrder.getAccesssioningId());
						paramList.add(queryMessage.getDeviceId());
						dpcrAnalyzerMessageService.sendNegativeACKMessage(queryMessage.getMessageControlId(),
								ErrorCode.PLATE_NO_ORDER_CODE, ErrorCode.PLATE_NO_ORDER_DESC,
								NotificationGroupType.NO_ORDER_FOUND_DPCR_DESC, paramList, token);

						return true;
					}

					return false;
				} catch (IOException e) {
					logger.error("Failed to validate dPCR plateId, plateId:" + plateId, e);
					return true;
				}

			}).findFirst();

			if (optional.isPresent())
				return;

			String runId = deviceRunService.generateDeviceRunId();
			long runResultId = dpcrAnalyzerMessageService.saveRunResult(queryMessage, runId, token);
			boolean sendWFMQueryMessageStatus = dpcrAnalyzerMessageService.sendWFMQueryMessage(queryMessage, sampleResultsList, runResultId, token);
		
			if (!sendWFMQueryMessageStatus) {
				dpcrAnalyzerMessageService.sendNegativeACKMessage(queryMessage.getMessageControlId(), null, null, null,
						null, token);
				return;
			}
			
			dpcrAnalyzerMessageService.sendPositiveACKMessage(queryMessage.getMessageControlId(), token);
			dpcrAnalyzerMessageService.sendOMLMessage(queryMessage, runId, sampleResultsList, token);

		} catch (Exception e) {
			logger.error("Error when processing dPCR QBP message", e);
		}
	}

	@Async
	public void processAsyncAcknowledgementMessage(final AcknowledgementMessage ackMessage, String token) {

		try {

			if (ackMessage.getStatus().equalsIgnoreCase("AR")) {
				List<String> paramList = new LinkedList<>();
				paramList.add(ackMessage.getRunId());
				paramList.add(ackMessage.getDeviceSerialNo());
				AdmNotificationService.sendNotification(NotificationGroupType.RESULT_REJECTED_DPCR, paramList, token,
						admHostUrl + UrlConstants.ADM_NOTIFICATION_URL);
				return;
			}
			
			RunResultsDTO runResultsAndSampleResults = rmmIntegrationService.getRunResultsAndSampleResults(
					ackMessage.getRunId(), DPCRProcessStepName.DPCR_ANALYZER, ackMessage.getDeviceSerialNo(), token);

			if (runResultsAndSampleResults == null || runResultsAndSampleResults.getSampleResults() == null)
				return;

			Collection<SampleResultsDTO> sampleResults = dpcrAnalyzerMessageService
					.traceSampleResults(runResultsAndSampleResults, false, token);
			for (SampleResultsDTO sample : sampleResults) {

				WFMAcknowledgementMessage ack = new WFMAcknowledgementMessage();
				ack.setAccessioningId(sample.getAccesssioningId());
				ack.setDeviceId(ackMessage.getDeviceSerialNo());

				wfmIntegrationService.genericMessagePoster(ack, wfmHosturl + UrlConstants.WFM_DPCR_ANALYZER_ACK_URL,
						token);
			}

		} catch (IOException | HMTPException e) {
			logger.error("Error when processing dPCR ACK message", e);
		}
	}

	@Async
	public void processAsyncESUMessage(ESUMessage esuMessage, String token) {

		try {

			if (!isValidESUMessage(esuMessage)) {
				logger.info("ESUMessage is not valid – error code as 102");
				List<String> paramList = new LinkedList<>();
				paramList.add(esuMessage.getDeviceId());
				dpcrAnalyzerMessageService.sendNegativeACKMessage(esuMessage.getMessageControlId(),
						ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE, ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC,
						NotificationGroupType.MISSING_INFO_DPCR_DESC, paramList, token);
				return; 
			}

			if (esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.IN_PROGRESS)
					|| esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.ID_PROCESSED)
					|| esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.SS_ABORTED)) {

				RunResultsDTO runResultsDTO = rmmIntegrationService.getRunResultsAndSampleResults(esuMessage.getRunId(),
						DPCRProcessStepName.DPCR_ANALYZER, esuMessage.getDeviceId(), token);
				
				if (runResultsDTO == null 
						|| ((runResultsDTO.getRunStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.COMPLETED)
								|| runResultsDTO.getRunStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.COMPLETED_WITH_FLAGS))
								&& esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.IN_PROGRESS))) {
					
					logger.info("RunResult not found for RunId: " + esuMessage.getRunId());
					logger.info("Inprogress ESUMessage received after completed status");
					dpcrAnalyzerMessageService.sendNegativeACKMessage(esuMessage.getMessageControlId(),
							ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE, ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null,
							null, token);
					return;
				}
				
				if(isStatusChanged(esuMessage, runResultsDTO)) {
					List<String> paramList = new LinkedList<>();
					paramList.add(esuMessage.getDeviceId());
					paramList.add(esuMessage.getRunId());
					dpcrAnalyzerMessageService.sendNegativeACKMessage(esuMessage.getMessageControlId(),
							ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE, ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC,
							NotificationGroupType.MUL_MSG_DIFF_STATUS_DPCR_DESC, paramList, token);
					return; 
				}
				
				if (StringUtils.isNotBlank(esuMessage.getEstimatedTimeRemaining())) {
					Date start = formatter.parse(esuMessage.getDateTimeMessageGenerated());
					Date end = formatter.parse(esuMessage.getEstimatedTimeRemaining());
					long diff = end.getTime() - start.getTime();

					if (diff < 0)
						diff = 0;

					runResultsDTO.setRunRemainingTime(diff);
				} else {
					runResultsDTO.setRunRemainingTime(0L);
				}

				runResultsDTO.setUpdatedBy(IMMConstants.SYSTEM_STR);
				Timestamp now = new Timestamp(System.currentTimeMillis());
				runResultsDTO.setUpdatedDateTime(now);
				runResultsDTO.setRunStatus(getRunResultStatus(esuMessage, runResultsDTO));

				if (runResultsDTO.getRunStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.COMPLETED)
						|| runResultsDTO.getRunStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.COMPLETED_WITH_FLAGS)) {
					
					setRunResultsDetails(esuMessage, runResultsDTO);
					Date completionDate = formatter.parse(esuMessage.getDateTimeMessageGenerated());
					runResultsDTO.setRunCompletionTime(new Timestamp(completionDate.getTime()));
				}

				boolean sendWFMESUMessageStatus = dpcrAnalyzerMessageService.sendWFMESUMessage(esuMessage, runResultsDTO, token);
				if (!sendWFMESUMessageStatus) {
					dpcrAnalyzerMessageService.sendNegativeACKMessage(esuMessage.getMessageControlId(), null, null, null,
							null, token);					
					return;
				}
				
				rmmIntegrationService.saveRunResult(runResultsDTO, token);
				dpcrAnalyzerMessageService.sendPositiveACKMessage(esuMessage.getMessageControlId(), token);
			} else {
				throw new InvalidDataException("Invalid dPCR Run Status");
			}
			
		} catch (Exception e) {
			logger.error("Error when processing dPCR ESU message", e);
		}
	}
	
	@Async
	public void processAsyncORUMessage(final ORUMessage oruMessage, String token) {

		try {

			if (!isValidORUMessage(oruMessage)) {
				logger.info("ORUMessage is not valid – error code as 102");
				List<String> paramList = new LinkedList<>();
				paramList.add(oruMessage.getDeviceId());
				dpcrAnalyzerMessageService.sendNegativeACKMessage(oruMessage.getMessageControlId(),
						ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE, ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC,
						NotificationGroupType.MISSING_INFO_DPCR_DESC, paramList, token);
				return;
			}
			
			RunResultsDTO runResultsDTO = rmmIntegrationService.getRunResultsAndSampleResults(oruMessage.getRunId(),
					DPCRProcessStepName.DPCR_ANALYZER, oruMessage.getDeviceId(), token);

			if (runResultsDTO == null || runResultsDTO.getRunStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.COMPLETED)
					|| runResultsDTO.getRunStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.COMPLETED_WITH_FLAGS)) {

				logger.info("RunResult not found for RunId: " + oruMessage.getRunId());
				logger.info("ORUMessage received after completed status – error code as 102");
				dpcrAnalyzerMessageService.sendNegativeACKMessage(oruMessage.getMessageControlId(),
						ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE, ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null,
						null, token);
				return;
			}

			if(runResultsDTO.getSampleResults().isEmpty())
				runResultsDTO.setRunStatus(DPCRAnalyzerRunStatus.IN_PROGRESS);
			
			runResultsDTO.setUpdatedBy(IMMConstants.SYSTEM_STR);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			runResultsDTO.setUpdatedDateTime(now);
			Timestamp runStartTime = new Timestamp(formatter.parse(oruMessage.getDateTimeMessageGenerated()).getTime());
			runResultsDTO.setRunStartTime(runStartTime);
			runResultsDTO.setOperatorName(oruMessage.getOperatorName());
			runResultsDTO.setComments(oruMessage.getRunComments());
			setRunResultsDetails(oruMessage, runResultsDTO);

			boolean sendWFMORUMessageStatus = dpcrAnalyzerMessageService.sendWFMORUMessage(oruMessage, runResultsDTO,
					token);
			if (!sendWFMORUMessageStatus) {
				dpcrAnalyzerMessageService.sendNegativeACKMessage(oruMessage.getMessageControlId(), null, null, null,
						null, token);
				return;
			}

			rmmIntegrationService.saveRunResult(runResultsDTO, token);
			dpcrAnalyzerMessageService.sendPositiveACKMessage(oruMessage.getMessageControlId(), token);
			
		} catch (Exception e) {
			logger.error("Error when processing dPCR ORUMessage ", e);
		}
	}
	
	private boolean isValidESUMessage(ESUMessage esuMessage) {

		if (StringUtils.isNotBlank(esuMessage.getRunId()) && StringUtils.isNotBlank(esuMessage.getDeviceId())
				&& StringUtils.isNotBlank(esuMessage.getStatus())) {

			if (esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.ID_PROCESSED)
					&& StringUtils.isBlank(esuMessage.getFilePath()))
				return false;

			return !(esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.IN_PROGRESS)
					&& StringUtils.isBlank(esuMessage.getEstimatedTimeRemaining()));
		} else {
			return false;
		}
	}
	
	private boolean isValidORUMessage(ORUMessage oruMessage) {

		if (StringUtils.isBlank(oruMessage.getRunId()) || StringUtils.isBlank(oruMessage.getDeviceId()))
			return false;

		if(oruMessage.getOruSampleDetails().isEmpty())
			return false;
		
		return oruMessage.getOruSampleDetails().stream().noneMatch(s -> StringUtils.isBlank(s.getAccessioningId()));
	}
	
	private boolean isStatusChanged(ESUMessage esuMessage, RunResultsDTO runResultsDTO) {

		switch (runResultsDTO.getRunStatus()) {

		case DPCRAnalyzerRunStatus.ABORTED:
			return !esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.SS_ABORTED);

		case DPCRAnalyzerRunStatus.COMPLETED:
		case DPCRAnalyzerRunStatus.COMPLETED_WITH_FLAGS:
			return !esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.ID_PROCESSED);
		default:
			return false;
		}

	}
	
	private String getRunResultStatus(ESUMessage esuMessage, RunResultsDTO runResultsDTO) {

		if (esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.IN_PROGRESS))
			return DPCRAnalyzerRunStatus.IN_PROGRESS;

		if (esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.SS_ABORTED))
			return DPCRAnalyzerRunStatus.ABORTED;

		if (esuMessage.getStatus().equalsIgnoreCase(DPCRAnalyzerRunStatus.ID_PROCESSED)) {

			return runResultsDTO.getSampleResults().stream()
					.anyMatch(sampleResult -> StringUtils.isNotBlank(sampleResult.getFlag()))
							? DPCRAnalyzerRunStatus.COMPLETED_WITH_FLAGS
							: DPCRAnalyzerRunStatus.COMPLETED;
		} else {
			throw new InvalidDataException("Invalid dPCR Run Status");
		}
	}

	private void setRunResultsDetails(ESUMessage esuMessage, RunResultsDTO runResultsDTO) {

		Optional<RunResultsDetailDTO> optional = runResultsDTO.getRunResultsDetail().stream()
				.filter(runDetail -> runDetail.getAttributeName().equals(IMMConstants.DPCR_ANALYZER_FILE_PATH_STR))
				.findFirst();

		RunResultsDetailDTO runResultsDetailDTO = null;
		if (optional.isPresent()) {
			runResultsDetailDTO = optional.get();
			runResultsDetailDTO.setUpdatedBy(IMMConstants.ADMIN_USER_STR);
			runResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
			runResultsDetailDTO.setAttributeValue(esuMessage.getFilePath());

		} else {
			runResultsDetailDTO = new RunResultsDetailDTO();
			runResultsDetailDTO.setCreatedBy(IMMConstants.ADMIN_USER_STR);
			runResultsDetailDTO.setCreatedDateTime(Timestamp.from(Instant.now()));
			runResultsDetailDTO.setAttributeName(IMMConstants.DPCR_ANALYZER_FILE_PATH_STR);
			runResultsDetailDTO.setAttributeValue(esuMessage.getFilePath());

			runResultsDTO.getRunResultsDetail().add(runResultsDetailDTO);
		}

	}

	private void setRunResultsDetails(final ORUMessage oruMessage, RunResultsDTO runResultsDTO) {

		Timestamp now = new Timestamp(System.currentTimeMillis());

		if (StringUtils.isNotBlank(oruMessage.getReleasedBy())) {

			Optional<RunResultsDetailDTO> releasedByOptional = runResultsDTO.getRunResultsDetail().stream()
					.filter(r -> r.getAttributeName().equals(Constants.RELEASED_BY_STR)).findFirst();

			RunResultsDetailDTO runDetail = null;
			if (releasedByOptional.isPresent()) {
				runDetail = releasedByOptional.get();
				runDetail.setUpdatedDateTime(now);
				runDetail.setUpdatedBy(IMMConstants.ADMIN_USER_STR);
				runDetail.setAttributeValue(oruMessage.getReleasedBy());
			} else {
				runDetail = new RunResultsDetailDTO();
				runDetail.setCreatedDateTime(now);
				runDetail.setCreatedBy(IMMConstants.ADMIN_USER_STR);
				runDetail.setAttributeName(Constants.RELEASED_BY_STR);
				runDetail.setAttributeValue(oruMessage.getReleasedBy());

				runResultsDTO.getRunResultsDetail().add(runDetail);
			}
		}

		if (StringUtils.isNotBlank(oruMessage.getSentBy())) {

			Optional<RunResultsDetailDTO> sentByOptional = runResultsDTO.getRunResultsDetail().stream()
					.filter(r -> r.getAttributeName().equals(Constants.SENT_BY_STR)).findFirst();

			RunResultsDetailDTO runDetail = null;

			if (sentByOptional.isPresent()) {
				runDetail = sentByOptional.get();
				runDetail.setUpdatedDateTime(now);
				runDetail.setUpdatedBy(IMMConstants.ADMIN_USER_STR);
				runDetail.setAttributeValue(oruMessage.getSentBy());
			} else {
				runDetail = new RunResultsDetailDTO();
				runDetail.setCreatedDateTime(now);
				runDetail.setCreatedBy(IMMConstants.ADMIN_USER_STR);
				runDetail.setAttributeName(Constants.SENT_BY_STR);
				runDetail.setAttributeValue(oruMessage.getSentBy());

				runResultsDTO.getRunResultsDetail().add(runDetail);
			}
		}
	}

}
