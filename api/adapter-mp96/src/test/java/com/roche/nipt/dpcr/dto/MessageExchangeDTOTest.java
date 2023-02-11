package com.roche.nipt.dpcr.dto;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MessageExchangeDTOTest {
	MessageExchangeDTO messageExchangeDTO= new MessageExchangeDTO();
	
	Exchange ex;
	
	AsyncCallback as;
	
  @BeforeTest
  public void beforeTest() {
	  messageExchangeDTO.setCallback(as);
	  messageExchangeDTO.setExchange(ex);
  }

  @AfterTest
  public void afterTest() {
	  
	  messageExchangeDTO.getCallback();
	  messageExchangeDTO.getExchange();
  }

  @Test
  public String toString() {
	return messageExchangeDTO.toString();
   
  }
 
}
