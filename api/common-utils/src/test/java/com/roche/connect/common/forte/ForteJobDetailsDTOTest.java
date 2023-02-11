package com.roche.connect.common.forte;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ForteJobDetailsDTOTest {
    ForteJobDetailsDTO forteJobDetailsDTO;
    
    @BeforeTest public void setUp() {
        forteJobDetailsDTO = new ForteJobDetailsDTO();
    }
    
    @Test
    public void forteJobDetailsDTOTest() {
        forteJobDetailsDTO.setId("1");
        forteJobDetailsDTO.setKind("tertiary");
        Assert.assertEquals("1", forteJobDetailsDTO.getId());
        Assert.assertEquals("tertiary", forteJobDetailsDTO.getKind());
    }
}
