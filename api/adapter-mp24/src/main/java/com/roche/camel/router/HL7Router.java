/*******************************************************************************
 * HL7Router.java                  
 *  Version:  1.0
 * 
 *  Authors:  Arun Paul Jacob
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
 *   arunpaul.j@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.camel.router;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.dataformat.HL7DataFormat;
import org.apache.camel.processor.interceptor.TraceEventHandler;
import org.apache.camel.processor.interceptor.TraceInterceptor;
import org.apache.camel.processor.interceptor.Tracer;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.camel.constant.MP24AdapterConstants;
import com.roche.camel.dto.HL7HeaderSegmentDTO;
import com.roche.camel.dto.MessageContainerDTO;
import com.roche.camel.service.DeviceHandlerService;
import com.roche.camel.service.ResponseHandlerService;
import com.roche.camel.util.InstanceUtil;
import com.roche.camel.util.MP24ParamConfig;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v251.group.SSU_U03_SPECIMEN_CONTAINER;
import ca.uhn.hl7v2.model.v251.message.ESU_U01;
import ca.uhn.hl7v2.model.v251.message.QBP_Q11;
import ca.uhn.hl7v2.model.v251.message.SSU_U03;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.model.v251.segment.SPM;
import ca.uhn.hl7v2.util.Terser;

/**
 * The Class HL7Router.
 */
@Component
class HL7Router extends RouteBuilder {

	/** The server port. */
	@Value("${server.port}")
	String serverPort;

	/** The camel end point. */
	@Value("${camel.hl7router.endpoint}")
	String camelEndPoint;

	/** The camel context. */
	@Autowired
	CamelContext camelContext;

	/** The response handler service. */
	@Autowired
	ResponseHandlerService responseHandlerService;

	@Autowired
	DeviceHandlerService deviceHandlerService;

	@Value("${device.equipmentSatus}")
	private String equipmentSatus;

	@Value("${device.deviceTypeName}")
	private String deviceTypeName;

	@Value("${pas.login_entity}")
	private String loginEntity;

	@Value("${pas.authenticate_url}")
	private String loginUrl;

