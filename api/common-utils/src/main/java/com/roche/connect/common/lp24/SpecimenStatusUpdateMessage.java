package com.roche.connect.common.lp24;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SpecimenStatusUpdateMessage implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
    
    private String deviceSerialNumber;
    
    private String sendingApplicationName;
    
    private String processStepName;
    
    private String receivingApplication;
    
    private String dateTimeMessageGenerated;
    
    private String messageControlId;
    
    @JsonSerialize
    private EnumMessageType messageType;
    
    private String containerId;
    
    private StatusUpdate statusUpdate;
    
    private String accessioningId;
    
    private Long runResultsId;
    
    private Long orderId;
    
    private String responseMessage;
    
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
    
    public StatusUpdate getStatusUpdate() {
        return statusUpdate;
    }
    
    public void setStatusUpdate(StatusUpdate statusUpdate) {
        this.statusUpdate = statusUpdate;
    }
    
    public String getResponseMessage() {
        return responseMessage;
    }
    
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
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

	public String getMessageControlId() {
		return messageControlId;
	}

	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}

    public String getProcessStepName() {
        return processStepName;
    }

    public void setProcessStepName(String processStepName) {
        this.processStepName = processStepName;
    }
    
}
