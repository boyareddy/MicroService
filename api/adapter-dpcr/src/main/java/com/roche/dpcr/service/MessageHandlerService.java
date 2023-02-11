package com.roche.dpcr.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.Charsets;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.common.server.service.AuditTrailEntity;
import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.roche.common.audittrail.PostAuditTrailEntry;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.Assay;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.dpcr_analyzer.QueryMessage;
import com.roche.connect.common.dpcr_analyzer.QueryResponseMessage;
import com.roche.dpcr.dto.MessageExchange;
import com.roche.dpcr.util.InstanceUtil;
import com.roche.dpcr.util.RestClient;
import com.roche.dpcr.util.TokenUtils;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.group.OML_O21_ORDER;
import ca.uhn.hl7v2.model.v26.message.ACK;
import ca.uhn.hl7v2.model.v26.message.OML_O21;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.OBR;
import ca.uhn.hl7v2.model.v26.segment.ORC;
import ca.uhn.hl7v2.model.v26.segment.SAC;
import ca.uhn.hl7v2.model.v26.segment.SPM;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

@Path("/rest/api/v1")
@Component
public class MessageHandlerService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	private static final String SOURCE_AND_DEVICETYPE = "?source=device&devicetype=";
	private static final String MESSAGE_TYPE = "&messagetype=";
	private static final String DEVICEID = "&deviceid=";
	private static final String SIMPLE_DATE_FORMAT = "yyyyMMddHHmmss";
	private static final String ORDER = "/.ORDER(";
	private static final String OBV = ")/OBSERVATION_REQUEST/OBR-4(";

	private static final String ADM_NOTIFICATION_URL = "/json/rest/api/v1/notification";

	@Autowired
	private ResourceLoader resourceLoader;

	private String immUrl;

	private Response response;

	private String admHostUrl;

	private String loginEntity;

	@Value("${connect.imm_dpcr_url}")
	public void setImmUrl(String immUrl) {
		this.immUrl = immUrl;
	}

	public String getLoginEntity() {
		return loginEntity;
	}

	@Value("${pas.login_entity}")
	public void setLoginEntity(String loginEntity) {
		this.loginEntity = loginEntity;
	}

	@Value("${connect.adm_host_url}")
	public void setAdmHostUrl(String admHostUrl) {
		this.admHostUrl = admHostUrl;
	}

	private String ipAddress;
	
	public void sendQueryToIMM(QueryMessage queryMessage) throws JsonProcessingException, UnsupportedEncodingException {
		logger.info("in sendQueryToIMM() -> ");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryMessage);
		logger.info("In MessageHandlerService Class - sendQueryToIMM() --> "
				+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryMessage));
		String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.DPCR_ANALYZER + MESSAGE_TYPE
				+ MessageType.DPCR_ANALYZER_QBP + DEVICEID + queryMessage.getDeviceId();
		logger.info("QBP request URL to IMM :" + url);
		postRequest(URLEncoder.encode(url, CharEncoding.UTF_8),
				Entity.entity(queryMessage, MediaType.APPLICATION_JSON));

	}

	public void sendESUToIMM(ESUMessage esuMessage) throws JsonProcessingException, UnsupportedEncodingException {
		logger.info("In MessageHandlerService Class - sendESUToIMM() -> ");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValueAsString(esuMessage);
		logger.info("sendESUToIMM() --> "
				+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(esuMessage));
		String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.DPCR_ANALYZER + MESSAGE_TYPE
				+ MessageType.DPCR_ANALYZER_ESU + DEVICEID + esuMessage.getDeviceId();
		logger.info("ESU request URL to IMM :" + url);
		postRequest(URLEncoder.encode(url, CharEncoding.UTF_8), Entity.entity(esuMessage, MediaType.APPLICATION_JSON));

	}

	public void sendOMLACKToIMM(AcknowledgementMessage acknowledgementMessage)
			throws JsonProcessingException, UnsupportedEncodingException {
		logger.info("In MessageHandlerService Class - sendOMLACKToIMM() -> ");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValueAsString(acknowledgementMessage);
		logger.info("sendOMLACKToIMM() --> "
				+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(acknowledgementMessage));
		String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.DPCR_ANALYZER + MESSAGE_TYPE
				+ MessageType.DPCR_ANALYZER_ACK + DEVICEID + acknowledgementMessage.getDeviceSerialNo();
		logger.info("OML ACK request URL to IMM :" + url);
		postRequest(URLEncoder.encode(url, CharEncoding.UTF_8),
				Entity.entity(acknowledgementMessage, MediaType.APPLICATION_JSON));

	}

	@Path("/sendacktodpcr")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendACKToDpcr(@RequestBody AcknowledgementMessage queryAckMessage)
			throws HL7Exception, IOException {
		logger.info("In MessageHandlerService Class - sendACKToDpcr() -->");
		ObjectMapper mapper = new ObjectMapper();
		MessageExchange messageExchange = InstanceUtil.getInstance().getConnectionMap()
				.get(queryAckMessage.getMessageControlId());
		logger.info("In sendACKToDpcr(), ACK JSON From IMM --> "
				+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryAckMessage));
		ACK ackMessage = (ACK) fetchACKU01File();
		if(ackMessage != null) {
		constructACKMSHSegment(queryAckMessage, messageExchange, ackMessage);
		constructACKMSASegment(queryAckMessage, ackMessage);
		String acknowledgementMsg = "\u000b" + ackMessage.toString().trim() + "\u001c" + "\r";
		logger.info("In MessageHandlerService Class - sendACKToDpcr() HL7 formed-->"+acknowledgementMsg);
		setIpAddress(getLocalIP());
		if("Z01".equalsIgnoreCase(messageExchange.getAckTriggerEvent()))
			createAuditBean("Ackforworkorderrequest",acknowledgementMsg);
		else if("R01".equalsIgnoreCase(messageExchange.getAckTriggerEvent()))
			createAuditBean("Ackforresultupdate",acknowledgementMsg);
		else if("U01".equalsIgnoreCase(messageExchange.getAckTriggerEvent()))
			createAuditBean("AckforDevicestateupdate",acknowledgementMsg);
		messageExchange.getOut().write(acknowledgementMsg.getBytes());
		}
		if("Z01".equalsIgnoreCase(messageExchange.getAckTriggerEvent()) && !"AR".equalsIgnoreCase(queryAckMessage.getStatus())) {
			messageExchange.getOut().flush();
			messageExchange.setOut(messageExchange.getOut());
		}else {
			messageExchange.getOut().close();
			InstanceUtil.getInstance().getConnectionMap().remove(queryAckMessage.getMessageControlId());
		} 

		return Response.status(javax.ws.rs.core.Response.Status.OK).build();
	}

	private void constructACKMSASegment(AcknowledgementMessage queryAckMessage, ACK ackMessage) throws HL7Exception {
		logger.info("In MessageHandlerService Class - constructACKMSASegment() -->");
		ackMessage.getMSA().getMsa1_AcknowledgmentCode().setValue(queryAckMessage.getStatus());
		ackMessage.getMSA().getMsa2_MessageControlID().setValue(queryAckMessage.getMessageControlId());
		
		if ("AR".equalsIgnoreCase(queryAckMessage.getStatus())) {
			try {
				ackMessage.getMSA().getTextMessage().setValue(queryAckMessage.getErrorCode());
				ackMessage.getERR().clear();
			} catch (Exception e) {
				logger.error("constructACKMSASegment() -->" + e.getLocalizedMessage());
				InstanceUtil.getInstance().getConnectionMap().remove(queryAckMessage.getMessageControlId());
			}
		} else {
			ackMessage.getERR().clear();
		}
	}

	private void constructACKMSHSegment(AcknowledgementMessage queryAckMessage, MessageExchange messageExchange,
			ACK ackMessage) throws DataTypeException {
		logger.info("In MessageHandlerService Class - constructACKMSHSegment() -->");
		try {
			SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
			ackMessage.getMSH().getSendingApplication().getHd1_NamespaceID().setValue(messageExchange.getSendingApp());
			ackMessage.getMSH().getSendingFacility().getHd1_NamespaceID().clear();
			ackMessage.getMSH().getReceivingApplication().getHd1_NamespaceID()
					.setValue(messageExchange.getRecevingApp());
			ackMessage.getMSH().getReceivingFacility().getHd1_NamespaceID().clear();
			ackMessage.getMSH().getReceivingApplication().getHd2_UniversalID().clear();
			ackMessage.getMSH().getMessageControlID().setValue(queryAckMessage.getMessageControlId());
			ackMessage.getMSH().getDateTimeOfMessage().setValue(f.format(new Date()));
			ackMessage.getMSH().getVersionID().getVid1_VersionID().setValue(messageExchange.getMsgVersion());
			ackMessage.getMSH().getMessageType().getMsg2_TriggerEvent().setValue(messageExchange.getAckTriggerEvent());
		} catch (Exception e) {
			logger.error("constructACKMSHSegment() -->" + e.getLocalizedMessage());
		}
	}

	private Message fetchACKU01File() throws IOException, HL7Exception {
		logger.info("In MessageHandlerService Class - fetchACKU01File() -->");
		try (HapiContext context = new DefaultHapiContext()) {
			ResourceBundle mybundle = ResourceBundle.getBundle("application");
			String filePath = mybundle.getString("ACK_U01_FILEPATH");
			resourceLoader = new DefaultResourceLoader();
			Resource resource = resourceLoader.getResource(filePath);
			InputStream stream = resource.getInputStream();
			context.getExecutorService();
			String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
			Parser p = context.getPipeParser();
			return p.parse(msg);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Path("/sendomltodpcr")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendOMLToDpcr(@RequestBody QueryResponseMessage queryResposneMessage)
			throws HL7Exception, IOException {
		logger.info("In MessageHandlerService Class - sendOMLToDpcr() -->");
		ObjectMapper mapper = new ObjectMapper();
		MessageExchange messageExchange = InstanceUtil.getInstance().getConnectionMap()
				.get(queryResposneMessage.getMessageControlId());
		logger.info("In sendOMLToDpcr(), OML JSON From IMM --> "
				+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryResposneMessage));
		SimpleDateFormat f = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
		OML_O21 omlMsg = new OML_O21();
		omlMsg.initQuickstart("OML", "O21", "P");
		constructOMLMSHSegment(queryResposneMessage, messageExchange, f, omlMsg);
		constructOMLOrderSegments(queryResposneMessage, omlMsg ,messageExchange);
		String omlMessage = "\u000b" + omlMsg.toString() + "\u001c" + "\r";
		logger.info("In sendOMLToDpcr() --> omlMessage : " +omlMessage);
		setIpAddress(getLocalIP());
		createAuditBean("Workorderresponse", omlMessage);
		messageExchange.getOut().write(omlMessage.getBytes());
		messageExchange.getOut().close();
		messageExchange.setOut(messageExchange.getOut());
		return Response.status(javax.ws.rs.core.Response.Status.OK).build();

	}

	private void constructOMLOrderSegments(QueryResponseMessage queryResposneMessage, OML_O21 omlMsg, MessageExchange messageExchange)
			throws HL7Exception {
		logger.info("In MessageHandlerService Class - constructOMLOrderSegments() --> ");
		try {
			OML_O21_ORDER omlOrder = null;
			ORC orcSegment = null;
			OBR obrSegment = null;
			SPM spmSegment = null;
			SAC sacSegment = null;
			Terser terser = new Terser(omlMsg);
			for (int i = 0; i < queryResposneMessage.getSamples().size(); i++) {
				omlOrder = omlMsg.getORDER(i);
				orcSegment = omlOrder.getORC();
				obrSegment = omlOrder.getOBSERVATION_REQUEST().getOBR();
				spmSegment = omlOrder.getOBSERVATION_REQUEST().getSPECIMEN().getSPM();
				sacSegment = omlOrder.getOBSERVATION_REQUEST().getSPECIMEN().getCONTAINER().getSAC();
				constructOMLORCSegment(queryResposneMessage, orcSegment, i, messageExchange);
				constructOMLOBRSegment(queryResposneMessage, obrSegment, i, terser);
				constructOMLSPMSegment(queryResposneMessage, spmSegment, i);
				constructOMLSACSegment(queryResposneMessage, sacSegment, i);
			}
		} catch (Exception e) {
			logger.error("constructOMLOrderSegments() -->" + e.getLocalizedMessage());
		}
	}

	private void constructOMLSACSegment(QueryResponseMessage queryResposneMessage, SAC sacSegment, int i)
			throws HL7Exception {
		logger.info("In MessageHandlerService Class - constructOMLSACSegment() --> ");
		try {
			sacSegment.getContainerStatus().getCwe1_Identifier().setValue("P");
			sacSegment.getCarrierIdentifier().getEi1_EntityIdentifier()
					.setValue(queryResposneMessage.getSamples().get(i).getContainerId());
			sacSegment.getPositionInCarrier().getNa1_Value1()
					.setValue(queryResposneMessage.getSamples().get(i).getPosition());
		} catch (Exception e) {
			logger.error("constructOMLSACSegment() -->" + e.getMessage(), e);
		}
	}

	private void constructOMLSPMSegment(QueryResponseMessage queryResposneMessage, SPM spmSegment, int i)
			throws HL7Exception {
		logger.info("In MessageHandlerService Class - constructOMLSPMSegment() --> ");
		try {
			spmSegment.getSetIDSPM().setValue(String.valueOf(i + 1));
			spmSegment.getSpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()
					.setValue(queryResposneMessage.getSamples().get(i).getAccessioningId());
			spmSegment.getSpecimenType().getCwe1_Identifier().setValue("SMP");
		} catch (Exception e) {
			logger.error("constructOMLSPMSegment() -->" + e.getMessage(), e);
		}
	}

	private void constructOMLOBRSegment(QueryResponseMessage queryResposneMessage, OBR obrSegment, int i, Terser terser)
			throws HL7Exception {
		logger.info("In MessageHandlerService Class - constructOMLOBRSegment() --> " +obrSegment.encode());
		try {
			obrSegment.getSetIDOBR().setValue(String.valueOf(i + 1));
			obrSegment.getPlacerOrderNumber().getEi1_EntityIdentifier()
					.setValue(queryResposneMessage.getSamples().get(i).getAccessioningId());
			for (int j = 0; j < queryResposneMessage.getSamples().get(i).getAssay().size(); j++) {
				Assay assay = queryResposneMessage.getSamples().get(i).getAssay().get(j);
				terser.set(ORDER+i+OBV + j + ")-4", assay.getName());
				terser.set(ORDER+i+OBV + j + ")-5", assay.getVersion());
				terser.set(ORDER+i+OBV + j + ")-6", assay.getMasterMix());
				terser.set(ORDER+i+OBV + j + ")-7", assay.getKit());
			}
		} catch (Exception e) {
			logger.error("constructOMLOBRSegment() -->" + e.getMessage(), e);
		}

	}

	private void constructOMLORCSegment(QueryResponseMessage queryResposneMessage, ORC orcSegment, int i, MessageExchange messageExchange)
			throws HL7Exception {
		logger.info("In MessageHandlerService Class - constructOMLORCSegment() --> ");
		try {
			orcSegment.getOrderControl().setValue("NW");
			orcSegment.getPlacerOrderNumber().getEi1_EntityIdentifier()
					.setValue(queryResposneMessage.getSamples().get(i).getAccessioningId());
			orcSegment.getPlacerGroupNumber().getEi1_EntityIdentifier()
					.setValue(queryResposneMessage.getRunId());
			messageExchange.setRunId(queryResposneMessage.getRunId());
		} catch (Exception e) {
			logger.error("constructOMLORCSegment() -->" + e.getMessage(), e);
		}
	}

	private void constructOMLMSHSegment(QueryResponseMessage queryResposneMessage, MessageExchange messageExchange,
			SimpleDateFormat f, OML_O21 omlMsg) throws HL7Exception {
		logger.info("In MessageHandlerService Class - constructOMLMSHSegment() --> ");
		try {
			MSH mshSegment = omlMsg.getMSH();
			mshSegment.getSendingApplication().getHd1_NamespaceID().setValue(messageExchange.getSendingApp());
			mshSegment.getSendingFacility().getHd1_NamespaceID().clear();
			mshSegment.getReceivingApplication().getHd1_NamespaceID().setValue(messageExchange.getRecevingApp());
			mshSegment.getReceivingApplication().getHd2_UniversalID().clear();
			mshSegment.getReceivingFacility().getHd1_NamespaceID().clear();
			mshSegment.getDateTimeOfMessage().setValue(f.format(new Date()));
			mshSegment.getMessageControlID().setValue(queryResposneMessage.getMessageControlId());
			mshSegment.getVersionID().getVid1_VersionID().setValue(messageExchange.getMsgVersion());
			mshSegment.getMessageType().getMsg3_MessageStructure().clear();
		} catch (Exception e) {
			logger.error("constructOMLMSHSegment() -->" + e.getMessage(), e);
		}
	}

	public int postRequest(String url, Entity entity) {
		try {
			
			String token=TokenUtils.getToken(true);
			
			Builder serviceBuilder = RestClientUtil.getBuilder(url, null);
			serviceBuilder.header("Cookie", "brownstoneauthcookie=" + token);
			response = serviceBuilder.post(entity);
			logger.info("Response status from IMM: " + response.getStatus());
		} catch (Exception e) {
			logger.error("postRequest() -->" + e.getMessage(), e);
		}

		return response.getStatus();
	}

	public void sendNotification(String message, String errType) throws HMTPException {
		logger.info("In MessageHandlerService Class - sendNotification() --> ");
		
		String token=TokenUtils.getToken(true);
		
		MessageDto messagedto = new MessageDto();
		messagedto.setMessageGroup(errType);
		List<String> errormessages = new ArrayList<>();
		errormessages.add(message);
		messagedto.setContents(errormessages);
		messagedto.setLocale("en_US");
		logger.info("sendNotification for" + messagedto.getContents());
		try {
			RestClient.post(admHostUrl + ADM_NOTIFICATION_URL, messagedto, token, null);
			logger.info("In sendNotification() - Notification send successfully!");

		} catch (Exception e) {
			logger.error("AssayValidation: Error while sending notification :", e.getMessage());
			throw new HMTPException("Error while sending notification :" + e);
		}
	}
	
	public void sendORUToIMM(ORUMessage oRUMessage) throws JsonProcessingException, UnsupportedEncodingException {
		logger.info("in sendORUToIMM() -> ");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oRUMessage);
		logger.info("In MessageHandlerService Class - sendORUToIMM() --> "
				+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oRUMessage));
		String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.DPCR_ANALYZER + MESSAGE_TYPE
				+ MessageType.DPCR_ANALYZER_ORU + DEVICEID + oRUMessage.getDeviceId();
		logger.info("ORU request URL to IMM :" + url);
		postRequest(URLEncoder.encode(url, CharEncoding.UTF_8),
				Entity.entity(oRUMessage, MediaType.APPLICATION_JSON));

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
		auditData.setIpaddress(getIpAddress());
		String token =TokenUtils.getToken(true);
		
		PostAuditTrailEntry.postDataToAuditTrail(Entity.entity(auditData,MediaType.APPLICATION_JSON), token, ConfigurationParser.getString("pas.audit_create_url"));
		} catch (Exception e) {
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


}
