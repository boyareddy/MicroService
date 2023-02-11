package com.roche.nipt.dpcr.util;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;

public class RouterUrlConfigTest {
	private static final Logger log = LogManager.getLogger(RouterUrlConfigTest.class);
	
	private static RouterUrlConfig instance;

	private Map<String, String> urlMap;
	
	RouterUrlConfig routerUrlConfig = new RouterUrlConfig();
	
	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@Test
	public void fetchUrlConfig() {
	}

	@Test
	public void getFile() {
	}

	@Test
	public void getInstance() {
		routerUrlConfig.getInstance();
	}

	@Test
	public void getURLMap() {
		urlMap = routerUrlConfig.getURLMap();
		log.info(urlMap);
	}

	@Test
	public void getUrl() {
		routerUrlConfig.getUrl(AppUrlEntry.NO_ENTRY);
	}

	@Test
	public void setURLMap() {
		routerUrlConfig.setURLMap(urlMap);
	}
}
