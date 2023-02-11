package com.roche.connect.common.amm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AssayListDataDTOTest {
	
	private com.roche.connect.common.amm.dto.AssayListDataDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.amm.dto.AssayListDataDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetListType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setListType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getListType(),
					"Getter and Setter Method Test failed for ListType"
					);
		}
	}
	@Test
	public void testGetSetValue() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setValue(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getValue(),
					"Getter and Setter Method Test failed for Value"
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
	
	
}
