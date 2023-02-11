package com.roche.connect.common.rmm.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class SearchOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long runResultsId;
	private long sampleResultId;
	private String accessioningId;
	private long orderId;
	private String status;
	private String processStepName;
	private String flags;
	private String assayType;
	private String sampleType;

	public Long getRunResultsId() {
		return runResultsId;
	}

	public void setRunResultsId(Long runResultsId) {
		this.runResultsId = runResultsId;
	}

	public long getSampleResultId() {
		return sampleResultId;
	}

	public void setSampleResultId(long sampleResultId) {
		this.sampleResultId = sampleResultId;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
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

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

}
