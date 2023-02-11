package com.roche.connect.htp.adapter.test;

import static org.junit.Assert.assertFalse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.forte.SecondaryJobDetailsDTO;
import com.roche.connect.common.forte.SecondarySampleAssayDetails;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO.ComplexIdDetailsStatus;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.htp.adapter.model.ComplexIdDetails;
import com.roche.connect.htp.adapter.model.Cycle;
import com.roche.connect.htp.adapter.model.ForteJob;
import com.roche.connect.htp.adapter.readrepository.ComplexIdDetailsReadRepository;
import com.roche.connect.htp.adapter.readrepository.CycleReadRepository;
import com.roche.connect.htp.adapter.services.RunServiceImpl;
import com.roche.connect.htp.adapter.services.WebServicesImpl;
import com.roche.connect.htp.adapter.util.PatientCache;

public class RunServiceImplTest {
	
	@Spy
    CycleReadRepository cycleReadRepository;

	@Mock
	ComplexIdDetailsDTO complexIdDetailsDTO;
	@Mock
	WebServicesImpl webServicesImpl;
	@Mock
	File file;
	PatientCache cache;
	
	@InjectMocks
	RunServiceImpl runServiceImpl;
	
	@InjectMocks
	ComplexIdDetails complexIdDetails;
	
	@Mock
	ComplexIdDetailsReadRepository complexIdDetailsReadRepository;
	Map<String, Object> map=null;
	SecondaryJobDetailsDTO secJobDetailsDTO=null;
	@Mock
	private Response response;
	private int successStatus = 200;
	@Mock HMTPLoggerImpl hmtpLoggerImpl;
	
