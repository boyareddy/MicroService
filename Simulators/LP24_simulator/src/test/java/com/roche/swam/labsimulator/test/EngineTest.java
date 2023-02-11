package com.roche.swam.labsimulator.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.roche.swam.labsimulator.engine.Engine;
import com.roche.swam.labsimulator.engine.bl.AppConfig;


@RunWith(MockitoJUnitRunner.class)
public class EngineTest {

	@InjectMocks
	Engine engineMock;
	
	@Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


	@Test(expected = Exception.class)
	 public void testStartup() throws IOException, ClassNotFoundException {
		ApplicationContext c = new AnnotationConfigApplicationContext(AppConfig.class);
	    engineMock = c.getBean(Engine.class);
	    engineMock.startup();
	}
	

}
