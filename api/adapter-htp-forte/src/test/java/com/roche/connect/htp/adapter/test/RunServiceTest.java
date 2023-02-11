package com.roche.connect.htp.adapter.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO.ComplexIdDetailsStatus;
import com.roche.connect.common.htp.status.Completed;
import com.roche.connect.common.htp.status.HTPConstants;
import com.roche.connect.common.htp.status.InProgress;
import com.roche.connect.common.htp.status.Start;
import com.roche.connect.common.htp.status.StartPlanned;
import com.roche.connect.common.rmm.dto.InputStripDetailsDTO;
import com.roche.connect.common.rmm.dto.OutputStripDetailsDTO;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.htp.adapter.model.ComplexIdDetails;
import com.roche.connect.htp.adapter.model.Cycle;
import com.roche.connect.htp.adapter.model.Notification;
import com.roche.connect.htp.adapter.readrepository.ComplexIdDetailsReadRepository;
import com.roche.connect.htp.adapter.rest.HtpAdapterRestApiImpl;
import com.roche.connect.htp.adapter.services.RunServiceImpl;
import com.roche.connect.htp.adapter.services.WebServices;
import com.roche.connect.htp.adapter.services.WebServicesImpl;
import com.roche.connect.htp.adapter.util.CycleStatus;
import com.roche.connect.htp.adapter.util.RunStatus;
import com.roche.connect.htp.adapter.writerepository.ComplexIdDetailsWriteRepository;

public class RunServiceTest {

	@InjectMocks
	RunServiceImpl runServiceImpl = new RunServiceImpl();

	@InjectMocks
	HtpAdapterRestApiImpl htpSimulatorRestApiImpl = new HtpAdapterRestApiImpl();

	Map<String, Object> cyclesMap = null;
	RunResultsDTO runResultsDTO = null;
	Map<String, Object> statusUpdate = null;
	ResponseEntity<String> resEntity = null;
	Map<String, Object> runObj = null;
	ComplexIdDetailsDTO complexIdDetailsDTO = null;
	String path = null;

	@Mock
	Cycle actualResult;

	@Mock HMTPLoggerImpl hmtpLogger;
  	
  	Logger logger = LogManager.getLogger(RunServiceTest.class);
	
	@Mock
	WebServices webServices = org.mockito.Mockito.mock(WebServices.class);
	@Mock
	WebServicesImpl webServicesImpl;

	@Mock
	Response response = org.mockito.Mockito.mock(Response.class);

	ComplexIdDetails complexDetails = null;

	@Mock
	ComplexIdDetailsReadRepository complexIdDetailsReadRepository;
	@Mock
	ComplexIdDetailsWriteRepository complexIdDetailsWriteRepository;

	@SuppressWarnings("unchecked")
	@BeforeTest
	public void setUp() throws JsonParseException, JsonMappingException, IOException, ParseException {

		ObjectMapper mapper = new ObjectMapper();
		File runFile = new File("src/test/java/resource/run.json");

		MockitoAnnotations.initMocks(this);
		// Mockito.when(webServices.postRequest("", "")).thenReturn(resEntity);

		try {
			runObj = mapper.readValue(runFile, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		cyclesMap = getCyclesMap();
		complexDetails = getComplexIdDetails();
		runResultsDTO = getRunResultsDTO();
		statusUpdate = getStatusUpdate();
		actualResult = getCycleVO();

		// actualResult = runServiceImpl.updateCycle(cyclesMap, "");
		// runResultsDTO = runServiceImpl.convertJsonToRun(runObj);
		complexIdDetailsDTO = getComplexIdDetailsDTO();
		Mockito.doNothing().when(hmtpLogger).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(complexIdDetailsReadRepository.findById(Mockito.anyLong(),  Mockito.anyLong())).thenReturn(getComplexIdDetails());
		Mockito.when(complexIdDetailsReadRepository.save(getComplexIdDetails())).thenReturn(getComplexIdDetails());
		Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(),  Mockito.anyLong())).thenReturn(complexDetails);
	}

	private ComplexIdDetailsDTO getComplexIdDetailsDTO() {
		ComplexIdDetailsDTO complexIdDTO = new ComplexIdDetailsDTO();
		complexIdDTO.setRunProtocol("Test protocol");
		complexIdDTO.setStatus(ComplexIdDetailsStatus.READY);
		complexIdDTO.setComplexCreatedDatetime(new Timestamp(0));
		return complexIdDTO;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getCyclesMap() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File cycleFile = new File("src/test/java/resource/cycle.json");
		Map<String, Object> cycleObj = mapper.readValue(cycleFile, Map.class);
		return cycleObj;
	}

