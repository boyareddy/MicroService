package com.roche.connect.rmm.testNG;

import static org.testng.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.common.adm.notification.RestClient;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.util.SampleStatus;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.readrepository.RunResultsReadRepository;
import com.roche.connect.rmm.services.AssayIntegrationService;
import com.roche.connect.rmm.util.RunResultsSchedulerUtil;
import com.roche.connect.rmm.writerepository.RunResultsWriteRepository;

@PrepareForTest({ RestClientUtil.class, RestClient.class, AdmNotificationService.class, ThreadSessionManager.class,HMTPLoggerImpl.class ,Timestamp.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class RunResultsSchedulerUtilTest {

	@InjectMocks
	RunResultsSchedulerUtil runResultsSchedulerUtil;

	@Mock
	RunResultsReadRepository runResultsReadRepository;

	@Mock
	RunResultsWriteRepository runResultsWriteRepository;

	@Mock
	AssayIntegrationService assayIntegrationService;

	@Mock
	Invocation.Builder builder;

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	List<SampleResults> sampleResultsDTOList = new ArrayList<>();
	List<RunResults> runResults = new ArrayList<>();

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Mock Timestamp maxTimeStamp ;
	@BeforeTest
	public void setUp() throws HMTPException {

		// runResultsSchedulerUtil.setMp24TimeInMin("30");

		RunResults runResult = new RunResults();
		runResult.setRunStatus(SampleStatus.INPROGRESS.getText());
		Company company = new Company();
		company.setId(1);
		runResult.setCompany(company );
		/*SampleResults sr1 = new SampleResults();
		sr1.setResult("passed");
		sampleResultsDTOList.add(sr1);

		SampleResults sr2 = new SampleResults();
		sr2.setResult("passed");
		sampleResultsDTOList.add(sr2);*/
		
		runResult.setSampleResults(sampleResultsDTOList);
		runResults.add(runResult);

		List<ProcessStepActionDTO> processStepList = new ArrayList<>();

		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
		processStepActionDTO.setProcessStepSeq(1);
		processStepActionDTO.setProcessStepName("NA Extraction");
		processStepList.add(processStepActionDTO);

		MockitoAnnotations.initMocks(this);

		Mockito.when(assayIntegrationService.getProcessStepAction(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(processStepList);

		PowerMockito.mockStatic(Timestamp.class);
		Mockito.when(Timestamp.from(Mockito.any(Instant.class))).thenReturn(maxTimeStamp);
		Mockito.when(runResultsReadRepository.findRunResultByProcessStepNameByRunStatusAndRunStartTime(
				Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class))).thenReturn(runResults);
		
		
		/*Mockito.when(runResultsReadRepository
				.findRunResultByProcessStepNameByRunStatusAndRunStartTime("NA Extraction",
						SampleStatus.INPROGRESS.getText(), maxTimeStamp)).thenReturn(runResults);*/

		// Mockito.when(runResultsWriteRepository.save(runResult)).thenReturn(true);

		Mockito.when(runResultsWriteRepository.save(runResult)).thenReturn(runResult);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		/*
		 * SampleResultsDTO sr1 = new SampleResultsDTO(); sr1.setResult("inprogress");
		 */
		
		/*PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession().setAccessorCompanyId(Mockito.anyLong())*/

	}

	@Test
	public void getRunResultStatusBySampleStatusTest() throws HMTPException {

		SampleResults sr1 = new SampleResults();
		sr1.setResult("passed");
		sampleResultsDTOList.add(sr1);

		SampleResults sr2 = new SampleResults();
		sr2.setResult("passed");
		sampleResultsDTOList.add(sr2);
		String result = null;

		result = runResultsSchedulerUtil.getRunResultStatusBySampleStatus(sampleResultsDTOList);
		assertEquals(SampleStatus.COMPLETED.getText(), result);

		sampleResultsDTOList.clear();

		sampleResultsDTOList.add(sr1);
		sr2.setResult("aborted");
		sampleResultsDTOList.add(sr2);
		result = runResultsSchedulerUtil.getRunResultStatusBySampleStatus(sampleResultsDTOList);
		assertEquals(SampleStatus.COMPLETED_WITH_FLAGS.getText(), result);

		sampleResultsDTOList.clear();
		sr2.setResult("aborted");
		sampleResultsDTOList.add(sr2);
		result = runResultsSchedulerUtil.getRunResultStatusBySampleStatus(sampleResultsDTOList);
		assertEquals(SampleStatus.ABORTED.getText(), result);

		sampleResultsDTOList.clear();
		sr2.setResult("inprogress");
		sampleResultsDTOList.add(sr2);
		result = runResultsSchedulerUtil.getRunResultStatusBySampleStatus(sampleResultsDTOList);
		assertEquals(SampleStatus.COMPLETED_WITH_FLAGS.getText(), result);

		sampleResultsDTOList.clear();
		sr2.setResult("flagged");
		sampleResultsDTOList.add(sr2);
		result = runResultsSchedulerUtil.getRunResultStatusBySampleStatus(sampleResultsDTOList);
		assertEquals(SampleStatus.COMPLETED_WITH_FLAGS.getText(), result);
	}

	@Test(expectedExceptions = HMTPException.class)
	public void getRunResultStatusBySampleStatusTest2() throws HMTPException {
		List<SampleResults> sampleResultsDTOList2 = new ArrayList<>();
		runResultsSchedulerUtil.getRunResultStatusBySampleStatus(sampleResultsDTOList2);

	}

	@Test(expectedExceptions = HMTPException.class)
	public void getRunResultStatusBySampleStatusTest3() throws HMTPException {
		List<SampleResults> sampleResultsDTOList2 = new ArrayList<>();
		SampleResults sr1 = new SampleResults();
		sr1.setResult("Test");
		sampleResultsDTOList2.add(sr1);
		runResultsSchedulerUtil.getRunResultStatusBySampleStatus(sampleResultsDTOList2);
	}

	@Test(expectedExceptions = HMTPException.class)
	public void getRunResultStatusBySampleStatusTest4() throws HMTPException {
		List<SampleResults> sampleResultsDTOList2 = new ArrayList<>();
		SampleResults sr1 = new SampleResults();
		sr1.setResult("Test");
		sampleResultsDTOList2.add(sr1);
		runResultsSchedulerUtil.getRunResultStatusBySampleStatus(null);
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

	@Test
	public void getTokenTest() {
		PowerMockito.mockStatic(RestClientUtil.class);
		ReflectionTestUtils.setField(runResultsSchedulerUtil, "loginUrl", "http://localhost");
		ReflectionTestUtils.setField(runResultsSchedulerUtil, "loginEntity", "http://localhost");
		Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(builder);
		Mockito.when(
				builder.post(Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED), String.class))
				.thenReturn("token");
		runResultsSchedulerUtil.getToken();
	}

	@Test
	public void udpateRunResultStatusTest() {
		PowerMockito.mockStatic(RestClientUtil.class);
		ReflectionTestUtils.setField(runResultsSchedulerUtil, "loginUrl", "http://localhost");
		ReflectionTestUtils.setField(runResultsSchedulerUtil, "loginEntity", "http://localhost");
		ReflectionTestUtils.setField(runResultsSchedulerUtil, "mp24TimeInMin", "20");
		Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(builder);
		Mockito.when(
				builder.post(Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED), String.class))
				.thenReturn("token");

		runResultsSchedulerUtil.udpateRunResultStatus();
	}
	
	
	@Test
	public void udpateRunResultStatusLP24Test() throws HMTPException {
		
		List<RunResults> runResults2 = new ArrayList<>();
		
		RunResults runResult = new RunResults();
		runResult.setRunStatus(SampleStatus.INPROGRESS.getText());
		Company company = new Company();
		company.setId(1);
		runResult.setCompany(company );
		
		List<SampleResults> srList = new ArrayList<>();
		SampleResults sr1 = new SampleResults();
		sr1.setResult("passed");
		srList.add(sr1);
		
		runResult.setSampleResults(srList);
		runResults2.add(runResult);
		
		List<ProcessStepActionDTO> processStepList1 = new ArrayList<>();

		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
		processStepActionDTO.setProcessStepSeq(2);
		processStepActionDTO.setProcessStepName("NA Extraction");
		processStepList1.add(processStepActionDTO);
		
		Mockito.when(assayIntegrationService.getProcessStepAction(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(processStepList1);

		Mockito.when(runResultsReadRepository.findRunResultByProcessStepNameByRunStatusAndRunStartTime(
				Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class))).thenReturn(runResults2);
		
		PowerMockito.mockStatic(RestClientUtil.class);
		ReflectionTestUtils.setField(runResultsSchedulerUtil, "loginUrl", "http://localhost");
		ReflectionTestUtils.setField(runResultsSchedulerUtil, "loginEntity", "http://localhost");
		ReflectionTestUtils.setField(runResultsSchedulerUtil, "lp24TimeInMin", "20");
		Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(builder);
		Mockito.when(
				builder.post(Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED), String.class))
				.thenReturn("token");

		runResultsSchedulerUtil.udpateRunResultStatusForLP24();
	}
}
