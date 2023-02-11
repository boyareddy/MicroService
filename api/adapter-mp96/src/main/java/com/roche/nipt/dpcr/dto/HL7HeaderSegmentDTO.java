/**
 * 
 */
package com.roche.nipt.dpcr.dto;

/**
 * @author gosula.r
 *
 */
public class HL7HeaderSegmentDTO {

	private String deviceName;
	private String deviceId;
	private String messageType;
	private String dateAndTime;
	private String queryDefDesc;
	private String processingId;
	private String versionId;
	

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getQueryDefDesc() {
		return queryDefDesc;
	}

	public void setQueryDefDesc(String queryDefDesc) {
		this.queryDefDesc = queryDefDesc;
	}

	@Override
	public String toString() {
		return "HL7HeaderSegmentDTO [deviceName=" + deviceName + ", deviceId=" + deviceId + ", messageType="
				+ messageType + ", dateAndTime=" + dateAndTime + ", queryDefDesc=" + queryDefDesc + ", processingId="
				+ processingId + ", versionId=" + versionId + "]";
	}

	public String getProcessingId() {
		return processingId;
	}

	public void setProcessingId(String processingId) {
		this.processingId = processingId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
