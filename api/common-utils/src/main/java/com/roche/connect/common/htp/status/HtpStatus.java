package com.roche.connect.common.htp.status;

public class HtpStatus {

	private String deviceRunId;
	private String deviceId;
	private String deviceName;
	private String sendingApplication;
	private String sendingFacility;
	private String receivingApplication;
	private String receivingFacility;

	private String runStatus;

	public String getDeviceRunId() {
		return deviceRunId;
	}

	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getSendingApplication() {
		return sendingApplication;
	}

	public void setSendingApplication(String sendingApplication) {
		this.sendingApplication = sendingApplication;
	}

	public String getSendingFacility() {
		return sendingFacility;
	}

	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	public String getReceivingApplication() {
		return receivingApplication;
	}

	public void setReceivingApplication(String receivingApplication) {
		this.receivingApplication = receivingApplication;
	}

	public String getReceivingFacility() {
		return receivingFacility;
	}

	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	@Override
	public String toString() {
		return "HtpStatus [deviceRunId=" + deviceRunId + ", deviceId=" + deviceId + ", deviceName=" + deviceName
				+ ", sendingApplication=" + sendingApplication + ", sendingFacility=" + sendingFacility
				+ ", receivingApplication=" + receivingApplication + ", receivingFacility=" + receivingFacility
				+ ", runStatus=" + runStatus + "]";
	}

	
}
