package com.roche.connect.common.lp24;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ConsumableTest {
	
	private com.roche.connect.common.lp24.Consumable classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.lp24.Consumable();
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
	
	
	
}
