package com.roche.connect.common.mp96;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class WFMQueryResponseMessageTest {
	
	private com.roche.connect.common.mp96.WFMQueryResponseMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.mp96.WFMQueryResponseMessage();
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
	
	
}
