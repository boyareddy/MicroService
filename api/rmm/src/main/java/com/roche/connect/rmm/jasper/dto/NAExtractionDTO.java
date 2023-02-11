package com.roche.connect.rmm.jasper.dto;

import java.sql.Timestamp;
import java.util.List;

public class NAExtractionDTO {
	private long runResultId;
	private String deviceId;
	private String processStepName;
	private String deviceRunId;
	private String runStatus;
	private String dvcRunResult;
	private String runFlag;
	private String operatorName;
	private String comments;
    private String assayType;
	private String totalSamplecount;
	private String wfmsflag;
	private String totalSampleFlagCount;
	private String totalSampleFailedCount;
	private String plateId;
	private Timestamp startTime;
	private Timestamp completionTime;
	private String operatorId;
	private String sampleVolume;
	private String eluateVolume;
	private String protocolName;
	private String outputFormat;
	private Long samples;
	//private List<ReagentsAndConsumablesDTO> mp96reagentsAndConsumables;
	private List<MPReagentsDTO> mp96reagents;
	private List<MPConsumablesDTO> mp96Consumables;
	private List<PlateBasisSamplesDTO> mp96samplesData;
	private List<SampleCommentsDTO> mp96sampleComments;
	private List<FlagsDTO> mp96Flags;
	private List<SamplesDataDTO> mpsampleDetails;
	
	public List<SamplesDataDTO> getMpsampleDetails() {
		return mpsampleDetails;
	}
	public void setMpsampleDetails(List<SamplesDataDTO> mpsampleDetails) {
		this.mpsampleDetails = mpsampleDetails;
	}
	public long getRunResultId() {
		return runResultId;
	}
	public void setRunResultId(long runResultId) {
		this.runResultId = runResultId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
	public String getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	public String getDvcRunResult() {
		return dvcRunResult;
	}
	public void setDvcRunResult(String dvcRunResult) {
		this.dvcRunResult = dvcRunResult;
	}
	public String getRunFlag() {
		return runFlag;
	}
	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
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
	public String getWfmsflag() {
		return wfmsflag;
	}
	public void setWfmsflag(String wfmsflag) {
		this.wfmsflag = wfmsflag;
	}
	public String getTotalSampleFlagCount() {
		return totalSampleFlagCount;
	}
	public void setTotalSampleFlagCount(String totalSampleFlagCount) {
		this.totalSampleFlagCount = totalSampleFlagCount;
	}
	public String getTotalSampleFailedCount() {
		return totalSampleFailedCount;
	}
	public void setTotalSampleFailedCount(String totalSampleFailedCount) {
		this.totalSampleFailedCount = totalSampleFailedCount;
	}
	
	public String getPlateId() {
		return plateId;
	}
	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}
	/*public List<ReagentsAndConsumablesDTO> getMp96reagentsAndConsumables() {
		return mp96reagentsAndConsumables;
	}
	public void setMp96reagentsAndConsumables(List<ReagentsAndConsumablesDTO> mp96reagentsAndConsumables) {
		this.mp96reagentsAndConsumables = mp96reagentsAndConsumables;
	}*/
	
	public List<PlateBasisSamplesDTO> getMp96samplesData() {
		return mp96samplesData;
	}
	public List<MPReagentsDTO> getMp96reagents() {
		return mp96reagents;
	}
	public void setMp96reagents(List<MPReagentsDTO> mp96reagents) {
		this.mp96reagents = mp96reagents;
	}
	public List<MPConsumablesDTO> getMp96Consumables() {
		return mp96Consumables;
	}
	public void setMp96Consumables(List<MPConsumablesDTO> mp96Consumables) {
		this.mp96Consumables = mp96Consumables;
	}
	public void setMp96samplesData(List<PlateBasisSamplesDTO> mp96samplesData) {
		this.mp96samplesData = mp96samplesData;
	}
	public List<SampleCommentsDTO> getMp96sampleComments() {
		return mp96sampleComments;
	}
	public void setMp96sampleComments(List<SampleCommentsDTO> mp96sampleComments) {
		this.mp96sampleComments = mp96sampleComments;
	}
	public List<FlagsDTO> getMp96Flags() {
		return mp96Flags;
	}
	public void setMp96Flags(List<FlagsDTO> mp96Flags) {
		this.mp96Flags = mp96Flags;
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
	
}
