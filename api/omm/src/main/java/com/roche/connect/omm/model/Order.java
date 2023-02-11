/*******************************************************************************
 * File Name: Order.java            
 * Version:  1.0
 * 
 * Authors: Ankit Singh
 * 
 * =========================================
 * 
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
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
 * =========================================
 * 
 * ChangeLog:
 ******************************************************************************/
package com.roche.connect.omm.model;

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

import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.MultiTenantEntity;
import com.hcl.hmtp.common.server.entity.util.GMTDateConverter;

@Entity
@Table(name = "ORDERS", indexes = { @Index(name = "ORDER_SEARCH_INDEX", columnList = "ACCESSIONING_ID,ORDER_STATUS") })
@EntityListeners({ AuditingEntityListener.class })
public class Order implements MultiTenantEntity,AuditableEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(initialValue = 10000000, allocationSize = 1, name = "order_id_sequence", sequenceName = "order_id_sequence")
	@GeneratedValue(generator = "order_id_sequence")
	@Column(name = "ORDER_ID")
	private long id;

	@Column(name = "PATIENT_SAMPLE_ID")
	private Long patientSampleId;

	@Column(name = "ACCESSIONING_ID",unique=true)
	private String accessioningId;

	@Column(name = "ORDER_STATUS", length = 50)
	private String orderStatus;

	@Column(name = "ASSAY_TYPE", length = 50)
	private String assayType;

	@Column(name = "ORDER_COMMENTS", length = 255)
	private String orderComments;

	@Column(name = "ACTIVE_FLAG", length = 10)
	private String activeFlag;

	@Column(name = "CREATED_BY", length = 50)
	private String createdABy;

	@Column(name = "CREATED_DATE_TIME", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp createdDateTime;

	@Column(name = "UPDATED_BY", length = 50)
	private String updatedBy;

	@Column(name = "UPDATED_DATE_TIME",nullable=false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp updatedDateTime;

	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	private Collection<TestOptions> listOfTestOptions = new ArrayList<>();
	
	@Column(name="PRIORITY", length=30)
	private String priority;
	
	@Column(name="PRIORITY_UPDATED_TIME", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp priorityUpdatedTime;
	
	@Column(name="req_field_missing_flag")
	private boolean reqFieldMissingFlag;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getCreatedABy() {
		return createdABy;
	}

	public void setCreatedABy(String createdABy) {
		this.createdABy = createdABy;
	}

	public Long getPatientSampleId() {
		return patientSampleId;
	}

	public void setPatientSampleId(Long patientSampleId) {
		this.patientSampleId = patientSampleId;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderComments() {
		return orderComments;
	}

	public void setOrderComments(String orderComments) {
		this.orderComments = orderComments;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public Collection<TestOptions> getListOfTestOptions() {
		return listOfTestOptions;
	}

	public void setListOfTestOptions(Collection<TestOptions> listOfTestOptions) {
		this.listOfTestOptions = listOfTestOptions;
	}

	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
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

    @Column(name = "CREATEDATE") @Convert(
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

	public boolean isReqFieldMissingFlag() {
		return reqFieldMissingFlag;
	}

	public void setReqFieldMissingFlag(boolean reqFieldMissingFlag) {
		this.reqFieldMissingFlag = reqFieldMissingFlag;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Timestamp getPriorityUpdatedTime() {
		return priorityUpdatedTime;
	}

	public void setPriorityUpdatedTime(Timestamp priorityUpdatedTime) {
		this.priorityUpdatedTime = priorityUpdatedTime;
	}
			
}
