package com.roche.connect.common.order.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class WorkflowDTO {
	private String accessioningId;
	private String comments;
	private long orderId;
	private String workflowType;
	private String assaytype;
	private String sampletype;
	private String workflowStatus;
	private String flags;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date createDate;
	private boolean mandatoryFieldMissing;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public String getAssaytype() {
		return assaytype;
	}

	public void setAssaytype(String assaytype) {
		this.assaytype = assaytype;
	}

	public String getSampletype() {
		return sampletype;
	}

	public void setSampletype(String sampletype) {
		this.sampletype = sampletype;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public boolean isMandatoryFieldMissing() {
		return mandatoryFieldMissing;
	}

	public void setMandatoryFieldMissing(boolean mandatoryFieldMissing) {
		this.mandatoryFieldMissing = mandatoryFieldMissing;
	}
	
}
