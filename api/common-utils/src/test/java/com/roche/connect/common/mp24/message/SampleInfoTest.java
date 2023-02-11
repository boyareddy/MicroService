package com.roche.connect.common.mp24.message;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class SampleInfoTest {
    SampleInfo sampleInfo;
    
    @BeforeTest
    public void setUp() {
        sampleInfo = new SampleInfo();
    }
    
    @Test
    public void getSampleInfoTest() {
        sampleInfo.setContainerType("type");
        sampleInfo.setDateTimeSpecimenCollected("20200210");
        sampleInfo.setDateTimeSpecimenExpiration("20200210");
        sampleInfo.setDateTimeSpecimenReceived("20200210");
        sampleInfo.setSampleOutputId("2");
        sampleInfo.setSampleOutputPosition("3");
        sampleInfo.setSampleType("Whole Blood");
        sampleInfo.setSpecimenCollectionSite("site");
        sampleInfo.setSpecimenDescription("description");
        sampleInfo.setSpecimenRole("role");
        sampleInfo.setSpecimenSourceSite("sourceSite");
        
        Assert.assertEquals("type", sampleInfo.getContainerType());
        Assert.assertEquals("20200210", sampleInfo.getDateTimeSpecimenCollected());
        Assert.assertEquals("20200210", sampleInfo.getDateTimeSpecimenExpiration());
        Assert.assertEquals("20200210", sampleInfo.getDateTimeSpecimenReceived());
        Assert.assertEquals("2", sampleInfo.getSampleOutputId());
        Assert.assertEquals("3", sampleInfo.getSampleOutputPosition());
        Assert.assertEquals("Whole Blood", sampleInfo.getSampleType());
        Assert.assertEquals("site", sampleInfo.getSpecimenCollectionSite());
        Assert.assertEquals("description", sampleInfo.getSpecimenDescription());
        Assert.assertEquals("role", sampleInfo.getSpecimenRole());
        Assert.assertEquals("sourceSite", sampleInfo.getSpecimenSourceSite());
        
    }
}
