package com.roche.lpcamel.service;

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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SimpleTimeZone;
import java.util.function.Predicate;
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
import javax.ws.rs.core.Response.Status;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.commons.io.Charsets;
import org.apache.cxf.common.util.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.common.server.service.AuditTrailEntity;
import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.roche.common.adm.notification.RestClient;
import com.roche.common.audittrail.PostAuditTrailEntry;
import com.roche.common.error.ErrorCode;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.Consumable;
import com.roche.connect.common.lp24.ContainerInfo;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SampleInfo;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.lpcamel.dto.HL7HeaderSegmentDTO;
import com.roche.lpcamel.dto.MP24ConfigDetails;
import com.roche.lpcamel.dto.MandatoryFieldSSU;
import com.roche.lpcamel.dto.MessageContainerDTO;
import com.roche.lpcamel.dto.MessagePayload;
import com.roche.lpcamel.dto.ResponseDTO;
import com.roche.lpcamel.dto.SampleTypeDTO;
import com.roche.lpcamel.util.InstanceUtil;
import com.roche.lpcamel.util.MP24ParamConfig;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
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
@Api(value = "LP adapter")
@Component
public class ResponseHandlerService {

	private static final String ABORTED = "Aborted";
	private static final String FAILED = "Failed";

	/** The login url. */
	@Value("${pas.authenticate_url}")
	private String loginUrl;

	/** The login entity. */
	@Value("${pas.login_entity}")
	private String loginEntity;

	@Value("${connect.imm_url}")
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
	private static final String NO_ORDER = "NOORDER";
	private static final String DUPLICATE = "DUPLICATE";
	private static final String RSP_MSG_INFO = "RSP_K11 message JSON :::::";
	private static final String CONNECTION_MSG_INFO = "Connection Alive status::";
	private static final String HL7_ERROR_MESSAGE = "HLException:::::Error in handling the HL7 message";
	private static final String IOEXCEPTION_ERROR_MESSAGE = "IOException:::::Error in handling the HL7 message";
	private static final String OFFLINE = "offline";
	
	private String ackCode;
	private String ipaddress;


	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getAckCode() {
		return ackCode;
	}

	public void setAckCode(String ackCode) {
		this.ackCode = ackCode;
	}

	/**
	 * Hit Ping
	 * 
	 * @param requestBody
	 * @return
	 * @throws HMTPException
	 * @throws ParseException
	 */
	@POST
	@Path("/lp/ping")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response hitPing() {
		logger.info("Enter ResponseHandlerService.hitPing method ");

		return Response.ok().build();

	}

	/**
	 * Hit hello
	 * 
	 * @param requestBody
	 * @return
	 * @throws JsonProcessingException
	 * @throws HMTPException
	 * @throws ParseException
	 */
	@POST
	@Path("/lp/hello")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response hitHello(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
		logger.info("RequestBody from LP simulator is: " + requestBody);
		logger.info("Enter ResponseHandlerService.hitHello method ");
		logger.info("LP-Device information " + new ObjectMapper().writeValueAsString(requestBody));
		return Response.ok().build();
	}