	@Value("${connect.adm_notification_url}")
	private String admNotificationURL;

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(HL7Router.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {

		Tracer tracer = new Tracer();
		tracer.addTraceHandler(new TraceEventHandler() {

			@Override
			public void traceExchangeOut(ProcessorDefinition<?> node, Processor target,
					TraceInterceptor traceInterceptor, Exchange exchange, Object traceState) throws Exception {
				/** empty method comments - sonar issues */
			}

			@Override
			public Object traceExchangeIn(ProcessorDefinition<?> node, Processor target,
					TraceInterceptor traceInterceptor, Exchange exchange) throws Exception {
				return null;
			}

			@Override
			public void traceExchange(ProcessorDefinition<?> node, Processor target, TraceInterceptor traceInterceptor,
					Exchange exchange) throws Exception {

				traceInterceptor.getTracer().getDefaultTraceFormatter().setShowBody(true);
				logger.info(exchange.getIn().getHeaders().values().toString());
			}
		});

		camelContext.addInterceptStrategy(tracer);

		MP24ParamConfig.getInstance().getMp24ConfigDetails();
		HL7DataFormat dataFormat = new HL7DataFormat();
		from(camelEndPoint).unmarshal(dataFormat).process(new AsyncProcessor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				/** empty block of code --sonar issues */
			}

			public String getToken() {
				Builder loginBuilder = RestClientUtil.getBuilder(loginUrl, null);
				Entity<String> entityString = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
				return loginBuilder.post(entityString, String.class);

			}

			@Override
			public boolean process(Exchange exchange, AsyncCallback callback) {
				String serialNo ="";

				try {
					String version ="";
					String remoteHost = (exchange.getIn().getHeader(Mina2Constants.MINA_REMOTE_ADDRESS).toString()).split(":")[0];
					responseHandlerService.setIpaddress(remoteHost.substring(1));
					String triggerEvent = "";
					Message message = exchange.getIn().getBody(Message.class);
					logger.info(exchange.getIn().getBody(String.class));
					ca.uhn.hl7v2.model.Message message1 = (ca.uhn.hl7v2.model.Message) exchange.getIn().getBody();
					 version = message.getHeader(MP24AdapterConstants.CAMEL_VERSION).toString();
					 if(!MP24AdapterConstants.HL7_VERSION.equalsIgnoreCase(version)) {
						 Terser t = new Terser(message1);
						 serialNo=t.get("/MSH-3-2");
						 throw new ClassCastException("Invalid Version");
					 }
					String msgType = message.getHeader(MP24AdapterConstants.CAMEL_HL7_MESSAGE_TYPE).toString();
					if (message.getHeader("CamelHL7TriggerEvent") != null) {
						triggerEvent = message.getHeader("CamelHL7TriggerEvent").toString();
						logger.info(triggerEvent);
					}
					String pId = message.getHeader("CamelHL7ProcessingId").toString();
					MessageContainerDTO containerDTO = new MessageContainerDTO();
					containerDTO.setExchange(exchange);
					containerDTO.setCallback(callback);
					MSH header = (MSH) message1.get(MP24AdapterConstants.MSH);
					serialNo=header.getMsh3_SendingApplication().getHd2_UniversalID().getValue();
					String messagetype = "";
					IoSession session = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
					if(msgType.equalsIgnoreCase(MP24AdapterConstants.QBP))
						messagetype = "Workorderrequest";
					else if(msgType.equalsIgnoreCase(MP24AdapterConstants.SSU))
						messagetype = "Statusupdate";
					else if(msgType.equalsIgnoreCase(MP24AdapterConstants.ESU))
						messagetype = "Devicestateupdate";
						
					responseHandlerService.createAuditBean(messagetype, message1.toString());
					
					if (!deviceTypeName.equalsIgnoreCase(
							header.getMsh3_SendingApplication().getHd1_NamespaceID().getValue())) {
						session.closeNow();
						List<String> content = new ArrayList<>();
						content.add(serialNo);
						AdmNotificationService.sendNotification(NotificationGroupType.INVALID_DEVICE_MODEL_MP24,
								content, getToken(), admNotificationURL);
						throw new HL7Exception("Device type mismatched, supported model is " + deviceTypeName);
					}

					if (msgType.equalsIgnoreCase(MP24AdapterConstants.QBP) && triggerEvent.equalsIgnoreCase("WOS")
							&& (pId.equalsIgnoreCase("P") || pId.equalsIgnoreCase("T"))) {

						qbpProcessExchange(exchange, callback, containerDTO, message1, session);
					} else if (msgType.equalsIgnoreCase(MP24AdapterConstants.SSU)
							&& triggerEvent.equalsIgnoreCase("U03")
							&& (pId.equalsIgnoreCase("P") || pId.equalsIgnoreCase("T"))) {
						ssuProcessExchange(exchange, callback, containerDTO, message1);
					} else if (msgType.equalsIgnoreCase(MP24AdapterConstants.ESU)
							&& triggerEvent.equalsIgnoreCase("U01")
							&& (pId.equalsIgnoreCase("P") || pId.equalsIgnoreCase("T"))) {
						esuProcessExchange(exchange, callback, containerDTO, message1, serialNo);
					} else {
						exchange.getOut().setBody("");
						callback = containerDTO.getCallback();
						callback.done(false);
						logger.info("process stopped because of unsupported messageType/eventType/processingId");
						logger.info("messageType : " + msgType + "----" + "eventType : " + triggerEvent + "----"
								+ "processingId : " + pId);
						responseHandlerService.sendNotificationInvalidMessageType();
						logger.info("Invalid message type : closing connection ");
					}
				} catch (ClassCastException e) {
					exchange.getOut().setBody("");
					callback.done(false);
					logger.info("Process stopped because of invalid HL7 version");
					logger.info("HL7 version : " + e.getMessage());
					List<String> content = new ArrayList<>();
					content.add(serialNo);
					try {
						AdmNotificationService.sendNotification(NotificationGroupType.INVALID_HL7_VER_MP24, content,
								getToken(), admNotificationURL);
					} catch (HMTPException e1) {
						logger.info("Error while sending notification" + e1.getMessage());
					}
					logger.info("Invalid HL7 version: closing connection ");

				} catch (HL7Exception e1) {
					exchange.getOut().setBody("");
					callback.done(false);
					logger.error("HL7Exception at hl7 router | " + e1.getMessage());
					return false;
				} catch (Exception e1) {
					exchange.getOut().setBody("");
					callback.done(false);
					logger.info("Invalid Message" + e1.getMessage());
				}

				return false;
			}

			public void qbpProcessExchange(Exchange exchange, AsyncCallback callback, MessageContainerDTO containerDTO,
					ca.uhn.hl7v2.model.Message message1, IoSession session) {
				QBP_Q11 qbp;
				qbp = (QBP_Q11) message1;
				Type sampleIdSeg = qbp.getQPD().getQpd3_UserParametersInsuccessivefields().getData();
				try {
					String sampleId = sampleIdSeg.encode();

					HL7HeaderSegmentDTO headerSegmentDTO = new HL7HeaderSegmentDTO();
					MSH msh1 = (MSH) qbp.get(MP24AdapterConstants.MSH);
					if (!deviceHandlerService.deviceValidation(exchange, msh1)) {
						exchange.getOut().setBody("");
						callback.done(false);
						throw new HL7Exception("The unregistered device causes connection drop");
					}
					QPD qpd1 = (QPD) qbp.get(MP24AdapterConstants.QPD);
					headerSegmentDTO.setSendingApplicationName(
							msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
					headerSegmentDTO
							.setDeviceSerialNumber(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue());
					headerSegmentDTO.setSendingFacility(msh1.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
					headerSegmentDTO
							.setReceivingFacility(msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().getValue());
					headerSegmentDTO.setReceivingApplication(
							msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
					headerSegmentDTO.setMessageControlId(msh1.getMsh10_MessageControlID().getValue());

					headerSegmentDTO.setSampleID(sampleId);
					headerSegmentDTO
							.setDateTimeMessageGenerated(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue());
					headerSegmentDTO.setCharacterSet(Arrays.toString(msh1.getMsh18_CharacterSet()));
					headerSegmentDTO.setMessageType(msh1.getMsh9_MessageType().getMsg1_MessageCode().getValue());
					headerSegmentDTO.setProcessingId(msh1.getMsh11_ProcessingID().getPt1_ProcessingID().getValue());
					headerSegmentDTO.setQueryDefId(qpd1.getQpd1_MessageQueryName().getCe1_Identifier().getValue());
					headerSegmentDTO.setQueryDefdesc(qpd1.getQpd1_MessageQueryName().getCe2_Text().getValue());
					headerSegmentDTO.setQueryDefEncodingSystem(
							qpd1.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem().getValue());
					headerSegmentDTO.setVersionId(msh1.getMsh12_VersionID().getVid1_VersionID().getValue());
					containerDTO.setHeaderSegment(headerSegmentDTO);

					if (!(sampleId.trim().equals("") || sampleId == null)) {
						InstanceUtil.getInstance().getConnectionMap().put(headerSegmentDTO.getMessageControlId(),
								containerDTO);
						MP24ParamConfig.getInstance().getMp24ConfigDetails();
						responseHandlerService.convertQBPToMsgPayload(headerSegmentDTO);
					} else {
						ca.uhn.hl7v2.model.Message rspMessage = responseHandlerService
								.getNoOrderResponse(headerSegmentDTO);
						logger.info("Connection Alive status::" + session.isActive());
						exchange.getOut().setBody(rspMessage);
						logger.info(exchange.getOut().getBody(String.class));
						callback.done(false);
					}

				} catch (HL7Exception | IOException e) {
					exchange.getOut().setBody("");
					callback = containerDTO.getCallback();
					callback.done(false);
					logger.error("HL7Exception at hl7 router qbp process message | " + e.getMessage());
				} catch (Exception e) {
					exchange.getOut().setBody("");
					callback = containerDTO.getCallback();
					callback.done(false);
					logger.error("Exception at hl7 router qbp process message | " + e.getMessage());
				}
			}

			public void ssuProcessExchange(Exchange exchange, AsyncCallback callback, MessageContainerDTO containerDTO,
					ca.uhn.hl7v2.model.Message message1) {
				SSU_U03 ssu;
				ssu = (SSU_U03) message1;
				String sampleID = "";
				try {
					HL7HeaderSegmentDTO headerSegmentDTO = new HL7HeaderSegmentDTO();
					MSH msh1 = (MSH) ssu.get(MP24AdapterConstants.MSH);
					if (!deviceHandlerService.deviceValidation(exchange, msh1)) {
						throw new HL7Exception("the unregistered device causes connection drop");
					}

					SSU_U03_SPECIMEN_CONTAINER sacContainer = ssu.getSPECIMEN_CONTAINER();
					SPM spm = sacContainer.getSPECIMEN().getSPM();
					headerSegmentDTO.setSendingApplicationName(
							msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
					headerSegmentDTO
							.setDeviceSerialNumber(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue());
					headerSegmentDTO.setSendingFacility(msh1.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
					headerSegmentDTO
							.setReceivingFacility(msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().getValue());
					headerSegmentDTO.setReceivingApplication(
							msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
					headerSegmentDTO.setMessageControlId(msh1.getMsh10_MessageControlID().getValue());
					if (StringUtils.isNotBlank(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier()
							.getEi1_EntityIdentifier().getValue())) {
						sampleID = spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier()
								.getEi1_EntityIdentifier().getValue();
					} else {
						sampleID = spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()
								.getValue();
					}
					headerSegmentDTO.setSampleID(sampleID);
					headerSegmentDTO
							.setDateTimeMessageGenerated(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue());
					headerSegmentDTO.setCharacterSet(Arrays.toString(msh1.getMsh18_CharacterSet()));
					headerSegmentDTO.setMessageType(msh1.getMsh9_MessageType().getMsg1_MessageCode().getValue());
					headerSegmentDTO.setProcessingId(msh1.getMsh11_ProcessingID().getPt1_ProcessingID().getValue());
					headerSegmentDTO.setVersionId(msh1.getMsh12_VersionID().getVid1_VersionID().getValue());
					containerDTO.setHeaderSegment(headerSegmentDTO);
					InstanceUtil.getInstance().getConnectionMap().put(headerSegmentDTO.getMessageControlId(),
							containerDTO);
					responseHandlerService.convertSSUToMsgPayload(ssu);
				} catch (HL7Exception e) {
					exchange.getOut().setBody("");
					callback = containerDTO.getCallback();
					callback.done(false);
					logger.error("HL7Exception at hl7 router ssu process message | " + e.getMessage());
				}
			}

			public void esuProcessExchange(Exchange exchange, AsyncCallback callback, MessageContainerDTO containerDTO,
					ca.uhn.hl7v2.model.Message message1, String serialNo) throws HMTPException {
				ESU_U01 esu;
				esu = (ESU_U01) message1;
				try {
					HL7HeaderSegmentDTO headerSegmentDTO = new HL7HeaderSegmentDTO();
					MSH msh1 = (MSH) esu.get(MP24AdapterConstants.MSH);
					String deviceState = esu.getEQU().getEqu3_EquipmentState().getCe1_Identifier().getValue();
					String[] status = equipmentSatus.split(",");
					boolean flag = false;
					for (String state : status) {
						if (state.equalsIgnoreCase(deviceState)) {
							flag = true;
						}
					}
					if (!flag) {
						responseHandlerService.setAckCode("AR");
						List<String> content = new ArrayList<>();
						content.add(deviceState);
						content.add(serialNo);
						AdmNotificationService.sendNotification(NotificationGroupType.UNSUPPORTED_EQUIP_STATUS_MP24,
								content, getToken(), admNotificationURL);
					} else {
						responseHandlerService.setAckCode("AA");
						deviceHandlerService.setDeviceSubStatus(deviceState);
						if (!deviceHandlerService.deviceValidation(exchange, msh1)) {
							throw new HL7Exception("the unregistered device causes connection drop");
						}
					}
					headerSegmentDTO.setSendingApplicationName(
							msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
					headerSegmentDTO
							.setDeviceSerialNumber(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue());
					headerSegmentDTO.setSendingFacility(msh1.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
					headerSegmentDTO.setReceivingApplication(
							msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
					headerSegmentDTO
							.setReceivingFacility(msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().getValue());
					headerSegmentDTO.setMessageControlId(msh1.getMsh10_MessageControlID().getValue());
					headerSegmentDTO
							.setDateTimeMessageGenerated(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue());
					headerSegmentDTO.setMessageType(msh1.getMsh9_MessageType().getMsg1_MessageCode().getValue());
					headerSegmentDTO.setProcessingId(msh1.getMsh11_ProcessingID().getPt1_ProcessingID().getValue());
					headerSegmentDTO.setVersionId(msh1.getMsh12_VersionID().getVid1_VersionID().getValue());
					containerDTO.setHeaderSegment(headerSegmentDTO);
					InstanceUtil.getInstance().getConnectionMap().put(msh1.getMsh10_MessageControlID().getValue(),
							containerDTO);
					responseHandlerService.convertESUToMsgPayload(esu);
				} catch (HL7Exception e) {
					exchange.getOut().setBody("");
					callback = containerDTO.getCallback();
					callback.done(false);
					logger.error("HL7Exception at hl7 router esu process message " + e.getMessage());
				}
			}

		});
	}
}