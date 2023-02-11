package com.roche.connect.common.order.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AssayDTOTest {

	private com.roche.connect.common.order.dto.AssayDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.order.dto.AssayDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
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
	public void testGetSetEggDonor() {

		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEggDonor(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getEggDonor(),
					"Getter and Setter Method Test failed for EggDonor");
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
	public void testGetSetPatientAssayid() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPatientAssayid(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getPatientAssayid()),
					"Getter and Setter Method Test failed for PatientAssayid");
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

}
