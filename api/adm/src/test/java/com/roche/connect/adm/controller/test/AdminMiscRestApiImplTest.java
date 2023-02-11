package com.roche.connect.adm.controller.test;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.CharEncoding;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.dto.UserDetailsDTO;
import com.roche.connect.adm.dto.UserEntityDTO;
import com.roche.connect.adm.model.BackupHistory;
import com.roche.connect.adm.model.SystemSettings;
import com.roche.connect.adm.repository.BackupHistoryReadRepository;
import com.roche.connect.adm.repository.SystemSettingsReadRepository;
import com.roche.connect.adm.rest.AdminMiscRestApiImpl;
import com.roche.connect.adm.service.BackupAndRestoreService;
import com.roche.connect.adm.util.ADMConstants;
import com.roche.connect.adm.util.BackupConstants;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, ThreadSessionManager.class ,Files.class}) 
@PowerMockIgnore({"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
	"org.apache.logging.*", "org.slf4j.*" })
public class AdminMiscRestApiImplTest {

	@InjectMocks
	AdminMiscRestApiImpl adminMiscRestApiImpl;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	UserSession userSession;
	@Mock
	Builder builder;
	@Mock
	Response response;
	@Mock
	Builder resetPwdBuilder;
	@Mock
	Response resetPswResponse;
	@Mock
	BackupAndRestoreService backupAndRestoreService;
	@Mock
	BackupHistoryReadRepository backupHistoryReadRepository;
	@Mock
	SystemSettingsReadRepository systemSettingsReadRepository;

