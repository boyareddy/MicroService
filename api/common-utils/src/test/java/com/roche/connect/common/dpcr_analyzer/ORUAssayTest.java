package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ORUAssayTest {
	
	private com.roche.connect.common.dpcr_analyzer.ORUAssay classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.ORUAssay();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}
	@Test
	public void testGetSetName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getName(),
					"Getter and Setter Method Test failed for Name"
					);
		}
	}
	@Test
	public void testGetSetVersion() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setVersion(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getVersion(),
					"Getter and Setter Method Test failed for Version"
					);
		}
	}
	@Test
	public void testGetSetPackageName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPackageName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getPackageName(),
					"Getter and Setter Method Test failed for PackageName"
					);
		}
	}@Test
	public void testGetSetKit() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setKit(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getKit(),
					"Getter and Setter Method Test failed for Kit"
					);
		}
	}
	@Test
	public void testGetSetQuantitativeValue() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQuantitativeValue(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getQuantitativeValue(),
					"Getter and Setter Method Test failed for QuantitativeValue"
					);
		}
	}
	@Test
	public void testGetSetQuantitativeResult() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQuantitativeResult(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getQuantitativeResult(),
					"Getter and Setter Method Test failed for QuantitativeResult"
					);
		}
	}
	@Test
	public void testGetSetQualitativeValue() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQualitativeValue(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getQualitativeValue(),
					"Getter and Setter Method Test failed for QualitativeValue"
					);
		}
	}
	@Test
	public void testGetSetQualitativeResult() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQualitativeResult(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getQualitativeResult(),
					"Getter and Setter Method Test failed for QualitativeResult"
					);
		}
	}
	
}
