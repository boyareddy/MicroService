package com.roche.connect.rmm.testNG;

import static org.testng.Assert.assertNotEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.rmm.jasper.dto.JasperUtil;
import com.roche.connect.rmm.jasper.dto.LpplateBasedSamples;
import com.roche.connect.rmm.jasper.dto.SampleDTO;

public class JasperUtilTest {

	@Test
	public void getLpPlateBasedSamplesTest() throws JsonParseException, JsonMappingException, IOException{
		FileReader fr = new FileReader(new File("src/test/java/Resource/listOfPlates.json"));
		ObjectMapper mapper = new ObjectMapper();
		List<SampleDTO> listOfPlates = mapper.readValue(fr, new TypeReference<List<SampleDTO>>(){});
		List<SampleDTO> listOfPlatesTemp = new ArrayList<>();
		
		for(SampleDTO sampleDTO : listOfPlates){
			listOfPlatesTemp.add(sampleDTO);
			LpplateBasedSamples lpplateBasedSamples = JasperUtil.getLpPlateBasedSamples(listOfPlatesTemp);
			assertNotEquals(lpplateBasedSamples, null); 
		}
	}
}
