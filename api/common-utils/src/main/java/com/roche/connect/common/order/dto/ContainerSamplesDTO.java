package com.roche.connect.common.order.dto;

import java.sql.Timestamp;

public class ContainerSamplesDTO {

	private Long containerSampleId;
	private String containerID;
	private String position;
	private String containerType;
	private String accessioningID;
	private String deviceRunID;
	private String activeFlag;
	private String createdBy;
	private Timestamp createdDateTime;
	private String updatedBy;
	private Timestamp updatedDateTime;
	private String status;
	private Long loadID;
	private String deviceID;
	private String assayType;
	private String orderComments;
	private Long orderID;

	public Long getContainerSampleId() {
		return containerSampleId;
	}

	public void setContainerSampleId(Long containerSampleId) {
		this.containerSampleId = containerSampleId;
	}

	public String getContainerID() {
		return containerID;
	}

	public void setContainerID(String containerID) {
		this.containerID = containerID;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public String getAccessioningID() {
		return accessioningID;
	}

	public void setAccessioningID(String accessioningID) {
		this.accessioningID = accessioningID;
	}

	public String getDeviceRunID() {
		return deviceRunID;
	}

	public void setDeviceRunID(String deviceRunID) {
		this.deviceRunID = deviceRunID;
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

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getLoadID() {
		return loadID;
	}

	public void setLoadID(Long loadID) {
		this.loadID = loadID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getOrderComments() {
		return orderComments;
	}

	public void setOrderComments(String orderComments) {
		this.orderComments = orderComments;
	}

	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}

}
