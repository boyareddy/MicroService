package com.roche.connect.common.order.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class ProcessStepValuesDTO {

	private Long sampleResultId;
	private String accesssioningId;
	private Long orderId;
	private String outputContainerId;
	private String outputContainerPosition;
	private String outputKitId;

	private Long runResultId;
	private String deviceId;
	private String processStepName;
	private String runStatus;
	private String runFlag;
	private String operatorName;
	private String comments;
	private String updatedBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp runStartTime;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp runCompletionTime;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp runRemainingTime;

	public Long getSampleResultId() {
		return sampleResultId;
	}

	public void setSampleResultId(Long sampleResultId) {
		this.sampleResultId = sampleResultId;
	}

	public String getAccesssioningId() {
		return accesssioningId;
	}

	public void setAccesssioningId(String accesssioningId) {
		this.accesssioningId = accesssioningId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOutputContainerId() {
		return outputContainerId;
	}

	public void setOutputContainerId(String outputContainerId) {
		this.outputContainerId = outputContainerId;
	}

	public String getOutputContainerPosition() {
		return outputContainerPosition;
	}

	public void setOutputContainerPosition(String outputContainerPosition) {
		this.outputContainerPosition = outputContainerPosition;
	}

	public String getOutputKitId() {
		return outputKitId;
	}

	public void setOutputKitId(String outputKitId) {
		this.outputKitId = outputKitId;
	}

	public Long getRunResultId() {
		return runResultId;
	}

	public void setRunResultId(Long runResultId) {
		this.runResultId = runResultId;
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

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public String getRunFlag() {
		return runFlag;
	}

	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getRunStartTime() {
		return runStartTime;
	}

	public void setRunStartTime(Timestamp runStartTime) {
		this.runStartTime = runStartTime;
	}

	public Timestamp getRunCompletionTime() {
		return runCompletionTime;
	}

	public void setRunCompletionTime(Timestamp runCompletionTime) {
		this.runCompletionTime = runCompletionTime;
	}

	public Timestamp getRunRemainingTime() {
		return runRemainingTime;
	}

	public void setRunRemainingTime(Timestamp runRemainingTime) {
		this.runRemainingTime = runRemainingTime;
	}

}
