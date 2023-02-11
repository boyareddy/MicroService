package com.roche.connect.rmm.testNG;

import static org.mockito.Matchers.any;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.rmm.ApplicationBootRmm;
import com.roche.connect.rmm.rest.RunCrudRestApiImpl;
import com.roche.connect.rmm.specifications.RunResultsSpecifications;
import com.roche.connect.rmm.util.MapDTOToEntity;
import com.roche.connect.rmm.util.MapEntityToDTO;

@SpringBootTest(classes = ApplicationBootRmm.class)
public class RunCrudRestApiImplTest  {
	
	@Mock
	MapDTOToEntity mapDTOToEntity;
	@Mock
	MapEntityToDTO mapEntityToDTO;
	@InjectMocks
	RunCrudRestApiImpl runCrudRestApiImpl ;	
	   @Mock HMTPLoggerImpl hmtpLoggerImpl;
	@BeforeTest
	public void beforeTest() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
	}

	@Test
	public void getProcessStepResultsHMTPException() throws HMTPException {
	runCrudRestApiImpl.getProcessStepResults("");
	
	}
	@Test(expectedExceptions = Exception.class)
	public void getProcessStepResultsException() throws HMTPException {
	runCrudRestApiImpl.getProcessStepResults("acc-001");
	}

	@Test
	public void getRunResultsByDeviceRunIdHMTPException() throws HMTPException {
	runCrudRestApiImpl.getRunResultsByDeviceRunId("");
	}
	@Test
	public void getRunResultsByPassingdeviceidAsNullValue() throws HMTPException {
	Response response=	runCrudRestApiImpl.getRunResults("", "processstepname", "runstatus");
	Assert.assertEquals(response.getStatus(), 400);
	}
	@Test
	public  void getRunResultsByPassingProcessstepnameAsNullValue() throws HMTPException {
	Response response= 	runCrudRestApiImpl.getRunResults("deviceRunId", "", "runstatus");
	Assert.assertEquals(response.getStatus(), 400);
	}  
	@Test(expectedExceptions = HMTPException.class)
	public void postSampleResultsException() throws HMTPException {
		runCrudRestApiImpl.postSampleResults(any(SampleResultsDTO.class));
	}

	@Test(expectedExceptions = HMTPException.class)
	public void addRunResultsException() throws HMTPException {
		runCrudRestApiImpl.addRunResults(any(RunResultsDTO.class));
	}

	@Test(expectedExceptions = HMTPException.class)
	public void addSampleResultsException() throws HMTPException {
		runCrudRestApiImpl.addSampleResults(any(SampleResultsDTO.class));
	}

	@Test(expectedExceptions = HMTPException.class)
	public void getSampleResultsException() throws HMTPException {
		runCrudRestApiImpl.getSampleResults("deviceId", "processStepName", "outputContainerId", "inputContainerId",
				"inputContainerPosition", "outputContainerPosition","accessioningId");
	}

	@Test(expectedExceptions = HMTPException.class)
	public void getRunResultsBySampleResultIdException() throws HMTPException {
		runCrudRestApiImpl.getRunResults(2);
	}
	@Mock
	private Sort runResultsTypeSpecification;
	
	@Test
	void getsampleresults(){		
	RunResultsSpecifications.getSampleResults("deviceId", "processStepName", "outputContainerId", "inputContainerId", "inputContainerPosition", "outputContainerPosition","accessioningId");
	}
	@Test
	public void getSampleandRunResultsTestByPassNullValueForDeviceRunId() throws HMTPException {
	Response response=	runCrudRestApiImpl.getSampleandRunResults("", "processStepName","deviceid");
	Assert.assertEquals(response.getStatus(), 400);
	}@Test
	public void getSampleandRunResultsTestByPassNullValueForProcessStepName() throws HMTPException {	
	Response response=	runCrudRestApiImpl.getSampleandRunResults("deviceRunId","","deviceid");
	Assert.assertEquals(response.getStatus(), 400);
	}
	@Test
	public void findInprogressStatusByDeviceIDTestByPassNullValueForDeviceId() throws HMTPException {
		Response response=	runCrudRestApiImpl.findInprogressStatusByDeviceID("", "runStatus");
		Assert.assertEquals(response.getStatus(), 400);
	}
	@Test
	public void findInprogressStatusByDeviceIDTestByPassNullValueForRunStatus() throws HMTPException {
		Response response=	runCrudRestApiImpl.findInprogressStatusByDeviceID("deviceId", "");
		Assert.assertEquals(response.getStatus(), 400);
	}
	

	
	@Test
	public void getRunResultsByProcessStepNameTestByPassNullValueForprocessStepName() throws HMTPException {
		Response response=	runCrudRestApiImpl.getRunResultsByProcessStepName("");
		Assert.assertEquals(response.getStatus(), 400);
	}
	
	@Test
	public void getRunlistByAssayTypeTestByPassNullValueForAssayType() throws HMTPException {
		Response response = runCrudRestApiImpl.listActiveRuns("", "", "");
		Assert.assertEquals(response.getStatus(), 400);
	}
	
	@Test
	public void getlistInCompletedWFSActiveRunsNullValueForAssayType() throws HMTPException {
		Response response = runCrudRestApiImpl.listInCompletedWFSActiveRuns("", "");
		Assert.assertEquals(response.getStatus(), 400);
	}
	
	@Test
	public void getlistInCompletedWFSActiveRunsHMTPException() throws HMTPException {
		runCrudRestApiImpl.listInCompletedWFSActiveRuns(null, null);
	}
	
	@Test
	public void getRunResultsFromDTOTestForNullTest() throws HMTPException {
		runCrudRestApiImpl.listInCompletedWFSActiveRuns(null, null);		
		
	}
	
	@Test
	public void getAllDetailsByRunResultsIdForNullTest() throws HMTPException {
		runCrudRestApiImpl.getAllDetailsByRunResultsId(null);
	}
	
}
