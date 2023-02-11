package com.roche.connect.common.rmm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class InputStripDetailsDTOTest {
	
	private com.roche.connect.common.rmm.dto.InputStripDetailsDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.rmm.dto.InputStripDetailsDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}
	@Test
	public void testGetSetStripId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setStripId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getStripId(),
					"Getter and Setter Method Test failed for StripId"
					);
		}
	}
	@Test
	public void testGetSetSamplecounts() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSamplecounts(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSamplecounts(),
					"Getter and Setter Method Test failed for Samplecounts"
					);
		}
	}

	
	
}
