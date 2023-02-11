/******************************************************************************
  *RunResultsDTO.java                  
  * Version:  1.0
  *
  * Authors:  sivaraman.r
  *
  **********************
  * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
  * All Rights Reserved.
  *
  * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
  * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
  * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
  * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
  * executed Confidentiality and Non-disclosure agreements explicitly covering such access
  *
  * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
  * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
  * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
  * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
  * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
  *               
  **********************
  * ChangeLog:
  *
******************************************************************************/
package com.roche.connect.common.rmm.dto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class RunResultsDTO {

	private long runResultId;
	private String deviceId;
	private String deviceType;
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
	private Integer totalSampleFlagCount;
	private Integer totalSampleFailedCount;

	private Collection<InputStripDetailsDTO> inputcontainerIds = new ArrayList<>();
	private Collection<OutputStripDetailsDTO> outputcontainerIds = new ArrayList<>();

	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp runStartTime;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp runCompletionTime;
	private Long runRemainingTime;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp verifiedDate;
	private String verifiedBy;
	private String updatedBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp updatedDateTime;
	private String createdBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date createdDateTime;
	private Collection<SampleResultsDTO> sampleResults = new ArrayList<>();
	private Collection<RunReagentsAndConsumablesDTO> runReagentsAndConsumables = new ArrayList<>();
	private Collection<RunResultsDetailDTO> runResultsDetail = new ArrayList<>();
	private Collection<UniqueReagentsAndConsumables> uniqueReagentsAndConsumables = new ArrayList<>();

	String instant = Instant.now().toString();

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

	public Timestamp getRunStartTime() {
		return runStartTime;
	}

	public void setRunStartTime(Timestamp runStartTime) {
		this.runStartTime = runStartTime;
	}

	public Timestamp getRunCompletionTime() {
		return runCompletionTime;
	}

	public void setRunCompletionTime(Timestamp runCompletionTime) {
		this.runCompletionTime = runCompletionTime;
	}

	public Long getRunRemainingTime() {
		return runRemainingTime;
	}

	public void setRunRemainingTime(Long runRemainingTime) {
		this.runRemainingTime = runRemainingTime;
	}

	public Timestamp getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Timestamp verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public Collection<SampleResultsDTO> getSampleResults() {
		return sampleResults;
	}

	public void setSampleResults(Collection<SampleResultsDTO> sampleResults) {
		this.sampleResults = sampleResults;
	}

	public Collection<RunReagentsAndConsumablesDTO> getRunReagentsAndConsumables() {
		return runReagentsAndConsumables;
	}

	public void setRunReagentsAndConsumables(Collection<RunReagentsAndConsumablesDTO> runReagentsAndConsumables) {
		this.runReagentsAndConsumables = runReagentsAndConsumables;
	}

	public Collection<RunResultsDetailDTO> getRunResultsDetail() {
		return runResultsDetail;
	}

	public void setRunResultsDetail(Collection<RunResultsDetailDTO> runResultsDetail) {
		this.runResultsDetail = runResultsDetail;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getTotalSamplecount() {
		return totalSamplecount;
	}

	public void setTotalSamplecount(String totalSamplecount) {
		this.totalSamplecount = totalSamplecount;
	}

	public Collection<InputStripDetailsDTO> getInputcontainerIds() {
		return inputcontainerIds;
	}

	public void setInputcontainerIds(Collection<InputStripDetailsDTO> inputcontainerIds) {
		this.inputcontainerIds = inputcontainerIds;
	}

	public Collection<OutputStripDetailsDTO> getOutputcontainerIds() {
		return outputcontainerIds;
	}

	public void setOutputcontainerIds(Collection<OutputStripDetailsDTO> outputcontainerIds) {
		this.outputcontainerIds = outputcontainerIds;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getWfmsflag() {
		return wfmsflag;
	}

	public void setWfmsflag(String wfmsflag) {
		this.wfmsflag = wfmsflag;
	}

	public Collection<UniqueReagentsAndConsumables> getUniqueReagentsAndConsumables() {
		return uniqueReagentsAndConsumables;
	}

	public void setUniqueReagentsAndConsumables(Collection<UniqueReagentsAndConsumables> uniqueReagentsAndConsumables) {
		this.uniqueReagentsAndConsumables = uniqueReagentsAndConsumables;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (runResultId ^ (runResultId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RunResultsDTO other = (RunResultsDTO) obj;
		return runResultId == other.runResultId;
	}

	public Integer getTotalSampleFlagCount() {
		return totalSampleFlagCount;
	}

	public void setTotalSampleFlagCount(Integer totalSampleFlagCount) {
		this.totalSampleFlagCount = totalSampleFlagCount;
	}

	public Integer getTotalSampleFailedCount() {
		return totalSampleFailedCount;
	}

	public void setTotalSampleFailedCount(Integer totalSampleFailedCount) {
		this.totalSampleFailedCount = totalSampleFailedCount;
	}
}