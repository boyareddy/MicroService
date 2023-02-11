package com.roche.connect.forte.adapter.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "FORTE_JOB")
public class ForteJob implements Serializable{
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
			@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "ID", nullable = false, unique = true)
	private UUID id;
	
	@Column(name = "DEVICE_RUN_ID")
	private String deviceRunId;
	
	@Column(name = "CYCLE_ID")
	private UUID cycleId;
	
	@Column(name = "OUT_FILE")
	private String outFilePath;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDeviceRunId() {
		return deviceRunId;
	}

	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}

	public UUID getCycleId() {
		return cycleId;
	}

	public void setCycleId(UUID cycleId) {
		this.cycleId = cycleId;
	}

	public String getOutFilePath() {
		return outFilePath;
	}

	public void setOutFilePath(String outFilePath) {
		this.outFilePath = outFilePath;
	}

	public String getOutFileChecksum() {
		return outFileChecksum;
	}

	public void setOutFileChecksum(String outFileChecksum) {
		this.outFileChecksum = outFileChecksum;
	}

	public String getQc() {
		return qc;
	}

	public void setQc(String qc) {
		this.qc = qc;
	}

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getEstimatedTimeToCompletion() {
		return estimatedTimeToCompletion;
	}

	public void setEstimatedTimeToCompletion(String estimatedTimeToCompletion) {
		this.estimatedTimeToCompletion = estimatedTimeToCompletion;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getSentToTertiary() {
		return sentToTertiary;
	}

	public void setSentToTertiary(String sentToTertiary) {
		this.sentToTertiary = sentToTertiary;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDateandTime() {
		return createdDateandTime;
	}

	public void setCreatedDateandTime(Timestamp createdDateandTime) {
		this.createdDateandTime = createdDateandTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDateandTime() {
		return updatedDateandTime;
	}

	public void setUpdatedDateandTime(Timestamp updatedDateandTime) {
		this.updatedDateandTime = updatedDateandTime;
	}

	@Column(name = "OUT_FILE_CHECKSUM")
	private String outFileChecksum;
	
	@Column(name = "QC")
	private String qc;
	
	@Column(name = "ERROR_KEY")
	private String errorKey;
	
	@Column(name = "ESTIMATED_TIME_TO_COMPLETION")
	private String estimatedTimeToCompletion;
	
	@Column(name = "JOB_STATUS")
	private String jobStatus;
	
	@Column(name = "JOB_TYPE")
	private String jobType;
	
	@Column(name = "SENT_TO_TERTIARY")
	private String sentToTertiary;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE_TIME")
	private Timestamp createdDateandTime;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name = "UPDATED_DATE_TIME")
	private Timestamp updatedDateandTime;
	
	
	
	

}
