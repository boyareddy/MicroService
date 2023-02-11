/*******************************************************************************
 * 
 * SampleResults.java                  
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.MultiTenantEntity;
import com.hcl.hmtp.common.server.entity.util.GMTDateConverter;

@Entity
@Table(name = "SAMPLE_RESULTS", indexes = { @Index(name = "SAMPLE_SEARCH_INDEX", columnList = "ACCESSIONING_ID") })
@EntityListeners({ AuditingEntityListener.class })
public class SampleResults implements MultiTenantEntity,AuditableEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sample_results_sequence",sequenceName="sample_results_sequence")
	@GeneratedValue(strategy = GenerationType.AUTO,generator="sample_results_sequence")
	@Column(name="SAMPLE_RESULTS_ID")
	private long id;
	
	@Column(name="ACCESSIONING_ID")
	private String accesssioningId;
	
	@Column(name="ORDER_ID")
	private long orderId;
	
	@Column(name="INPUT_CONTAINER_ID")
	private String inputContainerId;
	
	@Column(name="INPUT_CONTAINER_POSITION")
	private String inputContainerPosition;
	
	@Column(name="INPUT_KIT_ID")
	private String inputKitId;
	
	@Column(name="INPUT_CONTAINER_TYPE")
	private String inputContainerType;
	
	@Column(name="OUTPUT_CONTAINER_ID")
	private String outputContainerId;

    @Column(name="OUTPUT_CONTAINER_POSITION")
	private String outputContainerPosition;
	
	@Column(name="OUTPUT_KIT_ID")
	private String outputKitId;
	
	@Column(name="OUTPUT_CONTAINER_TYPE")
	private String outputContainerType;
	
	@Column(name="OUTPUT_PLATE_TYPE")
	private String outputPlateType;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="RESULT")
	private String result;
	
	@Column(name="COMMENTS",columnDefinition="varchar")
	private String comments;
	
	@Column(name="FLAG")
	private String flag;
	
	@Column(name="RECIEVED_DATE", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp receivedDate;
	
	@Column(name="VERIFIED_DATE", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp verifiedDate;
	
	@Column(name="VERIFIED_BY")
	private String verifiedBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@Column(name="UPDATED_DATE_TIME", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp updatedDateTime; 
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="RUN_RESULTS_ID")
	private RunResults runResultsId;
	@JsonManagedReference
	@OneToMany(mappedBy="sampleResultsMappedToReagents",fetch = FetchType.LAZY)
	private Collection<SampleReagentsAndConsumables> sampleReagentsAndConsumables = new ArrayList<>();
	@JsonManagedReference
	@OneToMany(mappedBy="sampleResultsMappedToSampleProtocol",fetch = FetchType.LAZY)
	private Collection<SampleProtocol> sampleProtocol = new ArrayList<>();
	@JsonManagedReference
	@OneToMany(mappedBy="sampleResultsMappedToSampleDetail",fetch = FetchType.LAZY)
	private Collection<SampleResultsDetail> sampleResultsDetail = new ArrayList<>();
	
	@Column(name="SAMPLE_TYPE")
	private String sampleType;
	
	@Override
	public long getId() {
		return id;
	}
	@Override
	public void setId(long id) {
		this.id = id;
	}
	public RunResults getRunResultsId() {
		return runResultsId;
	}
	public void setRunResultsId(RunResults runResultsId) {
		this.runResultsId = runResultsId;
	}
	public String getAccesssioningId() {
		return accesssioningId;
	}
	public void setAccesssioningId(String accesssioningId) {
		this.accesssioningId = accesssioningId;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getInputContainerId() {
		return inputContainerId;
	}
	public void setInputContainerId(String inputContainerId) {
		this.inputContainerId = inputContainerId;
	}
	public String getOutputContainerId() {
		return outputContainerId;
	}
	public void setOutputContainerId(String outputContainerId) {
		this.outputContainerId = outputContainerId;
	}
	public Timestamp getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
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
	public String getInputContainerPosition() {
		return inputContainerPosition;
	}
	public void setInputContainerPosition(String inputContainerPosition) {
		this.inputContainerPosition = inputContainerPosition;
	}
	public String getInputKitId() {
		return inputKitId;
	}
	public void setInputKitId(String inputKitId) {
		this.inputKitId = inputKitId;
	}
	public String getOutputContainerPosition() {
		return outputContainerPosition;
	}
	public void setOutputContainerPosition(String outputContainerPosition) {
		this.outputContainerPosition = outputContainerPosition;
	}
	public String getOutputKitId() {
		return outputKitId;
	}
	public void setOutputKitId(String outputKitId) {
		this.outputKitId = outputKitId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public Collection<SampleReagentsAndConsumables> getSampleReagentsAndConsumables() {
		return sampleReagentsAndConsumables;
	}
	public void setSampleReagentsAndConsumables(Collection<SampleReagentsAndConsumables> sampleReagentsAndConsumables) {
		this.sampleReagentsAndConsumables = sampleReagentsAndConsumables;
	}
	public Collection<SampleProtocol> getSampleProtocol() {
		return sampleProtocol;
	}
	public void setSampleProtocol(Collection<SampleProtocol> sampleProtocol) {
		this.sampleProtocol = sampleProtocol;
	}
	public Collection<SampleResultsDetail> getSampleResultsDetail() {
		return sampleResultsDetail;
	}
	public void setSampleResultsDetail(Collection<SampleResultsDetail> sampleResultsDetail) {
		this.sampleResultsDetail = sampleResultsDetail;
	}	
	public String getInputContainerType() {
		return inputContainerType;
	}
	public void setInputContainerType(String inputContainerType) {
		this.inputContainerType = inputContainerType;
	}
	public String getOutputContainerType() {
		return outputContainerType;
	}
	public void setOutputContainerType(String outputContainerType) {
		this.outputContainerType = outputContainerType;
	}
	public String getOutputPlateType() {
		return outputPlateType;
	}
	public void setOutputPlateType(String outputPlateType) {
		this.outputPlateType = outputPlateType;
	}
	
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
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