package com.roche.connect.imm.testng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.mp96.AdaptorACKMessage;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.mp96.OULSampleResultMessage;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.service.MP96AsyncMessageService;
import com.roche.connect.imm.service.MP96MessageService;
import com.roche.connect.imm.service.OrderIntegrationService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;
import com.roche.connect.imm.utils.RestClient;

@PrepareForTest({ RestClientUtil.class, RestClient.class,AdmNotificationService.class  })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class Mp96MessageServiceTest {
	
	
	public static final String MP96QueryMessage = "src/test/java/resource/MP96dPCRQBPPositive.json";
	public static final String containerSamplesJson = "src/test/java/resource/ContainerSampleCSVUpdate.json";
	public static final String mp96ACKMessage = "src/test/java/resource/MP96dPCRACKPositive.json";
	public static final String mp96OULMessage = "src/test/java/resource/MP96dPCROULPositive.json";

	private ObjectMapper objectMapper = new ObjectMapper();

	

	@InjectMocks
	private MP96MessageService mp96MessageService = new MP96MessageService();

	@Mock
	private OrderIntegrationService orderIntegrationService = org.mockito.Mockito.mock(OrderIntegrationService.class);

	@Mock
	private RmmIntegrationService rmmIntegrationService = org.mockito.Mockito.mock(RmmIntegrationService.class);

	@Mock
	private MP96AsyncMessageService mp96AsyncMessageService = Mockito
			.mock(MP96AsyncMessageService.class);

	@Mock
	private Response response;
	
	
	@Mock
	HMTPLoggerImpl loggerImpl;

	
	
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}
	
	
	@Test(priority=1)
	public void processQueryMessage() throws IOException, Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		 List<ContainerSamplesDTO> containerSamples =new ArrayList<>();
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(MP96QueryMessage);
		QueryMessage queryMessage = objectMapper.readValue(jsonContent, QueryMessage.class);
		
		String jsonContent2 = JsonFileReaderAsString.getJsonfromFile(containerSamplesJson);
		TypeReference<List<ContainerSamplesDTO>> class1 = new TypeReference<List<ContainerSamplesDTO>>() {
		};
		containerSamples = objectMapper.readValue(jsonContent2, class1);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(orderIntegrationService.getDPCRContainerSamples()).thenReturn(containerSamples);
		
		String token="1";
		Mockito.doNothing().when(mp96AsyncMessageService).performAsyncQueryMessageRequest(queryMessage, containerSamples, token);
		try {
	 mp96MessageService.processQueryMessage(queryMessage);
		}catch(Exception e) {
			
		}
		
	
	}
	
	
	@Test(priority=2)
	public void processACKMessage() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		 List<ContainerSamplesDTO> containerSamples =new ArrayList<>();
		String jsonContent2 = JsonFileReaderAsString.getJsonfromFile(containerSamplesJson);
		TypeReference<List<ContainerSamplesDTO>> class1 = new TypeReference<List<ContainerSamplesDTO>>() {
		};
		containerSamples = objectMapper.readValue(jsonContent2, class1);
		
		String jsonContent3 = JsonFileReaderAsString.getJsonfromFile(mp96ACKMessage);
		AdaptorACKMessage adaptorACKMessage = objectMapper.readValue(jsonContent3, AdaptorACKMessage.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(orderIntegrationService.getDPCRContainerSamples(adaptorACKMessage.getDeviceRunId())).thenReturn(containerSamples);
		
		String token="1";
		Mockito.doNothing().when(mp96AsyncMessageService).performAsyncACKMessage(adaptorACKMessage, containerSamples, token);
		
		try {
			mp96MessageService.processACKMessage(adaptorACKMessage);
				}catch(Exception e) {
					
				}
	
	}

	@Test(priority=3)
	public void processOULMessage() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		String jsonContent4 = JsonFileReaderAsString.getJsonfromFile(mp96OULMessage);
		OULRunResultMessage oulRunResultMessage = objectMapper.readValue(jsonContent4, OULRunResultMessage.class);
		String token ="1";
		RunResultsDTO runResultsDTO =new RunResultsDTO();
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				oulRunResultMessage.getRunId(), MessageType.MP96_NAEXTRACTION, oulRunResultMessage.getDeviceId())).thenReturn(null);
		
		Mockito.when(orderIntegrationService.isRunIDValid(oulRunResultMessage.getRunId())).thenReturn(true);
		Mockito.doNothing().when(mp96AsyncMessageService).performAsyncOULMessage(oulRunResultMessage, runResultsDTO,token);
		
		try {
			mp96MessageService.processOULMessage(oulRunResultMessage);
				}catch(Exception e) {
					
				}
	
	}
	
	
	
	@Test(priority=4)
	public void processOULMessageRunValidfalse() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		String jsonContent4 = JsonFileReaderAsString.getJsonfromFile(mp96OULMessage);
		OULRunResultMessage oulRunResultMessage = objectMapper.readValue(jsonContent4, OULRunResultMessage.class);
		String token ="1";
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				oulRunResultMessage.getRunId(), MessageType.MP96_NAEXTRACTION, oulRunResultMessage.getDeviceId())).thenReturn(null);
		Mockito.when(orderIntegrationService.isRunIDValid(oulRunResultMessage.getRunId())).thenReturn(false);
		Mockito.doNothing().when(mp96AsyncMessageService).sendNegativeACKMessage(oulRunResultMessage,token);
		
		
		try {
			mp96MessageService.processOULMessage(oulRunResultMessage);
				}catch(Exception e) {
					
				}
	
	}
	
	
	@Test(priority=5)
	public void processOULMessageisRunMovedNextState() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		String token ="1";
		RunResultsDTO runResultsDTO =new RunResultsDTO();
		runResultsDTO.setDeviceRunId("123");
		String jsonContent4 = JsonFileReaderAsString.getJsonfromFile(mp96OULMessage);
		OULRunResultMessage oulRunResultMessage = objectMapper.readValue(jsonContent4, OULRunResultMessage.class);
		OULSampleResultMessage sampleResultMessage = oulRunResultMessage.getOulSampleResultMessage().get(0);
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				oulRunResultMessage.getRunId(), MessageType.MP96_NAEXTRACTION, oulRunResultMessage.getDeviceId())).thenReturn(runResultsDTO);
		
		List<SampleResultsDTO> existingSampleResults =new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setAccesssioningId("1234");
		existingSampleResults.add(sampleResultsDTO);
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, null,
					null, sampleResultMessage.getOutputContainerId(), null, null)).thenReturn(existingSampleResults);
		Mockito.doNothing().when(mp96AsyncMessageService).sendNegativeACKMessage(oulRunResultMessage,token);
		
		
		try {
			mp96MessageService.processOULMessage(oulRunResultMessage);
				}catch(Exception e) {
					
				}
	
	}
	
	@Test(priority=6)
	public void processOULMessageisMP96StatusChanged() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		String token ="1";
		RunResultsDTO runResultsDTO =new RunResultsDTO();
		runResultsDTO.setDeviceRunId("123");
		runResultsDTO.setRunStatus("Completed");
		String jsonContent4 = JsonFileReaderAsString.getJsonfromFile(mp96OULMessage);
		OULRunResultMessage oulRunResultMessage = objectMapper.readValue(jsonContent4, OULRunResultMessage.class);
		OULSampleResultMessage sampleResultMessage = oulRunResultMessage.getOulSampleResultMessage().get(0);
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				oulRunResultMessage.getRunId(), MessageType.MP96_NAEXTRACTION, oulRunResultMessage.getDeviceId())).thenReturn(runResultsDTO);
		oulRunResultMessage.setRunResultStatus("Failed");
		List<SampleResultsDTO> existingSampleResults =new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setAccesssioningId("1234");
		existingSampleResults.add(sampleResultsDTO);
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, null,
					null, sampleResultMessage.getOutputContainerId(), null, null)).thenReturn(null);
		Mockito.doNothing().when(mp96AsyncMessageService).sendNegativeACKMessage(oulRunResultMessage,token);
		
		
		try {
			mp96MessageService.processOULMessage(oulRunResultMessage);
				}catch(Exception e) {
					
				}
	
	}
	
	
	@Test(priority=7)
	public void processOULMessageisMP96StatusChangedElse() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		String token ="1";
		RunResultsDTO runResultsDTO =new RunResultsDTO();
		runResultsDTO.setDeviceRunId("123");
		runResultsDTO.setRunStatus("Failed");
		String jsonContent4 = JsonFileReaderAsString.getJsonfromFile(mp96OULMessage);
		OULRunResultMessage oulRunResultMessage = objectMapper.readValue(jsonContent4, OULRunResultMessage.class);
		OULSampleResultMessage sampleResultMessage = oulRunResultMessage.getOulSampleResultMessage().get(0);
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				oulRunResultMessage.getRunId(), MessageType.MP96_NAEXTRACTION, oulRunResultMessage.getDeviceId())).thenReturn(runResultsDTO);
		oulRunResultMessage.setRunResultStatus("Passed");
		List<SampleResultsDTO> existingSampleResults =new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setAccesssioningId("1234");
		existingSampleResults.add(sampleResultsDTO);
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, null,
					null, sampleResultMessage.getOutputContainerId(), null, null)).thenReturn(null);
		Mockito.doNothing().when(mp96AsyncMessageService).sendNegativeACKMessage(oulRunResultMessage,token);
		
		
		try {
			mp96MessageService.processOULMessage(oulRunResultMessage);
				}catch(Exception e) {
					
				}
	
	}
	
	
	@Test(priority=8)
	public void processOULMessagePositiveElse() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		String token ="1";
		RunResultsDTO runResultsDTO =new RunResultsDTO();
		runResultsDTO.setDeviceRunId("123");
		runResultsDTO.setRunStatus("ABC");
		String jsonContent4 = JsonFileReaderAsString.getJsonfromFile(mp96OULMessage);
		OULRunResultMessage oulRunResultMessage = objectMapper.readValue(jsonContent4, OULRunResultMessage.class);
		OULSampleResultMessage sampleResultMessage = oulRunResultMessage.getOulSampleResultMessage().get(0);
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				oulRunResultMessage.getRunId(), MessageType.MP96_NAEXTRACTION, oulRunResultMessage.getDeviceId())).thenReturn(runResultsDTO);
		oulRunResultMessage.setRunResultStatus("Passed");
		List<SampleResultsDTO> existingSampleResults =new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setAccesssioningId("1234");
		existingSampleResults.add(sampleResultsDTO);
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, null,
					null, sampleResultMessage.getOutputContainerId(), null, null)).thenReturn(null);
		
		Mockito.when(rmmIntegrationService.updateRunResult(runResultsDTO)).thenReturn(true);
		Mockito.doNothing().when(mp96AsyncMessageService).performAsyncOULMessage(oulRunResultMessage, runResultsDTO, token);
		try {
			mp96MessageService.processOULMessage(oulRunResultMessage);
				}catch(Exception e) {
					
				}
	
	}
	
	
	@Test(priority=9)
	public void processACKMessageException() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		String jsonContent3 = JsonFileReaderAsString.getJsonfromFile(mp96ACKMessage);
		AdaptorACKMessage adaptorACKMessage = objectMapper.readValue(jsonContent3, AdaptorACKMessage.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(orderIntegrationService.getDPCRContainerSamples(adaptorACKMessage.getDeviceRunId())).thenThrow(Exception.class);
		
		try {
			mp96MessageService.processACKMessage(adaptorACKMessage);
				}catch(Exception e) {
					
				}
	
	}
	
	@Test(priority=10)
	public void processQueryMessageException() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(MP96QueryMessage);
		QueryMessage queryMessage = objectMapper.readValue(jsonContent, QueryMessage.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(orderIntegrationService.getDPCRContainerSamples()).thenThrow(Exception.class);
		
		try {
			mp96MessageService.processQueryMessage(queryMessage);
				}catch(Exception e) {
					
				}
	
	}

	
	@Test(priority=11)
	public void processQueryMessageEmpty() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		 List<ContainerSamplesDTO> containerSamples =null;
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(MP96QueryMessage);
		QueryMessage queryMessage = objectMapper.readValue(jsonContent, QueryMessage.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(orderIntegrationService.getDPCRContainerSamples()).thenReturn(containerSamples);
		
		try {
			mp96MessageService.processQueryMessage(queryMessage);
				}catch(Exception e) {
					
				}
	
	}
	
	
	@Test(priority=12)
	public void processACKMessageEmpty() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		 List<ContainerSamplesDTO> containerSamples =null;
		String jsonContent3 = JsonFileReaderAsString.getJsonfromFile(mp96ACKMessage);
		AdaptorACKMessage adaptorACKMessage = objectMapper.readValue(jsonContent3, AdaptorACKMessage.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(orderIntegrationService.getDPCRContainerSamples(adaptorACKMessage.getDeviceRunId())).thenReturn(containerSamples);
		
		try {
			mp96MessageService.processACKMessage(adaptorACKMessage);
				}catch(Exception e) {
					
				}
	
	}
	@Test(priority=13)
	public void processOULMessageException() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.mock(RestClient.class);
		 
		String jsonContent4 = JsonFileReaderAsString.getJsonfromFile(mp96OULMessage);
		OULRunResultMessage oulRunResultMessage = objectMapper.readValue(jsonContent4, OULRunResultMessage.class);
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				oulRunResultMessage.getRunId(), MessageType.MP96_NAEXTRACTION, oulRunResultMessage.getDeviceId())).thenThrow(Exception.class);
		
		try {
			mp96MessageService.processOULMessage(oulRunResultMessage);
				}catch(Exception e) {
					
				}
	
	}
	
	
}
