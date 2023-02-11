package com.roche.connect.rmm.testNG;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.rmm.model.RunReagentsAndConsumables;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.RunResultsDetail;
import com.roche.connect.rmm.model.SampleProtocol;
import com.roche.connect.rmm.model.SampleReagentsAndConsumables;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.model.SampleResultsDetail;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.util.MapEntityToDTO;

public class MapEntityToDTOTest {
	@InjectMocks
	MapEntityToDTO mapEntityToDTO;
	
	String runresult = "";
	String SampleResult = "";
	SampleResults sampleResults=null;
	RunResults runResults;
	public static final String jsonForSucessRunResult = "src/test/java/resource/RunResultEntity.json";
	public static final String jsonForSucessSampleResult = "src/test/java/resource/sampleentity.json";
	@BeforeTest
	public void beforeTest() throws IOException {
		runresult = JsonFileReaderAsString.getJsonfromFile(jsonForSucessRunResult);
		SampleResult=JsonFileReaderAsString.getJsonfromFile(jsonForSucessSampleResult);
		ObjectMapper objectMapper = new ObjectMapper();
		
	 		JsonNode jsonNode = objectMapper.readTree(runresult);
		runResults= objectMapper.readValue(objectMapper.treeAsTokens(jsonNode), RunResults.class);
			JsonNode jsonNode1 = objectMapper.readTree(SampleResult);
			sampleResults=objectMapper.readValue(objectMapper.treeAsTokens(jsonNode1),SampleResults.class);
	MockitoAnnotations.initMocks(this);
	}
	@Test
	public void getRunResultsFromDTOTEst() throws HMTPException{
		
		Map<String, String> deviceMap=new HashMap<>();
		deviceMap.put("MP24", "ABC");
		mapEntityToDTO.getRunResultsDTOFromRunResults(runResults);
		mapEntityToDTO.getSampleDTOFromSampleResults(sampleResults);
		mapEntityToDTO.convertToDTO(sampleResults);
		mapEntityToDTO.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO((List<RunReagentsAndConsumables>) runResults.getRunReagentsAndConsumables());
		mapEntityToDTO.convertRunResultsDetailToRunResultsDetailDTODTO((List<RunResultsDetail>) runResults.getRunResultsDetail());
		mapEntityToDTO.convertSampleProtocolToSampleProtocolDTO((List<SampleProtocol>) sampleResults.getSampleProtocol());
		mapEntityToDTO.convertSampleReagentsAndConsumablesToReagentDTO((List<SampleReagentsAndConsumables>) sampleResults.getSampleReagentsAndConsumables());
		mapEntityToDTO.convertSampleResultsDetailToSampleResultsDetailDTO((List<SampleResultsDetail>) sampleResults.getSampleResultsDetail());
		mapEntityToDTO.mapsampleResultstoProcessDTO(sampleResults);
//		mapEntityToDTO.getSearchRunResultsDTOFromRunResults(runResults,deviceMap);
		mapEntityToDTO.getSearchOrderFromSampleResults(sampleResults);
		mapEntityToDTO.getSearchOrderFromOrderDTO(getOrderDTO());
		
		
//		mapEntityToDTO.mapProcessStepvalues("LP24PrePCR");
//		mapEntityToDTO.mapProcessStepvalues("LP24PostPCR");
//		mapEntityToDTO.mapProcessStepvalues("LP24SeqPrep");
//		mapEntityToDTO.mapProcessStepvalues("Sequencing");
//		mapEntityToDTO.mapProcessStepvalues("Analysis");
	}
	
	public OrderDTO getOrderDTO() {
		
		OrderDTO orderDTO=new OrderDTO();
		orderDTO.setAccessioningId("100L");
		orderDTO.setOrderId(20L);
		orderDTO.setOrderStatus("completed");
		orderDTO.setActiveFlag("D1");
		orderDTO.setAssayType("asa");
		orderDTO.setSampleType("plasma");
		
		return orderDTO;
		
		
		
	}
	
}
