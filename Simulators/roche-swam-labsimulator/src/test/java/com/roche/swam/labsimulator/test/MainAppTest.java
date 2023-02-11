/*package com.roche.swam.labsimulator.test;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.Test;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.WebServicesClientResponse;
import com.roche.swam.labsimulator.engine.Engine;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.MessageBodyWorkers;

public class MainAppTest {
	
	@InjectMocks
	MainApp mainAppMock = new MainApp();
	
	@Mock
	WebServicesClientResponse webClientResponse = org.mockito.Mockito.mock(WebServicesClientResponse.class);
	
	@Mock
	Engine engineMock;
	
	@Mock
	Timer timer;
	@Mock
	ClientResponse response;
    Logger logger = Logger.getLogger(MainAppTest.class);
    InBoundHeaders headers = null;
    InputStream entity = null;
    MessageBodyWorkers workers = null;
   
	@Before
	    public void setup() throws Exception {
		  MockitoAnnotations.initMocks(this);
		  headers = new InBoundHeaders();
	        List<String> mediaTypes = new ArrayList<>();
	        mediaTypes.add(MediaType.APPLICATION_JSON);
	        headers.put(HttpHeaders.CONTENT_TYPE, mediaTypes);
	        response = new ClientResponse(200, headers, entity, workers);
		  Mockito.when(webClientResponse.getResponse(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyObject())).thenReturn(response);
		  MainApp.main(new String[] { "C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json" });
		  Assert.assertEquals("new String[] { \"C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json\" }", "C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json");
	    }
	
	
	@Test(priority=1)
	 public void testStop() throws Exception {
	    mainAppMock.stop();
		 Assert.assertEquals(false, mainAppMock.getServer().getServer().isRunning());		 
	 }
	
	
	
	 @Test(expectedExceptions = Exception.class,priority=2)
	 public void testStart() throws Exception {
		 mainAppMock.start("");
		 Assert.assertEquals(true, mainAppMock.getServer().getServer().isRunning());
	 }
	
	 @Test
	 public void helloTest()
	 {
		 try {
			mainAppMock.hello();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	 }
	 
	 
	 @Test(priority=3)
	 public void ping()
	 {
		 try {
			  MainApp.main(new String[] {"LP-PRE-PCR","LP-POST-PCR","LP-SEQ-PP"});
			 	mainAppMock.ping();
				 Assert.assertEquals(true, mainAppMock.getServer().getServer().isRunning());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		   
	 }

}
*/