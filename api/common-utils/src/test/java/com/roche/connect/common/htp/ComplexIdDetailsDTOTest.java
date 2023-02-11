package com.roche.connect.common.htp;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ComplexIdDetailsDTOTest {
	
	private com.roche.connect.common.htp.ComplexIdDetailsDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.htp.ComplexIdDetailsDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}
	@Test
	public void testGetSetRunProtocol() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunProtocol(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRunProtocol(),
					"Getter and Setter Method Test failed for RunProtocol"
					);
		}
	}

	
}
