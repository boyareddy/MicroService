package com.roche.connect.htp.adapter.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.MultiTenantEntity;

@Entity
@Table(name = "FORTE_JOB")
public class ForteJob implements MultiTenantEntity {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, unique = true)
	private long id;

	@Column(name = "DEVICE_RUN_ID")
	private String deviceRunId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CYCLE_ID")
	private Cycle cycleId;

	@Column(name = "OUT_FILE")
	private String outFilePath;

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
	private Timestamp createdDateTime;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DATE_TIME")
	private Timestamp updatedDateTime;
	
	@ManyToOne 
	@JoinColumn(name = "COMPANYID") 
	private Company company;

	@PrePersist
	protected void onCreate() {
		createdDateTime = new Timestamp(new Date().getTime());
	}

	@PreUpdate
	protected void onUpdate() {
		updatedDateTime = new Timestamp(new Date().getTime());
	}

	public Cycle getCycleId() {
		return cycleId;
	}

	public void setCycleId(Cycle cycleId) {
		this.cycleId = cycleId;
	}

	public String getDeviceRunId() {
		return deviceRunId;
	}

	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
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
		return createdDateTime;
	}

	public void setCreatedDateandTime(Timestamp createdDateandTime) {
		this.createdDateTime = createdDateandTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDateandTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateandTime(Timestamp updatedDateandTime) {
		this.updatedDateTime = updatedDateandTime;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getOwnerPropertyName() {
		return "company";
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	

}
