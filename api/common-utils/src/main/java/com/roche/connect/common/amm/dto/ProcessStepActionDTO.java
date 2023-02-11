package com.roche.connect.common.amm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessStepActionDTO {
	private String deviceType;
	private Integer processStepSeq;
	private String processStepName;
	private String inputContainerType;
	private String outputContainerType;
	private String manualVerificationFlag;
	private String assayType;
	private String sampleVolume;
	private String eluateVolume;
	private String reagent;

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getProcessStepName() {
		return processStepName;
	}

	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
	}

	public String getInputContainerType() {
		return inputContainerType;
	}

	public void setInputContainerType(String inputContainerType) {
		this.inputContainerType = inputContainerType;
	}

	public String getOutputContainerType() {
		return outputContainerType;
	}

	public void setOutputContainerType(String outputContainerType) {
		this.outputContainerType = outputContainerType;
	}

	public String getManualVerificationFlag() {
		return manualVerificationFlag;
	}

	public void setManualVerificationFlag(String manualVerificationFlag) {
		this.manualVerificationFlag = manualVerificationFlag;
	}

	public Integer getProcessStepSeq() {
		return processStepSeq;
	}

	public void setProcessStepSeq(Integer processStepSeq) {
		this.processStepSeq = processStepSeq;
	}

	public String getSampleVolume() {
		return sampleVolume;
	}

	public void setSampleVolume(String sampleVolume) {
		this.sampleVolume = sampleVolume;
	}

	public String getEluateVolume() {
		return eluateVolume;
	}

	public void setEluateVolume(String eluateVolume) {
		this.eluateVolume = eluateVolume;
	}

	public String getReagent() {
		return reagent;
	}

	public void setReagent(String reagent) {
		this.reagent = reagent;
	}

}
