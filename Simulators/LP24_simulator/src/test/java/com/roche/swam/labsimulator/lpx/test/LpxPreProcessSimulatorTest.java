/*package com.roche.swam.labsimulator.lpx.test;

import java.io.IOException;
import java.util.ResourceBundle;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.roche.swam.labsimulator.engine.bl.AppConfig;
import com.roche.swam.labsimulator.lpx.LpxPreProcessSimulator;


public class LpxPreProcessSimulatorTest {
    
    @InjectMocks
    LpxPreProcessSimulator lpxPreSimulatorMock;
    
    @Mock
    ResourceBundle ResourceBundleMock;
    
    @BeforeTest
    public void setUp() {
        ApplicationContext c = new AnnotationConfigApplicationContext(AppConfig.class);
        lpxPreSimulatorMock = c.getBean(LpxPreProcessSimulator.class);
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testLoadSamples() {
        lpxPreSimulatorMock.loadSamples(6);
    }
    
    @Test
    public void testStartRun() throws JsonGenerationException, JsonMappingException, IOException {
        lpxPreSimulatorMock.startRun();
    }
    
    @Test
    public void testFinishRun() throws JsonGenerationException, JsonMappingException, IOException {
        lpxPreSimulatorMock.finishRun();
    }
    
    
    @Test
    public void testCalculateState() {
        lpxPreSimulatorMock.calculateState();
    }
}
*/