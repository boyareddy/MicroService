package com.roche.connect.rmm.testNG;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
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
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.dmm.DeviceDTO;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderElements;
import com.roche.connect.common.order.dto.OrderStatus;
import com.roche.connect.rmm.services.AssayIntegrationService;
import com.roche.connect.rmm.services.DeviceIntegrationService;
import com.roche.connect.rmm.services.OrderIntegrationService;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
@PrepareForTest({ RestClientUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class AllIntegrationServiceTest {

	@InjectMocks
	AssayIntegrationService assayIntegrationService;

	
	@InjectMocks
	OrderIntegrationService orderIntegrationService;
	
	@InjectMocks
	DeviceIntegrationService deviceIntegrationService;
	
	
	@Mock
	Invocation.Builder assayClient;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	Response response;
	
	@Mock
	ObjectMapper objectMapper;

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	public void config() {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
	}

	@Test(priority=1)
	public void getProcessStepActionTest() throws HMTPException, UnsupportedEncodingException {
		config();
		ReflectionTestUtils.setField(assayIntegrationService, "ammHostURL", "http://localhost");
		List<ProcessStepActionDTO> processStepActionDTOList=new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO=new ProcessStepActionDTO();
		processStepActionDTO.setAssayType("NIPTDPCR");
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {
		})).thenReturn(processStepActionDTOList);
		 assayIntegrationService.getProcessStepAction("NIPTHTP","MP24", "token");

	}
	
	@Test(priority = 2)
	public void orderIntegrationTest() throws HMTPException, IOException {
		config();
		ReflectionTestUtils.setField(orderIntegrationService, "ommHostUrl", "http://localhost");

		

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get()).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_NO_CONTENT);
		Mockito.when(objectMapper.writeValueAsString(Collections.emptyList())).thenReturn("sample");
		OrderElements orderElementsResp = orderIntegrationService.searchOrderByAccessioningId("ORD",
				OrderStatus.ORDER_STATUS_UNASSIGNED, 0, 100);

		Assert.assertEquals(orderElementsResp, null);

	}
	
	@Test(priority = 2)
	public void orderIntegrationTest2() throws HMTPException, IOException {
		config();
		ReflectionTestUtils.setField(orderIntegrationService, "ommHostUrl", "http://localhost");
		List<OrderDTO> orderList = new ArrayList<>();
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setAccessioningId("ORD");
		orderList.add(orderDTO);

		String searchUnassignedOrderPath = "src/test/java/resource/SearchUnassignedOrders.json";
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(searchUnassignedOrderPath);
		OrderElements orderElements = objectMapper.readValue(jsonContent, OrderElements.class);

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get()).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		Mockito.when(response.readEntity(OrderElements.class)).thenReturn(orderElements);
		Mockito.when(objectMapper.writeValueAsString(orderList)).thenReturn("sample");
		OrderElements orderElementsResp = orderIntegrationService.searchOrderByAccessioningId("ORD",
				OrderStatus.ORDER_STATUS_UNASSIGNED, 0, 100);

		Assert.assertEquals(orderElementsResp, orderElements);

	}
	
	@Test(priority = 2, expectedExceptions = IOException.class)
	public void orderIntegrationTest3() throws HMTPException, IOException {
		config();
		ReflectionTestUtils.setField(orderIntegrationService, "ommHostUrl", "http://localhost");
		List<OrderDTO> orderList = new ArrayList<>();
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setAccessioningId("ORD");
		orderList.add(orderDTO);

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get()).thenThrow(IOException.class);
		orderIntegrationService.searchOrderByAccessioningId("ORD", OrderStatus.ORDER_STATUS_UNASSIGNED, 0, 100);

	}
	
	@Test(priority=3)
	public void deviceIntegrationTest() throws HMTPException, IOException {
		config();
		ReflectionTestUtils.setField(deviceIntegrationService, "deviceHostUrl", "http://localhost");
		List<DeviceDTO> deviceDTOList=new ArrayList<>();
		DeviceDTO deviceDTO=new DeviceDTO();
		deviceDTO.setDeviceId("ABC");
		deviceDTOList.add(deviceDTO);
		
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<DeviceDTO>>() {
		})).thenReturn(deviceDTOList);
		Mockito.when(objectMapper.writeValueAsString(deviceDTOList)).thenReturn("sample");
		deviceIntegrationService.getDevice("ABC");
		

	}

}
