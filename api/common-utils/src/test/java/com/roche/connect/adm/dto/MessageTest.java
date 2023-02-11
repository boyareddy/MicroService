package com.roche.connect.adm.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class MessageTest {
	
	private com.roche.connect.adm.dto.Message classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.adm.dto.Message();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetMessage() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMsg(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getMsg(),
					"Getter and Setter Method Test failed for guid");
		}
	}
	@Test
	public void testGetSetSeverity() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSeverity(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSeverity(),
					"Getter and Setter Method Test failed for guid"
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
	public void testGetSetTopic() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTopic(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getTopic(),
					"Getter and Setter Method Test failed for Topic"
				);
		}
	}
	@Test()
	public void testGetSetViewed() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setViewed(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getViewed(),
					"Getter and Setter Method Test failed for Viewed"
					);
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
	public void testGetSetCreatedDateTime() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCreatedDateTime(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getCreatedDateTime()));
		}
	}

	@Test
	public void testGetSetViewedDateTime() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setViewedDateTime(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getViewedDateTime()));
		}
	}
	
}
