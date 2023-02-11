package com.roche.connect.common.mp96;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OULSampleResultMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	private String time;
	private String operator;
	private String accessioningId;
	private String outputContainerId;
	private String reagentKitName;
	private String reagentVesion;
	private String runStartTime;
	private String runEndTime;
	private String outputPlateType;
	private String protocal;
	private String sampleVolume;
	private String elevateVolume;
	private String softwareVersion;
	private String serialNo;
	private String position;
	private String volume;
	private String barcode;
	private String expDate;
	private String lotNo;
	private String sampleResult;
	private String sampleComments;
	private String flag;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public String getOutputContainerId() {
		return outputContainerId;
	}

	public void setOutputContainerId(String outputContainerId) {
		this.outputContainerId = outputContainerId;
	}

	public String getReagentKitName() {
		return reagentKitName;
	}

	public void setReagentKitName(String reagentKitName) {
		this.reagentKitName = reagentKitName;
	}

	public String getReagentVesion() {
		return reagentVesion;
	}

	public void setReagentVesion(String reagentVesion) {
		this.reagentVesion = reagentVesion;
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

	public String getOutputPlateType() {
		return outputPlateType;
	}

	public void setOutputPlateType(String outputPlateType) {
		this.outputPlateType = outputPlateType;
	}

	public String getProtocal() {
		return protocal;
	}

	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}

	public String getSampleVolume() {
		return sampleVolume;
	}

	public void setSampleVolume(String sampleVolume) {
		this.sampleVolume = sampleVolume;
	}

	public String getElevateVolume() {
		return elevateVolume;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public void setElevateVolume(String elevateVolume) {
		this.elevateVolume = elevateVolume;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSampleResult() {
		return sampleResult;
	}

	public void setSampleResult(String sampleResult) {
		this.sampleResult = sampleResult;
	}

	public String getSampleComments() {
		return sampleComments;
	}

	public void setSampleComments(String sampleComments) {
		this.sampleComments = sampleComments;
	}


	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OULSampleResultMessage [time=" + time + ", operator=" + operator + ", accessioningId=" + accessioningId
				+ ", outputContainerId=" + outputContainerId + ", reagentKitName=" + reagentKitName + ", reagentVesion="
				+ reagentVesion + ", runStartTime=" + runStartTime + ", runEndTime=" + runEndTime + ", outputPlateType="
				+ outputPlateType + ", protocal=" + protocal + ", sampleVolume=" + sampleVolume + ", elevateVolume="
				+ elevateVolume + ", softwareVersion=" + softwareVersion + ", serialNo=" + serialNo + ", position="
				+ position + ", volume=" + volume + ", barcode=" + barcode + ", expDate=" + expDate + ", lotNo=" + lotNo
				+ ", sampleResult=" + sampleResult + ", sampleComments=" + sampleComments + ", flag=" + flag + "]";
	}
	
	
}