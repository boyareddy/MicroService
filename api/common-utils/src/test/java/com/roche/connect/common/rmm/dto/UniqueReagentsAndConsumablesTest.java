package com.roche.connect.common.rmm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class UniqueReagentsAndConsumablesTest {

	private com.roche.connect.common.rmm.dto.UniqueReagentsAndConsumables classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.rmm.dto.UniqueReagentsAndConsumables();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetAttributeName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAttributeName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAttributeName(),
					"Getter and Setter Method Test failed for AttributeName");
		}
	}

	@Test
	public void testGetSetAttributeValue() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAttributeValue(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAttributeValue(),
					"Getter and Setter Method Test failed for AttributeValue");
		}
	}

}
