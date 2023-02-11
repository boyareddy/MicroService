package com.roche.connect.imm.testng;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.imm.exception.InvalidDataException;

public class InvalidDataExceptionTest {

	InvalidDataException invalidDataException1 = null;

	@BeforeTest
	public void setUp() {
		invalidDataException1 = new InvalidDataException("Invalid PlateId");
	}

	@Test
	public void invalidDataException() {

		assertEquals("Invalid PlateId", invalidDataException1.getMessage());
	}

}
