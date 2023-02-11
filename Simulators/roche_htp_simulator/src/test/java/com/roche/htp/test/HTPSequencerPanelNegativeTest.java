/*package com.roche.htp.test;

import java.io.ByteArrayInputStream;
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
import org.mockito.Spy;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.htp.simulator.HTPSequencerPanel;
import com.roche.htp.simulator.WebServicesClientResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.MessageBodyWorkers;

public class HTPSequencerPanelNegativeTest {
    
    @InjectMocks HTPSequencerPanel htpSequencerPanelTest = new HTPSequencerPanel();
    
    @Mock WebServicesClientResponse clientMock =
        org.mockito.Mockito.mock(WebServicesClientResponse.class);
    
    String url = null;
    String type = null;
    String methodType = null;
    JSONObject input = null;
    ClientResponse response = null;
    int expectedCorrectStatusCode = 0;
    Logger logger = Logger.getLogger(HTPSequencerPanelNegativeTest.class);
    
    InBoundHeaders headers = null;
    
    @BeforeTest public void setUp() throws Exception {
        InputStream entity = null;
        MessageBodyWorkers workers = null;
        url = "http://localhost:98/htp-adapter/json/rest/api/htp/ping";
        DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
        Client client = Client.create(defaultClientConfig);
        headers = new InBoundHeaders();
        List<String> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        headers.put(HttpHeaders.CONTENT_TYPE, mediaTypes);
        
        
        response = new ClientResponse(200, headers, entity, workers);
        
        JSONObject obj = new JSONObject();
        obj.put("run_id","xyz");

        String str = obj.toString();
        InputStream is = new ByteArrayInputStream(str.getBytes());
        response.setEntityInputStream(is);
        System.out.println(response.toString());
        
        type = "application/json";
        methodType = "post";
        input = new JSONObject();
        input.put("run_id", "xyz");
        MockitoAnnotations.initMocks(this);
        Mockito.when(clientMock.getResponse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
        System.out.println(response.getStatus());
    }
    
    @Test public void pingNegativeTestCase() {
        htpSequencerPanelTest.ping();
    }
    
    @AfterTest public void afterTest() {
        url = null;
        type = null;
        methodType = null;
        input = null;
    }
    
}
*/