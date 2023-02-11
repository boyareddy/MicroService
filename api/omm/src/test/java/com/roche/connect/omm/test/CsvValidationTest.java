package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.omm.model.ContainerSamples;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.CSVParserUtil;
import com.roche.connect.omm.util.ResourceBundleUtil;

@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class CsvValidationTest {
    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    int expectedIncorrectStatusCode = 0;
    List<String> parsedCSV = null;
    List<String> msgList = null;
    Map<String, List<String>> rowErrors = null;
    @Mock
    CSVParserUtil cSVParserUtil = org.mockito.Mockito.mock(CSVParserUtil.class);
    @Spy
    ResourceBundleUtil resourceBundleUtil=Mockito.spy(new ResourceBundleUtil());
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    /*@InjectMocks
    CsvValidationTest csvValidationTest=new CsvValidationTest();*/
    @Mock
    ContainerSamplesReadRepository containerSamplesReadRepository=org.mockito.Mockito.mock(ContainerSamplesReadRepository.class);
    List<ContainerSamples> containerSamples=null;
    String csvFilePath = null;
    InputStream stream = null;
    File csvFile = null;
    String row, msg, message, successMsg, erroMsg = null;

    private List<ContainerSamples> containerSample;

    @BeforeTest
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        csvFilePath = "src/test/java/resource/samplecsv.csv";

        csvFile = new File(csvFilePath);
        stream = new FileInputStream(csvFile);

        successMsg = "CSV validation done successfully";
        erroMsg = "CSV validation error occured";
        msgList = new ArrayList<>();
        parsedCSV = cSVParserUtil.parseCSV(stream);
        rowErrors = new HashMap<String, List<String>>();
        containerSample=new ArrayList<>();
        containerSample.add(getContainerSamples());
      }

    @Parameters({ "OrderManagementUnitTest" })
    @Test(priority = 1)
    public void csvValidationPosTest() throws ParseException, HMTPException {
           Mockito.when(containerSamplesReadRepository.findByContainerID(any(String.class), any(long.class))).thenReturn(containerSample);
        rowErrors = orderService.validateCSV(getParserString());
        
        if (rowErrors.isEmpty()) {
            msgList.add(successMsg);
            assertEquals(msgList.get(0), successMsg);
        } else {
            msgList.add(erroMsg);
            assertEquals(msgList.get(0), erroMsg);
        }
    }
    @Parameters({ "OrderManagementUnitTest" })
    @Test(priority = 2)
    public void csvValidationEmptyTest() throws ParseException, HMTPException {
        Mockito.when(containerSamplesReadRepository.findByContainerID(any(String.class), any(long.class))).thenReturn(containerSample);
        rowErrors = orderService.validateCSV(parsedCSV);
        
        if (rowErrors.isEmpty()) {
            msgList.add(successMsg);
            assertEquals(msgList.get(0), successMsg);
        } else {
            msgList.add(erroMsg);
            assertEquals(msgList.get(0), erroMsg);
        }
    }
    @Parameters({ "OrderManagementUnitTest" })
    @Test(priority = 3)
    public void csvValidationheaderErrorsTest() throws ParseException, HMTPException {
        rowErrors = orderService.validateCSV(getParserStringHeader());
        
        if (rowErrors.isEmpty()) {
            msgList.add(successMsg);
            assertEquals(msgList.get(0), successMsg);
        } else {
            msgList.add(erroMsg);
            assertEquals(msgList.get(0), erroMsg);
        }
    }
    @Parameters({ "OrderManagementUnitTest" })
    @Test(priority = 4)
    public void csvValidationDelimeterTest() throws ParseException, HMTPException {
        rowErrors = orderService.validateCSV(getParserStringDelimeter());
        
        if (rowErrors.isEmpty()) {
            msgList.add(successMsg);
            assertEquals(msgList.get(0), successMsg);
        } else {
            msgList.add(erroMsg);
            assertEquals(msgList.get(0), erroMsg);
        }
    }
    @Parameters({ "OrderManagementUnitTest" })
    @Test(priority = 5)
    public void csvValidationExceptionTest() throws HMTPException {
        Mockito.when(containerSamplesReadRepository.findByContainerID(any(String.class), any(long.class))).thenReturn(null);
         orderService.validateCSV(getParserString());      
        }
    private List<String> getParserString(){
        return  Arrays.asList("Container Type,Container ID,Position,Accessioning ID","96 wellplate,wp-502,A1,file1");   
        }
    private List<String> getParserStringHeader(){
        return  Arrays.asList("Container Type,Container ID,Position,Accessioning ID");   
        }
    private List<String> getParserStringDelimeter(){
        return  Arrays.asList("Container Type.,,Container ID,Position,Accessioning ID");   
        }
   
    private ContainerSamples getContainerSamples() {
        ContainerSamples containerSampless = new ContainerSamples();
        containerSampless.setAccessioningID("file1");
        containerSampless.setContainerID("wp-502");
        return containerSampless;
    }
   

}
