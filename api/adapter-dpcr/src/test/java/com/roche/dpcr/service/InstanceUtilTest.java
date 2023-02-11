package com.roche.dpcr.service;

import java.util.HashMap;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.dpcr.dto.MessageExchange;
import com.roche.dpcr.util.InstanceUtil;

import ca.uhn.hl7v2.model.Message;

public class InstanceUtilTest {
	@InjectMocks InstanceUtil instanceUtil;
	
	Map<String, MessageExchange> connectionMap  = new HashMap<>();
	
	 Map<String, Message> messageMap = new HashMap<>();
	
	MessageExchange messageExchange = new MessageExchange();
	
	@Mock Message message;
	
	@BeforeTest
	public void setUp() {
		instanceUtil = InstanceUtil.getInstance();
		messageExchange.setDeviceType("DPCR");
		connectionMap.put("key",messageExchange);
		messageMap.put("message", message);
	}
	
	@Test
	public void getInstanceUtilTest() {
		instanceUtil.setConnectionMap(connectionMap);
		instanceUtil.setMessageMap(messageMap);
		System.out.println(instanceUtil.getConnectionMap()+" "+instanceUtil.getMessageMap());
	}
}