	/**
	 * Send response to LP.
	 *
	 * @param responseMessage
	 *            the response message
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Path("/responseMessage")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendResponseToLP(@RequestBody ResponseMessage responseMessage) throws IOException {
		logger.info("Processing on thread:::" + Thread.currentThread().getName());

		ObjectMapper mapper = new ObjectMapper();

		MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
				.get(responseMessage.getMessageControlId());
		try {
			logger.info(RSP_MSG_INFO + mapper.writeValueAsString(responseMessage));
			Message rspMessage = processRSPMessage(responseMessage);
			logger.info("THe RSSP message: " + rspMessage.toString());
			Exchange exchange = containerDTO.getExchange();
			String serialNo = containerDTO.getHeaderSegment().getDeviceSerialNumber();
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
			InstanceUtil.getInstance().getConnectionMap().remove(responseMessage.getMessageControlId());
			logger.info("End sendResponseToLP");
		} catch (Exception e) {
			containerDTO.getExchange();
			AsyncCallback callback = containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(responseMessage.getMessageControlId());
			logger.error(HL7_ERROR_MESSAGE);
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * Send ACK to LP.
	 *
	 * @param responseDTO
	 *            the response DTO
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Path("/acknowledgementMessage")
	@POST
	@Produces("application/json")
	@Consumes("application/json")

	public Response sendACKToLP(@RequestBody AcknowledgementMessage responseDTO) throws IOException {
		logger.info("Processing ACK_U03 message in thread:::::" + Thread.currentThread().getName());
		try (HapiContext context = new DefaultHapiContext()) {
		    context.getExecutorService();
			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(responseDTO.getMessageControlId());
			Resource resource = resourceLoader.getResource("classpath:hl7/ACK_U03.txt");
			InputStream stream = resource.getInputStream();
			SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
			String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
			Parser p = context.getPipeParser();
			ACK queryMessage1 = (ACK) p.parse(msg);

			MSH msh1 = (MSH) queryMessage1.get("MSH");
			MSA msa = (MSA) queryMessage1.get("MSA");
			HL7HeaderSegmentDTO headerSegmentDTO = containerDTO.getHeaderSegment();
			msh1.getMsh3_SendingApplication().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingApplication());
			msh1.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getSendingFacility());
			msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingFacility());
			msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID()
					.setValue(headerSegmentDTO.getSendingApplicationName());
			msh1.getMsh5_ReceivingApplication().getHd2_UniversalID().setValue(headerSegmentDTO.getDeviceSerialNumber());
			msh1.getMsh5_ReceivingApplication().getHd3_UniversalIDType().setValue("M");
			msh1.getMsh10_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
			msh1.getMsh7_DateTimeOfMessage().getTime().setValue(f.format(new Date()));
			msh1.getMsh11_ProcessingID().getProcessingID().setValue(headerSegmentDTO.getProcessingId());
			msh1.getMsh12_VersionID().getVersionID().setValue(headerSegmentDTO.getVersionId());

			msa.getMsa2_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
			ERR err = queryMessage1.getERR();
			if ("AR".equalsIgnoreCase(responseDTO.getStatus())) {
				msa.getMsa1_AcknowledgmentCode().setValue("AR");
				err.getErr3_HL7ErrorCode().getCwe1_Identifier().setValue(responseDTO.getErrorCode());
				err.getErr8_UserMessage().setValue(responseDTO.getErrorDescription());
				err.getErr3_HL7ErrorCode().getCwe2_Text().setValue(responseDTO.getErrorDescription());
			} else {
				msa.getMsa1_AcknowledgmentCode().setValue("AA");
				err.clear();
			}
			Exchange exchange = containerDTO.getExchange();
			String serialNo = containerDTO.getHeaderSegment().getDeviceSerialNumber();
			IoSession session = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
			logger.info(CONNECTION_MSG_INFO + session.isActive());
			if (!session.isActive())
				deviceHandlerService.updateDeviceStatus(serialNo, OFFLINE);
			setIpaddress(getLocalIP());
			createAuditBean("Ackforstatusupdate",queryMessage1.toString());
			exchange.getOut().setBody(queryMessage1);
			logger.info(exchange.getOut().getBody(String.class));
			AsyncCallback callback = containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getMessageControlId());
		} catch (HL7Exception e) {
			logger.error(HL7_ERROR_MESSAGE);
		} catch (IOException e) {
			logger.error(IOEXCEPTION_ERROR_MESSAGE);
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * Convert QBP to msg payload.
	 *
	 * @param headerSegmentDTO
	 *            the header segment DTO
	 * @throws Exception
	 */
	public void convertQBPToMsgPayload(HL7HeaderSegmentDTO headerSegmentDTO) throws Exception {
		logger.info("Processing QBP_Q11 message in thread:::::" + Thread.currentThread().getName());
		QueryMessage queryMessage = new QueryMessage();
		queryMessage.setContainerId(headerSegmentDTO.getSampleID());
		queryMessage.setDeviceSerialNumber(headerSegmentDTO.getDeviceSerialNumber());
		queryMessage.setSendingApplicationName(headerSegmentDTO.getSendingApplicationName());
		queryMessage.setMessageType(EnumMessageType.QueryMessage);
		queryMessage.setDateTimeMessageGenerated(headerSegmentDTO.getDateTimeMessageGenerated());
		queryMessage.setReceivingApplication(headerSegmentDTO.getReceivingApplication());
		queryMessage.setMessageControlId(headerSegmentDTO.getMessageControlId());

		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.info("QBP_Q11 message converted to JSON :::::" + mapper.writeValueAsString(queryMessage));
		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException:::::" + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException:::::" + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException:::::" + e.getMessage());
		}

