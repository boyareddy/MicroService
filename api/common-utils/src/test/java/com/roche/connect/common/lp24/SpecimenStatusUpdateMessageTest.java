package com.roche.connect.common.lp24;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SpecimenStatusUpdateMessageTest {

	private com.roche.connect.common.lp24.SpecimenStatusUpdateMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.lp24.SpecimenStatusUpdateMessage();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	@Test
	public void testGetSetDeviceSerialNumber() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceSerialNumber(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDeviceSerialNumber(),
					"Getter and Setter Method Test failed for DeviceSerialNumber");
		}
	}

	@Test
	public void testGetSetSendingApplicationName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSendingApplicationName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getSendingApplicationName(),
					"Getter and Setter Method Test failed for SendingApplicationName");
		}
	}

	@Test
	public void testGetSetProcessStepName() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProcessStepName(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getProcessStepName(),
					"Getter and Setter Method Test failed for ProcessStepName");
		}
	}

	@Test
	public void testGetSetReceivingApplication() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReceivingApplication(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getReceivingApplication(),
					"Getter and Setter Method Test failed for ReceivingApplication");
		}
	}

	@Test
	public void testGetSetDateTimeMessageGenerated() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDateTimeMessageGenerated(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getDateTimeMessageGenerated(),
					"Getter and Setter Method Test failed for DateTimeMessageGenerated");
		}
	}

	@Test
	public void testGetSetMessageControlId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageControlId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getMessageControlId(),
					"Getter and Setter Method Test failed for MessageControlId");
		}
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
	public void testGetSetAccessioningId() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setAccessioningId(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getAccessioningId(),
					"Getter and Setter Method Test failed for AccessioningId");
		}
	}

	@Test
	public void testGetSetRunResultsId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setRunResultsId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getRunResultsId()),
					"Getter and Setter Method Test failed for RunResultsId");
		}
	}

	@Test
	public void testGetSetOrderId() {
		long[] testStringArray = { -100, 0, 100 };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOrderId(testStringArray[i]);
			Assert.assertEquals(new Long(testStringArray[i]), new Long(classUnderTest.getOrderId()),
					"Getter and Setter Method Test failed for OrderId");
		}
	}

	@Test
	public void testGetSetResponseMessage() {
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setResponseMessage(testStringArray[i]);
			Assert.assertEquals(testStringArray[i], classUnderTest.getResponseMessage(),
					"Getter and Setter Method Test failed for ResponseMessage");
		}
	}

}
