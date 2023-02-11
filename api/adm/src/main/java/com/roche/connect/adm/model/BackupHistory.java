package com.roche.connect.adm.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.MultiTenantEntity;

@Entity
@Table(name = "backup_history")
public class BackupHistory  implements Serializable, MultiTenantEntity, AuditableEntity {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "job_id")
	private String processId;
	
	@Column(name = "backup_type")
	private String backupType;
	
	@Column(name = "interval")
	private String interval;
	
	@Column(name = "destination_folder")
	private String destinationFolder;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "error_reason")
	private String errorReason;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_date_time")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp createdDateTime;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_date_time")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp updatedDateTime;
	
	@ManyToOne 
	@JoinColumn(name = "COMPANYID") 
	private Company company;
		
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getBackupType() {
		return backupType;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setBackupType(String backupType) {
		this.backupType = backupType;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getDestinationFolder() {
		return destinationFolder;
	}

	public void setDestinationFolder(String destinationFolder) {
		this.destinationFolder = destinationFolder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
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


	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public void setId(long paramId) {
		this.id=paramId;
		
	}

	@Override
	public String getOwnerPropertyName() {
		return null;
	}

	@Override
	public String getEditedBy() {
		return null;
	}

	@Override
	public void setEditedBy(String paramEditedBy) {
		// Do nothing
	}

	@Override
	public Date getCreatedDate() {
		return null;
	}

	@Override
	public void setCreatedDate(Date paramCreateDate) {
		// Do nothing
	}

	@Override
	public Date getModifiedDate() {
		return null;
	}

	@Override
	public void setModifiedDate(Date paramModifiedDate) {
		// Do nothing
	}
	
}
