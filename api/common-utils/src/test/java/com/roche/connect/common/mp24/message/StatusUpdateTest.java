package com.roche.connect.common.mp24.message;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class StatusUpdateTest {
    StatusUpdate statusUpdate;
    
    
    @BeforeTest
    public void setUp() {
        statusUpdate = new StatusUpdate();
    }
    
    
    @Test
    public void getStatusUpdateTest() {
        statusUpdate.setComment("comment");
        List<Consumable> consumables = new ArrayList<>();
        Consumable c = new Consumable();
        c.setName("name");
        c.setValue("value");
        consumables.add(c);
        statusUpdate.setConsumables(consumables );
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setCarrierBarcode("code");
        statusUpdate.setContainerInfo(containerInfo );
        statusUpdate.setEquipmentState("equipState");
        statusUpdate.setEventDate("20201210");
        statusUpdate.setFlag("F1");
        statusUpdate.setInternalControls("control");
        statusUpdate.setOperatorName("operatorName");
        statusUpdate.setOrderName("orderName");
        statusUpdate.setOrderResult("result");
        statusUpdate.setProcessingCartridge("cartridge");
        statusUpdate.setProtocolName("ffPET");
        statusUpdate.setProtocolVersion("0.1");
        statusUpdate.setRunEndTime(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setRunResult("Passed");
        statusUpdate.setRunStartTime(new Timestamp(System.currentTimeMillis()));
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setContainerType("type");
        statusUpdate.setSampleInfo(sampleInfo );
        statusUpdate.setTipRack("rack");
        statusUpdate.setUpdatedBy("admin");
        
        
        Assert.assertEquals("comment", statusUpdate.getComment());
        Assert.assertEquals("name", statusUpdate.getConsumables().get(0).getName());
        Assert.assertEquals("code", statusUpdate.getContainerInfo().getCarrierBarcode());
        Assert.assertEquals("equipState", statusUpdate.getEquipmentState());
        Assert.assertEquals("20201210", statusUpdate.getEventDate());
        Assert.assertEquals("F1", statusUpdate.getFlag());
        Assert.assertEquals("control", statusUpdate.getInternalControls());
        Assert.assertEquals("operatorName", statusUpdate.getOperatorName());
        Assert.assertEquals("orderName", statusUpdate.getOrderName());
        Assert.assertEquals("result", statusUpdate.getOrderResult());
        Assert.assertEquals("cartridge", statusUpdate.getProcessingCartridge());
        Assert.assertEquals("ffPET", statusUpdate.getProtocolName());
        Assert.assertEquals("0.1", statusUpdate.getProtocolVersion());
        System.out.println(statusUpdate.getRunEndTime());
        Assert.assertEquals("Passed", statusUpdate.getRunResult());
        System.out.println( statusUpdate.getRunStartTime());
        Assert.assertEquals("type", statusUpdate.getSampleInfo().getContainerType());
        Assert.assertEquals("rack", statusUpdate.getTipRack());  
        Assert.assertEquals("admin", statusUpdate.getUpdatedBy());
        
    }
}
