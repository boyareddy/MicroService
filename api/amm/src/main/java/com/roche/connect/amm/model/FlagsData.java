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

@Entity
@Table(name = "Flags_Data")
@EntityListeners({ AuditingEntityListener.class })
public class FlagsData implements MultiTenantEntity,AuditableEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "flag_id")
	private long id;
	@Column(name = "assay_type")
	private String assayType;
	@Column(name = "device_type", length = 50)
	private String deviceType;
	@Column(name = "flag_code", length = 50)
	private String flagCode;
	@Column(name = "flag_severity", length = 50)
	private String flagSeverity;
	@Column(name = "active_flag", length = 10)
	private String activeFlag;
	@Column(name = "source")
	private String source;
	@Column(name="sample_Or_RunLevel")
	private  String sampleOrRunLevel;
	@Column(name="process_step_name")
	private String processStepName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assaytype_id")
	private AssayType assay;
	
	@ManyToOne 
	@JoinColumn(name = "COMPANYID") 
	private Company company;
	@Override
	public long getId() {
		return id;
	}
	@Override
	public void setId(long id) {
		this.id=id;
	}
	
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getFlagCode() {
		return flagCode;
	}
	public void setFlagCode(String flagCode) {
		this.flagCode = flagCode;
	}
	public String getFlagSeverity() {
		return flagSeverity;
	}
	public void setFlagSeverity(String flagSeverity) {
		this.flagSeverity = flagSeverity;
	}
	
	
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	

	public String getSampleOrRunLevel() {
		return sampleOrRunLevel;
	}
	public void setSampleOrRunLevel(String sampleOrRunLevel) {
		this.sampleOrRunLevel = sampleOrRunLevel;
	}


	public String getProcessStepName() {
		return processStepName;
	}
	public void setProcessStepName(String processStepName) {
		this.processStepName = processStepName;
	}
	public AssayType getAssay() {
		return assay;
	}
	public void setAssay(AssayType assay) {
		this.assay = assay;
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
	
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(final Company company) {
        this.company = company;
    }
    
    @Override public String getOwnerPropertyName() {
        return "company";
    }
}
