package com.roche.connect.common.amm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ProcessStepActionDTOTest {

	private com.roche.connect.common.amm.dto.ProcessStepActionDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.amm.dto.ProcessStepActionDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
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
	public void testGetSetOutputContainerType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputContainerType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOutputContainerType(),
					"Getter and Setter Method Test failed for OutputContainerType");
		}
	}

	@Test
	public void testGetSetManualVerificationFlag() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setManualVerificationFlag(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getManualVerificationFlag(),
					"Getter and Setter Method Test failed for ManualVerificationFlag");
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
	public void testGetSetSampleVolume() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleVolume(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getSampleVolume(),
					"Getter and Setter Method Test failed for SampleVolume");
		}
	}

	@Test
	public void testGetSetEluateVolume() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEluateVolume(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getEluateVolume(),
					"Getter and Setter Method Test failed for EluateVolume");
		}
	}

	@Test
	public void testGetSetReagent() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReagent(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getReagent(),
					"Getter and Setter Method Test failed for Reagent");
		}
	}

}
