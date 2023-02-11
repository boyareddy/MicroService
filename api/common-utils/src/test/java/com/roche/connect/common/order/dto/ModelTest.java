package com.roche.connect.common.order.dto;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelTest {
	
	OrderParentDTO orderParent;
	@BeforeTest
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		FileReader fr=new FileReader(new File("src/test/java/resources/OrderCrudUpdateDetails.json"));
		ObjectMapper objectMapper=new ObjectMapper();
		 orderParent=objectMapper.readValue(fr, OrderParentDTO.class);
	}
	@Test
	public void orderParentDTOTest(){
		assertEquals(orderParent.getOrder().getAccessioningId(),"999999");
		assertEquals(orderParent.getOrder().getAssayType(),"NIPTDPCR" );
		assertEquals(orderParent.getOrder().getOrderComments(), "first comment");
		assertEquals(orderParent.getOrder().isRetestSample(), true);
		assertEquals(orderParent.getOrder().getSampleType(), "Plasma");
		
		
	}
	@Test
	public void assayDTOTest(){
	assertEquals(orderParent.getOrder().getAssay().getEggDonor(), "Self");
	assertEquals(orderParent.getOrder().getAssay().getEggDonorAge(),Integer.valueOf(15));
	assertEquals(orderParent.getOrder().getAssay().getFetusNumber(),"2");
	assertEquals(orderParent.getOrder().getAssay().getIvfStatus(), "Yes");
	assertEquals(orderParent.getOrder().getAssay().getGestationalAgeDays(),Integer.valueOf(4));
	assertEquals(orderParent.getOrder().getAssay().getGestationalAgeWeeks(), Integer.valueOf(13));
	assertEquals(orderParent.getOrder().getAssay().getMaternalAge(), Integer.valueOf(5));
	}
	@Test
	public  void patientDTOTest() {
		assertEquals(orderParent.getOrder().getPatient().getPatientFirstName(), "Kumar");
		assertEquals(orderParent.getOrder().getPatient().getPatientLastName(), "Ajay");
		
		assertEquals(orderParent.getOrder().getPatient().getPatientMedicalRecNo(), "3423423433");
	
		
		assertEquals(orderParent.getOrder().getPatient().getRefClinicianName(), "apolo");
		assertEquals(orderParent.getOrder().getPatient().getClinicName(),"apolo");
		
	
		assertEquals(orderParent.getOrder().getPatient().getOtherClinicianName(),"apolo");
				
	}
	
	@Test
	public void statusDtoTest() {
	StatusDto statusDto=new StatusDto();
	statusDto.setStatus("Open");
	statusDto.setWorkflowStatus("ongoing");
	statusDto.setWorkflowType("NIPTDPCR");
	assertEquals(statusDto.getStatus(), "Open");
	assertEquals(statusDto.getWorkflowStatus(),"ongoing");
	assertEquals(statusDto.getWorkflowType(), "NIPTDPCR");
	}
	
	@Test
	public  void workflowDTOTest() {
		WorkflowDTO workflowDTO=new WorkflowDTO();
		workflowDTO.setAccessioningId("001");
		workflowDTO.setAssaytype("NIPTHTP");
		workflowDTO.setComments("test comments");
		workflowDTO.setFlags("F1");
		workflowDTO.setOrderId(1);
		workflowDTO.setSampletype("plasma");
		workflowDTO.setWorkflowStatus("ongoing");
		workflowDTO.setWorkflowType("NIPTDPCR");
		assertEquals(workflowDTO.getAccessioningId(),"001");
		assertEquals(workflowDTO.getAssaytype(),"NIPTHTP");
		assertEquals(workflowDTO.getComments(), "test comments");
		assertEquals(workflowDTO.getFlags(), "F1");
		assertEquals(workflowDTO.getOrderId(), 1);
		assertEquals(workflowDTO.getSampletype(), "plasma");
		assertEquals(workflowDTO.getWorkflowStatus(),"ongoing");
		assertEquals(workflowDTO.getWorkflowType(), "NIPTDPCR");
	}

	@Test
	public void containerSamplesDTOTest() {
		ContainerSamplesDTO containerSamplesDTO = new ContainerSamplesDTO();
		containerSamplesDTO.setAccessioningID("001");
		containerSamplesDTO.setActiveFlag("Y");
		containerSamplesDTO.setContainerID("WF-500");
		containerSamplesDTO.setContainerSampleId(1l);
		containerSamplesDTO.setAssayType("NIPTHTP");
		containerSamplesDTO.setContainerType("Well-plate");
		containerSamplesDTO.setDeviceID("RND_0001");
		containerSamplesDTO.setLoadID(1l);
		containerSamplesDTO.setUpdatedBy("admin");
		containerSamplesDTO.setOrderComments("test");
		containerSamplesDTO.setDeviceRunID("RND-w0001");
		containerSamplesDTO.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
		containerSamplesDTO.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
		containerSamplesDTO.setOrderID(1l);
		containerSamplesDTO.setPosition("A1");
		containerSamplesDTO.setStatus("open");
		containerSamplesDTO.setCreatedBy("admin");
		assertEquals(containerSamplesDTO.getAccessioningID(), "001");
		assertEquals(containerSamplesDTO.getActiveFlag(),"Y" );
		assertEquals(containerSamplesDTO.getAssayType(), "NIPTHTP");
		assertEquals(containerSamplesDTO.getContainerID(),"WF-500" );
		assertEquals(containerSamplesDTO.getContainerSampleId(),new Long(1));
		assertEquals(containerSamplesDTO.getContainerType(), "Well-plate");
		assertEquals(containerSamplesDTO.getCreatedBy(),"admin" );
		assertNotNull(containerSamplesDTO.getCreatedDateTime());
		assertNotNull(containerSamplesDTO.getUpdatedDateTime());
		assertEquals(containerSamplesDTO.getDeviceID(),"RND_0001" );
		assertEquals(containerSamplesDTO.getDeviceRunID(),"RND-w0001" );
		assertEquals(containerSamplesDTO.getOrderComments(), "test");
		assertEquals(containerSamplesDTO.getLoadID(), new Long(1));
		assertEquals(containerSamplesDTO.getUpdatedBy(), "admin");
		assertEquals(containerSamplesDTO.getPosition(), "A1");
		assertEquals(containerSamplesDTO.getStatus(), "open");
	}
	
	@Test
	public void processStepValuesDTOTest() {
		ProcessStepValuesDTO dto=new ProcessStepValuesDTO();
		dto.setAccesssioningId("a001");
		dto.setComments("test");
		dto.setDeviceId("RND001");
		dto.setOperatorName("admin");
		dto.setOrderId(1l);
		dto.setOutputContainerId("wp-001");
		dto.setOutputContainerPosition("A1");
		dto.setOutputKitId("MP001");
		dto.setProcessStepName("NA-Extaction");
		dto.setRunCompletionTime(new Timestamp(System.currentTimeMillis()));
		dto.setRunFlag("f1");
		dto.setRunRemainingTime(new Timestamp(System.currentTimeMillis()));
		dto.setRunResultId(1l);
		dto.setRunStartTime(new Timestamp(System.currentTimeMillis()));
		dto.setRunStatus("open");
		dto.setSampleResultId(1l);
		dto.setUpdatedBy("admin");
		assertEquals(dto.getAccesssioningId(), "a001");
		assertEquals(dto.getComments(),"test");
		assertEquals(dto.getDeviceId(), "RND001");
		assertEquals(dto.getOperatorName(), "admin");
		assertEquals(dto.getUpdatedBy(), "admin");
		assertEquals(dto.getOrderId(),new Long(1));
		assertEquals(dto.getOutputContainerId(), "wp-001");
		assertEquals(dto.getOutputContainerPosition(), "A1");
		assertEquals(dto.getOutputKitId(),"MP001");
		assertEquals(dto.getProcessStepName(), "NA-Extaction");
		assertEquals(dto.getRunFlag(), "f1");
		assertEquals(dto.getRunStatus(),"open");
		assertNotNull(dto.getRunCompletionTime());
		assertNotNull(dto.getRunRemainingTime());
		assertNotNull(dto.getRunStartTime());
		assertEquals(dto.getRunResultId(), new Long(1));
		assertEquals(dto.getSampleResultId(), new Long(1));
		assertEquals(dto.getUpdatedBy(), "admin");
		
	}
	
}
