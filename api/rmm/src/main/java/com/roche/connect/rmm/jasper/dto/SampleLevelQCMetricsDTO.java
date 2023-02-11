package com.roche.connect.rmm.jasper.dto;

public class SampleLevelQCMetricsDTO {
	private Long sampleId;
	private String fetalFraction;
	private String arrayQuality;
	private Long signal;
	private String sampleIntegrity;
	private String noise;
	private String signalToNoise;
	public Long getSampleID() {
		return sampleId;
	}
	public void setSampleID(Long sampleId) {
		this.sampleId = sampleId;
	}
	public String getFetalFraction() {
		return fetalFraction;
	}
	public void setFetalFraction(String fetalFraction) {
		this.fetalFraction = fetalFraction;
	}
	public String getArrayQuality() {
		return arrayQuality;
	}
	public void setArrayQuality(String arrayQuality) {
		this.arrayQuality = arrayQuality;
	}
	public Long getSignal() {
		return signal;
	}
	public void setSignal(Long signal) {
		this.signal = signal;
	}
	public String getSampleIntegrity() {
		return sampleIntegrity;
	}
	public void setSampleIntegrity(String sampleIntegrity) {
		this.sampleIntegrity = sampleIntegrity;
	}
	public String getNoise() {
		return noise;
	}
	public void setNoise(String noise) {
		this.noise = noise;
	}
	public String getSignalToNoise() {
		return signalToNoise;
	}
	public void setSignalToNoise(String signalToNoise) {
		this.signalToNoise = signalToNoise;
	}
	
}
