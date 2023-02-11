package com.roche.connect.common.mp96;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdaptorACKMessage {

	private String deviceId;
	private String deviceRunId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceRunId() {
		return deviceRunId;
	}

	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}

	@Override
	public String toString() {
		return "AdaptorACKMessage [deviceId=" + deviceId + ", deviceRunId=" + deviceRunId + "]";
	}
	
	
}
