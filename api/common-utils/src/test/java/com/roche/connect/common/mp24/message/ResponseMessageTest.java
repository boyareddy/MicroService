package com.roche.connect.common.mp24.message;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ResponseMessageTest {
    ResponseMessage responseMessage;
    
    
    @BeforeTest
    public void setUp() {
        responseMessage = new ResponseMessage();
    }
    
    @Test
    public void getResponseMessageTest() {
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setAvailableSpecimenVolume("200ml");
        responseMessage.setContainerInfo(containerInfo );
        responseMessage.setContainerTypeDescription("description");
        responseMessage.setContainerTypeID("12");
        responseMessage.setDateTimeSpecimenExpiration("20201120");
        responseMessage.setOrderControl("orderControl");
        responseMessage.setOrderEventDate("20190219");
        responseMessage.setOrderNumber("6");
        responseMessage.setOrderStatus("Completed");
        responseMessage.setProtocolDescription("protocol description");
        responseMessage.setProtocolName("ctDNA");
        responseMessage.setResultStatus("Passed");
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setContainerType("96wellplate");
        responseMessage.setSampleInfo(sampleInfo );
        
        Assert.assertEquals("200ml", responseMessage.getContainerInfo().getAvailableSpecimenVolume());
        Assert.assertEquals("description", responseMessage.getContainerTypeDescription());
        Assert.assertEquals("12", responseMessage.getContainerTypeID());
        Assert.assertEquals("20201120", responseMessage.getDateTimeSpecimenExpiration());
        Assert.assertEquals("orderControl", responseMessage.getOrderControl());
        Assert.assertEquals("20190219", responseMessage.getOrderEventDate());
        Assert.assertEquals("6", responseMessage.getOrderNumber());
        Assert.assertEquals("Completed", responseMessage.getOrderStatus());
        Assert.assertEquals("protocol description", responseMessage.getProtocolDescription());
        Assert.assertEquals("ctDNA", responseMessage.getProtocolName());
        Assert.assertEquals("Passed", responseMessage.getResultStatus());
        Assert.assertEquals("96wellplate", responseMessage.getSampleInfo().getContainerType());
    }
}