		try {

			String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.LP24 + MESSAGE_TYPE + MessageType.LP24_QBP
					+ DEVICEID + queryMessage.getDeviceSerialNumber();
			logger.info("QBP request URL to IMM :" + url);
			postRequest(url, Entity.entity(queryMessage, MediaType.APPLICATION_JSON));
		} catch (Exception e) {
			logger.info("Something went wrong, Request from Adapter to IMM: " + e.getMessage());
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
	 *             the HL 7 exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private Message processRSPMessage(ResponseMessage theMessage) throws HL7Exception, IOException {

		logger.info("Processing RSP_K11 message in thread:::::" + Thread.currentThread().getName());
	
		RSP_K11 queryMessage1 = null;
		try (HapiContext context = new DefaultHapiContext()) {
		    context.getExecutorService();
			logger.info("the message: " + theMessage.toString());
			Resource resource = resourceLoader.getResource("classpath:hl7/RSP_K11.txt");
			InputStream stream = resource.getInputStream();
			SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
			String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
			Parser p = context.getPipeParser();
			queryMessage1 = (RSP_K11) p.parse(msg);

			MSH msh1 = (MSH) queryMessage1.get("MSH");
			MSA msa = (MSA) queryMessage1.get("MSA");
			QAK qak = (QAK) queryMessage1.get("QAK");
			QPD qpd = (QPD) queryMessage1.get("QPD");
			SPM spm = (SPM) queryMessage1.get("SPM");
			ORC orc = (ORC) queryMessage1.get("ORC");
			OBR obr = (OBR) queryMessage1.get("OBR");
			SAC sac = (SAC) queryMessage1.get("SAC");
			MessageContainerDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(theMessage.getMessageControlId());
			if (containerDTO != null) {
				if (theMessage.getStatus().equals("SUCCESS") || theMessage.getStatus().equals(NO_ORDER)
						|| theMessage.getStatus().equals("AR") || theMessage.getStatus().equals(DUPLICATE)) {
					HL7HeaderSegmentDTO headerSegmentDTO = containerDTO.getHeaderSegment();
					msh1.getMsh3_SendingApplication().getHd1_NamespaceID()
							.setValue(headerSegmentDTO.getReceivingApplication());
					msh1.getMsh4_SendingFacility().getHd1_NamespaceID()
							.setValue(headerSegmentDTO.getReceivingFacility());
					msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID()
							.setValue(headerSegmentDTO.getReceivingFacility());
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

					qak.getQak3_MessageQueryName().clear();
					if ("AR".equalsIgnoreCase(theMessage.getStatus())
							|| DUPLICATE.equalsIgnoreCase(theMessage.getStatus())) {
						msa.getMsa1_AcknowledgmentCode().setValue("AR");
					} else {
						msa.getMsa1_AcknowledgmentCode().setValue("AA");
					}

					if (NO_ORDER.equalsIgnoreCase(theMessage.getStatus())) {
						qak.getQak2_QueryResponseStatus().setValue("NF");
					} else if (theMessage.getStatus().equals("AR")
							|| DUPLICATE.equalsIgnoreCase(theMessage.getStatus())) {
						qak.getQak2_QueryResponseStatus().setValue("AR");
					} else {
						qak.getQak2_QueryResponseStatus().setValue("OK");
					}

					qpd.getQpd1_MessageQueryName().getCe1_Identifier().setValue(headerSegmentDTO.getQueryDefId());
					qpd.getQpd1_MessageQueryName().getCe2_Text().setValue(headerSegmentDTO.getQueryDefDesc());
					qpd.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem()
							.setValue(headerSegmentDTO.getQueryDefEncodingSystem());
					spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()
							.setValue(theMessage.getContainerId());

					MP24ConfigDetails configDetails = MP24ParamConfig.getInstance().getMp24ConfigDetails();
					if (theMessage.getSampleType() != null) {

						for (SampleTypeDTO sampleTypeDTO : configDetails.getConfigType()) {
							if (sampleTypeDTO.getName().equalsIgnoreCase(theMessage.getSampleType())) {
								spm.getSpm4_SpecimenType().getCwe1_Identifier().setValue(sampleTypeDTO.getId());
								spm.getSpm4_SpecimenType().getCwe2_Text().setValue(sampleTypeDTO.getValue());
								spm.getSpm4_SpecimenType().getCwe3_NameOfCodingSystem()
										.setValue(sampleTypeDTO.getEncodingSystem());
							}
						}
					}
					spm.getSpm1_SetIDSPM().parse("1");
					spm.getSpm11_SpecimenRole(0).parse("P");
					spm.getSpm4_SpecimenType().getCwe2_Text().getValue();
					spm.getSpm14_SpecimenDescription(0).setValue(theMessage.getSpecimenDescription());
					sac.getSac3_ContainerIdentifier().getEi1_EntityIdentifier().setValue(theMessage.getContainerId());
					sac.getRegistrationDateTime().getTs1_Time().setValue(f.format(new Date()));
					if (theMessage.getAccessioningId() != null) {
						orc.getOrc1_OrderControl().setValue("NW");
						orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier()
								.setValue(theMessage.getAccessioningId());

						orc.getOrc5_OrderStatus().setValue("SC");
					}
					orc.getOrc9_DateTimeOfTransaction().getTs1_Time().setValue(f.format(new Date()));
					obr.getObr4_UniversalServiceIdentifier().parse(theMessage.getProtocolName());
					obr.getObr4_UniversalServiceIdentifier().getCe3_NameOfCodingSystem().setValue("99ROC");
					obr.getObr25_ResultStatus().setValue("S");
					if (NO_ORDER.equalsIgnoreCase(theMessage.getStatus())
							|| ("AR".equalsIgnoreCase(theMessage.getStatus())
									|| DUPLICATE.equalsIgnoreCase(theMessage.getStatus()))) {
						qak.getQak3_MessageQueryName().clear();

						qpd.getQpd3_UserParametersInsuccessivefields().clear();
						qpd.getQpd2_QueryTag().clear();
						orc.getOrc1_OrderControl().setValue(theMessage.getOrderControl());
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

		} catch (IOException e) {
			logger.error("Error while processing RSP message. Error :" + e.getMessage());
		} 
		return queryMessage1;
	}

	/**
	 * Convert SSU to msg payload.
	 *
	 * @param statusMessage
	 *            the status message
	 * @throws Exception
	 */
	public void convertSSUToMsgPayload(SSU_U03 statusMessage) throws Exception {
		logger.info("Processing SSU_U03 message in thread:::::" + Thread.currentThread().getName());
		MSH msh1 = (MSH) statusMessage.get("MSH");
		EQU equ = (EQU) statusMessage.get("EQU");
		statusMessage.getSPECIMEN_CONTAINER();
		SSU_U03_SPECIMEN_CONTAINER sacContainer = statusMessage.getSPECIMEN_CONTAINER();
		NTE nte = (NTE) sacContainer.get("NTE");
		SAC sac = sacContainer.getSAC();
		SPM spm = sacContainer.getSPECIMEN().getSPM();
		List<OBX> sacObxList = sacContainer.getOBXAll();
		List<OBX> spmObxList = sacContainer.getSPECIMEN().getOBXAll();
		SpecimenStatusUpdateMessage specimenStatusUpdateDTO = new SpecimenStatusUpdateMessage();
		StatusUpdate statusUpdate = new StatusUpdate();
		ContainerInfo containerInfo = new ContainerInfo();
		SampleInfo sampleInfo = new SampleInfo();
		List<Consumable> consumables = new ArrayList<>();
		String status = null;

		if (!isRequiredFieldMissing(statusMessage)) {
			specimenStatusUpdateDTO
					.setDeviceSerialNumber(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue());
			specimenStatusUpdateDTO.setMessageType(EnumMessageType.StatusUpdateMessage);
			specimenStatusUpdateDTO
					.setDateTimeMessageGenerated(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue());
			specimenStatusUpdateDTO
					.setSendingApplicationName(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
			specimenStatusUpdateDTO
					.setReceivingApplication(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
			specimenStatusUpdateDTO.setMessageControlId(msh1.getMsh10_MessageControlID().getValue());

			statusUpdate.setEventDate(equ.getEqu2_EventDateTime().getTs1_Time().getValue());
			statusUpdate.setEquipmentState(equ.getEqu3_EquipmentState().getCe1_Identifier().getValue());
			statusUpdate.setComment(nte.getNte3_Comment(0).getValue());
			logger.info("Comment on the sample is :" + statusUpdate.getComment());

			containerInfo
					.setContainerIdentifier(sac.getSac3_ContainerIdentifier().getEi1_EntityIdentifier().getValue());
			if (sac.getSac8_ContainerStatus().getCe1_Identifier().getValue().equals("R")) {
				containerInfo.setContainerStatus("Process Completed");
			} else if (sac.getSac8_ContainerStatus().getCe1_Identifier().getValue().equals("O")) {
				containerInfo.setContainerStatus("In Process");
			}
			containerInfo.setCarrierBarcode(sac.getSac10_CarrierIdentifier().getEi1_EntityIdentifier().getValue());
			containerInfo.setCarrierPosition(sac.getSac11_PositionInCarrier().getValue1().getValue());
			containerInfo.setContainerVolume(sac.getSac21_ContainerVolume().getValue());
			containerInfo.setAvailableSpecimenVolume(sac.getSac22_AvailableSpecimenVolume().getValue());
			containerInfo.setInitialSpecimenVolume(sac.getSac23_InitialSpecimenVolume().getValue());
			containerInfo.setUnitofVolume(sac.getSac24_VolumeUnits().getCe1_Identifier().getValue());
			if (!StringUtils.isEmpty(sac.getSac9_CarrierType().getCe1_Identifier().getValue())) {
				String[] splitCarrierType = sac.getSac9_CarrierType().getCe1_Identifier().getValue().split("_");

				containerInfo.setCarrierType(splitCarrierType[0]);
				if (splitCarrierType.length >= 2) {
					containerInfo.setOutputPlateType(splitCarrierType[1]);
				}
			}

			String sampleOutput = spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()
					.getValue();
			if (!StringUtils.isEmpty(sampleOutput)) {
				String[] splitSampleOutput = sampleOutput.split("_");
				sampleInfo.setNewContainerId(splitSampleOutput[0]);
				if (splitSampleOutput.length >= 2) {
					sampleInfo.setNewOutputPosition(splitSampleOutput[1]);
				}
			}

			for (OBX obx : sacObxList) {
				String observationValue = obx.getObx5_ObservationValue(0).encode();

				if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("P")) {
					statusUpdate.setProtocolName(observationValue);
				} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("PV")) {
					statusUpdate.setProtocolVersion(observationValue);
				} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("RES")) {
					statusUpdate.setRunResult(observationValue);
					status = observationValue;
				} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()
						.equalsIgnoreCase("RuntimeRange")) {

					String startEndTime = obx.getObx5_ObservationValue(0).getData().encode();
					try {

						SimpleDateFormat runDateFormatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
						if ("inprogress".equalsIgnoreCase(status) || FAILED.equalsIgnoreCase(status)|| ABORTED.equalsIgnoreCase(status)) {

							statusUpdate.setRunStartTime(new Timestamp(runDateFormatter.parse(startEndTime).getTime()));
						} else {
							statusUpdate.setRunStartTime(new Timestamp(runDateFormatter
									.parse(startEndTime.substring(0,
											obx.getObx5_ObservationValue(0).getData().encode().indexOf('^')))
									.getTime()));
							statusUpdate.setRunEndTime(new Timestamp(runDateFormatter
									.parse(startEndTime.substring(
											obx.getObx5_ObservationValue(0).getData().encode().indexOf('^') + 1))
									.getTime()));
						}

					} catch (ParseException e) {
						logger.info("Parsing exception in:" + e.getMessage());
					}
				}
			}

			for (OBX obx : spmObxList) {

				String observationValue = obx.getObx5_ObservationValue(0).encode();
				if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("OrderName")) {
					statusUpdate.setOrderName(observationValue);
					statusUpdate.setFlag(obx.getObx8_AbnormalFlags(0).getValue());
					statusUpdate.setOperatorName(obx.getObx16_ResponsibleObserver(0).encode());
					logger.info("Operator name:" + statusUpdate.getOperatorName());
				} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()
						.equalsIgnoreCase("OrderResult")) {
					statusUpdate.setOrderResult(observationValue);
				} else if (obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue().equalsIgnoreCase("IC")) {
					statusUpdate.setInternalControls(observationValue);

				} else {
					Consumable consumable = new Consumable();
					consumable.setName(obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue());
					consumable.setValue(observationValue);
					consumables.add(consumable);

				}

			}

			String sampleType = spm.getSpm4_SpecimenType().getCwe2_Text().encode();
			sampleInfo.setSampleType(sampleType.replaceAll("·", " "));

			if (spm.getSpm11_SpecimenRole(0).getCwe1_Identifier().getValue().equalsIgnoreCase("Q")) {
				sampleInfo.setSpecimenRole("Control");
			} else {
				sampleInfo.setSpecimenRole("Patient");
			}
			sampleInfo.setSpecimenDescription(spm.getSpm14_SpecimenDescription(0).getValue());
			specimenStatusUpdateDTO.setContainerId(spm.getSpm3_SpecimenParentIDs(0).encode());
			statusUpdate.setUpdatedBy("System");
			statusUpdate.setConsumables(consumables);
			statusUpdate.setContainerInfo(containerInfo);
			statusUpdate.setSampleInfo(sampleInfo);
			specimenStatusUpdateDTO.setStatusUpdate(statusUpdate);

			ObjectMapper mapper = new ObjectMapper();
			try {
				logger.info(
						"SSU_U03 message converted to JSON :::::" + mapper.writeValueAsString(specimenStatusUpdateDTO));
				String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.LP24 + MESSAGE_TYPE + MessageType.LP24_U03
						+ DEVICEID + specimenStatusUpdateDTO.getDeviceSerialNumber();
				logger.info("QBP request URL to IMM :" + url);
				postRequest(url, Entity.entity(specimenStatusUpdateDTO, MediaType.APPLICATION_JSON));

			} catch (JsonGenerationException e) {
				logger.error("JsonGenerationException:::::" + e.getMessage());
			} catch (JsonMappingException e) {
				logger.error("JsonMappingException:::::" + e.getMessage());
			} catch (Exception e) {
				logger.error("Exception:::::" + e.getMessage());
				throw e;
			}
		} else {
			logger.info(" Preparing ACK for Required field missing .... ");

			this.sendResponseRequiredFieldMissing(statusMessage);
		}
	}

