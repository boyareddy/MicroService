package com.roche.connect.common.mp24.message;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class RSPMessageTest {
    RSPMessage rSPMessage;
    
    
    @BeforeTest
    public void setUp() {
        rSPMessage = new RSPMessage();
    }
    
    @Test
    public void getRSPMessageTest() {
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setAvailableSpecimenVolume("200ml");
        rSPMessage.setContainerInfo(containerInfo);
        rSPMessage.setEluateVolume("100ml");
        rSPMessage.setOrderControl("NA");
        rSPMessage.setOrderEventDate("20201202");
        rSPMessage.setOrderNumber("3");
        rSPMessage.setOrderStatus("Passed");
        rSPMessage.setProtocolDescription("description");
        rSPMessage.setProtocolName("ffPET");
        rSPMessage.setQueryResponseStatus("Passed");
        rSPMessage.setResultStatus("Passed");
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setContainerType("96wellplate");
        rSPMessage.setSampleInfo(sampleInfo);
        
        Assert.assertEquals("200ml", rSPMessage.getContainerInfo().getAvailableSpecimenVolume());
        Assert.assertEquals("100ml", rSPMessage.getEluateVolume());
        Assert.assertEquals("NA", rSPMessage.getOrderControl());
        Assert.assertEquals("20201202", rSPMessage.getOrderEventDate());
        Assert.assertEquals("3", rSPMessage.getOrderNumber());
        Assert.assertEquals("Passed", rSPMessage.getOrderStatus());
        Assert.assertEquals("description", rSPMessage.getProtocolDescription());
        Assert.assertEquals("ffPET", rSPMessage.getProtocolName());
        Assert.assertEquals("Passed", rSPMessage.getQueryResponseStatus());
        Assert.assertEquals("Passed", rSPMessage.getResultStatus());
        Assert.assertEquals("96wellplate", rSPMessage.getSampleInfo().getContainerType());
    }
}
