package com.roche.connect.imm.testng;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.imm.rest.MessageRestAPIImpl;
import com.roche.connect.imm.service.ForteDetail;
import com.roche.connect.imm.service.MP24AsyncMessageService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;
import com.roche.connect.imm.utils.ObjectMapperFactory;
import com.roche.connect.imm.utils.RestClient;


@PrepareForTest({ RestClientUtil.class, RestClient.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class MessageRestAPIImplForteTest {
	
	
	public static String mp96QueryMessage = "src/test/java/resource/MP96dPCRQBPPositive.json";

	private String deviceId = "D12345";

	@InjectMocks
	private MessageRestAPIImpl messageRestAPIImpl = new MessageRestAPIImpl();
	
	
	@Mock
	private Invocation.Builder builder;

	@Mock
	private HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	private Response response;
	
	@Mock
	private ObjectMapperFactory objectMapperFactory;
	
	@Mock
	private ObjectMapper objectMappers;
	
	@Mock
	private ForteDetail forteDetail;
	
	@Mock
	MP24AsyncMessageService mp24AsyncMessageService;


	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@Test(priority=1)
	public void consumeGenericRequestForteGetJobMessage() throws HMTPException, IOException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(RestClient.class);
		
		String mp96QueryMessageStrs = JsonFileReaderAsString.getJsonfromFile(mp96QueryMessage);
		Object object=null;
		Mockito.when(objectMapperFactory.readObjectValue(mp96QueryMessageStrs,
				ObjectMapperFactory.DEVICE_STR, DeviceType.MP96, MessageType.FORTE_GET_JOB)).thenReturn(object);
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("messageType",  MessageType.FORTE_GET_JOB);
		
		
		 Mockito.when(objectMappers.readValue(Mockito.anyString(),Mockito.<TypeReference<Map<String, Object>>>any())).thenReturn(map);
		Mockito.when(forteDetail.getForteGetJob(map)).thenReturn(response);
		
		try {
		messageRestAPIImpl.consumeGenericRequest(mp96QueryMessageStrs,
				ObjectMapperFactory.DEVICE_STR, DeviceType.MP96, MessageType.FORTE_GET_JOB, deviceId);
		}catch(Exception e){
			
		}
		
	}
	
	
	@Test(priority=2)
	public void consumeGenericRequestForteGetJobPutMessage() throws HMTPException, IOException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(RestClient.class);
		
		String token="";
		String mp96QueryMessageStrs = JsonFileReaderAsString.getJsonfromFile(mp96QueryMessage);
		Object object=null;
		Mockito.when(objectMapperFactory.readObjectValue(mp96QueryMessageStrs,
				ObjectMapperFactory.DEVICE_STR, DeviceType.MP96, MessageType.FORTE_PUT_JOB)).thenReturn(object);
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("messageType",  MessageType.FORTE_PUT_JOB);
		
		
		 Mockito.when(objectMappers.readValue(Mockito.anyString(),Mockito.<TypeReference<Map<String, Object>>>any())).thenReturn(map);
		Mockito.when(forteDetail.getFortePutJob(deviceId, DeviceType.MP96,map,token)).thenReturn(response);
		
		try {
		messageRestAPIImpl.consumeGenericRequest(mp96QueryMessageStrs,
				ObjectMapperFactory.DEVICE_STR, DeviceType.MP96, MessageType.FORTE_PUT_JOB, deviceId);
		}catch(Exception e){
			
		}
		
	}
	
	@Test(priority=3)
	public void consumeGenericRequestupdateRunResultByDeviceIdMessage() throws HMTPException, IOException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(RestClient.class);
		
		String mp96QueryMessageStrs = JsonFileReaderAsString.getJsonfromFile(mp96QueryMessage);
		Object object=null;
		Mockito.when(objectMapperFactory.readObjectValue(mp96QueryMessageStrs,
				ObjectMapperFactory.DEVICE_STR, DeviceType.MP96, MessageType.UPDATE_RUN)).thenReturn(object);
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("messageType",  MessageType.UPDATE_RUN);
		
		
		Mockito.doNothing().when(mp24AsyncMessageService).updateRunResultByDeviceId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString());
		 Mockito.when(objectMappers.readValue(Mockito.anyString(),Mockito.<TypeReference<Map<String, Object>>>any())).thenReturn(map);
		
		try {
		messageRestAPIImpl.consumeGenericRequest(mp96QueryMessageStrs,
				ObjectMapperFactory.DEVICE_STR,DeviceType.MP24, MessageType.UPDATE_RUN, deviceId);
		}catch(Exception e){
			
		}
		
	}
	
	
	@Test(priority=4)
	public void consumeGenericRequestupdateRunResultByDeviceIdMessageException() throws HMTPException, IOException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(RestClient.class);
		String mp96QueryMessageStrs = JsonFileReaderAsString.getJsonfromFile(mp96QueryMessage);
		Mockito.when(objectMapperFactory.readObjectValue(mp96QueryMessageStrs,
				ObjectMapperFactory.DEVICE_STR, DeviceType.MP96, MessageType.UPDATE_RUN)).thenThrow(Exception.class);
		try {
		messageRestAPIImpl.consumeGenericRequest(mp96QueryMessageStrs,
				ObjectMapperFactory.DEVICE_STR,DeviceType.MP24, MessageType.UPDATE_RUN, deviceId);
		}catch(Exception e){
			
		}
		
	}
}
