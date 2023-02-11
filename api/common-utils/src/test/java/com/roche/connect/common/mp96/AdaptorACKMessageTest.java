package com.roche.connect.common.mp96;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AdaptorACKMessageTest {
	
	private com.roche.connect.common.mp96.AdaptorACKMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.mp96.AdaptorACKMessage();
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
	
	
}
