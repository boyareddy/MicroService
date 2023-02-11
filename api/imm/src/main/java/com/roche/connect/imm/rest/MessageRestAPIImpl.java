/*******************************************************************************
 * MessageRestAPIImpl.java                  
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
package com.roche.connect.imm.rest;

import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.common.mp96.AdaptorACKMessage;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.connect.imm.service.DPCRAnalyzerAsyncMessageService;
import com.roche.connect.imm.service.ForteDetail;
import com.roche.connect.imm.service.LP24MessageService;
import com.roche.connect.imm.service.MP24AsyncMessageService;
import com.roche.connect.imm.service.MP96MessageService;
import com.roche.connect.imm.service.MessageProcessorService;
import com.roche.connect.imm.service.MessageService;
import com.roche.connect.imm.utils.ObjectMapperFactory;

/**
 * The Class MessageRestAPIImpl.
 */
@Component
public class MessageRestAPIImpl implements MessageRestAPI {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Value("${connect.wfm_host_url}")
	private String wfmHostUrl;

	/** The message service. */
	@Autowired
	private ForteDetail forteDetail;

	@Autowired
	private MessageService messageService;

	@Autowired
	MP24AsyncMessageService mp24AsyncMessageService;

	@Autowired
	private LP24MessageService lp24MessageService;

	@Autowired
	private MP96MessageService mp96MessageService;

	@Autowired
	private DPCRAnalyzerAsyncMessageService dpcrAnalyzerAsyncMessageService;

	@Autowired
	private MessageProcessorService messageProcessorService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory();

	@Override
	public Response consumeGenericRequest(String requestBody, String source, String deviceType, String messageType,
			String deviceId) throws HMTPException {

		logger.info("Entering consumeGenericRequest: \t" + requestBody);

		try {

			Object objectValue = objectMapperFactory.readObjectValue(requestBody, source, deviceType, messageType);

			if (objectValue != null) {
				messageService.saveMessage(objectValue, messageType, deviceId);
				return processGenericRequest(objectValue);
			}

			Map<String, Object> map = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {
			});

			String messageTypeActual = null;

			if (messageType != null) {
				messageTypeActual = messageType;
			} else if (map.containsKey("messageType")) {
				messageTypeActual = map.get("messageType").toString();
			}

			logger.info("Message Type: " + messageTypeActual);

			if (messageTypeActual != null) {

				String token = (String) ThreadSessionManager.currentUserSession()
						.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
				if (messageTypeActual.equalsIgnoreCase(MessageType.FORTE_GET_JOB)) {

					return forteDetail.getForteGetJob(map);

				} else if (messageTypeActual.equalsIgnoreCase(MessageType.FORTE_PUT_JOB)) {

					return forteDetail.getFortePutJob(deviceId, deviceType, map, token);

				} else if (messageTypeActual.equalsIgnoreCase(MessageType.UPDATE_RUN)
						&& (deviceType.equalsIgnoreCase(DeviceType.MP24)
								|| deviceType.equalsIgnoreCase(DeviceType.LP24))) {
					mp24AsyncMessageService.updateRunResultByDeviceId(deviceId, deviceType, token);
					return Response.status(Status.OK).build();
				}
			}
		} catch (Exception e) {
			logger.error("Failed to handle Bad request, message: " + e.getMessage(), e);
		}

		return Response.status(Status.BAD_REQUEST).build();
	}

	private Response processGenericRequest(Object objectValue) {

		if (objectValue instanceof QueryMessage) {
			return mp96MessageService.processQueryMessage((QueryMessage) objectValue);
		} else if (objectValue instanceof AdaptorACKMessage) {
			return mp96MessageService.processACKMessage((AdaptorACKMessage) objectValue);
		} else if (objectValue instanceof OULRunResultMessage) {
			return mp96MessageService.processOULMessage((OULRunResultMessage) objectValue);
		} else if (objectValue instanceof com.roche.connect.common.lp24.QueryMessage) {
			return lp24MessageService.processQueryMessage((com.roche.connect.common.lp24.QueryMessage) objectValue);
		} else if (objectValue instanceof com.roche.connect.common.lp24.ResponseMessage) {
			return lp24MessageService
					.processQueryResponseMessage((com.roche.connect.common.lp24.ResponseMessage) objectValue);
		} else if (objectValue instanceof com.roche.connect.common.lp24.SpecimenStatusUpdateMessage) {
			return lp24MessageService.processStatusUpdateMessages(
					(com.roche.connect.common.lp24.SpecimenStatusUpdateMessage) objectValue);
		} else if (objectValue instanceof com.roche.connect.common.lp24.AcknowledgementMessage) {
			return lp24MessageService
					.processACKMessages((com.roche.connect.common.lp24.AcknowledgementMessage) objectValue);
		} else if (objectValue instanceof AdaptorRequestMessage) {
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
			messageProcessorService.processRequest((AdaptorRequestMessage) objectValue, token);
			return Response.status(Status.OK).build();
		} else if (objectValue instanceof AdaptorResponseMessage) {
			return messageProcessorService.submitResponse((AdaptorResponseMessage) objectValue);
		} else if (objectValue instanceof com.roche.connect.common.dpcr_analyzer.QueryMessage) {
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
			dpcrAnalyzerAsyncMessageService
					.performAsyncQueryMessage((com.roche.connect.common.dpcr_analyzer.QueryMessage) objectValue, token);
			return Response.status(Status.OK).build();
		} else if (objectValue instanceof AcknowledgementMessage) {
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
			dpcrAnalyzerAsyncMessageService.processAsyncAcknowledgementMessage((AcknowledgementMessage) objectValue,
					token);
			return Response.status(Status.OK).build();
		} else if (objectValue instanceof ESUMessage) {
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
			dpcrAnalyzerAsyncMessageService.processAsyncESUMessage((ESUMessage) objectValue, token);
			return Response.status(Status.OK).build();
		} else if (objectValue instanceof ORUMessage) {
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
			dpcrAnalyzerAsyncMessageService.processAsyncORUMessage((ORUMessage) objectValue, token);
			return Response.status(Status.OK).build();
		}

		return Response.status(Status.BAD_REQUEST).build();
	}

}
