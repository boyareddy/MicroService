package com.roche.connect.common.order.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PatientDTOTest {

	private com.roche.connect.common.order.dto.PatientDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.order.dto.PatientDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
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
	public void testGetSetRefClinicianName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRefClinicianName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getRefClinicianName(),
					"Getter and Setter Method Test failed for RefClinicianName");
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
	public void testGetSetClinicName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setClinicName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getClinicName(),
					"Getter and Setter Method Test failed for ClinicName");
		}
	}

	@Test
	public void testGetSetAccountNumber() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAccountNumber(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAccountNumber(),
					"Getter and Setter Method Test failed for AccountNumber");
		}
	}

	@Test
	public void testGetSetLabId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setLabId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getLabId(),
					"Getter and Setter Method Test failed for LabId");
		}
	}

	@Test
	public void testGetSetReasonForReferral() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReasonForReferral(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getReasonForReferral(),
					"Getter and Setter Method Test failed for ReasonForReferral");
		}
	}

}
