package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.readrepository.PatientSampleReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.CSVParserUtil;
import com.roche.connect.omm.util.JsonFileReaderAsString;
import com.roche.connect.omm.util.ValidationUtil;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
@SuppressWarnings("deprecation")
public class OrderCatchBlockTest extends AbstractTestNGSpringContextTests {

	@Mock
	OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);

	@Mock
	ContainerSamplesReadRepository containerSamplesReadRepository = org.mockito.Mockito
			.mock(ContainerSamplesReadRepository.class);;

	@Mock
	ValidationUtil validateUtil = org.mockito.Mockito.mock(ValidationUtil.class);

	@Mock
	PatientSampleReadRepository patientSampleReadRepository = org.mockito.Mockito
			.mock(PatientSampleReadRepository.class);

	@Mock
	CSVParserUtil csvParserUtil = org.mockito.Mockito.mock(CSVParserUtil.class);

	@Spy
	OrderService orderServiceImpl = org.mockito.Mockito.spy(new OrderServiceImpl());

	@Mock
	OrderService orderService;

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	OrderParentDTO orderParentDTO = null;
	Long orderId = 0L;
	long domainId = 0L;
	String accessioningID = null;
	String orderStatus = null;
	String jsonFileString = null;
	String deviceRunId = null;
	String containerID = null;

	Map<Object, Object> ordersMap = null;
	List<Map> ordersMapList = null;
	Order order = null;
	List<Order> ordList = null;
	PatientSamples patientSamples = null;
	List<PatientSamples> patientsamplesList = null;
	List<ContainerSamplesDTO> containerSamplesDTO = null;
	InputStream in = null;
	List<Map<String, Object>> loadIds = null;

	@BeforeTest
	public void setUp() throws Exception {

		orderId = 10000004L;
		domainId = 1L;
		accessioningID = "999999";
		orderStatus = "In workflow";
		orderParentDTO = null;
		deviceRunId = "mp96";
		containerID = "12345";
		jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudUpdateDetails.json");

		ordersMap = new HashMap<>();
		ordersMap.put(1, "");
		ordersMapList = new ArrayList<>();
		ordersMapList.add(ordersMap);
		ordersMapList.removeAll(ordersMapList);

		ordList = new ArrayList<>();
		ordList.add(order);
		ordList.removeAll(ordList);

		patientsamplesList = new ArrayList<>();
		patientsamplesList.add(patientSamples);
		patientsamplesList.removeAll(patientsamplesList);

		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(orderReadRepository.findOrders(domainId)).thenReturn(ordersMapList);
		Mockito.when(orderReadRepository.findOne(orderId)).thenReturn(order);
		Mockito.when(orderReadRepository.findWorkflowOrders(domainId)).thenReturn(ordersMapList);
		Mockito.when(orderReadRepository.findAllByAccessioningId(accessioningID, domainId)).thenReturn(ordList);
		Mockito.when(patientSampleReadRepository.findAllBySampleId(accessioningID, domainId))
				.thenReturn(patientsamplesList);
		Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(false);

	}

	// createOrder Negative Test
	/*@Test
	public void createOrderValidDateNegTest() throws Exception {
		Mockito.when(orderService.isAccessioningDuplicate(Mockito.any())).thenReturn(false);
		Mockito.when(validateUtil.isValidDate(Mockito.any(), Mockito.any())).thenReturn(false);
		Response response = orderCrudRestApiImpl.createOrder(jsonFileString);
		assertEquals(response.getStatus(), 400);
	}*/
	
	@Test
	public void createOrderAccessioningNegTest() throws Exception {
		Mockito.when(orderService.isAccessioningDuplicate(Mockito.any())).thenReturn(true);
		Response response = orderCrudRestApiImpl.createOrder(jsonFileString);
		assertEquals(response.getStatus(), 400);
	}
	@Test
	public void createOrderHMTPExpNegTest() throws Exception {
		Mockito.when(orderService.isAccessioningDuplicate(Mockito.any()))
				.thenThrow(new NullPointerException("Error occurred at line 10"));
		Response response = orderCrudRestApiImpl.createOrder(jsonFileString);
		assertEquals(response.getStatus(), 400);
	}

	// getOrderByOrderId
	@Test
	public void getOrderByOrderIdNegTest() throws Exception {
		Response response = orderCrudRestApiImpl.getOrderByOrderId(orderId);
		assertEquals(response.getStatus(), 404);
	}

	// getOrderBySampleId
	@Test
	public void getOrderBySampleIdNegTest() throws Exception {
		Response response = orderCrudRestApiImpl.getOrderBySampleId(accessioningID);
		assertEquals(response.getStatus(), 404);
	}

	// updateOrderStatus
	@Test
	public void getUpdOrdStatusNegTest() throws Exception {
		Response response = orderCrudRestApiImpl.updateOrderStatus(orderId, orderStatus);
		assertEquals(response.getStatus(), 404);
	}

	@Test
	public void getOrdStatusByNullIdNegTest() throws HMTPException {
		Response response = orderCrudRestApiImpl.updateOrderStatus(null, orderStatus);
		assertEquals(response.getStatus(), 400);
	}

	@Test
	public void getOrdStatusByNullStatusNegTest() throws HMTPException {
		Response response = orderCrudRestApiImpl.updateOrderStatus(orderId, null);
		assertEquals(response.getStatus(), 400);
	}

	@Test
	public void getOrdStatusByEmptyStatusNegTest() throws HMTPException {
		Response response = orderCrudRestApiImpl.updateOrderStatus(orderId, "");
		assertEquals(response.getStatus(), 400);
	}

	// getOrderList
	@Test
	public void getOrderListDisplayNegTest() throws Exception {
		Response response = orderCrudRestApiImpl.getOrderList();
		assertEquals(response.getStatus(), 404);
	}

	// updateOrder
	@Test(expectedExceptions = Exception.class)
	public void updateOrderNegTest() throws Exception {
		orderCrudRestApiImpl.update(null);
	}

	// OrderWorkflowList
	/*
	 * @Test public void getInworkflowOrderListNegTest() throws Exception {
	 * Response response =orderCrudRestApiImpl.getOrderWorkflowList();
	 * assertEquals(response.getStatus(), 404); }
	 */

	// OrderWorkflowList
	@Test(expectedExceptions = { NullPointerException.class, HMTPException.class })
	public void createTestOptionsObjNegTest() throws HMTPException {
		orderServiceImpl.createTestOptionsObject(orderParentDTO);
	}

	// ValidationUtill
	@Test(expectedExceptions = { NullPointerException.class, HMTPException.class })
	public void validationUtilNegTest() throws HMTPException {
		orderServiceImpl.assayValidations(orderParentDTO);
	}

	// ListOfContainerIdWithOpenStatus Negative Test

	@Test(expectedExceptions = Exception.class)
	public void getListOfContainerIdWithOpenStatusNegativeTest() throws HMTPException {
		/*
		 * Mockito.doThrow(Exception.class).when(containerSamplesReadRepository)
		 * .findAllContainerIdWithOpenStatus(any(String[].class), anyLong());
		 */
		Mockito.when(
				containerSamplesReadRepository.findAllContainerIdWithOpenStatus(any(String[].class), any(Long.class)))
				.thenThrow(Exception.class);
		orderCrudRestApiImpl.getListOfContainerIdWithOpenStatus("open.sendToDevice");
	}

	// UpdateOrdersStatus Negative Test
	@Test(expectedExceptions = Exception.class)
	public void updateorderstatusExceptionTest() throws HMTPException {
		Mockito.when(orderReadRepository.findOrderByOrderId(any(Long.class), any(Long.class)))
				.thenThrow(Exception.class);
		orderCrudRestApiImpl.updateOrderStatus(orderId, "status.orderStatus");
	}

	// Update
	@Test(expectedExceptions = { NullPointerException.class, Exception.class })
	public void UpdateExceptionTest() throws HMTPException {
		Mockito.doThrow(Exception.class).when(orderCrudRestApiImpl.update(jsonFileString));

	}

	// OrderByOrderId
	@Test(expectedExceptions = { Exception.class })
	public void getOrderByOrderIdExceptionTest() throws HMTPException {
		Mockito.doThrow(Exception.class).when(orderCrudRestApiImpl.getOrderByOrderId(orderId));

	}

	// OrderList
	@Test(expectedExceptions = { Exception.class })
	public void getOrderListExceptionTest() throws HMTPException {
		Mockito.doThrow(Exception.class).when(orderCrudRestApiImpl.getOrderList());

	}

	// ContainerSamples
	@Test(expectedExceptions = { Exception.class })
	public void getContainerSamplesExceptionTest() throws HMTPException {
		Mockito.doThrow(Exception.class).when(orderCrudRestApiImpl.getContainerSamples(deviceRunId, containerID));
	}

	// updateContainerSamples
	@Test(expectedExceptions = { Exception.class })
	public void updateContainerSamples() throws HMTPException {
		Mockito.doThrow(Exception.class).when(
				orderCrudRestApiImpl.updateContainerSamples(jsonFileString, deviceRunId, accessioningID, orderStatus));
	}
	//UnassignedOrderCount
	@Test(expectedExceptions = { Exception.class })
    public void getUnassignedOrderCountExceptionTest() throws HMTPException {
        Mockito.when(orderReadRepository.findUnassignedOrdersCount(any(Long.class)))
        .thenThrow(Exception.class);
        orderCrudRestApiImpl.getUnassignedOrderCount();

    }

	// StoreContainerSamples Negative Test
	@Test(expectedExceptions = { NullPointerException.class, Exception.class })
	public void getStoreContainerSamplesNegTest() throws HMTPException {
		Mockito.when(containerSamplesReadRepository.findHighestLoadIdByContainer(any(Long.class))).thenReturn(loadIds);
		Mockito.when(orderServiceImpl.isCSVJsonValid(containerSamplesDTO)).thenReturn(false);
		orderCrudRestApiImpl.storeContainerSamples(jsonFileString);
	}

	@Test(expectedExceptions = Exception.class)
	public void getStoreContainerSamplesNegTest1() throws HMTPException {
		Mockito.when(containerSamplesReadRepository.findHighestLoadIdByContainer(any(Long.class)))
				.thenThrow(Exception.class);
		orderCrudRestApiImpl.storeContainerSamples(jsonFileString);
	}

	// validateContainerSamples Negative Test

	@Test(expectedExceptions = { NullPointerException.class, Exception.class })
	public void validateContainerSamplesNegTest() throws HMTPException, IOException {
		Mockito.when(csvParserUtil.parseCSV(in)).thenThrow(Exception.class);
		orderCrudRestApiImpl.validateContainerSamples(in);
	}
	
	@Test(expectedExceptions = { Exception.class })
    public void updateContainerSamplesFlag() throws HMTPException {
	     Mockito.when(containerSamplesReadRepository.findByContainerID(Mockito.anyString(),Mockito.anyLong()))
        .thenThrow(Exception.class);
        orderCrudRestApiImpl.updateContainerSamplesFlag(containerID);
    }
	
	@Test(expectedExceptions = { Exception.class })
    public void containerInfoForAccessioningId() throws HMTPException {
         Mockito.when(containerSamplesReadRepository.findSampleByAccessioningId(Mockito.anyString(),Mockito.anyLong()))
        .thenThrow(Exception.class);
        orderCrudRestApiImpl.containerInfoForAccessioningId(accessioningID);
    }
	@Test
	public void updateOrderDataIntegrityViolationExceptionTest() throws Exception {
	    Mockito.when(validateUtil.validateOrderParentDetails(Mockito.any()))
	                .thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));
	    Response response = orderCrudRestApiImpl.update(jsonFileString);
	    assertEquals(response.getStatus(), 400);
	}
	@Test
	public void updateOrderJsonMappingExceptionTest() throws HMTPException, JsonProcessingException{
	    String jsonObj = new String("{\"order\":{\"retestSample\":\"123\",\"orderComments\":\"first comment\"}}");
	    Response response = orderCrudRestApiImpl.update(jsonObj);
	    assertEquals(response.getStatus(), 400);
	}
	
	@Test(expectedExceptions = { Exception.class })
    public void getMandatoryFlagByAccessioningIdExpTest() throws HMTPException {
	    boolean response = orderCrudRestApiImpl.getMandatoryFlagByAccessioningId("NIPTDPCR",null);
    }

}
