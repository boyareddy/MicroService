package com.roche.connect.common.amm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DeviceTestOptionsDTOTest {

	private com.roche.connect.common.amm.dto.DeviceTestOptionsDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.amm.dto.DeviceTestOptionsDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetTestName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTestName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getTestName(),
					"Getter and Setter Method Test failed for TestName");
		}
	}

	@Test
	public void testGetSetTestProtocol() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTestProtocol(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getTestProtocol(),
					"Getter and Setter Method Test failed for TestProtocol");
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
	public void testGetSetAssayType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAssayType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAssayType(),
					"Getter and Setter Method Test failed for AssayType");
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
	public void testGetSetAnalysisPackageName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAnalysisPackageName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAnalysisPackageName(),
					"Getter and Setter Method Test failed for AnalysisPackageName");
		}
	}

}
