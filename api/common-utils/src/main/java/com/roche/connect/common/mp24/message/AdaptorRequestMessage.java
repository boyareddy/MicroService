package com.roche.connect.common.mp24.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdaptorRequestMessage implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String deviceSerialNumber;
	private String sendingApplicationName;	
	private String receivingApplication;	
	private String dateTimeMessageGenerated;
	private String messageType;
	private String messageControlId;
	
	public String getMessageControlId() {
		return messageControlId;
	}

	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}

	private String accessioningId;
	private String containerId;

	private ResponseMessage rspMessage;
	private StatusUpdate statusUpdate;

	
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
	
	public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
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


	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public ResponseMessage getResponseMessage() {
		return rspMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.rspMessage = responseMessage;
	}

	public StatusUpdate getStatusUpdate() {
		return statusUpdate;
	}

	public void setStatusUpdate(StatusUpdate statusUpdate) {
		this.statusUpdate = statusUpdate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AdaptorRequestMessage [deviceSerialNumber=" + deviceSerialNumber + ", sendingApplicationName="
				+ sendingApplicationName + ", receivingApplication=" + receivingApplication
				+ ", dateTimeMessageGenerated=" + dateTimeMessageGenerated + ", messageType=" + messageType
				+ ", messageControlId=" + messageControlId + ", accessioningId=" + accessioningId + ", containerId="
				+ containerId + ", rspMessage=" + rspMessage + ", statusUpdate=" + statusUpdate + "]";
	}
}
