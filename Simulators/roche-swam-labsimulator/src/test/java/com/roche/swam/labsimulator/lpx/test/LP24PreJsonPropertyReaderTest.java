package com.roche.swam.labsimulator.lpx.test;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.swam.labsimulator.mpx.bl.Sample;
import com.roche.swam.labsimulator.util.LP24PreJsonPropertyReader;


public class LP24PreJsonPropertyReaderTest {
    
    @InjectMocks LP24PreJsonPropertyReader lp24PreJsonPropertyReaderMock;
    
    @BeforeTest public void setUp() {
        if(lp24PreJsonPropertyReaderMock != null) {
            MockitoAnnotations.initMocks(this);
            lp24PreJsonPropertyReaderMock = new LP24PreJsonPropertyReader();
        }else {
            System.out.println("Test class Constructor instantiation failed.");
        }
    }
    
    @Test public void testReadJsonData() {
        try {
            if(lp24PreJsonPropertyReaderMock != null) {
                Sample sample = lp24PreJsonPropertyReaderMock.readJsonData("116362473_1");
                Assert.assertEquals("17270", sample.getSampleId());
                Assert.assertEquals("2.5.1", sample.getVersionId());
                Assert.assertEquals("Roche Diagnostics", sample.getReceivingFacilityName());
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
