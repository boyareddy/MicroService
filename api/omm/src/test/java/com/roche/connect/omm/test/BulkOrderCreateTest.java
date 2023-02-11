package com.roche.connect.omm.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.BulkOrdersDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.util.BulkOrderValidationUtil;
import com.roche.connect.omm.writerepository.OrderWriteRepository;
import com.roche.connect.omm.writerepository.PatientAssayWriteRepository;
import com.roche.connect.omm.writerepository.PatientSampleWriteRepository;
import com.roche.connect.omm.writerepository.PatientWriteRepository;
import com.roche.connect.omm.writerepository.TestOptionsWriteRepository;

public class BulkOrderCreateTest {

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl;
	
	@Mock
	BulkOrderValidationUtil bulkOrderValidationUtil;
	
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	Patient patient;
	@Mock
	PatientWriteRepository patientWriteRepository;
	@Mock
	OrderService orderService;
	@Mock
	PatientAssay patientAssay;
	// @Mock OrderParentDTO orderParentDTO;
	List<PatientSamples> patientSamples = new ArrayList<PatientSamples>();
	@Mock
	PatientSampleWriteRepository patientSampleWriteRepository;
	@Mock
	OrderWriteRepository orderWriteRepository;
	@Mock
	TestOptionsWriteRepository testOptionsWriteRepository;
	@Mock
	PatientAssayWriteRepository patientAssayWriteRepository;
	@Mock
	File file;
	
	List<TestOptions> testOptions = new ArrayList<>();
	List<PatientAssay> patientAssays = new ArrayList<>();

	@BeforeTest public void setUp() throws ParseException, HMTPException {
		 MockitoAnnotations.initMocks(this);
		 patientSamples.add(getPatientSamples());
		 Mockito.when(patientWriteRepository.save(orderService.createPatientObject(Mockito.any(OrderParentDTO.class)))).thenReturn(patient);
		 Mockito.when(orderService.createPatientAssayObject(Mockito.any(OrderParentDTO.class))).thenReturn(getPatientAssay());
		 Mockito.when(orderService.createPatientSamplesObject(Mockito.any(OrderParentDTO.class))).thenReturn(patientSamples);
		 Mockito.when(patientSampleWriteRepository.save(patientSamples)).thenReturn(patientSamples);
		 Mockito.when(orderService.createOrderObject(Mockito.any(OrderParentDTO.class))).thenReturn(getOrder());
		 Mockito.when(orderWriteRepository.save(Mockito.any(Order.class))).thenReturn(getOrder());
		 Mockito.when(orderService.createTestOptionsObject(Mockito.any(OrderParentDTO.class))).thenReturn(testOptions);
		 Mockito.when(testOptionsWriteRepository.save(Mockito.anyList())).thenReturn(testOptions);
		 Mockito.when(patientAssayWriteRepository.save(Mockito.anyList())).thenReturn(patientAssays);
		 ReflectionTestUtils.setField(orderCrudRestApiImpl, "uploadedBulkOrderLocation", "");
		 ReflectionTestUtils.setField(orderCrudRestApiImpl, "fileMemorySize", 1L);
		 ReflectionTestUtils.setField(orderCrudRestApiImpl, "ordersThresholdSize", 500L);
	 }

	public Order getOrder() {
		Order order = new Order();
		return order;

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

	public PatientAssay getPatientAssay() {
		PatientAssay patientAssay = new PatientAssay();
		return patientAssay;
	}

	public PatientSamples getPatientSamples() {
		PatientSamples patientSamples = new PatientSamples();
		patientSamples.setPatient(patient);
		return patientSamples;
	}

	@Test
	public void createBulkOrdersTest() throws HMTPException {
		orderCrudRestApiImpl.createBulkOrders(getBulkOrdersDTO());
	}

	@Test
	public void validateBulkOrdersTest() throws HMTPException, FileNotFoundException {
		//BulkOrderValidationUtil bulkOrderValidationUtil = Mockito.spy(BulkOrderValidationUtil.class);
		Mockito.doNothing().when(bulkOrderValidationUtil).validateAccessioningId(Mockito.anyString(), Mockito.anyInt(), Mockito.any(OrderParentDTO.class), Mockito.anyList(), Mockito.anyList());
		orderCrudRestApiImpl.validateBulkOrders(getInputStream(), "IST");
	}
	
	

	public InputStream getInputStream() throws FileNotFoundException {
		InputStream inputStream = new FileInputStream("src/test/java/resource/Bulk_upload_order_entry_NIPT_dPCR.csv");
		return inputStream;
	}
	
	
	public BulkOrdersDTO getBulkOrdersDTO() {
		BulkOrdersDTO bulkOrdersDTO = new BulkOrdersDTO();

		FileReader fr;
		try {
			fr = new FileReader(new File("src/test/java/resource/createbulkorder.json"));
			ObjectMapper objectMapper = new ObjectMapper();
			bulkOrdersDTO = objectMapper.readValue(fr, BulkOrdersDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bulkOrdersDTO;
	}
}
