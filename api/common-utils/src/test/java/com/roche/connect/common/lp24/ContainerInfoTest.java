package com.roche.connect.common.lp24;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ContainerInfoTest {
	
	private com.roche.connect.common.lp24.ContainerInfo classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.lp24.ContainerInfo();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	
	@Test
	public void testGetSetAvailableSpecimenVolume() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAvailableSpecimenVolume(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getAvailableSpecimenVolume(),
					"Getter and Setter Method Test failed for AvailableSpecimenVolume"
					);
		}
	}
	@Test
	public void testGetSetInitialSpecimenVolume() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setInitialSpecimenVolume(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getInitialSpecimenVolume(),
					"Getter and Setter Method Test failed for InitialSpecimenVolume"
					);
		}
	}
	@Test
	public void testGetSetSpecimenEventDate() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSpecimenEventDate(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSpecimenEventDate(),
					"Getter and Setter Method Test failed for SpecimenEventDate"
					);
		}
	}
	@Test
	public void testGetSetSpecimenVolume() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSpecimenVolume(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSpecimenVolume(),
					"Getter and Setter Method Test failed for SpecimenVolume"
					);
		}
	}
	@Test
	public void testGetSetUnitofVolume() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setUnitofVolume(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getUnitofVolume(),
					"Getter and Setter Method Test failed for UnitofVolume"
					);
		}
	}
	@Test
	public void testGetSetCarrierType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCarrierType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getCarrierType(),
					"Getter and Setter Method Test failed for CarrierType"
					);
		}
	}
	
	@Test
	public void testGetSetOutputPlateType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputPlateType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getOutputPlateType(),
					"Getter and Setter Method Test failed for OutputPlateType"
					);
		}
	}
	
	
	
}
