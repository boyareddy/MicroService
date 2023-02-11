package com.roche.connect.wfm.test.testng;
/*package com.roche.connect.wfm.service;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.wfm.ApplicationBootWFM;
import com.roche.connect.wfm.test.util.JsonFileReaderAsString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootWFM.class, webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,WebMvcAutoConfiguration.class })
public class ApplicationBootWFMTest 
{
	@LocalServerPort
	private int port;	
	
	public static final String sampleJSON = "src/test/java/resource/QbpMp24.json";
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();	
	@Test
	public void getExecuteStartWFM() throws JsonParseException, JsonMappingException, IOException
	{
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		String jsonfromFile = JsonFileReaderAsString.getJsonfromFile(sampleJSON);
		AdaptorRequestMessage adaptorRequestMessage = new ObjectMapper().readValue(jsonfromFile, AdaptorRequestMessage.class); 
		HttpEntity<AdaptorRequestMessage> entity = new HttpEntity<AdaptorRequestMessage>(adaptorRequestMessage, headers);
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/wfm/anonymous/json/rest/api/v1/startwfmprocess"),
				HttpMethod.POST, entity, String.class);
		int statusCode = response.getStatusCodeValue();
		assertEquals(200, statusCode);
	}
	private String createURLWithPort(String url) 
	{
		return "http://localhost:"+this.port+url;
	}
	
}
*/