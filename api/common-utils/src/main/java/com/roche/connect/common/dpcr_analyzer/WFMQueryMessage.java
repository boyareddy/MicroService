package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;

public class WFMQueryMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String messageType;
	private String deviceId;
	private String accessioningId;
	private String containerId;
	private String containerPosition;
	private Long runResultsId;
	private String processStepName;

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	public String getContainerPosition() {
		return containerPosition;
	}

	public void setContainerPosition(String containerPosition) {
		this.containerPosition = containerPosition;
	}

	public Long getRunResultsId() {
		return runResultsId;
	}

	public void setRunResultsId(Long runResultsId) {
		this.runResultsId = runResultsId;
	}

	public String getProcessStepName() {
		return processStepName;
	}

	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
	}

}
