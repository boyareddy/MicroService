package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class QueryResponseSampleTest {
	
	private com.roche.connect.common.dpcr_analyzer.QueryResponseSample classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.QueryResponseSample();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}
	@Test
	public void testGetSetAccessioningId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAccessioningId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getAccessioningId(),
					"Getter and Setter Method Test failed for AccessioningId"
					);
		}
	}
	@Test
	public void testGetSetContainerId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setContainerId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getContainerId(),
					"Getter and Setter Method Test failed for ContainerId"
					);
		}
	}
	@Test
	public void testGetSetPosition() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPosition(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getPosition(),
					"Getter and Setter Method Test failed for Position"
					);
		}
	}
	@Test
	public void testGetSetStatus() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setStatus(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getStatus(),
					"Getter and Setter Method Test failed for Status"
					);
		}
	}
	@Test
	public void testGetSetPlateIntegator() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPlateIntegator(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getPlateIntegator(),
					"Getter and Setter Method Test failed for PlateIntegator"
					);
		}
	}
}
