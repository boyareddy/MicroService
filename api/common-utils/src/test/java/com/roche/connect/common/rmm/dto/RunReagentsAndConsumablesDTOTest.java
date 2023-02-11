package com.roche.connect.common.rmm.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RunReagentsAndConsumablesDTOTest {

	private com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getType(),
					"Getter and Setter Method Test failed for Type");
		}
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
	public void testGetSetRunReagentsAndConsumablesId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunReagentsAndConsumablesId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getRunReagentsAndConsumablesId()),
					"Getter and Setter Method Test failed for RunReagentsAndConsumablesId");
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
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCreatedDateTime(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getCreatedDateTime()));
		}
	}

}
