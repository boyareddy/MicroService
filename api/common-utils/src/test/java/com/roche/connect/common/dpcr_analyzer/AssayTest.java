package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AssayTest {
	
	private com.roche.connect.common.dpcr_analyzer.Assay classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.Assay();
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
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getName(),
					"Getter and Setter Method Test failed for Name"
					);
		}
	}
	@Test
	public void testGetSetVersion() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setVersion(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getVersion(),
					"Getter and Setter Method Test failed for Version"
					);
		}
	}
	@Test
	public void testGetSetMasterMix() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMasterMix(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getMasterMix(),
					"Getter and Setter Method Test failed for MasterMix"
					);
		}
	}
	@Test
	public void testGetSetKit() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setKit(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getKit(),
					"Getter and Setter Method Test failed for Kit"
					);
		}
	}
	
}
