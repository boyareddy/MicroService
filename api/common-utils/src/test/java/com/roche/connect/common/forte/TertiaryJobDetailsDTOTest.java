package com.roche.connect.common.forte;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class TertiaryJobDetailsDTOTest {
    TertiaryJobDetailsDTO tertiaryJobDetailsDTO;
    
    @BeforeTest public void setUp() {
        tertiaryJobDetailsDTO = new TertiaryJobDetailsDTO();
    }
    
    List<String> tertiary_checksums = new ArrayList<>();
    List<String> tertiary_infiles = new ArrayList<>();
    
    @Test
    public void tertiaryJobDetailsDTOTest() {
        tertiary_checksums.add("345");
        tertiary_infiles.add("in.txt");
        tertiaryJobDetailsDTO.setId("1");
        tertiaryJobDetailsDTO.setKind("tertiary");
        tertiaryJobDetailsDTO.setTertiary_checksums(tertiary_checksums);
        tertiaryJobDetailsDTO.setTertiary_infiles(tertiary_infiles);
        tertiaryJobDetailsDTO.setTertiary_parameters("ter_parameters");
        
        Assert.assertEquals("1", tertiaryJobDetailsDTO.getId());
        Assert.assertEquals("tertiary", tertiaryJobDetailsDTO.getKind());
        Assert.assertEquals("345", tertiaryJobDetailsDTO.getTertiary_checksums().get(0));
        Assert.assertEquals("in.txt", tertiaryJobDetailsDTO.getTertiary_infiles().get(0));
        Assert.assertEquals("ter_parameters", tertiaryJobDetailsDTO.getTertiary_parameters());
    }
}
