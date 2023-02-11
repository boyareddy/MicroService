package com.roche.connect.commonutils.testng;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ProcessStepDuplicateDTOTest {
	
	private com.roche.connect.common.util.ProcessStepDuplicateDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.util.ProcessStepDuplicateDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetAssayType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAssayType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getAssayType(),
					"Getter and Setter Method Test failed for AssayType"
					);
		}
	}
	@Test
	public void testGetSetDeviceType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDeviceType(),
					"Getter and Setter Method Test failed for DeviceType"
					);
		}
	}
	@Test
	public void testGetSetDeviceName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDeviceName(),
					"Getter and Setter Method Test failed for DeviceName"
					);
		}
	}
	@Test
	public void testGetSetManualVerificationFlag() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setManualVerificationFlag(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getManualVerificationFlag(),
					"Getter and Setter Method Test failed for ManualVerificationFlag"
					);
		}
	}
	@Test
	public void testGetSetProcessStepSequence() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProcessStepSequence(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getProcessStepSequence(),
					"Getter and Setter Method Test failed for ProcessStepSequence"
					);
		}
	}
	@Test
	public void testGetSetProcessStepName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProcessStepName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getProcessStepName(),
					"Getter and Setter Method Test failed for ProcessStepName"
					);
		}
	}
	@Test
	public void testGetSetInputContainerType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setInputContainerType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getInputContainerType(),
					"Getter and Setter Method Test failed for InputContainerType"
					);
		}
	}
	@Test
	public void testGetSetOutputContainerType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputContainerType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getOutputContainerType(),
					"Getter and Setter Method Test failed for OutputContainerType"
					);
		}
	}
	@Test
	public void testGetSetSampleVolume() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleVolume(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSampleVolume(),
					"Getter and Setter Method Test failed for SampleVolume"
					);
		}
	}
	@Test
	public void testGetSetEluateVolume() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEluateVolume(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getEluateVolume(),
					"Getter and Setter Method Test failed for EluateVolume"
					);
		}
	}
	@Test
	public void testGetSetReagent() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReagent(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getReagent(),
					"Getter and Setter Method Test failed for Reagent"
					);
		}
	}
	
	
	
}
