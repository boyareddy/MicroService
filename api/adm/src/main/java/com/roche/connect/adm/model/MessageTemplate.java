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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hcl.hmtp.common.server.entity.AuditableEntity;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.util.GMTDateConverter;



@Entity
@Table(name="\"message_template\"")
public class MessageTemplate implements Serializable, com.hcl.hmtp.common.server.entity.MultiTenantEntity, AuditableEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "message_template_sequence",sequenceName="message_template_sequence")	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="message_template_sequence")
	@Column(name="\"template_id\"")
	private long id;

	@Column(name="\"active_flag\"", nullable=false)
	private String activeFlag;

	@Column(name="\"channel\"", nullable=false)
	private String channel;
	

	@Column(name="\"title\"", nullable=false, length=1000)
	private String title;
	
	@Column(name="\"description\"", nullable=false, length=10000)
	private String description;

	@Column(name="\"severity\"")
	private String severity;
	
	@Column(name="\"priority\"")
	private Integer priority;
	
	@Column(name="\"locale\"", nullable=false)
	private String locale;
	
	@Column(name="\"message_group\"", nullable=false)
	private String messageGroup;
	
	@Column(name="\"module\"", nullable=false)
	private String module;

	//bi-directional many-to-one association to Message
	@JsonManagedReference
	@OneToMany(mappedBy="messageTemplate", fetch=FetchType.LAZY)
	private List<Message> messages;
	
	@Column(name="\"created_by\"")
	private String createdUser;

	@Column(name="\"created_date_time\"", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp createdDateTime;
	
	@Column(name="\"updated_by\"")
	private String updatedBy;

	@Column(name="\"updated_date_time\"", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Timestamp updatedDateTime;
	
	//bi-directional many-to-one association to Message
	@JsonManagedReference
	@OneToMany(mappedBy="messageTemplate", fetch=FetchType.LAZY)
	private List<MessageRecipient> messageRecipients;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String notificationChannel) {
		this.channel = notificationChannel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String messageTitle) {
		this.title = messageTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String message) {
		this.description = message;
	}
	
	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getMessageGroup() {
		return messageGroup;
	}

	public void setMessageGroup(String group) {
		this.messageGroup = group;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<MessageRecipient> getMessageRecepients() {
		return messageRecipients;
	}

	public void setMessageRecepients(List<MessageRecipient> messageRecipients) {
		this.messageRecipients = messageRecipients;
	}
	
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public String getModule() {
		return module;
	}

	public void setModule(String topic) {
		this.module = topic;
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

	@Override
	public String toString() {
		return "MessageTemplate [id=" + id + ", activeFlag=" + activeFlag + ", channel=" + channel + ", title=" + title
				+ ", description=" + description + ", severity=" + severity + ", priority=" + priority + ", locale="
				+ locale + ", messageGroup=" + messageGroup + ", module=" + module + ", messages=" + messages
				+ ", createdBy=" + createdUser + ", createdDateTime=" + createdDateTime + ", updatedBy=" + updatedBy
				+ ", updatedDateTime=" + updatedDateTime + ", messageRecepients=" + messageRecipients + "]";
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