	@BeforeTest
	public void setUp() throws UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		ReflectionTestUtils.setField(adminMiscRestApiImpl, "adminAPI", "http://localhost");
		ReflectionTestUtils.setField(adminMiscRestApiImpl, "requiredSpace", 1000000000);
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
	public void addDeviceUserResponse500Test() throws HMTPException, UnsupportedEncodingException {
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when((String) ThreadSessionManager.currentUserSession()
				.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString())).thenReturn("token");
		String url = "http://localhost/json/users?ownerId=1&roles=Device";
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null)).thenReturn(builder);
		Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any(Entity.class))).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(201);
		String url1 = "http://localhost/json/users/user/admin/domain/null/password/cGFzMTIz";
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url1, CharEncoding.UTF_8), null))
				.thenReturn(resetPwdBuilder);
		Mockito.when(resetPwdBuilder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(resetPwdBuilder);
		Mockito.when(resetPwdBuilder.post(Mockito.any(Entity.class))).thenReturn(resetPswResponse);
		Mockito.when(resetPswResponse.getStatus()).thenReturn(500);
		adminMiscRestApiImpl.addDeviceUser(getUserDetailsDTO());
	}

	@Test
	public void addDeviceUserResponse201Test() throws HMTPException, UnsupportedEncodingException {
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when((String) ThreadSessionManager.currentUserSession()
				.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString())).thenReturn("token");
		String url = "http://localhost/json/users?ownerId=1&roles=Device";
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null)).thenReturn(builder);
		Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any(Entity.class))).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(201);
		adminMiscRestApiImpl.addDeviceUser(getUserDetailsDTO());
	}

	@Test
	public void addDeviceUserResponseTokenNullTest() throws HMTPException, UnsupportedEncodingException {
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString())).thenReturn(null);
		String url = "http://localhost/json/users?ownerId=1&roles=Device";
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null)).thenReturn(builder);
		Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any(Entity.class))).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(201);
		adminMiscRestApiImpl.addDeviceUser(getUserDetailsDTO());
	}
	
	@Test
	public void addDeviceUserNullTest() {
		try {
			adminMiscRestApiImpl.addDeviceUser(null);
		} catch (Exception e) {
			System.out.println("Caught Exception");
		}
	}

	@Test
	public void validateOutputFilePathTest() throws HMTPException {
		adminMiscRestApiImpl.validateOutputFilePath("C:\\Users","Illumina Sequencer");
	}

	@Test
	public void validateOutputFilePathNegativeTest() throws HMTPException {
		Path path = Paths.get("C:\\Users\\ITDtpAdmin");
		PowerMockito.mockStatic(Files.class);
		Mockito.when(Files.isReadable(path)).thenReturn(false);
		adminMiscRestApiImpl.validateOutputFilePath(path.toString(),"Illumina Sequencer");
	}

	@Test
	public void validateOutputFilePathNullTest() throws HMTPException {
		adminMiscRestApiImpl.validateOutputFilePath("","Illumina Sequencer");
	}

	@Test
	public void validateOutputFilePathDoesNotExistTest() throws HMTPException {
		adminMiscRestApiImpl.validateOutputFilePath("C:\\sarath","Illumina Sequencer");
	}
	
	@Test
	public void doManualBackupFullTest() {
		try {
			Map<String, String> request = new HashMap<String, String>();
			List<BackupHistory> backupHistory = new ArrayList<>();
			request.put(BackupConstants.DESTINATIONPATH, "src/test/java/com/roche/connect/adm/test/resource/backup.properties");
			PowerMockito.mockStatic(ThreadSessionManager.class);
			Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
			Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
			Mockito.when(backupHistoryReadRepository.findByStatus(BackupConstants.INPROGRESS)).thenReturn(backupHistory);
			adminMiscRestApiImpl.doManualBackup(request);
		} catch (HMTPException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void doManualBackupHistoryNotEmptyTest(){
		try {
			Map<String, String> request = new HashMap<String, String>();
			List<BackupHistory> backupHistory = new ArrayList<>();
			BackupHistory backupHist = new BackupHistory();
			backupHistory.add(backupHist);
			request.put(BackupConstants.DESTINATIONPATH, "sample");
			PowerMockito.mockStatic(ThreadSessionManager.class);
			Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
			Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
			Mockito.when(backupHistoryReadRepository.findByStatus(BackupConstants.INPROGRESS)).thenReturn(backupHistory);
			adminMiscRestApiImpl.doManualBackup(request);
		} catch (HMTPException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void doManualBackupDestFoldrIsNotExistTest() {
		try {
			Map<String, String> request = new HashMap<String, String>();
			List<BackupHistory> backupHistory = new ArrayList<>();
			request.put(BackupConstants.DESTINATIONPATH, "sample");
			PowerMockito.mockStatic(ThreadSessionManager.class);
			Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
			Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
			Mockito.when(backupHistoryReadRepository.findByStatus(BackupConstants.INPROGRESS)).thenReturn(backupHistory);
			adminMiscRestApiImpl.doManualBackup(request);
		} catch (HMTPException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void doScheduledBackupTest() {
		try {
			Map<String, String> request = new HashMap<String, String>();
			request.put("backup_interval", "sample");
			request.put("backup_location","\\adm\\src\\test\\java\\com");
			Mockito.doNothing().when(backupAndRestoreService).saveSchedule(request);
			adminMiscRestApiImpl.doScheduledBackup(request);
		} catch (HMTPException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getBackupSystemSettingsListEmptyTest() {
		List<SystemSettings> systemSettingsList = new ArrayList<>();
		Mockito.when(systemSettingsReadRepository.findByAttributeType(BackupConstants.BACKUP)).thenReturn(systemSettingsList);
		try {
			adminMiscRestApiImpl.getBackup();
		} catch (HMTPException e) {
			System.out.println("getBackupSystemSettingsListEmptyTest Exception Caught");
		}
	}
	
	/*@Test
	public void getBackupSystemSettingsListNotEmptyTest() {
		List<SystemSettings> systemSettingsList = new ArrayList<>();
		SystemSettings systemSettings = new SystemSettings();
		systemSettings.setAttributeValue("2019/06/20");
		systemSettingsList.add(systemSettings);
		Mockito.when(systemSettingsReadRepository.findByAttributeType(BackupConstants.BACKUP)).thenReturn(systemSettingsList);
		try {
			adminMiscRestApiImpl.getBackup();
		} catch (HMTPException e) {
			e.printStackTrace();
		}
	}*/
	
	@Test
	public void getBackupSystemSettingsNullTest() {
		List<SystemSettings> systemSettingsList = new ArrayList<>();
		SystemSettings systemSettings = null;
		systemSettingsList.add(systemSettings);
		Mockito.when(systemSettingsReadRepository.findByAttributeType(BackupConstants.BACKUP)).thenReturn(systemSettingsList);
		try {
			adminMiscRestApiImpl.getBackup();
		} catch (HMTPException e) {
			e.printStackTrace();
		}
	}

	public UserEntityDTO getUserEntityDTO() {
		UserEntityDTO userEntityDTO = new UserEntityDTO();
		userEntityDTO.setUserDetails(getUserDetailsDTO());
		userEntityDTO.setPassword("pas123");
		return userEntityDTO;

	}

	public UserDetailsDTO getUserDetailsDTO() {
		UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
		userDetailsDTO.setEmail("admin@hcl.com");
		userDetailsDTO.setFirstName("admin");
		userDetailsDTO.setLastName("");
		userDetailsDTO.setLoginName("admin");
		return userDetailsDTO;
	}

}
