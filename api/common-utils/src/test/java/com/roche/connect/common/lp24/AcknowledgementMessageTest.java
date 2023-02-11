package com.roche.connect.common.lp24;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AcknowledgementMessageTest {
	
	private com.roche.connect.common.lp24.AcknowledgementMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.lp24.AcknowledgementMessage();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	
	@Test
	public void testGetSetStatus() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setStatus(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getStatus(),
					"Getter and Setter Method Test failed for Status"
					);
		}
	}
	@Test
	public void testGetSetDeviceSerialNumber() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceSerialNumber(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDeviceSerialNumber(),
					"Getter and Setter Method Test failed for DeviceSerialNumber"
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
	public void testGetSetReceivingApplication() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReceivingApplication(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getReceivingApplication(),
					"Getter and Setter Method Test failed for ReceivingApplication"
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
	@Test
	public void testGetSetContainerId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setContainerId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getContainerId(),
					"Getter and Setter Method Test failed for ContainerId"
					);
		}
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
	public void testGetSetErrorCode() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setErrorCode(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getErrorCode(),
					"Getter and Setter Method Test failed for ErrorCode"
					);
		}
	}
	@Test
	public void testGetSetErrorDescription() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setErrorDescription(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getErrorDescription(),
					"Getter and Setter Method Test failed for ErrorDescription"
					);
		}
	}
	
	
}
