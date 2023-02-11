package com.camel.dto;

import java.util.List;

public class StatusUpdate {
	private String eventDate;
	private String equipmentState;
	private ContainerInfo containerInfo;
	private String protocolName;
	private String protocolVersion;
	private String runResult;
	private String runStartTime;
	private String runEndTime;
	private String comment;
	private SampleInfo sampleInfo;
	private String orderName;
	private String orderResult;
	private String internalControls;
	private String processingCartridge;
	private String tipRack;
	private String reagentCassette;
	private List<String> reagent2mlTube;
	private List<String> reagent25mlTube;
	
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
	public String getRunStartTime() {
		return runStartTime;
	}
	public void setRunStartTime(String runStartTime) {
		this.runStartTime = runStartTime;
	}
	public String getRunEndTime() {
		return runEndTime;
	}
	public void setRunEndTime(String runEndTime) {
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
	public String getReagentCassette() {
		return reagentCassette;
	}
	public void setReagentCassette(String reagentCassette) {
		this.reagentCassette = reagentCassette;
	}
	public List<String> getReagent2mlTube() {
		return reagent2mlTube;
	}
	public void setReagent2mlTube(List<String> reagent2mlTube) {
		this.reagent2mlTube = reagent2mlTube;
	}
	public List<String> getReagent25mlTube() {
		return reagent25mlTube;
	}
	public void setReagent25mlTube(List<String> reagent25mlTube) {
		this.reagent25mlTube = reagent25mlTube;
	}

	
}
