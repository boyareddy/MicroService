package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.amm.dto.TestOptionsDTO;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.readrepository.PatientSampleReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;

/**
 * 
 * @author imtiyazm , This test class is used to mock getAllOrder() 
 * 
 */
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
	"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class GetAllOrderTest {

 
	@Mock
	OrderReadRepository orderReadRepository = org.mockito.Mockito
			.mock(OrderReadRepository.class);
	@Mock
	PatientSampleReadRepository patientSampleReadRepository = org.mockito.Mockito
			.mock(PatientSampleReadRepository.class);
	
	@Mock
	OrderService orderService = org.mockito.Mockito
			.mock(OrderService.class);
	
	  
	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	
	Order order;
	List<Order> ordList;
	
	PatientSamples patientSamples;
	List<PatientSamples> patientSampleList;
	OrderDTO orderDTO;
	AssayDTO assayDTO;
	TestOptionsDTO testOptionsDTO;
	PatientDTO patientDTO;
	List<OrderDTO> orderDTOList;
	long domainId = 0;
	Map<String, Boolean> testOptionsMap;

	@BeforeTest
	public void setUp() {
		domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		testOptionsMap = new HashMap<String, Boolean>();
		testOptionsMap.put("we", true);
		testOptionsMap.put("as", true);
		testOptionsMap.put("sd", true);
		order = new Order();
		order.setId(123L);
		order.setAccessioningId("121");
		order.setCreatedABy("");
		ordList = new ArrayList<Order>();
		ordList.add(order);
		assayDTO = new AssayDTO();
		assayDTO.setPatientAssayid(88L);
		assayDTO.setIvfStatus("");
		testOptionsDTO = new TestOptionsDTO();
		testOptionsDTO.setAssayType("");
		testOptionsDTO.setTestName("");
		testOptionsDTO.setTestDescription("");
		patientDTO = new PatientDTO();
		patientDTO.setPatientFirstName("");
		patientDTO.setPatientLastName("");
		orderDTO = new OrderDTO();
		orderDTO.setOrderId(567L);
		orderDTO.setAccessioningId("334");
		orderDTO.setCreatedBy("");
		orderDTO.setPatient(patientDTO);
		assayDTO.setTestOptions(testOptionsMap);
		orderDTO.setAssay(assayDTO);
		orderDTOList = new ArrayList<>();
		orderDTOList.add(orderDTO);
		patientSamples = new PatientSamples();
		patientSamples.setId(23L);
		patientSamples.setSampleId("347");
		patientSampleList = new ArrayList<>();
		patientSampleList.add(patientSamples);
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
	}

	@Test
	public void getAllOrderTestPosTest() throws HMTPException {
		Mockito.when(orderReadRepository.findAllOrders(any(long.class))).thenReturn(ordList);
		Mockito.when(patientSampleReadRepository.findAllBySampleId(any(String.class), any(long.class))).thenReturn(patientSampleList);
		Mockito.when(orderService.createOrderDTO(order)).thenReturn(orderDTO);
		Mockito.when(orderService.createAssayDTO(order)).thenReturn(assayDTO);
		Mockito.when(orderService.createTestOptionsDTO(order)).thenReturn(testOptionsMap);
		Mockito.when(orderService.createPatientDTO(order)).thenReturn(patientDTO);
		Response response = orderCrudRestApiImpl.getAllOrder();
		assertEquals(response.getStatus(), HttpStatus.SC_OK);

	}  
 
	@Test
	public void getAllOrdersNegTest() throws HMTPException {
		ordList.clear();
		Mockito.when(orderReadRepository.findAllOrders(any(long.class))).thenReturn(ordList);
		Mockito.when(patientSampleReadRepository.findAllBySampleId(any(String.class), any(long.class))).thenReturn(patientSampleList);
		Mockito.when(orderService.createOrderDTO(order)).thenReturn(orderDTO);
		Mockito.when(orderService.createAssayDTO(order)).thenReturn(assayDTO);
		Mockito.when(orderService.createTestOptionsDTO(order)).thenReturn(testOptionsMap);
		Mockito.when(orderService.createPatientDTO(order)).thenReturn(patientDTO);
		Response response = orderCrudRestApiImpl.getAllOrder();
		assertEquals(response.getStatus(), HttpStatus.SC_NOT_FOUND);
	}
	

	
	@Test(expectedExceptions=HMTPException.class)
	public void getAllOrderNegAsExceptionTest() throws HMTPException {
		Mockito.when(orderReadRepository.findAllOrders(any(long.class))).thenReturn(ordList);
		Mockito.when(patientSampleReadRepository.findAllBySampleId(any(String.class), any(long.class))).thenThrow(SQLException.class);
		Mockito.when(orderService.createOrderDTO(order)).thenReturn(null);
		Mockito.when(orderService.createAssayDTO(order)).thenReturn(assayDTO);
		Mockito.when(orderService.createTestOptionsDTO(order)).thenReturn(testOptionsMap);
		Mockito.when(orderService.createPatientDTO(order)).thenReturn(patientDTO);
		orderCrudRestApiImpl.getAllOrder();
	}

}
