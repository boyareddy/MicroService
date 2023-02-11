package com.roche.connect.omm.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.MandatoryFlagValidationUtil;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, MandatoryFlagValidationUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class GetRequiredFieldMissingFlagTest {

	@InjectMocks
	OrderServiceImpl orderServiceImpl;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	MandatoryFlagValidationUtil validations;

	List<AssayInputDataValidationsDTO> assayMandatoryFields = new ArrayList<>();
	Order order = null;
	Long domainId = 0L;
	boolean flag = false;
	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;

	@BeforeTest
	public void before() {
		expectedCorrectStatusCode = 200;
		expectedIncorrectStatusCode = 400;
		MockitoAnnotations.initMocks(this);
		assayMandatoryFields.add(getAssayInputDataValidationsDTOFields());
		//assayMandatoryFields.add(getAssayInputDataValidationsDTOMaternalAge());

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
	public void getRequiredFieldMissingFlag() throws HMTPException {
		PowerMockito.mockStatic(MandatoryFlagValidationUtil.class);
		Mockito.when(MandatoryFlagValidationUtil.getInstance()).thenReturn(validations);
		Mockito.when(validations.get(Mockito.anyString())).thenReturn(assayMandatoryFields);
		orderServiceImpl.getRequiredFieldMissingFlag(getOrderParentDTO());
	}

	public OrderParentDTO getOrderParentDTO() {
		OrderParentDTO orderParentDTO = new OrderParentDTO();
		orderParentDTO.setOrder(getOrderDTO());
		
		return orderParentDTO;
	}

	public OrderDTO getOrderDTO() {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setAssayType("NIPTHTP");
		orderDTO.setAssay(getAssayDTO());
		orderDTO.setOrderComments("Comments");
		orderDTO.setPatient(getPatientDTO());
		return orderDTO;
	}

	public AssayDTO getAssayDTO() {
		AssayDTO assay = new AssayDTO();
		assay.setMaternalAge(23);
		assay.setIvfStatus("IVF status");
		assay.setFetusNumber("2");
		return assay;
	}
	
	public PatientDTO getPatientDTO(){
		PatientDTO patientDTO = new PatientDTO();
		patientDTO.setPatientFirstName("John");
		patientDTO.setPatientLastName("Micheal");
		patientDTO.setPatientMedicalRecNo("1");
		patientDTO.setPatientDOB("12/03/2000");
		patientDTO.setRefClinicianName("Roche");
		patientDTO.setLabId("1");
		patientDTO.setClinicName("Roche");
		patientDTO.setOtherClinicianName("Roche");
		patientDTO.setReasonForReferral("referal");
		patientDTO.setAccountNumber("23456789");
		return patientDTO;
	}
	

	public AssayInputDataValidationsDTO getAssayInputDataValidationsDTOFields() {
		AssayInputDataValidationsDTO assayInputDataValidationsDTO = new AssayInputDataValidationsDTO();
		assayInputDataValidationsDTO.setFieldName("Maternal age");
		assayInputDataValidationsDTO.setFieldName("IVF status");
		assayInputDataValidationsDTO.setFieldName("Gestational age");
		assayInputDataValidationsDTO.setFieldName("Number of fetus");
		assayInputDataValidationsDTO.setFieldName("First name");
		assayInputDataValidationsDTO.setFieldName("Last name");
		assayInputDataValidationsDTO.setFieldName("Medical record number");
		assayInputDataValidationsDTO.setFieldName("DOB");
		assayInputDataValidationsDTO.setFieldName("Referring clinician");
		assayInputDataValidationsDTO.setFieldName("Laboratory ID");
		assayInputDataValidationsDTO.setFieldName("Other clinician");
		assayInputDataValidationsDTO.setFieldName("Clinic name"); 
		assayInputDataValidationsDTO.setFieldName("Reason for Referral");
		assayInputDataValidationsDTO.setFieldName("Account #");
		assayInputDataValidationsDTO.setFieldName("mandatory flag");
	
		
		
		assayInputDataValidationsDTO.setAssayType("NIPTHTP");
		assayInputDataValidationsDTO.setIsMandatory("true");
		return assayInputDataValidationsDTO;
	}
	/*String[] row ={"1","2","3",null}; 
	Map<Integer, String> accessioningIdProcessed = new HashMap<>();
	Map<Integer, String> containerIdPositionProcessed= new HashMap<>();
	
	@Test
	public void checkFieldsForCSVStreamTest(){
		orderServiceImpl.checkFieldsForCSVStream(1, row, accessioningIdProcessed, containerIdPositionProcessed);
	}*/


	@AfterTest
	public void afterTest() {
		// orderaccessioningId=null;
		domainId = 0L;
		expectedCorrectStatusCode = 0;
		expectedIncorrectStatusCode = 0;
	}

}
