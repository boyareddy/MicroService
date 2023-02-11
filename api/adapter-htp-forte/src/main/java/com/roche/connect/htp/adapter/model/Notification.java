package com.roche.connect.htp.adapter.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author daram.m
 *
 */
public class Notification {
	@JsonProperty(required = true)
	private String severity;
	private Timestamp datetime;
	private String code;
	private String description;
	private String englishDescription;

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public Timestamp getDatetime() {
		return datetime;
	}

	public void setDatetime(Timestamp timestamp) {
		this.datetime = timestamp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnglishDescription() {
		return englishDescription;
	}

	public void setEnglishDescription(String englishDescription) {
		this.englishDescription = englishDescription;
	}
	
}
