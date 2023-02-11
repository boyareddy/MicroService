package com.roche.connect.common.lp24;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class QueryMessageTest {
	
	private com.roche.connect.common.lp24.QueryMessage classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.lp24.QueryMessage();
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
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDeviceSerialNumber(),
					"Getter and Setter Method Test failed for DeviceSerialNumber"
					);
		}
	}
	@Test
	public void testGetSetSendingApplicationName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSendingApplicationName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSendingApplicationName(),
					"Getter and Setter Method Test failed for SendingApplicationName"
					);
		}
	}
	@Test
	public void testGetSetProcessStepName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProcessStepName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getProcessStepName(),
					"Getter and Setter Method Test failed for ProcessStepName"
					);
		}
	}
	@Test
	public void testGetSetSendingFacility() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSendingFacility(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSendingFacility(),
					"Getter and Setter Method Test failed for SendingFacility"
					);
		}
	}
	@Test
	public void testGetSetReceivingApplication() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReceivingApplication(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getReceivingApplication(),
					"Getter and Setter Method Test failed for ReceivingApplication"
					);
		}
	}
	@Test
	public void testGetSetReceivingFacility() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setReceivingFacility(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getReceivingFacility(),
					"Getter and Setter Method Test failed for ReceivingFacility"
					);
		}
	}
	
	@Test
	public void testGetSetDateTimeMessageGenerated() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDateTimeMessageGenerated(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDateTimeMessageGenerated(),
					"Getter and Setter Method Test failed for DateTimeMessageGenerated"
					);
		}
	}
	@Test
	public void testGetSetProcessingId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setProcessingId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getProcessingId(),
					"Getter and Setter Method Test failed for ProcessingId"
					);
		}
	}
	@Test
	public void testGetSetVersionId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setVersionId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getVersionId(),
					"Getter and Setter Method Test failed for VersionId"
					);
		}
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
	public void testGetSetCharacterSet() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCharacterSet(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getCharacterSet(),
					"Getter and Setter Method Test failed for CharacterSet"
					);
		}
	}
	@Test
	public void testGetSetMessageQueryName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setMessageQueryName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getMessageQueryName(),
					"Getter and Setter Method Test failed for MessageQueryName"
					);
		}
	}
	@Test
	public void testGetSetQueryDefId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQueryDefId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getQueryDefId(),
					"Getter and Setter Method Test failed for QueryDefId"
					);
		}
	}
	@Test
	public void testGetSetQueryDefDesc() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQueryDefDesc(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getQueryDefDesc(),
					"Getter and Setter Method Test failed for QueryDefDesc"
					);
		}
	}
	@Test
	public void testGetSetQueryDefEncodingSystem() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setQueryDefEncodingSystem(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getQueryDefEncodingSystem(),
					"Getter and Setter Method Test failed for QueryDefEncodingSystem"
					);
		}
	}
	@Test
	public void testGetSetContainerId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setContainerId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getContainerId(),
					"Getter and Setter Method Test failed for ContainerId"
					);
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
	
	
}
