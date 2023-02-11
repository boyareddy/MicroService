/*package com.roche.htp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.htp.simulator.HTPSequencerPanel;
import com.roche.htp.simulator.WebServicesClientResponse;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.MessageBodyWorkers;

public class HTPSequencerPanelTest {
    
    @InjectMocks HTPSequencerPanel htpPanel = new HTPSequencerPanel();
    
    @Mock WebServicesClientResponse webService = org.mockito.Mockito.mock(WebServicesClientResponse.class);
    JSONObject json = null;
    
    Logger logger = Logger.getLogger(HTPSequencerPanelTest.class);
    String runId ="123455";
    Integer cycleNo = 3;
    Boolean isLastCycle = false;
    String types="fastq.gz";
    
    String url = null;
    String type = null;
    String methodType = null;
    ClientResponse response = null;
    int expectedCorrectStatusCode = 0;
    InBoundHeaders headers = null;
    InputStream entity = null;
    MessageBodyWorkers workers = null;
    
    
    @BeforeTest public void setUp() {
        MockitoAnnotations.initMocks(this);
        url = "http://localhost:98/forte-adapter/api/forte/ping";
        type = "application/json";
        methodType = "post";
        
        headers = new InBoundHeaders();
        List<String> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        headers.put(HttpHeaders.CONTENT_TYPE, mediaTypes);
        
        response = new ClientResponse(200, headers, entity, workers);
        Mockito.when(webService.getResponse(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(response);
        json = getRunObject();
        
    }
    
    @Test public void mainTest() {
        try {
            HTPSequencerPanel.main(new String[]{"0","1","2","3","4","5","6","7","8 2","9 3 false","10"});
            
        } catch (Exception e) {
            logger.error(e.getMessage());
            Assert.fail("Main Method testing failed");
        }
    }
    
    @Test public void getConfigTest() {
        try {
            htpPanel.getConfig();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Assert.fail("config test failed");
        }
    }
    
    @Test public void pingTest() {
        try {
            htpPanel.ping();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void helloTest() {
        try {
            htpPanel.hello();
            htpPanel.displayResponseDetails(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void getOrderDetailsByComplexIdTest() {
        try {
            htpPanel.getOrderDetailsByComplexId();
            
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    
    @Test public void runsTest() {
        
        try {
            htpPanel.runs(json);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void runUpdateByRunIdTest() {
        try {
            htpPanel.runUpdateByRunId("12345");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void getRunDetailsByRunIdTest() {
        try {
            htpPanel.getRunDetailsByRunId("12345");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test
    
    public void updateRunByStartedTest() {
        try {
            htpPanel.updateRunByStarted("12345");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test
    
    public void updateRunByInProcessTest() {
        try {
            htpPanel.updateRunByInProcess("12345");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test
    
    public void updateFinalRunTest() {
        try {
            htpPanel.updateFinalRun("12345");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
   
    
    @Test public void notificationsTest() {
        try {
            htpPanel.notifications("error");
            htpPanel.notifications("warning");
            htpPanel.notifications("informational");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void getRunIdTest() {
        try {
            htpPanel.getRunId();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void getChecksumTest() {
        try {
            htpPanel.getChecksum(new File("resources//instrumentInformation.json"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
   
    @Test public void getChecksumStreamTest() throws FileNotFoundException {
            htpPanel.getChecksum(new FileInputStream(new File("resources//instrumentInformation.json")));
    }
    
    @Test public void createCycleFileAndGetChecksumTest() {
    		htpPanel.createCycleFileAndGetChecksum(htpPanel.readProperties("mountIp"), htpPanel.readProperties("mountUsername"),"Connect@123" , Integer.parseInt(htpPanel.readProperties("mountPort")), "/home/rconnect/roche/ips/", "20181116_xyz_L02_cycle00_ExpState.json");
    }
   
    @Test
    public void iterateCycleTest() {
        try {
            htpPanel.iterateCycle(runId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test
    public void updateCycleTest() {
        try {
            htpPanel.updateCycle(runId, cycleNo, isLastCycle);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test
    public void getFileChecksumWindowsTest() {
    	htpPanel.getFileChecksumWindows("resources//test//testingChecksum.txt");
    }
    @Test
    public void getFileChecksumIPSTest() {
    	htpPanel.createCycleFileAndGetChecksum(htpPanel.readProperties("mountIp"), htpPanel.readProperties("mountUsername"),"Connect@123" , Integer.parseInt(htpPanel.readProperties("mountPort")), "/home/rconnect/roche/ips/", "20181116_xyz_L02_cycle00_ExpState.json");
    	htpPanel.getFileChecksumIPS("runs/181116_xyz_L02/cycle00", "20181116_xyz_L02_cycle00_ExpAnno.json");
    }    
    
    @Test
    public void getDirectoryFreeSpaceTest() {
        try {
            htpPanel.getDirectoryFreeSpace();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    @Test public void displayResponseDetailsTest() {
        try {
            htpPanel.displayResponseDetails(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void readPropertiesTest() {
        try {
            htpPanel.readProperties("hostUrl");
            htpPanel.readProperties("url"); // catch-block coverage
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void readJsonObjectTest() {
        try {
            htpPanel.readJsonObject("runPath");
            htpPanel.readJsonObject("runPaths"); // catch-block coverage
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        
    }
    
    
    @Test public void cycleCompleTest() {
        try {
            htpPanel.cycleComplete("12345", 3);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test public void transferCompleteTest() {
        try {
            htpPanel.transferComplete("12345");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    
    private JSONObject getRunObject() {
        JSONObject runObj = new JSONObject();
        runObj.put("run_id", "12300001");
        runObj.put("run_protocol", "NIPT");
        return runObj;
    }
    
}
*/