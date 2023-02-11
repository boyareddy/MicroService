package com.roche.connect.adm.dto;

import java.lang.String;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TokenDetailsTest {

	private com.roche.connect.adm.dto.TokenDetails classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.adm.dto.TokenDetails();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetUserName() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setUserName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getUserName(),
					"Getter and Setter Method Test failed for UserName");
		}
	}

	@Test
	public void testGetSetDomainName() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDomainName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDomainName(),
					"Getter and Setter Method Test failed for DomainName");
		}
	}

	@Test
	public void testGetSetIssuedDate() {
		Date date1 = new Date();
		Date date2 = new Date();
		Date[] testStringArray = { date1, date2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setIssuedDate(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getIssuedDate()));
		}
	}

	@Test
	public void testGetSetExpiryDate() {
		Date date1 = new Date();
		Date date2 = new Date();
		Date[] testStringArray = { date1, date2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setExpiryDate(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getExpiryDate()));
		}
	}

}
