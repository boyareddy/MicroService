package com.roche.swam.labsimulator.test;

import java.io.IOException;
import java.util.List;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.swam.labsimulator.common.bl.sim.Simulator;
import com.roche.swam.labsimulator.engine.Engine;
import com.roche.swam.labsimulator.engine.bl.AppConfig;
import com.roche.swam.labsimulator.engine.bl.config.Equipment;
import com.roche.swam.labsimulator.mpx.MpxSimulator;


public class EngineTest {

	@InjectMocks
	Engine engineMock;
	
	@BeforeTest
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


	@Test
	 public void testStartup() throws IOException, ClassNotFoundException {
		ApplicationContext c = new AnnotationConfigApplicationContext(AppConfig.class);
	    engineMock = c.getBean(Engine.class);
	    engineMock.startup();
	}
	

}
