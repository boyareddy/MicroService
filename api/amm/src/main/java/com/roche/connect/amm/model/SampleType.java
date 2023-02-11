 /*******************************************************************************
 *  * File Name: SampleType.java            
 *  * Version:  1.0
 *  * 
 *  * Authors: Dasari Ravindra
 *  * 
 *  * =========================================
 *  * 
 *  * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  * All Rights Reserved.
 *  * 
 *  * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 *  * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 *  * 
 *  * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 *  * 
 *  * =========================================
 *  * 
 *  * ChangeLog:
 *  ******************************************************************************/
package com.roche.connect.amm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

/**
 * The Class SampleType.
 */
@Entity
@Table(name = "SAMPLE_TYPE")
@EntityListeners({ AuditingEntityListener.class })
public class SampleType implements MultiTenantEntity,AuditableEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue
	@Column(name = "sampletype_id")
	private long id;

	/** The sample type name. */
	@Column(name = "sample_type",length=50)
	private String sampleTypeName;
	
	/** The assay type. */
	@Column(name = "assay_type",length=50)
	private String assayType;
	
	/** The max age days. */
	@Column(name="max_age_days")
	private int maxAgeDays;
	
	/** The active flag. */
	@Column(name="active_flag",length=10)
	private String activeFlag;

	/** The assay. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assaytype_id")
	private AssayType assay;
	
	/**
	 * Gets the assay type name.
	 *
	 * @return the assay type name
	 */
	public String getAssayType() {
		return assayType;
	}

	/**
	 * Sets the assay type name.
	 *
	 * @param assayType
	 *            the new assay type
	 */
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Override
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the sample type name.
	 *
	 * @return the sample type name
	 */
	public String getSampleTypeName() {
		return sampleTypeName;
	}

	/**
	 * Sets the sample type name.
	 *
	 * @param sampleTypeName
	 *            the new sample type name
	 */
	public void setSampleTypeName(String sampleTypeName) {
		this.sampleTypeName = sampleTypeName;
	}

	
	/**
	 * Gets the max age days.
	 *
	 * @return the max age days
	 */
	public int getMaxAgeDays() {
		return maxAgeDays;
	}

	/**
	 * Sets the max age days.
	 *
	 * @param maxAgeDays
	 *            the new max age days
	 */
	public void setMaxAgeDays(int maxAgeDays) {
		this.maxAgeDays = maxAgeDays;
	}

	/**
	 * Gets the active flag.
	 *
	 * @return the active flag
	 */
	public String getActiveFlag() {
		return activeFlag;
	}

	/**
	 * Sets the active flag.
	 *
	 * @param activeFlag
	 *            the new active flag
	 */
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	/**
	 * Gets the assay.
	 *
	 * @return the assay
	 */
	public AssayType getAssay() {
		return assay;
	}

	/**
	 * Sets the assay.
	 *
	 * @param assay
	 *            the new assay
	 */
	public void setAssay(AssayType assay) {
		this.assay = assay;
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

    @Column(name = "CREATEDATE") @Convert(converter = GMTDateConverter.class) @CreatedDate private Date createDate;
    @Override public Date getCreatedDate() {
        return createDate;
    }

    @Override public void setCreatedDate(final Date paramCreatedDateParam) {
       createDate = paramCreatedDateParam;
    }
    
    @Column(name = "MODIFIEDDATE") @Convert(converter = GMTDateConverter.class) @LastModifiedDate private Date modifiedDate;
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
