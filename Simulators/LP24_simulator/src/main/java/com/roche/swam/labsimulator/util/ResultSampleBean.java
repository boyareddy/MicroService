package com.roche.swam.labsimulator.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roche.swam.labsimulator.mpx.bl.Consumables;

@JsonInclude(Include.NON_NULL)
public class ResultSampleBean {
	private String sampleID, result, flag,internalControls,processingCartridge,tipRack,inputContainerId,inputPosition,plateType,sampleComments;
	 private List<Consumables> consumables;

	public String getSampleID() {
		return sampleID;
	}

	public void setSampleID(String sampleID) {
		this.sampleID = sampleID;
	}

	public String getResult() {
		return result;
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


	public void setResult(String result) {
		this.result = result;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List<Consumables> getConsumables() {
		return consumables;
	}

	public void setConsumables(List<Consumables> consumables) {
		this.consumables = consumables;
	}

	public String getInputContainerId() {
		return inputContainerId;
	}

	public void setInputContainerId(String inputContainerId) {
		this.inputContainerId = inputContainerId;
	}

	public String getInputPosition() {
		return inputPosition;
	}

	public void setInputPosition(String inputPosition) {
		this.inputPosition = inputPosition;
	}

	public String getPlateType() {
		return plateType;
	}

	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}

	public String getSampleComments() {
		return sampleComments;
	}

	public void setSampleComments(String sampleComments) {
		this.sampleComments = sampleComments;
	}

	@Override
	public String toString() {
		return "ResultSampleBean [sampleID=" + sampleID + ", result=" + result + ", flag=" + flag
				+ ", internalControls=" + internalControls + ", processingCartridge=" + processingCartridge
				+ ", tipRack=" + tipRack + ", inputContainerId=" + inputContainerId + ", inputPosition=" + inputPosition
				+ ", plateType=" + plateType + ", sampleComments=" + sampleComments + ", consumables=" + consumables
				+ "]";
	}


	
	

}
