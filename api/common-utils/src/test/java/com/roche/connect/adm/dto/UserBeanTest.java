package com.roche.connect.adm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class UserBeanTest {

	private com.roche.connect.adm.dto.UserBean classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.adm.dto.UserBean();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetMessage() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEmail(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getEmail(),
					"Getter and Setter Method Test failed for Email");
		}
	}

	@Test
	public void testGetSetSeverity() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setLoginName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getLoginName(),
					"Getter and Setter Method Test failed for LoginName");
		}
	}

	@Test()
	public void testGetSetTitle() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFirstName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getFirstName(),
					"Getter and Setter Method Test failed for FirstName");
		}
	}

	@Test
	public void testGetSetId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getId()),
					"Getter and Setter Method Test failed for Id");
		}
	}

	@Test
	public void testGetSetRetired() {
		Boolean[] testStringArray = { true, false };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRetired(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], (Boolean) classUnderTest.isRetired());
		}
	}

}
