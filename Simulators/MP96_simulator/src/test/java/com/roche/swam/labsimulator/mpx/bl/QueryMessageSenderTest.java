package com.roche.swam.labsimulator.mpx.bl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.annotations.Test;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.util.Mp96RunData;

import mockit.Deencapsulation;
import mockit.Tested;
@RunWith(MockitoJUnitRunner.class)
public class QueryMessageSenderTest {
	
	
	@Mock
	Mp96RunData mp96RunData;
	
	@Tested
	QueryMessageSender queryMessageSender;
	

 

 

@Test
  public void run() {
	  
	 QueryMessageSender queryMessageSender = new QueryMessageSender(); 
	   queryMessageSender.run();
  }
	
  @Test
  public void processOrmMessage() throws IOException {
	  InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/ORM_001.txt");
      String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');  
      QueryMessageSender queryMessageSender = new QueryMessageSender(); 
	  Deencapsulation.invoke(queryMessageSender, "processOrmMessage", msg);
			   Assert.assertEquals(MainApp.checkSampleDataJson(), true);
	  
  }


  
  @Test
  public void cleanUp() {
	  QueryMessageSender  queryMessageSender = new QueryMessageSender(); 
   Path p= Paths.get("sa");
   queryMessageSender.cleanUp(p);
   
  }
}
