package com.camel.dto;

public class HL7HeaderSegmentDTO {
	
	String deviceSerialNumber;
	String sendingApplicationName;
	String sendingFacility;
	String receivingApplication;
	String receivingFacility;
	String dateTimeMessageGenerated;
	String messageType;
	String messageControlId;
	String processingId;
	String versionId;
	String  characterSet;
	String  sampleID;
	String  messageQueryName;
	String queryDef_Id;
	String queryDef_desc;
	String queryDef_EncodingSystem;
	
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
	public String getProcessingId() {
		return processingId;
	}
	public void setProcessingId(String processingId) {
		this.processingId = processingId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getCharacterSet() {
		return characterSet;
	}
	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}
	public String getSampleID() {
		return sampleID;
	}
	public void setSampleID(String sampleID) {
		this.sampleID = sampleID;
	}
	public String getMessageQueryName() {
		return messageQueryName;
	}
	public void setMessageQueryName(String messageQueryName) {
		this.messageQueryName = messageQueryName;
	}
	public String getQueryDef_Id() {
		return queryDef_Id;
	}
	public void setQueryDef_Id(String queryDef_Id) {
		this.queryDef_Id = queryDef_Id;
	}
	public String getQueryDef_desc() {
		return queryDef_desc;
	}
	public void setQueryDef_desc(String queryDef_desc) {
		this.queryDef_desc = queryDef_desc;
	}
	public String getQueryDef_EncodingSystem() {
		return queryDef_EncodingSystem;
	}
	public void setQueryDef_EncodingSystem(String queryDef_EncodingSystem) {
		this.queryDef_EncodingSystem = queryDef_EncodingSystem;
	}
	
	
}
