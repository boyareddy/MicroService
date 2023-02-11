package com.roche.connect.rmm.jasper.dto;

import java.sql.Timestamp;
import java.util.List;

public class LibraryPrepDTO {
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
	private Timestamp startTime;
	private Timestamp completionTime;
	private String operatorId;
	private String sampleVolume;
	private String eluateVolume;
	private String protocolName;
	private String outputFormat;
	private Long samples;
	//private String plateId;
	//private List<PlateBasisSamplesDTO> lpsamplesData;
	private List<LpplateBasedSamples> lpsamplesData;
	private List<ReagentsAndConsumablesDTO> lpReagentsAndConsumables;
	private List<SampleCommentsDTO> lpSampleComments;
	private List<FlagsDTO> lpFlags;
	
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
	
	/*public String getPlateId() {
		return plateId;
	}
	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}*/
	
	public List<ReagentsAndConsumablesDTO> getLpReagentsAndConsumables() {
		return lpReagentsAndConsumables;
	}
	/*public List<PlateBasisSamplesDTO> getLpsamplesData() {
		return lpsamplesData;
	}
	public void setLpsamplesData(List<PlateBasisSamplesDTO> lpsamplesData) {
		this.lpsamplesData = lpsamplesData;
	}*/
	
	public void setLpReagentsAndConsumables(List<ReagentsAndConsumablesDTO> lpReagentsAndConsumables) {
		this.lpReagentsAndConsumables = lpReagentsAndConsumables;
	}
	public List<LpplateBasedSamples> getLpsamplesData() {
		return lpsamplesData;
	}
	public void setLpsamplesData(List<LpplateBasedSamples> lpsamplesData) {
		this.lpsamplesData = lpsamplesData;
	}
	public List<SampleCommentsDTO> getLpSampleComments() {
		return lpSampleComments;
	}
	public void setLpSampleComments(List<SampleCommentsDTO> lpSampleComments) {
		this.lpSampleComments = lpSampleComments;
	}
	public List<FlagsDTO> getLpFlags() {
		return lpFlags;
	}
	public void setLpFlags(List<FlagsDTO> lpFlags) {
		this.lpFlags = lpFlags;
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
