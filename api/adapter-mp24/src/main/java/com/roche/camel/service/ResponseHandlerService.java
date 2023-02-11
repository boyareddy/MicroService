/*******************************************************************************
 * ResponseHandlerService.java                  
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
package com.roche.camel.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.SimpleTimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.common.server.service.AuditTrailEntity;
import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.roche.camel.constant.MP24AdapterConstants;
import com.roche.camel.dto.HL7HeaderSegmentDTO;
import com.roche.camel.dto.MP24ConfigDetails;
import com.roche.camel.dto.MessageContainerDTO;
import com.roche.camel.dto.MessagePayload;
import com.roche.camel.dto.ResponseDTO;
import com.roche.camel.dto.ResponseDTO.Status;
import com.roche.camel.dto.SampleTypeDTO;
import com.roche.camel.util.InstanceUtil;
import com.roche.camel.util.MP24ParamConfig;
import com.roche.common.adm.notification.RestClient;
import com.roche.common.audittrail.PostAuditTrailEntry;
import com.roche.common.error.ErrorCode;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.mp24.message.Consumable;
import com.roche.connect.common.mp24.message.ContainerInfo;
import com.roche.connect.common.mp24.message.RSPMessage;
import com.roche.connect.common.mp24.message.SampleInfo;
import com.roche.connect.common.mp24.message.StatusUpdate;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.group.SSU_U03_SPECIMEN_CONTAINER;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.ESU_U01;
import ca.uhn.hl7v2.model.v251.message.RSP_K11;
import ca.uhn.hl7v2.model.v251.message.SSU_U03;
import ca.uhn.hl7v2.model.v251.segment.EQU;
import ca.uhn.hl7v2.model.v251.segment.ERR;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.NTE;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.QAK;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.model.v251.segment.SAC;
import ca.uhn.hl7v2.model.v251.segment.SPM;
import ca.uhn.hl7v2.parser.Parser;
import io.swagger.annotations.Api;

/**
 * The Class ResponseHandlerService.
 */
@Path("/rest/api/v1")
@Api(value = "MP adapter")
@Component
public class ResponseHandlerService {

	/** The login url. */
	@Value("${pas.authenticate_url}")
	private String loginUrl;

	/** The login entity. */
	@Value("${pas.login_entity}")
	private String loginEntity;

	@Value("${pas.device_url}")
	String deviceEndPoint;

	@Value("${connect.imm_mp24_url}")
	private String immUrl;

	@Value("${connect.adm_notification_url}")
	private String admUrl;

	/** The resource loader. */
	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	DeviceHandlerService deviceHandlerService;

	/** The logger. */
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	/** The Constant SIMPLE_DATE_FORMAT. */
	private static final String SIMPLE_DATE_FORMAT = "yyyyMMddHHmmss";

	private static final String SIMPLE_DATE_FORMAT_RMM = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static final String SOURCE_AND_DEVICETYPE = "?source=device&devicetype=";
	private static final String MESSAGE_TYPE = "&messagetype=";
	private static final String DEVICEID = "&deviceid=";
	private static final String HL7_ERROR_MESSAGE = "HLException:::::Error in handling the HL7 message";
	private static final String IOEXCEPTION_ERROR_MESSAGE ="IOException:::::Error in handling the HL7 message";
	private static final String RSP_MSG_INFO ="RSP_K11 message JSON :::::";
	private static final String CONNECTION_MSG_INFO ="Connection Alive status::";
	private static final String OFFLINE = "offline";
	private static final String ABORTED ="Aborted";
	
	private String ackCode;
	
	private String ipaddress;

