/*******************************************************************************
 * MessageProcessorService.java                  
 *  Version:  1.0
 * 
 *  Authors:  Alexander
 * 
 * *********************
 *  Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  All Rights Reserved.
 * 
 *  NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 *  The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *                
 * *********************
 *  ChangeLog:
 * 
 *  Alexanders@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.imm.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.common.exceptions.InvalidStateException;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.enums.NiptDeviceType;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO.ComplexIdDetailsStatus;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.htp.status.HTPConstants;
import com.roche.connect.common.htp.status.HTPStatusConstants;
import com.roche.connect.common.htp.status.HtpStatus;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.Consumable;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.common.mp24.message.RSPMessage;
import com.roche.connect.common.mp24.message.SampleInfo;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.util.SampleStatus;
import com.roche.connect.imm.utils.IMMConstants;
import com.roche.connect.imm.utils.MessageType;
import com.roche.connect.imm.utils.NIPTProcessStepConstants;
import com.roche.connect.imm.utils.UrlConstants;

/**
 * The Class MessageProcessorService.
 *
 * @author gosula.r
 */
@Component
public class MessageProcessorService {

	/** The logger. */
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	/** The wfm QBP request url. */
	@Value("${connect.wfm_qbp_request_url}")
	private String wfmQBPRequestUrl;

	/** The wfm update request url. */
	@Value("${connect.wfm_update_request_url}")
	private String wfmUpdateRequestUrl;

	/** The rmm run result url. */
	@Value("${connect.rmm_run_result_url}")
	private String rmmRunResultUrl;

	/** The rmm sample result url. */
	@Value("${connect.rmm_sample_result_url}")
	private String rmmSampleResultUrl;

	/** The rmm sample result by OP container. */
	@Value("${connect.rmm_get_sample_result}")
	private String rmmGetSampleResult;

	/** The rmm sample result by run result id. */
	@Value("${connect.rmm_run_result_by_id_url}")
	private String rmmRunResultById;

	/** The rmm run result by devie id url. */
	@Value("${connect.rmm_run_result_by_deviceid_url}")
	private String rmmRunResultByDeviceIdUrl;

	/** The amm test types by assayTypeId and deviceType */
	@Value("${connect.amm_testoptions_url}")
	private String ammTestOptionsUrl;

	/** AMM Host Url */
	@Value("${connect.amm_host_url}")
	private String ammHostUrl;

	@Value("${connect.mp24_adaptor_host_url}")
	private String mp24AdaptorHostUrl;

	@Value("${connect.lp24_adaptor_host_url}")
	private String lp24AdaptorHostUrl;

	/** AMM Process step action Url */
	@Value("${connect.amm_processstep_action_url}")
	private String ammProcessStepActionUrl;

	/** AMM Process step action Url */
	@Value("${connect.amm_device_test_options_url}")
	private String ammDeviceTestOptionsUrl;

	/** The message service. */
	@Autowired
	private MessageService messageService;

	/** The RMM Integration service. */
	@Autowired
	private RmmIntegrationService rmmService;

	@Autowired
	private AssayIntegrationService assayService;

	@Autowired
	private OrderIntegrationService ommService;

	@Value("${connect.adm_host_url}")
	private String admHostUrl;

	/** The object mapper. */
	private ObjectMapper objectMapper = new ObjectMapper();

	public String getAmmTestOptionsUrl() {
		return ammTestOptionsUrl;
	}

	public void setAmmTestOptionsUrl(String ammTestOptionsUrl) {
		this.ammTestOptionsUrl = ammTestOptionsUrl;
	}

	public String getAmmHostUrl() {
		return ammHostUrl;
	}

	public void setAmmHostUrl(String ammHostUrl) {
		this.ammHostUrl = ammHostUrl;
	}

	public String getAmmProcessStepActionUrl() {
		return ammProcessStepActionUrl;
	}

	public void setAmmProcessStepActionUrl(String ammProcessStepActionUrl) {
		this.ammProcessStepActionUrl = ammProcessStepActionUrl;
	}

	public String getAmmDeviceTestOptionsUrl() {
		return ammDeviceTestOptionsUrl;
	}

	public void setAmmDeviceTestOptionsUrl(String ammDeviceTestOptionsUrl) {
		this.ammDeviceTestOptionsUrl = ammDeviceTestOptionsUrl;
	}

	public String getRmmGetSampleResult() {
		return rmmGetSampleResult;
	}

	public void setRmmGetSampleResult(String rmmGetSampleResult) {
		this.rmmGetSampleResult = rmmGetSampleResult;
	}
	
	@Value("${assay.mp24_Assay}")
	private String mp24Assay;

	/**
	 * Process request.
	 *
	 * @param adaptorRequestMessage the adaptor request message
	 * @param token                 the token
	 */
	@Async
	public void processRequest(AdaptorRequestMessage adaptorRequestMessage, String token) {

		try {

			String url = getSubmitUrl(adaptorRequestMessage.getMessageType());
			String resourceUrl = url + getUrlParam(adaptorRequestMessage.getMessageType(),
					adaptorRequestMessage.getSendingApplicationName());
			logger.info("Resource :" + resourceUrl + "    Url: processRequest" + url);
			logger.info("AdaptorRequestMessage" + adaptorRequestMessage.toString());
			if (url == null) {
				logger.error("Microservice url is not set for respective message type. Message type: "
						+ adaptorRequestMessage.getMessageType());
				return;
			}

			String messageJson = objectMapper.writeValueAsString(adaptorRequestMessage);
			logger.info("AdaptorRequestMessage: " + messageJson);

			if (adaptorRequestMessage.getMessageType().equals(MessageType.NA_EXTRACTION)) {

				// Assay missMatch for HTP Assay & No Order Found Notifications in RC-8567

				String assayType = mp24Assay;
				List<OrderDTO> ordersFromOmm = Optional
						.ofNullable(ommService.getOrder(adaptorRequestMessage.getAccessioningId(), token))
						.orElse(Collections.emptyList());

				OrderDTO orderDTO = !ordersFromOmm.isEmpty() ? ordersFromOmm.get(0) : null;

				if (orderDTO == null) {
					logger.error("Order Not available for this Sample ID from OMM");
					// Sending Notification for No order Found
					List<String> content = new ArrayList<>();
					content.add(adaptorRequestMessage.getAccessioningId());
					content.add(adaptorRequestMessage.getDeviceSerialNumber());
					AdmNotificationService.sendNotification(NotificationGroupType.NO_ORDER_FOUND_MP24, content, token,admHostUrl+UrlConstants.ADM_NOTIFICATION_URL);
					processNoOrderFoundResponse(adaptorRequestMessage, "AA");
				} else {
					if (orderDTO.getAssayType().equalsIgnoreCase(assayType)) {
						logger.info("The given SampleId's Assay is NIPTHTP");
						postRequest(url, Entity.entity(adaptorRequestMessage, MediaType.APPLICATION_JSON), token);
					} else {
						logger.error("The given sampleId's assayType is not NIPTHTP " + orderDTO.getAssayType());
						List<String> content = new ArrayList<>();
						content.add(adaptorRequestMessage.getAccessioningId());
						content.add(adaptorRequestMessage.getDeviceSerialNumber());
						AdmNotificationService.sendNotification(NotificationGroupType.NO_ORDER_FOUND_MP24, content,
								token,admHostUrl+UrlConstants.ADM_NOTIFICATION_URL);
						processNoOrderFoundResponse(adaptorRequestMessage, "AR");
					}
				}
			} else {
				postRequest(resourceUrl, Entity.entity(adaptorRequestMessage, MediaType.APPLICATION_JSON), token);
			}
		} catch (Exception e) {
			logger.error("Failed to process the adaptor request, message" + e.getMessage(), e);
		}

	}

