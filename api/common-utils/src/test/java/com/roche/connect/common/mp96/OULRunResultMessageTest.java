package com.roche.connect.common.mp96;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class OULRunResultMessageTest {
	
	private com.roche.connect.common.mp96.OULRunResultMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.mp96.OULRunResultMessage();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
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
	public void testGetSetDeviceRunId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceRunId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDeviceRunId(),
					"Getter and Setter Method Test failed for DeviceRunId"
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
	public void testGetSetRunComments() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunComments(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRunComments(),
					"Getter and Setter Method Test failed for RunComments"
					);
		}
	}
	@Test
	public void testGetSetRunResultStatus() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunResultStatus(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRunResultStatus(),
					"Getter and Setter Method Test failed for RunResultStatus"
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
	
	
}
