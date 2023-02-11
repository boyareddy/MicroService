package com.roche.connect.omm.test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.writerepository.ContainerSamplesWriteRepository;

@PrepareForTest({ HMTPLoggerImpl.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class RemoveContainerSamplesTest {

	@Mock
	ContainerSamplesReadRepository containerSamplesReadRepository = Mockito.mock(ContainerSamplesReadRepository.class);

	@Mock
	ContainerSamplesWriteRepository containerSamplesWriteRepository = Mockito
			.mock(ContainerSamplesWriteRepository.class);

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl = Mockito.mock(HMTPLoggerImpl.class);

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();

	List<ContainerSamples> containers = new ArrayList<ContainerSamples>();

	private ContainerSamples getContainerSamples() {
		ContainerSamples containerSamples = new ContainerSamples();
		containerSamples.setAccessioningID("404040");
		containerSamples.setActiveFlag("Y");
		containerSamples.setAssayType("NIPTDPCR");
		containerSamples.setContainerType("96 wellplate");
		return containerSamples;
	}
	
	@BeforeTest
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(containerSamplesReadRepository.findByIdAndContainerIDAndStatus(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyLong())).thenReturn(containers);
		Mockito.when(containerSamplesWriteRepository.deleteByContainerID(Mockito.anyString())).thenReturn(1L);

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

	@Test(expectedExceptions = Exception.class,priority=2)
	public void removeContainerSamplesNegTest() throws HMTPException {
		containers.add(getContainerSamples());
		orderCrudRestApiImpl.removeContainerSamples("WP 100");
	}
	
	@Test(priority=1)
	public void removeContainerSamplesPosTest() throws HMTPException {
		Response expecedResponse =orderCrudRestApiImpl.removeContainerSamples("WP 100");
		assertEquals(expecedResponse.getStatus(), 200);
	}

	@AfterTest
	public void afterTest() {

	}

	

}
