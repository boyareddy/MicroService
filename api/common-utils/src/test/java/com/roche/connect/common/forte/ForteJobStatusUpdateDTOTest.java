package com.roche.connect.common.forte;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ForteJobStatusUpdateDTOTest {
    ForteJobStatusUpdateDTO forteJobStatusUpdateDTO;
    
    @BeforeTest public void setUp() {
        forteJobStatusUpdateDTO = new ForteJobStatusUpdateDTO();
    }
    
    @Test
    public void forteJobDetailsDTOTest() {
        forteJobStatusUpdateDTO.setComplexId("8001");
        forteJobStatusUpdateDTO.setJobStatus("done");
        forteJobStatusUpdateDTO.setJobType(ForteJobTypes.SECONDARY);
        Assert.assertEquals("8001", forteJobStatusUpdateDTO.getComplexId());
        Assert.assertEquals("done", forteJobStatusUpdateDTO.getJobStatus());
        Assert.assertEquals("SECONDARY", forteJobStatusUpdateDTO.getJobType().toString());
    }
}
