package com.roche.swam.labsimulator.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.roche.swam.labsimulator.engine.bl.AppConfig;
import com.roche.swam.labsimulator.lpx.LpxPostProcessSimulator;
import com.roche.swam.labsimulator.mpx.MpxSimulator;

public class LpxPostProcessSimulatorTest {

	
	@InjectMocks
	LpxPostProcessSimulator lpxPostProcessSimulator;
	
	
	
	@Before
	public void setUp() {
		ApplicationContext c = new AnnotationConfigApplicationContext(AppConfig.class);
		lpxPostProcessSimulator = c.getBean(LpxPostProcessSimulator.class);
		MockitoAnnotations.initMocks(this);
	}
	
	// Getting nullPointer Exception (Check once again)
	/*@Test
	public void testStartProcess() throws InterruptedException {
		mpxSimulatorMock.startProcess();
	}*/
	
	/*@Test
	public void testCreateOrders() {
		mpxSimulatorMock.createOrders();
	}*/
	
	@Test
	public void testLoadSamples() {
		lpxPostProcessSimulator.loadSamples(6);
	}
	
	@Test
	public void testStartRun() throws JsonGenerationException, JsonMappingException, IOException {
		lpxPostProcessSimulator.startRun();
	}
	
	@Test
	public void testFinishRun() throws JsonGenerationException, JsonMappingException, IOException {
		lpxPostProcessSimulator.finishRun();
	}
	
	
	@Test
	public void testCalculateState() {
		lpxPostProcessSimulator.calculateState();
	}
}
