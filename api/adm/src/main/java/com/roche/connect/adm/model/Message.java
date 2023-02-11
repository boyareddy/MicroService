/*******************************************************************************
 * 
 * Message.java                  
 *  Version:  1.0
 * 
 * Authors: dineshj
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

package com.roche.connect.adm.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.util.GMTDateConverter;

@Entity
@Table(name="\"message\"")
public class Message implements Serializable, com.hcl.hmtp.common.server.entity.MultiTenantEntity, AuditableEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "message_sequence",sequenceName="message_sequence")	
	@GeneratedValue(strategy=GenerationType.AUTO,generator="message_sequence")
	@Column(name="\"message_id\"")
	private long id;

	@Column(name="\"created_by\"", nullable=false)
	private String createdUser;

	@Column(name="\"created_date_time\"", nullable=false, columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp createdDateTime;
	
	@Column(name="\"title\"", nullable=false, length=1000)
	private String title;

	@Column(name="\"description\"", nullable=false, length=10000)
	private String description;

	@Column(name="\"viewed\"", nullable=false)
	private String viewed;

	@Column(name="\"viewed_date_time\"", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp viewedDateTime;
	
	@Column(name="\"severity\"", nullable=false)
	private String severity;
	
	@Column(name="\"module\"", nullable=false)
	private String module;

	//bi-directional many-to-one association to MessageTemplate
	@JsonBackReference
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="\"template_id\"", nullable=false)
	private MessageTemplate messageTemplate;
	
	//bi-directional many-to-one association to Message
	@JsonManagedReference
	@OneToMany(mappedBy="message", fetch=FetchType.LAZY)
	private List<Recipient> recipient;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String tile) {
		this.title = tile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String message) {
		this.description = message;
	}

	public String getViewed() {
		return viewed;
	}

	public void setViewed(String viewed) {
		this.viewed = viewed;
	}

	public Timestamp getViewedDateTime() {
		return viewedDateTime;
	}

	public void setViewedDateTime(Timestamp viewedDateTime) {
		this.viewedDateTime = viewedDateTime;
	}

	public MessageTemplate getMessageTemplate() {
		return messageTemplate;
	}

	public void setMessageTemplate(MessageTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
	}
	
	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String topic) {
		this.module = topic;
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

	public List<Recipient> getRecipient() {
		return recipient;
	}

	public void setRecipient(List<Recipient> recipient) {
		this.recipient = recipient;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", createdUser=" + createdUser + ", createdDateTime=" + createdDateTime + ", title="
				+ title + ", description=" + description + ", viewed=" + viewed + ", viewedDateTime=" + viewedDateTime
				+ ", severity=" + severity + ", module=" + module + ", messageTemplate=" + messageTemplate
				+ ", recipient=" + recipient + ", company=" + company + "]";
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
