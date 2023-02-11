package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class UnassignedOrderListServiceTest  {
	@Mock
	OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);

	@Mock
	OrderServiceImpl orderServiceImpl = org.mockito.Mockito.mock(OrderServiceImpl.class);

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();
	
	@Mock HMTPLoggerImpl hmtpLoggerImpl;

	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;
	@SuppressWarnings("rawtypes")
	List<Map> ordersMap = new ArrayList<>();
	Order order = null;
	OrderDTO orderDTO = null;
	PatientDTO patientDTO = null;
	AssayDTO assayDTO = null;
	OrderParentDTO orderParentDTO = null;
	Map<Integer, OrderDTO> mapOrderDTO = new HashMap<>();
	Long orderId = 0L;
	Long domainId=0L;
	List<OrderDTO> orderDTOList = new ArrayList<OrderDTO>();

	@BeforeTest
	public void setUp() {

		expectedCorrectStatusCode = 200;
		expectedIncorrectStatusCode = 400;
		orderId = 10000020L;
		domainId=1L;

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

		orderDTOList.add(orderDTO);
		mapOrderDTO.put(1, orderDTO);

		ordersMap.add(mapOrderDTO);

		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		Mockito.when(orderReadRepository.findOrders(any(long.class))).thenReturn(ordersMap);
		Mockito.when(orderServiceImpl.convertMapToOrders(ordersMap)).thenReturn(orderDTOList);
	}

	@Test
	public void getOrderListDisplayPosTest() throws HMTPException {
		Response response = orderCrudRestApiImpl.getOrderList();
		assertEquals(response.getStatus(), expectedCorrectStatusCode);
	}
	
	/*@Test(expectedExceptions = { NullPointerException.class, HMTPException.class, })
	public void validationUtilNegTest() throws Exception {
		orderCrudRestApiImpl.getOrderList();
	}
*/
	@AfterTest
	public void afterTest() {
		expectedCorrectStatusCode = 0;
		expectedIncorrectStatusCode = 0;
	}

}
