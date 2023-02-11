package com.roche.connect.common.rmm.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SearchRunResultTest {

	private com.roche.connect.common.rmm.dto.SearchRunResult classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.rmm.dto.SearchRunResult();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetDevice() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDevice(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDevice(),
					"Getter and Setter Method Test failed for Device");
		}
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
	public void testGetSetDeviceRunId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceRunId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDeviceRunId(),
					"Getter and Setter Method Test failed for DeviceRunId");
		}
	}

	@Test
	public void testGetSetRunStatus() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunStatus(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getRunStatus(),
					"Getter and Setter Method Test failed for RunStatus");
		}
	}

	@Test
	public void testGetSetOperatorName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOperatorName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOperatorName(),
					"Getter and Setter Method Test failed for OperatorName");
		}
	}

	@Test
	public void testGetSetRunResultId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunResultId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getRunResultId()),
					"Getter and Setter Method Test failed for RunResultId");
		}
	}

	@Test
	public void testGetSetSampleCount() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleCount(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getSampleCount()),
					"Getter and Setter Method Test failed for SampleCount");
		}
	}

	@Test
	public void testGetSetRunCompletionTime() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunCompletionTime(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getRunCompletionTime()));
		}
	}

}
