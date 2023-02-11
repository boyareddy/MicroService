package com.camel.dto;

public class RSPMessageDTO {
	
	SampleInfo sampleInfo;
	
	ContainerInfo containerInfo;
	
	String orderControl;
	String orderNumber;
	String orderStatus;
	String orderEventDate;
	String protocolName;
	String protocolDescription;
	String resultStatus;
	String eluateVolume;
	
	public SampleInfo getSampleInfo() {
		return sampleInfo;
	}

	public void setSampleInfo(SampleInfo sampleInfo) {
		this.sampleInfo = sampleInfo;
	}

	public ContainerInfo getContainerInfo() {
		return containerInfo;
	}

	public void setContainerInfo(ContainerInfo containerInfo) {
		this.containerInfo = containerInfo;
	}

	public String getOrderControl() {
		return orderControl;
	}

	public void setOrderControl(String orderControl) {
		this.orderControl = orderControl;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderEventDate() {
		return orderEventDate;
	}

	public void setOrderEventDate(String orderEventDate) {
		this.orderEventDate = orderEventDate;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getProtocolDescription() {
		return protocolDescription;
	}

	public void setProtocolDescription(String protocolDescription) {
		this.protocolDescription = protocolDescription;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getEluateVolume() {
		return eluateVolume;
	}

	public void setEluateVolume(String eluateVolume) {
		this.eluateVolume = eluateVolume;
	}
}
