/*******************************************************************************
 * File Name: Patient.java            
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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "PATIENT")
@EntityListeners({ AuditingEntityListener.class })
@JsonIgnoreProperties({ "order", "hibernateLazyInitializer", "handler" })
public class Patient implements MultiTenantEntity,AuditableEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "patient_id_sequence", sequenceName = "patient_id_sequence")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "patient_id_sequence")
	@Column(name = "PATIENT_ID")
	private long id;
	@Column(name = "PATIENT_FIRST_NAME")
	private String patientFirstName;
	@Column(name = "PATIENT_LAST_NAME")
	private String patientLastName;
	/**@Column(name = "PATIENT_GENDER")
	private String patientGender;
	@Column(name = "PATIENT_CONTACT_NUMBER")
	private String patientContactNo;
	@Column(name = "TREATING_DOCTOR_NAME")
	private String treatingDoctorName;
	@Column(name = "TREATING_DOCTOR_CONTACT_NO")
	private String treatingDoctorContactNo;
	@Column(name = "REF_CLINICIAN_FAX")
	private String refClinicianFaxNo;
	@Column(name = "OTHER_CLINICIAN_FAX")
	private String otherClinicianFaxNo;*/
	
	@Column(name = "PATIENT_MEDICAL_REC_NO")
	private String patientMedicalRecNo;
	@Column(name = "PATIENT_DOB")
	private String patientDOB;
	@Column(name = "REF_CLINICIAN_NAME")
	private String refClinicianName;
	@Column(name = "CLINIC_NAME")
	private String clinicName;
	@Column(name = "OTHER_CLINICIAN_NAME")
	private String otherClinicianName;
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;
	@Column(name = "LAB_ID")
	private String labId;
	@Column(name = "REASON_FOR_REFERRAL")
	private String reasonForReferral;
	@Column(name = "ACTIVE_FLAG", length = 10)
	private String activeFlag;
	@Column(name = "CREATED_BY")
	private String createdABy;
	@Column(name = "CREATED_DATE_TIME", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp createdDateTime;
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	@Column(name = "UPDATED_DATE_TIME", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp updatedDateTime;

	@OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
	private Collection<PatientSamples> patientSamples = new ArrayList<>();

	@OneToMany(mappedBy = "patient")
	private Collection<Order> order = new ArrayList<>();

	@OneToOne(mappedBy = "patient", fetch = FetchType.LAZY)
	private PatientAssay petientAssay;
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getLabId() {
		return labId;
	}

	public void setLabId(String labId) {
		this.labId = labId;
	}

	public String getReasonForReferral() {
		return reasonForReferral;
	}

	public void setReasonForReferral(String reasonForReferral) {
		this.reasonForReferral = reasonForReferral;
	}

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}

	public Collection<Order> getOrder() {
		return order;
	}

	public void setOrder(Collection<Order> order) {
		this.order = order;
	}

	public PatientAssay getPetientAssay() {
		return petientAssay;
	}

	public void setPetientAssay(PatientAssay petientAssay) {
		this.petientAssay = petientAssay;
	}

	public Collection<PatientSamples> getPatientSamples() {
		return patientSamples;
	}

	public void setPatientSamples(Collection<PatientSamples> patientSamples) {
		this.patientSamples = patientSamples;
	}

	public void addPatientSample(PatientSamples patientSample) {
		this.patientSamples.add(patientSample);
		patientSample.setPatient(this);
	}

	public void deletePatientSample(PatientSamples patientSample) {
		this.patientSamples.remove(patientSample);
		patientSample.setPatient(this);
	}

	public String getPatientFirstName() {
		return patientFirstName;
	}

	public void setPatientFirstName(String patientFirstName) {
		this.patientFirstName = patientFirstName;
	}

	public String getPatientLastName() {
		return patientLastName;
	}

	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}

	public String getPatientMedicalRecNo() {
		return patientMedicalRecNo;
	}

	public void setPatientMedicalRecNo(String patientMedicalRecNo) {
		this.patientMedicalRecNo = patientMedicalRecNo;
	}

	public String getPatientDOB() {
		return patientDOB;
	}

	public void setPatientDOB(String patientDOB) {
		this.patientDOB = patientDOB;
	}

	public String getRefClinicianName() {
		return refClinicianName;
	}

	public void setRefClinicianName(String refClinicianName) {
		this.refClinicianName = refClinicianName;
	}
	
	public String getOtherClinicianName() {
		return otherClinicianName;
	}

	public void setOtherClinicianName(String otherClinicianName) {
		this.otherClinicianName = otherClinicianName;
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
