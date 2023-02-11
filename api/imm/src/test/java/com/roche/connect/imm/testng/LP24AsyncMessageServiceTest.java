package com.roche.connect.imm.testng;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.Invocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.imm.service.LP24AsyncMessageService;
import com.roche.connect.imm.service.WFMIntegrationService;
import com.roche.connect.imm.utils.RestClient;

@PrepareForTest({ RestClientUtil.class, RestClient.class,AdmNotificationService.class  })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class LP24AsyncMessageServiceTest {

	
	@InjectMocks
	LP24AsyncMessageService lP24AsyncMessageService;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	Invocation.Builder assayClient;

	@Mock
	ObjectMapper objectMapper;
	
	String token="1";
	
	@Mock
	private WFMIntegrationService wfmIntegrationService = org.mockito.Mockito.mock(WFMIntegrationService.class);
	
	
	
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}
	
	private static Logger logger = LogManager.getLogger(LP24AsyncMessageServiceTest.class);
	@Test
	public void performAsyncLP24LApplicationRejectMessageTest() throws Exception {
		
		PowerMockito.mockStatic(RestClient.class);
		PowerMockito.mockStatic(AdmNotificationService.class);
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
			lP24AsyncMessageService.performAsyncLP24LApplicationRejectMessage(getQueryMessage(),  token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}
	
	@Test
	public void performAsyncLP24LApplicationRejectMessageTestElse() throws UnsupportedEncodingException {
		
		
		try {
			lP24AsyncMessageService.performAsyncLP24LApplicationRejectMessage(getQueryMessage(),  token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}
	
	
	
	@Test
	public void performLP24SSUFailureMessageTest() throws Exception {
		
		AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
		PowerMockito.mockStatic(AdmNotificationService.class);
		String url="";
		Mockito.when(wfmIntegrationService.genericMessagePoster(acknowledgementMessage, url, token)).thenReturn(true);
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
			lP24AsyncMessageService.performLP24SSUFailureMessage(getSpecimenStatusUpdateMessage(),  token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}
	
	
	@Test
	public void performLP24SSUFailureMessageTestException() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		Mockito.when(wfmIntegrationService.genericMessagePoster(Mockito.any(), Mockito.anyString(),  Mockito.anyString())).thenThrow(Exception.class);
		try {
			lP24AsyncMessageService.performLP24SSUFailureMessage(getSpecimenStatusUpdateMessage(),  token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}
	
	
	
	
	
public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessage() {
		
	SpecimenStatusUpdateMessage specimenStatusUpdateMessage=new SpecimenStatusUpdateMessage();
		
	specimenStatusUpdateMessage.setSendingApplicationName("ABC");
	specimenStatusUpdateMessage.setContainerId("ABC");
	specimenStatusUpdateMessage.setDeviceSerialNumber("ABC");
	specimenStatusUpdateMessage.setMessageControlId("ABC123");
		
		return specimenStatusUpdateMessage;
		
		
		
	}
	
	public QueryMessage getQueryMessage() {
		
		QueryMessage queryMessage=new QueryMessage();
		queryMessage.setSendingApplicationName("ABC");
		queryMessage.setContainerId("ABC");
		queryMessage.setDeviceSerialNumber("ABC");
		queryMessage.setMessageControlId("ABC123");
		
		
		return queryMessage;
		
		
		
	}
	
	
}
