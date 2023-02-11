package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.CSVParserUtil;
import com.roche.connect.omm.util.JsonFileReaderAsString;
import com.roche.connect.omm.util.ResourceBundleUtil;
import com.roche.connect.omm.writerepository.ContainerSamplesWriteRepository;

/**
 * @author imtiyazm This class is for mock test to store container samples
 */

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class StoreAndValidateContainerSampleTest {

	@Mock
	ContainerSamplesWriteRepository containerSamplesWriteRepository = org.mockito.Mockito
			.mock(ContainerSamplesWriteRepository.class);

	@Mock
	ContainerSamplesReadRepository containerSamplesReadRepository = org.mockito.Mockito
			.mock(ContainerSamplesReadRepository.class);

	@Mock
	OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);
	
	@Mock
	ResourceBundleUtil resourceBundleUtil;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	CSVParserUtil csvParserUtil = org.mockito.Mockito.mock(CSVParserUtil.class);

	@InjectMocks
	@Spy
	OrderService orderServiceImpl = org.mockito.Mockito.spy(new OrderServiceImpl());

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();

	int expectedCorrectStatusCode = 0;
	int expectedIncorrectCorrectStatusCode = 0;
	String jsonFileString = null;
    String jsonFileStringWrong = null;
	
	Map<String, Object> mapObject = null;
	int expectedIncorrectStatusCode = 0;
	List<Map<String, Object>> loadIds = null;
	String csvJsonFile = null;
	String csvJsonFileWrong = null;

	List<Map<Object, Object>> containerSample = null;
	Order order = null;
	List<Order> orderaccessioningId = null;
	List<Order> orderList = null;
	String deviceRunId = null;
	String accessioningID = null;
	String status = null;
	Company company = null;
	ContainerSamples containerSamples = null;
	List<ContainerSamples> containerSampleListToUpdateStatus = null;
	List<ContainerSamples> containerSampleList = new ArrayList<>();
	long ownerId = 0L;
	List<ContainerSamplesDTO> containerSamplesDTOList = null;
	ContainerSamplesDTO containerSamplesDTO = null;

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@SuppressWarnings("deprecation")
	@BeforeTest
	public void setUp() throws Exception, JsonMappingException {
		csvJsonFile = "src/test/java/Resource/StoreContainerSamples.json";
		jsonFileString = JsonFileReaderAsString.getJsonfromFile(csvJsonFile);
		
		csvJsonFileWrong = "src/test/java/Resource/StoreContainerSamplesNeg.json";
        jsonFileStringWrong = JsonFileReaderAsString.getJsonfromFile(csvJsonFileWrong);
        
		loadIds = new ArrayList<>();
		mapObject = new HashMap<>();
		mapObject.put("loadID", 1L);
		mapObject.put("containerID", "WP 810");
		loadIds.add(mapObject);

		containerSample = new ArrayList<>();
		order = new Order();
		order.setAccessioningId("300001");
		order.setAssayType("NIPTDPCR");
		orderaccessioningId = new ArrayList<>();
		orderaccessioningId.add(order);
		orderList = new ArrayList<>();
		orderList.add(order);

		// updateContainerSamples
		containerSamples = new ContainerSamples();
		containerSamples.setId(10);
		containerSamples.setAccessioningID("4444");
		containerSamples.setActiveFlag("Y");
		containerSamples.setAssayType("NIPTDPCR");
		containerSamples.setContainerID("WP 810");
		containerSamples.setContainerType("96 wellplate");
		containerSamples.setCreatedBy("admin");
		containerSamples.setCreatedDateTime(new Timestamp(new Date().getTime()));
		containerSamples.setDeviceID("D01");
		containerSamples.setDeviceRunID("DRun01");
		containerSamples.setLoadID(1L);
		containerSamples.setPosition("C9");
		containerSamples.setStatus("senttodevice");
		containerSamples.setUpdatedBy("admin");
		containerSamples.setUpdatedDateTime(new Timestamp(new Date().getTime()));
		company = new Company();
		company.setId(-1);
		containerSamples.setCompany(company);
		containerSampleListToUpdateStatus = new ArrayList<>();
		containerSampleListToUpdateStatus.add(containerSamples);

		containerSamplesDTO = new ContainerSamplesDTO();
		containerSamplesDTOList = new ArrayList<>();
		containerSamplesDTO.setContainerSampleId(Long.parseLong("1234"));
		containerSamplesDTOList.add(containerSamplesDTO);

		expectedCorrectStatusCode = 200;
		expectedIncorrectStatusCode = 400;
		ownerId = 1l;

		MockitoAnnotations.initMocks(this);
		// Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString());
		Mockito.when(containerSamplesReadRepository.findHighestLoadIdByContainer(anyLong())).thenReturn(loadIds);
		Mockito.when(containerSamplesWriteRepository.save(containerSampleList)).thenReturn(containerSampleList);
		Mockito.when(containerSamplesReadRepository.findSampleByAccessioningId(any(String.class), any(long.class)))
				.thenReturn(containerSample);
		Mockito.when(orderReadRepository.findAllByAccessioningId(any(String.class), any(long.class)))
				.thenReturn(orderaccessioningId);
		Mockito.when(orderReadRepository.findAllByAccessioningId(any(String.class), any(long.class)))
				.thenReturn(orderaccessioningId);

		// updateContainerSample method
		Mockito.when(containerSamplesReadRepository.findContainerSamplesByAccessioningId(any(String.class)))
				.thenReturn(containerSampleListToUpdateStatus);
		Mockito.when(containerSamplesWriteRepository.save(any(ContainerSamples.class), any(Long.class)))
				.thenReturn(containerSamples);
		Mockito.when(
				containerSamplesReadRepository.findAllContainerSamplesByDeviceRun(any(String.class), any(Long.class)))
				.thenReturn(containerSampleListToUpdateStatus);
		Mockito.when(containerSamplesWriteRepository.save(containerSampleListToUpdateStatus, ownerId))
				.thenReturn(containerSampleListToUpdateStatus);
		Mockito.when(containerSamplesReadRepository.findOne(containerSamplesDTO.getContainerSampleId()))
				.thenReturn(containerSamples);

	}

	// StoreContainerSamples Pos and Neg
	@Test(priority = 1)
	public void storeContainerSamplesPosTest() throws Exception {
		Response actualResponseCode = orderCrudRestApiImpl.storeContainerSamples(jsonFileString);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}
	@Test(expectedExceptions=HMTPException.class,priority = 2)
    public void storeContainerSamplesIsCSVJsonValidTest() throws Exception {
       orderCrudRestApiImpl.storeContainerSamples(jsonFileStringWrong); 
    }
   // UpdateContainerSamples Pos and Neg
	@Test(priority = 3)
	public void updateContainerSamplesPosTest() throws Exception {
		// deviceRunId="D001";
		accessioningID = "12345";
		status = "processed";
		Response actualResponseCode = orderCrudRestApiImpl.updateContainerSamples(jsonFileString, deviceRunId,
				accessioningID, status);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}

	@Test(priority = 4)
	public void updateContainerSamplesRequestBodyEmptyPosTest() throws Exception {
		deviceRunId = "D001";
		accessioningID = null;
		jsonFileString = "";
		Response actualResponseCode = orderCrudRestApiImpl.updateContainerSamples(jsonFileString, deviceRunId,
				accessioningID, status);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}

	@Test(priority = 5)
	public void updateContainerSamplesDeviceRunIdNullPosTest() throws Exception {
		deviceRunId = null;
		accessioningID = null;
		jsonFileString = JsonFileReaderAsString.getJsonfromFile(csvJsonFile);
		Response actualResponseCode = orderCrudRestApiImpl.updateContainerSamples(jsonFileString, deviceRunId,
				accessioningID, status);
		assertEquals(actualResponseCode.getStatus(), expectedCorrectStatusCode);
	}

	@Test(expectedExceptions = { NullPointerException.class, HMTPException.class }, priority = 6)
	public void updateContainerSamplesDeviceRunIdNullNegTest() throws Exception {
		deviceRunId = null;
		accessioningID = null;
		jsonFileString = null;
		orderCrudRestApiImpl.updateContainerSamples(jsonFileString, deviceRunId, accessioningID, status);

	}

	// ValidateContainerSample Test
	@Test(priority = 7)
	public void validateContainerSamplesPosTest() throws IOException, HMTPException {
		FileInputStream fin = new FileInputStream(new File(csvJsonFile));
		Response actualResponseCode = orderCrudRestApiImpl.validateContainerSamples(fin);
		assertEquals(actualResponseCode.getStatus(), expectedIncorrectStatusCode);
	}
	  @Test(expectedExceptions=HMTPException.class,priority = 8)
	    public void updateContaieRunIdCompanyTest() throws HMTPException {
	        deviceRunId = null;
	        accessioningID = null;
	        company = new Company();
	        company.setId(-12);
	        containerSamples.setCompany(company);
	         orderCrudRestApiImpl.updateContainerSamples(jsonFileString, deviceRunId,
	                accessioningID, status);
	    }
	  
	  @Test(expectedExceptions=HMTPException.class,priority = 9)
	    public void updateContainerSamplesDeviceRunIdCompanyTest() throws HMTPException {
	        deviceRunId = "D001";
	        accessioningID = null;
	        jsonFileString = "";
	        company = new Company();
            company.setId(-12);
            containerSamples.setCompany(company);
            orderCrudRestApiImpl.updateContainerSamples(jsonFileString, deviceRunId,
	                accessioningID, status);
	    }
	  
      @Test(expectedExceptions=HMTPException.class,priority = 10)
	    public void updateContainerSamplesCompanyTest() throws Exception {
	        deviceRunId = null;
	        accessioningID = null;
	        company = new Company();
            company.setId(-12);
            containerSamples.setCompany(company);
	        jsonFileString = JsonFileReaderAsString.getJsonfromFile(csvJsonFile);
	        orderCrudRestApiImpl.updateContainerSamples(jsonFileString, deviceRunId,
	                accessioningID, status);
	   	    }
      @Test(priority = 11)
      public void CSVJsonValidisOrderPresentTest() throws Exception {
         List<Order> orderList = new ArrayList<>();
          containerSamplesDTO = new ContainerSamplesDTO();
          containerSamplesDTO.setAccessioningID("123");
          containerSamplesDTO.setAssayType("NIPTDPCR");
          containerSamplesDTOList = new ArrayList<>();
          containerSamplesDTO.setContainerSampleId(Long.parseLong("1234"));
          containerSamplesDTOList.add(containerSamplesDTO);
          Mockito.when(orderReadRepository.findAllByAccessioningId(any(String.class), any(long.class))).thenReturn(orderList);
          orderServiceImpl.isCSVJsonValid(containerSamplesDTOList); 
      }  
      @Test(priority = 12)
      public void CSVJsonValidisAssayTypeValidTest() throws Exception {
         List<Order> orderList = new ArrayList<>();
          Order order=new Order();
          order.setAssayType("nipthtp");
          order.setAccessioningId("12");
          orderList.add(order); 
          containerSamplesDTO = new ContainerSamplesDTO();
          containerSamplesDTO.setAccessioningID("123");
          containerSamplesDTO.setAssayType("NIPTDPC");
          containerSamplesDTOList = new ArrayList<>();
          containerSamplesDTO.setContainerSampleId(Long.parseLong("1234"));
          containerSamplesDTOList.add(containerSamplesDTO);
          Mockito.when(orderReadRepository.findAllByAccessioningId(any(String.class), any(long.class))).thenReturn(orderList);
          orderServiceImpl.isCSVJsonValid(containerSamplesDTOList); 
      }
      @Test(priority = 13)
      public void CSVJsonValidisAccessioningDuplicateTest() throws Exception {
         List<Order> orderList = new ArrayList<>();
          Order order=new Order();
          order.setAssayType("nipthtp");
          order.setAccessioningId("12");
          orderList.add(order); 
          containerSamplesDTO = new ContainerSamplesDTO();
          containerSamplesDTO.setAccessioningID("123");
          containerSamplesDTO.setAssayType("NIPTDPC");
          containerSamplesDTOList = new ArrayList<>();
          containerSamplesDTO.setContainerSampleId(Long.parseLong("1234"));
          containerSamplesDTOList.add(containerSamplesDTO);
          containerSample = new ArrayList<>();
          Map<Object, Object> map=new HashMap<>();
          map.put("containerId", "WPI0");
          containerSample.add(map);
          Mockito.when(containerSamplesReadRepository.findSampleByAccessioningId(any(String.class), any(long.class))).thenReturn(containerSample);
          orderServiceImpl.isCSVJsonValid(containerSamplesDTOList); 
      }
      @Test(expectedExceptions=HMTPException.class,priority = 14)
      public void CSVJsonValidisContaineridAndPositionDuplicateTest() throws Exception {      
           containerSample = new ArrayList<>();
           Map<Object, Object> map=new HashMap<>();
           map.put("containerId", "WPI0");
           containerSample.add(map);
           Mockito.when(containerSamplesReadRepository.findSampleByContaineridAndPosition(any(String.class), any(String.class), any(long.class))).thenReturn(containerSample,containerSample);
           orderCrudRestApiImpl.storeContainerSamples(jsonFileStringWrong); 
       }   
	@AfterTest
	public void afterTest() {
		expectedCorrectStatusCode = 0;
		jsonFileString = null;
	}

}
