package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ORUMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dateTimeMessageGenerated;
	private String messageControlId;
	private String messageType;
	private String deviceId;
	private String runId;
	private String operatorName;
	private String runComments;
	private String releasedBy;
	private String sentBy;
	private Collection<ORUSampleDetails> oruSampleDetails = new ArrayList<>();

	public String getDateTimeMessageGenerated() {
		return dateTimeMessageGenerated;
	}

	public void setDateTimeMessageGenerated(String dateTimeMessageGenerated) {
		this.dateTimeMessageGenerated = dateTimeMessageGenerated;
	}

	public String getMessageControlId() {
		return messageControlId;
	}

	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}

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

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getRunComments() {
		return runComments;
	}

	public void setRunComments(String runComments) {
		this.runComments = runComments;
	}

	public String getReleasedBy() {
		return releasedBy;
	}

	public void setReleasedBy(String releasedBy) {
		this.releasedBy = releasedBy;
	}

	public String getSentBy() {
		return sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

	public Collection<ORUSampleDetails> getOruSampleDetails() {
		return oruSampleDetails;
	}

	public void setOruSampleDetails(Collection<ORUSampleDetails> oruSampleDetails) {
		this.oruSampleDetails = oruSampleDetails;
	}

}
