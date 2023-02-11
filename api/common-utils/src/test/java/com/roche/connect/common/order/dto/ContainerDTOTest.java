package com.roche.connect.common.order.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ContainerDTOTest {

	private com.roche.connect.common.order.dto.ContainerDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.order.dto.ContainerDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetContainerId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setContainerId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getContainerId(),
					"Getter and Setter Method Test failed for ContainerId");
		}
	}

	@Test
	public void testGetSetContainerType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setContainerType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getContainerType(),
					"Getter and Setter Method Test failed for ContainerType");
		}
	}

	@Test
	public void testGetSetSampleCount() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleCount(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getSampleCount()),
					"Getter and Setter Method Test failed for SampleCount");
		}
	}

}
