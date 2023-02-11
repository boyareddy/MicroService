package com.roche.connect.omm.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.common.order.dto.ProcessStepValuesDTO;
import com.roche.connect.common.order.dto.WorkflowDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.CSVParserUtil;



public class DTOServicesTest {
	String expectedPatientFirstName = null;
	String actualPatientFirstName = null;

	String expectedTestId = null;
	String actualTestId = null;
	

	String expectedEggDonor = null;
	String actualEggDonor = null;

	String expectedAccessioningId = null;
	String actualAccessioningId = null;

	Long actualOrderId = null;
	Long expectedOrderId = null;
	
	String expectedWorkflowType = null;
	String actualWorkflowType = null;

	String csvFilePath = null;
	InputStream stream = null;
	File csvFile = null;
	
	Order order = null;
	OrderParentDTO orderParentDTO = null;
	PatientAssay petientAssay = null;
	AssayDTO assayDTO = null;
	PatientDTO patientDTO = null;
	OrderDTO orderDTO = null;
	Map<String, Boolean> testOptionsMap = null;
	Patient patient = null;
	TestOptions testOptions = null;
	List<TestOptions> listOfTestOptions = null;
	List<PatientSamples> patientSampleList = null;
	PatientSamples patientSamples = null;
	ProcessStepValuesDTO processStepValuesDTO = null;
	WorkflowDTO workflowDTO = null;
	
	CSVParserUtil csvParserUtil=null;
	
	@InjectMocks
	OrderServiceImpl orderService = new OrderServiceImpl();
	
	@BeforeTest
	public void setUp() throws Exception {
		
		patient = new Patient();
		order = new Order();
		orderDTO = new OrderDTO();
		patientSamples = new PatientSamples();
		patientSampleList = new ArrayList<>();
		testOptions = new TestOptions();
		petientAssay = new PatientAssay();
		listOfTestOptions = new ArrayList<>();
		testOptionsMap = new HashMap<String, Boolean>();
		orderParentDTO = new OrderParentDTO();
		processStepValuesDTO = new ProcessStepValuesDTO();
		
		

		patientSamples.setId(452L);
		patientSamples.setCreatedBy("");
		patientSamples.setSampleId("452");
		patientSamples.setActiveFlag("Y");

		patient.setId(452L);
		patient.setPatientFirstName("Kumar");
		patient.setPatientLastName("");
		
		patient.setPatientMedicalRecNo("");
		Timestamp ts=new Timestamp(new Date().getTime());
		patient.setPatientDOB("01/08/2018 14:50:50.42");
		/*patient.setPatientGender("");
		patient.setPatientContactNo("");
		patient.setTreatingDoctorName("");
		patient.setTreatingDoctorContactNo("");
		patient.setRefClinicianName("");
		patient.setRefClinicianFaxNo("");
		patient.setOtherClinicianName("");
		patient.setOtherClinicianFaxNo("");*/
		patient.setClinicName("");

		testOptions.setTestId("harmony");
		testOptions.setActiveFlag("Y");
		testOptions.setCreatedBy("");
		testOptions.setCreatedDateTime(ts);

		listOfTestOptions.add(testOptions);
		order.setListOfTestOptions(listOfTestOptions);

		petientAssay.setEggDonor("Self");
		petientAssay.setActiveFlag("Y");
		petientAssay.setId(452L);
		petientAssay.setCollectionDate(ts);
		petientAssay.setCreatedBy("");
		petientAssay.setFetusNumber("");
		petientAssay.setIvfStatus("");
		petientAssay.setMaternalAge(34);
		petientAssay.setUpdatedBy("");
		petientAssay.setGestationalAgeDays(2);
		petientAssay.setRecievedDate(ts);
		petientAssay.setGestationalAgeWeeks(4);
		petientAssay.setUpdatedDateTime(ts);

		patient.setPetientAssay(petientAssay);
		patientSampleList.add(patientSamples);
		patient.setPatientSamples(patientSampleList);

		order.setId(10000020L);
		order.setPatient(patient);
		order.setPatientSampleId(452L);
		order.setAccessioningId("9876543");
		order.setAssayType("NIPT");
		order.setOrderStatus("");
		order.setActiveFlag("Y");
		order.setCreatedBy("");
		order.setCreatedDateTime(ts);
		order.setUpdatedBy("");
		order.setUpdatedDateTime(ts);

		orderParentDTO.setOrder(orderDTO);

		ReflectionTestUtils.setField(orderService, "phiEncrypt", false);
		
		testOptionsMap = orderService.createTestOptionsDTO(order);
		patientDTO = orderService.createPatientDTO(order);
		assayDTO = orderService.createAssayDTO(order);
		orderDTO = orderService.createOrderDTO(order);
		orderParentDTO = orderService.createOrderParentDTO(order);
		
		//createWrokflowOrderObjectTest
		orderDTO.setAccessioningId("9876543");
		processStepValuesDTO.setComments("");
		orderDTO.setOrderId(10000020L);
		processStepValuesDTO.setProcessStepName("NA-Extraction");
		orderDTO.setAssayType("");
		orderDTO.setSampleType("");
		processStepValuesDTO.setRunStatus("In workflow");
		processStepValuesDTO.setRunFlag("");
		workflowDTO = orderService.createWrokflowOrderObject(processStepValuesDTO, orderDTO);
		
		expectedEggDonor = "Self";
		expectedPatientFirstName = "Kumar";
		expectedAccessioningId = "9876543";
		expectedTestId = "harmony";
		expectedOrderId = 10000020L;
		expectedWorkflowType = "NA-Extraction";
		

		csvFilePath = "src/test/java/resource/samplecsv.csv";
		csvFile = new File(csvFilePath);
		stream = new FileInputStream(csvFile);
		csvParserUtil=new CSVParserUtil();

	}

