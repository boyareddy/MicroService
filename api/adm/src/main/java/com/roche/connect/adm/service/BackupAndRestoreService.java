package com.roche.connect.adm.service;

import java.sql.Timestamp;
import java.util.Map;

import com.hcl.hmtp.common.client.exceptions.HMTPException;

public interface BackupAndRestoreService {
	
	public void saveSchedule(Map<String, String> request) throws HMTPException;
	void runScheduledBackup();
	Timestamp getNextSchedule(String scheduleType);
	void updateBackupStatus();
	void takeBackup(long domainId, String username, String destinationPath);
	String getFormatedTime(Timestamp time);
	

}
