package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class WFMQueryMessageTest {
	
	private com.roche.connect.common.dpcr_analyzer.WFMQueryMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.WFMQueryMessage();
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
	public void testGetSetContainerPosition() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setContainerPosition(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getContainerPosition(),
					"Getter and Setter Method Test failed for ContainerPosition"
					);
		}
	}
	
	
	
	
}
