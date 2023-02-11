package com.roche.connect.common.dmm;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DeviceTypeDTOTest {

	private com.roche.connect.common.dmm.DeviceTypeDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dmm.DeviceTypeDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getName(),
					"Getter and Setter Method Test failed for Name");
		}
	}

	@Test
	public void testGetSetDescription() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDescription(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDescription(),
					"Getter and Setter Method Test failed for Description");
		}
	}

	@Test
	public void testGetSetIsRetired() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setIsRetired(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getIsRetired(),
					"Getter and Setter Method Test failed for IsRetired");
		}
	}

	@Test
	public void testGetSetEditedBy() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEditedBy(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getEditedBy(),
					"Getter and Setter Method Test failed for EditedBy");
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
	public void testGetSetDeviceTypeId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceTypeId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDeviceTypeId(),
					"Getter and Setter Method Test failed for DeviceTypeId");
		}
	}

}