	// createPatientDTO
	@Test
	public void positiveCreatePatientDTOTest() throws ParseException {
		actualPatientFirstName = patientDTO.getPatientFirstName();
		assertEquals(actualPatientFirstName, expectedPatientFirstName);
	}

	@Test
	public void negativeCreatePatientDTOTest() throws ParseException {
		actualPatientFirstName = patientDTO.getPatientFirstName();
		assertNotEquals(actualPatientFirstName, "testName");

	}

	// createTestOptionsDTO
	@Test
	public void positiveTestOptionDTOTest() throws ParseException {
		Map.Entry<String, Boolean> entry = testOptionsMap.entrySet().iterator().next();
		String actualTestId = entry.getKey();
		assertEquals(actualTestId, expectedTestId);
	}

	@Test
	public void negativeTestOptionDTOTest() throws ParseException {
		Map.Entry<String, Boolean> entry = testOptionsMap.entrySet().iterator().next();
		String actualTestId = entry.getKey();
		assertNotEquals(actualTestId, "testId");

	}

	// createAssayDTO
	@Test
	public void positiveAssayDTOTest() throws ParseException {
		actualEggDonor = assayDTO.getEggDonor();
		assertEquals(actualEggDonor, expectedEggDonor);
	}

	@Test
	public void negativeAssayDTOTest() throws ParseException {
		actualEggDonor = assayDTO.getEggDonor();
		assertNotEquals(actualEggDonor, "testDonor");

	}

	// createOrderDTO
	@Test
	public void positiveOrderDTOTest() throws ParseException {
		actualAccessioningId = orderDTO.getAccessioningId();
		assertEquals(actualAccessioningId, expectedAccessioningId);
	}

	@Test
	public void negativeOrderDTOTest() throws ParseException {
		actualAccessioningId = orderDTO.getAccessioningId();
		assertNotEquals(actualAccessioningId, "testAccessioningId");

	}

	// createOrderParentDTO
	@Test
	public void positiveOrderParentDTOTest() throws ParseException {
		actualOrderId = orderParentDTO.getOrder().getOrderId();
		assertEquals(actualOrderId, expectedOrderId);
	}

	@Test
	public void negativeOrderParentDTOTest() throws ParseException {
		actualOrderId = orderParentDTO.getOrder().getOrderId();
		assertNotEquals(actualOrderId, 4857934L);

	}
	
	//createWrokflowOrderObjectTest
	@Test
	public void positiveWorkflowOrderObjectTest() throws ParseException {
		actualWorkflowType = workflowDTO.getWorkflowType();
		assertEquals(actualWorkflowType, expectedWorkflowType);
	}

	@Test
	public void negativeWorkflowOrderObjectTest() throws ParseException {
		actualWorkflowType = workflowDTO.getWorkflowType();
		assertNotEquals(actualWorkflowType, "testWorkflowType");

	}
	
	
/*	@Test(expectedExceptions = { NullPointerException.class, HMTPException.class,InvocationTargetException.class })
	public void parseCSVNegTest() throws Exception {
		csvParserUtil.parseCSV(stream);
	}*/
	

	@AfterTest
	public void tearDown() throws Exception {
		expectedEggDonor = null;
		expectedPatientFirstName = null;
		expectedTestId = null;
		expectedAccessioningId = null;
		expectedOrderId = null;
	}

}
