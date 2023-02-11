package com.roche.connect.forte.adapter.test;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.forte.SecondaryJobDetailsDTO;
import com.roche.connect.common.forte.TertiaryJobDetailsDTO;
import com.roche.connect.htp.adapter.model.ComplexIdDetails;
import com.roche.connect.htp.adapter.model.Cycle;
import com.roche.connect.htp.adapter.model.ForteJob;
import com.roche.connect.htp.adapter.readrepository.ComplexIdDetailsReadRepository;
import com.roche.connect.htp.adapter.readrepository.CycleReadRepository;
import com.roche.connect.htp.adapter.readrepository.ForteJobReadRepository;
import com.roche.connect.htp.adapter.rest.ForteAdapterRestApiImpl;
import com.roche.connect.htp.adapter.services.RunService;
import com.roche.connect.htp.adapter.writerepository.CycleWriteRepository;
import com.roche.connect.htp.adapter.writerepository.ForteJobWriteRepository;

/*@SpringBootTest(classes = ApplicationBoot.class)*/
public class ForteAdapterRestApiImplTest /*extends AbstractTestNGSpringContextTests*/ {

	@InjectMocks
	ForteAdapterRestApiImpl forteAdapterMock = new ForteAdapterRestApiImpl();
	@Mock
	Logger logger = LogManager.getLogger(Logger.class);

	@Mock
	CycleReadRepository cycleReadRepository;
	
	@Mock
	CycleWriteRepository cycleWriteRepository;

	@Mock
	ComplexIdDetailsReadRepository complexIdDetailsWriteRepository = org.mockito.Mockito
			.mock(ComplexIdDetailsReadRepository.class);

	@Mock
	RunService runService = org.mockito.Mockito.mock(RunService.class);

	@Mock
	ForteJobReadRepository forteJobReadRepository;
	
	@Mock
	ForteJobWriteRepository forteJobWriteRepository;
	
	 @Mock HMTPLoggerImpl hmtpLoggerImpl;

	String jobId = String.valueOf(100000l);

	private Response responce = null;

	private int successStatus = 200;

	private int NoContent = 204;
	ForteJob forteJob;

	private TertiaryJobDetailsDTO tertiaryJobDetails;

	private SecondaryJobDetailsDTO secondaryDetails;

	private int BadStatus = 400;
	ComplexIdDetails comp;

	@BeforeTest
	public void beforeTest() throws IOException {
		MockitoAnnotations.initMocks(this); // mandatory
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Cycle cycle = getCycleVO();
		tertiaryJobDetails = getTertiaryJobDetails();
		secondaryDetails = getSecondaryJobDetailsDTO();
		comp = getComplexIdDetails();
		forteJob = getForteJobs();
		// Mockito.when(cycleWriteRepository.findTopByStatusAndSendToSecondaryFlagAndTypeOrderByUpdatedDateTime(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(getCycleVO());
		Mockito.when(runService.getForteJob(cycle)).thenReturn(forteJob);
		Mockito.when(forteJobWriteRepository.save(forteJob)).thenReturn(forteJob);
		Mockito.when(cycleWriteRepository.save(cycle)).thenReturn(cycle);
		Mockito.when(forteJobReadRepository.findById(Long.parseLong(jobId))).thenReturn(forteJob);
		Mockito.when(cycleReadRepository.findByRunIdAndType(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(getCyclesList());
		Mockito.when(forteJobReadRepository.findByDeviceRunIdAndJobStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(getForteJobsList());

	}

	@Test(priority = 1)
	public void hitPingTest() throws HMTPException {
		responce = forteAdapterMock.hitPing();
		assertEquals(responce.getStatus(), successStatus);
	}

	@Test
	public void hitHelloPTest() throws HMTPException {

		responce = forteAdapterMock.hitHello(getHelloMap());
		assertEquals(responce.getStatus(), successStatus);
	}

	@Test
	public void hitHelloNTest() throws HMTPException {

		responce = forteAdapterMock.hitHello(null);
		assertEquals(responce.getStatus(), NoContent);
	}

	@Test
	public void getJobTertieryTest() throws HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(runService.getTertiaryJobDetails()).thenReturn(tertiaryJobDetails);
		responce = forteAdapterMock.getJob();
		assertEquals(responce.getStatus(), successStatus);
	}

	@Test
	public void getJobSecondaryTest() throws HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(runService.getTertiaryJobDetails()).thenReturn(null);
		Mockito.when(runService.getSecondaryJobDetails()).thenReturn(secondaryDetails);
		responce = forteAdapterMock.getJob();
		assertEquals(responce.getStatus(), successStatus);
	}

	@Test
	public void getJobNagativeTest() throws HMTPException {

		MockitoAnnotations.initMocks(this);
		Mockito.when(runService.getTertiaryJobDetails()).thenReturn(null);
		Mockito.when(runService.getSecondaryJobDetails()).thenReturn(null);
		responce = forteAdapterMock.getJob();
		assertEquals(responce.getStatus(), NoContent);
	}

	@Test(priority = 4)
	public void updateJobStatusTest() throws IOException, HMTPException {

		responce = forteAdapterMock.updateJobStatus(jobId, getUpdateStatusStartMap());
		assertEquals(responce.getStatus(), successStatus);

	}

	@Test(priority = 4)
	public void updateJobStatusInprogressTest() throws IOException, HMTPException {

		responce = forteAdapterMock.updateJobStatus(jobId, getUpdateStatusInprogressMap());
		assertEquals(responce.getStatus(), successStatus);
	}

	@Test(priority = 4)
	public void updateJobStatusDoneTest() throws IOException, HMTPException {
		Mockito.when(runService.updateJobStatusToIMM(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Response.ok().build());
		Mockito.when(complexIdDetailsWriteRepository.findByDeviceRunId(Mockito.any(),  Mockito.anyLong())).thenReturn(comp);
		Mockito.when(cycleReadRepository.findByRunIdAndType(Mockito.anyString(), Mockito.anyString(),  Mockito.anyLong()))
				.thenReturn(getCyclesList());
		Mockito.when(runService.validateChecksum(Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(forteJobReadRepository.findById(Mockito.anyLong())).thenReturn(forteJob);
		responce = forteAdapterMock.updateJobStatus(jobId, getUpdateStatusDoneMap());
		assertEquals(responce.getStatus(), successStatus);
	}

	@Test(priority = 4)
	public void updateJobStatusErrorTest() throws IOException, HMTPException {

		responce = forteAdapterMock.updateJobStatus(jobId, getUpdateStatusErrorMap());
		assertEquals(responce.getStatus(), successStatus);
	}
	@Test
	public void updateJobStatusCompletedTest() throws IOException, HMTPException {
		responce = forteAdapterMock.updateJobStatus(null, getUpdateStatusCompletedMap());
		assertEquals(responce.getStatus(), BadStatus);
	}
	
	@Test
	public void updateJobStatusDefaultTest() throws IOException, HMTPException {
		Map<String, Object> map = new HashMap<>();
		map.put("job_status", "default");
		map.put("estimated_time_to_completion", 6000);
		responce = forteAdapterMock.updateJobStatus(jobId, map);
		assertEquals(responce.getStatus(), BadStatus);
	}

	public List<Cycle> getCyclesList() {
		List<Cycle> listOfCycles = new ArrayList<>();
		listOfCycles.add(getCycleVO());
		return listOfCycles;
	}

	public List<ForteJob> getForteJobsList() {
		List<ForteJob> forteJobsList = new ArrayList<>();
		forteJobsList.add(getForteJobs());
		return forteJobsList;
	}

	public Map<String, Object> getHelloMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("instance_id", "FORTE-01");
		map.put("forte_software_version", "v1");
		return map;
	}

	public Map<String, Object> getUpdateStatusStartMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("job_status", "start");
		map.put("estimated_time_to_completion", 6000);
		return map;
	}

	public Map<String, Object> getUpdateStatusInprogressMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("job_status", "inprogress");
		map.put("estimated_time_to_completion", 6000);
		return map;
	}
	
	public Map<String, Object> getUpdateStatusCompletedMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("job_status", "completed");
		map.put("estimated_time_to_completion", 6000);
		return map;
	}

