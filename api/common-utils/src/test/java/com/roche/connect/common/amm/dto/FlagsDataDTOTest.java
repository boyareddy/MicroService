package com.roche.connect.common.amm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class FlagsDataDTOTest {

	private com.roche.connect.common.amm.dto.FlagsDataDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.amm.dto.FlagsDataDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetFlagCode() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFlagCode(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getFlagCode(),
					"Getter and Setter Method Test failed for FlagCode");
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
	public void testGetSetSeverity() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSeverity(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getSeverity(),
					"Getter and Setter Method Test failed for Severity");
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
	public void testGetSetDeviceType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDeviceType(),
					"Getter and Setter Method Test failed for DeviceType");
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
	public void testGetSetSource() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSource(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getSource(),
					"Getter and Setter Method Test failed for Source");
		}
	}

	@Test
	public void testGetSetSampleOrRunLevel() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleOrRunLevel(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getSampleOrRunLevel(),
					"Getter and Setter Method Test failed for SampleOrRunLevel");
		}
	}

}
