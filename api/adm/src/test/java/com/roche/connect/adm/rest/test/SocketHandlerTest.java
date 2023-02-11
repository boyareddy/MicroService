package com.roche.connect.adm.rest.test;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.config.SocketHandler;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, URI.class ,TextMessage.class}) @PowerMockIgnore({ "sun.misc.Launcher.*",
"com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*", "org.apache.logging.*",
"org.slf4j.*" }) public class SocketHandlerTest {
    
    @InjectMocks SocketHandler socketHandler;
    
    @Mock WebSocketSession session;
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    TextMessage message;
    
    @BeforeTest public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        Mockito.doNothing().when(session).close();
        Mockito.when(session.isOpen()).thenReturn(true);
         message = PowerMockito.mock(TextMessage.class);
        Mockito.when(session.getId()).thenReturn("1");
        Mockito.doNothing().when(session).sendMessage(Mockito.any(WebSocketMessage.class));
    }
    
    /**
     * We need a special {@link IObjectFactory}.
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test public void sendMessageTest() {
        Set<String> roleName = new HashSet<>();
        roleName.add("roleName");
        socketHandler.sendMessage("messagePayload", roleName);
    }
    
    @Test public void closeSessionTest() throws IOException {
        socketHandler.closeSession(session);
    }
    
    @Test public void afterConnectionEstablishedTest() throws Exception {
        URI uri = PowerMockito.mock(URI.class);
        Mockito.when(session.getUri()).thenReturn(uri);
        Mockito.when(session.getUri().getQuery()).thenReturn("token=token");
        /*
          String[] strArr = new String[] {"token=token"};
        Mockito.when(session.getUri().getQuery().split("=")).thenReturn(strArr);
         */
        try {
            socketHandler.afterConnectionEstablished(session);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Test public void isMaxLimitExceedTest() throws IOException {
        socketHandler.isMaxLimitExceed("127.0.0.1");
    }
    
  /*  @Test
    public void handleTextMessageTest() throws InterruptedException, IOException {
        socketHandler.handleTextMessage(session, message);
    }*/
}
