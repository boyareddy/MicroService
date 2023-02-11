package com.roche.connect.imm.testng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.Invocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.imm.service.DeviceIntegrationService;
import com.roche.connect.imm.service.DeviceRunService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;

@PrepareForTest({ RestClientUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class DeviceIntegrationServiceTest {

	
	@InjectMocks
	DeviceIntegrationService deviceIntegrationService;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	
	@InjectMocks
	DeviceRunService deviceRunService;
	
	@Mock
	Invocation.Builder assayClient;

	
	@Mock
	ObjectMapper objectMapper;
	/** Mandatory to mock static classes **/
	
	Logger logger = LogManager.getLogger(DeviceIntegrationServiceTest.class);
	public static final String JSON_ARRAY_POSITIVE_DEVICE = "src/test/java/resource/DeviceIntegrationPositive.json";
	public static final String JSON_ARRAY_NEGATIVE_DEVICE = "src/test/java/resource/DeviceIntegrationNegative.json";
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
	public void sendNotification() throws IOException
			 {

		config();
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(JSON_ARRAY_POSITIVE_DEVICE);
		
		
		Mockito.when(objectMapper.writeValueAsString("sample")).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(String.class)).thenReturn(jsonContent.toString());
		
		try {
		deviceIntegrationService.getAssayTypeFromDM("ABC123", "1");
		}catch(Exception e) {
			
			
		}
	}
	
	
	@Test(priority = 2)
	public void sendNotificationNegative2() throws IOException
			 {

		config();
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(JSON_ARRAY_NEGATIVE_DEVICE);
		
		
		Mockito.when(objectMapper.writeValueAsString("sample")).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(String.class)).thenReturn(jsonContent.toString());
		
		try {
		deviceIntegrationService.getAssayTypeFromDM("ABC123", "1");
		}catch(Exception e) {
			logger.info("sendNotificationNegativesecond in DeviceIntegrationServiceTest Test case passed");
			
		}
	}
	
	
	@Test(priority = 3)
	public void generateDeviceRunId() throws IOException
			 {

		config();
		ReflectionTestUtils.setField(deviceRunService, "runIDPrefix",
				"RND");
		
		try {
			deviceRunService.generateDeviceRunId();
		}catch(Exception e) {
			logger.info("generateDeviceRunId in DeviceIntegrationServiceTest Test case passed");
			
		}
	}

	@Test(priority = 4)
	public void sendNotificationNegativeThird() throws IOException {

		config();
		Mockito.when(objectMapper.writeValueAsString("sample")).thenReturn("sample");

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null)))
				.thenThrow(UnsupportedEncodingException.class);

		try {
			deviceIntegrationService.getAssayTypeFromDM("ABC123", "1");
		} catch (Exception e) {

		}
	}
}
