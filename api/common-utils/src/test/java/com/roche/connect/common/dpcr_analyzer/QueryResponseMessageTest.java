package com.roche.connect.common.dpcr_analyzer;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class QueryResponseMessageTest {
	
	private com.roche.connect.common.dpcr_analyzer.QueryResponseMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dpcr_analyzer.QueryResponseMessage();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
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
	@Test
	public void testGetSetMessageType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getMessageType(),
					"Getter and Setter Method Test failed for MessageType"
					);
		}
	}
	@Test
	public void testGetSetRunId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRunId(),
					"Getter and Setter Method Test failed for RunId"
					);
		}
	}
	
}
