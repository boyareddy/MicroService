package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class ContainerSamplesStatusTest {

	@Mock
	ContainerSamplesReadRepository containerSamplesReadRepository;

	@Spy
	OrderService orderServiceImpl = org.mockito.Mockito.spy(new OrderServiceImpl());

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	
	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();

	
	String containerID = null;
	Long domainId = 0L;
	String deviceRunId = null;
	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;

	List<Object> listContainerSamples = null;
	List<ContainerSamples> containerSampleList = new ArrayList<>();
	List<ContainerSamples> containerSampleListFirst = new ArrayList<>();
	List<Object> containerSamplesDTOList = new ArrayList<Object>();

	@BeforeTest
	public void setUp() {
		expectedCorrectStatusCode = 200;
		expectedIncorrectStatusCode = 204;

		deviceRunId = "RND1345";
		containerSampleList.add(getContainerSamples());

		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

	}

	@Test(priority = 1)
	public void validateContainerSamplesStatusNegTest() throws HMTPException {
		Mockito.when(containerSamplesReadRepository.findAllContainerSamplesByDeviceRunID((Mockito.anyString()),
				any(Long.class))).thenReturn(containerSamplesDTOList);
		Response response = orderCrudRestApiImpl.validateContainerSamplesStatus(deviceRunId);
		assertEquals(response.getStatus(), expectedIncorrectStatusCode);
	}

	@Test(priority = 2)
	public void validateContainerSamplesStatusPosTest() throws HMTPException {
		Mockito.when(containerSamplesReadRepository.findAllContainerSamplesByDeviceRun(Mockito.anyString(),
				Mockito.anyLong())).thenReturn(containerSampleList);
		Response response = orderCrudRestApiImpl.validateContainerSamplesStatus(deviceRunId);
		assertEquals(response.getStatus(), expectedCorrectStatusCode);
	}
	
	@Test(priority = 3)
	public void validateContainerSamplesStatusFirstTest() throws HMTPException {
		ContainerSamples containerSampless = new ContainerSamples();
		containerSampless.setAccessioningID("123");
		containerSampless.setStatus("open");
		
		containerSampleListFirst.add(containerSampless);
		Mockito.when(containerSamplesReadRepository.findAllContainerSamplesByDeviceRun(Mockito.anyString(),
				Mockito.anyLong())).thenReturn(containerSampleListFirst);
		Response response = orderCrudRestApiImpl.validateContainerSamplesStatus(deviceRunId);
		assertEquals(response.getStatus(), expectedIncorrectStatusCode);
	}
	
	@Test(expectedExceptions = Exception.class,priority = 4)
	public void validateContainerSamplesStatusNoDeviceIdTest() throws HMTPException {
		orderCrudRestApiImpl.validateContainerSamplesStatus(null);
		
	}
	
	private ContainerSamples getContainerSamples() {
		ContainerSamples containerSampless = new ContainerSamples();
		containerSampless.setAccessioningID("123");
		containerSampless.setStatus("senttodevice");
		containerSampless.setDeviceRunID(deviceRunId);
		
		return containerSampless;
	}

	@AfterTest
	public void afterTest() {
		domainId = 0L;
		expectedCorrectStatusCode = 0;
		expectedIncorrectStatusCode = 0;
	}

}
