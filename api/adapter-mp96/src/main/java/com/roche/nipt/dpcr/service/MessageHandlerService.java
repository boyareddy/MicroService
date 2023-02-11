package com.roche.nipt.dpcr.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.Charsets;
import org.apache.mina.core.session.IoSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

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
import com.roche.connect.common.mp96.AdaptorACKMessage;
import com.roche.connect.common.mp96.OULACKMessage;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.connect.common.mp96.QueryResponseMessage;
import com.roche.connect.common.mp96.QueryResponseSample;
import com.roche.nipt.dpcr.dto.MessageExchangeDTO;
import com.roche.nipt.dpcr.util.InstanceUtil;
import com.roche.nipt.dpcr.util.RestClient;
import com.roche.nipt.dpcr.util.TokenUtils;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ACK;
import ca.uhn.hl7v2.model.v24.message.ORM_O01;
import ca.uhn.hl7v2.model.v24.segment.ERR;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

@Path("/rest/api/v1")
@Component
public class MessageHandlerService {

	private HMTPLogger log = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Value("${pas.login_entity}")
	private String loginEntity;

	@Value("${connect.imm_dpcr_url}")
	private String immUrl;
	
	@Value("${connect.adm_notification_url}")
	private String admUrl;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	DeviceHandlerService deviceHandlerService;

	private Response response;

	private static final char CARRIAGE_RETURN = 13;
	private static final String SOURCE_AND_DEVICETYPE = "?source=device&devicetype=";
	private static final String MESSAGE_TYPE = "&messagetype=";
	private static final String DEVICEID = "&deviceid=";
	
	private String ipAddress;


