package com.roche.dpcr.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.io.CharStreams;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.common.server.service.AuditTrailEntity;
import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.roche.common.audittrail.PostAuditTrailEntry;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.Assay;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUAssay;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUPartitionEngine;
import com.roche.connect.common.dpcr_analyzer.ORUSampleDetails;
import com.roche.connect.common.dpcr_analyzer.QueryMessage;
import com.roche.connect.common.dpcr_analyzer.QueryResponseMessage;
import com.roche.connect.common.dpcr_analyzer.QueryResponseSample;
import com.roche.dpcr.dto.MessageExchange;
import com.roche.dpcr.util.InstanceUtil;
import com.roche.dpcr.util.RestClient;
import com.roche.dpcr.util.TokenUtils;

import ca.uhn.hl7v2.HL7Exception;

@PrepareForTest({ RestClientUtil.class, CharStreams.class, HMTPLoggerImpl.class, InstanceUtil.class ,RestClient.class,TokenUtils.class,ConfigurationParser.class,PostAuditTrailEntry.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class MessageHandlerServiceTest {

	@InjectMocks
	MessageHandlerService messageHandlerService;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	MessageExchange messageExchange;
	@Mock
	Map<String, MessageExchange> connectionMap;
	@Mock
	InstanceUtil instanceUtil;
	@Mock
	OutputStream outputStream;

	@BeforeTest
	public void setUp() throws Exception {

	}

	public void instanceUtilMock() throws IOException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		PowerMockito.mockStatic(InstanceUtil.class);
		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(connectionMap);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString()))
				.thenReturn(messageExchange);
		Mockito.when(messageExchange.getOut()).thenReturn(outputStream);
		Mockito.doNothing().when(outputStream).flush();
		messageHandlerService.setImmUrl("http://localhost");
		messageHandlerService.setLoginEntity("http://localhost");
		messageHandlerService.setAdmHostUrl("http://localhost");
		System.out.println(messageHandlerService.getLoginEntity());

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
	public void sendACKToDpcr() throws HL7Exception, IOException {
		instanceUtilMock();
		messageHandlerService.sendACKToDpcr(getQueryAckMessage());
		messageHandlerService.sendACKToDpcr(getQueryAckMessageAppReject());
	}

	@Test
	public void sendOMLToDpcr() throws HL7Exception, IOException {
		instanceUtilMock();
		messageHandlerService.sendOMLToDpcr(getQueryResponseMessage());
	}

	QueryResponseMessage getQueryResponseMessage() {
		QueryResponseMessage queryResponseMessage = new QueryResponseMessage();
		Assay assay = new Assay();
		QueryResponseSample queryResponseSample = new QueryResponseSample();
		List<QueryResponseSample> queryResponseSampleList = new ArrayList<>();
		List<Assay> assayList = new ArrayList<>();
		assay.setKit("kit");
		assay.setMasterMix("masterMix");
		assay.setName("name");
		assay.setVersion("version");
		assayList.add(assay);
		queryResponseSample.setAccessioningId("accessioningId");
		queryResponseSample.setAssay(assayList);
		queryResponseSample.setContainerId("containerId");
		queryResponseSample.setPlateIntegator("fadsf");
		queryResponseSample.setPosition("fad");
		queryResponseSample.setStatus("sfda");
		queryResponseSampleList.add(queryResponseSample);
		queryResponseMessage.setSamples(queryResponseSampleList);
		queryResponseMessage.setMessageControlId("fgafadf");
		queryResponseMessage.setMessageType("fadfa");
		queryResponseMessage.setRunId("fadsfad");

		return queryResponseMessage;

	}

	String loginUrl = "http://localhost";
	String loginEntity = "http://localhost";
	@Mock
	Invocation.Builder loginBuilder;
	@Mock
	Invocation.Builder serviceBuilder;
	@Mock
	Response response;

	@Test
	public void sendQueryToIMMTest() throws JsonProcessingException, UnsupportedEncodingException {
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(loginBuilder);
		Entity<String> entityString = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
		Mockito.when(loginBuilder.post(entityString, String.class)).thenReturn("token");
		String url = "http://localhost:9090/imm/json/rest/api/v1/messages?source=device&devicetype="
				+ "dPCR Analyzer&messagetype=QBP&deviceid=DPCR-001";
		Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(serviceBuilder);
		Mockito.when(serviceBuilder.header("Cookie", "brownstoneauthcookie= token")).thenReturn(serviceBuilder);
		Entity<?> entity = Entity.entity(getQueryMessage(), MediaType.APPLICATION_JSON);
		Mockito.when(serviceBuilder.post(entity)).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(200);
		messageHandlerService.sendQueryToIMM(getQueryMessage());
	}

	@Test
	public void sendESUToIMMTest() throws JsonProcessingException, UnsupportedEncodingException {
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(loginBuilder);
		Entity<String> entityString = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
		Mockito.when(loginBuilder.post(entityString, String.class)).thenReturn("token");
		String url = "http://localhost:9090/imm/json/rest/api/v1/messages?source=device&devicetype="
				+ "dPCR Analyzer&messagetype=ESU&deviceid=DPCR-001";
		Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(serviceBuilder);
		Mockito.when(serviceBuilder.header("Cookie", "brownstoneauthcookie= token")).thenReturn(serviceBuilder);
		Entity<?> entity = Entity.entity(getQueryMessage(), MediaType.APPLICATION_JSON);
		Mockito.when(serviceBuilder.post(entity)).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(200);
		messageHandlerService.sendESUToIMM(getESUMessage());

	}

	@Test
	public void sendOMLACKToIMMTest() throws JsonProcessingException, UnsupportedEncodingException {
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(loginBuilder);
		Entity<String> entityString = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
		Mockito.when(loginBuilder.post(entityString, String.class)).thenReturn("token");
		String url = "http://localhost:9090/imm/json/rest/api/v1/messages?source=device&devicetype="
				+ "dPCR Analyzer&messagetype=ACK&deviceid=DPCR-001";
		Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(serviceBuilder);
		Mockito.when(serviceBuilder.header("Cookie", "brownstoneauthcookie= token")).thenReturn(serviceBuilder);
		Entity<?> entity = Entity.entity(getQueryMessage(), MediaType.APPLICATION_JSON);
		Mockito.when(serviceBuilder.post(entity)).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(200);
		messageHandlerService.sendOMLACKToIMM(getAcknowledgementMessage());

	}

	@Test
	public void sendORUToIMMTest() throws JsonProcessingException, UnsupportedEncodingException {
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(loginBuilder);
		Entity<String> entityString = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
		Mockito.when(loginBuilder.post(entityString, String.class)).thenReturn("token");
		String url = "http://localhost:9090/imm/json/rest/api/v1/messages?source=device&devicetype="
				+ "dPCR Analyzer&messagetype=ORU&deviceid=DPCR-001";
		Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(serviceBuilder);
		Mockito.when(serviceBuilder.header("Cookie", "brownstoneauthcookie= token")).thenReturn(serviceBuilder);
		Entity<?> entity = Entity.entity(getQueryMessage(), MediaType.APPLICATION_JSON);
		Mockito.when(serviceBuilder.post(entity)).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(200);
		messageHandlerService.sendORUToIMM(getORUMessage());

	}
	
	
	@Test
	public void sendNotificationTest() throws HMTPException, UnsupportedEncodingException {
		//messageHandlerService.setLoginUrl("http://localhost");
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.when( RestClientUtil.getBuilder("http://localhost", null)).thenReturn(loginBuilder);
		Mockito.when(loginBuilder.post(Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED), String.class)).thenReturn("token");
		String admHostUrl="http://localhost";
		
		MessageDto messagedto = new MessageDto();
		messagedto.setMessageGroup("error");
		List<String> errormessages = new ArrayList<>();
		errormessages.add("message");
		messagedto.setContents(errormessages);
		messagedto.setLocale("en_US");
		
		Mockito.when(RestClient.post(admHostUrl + "/json/rest/api/v1/notification", messagedto, "token", null)).thenReturn(response);
		PowerMockito.mockStatic(TokenUtils.class);
		Mockito.when(TokenUtils.getToken(true)).thenReturn("token");
		messageHandlerService.sendNotification("message", "error");
	}
	
	@Test
	public void postRequestTest() {
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(loginBuilder);
		Entity<String> entityString  = Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED);
		Mockito.when(loginBuilder.post(entityString, String.class)).thenReturn("token");
		Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(serviceBuilder);
		Mockito.when(serviceBuilder.header("Cookie", "brownstoneauthcookie= token")).thenReturn(serviceBuilder);
		Mockito.when(serviceBuilder.post(Entity.entity(getORUMessage(), MediaType.APPLICATION_JSON))).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(200);
		messageHandlerService.postRequest("http://localhost", Entity.entity(getORUMessage(), MediaType.APPLICATION_JSON));
	}
	
	
	@Test
	public void createAuditBeanTest()
	{
		PowerMockito.mockStatic(ConfigurationParser.class);
		PowerMockito.mockStatic(TokenUtils.class);
		Mockito.when(ConfigurationParser.getString("pas.logged_module_name")).thenReturn("1234");
		Mockito.when(ConfigurationParser.getString("pas.domainName")).thenReturn("hcl.com");
		Mockito.when(ConfigurationParser.getString("pas.username")).thenReturn("10");
		Mockito.when(TokenUtils.getToken(true)).thenReturn("token");
		Mockito.when(ConfigurationParser.getString("pas.audit_create_url")).thenReturn("http://localhost");
		//Mockito.doNothing().when(arg0)
		//Mockito.doNothing().when(PostAuditTrailEntry.postDataToAuditTrail(Entity.entity(getAuditTrailEntity(),MediaType.APPLICATION_JSON), "token", "http://localhost"));
		messageHandlerService.createAuditBean("messagecode", "message");
	}

	
	public AuditTrailEntity getAuditTrailEntity(){
		AuditTrailEntity auditData=new AuditTrailEntity();
		auditData.setCompanydomainname("hcl.com");
		auditData.setMessagecode("messagecode");
		auditData.setObjectnewvalue("");
		auditData.setObjectoldvalue(null);
		auditData.setParams(null);
		auditData.setSystemid("1234");
		auditData.setSystemmodulename("1234");
		auditData.setTimestamp("");
		auditData.setUserid(ConfigurationParser.getString("pas.username"));
		auditData.setIpaddress("");
		return auditData;
	}
	public QueryMessage getQueryMessage() {
		QueryMessage queryMessage = new QueryMessage();
		queryMessage.setDeviceId("DPCR-001");
		queryMessage.setDateTimeMessageGenerated("20190302121212");
		queryMessage.setMessageControlId("457654321");
		queryMessage.setMessageType("QBP");
		queryMessage.setRecevingApplicationName("Connect");
		queryMessage.setRecevingFacility("Connect");
		queryMessage.setSendingFacility("DPCR");
		queryMessage.setSendingApplicationName("DPCR");
		return queryMessage;

	}

	AcknowledgementMessage getQueryAckMessage() {
		AcknowledgementMessage queryAckMessage = new AcknowledgementMessage();
		queryAckMessage.setDateTimeMessageGenerated("20190220");
		queryAckMessage.setDeviceSerialNo("DPCR0012");
		queryAckMessage.setErrorCode("error code");
		queryAckMessage.setErrorDescription("");
		queryAckMessage.setMessageControlId("8001");
		queryAckMessage.setRunId("20");
		queryAckMessage.setStatus("Query");
		return queryAckMessage;

	}
	
	private AcknowledgementMessage getQueryAckMessageAppReject() {
		
		AcknowledgementMessage queryAckMessage = new AcknowledgementMessage();
		queryAckMessage.setDateTimeMessageGenerated("20190220");
		queryAckMessage.setDeviceSerialNo("DPCR0012");
		queryAckMessage.setErrorCode("error code");
		queryAckMessage.setErrorDescription("");
		queryAckMessage.setMessageControlId("8001");
		queryAckMessage.setRunId("20");
		queryAckMessage.setStatus("AR");
		return queryAckMessage;
	}

	public ORUMessage getORUMessage() {
		ORUMessage oRUMessage = new ORUMessage();
		ORUSampleDetails oRUSampleDetails = new ORUSampleDetails();
		ORUAssay oruAssay = new ORUAssay();
		ORUPartitionEngine oRUPartitionEngine = new ORUPartitionEngine();

		List<ORUSampleDetails> oRUSampleDetailsList = new ArrayList<>();
		List<ORUAssay> oRUAssayList = new ArrayList<>();
		List<ORUPartitionEngine> oRUPartitionEngineList = new ArrayList<>();
		oRUMessage.setDateTimeMessageGenerated("20190220");
		oRUMessage.setDeviceId("retqewaee");
		oRUMessage.setMessageType("raqg");
		oRUMessage.setMessageControlId("qre");
		oRUMessage.setReleasedBy("reqg");
		oRUMessage.setOperatorName("rgea");
		oRUMessage.setSentBy("rqegr");
		oRUMessage.setRunComments("regvaf");
		oRUMessage.setRunId("regfa");
		oRUSampleDetails.setAccessioningId("rgae");
		oRUSampleDetails.setFlag("rg");
		oRUSampleDetailsList.add(oRUSampleDetails);

		oruAssay.setKit("raf");
		oruAssay.setName("erqar");
		oruAssay.setPackageName("redf");
		oruAssay.setQualitativeResult("rgar");
		oruAssay.setQualitativeValue("arr");
		oruAssay.setQuantitativeResult("ra");
		oruAssay.setQuantitativeValue("rGGT");
		oruAssay.setVersion("REA");
		oRUAssayList.add(oruAssay);

		oRUPartitionEngine.setDateandTime("20190220");
		oRUPartitionEngine.setFluidId("dfga");
		oRUPartitionEngine.setPlateId("df");
		oRUPartitionEngine.setSerialNumber("rea");
		oRUPartitionEngineList.add(oRUPartitionEngine);

		oRUMessage.setOruSampleDetails(oRUSampleDetailsList);

		return oRUMessage;

	}

	public AcknowledgementMessage getAcknowledgementMessage() {
		AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
		acknowledgementMessage.setDateTimeMessageGenerated("20190220");
		acknowledgementMessage.setDeviceSerialNo("dagfrae");
		acknowledgementMessage.setErrorCode("erwqwe");
		acknowledgementMessage.setErrorDescription("adfa");
		acknowledgementMessage.setMessageControlId("afdfa");
		acknowledgementMessage.setRunId("fadsf");
		acknowledgementMessage.setStatus("fadsf");
		return acknowledgementMessage;

	}

	public ESUMessage getESUMessage() {
		ESUMessage esuMessage = new ESUMessage();
		esuMessage.setDateTimeMessageGenerated("20190220");
		esuMessage.setDeviceId("aafgad");
		esuMessage.setEstimatedTimeRemaining("20190220");
		esuMessage.setFilePath("agd");
		esuMessage.setMessageControlId("adfa");
		esuMessage.setMessageType("afda");
		esuMessage.setRunId("fasd");
		esuMessage.setStatus("dfaf");
		return esuMessage;

	}

}
