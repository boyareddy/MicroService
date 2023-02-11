package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class QueryMessageTest {
	
	private com.roche.connect.common.dpcr_analyzer.QueryMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.QueryMessage();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}
	@Test
	public void testGetSetMessageControlId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageControlId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getMessageControlId(),
					"Getter and Setter Method Test failed for MessageControlId"
					);
		}
	}
	@Test
	public void testGetSetMessageType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getMessageType(),
					"Getter and Setter Method Test failed for MessageType"
					);
		}
	}
	@Test
	public void testGetSetDeviceId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDeviceId(),
					"Getter and Setter Method Test failed for DeviceId"
					);
		}
	}
	@Test
	public void testGetSetSendingApplicationName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSendingApplicationName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSendingApplicationName(),
					"Getter and Setter Method Test failed for SendingApplicationName"
					);
		}
	}
	@Test
	public void testGetSetSendingFacility() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSendingFacility(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSendingFacility(),
					"Getter and Setter Method Test failed for SendingFacility"
					);
		}
	}
	@Test
	public void testGetSetRecevingApplicationName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRecevingApplicationName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRecevingApplicationName(),
					"Getter and Setter Method Test failed for RecevingApplicationName"
					);
		}
	}
	@Test
	public void testGetSetRecevingFacility() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRecevingFacility(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRecevingFacility(),
					"Getter and Setter Method Test failed for RecevingFacility"
					);
		}
	}
	@Test
	public void testGetSetDateTimeMessageGenerated() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDateTimeMessageGenerated(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDateTimeMessageGenerated(),
					"Getter and Setter Method Test failed for DateTimeMessageGenerated"
					);
		}
	}
	
}
