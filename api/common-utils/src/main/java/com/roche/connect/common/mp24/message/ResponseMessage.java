package com.roche.connect.common.mp24.message;

import java.io.Serializable;

public class ResponseMessage implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String orderControl;
	private String orderNumber;
	private String orderStatus;
	private String orderEventDate;
	private String protocolName;
	private String protocolDescription;
	private String resultStatus;
	private String dateTimeSpecimenExpiration;
	private String containerTypeID;
	private String containerTypeDescription;
	private SampleInfo sampleInfo;
	private ContainerInfo containerInfo;

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

	public String getDateTimeSpecimenExpiration() {
		return dateTimeSpecimenExpiration;
	}

	public void setDateTimeSpecimenExpiration(String dateTimeSpecimenExpiration) {
		this.dateTimeSpecimenExpiration = dateTimeSpecimenExpiration;
	}

	public String getContainerTypeID() {
		return containerTypeID;
	}

	public void setContainerTypeID(String containerTypeID) {
		this.containerTypeID = containerTypeID;
	}

	public String getContainerTypeDescription() {
		return containerTypeDescription;
	}

	public void setContainerTypeDescription(String containerTypeDescription) {
		this.containerTypeDescription = containerTypeDescription;
	}

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

}
