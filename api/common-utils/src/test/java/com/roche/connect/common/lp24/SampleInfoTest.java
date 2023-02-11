package com.roche.connect.common.lp24;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class SampleInfoTest {
	
	private com.roche.connect.common.lp24.SampleInfo classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.lp24.SampleInfo();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	
	@Test
	public void testGetSetNewContainerId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setNewContainerId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getNewContainerId(),
					"Getter and Setter Method Test failed for NewContainerId"
					);
		}
	}
	
	@Test
	public void testGetSetNewOutputPosition() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setNewOutputPosition(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getNewOutputPosition(),
					"Getter and Setter Method Test failed for NewOutputPosition"
					);
		}
	}
	@Test
	public void testGetSetSampleType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSampleType(),
					"Getter and Setter Method Test failed for SampleType"
					);
		}
	}
	@Test
	public void testGetSetSpecimenSourceSite() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSpecimenSourceSite(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSpecimenSourceSite(),
					"Getter and Setter Method Test failed for SpecimenSourceSite"
					);
		}
	}
	@Test
	public void testGetSetSpecimenDescription() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSpecimenDescription(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSpecimenDescription(),
					"Getter and Setter Method Test failed for SpecimenDescription"
					);
		}
	}
	@Test
	public void testGetSetDateTimeSpecimenCollected() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDateTimeSpecimenCollected(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDateTimeSpecimenCollected(),
					"Getter and Setter Method Test failed for DateTimeSpecimenCollected"
					);
		}
	}
	
	@Test
	public void testGetSetDateTimeSpecimenReceived() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDateTimeSpecimenReceived(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDateTimeSpecimenReceived(),
					"Getter and Setter Method Test failed for DateTimeSpecimenReceived"
					);
		}
	}
	
	@Test
	public void testGetSetDateTimeSpecimenExpiration() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDateTimeSpecimenExpiration(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDateTimeSpecimenExpiration(),
					"Getter and Setter Method Test failed for DateTimeSpecimenExpiration"
					);
		}
	}
	
	
}
