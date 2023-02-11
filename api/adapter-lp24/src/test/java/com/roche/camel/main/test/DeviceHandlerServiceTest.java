package com.roche.camel.main.test;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.mina.core.session.IoSession;
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
import com.roche.lpcamel.service.DeviceHandlerService;
import com.roche.lpcamel.service.ResponseHandlerService;

import ca.uhn.hl7v2.model.v251.datatype.HD;
import ca.uhn.hl7v2.model.v251.datatype.ID;
import ca.uhn.hl7v2.model.v251.datatype.IS;
import ca.uhn.hl7v2.model.v251.datatype.ST;
import ca.uhn.hl7v2.model.v251.datatype.VID;
import ca.uhn.hl7v2.model.v251.segment.MSH;

@PrepareForTest({RestClientUtil.class})
@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*","org.slf4j.*" })
public class DeviceHandlerServiceTest {
	@InjectMocks
	DeviceHandlerService deviceHandlerService;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	ResponseHandlerService responseHandlerService;

	@Mock IoSession session;
	Map<String, IoSession> deviceMap = new HashMap<>();

	@Mock Invocation.Builder builder;
	@Mock  Invocation.Builder builder2 ;
	@Mock  Invocation.Builder builder3 ;
	@Mock Response resp;
	@Mock Exchange exchange;
	@Mock MSH msh;
	@Mock Message message;
	@Mock HD hd;
	@Mock ST st;
	@Mock VID vid;
	@Mock ID id;
	@Mock IS is;

	String  loginEntity ="http://localhost";
	String url="null/json/device/fetch/expr?filterExpression=%28%28serialNo%3D%27MP001%27%29+%26+%28state%3D%27ACTIVE%27%29+%7C+%28state%3D%27NEW%27%29%29";
	String url1 = "deviceEndPoint/json/device/fetch/expr?filterExpression=%28%28serialNo%3D%27MP001%27%29+%26+%28state%3D%27ACTIVE%27%29+%7C+%28state%3D%27NEW%27%29%29";
	@BeforeTest
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this); 
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		PowerMockito.mockStatic(RestClientUtil.class);
		deviceMap.put("MP001", session);
		DeviceHandlerService.setDeviceMap(deviceMap);
		deviceHandlerService.setDeviceIP("127.0.0.1");
		deviceHandlerService.setProtocolVersion("2.5.1");
		deviceHandlerService.setLocation("location");
		deviceHandlerService.setDeviceSubStatus("offline");
		deviceHandlerService.setDeviceModel("MP");
		 System.out.println(deviceHandlerService.getProtocolVersion()+
		     deviceHandlerService.getDeviceModel()+
		     deviceHandlerService.getHostName()+deviceHandlerService.getLocation()+
		     deviceHandlerService.getDeviceSubStatus()+DeviceHandlerService.getDeviceMap());

		ReflectionTestUtils.setField(deviceHandlerService, "deviceFlag", true);
		ReflectionTestUtils.setField(deviceHandlerService, "loginUrl", "http://localhost");
		ReflectionTestUtils.setField(deviceHandlerService, "loginEntity", "http://localhost");
		ReflectionTestUtils.setField(deviceHandlerService, "deviceEndPoint", "deviceEndPoint");
		
		Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(builder);

		javax.ws.rs.client.Entity<String> entity = javax.ws.rs.client.Entity.entity(loginEntity , MediaType.APPLICATION_FORM_URLENCODED);
		Mockito.when(builder.post(entity,String.class)).thenReturn("token");

		String url2 ="deviceEndPoint/json/device/update/9b650548-e591-4293-8735-26f76dc8d4a2";
		Mockito.when(RestClientUtil.getBuilder(url2, null)).thenReturn(builder3);
		Mockito.when(RestClientUtil.getBuilder(url, null)). thenReturn(builder2);
		Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder2);
		Mockito.when(builder2.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder2);
		Mockito.when(builder2.get(String.class)).thenReturn("[ { \"deviceType\": { \"deviceTypeId\": \"fa99cb08-76bf-40c2-8dc7-9c44116dee78\" }, \"attributes\": { \"protocolVersion\": \"\",\"location\":\"location\" }, \"deviceId\": \"9b650548-e591-4293-8735-26f76dc8d4a2\" } ]");
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
	public void deviceValidateTest() {
		Mockito.when(exchange.getIn()).thenReturn(message);
		Mockito.when(message.getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class)).thenReturn(session);
		Mockito.when((message.getHeader(Mina2Constants.MINA_REMOTE_ADDRESS))).thenReturn("/127.0.0.1");
		Mockito.when(msh.getMsh3_SendingApplication()).thenReturn(hd);
		Mockito.when(hd.getHd2_UniversalID()).thenReturn(st);
		Mockito.when(st.getValue()).thenReturn("MP001");
		Mockito.when(msh.getMsh12_VersionID()).thenReturn(vid);
		Mockito.when(vid.getVersionID()).thenReturn(id);
		Mockito.when(id.getValue()).thenReturn("2.5.1");
		Mockito.when(msh.getMsh4_SendingFacility()).thenReturn(hd);
		Mockito.when(hd.getHd1_NamespaceID()).thenReturn(is);
		Mockito.when(is.getValue()).thenReturn("local");
		
		try {
			Boolean isRegisteredDevice=deviceHandlerService.deviceValidation(exchange, msh);
			assertEquals(isRegisteredDevice, isRegisteredDevice);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void checkConnectionStatusTest() {
	   
	    deviceHandlerService.checkConnectionStatus();
	}
	/* @Test
    public void sendNotificationTest() {
        try {
        	Mockito.doNothing().when(responseHandlerService).postRequest(Mockito.anyString(),Mockito.any());
            deviceHandlerService.sendNotification("12345", "invalidDevice");
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
    }
	 */
	 
	 
}
