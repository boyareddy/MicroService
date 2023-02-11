package com.roche.nipt.dpcr.dto;

public class MessagePayloadDTO {
	
	private String deviceName;
	private String deviceSerialNumber;
	private String messageType;
	private String dateAndTime;
	private String runId;
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getDateAndTime() {
		return dateAndTime;
	}
	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	public String getRunId() {
		return runId;
	}
	public void setRunId(String runId) {
		this.runId = runId;
	}
	@Override
	public String toString() {
		return "MessagePayloadDTO [deviceName=" + deviceName + ", deviceSerialNumber=" + deviceSerialNumber
				+ ", messageType=" + messageType + ", dateAndTime=" + dateAndTime + ", runId=" + runId + "]";
	}
}