	/**
	 * Submit response.
	 *
	 * @param adaptorResponseMessage the adaptor response message
	 */
	public Response submitResponse(AdaptorResponseMessage adaptorResponseMessage) {

		try {
			String messageJson = objectMapper.writeValueAsString(adaptorResponseMessage);
			logger.info("AdaptorResponseMessage: " + messageJson);
			String adaptorSubmitUrl = getAdaptorSubmitUrl(adaptorResponseMessage.getResponse().getMessageType());
			logger.info("adapter URl:" + adaptorSubmitUrl);
			RestClientUtil.postMethodCall(adaptorSubmitUrl, adaptorResponseMessage, null);

			return Response.status(Status.OK).build();
		} catch (Exception e) {
			logger.error("Failed to submit AdaptorResponse data to Adaptor, message" + e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Gets the submit url.
	 *
	 * @param messageType the message type
	 * @return the submit url
	 */
	private String getSubmitUrl(String messageType) {
		switch (messageType) {

		case MessageType.NA_EXTRACTION:
			return wfmQBPRequestUrl;
		case MessageType.STATUS_UPDATE:
			return wfmUpdateRequestUrl;
		case MessageType.QUERY_MESSAGE:
			return wfmUpdateRequestUrl;
		case MessageType.STATUSUPDATE_MESSAGE:
			return wfmUpdateRequestUrl;
		default:
			return null;
		}

	}

	/**
	 * Gets the adaptor submit url.
	 *
	 * @param messageType the message type
	 * @return the adaptor submit url
	 */
	private String getAdaptorSubmitUrl(String messageType) {

		switch (messageType) {
		case MessageType.RSP:
			return mp24AdaptorHostUrl + UrlConstants.MP24_ADAPTOR_RSP_URL;
		case MessageType.U03ACK:
			return mp24AdaptorHostUrl + UrlConstants.MP24_ADAPTOR_ACK_URL;
		case MessageType.RESPONSE_MESSAGE:
			return lp24AdaptorHostUrl + UrlConstants.LP24_ADAPTOR_RSP_URL;
		case MessageType.ACKNOWLEDGEMENT_MESSAGE:
			return lp24AdaptorHostUrl + UrlConstants.LP24_ADAPTOR_OUL_ACK_URL;
		default:
			return null;
		}
	}

	/**
	 * Gets the url param.
	 *
	 * @param messageType    the message type
	 * @param instrumentName the instrument name
	 * @return the url param
	 */
	private String getUrlParam(String messageType, String instrumentName) {
		logger.info("getUrlParamMethod - Msg Type:" + messageType + "  Insturment:" + instrumentName);
		if (messageType.equals(MessageType.STATUS_UPDATE) && instrumentName.equalsIgnoreCase("MagnaPure24")) {
			return UrlConstants.MP_STATUS;
		} else if (messageType.equals(MessageType.QUERY_MESSAGE) && instrumentName.contains("LP")) {
			return UrlConstants.LP_QUERY;
		} else if (messageType.equals(MessageType.STATUSUPDATE_MESSAGE) && instrumentName.contains("LP")) {
			return UrlConstants.LP_STATUS;
		} else {
			return null;
		}
	}

	/**
	 * Process request.
	 *
	 * @param specimenStatusUpdateMessage the specimen status update message
	 * @param token                       the token
	 */
	@Async
	public void processRequest(SpecimenStatusUpdateMessage specimenStatusUpdateMessage, String token) {
		try {
			String url = getSubmitUrl((specimenStatusUpdateMessage.getMessageType()).toString());
			if (url == null) {
				logger.error("Microservice url is not set for LP24. message type. Message type: "
						+ specimenStatusUpdateMessage.getMessageType());
				return;
			}
			String finalUrl = url + getUrlParam((specimenStatusUpdateMessage.getMessageType()).toString(),
					specimenStatusUpdateMessage.getSendingApplicationName());
			String messageJson = objectMapper.writeValueAsString(specimenStatusUpdateMessage);
			logger.info("SSU message: " + messageJson);
			postRequest(finalUrl, Entity.entity(specimenStatusUpdateMessage, MediaType.APPLICATION_JSON), token);

		} catch (Exception e) {
			logger.error("Failed to process the operation, message" + e.getMessage(), e);
		}

	}

	/**
	 * Process QueryMessage from Adapter
	 * 
	 * @param queryMessage
	 * @param token
	 */
	@Async
	public void processRequest(QueryMessage queryMessage, String token) {
		try {
			String url = getSubmitUrl((queryMessage.getMessageType()).toString());
			if (url == null) {
				logger.error("Microservice url is not set for respective message type. Message type: "
						+ queryMessage.getMessageType());
				return;
			}
			String finalUrl = url
					+ getUrlParam((queryMessage.getMessageType()).toString(), queryMessage.getSendingApplicationName());

			String messageJson = objectMapper.writeValueAsString(queryMessage);
			logger.info("Query message: " + messageJson);
			postRequest(finalUrl, Entity.entity(queryMessage, MediaType.APPLICATION_JSON), token);
		} catch (Exception e) {
			logger.error("Failed to process the operation, message" + e.getMessage(), e);
		}
	}

	/**
	 * Process LP sequence request.
	 *
	 * @param specimenStatusUpdateMessage the specimen status update message
	 */
	public void processLPSequenceRequest(SpecimenStatusUpdateMessage specimenStatusUpdateMessage) {
		AcknowledgementMessage ackMessage = new AcknowledgementMessage();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);

		ackMessage.setContainerId(specimenStatusUpdateMessage.getContainerId());
		ackMessage.setDateTimeMessageGenerated(dateFormatter.format(Calendar.getInstance().getTime()));
		ackMessage.setDeviceSerialNumber(specimenStatusUpdateMessage.getDeviceSerialNumber());
		ackMessage.setMessageType(EnumMessageType.AcknowledgementMessage);
		ackMessage.setReceivingApplication(specimenStatusUpdateMessage.getSendingApplicationName());
		ackMessage.setSendingApplicationName(specimenStatusUpdateMessage.getReceivingApplication());
		ackMessage.setMessageControlId(specimenStatusUpdateMessage.getMessageControlId());
		ackMessage.setStatus("SUCCESS");
		submitResponse(ackMessage);
	}

	public void processNoOrderFoundResponse(AdaptorRequestMessage adaptorRequestMessage, String responseStatus) {
		logger.info("Entering processNoOrderFoundResponse()" + adaptorRequestMessage.toString());
		AdaptorResponseMessage adaptorResponseMessage = new AdaptorResponseMessage();
		com.roche.connect.common.mp24.message.Response response = new com.roche.connect.common.mp24.message.Response();
		response.setMessageControlId(adaptorRequestMessage.getMessageControlId());
		response.setAccessioningId(adaptorRequestMessage.getAccessioningId());
		response.setMessageType(MessageType.RSP);
		RSPMessage rspMessage = new RSPMessage();
		SampleInfo sampleInfo = new SampleInfo();
		rspMessage.setOrderControl("DC");
		rspMessage.setSampleInfo(sampleInfo);
		rspMessage.setQueryResponseStatus(responseStatus);
		response.setRspMessage(rspMessage);
		adaptorResponseMessage.setStatus(IMMConstants.NOORDER);
		adaptorResponseMessage.setResponse(response);
		submitResponse(adaptorResponseMessage);
		logger.info("Processed processNoOrderFoundResponse()" + adaptorResponseMessage.toString());
	}

	/**
	 * Process LP sequence request.
	 *
	 * @param queryMessage the query message
	 * @param orderid
	 */
	public void processLPSequenceRequestOrAssayMismatch(QueryMessage queryMessage, long orderId, String protocolName,
			String status) {
		OrderDTO order = null;
		if (orderId != 0) {
			try {
				order = ommService.getOrderByOrderId(orderId);
			} catch (HMTPException e1) {
				logger.error("Failed to get the order details from OMM " + e1.getMessage(), e1);
			}
		}
		SimpleDateFormat dateFormatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(status);
		responseMessage.setContainerId(queryMessage.getContainerId());
		responseMessage.setAccessioningId(getNext());
		responseMessage.setDateTimeMessageGenerated(dateFormatter.format(Calendar.getInstance().getTime()));
		responseMessage.setDeviceSerialNumber(queryMessage.getDeviceSerialNumber());
		responseMessage.setMessageType(EnumMessageType.ResponseMessage);
		responseMessage.setReceivingApplication(queryMessage.getSendingApplicationName());
		responseMessage.setSendingApplicationName(queryMessage.getReceivingApplication());
		responseMessage.setMessageControlId(queryMessage.getMessageControlId());
		if (order != null && (!status.equalsIgnoreCase(IMMConstants.NOORDER)
				|| !status.equalsIgnoreCase(IMMConstants.DUPLICATE))) {
			responseMessage.setSampleType(order.getSampleType());
			responseMessage.setSpecimenDescription(order.getSampleType());
		} else {
			responseMessage.setSampleType("");
			responseMessage.setSpecimenDescription("");
		}

		responseMessage.setProtocolName(protocolName);

		if (status.equalsIgnoreCase(IMMConstants.NOORDER) || status.equalsIgnoreCase(IMMConstants.DUPLICATE)) {
			responseMessage.setOrderControl("DC");
		} else {
			responseMessage.setOrderControl("NW");
		}

		try {
			logger.info("Inside k :" + responseMessage.getDeviceSerialNumber());
			messageService.saveMessage(responseMessage);
			submitResponse(responseMessage);

		} catch (Exception e) {
			logger.error("Failed to handle RSP WFM request, message: " + e.getMessage(), e);
		}
	}

	/**
	 * Submit response.
	 *
	 * @param responseMessage the response message
	 */
	public void submitResponse(ResponseMessage responseMessage) {
		try {
			logger.info("submitResponse():" + responseMessage.getAccessioningId() + " "
					+ responseMessage.getContainerId() + " " + responseMessage.getMessageType());
			String messageJson = objectMapper.writeValueAsString(responseMessage);
			logger.info("ResponseMessage: " + messageJson);
			String adaptorSubmitUrl = getAdaptorSubmitUrl((responseMessage.getMessageType()).toString());

			Builder builder = RestClientUtil.getBuilder(adaptorSubmitUrl, null);
			Response resp = builder.post(Entity.entity(responseMessage, MediaType.APPLICATION_JSON));
			logger.info("Response status from WFM/Adaptor: " + resp.getStatus());

		} catch (Exception e) {
			logger.error("Failed to submit ResponseMessage to adaptor, message" + e.getMessage(), e);
		}

	}

	/**
	 * Submit response.
	 *
	 * @param acknowledgementMessage the acknowledgement message
	 */
	public void submitResponse(AcknowledgementMessage acknowledgementMessage) {
		try {
			String messageJson = objectMapper.writeValueAsString(acknowledgementMessage);
			logger.info("AcknowledgementMessage: " + messageJson);
			String adaptorSubmitUrl = getAdaptorSubmitUrl((acknowledgementMessage.getMessageType()).toString());
			RestClientUtil.postMethodCall(adaptorSubmitUrl, acknowledgementMessage, null);
		} catch (Exception e) {
			logger.error("Failed to submit response to adaptor, message" + e.getMessage(), e);
		}

	}

	/**
	 * Submit response.
	 *
	 * @param acknowledgementMessage the acknowledgement message
	 */
	public void submitResponse(AdaptorRequestMessage acknowledgementMessage) {
		try {
			String messageJson = objectMapper.writeValueAsString(acknowledgementMessage);
			logger.info("AcknowledgementMessage: " + messageJson);
			String adaptorSubmitUrl = getAdaptorSubmitUrl(acknowledgementMessage.getMessageType());
			RestClientUtil.postMethodCall(adaptorSubmitUrl, acknowledgementMessage, null);
		} catch (Exception e) {
			logger.error("Failed to submit response to adaptor, message" + e.getMessage(), e);
		}

	}

	/**
	 * Save run result.
	 *
	 * @param runResult the run result
	 */
	public void saveRunResult(RunResultsDTO runResult) {
		runResult.setAssayType("NIPTHTP");
		logger.info("VO From Adapter to IMM is: --------" + runResult);
		Response res = RestClientUtil.postOrPutBuilder(rmmRunResultUrl, null)
				.post(Entity.entity(runResult, MediaType.APPLICATION_JSON));
		logger.info("Result message for posting VO is: " + res.getStatus());
	}

	/**
	 * Gets the run result.
	 *
	 * @param htpStatus the htp status
	 * @return the run result
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public RunResultsDTO getRunResult(HtpStatus htpStatus) throws UnsupportedEncodingException {
		logger.info("getRunResult DeviceRunID::::--------" + htpStatus.getDeviceRunId());
		String url = rmmRunResultByDeviceIdUrl + IMMConstants.DEVICE_RUN_ID1 + htpStatus.getDeviceRunId();
		Invocation.Builder responseClient = RestClientUtil
				.getBuilder(URLEncoder.encode(url, NIPTProcessStepConstants.CHAR_SET), null);
		RunResultsDTO runResultsDTO = responseClient.get(new GenericType<RunResultsDTO>() {
		});

		logger.info("RunResult data from RMM : " + runResultsDTO.toString());
		return runResultsDTO;

	}

	/**
	 * Gets the sample results.
	 *
	 * @param complexId   the complex id
	 * @param processStep the process step
	 * @return the sample results
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public List<SampleResultsDTO> getSampleResultsByProcessStepAndOutputContainerId(String processStep,
			String complexId) throws UnsupportedEncodingException {

		logger.info("complexId is:--------" + complexId + " :ProcessStepName is:-----" + processStep);

		if (rmmGetSampleResult == null) {
			logger.error("WFM Microservice url is not set for RMM SampleResult by OutputContainor id.");
			return Collections.emptyList();
		}

		String url = rmmGetSampleResult + IMMConstants.OUTPUT_CONTAINER_ID1 + complexId + IMMConstants.PROCESS_STEPNAME2
				+ processStep;
		logger.info("URL to Sample Results from RMM : " + url);

		return rmmService.getSampleResultsFromUrl(url);
	}

	public List<SampleResultsDTO> getSampleResultsByProcessStepAndOutputContainerIdAndPosition(String processStep,
			String complexId, String position) throws UnsupportedEncodingException {

		logger.info("complexId is:--------" + complexId + " :  ProcessStep is:------" + processStep);

		if (rmmGetSampleResult == null) {
			logger.error("WFM Microservice url is not set for RMM SampleResult by OutputContainor id.");
			return Collections.emptyList();
		}

		String url = rmmGetSampleResult + IMMConstants.OUTPUT_CONTAINER_ID1 + complexId + IMMConstants.PROCESS_STEPNAME2
				+ processStep + IMMConstants.OUTPUT_CONTAINER_POSITION2 + position;
		logger.info("URL to Sample Results from RMM ByProcessStepAndOutputContainerIdAndPosition: " + url);

		return rmmService.getSampleResultsFromUrl(url);
	}

	/**
	 * Gets the sample results using processStep and inputContainerId.
	 * 
	 * @param processStep
	 * @param inputContainerId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public List<SampleResultsDTO> getSampleResultsByProcessStepAndInputContainerId(String processStep,
			String inputContainerId) throws UnsupportedEncodingException {
		logger.info("MessageProcessorService.getSampleResultsByProcessStepAndInputContainerId: " + processStep,
				inputContainerId);

		if (rmmGetSampleResult == null) {
			logger.error(
					"RMM Microservice url to get sample results using process step and input container id is missing.");
			return Collections.emptyList();
		}

		String url = rmmGetSampleResult + IMMConstants.PROCESS_STEPNAME1 + processStep
				+ IMMConstants.INPUT_CONTAINER_ID2 + inputContainerId;
		logger.info("URL to Sample Results from RMM ByProcessStepAndInputContainerId: " + url);
		return rmmService.getSampleResultsFromUrl(url);

	}

	public List<SampleResultsDTO> getSampleResultsByProcessStepAndInputContainerIdAndInputContainerPosition(
			String processStep, String inputContainerId, String inputContainerPosition)
			throws UnsupportedEncodingException {
		logger.info(
				"MessageProcessorService.getSampleResultsByProcessStepAndInputContainerIdAndInputContainerPosition: "
						+ processStep,
				inputContainerId, inputContainerPosition);

		if (rmmGetSampleResult == null) {
			logger.error(
					"RMM Microservice url to get sample results using process step and input container id and input Container Position is missing.");
			return Collections.emptyList();
		}

		String url = rmmGetSampleResult + IMMConstants.PROCESS_STEPNAME1 + processStep
				+ IMMConstants.INPUT_CONTAINER_ID2 + inputContainerId + IMMConstants.INPUT_CONTAINER_POSITION2
				+ inputContainerPosition;
		logger.info("URL to Sample Results from RMM ByProcessStepAndInputContainerIdAndInputContainerPosition:: " + url);
		return rmmService.getSampleResultsFromUrl(url);

	}

	/**
	 * Gets the run result by device id and process step name.
	 *
	 * @param deviceId        the device id
	 * @param processStepName the process step name
	 * @return the run result by device id and process step name
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public RunResultsDTO getRunResultByDeviceIdAndProcessStepName(String deviceId, String processStepName)
			throws UnsupportedEncodingException {
		logger.info("deviceId is:--------" + deviceId + "processStepName is:--" + processStepName);
		if (rmmRunResultUrl == null) {
			logger.error("WFM Microservice URL is not set for HTP StatusMessage::getRunResultByDeviceIdAndProcessStepName");
			return null;
		}

		String url = rmmRunResultUrl + IMMConstants.DEVICE_ID1 + deviceId + IMMConstants.PROCESS_STEPNAME2
				+ processStepName;
		logger.info("URL: getRunResultByDeviceIdAndProcessStepName" + url);

		Invocation.Builder responseClient = RestClientUtil
				.getBuilder(URLEncoder.encode(url, NIPTProcessStepConstants.CHAR_SET), null);

		Response response = responseClient.get();

		RunResultsDTO runResultsDTO = null;

		if (response.getStatus() == 200) {
			runResultsDTO = response.readEntity(new GenericType<RunResultsDTO>() {
			});
		} else if (response.getStatus() == 400) {
			logger.info(
					"Run result not found in RMM: for deviceId: " + deviceId + " &processStepName" + processStepName);
		}

		return runResultsDTO;
	}

	/**
	 * Gets the run result.
	 *
	 * @param deviceRunId     the device run id
	 * @param processStepName the process step name
	 * @return the run result
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public RunResultsDTO getRunResult(String deviceRunId, String processStepName) throws UnsupportedEncodingException {
		logger.info("deviceRunId is:--" + deviceRunId + " :  processStepName getRunResult is:--------" + processStepName);
		if (rmmRunResultUrl == null) {
			logger.error("getRunResult:::WFM Microservice url is not set for HTP StatusMessage");
			return null;
		}

		String url = rmmSampleResultUrl + IMMConstants.DEVICE_RUN_ID1 + deviceRunId + IMMConstants.PROCESS_STEPNAME2
				+ processStepName;
		logger.info("URL: getRunResult:: " + url);
		Invocation.Builder responseClient = RestClientUtil
				.getBuilder(URLEncoder.encode(url, NIPTProcessStepConstants.CHAR_SET), null);

		Response response = responseClient.get();

		RunResultsDTO runResultsDTO = null;

		if (response.getStatus() == 200) {
			runResultsDTO = response.readEntity(new GenericType<RunResultsDTO>() {
			});
			logger.info("RunResult data from RMM:" + runResultsDTO.toString());
		} else if (response.getStatus() == 400) {
			logger.info("Run result not found in RMM: for deviceRunId: " + deviceRunId + "Process Step Name"
					+ processStepName);
		}

		return runResultsDTO;
	}

	/**
	 * Gets the run result.
	 *
	 * @param deviceRunId     the device run id
	 * @param processStepName the process step name
	 * @return the run result
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public RunResultsDTO getRunResult(String deviceRunId, String processStepName, String deviceSerialNumber)
			throws UnsupportedEncodingException {
		logger.info("deviceRunId is:----getRunResult----" + deviceRunId + " :  processStepName is:----getRunResult----" + processStepName);
		if (rmmRunResultUrl == null) {
			logger.error("WFM Microservice url is not set for HTP StatusMessage for getRunResult::");
			return null;
		}

		String url = rmmSampleResultUrl + IMMConstants.DEVICE_RUN_ID1 + deviceRunId + IMMConstants.PROCESS_STEPNAME2
				+ processStepName + IMMConstants.DEVICE_SERIAL_NUM2 + deviceSerialNumber;
		logger.info("getRunResult URL: " + url);
		Invocation.Builder responseClient = RestClientUtil
				.getBuilder(URLEncoder.encode(url, NIPTProcessStepConstants.CHAR_SET), null);

		Response response = responseClient.get();

		RunResultsDTO runResultsDTO = null;

		if (response.getStatus() == 200) {
			runResultsDTO = response.readEntity(new GenericType<RunResultsDTO>() {
			});
			logger.info("RunResult data from RMM:" + runResultsDTO.toString());
		} else if (response.getStatus() == 400) {
			logger.info("Run result not found in RMM: for deviceRunId: " + deviceRunId + ":processStepName:"
					+ processStepName);
		}

		return runResultsDTO;
	}

	/**
	 * Submit htp sample status.
	 *
	 * @param htpSampleStatus the htp sample status
	 * @param token           the token
	 */
	@Async
	public void submitHtpSampleStatus(List<HtpStatusMessage> htpSampleStatus, String token) {

		logger.info("During Call from IMM to WFM,list is  :\n"
				+ Arrays.toString(htpSampleStatus.toArray(new HtpStatusMessage[] {})));
		if (wfmUpdateRequestUrl == null) {
			logger.error("WFM Microservice url is not set for HTP StatusMessage::submitHtpSampleStatus::");
			return;
		}
		String url = wfmUpdateRequestUrl + UrlConstants.HTP_STATUS;
		logger.info("URL FOR WFM is: " + url);
		htpSampleStatus.stream().forEach(s -> {
			try {
				postRequest(url, Entity.entity(s, MediaType.APPLICATION_JSON), token);
			} catch (Exception e) {
				logger.error("RunResult data from RMM: " + e.getMessage());
			}
		});

	}

	/**
	 * Process lp seq request.
	 *
	 * @param queryMessage the query message
	 * @param orderid
	 * @param token        the token
	 */
	public void processLpSeqRequest(QueryMessage queryMessage, long orderId, String token) {

		List<SampleResultsDTO> sampleResults = new ArrayList<>();
		String protocolName = null;
		try {
			List<ProcessStepActionDTO> processStepList = assayService.getProcessStepAction(AssayType.NIPT_HTP,
					DeviceType.LP24);
			List<DeviceTestOptionsDTO> deviceTestOptions = assayService.getTestOptionsByOrderIdandDeviceandProcessStep(
					AssayType.NIPT_HTP, queryMessage.getSendingApplicationName(), queryMessage.getProcessStepName());
			protocolName = deviceTestOptions.get(0).getTestProtocol();

			Optional<ProcessStepActionDTO> findProcessStep = processStepList.stream()
					.filter(e -> e.getProcessStepSeq().equals(3)).findFirst();
			String lpPostProcessStepName = findProcessStep.isPresent() ? findProcessStep.get().getProcessStepName()
					: null;

			String containerId = queryMessage.getContainerId() != null ? queryMessage.getContainerId().split("_")[0]
					: null;

			String position = queryMessage.getContainerId() != null ? queryMessage.getContainerId().split("_")[1]
					: null;

			if (StringUtils.isEmpty(lpPostProcessStepName)) {
				throw new HMTPException("LP Post Process step name is Empty");
			} else {
				sampleResults = getSampleResultsByProcessStepAndOutputContainerIdAndPosition(lpPostProcessStepName,
						containerId, position);

				if (!sampleResults.isEmpty()) {

					List<SampleResultsDTO> sampleResults1 = getSampleResultsByProcessStepAndInputContainerIdAndInputContainerPosition(
							queryMessage.getProcessStepName(), containerId, position);
					if (sampleResults1.isEmpty()) {
						if (sampleResults.stream()
								.anyMatch(e -> e.getStatus().equalsIgnoreCase(SampleStatus.COMPLETED.getText()))) {
							sampleResults.forEach(e -> {
								queryMessage.setOrderId(e.getOrderId());
								queryMessage.setAccessioningId(e.getAccesssioningId());

								processRequest(queryMessage, token);
							});

							processLPSequenceRequestOrAssayMismatch(queryMessage, orderId, protocolName,
									IMMConstants.SUCCESS);
						} else {
							logger.info("Sending Notification to ADM for No Order Found");
							List<String> content = new ArrayList<>();
							content.add(containerId);
							content.add(queryMessage.getDeviceSerialNumber());
							processLPSequenceRequestOrAssayMismatch(queryMessage, orderId, protocolName,
									IMMConstants.NOORDER);
							AdmNotificationService.sendNotification(NotificationGroupType.NO_ORDER_FOUND_LP24, content,
									token,admHostUrl+UrlConstants.ADM_NOTIFICATION_URL);

						}
					} else {
						processLPSequenceRequestOrAssayMismatch(queryMessage, orderId, protocolName,
								IMMConstants.DUPLICATE);
					}
				} else {
					processLPSequenceRequestOrAssayMismatch(queryMessage, orderId, protocolName, IMMConstants.NOORDER);
				}
			}
		} catch (Exception e) {
			processLPSequenceRequestOrAssayMismatch(queryMessage, orderId, protocolName, IMMConstants.NOORDER);
			logger.info(
					"Something went wrong during MessageProcessorService.processLpSeqRequest\"\r\n" + e.getMessage());
		}

	}

	/**
	 * Process lp seq request.
	 *
	 * @param specimenStatusUpdateMessage the specimen status update message
	 * @param token                       the token
	 * @throws HMTPException
	 * @throws InvalidStateException
	 * @throws UnsupportedEncodingException
	 */
	public void processLpSeqRequest(SpecimenStatusUpdateMessage specimenStatusUpdateMessage, String token)
			throws HMTPException, InvalidStateException, UnsupportedEncodingException {

		List<ProcessStepActionDTO> processStepList = assayService.getProcessStepAction(AssayType.NIPT_HTP,
				NiptDeviceType.LP24.getText());

		final RunResultsDTO runResultDTO = getRunResultForSSU(specimenStatusUpdateMessage);

		if (runResultDTO != null) {

			if (specimenStatusUpdateMessage.getContainerId() != null) {
				try {
					String containerId = specimenStatusUpdateMessage.getContainerId() != null
							? specimenStatusUpdateMessage.getContainerId().split("_")[0]
							: null;

					String position = specimenStatusUpdateMessage.getContainerId() != null
							? specimenStatusUpdateMessage.getContainerId().split("_")[1]
							: null;

					Optional<ProcessStepActionDTO> firstProcessStep = processStepList.stream()
							.filter(e -> e.getProcessStepSeq().equals(3)).findFirst();
					String lpPostProcessStepName = firstProcessStep.isPresent()
							? firstProcessStep.get().getProcessStepName()
							: null;

					List<SampleResultsDTO> sampleResults = getSampleResultsByProcessStepAndOutputContainerIdAndPosition(
							lpPostProcessStepName, containerId, position);

					sampleResults.forEach(e -> {
						specimenStatusUpdateMessage.setRunResultsId(runResultDTO.getRunResultId());
						specimenStatusUpdateMessage.setOrderId(e.getOrderId());
						specimenStatusUpdateMessage.setAccessioningId(e.getAccesssioningId());
						processRequest(specimenStatusUpdateMessage, token);
					});
					processLPSequenceRequest(specimenStatusUpdateMessage);
				} catch (UnsupportedEncodingException e) {
					logger.info(
							"Something went wrong during MessageProcessorService.processLpSeqRequest" + e.getMessage());
				}
			}
		} else {
			logger.info("Something went wrong during MessageProcessorService.processLpSeqRequest Run Result are null");
		}
	}

	/**
	 * Creates the run result from QM.
	 *
	 * @param queryMessage the query message
	 * @return the run results DTO
	 */
	public RunResultsDTO createRunResultFromQM(QueryMessage queryMessage) {

		List<ProcessStepActionDTO> processStepList = assayService.getProcessStepAction(AssayType.NIPT_HTP,
				NiptDeviceType.LP24.getText());

		try {
			RunResultsDTO prevRunResults = getRunResultByDeviceIdAndProcessStepName(
					queryMessage.getDeviceSerialNumber(),
					processStepList.size() >= 3 ? processStepList.get(2).getProcessStepName() : null);

			if (prevRunResults == null) {
				logger.info("Run Result not exit");
				RunResultsDTO runResultsDTO = new RunResultsDTO();
				runResultsDTO.setDeviceId(queryMessage.getDeviceSerialNumber());
				runResultsDTO.setProcessStepName(
						processStepList.size() >= 3 ? processStepList.get(2).getProcessStepName() : null);
				runResultsDTO.setRunStatus(NIPTProcessStepConstants.IN_PROGRESS);
				runResultsDTO.setAssayType("NIPTHTP");

				saveRunResult(runResultsDTO);

				return getRunResultByDeviceIdAndProcessStepName(queryMessage.getDeviceSerialNumber(),
						processStepList.size() >= 3 ? processStepList.get(2).getProcessStepName() : null);

			} else {
				logger.info("Run Result already exit re using the run result:" + prevRunResults.getRunResultId());
				return prevRunResults;
			}

		} catch (UnsupportedEncodingException e) {
			logger.info("Exception occurred while processing MessageProcessorService.createRunResultFromQM: "
					+ e.getMessage());
		}

		return null;

	}

	public void checkIsValidStatusForSSU(RunResultsDTO runResultsDTO,
			SpecimenStatusUpdateMessage specimenStatusUpdateMessage) throws HMTPException, InvalidStateException {

		Optional<SampleResultsDTO> optionalSR = runResultsDTO.getSampleResults().stream().findFirst();

		SampleResultsDTO sampleResultDto = null;

		if (optionalSR.isPresent())
			sampleResultDto = optionalSR.get();
		else
			throw new HMTPException("No Samples mapped to the Run Results in RMM");

		String status = Optional.ofNullable(specimenStatusUpdateMessage.getStatusUpdate().getOrderResult())
				.orElseThrow(() -> new HMTPException("Status is not a valid one"));

		if (!SampleStatus.INPROGRESS.getText().equalsIgnoreCase(sampleResultDto.getResult())) {
			if (!status.equalsIgnoreCase(sampleResultDto.getResult())) {
				String token = (String) ThreadSessionManager.currentUserSession()
						.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
				List<String> content = new ArrayList<>();
				content.add(specimenStatusUpdateMessage.getDeviceSerialNumber());
				content.add(sampleResultDto.getAccesssioningId());
				AdmNotificationService.sendNotification(NotificationGroupType.MUL_MSG_DIFF_STATUS_LP24, content, token,admHostUrl+UrlConstants.ADM_NOTIFICATION_URL);
				throw new InvalidStateException("Invalid State Message");
			}
		}
	}

	public RunResultsDTO getRunResultForSSU(SpecimenStatusUpdateMessage specimenStatusUpdateMessage)
			throws HMTPException, InvalidStateException, UnsupportedEncodingException {

		List<ProcessStepActionDTO> processStepList = assayService.getProcessStepAction(AssayType.NIPT_HTP,
				NiptDeviceType.LP24.getText());

		RunResultsDTO runResultsDTO;

		try {

			Optional<ProcessStepActionDTO> firstProcessStep = processStepList.stream()
					.filter(e -> e.getProcessStepSeq().equals(4)).findFirst();

			String lpSeqProcessStepName = firstProcessStep.isPresent() ? firstProcessStep.get().getProcessStepName()
					: null;

			if (StringUtils.isEmpty(lpSeqProcessStepName)) {
				throw new HMTPException("LP Seq Process step name is empty");
			} else {

				runResultsDTO = getRunResult(specimenStatusUpdateMessage.getStatusUpdate().getOrderName(),
						lpSeqProcessStepName, specimenStatusUpdateMessage.getDeviceSerialNumber());

				if (runResultsDTO == null) {
					runResultsDTO = new RunResultsDTO();
				}

				if (!StringUtils.isEmpty(runResultsDTO.getRunStatus())) {
					checkIsValidStatusForSSU(runResultsDTO, specimenStatusUpdateMessage);
				}

				if (specimenStatusUpdateMessage.getStatusUpdate().getRunResult()
						.equalsIgnoreCase(SampleStatus.ABORTED.getText()))
					runResultsDTO.setRunStatus(SampleStatus.ABORTED.getText());
				else if (specimenStatusUpdateMessage.getStatusUpdate().getRunResult()
						.equalsIgnoreCase(SampleStatus.INPROGRESS.getText()))
					runResultsDTO.setRunStatus(SampleStatus.INPROGRESS.getText());
				else if (specimenStatusUpdateMessage.getStatusUpdate().getRunResult()
						.equalsIgnoreCase(SampleStatus.PASSED.getText()))
					runResultsDTO.setRunStatus(SampleStatus.COMPLETED.getText());
				else if (specimenStatusUpdateMessage.getStatusUpdate().getRunResult()
						.equalsIgnoreCase(SampleStatus.PASSEDWITHFLAG.getText()))
					runResultsDTO.setRunStatus(SampleStatus.COMPLETED_WITH_FLAGS.getText());
				else
					throw new HMTPException("Run status is not a valid one");

				runResultsDTO.setDeviceId(specimenStatusUpdateMessage.getDeviceSerialNumber());
				runResultsDTO.setDeviceRunId(specimenStatusUpdateMessage.getStatusUpdate().getOrderName());
				runResultsDTO.setProcessStepName(
						processStepList.size() >= 3 ? processStepList.get(2).getProcessStepName() : null);
				runResultsDTO.setAssayType(AssayType.NIPT_HTP);
				runResultsDTO.setRunStartTime(specimenStatusUpdateMessage.getStatusUpdate().getRunStartTime());
				runResultsDTO.setOperatorName(specimenStatusUpdateMessage.getStatusUpdate().getOperatorName());
				runResultsDTO.setUpdatedBy(specimenStatusUpdateMessage.getStatusUpdate().getUpdatedBy());
				runResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
				runResultsDTO.setComments(specimenStatusUpdateMessage.getStatusUpdate().getComment());

				if (runResultsDTO.getRunStatus().equalsIgnoreCase(SampleStatus.COMPLETED.getText())
						|| runResultsDTO.getRunStatus().equalsIgnoreCase(SampleStatus.COMPLETED_WITH_FLAGS.getText())) {
					runResultsDTO.setRunCompletionTime(specimenStatusUpdateMessage.getStatusUpdate().getRunEndTime());

					RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = null;

					if (!specimenStatusUpdateMessage.getStatusUpdate().getConsumables().isEmpty()) {
						for (Consumable consumable : specimenStatusUpdateMessage.getStatusUpdate().getConsumables()) {
							runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
							runReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
							runReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
							runResultsDTO.getRunReagentsAndConsumables().add(runReagentsAndConsumablesDTO);
						}
					}
				}

				saveRunResult(runResultsDTO);

				return getRunResult(specimenStatusUpdateMessage.getStatusUpdate().getOrderName(), lpSeqProcessStepName);
			}
		} catch (UnsupportedEncodingException e) {
			logger.info("UnsupportedEncodingException " + e.getMessage());
			throw e;
		} catch (InvalidStateException e1) {
			logger.info("InvalidStateException " + e1.getMessage());
			processNegativeLPSequenceRequest(specimenStatusUpdateMessage);
			throw e1;
		} catch (Exception e2) {
			logger.info("Exception " + e2.getMessage());
			throw e2;
		}
	}

	/**
	 * Update run result from SSU.
	 *
	 * @param runResultsDTO               the run results DTO
	 * @param specimenStatusUpdateMessage the specimen status update message
	 * @return the run results DTO
	 */
	public RunResultsDTO updateRunResultFromSSU(RunResultsDTO runResultsDTO,
			SpecimenStatusUpdateMessage specimenStatusUpdateMessage) {

		String orderStatus = "";

		if (!StringUtils.isEmpty(specimenStatusUpdateMessage.getStatusUpdate())) {
			orderStatus = specimenStatusUpdateMessage.getStatusUpdate().getOrderResult();
		}

		if (orderStatus.equalsIgnoreCase(SampleStatus.ABORTED.getText()))
			runResultsDTO.setRunStatus(SampleStatus.ABORTED.getText());
		else if (orderStatus.equalsIgnoreCase(SampleStatus.PASSEDWITHFLAG.getText()))
			runResultsDTO.setRunStatus(SampleStatus.COMPLETED_WITH_FLAGS.getText());
		else
			runResultsDTO.setRunStatus(SampleStatus.COMPLETED.getText());

		RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = null;

		runResultsDTO.setOperatorName(specimenStatusUpdateMessage.getStatusUpdate().getOperatorName());
		runResultsDTO.setRunStartTime(specimenStatusUpdateMessage.getStatusUpdate().getRunStartTime());
		runResultsDTO.setRunCompletionTime(specimenStatusUpdateMessage.getStatusUpdate().getRunEndTime());
		runResultsDTO.setUpdatedBy(specimenStatusUpdateMessage.getStatusUpdate().getUpdatedBy());
		runResultsDTO.setUpdatedDateTime(getCurrentUTCTimeStamp());
		runResultsDTO.setComments(specimenStatusUpdateMessage.getStatusUpdate().getComment());

		if (!specimenStatusUpdateMessage.getStatusUpdate().getConsumables().isEmpty()) {
			for (Consumable consumable : specimenStatusUpdateMessage.getStatusUpdate().getConsumables()) {
				runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
				runReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
				runReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
				runResultsDTO.getRunReagentsAndConsumables().add(runReagentsAndConsumablesDTO);
			}
		}

		return runResultsDTO;
	}

	/**
	 * Post request.
	 *
	 * @param url    the url
	 * @param entity the entity
	 * @param token  the token
	 */
	public void postRequest(String url, Entity entity, String token) {
		Builder builder = RestClientUtil.getBuilder(url, null);
		builder.header("Cookie", "brownstoneauthcookie=" + token);
		Response resp = builder.post(entity);
		logger.info("Response status from : " + resp.getStatus());
	}

	/**
	 * Gets the current UTC time stamp.
	 *
	 * @return the current UTC time stamp
	 */
	public Timestamp getCurrentUTCTimeStamp() {
		ZoneOffset zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
		TimeZone timezone = TimeZone.getTimeZone(zoneOffset);
		return new Timestamp((new Date().getTime()) - timezone.getOffset(new Date().getTime()));
	}

	public String getNext() {
		long timeSeed = System.nanoTime();
		double randSeed = Math.random() * 1000;
		long midSeed = (long) (timeSeed * randSeed);
		String s = midSeed + "";
		return s.substring(0, 9);
	}

	/**
	 * 
	 * @param assayTypeId
	 * @param deviceType
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public List<DeviceTestOptionsDTO> getListOfTestOptions(Long assayTypeId, String deviceType)
			throws UnsupportedEncodingException {
		List<DeviceTestOptionsDTO> listOfTestOptionsDTOs = null;
		String url = null;
		try {
			url = RestClientUtil.getUrlString(ammTestOptionsUrl + assayTypeId, "", "",
					"/devicetestoptions?deviceType=" + deviceType, null);

			Invocation.Builder assayClient = RestClientUtil
					.getBuilder(URLEncoder.encode(url, NIPTProcessStepConstants.CHAR_SET), null);
			listOfTestOptionsDTOs = assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {
			});
		} catch (HMTPException e) {
			logger.info("Something went wrong during MessageProcessorService.getListOfTestOptions" + e.getMessage());
		}
		return listOfTestOptionsDTOs;
	}

	/**
	 * 
	 * @param assayType
	 * @param deviceType
	 * @return
	 */
	public String getTestProtocolByByAssayTypeAndDeviceType(String assayType, String deviceType) {
		logger.info(
				"MessageProcessorService.getTestProtocolByByAssayTypeAndDeviceType: " + assayType + "," + deviceType);
		List<DeviceTestOptionsDTO> deviceTestOptions = assayService.getDeviceTestOptionsData(assayType, deviceType);
		String testProtocol = (!deviceTestOptions.isEmpty()) ? deviceTestOptions.get(0).getTestProtocol() : null;
		logger.info("Response from Test protocol: " + testProtocol);
		return testProtocol;
	}

	public ComplexIdDetailsDTO getComplexIdDetailsStatus(String complexId) throws HMTPException {

        logger.info("MessageProcessorService.getComplexIdStatus: " + complexId);

        ComplexIdDetailsDTO complexIdDetails = new ComplexIdDetailsDTO();

        ComplexIdDetailsStatus status = ComplexIdDetailsStatus.INVALID;

        try {
            
            logger.info("Fetching Process step list from AMM " + AssayType.NIPT_HTP);
            List<ProcessStepActionDTO> processStepList = assayService.getProcessStepAction(AssayType.NIPT_HTP, null);
            
            Optional<ProcessStepActionDTO> findFirst = processStepList.stream()
                .filter(e -> e.getDeviceType().equalsIgnoreCase(NiptDeviceType.HTP.getText())).findFirst();
            
            if (findFirst.isPresent()) {
                ProcessStepActionDTO htpProcessStep = Optional.ofNullable(findFirst.get())
                    .orElseThrow(() -> new HMTPException("Missing process step action for HTP"));
                
                Optional<ProcessStepActionDTO> findFirst2 = processStepList.stream()
                    .filter(e -> e.getProcessStepSeq() == htpProcessStep.getProcessStepSeq() - 1).findFirst();
                
                ProcessStepActionDTO lpSeqProcessStep = null;
                if (findFirst2.isPresent()) {
                    lpSeqProcessStep = findFirst2.get();
                } else {
                    throw new HMTPException("Missing process step action for LP seqPrep");
                }
                
                logger
                    .info("Response from AMM Process step action htpProcessStep:" + htpProcessStep.getProcessStepName()
                        + "\t lpSeqProcessStep: " + NIPTProcessStepConstants.LP24_SEQ_PREP);
                
                List<SampleResultsDTO> lpSeqSampleRestults =
                    getSampleResultsByProcessStepAndOutputContainerId(lpSeqProcessStep.getProcessStepName(), complexId);
                
                if (!lpSeqSampleRestults.isEmpty()) {
                    
                    RunResultsDTO lpRunResults =
                        rmmService.getRunResultsById(lpSeqSampleRestults.get(0).getRunResultsId());

					if (lpRunResults != null
							&& (lpRunResults.getRunStatus().equalsIgnoreCase(SampleStatus.COMPLETED.getText())
									|| lpRunResults.getRunStatus()
											.equalsIgnoreCase(SampleStatus.COMPLETED_WITH_FLAGS.getText()))) {

						complexIdDetails.setComplexCreatedDatetime(lpRunResults.getRunCompletionTime());

						List<SampleResultsDTO> seqSampleRestults = getSampleResultsByProcessStepAndInputContainerId(
								htpProcessStep.getProcessStepName(), complexId);

						status = !seqSampleRestults.isEmpty() ? ComplexIdDetailsStatus.USED
								: ComplexIdDetailsStatus.READY;

						if (status.equals(ComplexIdDetailsStatus.USED)) {
							String runProtocol = seqSampleRestults.get(0).getSampleProtocol().isEmpty()
									? null
									: seqSampleRestults.get(0).getSampleProtocol().iterator().next().getProtocolName();

							complexIdDetails.setRunProtocol(runProtocol);
						}
					}
                }
                
                complexIdDetails.setStatus(status);
                
            } else {
                throw new HMTPException("Missing process step action for HTP");
            }
            
        } catch (UnsupportedEncodingException e) {
            logger.info("UnsupportedEncodingException Exception occured on : " + e.getMessage());
        }

        return complexIdDetails;
    }

	public ComplexIdDetailsDTO getComplexIdDetailsByComplexId(String complexId) throws HMTPException {

		logger.info("MessageProcessorService.getComplexIdDetailsByComplexId: " + complexId);

		ComplexIdDetailsDTO complexIdDetails = null;

		try {
			complexIdDetails = getComplexIdDetailsStatus(complexId);

			if (complexIdDetails.getStatus().getText().equals(ComplexIdDetailsStatus.READY.getText())) {
				String runProtocol = getTestProtocolByByAssayTypeAndDeviceType(AssayType.NIPT_HTP,
						NiptDeviceType.HTP.getText());
				complexIdDetails.setRunProtocol(runProtocol);
			}

			logger.info(new ObjectMapper().writeValueAsString(complexIdDetails));
		} catch (JsonProcessingException e) {
			logger.info("Exception" + e.getMessage());
		}

		return complexIdDetails;
	}

    public void updateRunResults(RunResultsDTO runResultInfo) throws HMTPException {

        try {
            logger.info("MessageProcessorService.updateRunResults: " + objectMapper.writeValueAsString(runResultInfo));
            RunResultsDTO runResult = rmmService.getRunResultsByDeviceRunId(runResultInfo.getDeviceRunId());

            if (runResult != null) {
                int inc;
                logger.info("If Statment Run Result from RMM not null RunResultsDTO: "
                        + objectMapper.writeValueAsString(runResult));

                List<ProcessStepActionDTO> processStepList = assayService.getProcessStepAction(AssayType.NIPT_HTP,
                        null);

                Optional<ProcessStepActionDTO> findProcessStep = processStepList.stream()
                        .filter(e -> e.getDeviceType().equalsIgnoreCase(NiptDeviceType.HTP.getText())).findFirst();
                ProcessStepActionDTO htpProcessStep = findProcessStep.isPresent() ? findProcessStep.get() : null;

                if (!HTPStatusConstants.CREATED.getText().equalsIgnoreCase(runResultInfo.getRunStatus())
                        && null != runResultInfo.getRunStatus()) {
                    for (RunResultsDetailDTO runResultsDetailDTO1 : runResultInfo.getRunResultsDetail()) {
                        inc = 0;
                        for (RunResultsDetailDTO runResultsDetailDTO2 : runResult.getRunResultsDetail()) {
                            if (runResultsDetailDTO1.getAttributeName()
                                    .equalsIgnoreCase(runResultsDetailDTO2.getAttributeName())) {
                                runResultsDetailDTO2.setAttributeValue(runResultsDetailDTO1.getAttributeValue() != null
                                        ? runResultsDetailDTO1.getAttributeValue()
                                        : runResultsDetailDTO2.getAttributeValue());
                                runResultsDetailDTO2.setCreatedBy(runResultsDetailDTO1.getCreatedBy() != null
                                        ? runResultsDetailDTO1.getCreatedBy()
                                        : runResultsDetailDTO2.getCreatedBy());
                                runResultsDetailDTO2
                                        .setCreatedDateTime(runResultsDetailDTO1.getCreatedDateTime() != null
                                                ? runResultsDetailDTO1.getCreatedDateTime()
                                                : runResultsDetailDTO2.getCreatedDateTime());
                                runResultsDetailDTO2.setUpdatedBy(runResultsDetailDTO1.getUpdatedBy() != null
                                        ? runResultsDetailDTO1.getUpdatedBy()
                                        : runResultsDetailDTO2.getUpdatedBy());
                                runResultsDetailDTO2
                                        .setUpdatedDateTime(runResultsDetailDTO1.getUpdatedDateTime() != null
                                                ? runResultsDetailDTO1.getUpdatedDateTime()
                                                : runResultsDetailDTO2.getUpdatedDateTime());
                                runResultsDetailDTO2
                                        .setRunResultsDetailsId(runResultsDetailDTO1.getRunResultsDetailsId() != 0
                                                ? runResultsDetailDTO1.getRunResultsDetailsId()
                                                : runResultsDetailDTO2.getRunResultsDetailsId());
                            } else {
                                inc++;
                            }
                        }
                        if (inc == runResult.getRunResultsDetail().size()) {
                            runResult.getRunResultsDetail().add(runResultsDetailDTO1);
                        }
                    }
                    runResult.setRunRemainingTime(runResultInfo.getRunRemainingTime());
                } else {
                    runResult.setAssayType(runResultInfo.getAssayType() != null ? runResultInfo.getAssayType()
                            : runResult.getAssayType());
                    runResult.setComments(runResultInfo.getComments() != null ? runResultInfo.getComments()
                            : runResult.getComments());
                    runResult.setCreatedBy(runResultInfo.getCreatedBy() != null ? runResultInfo.getCreatedBy()
                            : runResult.getCreatedBy());
                    runResult.setCreatedDateTime(
                            runResultInfo.getCreatedDateTime() != null ? runResultInfo.getCreatedDateTime()
                                    : runResult.getCreatedDateTime());
                    runResult.setDeviceId(runResultInfo.getDeviceId() != null ? runResultInfo.getDeviceId()
                            : runResult.getDeviceId());
                    runResult.setDeviceRunId(runResultInfo.getDeviceRunId() != null ? runResultInfo.getDeviceRunId()
                            : runResult.getDeviceRunId());
                    runResult.setDvcRunResult(runResultInfo.getDvcRunResult() != null ? runResultInfo.getDvcRunResult()
                            : runResult.getDvcRunResult());
                    runResult.setOperatorName(runResultInfo.getOperatorName() != null ? runResultInfo.getOperatorName()
                            : runResult.getOperatorName());
                    runResult.setProcessStepName(
                            runResultInfo.getProcessStepName() != null ? runResultInfo.getProcessStepName()
                                    : runResult.getProcessStepName());
                    runResult.setRunCompletionTime(
                            runResultInfo.getRunCompletionTime() != null ? runResultInfo.getRunCompletionTime()
                                    : runResult.getRunCompletionTime());
                    runResult.setRunFlag(
                            runResultInfo.getRunFlag() != null ? runResultInfo.getRunFlag() : runResult.getRunFlag());
                    runResult.setRunRemainingTime(
                            runResultInfo.getRunRemainingTime() != null ? runResultInfo.getRunRemainingTime()
                                    : runResult.getRunRemainingTime());
                    runResult.setRunStartTime(runResultInfo.getRunStartTime() != null ? runResultInfo.getRunStartTime()
                            : runResult.getRunStartTime());
                    runResult.setRunStatus(runResultInfo.getRunStatus() != null ? runResultInfo.getRunStatus()
                            : runResult.getRunStatus());
                    runResult.setTotalSamplecount(
                            runResultInfo.getTotalSamplecount() != null ? runResultInfo.getTotalSamplecount()
                                    : runResult.getTotalSamplecount());
                    runResult.setUpdatedBy(runResultInfo.getUpdatedBy() != null ? runResultInfo.getUpdatedBy()
                            : runResult.getUpdatedBy());
                    runResult.setUpdatedDateTime(
                            runResultInfo.getUpdatedDateTime() != null ? runResultInfo.getUpdatedDateTime()
                                    : runResult.getUpdatedDateTime());
                    runResult.setVerifiedBy(runResultInfo.getVerifiedBy() != null ? runResultInfo.getVerifiedBy()
                            : runResult.getVerifiedBy());
                    runResult.setVerifiedDate(runResultInfo.getVerifiedDate() != null ? runResultInfo.getVerifiedDate()
                            : runResult.getVerifiedDate());
                    // For Run Reagents

                    for (RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO1 : runResultInfo
                            .getRunReagentsAndConsumables()) {
                        inc = 0;
                        for (RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO2 : runResult
                                .getRunReagentsAndConsumables()) {
                            if (runReagentsAndConsumablesDTO1.getAttributeName()
                                    .equalsIgnoreCase(runReagentsAndConsumablesDTO2.getAttributeName())) {
                                runReagentsAndConsumablesDTO2
                                        .setAttributeValue(runReagentsAndConsumablesDTO1.getAttributeValue() != null
                                                ? runReagentsAndConsumablesDTO1.getAttributeValue()
                                                : runReagentsAndConsumablesDTO2.getAttributeValue());
                                runReagentsAndConsumablesDTO2
                                        .setCreatedBy(runReagentsAndConsumablesDTO1.getCreatedBy() != null
                                                ? runReagentsAndConsumablesDTO1.getCreatedBy()
                                                : runReagentsAndConsumablesDTO2.getCreatedBy());
                                runReagentsAndConsumablesDTO2
                                        .setCreatedDateTime(runReagentsAndConsumablesDTO1.getCreatedDateTime() != null
                                                ? runReagentsAndConsumablesDTO1.getCreatedDateTime()
                                                : runReagentsAndConsumablesDTO2.getCreatedDateTime());
                                runReagentsAndConsumablesDTO2.setType(runReagentsAndConsumablesDTO1.getType() != null
                                        ? runReagentsAndConsumablesDTO1.getType()
                                        : runReagentsAndConsumablesDTO2.getType());
                                runReagentsAndConsumablesDTO2
                                        .setUpdatedBy(runReagentsAndConsumablesDTO1.getUpdatedBy() != null
                                                ? runReagentsAndConsumablesDTO1.getUpdatedBy()
                                                : runReagentsAndConsumablesDTO2.getUpdatedBy());
                                runReagentsAndConsumablesDTO2
                                        .setUpdatedDateTime(runReagentsAndConsumablesDTO1.getUpdatedDateTime() != null
                                                ? runReagentsAndConsumablesDTO1.getUpdatedDateTime()
                                                : runReagentsAndConsumablesDTO2.getUpdatedDateTime());
                                runReagentsAndConsumablesDTO2.setRunReagentsAndConsumablesId(
                                        runReagentsAndConsumablesDTO1.getRunReagentsAndConsumablesId() != 0
                                                ? runReagentsAndConsumablesDTO1.getRunReagentsAndConsumablesId()
                                                : runReagentsAndConsumablesDTO2.getRunReagentsAndConsumablesId());
                            } else {
                                inc++;
                            }
                        }
                        if (inc == runResult.getRunReagentsAndConsumables().size()) {
                            runResult.getRunReagentsAndConsumables().add(runReagentsAndConsumablesDTO1);
                        }
                    }

                    // For Run Details

                    for (RunResultsDetailDTO runResultsDetailDTO1 : runResultInfo.getRunResultsDetail()) {
                        inc = 0;
                        for (RunResultsDetailDTO runResultsDetailDTO2 : runResult.getRunResultsDetail()) {
                            if (runResultsDetailDTO1.getAttributeName()
                                    .equalsIgnoreCase(runResultsDetailDTO2.getAttributeName())) {
                                runResultsDetailDTO2.setAttributeValue(runResultsDetailDTO1.getAttributeValue() != null
                                        ? runResultsDetailDTO1.getAttributeValue()
                                        : runResultsDetailDTO2.getAttributeValue());
                                runResultsDetailDTO2.setCreatedBy(runResultsDetailDTO1.getCreatedBy() != null
                                        ? runResultsDetailDTO1.getCreatedBy()
                                        : runResultsDetailDTO2.getCreatedBy());
                                runResultsDetailDTO2
                                        .setCreatedDateTime(runResultsDetailDTO1.getCreatedDateTime() != null
                                                ? runResultsDetailDTO1.getCreatedDateTime()
                                                : runResultsDetailDTO2.getCreatedDateTime());
                                runResultsDetailDTO2.setUpdatedBy(runResultsDetailDTO1.getUpdatedBy() != null
                                        ? runResultsDetailDTO1.getUpdatedBy()
                                        : runResultsDetailDTO2.getUpdatedBy());
                                runResultsDetailDTO2
                                        .setUpdatedDateTime(runResultsDetailDTO1.getUpdatedDateTime() != null
                                                ? runResultsDetailDTO1.getUpdatedDateTime()
                                                : runResultsDetailDTO2.getUpdatedDateTime());
                                runResultsDetailDTO2
                                        .setRunResultsDetailsId(runResultsDetailDTO1.getRunResultsDetailsId() != 0
                                                ? runResultsDetailDTO1.getRunResultsDetailsId()
                                                : runResultsDetailDTO2.getRunResultsDetailsId());
                            } else {
                                inc++;
                            }
                        }
                        if (inc == runResult.getRunResultsDetail().size()) {
                            runResult.getRunResultsDetail().add(runResultsDetailDTO1);
                        }
                    }
                }
                runResult.setUpdatedBy(HTPConstants.UPDATED_BY);
                runResult.setUpdatedDateTime(Timestamp.from(Instant.now()));

                if (HTPStatusConstants.CREATED.getText().equals(runResultInfo.getRunStatus())) {

                    /*
                     * ProcessStepActionDTO lpSeqProcessStep = processStepList.parallelStream()
                     * .filter(e -> e.getProcessStepSeq() == htpProcessStep.getProcessStepSeq() -
                     * 1).findFirst() .get();
                     * 
                     * runResult.getSampleResults() .addAll(
                     * getSampleResultsByProcessStepAndOutputContainerId(
                     * lpSeqProcessStep.getProcessStepName(),
                     * runResultInfo.getRunResultsDetail().stream() .filter(e ->
                     * e.getAttributeName().equalsIgnoreCase("complexId"))
                     * .findFirst().get().getAttributeName()));
                     */
                    /*
                     * runResult.getSampleResults().addAll(
                     * getSampleResultsByProcessStepAndOutputContainerId(
                     * NIPTProcessStepConstants.LP24_SEQ_PREP,
                     * runResultInfo.getRunResultsDetail().stream() .filter(e ->
                     * e.getAttributeName().equalsIgnoreCase("complexId"))
                     * .findFirst().get().getAttributeName()));
                     */
                }

                if (!(HTPStatusConstants.CREATED.getText().equalsIgnoreCase(runResultInfo.getRunStatus()))
                        && null != runResultInfo.getRunStatus()) {

                    List<HtpStatusMessage> htpSampleStatusList = new ArrayList<>();

                    if (HTPStatusConstants.STARTED.getText().equals(runResultInfo.getRunStatus())) {

                        Optional<ProcessStepActionDTO> firstProcessStep = processStepList.parallelStream()
                                .filter(e -> e.getProcessStepSeq() == htpProcessStep.getProcessStepSeq() - 1)
                                .findFirst();
                        
                        ProcessStepActionDTO lpSeqProcessStep = null;
                        
                        if(firstProcessStep.isPresent()) {
                            lpSeqProcessStep = firstProcessStep.isPresent() ? firstProcessStep.get()
                                : null;
                        } else {
                            throw new HMTPException("Missing Process step action");
                        }
                        
                        Optional<RunResultsDetailDTO> findFirstComplexId = runResultInfo.getRunResultsDetail().stream()
                                .filter(e -> e.getAttributeName().equalsIgnoreCase("complexId"))
                                .findFirst();
                        
                        if(findFirstComplexId.isPresent()) {
                            runResult.getSampleResults()
                            .addAll(getSampleResultsByProcessStepAndOutputContainerId(
                                    lpSeqProcessStep.getProcessStepName(),
                                    findFirstComplexId.get().getAttributeValue()));
                        } else {
                            throw new HMTPException("ComplexId Missing in Run Result details");
                        }
                        

                    } else {
                        Optional<RunResultsDetailDTO> findFirstComplexId2 = runResultInfo.getRunResultsDetail().stream()
                        .filter(e -> e.getAttributeName().equalsIgnoreCase("complexId"))
                        .findFirst();
                        
                        if(findFirstComplexId2.isPresent()) {
                            runResult.getSampleResults()
                            .addAll(getSampleResultsByProcessStepAndInputContainerId(
                                    htpProcessStep.getProcessStepName(),
                                    findFirstComplexId2.get().getAttributeValue()));
                        } else {
                            throw new HMTPException("ComplexId Missing in Run Result details");
                        }
                        
                    }
                    List<SampleReagentsAndConsumablesDTO> sampleRCList = new ArrayList<>();

                    logger.info("Adding Reagents and Consumables in to sample results from run results");

                    runResult.getRunReagentsAndConsumables().forEach(e -> {
                        SampleReagentsAndConsumablesDTO reagentsAndConsumable = new SampleReagentsAndConsumablesDTO();
                        reagentsAndConsumable.setAttributeName(e.getAttributeName());
                        reagentsAndConsumable.setAttributeValue(e.getAttributeValue());
                        reagentsAndConsumable.setCreatedBy(e.getCreatedBy());
                        reagentsAndConsumable.setCreatedDateTime(e.getCreatedDateTime());
                        reagentsAndConsumable.setType(e.getType());
                        reagentsAndConsumable.setUpdatedBy(e.getUpdatedBy());
                        reagentsAndConsumable.setUpdatedDateTime(e.getUpdatedDateTime());
                        sampleRCList.add(reagentsAndConsumable);

                    });

                    for (SampleResultsDTO sampleResult : runResult.getSampleResults()) {

                        HtpStatusMessage htpStatusMessage = new HtpStatusMessage();

                        htpStatusMessage.setAccessioningId(sampleResult.getAccesssioningId());
                        htpStatusMessage.setStatus(runResultInfo.getRunStatus());
                        htpStatusMessage.setRunResultsId((runResult.getRunResultId()));

                        if (HTPStatusConstants.STARTED.getText().equals(runResultInfo.getRunStatus())) {
                            htpStatusMessage.setInputContainerId(sampleResult.getOutputContainerId());
                        } else {
                            htpStatusMessage.setInputContainerId(sampleResult.getInputContainerId());
                        }

                        htpStatusMessage.setOrderId(sampleResult.getOrderId());
                        htpStatusMessage.setDeviceRunId(runResult.getDeviceRunId());
                        htpStatusMessage.setDeviceId(runResult.getDeviceId());
                        htpStatusMessage.setDeviceName(NiptDeviceType.HTP.getText());
                        htpStatusMessage.setUpdatedBy(runResult.getUpdatedBy());
                        htpStatusMessage.setUpdatedDateTime(runResult.getUpdatedDateTime());

                        htpStatusMessage.setSendingApplication(NiptDeviceType.HTP.getText());
                        htpStatusMessage.setSendingFacility(null);

                        htpStatusMessage.setReceivingApplication(NiptDeviceType.HTP.getText());
                        htpStatusMessage.setReceivingFacility(null);
                        htpStatusMessage.setEstimatedTimeRemaining(143);
                        htpStatusMessage.setProcessStepName(htpProcessStep.getProcessStepName());
                        htpStatusMessage.setSampleReagentsAndConsumables(sampleRCList);
                        htpSampleStatusList.add(htpStatusMessage);
                    }

                    try {
                        logger.info("Updating sample results to WFM");

                        String token = (String) ThreadSessionManager.currentUserSession()
                                .getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
                        submitHtpSampleStatus(htpSampleStatusList, token);

                        logger.info("Updating sample results to WFM Completed");

                    } catch (Exception e) {
                        logger.error("Failed to submit HTP status to WFM, message" + e.getMessage(), e);
                    }

                }

				if (HTPStatusConstants.STARTED.getText().equalsIgnoreCase(runResultInfo.getRunStatus())
						|| HTPStatusConstants.IN_PROCESS.getText().equalsIgnoreCase(runResultInfo.getRunStatus()) || HTPStatusConstants.COMPLETED.getText().equalsIgnoreCase(runResultInfo.getRunStatus())) {
					runResult.setRunStatus(HTPStatusConstants.IN_PROCESS.getText());
				} else if (HTPStatusConstants.TRANSFERCOMPLETE.getText().equalsIgnoreCase(runResultInfo.getRunStatus())){
                    runResult.setRunStatus(HTPStatusConstants.COMPLETED.getText());
				}
				else{
					runResult.setRunStatus(runResultInfo.getRunStatus());
				}
                rmmService.updateRunResult(runResult);
                logger.info("Updating Run results to RMM Completed");
            } else {
                // RunID Incorrect or Not Available in RMM
                logger.info("RunResult not available for this RunId :" + runResultInfo.getDeviceRunId());
                throw new HMTPException("RunId not available :" + runResultInfo.getDeviceRunId());
            }
        } catch (JsonProcessingException e) {
            logger.info(
                    "Exception ocurred while processing MessageProcessorService.updateRunResults: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.info(
                    "UnsupportedEncodingException Exception ocurred while processing MessageProcessorService.updateRunResults: "
                            + e.getMessage());
        }
    }

	public void processNegativeLPSequenceRequest(SpecimenStatusUpdateMessage specimenStatusUpdateMessage) {
		AcknowledgementMessage ackMessage = new AcknowledgementMessage();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);

		ackMessage.setContainerId(specimenStatusUpdateMessage.getContainerId());
		ackMessage.setDateTimeMessageGenerated(dateFormatter.format(Calendar.getInstance().getTime()));
		ackMessage.setDeviceSerialNumber(specimenStatusUpdateMessage.getDeviceSerialNumber());
		ackMessage.setMessageType(EnumMessageType.AcknowledgementMessage);
		ackMessage.setReceivingApplication(specimenStatusUpdateMessage.getSendingApplicationName());
		ackMessage.setSendingApplicationName(specimenStatusUpdateMessage.getReceivingApplication());
		ackMessage.setMessageControlId(specimenStatusUpdateMessage.getMessageControlId());
		ackMessage.setStatus("AR");
		submitResponse(ackMessage);
	}
}