	public void convertQBPToPayload(QueryMessage queryMessage) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String qbpMessageJson = mapper.writeValueAsString(queryMessage);
		log.info("In MessageHandlerService Class - convertQBPPayload() --> " + qbpMessageJson);
		String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.MP96 + MESSAGE_TYPE + MessageType.MP96_QBP + DEVICEID
				+ queryMessage.getDeviceId();
		log.info("QBP request URL to IMM :" + url);
		try {
			int statusCode = postRequest(URLEncoder.encode(url, CharEncoding.UTF_8),
					Entity.entity(queryMessage, MediaType.APPLICATION_JSON));

			if (!(statusCode == 200 || statusCode == 202)) {
				throw new IOException();
			}

		} catch(Exception e) {
			
			MessageExchangeDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(queryMessage.getDeviceId());
			containerDTO.getExchange().getOut().setBody("");
			AsyncCallback callback = containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(queryMessage.getDeviceId());
			log.info("Something went wrong, No order found in OMM or no response from IMM " + e.getMessage());
		}

	}

	@Path("/sendrsptomp96")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendResponseToMP96(@RequestBody QueryResponseMessage responseDTO) throws IOException, HL7Exception {

		ObjectMapper mapper = new ObjectMapper();
		String woiMessageJson = mapper.writeValueAsString(responseDTO);
		log.info("In MessageHandlerService Class - sendResponseToMP96() --> " + woiMessageJson);

		Message ormMessage = fetchORMFile(responseDTO);
		log.info("work order import message sent in the below format" + "\n" + "----------------" + "\n" + ormMessage);
		MessageExchangeDTO containerDTO = InstanceUtil.getInstance().getConnectionMap().get(responseDTO.getDeviceID());
		InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getDeviceID());
		Exchange exchange = containerDTO.getExchange();
		String serialNo = responseDTO.getDeviceID();
		IoSession session = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
		if (!session.isActive())
			deviceHandlerService.updateDeviceStatus(serialNo, "offline");
		setIpAddress(getLocalIP());
		if(ormMessage!=null)
			createAuditBean("Workorderresponse",ormMessage.toString());
		else
			createAuditBean("Workorderresponse","");
		exchange.getOut().setBody(ormMessage);
		AsyncCallback callback = containerDTO.getCallback();
		callback.done(false);
		InstanceUtil.getInstance().getConnectionMap().remove(responseDTO.getDeviceID());

		return Response.status(javax.ws.rs.core.Response.Status.OK).build();
	}

	private ORM_O01 fetchORMFile(QueryResponseMessage responseDTO) throws IOException, HL7Exception {
		log.info("In MessageHandlerService Class - fetchORMFile() --> ");
		ResourceBundle mybundle = ResourceBundle.getBundle("application");
		String filePath = mybundle.getString("WOI_FILEPATH");
		   try (HapiContext context = new DefaultHapiContext()) {
		       resourceLoader = new DefaultResourceLoader();
		       Resource resource = resourceLoader.getResource(filePath);
		       InputStream stream = resource.getInputStream();
		       context.getExecutorService();
		       String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
		       Parser p = context.getPipeParser();
		       ORM_O01 responseMsg = (ORM_O01) p.parse(msg);
		       StringBuilder orm = constructMSHMessage(responseMsg, responseDTO)
		           .append(constructORCMessage(responseMsg, responseDTO))
		           .append(constructObservationMessage(responseMsg, responseDTO));
		       return (ORM_O01) p.parse(orm.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
		return null;

	}

	private StringBuilder constructMSHMessage(ORM_O01 ormMsg, QueryResponseMessage responseDTO)
			throws HL7Exception, IOException {
		log.info("In MessageHandlerService Class - constructMSHMessage() --> ");
		MSH msh = ormMsg.getMSH();
		StringBuilder mshString = new StringBuilder();
		msh.getMsh10_MessageControlID().setValue(responseDTO.getDeviceID());
		msh.getMsh7_DateTimeOfMessage().getTs1_TimeOfAnEvent().setValue(responseDTO.getDateTime());
		mshString.append(convertHl7toString(msh.encode())).append(CARRIAGE_RETURN);
		return mshString;

	}

	private StringBuilder constructORCMessage(ORM_O01 ormMsg, QueryResponseMessage responseDTO)
			throws HL7Exception, IOException {
		log.info("In MessageHandlerService Class - constructORCMessage() --> ");
		ORC orc = ormMsg.getORDER().getORC();
		StringBuilder orcString = new StringBuilder();
		orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(responseDTO.getRunId());
		orc.getOrc10_EnteredBy(0).getXcn1_IDNumber().setValue(responseDTO.getCreatedBy());
		orcString.append(convertHl7toString(orc.encode())).append(CARRIAGE_RETURN);
		return orcString;

	}

	private StringBuilder constructObservationMessage(ORM_O01 ormMsg, QueryResponseMessage responseDTO)
			throws HL7Exception, IOException {
		log.info("In MessageHandlerService Class - constructObservationMessage() --> ");
		OBR obr = ormMsg.getORDER().getORDER_DETAIL().getOBR();
		NTE nte = ormMsg.getORDER().getORDER_DETAIL().getNTE();
		StringBuilder obvString = new StringBuilder();
		int countSamples = 1;
		for (QueryResponseSample queryResponseSample : responseDTO.getSamples()) {
			obr.getObr1_SetIDOBR().setValue(String.valueOf(countSamples));
			obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(queryResponseSample.getAccessioningId());
			obr.getObr3_FillerOrderNumber().getEi1_EntityIdentifier().setValue(queryResponseSample.getContainerId());
			obr.getObr3_FillerOrderNumber().getEi2_NamespaceID().setValue(queryResponseSample.getPosition());
			obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().setValue(queryResponseSample.getReagentName());
			obr.getObr4_UniversalServiceIdentifier().getCe2_Text().setValue(queryResponseSample.getReagentVersion());
			obr.getObr15_SpecimenSource().getSps1_SpecimenSourceNameOrCode()
					.parse(queryResponseSample.getProtocolName());
			obr.getObr15_SpecimenSource().getSps2_Additives().setValue(queryResponseSample.getProtocolVersion());
			obr.getObr18_PlacerField1().setValue(queryResponseSample.getSampleVolume());
			obr.getObr19_PlacerField2().setValue(queryResponseSample.getEluateVolume());
			obvString.append(convertHl7toString(obr.encode())).append(CARRIAGE_RETURN);
			if (queryResponseSample.getComment() != null) {
				nte.getNte1_SetIDNTE().setValue(String.valueOf(countSamples));
				nte.getNte3_Comment(0).setValue(queryResponseSample.getComment().replaceAll("\\r\\n|\\r|\\n", " "));
				obvString.append(convertHl7toString(nte.encode())).append(CARRIAGE_RETURN);
			}
			countSamples++;

		}
		return obvString;
	}

	private String convertHl7toString(String message) throws IOException {
		InputStream messageResource = new ByteArrayInputStream(message.getBytes());
		return CharStreams.toString(new InputStreamReader(messageResource, Charsets.UTF_8)).replace('\n', '\r');
	}

	public int postRequest(String url, Entity entity) {

		try {
			
			String token=TokenUtils.getToken(true);
			Builder serviceBuilder = RestClientUtil.getBuilder(url, null);
			serviceBuilder.header("Cookie", "brownstoneauthcookie=" + token);
			response = serviceBuilder.post(entity);
			log.info("Response status from IMM: " + response.getStatus());
		} catch (Exception e) {
			log.error("postRequest() -->" + e.getMessage(), e);
		}

		return response.getStatus();
	}

	public void convertACKToPayload(AdaptorACKMessage adaptorACKMessage) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String ackMessageJson = mapper.writeValueAsString(adaptorACKMessage);
		log.info("In MessageHandlerService Class - convertACKToPayload() --> " + ackMessageJson);
		String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.MP96 + MESSAGE_TYPE + MessageType.MP96_ACK + DEVICEID
				+ adaptorACKMessage.getDeviceId();
		log.info("ACK request URL to IMM :" + url);
		try {
			postRequest(URLEncoder.encode(url, CharEncoding.UTF_8),
					Entity.entity(adaptorACKMessage, MediaType.APPLICATION_JSON));
			
			MessageExchangeDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(adaptorACKMessage.getDeviceId());
			InstanceUtil.getInstance().getConnectionMap().remove(adaptorACKMessage.getDeviceId());
			AsyncCallback callback = containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(adaptorACKMessage.getDeviceId());
		}  catch(Exception e ) {
			MessageExchangeDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(adaptorACKMessage.getDeviceId());
			InstanceUtil.getInstance().getConnectionMap().remove(adaptorACKMessage.getDeviceId());
			containerDTO.getExchange().getOut().setBody("");
			AsyncCallback callback = containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(adaptorACKMessage.getDeviceId());
			log.info("Something went wrong, Request from Adapter to IMM:: " + e.getMessage());
		}
	}

	public void convertOULToPayload(OULRunResultMessage oulRunResultMessage) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String oulMessageJson = mapper.writeValueAsString(oulRunResultMessage.toString());
		log.info("In MessageHandlerService Class - convertOULToPayload() --> " + oulMessageJson);
		String url = immUrl + SOURCE_AND_DEVICETYPE + DeviceType.MP96 + MESSAGE_TYPE + MessageType.MP96_OUL + DEVICEID
				+ oulRunResultMessage.getDeviceId();
		log.info("OUL request URL to IMM :" + url);
		try {
			int statusCode = postRequest(URLEncoder.encode(url, CharEncoding.UTF_8),
					Entity.entity(oulRunResultMessage, MediaType.APPLICATION_JSON));

			if (!(statusCode == 200 || statusCode == 202)) {
				throw new IOException();
			}

		}  catch(Exception e) {
			
			MessageExchangeDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
					.get(oulRunResultMessage.getDeviceId());
			InstanceUtil.getInstance().getConnectionMap().remove(oulRunResultMessage.getDeviceId());
			containerDTO.getExchange().getOut().setBody("");
			AsyncCallback callback = containerDTO.getCallback();
			callback.done(false);
			InstanceUtil.getInstance().getConnectionMap().remove(oulRunResultMessage.getDeviceId());
			log.info("Something went wrong, Request from Adapter to IMM:: " + e.getMessage());
		}
	}

	@Path("/sendacktomp96")
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendACKToMP96(@RequestBody OULACKMessage oulAckMessage) throws HL7Exception, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String oulAckMessageJson = mapper.writeValueAsString(oulAckMessage);
		log.info("In MessageHandlerService Class - sendResponseToMP96() --> " + oulAckMessageJson);

		Message ackMessage = fetchACKFile(oulAckMessage);
		log.info("ack message sent in the below format" + "\n" + "----------------" + "\n" + ackMessage);
		MessageExchangeDTO containerDTO = InstanceUtil.getInstance().getConnectionMap()
				.get(oulAckMessage.getDeviceRunId());
		InstanceUtil.getInstance().getConnectionMap().remove(oulAckMessage.getDeviceRunId());
		Exchange exchange = containerDTO.getExchange();
		String serialNo = oulAckMessage.getDeviceId();
		IoSession session = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
		if (!session.isActive())
			deviceHandlerService.updateDeviceStatus(serialNo, "offline");
		setIpAddress(getLocalIP());
		if(ackMessage!=null)
			createAuditBean("Ackforstatusupdate",ackMessage.toString());
		else
			createAuditBean("Ackforstatusupdate","");
		exchange.getOut().setBody(ackMessage);
		AsyncCallback callback = containerDTO.getCallback();
		callback.done(false);
		InstanceUtil.getInstance().getConnectionMap().remove(oulAckMessage.getDeviceRunId());
		return Response.status(javax.ws.rs.core.Response.Status.OK).build();
	}

	private Message fetchACKFile(OULACKMessage oulAckMessage) throws IOException, HL7Exception {
	    ACK ackMsg = null;
	    try (HapiContext context = new DefaultHapiContext()) {
		    context.getExecutorService();
            log.info("In MessageHandlerService Class - fetchACKFile() --> ");
            ResourceBundle mybundle = ResourceBundle.getBundle("application");
            String filePath = mybundle.getString("ACK_FILEPATH");
            resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(filePath);
            InputStream stream = resource.getInputStream();
            String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
            Parser p = context.getPipeParser();
            ACK ackMessage = (ACK) p.parse(msg);
            StringBuilder ack = constructACKMSHMessage(ackMessage, oulAckMessage)
            		.append(constructACKMSAMessage(ackMessage, oulAckMessage))
            		.append(constructACKERRMessage(ackMessage, oulAckMessage));
            ackMsg = (ACK) p.parse(ack.toString());
        } catch (Exception e) {
          log.error("error message in fetching ACK file:  "+e.getMessage());
        } 
		return ackMsg;
	}

	private Object constructACKMSAMessage(ACK ackMessage, OULACKMessage oulAckMessage)
			throws HL7Exception, IOException {
		log.info("In MessageHandlerService Class - constructACKMSAMessage() --> ");
		MSA msa = ackMessage.getMSA();
		StringBuilder msaString = new StringBuilder();
		msa.getMsa1_AcknowledgementCode().setValue(oulAckMessage.getStatus());
		msa.getMsa2_MessageControlID().setValue(oulAckMessage.getDeviceRunId());
		msaString.append(convertHl7toString(msa.encode())).append(CARRIAGE_RETURN);
		return msaString;
	}

	private StringBuilder constructACKMSHMessage(ACK ackMessage, OULACKMessage oulAckMessage)
			throws HL7Exception, IOException {
		log.info("In MessageHandlerService Class - constructACKMSHMessage() --> ");
		MSH msh = ackMessage.getMSH();
		StringBuilder mshString = new StringBuilder();
		msh.getMsh7_DateTimeOfMessage().getTs1_TimeOfAnEvent().setValue(oulAckMessage.getDateTimeMessageGenerated());
		msh.getMsh10_MessageControlID().setValue(oulAckMessage.getDateTimeMessageGenerated());
		mshString.append(convertHl7toString(msh.encode())).append(CARRIAGE_RETURN);
		return mshString;
	}

	private StringBuilder constructACKERRMessage(ACK ackMessage, OULACKMessage oulAckMessage)
			throws HL7Exception, IOException {
		ERR err = ackMessage.getERR();
		StringBuilder errString = new StringBuilder();

		if ("AR".equalsIgnoreCase(oulAckMessage.getStatus())) {
			Terser ter = new Terser(ackMessage);
			ter.set("/.ERR-3-1", oulAckMessage.getErrorCode());
			ter.set("/.ERR-3-2", oulAckMessage.getErrorDescription());
			ter.set("/.ERR-8-1", oulAckMessage.getErrorDescription());
			errString.append(convertHl7toString(err.encode()));
		}

		return errString;
	}
	
	public void sendNotification(String message, String errType) throws HMTPException {
		
		String token=TokenUtils.getToken(true);
		log.info("In MessageHandlerService Class - sendNotification() --> ");
		MessageDto messagedto = new MessageDto();
		messagedto.setMessageGroup(errType);
		List<String> errormessages = new ArrayList<>();
		errormessages.add(message);
		messagedto.setContents(errormessages);
		messagedto.setLocale("en_US");
		log.info("sendNotification for" + messagedto.getContents());
		try {
			RestClient.post(admUrl, messagedto, token, null);
			log.info("In sendNotification() - Notification send successfully!");

		} catch (Exception e) {
			log.error("AssayValidation: Error while sending notification :", e.getMessage());
			throw new HMTPException("Error while sending notification :" + e);
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
		auditData.setIpaddress(getIpAddress());
		String token =TokenUtils.getToken(true);
		PostAuditTrailEntry.postDataToAuditTrail(Entity.entity(auditData,MediaType.APPLICATION_JSON), token, ConfigurationParser.getString("pas.audit_create_url"));
		} catch (Exception e) {
			log.error("Audit Trail | error while posting audit data:: " +e.getMessage());
		}
					
	}
	
	private String getLocalIP() {
		String localIP="";
		try {
			localIP=InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("Error while getting IP address:: " +e.getMessage());
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
