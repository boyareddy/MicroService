package com.roche.connect.common.lp24;

public class Response {
	
	private String deviceSerialNumber;
	
	private String sendingApplicationName;
	
	private String receivingApplication;
	
	private String dateTimeMessageGenerated;
	
	private String messageType;
	
	private String rspMessage;
	
	private String statusUpdate;

	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}

	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}

	public String getSendingApplicationName() {
		return sendingApplicationName;
	}

	public void setSendingApplicationName(String sendingApplicationName) {
		this.sendingApplicationName = sendingApplicationName;
	}

	public String getReceivingApplication() {
		return receivingApplication;
	}

	public void setReceivingApplication(String receivingApplication) {
		this.receivingApplication = receivingApplication;
	}

	public String getDateTimeMessageGenerated() {
		return dateTimeMessageGenerated;
	}

	public void setDateTimeMessageGenerated(String dateTimeMessageGenerated) {
		this.dateTimeMessageGenerated = dateTimeMessageGenerated;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getRspMessage() {
		return rspMessage;
	}

	public void setRspMessage(String rspMessage) {
		this.rspMessage = rspMessage;
	}

	public String getStatusUpdate() {
		return statusUpdate;
	}

	public void setStatusUpdate(String statusUpdate) {
		this.statusUpdate = statusUpdate;
	}
	
	

}
