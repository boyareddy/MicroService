package com.roche.connect.adm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class UserDetailsDTOTest {
	
	private com.roche.connect.adm.dto.UserDetailsDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.adm.dto.UserDetailsDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetLoginName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setLoginName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getLoginName(),
					"Getter and Setter Method Test failed for LoginName");
		}
	}
	@Test
	public void testGetSetFirstName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFirstName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getFirstName(),
					"Getter and Setter Method Test failed for FirstName");
		}
	}
	@Test
	public void testGetSetLastName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setLastName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getLastName(),
					"Getter and Setter Method Test failed for LastName");
		}
	}
	@Test
	public void testGetSetEmail() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEmail(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getEmail(),
					"Getter and Setter Method Test failed for Email");
		}
	}
	
}
