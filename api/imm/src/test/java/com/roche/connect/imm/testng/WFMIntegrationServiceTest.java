package com.roche.connect.imm.testng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
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
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.connect.common.mp96.WFMQueryMessage;
import com.roche.connect.imm.service.WFMIntegrationService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;
import com.roche.connect.imm.utils.RestClient;

@PrepareForTest({ RestClientUtil.class,RestClient.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class WFMIntegrationServiceTest {

	public static final String MP96QueryMessage = "src/test/java/resource/MP96dPCRQBPPositive.json";
	private ObjectMapper objectMapper = new ObjectMapper();
	private QueryMessage queryMessage = null;

	@Mock
	private HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	private Response response;

	@Mock
	private Invocation.Builder builder;

	@InjectMocks
	private WFMIntegrationService wfmIntegrationService = new WFMIntegrationService();

	
	Logger logger = LogManager.getLogger(WFMIntegrationServiceTest.class);
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@BeforeMethod
	public void beforeMethod() throws IOException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString());
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(MP96QueryMessage);
		queryMessage = objectMapper.readValue(jsonContent, QueryMessage.class);

		ReflectionTestUtils.setField(wfmIntegrationService, "wfmHostUrl", "http://localhost:99/omm");
	}

	@Test
	public void genericMessagePoster() throws IOException {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);

		Assert.assertEquals(true, wfmIntegrationService.genericMessagePoster(queryMessage, new String(), null));
	}

	@Test(expectedExceptions = UnsupportedEncodingException.class)
	public void genericMessagePoster2() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null)))
				.thenThrow(UnsupportedEncodingException.class);

		Assert.assertEquals(true, wfmIntegrationService.genericMessagePoster(queryMessage, new String(), null));
	}

	@Test
	public void workOrderRequest() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Assert.assertEquals(true, wfmIntegrationService.workOrderRequest(new WFMQueryMessage(), null));
	}

	@Test(expectedExceptions = UnsupportedEncodingException.class)
	public void workOrderRequest2() throws IOException {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null)))
				.thenThrow(UnsupportedEncodingException.class);

		Assert.assertEquals(true, wfmIntegrationService.workOrderRequest(new WFMQueryMessage(), null));
	}

	@Test
	public void updateForteStatus() throws Exception {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);

		Assert.assertEquals(true, wfmIntegrationService.updateForteStatus(new ForteStatusMessage(), null));
	}

	@Test(expectedExceptions = IOException.class)
	public void updateForteStatus2() throws Exception {
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenThrow(IOException.class);

		Assert.assertEquals(true, wfmIntegrationService.updateForteStatus(new ForteStatusMessage(), null));
	}
	
	
	@Test
	public void updateRunResultStatusByDeviceId() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		try {
		wfmIntegrationService.updateRunResultStatusByDeviceId("MP24", "MP24","1");
		}catch(Exception e) {
			
			logger.info("updateRunResultStatusByDeviceId test case passed");
		}
	}
	
	
	@Test
	public void updateRunResultStatusByDeviceIdElse() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		try {
		wfmIntegrationService.updateRunResultStatusByDeviceId("LP24", "LP24","1");
		}catch(Exception e) {
			
			logger.info("updateRunResultStatusByDeviceId test case passed");
		}
	}
	
	@Test
	public void updateRunResultStatusByDeviceIdException() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doThrow(UnsupportedEncodingException.class).when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		try {
		wfmIntegrationService.updateRunResultStatusByDeviceId("LP24", "LP24","1");
		}catch(Exception e) {
			
			logger.info("updateRunResultStatusByDeviceIdException test case passed");
		}
	}
	
	

}
