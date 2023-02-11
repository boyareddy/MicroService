package com.roche.connect.common.mp96;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class WFMoULMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private String deviceSerialNumber;
	private String sendingApplicationName;
	private String dateTimeMessageGenerated;
	private String messageType;
	private String accessioningId;
	private Long runResultsId;
	private String runResultStatus;

	private OULSampleResultMessage oulSampleResultMessage;

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

	public Long getRunResultsId() {
		return runResultsId;
	}

	public void setRunResultsId(Long runResultsId) {
		this.runResultsId = runResultsId;
	}

	public String getRunResultStatus() {
		return runResultStatus;
	}

	public void setRunResultStatus(String runResultStatus) {
		this.runResultStatus = runResultStatus;
	}

	public OULSampleResultMessage getOulSampleResultMessage() {
		return oulSampleResultMessage;
	}

	public void setOulSampleResultMessage(OULSampleResultMessage oulSampleResultMessage) {
		this.oulSampleResultMessage = oulSampleResultMessage;
	}

	@Override
	public String toString() {
		return "WFMoULMessage [sendingApplicationName=" + sendingApplicationName + ", dateTimeMessageGenerated="
				+ dateTimeMessageGenerated + ", messageType=" + messageType + ", accessioningId=" + accessioningId
				+ ", runResultsId=" + runResultsId + ", runResultStatus=" + runResultStatus
				+ ", oulSampleResultMessage=" + oulSampleResultMessage + "]";
	}

}
