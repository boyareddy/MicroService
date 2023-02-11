package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ESUMessageTest {
	
	private com.roche.connect.common.dpcr_analyzer.ESUMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.ESUMessage();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}
	@Test
	public void testGetSetEstimatedTimeRemaining() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEstimatedTimeRemaining(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getEstimatedTimeRemaining(),
					"Getter and Setter Method Test failed for EstimatedTimeRemaining"
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
	public void testGetSetFilePath() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFilePath(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getFilePath(),
					"Getter and Setter Method Test failed for FilePath"
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
	
}
