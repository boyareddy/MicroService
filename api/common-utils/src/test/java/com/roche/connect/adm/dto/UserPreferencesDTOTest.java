package com.roche.connect.adm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class UserPreferencesDTOTest {
	
	private com.roche.connect.adm.dto.UserPreferencesDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.adm.dto.UserPreferencesDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetPreferenceKey() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPreferenceKey(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPreferenceKey(),
					"Getter and Setter Method Test failed for PreferenceKey");
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
