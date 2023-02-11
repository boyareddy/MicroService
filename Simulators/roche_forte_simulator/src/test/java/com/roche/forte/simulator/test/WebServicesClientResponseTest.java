package com.roche.forte.simulator.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.forte.simulator.WebServicesClientResponse;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.MessageBodyWorkers;


public class WebServicesClientResponseTest {
    @InjectMocks 
    WebServicesClientResponse webClient = new WebServicesClientResponse();
    
    @Mock
    WebResource webResource = org.mockito.Mockito.mock(WebResource.class);
   
   Logger logger = Logger.getLogger(WebServicesClientResponseTest.class);
   ClientResponse response = null;
   InBoundHeaders headers = null;
   InputStream entity = null;
   MessageBodyWorkers workers = null;
   JSONObject jsonInput = new JSONObject();
   String mediaType ="application/json";
   
   @BeforeTest
   public void setUp() {
       MockitoAnnotations.initMocks(this);
       headers = new InBoundHeaders();
       List<String> mediaTypes = new ArrayList<>();
       mediaTypes.add(MediaType.APPLICATION_JSON);
       headers.put(HttpHeaders.CONTENT_TYPE, mediaTypes);
       response = new ClientResponse(200, headers, entity, workers);
       jsonInput.put("instance_id","FORTE-01");
       jsonInput.put("forte_software_version", "v1");
       Mockito.when(webResource.post(ClientResponse.class)).thenReturn(response);
       Mockito.when(webResource.post(ClientResponse.class, jsonInput.toString())).thenReturn(response);
       Mockito.when(webResource.put(ClientResponse.class)).thenReturn(response);
       Mockito.when(webResource.put(ClientResponse.class, jsonInput.toString())).thenReturn(response);
       Mockito.when(webResource.get(ClientResponse.class)).thenReturn(response);
   }
    
   @Test public void getResponseTest() {
       try {
           webClient.getResponse("", "", "", null);
       } catch (Exception e) {
           logger.error(e.getMessage());
       }
   }
   
   @Test
   public void checkHTTPMethodsTest() {
       try {
           webClient.checkHTTPMethods(mediaType, "post", null);
           webClient.checkHTTPMethods(mediaType, "put", null);
           webClient.checkHTTPMethods(mediaType, "get", null);
           webClient.checkHTTPMethods(mediaType, "post", getRunObject());
           webClient.checkHTTPMethods(mediaType, "put", getRunObject());
           webClient.checkHTTPMethods(mediaType, "get", getRunObject());
           
       } catch (Exception e) {
           logger.error(e.getMessage());
       }
   }
   
   private JSONObject getRunObject() {
       JSONObject runObj = new JSONObject();
       runObj.put("instance_id", "FORTE-01");
       runObj.put("forte_software_version","v2");
       return runObj;
   }
}
