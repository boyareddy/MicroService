package com.roche.connect.common.amm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestOptionsDTOTest {

	private com.roche.connect.common.amm.dto.TestOptionsDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.amm.dto.TestOptionsDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetTestName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTestName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getTestName(),
					"Getter and Setter Method Test failed for TestName");
		}
	}

	@Test
	public void testGetSetTestDescription() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTestDescription(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getTestDescription(),
					"Getter and Setter Method Test failed for TestDescription");
		}
	}

	@Test
	public void testGetSetAssayType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAssayType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAssayType(),
					"Getter and Setter Method Test failed for AssayType");
		}
	}

}
