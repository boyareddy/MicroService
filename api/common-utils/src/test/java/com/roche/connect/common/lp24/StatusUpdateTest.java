package com.roche.connect.common.lp24;

import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class StatusUpdateTest {

	private com.roche.connect.common.lp24.StatusUpdate classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.lp24.StatusUpdate();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetEventDate() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEventDate(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getEventDate(),
					"Getter and Setter Method Test failed for EventDate");
		}
	}

	@Test
	public void testGetSetEquipmentState() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEquipmentState(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getEquipmentState(),
					"Getter and Setter Method Test failed for EquipmentState");
		}
	}

	@Test
	public void testGetSetProtocolName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProtocolName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getProtocolName(),
					"Getter and Setter Method Test failed for ProtocolName");
		}
	}

	@Test
	public void testGetSetProtocolVersion() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProtocolVersion(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getProtocolVersion(),
					"Getter and Setter Method Test failed for ProtocolVersion");
		}
	}

	@Test
	public void testGetSetRunResult() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunResult(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getRunResult(),
					"Getter and Setter Method Test failed for RunResult");
		}
	}

	@Test
	public void testGetSetRunStartTime() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunStartTime(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getRunStartTime()));
		}
	}

	@Test
	public void testGetSetRunEndTime() {
		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		Date date = new Date();
		Timestamp timestamp2 = new Timestamp(date.getTime());
		Timestamp[] testStringArray = { timestamp1, timestamp2 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunEndTime(testStringArray[i]);
			Assert.assertTrue(testStringArray[i].equals(classUnderTest.getRunEndTime()));
		}
	}

	@Test
	public void testGetSetComment() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setComment(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getComment(),
					"Getter and Setter Method Test failed for Comment");
		}
	}

	@Test
	public void testGetSetOrderName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOrderName(),
					"Getter and Setter Method Test failed for OrderName");
		}
	}

	@Test
	public void testGetSetOrderResult() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderResult(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOrderResult(),
					"Getter and Setter Method Test failed for OrderResult");
		}
	}

	@Test
	public void testGetSetInternalControls() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setInternalControls(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getInternalControls(),
					"Getter and Setter Method Test failed for InternalControls");
		}
	}

	@Test
	public void testGetSetProcessingCartridge() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProcessingCartridge(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getProcessingCartridge(),
					"Getter and Setter Method Test failed for ProcessingCartridge");
		}
	}

	@Test
	public void testGetSetTipRack() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setTipRack(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getTipRack(),
					"Getter and Setter Method Test failed for TipRack");
		}
	}

	@Test
	public void testGetSetOperatorName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOperatorName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getOperatorName(),
					"Getter and Setter Method Test failed for OperatorName");
		}
	}

	@Test
	public void testGetSetUpdatedBy() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setUpdatedBy(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getUpdatedBy(),
					"Getter and Setter Method Test failed for UpdatedBy");
		}
	}

	@Test
	public void testGetSetFlag() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setFlag(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getFlag(),
					"Getter and Setter Method Test failed for Flag");
		}
	}

}
