package com.roche.connect.imm.testng;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.common.exceptions.InvalidStateException;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.LP24AsyncMessageService;
import com.roche.connect.imm.service.LP24MessageService;
import com.roche.connect.imm.service.MessageProcessorService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;

@PrepareForTest({RestClientUtil.class, HMTPLoggerImpl.class, URLEncoder.class ,AdmNotificationService.class})
@PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" }) 
public class LP24MessageServiceTest{

	public static final String lp24QueryMessage = "src/test/java/resource/lp24QueryMessage.json";
	public static final String lp24SSUMessage = "src/test/java/resource/lp24SSUMessage.json";
	public static final String sampleResultList = "src/test/java/resource/SampleResultList.json";

	@InjectMocks
	private LP24MessageService lp24MessageService = new LP24MessageService();

	@Mock
	private RmmIntegrationService rmmIntegrationService;
	@Mock HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	private LP24AsyncMessageService lp24AsyncMessageService = Mockito
			.mock(LP24AsyncMessageService.class);
	
    @Mock AssayIntegrationService assayService;
    @Mock MessageProcessorService messageProcessorService;
    @Mock AssayIntegrationService assayIntegrationService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private QueryMessage queryMessage = null;
    private List<SampleResultsDTO> sampleResultDTOList = null;
    private SpecimenStatusUpdateMessage specimenStatusUpdateMessage = null;
    List<ProcessStepActionDTO> processStepList = new ArrayList<>();
    List<SampleResultsDTO> sampleResultDTOListsForHTP  = new ArrayList<>();

	@BeforeMethod
	public void beforeMethod() throws Exception {

		MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing().when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(lp24QueryMessage);
		queryMessage = objectMapper.readValue(jsonContent, QueryMessage.class);

		String jsonContent2 = JsonFileReaderAsString.getJsonfromFile(sampleResultList);
		TypeReference<List<SampleResultsDTO>> class1 = new TypeReference<List<SampleResultsDTO>>() {
		};
		sampleResultDTOList = objectMapper.readValue(jsonContent2, class1);

		String jsonContent4 = JsonFileReaderAsString.getJsonfromFile(lp24SSUMessage);
		specimenStatusUpdateMessage = objectMapper.readValue(jsonContent4, SpecimenStatusUpdateMessage.class);
		processStepList.add(getProcessStepActionDTOForHTP());
		processStepList.add(getProcessStepActionDTO());
		sampleResultDTOListsForHTP.add(getSampleResultsDTO());
	}
	
	@ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

