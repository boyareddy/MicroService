package com.roche.connect.imm.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeviceRunService {

	@Value("${connect.runID}")
	private String runIDPrefix;

	public String generateDeviceRunId() {
		Calendar cal = Calendar.getInstance();
		String runIdTemp = String.valueOf(cal.get(Calendar.YEAR)).substring(2, 4) + cal.get(Calendar.DAY_OF_MONTH)
				+ String.valueOf(System.currentTimeMillis()).substring(5, 12);
		return runIDPrefix + runIdTemp;
	}
}
