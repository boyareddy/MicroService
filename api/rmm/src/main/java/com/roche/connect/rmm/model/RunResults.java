/*******************************************************************************
 * 
 * RunResults.java                  
 *  Version:  1.0
 * 
 * Authors: chelurubhaskar.r
 * 
 * ==================================
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *  ==================================
 * ChangeLog:
 * 
 * 
 ******************************************************************************/
package com.roche.connect.rmm.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.MultiTenantEntity;
import com.hcl.hmtp.common.server.entity.util.GMTDateConverter;


@Entity
@Table(name = "RUN_RESULTS", indexes = { @Index(name = "RUN_SEARCH_INDEX", columnList = "DEVICE_RUN_ID") })
@EntityListeners({ AuditingEntityListener.class })
public class RunResults implements MultiTenantEntity ,AuditableEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "run_results_sequence",sequenceName="run_results_sequence")
	@GeneratedValue(strategy = GenerationType.AUTO,generator="run_results_sequence")
	@Column(name="RUN_RESULTS_ID")
	private long id;
	
	@Column(name="DEVICE_ID")
	private String deviceId;
	
	@Column(name="PROCESS_STEP_NAME")
	private String processStepName;
	
	@Column(name="DEVICE_RUN_ID")
	private String deviceRunId;
	
	@Column(name="RUN_STATUS")
	private String runStatus;
	
	@Column(name="DVC_RUN_RESULT")
	private String dvcRunResult;
	
	@Column(name="RUN_FLAG")
	private String runFlag;	
	
	@Column(name="OPERATOR_NAME")
	private String operatorName;
	
	@Column(name="COMMENTS",columnDefinition="varchar")
	private String comments;
	
	@Column(name = "ASSAY_TYPE", length = 50)
	private String assayType;
	
	@Column(name="RUN_START_TIME", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp runStartTime;

	@Column(name="RUN_COMPLETION_TIME", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp runCompletionTime;
	
	@Column(name="RUN_REMAINING_TIME")
	private Long runRemainingTime;
	
	@Column(name="VERIFIED_DATE", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp verifiedDate;
	
	@Column(name="VERIFIED_BY")
	private String verifiedBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@Column(name="UPDATED_DATE_TIME",nullable=false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp updatedDateTime; 
	@JsonManagedReference
	@OneToMany(mappedBy="runResultsId",fetch = FetchType.LAZY)
	private Collection<SampleResults> sampleResults = new ArrayList<>();
	@JsonManagedReference
    @OneToMany(mappedBy="runResultsMappedToReagents",fetch = FetchType.LAZY)
	private Collection<RunReagentsAndConsumables> runReagentsAndConsumables = new ArrayList<>();
	@JsonManagedReference
	@OneToMany(mappedBy="runResultsMappedToRunDetails",fetch = FetchType.LAZY)
	private Collection<RunResultsDetail> runResultsDetail = new ArrayList<>();
	
	@Override
	public long getId() {
		return id;
	}
	@Override
	public void setId(long id) {
		this.id = id;
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
	public Collection<SampleResults> getSampleResults() {
		return sampleResults;
	}
	public void setSampleResults(Collection<SampleResults> sampleResults) {
		this.sampleResults = sampleResults;
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
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
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
	public Collection<RunReagentsAndConsumables> getRunReagentsAndConsumables() {
		return runReagentsAndConsumables;
	}
	public void setRunReagentsAndConsumables(Collection<RunReagentsAndConsumables> runReagentsAndConsumables) {
		this.runReagentsAndConsumables = runReagentsAndConsumables;
	}
	public Collection<RunResultsDetail> getRunResultsDetail() {
		return runResultsDetail;
	}
	public void setRunResultsDetail(Collection<RunResultsDetail> runResultsDetail) {
		this.runResultsDetail = runResultsDetail;
	}
	@ManyToOne 
	@JoinColumn(name = "COMPANYID") 
	private Company company;
    
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(final Company company) {
        this.company = company;
    }
    
    @Override public String getOwnerPropertyName() {
        return "company";
    }
    @Column(name = "EDITEDBY") @LastModifiedBy private String editedBy;
    @Override public String getEditedBy() {
        return editedBy;
    }

    @Override public void setEditedBy(final String paramEditedByParam) {
        editedBy = paramEditedByParam;
    }

    @Column(name = "CREATEDATE", columnDefinition= "TIMESTAMP WITH TIME ZONE") @Convert(
            converter = GMTDateConverter.class) @CreatedDate private Date createDate;
    @Override public Date getCreatedDate() {
        return createDate;
    }

    @Override public void setCreatedDate(final Date paramCreatedDateParam) {
       createDate = paramCreatedDateParam;
    }
    @Column(name = "MODIFIEDDATE") @Convert(
            converter = GMTDateConverter.class) @LastModifiedDate private Date modifiedDate;

       @Override public Date getModifiedDate() {
        return modifiedDate;
    }
	    @Override public void setModifiedDate(final Date paramModifiedDateParam) {
        modifiedDate = paramModifiedDateParam;
    }
       @Column(name = "CREATEDBY") @CreatedBy private String createdBy;
			@Override public String getCreatedBy() {
			return createdBy;
	}

			@Override public void setCreatedBy(String paramCreatedBy) {
			createdBy = paramCreatedBy;
	}
}
