package com.roche.connect.adm.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceSummaryDTO {

	private String assayType;
	private String deviceType;
	private String deviceId;
	private String processStepName;
	private String updatedDateTime;
	private String deviceRunId;
	private String runStatus;
	
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String assayType) {
		this.deviceType = assayType;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getProcessStepName() {
		return processStepName;
	}
	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
	}
	public String getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(String updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	public String getDeviceRunId() {
		return deviceRunId;
	}
	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}
	public String getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	
	@Override
	public String toString() {
		return "DeviceSummaryDTO [assayType=" + assayType + ", deviceType=" + deviceType + ", deviceId=" + deviceId + ", processStepName=" 
	+ processStepName + ", updatedDateTime=" + updatedDateTime + ", deviceRunId=" + deviceRunId + ", runStatus=" + runStatus +"]";
	}

	
}
