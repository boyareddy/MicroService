package com.roche.connect.rmm.jasper.dto;

import java.sql.Timestamp;
import java.util.List;

public class DPCRDTO {
	private long runResultId;
	private String processStepName;
	private String deviceRunId;
	private String runFlag;
	private String comments;
    private String assayType;
	private String totalSamplecount;
	private String plateId;
	private Timestamp startTime;
	private Timestamp completionTime;
	private String operatorId;
	private String sampleVolume;
	private String eluateVolume;
	private String protocolName;
	private String outputFormat;
	private String outputFilePath;
	private Long samples;
	
	private List<SampleCommentsDTO> dpcrSampleComments;
	private List<FlagsDTO> dpcrFlags;
	private List<DPCRReagentnConsumableDTO> dpcrReagents;
	private List<PlateBasisSamplesDTO> dpcrsamplesData;
	public long getRunResultId() {
		return runResultId;
	}
	public void setRunResultId(long runResultId) {
		this.runResultId = runResultId;
	}
	public String getProcessStepName() {
		return processStepName;
	}
	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
	}
	public String getDeviceRunId() {
		return deviceRunId;
	}
	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}
	public String getRunFlag() {
		return runFlag;
	}
	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getTotalSamplecount() {
		return totalSamplecount;
	}
	public void setTotalSamplecount(String totalSamplecount) {
		this.totalSamplecount = totalSamplecount;
	}
	public String getPlateId() {
		return plateId;
	}
	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(Timestamp completionTime) {
		this.completionTime = completionTime;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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
	public String getProtocolName() {
		return protocolName;
	}
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	public String getOutputFormat() {
		return outputFormat;
	}
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
	public Long getSamples() {
		return samples;
	}
	public void setSamples(Long samples) {
		this.samples = samples;
	}
	public List<SampleCommentsDTO> getDpcrSampleComments() {
		return dpcrSampleComments;
	}
	public void setDpcrSampleComments(List<SampleCommentsDTO> dpcrSampleComments) {
		this.dpcrSampleComments = dpcrSampleComments;
	}
	public List<FlagsDTO> getDpcrFlags() {
		return dpcrFlags;
	}
	public void setDpcrFlags(List<FlagsDTO> dpcrFlags) {
		this.dpcrFlags = dpcrFlags;
	}
	public List<DPCRReagentnConsumableDTO> getDpcrReagents() {
		return dpcrReagents;
	}
	public void setDpcrReagents(List<DPCRReagentnConsumableDTO> dpcrReagents) {
		this.dpcrReagents = dpcrReagents;
	}
	public List<PlateBasisSamplesDTO> getDpcrsamplesData() {
		return dpcrsamplesData;
	}
	public void setDpcrsamplesData(List<PlateBasisSamplesDTO> dpcrsamplesData) {
		this.dpcrsamplesData = dpcrsamplesData;
	}
	public String getOutputFilePath() {
		return outputFilePath;
	}
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}
	
}