	public String getAckCode() {
		return ackCode;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public void setAckCode(String ackCode) {
		this.ackCode = ackCode;
	}

	/**
	 * Send response to MP 24.
	 *
	 * @param responseDTO
	 *            the response DTO
	 * @return the response
	 * @throws IOException 
	 * @throws Exception
	 *             the exception
	 */
	@Path("/sendrsptomp24")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendResponseToMP24(@RequestBody ResponseDTO<MessagePayload> responseDTO) throws IOException {
		logger.info("Processing on thread:::" + Thread.currentThread().getName());

		ObjectMapper mapper = new ObjectMapper();

		try {
			logger.info(RSP_MSG_INFO + mapper.writeValueAsString(responseDTO));
			Message rspMessage = processRSPMessage(responseDTO);
			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(responseDTO.getResponse().getMessageControlId());
			InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getResponse().getMessageControlId());
			String serialNo = containerDTO.getHeaderSegment().getDeviceSerialNumber();
			Exchange exchange = containerDTO.getExchange();
			IoSession session = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
			logger.info(CONNECTION_MSG_INFO + session.isActive());
			if (!session.isActive())
				deviceHandlerService.updateDeviceStatus(serialNo, OFFLINE);
			setIpaddress(getLocalIP());
			createAuditBean("Workorderresponse",rspMessage.toString());
			exchange.getOut().setBody(rspMessage);
			logger.info(exchange.getOut().getBody(String.class));
			AsyncCallback callback = containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getResponse().getMessageControlId());
		} catch (HL7Exception | IOException e) {
			logger.error(HL7_ERROR_MESSAGE);
		}
		return Response.status(javax.ws.rs.core.Response.Status.OK).build();
	}

	/**
	 * Send ACK to MP 24.
	 *
	 * @param responseDTO
	 *            the response DTO
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Path("/sendacktomp24")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendACKToMP24(@RequestBody ResponseDTO<MessagePayload> responseDTO) throws IOException {
		logger.info("Processing ACK_U03 message in thread:::::" + Thread.currentThread().getName());
		try (HapiContext context = new DefaultHapiContext()) {
			context.getExecutorService();
			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(responseDTO.getResponse().getMessageControlId());
			Resource resource = resourceLoader.getResource("classpath:hl7/ACK_U03.txt");
			InputStream stream = resource.getInputStream();
			String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
			Parser p = context.getPipeParser();
			ACK queryMessage1 = (ACK) p.parse(msg);

			MSH msh1 = (MSH) queryMessage1.get("MSH");
			MSA msa = (MSA) queryMessage1.get("MSA");
			HL7HeaderSegmentDTO headerSegmentDTO = containerDTO.getHeaderSegment();

			setMsh(headerSegmentDTO, msh1);

			msa.getMsa2_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
			if (responseDTO.isSuccessful()) {

				msa.getMsa1_AcknowledgmentCode().setValue("AA");
			} else {
				msa.getMsa1_AcknowledgmentCode().setValue("AR");
			}
			setIpaddress(getLocalIP());
			createAuditBean("Ackforstatusupdate",queryMessage1.toString());
			closeConnection(headerSegmentDTO, containerDTO, queryMessage1, responseDTO);
		} catch (HL7Exception e) {
			logger.error(HL7_ERROR_MESSAGE);
		} catch (IOException e) {
			logger.error(IOEXCEPTION_ERROR_MESSAGE);
		}
		return Response.status(javax.ws.rs.core.Response.Status.OK).build();
	}

	/**
	 * Convert QBP to msg payload.
	 *
	 * @param headerSegmentDTO
	 *            the header segment DTO
	 */
	public void convertQBPToMsgPayload(HL7HeaderSegmentDTO headerSegmentDTO) {
		logger.info("Processing QBP_Q11 message in thread:::::" + Thread.currentThread().getName());
		MessagePayload messagePayload = new MessagePayload();
		String deviceId = headerSegmentDTO.getDeviceSerialNumber();
		messagePayload.setDeviceSerialNumber(deviceId);
		messagePayload.setSendingApplicationName(headerSegmentDTO.getSendingApplicationName());
		messagePayload.setDateTimeMessageGenerated(headerSegmentDTO.getDateTimeMessageGenerated());
		messagePayload.setMessageType(MessageType.MP24_NAEXTRACTION);
		messagePayload.setAccessioningId(headerSegmentDTO.getSampleID());

		messagePayload.setReceivingApplication(headerSegmentDTO.getReceivingApplication());
		logger.info("message control id :" + headerSegmentDTO.getMessageControlId());
		messagePayload.setMessageControlId(headerSegmentDTO.getMessageControlId());
		logger.info("msgPayload :" + messagePayload.getMessageControlId());
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.info("QBP_Q11 message converted to JSON :::::" + mapper.writeValueAsString(messagePayload));
		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException:::::" + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException:::::" + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException:::::" + e.getMessage());
		}


		try {
			String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.MP24 + MESSAGE_TYPE + MessageType.MP24_NAEXTRACTION
					+ DEVICEID + deviceId;
			logger.info("QBP request URL to IMM :" + url);
			logger.info("messagePayload" + new ObjectMapper().writeValueAsString(messagePayload));
			Entity<MessagePayload> entity1 = Entity.entity(messagePayload, MediaType.APPLICATION_JSON);
			postRequest(url, entity1);
		} catch (Exception e) {
			logger.info("Something went wrong, Request from Adapter to IMM: " + e);
		}

	}

	public MSH setMsh(HL7HeaderSegmentDTO headerSegmentDTO, MSH msh1) throws DataTypeException {

		SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

		msh1.getMsh3_SendingApplication().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingApplication());
		msh1.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingFacility());
		msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getSendingFacility());
		msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().setValue(headerSegmentDTO.getSendingApplicationName());
		msh1.getMsh5_ReceivingApplication().getHd2_UniversalID().setValue(headerSegmentDTO.getDeviceSerialNumber());
		msh1.getMsh5_ReceivingApplication().getHd3_UniversalIDType().setValue("M");
		msh1.getMsh10_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
		msh1.getMsh7_DateTimeOfMessage().getTime().setValue(f.format(new Date()));
		msh1.getMsh11_ProcessingID().getProcessingID().setValue(headerSegmentDTO.getProcessingId());
		msh1.getMsh12_VersionID().getVersionID().setValue(headerSegmentDTO.getVersionId());

		return msh1;
	}
	
	public Message getNoOrderResponse(HL7HeaderSegmentDTO headerSegmentDTO) throws IOException, HL7Exception  {

		logger.info("Processing No order Response RSP_K11 message in thread:::::" + Thread.currentThread().getName());

		Resource resource = resourceLoader.getResource("classpath:hl7/RSP_K11.txt");
		InputStream stream;
		try (HapiContext context = new DefaultHapiContext()) {
		    context.getExecutorService();
			stream = resource.getInputStream();
			SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
			String rspMessage = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
			Parser p = context.getPipeParser();
			RSP_K11 queryMessage = (RSP_K11) p.parse(rspMessage);

			MSH msh1 = (MSH) queryMessage.get("MSH");
			MSA msa = (MSA) queryMessage.get("MSA");
			QAK qak = (QAK) queryMessage.get("QAK");
			QPD qpd = (QPD) queryMessage.get("QPD");
			SPM spm = (SPM) queryMessage.get("SPM");
			ORC orc = (ORC) queryMessage.get("ORC");
			OBR obr = (OBR) queryMessage.get("OBR");
			SAC sac = (SAC) queryMessage.get("SAC");

			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap().get(null);

			msh1 = setMsh(headerSegmentDTO, msh1);

			msa.getMsa2_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());

			qak.getQak3_MessageQueryName().getCe1_Identifier().setValue(headerSegmentDTO.getQueryDefId());
			qak.getQak3_MessageQueryName().getCe2_Text().setValue(headerSegmentDTO.getQueryDefEncodingSystem());
			qak.getQak3_MessageQueryName().getCe3_NameOfCodingSystem()
					.setValue(headerSegmentDTO.getQueryDefEncodingSystem());
			msa.getMsa1_AcknowledgmentCode().setValue("AR");

			qpd.getQpd1_MessageQueryName().getCe1_Identifier().setValue(headerSegmentDTO.getQueryDefId());
			qpd.getQpd1_MessageQueryName().getCe2_Text().setValue(headerSegmentDTO.getQueryDefdesc());
			qpd.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem()
					.setValue(headerSegmentDTO.getQueryDefEncodingSystem());
			qpd.getQpd2_QueryTag().setValue(headerSegmentDTO.getMessageControlId());
			qpd.getQpd3_UserParametersInsuccessivefields().parse(null);

			spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().setValue(null);

			spm.getSpm11_SpecimenRole(0).parse("P");
			spm.getSpm14_SpecimenDescription(0).setValue(null);
			sac.getRegistrationDateTime().getTs1_Time().setValue(f.format(new Date()));

			sac.getSac3_ContainerIdentifier().parse(null);
			orc.getOrc1_OrderControl().setValue("DC");
			orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(null);
			orc.getOrc9_DateTimeOfTransaction().getTs1_Time().setValue(f.format(new Date()));
			orc.getOrc5_OrderStatus().setValue(null);
			obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(null);
			obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(null);

			obr.getObr4_UniversalServiceIdentifier().getCe3_NameOfCodingSystem().setValue("99ROC");
			obr.getObr25_ResultStatus().setValue("S");

			qak.getQak2_QueryResponseStatus().setValue("NF");
			qak.getQak3_MessageQueryName().clear();
			qpd.getQpd3_UserParametersInsuccessivefields().clear();
			qpd.getQpd2_QueryTag().clear();
			orc.getOrc1_OrderControl().setValue("DC");
			orc.getOrc2_PlacerOrderNumber().clear();
			orc.getOrc5_OrderStatus().clear();
			spm.getSpm4_SpecimenType().clear();
			spm.getSpm14_SpecimenDescription(0).clear();
			spm.getSpm27_ContainerType().clear();

			obr.clear();

			logger.info("" + containerDTO);
			logger.info(RSP_MSG_INFO + queryMessage);
			return queryMessage;

		} catch (IOException | HL7Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	/**
	 * Process RSP message.
	 *
	 * @param theMessage
	 *            the the message
	 * @return the message
	 * @throws HL7Exception 
	 * @throws IOException 
	 * @throws Exception
	 *             the exception
	 */
	private Message processRSPMessage(ResponseDTO<MessagePayload> theMessage) throws HL7Exception, IOException  {

		logger.info("Processing RSP_K11 message in thread:::::" + Thread.currentThread().getName());
		try (HapiContext context = new DefaultHapiContext()) {
		    context.getExecutorService();
			logger.info("the message: " + theMessage.toString());
			Resource resource = resourceLoader.getResource("classpath:hl7/RSP_K11.txt");
			InputStream stream = resource.getInputStream();
			SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
			String rspMessage = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
			Parser p = context.getPipeParser();
			RSP_K11 queryMessage = (RSP_K11) p.parse(rspMessage);

			MSH msh1 = (MSH) queryMessage.get("MSH");
			MSA msa = (MSA) queryMessage.get("MSA");
			QAK qak = (QAK) queryMessage.get("QAK");
			QPD qpd = (QPD) queryMessage.get("QPD");
			SPM spm = (SPM) queryMessage.get("SPM");
			ORC orc = (ORC) queryMessage.get("ORC");
			OBR obr = (OBR) queryMessage.get("OBR");
			SAC sac = (SAC) queryMessage.get("SAC");

			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(theMessage.getResponse().getMessageControlId());
			logger.info("" + containerDTO);
			if (containerDTO != null) {
				if (theMessage.getStatus().equals(Status.SUCCESS) || theMessage.getStatus().equals(Status.NOORDER)
						|| theMessage.getStatus().equals(Status.DUPLICATE)) {
					HL7HeaderSegmentDTO headerSegmentDTO = containerDTO.getHeaderSegment();
					msh1.getMsh3_SendingApplication().getHd1_NamespaceID()
							.setValue(headerSegmentDTO.getReceivingApplication());
					msh1.getMsh4_SendingFacility().getHd1_NamespaceID()
							.setValue(headerSegmentDTO.getReceivingFacility());
					msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID()
							.setValue(headerSegmentDTO.getSendingFacility());
					msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID()
							.setValue(headerSegmentDTO.getSendingApplicationName());
					msh1.getMsh5_ReceivingApplication().getHd2_UniversalID()
							.setValue(headerSegmentDTO.getDeviceSerialNumber());
					msh1.getMsh5_ReceivingApplication().getHd3_UniversalIDType().setValue("M");
					msh1.getMsh10_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
					msh1.getMsh7_DateTimeOfMessage().getTime().setValue(f.format(new Date()));
					msh1.getMsh11_ProcessingID().getProcessingID().setValue(headerSegmentDTO.getProcessingId());
					msh1.getMsh12_VersionID().getVersionID().setValue(headerSegmentDTO.getVersionId());

					msa.getMsa2_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
					msa.getMsa1_AcknowledgmentCode().setValue("AA");
					qak.getQak2_QueryResponseStatus().setValue("OK");
					qpd.getQpd1_MessageQueryName().getCe1_Identifier().setValue(headerSegmentDTO.getQueryDefId());
					qpd.getQpd1_MessageQueryName().getCe2_Text().setValue(headerSegmentDTO.getQueryDefdesc());
					qpd.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem()
							.setValue(headerSegmentDTO.getQueryDefEncodingSystem());
					
					spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()
							.setValue(theMessage.getResponse().getAccessioningId());
					RSPMessage rspMessageDTO = theMessage.getResponse().getRspMessage();
					SampleInfo sampleInfo = rspMessageDTO.getSampleInfo();
					ContainerInfo containerInfo = rspMessageDTO.getContainerInfo();
					MP24ConfigDetails configDetails = MP24ParamConfig.getInstance().getMp24ConfigDetails();
					if (sampleInfo.getSampleType() != null) {

						for (SampleTypeDTO sampleTypeDTO : configDetails.getConfigType()) {
							if (sampleTypeDTO.getName().equalsIgnoreCase(sampleInfo.getSampleType())) {
								spm.getSpm4_SpecimenType().getCwe1_Identifier().setValue(sampleTypeDTO.getId());
								spm.getSpm4_SpecimenType().getCwe2_Text().setValue(sampleTypeDTO.getValue());
								spm.getSpm4_SpecimenType().getCwe3_NameOfCodingSystem()
										.setValue(sampleTypeDTO.getEncodingSystem());
							}
						}
					}
					spm.getSpm11_SpecimenRole(0).parse("P");
					spm.getSpm14_SpecimenDescription(0).setValue(sampleInfo.getSpecimenDescription());
					sac.getRegistrationDateTime().getTs1_Time().setValue(f.format(new Date()));
					if (containerInfo != null && containerInfo.getAvailableSpecimenVolume() != null
							&& !theMessage.getStatus().equals(Status.NOORDER)
							&& !theMessage.getStatus().equals(Status.DUPLICATE)) {
						sac.getSac22_AvailableSpecimenVolume().setValue(containerInfo.getAvailableSpecimenVolume());
					}
					sac.getSac3_ContainerIdentifier().parse(theMessage.getResponse().getAccessioningId());
					orc.getOrc1_OrderControl().setValue(rspMessageDTO.getOrderControl());
					orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier()
							.setValue(theMessage.getResponse().getAccessioningId());
					orc.getOrc9_DateTimeOfTransaction().getTs1_Time().setValue(f.format(new Date()));

					if (!theMessage.getStatus().equals(Status.DUPLICATE)) {
						orc.getOrc5_OrderStatus().setValue(rspMessageDTO.getOrderStatus());
						obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier()
								.setValue(rspMessageDTO.getProtocolName());
						obr.getObr4_UniversalServiceIdentifier().getCe2_Text()
								.setValue(rspMessageDTO.getProtocolDescription());
					} else {
						orc.getOrc5_OrderStatus().setValue("");
						obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue("");
						obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue("");
					}
					obr.getObr4_UniversalServiceIdentifier().getCe3_NameOfCodingSystem().setValue("99ROC");
					obr.getObr25_ResultStatus().setValue("S");
					obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(theMessage.getResponse().getAccessioningId());
					obr.getObr16_OrderingProvider(0).getXcn1_IDNumber().setValue("0");
					obr.getObr29_Parent().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().setValue("0");
					if (theMessage.getStatus().equals(Status.NOORDER)
							|| theMessage.getStatus().equals(Status.DUPLICATE)) {
						if (theMessage.getStatus().equals(Status.DUPLICATE)) {
							msa.getMsa1_AcknowledgmentCode().setValue("AR");
							qak.getQak2_QueryResponseStatus().setValue("AR");
						} else {
							msa.getMsa1_AcknowledgmentCode().setValue(theMessage.getResponse().getRspMessage().getQueryResponseStatus());
							qak.getQak2_QueryResponseStatus().setValue("NF");
						}
						qak.getQak3_MessageQueryName().clear();
						qpd.getQpd3_UserParametersInsuccessivefields().clear();
						qpd.getQpd2_QueryTag().clear();
						orc.getOrc1_OrderControl().setValue(theMessage.getResponse().getRspMessage().getOrderControl());
						orc.getOrc2_PlacerOrderNumber().clear();
						orc.getOrc5_OrderStatus().clear();
						spm.getSpm4_SpecimenType().clear();
						spm.getSpm14_SpecimenDescription(0).clear();
						spm.getSpm27_ContainerType().clear();

						obr.clear();

					}
					
				} else {
					msa.getMsa1_AcknowledgmentCode().setValue("AE");
					qak.getQak2_QueryResponseStatus().setValue("AE");
				}

			}
			logger.info(RSP_MSG_INFO + queryMessage);
			return queryMessage;
		} catch (HL7Exception | IOException e) {
			logger.info(e.getMessage());
			throw e;
		} 
	}

	/**
	 * Convert SSU to msg payload.
	 *
	 * @param statusMessage
	 *            the status message
	 * @throws HL7Exception
	 *             the HL 7 exception
	 */
	public void convertSSUToMsgPayload(SSU_U03 statusMessage) throws HL7Exception {
		try {
			logger.info("Processing SSU_U03 message in thread:::::" + Thread.currentThread().getName());
			MSH msh1 = (MSH) statusMessage.get("MSH");
			EQU equ = (EQU) statusMessage.get("EQU");
			String inprogressStatus = null;
			SSU_U03_SPECIMEN_CONTAINER sacContainer = statusMessage.getSPECIMEN_CONTAINER();
			NTE nte = (NTE) sacContainer.get("NTE");
			SAC sac = sacContainer.getSAC();
			SPM spm = sacContainer.getSPECIMEN().getSPM();
			List<OBX> sacObxList = sacContainer.getOBXAll();
			List<OBX> spmObxList = sacContainer.getSPECIMEN().getOBXAll();
			MessagePayload messagePayload = new MessagePayload();
			List<Consumable> consumables = new ArrayList<>();
			String noData = MP24AdapterConstants.MISSING_DATA;
			StatusUpdate statusUpdateDTO = new StatusUpdate();

			String deviceId = Optional.ofNullable(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue())
					.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData);

			String sendingApplicationName = Optional
					.ofNullable(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue()).map(String::trim)
					.filter(d -> !d.isEmpty()).orElse(noData);

			String runResult = Optional.ofNullable(sacObxList.get(2).getObx5_ObservationValue(0).encode())
					.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData);

			String accessioningId = Optional.ofNullable(
					spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().getValue())
					.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData);

			String orderName = Optional.ofNullable(spmObxList.get(0).getObx5_ObservationValue(0).encode())
					.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData);

			String orderResult = Optional.ofNullable(spmObxList.get(1).getObx5_ObservationValue(0).encode())
					.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData);

			String carrierPosition = null;
			String carrierBarcode = null;
			if(!orderResult.equalsIgnoreCase(ABORTED)) {
				carrierPosition = Optional.ofNullable(sac.getSac11_PositionInCarrier().getValue1().getValue())
						.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData);

				carrierBarcode = Optional
						.ofNullable(sac.getSac10_CarrierIdentifier().getEi1_EntityIdentifier().getValue()).map(String::trim)
						.filter(d -> !d.isEmpty()).orElse(noData);
			}
						
			String spm1check3and2 = orderResult.equalsIgnoreCase(ABORTED)
					? Optional
							.ofNullable(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier()
									.getEi1_EntityIdentifier().getValue())
							.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData)
					: Optional
							.ofNullable(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier()
									.getEi1_EntityIdentifier().getValue())
							.map(String::trim).filter(d -> !d.isEmpty())
							.orElse(Optional
									.ofNullable(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier()
											.getEi1_EntityIdentifier().getValue())
									.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData));

			if (!validateRequiredDetails(deviceId, sendingApplicationName, carrierPosition, carrierBarcode, runResult,
					accessioningId, orderName, orderResult, spm1check3and2)) {
				messagePayload.setMessageControlId(msh1.getMessageControlID().getValue());
				messagePayload.setDeviceSerialNumber(deviceId);
				messagePayload.setDateTimeMessageGenerated(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue());
				messagePayload.setMessageType(MessageType.MP24_STATUS_UPDATE);

				messagePayload.setSendingApplicationName(sendingApplicationName);
				messagePayload
						.setReceivingApplication(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
				statusUpdateDTO.setComment(nte.getNte3_Comment(0).getValue());
				logger.info("Comment on the sample is :" + statusUpdateDTO.getComment());
				statusUpdateDTO.setEquipmentState(equ.getEqu3_EquipmentState().getCe1_Identifier().getValue());
				statusUpdateDTO.setEventDate(equ.getEqu2_EventDateTime().getTs1_Time().getValue());
				ContainerInfo containerInfo = new ContainerInfo();
				containerInfo
						.setContainerPosition(sac.getSac3_ContainerIdentifier().getEi1_EntityIdentifier().getValue());
				if (sac.getSac8_ContainerStatus().getCe1_Identifier().getValue().equals("R")) {
					containerInfo.setContainerStatus("Process Completed");
				} else if (sac.getSac8_ContainerStatus().getCe1_Identifier().getValue().equals("O")) {
					containerInfo.setContainerStatus("In Process");
				}

				containerInfo.setCarrierType(sac.getSac9_CarrierType().getCe1_Identifier().getValue());
				containerInfo.setCarrierBarcode(carrierBarcode);
				containerInfo.setCarrierPosition(carrierPosition);

				for (OBX obx : sacObxList) {
					String observationValue = obx.getObx5_ObservationValue(0).encode();
					if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("P")) {
						statusUpdateDTO.setProtocolName(observationValue);
					} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()
							.equalsIgnoreCase("PV")) {
						statusUpdateDTO.setProtocolVersion(observationValue);
					} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()
							.equalsIgnoreCase("RES")) {
						statusUpdateDTO.setRunResult(observationValue);
						inprogressStatus = observationValue;
					} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()
							.equalsIgnoreCase("RuntimeRange")) {

						runStartAndEndTimeForSSU(inprogressStatus, statusUpdateDTO, obx);
					}
				}
				for (OBX obx : spmObxList) {
					String observationValue = obx.getObx5_ObservationValue(0).encode();
					if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()
							.equalsIgnoreCase("OrderName")) {
						statusUpdateDTO.setOrderName(observationValue);
						statusUpdateDTO.setFlag(obx.getObx8_AbnormalFlags(0).getValue());

						statusUpdateDTO.setOperatorName(obx.getObx16_ResponsibleObserver(0).encode());
						logger.info("Operator name:" + statusUpdateDTO.getOperatorName());
					} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()
							.equalsIgnoreCase("OrderResult")) {
						statusUpdateDTO.setOrderResult(observationValue);
					} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()
							.equalsIgnoreCase("IC")) {
						statusUpdateDTO.setInternalControls(observationValue);

					} else {

						Consumable consumable = new Consumable();
						consumable.setName(obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue());
						consumable.setValue(observationValue);
						consumables.add(consumable);
					}
				}
				statusUpdateDTO.setUpdatedBy("System");
				statusUpdateDTO.setConsumables(consumables);

				SampleInfo sampleInfo = new SampleInfo();

				if (!statusUpdateDTO.getOrderResult().equalsIgnoreCase(ABORTED)) {
					String sampleOutput = accessioningId;
					String[] splitSampleOutput = sampleOutput.split("_");
					sampleInfo.setSampleOutputId(splitSampleOutput[0]);
					if (splitSampleOutput.length > 1) {
						sampleInfo.setSampleOutputPosition(splitSampleOutput[1]);
					}
				}

				String sampleType = spm.getSpm4_SpecimenType().getCwe2_Text().encode();
				sampleInfo.setSampleType(sampleType.replaceAll("·", " "));

				if (spm.getSpm11_SpecimenRole(0).getCwe1_Identifier().getValue().equalsIgnoreCase("Q")) {
					sampleInfo.setSpecimenRole("Control");
				} else {
					sampleInfo.setSpecimenRole("Patient");
				}

				if (StringUtils.isNotBlank(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier()
						.getEi1_EntityIdentifier().getValue())) {
					messagePayload.setAccessioningId(spm.getSpm3_SpecimenParentIDs(0).encode());
				} else {
					messagePayload.setAccessioningId(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier()
							.getEi1_EntityIdentifier().getValue());
				}

				statusUpdateDTO.setContainerInfo(containerInfo);
				statusUpdateDTO.setSampleInfo(sampleInfo);
				messagePayload.setStatusUpdate(statusUpdateDTO);
				ObjectMapper mapper = new ObjectMapper();
				logger.info("SSU_U03 message converted to JSON :::::" + mapper.writeValueAsString(messagePayload));
				ssuToIMMRestClient(messagePayload, deviceId);
			} else {

				logger.info(" Preparing ACK for Required field missing .... ");

				this.sendNotificationToADM(NotificationGroupType.MISSING_INFO_MP24.getGroupType(), orderName,deviceId);
				
				ACK ack = (ACK) statusMessage.generateACK();
				MSA msa = ack.getMSA();
				msa.getAcknowledgmentCode().setValue("AR");
				ERR err = ack.getERR();
				err.getErr3_HL7ErrorCode().getCwe1_Identifier().setValue(ErrorCode.REQUIRED_FIELD_MISSING);
				err.getErr3_HL7ErrorCode().getCwe2_Text().setValue(ErrorCode.REQUIRED_FIELD_MISSING_TEXT);
				err.getErr3_HL7ErrorCode().getCwe3_NameOfCodingSystem().setValue(ErrorCode.NAME_OF_CODINGSYSTEM);
				err.getErr8_UserMessage().setValue(ErrorCode.REQUIRED_FIELD_MISSING_TEXT);
				MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
						.get(statusMessage.getMSH().getMessageControlID().getValue());
				Exchange exchange = containerDTO.getExchange();
				exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
				exchange.getOut().setBody(ack);
				AsyncCallback callback = containerDTO.getCallback();
				callback.done(false);
			}
		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException:::::" + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException:::::" + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException:::::" + e.getMessage());
		}

	}

    public void runStartAndEndTimeForSSU(String inprogressStatus, StatusUpdate statusUpdateDTO, OBX obx)
        throws HL7Exception {
        String startEndTime = obx.getObx5_ObservationValue(0).getData().encode();
        try {

        	SimpleDateFormat runDateFormatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        	if ("inprogress".equalsIgnoreCase(inprogressStatus)) {

        		statusUpdateDTO
        				.setRunStartTime(new Timestamp(runDateFormatter.parse(startEndTime).getTime()));
        	} else {
        		statusUpdateDTO.setRunStartTime(new Timestamp(runDateFormatter
        				.parse(startEndTime.substring(0,
        						obx.getObx5_ObservationValue(0).getData().encode().indexOf('^')))
        				.getTime()));
        		statusUpdateDTO.setRunEndTime(new Timestamp(runDateFormatter
        				.parse(startEndTime.substring(
        						obx.getObx5_ObservationValue(0).getData().encode().indexOf('^') + 1))
        				.getTime()));
        	}

        } catch (ParseException e) {
        	logger.info("Parsing exception in:" + e.getMessage());
        }
    }

    public void ssuToIMMRestClient(MessagePayload messagePayload, String deviceId) {
        try {
        	String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.MP24 + MESSAGE_TYPE
        			+ MessageType.MP24_STATUS_UPDATE + DEVICEID + deviceId;
        	logger.info("SSU request URL to IMM :" + url);
        	postRequest(url, Entity.entity(messagePayload, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
        	logger.info("Something went wrong, Request from Adapter to IMM: " + e);
        }
    }

	/**
	 * Post request.
	 *
	 * @param url
	 *            the url
	 * @param entity
	 *            the entity
	 */
	public void postRequest(String url, Entity entity) {
		
		Response resp = null;
		
		try {
			Builder builder = RestClientUtil.getBuilder(loginUrl, null);
			Entity<String> entity2 = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
			String token = builder.post(entity2, String.class);
			
			Builder builder2 = RestClientUtil.getBuilder(url, null);
			builder2.header("Cookie", "brownstoneauthcookie=" + token);
			DateFormat df = new SimpleDateFormat(SIMPLE_DATE_FORMAT_RMM);
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(df);

			logger.info("entity : \t" + mapper.writeValueAsString(entity));
			resp = builder2.post(entity);
			logger.info("Response status from WFM: " + resp.getStatus());
		} catch (Exception e) {
			logger.info("Something went wrong: " + e);
		}
	}

	public void sendNotificationInvalidMessageType() {
		try {

			logger.info("sending Notification .. ");
			MessageDto notification = new MessageDto();
			postRequest(admUrl, Entity.entity(notification, MediaType.APPLICATION_JSON));
		} catch (Exception e) {
			logger.error("ERR:- Error while sending Notification " + e);
		}

	}

	public void sendNotificationInvalidHL7Version() {
		try {

			logger.info("Invalid HL7 version : sending Notification .. ");
			MessageDto notification = new MessageDto();
			postRequest(admUrl, Entity.entity(notification, MediaType.APPLICATION_JSON));
		} catch (Exception e) {
			logger.error("ERR:- Error while sending Notification " + e);
		}

	}

	public void sendACKU01ToMP24(@RequestBody ResponseDTO<MessagePayload> responseDTO) throws IOException {
		logger.info("Processing ACK_U01 message in thread:::::" + Thread.currentThread().getName());
		try (HapiContext context = new DefaultHapiContext()) {
		    context.getExecutorService();
			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(responseDTO.getResponse().getMessageControlId());
			Resource resource = resourceLoader.getResource("classpath:hl7/ACK_U01.txt");
			InputStream stream = resource.getInputStream();
			SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
			String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
			Parser p = context.getPipeParser();
			ACK queryMessage1 = (ACK) p.parse(msg);
			MSH msh1 = (MSH) queryMessage1.get("MSH");
			MSA msa = (MSA) queryMessage1.get("MSA");
			HL7HeaderSegmentDTO headerSegmentDTO = containerDTO.getHeaderSegment();
			msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue();
			msh1.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getSendingFacility());
			msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue();
			msh1.getMsh5_ReceivingApplication().getHd2_UniversalID().setValue(headerSegmentDTO.getDeviceSerialNumber());
			msh1.getMsh5_ReceivingApplication().getHd3_UniversalIDType().setValue("M");
			msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingFacility());
			msh1.getMsh7_DateTimeOfMessage().getTime().setValue(f.format(new Date()));
			msh1.getMsh10_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
			msh1.getMsh11_ProcessingID().getProcessingID().setValue(headerSegmentDTO.getProcessingId());
			msh1.getMsh12_VersionID().getVersionID().setValue(headerSegmentDTO.getVersionId());
			msa.getMsa2_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
			msa.getMsa1_AcknowledgmentCode().setValue(getAckCode());
			setIpaddress(getLocalIP());
			createAuditBean("AckforDevicestateupdate",queryMessage1.toString());
			closeConnection(headerSegmentDTO, containerDTO, queryMessage1, responseDTO);
		} catch (HL7Exception e) {
			logger.error(HL7_ERROR_MESSAGE);
		} catch (IOException e) {
			logger.error(IOEXCEPTION_ERROR_MESSAGE);
		}
	}

	public void closeConnection(HL7HeaderSegmentDTO headerSegmentDTO, MessageContainerDTO containerDTO,
			ACK queryMessage1, ResponseDTO<MessagePayload> responseDTO) {
		String serialNo = headerSegmentDTO.getDeviceSerialNumber();
		Exchange exchange = containerDTO.getExchange();
		IoSession session = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
		logger.info(CONNECTION_MSG_INFO + session.isActive());
		if (!session.isActive())
			deviceHandlerService.updateDeviceStatus(serialNo, OFFLINE);
		exchange.getOut().setBody(queryMessage1);
		logger.info(exchange.getOut().getBody(String.class));
		AsyncCallback callback = containerDTO.getCallback();
		callback.done(false);
		InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getResponse().getMessageControlId());
	}
	
	public void convertESUToMsgPayload(ESU_U01 statusMessage) throws HL7Exception {
		logger.info("Processing ESU_U01 message in thread:::::" + Thread.currentThread().getName());
		ResponseDTO<MessagePayload> responseDTO = new ResponseDTO<>();
		StatusUpdate statusUpdateDTO = new StatusUpdate();
		MessagePayload messagePayload = new MessagePayload();
		MSH msh1 = (MSH) statusMessage.get("MSH");
		EQU equ = (EQU) statusMessage.get("EQU");
		String deviceId = msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue();
		messagePayload.setDeviceSerialNumber(deviceId);
		messagePayload.setDateTimeMessageGenerated(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue());
		messagePayload.setMessageControlId(msh1.getMsh10_MessageControlID().getValue());
		messagePayload.setMessageType(MessageType.MP24_STATUS_UPDATE);
		messagePayload.setSendingApplicationName(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
		messagePayload.setReceivingApplication(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
		logger.info("Comment on the sample is :" + statusUpdateDTO.getComment());
		statusUpdateDTO.setEquipmentState(equ.getEqu3_EquipmentState().getCe1_Identifier().getValue());
		statusUpdateDTO.setEventDate(equ.getEqu2_EventDateTime().getTs1_Time().getValue());
		statusUpdateDTO.setUpdatedBy("System");
		messagePayload.setStatusUpdate(statusUpdateDTO);
		try {
			responseDTO.setResponse(messagePayload);
			sendACKU01ToMP24(responseDTO);
		} catch (IOException e) {
			logger.error(IOEXCEPTION_ERROR_MESSAGE);
		}
	}

	public boolean validateRequiredDetails(String... values) {
		return Stream.of(values).filter(Objects::nonNull).anyMatch(v -> v.equalsIgnoreCase("Missing"));
	}
	
	public void sendNotificationToADM(String messageGroup, String... contentArgs) {
        try {
            logger.info("sending notification to ADM for: " + messageGroup);
            MessageDto notificationDto = new MessageDto();

            List<String> contents = Stream.of(contentArgs).collect(Collectors.toList());

            notificationDto.setContents(contents);
            notificationDto.setMessageGroup(messageGroup);
            notificationDto.setLocale(NotificationGroupType.LOCALE.getGroupType());

            postRequest(admUrl, Entity.entity(notificationDto, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            logger.error("Error while sending a notification!" + e.getLocalizedMessage());
        }
    }
	
	public void createAuditBean(String messagecode,String message) {
		try {
		Date timeStamp = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		String dateTimeUTC=sdf.format(timeStamp);
		String systemId=ConfigurationParser.getString("pas.logged_module_name");
		String objectNewValue = "{"	+ "\"message\": "+JSONObject.quote(message) + "}";
		AuditTrailEntity auditData=new AuditTrailEntity();
		auditData.setCompanydomainname(ConfigurationParser.getString("pas.domainName"));
		auditData.setMessagecode(messagecode);
		auditData.setObjectnewvalue(objectNewValue);
		auditData.setObjectoldvalue(null);
		auditData.setParams(null);
		auditData.setSystemid(systemId);
		auditData.setSystemmodulename(systemId);
		auditData.setTimestamp(dateTimeUTC);
		auditData.setUserid(ConfigurationParser.getString("pas.username"));
		auditData.setIpaddress(getIpaddress());
		String token = RestClient.post(loginUrl, Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED), null, String.class);
		PostAuditTrailEntry.postDataToAuditTrail(Entity.entity(auditData,MediaType.APPLICATION_JSON), token, ConfigurationParser.getString("pas.audit_create_url"));
		} catch (UnsupportedEncodingException e) {
			logger.error("Audit Trail | error while posting audit data::" +e.getMessage());
		}catch (Exception e) {
			logger.error("Audit Trail | error while posting audit data:: " +e.getMessage());
		}
					
	}
	
	private String getLocalIP() {
		String localIP="";
		try {
			localIP=InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("Error while getting IP address:: " +e.getMessage());
		}
		return localIP; 
	}

}