package com.roche.connect.common.dpcr_analyzer;

public class WFMAcknowledgementMessage {

	private String deviceId;
	private String accessioningId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

}