	public void sendResponseRequiredFieldMissing(SSU_U03 statusMessage) throws HL7Exception, IOException {

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
		setIpaddress(getLocalIP());
		createAuditBean("Ackforstatusupdate",ack.toString());
		exchange.getOut().setBody(ack);
		AsyncCallback callback = containerDTO.getCallback();
		callback.done(false);
	}

	public boolean isRequiredFieldMissing(SSU_U03 statusMessage) throws HL7Exception {

		logger.info(" ResponseHandlerService :: => isRequiredFielddMissing() ");
	    EnumMap<MandatoryFieldSSU, String> requiredFiled = new EnumMap<>(MandatoryFieldSSU.class);
		String noData = "Missing";
		MSH msh1 = (MSH) statusMessage.get("MSH");
		statusMessage.getSPECIMEN_CONTAINER();
		SSU_U03_SPECIMEN_CONTAINER sacContainer = statusMessage.getSPECIMEN_CONTAINER();
		SAC sac = sacContainer.getSAC();
		SPM spm = sacContainer.getSPECIMEN().getSPM();
		List<OBX> sacObxList = sacContainer.getOBXAll();
		List<OBX> spmObxList = sacContainer.getSPECIMEN().getOBXAll();

		requiredFiled.put(MandatoryFieldSSU.DEVICEID,
				Optional.ofNullable(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue()).map(String::trim)
						.filter(d -> !d.isEmpty()).orElse(noData));

		requiredFiled.put(MandatoryFieldSSU.SENDINGAPPLICATIONNAME,
				Optional.ofNullable(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue()).map(String::trim)
						.filter(d -> !d.isEmpty()).orElse(noData));

		requiredFiled.put(MandatoryFieldSSU.RUNRESULT,
				Optional.ofNullable(sacObxList.get(2).getObx5_ObservationValue(0).encode()).map(String::trim)
						.filter(d -> !d.isEmpty()).orElse(noData));

		requiredFiled.put(MandatoryFieldSSU.ORDERNAME,
				Optional.ofNullable(spmObxList.get(0).getObx5_ObservationValue(0).encode()).map(String::trim)
						.filter(d -> !d.isEmpty()).orElse(noData));

		requiredFiled.put(MandatoryFieldSSU.ORDERRESULT,
				Optional.ofNullable(spmObxList.get(1).getObx5_ObservationValue(0).encode()).map(String::trim)
						.filter(d -> !d.isEmpty()).orElse(noData));

		if (!(requiredFiled.get(MandatoryFieldSSU.ORDERRESULT).equalsIgnoreCase(FAILED)||requiredFiled.get(MandatoryFieldSSU.ORDERRESULT).equalsIgnoreCase(ABORTED))) {
			requiredFiled.put(MandatoryFieldSSU.ACCESSIONINGID,
					Optional.ofNullable(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier()
							.getEi1_EntityIdentifier().getValue()).map(String::trim).filter(d -> !d.isEmpty())
							.orElse(noData));
			requiredFiled.put(MandatoryFieldSSU.CARRIERBARCODE,
					Optional.ofNullable(sac.getSac10_CarrierIdentifier().getEi1_EntityIdentifier().getValue())
							.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData));
		}
		requiredFiled.put(MandatoryFieldSSU.SPM1CHECK3AND2,
				(requiredFiled.get(MandatoryFieldSSU.ORDERRESULT).equalsIgnoreCase(FAILED)||requiredFiled.get(MandatoryFieldSSU.ORDERRESULT).equalsIgnoreCase(ABORTED))
						? Optional.ofNullable(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier()
								.getEi1_EntityIdentifier().getValue()).map(String::trim).filter(d -> !d.isEmpty())
								.orElse(noData)
						: Optional
								.ofNullable(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier()
										.getEi1_EntityIdentifier().getValue())
								.map(String::trim).filter(d -> !d.isEmpty())
								.orElse(Optional
										.ofNullable(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier()
												.getEi1_EntityIdentifier().getValue())
										.map(String::trim).filter(d -> !d.isEmpty()).orElse(noData)));

