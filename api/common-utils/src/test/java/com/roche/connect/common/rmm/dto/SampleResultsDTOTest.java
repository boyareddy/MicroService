package com.roche.connect.common.rmm.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SampleResultsDTOTest {

	private com.roche.connect.common.rmm.dto.SampleResultsDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.rmm.dto.SampleResultsDTO();
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
	public void testGetSetInputContainerId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setInputContainerId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getInputContainerId(),
					"Getter and Setter Method Test failed for InputContainerId");
		}
	}

	@Test
	public void testGetSetInputContainerPosition() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setInputContainerPosition(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getInputContainerPosition(),
					"Getter and Setter Method Test failed for InputContainerPosition");
		}
	}

	@Test
	public void testGetSetInputKitId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setInputKitId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getInputKitId(),
					"Getter and Setter Method Test failed for InputKitId");
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
	public void testGetSetOutputContainerType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputContainerType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOutputContainerType(),
					"Getter and Setter Method Test failed for OutputContainerType");
		}
	}

	@Test
	public void testGetSetOutputPlateType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputPlateType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOutputPlateType(),
					"Getter and Setter Method Test failed for OutputPlateType");
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
	public void testGetSetStatus() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setStatus(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getStatus(),
					"Getter and Setter Method Test failed for Status");
		}
	}

	@Test
	public void testGetSetResult() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setResult(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getResult(),
					"Getter and Setter Method Test failed for Result");
		}
	}

	@Test
	public void testGetSetFlag() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFlag(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getFlag(),
					"Getter and Setter Method Test failed for Flag");
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
	public void testGetSetVerifiedBy() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setVerifiedBy(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getVerifiedBy(),
					"Getter and Setter Method Test failed for VerifiedBy");
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
	public void testGetSetInputContainerType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setInputContainerType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getInputContainerType(),
					"Getter and Setter Method Test failed for InputContainerType");
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

	@Test
	public void testGetSetReceivedDate() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReceivedDate(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getReceivedDate()));
		}
	}

	@Test
	public void testGetSetVerifiedDate() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setVerifiedDate(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getVerifiedDate()));
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
	public void testGetSetMandatoryFieldMissing() {
		Boolean[] testStringArray = { true, false };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMandatoryFieldMissing(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], (Boolean) classUnderTest.isMandatoryFieldMissing());
		}
	}

}
