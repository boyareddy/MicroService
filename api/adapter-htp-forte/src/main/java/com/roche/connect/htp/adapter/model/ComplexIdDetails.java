package com.roche.connect.htp.adapter.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.MultiTenantEntity;

@Entity
@Table(name = "COMPLEX_ID_DETAILS")
public class ComplexIdDetails implements MultiTenantEntity {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, unique = true)
	private long id;

	@Column(name = "COMPLEX_ID", nullable = false)
	private String complexId;

	@Column(name = "DEVICE_RUN_ID")
	private String deviceRunId;

	@Column(name = "DEVICE_ID")
	private String deviceId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE_TIME")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp createdDateTime;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DATE_TIME")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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

	public String getComplexId() {
		return complexId;
	}

	public void setComplexId(String complexId) {
		this.complexId = complexId;
	}

	public String getDeviceRunId() {
		return deviceRunId;
	}

	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
