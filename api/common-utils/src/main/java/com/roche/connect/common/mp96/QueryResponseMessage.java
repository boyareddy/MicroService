package com.roche.connect.common.mp96;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResponseMessage {

	private String messageType;
	private String runId;
	private String dateTime;
	private String createdBy;
	private String deviceID;
	private List<QueryResponseSample> samples;

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public List<QueryResponseSample> getSamples() {
		return samples;
	}

	public void setSamples(List<QueryResponseSample> samples) {
		this.samples = samples;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	@Override
	public String toString() {
		return "QueryResponseMessage [messageType=" + messageType + ", runId=" + runId + ", dateTime=" + dateTime
				+ ", createdBy=" + createdBy + ", deviceID=" + deviceID + ", samples=" + samples + "]";
	}

}
