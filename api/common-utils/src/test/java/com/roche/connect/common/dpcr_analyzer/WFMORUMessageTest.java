package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class WFMORUMessageTest {
	
	private com.roche.connect.common.dpcr_analyzer.WFMORUMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.WFMORUMessage();
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
	public void testGetSetAccessioningId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAccessioningId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getAccessioningId(),
					"Getter and Setter Method Test failed for AccessioningId"
					);
		}
	}
	@Test
	public void testGetSetInputContainerId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setInputContainerId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getInputContainerId(),
					"Getter and Setter Method Test failed for InputContainerId"
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
	public void testGetSetProcessStepName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProcessStepName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getProcessStepName(),
					"Getter and Setter Method Test failed for ProcessStepName"
					);
		}
	}
	@Test
	public void testGetSetFlag() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFlag(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getFlag(),
					"Getter and Setter Method Test failed for Flag"
					);
		}
	}
	@Test
	public void testGetSetRunResultId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunResultId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRunResultId(),
					"Getter and Setter Method Test failed for RunResultId"
					);
		}
	}
	
	
}
