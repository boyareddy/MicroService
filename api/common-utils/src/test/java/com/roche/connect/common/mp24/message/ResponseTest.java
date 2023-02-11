package com.roche.connect.common.mp24.message;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ResponseTest {
    Response response;
    
    @BeforeTest
    public void setUp() {
        response = new Response();
    }
    
    @Test
    public void getResponseTest() {
        response.setAccessioningId("8001");
        response.setDateTimeMessageGenerated("20190220");
        response.setDeviceSerialNumber("MP24-001");
        response.setMessageControlId("8989");
        response.setMessageType("RSP");
        response.setReceivingApplication("Connect");
        RSPMessage rspMessage = new RSPMessage();
        rspMessage.setOrderNumber("12345");
        response.setRspMessage(rspMessage );
        response.setSendingApplicationName("MP24");
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setComment("comment");
        response.setStatusUpdate(statusUpdate );
        
        Assert.assertEquals("8001", response.getAccessioningId());
        Assert.assertEquals("20190220", response.getDateTimeMessageGenerated());
        Assert.assertEquals("MP24-001", response.getDeviceSerialNumber());
        Assert.assertEquals("8989", response.getMessageControlId());
        Assert.assertEquals("RSP", response.getMessageType());
        Assert.assertEquals("Connect", response.getReceivingApplication());
        Assert.assertEquals("12345", response.getRspMessage().getOrderNumber());
        Assert.assertEquals("MP24", response.getSendingApplicationName());
        Assert.assertEquals("comment", response.getStatusUpdate().getComment());
    }
}
