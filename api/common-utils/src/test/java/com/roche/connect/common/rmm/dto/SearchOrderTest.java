package com.roche.connect.common.rmm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SearchOrderTest {

	private com.roche.connect.common.rmm.dto.SearchOrder classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.rmm.dto.SearchOrder();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetProcessStepName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProcessStepName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getProcessStepName(),
					"Getter and Setter Method Test failed for ProcessStepName");
		}
	}

	@Test
	public void testGetSetFlags() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFlags(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getFlags(),
					"Getter and Setter Method Test failed for Flags");
		}
	}

	@Test
	public void testGetSetAssayType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAssayType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAssayType(),
					"Getter and Setter Method Test failed for AssayType");
		}
	}

	@Test
	public void testGetSetSampleType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getSampleType(),
					"Getter and Setter Method Test failed for SampleType");
		}
	}

	@Test
	public void testGetSetRunResultsId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunResultsId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getRunResultsId()),
					"Getter and Setter Method Test failed for RunResultsId");
		}
	}

	@Test
	public void testGetSetSampleResultId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleResultId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getSampleResultId()),
					"Getter and Setter Method Test failed for SampleResultId");
		}
	}

	@Test
	public void testGetSetOrderId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getOrderId()),
					"Getter and Setter Method Test failed for OrderId");
		}
	}

}
