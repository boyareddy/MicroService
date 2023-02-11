package com.roche.connect.rmm.jasper.dto;

public class FlagsDTO {
	private String flagId;
	private String severity;
	private String description;
	public String getFlagId() {
		return flagId;
	}
	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
