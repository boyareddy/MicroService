package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String messageControlId;
	private String messageType;
	private String deviceId;
	private String sendingApplicationName;
	private String sendingFacility;
	private String recevingApplicationName;
	private String recevingFacility;
	private String dateTimeMessageGenerated;
	private List<String> plateId;
	
	public String getSendingFacility() {
		return sendingFacility;
	}

	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	public String getRecevingApplicationName() {
		return recevingApplicationName;
	}

	public void setRecevingApplicationName(String recevingApplicationName) {
		this.recevingApplicationName = recevingApplicationName;
	}

	public String getRecevingFacility() {
		return recevingFacility;
	}

	public void setRecevingFacility(String recevingFacility) {
		this.recevingFacility = recevingFacility;
	}

	public String getDateTimeMessageGenerated() {
		return dateTimeMessageGenerated;
	}

	public void setDateTimeMessageGenerated(String dateTimeMessageGenerated) {
		this.dateTimeMessageGenerated = dateTimeMessageGenerated;
	}

	public String getMessageControlId() {
		return messageControlId;
	}

	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSendingApplicationName() {
		return sendingApplicationName;
	}

	public void setSendingApplicationName(String sendingApplicationName) {
		this.sendingApplicationName = sendingApplicationName;
	}

	public List<String> getPlateId() {
		return plateId;
	}

	public void setPlateId(List<String> plateId) {
		this.plateId = plateId;
	}

}
