package com.roche.connect.common.amm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AssayInputDataValidationsDTOTest {
	
	private com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetFieldName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFieldName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getFieldName(),
					"Getter and Setter Method Test failed for FieldName"
					);
		}
	}
	@Test
	public void testGetSetExpression() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setExpression(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getExpression(),
					"Getter and Setter Method Test failed for Expression"
					);
		}
	}
	@Test
	public void testGetSetAssayType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAssayType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getAssayType(),
					"Getter and Setter Method Test failed for AssayType"
					);
		}
	}
	@Test
	public void testGetSetIsMandatory() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setIsMandatory(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getIsMandatory(),
					"Getter and Setter Method Test failed for IsMandatory"
					);
		}
	}
	@Test
	public void testGetSetGroupName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setGroupName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getGroupName(),
					"Getter and Setter Method Test failed for GroupName"
					);
		}
	}
	
	
}
