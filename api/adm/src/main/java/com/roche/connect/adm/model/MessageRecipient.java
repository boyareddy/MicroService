package com.roche.connect.adm.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.util.GMTDateConverter;

@Entity
@Table(name="\"message_recipient\"")
public class MessageRecipient implements Serializable, com.hcl.hmtp.common.server.entity.MultiTenantEntity, AuditableEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "message_recipient_sequence", sequenceName = "message_recipient_sequence")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "message_recipient_sequence")
	@Column(name = "\"id\"")
	private long id;

	@Column(name = "\"role_id\"", nullable = false)
	private Integer roleId;

	@Column(name = "\"type\"", nullable = false)
	private int type;

	
	@Column(name="\"created_by\"")
	private String createdUser;

	@Column(name = "\"created_date_time\"", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private Timestamp createdDateTime;

	@Column(name = "\"updated_by\"")
	private String updatedBy;

	@Column(name = "\"updated_date_time\"", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private Timestamp updatedDateTime;

	
	//bi-directional many-to-one association to MessageTemplate
	@JsonBackReference
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="\"template_id\"", nullable=false)
	private MessageTemplate messageTemplate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setUserId(Integer userId) {
		this.roleId = userId;
	}

	public MessageTemplate getMessageTemplate() {
		return messageTemplate;
	}

	public void setMessageTemplate(MessageTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
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

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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

	@Override
	public String toString() {
		return "MessageRecepient [id=" + id + ", roleId=" + roleId + ", type=" + type + ", createdBy=" + createdUser
				+ ", createdDateTime=" + createdDateTime + ", updatedBy=" + updatedBy + ", updatedDateTime="
				+ updatedDateTime + ", messageTemplate=" + messageTemplate + "]";
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
