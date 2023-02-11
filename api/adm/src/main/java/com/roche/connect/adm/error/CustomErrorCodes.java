package com.roche.connect.adm.error;

import com.hcl.hmtp.common.client.exceptions.ErrorCodes;

public enum CustomErrorCodes implements ErrorCodes{
	
	NOTIFICATION_ADD_FAILED(500),
	NOTIFICATION_UPDATE_FAILED(500),
	NOTIFICATION_FETCH_FAILED(500),
	NOTIFICATION_TEMPLATE_NOT_FOUND(500),
	NOTIFICATION_NOT_AVAILABLE(500),
	NOTIFICATION_BUILD_FAILED(500),
	NOTIFICATION_GROUP_NOT_VALID(500),
	VERSION_FILE_NOT_AVAILABLE(404),
	SYSTEM_SETTINGS_NOT_AVAILABLE(404);
	
	private int value;
	
	private CustomErrorCodes(int value) {
		this.value = value;
	}

	@Override
	public int getErrorCode() {
		return this.value;
	}


}
