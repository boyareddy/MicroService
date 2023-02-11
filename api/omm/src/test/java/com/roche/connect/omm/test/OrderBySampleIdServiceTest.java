package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.readrepository.PatientSampleReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.JsonFileReaderAsString;

@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class OrderBySampleIdServiceTest  {
	
	@Mock
	OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);

	@Mock
	PatientSampleReadRepository patientSampleReadRepository = org.mockito.Mockito
			.mock(PatientSampleReadRepository.class);

	@Mock
	OrderServiceImpl orderServiceImpl = org.mockito.Mockito.mock(OrderServiceImpl.class);

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();
	
	@Mock HMTPLoggerImpl hmtpLoggerImpl;
	
	List<Order> orderList = new ArrayList<Order>();
	Order order = null;
	PatientAssay patientAssay = null;
	List<TestOptions> listOfTestOptions = null;
	TestOptions testOptions = null;
	List<PatientSamples> patientsampleList = new ArrayList<PatientSamples>();
	PatientSamples patientSamples = null;

	Map<String, Boolean> testOptionsMap = null;

	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;
	String accessioningID = null;
	long domainId=0L;
	
	String jsonFileString = null;
	OrderParentDTO orderParentNew=null;
	OrderDTO orderDTO =null;
	/*AssayDTO assayDTO=null;
	TestOptionsDTO testOptionsDTO=null;
	PatientDTO patientDTO=null;*/

	@BeforeTest
	public void setUp() throws Exception {
		
		//new changes
		jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudUpdateDetails.json");
		FileReader frNew = new FileReader(new File("src/test/java/Resource/OrderCrudCommon.json"));
		ObjectMapper objectMapperNew = new ObjectMapper();
		orderParentNew = objectMapperNew.readValue(frNew, OrderParentDTO.class);
		orderDTO=orderParentNew.getOrder();
		
		
		//reading patientassay json to cover patient assay model class
		FileReader fr = new FileReader(new File("src/test/java/Resource/PatientAssayMapper.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		patientAssay=objectMapper.readValue(fr, PatientAssay.class);
		expectedCorrectStatusCode = 200;
		expectedIncorrectStatusCode = 510;
		accessioningID = "9876543";
		domainId=1L;
		
		order = new Order();
		order.setId(10000000);
		order.setPatientSampleId(452L);
		order.setAccessioningId(accessioningID);
		order.setActiveFlag("Y");
		order.setAssayType("NIPT");
		order.setCreatedBy("ADMIN");
		order.setOrderComments("first comment");
		order.setOrderStatus("unassigned");
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		order.setCreatedDateTime(ts);
		order.setUpdatedBy("ADMIN");
		order.setUpdatedDateTime(ts);
		Patient patient = new Patient();
		patient.setId(452L);
		order.setPatient(patient);
		listOfTestOptions = new ArrayList<>();
		testOptions = new TestOptions();
		testOptions.setActiveFlag("Y");
		testOptions.setTestId("Harmony");
		listOfTestOptions.add(testOptions);
		order.setListOfTestOptions(listOfTestOptions);
		orderList.add(order);

		patientSamples = new PatientSamples();
		patientSamples.setId(452L);
		patientSamples.setActiveFlag("Y");
		patientSamples.setSampleId("9876543");
		patientSamples.setSampleType("Plasma");
		patientSamples.setPatient(patient);
		patientsampleList.add(patientSamples);

		testOptionsMap = new HashMap<String, Boolean>();
		testOptionsMap.put("harmony", true);
		testOptionsMap.put("scap", true);
		testOptionsMap.put("mx", false);
		testOptionsMap.put("fetalSex", false);
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		Mockito.when(orderReadRepository.findAllByAccessioningId(any(String.class),any(long.class))).thenReturn(orderList);
		Mockito.when(orderReadRepository.findOrderDetailsByAccessioningId(Mockito.anyString())).thenReturn(orderList);
		Mockito.when(patientSampleReadRepository.findPatientDetailsBySampleId(Mockito.anyString())).thenReturn(patientsampleList);
		Mockito.when(patientSampleReadRepository.findAllBySampleId(any(String.class),any(long.class))).thenReturn(patientsampleList);
		Mockito.when(orderServiceImpl.createOrderDTO(order)).thenReturn(orderDTO);
		Mockito.when(orderServiceImpl.createAssayDTO(order)).thenReturn(orderDTO.getAssay());
		Mockito.when(orderServiceImpl.createTestOptionsDTO(order)).thenReturn(orderDTO.getAssay().getTestOptions());
		Mockito.when(orderServiceImpl.createPatientDTO(order)).thenReturn(orderDTO.getPatient());
	}

	@Test
	public void getOrderBySampleIdPosTest() throws HMTPException {
		Response response = orderCrudRestApiImpl.getOrderBySampleId(accessioningID);
		assertEquals(response.getStatus(), expectedCorrectStatusCode);
	}
	
	@Test
	public void getOrderBySampleIdNegTest() throws HMTPException {
		Response response =orderCrudRestApiImpl.getOrderBySampleId(null);
		assertEquals(response.getStatus(), 400);
	}

	@AfterTest
	public void afterTest() {
		expectedCorrectStatusCode = 0;
		expectedIncorrectStatusCode = 0;
	}

}
