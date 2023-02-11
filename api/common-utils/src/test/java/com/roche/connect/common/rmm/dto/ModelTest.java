package com.roche.connect.common.rmm.dto;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelTest {
	RunResultsDTO runResultsDTO;
	ProcessStepValuesDTO processStepValuesDTO;
	@BeforeTest
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		FileReader fr=new FileReader(new File("src/test/java/resources/RunResultsDTO.json"));
		FileReader fr1=new FileReader(new File("src/test/java/resources/processStepNameDTO.json"));
		ObjectMapper objectMapper=new ObjectMapper();
		runResultsDTO=objectMapper.readValue(fr,RunResultsDTO.class);
		processStepValuesDTO=objectMapper.readValue(fr1,ProcessStepValuesDTO.class);
		objectMapper.writeValueAsString(processStepValuesDTO);
		objectMapper.writeValueAsString(runResultsDTO);
		
	}
	@Test
	public void runResultsDTOTest() {
		runResultsDTO.hashCode();
		assertEquals(runResultsDTO.getAssayType(),"NIPTDPCR");
		assertEquals(runResultsDTO.getOperatorName(),"jimenj15");
	}
	@Test
	public void processStepValuesDTOTest(){
		assertEquals(processStepValuesDTO.getAccesssioningId(),"demo_06");
	}

}
