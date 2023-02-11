package com.roche.connect.common.rmm.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SampleResultsDetailDTOTest {

	private com.roche.connect.common.rmm.dto.SampleResultsDetailDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.rmm.dto.SampleResultsDetailDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetAttributeName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAttributeName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAttributeName(),
					"Getter and Setter Method Test failed for AttributeName");
		}
	}

	@Test
	public void testGetSetAttributeValue() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAttributeValue(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAttributeValue(),
					"Getter and Setter Method Test failed for AttributeValue");
		}
	}

	@Test
	public void testGetSetUpdatedBy() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setUpdatedBy(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getUpdatedBy(),
					"Getter and Setter Method Test failed for UpdatedBy");
		}
	}

	@Test
	public void testGetSetCreatedBy() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCreatedBy(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getCreatedBy(),
					"Getter and Setter Method Test failed for CreatedBy");
		}
	}

	@Test
	public void testGetSetSampleResultsDetailId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleResultsDetailId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getSampleResultsDetailId()),
					"Getter and Setter Method Test failed for SampleResultsDetailId");
		}
	}

	@Test
	public void testGetSetUpdatedDateTime() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setUpdatedDateTime(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getUpdatedDateTime()));
		}
	}

	@Test
	public void testGetSetCreatedDateTime() {
		Date date1 = new Date();
		Date date2 = new Date();
		Date[] testStringArray = { date1, date2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCreatedDateTime(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getCreatedDateTime()));
		}
	}

}
