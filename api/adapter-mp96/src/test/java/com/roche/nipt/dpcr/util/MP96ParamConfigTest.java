package com.roche.nipt.dpcr.util;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;

public class MP96ParamConfigTest {
	
	private static final Logger logger = LogManager.getLogger(MP96ParamConfigTest.class);
	
	MP96ParamConfig mp96 = new MP96ParamConfig();
	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@Test
	public void fetchSampleTypeConfig() {
	}

	@Test
	public void getInstance() throws IOException {
		mp96.getInstance();
	}

	@Test
	public void getMp96ConfigDetails() {
		mp96.getMp96ConfigDetails();
		
	}

	@Test
	public void setMp24ConfigDetails() {
		mp96 = new MP96ParamConfig();
		mp96.setMp24ConfigDetails(mp96);
	}
}
