package com.roche.connect.wfm.test.testng;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.dpcr_analyzer.WFMAcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMORUMessage;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.connect.common.mp96.WFMACKMessage;
import com.roche.connect.common.mp96.WFMQueryMessage;
import com.roche.connect.common.mp96.WFMQueryResponseMessage;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.AssayTypeDTO;
import com.roche.connect.wfm.model.ProcessStepActionDTO;
import com.roche.connect.wfm.model.SampleWFMStates;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.repository.WfmReadRepository;
import com.roche.connect.wfm.rest.WfmdPCRRestApiImpl;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;
import com.roche.connect.wfm.service.WFMDPCRService;
import com.roche.connect.wfm.test.util.JsonFileReaderAsString;
import com.roche.connect.wfm.writerepository.WfmWriteRepository;


@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class}) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" })
public class ExecutedPCRTest {
	@Mock
	AssayIntegrationService assayIntegrationService = org.mockito.Mockito.mock(AssayIntegrationService.class);

	@InjectMocks
	AssayIntegrationService assayIntegrationServices = org.mockito.Mockito.mock(AssayIntegrationService.class);
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	RuntimeService runtimeService = org.mockito.Mockito.mock(RuntimeService.class);

	@Mock
	OrderIntegrationService orderIntegrationService = org.mockito.Mockito.mock(OrderIntegrationService.class);

	@Mock
	WfmWriteRepository wfmWriteRepository = org.mockito.Mockito.mock(WfmWriteRepository.class);

	@Mock
	WfmReadRepository wfmRepository = org.mockito.Mockito.mock(WfmReadRepository.class);

	@Mock
	ActivityProcessDataDTO activityProcessData = org.mockito.Mockito.mock(ActivityProcessDataDTO.class);

	@Mock
	ObjectMapper objectMappers = org.mockito.Mockito.mock(ObjectMapper.class);

	@Mock
	WFMDPCRService wfmServices = org.mockito.Mockito.mock(WFMDPCRService.class);

	
	@Mock
	WFMDPCRService wfmdPCRService;
	
	@Mock
	ResultIntegrationService resultIntegrationService = org.mockito.Mockito.mock(ResultIntegrationService.class);

	@Mock
	ResponseRenderingService responseRenderingService = org.mockito.Mockito.mock(ResponseRenderingService.class);

	@InjectMocks
	WfmdPCRRestApiImpl wfmRestApiImpl = new WfmdPCRRestApiImpl();

	@Mock
	AssayIntegrationService assayIntegrationServiceAck = org.mockito.Mockito.mock(AssayIntegrationService.class);

	@Mock
	RuntimeService runtimeServiceAck = org.mockito.Mockito.mock(RuntimeService.class);

	@Mock
	OrderIntegrationService orderIntegrationServiceAck = org.mockito.Mockito.mock(OrderIntegrationService.class);

	@Mock
	WfmWriteRepository wfmWriteRepositoryAck = org.mockito.Mockito.mock(WfmWriteRepository.class);

	@Mock
	WfmReadRepository wfmRepositoryAck = org.mockito.Mockito.mock(WfmReadRepository.class);
	@Mock
	ActivityProcessDataDTO activityProcessDataAck = org.mockito.Mockito.mock(ActivityProcessDataDTO.class);

	@InjectMocks
	@Spy
	WfmdPCRRestApiImpl wfmRestApiImplAck = new WfmdPCRRestApiImpl();

	SpecimenStatusUpdateMessage specimenStatusUpdateMessage = null;
	WFMACKMessage wfmACKMessageAck = null;
	int expectedCorrectStatusCodeAck = 200;
	int expectedInCorrectStatusCodeAck = 406;
	int expectedInCorrectStatusCodeSecond = 500;
	String jsonFileStringAckMP96 = null;
	String jsonFileStringLP24 = null;
	String jsonFileStringNegativeLP24 = null;
	String jsonFileStringLP24RunUpdate = null;
	String jsonFileStringLP24Status = null;
	String jsonFileStringNegativeLP24Status = null;
	String jsonFileStringdPCRAnalyzer = null;
	String jsonFileStringdPCRAnalyzerESU = null;
	String jsonFileStringdPCRAnalyzerACK = null;
	String jsonFileStringMP96 = null;
	WFMQueryMessage wfmQueryMessage = null;
	WFMoULMessage wfmoULMessage = null;
	QueryMessage queryMessage = null;
	List<WfmDTO> orderList = new ArrayList<WfmDTO>();

	List<AssayTypeDTO> assays = new ArrayList<AssayTypeDTO>();

	int expectedCorrectStatusCode = 200;
	String jsonFileString = null;
	String jsonFileStringmp96 = null;
	boolean expectedCorrectInput = true;
	List<ProcessStepActionDTO> list = new ArrayList<ProcessStepActionDTO>();
	///////

