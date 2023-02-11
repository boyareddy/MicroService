package com.roche.connect.common.dmm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String gatewayId;
	private String serialNo;
	private boolean status;
	private String isRetired;
	private String state;
	private String site;
	private String ipAddress;
	private String editedBy;
	private String createdBy;
	private String deviceId;
	private String ownerPropertyName;
	private DeviceTypeDTO deviceType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getIsRetired() {
		return isRetired;
	}

	public void setIsRetired(String isRetired) {
		this.isRetired = isRetired;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getEditedBy() {
		return editedBy;
	}

	public void setEditedBy(String editedBy) {
		this.editedBy = editedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOwnerPropertyName() {
		return ownerPropertyName;
	}

	public void setOwnerPropertyName(String ownerPropertyName) {
		this.ownerPropertyName = ownerPropertyName;
	}

	public DeviceTypeDTO getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceTypeDTO deviceType) {
		this.deviceType = deviceType;
	}

}
