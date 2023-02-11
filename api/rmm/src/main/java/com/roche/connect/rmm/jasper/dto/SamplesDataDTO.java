package com.roche.connect.rmm.jasper.dto;

public class SamplesDataDTO {
	private String sampleId;
	private String assayType;
	private String position;
	private String status;
	private String flags;
	private String qualitativeResult;
	private String quantitativeResult;
	
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFlags() {
		return flags;
	}
	public void setFlags(String flags) {
		this.flags = flags;
	}
	public String getQualitativeResult() {
		return qualitativeResult;
	}
	public void setQualitativeResult(String qualitativeResult) {
		this.qualitativeResult = qualitativeResult;
	}
	public String getQuantitativeResult() {
		return quantitativeResult;
	}
	public void setQuantitativeResult(String quantitativeResult) {
		this.quantitativeResult = quantitativeResult;
	}
	
}
