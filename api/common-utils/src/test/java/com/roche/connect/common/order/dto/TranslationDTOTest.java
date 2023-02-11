package com.roche.connect.common.order.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TranslationDTOTest {

	private com.roche.connect.common.order.dto.TranslationDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.order.dto.TranslationDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetMessageGroup() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageGroup(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getMessageGroup(),
					"Getter and Setter Method Test failed for MessageGroup");
		}
	}

	@Test
	public void testGetSetMessageContent() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageContent(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getMessageContent(),
					"Getter and Setter Method Test failed for MessageContent");
		}
	}

}
