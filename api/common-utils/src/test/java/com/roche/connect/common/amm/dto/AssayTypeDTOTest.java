package com.roche.connect.common.amm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AssayTypeDTOTest {
	
	private com.roche.connect.common.amm.dto.AssayTypeDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.amm.dto.AssayTypeDTO();
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
	public void testGetSetAssayVersion() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAssayVersion(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getAssayVersion(),
					"Getter and Setter Method Test failed for AssayVersion"
					);
		}
	}
	@Test
	public void testGetSetWorkflowDefFile() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setWorkflowDefFile(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getWorkflowDefFile(),
					"Getter and Setter Method Test failed for WorkflowDefFile"
					);
		}
	}
	@Test
	public void testGetSetWorkflowDefVersion() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setWorkflowDefVersion(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getWorkflowDefVersion(),
					"Getter and Setter Method Test failed for WorkflowDefVersion"
					);
		}
	}
	
}
