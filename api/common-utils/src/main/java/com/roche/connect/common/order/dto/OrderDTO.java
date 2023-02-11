package com.roche.connect.common.order.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class OrderDTO  implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId;

	private Long patientId;

	private Long patientSampleId;

	private String accessioningId;

	private String orderStatus;

	private String assayType;

	private String sampleType;

	private boolean retestSample;

	private String orderComments;

	private String activeFlag;

	private String createdBy;

	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp createdDateTime;

	private String updatedBy;
	
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp updatedDateTime;

	private AssayDTO assay;
	private PatientDTO patient;
	
	private boolean reqFieldMissingFlag;
	
	private String priority;
	
	private Timestamp priorityUpdatedTime;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getPatientSampleId() {
		return patientSampleId;
	}

	public void setPatientSampleId(Long patientSampleId) {
		this.patientSampleId = patientSampleId;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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

	public boolean isRetestSample() {
        return retestSample;
    }

    public void setRetestSample(boolean retestSample) {
        this.retestSample = retestSample;
    }

    public String getOrderComments() {
		return orderComments;
	}

	public void setOrderComments(String orderComments) {
		this.orderComments = orderComments;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public AssayDTO getAssay() {
		return assay;
	}

	public void setAssay(AssayDTO assay) {
		this.assay = assay;
	}

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public boolean isReqFieldMissingFlag() {
		return reqFieldMissingFlag;
	}

	public void setReqFieldMissingFlag(boolean reqFieldMissingFlag) {
		this.reqFieldMissingFlag = reqFieldMissingFlag;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Timestamp getPriorityUpdatedTime() {
		return priorityUpdatedTime;
	}

	public void setPriorityUpdatedTime(Timestamp priorityUpdatedTime) {
		this.priorityUpdatedTime = priorityUpdatedTime;
	}
	
}
