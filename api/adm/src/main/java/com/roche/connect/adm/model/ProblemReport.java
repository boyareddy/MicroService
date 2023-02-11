package com.roche.connect.adm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.util.GMTDateConverter;

@Entity
@Table(name = "Problem_Report")
public class ProblemReport implements com.hcl.hmtp.common.server.entity.MultiTenantEntity, AuditableEntity {

	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private long id;

	/** The StartDate. */
	@Column(name = "StartDate")
	private String startDate;

	/** The EndDate. */
	@Column(name = "EndDate")
	private String endDate;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "ProblemReportPath")
	private String problemReportPath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getProblemReportPath() {
		return problemReportPath;
	}

	public void setProblemReportPath(String problemReportPath) {
		this.problemReportPath = problemReportPath;
	}

	

	@Column(name = "EDITEDBY")
	@LastModifiedBy
	private String editedBy;

	@Override
	public String getEditedBy() {
		return editedBy;
	}

	@Override
	public void setEditedBy(final String paramEditedByParam) {
		editedBy = paramEditedByParam;
	}

	@Column(name = "CREATEDATE")
	@Convert(converter = GMTDateConverter.class)
	@CreatedDate
	private Date createDate;

	@Override
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Override
	public void setCreatedDate(final Date paramCreatedDateParam) {
		this.createdDate = paramCreatedDateParam;
	}

	@Column(name = "MODIFIEDDATE")
	@Convert(converter = GMTDateConverter.class)
	@LastModifiedDate
	private Date modifiedDate;

	@Override
	public Date getModifiedDate() {
		return modifiedDate;
	}

	@Override
	public void setModifiedDate(final Date paramModifiedDateParam) {
		modifiedDate = paramModifiedDateParam;
	}

	@Column(name = "CREATEDBY")
	@CreatedBy
	private String createdBy;

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(String paramCreatedBy) {
		createdBy = paramCreatedBy;
	}

	@Override
	public String getOwnerPropertyName() {
		return null;
	}

}
