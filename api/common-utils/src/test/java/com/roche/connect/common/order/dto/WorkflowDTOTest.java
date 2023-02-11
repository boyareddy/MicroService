package com.roche.connect.common.order.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WorkflowDTOTest {

	private com.roche.connect.common.order.dto.WorkflowDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.order.dto.WorkflowDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetAccessioningId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAccessioningId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAccessioningId(),
					"Getter and Setter Method Test failed for AccessioningId");
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
	public void testGetSetOrderId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getOrderId()),
					"Getter and Setter Method Test failed for OrderId");
		}
	}

	@Test
	public void testGetSetWorkflowType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setWorkflowType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getWorkflowType(),
					"Getter and Setter Method Test failed for WorkflowType");
		}
	}

	@Test
	public void testGetSetAssaytype() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAssaytype(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAssaytype(),
					"Getter and Setter Method Test failed for Assaytype");
		}
	}

	@Test
	public void testGetSetSampletype() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampletype(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getSampletype(),
					"Getter and Setter Method Test failed for Sampletype");
		}
	}

	@Test
	public void testGetSetWorkflowStatus() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setWorkflowStatus(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getWorkflowStatus(),
					"Getter and Setter Method Test failed for WorkflowStatus");
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
	public void testGetSetCreateDate() {
		Date date1 = new Date();
		Date date2 = new Date();
		Date[] testStringArray = { date1, date2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCreateDate(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getCreateDate()));
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