		return validateRequiredDetails(requiredFiled, noData);

	}

	public boolean validateRequiredDetails(Map<MandatoryFieldSSU, String> mapped, String noData) {

        logger.info(" ResponseHandlerService :: ==> validateRequiredDetails ");
        
        Predicate<MandatoryFieldSSU> checker = k -> mapped.get(k).equalsIgnoreCase(noData);
        
        boolean result = mapped.keySet().stream().anyMatch(checker);
        
        if (result) {
            this.sendNotificationToADM(NotificationGroupType.MISSING_INFO_LP24.getGroupType(), mapped.get(MandatoryFieldSSU.ORDERNAME),
                mapped.get(MandatoryFieldSSU.DEVICEID));
        }
        
        return result;
    }

	/**
	 * Post request.
	 *
	 * @param url
	 *            the url
	 * @param entity
	 *            the entity
	 * @throws Exception
	 */
	public void postRequest(String url, Entity<Object> entity) throws Exception {
		try {
			DateFormat df = new SimpleDateFormat(SIMPLE_DATE_FORMAT_RMM);
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(df);

			logger.info("entity : \t" + mapper.writeValueAsString(entity));

			Builder builder = RestClientUtil.getBuilder(loginUrl, null);
			String token = builder.post(Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED),
					String.class);

			Builder builder2 = RestClientUtil.getBuilder(url, null);
			builder2.header("Cookie", "brownstoneauthcookie=" + token);
			Response resp = builder2.post(entity);
			logger.info("Response status from IMM: " + resp.getStatus());
		} catch (Exception e) {
			logger.info("Something went wrong: " + e.getMessage());
			throw e;
		}
	}


	public Message getNoOrderResponse(HL7HeaderSegmentDTO headerSegmentDTO) throws Exception {

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

			msh1.getMsh3_SendingApplication().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingApplication());
			msh1.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getReceivingFacility());
			msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().setValue(headerSegmentDTO.getSendingFacility());
			msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID()
					.setValue(headerSegmentDTO.getSendingApplicationName());
			msh1.getMsh5_ReceivingApplication().getHd2_UniversalID().setValue(headerSegmentDTO.getDeviceSerialNumber());
			msh1.getMsh5_ReceivingApplication().getHd3_UniversalIDType().setValue("M");
			msh1.getMsh10_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());
			msh1.getMsh7_DateTimeOfMessage().getTime().setValue(f.format(new Date()));
			msh1.getMsh11_ProcessingID().getProcessingID().setValue(headerSegmentDTO.getProcessingId());
			msh1.getMsh12_VersionID().getVersionID().setValue(headerSegmentDTO.getVersionId());

			msa.getMsa2_MessageControlID().setValue(headerSegmentDTO.getMessageControlId());

			qak.getQak3_MessageQueryName().getCe1_Identifier().setValue(headerSegmentDTO.getQueryDefDesc());
			qak.getQak3_MessageQueryName().getCe2_Text().setValue(headerSegmentDTO.getQueryDefEncodingSystem());
			qak.getQak3_MessageQueryName().getCe3_NameOfCodingSystem()
					.setValue(headerSegmentDTO.getQueryDefEncodingSystem());
			msa.getMsa1_AcknowledgmentCode().setValue("AR");

			qpd.getQpd1_MessageQueryName().getCe1_Identifier().setValue(headerSegmentDTO.getQueryDefDesc());
			qpd.getQpd1_MessageQueryName().getCe2_Text().setValue(headerSegmentDTO.getQueryDefDesc());
			qpd.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem()
					.setValue(headerSegmentDTO.getQueryDefEncodingSystem());
			qpd.getQpd2_QueryTag().setValue(headerSegmentDTO.getMessageControlId());
			qpd.getQpd3_UserParametersInsuccessivefields().parse(null);

			spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().setValue(null);

			spm.getSpm1_SetIDSPM().parse("1");
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

		} catch (IOException e) {
			logger.info(e.getMessage());
			throw new Exception();
		} 

	}

	public void sendACKU01ToLP24(@RequestBody ResponseDTO<MessagePayload> responseDTO) throws IOException {
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
			String serialNo = headerSegmentDTO.getDeviceSerialNumber();
			Exchange exchange = containerDTO.getExchange();
			IoSession session = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
			logger.info(CONNECTION_MSG_INFO + session.isActive());
			if (!session.isActive())
				deviceHandlerService.updateDeviceStatus(serialNo, OFFLINE);
			setIpaddress(getLocalIP());
			createAuditBean("AckforDevicestateupdate",queryMessage1.toString());
			exchange.getOut().setBody(queryMessage1);
			logger.info(exchange.getOut().getBody(String.class));
			AsyncCallback callback = containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getResponse().getMessageControlId());
		} catch (HL7Exception e) {
			logger.error(HL7_ERROR_MESSAGE);
		} catch (IOException e) {
			logger.error(IOEXCEPTION_ERROR_MESSAGE);
		}
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
		messagePayload.setMessageType(MessageType.LP24_STATUS_UPDATE);
		messagePayload.setSendingApplicationName(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
		messagePayload.setReceivingApplication(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
		logger.info("Comment on the sample is :" + statusUpdateDTO.getComment());
		statusUpdateDTO.setEquipmentState(equ.getEqu3_EquipmentState().getCe1_Identifier().getValue());
		statusUpdateDTO.setEventDate(equ.getEqu2_EventDateTime().getTs1_Time().getValue());
		statusUpdateDTO.setUpdatedBy("System");
		messagePayload.setStatusUpdate(statusUpdateDTO);
		try {
			responseDTO.setResponse(messagePayload);
			sendACKU01ToLP24(responseDTO);
		} catch (IOException e) {
			logger.error(IOEXCEPTION_ERROR_MESSAGE);
		}
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
