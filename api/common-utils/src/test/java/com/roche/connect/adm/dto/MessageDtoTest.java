package com.roche.connect.adm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class MessageDtoTest {
	
	private com.roche.connect.adm.dto.MessageDto classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.adm.dto.MessageDto();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetMessage() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageGroup(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getMessageGroup(),
					"Getter and Setter Method Test failed for MessageGroup");
		}
	}
	@Test
	public void testGetSetSeverity() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setLocale(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getLocale(),
					"Getter and Setter Method Test failed for Locale"
					);
		}
	}
	
	
}
