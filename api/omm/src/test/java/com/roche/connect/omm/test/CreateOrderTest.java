package com.roche.connect.omm.test;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.JsonFileReaderAsString;
import com.roche.connect.omm.util.ValidationUtil;
import com.roche.connect.omm.writerepository.OrderWriteRepository;
import com.roche.connect.omm.writerepository.PatientAssayWriteRepository;
import com.roche.connect.omm.writerepository.PatientSampleWriteRepository;
import com.roche.connect.omm.writerepository.PatientWriteRepository;
import com.roche.connect.omm.writerepository.TestOptionsWriteRepository;

@PrepareForTest({ RestClientUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class CreateOrderTest {

	@Mock
	PatientWriteRepository patientWriteRepository = org.mockito.Mockito.mock(PatientWriteRepository.class);

	@Mock
	OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);

	@Mock
	PatientAssayWriteRepository patientAssayWriteRepository = org.mockito.Mockito
			.mock(PatientAssayWriteRepository.class);

	@Mock
	PatientSampleWriteRepository patientSampleWriteRepository = org.mockito.Mockito
			.mock(PatientSampleWriteRepository.class);

	@Mock
	OrderWriteRepository orderWriteRepository = org.mockito.Mockito.mock(OrderWriteRepository.class);

	@Mock
	TestOptionsWriteRepository testOptionsWriteRepository = org.mockito.Mockito.mock(TestOptionsWriteRepository.class);

	@Mock
	ValidationUtil validateUtil = org.mockito.Mockito.mock(ValidationUtil.class);

	@Mock
	PatientSamples patientSampleOne = org.mockito.Mockito.mock(PatientSamples.class);

	@Spy
	@InjectMocks
	OrderService orderServiceImpl = org.mockito.Mockito.spy(new OrderServiceImpl());

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();

	@Mock
	Invocation.Builder ammClient;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	OrderParentDTO orderParentNew = null;
	PatientDTO patientMapper = null;
	Patient patient = null;
	PatientAssay patientAssay = null;
	List<PatientSamples> patientSamplesList = null;
	PatientSamples patientSamples = null;
	PatientSamples patientSamplessecond = null;
	Order order = null;
	List<TestOptions> listOfTestOptions = null;
	TestOptions testoptions = null;

	int expectedCorrectStatusCode = 0;
	String jsonFileString = null;
	List<Order> orderaccessioningId = null;
	// boolean validateOrderParent=false;

	@BeforeTest
	public void setUp() throws Exception {

		jsonFileString = JsonFileReaderAsString.getJsonfromFile("src/test/java/Resource/OrderCrudCommon.json");
		FileReader fr = new FileReader(new File("src/test/java/Resource/OrderCrudCommon.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		orderParentNew = objectMapper.readValue(fr, OrderParentDTO.class);
		patientMapper = orderParentNew.getOrder().getPatient();

		patientSamplesList = new ArrayList<>();
		patientSamplesList.add(getPatientSamples());
		patientSamplesList.add(getPatientSamplesSecondObj());

		orderaccessioningId = new ArrayList<>();
		orderaccessioningId.add(getOrder());
		orderaccessioningId.removeAll(orderaccessioningId);

		// TestOptions
		listOfTestOptions = new ArrayList<>();
		listOfTestOptions.add(getTestOptions());

		expectedCorrectStatusCode = 200;

		MockitoAnnotations.initMocks(this);
	}

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@Test(priority = 1)
	public void createOrderPosTest() throws Exception {
		// PowerMockito.doReturn(true).when(orderServiceImpl).getRequiredFieldMissingFlag(any(OrderParentDTO.class));
		String api = "pas.amm_api_url";
		String apiPath = "/json/rest/api/v1/assay";
		String assayType = "NIPTDPCR";
		String urlassayType = "http://localhost";
		String UTF = "UTF-8";
		List<AssayInputDataValidationsDTO> mandatoryFieldAssayList = new ArrayList<>();
		mandatoryFieldAssayList.add(getInputValidationData());
		ReflectionTestUtils.setField(orderServiceImpl, "phiEncrypt", false);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(RestClientUtil.getUrlString(api, "", apiPath,
				"/" + "NIPTDPCR" + "/inputdatavalidations?groupName=" + "mandatory flag", null))
				.thenReturn(urlassayType);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(urlassayType, UTF), null)).thenReturn(ammClient);
		Mockito.when(ammClient.get(new GenericType<List<AssayInputDataValidationsDTO>>() {
		})).thenReturn(mandatoryFieldAssayList);

		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(orderReadRepository.findAllByAccessioningId(Mockito.any(String.class), Mockito.any(Long.class)))
				.thenReturn(orderaccessioningId);
		Mockito.when(validateUtil.isValidDate(Mockito.any(Timestamp.class), Mockito.any(Timestamp.class)))
				.thenReturn(true);
		Mockito.when(validateUtil.isValidName(Mockito.any(String.class))).thenReturn(true);
		Mockito.when(validateUtil.validateOrderParentDetails(Mockito.any(OrderParentDTO.class))).thenReturn(true);
		Mockito.when(patientSampleOne.getId()).thenReturn(Mockito.any(Long.class));

		// saving the data into db
		Mockito.when(patientWriteRepository.save(patient)).thenReturn(patient);
		Mockito.when(patientAssayWriteRepository.save(patientAssay)).thenReturn(patientAssay);
		Mockito.when(patientSampleWriteRepository.save(patientSamplesList.get(1))).thenReturn(patientSamplessecond);
		Mockito.when(patientSampleWriteRepository.save(Mockito.any(PatientSamples.class))).thenReturn(patientSamples);
		Mockito.when(orderWriteRepository.save(Mockito.any(Order.class))).thenReturn(order);
		Mockito.when(testOptionsWriteRepository.save(Mockito.any(TestOptions.class))).thenReturn(testoptions);

		Response actualResponseCode = orderCrudRestApiImpl.createOrder(jsonFileString);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}

	@AfterTest
	public void afterTest() {
		expectedCorrectStatusCode = 0;
		jsonFileString = null;
	}

	// get Patient Object
	private Patient getPatient() {
		patient = new Patient();
		patient.setId(1L);
		patient.setPatientFirstName(patientMapper.getPatientFirstName());
		patient.setPatientLastName(patientMapper.getPatientLastName());
		return patient;
	}

	// get PatientAssay Object
	private PatientAssay getPatientAssay() {
		patientAssay = new PatientAssay();
		patientAssay.setMaternalAge(5);
		patientAssay.setActiveFlag("Y");
		patientAssay.setPatient(getPatient());
		return patientAssay;
	}

	// get patientSamplesOne Object
	private PatientSamples getPatientSamples() {
		patientSamples = new PatientSamples();
		patientSamples.setId(1);
		patientSamples.setSampleType("plasma");
		patientSamples.setCreatedBy("ADMIN");
		return patientSamples;
	}

	// get patientSamplesSecond Object
	private PatientSamples getPatientSamplesSecondObj() {
		patientSamplessecond = new PatientSamples();
		patientSamplessecond.setId(2);
		patientSamplessecond.setSampleType("Blood");
		patientSamplessecond.setCreatedBy("ADMIN");
		return patientSamplessecond;
	}

	// get Order object
	private Order getOrder() {
		order = new Order();
		order.setId(100000);
		order.setAccessioningId("12345");
		order.setAssayType("NIPT");
		order.setPatientSampleId(patientSamples.getId());
		return order;
	}

	// get TestOptions
	private TestOptions getTestOptions() {
		testoptions = new TestOptions();
		testoptions.setTestId("Harmony");
		testoptions.setActiveFlag("Y");
		return testoptions;
	}

	private AssayInputDataValidationsDTO getInputValidationData() {
		AssayInputDataValidationsDTO inputData = new AssayInputDataValidationsDTO();
		inputData.setAssayType("NIPTDPCR");
		inputData.setExpression("NA");
		inputData.setFieldName("Comments");
		inputData.setGroupName("mandatory flag");
		inputData.setIsMandatory("false");
		inputData.setMaxVal(null);
		inputData.setMinVal(null);
		return inputData;
	}

}
