package com.roche.nipt.dpcr.util;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;

public class MP96SampleTypeEncodingTest {
	
	private static Logger log = LogManager.getLogger(AppUrlEntryTest.class);

	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@Test
	public void MP96SampleTypeEncoding() {
	}

	@Test
	public void findByKey() {
		
		String stand = MP96SampleTypeEncoding.findByKey(MP96SampleTypeEncoding.STANDARD);
		log.info(stand);
		String exp = MP96SampleTypeEncoding.findByKey(MP96SampleTypeEncoding.EXPRESS);
		log.info(exp);
		String duo = MP96SampleTypeEncoding.findByKey(MP96SampleTypeEncoding.DUO);
		log.info(duo);
		String flex = MP96SampleTypeEncoding.findByKey(MP96SampleTypeEncoding.FLEX);
		log.info(flex);
		String adv = MP96SampleTypeEncoding.findByKey(MP96SampleTypeEncoding.ADVANCED);
		log.info(adv);
	}

	@Test
	public void findByValue() {
		String value = "xyzj";
		MP96SampleTypeEncoding.findByValue(value);
	}

	@Test
	public void fromValue() {
		String value = "xyzj";
		MP96SampleTypeEncoding.fromValue(value);
	
	}

	@Test
	public void getEncodingValue() {
		String value = "xyzj";
		MP96SampleTypeEncoding.getEncodingValue(value);
	}
}
