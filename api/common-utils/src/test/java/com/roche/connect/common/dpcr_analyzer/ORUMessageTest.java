package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ORUMessageTest {
	
	private com.roche.connect.common.dpcr_analyzer.ORUMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.ORUMessage();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
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
	public void testGetSetOperatorName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOperatorName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getOperatorName(),
					"Getter and Setter Method Test failed for OperatorName"
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
	public void testGetSetReleasedBy() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReleasedBy(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getReleasedBy(),
					"Getter and Setter Method Test failed for ReleasedBy"
					);
		}
	}
	@Test
	public void testGetSetSentBy() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSentBy(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSentBy(),
					"Getter and Setter Method Test failed for SentBy"
					);
		}
	}
	
}
