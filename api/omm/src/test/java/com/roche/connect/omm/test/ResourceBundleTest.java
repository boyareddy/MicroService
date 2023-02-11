package com.roche.connect.omm.test;

import java.io.File;
import java.util.Locale;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.ResourceBundleUtil;

@PrepareForTest({ LocaleContextHolder.class, OrderServiceImpl.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class ResourceBundleTest {

	ResourceBundleUtil resourceBundleUtil = new ResourceBundleUtil();

	Locale currentLocale = null;

	@InjectMocks
	OrderServiceImpl orderServiceImpl;

	@Mock
	File file;

	@BeforeTest
	public void setUp() {
		currentLocale = Locale.UK;
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void getResourceBundleNTest() throws HMTPException {
		PowerMockito.mockStatic(LocaleContextHolder.class);
		Mockito.when(LocaleContextHolder.getLocale()).thenReturn(currentLocale);
		resourceBundleUtil.getResourceBundle();
	}

	/**
	 * We need a special {@link IObjectFactory}.
	 * 
	 * @return {@link PowerMockObjectFactory}.
	 */
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@AfterTest
	public void afterTest() {

	}
}
