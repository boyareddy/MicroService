package com.roche.connect.imm.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.common.error.ErrorCode;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.mp96.AdaptorACKMessage;
import com.roche.connect.common.mp96.OULACKMessage;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.mp96.OULSampleResultMessage;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.connect.common.mp96.QueryResponseMessage;
import com.roche.connect.common.mp96.QueryResponseSample;
import com.roche.connect.common.mp96.WFMACKMessage;
import com.roche.connect.common.mp96.WFMQueryMessage;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.prop.ErrorCodeService;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.util.RunStatusConstants;
import com.roche.connect.imm.utils.IMMConstants;
import com.roche.connect.imm.utils.RestClient;
import com.roche.connect.imm.utils.UrlConstants;

@Component
public class MP96AsyncMessageService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Value("${connect.wfm_host_url}")
	private String wfmHostUrl;

	@Value("${connect.mp96_adaptor_host_url}")
	private String mp96AdaptorHostUrl;

	@Value("${connect.lp24_adaptor_host_url}")
	private String lp24AdaptorHostUrl;

	@Value("${connect.adm_host_url}")
	private String admHostUrl;

	@Autowired
	private OrderIntegrationService orderIntegrationService;

	@Autowired
	private AssayIntegrationService assayIntegrationService;

	@Autowired
	private WFMIntegrationService wfmIntegrationService;

	@Autowired
	private DeviceRunService deviceRunService;
	
	@Autowired
	private RmmIntegrationService rmmIntegrationService;
	
	
	
	@Async
	public void performAsyncQueryMessageRequest(QueryMessage queryMessage, List<ContainerSamplesDTO> containerSamples,
			String token) {

		if (containerSamples == null || containerSamples.isEmpty()) {
			return;
		}

		try {

			QueryResponseMessage queryResponseMessage = new QueryResponseMessage();

			String deviceRunId = deviceRunService.generateDeviceRunId();
			queryResponseMessage.setRunId(deviceRunId);
			queryResponseMessage.setDeviceID(queryMessage.getDeviceId());
			queryResponseMessage.setMessageType(MessageType.MP96_RESPONSE_MESSAGE_TYPE);

			queryResponseMessage.setDateTime(
					new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR).format(new Date()));

			List<ProcessStepActionDTO> processSteplist = assayIntegrationService
					.findProcessStepByAssayTypeAndDeviceType(DeviceType.NIPT_DPCR_ASSAY_TYPE_URL_KEY,
							DeviceType.MP96_DEVICE_TYPE, token);

			List<QueryResponseSample> queryRespList = new CopyOnWriteArrayList<>();
			List<WFMQueryMessage> workOrderRequestMessageList = new ArrayList<>();
			boolean flag = false;

			for (ContainerSamplesDTO containersampleDTO : containerSamples) {

				containersampleDTO.setDeviceRunID(deviceRunId);

				WFMQueryMessage workorderRequest = new WFMQueryMessage();
				workorderRequest.setAccessioningId(containersampleDTO.getAccessioningID());
				workorderRequest.setMessageType(MessageType.MP96_NAEXTRACTION);
				workorderRequest.setSendingApplicationName(queryMessage.getDeviceName());
				workorderRequest.setDeviceSerialNumber(queryMessage.getDeviceId());
				workOrderRequestMessageList.add(workorderRequest);

				if (!flag) {
					queryResponseMessage.setCreatedBy(containersampleDTO.getCreatedBy());
					flag = true;
				}

				QueryResponseSample queryRespsample = new QueryResponseSample();
				queryRespsample.setAccessioningId(containersampleDTO.getAccessioningID());
				queryRespsample.setContainerId(containersampleDTO.getContainerID());
				queryRespsample.setPosition(containersampleDTO.getPosition());
				queryRespsample.setComment(containersampleDTO.getOrderComments());

				if (!processSteplist.isEmpty()) {

					ProcessStepActionDTO processStepActionDTO = processSteplist.get(0);
					if (processStepActionDTO != null) {
						queryRespsample.setReagentName(processStepActionDTO.getReagent());
						queryRespsample.setReagentVersion("0.2");// HARD CODED SINCE NO SUCH VALUE IN AMM
						queryRespsample.setProtocolName(processStepActionDTO.getReagent());
						queryRespsample.setProtocolVersion("0.6.4");// HARD CODED SINCE NO SUCH VALUE IN AMM
						queryRespsample.setSampleVolume(processStepActionDTO.getSampleVolume());
						queryRespsample.setEluateVolume(processStepActionDTO.getEluateVolume());
					}
				}

				queryRespList.add(queryRespsample);
			}
			queryResponseMessage.setSamples(queryRespList);

			orderIntegrationService.updateContainerSamples(containerSamples, token);

			for (WFMQueryMessage workorderRequest : workOrderRequestMessageList) {
				wfmIntegrationService.workOrderRequest(workorderRequest, token);
			}

			logger.info("Sending Work order Import message to Adaptor, Message:" + queryResponseMessage);
			RestClient.post(mp96AdaptorHostUrl + UrlConstants.MP96_ADAPTOR_WORK_ORDER_RSP_URL, queryResponseMessage,
					token, null);

		} catch (Exception exp) {
			logger.error("Error occurred consumeGenericRequest Implementation" + exp.getMessage(), exp);
		}
	}

	@Async
	public void performAsyncACKMessage(AdaptorACKMessage adaptorACKMessage, final List<ContainerSamplesDTO> containerSamples,
			String token) {

		try {
			String url = wfmHostUrl + UrlConstants.MP96_WFM_ACK_UPDATE;

			for (ContainerSamplesDTO sample : containerSamples) {

				WFMACKMessage message = new WFMACKMessage();
				message.setAccessioningId(sample.getAccessioningID());
				message.setDeviceId(adaptorACKMessage.getDeviceId());

				Response response = RestClient.post(url, message, token, null);
				if (response.getStatus() != HttpStatus.SC_OK) {
					logger.info("MP96 - ACK message, Failure responseCode from WFM, responseCode: "
							+ response.getStatus());
					return;
				}
			}

			boolean containerSamplesUpdateStatus = orderIntegrationService.updateContainerSamples(containerSamples,
					token);
			logger.info("OMM  ContainerSample update status: " + containerSamplesUpdateStatus);

		} catch (Exception e) {
			logger.error("Error while performing ACK in Asynchronous way" + e.getMessage(), e);
		}
	}

	@Async
	public void performAsyncOULMessage(OULRunResultMessage oulRunResultMessage, RunResultsDTO runResult, String token) {

		try {
			String invalidAccessioningId=null;
			boolean isValidMessage = true;
			
			if (oulRunResultMessage.getOulSampleResultMessage() == null
					|| oulRunResultMessage.getOulSampleResultMessage().isEmpty()) {
				isValidMessage = false;
			}

			if (isValidMessage) {
				for (OULSampleResultMessage sampleResult : oulRunResultMessage.getOulSampleResultMessage()) {

					if (StringUtils.isBlank(sampleResult.getAccessioningId())
							|| StringUtils.isBlank(sampleResult.getOutputContainerId())
							|| StringUtils.isBlank(sampleResult.getOutputPlateType())
							|| StringUtils.isBlank(sampleResult.getPosition())) {

						invalidAccessioningId = sampleResult.getAccessioningId();
						logger.info(
								"OULMessage:Required message without specimen details and Container details from MP96:RUNId::"
										+ oulRunResultMessage.getRunId());
						isValidMessage = false;
						continue;

					}

					WFMoULMessage wfmOULMsg = new WFMoULMessage();

					wfmOULMsg.setAccessioningId(sampleResult.getAccessioningId());
					wfmOULMsg.setDeviceSerialNumber(oulRunResultMessage.getDeviceId());
					wfmOULMsg.setMessageType(oulRunResultMessage.getMessageType());
					wfmOULMsg.setRunResultsId(runResult.getRunResultId());
					wfmOULMsg.setRunResultStatus(oulRunResultMessage.getRunResultStatus());
					wfmOULMsg.setSendingApplicationName(oulRunResultMessage.getSendingApplicationName());

					wfmOULMsg.setOulSampleResultMessage(sampleResult);

					Response response = RestClient.post(wfmHostUrl + UrlConstants.MP96_WFM_OUL_URL, wfmOULMsg, token,
							null);
					if (response.getStatus() != HttpStatus.SC_OK) {
						logger.info("MP96 - OUL message, Failure responseCode from WFM, responseCode: "
								+ response.getStatus());
						return;
					}
				}
			}

			runResult.setRunStatus(getRunStatusBasedOnSamples(oulRunResultMessage));
			rmmIntegrationService.updateRunResult(runResult, token);

			OULACKMessage oulACKMessage = new OULACKMessage();
			oulACKMessage.setDeviceId(oulRunResultMessage.getDeviceId());
			oulACKMessage.setDeviceRunId(oulRunResultMessage.getRunId());
			SimpleDateFormat formatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);
			oulACKMessage.setDateTimeMessageGenerated(formatter.format(new Date()));
			if (isValidMessage) {
				oulACKMessage.setStatus("AA");
			} else {
				oulACKMessage.setStatus("AR");
				oulACKMessage.setErrorCode(ErrorCode.REQUIRED_FIELD_MISSING);
				oulACKMessage.setErrorDescription(ErrorCodeService.getInstance().get(ErrorCode.REQUIRED_FIELD_MISSING));

				List<String> errorMessages = new LinkedList<>();
				errorMessages.add(invalidAccessioningId);
				errorMessages.add(oulRunResultMessage.getDeviceId());
				AdmNotificationService.sendNotification(NotificationGroupType.MISSING_INFO_MP96_DESC, errorMessages, token,admHostUrl+UrlConstants.ADM_NOTIFICATION_URL);
				
			}

			RestClient.post(mp96AdaptorHostUrl + UrlConstants.MP96_ADAPTOR_OUL_ACK_URL, oulACKMessage, token, null);

		} catch (UnsupportedEncodingException | NullPointerException | HMTPException e) {
			logger.error("Error while performing OUL in Asynchronous way" + e.getMessage(), e);
		}

	}
	
	@Async
	public void sendNegativeACKMessage(OULRunResultMessage oulRunResultMessage, String token)
			throws UnsupportedEncodingException {

		List<String> errorMessages = new LinkedList<>();
		errorMessages.add(oulRunResultMessage.getDeviceId());
		errorMessages.add(oulRunResultMessage.getOulSampleResultMessage().get(0).getOutputContainerId());
		try {
			AdmNotificationService.sendNotification(NotificationGroupType.PLATE_RESULTS_MAPPING_MISSING_MP96DESC,
					errorMessages, token, admHostUrl + UrlConstants.ADM_NOTIFICATION_URL);
		} catch (HMTPException e) {
			logger.error("Failed to sendNegativeAckMessage Notification in MP96AsyncMessageService");
		}

		OULACKMessage oulACKMessage = new OULACKMessage();
		oulACKMessage.setDeviceId(oulRunResultMessage.getDeviceId());
		oulACKMessage.setDeviceRunId(oulRunResultMessage.getRunId());
		SimpleDateFormat formatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);
		oulACKMessage.setDateTimeMessageGenerated(formatter.format(new Date()));
		oulACKMessage.setStatus("AR");
		RestClient.post(mp96AdaptorHostUrl + UrlConstants.MP96_ADAPTOR_OUL_ACK_URL, oulACKMessage, token, null);
	}

	@Async
	public void processAsyncMessage(Object object, String url, String token) {

		try {
			wfmIntegrationService.genericMessagePoster(object, url, token);
		} catch (IOException e) {
			logger.error("Failed to process Generic Message", e);
		}
	}
	
	private String getRunStatusBasedOnSamples(OULRunResultMessage oulRunResultMessage) {

		boolean isSampleCompleted = false;
		boolean isSampleFailed = false;
		boolean isSampleFlaged = false;
		for (OULSampleResultMessage sample : oulRunResultMessage.getOulSampleResultMessage()) {

			if (sample.getSampleResult().equalsIgnoreCase("P")) {
				isSampleCompleted = true;
			} else if (sample.getSampleResult().equalsIgnoreCase("F")) {
				isSampleFailed = true;
			}

			if (StringUtils.isNotBlank(sample.getFlag()))
				isSampleFlaged = true;

			if (isSampleCompleted && isSampleFailed && isSampleFlaged)
				break;

		}

		if (isSampleCompleted && (isSampleFlaged || isSampleFailed))
			return RunStatusConstants.MP96_COMPLETED_WITH_FLAGS;

		return (!isSampleCompleted) ? RunStatusConstants.MP96_FAILED : RunStatusConstants.MP96_COMPLETED;
	}
}
