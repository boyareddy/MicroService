package com.roche.connect.rmm.testNG;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.data.jpa.domain.Specification;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.rmm.dto.ProcessStepValuesDTO;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.readrepository.SampleResultsReadRepository;
import com.roche.connect.rmm.rest.RunCrudRestApiImpl;
import com.roche.connect.rmm.specifications.RunResultsSpecifications;
import com.roche.connect.rmm.specifications.RunResultsTypeSpecification;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.writerepository.SampleResultsWriteRepository;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })

public class UpdateUserCommentsBySampleTest {

	@Mock
	SampleResultsReadRepository sampleResultsReadRepository = org.mockito.Mockito
			.mock(SampleResultsReadRepository.class);

	@Mock
	SampleResultsWriteRepository sampleResultsWriteRepository = org.mockito.Mockito
			.mock(SampleResultsWriteRepository.class);

	@InjectMocks
	RunCrudRestApiImpl runCrudRestApiImpl = new RunCrudRestApiImpl();

	@Mock
	Response response;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	ProcessStepValuesDTO processStepValuesDTO = null;
	String jsonFileSampleString = null;

	@BeforeTest
	public void setUp() throws Exception {
		jsonFileSampleString = JsonFileReaderAsString
				.getJsonfromFile("src/test/java/resource/UserCommentsUpdateSampleWise.json");
		ObjectMapper objectMapper = new ObjectMapper();
		processStepValuesDTO = objectMapper.readValue(jsonFileSampleString, ProcessStepValuesDTO.class);
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
	}

	@Test
	public void updateUserSampleCommentsTest() throws Exception {
		SampleResults sampleResults=new SampleResults();
		sampleResults.setAccesssioningId("12312");
		sampleResults.setComments("update by test");
		Mockito.when(sampleResultsReadRepository.findAll(Mockito.any(Specification.class))).thenReturn(Arrays.asList(sampleResults));
		Response actualResponseCode = runCrudRestApiImpl.updateUserComments(processStepValuesDTO);
		assertEquals(actualResponseCode.getStatus(), HttpStatus.SC_OK);
	}
	
	@Test(expectedExceptions=HMTPException.class)
	public void updateUserSampleCommentsExeptionTest() throws Exception {
		Mockito.when(sampleResultsReadRepository.findAll(Mockito.any(Specification.class))).thenThrow(SQLException.class);
		runCrudRestApiImpl.updateUserComments(processStepValuesDTO);
	}

	

}
