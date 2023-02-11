package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderServiceImpl;

@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class OrderByOrderIdTest  {
	
	@Mock
	OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);

	@Mock
	OrderServiceImpl orderServiceImpl = org.mockito.Mockito.mock(OrderServiceImpl.class);

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();
	
	@Mock HMTPLoggerImpl hmtpLoggerImpl;
	
	Order order = null;
	OrderDTO orderDTO = null;
	PatientDTO patientDTO = null;
	AssayDTO assayDTO = null;
	OrderParentDTO orderParentDTO = null;
	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;
	Long orderId = 0L;

	@BeforeTest
	public void setUp() {
		expectedCorrectStatusCode = 200;
		expectedIncorrectStatusCode = 400;
		orderId = 10000020L;

		order = new Order();
		order.setId(orderId);
		order.setAccessioningId("9876543");
		order.setActiveFlag("Y");
		order.setAssayType("NIPT");
		order.setCreatedBy("ADMIN");
		order.setOrderComments("first comment");
		order.setOrderStatus("unassigned");
		order.setPatientSampleId(452L);
		Patient patient = new Patient();
		patient.setId(452L);
		order.setPatient(patient);

		patientDTO = new PatientDTO();
		patientDTO.setPatientFirstName("abc");
		patientDTO.setPatientLastName("xyz");

		assayDTO = new AssayDTO();
		assayDTO.setPatientAssayid(1L);
		assayDTO.setEggDonor("yes");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("harmony", true);
		assayDTO.setTestOptions(map);

		orderDTO = new OrderDTO();
		orderDTO.setAccessioningId("9876543");
		orderDTO.setActiveFlag("Y");
		orderDTO.setAssayType("NIPT");
		orderDTO.setCreatedBy("ADMIN");
		orderDTO.setAssay(assayDTO);
		orderDTO.setPatient(patientDTO);

		orderParentDTO = new OrderParentDTO();
		orderParentDTO.setOrder(orderDTO);
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		Mockito.when(orderReadRepository.findOrderByOrderId(any(long.class), any(long.class))).thenReturn(order);
		Mockito.when(orderServiceImpl.createOrderParentDTO(order)).thenReturn(orderParentDTO);

	}

	@Test
	public void getOrderByOrderIdPosTest() throws HMTPException {
		Response response = orderCrudRestApiImpl.getOrderByOrderId(orderId);
		assertEquals(response.getStatus(), expectedCorrectStatusCode);

	}
    
	//OrderBySampleId
	@Test(expectedExceptions = {Exception.class})
    public void getOrderBySampleIdExceptionTest() throws HMTPException  {
        Mockito.when( orderReadRepository.findAllByAccessioningId(any(String.class),any(Long.class)))
        .thenThrow(Exception.class);
         orderCrudRestApiImpl.getOrderBySampleId("12345");
    }
	
	@AfterTest
	public void afterTest() {
		expectedCorrectStatusCode = 0;
		expectedIncorrectStatusCode = 0;
	}
	
	

}
