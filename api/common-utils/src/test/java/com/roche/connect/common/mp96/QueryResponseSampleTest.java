package com.roche.connect.common.mp96;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class QueryResponseSampleTest {
	
	private com.roche.connect.common.mp96.QueryResponseSample classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.mp96.QueryResponseSample();
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
	public void testGetSetReagentName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReagentName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getReagentName(),
					"Getter and Setter Method Test failed for ReagentName"
					);
		}
	}
	@Test
	public void testGetSetReagentVersion() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReagentVersion(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getReagentVersion(),
					"Getter and Setter Method Test failed for ReagentVersion"
					);
		}
	}
	@Test
	public void testGetSetProtocolName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProtocolName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getProtocolName(),
					"Getter and Setter Method Test failed for ProtocolName"
					);
		}
	}
	@Test
	public void testGetSetProtocolVersion() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProtocolVersion(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getProtocolVersion(),
					"Getter and Setter Method Test failed for ProtocolVersion"
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
	public void testGetSetComment() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setComment(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getComment(),
					"Getter and Setter Method Test failed for Comment"
					);
		}
	}
	
}
