/**
 * 
 */
package com.roche.nipt.dpcr.router;

import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.camel.impl.DefaultCamelContext;
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
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.mp96.AdaptorACKMessage;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.mp96.OULSampleResultMessage;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.nipt.dpcr.constant.CamelAdapterConstants;
import com.roche.nipt.dpcr.dto.MessageExchangeDTO;
import com.roche.nipt.dpcr.service.DeviceHandlerService;
import com.roche.nipt.dpcr.service.MessageHandlerService;
import com.roche.nipt.dpcr.util.InstanceUtil;
import com.roche.nipt.dpcr.util.MP96ParamConfig;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.ACK;
import ca.uhn.hl7v2.model.v24.message.OUL_R21;
import ca.uhn.hl7v2.model.v24.message.QBP_Q11;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.ORC;

/**
 * @author gosula.r
 *
 */
@Component
public class HL7Router extends RouteBuilder {

	private static final Logger logger = LogManager.getLogger(HL7Router.class);

	@Value("${server.port}")
	String serverPort;

	@Value("${camel.hl7router.endpoint}")
	String camelEndPoint;

	@Value("${connect.adm_notification_url}")
	private String admhosturl;
	
	@Value("${device.deviceTypeName}")
	private String deviceTypeName;
	
	@Value("${pas.login_entity}")
	private String loginEntity;

	@Value("${pas.authenticate_url}")
	private String loginUrl;
	
	@Value("${connect.adm_notification_url}")
	private String admNotificationURL;
	
	@Autowired
	CamelContext camelContext;

	@Autowired
	MessageHandlerService messageHandlerService;

	@Autowired
	DeviceHandlerService deviceHandlerService;
	
	private static final String UNSUPPORTED_HL7_MSG = "UNSUPPORTED_HL7_MSG_MP96";

