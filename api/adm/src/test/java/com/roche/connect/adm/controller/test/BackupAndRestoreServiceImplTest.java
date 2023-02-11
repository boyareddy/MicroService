package com.roche.connect.adm.controller.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.model.BackupHistory;
import com.roche.connect.adm.model.SystemSettings;
import com.roche.connect.adm.repository.BackupHistoryReadRepository;
import com.roche.connect.adm.repository.SystemSettingsReadRepository;
import com.roche.connect.adm.service.BackupAndRestoreServiceImpl;
import com.roche.connect.adm.util.BackupConstants;
import com.roche.connect.adm.util.UtilityService;
import com.roche.connect.adm.writerepository.BackupHistoryWriteRepository;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, ThreadSessionManager.class ,UtilityService.class}) 
@PowerMockIgnore({"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" })
public class BackupAndRestoreServiceImplTest {

	@InjectMocks
	BackupAndRestoreServiceImpl backupAndRestoreServiceImpl;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	UserSession userSession;
	@Mock
	SystemSettingsReadRepository systemSettingsReadRepository;
	@Mock
	SystemSettings systemSettings;
	@Mock
	BackupHistoryWriteRepository backupHistoryWriteRepository;
	@Mock
	BackupHistoryReadRepository backupHistoryReadRepository;

	long domainId = 1L;

	@BeforeTest
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(domainId);
		List<SystemSettings> sysSettingsList = new ArrayList<>();
		sysSettingsList.add(getSystemSettings());
		Mockito.when(systemSettingsReadRepository.findByAttributeType(BackupConstants.BACKUP))
				.thenReturn(sysSettingsList);
		ReflectionTestUtils.setField(backupAndRestoreServiceImpl, "backupCron", "0 0 23 * * ?");
		ReflectionTestUtils.setField(backupAndRestoreServiceImpl, "dayOfWeek", 1);
		ReflectionTestUtils.setField(backupAndRestoreServiceImpl, "dayOfMonth", 1);
	}

	@BeforeTest
	public SystemSettings getSystemSettings() {
		SystemSettings systemSettings = new SystemSettings();
		systemSettings.setAttributeName("backup_interval");
		systemSettings.setAttributeType("Backup");
		systemSettings.setAttributeValue("Weekly");
		Company company = new Company();
		company.setId(domainId);
		systemSettings.setCompany(company);
		return systemSettings;
	}

	@BeforeTest
	public BackupHistory getBackupHistory() {
		BackupHistory backupHistory = new BackupHistory();
		backupHistory.setStatus(BackupConstants.INPROGRESS);
		return backupHistory;
	}

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@Test
	public void takeBackupTest() {
		try {
			backupAndRestoreServiceImpl.takeBackup(1, "SYSTEM", "C://auditLog//");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void saveScheduleTest() {
		try {
			Map<String,String> request = new HashMap<String, String>();
			request.put("backup_interval", "Weekly");
			request.put("backup_location","\\adm\\src\\test\\java\\com");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void runScheduledBackupTest() {
		try {
			backupAndRestoreServiceImpl.runScheduledBackup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateBackupStatusListEmptyTest() {
		List<BackupHistory> backupHistoryList = new ArrayList<>();
		Mockito.when(backupHistoryReadRepository.findByStatus("InProgress")).thenReturn(backupHistoryList);
		backupAndRestoreServiceImpl.updateBackupStatus();
	}

	@Test
	public void updateBackupStatusListValueTest() {
		List<BackupHistory> backupHistoryList = new ArrayList<>();
		BackupHistory backupHistory = new BackupHistory();
		backupHistoryList.add(backupHistory);
		Mockito.when(backupHistoryReadRepository.findByStatus("InProgress")).thenReturn(backupHistoryList);
		try {
			backupAndRestoreServiceImpl.updateBackupStatus();
		} catch (Exception e) {
			System.out.println("Exception Cought");
		}
	}

}
