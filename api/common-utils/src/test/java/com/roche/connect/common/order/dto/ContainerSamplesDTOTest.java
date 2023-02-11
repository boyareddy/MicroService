package com.roche.connect.common.order.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ContainerSamplesDTOTest {

	private com.roche.connect.common.order.dto.ContainerSamplesDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.order.dto.ContainerSamplesDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetContainerID() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setContainerID(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getContainerID(),
					"Getter and Setter Method Test failed for ContainerID");
		}
	}

	@Test
	public void testGetSetPosition() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPosition(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPosition(),
					"Getter and Setter Method Test failed for Position");
		}
	}

	@Test
	public void testGetSetContainerType() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setContainerType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getContainerType(),
					"Getter and Setter Method Test failed for ContainerType");
		}
	}

	@Test
	public void testGetSetAccessioningID() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAccessioningID(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAccessioningID(),
					"Getter and Setter Method Test failed for AccessioningID");
		}
	}

	@Test
	public void testGetSetDeviceRunID() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceRunID(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDeviceRunID(),
					"Getter and Setter Method Test failed for DeviceRunID");
		}
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
	public void testGetSetStatus() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setStatus(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getStatus(),
					"Getter and Setter Method Test failed for Status");
		}
	}

	@Test
	public void testGetSetDeviceID() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceID(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDeviceID(),
					"Getter and Setter Method Test failed for DeviceID");
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
	public void testGetSetOrderComments() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderComments(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOrderComments(),
					"Getter and Setter Method Test failed for OrderComments");
		}
	}

	@Test
	public void testGetSetContainerSampleId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setContainerSampleId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getContainerSampleId()),
					"Getter and Setter Method Test failed for ContainerSampleId");
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
	public void testGetSetLoadID() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setLoadID(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getLoadID()),
					"Getter and Setter Method Test failed for LoadID");
		}
	}

	@Test
	public void testGetSetOrderID() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderID(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getOrderID()),
					"Getter and Setter Method Test failed for OrderID");
		}
	}

}
