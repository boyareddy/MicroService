package com.roche.connect.imm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MP24AsyncMessageService {

	@Autowired
	WFMIntegrationService wfmIntegrationService;

	@Async
	public void updateRunResultByDeviceId(String deviceId,String deviceType, String token) {
		wfmIntegrationService.updateRunResultStatusByDeviceId(deviceId,deviceType, token);
	}
}
