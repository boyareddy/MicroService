package com.roche.connect.common.htp;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RunResultTest {
    RunResult runResult;
    
    
    @BeforeTest
    public void setUp() {
        runResult = new RunResult();
    }
    
    @Test
    public void getRunResultTest() {
        runResult.setDeviceRunId("7979");
        runResult.setDeviceSerialNumber("MP24");
        runResult.setMessageType("QBP");
        Assert.assertEquals("7979", runResult.getDeviceRunId());
        Assert.assertEquals("MP24", runResult.getDeviceSerialNumber());
        Assert.assertEquals("QBP", runResult.getMessageType());
    }
}
