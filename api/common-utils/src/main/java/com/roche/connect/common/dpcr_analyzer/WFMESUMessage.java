package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WFMESUMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String messageType;
	private String deviceId;
	private String accessioningId;
	private String inputContainerId;
	private String status;
	private String processStepName;
	private String flag;
	private String runResultId;
	private String dateTimeMessageGenerated;

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

	public String getInputContainerId() {
		return inputContainerId;
	}

	public void setInputContainerId(String inputContainerId) {
		this.inputContainerId = inputContainerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcessStepName() {
		return processStepName;
	}

	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
	}

	public String getRunResultId() {
		return runResultId;
	}

	public void setRunResultId(String runResultId) {
		this.runResultId = runResultId;
	}

	public String getDateTimeMessageGenerated() {
		return dateTimeMessageGenerated;
	}

	public void setDateTimeMessageGenerated(String dateTimeMessageGenerated) {
		this.dateTimeMessageGenerated = dateTimeMessageGenerated;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
