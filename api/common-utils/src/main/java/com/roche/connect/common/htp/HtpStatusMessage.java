package com.roche.connect.common.htp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;

public class HtpStatusMessage implements Serializable{
   
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String accessioningId;
	private String status;
	private String inputContainerId;
	private String outputContainerId;
	private Long orderId;
	private Long runResultsId;
	private String deviceRunId;
	private String deviceId;
	private String deviceName;
	private String processStepName;
	private String sendingApplication;
	private String sendingFacility;
	private String receivingApplication;
	private String receivingFacility;
	private long estimatedTimeRemaining;
	private String updatedBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp updatedDateTime;
	private Collection<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumables = new ArrayList<>();
	
	public long getEstimatedTimeRemaining() {
		return estimatedTimeRemaining;
	}

	public void setEstimatedTimeRemaining(long estimatedTimeRemaining) {
		this.estimatedTimeRemaining = estimatedTimeRemaining;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInputContainerId() {
		return inputContainerId;
	}

	public void setInputContainerId(String inputContainerId) {
		this.inputContainerId = inputContainerId;
	}

	public String getOutputContainerId() {
		return outputContainerId;
	}

	public void setOutputContainerId(String outputContainerId) {
		this.outputContainerId = outputContainerId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getRunResultsId() {
		return runResultsId;
	}

	public void setRunResultsId(Long runResultsId) {
		this.runResultsId = runResultsId;
	}

	public String getDeviceRunId() {
		return deviceRunId;
	}

	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getProcessStepName() {
		return processStepName;
	}

	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
	}

	public String getSendingApplication() {
		return sendingApplication;
	}

	public void setSendingApplication(String sendingApplication) {
		this.sendingApplication = sendingApplication;
	}

	public String getSendingFacility() {
		return sendingFacility;
	}

	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	public String getReceivingApplication() {
		return receivingApplication;
	}

	public void setReceivingApplication(String receivingApplication) {
		this.receivingApplication = receivingApplication;
	}

	public String getReceivingFacility() {
		return receivingFacility;
	}

	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	
	public Collection<SampleReagentsAndConsumablesDTO> getSampleReagentsAndConsumables() {
		return sampleReagentsAndConsumables;
	}

	public void setSampleReagentsAndConsumables(Collection<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumables) {
		this.sampleReagentsAndConsumables = sampleReagentsAndConsumables;
	}
	
	@Override
	public String toString() {
		return "HtpStatusMessage [accessioningId=" + accessioningId + ", status=" + status + ", inputContainerId="
				+ inputContainerId + ", outputContainerId=" + outputContainerId + ", orderId=" + orderId
				+ ", runResultsId=" + runResultsId + ", deviceRunId=" + deviceRunId + ", deviceId=" + deviceId
				+ ", deviceName=" + deviceName + ", processStepName=" + processStepName + ", sendingApplication="
				+ sendingApplication + ", sendingFacility=" + sendingFacility + ", receivingApplication="
				+ receivingApplication + ", receivingFacility=" + receivingFacility + ", estimatedTimeRemaining=" + estimatedTimeRemaining + ", updatedBy="
				+ updatedBy + ", updatedDateTime=" + updatedDateTime + "]";
	}
	
	


}
