package com.roche.forte.simulator.test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.forte.simulator.ForteAnalyzerPanel;
import com.roche.forte.simulator.WebServicesClientResponse;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.MessageBodyWorkers;

public class ForteAnalyzerPanelTest {
    
    @InjectMocks ForteAnalyzerPanel forteAnalyzer = new ForteAnalyzerPanel();
    
    ForteAnalyzerPanel spyForte = Mockito.spy(forteAnalyzer);
    
    @Mock WebServicesClientResponse webService = org.mockito.Mockito.mock(WebServicesClientResponse.class);
    
    String url = null;
    String type = null;
    String methodType = null;
    ClientResponse response = null;
    int expectedCorrectStatusCode = 0;
    InBoundHeaders headers = null;
    InputStream entity = null;
    MessageBodyWorkers workers = null;
    
    Logger logger = Logger.getLogger(ForteAnalyzerPanelTest.class);
    
    @BeforeTest public void setUp() throws Exception {
        url = "http://localhost:98/forte-adapter/api/forte/ping";
        type = "application/json";
        methodType = "post";
        
        headers = new InBoundHeaders();
        List<String> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        headers.put(HttpHeaders.CONTENT_TYPE, mediaTypes);
        
        response = new ClientResponse(200, headers, entity, workers);
        MockitoAnnotations.initMocks(this);
        Mockito.doReturn("{\"id\":\"1\"}").when(spyForte).displayResponseDetails(response);
        System.out.println("Test spy Method response: "+spyForte.displayResponseDetails(response));
        Mockito.when(webService.getResponse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(response);
        System.out.println(response.getStatus());
    }
    
    @Test public void mainTest() {
        try {
            ForteAnalyzerPanel.main(new String[]{});
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void pingTest() {
        try {
            forteAnalyzer.ping();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void helloTest() {
        try {
            forteAnalyzer.hello();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void getJobQueueTest() {
        try {
            forteAnalyzer.getJobQueue();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void checkAnalysisTypeTest() {
        try {
            String secondaryJobDetails = "{\"kind\":\"secondary\",\"id\":\"1\",\"secondary_infile\": \"/mnt/ips/htp1\",  \"secondary_checksum\":\"12345\"}";
            String tertiaryJobDetails = "{\"kind\":\"tertiary\",\"id\":\"2\",\"tertiary_infiles\": \"[\\\"/mnt/ips/htp1\\\", \\\"/mnt/ips/htp2\\\" ]\",\"tertiary_checksums\": \"[12345, 23446, ...]\"}";
            forteAnalyzer.checkAnalysisType(secondaryJobDetails); // secondary
            forteAnalyzer.checkAnalysisType(tertiaryJobDetails); // tertiary
            forteAnalyzer.checkAnalysisType(""); // catch-block coverage
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void updateJobStartQueueTest() {
        try {
            forteAnalyzer.updateJobStartQueue("1","secondary");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void updateJobInprogressQueueTest() {
        try {
            forteAnalyzer.updateJobInprogressQueue("1","secondary");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void updateJobDoneQueueTest() {
        try {
            forteAnalyzer.updateJobDoneQueue("1","secondary");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void updateJobErrorQueueTest() {
        try {
            forteAnalyzer.updateJobErrorQueue("1");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void fileStorageDirectoryTest() {
        try {
            forteAnalyzer.fileStorageDirectory("C://ips","secondary");
            forteAnalyzer.fileStorageDirectory("C://ips","tertiary");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
/*    @Test
    public void getChecksumTest() {
        try {
            forteAnalyzer.getChecksum(new File("C:\\ips\\HTP-01\\runs\\181025_xyz_L03\\cycle03\\20181025_xyz_L03_cycle03_fastq.gz"));
            forteAnalyzer.getChecksum(new File("C://")); // catch-block coverage
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }*/
    
    @Test public void displayResponseDetailsTest() {
        try {
            forteAnalyzer.displayResponseDetails(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void readPropertiesTest() {
        try {
            forteAnalyzer.readProperties("hostUrl");
            forteAnalyzer.readProperties("url"); // catch-block coverage
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void readJsonObjectTest() {
        try {
            forteAnalyzer.readJsonObject("instrumentInformationPath");
            forteAnalyzer.readJsonObject("instrumentInformationPaths");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @AfterTest public void afterTest() {
        url = null;
        type = null;
        methodType = null;
    }
}
