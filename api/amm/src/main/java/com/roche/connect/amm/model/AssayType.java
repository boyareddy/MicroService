 /*******************************************************************************
 *  * File Name: AssayType.java            
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
 * The Class AssayType.
 */
@Entity
@Table(name = "ASSAY_TYPE")
@EntityListeners({ AuditingEntityListener.class })
public class AssayType implements MultiTenantEntity,AuditableEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue
	@Column(name = "assaytype_id")
	private long id;

	/** The assay type name. */
	@Column(name = "assay_type", length = 50)
	private String assayTypeName;

	/** The assay version. */
	@Column(name = "assay_version", length = 20)
	private String assayVersion;

	/** The workflow def file. */
	@Column(name = "workflow_def_file", length = 200)
	private String workflowDefFile;

	/** The workflow def version. */
	@Column(name = "workflow_file_version", length = 20)
	private String workflowDefVersion;

	/** The active flag. */
	@Column(name = "active_flag", length = 10)
	private String activeFlag;

	/** The samples. */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "assay")
	private Set<SampleType> samples = new HashSet<>();

	/** The process step actions. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "assay")
	private List<ProcessStepAction> processStepActions = new ArrayList<>();

	/** The test options. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "assay")
	private List<TestOptions> testOptions = new ArrayList<>();

	/** The assay input validations. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "assay")
	private List<AssayInputDataValidations> assayInputValidations = new ArrayList<>();

	/** The assay list data. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "assay")
	private List<AssayListData> assayListData = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "assay")
	private List<FlagsData> flagslistData=new ArrayList<>();

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
	 * Gets the assay type name.
	 *
	 * @return the assay type name
	 */
	public String getAssayTypeName() {
		return assayTypeName;
	}

	/**
	 * Sets the assay type name.
	 *
	 * @param assayTypeName
	 *            the new assay type name
	 */
	public void setAssayTypeName(String assayTypeName) {
		this.assayTypeName = assayTypeName;
	}

	/**
	 * Gets the assay version.
	 *
	 * @return the assay version
	 */
	public String getAssayVersion() {
		return assayVersion;
	}

	/**
	 * Sets the assay version.
	 *
	 * @param assayVersion
	 *            the new assay version
	 */
	public void setAssayVersion(String assayVersion) {
		this.assayVersion = assayVersion;
	}

	/**
	 * Gets the workflow def file.
	 *
	 * @return the workflow def file
	 */
	public String getWorkflowDefFile() {
		return workflowDefFile;
	}

	/**
	 * Sets the workflow def file.
	 *
	 * @param workflowDefFile
	 *            the new workflow def file
	 */
	public void setWorkflowDefFile(String workflowDefFile) {
		this.workflowDefFile = workflowDefFile;
	}

	/**
	 * Gets the workflow def version.
	 *
	 * @return the workflow def version
	 */
	public String getWorkflowDefVersion() {
		return workflowDefVersion;
	}

	/**
	 * Sets the workflow def version.
	 *
	 * @param workflowDefVersion
	 *            the new workflow def version
	 */
	public void setWorkflowDefVersion(String workflowDefVersion) {
		this.workflowDefVersion = workflowDefVersion;
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
	 * Gets the samples.
	 *
	 * @return the samples
	 */
	public Set<SampleType> getSamples() {
		return samples;
	}

	/**
	 * Sets the samples.
	 *
	 * @param samples
	 *            the new samples
	 */
	public void setSamples(Set<SampleType> samples) {
		this.samples = samples;
	}

	/**
	 * Gets the process step actions.
	 *
	 * @return the process step actions
	 */
	public List<ProcessStepAction> getProcessStepActions() {
		return processStepActions;
	}

	/**
	 * Sets the process step actions.
	 *
	 * @param processStepActions
	 *            the new process step actions
	 */
	public void setProcessStepActions(List<ProcessStepAction> processStepActions) {
		this.processStepActions = processStepActions;
	}

	/**
	 * Gets the assay input validations.
	 *
	 * @return the assay input validations
	 */
	public List<AssayInputDataValidations> getAssayInputValidations() {
		return assayInputValidations;
	}

	/**
	 * Sets the assay input validations.
	 *
	 * @param assayInputValidations
	 *            the new assay input validations
	 */
	public void setAssayInputValidations(List<AssayInputDataValidations> assayInputValidations) {
		this.assayInputValidations = assayInputValidations;
	}

	/**
	 * Gets the assay list data.
	 *
	 * @return the assay list data
	 */
	public List<AssayListData> getAssayListData() {
		return assayListData;
	}

	/**
	 * Sets the assay list data.
	 *
	 * @param assayListData
	 *            the new assay list data
	 */
	public void setAssayListData(List<AssayListData> assayListData) {
		this.assayListData = assayListData;
	}

	/**
	 * Gets the test options.
	 *
	 * @return the test options
	 */
	public List<TestOptions> getTestOptions() {
		return testOptions;
	}

	/**
	 * Sets the test options.
	 *
	 * @param testOptions
	 *            the new test options
	 */
	public void setTestOptions(List<TestOptions> testOptions) {
		this.testOptions = testOptions;
	}
	
	public List<FlagsData> getFlagslistData() {
		return flagslistData;
	}

	public void setFlagslistData(List<FlagsData> flagslistData) {
		this.flagslistData = flagslistData;
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
