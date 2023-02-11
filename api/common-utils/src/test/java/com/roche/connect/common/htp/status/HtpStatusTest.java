package com.roche.connect.common.htp.status;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class HtpStatusTest {
    HtpStatus htpStatus;
    
    @BeforeTest
    public void setUp() {
        htpStatus = new HtpStatus();
    }
    
    @Test
    public void getHtpStatusTest() {
        htpStatus.setDeviceId("HTP-001");
        htpStatus.setDeviceName("HTP");
        htpStatus.setDeviceRunId("567890");
        htpStatus.setReceivingApplication("Connect");
        htpStatus.setReceivingFacility("Connect");
        htpStatus.setRunStatus("started");
        htpStatus.setSendingApplication("HTP");
        htpStatus.setSendingFacility("HTP");
        Assert.assertEquals("HTP-001", htpStatus.getDeviceId());
        Assert.assertEquals("HTP", htpStatus.getDeviceName());
        Assert.assertEquals("567890", htpStatus.getDeviceRunId());
        Assert.assertEquals("Connect", htpStatus.getReceivingApplication());
        Assert.assertEquals("Connect", htpStatus.getReceivingFacility());
        Assert.assertEquals("started", htpStatus.getRunStatus());
        Assert.assertEquals("HTP", htpStatus.getSendingApplication());
        Assert.assertEquals("HTP", htpStatus.getSendingFacility());
    }
}
