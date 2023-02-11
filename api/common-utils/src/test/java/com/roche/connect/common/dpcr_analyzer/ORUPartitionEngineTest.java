package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ORUPartitionEngineTest {
	
	private com.roche.connect.common.dpcr_analyzer.ORUPartitionEngine classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.ORUPartitionEngine();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}
	@Test
	public void testGetSetPlateId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPlateId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getPlateId(),
					"Getter and Setter Method Test failed for PlateId"
					);
		}
	}
	@Test
	public void testGetSetSerialNumber() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSerialNumber(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSerialNumber(),
					"Getter and Setter Method Test failed for SerialNumber"
					);
		}
	}
	@Test
	public void testGetSetFluidId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFluidId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getFluidId(),
					"Getter and Setter Method Test failed for FluidId"
					);
		}
	}
	@Test
	public void testGetSetDateandTime() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDateandTime(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDateandTime(),
					"Getter and Setter Method Test failed for DateandTime"
					);
		}
	}
	
}
