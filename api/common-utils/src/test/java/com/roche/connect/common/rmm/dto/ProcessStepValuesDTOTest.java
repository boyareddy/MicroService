package com.roche.connect.common.rmm.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ProcessStepValuesDTOTest {

	private com.roche.connect.common.rmm.dto.ProcessStepValuesDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.rmm.dto.ProcessStepValuesDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetAccesssioningId() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAccesssioningId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAccesssioningId(),
					"Getter and Setter Method Test failed for AccesssioningId");
		}
	}

	@Test
	public void testGetSetOutputContainerId() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputContainerId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOutputContainerId(),
					"Getter and Setter Method Test failed for OutputContainerId");
		}
	}

	@Test
	public void testGetSetOutputContainerPosition() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputContainerPosition(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOutputContainerPosition(),
					"Getter and Setter Method Test failed for OutputContainerPosition");
		}
	}

	@Test
	public void testGetSetOutputKitId() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputKitId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOutputKitId(),
					"Getter and Setter Method Test failed for OutputKitId");
		}
	}

	@Test
	public void testGetSetOutputContainerType() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputContainerType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOutputContainerType(),
					"Getter and Setter Method Test failed for OutputContainerType");
		}
	}

	@Test
	public void testGetSetPlateType() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPlateType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPlateType(),
					"Getter and Setter Method Test failed for PlateType");
		}
	}

	@Test
	public void testGetSetDeviceId() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDeviceId(),
					"Getter and Setter Method Test failed for DeviceId");
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
	public void testGetSetRunStatus() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunStatus(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getRunStatus(),
					"Getter and Setter Method Test failed for RunStatus");
		}
	}

	@Test
	public void testGetSetRunFlag() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunFlag(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getRunFlag(),
					"Getter and Setter Method Test failed for RunFlag");
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
	public void testGetSetComments() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setComments(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getComments(),
					"Getter and Setter Method Test failed for Comments");
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
	public void testGetSetUpdatedBy() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setUpdatedBy(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getUpdatedBy(),
					"Getter and Setter Method Test failed for UpdatedBy");
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
	public void testGetSetQuantitativeResult() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQuantitativeResult(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getQuantitativeResult(),
					"Getter and Setter Method Test failed for QuantitativeResult");
		}
	}

	@Test
	public void testGetSetQualitativeResult() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQualitativeResult(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getQualitativeResult(),
					"Getter and Setter Method Test failed for QualitativeResult");
		}
	}

	@Test
	public void testGetSetSampleResultId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleResultId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), classUnderTest.getSampleResultId(),
					"Getter and Setter Method Test failed for SampleResultId");
		}
	}

	@Test
	public void testGetSetOrderId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), classUnderTest.getOrderId(),
					"Getter and Setter Method Test failed for OrderId");
		}
	}

	@Test
	public void testGetSetRunResultId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunResultId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), classUnderTest.getRunResultId(),
					"Getter and Setter Method Test failed for RunResultId");
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
	public void testGetSetRunStartTime() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunStartTime(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getRunStartTime()));
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

	@Test
	public void testGetSetRunRemainingTime() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunRemainingTime(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), classUnderTest.getRunRemainingTime(),
					"Getter and Setter Method Test failed for RunRemainingTime");
		}
	}

}
