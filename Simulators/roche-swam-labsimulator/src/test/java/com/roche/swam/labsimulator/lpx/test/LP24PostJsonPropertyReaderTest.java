package com.roche.swam.labsimulator.lpx.test;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.swam.labsimulator.mpx.bl.Sample;
import com.roche.swam.labsimulator.util.LP24PostJsonPropertyReader;



public class LP24PostJsonPropertyReaderTest {
    
    @InjectMocks LP24PostJsonPropertyReader lp24PostJsonPropertyReaderMock;
    
    @BeforeTest public void setUp() {
        if(lp24PostJsonPropertyReaderMock != null) {
            MockitoAnnotations.initMocks(this);
            lp24PostJsonPropertyReaderMock = new LP24PostJsonPropertyReader();
        }else {
            System.out.println("Test class Constructor instantiation failed.");
        }
    }
    
    @Test 
    public void testReadJsonData() {
        try {
            if(lp24PostJsonPropertyReaderMock != null) {
                Sample sample = lp24PostJsonPropertyReaderMock.readJsonData("232459054_A1");
                System.out.println(sample);
                Assert.assertEquals("116362473_1", sample.getSampleId());
                Assert.assertEquals("2.5.1", sample.getVersionId());
                Assert.assertEquals("Roche Diagnostics", sample.getReceivingFacilityName());
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
