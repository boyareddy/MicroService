package com.roche.swam.labsimulator.test;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.engine.Engine;

public class MainAppTest {
	
	@InjectMocks
	MainApp mainAppMock;
	
	@Mock
	Engine engineMock;


	
	@Before
	    public void setup() throws Exception {
		  MockitoAnnotations.initMocks(this);
		  MainApp.main(new String[] { "C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json" });
		  Assert.assertEquals("new String[] { \"C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json\" }", "C:\\Users\\gosula.r\\Documents\\rocheconnectdevelopment\\sourcecode\\simulator\\src\\main\\resources\\data.json");
	    }
	
	
	@Test
	 public void testStop() throws Exception {
	    mainAppMock.stop();
		 Assert.assertEquals(false, mainAppMock.getServer().getServer().isRunning());		 
	 }
	
	
	
	
	
	
	
	
	
}