	private static final String INVALID_VER = "INVALID_HL7_VER_MP96";

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
				logger.info("Exchange: " + exchange.getIn().getHeaders().values().toString());
			}
		});
		camelContext = new DefaultCamelContext();
		camelContext.addInterceptStrategy(tracer);

		MP96ParamConfig.getInstance().getMp96ConfigDetails();
		HL7DataFormat dataFormat = new HL7DataFormat();
		from(camelEndPoint).unmarshal(dataFormat).process(new AsyncProcessor() {

			@Override
			public void process(Exchange exchange) throws IOException {
				/** empty block of code --sonar issues */
			}

			@Override
			public boolean process(Exchange exchange, AsyncCallback callback) {

				String triggerEvent = "";
				MessageExchangeDTO messageExchangeDTO = new MessageExchangeDTO();
				String version ="";
				String serialNo="";
				try {
					String remoteHost = (exchange.getIn().getHeader(Mina2Constants.MINA_REMOTE_ADDRESS).toString()).split(":")[0];
					messageHandlerService.setIpAddress(remoteHost.substring(1));
					Message message = exchange.getIn().getBody(Message.class);
					logger.info("hl7 message sent in the below format" + "\n" + "----------------" + "\n"
							+ exchange.getIn().getBody(String.class));
					serialNo=message.getHeader(CamelAdapterConstants.CAMEL_HL7_SERIALNO).toString();
					 version = message.getHeader(CamelAdapterConstants.CAMEL_VERSION).toString();
					 if(!CamelAdapterConstants.VERSION.equalsIgnoreCase(version))
						 throw new IOException("Invalid Version");
					String msgType = message.getHeader(CamelAdapterConstants.CAMEL_HL7_MESSAGE_TYPE).toString();
					if (message.getHeader(CamelAdapterConstants.CAMEL_HL7_TRIGGER_EVENT) != null) {
						triggerEvent = message.getHeader(CamelAdapterConstants.CAMEL_HL7_TRIGGER_EVENT).toString();
					}

					String pId = message.getHeader(CamelAdapterConstants.CAMEL_HL7_PROCESSING_ID).toString();
					messageExchangeDTO.setExchange(exchange);
					messageExchangeDTO.setCallback(callback);
					ca.uhn.hl7v2.model.Message message1 = (ca.uhn.hl7v2.model.Message) exchange.getIn().getBody();
					String messagetype = "";
					if(msgType.equalsIgnoreCase(CamelAdapterConstants.QBP))
						messagetype = "Workorderrequest";
					else if(msgType.equalsIgnoreCase(CamelAdapterConstants.ACK))
						messagetype = "Ackforworkorderresponse";
					else if(msgType.equalsIgnoreCase(CamelAdapterConstants.OUL))
						messagetype = "Statusupdate";
					
					messageHandlerService.createAuditBean(messagetype, message1.toString());
					if (msgType.equalsIgnoreCase(CamelAdapterConstants.QBP)
							&& triggerEvent.equalsIgnoreCase(CamelAdapterConstants.Q11)
							&& (pId.equalsIgnoreCase(CamelAdapterConstants.P)
									|| pId.equalsIgnoreCase(CamelAdapterConstants.T))) {
						constructWorkOrderRequestMessage(messageExchangeDTO, message1);
					} else if (msgType.equalsIgnoreCase(CamelAdapterConstants.ACK)) {
						constructWorkOrderImportAckMessage(messageExchangeDTO, message1);
					} else if (msgType.equalsIgnoreCase(CamelAdapterConstants.OUL)
							&& triggerEvent.equalsIgnoreCase(CamelAdapterConstants.R21)
							&& (pId.equalsIgnoreCase(CamelAdapterConstants.P)
									|| pId.equalsIgnoreCase(CamelAdapterConstants.T))) {
						constructOulMessage(messageExchangeDTO, message1);
					} else {
						exchange.getOut().setBody("");
						callback = messageExchangeDTO.getCallback();
						callback.done(false);
						messageHandlerService.sendNotification(serialNo, UNSUPPORTED_HL7_MSG);
						logger.info("process stopped because of unspported messageType/eventType/processingId");
						logger.info("messageType : " + msgType + "----" + "eventType : " + triggerEvent + "----"
								+ "processingId : " + pId);
					}
				}  catch (HL7Exception e) {
					exchange.getOut().setBody("");
					callback = messageExchangeDTO.getCallback();
					callback.done(false);
				}catch (Exception e) {
					exchange.getOut().setBody("");
					callback.done(false);
					logger.info("Process stopped because of invalid HL7 version");
					
					try {
						messageHandlerService.sendNotification(serialNo, INVALID_VER);
					} catch (HMTPException e1) {
						logger.info("Process stopped because of invalid HL7 version : Error while posting notification");
					}
					logger.error("Unformatted HL7 Messasge recieved with error :" + e.getMessage());
				} 
				return false;
			}

			public String getToken() {
				Builder loginBuilder = RestClientUtil.getBuilder(loginUrl, null);
				Entity<String> entityString = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
				return loginBuilder.post(entityString, String.class);
			}
			private void constructWorkOrderRequestMessage(MessageExchangeDTO messageExchangeDTO,
					ca.uhn.hl7v2.model.Message message1) throws HL7Exception {
				QBP_Q11 qbp = (QBP_Q11) message1;
				try {
				    deviceHandlerService.setMessageTypeFlag(true);
					QueryMessage queryMessage = new QueryMessage();
					MSH msh = (MSH) qbp.get(CamelAdapterConstants.MSH);
					// added newly
					IoSession session = messageExchangeDTO.getExchange().getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
					if(!deviceTypeName.equalsIgnoreCase(msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue())) {
						session.closeNow();
						List<String> content=new ArrayList<>();
						content.add(msh.getMsh10_MessageControlID().getValue());
						AdmNotificationService.sendNotification(NotificationGroupType.INVALID_DEVICE_MODEL_MP96,content,getToken(),admNotificationURL);
						throw new HL7Exception("Device type mismatched, supported model is "+deviceTypeName);
					}
					if (!deviceHandlerService.deviceValidation(messageExchangeDTO.getExchange(), msh)) { 
						throw new HL7Exception("The unregistered device causes connection drop..");
					}

					String mshSegDeviceNo = msh.getMsh10_MessageControlID().getValue();
					queryMessage.setDeviceName(msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
					queryMessage.setDeviceId(msh.getMsh10_MessageControlID().getValue());
					queryMessage.setMessageType(msh.getMsh9_MessageType().getMessageType().getValue());
					queryMessage.setAssayType(deviceHandlerService.getDeviceAssaytype());
					InstanceUtil.getInstance().getConnectionMap().put(mshSegDeviceNo, messageExchangeDTO);
					MP96ParamConfig.getInstance().getMp96ConfigDetails();
					messageHandlerService.convertQBPToPayload(queryMessage);
				} catch (Exception e) {
					logger.error("HL7Exception at hl7 router qbp process message");
					throw new HL7Exception("The unregistered device causes connection drop : Closing Exchange "+e.getMessage());
				}
			}

			private void constructWorkOrderImportAckMessage(MessageExchangeDTO messageExchangeDTO,
					ca.uhn.hl7v2.model.Message message1) throws HL7Exception {
				try {
				    deviceHandlerService.setMessageTypeFlag(true);
					ACK ack = (ACK) message1;
					MSH msh = (MSH) ack.get(CamelAdapterConstants.MSH);
					IoSession session = messageExchangeDTO.getExchange().getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
					if(!deviceTypeName.equalsIgnoreCase(msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue())) {
						session.closeNow();
						List<String> content=new ArrayList<>();
						content.add(msh.getMsh10_MessageControlID().getValue());
						AdmNotificationService.sendNotification(NotificationGroupType.INVALID_DEVICE_MODEL_MP96,content,getToken(),admNotificationURL);
						throw new HL7Exception("Device type mismatched, supported model is "+deviceTypeName);
					}
					if (!deviceHandlerService.deviceValidation(messageExchangeDTO.getExchange(), msh)) {
						throw new HL7Exception("The unregistered device causes connection drop.");
					}
					MSA msa = (MSA) ack.get(CamelAdapterConstants.MSA);
					AdaptorACKMessage adaptorACKMessage = new AdaptorACKMessage();
					String mshSegDeviceNo = msh.getMsh10_MessageControlID().getValue();
					adaptorACKMessage.setDeviceId(msh.getMsh10_MessageControlID().getValue());
					adaptorACKMessage.setDeviceRunId(msa.getMsa2_MessageControlID().getValue());
					InstanceUtil.getInstance().getConnectionMap().put(mshSegDeviceNo, messageExchangeDTO);
					MP96ParamConfig.getInstance().getMp96ConfigDetails();
					messageHandlerService.convertACKToPayload(adaptorACKMessage);
				} catch (Exception e) {
					logger.error("HL7Exception at hl7 router ack process message");
					throw new HL7Exception("The unregistered device causes connection drop : Closing Exchange "+e.getMessage());
				}

			}

			private void constructOulMessage(MessageExchangeDTO messageExchangeDTO,
					ca.uhn.hl7v2.model.Message message1) throws HL7Exception {
				try {
				    deviceHandlerService.setMessageTypeFlag(false);
					OUL_R21 oul = (OUL_R21) message1;
					List<OULSampleResultMessage> oulMessage = new ArrayList<>();
					OULRunResultMessage oulRunResultMessage = new OULRunResultMessage();
					MSH msh = oul.getMSH();
					if (!deviceHandlerService.deviceValidation(messageExchangeDTO.getExchange(), msh)) {
						throw new HL7Exception("The unregistered device causes connection drop");
					}
					oulRunResultMessage.setMessageType(msh.getMsh9_MessageType().getMsg1_MessageType().getValue());
					oulRunResultMessage.setDeviceId(msh.getMsh10_MessageControlID().getValue());
					oulRunResultMessage.setSendingApplicationName(DeviceType.MP96);
					int samplesCount = oul.getORDER_OBSERVATIONReps();
					for (int i = 0; i < samplesCount; i++) {
						ORC orc = oul.getORDER_OBSERVATION(i).getORC();
						OBR obr = oul.getORDER_OBSERVATION(i).getOBR();
						OBX obx = oul.getORDER_OBSERVATION(i).getOBSERVATION().getOBX();
						NTE nte = oul.getORDER_OBSERVATION(i).getOBSERVATION().getNTE();
						OULSampleResultMessage oulSampleResultMessage = new OULSampleResultMessage();
						String runId = orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().getValue();
						oulRunResultMessage.setRunId(runId);
						oulRunResultMessage
								.setDeviceRunId(orc.getOrc2_PlacerOrderNumber().getEi2_NamespaceID().getValue());
						oulSampleResultMessage.setSampleComments(nte.getNte3_Comment(0).getValue());
						oulSampleResultMessage
								.setTime(orc.getOrc9_DateTimeOfTransaction().getTs1_TimeOfAnEvent().getValue());
						oulSampleResultMessage.setOperator(orc.getOrc10_EnteredBy(0).getXcn1_IDNumber().getValue());
						oulSampleResultMessage.setAccessioningId(
								obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().getValue());
						oulSampleResultMessage.setOutputContainerId(
								obr.getObr3_FillerOrderNumber().getEi1_EntityIdentifier().getValue());
						oulSampleResultMessage.setReagentKitName(
								obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().getValue());
						oulSampleResultMessage
								.setReagentVesion(obr.getObr4_UniversalServiceIdentifier().getCe2_Text().getValue());
						oulSampleResultMessage
								.setRunStartTime(obr.getObr7_ObservationDateTime().getTs1_TimeOfAnEvent().getValue());
						oulSampleResultMessage
								.setRunEndTime(obr.getObr8_ObservationEndDateTime().getTs1_TimeOfAnEvent().getValue());
						oulSampleResultMessage.setOutputPlateType(obr.getObr13_RelevantClinicalInfo().getValue());
						oulSampleResultMessage.setProtocal(obr.getObr15_SpecimenSource()
								.getSps1_SpecimenSourceNameOrCode().getCe1_Identifier().getValue());
						oulSampleResultMessage.setSampleVolume(obr.getObr18_PlacerField1().getValue());
						oulSampleResultMessage.setElevateVolume(obr.getObr19_PlacerField2().getValue());
						oulSampleResultMessage.setSoftwareVersion(obr.getObr20_FillerField1().getValue());
						oulSampleResultMessage.setSerialNo(obr.getObr21_FillerField2().getValue());
						oulSampleResultMessage
								.setPosition(obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue());
						oulSampleResultMessage.setSampleResult(obx.getObx11_ObservationResultStatus().getValue());
						oulRunResultMessage.setRunResultStatus(obx.getObx13_UserDefinedAccessChecks().getValue());
						oulRunResultMessage
								.setRunComments(orc.getOrc3_FillerOrderNumber().getEi1_EntityIdentifier().getValue());
						oulSampleResultMessage.setFlag(obx.getObx4_ObservationSubId().getValue());
						String[] obx5Values = obx.getObx5_ObservationValue(0).encode().split("\\^");
						if (obx5Values.length >= 1 && StringUtils.isNotBlank(obx5Values[0]))
							oulSampleResultMessage.setVolume(obx5Values[0]);
						if (obx5Values.length >= 2 && StringUtils.isNotBlank(obx5Values[1]))
							oulSampleResultMessage.setBarcode(obx5Values[1]);
						if (obx5Values.length >= 3 && StringUtils.isNotBlank(obx5Values[2]))
							oulSampleResultMessage.setExpDate(obx5Values[2]);
						if (obx5Values.length >= 4 && StringUtils.isNotBlank(obx5Values[3]))
							oulSampleResultMessage.setLotNo(obx5Values[3]);

						oulMessage.add(oulSampleResultMessage);

					}
					oulRunResultMessage.setOulSampleResultMessage(oulMessage);
					InstanceUtil.getInstance().getConnectionMap().put(oulRunResultMessage.getRunId(),
							messageExchangeDTO);
					MP96ParamConfig.getInstance().getMp96ConfigDetails();
					messageHandlerService.convertOULToPayload(oulRunResultMessage);
				} catch (Exception e) {
					logger.error("HL7Exception at hl7 router qbp process message");
					throw new HL7Exception("The unregistered device causes connection drop : Closing Exchange");
				}

			}
			
		});

	}

}
