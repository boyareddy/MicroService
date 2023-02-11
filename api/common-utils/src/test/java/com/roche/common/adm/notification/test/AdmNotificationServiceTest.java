package com.roche.common.adm.notification.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.reflections.ReflectionUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.common.server.security.entity.PasJwtToken;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.common.adm.notification.RestClient;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.common.constant.NotificationGroupType;

@PrepareForTest({RestClient.class}) @PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*",
"javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*", "org.apache.logging.*",
"org.slf4j.*" }) public class AdmNotificationServiceTest {
    @InjectMocks AdmNotificationService admNotificationService;
    @Mock Response response;
    
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    
    @Mock ObjectMapper mockObjectMapper;
    
    String url = "http://localhost";
    List<String> contents = new ArrayList<String>();
    MessageDto messagedto = getMessageDto();
    List<Object> list =new ArrayList<>();
    
    @BeforeTest public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClient.class);
        ReflectionTestUtils.setField(admNotificationService, "objectMapper", mockObjectMapper);
        ReflectionTestUtils.setField(admNotificationService, "logger", hmtpLoggerImpl);
        Mockito.when(mockObjectMapper.writeValueAsString(Mockito.any(MessageDto.class))).thenReturn("{messageGroup:INCORRECT_RUN_ID_HTP}");
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        Mockito.when(RestClient.post(url , messagedto, "token", list)).thenReturn(response);
    }
    
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new PowerMockObjectFactory();
    }
    
    @Test public void sendNotificationTest() throws Exception {
        AdmNotificationService.sendNotification(NotificationGroupType.INCORRECT_RUN_ID_HTP, contents, null, url);
    }
    @Test
    public void sendNotificationSeverityTest()throws Exception {
        AdmNotificationService.sendNotification(NotificationGroupType.INCORRECT_RUN_ID_HTP, contents, "1", "token", url);
    }
    
    public MessageDto getMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setContents(contents);
        messageDto.setLocale("en_US");
        messageDto.setSeverity("Warning");
        messageDto.setMessageGroup("INCORRECT_RUN_ID_HTP");
        return messageDto;
    }
}
