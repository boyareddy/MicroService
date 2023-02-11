package com.roche.connect.rmm.jasper.dto;

import java.util.List;

public class QCResultsDTO {
	private String medianReadLength;
	private String hqrYield;
	private String sequencingControlReadsPerc;
	private Long sequencingControlReads;
	private Long qcRunTotalSamples;
	private Long qcRunSamplesPassed;
	private double runSignalToNoise;
	private String negativeControlResult;
	private String positiveControlResult;
	private String t21Assessment;
	private String acpBarcode;
	private String runQCResult;
	private String runQCMedianSignal;
	private double qcRunNoise;
	private List<SampleLevelQCMetricsDTO> sampleLevelQCMetrics;
	private List<DPCRTestOptionMetricsDTO> dpcrTestOptionMetrics;
	
	public String getMedianReadLength() {
		return medianReadLength;
	}
	public void setMedianReadLength(String medianReadLength) {
		this.medianReadLength = medianReadLength;
	}
	public String getHqrYield() {
		return hqrYield;
	}
	public void setHqrYield(String hqrYield) {
		this.hqrYield = hqrYield;
	}
	public String getSequencingControlReadsPerc() {
		return sequencingControlReadsPerc;
	}
	public void setSequencingControlReadsPerc(String sequencingControlReadsPerc) {
		this.sequencingControlReadsPerc = sequencingControlReadsPerc;
	}
	public Long getSequencingControlReads() {
		return sequencingControlReads;
	}
	public void setSequencingControlReads(Long sequencingControlReads) {
		this.sequencingControlReads = sequencingControlReads;
	}
	public Long getQcRunSamplesPassed() {
		return qcRunSamplesPassed;
	}
	public void setQcRunSamplesPassed(Long qcRunSamplesPassed) {
		this.qcRunSamplesPassed = qcRunSamplesPassed;
	}
	public Long getQcRunTotalSamples() {
		return qcRunTotalSamples;
	}
	public void setQcRunTotalSamples(Long qcRunTotalSamples) {
		this.qcRunTotalSamples = qcRunTotalSamples;
	}
	public double getRunSignalToNoise() {
		return runSignalToNoise;
	}
	public void setRunSignalToNoise(double runSignalToNoise) {
		this.runSignalToNoise = runSignalToNoise;
	}
	public String getNegativeControlResult() {
		return negativeControlResult;
	}
	public void setNegativeControlResult(String negativeControlResult) {
		this.negativeControlResult = negativeControlResult;
	}
	public String getPositiveControlResult() {
		return positiveControlResult;
	}
	public void setPositiveControlResult(String positiveControlResult) {
		this.positiveControlResult = positiveControlResult;
	}
	public String getT21Assessment() {
		return t21Assessment;
	}
	public void setT21Assessment(String t21Assessment) {
		this.t21Assessment = t21Assessment;
	}
	public String getAcpBarcode() {
		return acpBarcode;
	}
	public void setAcpBarcode(String acpBarcode) {
		this.acpBarcode = acpBarcode;
	}
	public String getRunQCResult() {
		return runQCResult;
	}
	public void setRunQCResult(String runQCResult) {
		this.runQCResult = runQCResult;
	}
	public String getRunQCMedianSignal() {
		return runQCMedianSignal;
	}
	public void setRunQCMedianSignal(String runQCMedianSignal) {
		this.runQCMedianSignal = runQCMedianSignal;
	}
	public double getQcRunNoise() {
		return qcRunNoise;
	}
	public void setQcRunNoise(double qcRunNoise) {
		this.qcRunNoise = qcRunNoise;
	}
	public List<SampleLevelQCMetricsDTO> getSampleLevelQCMetrics() {
		return sampleLevelQCMetrics;
	}
	public void setSampleLevelQCMetrics(List<SampleLevelQCMetricsDTO> sampleLevelQCMetrics) {
		this.sampleLevelQCMetrics = sampleLevelQCMetrics;
	}
	public List<DPCRTestOptionMetricsDTO> getDpcrTestOptionMetrics() {
		return dpcrTestOptionMetrics;
	}
	public void setDpcrTestOptionMetrics(List<DPCRTestOptionMetricsDTO> dpcrTestOptionMetrics) {
		this.dpcrTestOptionMetrics = dpcrTestOptionMetrics;
	}
	
}
