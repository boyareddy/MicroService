package com.roche.connect.adm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class UserEntityDTOTest {
	
	private com.roche.connect.adm.dto.UserEntityDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.adm.dto.UserEntityDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetPassword() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPassword(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPassword(),
					"Getter and Setter Method Test failed for Password");
		}
	}
		
	
}
