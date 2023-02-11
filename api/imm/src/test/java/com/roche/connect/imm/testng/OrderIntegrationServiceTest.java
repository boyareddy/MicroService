package com.roche.connect.imm.testng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.imm.service.OrderIntegrationService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;
import com.roche.connect.imm.utils.RestClient;

@PrepareForTest({ RestClientUtil.class, RestClient.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class OrderIntegrationServiceTest {

	private static final String containerSamplesJson = "src/test/java/resource/ContainerSampleCSVUpdate.json";
	private static final String orderListJson = "src/test/java/resource/OrderList.json";
	private ObjectMapper objectMapper = new ObjectMapper();
	private List<ContainerSamplesDTO> containerSamples = null;
	private List<OrderDTO> orderList = null;

	@Mock
	private HMTPLoggerImpl hmtpLoggerImpl;

	@InjectMocks
	private OrderIntegrationService orderIntegrationService = new OrderIntegrationService();

	@Mock
	private Invocation.Builder builder;

	@Mock
	ObjectMapper objectMappers;
	
	@Mock
	private Response response;

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@BeforeMethod
	public void beforeMethod() throws IOException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString());
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		String jsonContent = JsonFileReaderAsString.getJsonfromFile(containerSamplesJson);
		TypeReference<List<ContainerSamplesDTO>> class1 = new TypeReference<List<ContainerSamplesDTO>>() {
		};
		containerSamples = objectMapper.readValue(jsonContent, class1);

		String jsonContent2 = JsonFileReaderAsString.getJsonfromFile(orderListJson);
		TypeReference<List<OrderDTO>> class2 = new TypeReference<List<OrderDTO>>() {
		};
		orderList = objectMapper.readValue(jsonContent2, class2);

		ReflectionTestUtils.setField(orderIntegrationService, "ommHostUrl", "http://localhost:96/omm");
	}

	@Test
	public void getDPCRContainerSamples() throws IOException, Exception {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<ContainerSamplesDTO>>() {
		})).thenReturn(containerSamples);

		Assert.assertEquals(containerSamples, orderIntegrationService.getDPCRContainerSamples());

	}

	@Test(expectedExceptions = Exception.class)
	public void getDPCRContainerSamples2() throws IOException, Exception {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenThrow(Exception.class);

		Assert.assertEquals(containerSamples, orderIntegrationService.getDPCRContainerSamples());
	}

	@Test
	public void getDPCRContainerSamplesWithDeviceRunId() throws IOException, Exception {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<ContainerSamplesDTO>>() {
		})).thenReturn(containerSamples);

		Assert.assertEquals(containerSamples, orderIntegrationService.getDPCRContainerSamples("RND12345"));
	}
	
	@Test(expectedExceptions = Exception.class)
	public void getDPCRContainerSamplesWithDeviceRunId2() throws IOException, Exception {

		Assert.assertEquals(containerSamples, orderIntegrationService.getDPCRContainerSamples(null));
	}

	@Test(expectedExceptions = Exception.class)
	public void getDPCRContainerSamplesWithDeviceRunId3() throws IOException, Exception {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenThrow(Exception.class);

		Assert.assertEquals(containerSamples, orderIntegrationService.getDPCRContainerSamples("RND12345"));
	}
	
	@Test
	public void updateContainerSamples() throws IOException, Exception {

		Mockito.when(
				RestClient.put(Mockito.anyString(), Mockito.eq(containerSamples), Mockito.eq(null), Mockito.eq(null)))
				.thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);
		
		Assert.assertEquals(true, orderIntegrationService.updateContainerSamples(containerSamples, null));

	}

	@Test
	public void updateContainerSamples2() throws IOException, Exception {

		Mockito.when(
				RestClient.put(Mockito.anyString(), Mockito.eq(containerSamples), Mockito.eq(null), Mockito.eq(null)))
				.thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		
		Assert.assertEquals(false, orderIntegrationService.updateContainerSamples(containerSamples, null));

	}
	
	@Test(expectedExceptions = Exception.class)
	public void updateContainerSamples3() throws IOException, Exception {

		Mockito.when(
				RestClient.put(Mockito.anyString(), Mockito.eq(containerSamples), Mockito.eq(null), Mockito.eq(null)))
				.thenThrow(Exception.class);
		
		Assert.assertEquals(false, orderIntegrationService.updateContainerSamples(containerSamples, null));

	}
	
	@Test
	public void getPatientAssayDetailsByAccessioningId() throws IOException, Exception {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<OrderDTO>>() {
		})).thenReturn(orderList);

		Assert.assertEquals(orderList, orderIntegrationService.getPatientAssayDetailsByAccesssioningId("12876635"));

	}

	@Test(expectedExceptions = HMTPException.class)
	public void getPatientAssayDetailsByAccessioningId2() throws IOException, Exception {

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<OrderDTO>>() {
		})).thenThrow(Exception.class);

		Assert.assertEquals(orderList, orderIntegrationService.getPatientAssayDetailsByAccesssioningId("12876635"));

	}
	
	@Test
	public void getOrderByOrderId() throws IOException, Exception {

		OrderParentDTO orderParentDTO = new OrderParentDTO();
		OrderDTO order = null;
		if (!orderList.isEmpty())
			order = orderList.get(0);

		orderParentDTO.setOrder(order);

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.readEntity(OrderParentDTO.class)).thenReturn(orderParentDTO);

		Assert.assertEquals(order, orderIntegrationService.getOrderByOrderId(343556L));

	}
	
	@Test(expectedExceptions = HMTPException.class)
	public void getOrderByOrderId2() throws IOException, Exception {

		OrderParentDTO orderParentDTO = new OrderParentDTO();
		OrderDTO order = null;
		if (!orderList.isEmpty())
			order = orderList.get(0);

		orderParentDTO.setOrder(order);

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);

		Mockito.when(response.readEntity(OrderParentDTO.class)).thenThrow(Exception.class);

		Assert.assertEquals(order, orderIntegrationService.getOrderByOrderId(343556L));

	}
	
	
	@Test
	public void getOrderList() throws IOException, Exception {

		String token="1";
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		List<OrderDTO> OrderDTOlist=new ArrayList<>();
		OrderDTO orderDTO=new OrderDTO();
		orderDTO.setAccessioningId("1234");
		OrderDTOlist.add(orderDTO);
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<OrderDTO>>() {
		})).thenReturn(OrderDTOlist);
		Mockito.when(objectMappers.writeValueAsString(OrderDTOlist)).thenReturn("sample");
		Mockito.when(builder.get()).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(200);
		orderIntegrationService.getOrder("1234",token);

	}
	
	
	@Test(expectedExceptions = Exception.class)
	public void getOrderListException() throws IOException, Exception {

		String token="1";
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		List<OrderDTO> OrderDTOlist=new ArrayList<>();
		OrderDTO orderDTO=new OrderDTO();
		orderDTO.setAccessioningId("1234");
		OrderDTOlist.add(orderDTO);
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get(new GenericType<List<OrderDTO>>() {
		})).thenThrow(Exception.class);
		
		orderIntegrationService.getOrder("1234",token);

	}
	
	@Test
	public void isRunIDValid() throws  Exception {
		
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(200);


		orderIntegrationService.isRunIDValid("1234");

	}

	@Test(expectedExceptions = Exception.class)
	public void isRunIDValidExceptioncase() throws  Exception {
		
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.get()).thenThrow(Exception.class);
		


		orderIntegrationService.isRunIDValid("1234");

	}
	
}
