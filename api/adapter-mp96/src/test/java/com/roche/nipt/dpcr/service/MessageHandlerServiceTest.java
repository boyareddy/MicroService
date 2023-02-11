package com.roche.nipt.dpcr.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.google.common.io.CharStreams;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.common.mp96.AdaptorACKMessage;
import com.roche.connect.common.mp96.OULACKMessage;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.mp96.OULSampleResultMessage;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.connect.common.mp96.QueryResponseMessage;
import com.roche.connect.common.mp96.QueryResponseSample;
import com.roche.nipt.dpcr.dto.MessageExchangeDTO;
import com.roche.nipt.dpcr.util.InstanceUtil;
import com.roche.nipt.dpcr.util.MP96ParamConfig;
import com.roche.nipt.dpcr.util.RestClient;
import com.roche.nipt.dpcr.util.TokenUtils;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.OUL_R21;
import ca.uhn.hl7v2.parser.PipeParser;

@PrepareForTest({ RestClientUtil.class, InstanceUtil.class, CharStreams.class, MP96ParamConfig.class,
		HMTPLoggerImpl.class,RestClient.class,TokenUtils.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
@TestPropertySource(properties = { "{pas.authenticate_url=abcx" })
public class MessageHandlerServiceTest {

	private static Logger log = LogManager.getLogger(MessageHandlerServiceTest.class);

	private static final char CARRIAGE_RETURN = 13;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	private ResourceLoader resourceLoader;
	@InjectMocks
	private MessageHandlerService messageHandlerService;
	@Mock
	private DeviceHandlerService deviceHandlerService;
	@Mock
	private Resource resource;
	@Mock
	ResourceLoader mockResourceLoader;
	@Mock
	PipeParser p;
	@Mock
	Response response;
	@Mock
	MessageExchangeDTO containerDTO;

	Logger logger = LogManager.getLogger(MessageHandlerServiceTest.class);

	@Mock
	OUL_R21 oulMessage;

	@Mock
	InputStream stream;
	@Mock
	InputStreamReader inputStreamReader;
	@Mock
	Message message;
	@Mock
	Exchange exchange;
	@Mock
	IoSession session;
	@Mock
	org.apache.camel.Message camelMessage;
	@Mock
	AsyncCallback callback;
	Map<String, IoSession> deviceMap = new HashMap<>();
	@Mock
	InstanceUtil instanceUtil;
	@Mock
	Map<String, MessageExchangeDTO> messageContainerDTO;

	String loginUrl = "http://localhost";
	String loginEntity = "http://localhost:98";
	String urlQBP = "null%3Fsource%3Ddevice%26devicetype%3DMagNA+Pure+96%26messagetype%3DQBP%26deviceid%3D12345";
	String urlACK = "null%3Fsource%3Ddevice%26devicetype%3DMagNA+Pure+96%26messagetype%3DACK%26deviceid%3D123456";
	String urlOUL = "null%3Fsource%3Ddevice%26devicetype%3DMagNA+Pure+96%26messagetype%3DOUL%26deviceid%3D1234567";
	@Mock
	Invocation.Builder loginBuilder;
	@Mock
	Invocation.Builder serviceBuilder;

	@BeforeTest
	public void setUp() throws Exception {
		Map<String, MessageExchangeDTO> connectionMap = new HashMap<>();
		connectionMap.put("key", containerDTO);
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
	}

	/**
	 * We need a special {@link IObjectFactory}.
	 * 
	 * @return {@link PowerMockObjectFactory}.
	 */
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@Test
	public void convertQBPToPayload() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(InstanceUtil.class);
		//ReflectionTestUtils.setField(messageHandlerService, "loginUrl", "http://localhost");
		ReflectionTestUtils.setField(messageHandlerService, "loginEntity", "http://localhost:98");
		Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(loginBuilder);
		javax.ws.rs.client.Entity<String> entity2 = javax.ws.rs.client.Entity.entity("http://loginEntity.com",
				MediaType.APPLICATION_FORM_URLENCODED);
		Mockito.when(loginBuilder.post(entity2, String.class)).thenReturn("token");
		Mockito.when(RestClientUtil.getBuilder(urlQBP, null)).thenReturn(serviceBuilder);
		String token = "token";
		Mockito.when(serviceBuilder.header("Cookie", "brownstoneauthcookie=" + token)).thenReturn(serviceBuilder);
		Mockito.when(serviceBuilder.post(Mockito.any(Entity.class))).thenReturn(response);
		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);
		Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
		Mockito.when(containerDTO.getExchange().getOut()).thenReturn(camelMessage);
		Mockito.when(containerDTO.getCallback()).thenReturn(callback);
		Mockito.doNothing().when(callback).done(false);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);
		messageHandlerService.convertQBPToPayload(getQueryMessage());
	}

	@Test
	public void sendResponseToMP96Test() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(CharStreams.class);
		PowerMockito.mockStatic(InstanceUtil.class);

		Mockito.when(resourceLoader.getResource(Mockito.anyString())).thenReturn(resource);
		Mockito.when(resource.getInputStream()).thenReturn(stream);
		Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class)))
				.thenReturn("MSH|^~\\&|||||20091126083614||ORM^O01|MPCZC8380G5K|P|2.4|\r\n"
						+ "ORC|1|Order790||||||||std\r\n"
						+ "OBR|1|Sample·ID·1|1234567890^A1|Cellular·RNA·LV^0.2|||||||||||Cellular·RNA·LV^0.6.4|||200|50|||\r\n"
						+ "NTE|1||Comment·Sample·1");

		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);
		Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
		Mockito.when(exchange.getIn()).thenReturn(camelMessage);
		Mockito.when(exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class)).thenReturn(session);
		Mockito.when(exchange.getOut()).thenReturn(camelMessage);
		Mockito.when(containerDTO.getCallback()).thenReturn(callback);
		Mockito.doNothing().when(callback).done(false);
		Mockito.when(deviceHandlerService.updateDeviceStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(true);
		messageHandlerService.sendResponseToMP96(getQueryResponseMessage());

	}

	@Test
	public void convertACKToPayload() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(InstanceUtil.class);
		//ReflectionTestUtils.setField(messageHandlerService, "loginUrl", "http://localhost");
		ReflectionTestUtils.setField(messageHandlerService, "loginEntity", "http://localhost:98");
		Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(loginBuilder);
		javax.ws.rs.client.Entity<String> entity2 = javax.ws.rs.client.Entity.entity("http://loginEntity.com",
				MediaType.APPLICATION_FORM_URLENCODED);
		Mockito.when(loginBuilder.post(entity2, String.class)).thenReturn("token");
		Mockito.when(RestClientUtil.getBuilder(urlACK, null)).thenReturn(serviceBuilder);
		String token = "token";
		Mockito.when(serviceBuilder.header("Cookie", "brownstoneauthcookie=" + token)).thenReturn(serviceBuilder);
		Mockito.when(serviceBuilder.post(Mockito.any(Entity.class))).thenReturn(response);
		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);
		Mockito.when(containerDTO.getCallback()).thenReturn(callback);
		Mockito.doNothing().when(callback).done(false);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);
		messageHandlerService.convertACKToPayload(getAdaptorACKMessage());

	}

	@Test
	public void convertOULToPayload() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(InstanceUtil.class);
		//ReflectionTestUtils.setField(messageHandlerService, "loginUrl", "http://localhost");
		ReflectionTestUtils.setField(messageHandlerService, "loginEntity", "http://localhost:98");
		Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(loginBuilder);
		javax.ws.rs.client.Entity<String> entity2 = javax.ws.rs.client.Entity.entity("http://loginEntity.com",
				MediaType.APPLICATION_FORM_URLENCODED);
		Mockito.when(loginBuilder.post(entity2, String.class)).thenReturn("token");
		Mockito.when(RestClientUtil.getBuilder(urlOUL, null)).thenReturn(serviceBuilder);
		String token = "token";
		Mockito.when(serviceBuilder.header("Cookie", "brownstoneauthcookie=" + token)).thenReturn(serviceBuilder);
		Mockito.when(serviceBuilder.post(Mockito.any(Entity.class))).thenReturn(response);
		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);
		Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
		Mockito.when(containerDTO.getExchange().getOut()).thenReturn(camelMessage);
		Mockito.when(containerDTO.getCallback()).thenReturn(callback);
		Mockito.doNothing().when(callback).done(false);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);
		messageHandlerService.convertOULToPayload(getOULRunResultMessage());
	}

	@Test
	public void sendACKToMP96() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(CharStreams.class);
		PowerMockito.mockStatic(InstanceUtil.class);

		Mockito.when(resourceLoader.getResource(Mockito.anyString())).thenReturn(resource);
		Mockito.when(resource.getInputStream()).thenReturn(stream);
		Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class)))
				.thenReturn("MSH|^~\\&|||||20091126142926||ACK|20091126142925|P|2.4\n" + "MSA|AA|Order792\n"
						+ "ERR|||102^Data type error^HL70357|||||Data type error");

		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);
		Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
		Mockito.when(exchange.getIn()).thenReturn(camelMessage);
		Mockito.when(exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class)).thenReturn(session);
		Mockito.when(exchange.getOut()).thenReturn(camelMessage);
		Mockito.when(containerDTO.getCallback()).thenReturn(callback);
		Mockito.doNothing().when(callback).done(false);
		messageHandlerService.sendACKToMP96(getOULACKMessage());
		messageHandlerService.sendACKToMP96(getOULACKMessageAppReject());

	}

	private OULACKMessage getOULACKMessage() {
		OULACKMessage oulackMessage = new OULACKMessage();
		oulackMessage.setDateTimeMessageGenerated("20091126142925");
		oulackMessage.setDeviceId("123456");
		oulackMessage.setDeviceRunId("123456");
		oulackMessage.setErrorCode("343");
		oulackMessage.setErrorDescription("radfa");
		oulackMessage.setStatus("AA");
		return oulackMessage;
	}

	private OULACKMessage getOULACKMessageAppReject() {
		OULACKMessage oulackMessage = new OULACKMessage();
		oulackMessage.setDateTimeMessageGenerated("20091126142925");
		oulackMessage.setDeviceId("123456");
		oulackMessage.setDeviceRunId("123456");
		oulackMessage.setErrorCode("343");
		oulackMessage.setErrorDescription("radfa");
		oulackMessage.setStatus("AR");
		return oulackMessage;
	}

	private OULRunResultMessage getOULRunResultMessage() {
		OULRunResultMessage oulRunResultMessage = new OULRunResultMessage();
		OULSampleResultMessage oulSampleResultMessage = new OULSampleResultMessage();
		List<OULSampleResultMessage> oulMessage = new ArrayList<>();
		messageHandlerService = new MessageHandlerService();
		oulRunResultMessage.setRunId("1234567");
		oulRunResultMessage.setDeviceRunId("1234567");
		oulRunResultMessage.setDeviceId("1234567");
		oulSampleResultMessage.setSampleComments("fasdfasdf");
		oulSampleResultMessage.setOperator("dagsfdga");
		oulSampleResultMessage.setAccessioningId("dgasfgas");
		oulSampleResultMessage.setOutputContainerId("gasdfg");
		oulSampleResultMessage.setReagentKitName("dfgdsfgfs");
		oulSampleResultMessage.setReagentVesion("obr.getObr4_UniversalServiceIdentifier().getCe2_Text().getValue()");
		oulSampleResultMessage.setRunStartTime("obr.getObr7_ObservationDateTime().getTs1_TimeOfAnEvent().getValue()");
		oulSampleResultMessage.setRunEndTime("obr.getObr8_ObservationEndDateTime().getTs1_TimeOfAnEvent().getValue()");
		oulSampleResultMessage.setOutputPlateType("obr.getObr13_RelevantClinicalInfo().getValue()");
		oulSampleResultMessage.setProtocal(
				"obr.getObr15_SpecimenSource().getSps1_SpecimenSourceNameOrCode().getCe1_Identifier().getValue()");
		oulSampleResultMessage.setSampleVolume("obr.getObr18_PlacerField1().getValue()");
		oulSampleResultMessage.setElevateVolume("obr.getObr19_PlacerField2().getValue()");
		oulSampleResultMessage.setSoftwareVersion("obr.getObr20_FillerField1().getValue()");
		oulSampleResultMessage.setSerialNo("obr.getObr21_FillerField2().getValue()");
		oulSampleResultMessage.setPosition("obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()");
		oulSampleResultMessage.setSampleResult("obx.getObx11_ObservationResultStatus().getValue()");
		oulRunResultMessage.setRunResultStatus("obx.getObx13_UserDefinedAccessChecks().getValue()");
		oulRunResultMessage.setRunComments("nte.getNte3_Comment(0).getValue()");
		oulMessage.add(oulSampleResultMessage);
		oulRunResultMessage.setOulSampleResultMessage(oulMessage);
		oulRunResultMessage.toString();
		return oulRunResultMessage;
	}

	private QueryMessage getQueryMessage() {
		QueryMessage queryMessage = new QueryMessage();
		queryMessage.setDeviceId("12345");
		queryMessage.setMessageType("qbp");
		queryMessage.setDeviceName("mp96");
		queryMessage.toString();
		return queryMessage;
	}

	private QueryResponseMessage getQueryResponseMessage() {
		QueryResponseMessage queryResponseMessage = new QueryResponseMessage();
		List<QueryResponseSample> listOfSamples = new ArrayList<>();
		QueryResponseSample queryResponseSample = new QueryResponseSample();
		MessageExchangeDTO messageExchangeDTO = new MessageExchangeDTO();
		messageExchangeDTO.getExchange();
		messageExchangeDTO.getCallback();
		queryResponseSample.setAccessioningId("d2nh2222232");
		queryResponseSample.setComment("sfdafd");
		queryResponseSample.setContainerId("DFa");
		queryResponseSample.setEluateVolume("fdfa");
		queryResponseSample.setPosition("dfa");
		queryResponseSample.setProtocolName("dfas");
		queryResponseSample.setProtocolVersion("fdsfd");
		queryResponseSample.setReagentName("GFD");
		queryResponseSample.setReagentVersion("dgfdsg");
		queryResponseSample.setSampleVolume("fabgsb");
		listOfSamples.add(queryResponseSample);
		queryResponseMessage.setSamples(listOfSamples);
		queryResponseMessage.setRunId("1");
		queryResponseMessage.setMessageType("ORM");
		queryResponseMessage.setDeviceID("12");
		queryResponseMessage.setDateTime("");
		queryResponseMessage.setCreatedBy("me");
		queryResponseMessage.toString();
		return queryResponseMessage;
	}

	private AdaptorACKMessage getAdaptorACKMessage() {
		AdaptorACKMessage adaptorACKMessage = new AdaptorACKMessage();
		adaptorACKMessage.setDeviceId("123456");
		// adaptorACKMessage.setAppStatus("fadfa");
		adaptorACKMessage.setDeviceRunId("243");
		adaptorACKMessage.toString();
		return adaptorACKMessage;

	}

	@Test
	public void sendNotificationTest() throws HMTPException, UnsupportedEncodingException {
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(loginBuilder);
		Mockito.when(loginBuilder.post(Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED), String.class))
				.thenReturn("token");
		String admHostUrl = "http://localhost";

		MessageDto messagedto = new MessageDto();
		messagedto.setMessageGroup("error");
		List<String> errormessages = new ArrayList<>();
		errormessages.add("message");
		messagedto.setContents(errormessages);
		messagedto.setLocale("en_US");

		Mockito.when(RestClient.post(admHostUrl + "/json/rest/api/v1/notification", messagedto, "token", null))
				.thenReturn(response);
		PowerMockito.mockStatic(TokenUtils.class);
		Mockito.when(TokenUtils.getToken(true)).thenReturn("token");
		messageHandlerService.sendNotification("message", "error");
	}
}