	public Map<String, Object> getUpdateStatusDoneMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("job_status", "done");
		map.put("qc", 400);
		map.put("outfile", "/mnt/ips/forte/...");
		map.put("outfile_checksum", 12345);
		return map;
	}

	public Map<String, Object> getUpdateStatusErrorMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("job_status", "error");
		map.put("error_key", "error");
		return map;
	}

	public Cycle getCycleVO() {
		Cycle c = new Cycle();
		c.setChecksum("1324");
		c.setType("fastq.gz");
		c.setId(100000l);
		c.setStatus("Complete");
		c.setPath("C://ips/htp/runs");
		c.setHqr(2);
		c.setReadLength(564235246);
		c.setSendToSecondaryFlag("true");
		c.setTransferComplete("complete");
		c.setCreatedBy("admin");
		c.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
		c.setUpdatedBy("lab manager");
		c.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		return c;
	}

	public ComplexIdDetails getComplexIdDetails() {
		ComplexIdDetails comp = new ComplexIdDetails();
		comp.setComplexId("12124314");
		comp.setDeviceId("HTP-01");
		comp.setDeviceRunId("5636575");
		comp.setId(100000l);
		comp.setCreatedBy(null);
		comp.setCreatedDateTime(null);
		comp.setUpdatedBy(null);
		return comp;
	}

	private TertiaryJobDetailsDTO getTertiaryJobDetails() {
		tertiaryJobDetails = new TertiaryJobDetailsDTO();
		tertiaryJobDetails.setId("234565");
		tertiaryJobDetails.setTertiary_parameters("2");
		return tertiaryJobDetails;
	}

	private SecondaryJobDetailsDTO getSecondaryJobDetailsDTO() {
		secondaryDetails = new SecondaryJobDetailsDTO();
		secondaryDetails.setSecondaryChecksum("76543234");
		secondaryDetails.setSecondaryInfile("5234653");
		return secondaryDetails;
	}

	public ForteJob getForteJobs() {
		ForteJob forteJob = new ForteJob();
		forteJob.setCreatedBy("admin");
		forteJob.setCreatedDateandTime(new Timestamp(System.currentTimeMillis()));
		forteJob.setDeviceRunId("2324");
		forteJob.setErrorKey("error");
		forteJob.setEstimatedTimeToCompletion("1324");
		forteJob.setId(100000l);
		forteJob.setJobStatus("done");
		forteJob.setJobType("secondary");
		forteJob.setOutFileChecksum("43523");
		forteJob.setOutFilePath("C://Users/");
		forteJob.setQc("400");
		forteJob.setSentToTertiary("YES");
		forteJob.setUpdatedBy("admin");
		forteJob.setUpdatedDateandTime(new Timestamp(System.currentTimeMillis()));
		return forteJob;
	}
}
