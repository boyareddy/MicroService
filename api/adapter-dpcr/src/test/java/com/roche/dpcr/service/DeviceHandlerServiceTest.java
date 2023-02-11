package com.roche.dpcr.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.json.JSONObject;
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

import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.dpcr.dto.MessageExchange;
import com.roche.dpcr.util.TokenUtils;

import ca.uhn.hl7v2.model.v24.datatype.ID;
import ca.uhn.hl7v2.model.v24.datatype.IS;
import ca.uhn.hl7v2.model.v26.datatype.HD;
import ca.uhn.hl7v2.model.v26.datatype.ST;
import ca.uhn.hl7v2.model.v26.datatype.VID;
import ca.uhn.hl7v2.model.v26.segment.MSH;

@PrepareForTest({ RestClientUtil.class, InetAddress.class,TokenUtils.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class DeviceHandlerServiceTest {
	@InjectMocks
	DeviceHandlerService deviceHandlerService;

	@Mock
	Socket socket;
	
	@Mock
	MessageExchange messageExchange;
	
	Map<String, MessageExchange> deviceMap = new HashMap<>();

	@Mock
	Invocation.Builder builder;
	@Mock
	Invocation.Builder builder2;
	@Mock
	Invocation.Builder builder3;
	@Mock
	Response resp;
	@Mock
	Exchange exchange;
	@Mock
	MSH msh;
	@Mock
	Message message;
	@Mock
	ST st;
	@Mock
	VID vid;
	@Mock
	ID id;
	@Mock
	HD hd;
	@Mock
	IS is;
	@Mock
	InetAddress addr;
	@Mock
	Object object;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	SocketAddress socketAddress;

	String loginEntity = "http://localhost";
	String url = "http://localhost/json/device/fetch/expr?filterExpression=%28%28serialNo%3D%27DPCR001%27%29+%26+%28state%3D%27ACTIVE%27%29+%7C+%28state%3D%27NEW%27%29%29";
	
	@BeforeTest
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		PowerMockito.mockStatic(InetAddress.class);
		deviceMap.put("DPCR001", messageExchange);
		DeviceHandlerService.setDeviceMap(deviceMap);
		deviceHandlerService.setDeviceIP("127.0.0.1");
		deviceHandlerService.setProtocolVersion("127.0.0.1");

		ReflectionTestUtils.setField(deviceHandlerService, "deviceFlag", true);
		ReflectionTestUtils.setField(deviceHandlerService, "loginUrl", "http://localhost");
		ReflectionTestUtils.setField(deviceHandlerService, "loginEntity", "http://localhost");
		ReflectionTestUtils.setField(deviceHandlerService, "deviceEndPoint", "http://localhost");
		
		
	        Mockito.when(InetAddress.getByName(Mockito.anyString())).thenReturn(addr);
	        Mockito.when(addr.getHostName()).thenReturn("localhost");
	        Mockito.when(socket.getRemoteSocketAddress()).thenReturn(socketAddress);
	        Mockito.when(socket.getRemoteSocketAddress().toString()).thenReturn("http://10.150.120.24");
	        Mockito.doNothing().when(socket).close();
	}
  
	public void restClientPostiveMock() {
		PowerMockito.mockStatic(RestClientUtil.class);
	 Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(builder);
     javax.ws.rs.client.Entity<String> entity = javax.ws.rs.client.Entity.entity(loginEntity , MediaType.APPLICATION_FORM_URLENCODED);
     Mockito.when(builder.post(entity,String.class)).thenReturn("token");
     
     Mockito.when(RestClientUtil.getBuilder(url, null)). thenReturn(builder2);
     
     Mockito.when(builder2.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder2);
     Mockito.when(builder2.get(String.class)).thenReturn("[ { \"deviceType\": { \"deviceTypeId\": \"fa99cb08-76bf-40c2-8dc7-9c44116dee78\" }, \"attributes\": { \"protocolVersion\": \"\" }, \"deviceId\": \"9b650548-e591-4293-8735-26f76dc8d4a2\" } ]");
     Mockito.when(RestClientUtil.getBuilder("null/json/device/update/9b650548-e591-4293-8735-26f76dc8d4a2", null)).thenReturn(builder3);
     Mockito.when( builder3.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder3);
     JSONObject attributes = new JSONObject();
     Mockito.when(builder3.post(Entity.entity(attributes.toString(), MediaType.APPLICATION_JSON))).thenReturn(resp);
	}
	
	public void restClientNegativeMock() {
		PowerMockito.mockStatic(RestClientUtil.class);
		 Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(builder);
	     javax.ws.rs.client.Entity<String> entity = javax.ws.rs.client.Entity.entity(loginEntity , MediaType.APPLICATION_FORM_URLENCODED);
	     Mockito.when(builder.post(entity,String.class)).thenReturn("token");
	     
	     Mockito.when(RestClientUtil.getBuilder(url, null)). thenReturn(builder2);
	     
	     Mockito.when(builder2.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder2);
	     
	     Mockito.when(builder2.get(String.class)).thenReturn("[]");
	     Mockito.when(RestClientUtil.getBuilder("null/json/device/update/9b650548-e591-4293-8735-26f76dc8d4a2", null)).thenReturn(builder3);
	     Mockito.when( builder3.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder3);
	     JSONObject attributes = new JSONObject();
	     Mockito.when(builder3.post(Entity.entity(attributes.toString(), MediaType.APPLICATION_JSON))).thenReturn(resp);
		}
		
	
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@Test
	public void deviceValidateTest() throws IOException {
		restClientPostiveMock();
		PowerMockito.mockStatic(TokenUtils.class);
		Mockito.when(TokenUtils.getToken(true)).thenReturn("token");
		deviceHandlerService.deviceValidation(socket, getMessageExchange());
	}
	
	@Test
	public void deviceValidateNegativeTest() throws IOException {
		restClientNegativeMock();
		PowerMockito.mockStatic(TokenUtils.class);
		Mockito.when(TokenUtils.getToken(true)).thenReturn("token");
		deviceHandlerService.deviceValidation(socket, getMessageExchange());
	}
	
	

	public MessageExchange getMessageExchange() {
		MessageExchange messageExchange = new MessageExchange();
		messageExchange.setSerialNo("DPCR001");
		messageExchange.setSendingApp("DPCR Analyzer");
		messageExchange.setDeviceType("DPCR");
		messageExchange.setDeviceSubStatus("NEW");
		messageExchange.setMsgVersion("2.5");
		return messageExchange;
	}

}