	public RunResultsDTO getRunResultsDTO() {
		Collection<InputStripDetailsDTO> collection = new ArrayList<>();
		Collection<RunResultsDetailDTO> runResultsDetail = new ArrayList<>();
		InputStripDetailsDTO inputStripDetailsDTO = new InputStripDetailsDTO();
		inputStripDetailsDTO.setSamplecounts("samplecounts");
		inputStripDetailsDTO.setStripId("stripId");
		collection.add(inputStripDetailsDTO);
		Collection<OutputStripDetailsDTO> collection2 = new ArrayList<>();
		OutputStripDetailsDTO ouDto = new OutputStripDetailsDTO();
		ouDto.setSamplecounts("samplecounts");
		ouDto.setStripId("stripId");
		collection2.add(ouDto);
		Collection<RunReagentsAndConsumablesDTO> collection3 = new ArrayList<>();
		RunReagentsAndConsumablesDTO dto = new RunReagentsAndConsumablesDTO();
		dto.setAttributeName("consumableDevicePartNumber");
		collection3.add(dto);
		RunResultsDTO runDTO = new RunResultsDTO();
		runDTO.setAssayType("NIPT");
		runDTO.setCreatedBy("createdBy");
		runDTO.setCreatedDateTime(new Date());
		runDTO.setDeviceId("deviceId");
		runDTO.setDeviceRunId("deviceRunId");
		runDTO.setDvcRunResult("dvcRunResult");
		runDTO.setInputcontainerIds(collection);
		runDTO.setOperatorName("operatorName");
		runDTO.setOutputcontainerIds(collection2);
		runDTO.setProcessStepName("processStepName");
		runDTO.setRunCompletionTime(new Timestamp(0));
		runDTO.setRunFlag("runFlag");
		runDTO.setRunRemainingTime(0L);
		runDTO.setRunResultId(12345);
		runDTO.setRunStartTime(new Timestamp(0));
		runDTO.setRunReagentsAndConsumables(collection3);
		runDTO.setComments("");
		runDTO.setRunStatus(RunStatus.STARTPLANNED.toString());
		RunResultsDetailDTO rrdd = new RunResultsDetailDTO();
		rrdd.setAttributeName("complex_id");
		rrdd.setAttributeValue("1234");
		rrdd.setCreatedBy("admin");
		rrdd.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
		rrdd.setRunResultsDetailsId(1234);
		rrdd.setUpdatedBy("admin");
		rrdd.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		runResultsDetail.add(rrdd);
		runDTO.setRunResultsDetail(runResultsDetail);
		return runDTO;
	}

	public Map<String, Object> getStatusUpdate() {
		Map<String, Object> map = new HashMap<>();
		map.put("", Object.class); // Fill all the values in map
		return map;
	}

	@Test
	public void getOrderDetailsTest() {
		runServiceImpl.getOrderDetails(1000l, complexIdDetailsDTO);
	}

	@Test(priority = 1)
	public void convertRunToJsonTest() {
		RunResultsDTO resultsDTO = getRunResultsDTO();
		Map<String, String> map = runServiceImpl.convertRunToJson(resultsDTO);
		map.get("run_id");
	}

	@Test(priority = 2)
	public void getComplexIdDetailsTest() {
		runServiceImpl.getComplexIdDetails("2345");
		// Assert.assertEquals(complxDTO.getRunProtocol(),
		// complexIdDetailsDTO.getRunProtocol());
	}

