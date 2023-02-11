package com.roche.connect.common.mp24.message;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AdaptorRequestMessageTest {
    AdaptorRequestMessage adaptorRequestMessage;
    
    @BeforeTest
    public void setUp() {
        adaptorRequestMessage = new AdaptorRequestMessage();
    }
    
    @Test
    public void getAdaptorRequestMessageTest() {
        adaptorRequestMessage.setAccessioningId("8001");
        adaptorRequestMessage.setReceivingApplication("Connect");
        adaptorRequestMessage.setDateTimeMessageGenerated("20190220");
        adaptorRequestMessage.setDeviceSerialNumber("MP24-001");
        adaptorRequestMessage.setMessageControlId("89989");
        adaptorRequestMessage.setMessageType("QBP");
        adaptorRequestMessage.setReceivingApplication("Connect");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setOrderStatus("Duplicate");
        adaptorRequestMessage.setResponseMessage(responseMessage );
        adaptorRequestMessage.setSendingApplicationName("MP24-001");
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setOrderResult("Passed");
        adaptorRequestMessage.setStatusUpdate(statusUpdate );
        
        
        Assert.assertEquals("8001", adaptorRequestMessage.getAccessioningId());
        Assert.assertEquals("Connect", adaptorRequestMessage.getReceivingApplication());
        Assert.assertEquals("20190220", adaptorRequestMessage.getDateTimeMessageGenerated());
        Assert.assertEquals("MP24-001", adaptorRequestMessage.getDeviceSerialNumber());
        Assert.assertEquals("89989", adaptorRequestMessage.getMessageControlId());
        Assert.assertEquals("QBP", adaptorRequestMessage.getMessageType());
        Assert.assertEquals("Connect", adaptorRequestMessage.getReceivingApplication());
        Assert.assertEquals("Duplicate", adaptorRequestMessage.getResponseMessage().getOrderStatus());
        Assert.assertEquals("MP24-001", adaptorRequestMessage.getSendingApplicationName());
        Assert.assertEquals("Passed", adaptorRequestMessage.getStatusUpdate().getOrderResult());
    }
}
