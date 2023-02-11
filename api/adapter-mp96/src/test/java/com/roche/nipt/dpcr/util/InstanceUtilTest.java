package com.roche.nipt.dpcr.util;

import org.testng.annotations.Test;

import com.roche.nipt.dpcr.dto.MessageExchangeDTO;

import ca.uhn.hl7v2.model.Message;

import org.testng.annotations.BeforeMethod;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterMethod;

public class InstanceUtilTest {

	
	InstanceUtil instanceUtil = new InstanceUtil();

	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@Test
	public void getConnectionMap() {
	}

	@Test
	public void getInstance() {
		instanceUtil.getInstance();
	}

	@Test
	public void getMessageMap() {
	}

	@Test
	public void setConnectionMap() {
		
		Map<String, MessageExchangeDTO> connectionMap = new HashMap<String, MessageExchangeDTO>();
		instanceUtil.setConnectionMap(connectionMap);
	}

	@Test
	public void setMessageMap() {
		Map<String, Message> messageMap = new HashMap<String, Message>();
		instanceUtil.setMessageMap(messageMap);
	}
}
