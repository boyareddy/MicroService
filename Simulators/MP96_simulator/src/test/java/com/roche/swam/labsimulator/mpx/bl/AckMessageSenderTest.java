package com.roche.swam.labsimulator.mpx.bl;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.roche.swam.labsimulator.Mp96TestServer;
import com.roche.swam.labsimulator.util.Mp96RunData;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ACK;
import ca.uhn.hl7v2.model.v24.segment.MSH;

public class AckMessageSenderTest {


  @Test
  public void buildAckMessage() throws HL7Exception, IOException {
	  Mp96RunData mp96RunData = new Mp96RunData();
	  mp96RunData.setDeviceId("MP96");
	  mp96RunData.setRunId("RND111");
	  AckMessageSender ackMessageSender =new AckMessageSender();
	  Message msg = null;
	  msg = ackMessageSender.buildAckMessage(mp96RunData, null);
	  ACK ack = (ACK) msg;
		 MSH mshSegment = ack.getMSH();
		 Assert.assertEquals("ACK", mshSegment.getMsh9_MessageType().getMsg1_MessageType().getValue()); 
		 Assert.assertEquals("MP96", mshSegment.getMsh10_MessageControlID().getValue()); 
  }

  @Test
  public void runMp96RunData() throws HL7Exception, IOException, InterruptedException {
	  Mp96TestServer mpx = new Mp96TestServer();
	  int port = 4322;
	  mpx.start(port);
	  Mp96RunData mp96RunData = new Mp96RunData();
	  mp96RunData.setDeviceId("MP96");
	  mp96RunData.setRunId("RND111");
	  AckMessageSender ackMessageSender =new AckMessageSender();
	  ackMessageSender.run(mp96RunData);
	  mpx.getServer().stopAndWait();
	  
  }

  @Test
  public void run() throws ParseException, LLPException {
	  AckMessageSender ackMessageSender =new AckMessageSender();
	  ackMessageSender.run();
  }
}