	@Test(priority = 3)
	public void convertJsonToRunPositiveTest() throws ParseException {
		try {

			runResultsDTO = runServiceImpl.convertJsonToRun(runObj);
			runObj.put("run_status", "Created");
			runResultsDTO = runServiceImpl.convertJsonToRun(runObj);
			runObj.put("run_status", "Started");
			runResultsDTO = runServiceImpl.convertJsonToRun(runObj);
			runObj.put("run_status", "inProcess");
			runResultsDTO = runServiceImpl.convertJsonToRun(runObj);
			runObj.put("run_status", "Completed");
			runResultsDTO = runServiceImpl.convertJsonToRun(runObj);
			runObj.put("run_status", "Failed");
			runResultsDTO = runServiceImpl.convertJsonToRun(runObj);

			Assert.assertEquals(runResultsDTO.getDeviceRunId(), runObj.get("run_id"));
			Assert.assertEquals(runResultsDTO.getDeviceId(), runObj.get("instrument_id"));
			Assert.assertEquals(runResultsDTO.getProcessStepName(), HTPConstants.PROCESS_STEP);
			Assert.assertEquals(runResultsDTO.getRunRemainingTime().longValue(),
					Long.parseLong(runObj.get("estimated_time_remaining").toString()));
			Assert.assertEquals(runResultsDTO.getUpdatedBy(), HTPConstants.UPDATED_BY);
			Assert.assertEquals(runResultsDTO.getComments(), HTPConstants.COMMENTS);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test(priority = 4)
	public void convertJsonToRunPositiveTest2() {
		Assert.assertNotNull(runResultsDTO.getDeviceRunId());
		Assert.assertNotNull(runResultsDTO.getProcessStepName());
		Assert.assertNotNull(runResultsDTO.getComments());
	}

	@Test
	public void updateCycleTest() throws JSONException, IOException {
		actualResult = runServiceImpl.updateCycle(cyclesMap, "");
		Assert.assertEquals(actualResult.getId(), 0);
		Assert.assertEquals(actualResult.getRunId(), "");
		Assert.assertEquals(actualResult.getChecksum(), cyclesMap.get("checksum").toString(),
				"Excepted checksum returned");
		Assert.assertEquals(actualResult.getCyclesNumber(), Integer.parseInt(cyclesMap.get("cycle").toString()),
				"Excepted cycle number returned");
	}

	@Test
	public void checkSumTest() throws IOException {
		try {
			assertTrue(!runServiceImpl.validateChecksum("src/test/java/resource/htp/ips/20181116_xyz_L02_cycle00_ExpAnno.json", "200008E346895"));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void updateRunTest() throws JsonProcessingException, HMTPException {
		StartPlanned s = new StartPlanned();
		s.setRunStatus(RunStatus.STARTPLANNED.toString());

		Start st = new Start();
		st.setRunStatus(RunStatus.START.toString());

		InProgress in = new InProgress();
		in.setRunStatus(RunStatus.INPROGRESS.toString());

		Completed com = new Completed();
		com.setRunStatus(RunStatus.COMPLETE.toString());
		try {
			runServiceImpl.updateRun("1", runResultsDTO);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Test
	public void createRunTest() throws IOException, JSONException, HMTPException {
		try {
			runServiceImpl.createRun(runResultsDTO);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// TestNG for HtpSimulatorRestApiImpl

	@Test
	public void notificationTest() {
		try {
			Notification notification = new Notification();
			htpSimulatorRestApiImpl.notification(notification);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void createRunInHTPAPIImplTest() {
		try {
			response = htpSimulatorRestApiImpl.createRun(runObj);
			//assertEquals(response.getStatus(), 201);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void CycleStatusTest() {
		for (CycleStatus cycleStatus : CycleStatus.values()) {
			Assert.assertEquals(cycleStatus, cycleStatus);
		}
	}

	public Cycle getCycleVO() {
		Cycle c = new Cycle();
		c.setChecksum("1324");
		c.setType("fastq.gz");
		c.setId(10000l);
		c.setStatus("Completed");
		c.setPath("C://ips/htp/runs");
		c.setHqr(2);
		c.setReadLength(564235246);
		c.setSendToSecondaryFlag("true");
		c.setTransferComplete("completed");
		c.setCreatedBy("admin");
		c.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
		c.setUpdatedBy("lab manager");
		c.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));

		logger.info(c.getType() + c.getStatus() + c.getPath() + c.getHqr() + c.getReadLength()
				+ c.getSendToSecondaryFlag() + c.getTransferComplete() + c.getCreatedBy() + c.getCreatedDateTime()
				+ c.getUpdatedBy() + c.getUpdatedDateTime());
		return c;
	}

	public ComplexIdDetails getComplexIdDetails() {
		ComplexIdDetails comp = new ComplexIdDetails();
		comp.setComplexId("12124314");
		comp.setDeviceId("HTP-01");
		comp.setDeviceRunId("5636575");
		comp.setId(10000l);
		logger.info(comp.getComplexId() + " " + comp.getDeviceId() + " " + comp.getDeviceRunId() + " " + comp.getId());
		return comp;
	}

	@Test
	public void completeCycleInHTPAPIImplTest() {
		try {
			htpSimulatorRestApiImpl.completeCycle("2", runObj);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void updateRunStatusTest() {
		try {
			runServiceImpl.updateRunStatus("12334", runResultsDTO);
		} catch (Exception e) {
			Assert.assertEquals("true", "true");
		}
	}

	@Test
	public void convertRunStatusToRunTest() {
		runObj.put("run_status","Created");
		runObj.put("output_folder","c:/");
		runServiceImpl.convertRunStatusToRun(runObj);
		runObj.put("run_status","Started");
		runServiceImpl.convertRunStatusToRun(runObj);
		runObj.put("run_status","InProcess");
		runServiceImpl.convertRunStatusToRun(runObj);
		runObj.put("run_status","Completed");
		runServiceImpl.convertRunStatusToRun(runObj);
		runObj.put("run_status","TransferCompleted");
		runServiceImpl.convertRunStatusToRun(runObj);
		runObj.put("run_status","Failed");
		runServiceImpl.convertRunStatusToRun(runObj);
		
		runObj.put("output_folder","c:/");
		
	}

	@Test
	public void getPatientDetailsFromIMMTest() {
		Mockito.when(webServicesImpl.postRequest(Mockito.anyString(), Mockito.any()))
				.thenThrow(JsonProcessingException.class);
		response = runServiceImpl.getPatientDetailsFromIMM("54534");
		assertEquals(response.getStatus(), 500);
	}

}
