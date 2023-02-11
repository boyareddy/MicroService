package com.roche.swam.labsimulator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.roche.swam.labsimulator.common.server.EmbeddedJetty;
import com.roche.swam.labsimulator.engine.Engine;

public class MainAppTest {
	
	
	private static final Logger log = LoggerFactory.getLogger(MainApp.class);
	private Engine engine;
	private EmbeddedJetty server;
	private static String deviceName;

	@Test
  public void checkSampleDataJson() {
	  File sampleFile = new File(MainApp.getFilepath("MP96DataJsonPath"));
	  Assert.assertEquals(sampleFile.exists(), true);
	  
  }
  
/*  @Test
  public void checkSampleDataJsonNegative() {
	  File sampleFile = new File(MainApp.getFilepath("sfsdfsdf"));
	  Assert.assertEquals(sampleFile.exists(), false);
	  
  }*/


  @Test
  public void getClasspath() throws IOException {
	  String classPath = "";
			File file = new File("./");
			File directory = new File(file.getCanonicalPath()+"/"+ MainApp.getFilepath("DeviceType"));
			if (!directory.exists()) {
				Assert.assertEquals(directory.exists(), false);
				directory.mkdir(); 
			}
			classPath=directory.getCanonicalPath();
			 Assert.assertEquals(classPath, "C:\\Users\\sooryaprakash.m\\git\\sourcecode\\MP96_simulator\\MagnaPure96");
  }


  @Test
  public void getFilepath() throws IOException {
	  
	  Properties prop = new Properties();
			FileReader fr= new FileReader("simulator.properties");
			prop.load(fr);
			 Assert.assertEquals(prop.getProperty("LP24DataJsonPath"),"LP24DataJson.JSON");
			 Assert.assertEquals(prop.getProperty("MP96DataJsonPath"),"Mp96data.json");
  }

  
  @Test
  public void getFilepathNegative() throws IOException {
	  
	  Properties prop = new Properties();
			FileReader fr= new FileReader("simulator.properties");
			prop.load(fr);
			 Assert.assertNotEquals(prop.getProperty(""),"LP24DataJson.JSON");
			 Assert.assertNotEquals(prop.getProperty(""),"Mp96data.json");

  }

 /* @Test
  public void main(String[] args) throws Exception {
	  MainApp mn =new MainApp();
		mn.start(args[0]);
  }*/

  @Test
  public void start() {
	 
  }

  @Test
  public void stop() {
  }
}
