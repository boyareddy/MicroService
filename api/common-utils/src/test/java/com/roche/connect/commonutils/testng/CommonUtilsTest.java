package com.roche.connect.commonutils.testng;

import static org.testng.Assert.assertEquals;

import org.junit.Test;
import org.testng.annotations.BeforeTest;

import com.roche.connect.common.enums.*;;


public class CommonUtilsTest {
	

	@BeforeTest
	public void setUp() {

	}

	@Test
	public void variablesTest() {

		assertEquals(AssayType.NIPT.getText(), "NIPTHTP");
		
	}

}
