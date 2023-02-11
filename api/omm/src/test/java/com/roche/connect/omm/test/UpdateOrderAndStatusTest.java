package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.common.util.DateUtil;
import com.roche.connect.common.util.DateUtilImpl;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.readrepository.PatientAssayReadRepository;
import com.roche.connect.omm.readrepository.PatientReadRepository;
import com.roche.connect.omm.readrepository.TestOptionsReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.JsonFileReaderAsString;
import com.roche.connect.omm.util.ValidationUtil;
import com.roche.connect.omm.writerepository.OrderWriteRepository;
import com.roche.connect.omm.writerepository.PatientAssayWriteRepository;
import com.roche.connect.omm.writerepository.PatientSampleWriteRepository;
import com.roche.connect.omm.writerepository.PatientWriteRepository;
import com.roche.connect.omm.writerepository.TestOptionsWriteRepository;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })

public class UpdateOrderAndStatusTest {
    @Mock ObjectMapper objectmapper;


	@Mock
	OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);

	@Mock
	OrderWriteRepository orderWriteRepository = org.mockito.Mockito.mock(OrderWriteRepository.class);

	@Mock
	PatientReadRepository patientReadRepository = org.mockito.Mockito.mock(PatientReadRepository.class);

	@Mock
	PatientWriteRepository patientWriteRepository = org.mockito.Mockito.mock(PatientWriteRepository.class);

	@Mock
	PatientSampleWriteRepository patientSampleWriteRepository = org.mockito.Mockito
			.mock(PatientSampleWriteRepository.class);

	@Mock
	PatientAssayReadRepository patientAssayReadRepository = org.mockito.Mockito.mock(PatientAssayReadRepository.class);

	@Mock
	PatientAssayWriteRepository patientAssayWriteRepository = org.mockito.Mockito
			.mock(PatientAssayWriteRepository.class);

	@Mock
	TestOptionsReadRepository testOptionsReadRepository = org.mockito.Mockito.mock(TestOptionsReadRepository.class);

	@Mock
	TestOptionsWriteRepository testOptionsWriteRepository = org.mockito.Mockito.mock(TestOptionsWriteRepository.class);

	@Mock
	DateUtil dateUtil = org.mockito.Mockito.mock(DateUtilImpl.class);

	@Mock
	ValidationUtil validateUtil = org.mockito.Mockito.mock(ValidationUtil.class);

	@InjectMocks
	@Spy
	OrderServiceImpl orderServiceImpl = org.mockito.Mockito.spy(new OrderServiceImpl());

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();

	@Mock
	Response response;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	OrderParentDTO orderParentNew = null;
	OrderDTO orderDTO = null;
	AssayDTO assayDTO = null;
	PatientDTO patientDTO = null;
	Order order = null;
	Patient patient = null;
	PatientSamples patientSamples = null;
	List<PatientSamples> patientSampleList = null;
	PatientAssay patientAssay = null;
	TestOptions testOptions = null;
	List<TestOptions> testOptionsList = null;

	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;
	String jsonFileString = null;
	String jsonFileStringReset = null;
	long orderId = 0;
	String orderStatus = null;
	Long patientId = 0L;

	@BeforeTest
	public void setUp() throws Exception {

		jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudUpdateDetails.json");
		jsonFileStringReset = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/Resource/OrderCrudUpdDetailsReset.json");
		FileReader fr = new FileReader(new File("src/test/java/Resource/OrderCrudUpdateDetails.json"));
		FileReader fr1 = new FileReader(new File("src/test/java/Resource/PatientAssayMapper.json"));

		ObjectMapper objectMapper = new ObjectMapper();
		orderParentNew = objectMapper.readValue(fr, OrderParentDTO.class);
		orderDTO = orderParentNew.getOrder();
		assayDTO = orderDTO.getAssay();
		patientDTO = orderDTO.getPatient();

		// order
		patientSamples = new PatientSamples();
		patientSamples.setActiveFlag("Y");
		patientSamples.setSampleType("plasma");
		patientSamples.setSampleId("12312");
		PatientSamples patientSamples2 = new PatientSamples();
		patientSamples2.setActiveFlag("Y");
		patientSamples2.setSampleType("plasma");
		patientSamples2.setSampleId("12312");
		patientSampleList = new ArrayList<>();
		patientSampleList.add(patientSamples);
		patientSampleList.add(patientSamples2);
		patient = new Patient();
		patient.setId(1);
		patient.setPatientLastName("Ajay");
		patient.setPatientFirstName("Kumar");
		patient.setOtherClinicianName("apolo");
		patient.setClinicName("apolo");
		patient.setRefClinicianName("apolo");
		patient.setPatientSamples(patientSampleList);

		order = new Order();
		order.setAccessioningId("999999");
		order.setAssayType("NIPTDPCR");
		order.setOrderComments("first comment");
		order.setPatient(patient);
		Company company = new Company();
		company.setId(-1);
		order.setCompany(company);
		Timestamp ts = new Timestamp(new Date().getTime());
		patientAssay = objectMapper.readValue(fr1, PatientAssay.class);

		// can set any random 2 values but for code coverage added all the
		// fields
		testOptions = new TestOptions();
		testOptions.setTestId("harmony");
		testOptions.setActiveFlag("Y");
		testOptions.setCreatedBy("ADMIN");
		testOptions.setCreatedDateTime(ts);
		testOptions.setId(10);
		testOptions.setUpdatedBy("ADMIN");
		testOptions.setUpdatedDateTime(ts);
		testOptions.setOrder(order);

		testOptionsList = new ArrayList<>();
		testOptionsList.add(testOptions);

		expectedCorrectStatusCode = 200;
		expectedIncorrectStatusCode = 400;
		orderId = 10000027;
		orderStatus = "unassigned";
		patientId = 1L;

		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		ReflectionTestUtils.setField(orderServiceImpl, "phiEncrypt", false);
		Mockito.when(dateUtil.getCurrentUTCTimeStamp()).thenReturn(new Timestamp(new Date().getTime()));
		Mockito.when(orderReadRepository.findOne(orderId)).thenReturn(order);
		PowerMockito.doReturn(order).when(orderServiceImpl).orderUpdateMapper(orderDTO, order);
		Mockito.when(patientSampleWriteRepository.save(patientSamples)).thenReturn(patientSamples);
		Mockito.when(orderWriteRepository.save(order)).thenReturn(order);
		Mockito.when(orderReadRepository.findPatientIdByOrderId(orderId)).thenReturn(patientId);
		Mockito.when(patientAssayWriteRepository.save(patientAssay)).thenReturn(patientAssay);
		Mockito.when(testOptionsReadRepository.findTestOptionDetailsByOrderId(orderId)).thenReturn(testOptionsList);
		Mockito.when(testOptionsWriteRepository.save(testOptions)).thenReturn(testOptions);
		Mockito.when(patientReadRepository.findOne(patientId)).thenReturn(patient);
		PowerMockito.doReturn(patient).when(orderServiceImpl).patientUpdateMapper(patient, patientDTO);
		Mockito.when(patientWriteRepository.save(patient)).thenReturn(patient);
		Mockito.when(orderReadRepository.findOrderByOrderId(any(long.class), any(long.class))).thenReturn(order);
		Mockito.when(validateUtil.isValidDate(any(Timestamp.class), any(Timestamp.class))).thenReturn(true);
	//	Mockito.when(validateUtil.isValidName(any(String.class))).thenReturn(true);
		//Mockito.when(orderService.getRequiredFieldMissingFlag(Mockito.any(OrderParentDTO.class))).thenReturn(true);
		
		//orderService.getRequiredFieldMissingFlag(orderParentDTO)
	}

	@Test
	public void updateOrderPosTest() throws Exception {
	    Mockito.when(validateUtil.isValidName(any(String.class))).thenReturn(true);
		PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
		Mockito.when(patientAssayReadRepository.findByPatient(any(Long.class))).thenReturn(patientAssay);
		Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
		Response actualResponseCode = orderCrudRestApiImpl.update(jsonFileString);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void updateOrderElseTest() throws Exception {
	    Mockito.when(validateUtil.isValidName(any(String.class))).thenReturn(true);
		Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(false);
		 orderCrudRestApiImpl.update(jsonFileString);
		
	}

	@Test(priority = 2)
	public void getUpdOrdStatusPosTest() throws HMTPException {
	    Mockito.when(validateUtil.isValidName(any(String.class))).thenReturn(true);
		PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
		Response response = orderCrudRestApiImpl.updateOrderStatus(orderId, orderStatus);
		assertEquals(response.getStatus(), expectedCorrectStatusCode);
	}

	@Test
	public void updateOrderPos2Test() throws Exception {
	    Mockito.when(validateUtil.isValidName(any(String.class))).thenReturn(true);
		PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
		Mockito.when(testOptionsReadRepository.findTestOptionDetailsByOrderId(any(Long.class))).thenReturn(testOptionsList);
		Mockito.when(orderReadRepository.findOne(any(Long.class))).thenReturn(order);
		Mockito.when(patientAssayReadRepository.findByPatient(any(Long.class))).thenReturn(patientAssay);	
		Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
		Mockito.when(patientReadRepository.findOne(any(Long.class))).thenReturn(patient);
		Response actualResponseCode = orderCrudRestApiImpl.update(jsonFileStringReset);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}
	
	@Test
	public void updateOrderPos2ElseTest() throws Exception {
	    Mockito.when(validateUtil.isValidName(any(String.class))).thenReturn(true);
	    PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class)); 
		Mockito.when(testOptionsReadRepository.findTestOptionDetailsByOrderId(any(Long.class))).thenReturn(testOptionsList);
		Mockito.when(orderReadRepository.findOne(any(Long.class))).thenReturn(order);
		Mockito.when(patientAssayReadRepository.findByPatient(any(Long.class))).thenReturn(patientAssay);	
		Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
		Mockito.when(patientReadRepository.findOne(any(Long.class))).thenReturn(patient);
		Response actualResponseCode = orderCrudRestApiImpl.update(JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudUpdateWrongDetails.json"));
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}

	/*@Test(priority = 3)
	public void updateOrderIdNullNegTest() throws Exception {
	    Mockito.when(validateUtil.isValidName(any(String.class))).thenReturn(true);	
	    PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
		Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
		Mockito.when(validateUtil.isValidDate(any(Timestamp.class), any(Timestamp.class))).thenReturn(false);
		Response response = orderCrudRestApiImpl.update(jsonFileStringReset);
		assertEquals(response.getStatus(), expectedIncorrectStatusCode);
	}
	 
	
	@Test
	public void updateOrderPatientFirstNameExceptionTest() throws Exception {
	    
		PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
		Mockito.when(testOptionsReadRepository.findTestOptionDetailsByOrderId(any(Long.class))).thenReturn(testOptionsList);
		Mockito.when(orderReadRepository.findOne(any(Long.class))).thenReturn(order);
		Mockito.when(patientAssayReadRepository.findByPatient(any(Long.class))).thenReturn(patientAssay);	
		Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
		Mockito.when(patientReadRepository.findOne(any(Long.class))).thenReturn(patient);
        ObjectMapper objectMapper = new ObjectMapper();
        OrderParentDTO  orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
        PatientDTO patientDto=new PatientDTO();
        patientDto.setPatientFirstName("121212");
        orderParentNew.getOrder().setPatient(patientDto);
        String json=objectMapper.writeValueAsString(orderParentNew);
        Response actualResponseCode = orderCrudRestApiImpl.update(json);
		assertEquals(actualResponseCode.getStatus(), expectedIncorrectStatusCode);
	}
	
	@Test
	public void updateOrderPatientLastNameExceptionTest() throws Exception {
	   
		PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
		Mockito.when(testOptionsReadRepository.findTestOptionDetailsByOrderId(any(Long.class))).thenReturn(testOptionsList);
		Mockito.when(orderReadRepository.findOne(any(Long.class))).thenReturn(order);
		Mockito.when(patientAssayReadRepository.findByPatient(any(Long.class))).thenReturn(patientAssay);	
		Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
		Mockito.when(patientReadRepository.findOne(any(Long.class))).thenReturn(patient);
        ObjectMapper objectMapper = new ObjectMapper();
        OrderParentDTO  orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
        PatientDTO patientDto=new PatientDTO();
        patientDto.setPatientFirstName("jhon");
        patientDto.setPatientLastName("121212");
        orderParentNew.getOrder().setPatient(patientDto);
        String json=objectMapper.writeValueAsString(orderParentNew);
        Response actualResponseCode = orderCrudRestApiImpl.update(json);
        assertEquals(actualResponseCode.getStatus(), expectedIncorrectStatusCode);
	}
	   @Test
	    public void updateOrderOtherClinicianNameTest() throws Exception {
	        PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
	        Mockito.when(testOptionsReadRepository.findTestOptionDetailsByOrderId(any(Long.class))).thenReturn(testOptionsList);
	        Mockito.when(orderReadRepository.findOne(any(Long.class))).thenReturn(order);
	        Mockito.when(patientAssayReadRepository.findByPatient(any(Long.class))).thenReturn(patientAssay);   
	        Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
	        Mockito.when(patientReadRepository.findOne(any(Long.class))).thenReturn(patient);
	        ObjectMapper objectMapper = new ObjectMapper();
	        OrderParentDTO  orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
	        PatientDTO patientDto=new PatientDTO();
	        patientDto.setOtherClinicianName("121212");
	        orderParentNew.getOrder().setPatient(patientDto);
	        String json=objectMapper.writeValueAsString(orderParentNew);
	        Response actualResponseCode = orderCrudRestApiImpl.update(json);
	        assertEquals(actualResponseCode.getStatus(), expectedIncorrectStatusCode);
	    }
	        
	@Test
    public void updateOrderClinicNameTest() throws Exception {
        Mockito.when(validateUtil.isValidName(any(String.class))).thenReturn(true);

        PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
        Mockito.when(testOptionsReadRepository.findTestOptionDetailsByOrderId(any(Long.class))).thenReturn(testOptionsList);
        Mockito.when(orderReadRepository.findOne(any(Long.class))).thenReturn(order);
        Mockito.when(patientAssayReadRepository.findByPatient(any(Long.class))).thenReturn(patientAssay);   
        Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
        Mockito.when(patientReadRepository.findOne(any(Long.class))).thenReturn(patient);
        ObjectMapper objectMapper = new ObjectMapper();
        OrderParentDTO orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
        PatientDTO patientDto = new PatientDTO();
        patientDto.setPatientFirstName("jhon");
        patientDto.setPatientLastName("jhon");
        //patientDto.setClinicName("jhon");
        patientDto.setOtherClinicianName("jhon");
        patientDto.setRefClinicianName("aa");
        orderParentNew.getOrder().setPatient(patientDto);
        String json = objectMapper.writeValueAsString(orderParentNew);
        Response actualResponseCode = orderCrudRestApiImpl.update(json);
        assertEquals(actualResponseCode.getStatus(), expectedIncorrectStatusCode);
    }


	
	@Test
	public void updateRefClinicianNameTest() throws Exception {
	       Mockito.when(validateUtil.isValidName(any(String.class))).thenReturn(true);
	    PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
		Mockito.when(testOptionsReadRepository.findTestOptionDetailsByOrderId(any(Long.class))).thenReturn(testOptionsList);
		Mockito.when(orderReadRepository.findOne(any(Long.class))).thenReturn(order);
		Mockito.when(patientAssayReadRepository.findByPatient(any(Long.class))).thenReturn(patientAssay);	
		Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
		Mockito.when(patientReadRepository.findOne(any(Long.class))).thenReturn(patient);
		ObjectMapper objectMapper = new ObjectMapper();
        OrderParentDTO orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);
        PatientDTO patientDto = new PatientDTO();
        patientDto.setPatientFirstName("jhon");
        patientDto.setPatientLastName("jhon");
        patientDto.setClinicName("jhon");
        patientDto.setOtherClinicianName("jhon");
       // patientDto.setRefClinicianName("aa");
        orderParentNew.getOrder().setPatient(patientDto);
        String json = objectMapper.writeValueAsString(orderParentNew);
        Response actualResponseCode = orderCrudRestApiImpl.update(json);
        assertEquals(actualResponseCode.getStatus(), expectedIncorrectStatusCode);
    }
	 @Test
     public void UpdateOrderIdNullTest() throws IOException, HMTPException {
        PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
        Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudUpdateDetails.json");
        OrderParentDTO orderParentNew = objectMapper.readValue(jsonFileString, OrderParentDTO.class);   
        orderParentNew.getOrder().setOrderId(null);
        String json = objectMapper.writeValueAsString(orderParentNew);
        Response actualResponseCode = orderCrudRestApiImpl.update(json);
        assertEquals(actualResponseCode.getStatus(), 400);
     }*/
	 @Test
     public void UpdateOrderOwnerIdTest() throws IOException, HMTPException {
        PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
        Mockito.when(validateUtil.isValidDate(any(Timestamp.class), any(Timestamp.class))).thenReturn(true);
        Mockito.when(validateUtil.validateOrderParentDetails(any(OrderParentDTO.class))).thenReturn(true);
        String jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudUpdateDetails.json");
        Order order=new Order();
        order.setAssayType("NIPTDPCR");
        Company company=new Company();
        company.setId(-10l);
        order.setCompany(company);
        Mockito.when(orderReadRepository.findOne(any(Long.class))).thenReturn(order);
        Response actualResponseCode = orderCrudRestApiImpl.update(jsonFileString);
        assertEquals(actualResponseCode.getStatus(), 404);
     }
	
	@AfterTest
	public void afterTest() {
		expectedCorrectStatusCode = 0;
		expectedIncorrectStatusCode = 0;
		jsonFileString = null;
	}

}
