package com.roche.connect.imm.testng;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO.ComplexIdDetailsStatus;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.imm.model.MessageStore;
import com.roche.connect.imm.rest.HTPRestAPIImpl;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.MessageProcessorService;
import com.roche.connect.imm.service.MessageService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.utils.RestClient;
import com.roche.connect.imm.writerepository.MessageStoreWriteRepository;


@PrepareForTest({ RestClientUtil.class, RestClient.class,AdmNotificationService.class  })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class HTPRestAPIImplTest {


	@InjectMocks
	HTPRestAPIImpl htpRestImpl = new HTPRestAPIImpl();

	
	@Mock
	HMTPLoggerImpl loggerImpl;
	
	@Mock
	private MessageService messageService;

	@Mock
	private MessageProcessorService messageProcessorService;

	@Mock
	private RmmIntegrationService rmmIntegrationService;


	@Mock
	private MessageStoreWriteRepository messageStoreWriteRepository;
			
	
	@Mock  
	private AssayIntegrationService assayIntegrationService;
	
	@Mock
	private ObjectMapper objectMapper;

	
	  List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();

	  @ObjectFactory
		public IObjectFactory getObjectFactory() {
			return new org.powermock.modules.testng.PowerMockObjectFactory();
		}

	@Test
	public void createRunPositiveTest() throws HMTPException, JsonProcessingException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		 List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();
		deviceTestOptions.add(getDeviceTestOptionsDTO());
		Mockito.when(messageStoreWriteRepository.save(Mockito.any(MessageStore.class))).thenReturn(getMessageStore());
		Mockito.doNothing().when(messageService).saveMessage(getRunResultsDTO());
		Mockito.doNothing().when(messageProcessorService).saveRunResult(getRunResultsDTO());
		  Mockito.when( assayIntegrationService.getDeviceTestOptionsData(Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		htpRestImpl.createRun(getRunResultsDTO());
	}

	@Test
	public void createRunNegativeTestSecond() throws HMTPException, JsonProcessingException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		  Mockito.when( assayIntegrationService.getDeviceTestOptionsData(Mockito.anyString(),Mockito.anyString())).thenThrow(JsonProcessingException.class);
		  try {
		  htpRestImpl.createRun(getRunResultsDTO());
		  }catch(Exception e) {
				
			}
	}
	@Test
	public void createRunNegativeTest() throws JsonProcessingException, HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		 List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();
		deviceTestOptions.add(getDeviceTestOptionsDTO());
		Mockito.doThrow(JsonProcessingException.class).when(messageService).saveMessage(getRunResultsDTO());
		try {
		Mockito.when( assayIntegrationService.getDeviceTestOptionsData(Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		}catch(Exception e) {
			
		}
	}

	@Test
	public void createRunNotification() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(AdmNotificationService.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		List<DeviceTestOptionsDTO> deviceTestOptionsList = new ArrayList<>();
		DeviceTestOptionsDTO deviceTestOptionsDTO=new DeviceTestOptionsDTO();
		deviceTestOptionsDTO.setTestProtocol("HTtocol");
		deviceTestOptionsList.add(deviceTestOptionsDTO);
		Mockito.when(assayIntegrationService.getDeviceTestOptionsData(Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptionsList);
		 htpRestImpl.createRun(getRunResultsDTO());
	}
	
	
	@Test
	public void updateRunPositiveTest() throws HMTPException, JsonProcessingException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(objectMapper.writeValueAsString(getRunResultsDTO())).thenReturn("ABC");
		Mockito.doNothing().when(messageService).saveMessage(getRunResultsDTO());
		Mockito.doNothing().when(messageProcessorService).updateRunResults(getRunResultsDTO());
		 htpRestImpl.updateRun("1234", getRunResultsDTO());
		
	}

	@Test
	public void updateRunNegativeTest() throws JsonProcessingException, HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(objectMapper.writeValueAsString(getRunResultsDTO())).thenReturn("ABC");
		Mockito.doThrow(JsonProcessingException.class).when(messageService).saveMessage(getRunResultsDTO());
		  htpRestImpl.updateRun("1234", getRunResultsDTO());
		
	}

	@Test
	public void updateRunNegativeSecondTest() throws  HMTPException, JsonProcessingException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(objectMapper.writeValueAsString(getRunResultsDTO())).thenReturn("ABC");
		Mockito.doThrow(HMTPException.class).when(messageService).saveMessage(getRunResultsDTO());
		  htpRestImpl.updateRun("1234", getRunResultsDTO());
		
	}
	
	@Test
	public void getRunResultsByDeviceRunIdPTest() throws UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(rmmIntegrationService.getRunResultsByDeviceRunId(Mockito.anyString())).thenReturn(getRunResultsDTO());
		htpRestImpl.getRunResultsByDeviceRunId(getRunResultsDTO().getDeviceRunId());
		
	}

	@Test
	public void getRunResultsByDeviceRunIdNTest() throws UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.doThrow(UnsupportedEncodingException.class).when(rmmIntegrationService)
				.getRunResultsByDeviceRunId(Mockito.anyString());
		htpRestImpl.getRunResultsByDeviceRunId(getRunResultsDTO().getDeviceRunId());
		
	}
	
	
	@Test
	public void getRunResultsByDeviceRunIdNullTest() throws UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		RunResultsDTO runResultsDto=null;
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(rmmIntegrationService.getRunResultsByDeviceRunId(Mockito.anyString())).thenReturn(runResultsDto);
		htpRestImpl.getRunResultsByDeviceRunId(getRunResultsDTO().getDeviceRunId());
		
	}
	@Test
	public void getRunResultsByDeviceRunIdNullDeviceRunIDTest() throws UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		htpRestImpl.getRunResultsByDeviceRunId(null);
		
	}
	

	@Test
	public void getComplexDetailsPTest() throws UnsupportedEncodingException, HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(messageProcessorService.getComplexIdDetailsByComplexId(Mockito.anyString()))
				.thenReturn(getComplexIdDetailsDTO());
		htpRestImpl.getComplexDetails("6789976");
	}

	@Test
	public void getComplexDetailsNTest() throws UnsupportedEncodingException, HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(messageProcessorService.getComplexIdDetailsByComplexId(Mockito.anyString()))
				.thenReturn(getComplexIdNDetailsDTO());
		 htpRestImpl.getComplexDetails("6789976");
	}
	
	
	
	

	public RunResultsDTO getRunResultsDTO() {
		RunResultsDTO runResults = new RunResultsDTO();
		runResults.setDeviceRunId("155155300");
		runResults.setDeviceId("3333");
		runResults.setDvcRunResult(null);
		runResults.setProcessStepName("Sequencing");
		runResults.setRunStatus("Completed");
		runResults.setComments("Run information for HTP/Sequencing");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		runResults.setRunStartTime(ts);
		runResults.setRunRemainingTime(System.currentTimeMillis());
		runResults.setOperatorName("Mike");
		runResults.setRunFlag("Y");
		runResults.setUpdatedBy("John");
		runResults.setVerifiedBy("Mary");
		Collection<RunResultsDetailDTO> runResultsDetail = new ArrayList<>();
		RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
		runResultsDetailDTO.setAttributeName("protocol");
		runResultsDetailDTO.setAttributeValue("HTP Protocol");
		runResultsDetail.add(runResultsDetailDTO);
		runResults.setRunResultsDetail(runResultsDetail);
		return runResults;
	}

	public MessageStore getMessageStore() {
		MessageStore msg = new MessageStore();
		msg.setDeviceID("1234");
		return msg;
	}

	private ComplexIdDetailsDTO getComplexIdDetailsDTO() {
		ComplexIdDetailsDTO complexIdDetails = new ComplexIdDetailsDTO();
		complexIdDetails.setStatus(ComplexIdDetailsStatus.READY);
		complexIdDetails.setComplexCreatedDatetime(new Timestamp(0));
		return complexIdDetails;
	}

	private ComplexIdDetailsDTO getComplexIdNDetailsDTO() {
		ComplexIdDetailsDTO complexIdNDetails = new ComplexIdDetailsDTO();
		complexIdNDetails.setStatus(ComplexIdDetailsStatus.INVALID);
		complexIdNDetails.setComplexCreatedDatetime(new Timestamp(0));
		return complexIdNDetails;
	}
	
	
	
	public DeviceTestOptionsDTO getDeviceTestOptionsDTO() {
	    DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
	    deviceTestOptionsDTO.setTestProtocol("HTP Protocol");
        return deviceTestOptionsDTO;
	}
	


}
