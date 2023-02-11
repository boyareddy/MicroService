package com.roche.connect.common.htp.status;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class InProgressTest {
    InProgress inProgress;
    
    @BeforeTest
    public void setUp() {
        inProgress = new InProgress();
    }
    
    @Test
    public void getInProgressTest() {
        inProgress.setEstimatedTimeRemaining(123L);
        inProgress.setEstimatedTimeRemainingBothRuns(1234L);
       Assert.assertSame(123L, inProgress.getEstimatedTimeRemaining());
       Assert.assertEquals(1234, inProgress.getEstimatedTimeRemainingBothRuns());
    }
}
