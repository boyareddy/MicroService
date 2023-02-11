package com.roche.swam.labsimulator.mpx.bl;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.uhn.hl7v2.HL7Exception;

public class ResultMessageSenderTest {
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
  }


  @Test
  public void runMp96RunData() {
  }

  @Test
  public void run() throws HL7Exception, InterruptedException {
	/*  Mp96TestServer mpx = new Mp96TestServer();
	  mpx.start();
		Assert.assertEquals(mpx.getServer().isRunning(), true);
	  Mp96RunData mp96RunData = new Mp96RunData();
	  mp96RunData.setDeviceId("MP96");
	  mp96RunData.setRunId("RND111");
	  List<Mp96SampleData> mp96SampleDataList = new ArrayList<>();
	  Mp96SampleData mp96SampleData = new Mp96SampleData();
	  mp96SampleData.setAccessioningId("111");
	  mp96SampleDataList.add(mp96SampleData);
	  mp96RunData.setMp96SampleData(mp96SampleDataList);
	  ResultMessageSender resultMessageSender = new ResultMessageSender();
	  resultMessageSender.run(mp96RunData);
	  mpx.getServer().stopAndWait();*/
	  
  }
}
