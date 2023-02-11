package com.roche.connect.common.htp.status;

import org.testng.Assert;
import org.testng.annotations.Test;


public class HTPStatusConstantsTest {
    
    
    @Test
    public void hTPStatusConstantsTest() {
        Assert.assertEquals("Completed", HTPStatusConstants.COMPLETED.getText());
        Assert.assertEquals("Created", HTPStatusConstants.CREATED.getText());
        Assert.assertEquals("Failed", HTPStatusConstants.FAILED.getText());
        Assert.assertEquals("InProcess", HTPStatusConstants.IN_PROCESS.getText());
        Assert.assertEquals("InProgress", HTPStatusConstants.IN_PROGRESS.getText());
        Assert.assertEquals("Started", HTPStatusConstants.STARTED.getText());
        Assert.assertEquals("TransferCompleted", HTPStatusConstants.TRANSFERCOMPLETE.getText());
    }
}
