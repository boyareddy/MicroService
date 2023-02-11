package com.roche.connect.imm.testng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.model.MessageStore;
import com.roche.connect.imm.rest.MessageRestAPIImpl;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.DPCRAnalyzerAsyncMessageService;
import com.roche.connect.imm.service.LP24AsyncMessageService;
import com.roche.connect.imm.service.LP24MessageService;
import com.roche.connect.imm.service.MP96AsyncMessageService;
import com.roche.connect.imm.service.MP96MessageService;
import com.roche.connect.imm.service.MessageProcessorService;
import com.roche.connect.imm.service.MessageService;
import com.roche.connect.imm.service.OrderIntegrationService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.service.WFMIntegrationService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;
import com.roche.connect.imm.utils.ObjectMapperFactory;
import com.roche.connect.imm.utils.RestClient;

@PrepareForTest({ RestClientUtil.class, RestClient.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class MessageRestAPIImplTest {
	public static String mp96QueryMessage = "src/test/java/resource/MP96dPCRQBPPositive.json";
	public static String mp96ACKMessage = "src/test/java/resource/MP96dPCRACKPositive.json";
	public static String mp96OULMessage = "src/test/java/resource/MP96dPCROULPositive.json";

	public static String lp24QueryMessage = "src/test/java/resource/lp24QueryMessage.json";
	public static String lp24RSPMessage = "src/test/java/resource/lp24RSPMessage.json";
	public static String lp24SSUMessage = "src/test/java/resource/lp24SSUMessage.json";
	public static String lp24ACKMessage = "src/test/java/resource/lp24ACKMessage.json";

	public static String mp24ACKMessage = "src/test/java/resource/MP24ACKMessage.json";
	public static String mp24QueryMessage = "src/test/java/resource/mp24QueryMessage.json";

	public static String containerSamplesJson = "src/test/java/resource/ContainerSampleCSVUpdate.json";
	public static String sampleResultList = "src/test/java/resource/SampleResultList.json";

	public static String dpcrQBPMessage = "src/test/java/resource/dpcrQBPMessage.json";
	public static String dpcrACKMessage = "src/test/java/resource/dpcrACKMessage.json";
	public static String dpcrESUMessage = "src/test/java/resource/dpcrESUMessage.json";
	public static String dpcrORUMessage = "src/test/java/resource/dpcrORUMessage.json";

	private String deviceId = "D12345";
	String mp96QueryMessageStr;
	String mp96ACKMessageStr;
	String mp96OULMessageStr;

	String lp24QueryMessageStr;
	String lp24RSPMessageStr;
	String lp24SSUMessageStr;
	String lp24ACKMessageStr;

	String mp24QueryMessageStr;
	String mp24ACKMessageStr;

	@InjectMocks
	private MessageRestAPIImpl messageRestAPIImpl = new MessageRestAPIImpl();

	@Spy
	@InjectMocks
	private MP96MessageService mp96MessageService;

	@Spy
	@InjectMocks
	private LP24MessageService lp24MessageService;

	@Spy
	@InjectMocks
	private OrderIntegrationService orderIntegrationService;

	@Mock
	private MessageService messageService;

	@Spy
	@InjectMocks
	private RmmIntegrationService rmmIntegrationService;

	@Spy
	@InjectMocks
	private AssayIntegrationService assayIntegrationService;

	@Mock
	private WFMIntegrationService wfmIntegrationService;

	@Spy
	@InjectMocks
	private MessageProcessorService messageProcessorService;

	@Spy
	@InjectMocks
	private MP96AsyncMessageService mp96AsyncMessageService;

	@Spy
	@InjectMocks
	private LP24AsyncMessageService lp24AsyncMessageService;
	
	@Mock
	private DPCRAnalyzerAsyncMessageService dpcrAnalyzerAsyncMessageService;
	
	@Mock
	private Invocation.Builder builder;

	@Mock
	private HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	private Response response;

	private ObjectMapper objectMapper = new ObjectMapper();

	private List<ProcessStepActionDTO> processStepList = new ArrayList<>();

	private List<ContainerSamplesDTO> containerSamples;
	
	private List<SampleResultsDTO> listOfSamples;

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@BeforeMethod
	public void beforeMethod() throws IOException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(RestClient.class);

		Mockito.when(messageService.saveMessage(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(new MessageStore());
		setupJSON();
		mockProcessStepList();

		ReflectionTestUtils.setField(orderIntegrationService, "ommHostUrl", "http://localhost:96/omm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(messageProcessorService, "mp24AdaptorHostUrl", "http://localhost:85/roche_camel_adapter");
	}

	private void setupJSON() throws IOException {

		mp96QueryMessageStr = JsonFileReaderAsString.getJsonfromFile(mp96QueryMessage);
		mp96ACKMessageStr = JsonFileReaderAsString.getJsonfromFile(mp96ACKMessage);
		mp96OULMessageStr = JsonFileReaderAsString.getJsonfromFile(mp96OULMessage);

		lp24QueryMessageStr = JsonFileReaderAsString.getJsonfromFile(lp24QueryMessage);
		lp24RSPMessageStr = JsonFileReaderAsString.getJsonfromFile(lp24RSPMessage);
		lp24SSUMessageStr = JsonFileReaderAsString.getJsonfromFile(lp24SSUMessage);
		lp24ACKMessageStr = JsonFileReaderAsString.getJsonfromFile(lp24ACKMessage);

		mp24QueryMessageStr = JsonFileReaderAsString.getJsonfromFile(mp24QueryMessage);
		mp24ACKMessageStr = JsonFileReaderAsString.getJsonfromFile(mp24ACKMessage);

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(containerSamplesJson);
		TypeReference<List<ContainerSamplesDTO>> class1 = new TypeReference<List<ContainerSamplesDTO>>() {
		};
		containerSamples = objectMapper.readValue(jsonContent, class1);
		
		String jsonContent2 = JsonFileReaderAsString.getJsonfromFile(sampleResultList);
		TypeReference<List<SampleResultsDTO>> class2 = new TypeReference<List<SampleResultsDTO>>() {
		};
		listOfSamples = objectMapper.readValue(jsonContent2, class2);

	}

	private void mockProcessStepList() {
		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();

		processStepActionDTO.setProcessStepName("NA Extraction");
		processStepActionDTO.setAssayType("NIPTDPCR");
		processStepList.add(processStepActionDTO);
	}

	private static RunResultsDTO getRunResultsDTO() {
		RunResultsDTO runResultsDTO = new RunResultsDTO();

		runResultsDTO.setRunResultId(10001L);
		runResultsDTO.setDeviceId("MP001-121");
		runResultsDTO.setProcessStepName("NA Extraction");
		runResultsDTO.setDeviceRunId("order123");
		runResultsDTO.setRunStatus("Completed");
		return runResultsDTO;
	}

	@Test
	public void consumeGenericRequestMP96QueryMessage() throws HMTPException, IOException {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.any())).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<ContainerSamplesDTO>>() {
		})).thenReturn(containerSamples);

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<ProcessStepActionDTO>>() {
		})).thenReturn(processStepList);

		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");

		Response response2 = messageRestAPIImpl.consumeGenericRequest(mp96QueryMessageStr,
				ObjectMapperFactory.DEVICE_STR, DeviceType.MP96, MessageType.MP96_QBP, deviceId);
		Assert.assertEquals(HttpStatus.SC_ACCEPTED, response2.getStatus());
	}

	@Test
	public void consumeGenericRequestMP96QueryMessage2() throws HMTPException, IOException {

	
		Response response2 = messageRestAPIImpl.consumeGenericRequest(mp96QueryMessageStr,
				ObjectMapperFactory.DEVICE_STR, null, null, deviceId);
		Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response2.getStatus());
	}
	
	@Test
	public void consumeGenericRequestMP96ACKMessage() throws HMTPException, IOException {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<ContainerSamplesDTO>>() {
		})).thenReturn(containerSamples);

		Mockito.when(
				RestClient.put(Mockito.anyString(), Mockito.eq(containerSamples), Mockito.eq(null), Mockito.eq(null)))
				.thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		
		Response response = messageRestAPIImpl.consumeGenericRequest(mp96ACKMessageStr, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP96, MessageType.MP96_ACK, deviceId);
		Assert.assertEquals(HttpStatus.SC_ACCEPTED, response.getStatus());
	}

	@Test
	public void consumeGenericRequestMP96OULMessage() throws HMTPException, IOException {

		RunResultsDTO runResultsDTO = getRunResultsDTO();

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(runResultsDTO);

		Mockito.when(RestClientUtil.putMethodCall(Mockito.anyString(), Mockito.any(), Mockito.eq(null)))
				.thenReturn(new Object());

		Response response = messageRestAPIImpl.consumeGenericRequest(mp96OULMessageStr, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP96, MessageType.MP96_OUL, deviceId);
		Assert.assertEquals(HttpStatus.SC_ACCEPTED, response.getStatus());
	}

	@Test
	public void consumeGenericRequestMP96OULMessageNew() throws HMTPException, IOException {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(null);

		Mockito.when(RestClientUtil.putMethodCall(Mockito.anyString(), Mockito.any(), Mockito.eq(null)))
				.thenReturn(new Object());

		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.eq(null), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(12L);

		Response response = messageRestAPIImpl.consumeGenericRequest(mp96OULMessageStr, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP96, MessageType.MP96_OUL, deviceId);
		Assert.assertEquals(HttpStatus.SC_ACCEPTED, response.getStatus());
	}

	@Test
	public void consumeGenericRequestMP96OULMessageFailure() throws HMTPException, IOException {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(null);

		Mockito.when(RestClientUtil.putMethodCall(Mockito.anyString(), Mockito.any(), Mockito.eq(null)))
				.thenReturn(new Object());

		Mockito.when(RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.eq(null), Mockito.eq(null),
				Mockito.eq(Long.class))).thenReturn(0L);

		Response response = messageRestAPIImpl.consumeGenericRequest(mp96OULMessageStr, ObjectMapperFactory.DEVICE_STR,
				DeviceType.MP96, MessageType.MP96_OUL, deviceId);
		Assert.assertEquals(HttpStatus.SC_FAILED_DEPENDENCY, response.getStatus());
	}

	@Test
	public void consumeGenericRequestMP96OULMessageInvalidData() throws HMTPException, IOException {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
		})).thenReturn(null);

		Mockito.when(RestClientUtil.putMethodCall(Mockito.anyString(), Mockito.any(), Mockito.eq(null)))
				.thenReturn(new Object());

		Mockito.when(RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.eq(null), Mockito.eq(null),
				Mockito.eq(Long.class))).thenReturn(12L);

		OULRunResultMessage oulRunResultMessage = objectMapper.readValue(mp96OULMessageStr, OULRunResultMessage.class);
		if (!oulRunResultMessage.getOulSampleResultMessage().isEmpty())
			oulRunResultMessage.getOulSampleResultMessage().get(0).setPosition("");

		Response response = messageRestAPIImpl.consumeGenericRequest(
				objectMapper.writeValueAsString(oulRunResultMessage), ObjectMapperFactory.DEVICE_STR, DeviceType.MP96,
				MessageType.MP96_OUL, deviceId);
		Assert.assertEquals(HttpStatus.SC_ACCEPTED, response.getStatus());
	}
 
	@Test
	public void consumeGenericRequestLP24QueryMessage() throws HMTPException, IOException {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<List<SampleResultsDTO>>() {
		})).thenReturn(listOfSamples);

		Response response2 = messageRestAPIImpl.consumeGenericRequest(lp24QueryMessageStr,
				ObjectMapperFactory.DEVICE_STR, DeviceType.LP24, MessageType.LP24_QBP, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response2.getStatus());
	}
	
	@Test
	public void consumeGenericRequestLP24RSPMessage() throws HMTPException, IOException {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);

		Response response = messageRestAPIImpl.consumeGenericRequest(lp24RSPMessageStr,
				ObjectMapperFactory.CONNECT_STR, DeviceType.LP24, MessageType.LP24_RSP, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	@Test
	public void consumeGenericRequestLP24SSUMessage() throws HMTPException, IOException {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(new GenericType<List<SampleResultsDTO>>() {
		})).thenReturn(listOfSamples);

		Response response = messageRestAPIImpl.consumeGenericRequest(lp24SSUMessageStr,
				ObjectMapperFactory.DEVICE_STR, DeviceType.LP24, MessageType.LP24_U03, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	
	@Test
	public void consumeGenericRequestLP24ACKMessage() throws HMTPException, IOException {

		Mockito.when(RestClientUtil.postMethodCall(Mockito.anyString(), Mockito.any(), Mockito.eq(null))).thenReturn(new Object());
		
		Response response = messageRestAPIImpl.consumeGenericRequest(lp24ACKMessageStr,
				ObjectMapperFactory.CONNECT_STR, DeviceType.LP24, MessageType.LP24_ACK, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	@Test
	public void consumeGenericRequestMP24QueryMessage() throws HMTPException, IOException {
		
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.header(Mockito.anyString(), Mockito.anyString())).thenReturn(builder);
		Mockito.when(builder.post( Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		
		ReflectionTestUtils.setField(messageProcessorService, "wfmQBPRequestUrl", "http://localhost:99/wfm/json/rest/api/v1/startwfmprocess");
		Response response = messageRestAPIImpl.consumeGenericRequest(mp24QueryMessageStr,
				ObjectMapperFactory.DEVICE_STR, DeviceType.MP24, MessageType.MP24_NAEXTRACTION, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	@Test
	public void consumeGenericRequestMP24ACKMessage() throws HMTPException, IOException {
		
		Mockito.when(RestClientUtil.postMethodCall(Mockito.anyString(), Mockito.any(), Mockito.eq(null))).thenReturn(new Object());
		
		Response response = messageRestAPIImpl.consumeGenericRequest(mp24ACKMessageStr,
				ObjectMapperFactory.CONNECT_STR, DeviceType.MP24, MessageType.MP24_ACK, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	@Test
	public void consumeGenericRequestDPCRQBPMessage() throws HMTPException, IOException {
		
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(dpcrQBPMessage);
		Response response = messageRestAPIImpl.consumeGenericRequest(jsonContent,
				ObjectMapperFactory.DEVICE_STR, DeviceType.DPCR_ANALYZER, MessageType.DPCR_ANALYZER_QBP, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	@Test
	public void consumeGenericRequestDPCRACKMessage() throws HMTPException, IOException {
		
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(dpcrACKMessage);
		Response response = messageRestAPIImpl.consumeGenericRequest(jsonContent,
				ObjectMapperFactory.DEVICE_STR, DeviceType.DPCR_ANALYZER, MessageType.DPCR_ANALYZER_ACK, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	@Test
	public void consumeGenericRequestDPCRESUMessage() throws HMTPException, IOException {
		
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(dpcrESUMessage);
		Response response = messageRestAPIImpl.consumeGenericRequest(jsonContent,
				ObjectMapperFactory.DEVICE_STR, DeviceType.DPCR_ANALYZER, MessageType.DPCR_ANALYZER_ESU, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	@Test
	public void consumeGenericRequestDPCRORUMessage() throws HMTPException, IOException {
		
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(dpcrORUMessage);
		Response response = messageRestAPIImpl.consumeGenericRequest(jsonContent,
				ObjectMapperFactory.DEVICE_STR, DeviceType.DPCR_ANALYZER, MessageType.DPCR_ANALYZER_ORU, deviceId);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}
	
	
}
