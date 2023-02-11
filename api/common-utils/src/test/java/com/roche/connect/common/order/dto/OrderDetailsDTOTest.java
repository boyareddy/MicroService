package com.roche.connect.common.order.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class OrderDetailsDTOTest {

	private com.roche.connect.common.order.dto.OrderDetailsDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.order.dto.OrderDetailsDTO();
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
	public void testGetSetOrderStatus() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderStatus(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOrderStatus(),
					"Getter and Setter Method Test failed for OrderStatus");
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
	public void testGetSetOrderComments() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderComments(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOrderComments(),
					"Getter and Setter Method Test failed for OrderComments");
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
	public void testGetSetPatientLastName() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPatientLastName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPatientLastName(),
					"Getter and Setter Method Test failed for PatientLastName");
		}
	}

	@Test
	public void testGetSetPatientFirstName() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPatientFirstName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPatientFirstName(),
					"Getter and Setter Method Test failed for PatientFirstName");
		}
	}

	@Test
	public void testGetSetPatientGender() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPatientGender(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPatientGender(),
					"Getter and Setter Method Test failed for PatientGender");
		}
	}

	@Test
	public void testGetSetPatientMedicalRecNo() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPatientMedicalRecNo(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPatientMedicalRecNo(),
					"Getter and Setter Method Test failed for PatientMedicalRecNo");
		}
	}

	@Test
	public void testGetSetPatientDOB() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPatientDOB(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPatientDOB(),
					"Getter and Setter Method Test failed for PatientDOB");
		}
	}

	@Test
	public void testGetSetPatientContactNo() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPatientContactNo(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPatientContactNo(),
					"Getter and Setter Method Test failed for PatientContactNo");
		}
	}

	@Test
	public void testGetSetTreatingDoctorName() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTreatingDoctorName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getTreatingDoctorName(),
					"Getter and Setter Method Test failed for TreatingDoctorName");
		}
	}

	@Test
	public void testGetSetTreatingDoctorContactNo() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTreatingDoctorContactNo(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getTreatingDoctorContactNo(),
					"Getter and Setter Method Test failed for TreatingDoctorContactNo");
		}
	}

	@Test
	public void testGetSetRefClinicianName() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRefClinicianName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getRefClinicianName(),
					"Getter and Setter Method Test failed for RefClinicianName");
		}
	}

	@Test
	public void testGetSetRefClinicianFaxNo() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRefClinicianFaxNo(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getRefClinicianFaxNo(),
					"Getter and Setter Method Test failed for RefClinicianFaxNo");
		}
	}

	@Test
	public void testGetSetOtherClinicianName() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOtherClinicianName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOtherClinicianName(),
					"Getter and Setter Method Test failed for OtherClinicianName");
		}
	}

	@Test
	public void testGetSetOtherClinicianFaxNo() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOtherClinicianFaxNo(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOtherClinicianFaxNo(),
					"Getter and Setter Method Test failed for OtherClinicianFaxNo");
		}
	}

	@Test
	public void testGetSetRefClinicianClinicName() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRefClinicianClinicName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getRefClinicianClinicName(),
					"Getter and Setter Method Test failed for RefClinicianClinicName");
		}
	}

	@Test
	public void testGetSetIvfStatus() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setIvfStatus(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getIvfStatus(),
					"Getter and Setter Method Test failed for IvfStatus");
		}
	}

	@Test
	public void testGetSetFetusNumber() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFetusNumber(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getFetusNumber(),
					"Getter and Setter Method Test failed for FetusNumber");
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
	public void testGetSetPatientId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPatientId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getPatientId()),
					"Getter and Setter Method Test failed for PatientId");
		}
	}

	@Test
	public void testGetSetPatientSampleId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPatientSampleId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getPatientSampleId()),
					"Getter and Setter Method Test failed for PatientSampleId");
		}
	}

	@Test
	public void testGetSetRetestSample() {
		Boolean[] testStringArray = { true, false };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRetestSample(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], (Boolean) classUnderTest.isRetestSample());
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
	public void testGetSetCollectionDate() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCollectionDate(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getCollectionDate()));
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

}
