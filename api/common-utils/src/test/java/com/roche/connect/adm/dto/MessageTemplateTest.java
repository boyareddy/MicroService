package com.roche.connect.adm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class MessageTemplateTest {
	
	private com.roche.connect.adm.dto.MessageTemplate classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.adm.dto.MessageTemplate();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetActiveFlag() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setActiveFlag(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getActiveFlag(),
					"Getter and Setter Method Test failed for ActiveFlag");
		}
	}
	@Test
	public void testGetSetChannel() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setChannel(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getChannel(),
					"Getter and Setter Method Test failed for Channel"
					);
		}
	}
	@Test()
	public void testGetSetTitle() {
	
		
		
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTitle(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getTitle(),
					"Getter and Setter Method Test failed for Title"
					);
		}
	}
	@Test()
	public void testGetSetDescription() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDescription(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDescription(),
					"Getter and Setter Method Test failed for Description"
				);
		}
	}
	@Test()
	public void testGetSetSeverity() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSeverity(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSeverity(),
					"Getter and Setter Method Test failed for Severity"
					);
		}
	}
	@Test()
	public void testGetSetLocale() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setLocale(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getLocale(),
					"Getter and Setter Method Test failed for Locale"
					);
		}
	}
	@Test()
	public void testGetSetMessageGroup() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageGroup(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getMessageGroup(),
					"Getter and Setter Method Test failed for MessageGroup"
					);
		}
	}
	
}
