package com.roche.connect.common.mp96;

import java.util.List;

public class OULRunResultMessage {

	private String runId;
	private String deviceRunId;
	private String runComments;
	private String deviceId;
	private String runResultStatus;
	private String messageType;
	private String sendingApplicationName;

	private List<OULSampleResultMessage> oulSampleResultMessage;

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

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

	public String getRunResultStatus() {
		return runResultStatus;
	}

	public void setRunResultStatus(String runResultStatus) {
		this.runResultStatus = runResultStatus;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public List<OULSampleResultMessage> getOulSampleResultMessage() {
		return oulSampleResultMessage;
	}

	public void setOulSampleResultMessage(List<OULSampleResultMessage> oulSampleResultMessage) {
		this.oulSampleResultMessage = oulSampleResultMessage;
	}

	public String getSendingApplicationName() {
		return sendingApplicationName;
	}

	public void setSendingApplicationName(String sendingApplicationName) {
		this.sendingApplicationName = sendingApplicationName;
	}

	public String getRunComments() {
		return runComments;
	}

	public void setRunComments(String runComments) {
		this.runComments = runComments;
	}

	@Override
	public String toString() {
		return "OULRunResultMessage [runId=" + runId + ", deviceRunId=" + deviceRunId + ", runComments=" + runComments
				+ ", deviceId=" + deviceId + ", runResultStatus=" + runResultStatus + ", messageType=" + messageType
				+ ", sendingApplicationName=" + sendingApplicationName + ", oulSampleResultMessage="
				+ oulSampleResultMessage + "]";
	}


}
