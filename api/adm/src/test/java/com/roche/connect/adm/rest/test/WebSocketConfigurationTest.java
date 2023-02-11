package com.roche.connect.adm.rest.test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.common.adm.notification.RestClient;
import com.roche.connect.adm.config.SocketHandler;
import com.roche.connect.adm.config.WebSocketConfiguration;

@PrepareForTest({ RestClient.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*",
"javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*", "org.apache.logging.*",
"org.slf4j.*" })
public class WebSocketConfigurationTest {
	@InjectMocks
	WebSocketConfiguration webSocketConfiguration;
	@Mock
	WebSocketHandlerRegistry registry;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	WebSocketHandlerRegistration webSocketHandlerRegistration;
	

	@BeforeTest
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new PowerMockObjectFactory();
	}

	@Test
	public void registerWebSocketHandlersTest() {
		ReflectionTestUtils.setField(webSocketConfiguration, "logger", hmtpLoggerImpl);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(registry.addHandler(Mockito.any(SocketHandler.class), Mockito.anyString())).thenReturn(webSocketHandlerRegistration);
		Mockito.when(webSocketHandlerRegistration.setAllowedOrigins("*")).thenReturn(webSocketHandlerRegistration);
		Mockito.when(webSocketHandlerRegistration.setHandshakeHandler(Mockito.any(DefaultHandshakeHandler.class))).thenReturn(webSocketHandlerRegistration);
		webSocketConfiguration.registerWebSocketHandlers(registry);
	}
}
