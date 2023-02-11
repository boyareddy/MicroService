package com.roche.connect.common.lp24;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ResponseMessage implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	
	private List<String> errors;
	
	private String deviceSerialNumber;
	
	private String sendingApplicationName;
	
	private String receivingApplication;
	
	private String dateTimeMessageGenerated;
	
	@JsonSerialize
	private EnumMessageType messageType;
	
	private String accessioningId;
	
	private String containerId;
	
	private String position;
	
	private String lp24Protocol;
	
	private String  sampleType;
	
	private String specimenDescription;
	private String queryResponseStatus;
	private String orderControl;
	private String messageControlId;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getErrors() {
		return errors;
	}

	public String getQueryResponseStatus() {
		return queryResponseStatus;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
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


	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	
	public String getSpecimenDescription() {
		return specimenDescription;
	}

	public void setSpecimenDescription(String specimenDescription) {
		this.specimenDescription = specimenDescription;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public EnumMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(EnumMessageType messageType) {
		this.messageType = messageType;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	private String protocolName;


	public void setQueryResponseStatus(String queryResponseStatus) {
		this.queryResponseStatus = queryResponseStatus;
	}

	public String getOrderControl() {
		return orderControl;
	}

	public void setOrderControl(String orderControl) {
		this.orderControl = orderControl;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getLp24Protocol() {
		return lp24Protocol;
	}

	public void setLp24Protocol(String lp24Protocol) {
		this.lp24Protocol = lp24Protocol;
	}

    public String getMessageControlId() {
        return messageControlId;
    }

    public void setMessageControlId(String messageControlId) {
        this.messageControlId = messageControlId;
    }

	
	

}