	@Test
	public void processQueryMessage() throws IOException {
		String[] s = queryMessage.getContainerId().split("_");
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, s[0], s[1], null, null, null))
				.thenReturn(sampleResultDTOList);
		Response response = lp24MessageService.processQueryMessage(queryMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void processQueryMessage2() throws IOException {
		Mockito.when(rmmIntegrationService.getSampleResults(Mockito.isNull(), Mockito.isNull(), Mockito.anyString(),
				Mockito.anyString(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull()))
				.thenReturn(Collections.emptyList());
		Response response = lp24MessageService.processQueryMessage(queryMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void processQueryMessage3() throws IOException {
		sampleResultDTOList.clear();
		sampleResultDTOList.add(null);
		Mockito.when(rmmIntegrationService.getSampleResults(Mockito.isNull(), Mockito.isNull(), Mockito.anyString(),
				Mockito.anyString(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull()))
				.thenReturn(sampleResultDTOList);
		Response response = lp24MessageService.processQueryMessage(queryMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void processQueryMessage4() throws IOException {
		Mockito.when(rmmIntegrationService.getSampleResults(Mockito.isNull(), Mockito.isNull(), Mockito.anyString(),
				Mockito.anyString(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull())).thenReturn(null);
		Response response = lp24MessageService.processQueryMessage(queryMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void processQueryMessageElseTest() throws IOException {
	  Mockito.when(rmmIntegrationService.getSampleResults(null, null,
                    "67890", "A1", null, null, null)).thenReturn(sampleResultDTOListsForHTP);
	    Mockito.when( assayService.getProcessStepAction(AssayType.NIPT_HTP,
                        DeviceType.LP24)).thenReturn(processStepList);
        Mockito.when(assayIntegrationService.getProcessStepAction(AssayType.NIPT_HTP,
                null)).thenReturn(processStepList);
        Mockito.when(rmmIntegrationService.getRunResultsById(Mockito.anyLong())).thenReturn(getRunResultsDTO());
	    Mockito.doNothing().when(messageProcessorService).processLpSeqRequest(Mockito.any(QueryMessage.class), Mockito.anyLong(), Mockito.anyString());
	    lp24MessageService.processQueryMessage(getQueryMessageForHTP());
	}
	public SampleResultsDTO getSampleResultsDTO() {
	    SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
	    sampleResultsDTO.setAssayType("NIPTHTP");
	    sampleResultsDTO.setRunResultsId(1L);
	    sampleResultsDTO.setOrderId(1L);
        return sampleResultsDTO;
	}
	
	public RunResultsDTO getRunResultsDTO() {
	    RunResultsDTO runResultsDTO  = new RunResultsDTO();
	    runResultsDTO.setAssayType("NIPTHTP");
	    runResultsDTO.setProcessStepName("Library Preparation");
	    return runResultsDTO;
	}
	public QueryMessage getQueryMessageForHTP() {
	    QueryMessage query = new QueryMessage();
	    query.setContainerId("67890_A1");
        return query;
	    
	}
	
	public ProcessStepActionDTO getProcessStepActionDTOForHTP() {
	    ProcessStepActionDTO process = new ProcessStepActionDTO();
	    process.setAssayType("NIPTHTP");
	    process.setProcessStepName("Library Preparation");
	    process.setProcessStepSeq(4);
	    return process;
	}
	
	public ProcessStepActionDTO getProcessStepActionDTO() {
        ProcessStepActionDTO process = new ProcessStepActionDTO();
        process.setAssayType("NIPTHTP");
        process.setProcessStepName("Library Preparation");
        process.setProcessStepSeq(5);
        return process;
    }
	
	@Test
	public void processInvalidContainerIdQueryMessage() throws IOException {
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, null, null, null, null, null))
				.thenReturn(sampleResultDTOList);
		Response response = lp24MessageService.processQueryMessage(queryMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void processInvalidMP96OutputContainerIdQueryMessage() throws IOException {
		queryMessage.setContainerId("6e75e9b8_A55");
		String[] s = queryMessage.getContainerId().split("_");
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, s[0], s[1], null, null, null))
				.thenReturn(sampleResultDTOList);
		Response response = lp24MessageService.processQueryMessage(queryMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void processInvalidLP24InputContainerIdQueryMessage() throws IOException {
		queryMessage.setContainerId("6e75e9b8");
		Response response = lp24MessageService.processQueryMessage(queryMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	
	
	@Test
	public void processQueryMessageException() throws IOException {
		queryMessage.setContainerId("6e75e9b8_B1");
		String[] s = queryMessage.getContainerId().split("_");
		String inputContainerId = s[0];
		String inputContainerPosition = s[1];
		Mockito.when(rmmIntegrationService.getSampleResults(null, null,inputContainerId, inputContainerPosition, null, null, null))
		.thenThrow(Exception.class);
		 lp24MessageService.processQueryMessage(queryMessage);
	
	}
	@Test
	public void processStatusUpdateMessages() throws IOException {
		String[] s = specimenStatusUpdateMessage.getContainerId().split("_");
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, s[0], s[1], null, null, null))
				.thenReturn(sampleResultDTOList);
		Response response = lp24MessageService.processStatusUpdateMessages(specimenStatusUpdateMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void processInvalidContainerIdStatusUpdateMessages() throws IOException {
		Mockito.when(rmmIntegrationService.getSampleResults(Mockito.isNull(), Mockito.isNull(), Mockito.anyString(),
				Mockito.anyString(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull()))
				.thenReturn(Collections.emptyList());
		Response response = lp24MessageService.processStatusUpdateMessages(specimenStatusUpdateMessage);
		Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatus());
	}

	@Test
	public void processInvalidContainerIdStatusUpdateMessages2() throws IOException {
		Mockito.when(rmmIntegrationService.getSampleResults(Mockito.isNull(), Mockito.isNull(), Mockito.anyString(),
				Mockito.anyString(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull())).thenReturn(null);
		Response response = lp24MessageService.processStatusUpdateMessages(specimenStatusUpdateMessage);
		Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatus());
	}

	@Test
	public void processInvalidMP96OutputContainerIdStatusUpdateMessages() throws IOException {
		specimenStatusUpdateMessage.setContainerId("6e75e9b8_A55");
		String[] s = specimenStatusUpdateMessage.getContainerId().split("_");
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, s[0], s[1], null, null, null))
				.thenReturn(sampleResultDTOList);
		Response response = lp24MessageService.processStatusUpdateMessages(specimenStatusUpdateMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void processInvalidLP24InputContainerIdStatusUpdateMessages() throws IOException {
		specimenStatusUpdateMessage.setContainerId("6e75e9b8");
		Response response = lp24MessageService.processStatusUpdateMessages(specimenStatusUpdateMessage);
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void processInvalidContainerIdStatusUpdateMessages3() throws IOException {
		sampleResultDTOList.clear();
		sampleResultDTOList.add(null);
		Mockito.when(rmmIntegrationService.getSampleResults(Mockito.isNull(), Mockito.isNull(), Mockito.anyString(),
				Mockito.anyString(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull()))
				.thenReturn(sampleResultDTOList);
		Response response = lp24MessageService.processStatusUpdateMessages(specimenStatusUpdateMessage);
		Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatus());
	}
	
	@Test
	public void processStatusUpdateMessagesHTPTest() throws IOException, HMTPException, InvalidStateException {
	    Mockito.when(rmmIntegrationService.getSampleResults(null, null,
                    "6789", "A1", null, null, null)).thenReturn(sampleResultDTOListsForHTP);
	    Mockito.when(assayService.getProcessStepAction(AssayType.NIPT_HTP,
                        DeviceType.LP24)).thenReturn(processStepList);
	    Mockito.when(assayIntegrationService.getProcessStepAction(AssayType.NIPT_HTP,
            null)).thenReturn(processStepList);
    Mockito.when(rmmIntegrationService.getRunResultsById(Mockito.anyLong())).thenReturn(getRunResultsDTO());
    Mockito.doNothing().when(messageProcessorService).processLpSeqRequest(Mockito.any(SpecimenStatusUpdateMessage.class), Mockito.anyString());
	    
	    lp24MessageService.processStatusUpdateMessages(getSpecimenStatusUpdateMessageForHTP());
	}
	
	
	
	
	@Test
	public void processQueryResponseMessage() throws Exception {
		ResponseMessage responseMessage=new ResponseMessage();
		responseMessage.setAccessioningId("12234");
		
		Mockito.doThrow(Exception.class).when(messageProcessorService).submitResponse(responseMessage);
		lp24MessageService.processQueryResponseMessage(responseMessage);
	}
	
	@Test
	public void processACKMessages() throws Exception {
		AcknowledgementMessage acknowledgementMessage=new AcknowledgementMessage();
		
		acknowledgementMessage.setContainerId("123");
		Mockito.doThrow(Exception.class).when(messageProcessorService).submitResponse(acknowledgementMessage);
		lp24MessageService.processACKMessages(acknowledgementMessage);
	}
	
	
	
	public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessageForHTP() {
	    SpecimenStatusUpdateMessage specimen = new SpecimenStatusUpdateMessage();
	    specimen.setContainerId("6789_A1");
        return specimen;
        
    }
}
