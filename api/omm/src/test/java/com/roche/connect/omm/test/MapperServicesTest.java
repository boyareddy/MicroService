package com.roche.connect.omm.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.common.util.DateUtil;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;

public class MapperServicesTest {
	String expectedEggDonor = null;
	String actualEggDonor = null;

	String expectedPatientFirstName = null;
	String actualPatientFirstName = null;

	String expectedAccessioningId = null;
	String actualAccessioningId = null;
	
	Long expectedOrderId=null;

	Order order = null;
	OrderParentDTO orderParent = null;
	PatientAssay patientAssay = null;
	AssayDTO assayDTO = null;
	PatientDTO patientDTO = null;
	OrderDTO orderDTO = null;
	Map<String, Boolean> testOptionsMap = null;
	Map<String,Object> ordersMap=null;
	List<Map> orderMapperList=null;
	List<OrderDTO> orderDTOList=null;
	Patient patient = null;

	@Mock
	DateUtil dateUtil = org.mockito.Mockito.mock(DateUtil.class);

	@InjectMocks
	OrderService orderService = new OrderServiceImpl();

	@BeforeTest
	public void setUp() throws ParseException {
		patientAssay = new PatientAssay();
		patient = new Patient();
		order = new Order();
		order.setPriority("normal");
		assayDTO = new AssayDTO();
		patientDTO = new PatientDTO();
		orderDTO = new OrderDTO();

		assayDTO.setMaternalAge(45);
		assayDTO.setGestationalAgeWeeks(5);
		assayDTO.setGestationalAgeDays(12);
		assayDTO.setEggDonor("Self");
		assayDTO.setIvfStatus("Y");
		assayDTO.setFetusNumber("2");
		assayDTO.setCollectionDate(dateUtil.getCurrentUTCTimeStamp());
		assayDTO.setReceivedDate(dateUtil.getCurrentUTCTimeStamp());
		testOptionsMap = new HashMap<String, Boolean>();
		testOptionsMap.put("harmony", true);
		testOptionsMap.put("scap", true);
		testOptionsMap.put("mx", false);
		testOptionsMap.put("fetalSex", false);
		assayDTO.setTestOptions(testOptionsMap);

		patientDTO.setPatientFirstName("Kumar");
		patientDTO.setPatientLastName("");
		
		patientDTO.setPatientMedicalRecNo("");
		patientDTO.setPatientDOB("01/08/2018 14:50:50.42");
		/*patientDTO.setPatientGender("");
		patientDTO.setPatientContactNo("");
		patientDTO.setTreatingDoctorName("");
		patientDTO.setTreatingDoctorContactNo("");
		patientDTO.setRefClinicianFaxNo("");
		patientDTO.setOtherClinicianFaxNo("");*/
		patientDTO.setRefClinicianName("");
		patientDTO.setOtherClinicianName("");
		patientDTO.setClinicName("");
		orderDTO.setAccessioningId("9876543");
		orderDTO.setAssayType("");
		orderDTO.setOrderComments("");
		orderDTO.setReqFieldMissingFlag(false);
		orderDTO.setPriority("high");
		
		//patient.setId(1);
		ordersMap=new HashMap<>();
		ordersMap.put("orderId", Long.parseLong("10000000"));
		ordersMap.put("patientId", Long.parseLong("10"));
		ordersMap.put("patientSampleId", Long.parseLong("11"));
		ordersMap.put("accessioningId", "10000001");
		ordersMap.put("orderStatus", "unassigned");
		ordersMap.put("assayType", "NIPT");
		ordersMap.put("orderComments", "comments");
		ordersMap.put("activeFlag", "Y");
		ordersMap.put("createdBy", "ADMIN");
		ordersMap.put("sampleType", "Plasma");
		ordersMap.put("reqFieldMissingFlag", false);
		ordersMap.put("priority", "high");
		//LocalDateTime date=LocalDateTime.now();
		DateFormat formatter;
	    formatter = new SimpleDateFormat("dd/MM/yyyy");
	    Date date = (Date) formatter.parse("28/09/2018");
	    java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
		ordersMap.put("createdDateTime",timeStampDate);
		ordersMap.put("priorityUpdatedTime",timeStampDate);
		orderMapperList=new ArrayList<>();		
		orderMapperList.add(ordersMap);

		ReflectionTestUtils.setField(orderService, "phiEncrypt", false);
		
		patientAssay = orderService.patientAssayMapper(patientAssay, assayDTO);
		patient = orderService.patientUpdateMapper(patient, patientDTO);
		order = orderService.orderUpdateMapper(orderDTO, order);
		
		expectedEggDonor = "Self";
		expectedPatientFirstName = "Kumar";
		expectedAccessioningId = "9876543";
		expectedOrderId=10000000L;

		MockitoAnnotations.initMocks(this);
		Mockito.when(dateUtil.getCurrentUTCTimeStamp()).thenReturn(new Timestamp(new Date().getTime()));
	}
		
	// OrderUpdateMapperTest
		@Test
		public void positiveOrderUpdateMapperTest() throws ParseException {
			actualAccessioningId = order.getAccessioningId();
			assertEquals(actualAccessioningId, expectedAccessioningId);
		}

		@Test
		public void negativeOrderUpdateMapperTest() throws ParseException {
			actualAccessioningId = order.getAccessioningId();
			assertNotEquals(actualAccessioningId, "87978");
		}
		
		// PatientUpdateMapperTest
		@Test
		public void positivePatientUpdateMapperTest() throws ParseException {
			actualPatientFirstName = patient.getPatientFirstName();
			assertEquals(actualPatientFirstName, expectedPatientFirstName);
		}

		@Test
		public void negativePatientUpdateMapperTest() throws ParseException {
			actualPatientFirstName = patient.getPatientFirstName();
			assertNotEquals(actualPatientFirstName, "testName");
		}


	// PatientAssayMapperTest
	@Test
	public void positivePatientAssayMapperTest() throws ParseException {
		actualEggDonor = patientAssay.getEggDonor();
		assertEquals(actualEggDonor, expectedEggDonor);
	}

	@Test
	public void negativePatientAssayMapperTest() throws ParseException {
		actualEggDonor = patientAssay.getEggDonor();
		assertNotEquals(actualEggDonor, "testDonor");
	}

	
	//convertMapToOrders
	
	@Test
	public void positiveConvertMapToOrdTest() throws ParseException {
		orderDTOList=orderService.convertMapToOrders(orderMapperList);

		orderDTO = orderDTOList.get(0);
		assertEquals(orderDTO.getOrderId(),expectedOrderId);
	}
	
	

	@AfterTest
	public void afterTest() {
		expectedEggDonor = null;
		expectedPatientFirstName = null;
		expectedAccessioningId = null;
		expectedOrderId=null;
	}

}
