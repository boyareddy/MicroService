package com.roche.connect.common.lp24;

import java.io.Serializable;

public class QueryMessage implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String deviceSerialNumber;

	private String sendingApplicationName;
	
	private String processStepName;

	private String sendingFacility;

	private String receivingApplication;

	private String receivingFacility;

	private String dateTimeMessageGenerated;

	private String processingId;

	private String versionId;

	private String messageControlId;

	private String accessioningId;

	private Long runResultsId;

	private Long orderId;

	private String characterSet;

	private String messageQueryName;

	private String queryDefId;

	private String queryDefDesc;

	private String queryDefEncodingSystem;

	private EnumMessageType messageType;

	private String containerId;

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

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public EnumMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(EnumMessageType messageType) {
		this.messageType = messageType;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public Long getRunResultsId() {
		return runResultsId;
	}

	public void setRunResultsId(Long runResultsId) {
		this.runResultsId = runResultsId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getSendingFacility() {
		return sendingFacility;
	}

	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	public String getReceivingFacility() {
		return receivingFacility;
	}

	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
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

	public String getMessageControlId() {
		return messageControlId;
	}

	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}

	public String getCharacterSet() {
		return characterSet;
	}

	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	public String getMessageQueryName() {
		return messageQueryName;
	}

	public void setMessageQueryName(String messageQueryName) {
		this.messageQueryName = messageQueryName;
	}

	public String getQueryDefId() {
		return queryDefId;
	}

	public void setQueryDefId(String queryDefId) {
		this.queryDefId = queryDefId;
	}

	public String getQueryDefDesc() {
		return queryDefDesc;
	}

	public void setQueryDefDesc(String queryDefDesc) {
		this.queryDefDesc = queryDefDesc;
	}

	public String getQueryDefEncodingSystem() {
		return queryDefEncodingSystem;
	}

	public void setQueryDefEncodingSystem(String queryDefEncodingSystem) {
		this.queryDefEncodingSystem = queryDefEncodingSystem;
	}

    public String getProcessStepName() {
        return processStepName;
    }

    public void setProcessStepName(String processStepName) {
        this.processStepName = processStepName;
    }

}
