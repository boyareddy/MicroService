package com.roche.connect.common.dmm;

import java.lang.String;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class DeviceDTOTest {
	
	private com.roche.connect.common.dmm.DeviceDTO classUnderTest;

	@BeforeTest
	public void setUp() {
		classUnderTest = new com.roche.connect.common.dmm.DeviceDTO();
	}

	@AfterTest
	public void tearDown() {
		classUnderTest = null;
	}

	
	@Test
	public void testGetSetName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getName(),
					"Getter and Setter Method Test failed for Name"
					);
		}
	}
	@Test
	public void testGetSetDescription() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDescription(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDescription(),
					"Getter and Setter Method Test failed for Description"
					);
		}
	}
	@Test
	public void testGetSetGatewayId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setGatewayId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getGatewayId(),
					"Getter and Setter Method Test failed for GatewayId"
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
	public void testGetSetIsRetired() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setIsRetired(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getIsRetired(),
					"Getter and Setter Method Test failed for IsRetired"
					);
		}
	}
	@Test
	public void testGetSetState() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setState(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getState(),
					"Getter and Setter Method Test failed for State"
					);
		}
	}
	@Test
	public void testGetSetSite() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setSite(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getSite(),
					"Getter and Setter Method Test failed for Site"
					);
		}
	}
	@Test
	public void testGetSetIpAddress() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setIpAddress(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getIpAddress(),
					"Getter and Setter Method Test failed for IpAddress"
					);
		}
	}

	@Test
	public void testGetSetEditedBy() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setEditedBy(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getEditedBy(),
					"Getter and Setter Method Test failed for EditedBy"
					);
		}
	}
	@Test
	public void testGetSetCreatedBy() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setCreatedBy(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getCreatedBy(),
					"Getter and Setter Method Test failed for CreatedBy"
					);
		}
	}
	@Test
	public void testGetSetDeviceId() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setDeviceId(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getDeviceId(),
					"Getter and Setter Method Test failed for DeviceId"
					);
		}
	}
	@Test
	public void testGetSetOwnerPropertyName() {
				
		String[] testStringArray = { null, new String(), null };
		for (int i = 0; i < testStringArray.length; i++) {
			classUnderTest.setOwnerPropertyName(testStringArray[i]);
			Assert.assertEquals(
					testStringArray[i], classUnderTest.getOwnerPropertyName(),
					"Getter and Setter Method Test failed for OwnerPropertyName"
					);
		}
	}
	
	
}
