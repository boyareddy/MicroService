package com.roche.connect.common.amm.dto;

public class FlagsDataDTO {
	private String flagCode;
	private String description;
	private String severity;
	private String assayType;
	private String deviceType;
	private String processStepName;
	private String source;
	private String sampleOrRunLevel;
	
	public String getFlagCode() {
		return flagCode;
	}
	public void setFlagCode(String flagCode) {
		this.flagCode = flagCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSampleOrRunLevel() {
		return sampleOrRunLevel;
	}
	public void setSampleOrRunLevel(String sampleOrRunLevel) {
		this.sampleOrRunLevel = sampleOrRunLevel;
	}
	public String getProcessStepName() {
		return processStepName;
	}
	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
	}
	
	

}
