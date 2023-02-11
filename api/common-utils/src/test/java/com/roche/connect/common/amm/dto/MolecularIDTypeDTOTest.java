package com.roche.connect.common.amm.dto;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MolecularIDTypeDTOTest {

	private com.roche.connect.common.amm.dto.MolecularIDTypeDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.amm.dto.MolecularIDTypeDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetMolecularId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMolecularId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getMolecularId(),
					"Getter and Setter Method Test failed for MolecularId");
		}
	}

	@Test
	public void testGetSetPlateType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPlateType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPlateType(),
					"Getter and Setter Method Test failed for PlateType");
		}
	}

	@Test
	public void testGetSetPlateLocation() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setPlateLocation(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getPlateLocation(),
					"Getter and Setter Method Test failed for PlateLocation");
		}
	}

	@Test
	public void testGetSetAssayType() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAssayType(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAssayType(),
					"Getter and Setter Method Test failed for AssayType");
		}
	}

}
