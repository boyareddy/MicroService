package com.roche.connect.common.forte;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SecondarySampleAssayDetailsTest {
    SecondarySampleAssayDetails secondarySampleAssayDetails;
    
    @BeforeTest public void setUp() {
        secondarySampleAssayDetails = new SecondarySampleAssayDetails();
    }
    
    @Test
    public void secondarySampleAssayDetailsTest() {
        secondarySampleAssayDetails.setAccessioningId("8001");
        secondarySampleAssayDetails.setEggDonor("2");
        secondarySampleAssayDetails.setFetusNumber("1");
        secondarySampleAssayDetails.setGestationalAgeDays(20);
        secondarySampleAssayDetails.setGestationalAgeWeeks(3);
        secondarySampleAssayDetails.setMaternalAge(23);
        secondarySampleAssayDetails.setMolecularId("56789");
        Assert.assertEquals("8001", secondarySampleAssayDetails.getAccessioningId());
        Assert.assertEquals("2", secondarySampleAssayDetails.getEggDonor());
        Assert.assertEquals("1", secondarySampleAssayDetails.getFetusNumber());
        Assert.assertSame(20, secondarySampleAssayDetails.getGestationalAgeDays());
        Assert.assertSame(3, secondarySampleAssayDetails.getGestationalAgeWeeks());
        Assert.assertSame(23, secondarySampleAssayDetails.getMaternalAge());
        Assert.assertEquals("56789", secondarySampleAssayDetails.getMolecularId());
    }
}
