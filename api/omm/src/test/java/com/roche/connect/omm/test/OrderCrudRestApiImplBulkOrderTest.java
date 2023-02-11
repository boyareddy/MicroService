package com.roche.connect.omm.test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.BulkOrdersDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.writerepository.OrderWriteRepository;
import com.roche.connect.omm.writerepository.PatientAssayWriteRepository;
import com.roche.connect.omm.writerepository.PatientSampleWriteRepository;
import com.roche.connect.omm.writerepository.PatientWriteRepository;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class OrderCrudRestApiImplBulkOrderTest {

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	InputStream inputStream;
	@Mock
	PatientSampleWriteRepository patientSampleWriteRepository;
	@Mock
	PatientAssayWriteRepository patientAssayWriteRepository;
	@Mock
	OrderWriteRepository orderWriteRepository;
	@Mock
	PatientWriteRepository patientWriteRepository;
	
	List<PatientSamples> patientSamplesList = new ArrayList<>();
	List<Order> orders = new ArrayList<>();
	List<PatientAssay> patientAssays = new ArrayList<>();
	Patient patient = new Patient();
	@Mock File file ;
	@Mock OrderService orderService;
	@BeforeTest
	    public void setUp() throws Exception {
	        MockitoAnnotations.initMocks(this);
	        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
			Mockito.when(patientWriteRepository.save(patient)).thenReturn(patient);
			Mockito.when(patientSampleWriteRepository.save(patientSamplesList)).thenReturn(patientSamplesList);
			Mockito.when(patientAssayWriteRepository.save(patientAssays)).thenReturn(patientAssays);
			Mockito.when(orderWriteRepository.save(orders)).thenReturn(orders);
			Mockito.when(file.getName()).thenReturn("fileName");
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

	/*
	 * @Test public void validateBulkOrdersTest() throws HMTPException {
	 * orderCrudRestApiImpl.validateBulkOrders(inputStream); }
	 */

	/*@Test
	public void createBulkOrdersTest() throws HMTPException {
		orderCrudRestApiImpl.createBulkOrders(getBulkOrdersDTO());
	}*/
	

	public BulkOrdersDTO getBulkOrdersDTO() {
		BulkOrdersDTO bulkOrdersDTO = new BulkOrdersDTO();
		List<OrderParentDTO> orderParentDTOList = new ArrayList<>();
		OrderParentDTO orderParentDTO = new OrderParentDTO();
		OrderDTO order1 = new OrderDTO();
		order1.setAccessioningId("");
		order1.setActiveFlag("True");
		AssayDTO assay = new AssayDTO();
		assay.setEggDonor("egg donar");
		assay.setGestationalAgeDays(2);
		assay.setIvfStatus("ivf status");
		order1.setAssay(assay);
		order1.setAssayType("NIPT HTP");
		order1.setCreatedBy("admin");
		// order1.setCreatedDateTime(createdDateTime);
		order1.setOrderComments("order comments");
		order1.setOrderId(323434L);
		PatientDTO patient = new PatientDTO();
		patient.setPatientDOB("21/01/1990");
		order1.setPatient(patient);
		order1.setSampleType("Blood");
		order1.setUpdatedBy("");
		orderParentDTO.setOrder(order1);
		orderParentDTOList.add(orderParentDTO);
		bulkOrdersDTO.setOrderParentDTO(orderParentDTOList);
		return bulkOrdersDTO;
	}
}
