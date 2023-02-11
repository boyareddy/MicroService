package com.roche.connect.common.mp24.message;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class StatusUpdate implements Serializable{
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String eventDate;
	private String equipmentState;
	private ContainerInfo containerInfo;
	private String protocolName;
	private String protocolVersion;
	private String runResult;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp runStartTime;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp runEndTime;
	private String comment;
	private SampleInfo sampleInfo;
	private String orderName;
	private String orderResult;
	private String internalControls;
	private String processingCartridge;
	private String tipRack;
	private String operatorName;
	private String updatedBy;
	private String flag;
	private List<Consumable> consumables;
	
	public List<Consumable> getConsumables() {
		return consumables;
	}

	public void setConsumables(List<Consumable> consumables) {
		this.consumables = consumables;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEquipmentState() {
		return equipmentState;
	}

	public void setEquipmentState(String equipmentState) {
		this.equipmentState = equipmentState;
	}

	public ContainerInfo getContainerInfo() {
		return containerInfo;
	}

	public void setContainerInfo(ContainerInfo containerInfo) {
		this.containerInfo = containerInfo;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getRunResult() {
		return runResult;
	}

	public void setRunResult(String runResult) {
		this.runResult = runResult;
	}

	public Timestamp getRunStartTime() {
		return runStartTime;
	}

	public void setRunStartTime(Timestamp runStartTime) {
		this.runStartTime = runStartTime;
	}

	public Timestamp getRunEndTime() {
		return runEndTime;
	}

	public void setRunEndTime(Timestamp runEndTime) {
		this.runEndTime = runEndTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public SampleInfo getSampleInfo() {
		return sampleInfo;
	}

	public void setSampleInfo(SampleInfo sampleInfo) {
		this.sampleInfo = sampleInfo;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderResult() {
		return orderResult;
	}

	public void setOrderResult(String orderResult) {
		this.orderResult = orderResult;
	}

	public String getInternalControls() {
		return internalControls;
	}

	public void setInternalControls(String internalControls) {
		this.internalControls = internalControls;
	}

	public String getProcessingCartridge() {
		return processingCartridge;
	}

	public void setProcessingCartridge(String processingCartridge) {
		this.processingCartridge = processingCartridge;
	}

	public String getTipRack() {
		return tipRack;
	}

	public void setTipRack(String tipRack) {
		this.tipRack = tipRack;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatusUpdate [eventDate=" + eventDate + ", equipmentState=" + equipmentState + ", containerInfo="
				+ containerInfo + ", protocolName=" + protocolName + ", protocolVersion=" + protocolVersion
				+ ", runResult=" + runResult + ", runStartTime=" + runStartTime + ", runEndTime=" + runEndTime
				+ ", comment=" + comment + ", sampleInfo=" + sampleInfo + ", orderName=" + orderName + ", orderResult="
				+ orderResult + ", internalControls=" + internalControls + ", processingCartridge="
				+ processingCartridge + ", tipRack=" + tipRack + "]";
	}

	
	
}
