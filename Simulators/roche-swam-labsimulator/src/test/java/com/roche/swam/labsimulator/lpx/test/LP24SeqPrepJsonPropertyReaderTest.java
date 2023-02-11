package com.roche.swam.labsimulator.lpx.test;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.swam.labsimulator.mpx.bl.Sample;
import com.roche.swam.labsimulator.util.LP24SeqPrepJsonPropertyReader;

public class LP24SeqPrepJsonPropertyReaderTest {
    
    @InjectMocks LP24SeqPrepJsonPropertyReader lp24SeqPrepJsonPropertyReaderMock;
    
    @BeforeTest public void setUp() {
        if (lp24SeqPrepJsonPropertyReaderMock != null) {
            MockitoAnnotations.initMocks(this);
            lp24SeqPrepJsonPropertyReaderMock = new LP24SeqPrepJsonPropertyReader();
        } else {
            System.out.println("Test class Constructor instantiation failed.");
        }
    }
    
    @Test public void testReadJsonData() {
        try {
            if (lp24SeqPrepJsonPropertyReaderMock != null) {
                Sample sample = lp24SeqPrepJsonPropertyReaderMock.readJsonData("391837859_RGQMU");
                Assert.assertEquals("109822962_A1", sample.getSampleId());
                Assert.assertEquals("2.5.1", sample.getVersionId());
                Assert.assertEquals("Roche Diagnostics", sample.getReceivingFacilityName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
