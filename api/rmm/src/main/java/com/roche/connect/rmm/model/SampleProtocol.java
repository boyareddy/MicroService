/*******************************************************************************
 * 
 * SampleProtocol.java                  
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.MultiTenantEntity;
import com.hcl.hmtp.common.server.entity.util.GMTDateConverter;

@Entity
@Table(name = "SAMPLE_PROTOCOL")
@EntityListeners({ AuditingEntityListener.class })
public class SampleProtocol implements MultiTenantEntity, AuditableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sample_protocol_sequence", sequenceName = "sample_protocol_sequence")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sample_protocol_sequence")
	@Column(name = "SAMPLE_PROTOCOL_ID")
	private long id;

	@Column(name = "PROTOCOL_NAME")
	private String protocolName;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DATE_TIME", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private Timestamp updatedDateTime;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SAMPLE_RESULTS_ID")
	private SampleResults sampleResultsMappedToSampleProtocol;

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
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

	public SampleResults getSampleResultsMappedToSampleProtocol() {
		return sampleResultsMappedToSampleProtocol;
	}

	public void setSampleResultsMappedToSampleProtocol(SampleResults sampleResultsMappedToSampleProtocol) {
		this.sampleResultsMappedToSampleProtocol = sampleResultsMappedToSampleProtocol;
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

	@Override
	public String getOwnerPropertyName() {
		return "company";
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

	@Column(name = "CREATEDATE", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Convert(converter = GMTDateConverter.class)
	@CreatedDate
	private Date createDate;

	@Override
	public Date getCreatedDate() {
		return createDate;
	}

	@Override
	public void setCreatedDate(final Date paramCreatedDateParam) {
		createDate = paramCreatedDateParam;
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
}
