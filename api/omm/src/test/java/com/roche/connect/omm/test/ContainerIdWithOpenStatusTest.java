package com.roche.connect.omm.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.omm.ApplicationBoot;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;

import antlr.collections.List;

@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class ContainerIdWithOpenStatusTest  {
	@Mock
	ContainerSamplesReadRepository containerSamplesReadRepository;
	
	@Mock HMTPLoggerImpl hmtpLoggerImpl;
	
	@InjectMocks
	OrderCrudRestApiImpl OrderCrudRestApiImpl;
	
	Map<Object, Object> map=null;

	@BeforeTest
	public void before() {
		
		map = new HashMap<>();
		map.put("containertype", "96 well plate");
		map.put("devicerunid", "mp96-1");
		map.put("containerId", "1");
		map.put("sample", 2);
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		
	}
	@Test
	public void getListOfContainerIdWithOpenStatusTest() throws HMTPException {
		Mockito.when(containerSamplesReadRepository.findAllContainerIdWithOpenStatus(any(String[].class), anyLong())).thenReturn(Arrays.asList(map));
		Response response1 = OrderCrudRestApiImpl.getListOfContainerIdWithOpenStatus("open");
		Assert.assertEquals(response1.getStatus(), 200);
	}
	/*@Test(expectedExceptions=HMTPException.class)
	public void getListOfContainerIdWithOpenStatusThrowException() throws HMTPException {
	Mockito.when(containerSamplesReadRepository.findAllContainerIdWithOpenStatus(any(String.class), anyLong()))
		.thenReturn(null);
	 OrderCrudRestApiImpl.getListOfContainerIdWithOpenStatus("open");	
	}*/
	@Test
	public void getListOfContainerIdWithOpenStatusbyPassingNullAsStatusTest() throws HMTPException {
		Response response = OrderCrudRestApiImpl.getListOfContainerIdWithOpenStatus(null);
		Assert.assertEquals(response.getStatus(), 400);
	}
	@Test
	public void getListOfContainerIdWithOpenStatusNegativeTest() throws HMTPException {
		Response response = OrderCrudRestApiImpl.getListOfContainerIdWithOpenStatus("open");
		Assert.assertNotEquals(response.getStatus(), 400);
	}
	
}
