package com.roche.connect.common.htp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;

public class HtpStatusMessageTest {
    HtpStatusMessage htpStatusMessage;
    
    
    @BeforeTest
    public void setUp() {
        htpStatusMessage = new HtpStatusMessage();
    }
    
    @Test
    public void getHtpStatusMessageTest() {
        htpStatusMessage.setAccessioningId("8001");
        htpStatusMessage.setDeviceId("HTP-001");
        htpStatusMessage.setDeviceName("HTP");
        htpStatusMessage.setDeviceRunId("1234");
        htpStatusMessage.setEstimatedTimeRemaining(123L);
        htpStatusMessage.setInputContainerId("2");
        htpStatusMessage.setOrderId(123L);
        htpStatusMessage.setOutputContainerId("2");
        htpStatusMessage.setProcessStepName("HTP Protocol");
        htpStatusMessage.setReceivingApplication("Connect");
        htpStatusMessage.setReceivingFacility("Connect");
        htpStatusMessage.setRunResultsId(123L);
        Collection<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumables = new ArrayList<>();
        SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
        sampleReagentsAndConsumablesDTO.setAttributeName("attributeName");
        sampleReagentsAndConsumablesDTO.setAttributeValue("attributeValue");
        sampleReagentsAndConsumables.add(sampleReagentsAndConsumablesDTO);
        htpStatusMessage.setSampleReagentsAndConsumables(sampleReagentsAndConsumables );
        htpStatusMessage.setSendingApplication("HTP");
        htpStatusMessage.setSendingFacility("HTP");
        htpStatusMessage.setStatus("Started");
        htpStatusMessage.setUpdatedBy("admin");
        htpStatusMessage.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        
        Assert.assertEquals("8001", htpStatusMessage.getAccessioningId());
        Assert.assertEquals("HTP-001", htpStatusMessage.getDeviceId());
        Assert.assertEquals("HTP", htpStatusMessage.getDeviceName());
        Assert.assertEquals("1234", htpStatusMessage.getDeviceRunId());
        Assert.assertEquals(123L, htpStatusMessage.getEstimatedTimeRemaining());
        Assert.assertEquals("2", htpStatusMessage.getInputContainerId());
        Assert.assertSame(123L, htpStatusMessage.getOrderId());
        Assert.assertEquals("2", htpStatusMessage.getOutputContainerId());
        Assert.assertEquals("HTP Protocol", htpStatusMessage.getProcessStepName());
        Assert.assertEquals("Connect", htpStatusMessage.getReceivingApplication());
        Assert.assertEquals("Connect", htpStatusMessage.getReceivingFacility());
        Assert.assertSame(123L, htpStatusMessage.getRunResultsId());
        Assert.assertEquals(true, htpStatusMessage.getSampleReagentsAndConsumables().iterator().hasNext());
        Assert.assertEquals("HTP", htpStatusMessage.getSendingApplication());
        Assert.assertEquals("HTP", htpStatusMessage.getSendingFacility());
        Assert.assertEquals("Started", htpStatusMessage.getStatus());
        Assert.assertEquals("admin", htpStatusMessage.getUpdatedBy());
       System.out.println(htpStatusMessage.getUpdatedDateTime());
        
        
        
    }
}
