package com.roche.connect.common.order.dto;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class StatusDtoTest {

	private com.roche.connect.common.order.dto.StatusDto classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.order.dto.StatusDto();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetStatus() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setStatus(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getStatus(),
					"Getter and Setter Method Test failed for Status");
		}
	}

	@Test
	public void testGetSetWorkflowStatus() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setWorkflowStatus(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getWorkflowStatus(),
					"Getter and Setter Method Test failed for WorkflowStatus");
		}
	}

	@Test
	public void testGetSetWorkflowType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setWorkflowType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getWorkflowType(),
					"Getter and Setter Method Test failed for WorkflowType");
		}
	}

}
