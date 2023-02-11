package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
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
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;

/**
 * 
 * @author imtiyazm,This class is used to test for fetching all container samples
 *
 */
@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class GetContainerSampleListTest {
	
	@Mock
	ContainerSamplesReadRepository containerSamplesReadRepository = org.mockito.Mockito.mock(ContainerSamplesReadRepository.class);
	
	@Spy
	OrderService orderServiceImpl = org.mockito.Mockito.spy(new OrderServiceImpl());
	
	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();
	
	@Mock HMTPLoggerImpl hmtpLoggerImpl;
	
	String containerID = null;
	Long domainId=0L;
	String deviceRunId = null;
	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;

	ContainerSamples containerSamples = null;
	List<Object> listContainerSamples=null;
	Map<String, Object> containerMap = null;
	List<Map<String, Object>> containers = null;
	List<ContainerSamplesDTO> containerSamplesDTO = null;
	
	
	
	@BeforeTest
	public void setUp() {
		expectedCorrectStatusCode = 200;
		expectedIncorrectStatusCode = 400;
		
		containerID=null;
		deviceRunId=null;
		domainId=1L;
		
		
		
		
		listContainerSamples=new ArrayList<>();
		Object[] containerSamples=new Object[20];
		containerSamples[0]="123123123";
		containerSamples[1]="NIPTDPCR";
		containerSamples[2]=null;
		containerSamples[3]="WP 676";
		containerSamples[4]="1";
		containerSamples[5]="sendtodevice";
		containerSamples[6]="A1";
		containerSamples[7]="RND3243";
		containerSamples[8]="12121";
		containerSamples[9]="comments";
		containerSamples[10]="630";
		containerSamples[11]="Y";
		containerSamples[12]="96 wellplate";
		containerSamples[13]="admin";
		containerSamples[14]="2018-10-02 18:48:05.123";
		containerSamples[15]="admin";
		containerSamples[16]="2018-10-02 18:48:05.123";
		listContainerSamples.add(containerSamples);
		
		containerMap = new HashMap<>();
		containers = new ArrayList<>();
		
		containerMap.put("accessioningID", "123123123");
		containerMap.put("position", "A12");
		containerMap.put("assayType", "NIPTDPCR");
		containerMap.put("containerID", "ABCDEF");
		containerMap.put("containerType", "96 well plate");
		containers.add(containerMap);
		
				
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		Mockito.when( containerSamplesReadRepository.findAllContainerSamplesByDeviceRunID(any(String.class),any(long.class))).thenReturn(listContainerSamples);
		Mockito.when( containerSamplesReadRepository.findconatinersetsamplebyconatinerid(any(String.class),any(long.class))).thenReturn(listContainerSamples);
		Mockito.when( containerSamplesReadRepository.findContainerIdByStatus(any(long.class))).thenReturn(containers);
		Mockito.when( containerSamplesReadRepository.findAllContainerSamples(any(long.class),any(String.class))).thenReturn(listContainerSamples);
		//Mockito.when( orderServiceImpl.containerSamplesMapperForObjTODTO(listContainerSamples).thenReturn(listContainerSamples);
		
	}

	@Test
	public void findContainerIdByStatusPosTest() throws HMTPException {
		containerID="ABCDEF";
		deviceRunId = "MP96";
		Response response = orderCrudRestApiImpl.getContainerSamples(deviceRunId,containerID);
		assertEquals(response.getStatus(), expectedCorrectStatusCode);
	}
	@Test
	public void findConatinersetSampleByConatinerIdPosTest() throws HMTPException {
		containerID="ABCDEF";
		deviceRunId=null;
		Response response = orderCrudRestApiImpl.getContainerSamples(deviceRunId,containerID);
		assertEquals(response.getStatus(), expectedCorrectStatusCode);
	}
	
	@Test
	public void findAllContainerSamplesByDeviceRunIDPosTest() throws HMTPException {
		containerID=null;
		deviceRunId="MP96";
		Response response = orderCrudRestApiImpl.getContainerSamples(deviceRunId,containerID);
		assertEquals(response.getStatus(), expectedCorrectStatusCode);
	}

	@AfterTest
	public void afterTest() {
		containerID=null;
		domainId=0L; 
		expectedCorrectStatusCode = 0;
		expectedIncorrectStatusCode = 0;
	}

}
