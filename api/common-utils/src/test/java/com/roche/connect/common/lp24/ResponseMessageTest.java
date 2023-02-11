package com.roche.connect.common.lp24;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ResponseMessageTest {
	
	private com.roche.connect.common.lp24.ResponseMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.lp24.ResponseMessage();
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
	public void testGetSetLp24Protocol() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setLp24Protocol(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getLp24Protocol(),
					"Getter and Setter Method Test failed for Lp24Protocol"
					);
		}
	}
	@Test
	public void testGetSetSampleType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSampleType(),
					"Getter and Setter Method Test failed for SampleType"
					);
		}
	}
	@Test
	public void testGetSetSpecimenDescription() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSpecimenDescription(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSpecimenDescription(),
					"Getter and Setter Method Test failed for SpecimenDescription"
					);
		}
	}
	@Test
	public void testGetSetQueryResponseStatus() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQueryResponseStatus(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getQueryResponseStatus(),
					"Getter and Setter Method Test failed for QueryResponseStatus"
					);
		}
	}
	@Test
	public void testGetSetOrderControl() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderControl(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getOrderControl(),
					"Getter and Setter Method Test failed for OrderControl"
					);
		}
	}
	@Test
	public void testGetSetMessageControlId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageControlId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getMessageControlId(),
					"Getter and Setter Method Test failed for MessageControlId"
					);
		}
	}
	

	
}
