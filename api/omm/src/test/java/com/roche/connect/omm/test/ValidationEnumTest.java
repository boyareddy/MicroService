package com.roche.connect.omm.test;

import static org.testng.Assert.assertNotNull;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.omm.util.OMMConstant;
import com.roche.connect.omm.util.OMMConstant.Validation;

public class ValidationEnumTest {
	@Mock
	public Enum<OMMConstant.Validation> validation = OMMConstant.Validation.NIPT;

	@BeforeTest
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		// Mockito.when(fixture.customMethod()).thenReturn("customMethod()");

	}

	@Test
	public void getUpdOrdStatusPosTest() throws HMTPException {
		for (Validation status : OMMConstant.Validation.values()) {
			//System.out.println(status);
			assertNotNull(status);
			// assertThat(status, status);

		}
	}

	@AfterTest
	public void afterTest() {

	}
}
