package com.roche.nipt.dpcr.dto;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class HL7HeaderSegmentDTOTest {
	HL7HeaderSegmentDTO hl7= new HL7HeaderSegmentDTO();
	private static final Logger log = LogManager.getLogger(HL7HeaderSegmentDTOTest.class);
	
	
  @BeforeTest
  public void beforeMethod() {
	 
  }

  @AfterTest
  public void afterMethod() {
	     
  }
  
  @Test
  public void testHL7DTO() {
	  hl7.setDateAndTime("sfasdf");
	  hl7.setDeviceId("dfas");
	  hl7.setDeviceName("agfs");
	  hl7.setMessageType("fadwf");
	  hl7.setProcessingId("afgqa");
	  hl7.setQueryDefDesc("qgerg");
	  hl7.setVersionId("grqeg");
	  
	  hl7.getDateAndTime();
	  hl7.getDeviceId();
	  hl7.getDeviceName();
	  hl7.getMessageType();
	  hl7.getProcessingId();
	  hl7.getQueryDefDesc();
	  hl7.getVersionId();
	  hl7.toString();
  }
}
