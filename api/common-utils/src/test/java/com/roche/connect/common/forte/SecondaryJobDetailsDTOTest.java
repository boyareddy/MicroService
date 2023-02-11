package com.roche.connect.common.forte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SecondaryJobDetailsDTOTest {
    SecondaryJobDetailsDTO secondaryJobDetailsDTO;
    
    @BeforeTest public void setUp() {
        secondaryJobDetailsDTO = new SecondaryJobDetailsDTO();
    }
    
    
    @Test
    public void secondaryJobDetailsDTOTest() {
        secondaryJobDetailsDTO.setId("1");
        secondaryJobDetailsDTO.setKind("secondary");
        secondaryJobDetailsDTO.setSecondaryChecksum("56789");
        secondaryJobDetailsDTO.setSecondaryInfile("sec.txt");
        List<Map<String, String>> secondarySampleDetails = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("sampleType", "Blood");
        secondarySampleDetails.add(map);
        secondaryJobDetailsDTO.setSecondarySampleDetails(secondarySampleDetails);
  
        Assert.assertEquals("1", secondaryJobDetailsDTO.getId());
        Assert.assertEquals("secondary", secondaryJobDetailsDTO.getKind());
        Assert.assertEquals("56789", secondaryJobDetailsDTO.getSecondaryChecksum());
        Assert.assertEquals("sec.txt", secondaryJobDetailsDTO.getSecondaryInfile());
        Assert.assertEquals("Blood", secondaryJobDetailsDTO.getSecondarySampleDetails().get(0).get("sampleType"));
    }
}
