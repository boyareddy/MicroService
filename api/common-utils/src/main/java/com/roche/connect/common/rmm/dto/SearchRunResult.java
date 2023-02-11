package com.roche.connect.common.rmm.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roche.connect.common.order.dto.ContainerDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class SearchRunResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private long runResultId;
	private String device;
	private String processStepName;
	private String deviceRunId;
	private String runStatus;
	private String operatorName;
	private long sampleCount;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp runCompletionTime;
	private ContainerDTO container;

	public long getRunResultId() {
		return runResultId;
	}

	public void setRunResultId(long runResultId) {
		this.runResultId = runResultId;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getProcessStepName() {
		return processStepName;
	}

	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public long getSampleCount() {
		return sampleCount;
	}

	public void setSampleCount(long sampleCount) {
		this.sampleCount = sampleCount;
	}

	public Timestamp getRunCompletionTime() {
		return runCompletionTime;
	}

	public void setRunCompletionTime(Timestamp runCompletionTime) {
		this.runCompletionTime = runCompletionTime;
	}

	public ContainerDTO getContainer() {
		return container;
	}

	public void setContainer(ContainerDTO container) {
		this.container = container;
	}

}
