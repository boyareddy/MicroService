package com.roche.connect.rmm.testNG;

import java.io.IOException;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.util.MapDTOToEntity;

public class MapDTOToEntityTest {
	@InjectMocks
	MapDTOToEntity mapDTOToEntity;
	String runresult = "";
	String SampleResult = "";
	RunResultsDTO runResultsDTO = null;
	SampleResultsDTO sampleResultsDTO = null;
	RunResults runResults;
	public static final String jsonForSucessRunResult = "src/test/java/resource/RunResultPositive.json";
	public static final String jsonForSucessSampleResult = "src/test/java/resource/sampleResults.json";

	@BeforeTest
	public void beforeTest() throws IOException {
		runresult = JsonFileReaderAsString.getJsonfromFile(jsonForSucessRunResult);
		SampleResult = JsonFileReaderAsString.getJsonfromFile(jsonForSucessSampleResult);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(runresult);
		runResultsDTO = objectMapper.readValue(objectMapper.treeAsTokens(jsonNode), RunResultsDTO.class);
		runResults = objectMapper.readValue(objectMapper.treeAsTokens(jsonNode), RunResults.class);
		JsonNode jsonNode1 = objectMapper.readTree(SampleResult);
		sampleResultsDTO = objectMapper.readValue(objectMapper.treeAsTokens(jsonNode1), SampleResultsDTO.class);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getRunResultsFromDTOTEst() throws HMTPException {
		mapDTOToEntity.getRunResultsFromDTO(runResultsDTO);
		mapDTOToEntity.getRunReagentsAndConsumablesFromDTO(runResultsDTO);
		mapDTOToEntity.getRunResultsDetailFromDTO(runResultsDTO);
		mapDTOToEntity.getSampleResultsFromDTO(sampleResultsDTO);
		mapDTOToEntity.getSampleProtocolFromDTO(sampleResultsDTO);
		mapDTOToEntity.getSampleReagentsAndConsumablesFromDTO(sampleResultsDTO);
		mapDTOToEntity.getSampleResultsDetailFromDTO(sampleResultsDTO);
		mapDTOToEntity.getRunResultsDetailFromDTO(runResultsDTO);
	}

}
