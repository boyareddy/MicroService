package com.roche.connect.common.mp24.message;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ContainerInfoTest {
    ContainerInfo containerInfo;
    
    @BeforeTest
    public void setUp() {
        containerInfo = new ContainerInfo();
    }
    
    @Test
    public void getContainerInfoTest() {
        containerInfo.setAvailableSpecimenVolume("200ml");
        containerInfo.setCarrierBarcode("787878");
        containerInfo.setCarrierPosition("2");
        containerInfo.setCarrierType("type");
        containerInfo.setContainerPosition("3");
        containerInfo.setContainerStatus("96wellplate");
        containerInfo.setContainerVolume("120ml");
        containerInfo.setInitialSpecimenVolume("100ml");
        containerInfo.setSpecimenEventDate("20190220");
        containerInfo.setSpecimenVolume("100ml");
        containerInfo.setUnitofVolume("2");
        Assert.assertEquals("200ml", containerInfo.getAvailableSpecimenVolume());
        Assert.assertEquals("787878", containerInfo.getCarrierBarcode());
        Assert.assertEquals("2", containerInfo.getCarrierPosition());
        Assert.assertEquals("type", containerInfo.getCarrierType());
        Assert.assertEquals("3", containerInfo.getContainerPosition());
        Assert.assertEquals("96wellplate", containerInfo.getContainerStatus());
        Assert.assertEquals("120ml", containerInfo.getContainerVolume());
        Assert.assertEquals("100ml", containerInfo.getInitialSpecimenVolume());
        Assert.assertEquals("20190220", containerInfo.getSpecimenEventDate());
        Assert.assertEquals("100ml", containerInfo.getSpecimenVolume());
        Assert.assertEquals("2", containerInfo.getUnitofVolume());
    }
}
