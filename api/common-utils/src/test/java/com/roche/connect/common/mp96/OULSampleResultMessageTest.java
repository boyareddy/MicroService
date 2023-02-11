package com.roche.connect.common.mp96;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class OULSampleResultMessageTest {
	
	private com.roche.connect.common.mp96.OULSampleResultMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.mp96.OULSampleResultMessage();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetTime() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTime(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getTime(),
					"Getter and Setter Method Test failed for Time"
					);
		}
	}
	@Test
	public void testGetSetOperator() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOperator(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getOperator(),
					"Getter and Setter Method Test failed for Operator"
					);
		}
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
	public void testGetSetOutputContainerId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputContainerId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getOutputContainerId(),
					"Getter and Setter Method Test failed for OutputContainerId"
					);
		}
	}
	@Test
	public void testGetSetReagentKitName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReagentKitName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getReagentKitName(),
					"Getter and Setter Method Test failed for ReagentKitName"
					);
		}
	}
	@Test
	public void testGetSetReagentVesion() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReagentVesion(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getReagentVesion(),
					"Getter and Setter Method Test failed for ReagentVesion"
					);
		}
	}
	@Test
	public void testGetSetRunStartTime() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunStartTime(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRunStartTime(),
					"Getter and Setter Method Test failed for RunStartTime"
					);
		}
	}
	@Test
	public void testGetSetRunEndTime() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunEndTime(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getRunEndTime(),
					"Getter and Setter Method Test failed for RunEndTime"
					);
		}
	}
	@Test
	public void testGetSetOutputPlateType() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOutputPlateType(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getOutputPlateType(),
					"Getter and Setter Method Test failed for OutputPlateType"
					);
		}
	}
	@Test
	public void testGetSetProtocal() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProtocal(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getProtocal(),
					"Getter and Setter Method Test failed for Protocal"
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
	public void testGetSetElevateVolume() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setElevateVolume(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getElevateVolume(),
					"Getter and Setter Method Test failed for ElevateVolume"
					);
		}
	}
	@Test
	public void testGetSetSoftwareVersion() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSoftwareVersion(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSoftwareVersion(),
					"Getter and Setter Method Test failed for SoftwareVersion"
					);
		}
	}
	@Test
	public void testGetSetSerialNo() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSerialNo(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSerialNo(),
					"Getter and Setter Method Test failed for SerialNo"
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
	public void testGetSetVolume() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setVolume(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getVolume(),
					"Getter and Setter Method Test failed for Volume"
					);
		}
	}
	@Test
	public void testGetSetBarcode() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setBarcode(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getBarcode(),
					"Getter and Setter Method Test failed for Barcode"
					);
		}
	}
	@Test
	public void testGetSetExpDate() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setExpDate(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getExpDate(),
					"Getter and Setter Method Test failed for ExpDate"
					);
		}
	}
	@Test
	public void testGetSetLotNo() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setLotNo(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getLotNo(),
					"Getter and Setter Method Test failed for LotNo"
					);
		}
	}
	@Test
	public void testGetSetSampleResult() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleResult(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSampleResult(),
					"Getter and Setter Method Test failed for SampleResult"
					);
		}
	}
	@Test
	public void testGetSetSampleComments() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSampleComments(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSampleComments(),
					"Getter and Setter Method Test failed for SampleComments"
					);
		}
	}
	@Test
	public void testGetSetFlag() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFlag(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getFlag(),
					"Getter and Setter Method Test failed for Flag"
					);
		}
	}
	
	
	
}
