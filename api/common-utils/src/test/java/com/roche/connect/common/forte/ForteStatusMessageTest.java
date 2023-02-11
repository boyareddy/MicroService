package com.roche.connect.common.forte;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ForteStatusMessageTest {
    ForteStatusMessage forteStatusMessage;
    
    @BeforeTest public void setUp() {
        forteStatusMessage = new ForteStatusMessage();
    }
    
    
    @Test
    public void forteStatusMessageTest() {
        forteStatusMessage.setAccessioningId("8001");
        forteStatusMessage.setDeviceId("FORTE-001");
        forteStatusMessage.setJobType(ForteJobTypes.TERTIARY.toString());
        forteStatusMessage.setSendingApplication("FORTE");
        forteStatusMessage.setStatus("start");
        Assert.assertEquals("8001", forteStatusMessage.getAccessioningId());
        Assert.assertEquals("FORTE-001", forteStatusMessage.getDeviceId());
        Assert.assertEquals("TERTIARY", forteStatusMessage.getJobType());
        Assert.assertEquals("FORTE", forteStatusMessage.getSendingApplication());
        Assert.assertEquals("start", forteStatusMessage.getStatus());
    }
}
