package com.roche.swam.labsimulator.mpx.bl;

import java.io.IOException;

import org.junit.Assert;
import org.testng.annotations.Test;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.QBP_Q11;
import ca.uhn.hl7v2.model.v24.segment.MSH;

public class QueryMessageBuilderTest {


  @Test
  public void build() throws HL7Exception, IOException {
	  
	  QueryMessageBuilder queryBuilder = new QueryMessageBuilder();
	  
	  QBP_Q11 qbp = (QBP_Q11) queryBuilder.build();
	  
	  MSH mshSegment = qbp.getMSH();
	  
	  Assert.assertEquals("QBP", mshSegment.getMsh9_MessageType().getMsg1_MessageType().getValue());
	  Assert.assertEquals("Q11", mshSegment.getMsh9_MessageType().getMsg2_TriggerEvent().getValue());
	  
  }

}
