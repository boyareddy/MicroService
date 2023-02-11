package com.roche.connect.common.amm.dto;

public class SampleTypeDTO {
	private String sampleType;
	private int maxAgeDays;
	private String assayType;
	
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public int getMaxAgeDays() {
		return maxAgeDays;
	}
	public void setMaxAgeDays(int maxAgeDays) {
		this.maxAgeDays = maxAgeDays;
	}
		
}
