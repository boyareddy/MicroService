package com.roche.connect.imm.testng;

import static org.testng.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Date;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.entity.Company;
import com.roche.connect.imm.model.MessageStore;


public class AllEntityIMMTest {
	MessageStore messageStore=null;
	Timestamp ts = null;
	
	Company company;

	@BeforeTest
	public void setUp() {
		
		company = new Company();
		messageStore =new MessageStore();
		ts = new Timestamp(new Date().getTime());
	}

	@Test
	public void testSetCompany() {
		Company company = new Company();
		messageStore.setId(1L);
		messageStore.setModifiedDate(ts);
		company.setDomainName("hcl.com");
		company.setParentCompany(new Company());
		messageStore.setDeviceID("MP96");
		messageStore.setMessage("ABC");
		messageStore.setMessageType("ABC");
		messageStore.setActiveFlag("Y");
		messageStore.setEditedBy("ABC");
		messageStore.setCreatedDate(ts);
		messageStore.setModifiedDate(ts);
		messageStore.setCreatedBy("ABC");
		
	
		
		
		assertEquals(messageStore.getId(),1L);
		assertEquals(messageStore.getModifiedDate(),messageStore.getModifiedDate());
		assertEquals(company.getDomainName(),company.getDomainName());
		assertEquals(company.getOwnerPropertyName(),company.getOwnerPropertyName());
		assertEquals(company.getParentCompany(),company.getParentCompany());
		assertEquals(messageStore.getDeviceID(),messageStore.getDeviceID());
		assertEquals(messageStore.getMessage(),messageStore.getMessage());
		assertEquals(messageStore.getMessageType(),messageStore.getMessageType());
		assertEquals(messageStore.getActiveFlag(),messageStore.getActiveFlag());
		assertEquals(messageStore.getEditedBy(),messageStore.getEditedBy());
		assertEquals(messageStore.getCreatedDate(),messageStore.getCreatedDate());
		assertEquals(messageStore.getModifiedDate(),messageStore.getModifiedDate());
		assertEquals(messageStore.getCreatedBy(),messageStore.getCreatedBy());
	}
		

	@AfterTest
	public void afterTest() {

	}
}