package com.roche.connect.omm.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.writerepository.ContainerSamplesWriteRepository;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
	"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class UpdateContainerSamplesFlagTest {
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	ContainerSamplesReadRepository containerSamplesReadRepository;
	@Mock
	ContainerSamplesWriteRepository containerSamplesWriteRepository;
	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl=new OrderCrudRestApiImpl();
	
	
	@Test
	public void UpdateContainerSamplesFlag() throws HMTPException{
	ContainerSamples containerSamples=new ContainerSamples();
	containerSamples.setAccessioningID("8989");
	containerSamples.setActiveFlag("F");
	containerSamples.setContainerID("wp-001");
		MockitoAnnotations.initMocks(this);
		Mockito.when(containerSamplesReadRepository.findByIdAndContainerIDAndStatus(Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(Arrays.asList(containerSamples));
		Response response=orderCrudRestApiImpl.updateContainerSamplesFlag(Mockito.anyString());
		Assert.assertEquals(response.getStatus(),HttpStatus.SC_BAD_REQUEST);
	}
	
	@Test
	public void UpdateContainerSamplesFlagTest1() throws HMTPException{
	ContainerSamples containerSamples=new ContainerSamples();
	containerSamples.setAccessioningID("8989");
	containerSamples.setActiveFlag("F");
	containerSamples.setContainerID("wp-001");
		MockitoAnnotations.initMocks(this);
		Mockito.when(containerSamplesReadRepository.findByContainerID(Mockito.anyString(),Mockito.anyLong())).thenReturn(Arrays.asList(containerSamples));
		Response response=orderCrudRestApiImpl.updateContainerSamplesFlag(Mockito.anyString());
		Assert.assertEquals(response.getStatus() ,HttpStatus.SC_OK);
	}
	@Test
	public void containerInfoForAccessioningIdTest()throws HMTPException {
		Map<Object, Object> map=null;
		map = new HashMap<>();
		map.put("containertype", "96 well plate");
		map.put("devicerunid", "mp96-1");
		map.put("containerId", "1");
		map.put("sample", 2);
		Mockito.when(containerSamplesReadRepository
		.findSampleByAccessioningId(Mockito.anyString(),Mockito.anyLong() )).thenReturn(Arrays.asList(map));
		Response response=	orderCrudRestApiImpl.containerInfoForAccessioningId(Mockito.anyString());
		Assert.assertEquals(response.getStatus() ,HttpStatus.SC_OK);
		
	}
	
}
