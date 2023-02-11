package com.roche.nipt.dpcr.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AppUrlEntryTest {

	private static Logger log = LogManager.getLogger(AppUrlEntryTest.class);

	@BeforeMethod
	public void beforeMethod() {

	}

	@AfterMethod
	public void afterMethod() {
	}

	@Test
	public void AppUrlEntry() {
		String value = "http://localhost:9090/imm/json/rest/api/v1/dpcr/messages";
		AppUrlEntry first = AppUrlEntry.findByValue(value);
		log.info(first);
	}

	@Test
	public void findByKey() {
		String root = AppUrlEntry.findByKey(AppUrlEntry.ROOT_ELEMENT);
		log.info(root);
		String no = AppUrlEntry.findByKey(AppUrlEntry.NO_ENTRY);
		log.info(no);
		String send = AppUrlEntry.findByKey(AppUrlEntry.SEND_REQUEST);
		log.info(send);
	}

	/*
	 * @Test public void findByValue() { throw new
	 * RuntimeException("Test not implemented"); }
	 */

	@Test
	public void getValue() {
		String value = "abc";
		log.info(value);
	}
}
