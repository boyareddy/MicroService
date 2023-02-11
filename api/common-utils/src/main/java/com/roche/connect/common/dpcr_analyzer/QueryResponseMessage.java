package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResponseMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String messageType;
	private String runId;
	private String messageControlId;

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

	public String getMessageControlId() {
		return messageControlId;
	}

	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}

	public List<QueryResponseSample> getSamples() {
		return samples;
	}

	public void setSamples(List<QueryResponseSample> samples) {
		this.samples = samples;
	}

}
