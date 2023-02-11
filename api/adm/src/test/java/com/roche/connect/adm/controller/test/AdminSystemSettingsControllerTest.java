package com.roche.connect.adm.controller.test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.web.multipart.MultipartFile;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.model.LabDetails;
import com.roche.connect.adm.model.SystemSettings;
import com.roche.connect.adm.rest.AdminSystemSettingsController;
import com.roche.connect.adm.writerepository.SystemSettingsWriteRepository;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, ThreadSessionManager.class }) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" })

public class AdminSystemSettingsControllerTest {
	@InjectMocks
	AdminSystemSettingsController adminSystemSettingsController;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	SystemSettingsWriteRepository systemSettingsWriteRepository;
	@Mock
	MultipartFile file;
	@Mock
	SystemSettings systemSettings;
	@Mock
	UserSession userSession;

	@BeforeTest
	public void setUp() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorUserName()).thenReturn("name");

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

	@Test
	public void imageUploadTest() throws HMTPException {
		Mockito.when(file.getOriginalFilename()).thenReturn("fileName.jpg");
		Mockito.when(file.getSize()).thenReturn(123L);
		Mockito.when(systemSettingsWriteRepository.save(Mockito.any(SystemSettings.class))).thenReturn(systemSettings);
		adminSystemSettingsController.imageUpload(file);
	}

	@Test
	public void getLabDetailsFullTest() {
		LabDetails labDetails = getLabDetails("AAAA");
		adminSystemSettingsController.getLabDetails(labDetails);

	}

	@Test
	public void getLabDetailsAddr1EmptyTest() {
		LabDetails labDetails = getLabDetails("");
		adminSystemSettingsController.getLabDetails(labDetails);

	}

	public LabDetails getLabDetails(String labAddress1) {
		LabDetails labDetails = new LabDetails();
		labDetails.setLabName("Roche");
		labDetails.setLabLogo("logo");
		labDetails.setLabAddress1(labAddress1);
		labDetails.setLabAddress2("BBBBB");
		labDetails.setLabAddress3("CCCCC");
		labDetails.setPhoneNumber("12345");
		return labDetails;
	}

}
