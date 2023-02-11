package com.roche.connect.common.htp.status;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class StartTest {
    Start start;
    
    @BeforeTest
    public void setUp() {
        start = new Start();
    }
    
    @Test
    public void startTest() {
        start.setRunId(123L);
        start.setRunProtocol("HTP Protocol");
        start.setEstimatedTimeRemaining(123L);
        start.setEstimatedTimeRemainingBothRuns(12345L);
        Assert.assertSame(123L, start.getRunId());
        Assert.assertEquals("HTP Protocol", start.getRunProtocol());
        Assert.assertSame(123L, start.getEstimatedTimeRemaining());
        Assert.assertEquals(12345L, start.getEstimatedTimeRemainingBothRuns());
    }
}
