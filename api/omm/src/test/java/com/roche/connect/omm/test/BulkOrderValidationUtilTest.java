package com.roche.connect.omm.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.amm.dto.AssayListDataDTO;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.amm.dto.SampleTypeDTO;
import com.roche.connect.common.amm.dto.TestOptionsDTO;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.util.BulkOrderValidationUtil;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, URLEncoder.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class BulkOrderValidationUtilTest {
	@InjectMocks
	BulkOrderValidationUtil bulkOrderValidationUtil;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	OrderReadRepository orderReadRepository;

	@Mock
	OrderCrudRestApiImpl orderCrudRestApiImpl;

	@Mock
	Invocation.Builder orderClient;

	@Mock
	Invocation.Builder orderClientSample;
	@Mock
	Invocation.Builder orderClientAssayList;

	@Mock
	Invocation.Builder orderClienttestOptions;

	@Mock
	Invocation.Builder orderClientinputDataValidations;

	Map<String, Object> assayDetails = new HashMap<>();

	String otherClinicianName = "RMMClinic";
	String columnName = "clinic";
	int row = 1;
	OrderParentDTO orderParentDTO = new OrderParentDTO();
	List<String> errors = new ArrayList<>();
	List<String> accessioningIdList = new ArrayList<>();
	String accessioningId = "12345";
	List<Order> listOrder = new ArrayList<>();
	String assayType;
	String retestSample = "abcde";
	String testOptions = "blood";
	String testOptionsFetalSex = "male";
	String orderComments = "done";
	String collectionDate = "02/20/2019";
	String receivedDate = "03/20/2019";
	String maternalAge = "26";
	String gestationalAgeDays = "30";
	String eggDonorAge = "30";
	String eggDonor = "30";
	String ivfStatus = "good";
	String fetusNumber = "30";
	String patientMedicalRecNo = "30";
	String patientDOB = "05/20/2011";
	String accessioningId2 = "12345";
	String gestationalAgeWeeks = "";
	String otherClinicianFaxNo = "30";
	String refClinicianClinicName = "rocheclinic";
	String refClinicianFaxNo = "30";
	String refClinicianName = "rocheFresh";
	String treatingDoctorName = "varunKumar";
	String treatingDoctorContactNo = "varunKumar";
	String patientContactNo = "all";
	String patientGender = "male";
	String patientFirstName = "apple";
	String patientLastName = "apple";

	List<AssayInputDataValidationsDTO> assayInputDataValidationsDTO = new ArrayList<>();
	List<AssayTypeDTO> assayTypelist = new ArrayList<AssayTypeDTO>();
	List<SampleTypeDTO> sampleTypelist = new ArrayList<SampleTypeDTO>();
	List<AssayListDataDTO> assayListData = new ArrayList<AssayListDataDTO>();
	List<TestOptionsDTO> testOptionlist = new ArrayList<TestOptionsDTO>();
	List<AssayInputDataValidationsDTO> inputDataValidationslist = new ArrayList<AssayInputDataValidationsDTO>();

	@BeforeTest
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		orderParentDTO = getOrderParentDTO();
		errors.add("error");
		accessioningIdList.add("accessioningIdList");
		listOrder.add(getOrder());
		assayInputDataValidationsDTO.add(getAssayInputDataValidationsDTO());
		assayDetails.put("inputDataValidationslist", assayInputDataValidationsDTO);
		Mockito.when(orderReadRepository.findAllByAccessioningId(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(listOrder);
		// Mockito.when(orderCrudRestApiImpl.totalMap).thenReturn(assayDetails);
	}

	/**
	 * We need a special {@link IObjectFactory}.
	 * 
	 * @return {@link PowerMockObjectFactory}.
	 */
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@Test
	public void validateOtherClinicianNameTest() {
		bulkOrderValidationUtil.validateOtherClinicianName(otherClinicianName, row, orderParentDTO, errors);
	}

	@Test
	public void validateAccessioningIdTest() {

		bulkOrderValidationUtil.validateAccessioningId(accessioningId, row, orderParentDTO, errors, accessioningIdList);
	}

	@Test
	public void validateAssayTypeTest() throws HMTPException {
		bulkOrderValidationUtil.validateAssayType(orderCrudRestApiImpl, assayType, row, orderParentDTO, errors);
	}

	@Test
	public void validateRetestsampleTest() throws HMTPException {
		bulkOrderValidationUtil.validateRetestsample(retestSample, row, orderParentDTO, errors);
	}

	@Test
	public void validateTestOptionsTest() throws HMTPException {
		bulkOrderValidationUtil.validateTestOptions(testOptions, testOptionsFetalSex, row, orderParentDTO, errors);
	}

	@Test
	public void validateOrderCommentsTest() throws HMTPException {
		bulkOrderValidationUtil.validateOrderComments(orderComments, row, orderParentDTO, errors);
	}

	@Test
	public void validateCollectionDateTest() {

		bulkOrderValidationUtil.validateCollectionDate(collectionDate, row, orderParentDTO, errors, "IST");
	}

	@Test
	public void validateReceivedDateTest() {
		bulkOrderValidationUtil.validateReceivedDate(receivedDate, row, orderParentDTO, errors, "IST");
	}

	@Test
	public void validateMaternalAgeSampleTypeTest() throws HMTPException {
		bulkOrderValidationUtil.validateMaternalAgeSampleType(orderCrudRestApiImpl,  maternalAge, row,
				orderParentDTO, errors);
	}

	@Test
	public void validateGestationalAgeTest() throws HMTPException {
		bulkOrderValidationUtil.validateGestationalAge(orderCrudRestApiImpl, gestationalAgeWeeks, row,
				orderParentDTO, errors);
	}

	@Test
	public void validateGestationalAgeDaysTest() throws HMTPException {
		bulkOrderValidationUtil.validateGestationalAgeDays(orderCrudRestApiImpl,  gestationalAgeDays, row,
				orderParentDTO, errors);
	}

	@Test
	public void validateEggDonorTest() throws HMTPException {
		bulkOrderValidationUtil.validateEggDonor(ivfStatus, eggDonor, eggDonorAge, maternalAge, row, orderParentDTO, errors);
	}

	@Test
	public void validateFetusNumberTest() throws HMTPException {
		bulkOrderValidationUtil.validateFetusNumber(orderCrudRestApiImpl, fetusNumber, row, orderParentDTO,
				errors);
	}

	@Test
	public void validatePatientMedicalRecNoTest() {
		bulkOrderValidationUtil.validatePatientMedicalRecNo(patientMedicalRecNo, row, orderParentDTO, errors);
	}

	@Test
	public void validatePatientDOBTest() {
		bulkOrderValidationUtil.validatePatientDOB(patientDOB, row, orderParentDTO, errors, "IST");
	}

	@Test
	public void isAccessioningDuplicateTest() {
		bulkOrderValidationUtil.isAccessioningDuplicate(accessioningId2, accessioningIdList);
	}

	@Test
	public void assayValidationsTest() throws HMTPException, UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(URLEncoder.class);
		String apiPath = "/json/rest/api/v1/assay/";
		String api = "pas.amm_api_url";
		// String urlassayType="url";
		String UTF = "UTF-8";
		// String urlSampleType ="urlSampleType";
		// String urlassayList="urlassayList";
		String encodeUrl = "url";
		Mockito.when(URLEncoder.encode(Mockito.anyString(), Mockito.anyString())).thenReturn(encodeUrl);
		Mockito.when(RestClientUtil.getUrlString(api, "", apiPath + "?assayType=" + assayType, "", null))
				.thenReturn(encodeUrl);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(encodeUrl, UTF), null)).thenReturn(orderClient);
		Mockito.when(orderClient.get(new GenericType<List<AssayTypeDTO>>() {
		})).thenReturn(assayTypelist);
		Mockito.when(RestClientUtil.getUrlString(api, "", apiPath + assayType + "/sampletype", "", null))
				.thenReturn(encodeUrl);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(encodeUrl, UTF), null)).thenReturn(orderClientSample);
		Mockito.when(orderClientSample.get(new GenericType<List<SampleTypeDTO>>() {
		})).thenReturn(sampleTypelist);
		Mockito.when(RestClientUtil.getUrlString(api, "", apiPath + assayType + "/listdata", "", null))
				.thenReturn(encodeUrl);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(encodeUrl, UTF), null))
				.thenReturn(orderClientAssayList);
		Mockito.when(orderClientAssayList.get(new GenericType<List<AssayListDataDTO>>() {
		})).thenReturn(assayListData);
		// String urltestOptions ="urltestOptions";
		Mockito.when(RestClientUtil.getUrlString(api, "", apiPath + assayType + "/testoptions", "", null))
				.thenReturn(encodeUrl);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(encodeUrl, UTF), null))
				.thenReturn(orderClienttestOptions);
		Mockito.when(orderClienttestOptions.get(new GenericType<List<TestOptionsDTO>>() {
		})).thenReturn(testOptionlist);
		// String urlinputDataValidations ="urlinputDataValidations";
		Mockito.when(RestClientUtil.getUrlString(api, "", apiPath + assayType + "/inputdatavalidations", "", null))
				.thenReturn(encodeUrl);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(encodeUrl, UTF), null))
				.thenReturn(orderClientinputDataValidations);
		Mockito.when(orderClientinputDataValidations.get(new GenericType<List<AssayInputDataValidationsDTO>>() {
		})).thenReturn(inputDataValidationslist);
		bulkOrderValidationUtil.assayValidations(orderCrudRestApiImpl, assayType);

	}

	/*@Test
	public void validateOtherClinicianFaxNoTest() {
		bulkOrderValidationUtil.validateOtherClinicianFaxNo(otherClinicianFaxNo, row, orderParentDTO, errors);
	}

	@Test
	public void validateRefClinicianFaxNoTest() {
		bulkOrderValidationUtil.validateRefClinicianFaxNo(refClinicianFaxNo, row, orderParentDTO, errors);
	}
	@Test
	public void validateTreatingDoctorNameTest() {
		bulkOrderValidationUtil.validateTreatingDoctorName(treatingDoctorName, row, orderParentDTO, errors);
	}

	@Test
	public void validateTreatingDoctorContactNoTest() {
		bulkOrderValidationUtil.validateTreatingDoctorContactNo(treatingDoctorContactNo, row, orderParentDTO, errors);
	}

	@Test
	public void validatePatientContactNoTest() {
		bulkOrderValidationUtil.validatePatientContactNo(patientContactNo, row, orderParentDTO, errors);
	}
	@Test
	public void validatePatientGenderTest() {
		bulkOrderValidationUtil.validatePatientGender(patientGender, row, orderParentDTO, errors);
	}
*/
	@Test
	public void validateRefClinicianClinicNameTest() {
		bulkOrderValidationUtil.validateRefClinicianClinicName(refClinicianClinicName, row, orderParentDTO, errors);
	}

	
	@Test
	public void validateRefClinicianNameTest() {
		bulkOrderValidationUtil.validateRefClinicianName(refClinicianName, row, orderParentDTO, errors);
	}

	@Test
	public void validatePatientFirstNameTest() {
		bulkOrderValidationUtil.validatePatientFirstName(patientFirstName, row, orderParentDTO, errors);
	}

	@Test
	public void validatePatientLastNameTest() {
		bulkOrderValidationUtil.validatePatientLastName(patientLastName, row, orderParentDTO, errors);
	}

	/*
	 * @Test public boolean isValidDateTest() {
	 * bulkOrderValidationUtil.isValidDate(collectionDate, receivedDate); return
	 * false; }
	 */

	public OrderParentDTO getOrderParentDTO() {
		OrderParentDTO orderParentDTO = new OrderParentDTO();
		orderParentDTO.setOrder(getOrderDTO());
		return orderParentDTO;
	}

	public OrderDTO getOrderDTO() {
		OrderDTO order = new OrderDTO();
		order.setAccessioningId("12345");
		order.setActiveFlag("true");
		order.setAssay(getAssayDTO());
		order.setAssayType("NIPTHTP");
		order.setPatient(getPatientDTO());
		return order;
	}

	public AssayDTO getAssayDTO() {
		AssayDTO assay = new AssayDTO();
		assay.setEggDonor("30");
		return assay;
	}

	public PatientDTO getPatientDTO() {
		PatientDTO patient = new PatientDTO();
		patient.setOtherClinicianName("otherRMMClinic");
		return patient;
	}

	public Order getOrder() {
		Order order = new Order();
		order.setAccessioningId("12345");
		return order;

	}

	public AssayInputDataValidationsDTO getAssayInputDataValidationsDTO() {
		AssayInputDataValidationsDTO assayInputDataValidationsDTO = new AssayInputDataValidationsDTO();
		assayInputDataValidationsDTO.setAssayType("NIPTDPCR");
		assayInputDataValidationsDTO.setFieldName("Maternal Age");
		assayInputDataValidationsDTO.setMaxVal(24L);
		assayInputDataValidationsDTO.setMinVal(20L);
		return assayInputDataValidationsDTO;
	}
}
