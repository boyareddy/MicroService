package com.roche.connect.common.mp96;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class QueryResponseMessageTest {
	
	private com.roche.connect.common.mp96.QueryResponseMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.mp96.QueryResponseMessage();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
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
	public void testGetSetRunId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRunId(),
					"Getter and Setter Method Test failed for RunId"
					);
		}
	}
	@Test
	public void testGetSetDateTime() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDateTime(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDateTime(),
					"Getter and Setter Method Test failed for DateTime"
					);
		}
	}
	@Test
	public void testGetSetCreatedBy() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCreatedBy(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getCreatedBy(),
					"Getter and Setter Method Test failed for CreatedBy"
					);
		}
	}
	@Test
	public void testGetSetDeviceID() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceID(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDeviceID(),
					"Getter and Setter Method Test failed for DeviceID"
					);
		}
	}
	
}
