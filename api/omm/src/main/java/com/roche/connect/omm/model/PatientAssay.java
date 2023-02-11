/*******************************************************************************
 * File Name: PatientAssay.java            
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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.MultiTenantEntity;
import com.hcl.hmtp.common.server.entity.util.GMTDateConverter;

@Entity
@Table(name = "PATIENT_ASSAY")
@JsonIgnoreProperties("patient")
@EntityListeners({ AuditingEntityListener.class })
public class PatientAssay implements MultiTenantEntity,AuditableEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "patient_assay_id_sequence", sequenceName = "patient_assay_id_sequence")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "patient_assay_id_sequence")
	@Column(name = "PATIENT_ASSAY_ID")
	private long id;

	@Column(name = "MATERNAL_AGE")
	private Integer maternalAge;

	@Column(name = "GESTATIONAL_AGE_WEEKS")
	private Integer gestationalAgeWeeks;

	@Column(name = "GESTATIONAL_AGE_DAYS")
	private Integer gestationalAgeDays;

	@Column(name = "IVF_STATUS", length = 50)
	private String ivfStatus;

	@Column(name = "EGG_DONOR", length = 50)
	private String eggDonor;
	
	
	@Column(name = "EGG_DONOR_AGE")
	private Integer eggDonorAge;

	@Column(name = "FETUS_NUMBER", length = 50)
	private String fetusNumber;

	@Column(name = "COLLECTION_DATE", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp collectionDate;

	@Column(name = "RECIEVED_DATE", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp recievedDate;

	@Column(name = "ACTIVE_FLAG", length = 10)
	private String activeFlag;

	@Column(name = "CREATED_BY", length = 50)
	private String createdABy;

	@Column(name = "CREATED_DATE_TIME", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp createdDateTime;

	@Column(name = "UPDATED_BY", length = 50)
	private String updatedBy;

	@Column(name = "UPDATED_DATE_TIME",columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp updatedDateTime;

	@OneToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;

	public Integer getMaternalAge() {
		return maternalAge;
	}

	public void setMaternalAge(Integer maternalAge) {
		this.maternalAge = maternalAge;
	}

	public Integer getGestationalAgeWeeks() {
		return gestationalAgeWeeks;
	}

	public void setGestationalAgeWeeks(Integer gestationalAgeWeeks) {
		this.gestationalAgeWeeks = gestationalAgeWeeks;
	}

	public Integer getGestationalAgeDays() {
		return gestationalAgeDays;
	}

	public void setGestationalAgeDays(Integer gestationalAgeDays) {
		this.gestationalAgeDays = gestationalAgeDays;
	}

	public String getEggDonor() {
		return eggDonor;
	}

	public void setEggDonor(String eggDonor) {
		this.eggDonor = eggDonor;
	}

	public Integer getEggDonorAge() {
		return eggDonorAge;
	}

	public void setEggDonorAge(Integer eggDonorAge) {
		this.eggDonorAge = eggDonorAge;
	}

	public String getFetusNumber() {
		return fetusNumber;
	}

	public void setFetusNumber(String fetusNumber) {
		this.fetusNumber = fetusNumber;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getIvfStatus() {
		return ivfStatus;
	}

	public void setIvfStatus(String ivfStatus) {
		this.ivfStatus = ivfStatus;
	}

	public Timestamp getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Timestamp collectionDate) {
		this.collectionDate = collectionDate;
	}

	public Timestamp getRecievedDate() {
		return recievedDate;
	}

	public void setRecievedDate(Timestamp recievedDate) {
		this.recievedDate = recievedDate;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getCreatedABy() {
		return createdABy;
	}

	public void setCreatedABy(String createdABy) {
		this.createdABy = createdABy;
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

	@Override
	public long getId() {
		return id;
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
}