	@BeforeTest
	public void setUp() throws Exception {

		jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/MP96dPCRPositive.json");
		FileReader fr = new FileReader(new File("src/test/java/Resource/MP96dPCRPositive.json"));
		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> variables = new HashMap<>();
		wfmQueryMessage = objectMapper.readValue(fr, WFMQueryMessage.class);

		ProcessInstance pt = null;
		WFMQueryResponseMessage wq = new WFMQueryResponseMessage();
		wq.setStatus("SUCCESS");

		String assay = "NIPTDPCR";
		SampleWFMStates sampleWFM = new SampleWFMStates();
		sampleWFM.setAccessioningId(wfmQueryMessage.getAccessioningId());
		sampleWFM.setDeviceId(wfmQueryMessage.getDeviceSerialNumber());
		sampleWFM.setCurrentStatus("OPEN");
		sampleWFM.setProcessId("3001");
		sampleWFM.setMessageType(wfmQueryMessage.getMessageType());
		sampleWFM.setCreatedDatetime(new Date());

		WfmDTO wfmdto = new WfmDTO();
		wfmdto.setOrderId(Long.parseLong("10000001"));
		wfmdto.setAssayType("NIPTDPCR");
		orderList.add(wfmdto);

		ProcessStepActionDTO processStepAction = new ProcessStepActionDTO();
		processStepAction.setDeviceType("MP96");
		processStepAction.setProcessStepName("NA Extraction");
		list.add(processStepAction);

		SampleWFMStates samplewfmstate = new SampleWFMStates();
		Company company = new Company();
		company.setId(1L);
		samplewfmstate.setCompany(company);

		AssayTypeDTO assaydto = new AssayTypeDTO();
		assaydto.setWorkflowDefFile("NIPTDPCR_Wfm_v1.bpmn20.xml");
		assays.add(assaydto);

		///// ACKmp96

		jsonFileStringAckMP96 = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/Resource/MP96dPCRACKPositive.json");
		FileReader frack = new FileReader(new File("src/test/java/Resource/MP96dPCRACKPositive.json"));
		ObjectMapper objectMapperAck = new ObjectMapper();

		wfmACKMessageAck = objectMapperAck.readValue(frack, WFMACKMessage.class);
		WFMQueryResponseMessage wqAck = new WFMQueryResponseMessage();
		wqAck.setStatus("SUCCESS");
		expectedCorrectStatusCodeAck = 200;
		//////////

		// LP24
		jsonFileStringLP24 = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/Lp24LibPrepPositive.json");
		jsonFileStringLP24RunUpdate = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/Resource/Lp24UpdateRunResult.json");
		jsonFileStringNegativeLP24 = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/Resource/Lp24LibPrepNegative.json");
		FileReader frLP24 = new FileReader(new File("src/test/java/Resource/Lp24LibPrepPositive.json"));
		ObjectMapper objectMapperLP24 = new ObjectMapper();
		queryMessage = objectMapperLP24.readValue(frLP24, QueryMessage.class);

		/// LP24 STATUS
		jsonFileStringLP24Status = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/Resource/Lp24LibPrepStatusPositive.json");
		jsonFileStringNegativeLP24Status = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/Resource/Lp24LibPrepStatusNegative.json");
		FileReader frLP24Status = new FileReader(new File("src/test/java/Resource/Lp24LibPrepStatusPositive.json"));

		ObjectMapper objectMapperlp24status = new ObjectMapper();
		specimenStatusUpdateMessage = objectMapperlp24status.readValue(frLP24Status, SpecimenStatusUpdateMessage.class);

		MockitoAnnotations.initMocks(this);
		Mockito.when(assayIntegrationService.validateAssayProcessStep(wfmQueryMessage.getAccessioningId(), "MP96",
				"NA Extraction")).thenReturn(true);
		Mockito.when(orderIntegrationService.findOrder(wfmQueryMessage.getAccessioningId())).thenReturn(orderList);
		Mockito.when(assayIntegrationService.findAssayDetail(assay)).thenReturn(assays);
		Mockito.when(runtimeService.startProcessInstanceByKey("NIPTDPCR_Wfm_v1", wfmQueryMessage.getAccessioningId(),
				variables)).thenReturn(pt);
		Mockito.when(activityProcessData.getQbpResponseMsg()).thenReturn(wq);
		Mockito.when(assayIntegrationService.getProcessStepsByAccessioningID(wfmQueryMessage.getAccessioningId()))
				.thenReturn(list);
		Mockito.doNothing().when(wfmServices).startProcess(wfmQueryMessage.getAccessioningId(), activityProcessData);
		Mockito.when(objectMappers.writeValueAsString(wfmQueryMessage)).thenReturn(wfmQueryMessage.getAccessioningId());
		Mockito.when(objectMappers.writeValueAsString(wq)).thenReturn(wq.getStatus());
		// saving the data into db
		Mockito.when(wfmWriteRepository.save(sampleWFM)).thenReturn(sampleWFM);
		// Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmQueryMessage.getAccessioningId(),
		// wfmQueryMessage.getMessageType(),1)).thenReturn(samplewfmstate);
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
				Mockito.any(String.class), Mockito.any(Long.class))).thenReturn(samplewfmstate);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		///// ACKmp96

		Mockito.when(activityProcessDataAck.getQbpResponseMsg()).thenReturn(wqAck);
		Mockito.doNothing().when(wfmServices).updatemp96Ack(activityProcessDataAck,
				wfmACKMessageAck.getAccessioningId());
		Mockito.when(objectMappers.writeValueAsString(wfmACKMessageAck))
				.thenReturn(wfmACKMessageAck.getAccessioningId());
		Mockito.when(objectMappers.readValue(jsonFileStringAckMP96, WFMACKMessage.class)).thenReturn(wfmACKMessageAck);

		//// LP24
		Mockito.when(objectMappers.readValue(jsonFileStringLP24, QueryMessage.class)).thenReturn(queryMessage);
		Mockito.when(objectMappers.writeValueAsString(queryMessage)).thenReturn(queryMessage.getContainerId());
		Mockito.doNothing().when(wfmServices).startdPCRLp24Process(queryMessage.getContainerId(),
				queryMessage.getAccessioningId(), queryMessage.getDeviceSerialNumber(), activityProcessData);
		Mockito.doNothing().when(responseRenderingService).orderNotFoundQueryResponsefordPCRLP(
				queryMessage.getContainerId(), queryMessage.getAccessioningId(), queryMessage.getDeviceSerialNumber(),
				queryMessage.getSendingApplicationName());

		Mockito.when(objectMappers.readValue(jsonFileStringLP24Status, SpecimenStatusUpdateMessage.class))
				.thenReturn(specimenStatusUpdateMessage);
		Mockito.when(objectMappers.writeValueAsString(specimenStatusUpdateMessage))
				.thenReturn(specimenStatusUpdateMessage.getContainerId());
		Mockito.doNothing().when(wfmServices).updatedPCRLp24Process(
				specimenStatusUpdateMessage.getAccessioningId(), specimenStatusUpdateMessage.getDeviceSerialNumber(),
				"order status", activityProcessData);
		Mockito.doNothing().when(responseRenderingService).orderNotFoundQueryResponseforLP(
				specimenStatusUpdateMessage.getContainerId(), specimenStatusUpdateMessage.getDeviceSerialNumber(),
				specimenStatusUpdateMessage.getSendingApplicationName());

		// MP96 U03

		jsonFileStringmp96 = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/MP96dPCRU03Positive.json");
		FileReader fr1 = new FileReader(new File("src/test/java/Resource/MP96dPCRU03Positive.json"));
		ObjectMapper objectMapper1 = new ObjectMapper();
		wfmoULMessage = objectMapper1.readValue(fr1, WFMoULMessage.class);

		Mockito.doNothing().when(wfmServices).updateMp96Process(activityProcessData, wfmoULMessage.getAccessioningId(),
				"Passed", wfmoULMessage.getDeviceSerialNumber());

		// dPCR Analyzer
		jsonFileStringdPCRAnalyzer = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/Resource/DPCRAnalyzerQBPPositive.json");

		jsonFileStringdPCRAnalyzerESU = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/Resource/DPCRAnalyzerESUPositive.json");
		
		jsonFileStringdPCRAnalyzerACK = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/DPCRAnalyzerACKmsg.json");

	}

	@Test
	public void DPCRStartWFMTestPosTest1() throws HMTPException, OrderNotFoundException, IOException {

		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyLong())).thenReturn(null);
		Mockito.doNothing().when(wfmServices).startMp96Process(Mockito.anyString(),
				Mockito.any(ActivityProcessDataDTO.class));
		wfmRestApiImpl.getExecutedPCRStartWFM(wfmQueryMessage);

	}

	@Test(priority = 1)
	public void DPCRStartWFMTestPosTest() throws HMTPException {
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStartWFM(wfmQueryMessage);
		
	}

	@Test(priority = 2)
	public void DPCRStartWFMTestNegTest() throws HMTPException {
		wfmQueryMessage.setSendingApplicationName("MP96");
		SampleWFMStates samplewfmstate = new SampleWFMStates();
		samplewfmstate.setAccessioningId(wfmQueryMessage.getAccessioningId());
		// Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmQueryMessage.getAccessioningId(),
		// wfmQueryMessage.getMessageType())).thenReturn(samplewfmstate);
		Response response = wfmRestApiImpl.getExecutedPCRStartWFM(wfmQueryMessage);
		Assert.assertEquals(response.getStatus(), 406);
	}

	@Test(priority = 3)
	public void DPCRStartWFMTestNegativeTest() throws HMTPException {
		SampleWFMStates rocheWfm = new SampleWFMStates();
		wfmQueryMessage.setSendingApplicationName("Magna Pure 96");
		rocheWfm.setAccessioningId(wfmQueryMessage.getAccessioningId());
		wfmQueryMessage.setMessageType("Extraction");
		// Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmQueryMessage.getAccessioningId(),
		// wfmQueryMessage.getMessageType())).thenReturn(rocheWfm);
		Response response = wfmRestApiImpl.getExecutedPCRStartWFM(wfmQueryMessage);
		
	}

	@Test(priority = 4)
	public void startProcess() throws Exception {
		wfmServices.startProcess(wfmQueryMessage.getAccessioningId(), activityProcessData);
	}

	@Test(priority = 5)
	public void DPCRStartWFMTestNegTest1() throws HMTPException {
		SampleWFMStates samplewfmstate = new SampleWFMStates();
		samplewfmstate.setAccessioningId(wfmQueryMessage.getAccessioningId());
		samplewfmstate.setDeviceId(wfmQueryMessage.getDeviceSerialNumber());
		samplewfmstate.setId(1);
		samplewfmstate.setMessageType(wfmQueryMessage.getMessageType());
		samplewfmstate.setCreatedDate(new Date());
		// Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmQueryMessage.getAccessioningId(),
		// WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(),1)).thenReturn(samplewfmstate);
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
				Mockito.any(String.class), Mockito.any(Long.class))).thenReturn(samplewfmstate);

		Response response = wfmRestApiImpl.getExecutedPCRStartWFM(wfmQueryMessage);
		Assert.assertEquals(response.getStatus(), 200);
	}

	@Test(priority = 6)
	public void DPCRStartWFMTestNegTest2() throws HMTPException {
		wfmQueryMessage.setAccessioningId(null);
		wfmQueryMessage.setMessageType(null);
		wfmQueryMessage.setDeviceSerialNumber(null);
		wfmRestApiImpl.getExecutedPCRStartWFM(wfmQueryMessage);
	}

	@Test(priority = 7)
	public void startMp96Process() throws Exception {
		Mockito.when(assayIntegrationService.validateAssayProcessStep(wfmQueryMessage.getAccessioningId(), "MP96",
				"NA Extraction")).thenReturn(true);
		Mockito.doNothing().when(wfmServices).startProcess(wfmQueryMessage.getAccessioningId(), activityProcessData);
		wfmServices.startMp96Process(wfmQueryMessage.getAccessioningId(), activityProcessData);
	}

	@Test(priority = 8)
	public void startMp96Process1() throws Exception {
		Mockito.when(assayIntegrationService.validateAssayProcessStep(wfmQueryMessage.getAccessioningId(), "MP96",
				"NA Extraction")).thenReturn(false);
		wfmServices.startMp96Process(wfmQueryMessage.getAccessioningId(), activityProcessData);
	}

	@Test(expectedExceptions = { NullPointerException.class, HMTPException.class })
	public void DPCRStartWFMTestNegativeTests() throws NullPointerException, HMTPException {
		WFMQueryMessage wfmQueryMessage = null;
		Response response = wfmRestApiImpl.getExecutedPCRStartWFM(wfmQueryMessage);
		// assertEquals(response.getStatus(), expectedInCorrectStatusCodeAck);
	}

	@Test(priority = 9)
	public void ExecutedPCRStatusUpdateWFMPosTest() throws HMTPException {
		Response actualResponseCode = wfmRestApiImplAck.getExecutedPCRStatusUpdateWFM("ACKMP96", jsonFileStringAckMP96);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCodeAck);
	}

	@Test(priority = 10)
	public void ExecutedPCRStatusUpdateWFMNegativeTest() throws HMTPException {
		Response actualResponseCode = wfmRestApiImplAck.getExecutedPCRStatusUpdateWFM("ACK96", jsonFileStringAckMP96);
		assertEquals(actualResponseCode.getStatus(), expectedInCorrectStatusCodeAck);

	}

	@Test(priority = 11)
	public void ExecutedPCRLP24QueryNegativeTest()
			throws HMTPException, JsonParseException, JsonMappingException, IOException {
		QueryMessage queryMessage = null;
		FileReader fr = new FileReader(new File("src/test/java/Resource/Lp24LibPrepNegative.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		queryMessage = objectMapper.readValue(fr, QueryMessage.class);
		Mockito.when(objectMappers.readValue(jsonFileStringNegativeLP24, QueryMessage.class)).thenReturn(queryMessage);
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("LPQuery",
				jsonFileStringNegativeLP24);
		assertEquals(actualResponseCode.getStatus(), expectedInCorrectStatusCodeSecond);
	}

	@Test(priority = 12)
	public void ExecutedPCRLP24StatusPosTest() throws HMTPException {
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("LPStatus",
				jsonFileStringLP24Status);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}

	@Test(priority = 13)
	public void ExecutedPCRLP24StatusNegTest() throws HMTPException {
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("ACK96", jsonFileStringLP24Status);
		assertEquals(actualResponseCode.getStatus(), expectedInCorrectStatusCodeAck);
	}

	@Test(priority = 14)
	public void ExecutedPCRLP24StatusNegativeTest()
			throws HMTPException, JsonParseException, JsonMappingException, IOException {
		SpecimenStatusUpdateMessage specimenStatusUpdateMessages = null;
		FileReader fr = new FileReader(new File("src/test/java/Resource/Lp24LibPrepNegative.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		specimenStatusUpdateMessages = objectMapper.readValue(fr, SpecimenStatusUpdateMessage.class);
		Mockito.when(objectMappers.readValue(jsonFileStringNegativeLP24Status, SpecimenStatusUpdateMessage.class))
				.thenReturn(specimenStatusUpdateMessages);
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("LPStatus",
				jsonFileStringNegativeLP24Status);
		assertEquals(actualResponseCode.getStatus(), expectedInCorrectStatusCodeSecond);
	}

	@Test(priority = 15)
	public void getExecuteMP96StatusUpdateWFM() throws HMTPException, OrderNotFoundException {

		Response actualResponseCode = wfmRestApiImpl.getExecuteMP96StatusUpdateWFM(wfmoULMessage);
		Assert.assertEquals(actualResponseCode.getStatus(), 200);

	}

	@Test(priority = 16)
	public void ExecutedPCRAnalyzerQueryPosTest()
			throws HMTPException, JsonParseException, JsonMappingException, IOException {
		com.roche.connect.common.dpcr_analyzer.WFMQueryMessage wfmQueryMessage = null;
		FileReader fr = new FileReader(new File("src/test/java/Resource/DPCRAnalyzerQBPPositive.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		wfmQueryMessage = objectMapper.readValue(fr, com.roche.connect.common.dpcr_analyzer.WFMQueryMessage.class);
		Mockito.when(objectMappers.readValue(jsonFileStringdPCRAnalyzer,
				com.roche.connect.common.dpcr_analyzer.WFMQueryMessage.class)).thenReturn(wfmQueryMessage);
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("AnalyzerQuery",
				jsonFileStringdPCRAnalyzer);

	}

	@Test(priority = 17)
	public void ExecutedPCRAnalyzerStatusUpdateWFM()
			throws HMTPException, JsonParseException, JsonMappingException, IOException {
		com.roche.connect.common.dpcr_analyzer.WFMESUMessage wfmESUMessage = null;
		FileReader fr = new FileReader(new File("src/test/java/Resource/DPCRAnalyzerESUPositive.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		wfmESUMessage = objectMapper.readValue(fr, com.roche.connect.common.dpcr_analyzer.WFMESUMessage.class);
		Mockito.when(objectMappers.readValue(jsonFileStringdPCRAnalyzerESU,
				com.roche.connect.common.dpcr_analyzer.WFMESUMessage.class)).thenReturn(wfmESUMessage);
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("AnalyzerStatus",
				jsonFileStringdPCRAnalyzerESU);

	}

	@Test(priority = 18)
	public void ExecutedPCRLP24UpdateRunResult()
			throws HMTPException, JsonParseException, JsonMappingException, IOException {

		FileReader fr = new FileReader(new File("src/test/java/Resource/Lp24UpdateRunResult.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap lp24UpdateRun = objectMapper.readValue(fr, HashMap.class);
		Mockito.when(objectMappers.readValue(jsonFileStringLP24RunUpdate, HashMap.class)).thenReturn(lp24UpdateRun);
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("LP24UpdateRunResult",
				jsonFileStringLP24RunUpdate);
		Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
				Mockito.anyString());

	}
	
	
	@Test(priority = 19)
	public void ExecutedPCRLP24QueryPosTest() throws HMTPException {
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("LPQuery", jsonFileStringLP24);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}
	
	@Test(priority = 20)
	public void getExecuteAnalyzerACK() throws HMTPException {
		WFMAcknowledgementMessage wfmAckMsg = new WFMAcknowledgementMessage();
		wfmAckMsg.setAccessioningId("8001");
		wfmAckMsg.setDeviceId("DPCR");
		Mockito.doNothing().when(wfmdPCRService).updatedPCRAnalyzerAck(Mockito.any(ActivityProcessDataDTO.class), Mockito.anyString());
		wfmRestApiImpl.getExecuteAnalyzerACK(wfmAckMsg);
		
	}
	
	@Test (priority = 21)
	public void getExecuteLPQueryUpdateWFM() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
		QueryMessage quyMsg = new QueryMessage();
		quyMsg.setMessageType(EnumMessageType.QueryMessage);
		quyMsg.setDeviceSerialNumber("LP24-1221");
		quyMsg.setContainerId("LP24_A1");
		quyMsg.setAccessioningId("15551");
		quyMsg.setSendingApplicationName("LP24");
		Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(wfmdPCRService).startdPCRLp24Process(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(ActivityProcessDataDTO.class));
		wfmRestApiImpl.getExecuteLPQueryUpdateWFM(quyMsg);
	}
	
	@Test (priority = 22)
	public void getExecuteLPQueryUpdateWFMNeg() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
		QueryMessage quyMsg = new QueryMessage();
		quyMsg.setMessageType(EnumMessageType.AcknowledgementMessage);
		quyMsg.setDeviceSerialNumber("LP24-1221");
		quyMsg.setContainerId("LP24_A1");
		quyMsg.setAccessioningId("15551");
		quyMsg.setSendingApplicationName("LP24");
		wfmRestApiImpl.getExecuteLPQueryUpdateWFM(quyMsg);
	}
	
	
	
	@Test (priority = 23)
	public void getExecutedPCRORUUpdateWFM() throws UnsupportedEncodingException, HMTPException, OrderNotFoundException {
		WFMORUMessage wfmORUMessage = new WFMORUMessage();
		wfmORUMessage.setDeviceId("RX-6555");
		wfmORUMessage.setMessageType("ORU");
		wfmORUMessage.setAccessioningId("14441");
		Mockito.doNothing().when(wfmdPCRService).updatedPCRORUProcess(Mockito.anyString(), Mockito.anyString(), Mockito.any(ActivityProcessDataDTO.class));
		wfmRestApiImpl.getExecutedPCRORUUpdateWFM(wfmORUMessage);
	}

	@Test(priority = 24)
	public void ExecutedPCRACKAnalyzerTest() throws HMTPException, JsonParseException, JsonMappingException, IOException {
		MockitoAnnotations.initMocks(this);
		jsonFileStringdPCRAnalyzerACK = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/DPCRAnalyzerACKmsg.json");
		FileReader fr = new FileReader(new File("src/test/java/Resource/DPCRAnalyzerACKmsg.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		WFMAcknowledgementMessage dPCRACK = objectMapper.readValue(fr, WFMAcknowledgementMessage.class);
		Mockito.when(objectMappers.readValue(jsonFileStringdPCRAnalyzerACK, WFMAcknowledgementMessage.class)).thenReturn(dPCRACK);
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("ACKAnalyzer",jsonFileStringdPCRAnalyzerACK);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);		

	}
	
	@Test(priority = 25)
	public void ExecutedPCRAnalyzerORUTest() throws HMTPException, JsonParseException, JsonMappingException, IOException {
		MockitoAnnotations.initMocks(this);
		jsonFileStringdPCRAnalyzerACK = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/DPCRAnalyzerACKmsg.json");
		FileReader fr = new FileReader(new File("src/test/java/Resource/DPCRAnalyzerACKmsg.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		WFMORUMessage dPCRACK = objectMapper.readValue(fr, WFMORUMessage.class);
		Mockito.when(objectMappers.readValue(jsonFileStringdPCRAnalyzerACK, WFMORUMessage.class)).thenReturn(dPCRACK);
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("AnalyzerORU",jsonFileStringdPCRAnalyzerACK);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);		

	}
	
	@Test(priority = 26)
	public void ExecuteMP96SampTest() throws HMTPException, JsonParseException, JsonMappingException, IOException {
		MockitoAnnotations.initMocks(this);
		jsonFileStringMP96 = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/MP96dPCRStatusPositive.json");
		FileReader fr = new FileReader(new File("src/test/java/Resource/MP96dPCRStatusPositive.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		WFMoULMessage dPCRACK = objectMapper.readValue(fr, WFMoULMessage.class);
		Mockito.when(objectMappers.readValue(jsonFileStringMP96, WFMoULMessage.class)).thenReturn(dPCRACK);
		Response actualResponseCode = wfmRestApiImpl.getExecutedPCRStatusUpdateWFM("MP96Status",jsonFileStringMP96);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);		

	}
	
	@Test (priority = 27)
	public void getExecuteLPQueryUpdateNeg() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		QueryMessage quyMsg = new QueryMessage();
		quyMsg.setMessageType(EnumMessageType.QueryMessage);
		quyMsg.setDeviceSerialNumber("LP24-1221");
		quyMsg.setContainerId("LP24_A1");
		quyMsg.setAccessioningId("15551");
		quyMsg.setSendingApplicationName("LP24");
		
		Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doThrow(Exception.class).when(wfmdPCRService).startdPCRLp24Process(Mockito.anyString(),Mockito.anyString(), Mockito.anyString(), Mockito.any());
		try {
		wfmRestApiImpl.getExecuteLPQueryUpdateWFM(quyMsg);
		}catch(Exception e) {
			
		}
	}
	
	@Test (priority = 28)
	public void getExecuteLPQueryUpdateNegSeco() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		QueryMessage quyMsg = new QueryMessage();
		quyMsg.setMessageType(EnumMessageType.QueryMessage);
		quyMsg.setDeviceSerialNumber("LP24-1221");
		quyMsg.setContainerId("LP24_A1");
		quyMsg.setAccessioningId("15551");
		quyMsg.setSendingApplicationName("LP24");
		
		Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doThrow(OrderNotFoundException.class).when(wfmdPCRService).startdPCRLp24Process(Mockito.anyString(),Mockito.anyString(), Mockito.anyString(), Mockito.any());
		try {
		wfmRestApiImpl.getExecuteLPQueryUpdateWFM(quyMsg);
		}catch(Exception e) {
			
		}
	}
	
	@Test (priority = 29)
	public void getExecutedPCRAnalyzerQueryUpdateWFM() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		com.roche.connect.common.dpcr_analyzer.WFMQueryMessage quyMsg = new com.roche.connect.common.dpcr_analyzer.WFMQueryMessage();
		quyMsg.setMessageType("QBP");
		quyMsg.setAccessioningId("155551");
		try {
			wfmRestApiImpl.getExecutedPCRAnalyzerQueryUpdateWFM(quyMsg);
		} catch(Exception e) {
			
		}
		
	}
	
	@Test (priority = 30)
	public void getExecutedPCRAnalyzerQueryUpdateWFMNeg() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		com.roche.connect.common.dpcr_analyzer.WFMQueryMessage quyMsg = new com.roche.connect.common.dpcr_analyzer.WFMQueryMessage();
		quyMsg.setMessageType("QBP");
		quyMsg.setAccessioningId("155551");
		quyMsg.setContainerId("123333");
		quyMsg.setContainerPosition("A1");
		quyMsg.setDeviceId("4555");
		quyMsg.setProcessStepName("PostPCR");
		quyMsg.setRunResultsId(10001L);
		Mockito.doThrow(Exception.class).when(wfmdPCRService).startdPCRAnalyzerProcess(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any());
		wfmRestApiImpl.getExecutedPCRAnalyzerQueryUpdateWFM(quyMsg);
		
	}
	
	@Test (priority = 31)
	public void getExecutedPCRAnalyzerQueryUpdateWFMNegSec() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		com.roche.connect.common.dpcr_analyzer.WFMQueryMessage quyMsg = new com.roche.connect.common.dpcr_analyzer.WFMQueryMessage();
		quyMsg.setMessageType("ACK");
		quyMsg.setAccessioningId("155551");
		quyMsg.setContainerId("123333");
		quyMsg.setContainerPosition("A1");
		quyMsg.setDeviceId("4555");
		quyMsg.setProcessStepName("PostPCR");
		quyMsg.setRunResultsId(10001L);
		wfmRestApiImpl.getExecutedPCRAnalyzerQueryUpdateWFM(quyMsg);
		
	}
	
	@Test (priority = 32)
	public void getExecutedPCRAnalyzerStatusUpdateWFM() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		WFMESUMessage esuMsg = new WFMESUMessage();
		wfmRestApiImpl.getExecutedPCRAnalyzerStatusUpdateWFM(esuMsg);
		
	}
	
	@Test (priority = 33)
	public void getExecutedPCRAnalyzerStatusUpdateWFMNeg() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		WFMESUMessage esuMsg = new WFMESUMessage();
		esuMsg.setAccessioningId("11112323");
		esuMsg.setMessageType("QBP");
		esuMsg.setDeviceId("222333");
		esuMsg.setStatus("InProcess");
		wfmRestApiImpl.getExecutedPCRAnalyzerStatusUpdateWFM(esuMsg);
		
	}
	
	@Test (priority = 34)
	public void getExecutedPCRAnalyzerStatusUpdateWFMNegSec() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		WFMESUMessage esuMsg = new WFMESUMessage();
		esuMsg.setAccessioningId("11112323");
		esuMsg.setMessageType("ESU");
		esuMsg.setDeviceId("222333");
		esuMsg.setStatus("InProcess");
		Mockito.doThrow(Exception.class).when(wfmdPCRService).updatedPCRAnalyzerProcess(Mockito.anyString(),Mockito.anyString(),Mockito.any());
		wfmRestApiImpl.getExecutedPCRAnalyzerStatusUpdateWFM(esuMsg);
		
	}
	
	@Test (priority = 35)
	public void getExecutedPCRAnalyzerStatusUpdateWFMNegThird() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		WFMESUMessage esuMsg = new WFMESUMessage();
		esuMsg.setAccessioningId("11112323");
		esuMsg.setMessageType("ESU");
		esuMsg.setDeviceId("222333");
		esuMsg.setStatus("InProcess");
		Mockito.doThrow(UnsupportedEncodingException.class).when(wfmdPCRService).updatedPCRAnalyzerProcess(Mockito.anyString(),Mockito.anyString(),Mockito.any());
		wfmRestApiImpl.getExecutedPCRAnalyzerStatusUpdateWFM(esuMsg);
		
	}
	
	@Test (priority = 36)
	public void getExecutedPCRAnalyzerStatusUpdateWFMNegfour() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		WFMESUMessage esuMsg = new WFMESUMessage();
		esuMsg.setAccessioningId("11112323");
		esuMsg.setMessageType("ESU");
		esuMsg.setDeviceId("222333");
		esuMsg.setStatus("InProcess");
		Mockito.doThrow(OrderNotFoundException.class).when(wfmdPCRService).updatedPCRAnalyzerProcess(Mockito.anyString(),Mockito.anyString(),Mockito.any());
		wfmRestApiImpl.getExecutedPCRAnalyzerStatusUpdateWFM(esuMsg);
		
	}
	
	@Test (priority = 37)
	public void getExecutedPCRLP24StatusUpdateWFM() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		StatusUpdate stateUpdate = new StatusUpdate();
		stateUpdate.setComment("check status");
		stateUpdate.setEquipmentState("ON");
		SpecimenStatusUpdateMessage esuMsg = new SpecimenStatusUpdateMessage();
		esuMsg.setAccessioningId("11112323");
		esuMsg.setMessageType(EnumMessageType.AcknowledgementMessage);
		esuMsg.setDeviceSerialNumber("123311");
		esuMsg.setSendingApplicationName("LP24");
		esuMsg.setStatusUpdate(stateUpdate);
		wfmRestApiImpl.getExecutedPCRLP24StatusUpdateWFM(esuMsg);
		
	}
	
	@Test (priority = 38)
	public void getExecutedPCRLP24StatusUpdateWFMNeg() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		StatusUpdate stateUpdate = new StatusUpdate();
		stateUpdate.setOrderResult("samplestatus");
		SpecimenStatusUpdateMessage esuMsg = new SpecimenStatusUpdateMessage();
		esuMsg.setAccessioningId("11112323");
		esuMsg.setMessageType(EnumMessageType.AcknowledgementMessage);
		esuMsg.setDeviceSerialNumber("123311");
		esuMsg.setSendingApplicationName("LP24");
		esuMsg.setStatusUpdate(stateUpdate);
		Mockito.doThrow(Exception.class).when(wfmdPCRService).updatedPCRLp24Process(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any());
		wfmRestApiImpl.getExecutedPCRLP24StatusUpdateWFM(esuMsg);
		
	}
	
	@Test (priority = 39)
	public void getExecutedPCRLP24StatusUpdateWFMNegSec() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		StatusUpdate stateUpdate = new StatusUpdate();
		stateUpdate.setOrderResult("samplestatus");
		SpecimenStatusUpdateMessage esuMsg = new SpecimenStatusUpdateMessage();
		esuMsg.setAccessioningId("11112323");
		esuMsg.setMessageType(EnumMessageType.AcknowledgementMessage);
		esuMsg.setDeviceSerialNumber("123311");
		esuMsg.setSendingApplicationName("LP24");
		esuMsg.setStatusUpdate(stateUpdate);
		Mockito.doThrow(OrderNotFoundException.class).when(wfmdPCRService).updatedPCRLp24Process(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any());
		wfmRestApiImpl.getExecutedPCRLP24StatusUpdateWFM(esuMsg);
		
	}
	
	@Test (priority = 40)
	public void getExecutedPCRLP24StatusUpdateWFMNegThird() throws HMTPException, OrderNotFoundException,Exception {
		MockitoAnnotations.initMocks(this);
		StatusUpdate stateUpdate = new StatusUpdate();
		stateUpdate.setOrderResult("samplestatus");
		SpecimenStatusUpdateMessage esuMsg = new SpecimenStatusUpdateMessage();
		esuMsg.setAccessioningId("11112323");
		esuMsg.setMessageType(EnumMessageType.AcknowledgementMessage);
		esuMsg.setDeviceSerialNumber("123311");
		esuMsg.setSendingApplicationName("LP24");
		esuMsg.setStatusUpdate(stateUpdate);
		Mockito.doThrow(UnsupportedEncodingException.class).when(wfmdPCRService).updatedPCRLp24Process(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any());
		wfmRestApiImpl.getExecutedPCRLP24StatusUpdateWFM(esuMsg);
		
	}
	
	@AfterTest
	public void afterTest() {
		expectedCorrectStatusCodeAck = 0;
		expectedInCorrectStatusCodeAck = 0;
		expectedInCorrectStatusCodeSecond = 0;
		jsonFileStringAckMP96 = null;
		jsonFileStringLP24 = null;
		jsonFileStringdPCRAnalyzer = null;
		jsonFileStringdPCRAnalyzerESU = null;
		jsonFileStringNegativeLP24 = null;
		jsonFileStringLP24Status = null;
		jsonFileStringNegativeLP24Status = null;
		jsonFileStringLP24RunUpdate = null;

	}

}
