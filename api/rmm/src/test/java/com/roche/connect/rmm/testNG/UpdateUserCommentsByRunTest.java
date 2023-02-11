package com.roche.connect.rmm.testNG;

import static org.testng.Assert.assertEquals;

import java.sql.SQLException;

import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.rmm.dto.ProcessStepValuesDTO;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.readrepository.RunResultsReadRepository;
import com.roche.connect.rmm.rest.RunCrudRestApiImpl;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.writerepository.RunResultsWriteRepository;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })

public class UpdateUserCommentsByRunTest {

	@Mock
	RunResultsReadRepository runResultsReadRepository = org.mockito.Mockito.mock(RunResultsReadRepository.class);

	@Mock
	RunResultsWriteRepository runResultsWriteRepository = org.mockito.Mockito.mock(RunResultsWriteRepository.class);

	@InjectMocks
	RunCrudRestApiImpl runCrudRestApiImpl = new RunCrudRestApiImpl();

	@Mock
	Response response;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	ProcessStepValuesDTO processStepValuesDTO = null;

	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;
	String jsonFileRunString = null;

	@BeforeTest
	public void setUp() throws Exception {
		jsonFileRunString = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/resource/UserCommentsUpdateRunWise.json");
		ObjectMapper objectMapper = new ObjectMapper();
		processStepValuesDTO = objectMapper.readValue(jsonFileRunString, ProcessStepValuesDTO.class);
		RunResults runResults = new RunResults();
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(runResultsReadRepository.findOne(processStepValuesDTO.getRunResultId())).thenReturn(runResults);

	}

	@Test
	public void updateUserRunCommentsTest() throws Exception {
		RunResults runResults = new RunResults();
		Mockito.when(runResultsWriteRepository.save(Mockito.any(RunResults.class))).thenReturn(runResults);
		Response actualResponseCode = runCrudRestApiImpl.updateUserComments(processStepValuesDTO);
		assertEquals(actualResponseCode.getStatus(), HttpStatus.SC_OK);
	}
	@Test
	public void updateUserRunCommentsIFTest() throws Exception {
		ProcessStepValuesDTO dto=new ProcessStepValuesDTO();
		dto.setComments(null);
		dto.setRunResultId(1l);
		RunResults runResults = new RunResults();
		Mockito.when(runResultsWriteRepository.save(Mockito.any(RunResults.class))).thenReturn(runResults);
		Response actualResponseCode = runCrudRestApiImpl.updateUserComments(dto);
		assertEquals(actualResponseCode.getStatus(), HttpStatus.SC_OK);
	}

	@Test(expectedExceptions = HMTPException.class)
	public void updateUserRunCommentsExceptionTest() throws Exception {
		Mockito.when(runResultsWriteRepository.save(Mockito.any(RunResults.class))).thenThrow(SQLException.class);
		runCrudRestApiImpl.updateUserComments(processStepValuesDTO);
	}
	
}
