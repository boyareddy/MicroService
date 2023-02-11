package com.camel.dto;

public class MessagePayload {
	private String deviceSerialNumber;
	private String sendingApplicationName;
	private String receivingApplication;
	private String dateTimeMessageGenerated;
	private String messageType;
	private String messageControlId;
	private String accessioningId;

	private RSPMessageDTO rspMessage;

	private StatusUpdate statusUpdate;

	public RSPMessageDTO getRspMessage() {
		return rspMessage;
	}

	public void setRspMessage(RSPMessageDTO rspMessage) {
		this.rspMessage = rspMessage;
	}

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

	public String getMessageControlId() {
		return messageControlId;
	}

	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public StatusUpdate getStatusUpdate() {
		return statusUpdate;
	}

	public void setStatusUpdate(StatusUpdate statusUpdate) {
		this.statusUpdate = statusUpdate;
	}

}
