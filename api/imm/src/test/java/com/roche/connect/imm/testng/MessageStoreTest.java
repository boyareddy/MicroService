package com.roche.connect.imm.testng;

import static org.testng.Assert.assertEquals;

import java.util.Date;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.imm.model.CustomException;
import com.roche.connect.imm.model.MessageStore;

public class MessageStoreTest {
	@InjectMocks
	MessageStore messageStore;
	CustomException customEx;

	@BeforeTest
	public void setUp() {
		messageStore = new MessageStore();
		customEx=new CustomException();
		initialize();
	}

	private void initialize() {
		messageStore.setActiveFlag("yes");
		messageStore.setDeviceID("56789");
		messageStore.setId(5682L);
		messageStore.setMessage("success");
		messageStore.setMessageType("QueryMessage");
		messageStore.setModifiedDate(new Date());
		
		customEx.setErrorCode(500);
		customEx.setErrorMessage("Somethingwentwrong");
	}

	@Test
	public void messageStoreTest() {
		assertEquals("yes", messageStore.getActiveFlag());
		assertEquals("56789", messageStore.getDeviceID());
		assertEquals(5682L, messageStore.getId());
		assertEquals("success", messageStore.getMessage());
		assertEquals("QueryMessage", messageStore.getMessageType());
		assertEquals(500, customEx.getErrorCode());
		assertEquals("Somethingwentwrong", customEx.getErrorMessage());
		Date date = new Date();
		messageStore.setModifiedDate(date);
		assertEquals(date, messageStore.getModifiedDate());
	}

}
