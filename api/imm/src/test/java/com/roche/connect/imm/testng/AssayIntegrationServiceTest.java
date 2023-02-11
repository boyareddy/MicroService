package com.roche.connect.imm.testng;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.imm.service.AssayIntegrationService;

@PrepareForTest({ RestClientUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class AssayIntegrationServiceTest {

	@InjectMocks
	AssayIntegrationService assayIntegrationService;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	Invocation.Builder assayClient;

	@Mock
	ObjectMapper objectMapper;
	
	String url = null;

	String processStepName = null;
	String ProcessStep = null;

	String assayType;
	String deviceType;
	String token;
	List<ProcessStepActionDTO> processStepList = new ArrayList<>();
	List<DeviceTestOptionsDTO> deviceTestOptionsDTOList = new ArrayList<DeviceTestOptionsDTO>();
	ProcessStepActionDTO processStepActionDTO = null;
	DeviceTestOptionsDTO deviceTestOptionsDTO = null;

	@BeforeTest
	public void setUp() throws HMTPException, UnsupportedEncodingException {

		assayType = "NIPTDPCR";
		deviceType = "MP96";
		token = "abc";
		processStepActionDTO = new ProcessStepActionDTO();
		deviceTestOptionsDTO = new DeviceTestOptionsDTO();
		deviceTestOptionsDTO.setAssayType("NIPTDPCR");
		deviceTestOptionsDTOList.add(deviceTestOptionsDTO);
		processStepActionDTO.setProcessStepName("NA Extraction");
		processStepActionDTO.setAssayType("NIPTDPCR");
		processStepList.add(processStepActionDTO);
		url = "http://localhost:88/amm/NIPTDPCR/testoptions?deviceType=MP96/";


	}

	/** Mandatory to mock static classes **/
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	public void config() {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
	}

	@Test(priority = 1)
	public void getDeviceTestOptionsData() throws HMTPException, UnsupportedEncodingException, JsonProcessingException {

		config();

		Mockito.when(objectMapper.writeValueAsString(deviceTestOptionsDTOList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {
		})).thenReturn(deviceTestOptionsDTOList);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		List<DeviceTestOptionsDTO> deviceTestOptionsDTOlist = assayIntegrationService
				.getDeviceTestOptionsData(assayType, deviceType);

		assertEquals(deviceTestOptionsDTOlist.get(0).getAssayType(), "NIPTDPCR");
	}

	@Test(priority = 1)
	public void getDeviceTestOptionsData2()
			throws HMTPException, UnsupportedEncodingException, JsonProcessingException {

		config();

		Mockito.when(objectMapper.writeValueAsString(deviceTestOptionsDTOList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {
		})).thenReturn(deviceTestOptionsDTOList);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");

		List<DeviceTestOptionsDTO> deviceTestOptionsDTOlist = assayIntegrationService
				.getDeviceTestOptionsData(assayType, null);

		assertEquals(deviceTestOptionsDTOlist.get(0).getAssayType(), "NIPTDPCR");
	}
	
	@Test(priority = 1)
	public void getDeviceTestOptionsData4()
			throws HMTPException, UnsupportedEncodingException, JsonProcessingException {

		config();

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {
		})).thenThrow(UnsupportedEncodingException.class);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");

		Mockito.when(objectMapper.writeValueAsString(deviceTestOptionsDTOList))
				.thenThrow(JsonProcessingException.class);

		List<DeviceTestOptionsDTO> deviceTestOptionsDTOlist = assayIntegrationService
				.getDeviceTestOptionsData(assayType, null);

		assertEquals(deviceTestOptionsDTOlist, null);
	}
	
	@Test(priority = 2)
	public void getProcessStepAction() throws HMTPException, UnsupportedEncodingException, JsonProcessingException {

		config();

		Mockito.when(objectMapper.writeValueAsString(processStepList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {
		})).thenReturn(processStepList);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		List<ProcessStepActionDTO> processsteplist = assayIntegrationService.getProcessStepAction(assayType,
				deviceType);

		assertEquals(processsteplist.get(0).getAssayType(), "NIPTDPCR");
	}

	@Test(priority = 2)
	public void getProcessStepAction2() throws HMTPException, UnsupportedEncodingException, JsonProcessingException {

		config();

		Mockito.when(objectMapper.writeValueAsString(processStepList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {
		})).thenReturn(processStepList);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		List<ProcessStepActionDTO> processsteplist = assayIntegrationService.getProcessStepAction(assayType, null);

		assertEquals(processsteplist.get(0).getAssayType(), "NIPTDPCR");
	}

	@Test(priority = 2)
	public void getProcessStepAction4() throws HMTPException, UnsupportedEncodingException, JsonProcessingException {

		config();
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenThrow(UnsupportedEncodingException.class);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		
		List<ProcessStepActionDTO> processsteplist = assayIntegrationService.getProcessStepAction(assayType, null);

		assertEquals(processsteplist, null);
	}
	
	@Test(priority = 3)
	public void getTestOptionsByOrderIdAndDeviceAndProcessStep()
			throws IOException, UnsupportedEncodingException, JsonProcessingException {

		config();

		Mockito.when(objectMapper.writeValueAsString(processStepList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {
		})).thenReturn(deviceTestOptionsDTOList);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		List<DeviceTestOptionsDTO> deviceTestOptionsDTOlist = assayIntegrationService
				.getTestOptionsByOrderIdandDeviceandProcessStep(assayType, deviceType, processStepName);

		assertEquals(deviceTestOptionsDTOlist.get(0).getAssayType(), "NIPTDPCR");
	}

	@Test(priority = 4)
	public void getTestOptionsByOrderIdAndDeviceAndProcessStepElse()
			throws IOException, UnsupportedEncodingException, JsonProcessingException {

		config();
		processStepName = "NA Extraction";
		Mockito.when(objectMapper.writeValueAsString(processStepList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {
		})).thenReturn(deviceTestOptionsDTOList);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		List<DeviceTestOptionsDTO> deviceTestOptionsDTOlist = assayIntegrationService
				.getTestOptionsByOrderIdandDeviceandProcessStep(assayType, deviceType, processStepName);

		assertEquals(deviceTestOptionsDTOlist.get(0).getAssayType(), "NIPTDPCR");
	}

	@Test(priority = 4, expectedExceptions = IOException.class)
	public void getTestOptionsByOrderIdAndDeviceAndProcessStep2()
			throws IOException, UnsupportedEncodingException, JsonProcessingException {

		config();
		processStepName = "NA Extraction";
		Mockito.when(objectMapper.writeValueAsString(processStepList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null)))
				.thenThrow(UnsupportedEncodingException.class);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(assayType, deviceType, processStepName);

	}

	@Test(priority = 4)
	public void getTestOptionsByOrderIdAndDeviceAndProcessStep3()
			throws IOException, UnsupportedEncodingException, JsonProcessingException {

		config();
		processStepName = "NA Extraction";
		Mockito.when(objectMapper.writeValueAsString(processStepList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {
		})).thenReturn(Collections.emptyList());
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		List<DeviceTestOptionsDTO> deviceTestOptionsDTOlist = assayIntegrationService
				.getTestOptionsByOrderIdandDeviceandProcessStep(assayType, deviceType, processStepName);

		assertEquals(deviceTestOptionsDTOlist, Collections.emptyList());
	}
	
	@Test(priority = 5)
	public void findProcessStepByAssayTypeAndDeviceType()
			throws HMTPException, UnsupportedEncodingException, JsonProcessingException {

		config();
		processStepName = "NA Extraction";
		Mockito.when(objectMapper.writeValueAsString(processStepList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {
		})).thenReturn(processStepList);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		List<ProcessStepActionDTO> processsteplist = assayIntegrationService
				.findProcessStepByAssayTypeAndDeviceType(assayType, deviceType, token);

		assertEquals(processsteplist.get(0).getAssayType(), "NIPTDPCR");
	}

	@Test(priority = 5)
	public void findProcessStepByAssayTypeAndDeviceType2()
			throws HMTPException, UnsupportedEncodingException, JsonProcessingException {

		config();
		processStepName = "NA Extraction";
		Mockito.when(objectMapper.writeValueAsString(processStepList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {
		})).thenReturn(processStepList);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		List<ProcessStepActionDTO> processsteplist = assayIntegrationService
				.findProcessStepByAssayTypeAndDeviceType(assayType, null, token);

		assertEquals(processsteplist.get(0).getAssayType(), "NIPTDPCR");
	}

	@Test(priority = 6, expectedExceptions = HMTPException.class)
	public void findProcessStepByAssayTypeAndDeviceType3()
			throws HMTPException, UnsupportedEncodingException, JsonProcessingException {

		config();
		processStepName = "NA Extraction";
		Mockito.when(objectMapper.writeValueAsString(processStepList)).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {
		})).thenThrow(Exception.class);
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostUrl", "http://localhost:88/amm");
		ReflectionTestUtils.setField(assayIntegrationService, "ammDeviceTestOptionsUrl", "testoptions");
		List<ProcessStepActionDTO> processsteplist = assayIntegrationService
				.findProcessStepByAssayTypeAndDeviceType(assayType, null, token);

		assertEquals(processsteplist.get(0).getAssayType(), "NIPTDPCR");
	}
	
	@Test(priority = 7)
	public void getDeviceTestOptionsDataJsonException()
			throws HMTPException, JsonProcessingException {

		config();
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenThrow(JsonProcessingException.class);
		 try {
		assayIntegrationService.getDeviceTestOptionsData(assayType, "MP96");
		 }catch(Exception e) {
			 
		 }

		
	}
	
	@Test(priority = 8)
	public void getProcessStepActionJsonException()
			throws HMTPException, JsonProcessingException {

		config();
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenThrow(JsonProcessingException.class);
		 try {
		assayIntegrationService.getProcessStepAction(assayType, "MP96");
		 }catch(Exception e) {
			 
		 }

		
	}
	
}
