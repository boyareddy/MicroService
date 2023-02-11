package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;

/**
 * 
 * @author imtiyazm
 * This class is for test validateContainerSamplesStatus(String deviceRunId)
 *
 */
public class ValidateContainerSampleStatusTest {

	@Mock
	ContainerSamplesReadRepository containerSamplesReadRepository = org.mockito.Mockito
			.mock(ContainerSamplesReadRepository.class);

	@Mock
	OrderService orderService = org.mockito.Mockito.mock(OrderService.class);

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	String containerID = null;
	Long domainId = 0L;
	String deviceRunId, deviceRunIds = null;

	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;

	List<Object> listContainerSamples = null;
	List<ContainerSamplesDTO> containerSamplesDTO = null;
	List<Object> containerSamplesDTOList = null;
	ContainerSamples containerSamples = null;
	List<ContainerSamples> containerSampleList = null;

	@BeforeTest
	public void setUp() {
		domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();

		deviceRunId = "RND1345";
		deviceRunIds = "";

		containerSamples = new ContainerSamples();
		containerSamples.setAccessioningID("123");
		containerSamples.setStatus("senttodevice");
		containerSamples.setDeviceRunID(deviceRunId);

		containerSampleList = new ArrayList<>();
		containerSampleList.add(containerSamples);

		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

	}
 
	@Test
	public void validateContainerSamplesStatusPosTest() throws HMTPException {
		Mockito.when(
				containerSamplesReadRepository.findAllContainerSamplesByDeviceRun(any(String.class), any(long.class)))
				.thenReturn(containerSampleList);

		Response response = orderCrudRestApiImpl.validateContainerSamplesStatus(deviceRunId);
		assertEquals(response.getStatus(), HttpStatus.SC_OK);

	}

	@Test
	public void validateContainerSamplesStatusNegTest() throws HMTPException {
		Mockito.when(
				containerSamplesReadRepository.findAllContainerSamplesByDeviceRun(any(String.class), any(long.class)))
				.thenReturn(Collections.emptyList());

		Response response = orderCrudRestApiImpl.validateContainerSamplesStatus(deviceRunId);
		assertEquals(response.getStatus(), HttpStatus.SC_NO_CONTENT);
	}

	@Test(expectedExceptions = HMTPException.class)
	public void validateContainerSamplesStatusNegAsExceptionTest() throws HMTPException {
		Mockito.when(
				containerSamplesReadRepository.findAllContainerSamplesByDeviceRun(any(String.class), any(long.class)))
				.thenThrow(SQLException.class);
		orderCrudRestApiImpl.validateContainerSamplesStatus(deviceRunId);
	}

}