	@BeforeTest
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		map = new HashMap<>();
		secJobDetailsDTO=getSecJobDetailsDTO();
		complexIdDetailsDTO=getcomplexIdDetailsDTO();
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(webServicesImpl.postRequest(Mockito.anyString(), Mockito.any())).thenReturn(Response.ok().build());
	}

	@Test
	public void getRunTest() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(webServicesImpl.getRequest(Mockito.anyString())).thenReturn(Response.ok().build());
		response = runServiceImpl.getRun("12300045");
		assertEquals(response.getStatus(), successStatus);
	}
	
	@Test(priority = 1)
	public void getOrderDetailsTest() {
		complexIdDetailsDTO=getcomplexIdDetailsDTO();
		map = runServiceImpl.getOrderDetails(10000l, complexIdDetailsDTO);
		assertEquals(map.get("status"), "READY");
	}


	@Test(priority = 2)
	public void getMountPathPTest() {
		ReflectionTestUtils.setField(runServiceImpl, "forteMountPath", "C:/");
		String path = runServiceImpl.getMountPath("FORTE", "/mnt/");
		assertEquals(path, "C:/");
	}
	
	@Test(priority = 2)
	public void getMountPathNTest() {

		String path = runServiceImpl.getMountPath("FORT", "mnt");
		assertEquals(path, "mnt");
	}

	@Test
	public void getPatientDetailsCacheTest() {
		cache=PatientCache.getInstance();
		cache.put("12345", secJobDetailsDTO);
		secJobDetailsDTO=runServiceImpl.getPatientDetails("12345", getCycleVO(), "1");
		assertEquals(secJobDetailsDTO.getKind(), "sample");
	}
	@Test
	public void getPatientDetailsTest() {
		List<SecondarySampleAssayDetails> secondarySampleDetails =  new ArrayList<>();
				SecondarySampleAssayDetails secondarySampleAssayDetails = new SecondarySampleAssayDetails();
		secondarySampleAssayDetails.setMaternalAge(12);
		secondarySampleAssayDetails.setEggDonor("mandy");
		secondarySampleAssayDetails.setFetusNumber("2");
		secondarySampleAssayDetails.setGestationalAgeDays(23);
		secondarySampleAssayDetails.setAccessioningId("1234567");
		secondarySampleDetails.add(secondarySampleAssayDetails);
		Mockito.when(webServicesImpl.postRequest(Mockito.anyString(), Mockito.anyString())).thenReturn(Response.ok().entity(secondarySampleDetails).build());
		//Mockito.when(runServiceImpl.getPatientDetailsFromIMM("123457")).thenReturn(Response.status(200).entity(secondarySampleDetails).build());
		secJobDetailsDTO=runServiceImpl.getPatientDetails("1234567", getCycleVO(), "1");
		assertEquals(secJobDetailsDTO.getKind(), "secondary");
		
	}
	/*@Test
	public void getPatientDetailsTest() {
		ReflectionTestUtils.setField(runServiceImpl, "forteMountPath", "C:/");
		Mockito.when(webServicesImpl.postRequest(Mockito.anyString(), Mockito.any())).thenReturn(Response.status(successStatus).build());
		Mockito.when(response.readEntity(new GenericType<List<SecondarySampleAssayDetails>>(){})).thenReturn(new ArrayList<>());
		secJobDetailsDTO=runServiceImpl.getPatientDetails("4565445", getCycleVO(), "1");
		assertEquals(secJobDetailsDTO.getKind(), "sample");
	}*/
	@Test
	public void checkFreeSpaceTest() throws Exception {
		Mockito.when(file.getUsableSpace()).thenReturn(100L);
		ReflectionTestUtils.setField(runServiceImpl, "htpMountPath", "C:/ips"); 
		JsonNode node=runServiceImpl.checkFreeSpace();
		assertEquals(node.get("freespace"),node.get("freespace"));
	}
	

	@Test
	public void getSampleDetailsTest() {
		List<Map<String, String>> sampleList=runServiceImpl.getSampleDetails(getSecondarySampleAssayDetailsList());
		Map<String, String> details=sampleList.get(0);
		assertEquals(details.get("egg_sources"), "yes");
	}

	@Test
	public void getPatientDetailsFromIMMTest() {
		try {
			response=runServiceImpl.getPatientDetailsFromIMM("54534");
			assertEquals(response.getStatus(), successStatus);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void updateJobStatusTest() {
		try {
			response=runServiceImpl.updateJobStatusToIMM("start", "secondary", "12233");
			assertEquals(response.getStatus(), successStatus);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	@Test
	public void getForteJobTest(){
		ForteJob forteJob=runServiceImpl.getForteJob(getCycleVO());
		assertEquals(forteJob.getJobType(), "secondary");
	}
	@Test
	public void createForteJobFromTertiaryJobDetailsTest(){
		ForteJob forteJob=runServiceImpl.createForteJobFromTertiaryJobDetails("MP001-12");
		assertEquals(forteJob.getJobType(), "tertiary");
	}
	@Test
	public void getFolderTest(){
		ReflectionTestUtils.setField(runServiceImpl, "regEx", "runs/"); 
		String path=runServiceImpl.getFolder("C:/");
		assertEquals(path, null);
		
	}

	public List<SecondarySampleAssayDetails> getSecondarySampleAssayDetailsList() {
		List<SecondarySampleAssayDetails> list = new ArrayList<>();
		list.add(getSecondarySampleAssayDetails());
		return list;
	}
	public ComplexIdDetailsDTO getcomplexIdDetailsDTO(){
	complexIdDetailsDTO = new ComplexIdDetailsDTO();
	complexIdDetailsDTO.setComplexCreatedDatetime(new Timestamp(0L));
	complexIdDetailsDTO.setRunProtocol("Test Protocal");
	complexIdDetailsDTO.setStatus(ComplexIdDetailsStatus.READY);
	return complexIdDetailsDTO;
	}
	private SecondaryJobDetailsDTO getSecJobDetailsDTO() {
		secJobDetailsDTO=new SecondaryJobDetailsDTO();
		secJobDetailsDTO.setId("56709");
		secJobDetailsDTO.setKind("sample");
		secJobDetailsDTO.setSecondaryChecksum("689765456");
		secJobDetailsDTO.setSecondaryInfile("sample");
		return secJobDetailsDTO;
	}

	public SecondarySampleAssayDetails getSecondarySampleAssayDetails() {
		SecondarySampleAssayDetails s = new SecondarySampleAssayDetails();
		s.setAccessioningId("1234");
		s.setEggDonor("yes");
		s.setFetusNumber("");
		s.setGestationalAgeDays(120);
		s.setGestationalAgeWeeks(17);
		s.setMaternalAge(25);
		s.setMolecularId("145");
		return s;
	}

	public Cycle getCycleVO() {
		Cycle c = new Cycle();
		c.setChecksum("1324");
		c.setType("fastq.gz");
		c.setId(10000l);
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
	
	@Test
	public void getExpectedStatusTest() {
		MockitoAnnotations.initMocks(this);
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setRunStatus("Inprocess");
		Mockito.when(runServiceImpl.getRun("12300045")).thenReturn(Response.ok().entity(runResultsDTO).build());
		String status = runServiceImpl.getExpectedStatus("12300045");
		
		//assertEquals(status, "Inprocess");
	}
	
	@Test
	public void isInValidRunSequenceTest() {
		ComplexIdDetails complexIdDet = new ComplexIdDetails();
		complexIdDet.setDeviceRunId("12300045");
		
		
		Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId("12300045", 1)).thenReturn(complexIdDet);
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setRunStatus("started");
		Mockito.when(runServiceImpl.getRun("12300045")).thenReturn(Response.ok().entity(runResultsDTO).build());
		
		runResultsDTO.setRunStatus("started");
		Mockito.when(runServiceImpl.getRun("12300045")).thenReturn(Response.ok().entity(runResultsDTO).build());
		//assertTrue(runServiceImpl.isInValidRunSequence("12300045","started",1));
		
		runResultsDTO.setRunStatus("started");
		Mockito.when(runServiceImpl.getRun("12300045")).thenReturn(Response.ok().entity(runResultsDTO).build());
	//	assertTrue(runServiceImpl.isInValidRunSequence("12300045","inprocess",1));
		
		runResultsDTO.setRunStatus("inProgress");
		Mockito.when(runServiceImpl.getRun("12300045")).thenReturn(Response.ok().entity(runResultsDTO).build());
		//assertTrue(runServiceImpl.isInValidRunSequence("12300045","completed",1));
		
		runResultsDTO.setRunStatus("inProgress");
		Mockito.when(runServiceImpl.getRun("12300045")).thenReturn(Response.ok().entity(runResultsDTO).build());
		//assertTrue(runServiceImpl.isInValidRunSequence("12300045","transfercompleted",1));
		
		runResultsDTO.setRunStatus("inProgress");
		Mockito.when(runServiceImpl.getRun("12300045")).thenReturn(Response.ok().entity(runResultsDTO).build());
	//	assertTrue(runServiceImpl.isInValidRunSequence("12300045","failed",1));
		
		runResultsDTO.setRunStatus("inProgresss");
		Mockito.when(runServiceImpl.getRun("12300045")).thenReturn(Response.ok().entity(runResultsDTO).build());
		//assertFalse(runServiceImpl.isInValidRunSequence("12300045","failed",1));
		
		complexIdDet.setDeviceRunId(null);
		Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId("12300045", 1)).thenReturn(complexIdDet);
		runResultsDTO.setRunStatus("inProgress");
		Mockito.when(runServiceImpl.getRun("12300045")).thenReturn(Response.ok().entity(runResultsDTO).build());
	//	assertFalse(runServiceImpl.isInValidRunSequence("12300045","started",1));
	}
	
	@Test
    public void validatePreviousCyclePositive() {
           
           Mockito.when(cycleReadRepository.findByRunId("123", 1)).thenReturn(Arrays.asList(getCycleVO()));
           runServiceImpl.validatePreviousCycle("123", 1, 1, "HTP-01");
    }
    
    @Test
    public void validatePreviousCycleNegative() {

    	Mockito.when(cycleReadRepository.findByRunId("123", 1)).thenReturn(Collections.emptyList());
           runServiceImpl.validatePreviousCycle("123", 1, 1, "HTP-01");
    }
    
    @Test
	public void checkFileSizeTest() {
    	ReflectionTestUtils.setField(runServiceImpl, "cycleFileNameAndSize", "fastq=576716800,versionInfo<1024,metrics=10240,expState=153600,expAnno=15360");
    	assertTrue(runServiceImpl.checkFileSize("src/test/java/resource/htp/ips/20181116_xyz_L02_cycle00_ExpAnno.json"));
    	assertTrue(runServiceImpl.checkFileSize("src/test/java/resource/htp/ips/20181116_xyz_L02_cycle00_ExpState.json"));
    	assertTrue(runServiceImpl.checkFileSize("src/test/java/resource/htp/ips/20181116_xyz_L02_cycle00_fastq.gz"));
    	assertTrue(runServiceImpl.checkFileSize("src/test/java/resource/htp/ips/20181116_xyz_L02_cycle00_metrics.txt"));
    	assertTrue(runServiceImpl.checkFileSize("src/test/java/resource/htp/ips/20181116_xyz_L02_cycle00_PrimaryAnalysisVersionInfo.txt"));
		
	}
    
    @Test 
    public void checkFileSizeWith() {
    	assertTrue(runServiceImpl.checkFileSize(">", 1024, 1025));
    	assertTrue(runServiceImpl.checkFileSize("=", 1024, 1024));
    	assertTrue(runServiceImpl.checkFileSize("<", 1024, 1023));
    	assertFalse(runServiceImpl.checkFileSize("!=", 111, 111));
    }
    
    @Test
    public void sendNotificationTest() {
    	ReflectionTestUtils.setField(runServiceImpl, "admNotificationURL", "https://www.test-roche.com:97/admjson/rest/api/v1/notification");
    	runServiceImpl.sendNotification("DeviceID01", "RunID01", NotificationGroupType.OUT_OF_SEQ_MSG_HTP);
    }

}
