package com.roche.connect.common.htp.status;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class StartPlannedTest {
    StartPlanned startPlanned;
    
    List<String> complexId = new ArrayList<>();
    List<String> sequencingProtocols= new ArrayList<>();
    
    @BeforeTest
    public void setUp() {
        startPlanned = new StartPlanned();
    }
    
    @Test
    public void getStartPlannedTest() {
        complexId.add("3457");
        sequencingProtocols.add("HTP Protocol");
        startPlanned.setSeqRunBatchSize(123L);
        startPlanned.setComplexId(complexId);
        startPlanned.setSequencingProtocols(sequencingProtocols);
        Assert.assertEquals(123L, startPlanned.getSeqRunBatchSize());
        Assert.assertEquals("3457", startPlanned.getComplexId().get(0));
        Assert.assertEquals("HTP Protocol", startPlanned.getSequencingProtocols().get(0));
    }
}
