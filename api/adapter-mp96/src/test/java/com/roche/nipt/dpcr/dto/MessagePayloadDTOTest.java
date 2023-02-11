package com.roche.nipt.dpcr.dto;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class MessagePayloadDTOTest {
	MessagePayloadDTO msg = new MessagePayloadDTO();
	
  @BeforeTest
  public void beforeTest() {
	  msg.setDateAndTime("sfafa");
	  msg.setDeviceName("fadwfa");
	  msg.setDeviceSerialNumber("dashgfj");
	  msg.setMessageType("fasdfas");
	  msg.setRunId("sfadhskh");
  }

  @AfterTest
  public void afterTest() {
	  msg.getDateAndTime();
	  msg.getDeviceName();
	  msg.getDeviceSerialNumber();
	  msg.getMessageType();
	  msg.getRunId();
  }


 

  @Test
  public String toString() {
	return msg.toString();
  }
}
