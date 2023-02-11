package com.roche.swam.labsimulator.mpx.bl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.roche.swam.labsimulator.util.Mp96RunData;
import com.roche.swam.labsimulator.util.Mp96SampleData;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.OUL_R21;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import mockit.Deencapsulation;

public class ResultMessageBuilderTest {
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
  }


  @Test
  public void ResultMessageBuilder() {
  }

  @Test
  public void build() throws HL7Exception, IOException {
	  Mp96RunData mp96RunData = new Mp96RunData();
	  mp96RunData.setDeviceId("MP96");
	  mp96RunData.setRunId("RND111");
	  List<Mp96SampleData> mp96SampleDataList = new ArrayList<>();
	  Mp96SampleData mp96SampleData = new Mp96SampleData();
	  mp96SampleData.setAccessioningId("111");
	  mp96SampleDataList.add(mp96SampleData);
	  mp96RunData.setSamples(mp96SampleDataList);
	  ResultMessageBuilder rsm = new ResultMessageBuilder();
	  OUL_R21 oul = (OUL_R21) rsm.build(mp96RunData);
	  
	  MSH mshSegment = oul.getMSH();
		 Assert.assertEquals("OUL", mshSegment.getMsh9_MessageType().getMsg1_MessageType().getValue()); 
		 Assert.assertEquals("1234567890", mshSegment.getMsh10_MessageControlID().getValue());
		 Assert.assertEquals("R21", mshSegment.getMsh9_MessageType().getMsg2_TriggerEvent().getValue());
	  
  }

  @Test
  public void convertHl7toString() {
	  String s= "NTE|1||Comment for the Sample X|";
	  ResultMessageBuilder rsm = new ResultMessageBuilder();
	String output=  Deencapsulation.invoke(rsm, "convertHl7toString", s);
	 Assert.assertEquals(output, s);
	  
  }
}
