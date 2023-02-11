package com.roche.connect.htp.adapter.test;

import java.io.IOException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.forte.SecondaryJobDetailsDTO;
import com.roche.connect.htp.adapter.model.ForteJob;
import com.roche.connect.htp.adapter.util.JsonFileReaderAsString;
import com.roche.connect.htp.adapter.util.PatientCache;

public class UtilTest {
	PatientCache cache;
	ForteJob forteJob;
	
	public static final String NEWDEVICEDTOJSON = "src/test/java/resource/NewDeviceDTO.json";
	public static final String FORTEJOB = "src/test/java/resource/ForteJob.json";

	@BeforeTest
	public void setUp() {
		cache = PatientCache.getInstance();
		forteJob=new ForteJob();
	}

	@Test
	public void cacheTest() {
		cache.put("name", new SecondaryJobDetailsDTO());
		if (cache.containskey("name")) {
			cache.get("name");
		}
	}
	
	@Test
	public void ForteJobDTOTest() throws IOException {
		//new ObjectMapper().writeValue(new File("src/test/java/resource/ForteJob.json"), forteJob);
		String jsonfromFile = JsonFileReaderAsString.getJsonfromFile(FORTEJOB);
		forteJob = new ObjectMapper().readValue(jsonfromFile, ForteJob.class);
	}
	

}
