/*******************************************************************************
 * HL7HeaderSegmentDTO.java                  
 *  Version:  1.0
 * 
 *  Authors:  Arun Paul Jacob
 * 
 * *********************
 *  Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  All Rights Reserved.
 * 
 *  NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 *  The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *                
 * *********************
 *  ChangeLog:
 * 
 *   arunpaul.j@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.camel.dto;

/**
 * The Class HL7HeaderSegmentDTO.
 */
public class HL7HeaderSegmentDTO {
	
	/** The device serial number. */
	String deviceSerialNumber;
	
	/** The sending application name. */
	String sendingApplicationName;
	
	/** The sending facility. */
	String sendingFacility;
	
	/** The receiving application. */
	String receivingApplication;
	
	/** The receiving facility. */
	String receivingFacility;
	
	/** The date time message generated. */
	String dateTimeMessageGenerated;
	
	/** The message type. */
	String messageType;
	
	/** The message control id. */
	String messageControlId;
	
	/** The processing id. */
	String processingId;
	
	/** The version id. */
	String versionId;
	
	/** The character set. */
	String  characterSet;
	
	/** The sample ID. */
	String  sampleID;
	
	/** The message query name. */
	String  messageQueryName;
	
	/** The query def id. */
	String queryDefId;
	
	/** The query def desc. */
	String queryDefdesc;
	
	/** The query def encoding system. */
	String queryDefEncodingSystem;
	
	/**
	 * Gets the device serial number.
	 *
	 * @return the device serial number
	 */
	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}
	
	/**
	 * Sets the device serial number.
	 *
	 * @param deviceSerialNumber the new device serial number
	 */
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}
	
	/**
	 * Gets the sending application name.
	 *
	 * @return the sending application name
	 */
	public String getSendingApplicationName() {
		return sendingApplicationName;
	}
	
	/**
	 * Sets the sending application name.
	 *
	 * @param sendingApplicationName the new sending application name
	 */
	public void setSendingApplicationName(String sendingApplicationName) {
		this.sendingApplicationName = sendingApplicationName;
	}
	
	/**
	 * Gets the sending facility.
	 *
	 * @return the sending facility
	 */
	public String getSendingFacility() {
		return sendingFacility;
	}
	
	/**
	 * Sets the sending facility.
	 *
	 * @param sendingFacility the new sending facility
	 */
	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}
	
	/**
	 * Gets the receiving application.
	 *
	 * @return the receiving application
	 */
	public String getReceivingApplication() {
		return receivingApplication;
	}
	
	/**
	 * Sets the receiving application.
	 *
	 * @param receivingApplication the new receiving application
	 */
	public void setReceivingApplication(String receivingApplication) {
		this.receivingApplication = receivingApplication;
	}
	
	/**
	 * Gets the receiving facility.
	 *
	 * @return the receiving facility
	 */
	public String getReceivingFacility() {
		return receivingFacility;
	}
	
	/**
	 * Sets the receiving facility.
	 *
	 * @param receivingFacility the new receiving facility
	 */
	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
	}
	
	/**
	 * Gets the date time message generated.
	 *
	 * @return the date time message generated
	 */
	public String getDateTimeMessageGenerated() {
		return dateTimeMessageGenerated;
	}
	
	/**
	 * Sets the date time message generated.
	 *
	 * @param dateTimeMessageGenerated the new date time message generated
	 */
	public void setDateTimeMessageGenerated(String dateTimeMessageGenerated) {
		this.dateTimeMessageGenerated = dateTimeMessageGenerated;
	}
	
	/**
	 * Gets the message type.
	 *
	 * @return the message type
	 */
	public String getMessageType() {
		return messageType;
	}
	
	/**
	 * Sets the message type.
	 *
	 * @param messageType the new message type
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * Gets the message control id.
	 *
	 * @return the message control id
	 */
	public String getMessageControlId() {
        return messageControlId;
    }
    
    /**
     * Sets the message control id.
     *
     * @param messageControlId the new message control id
     */
    public void setMessageControlId(String messageControlId) {
        this.messageControlId = messageControlId;
    }
    
    /**
     * Gets the processing id.
     *
     * @return the processing id
     */
    public String getProcessingId() {
		return processingId;
	}
	
	/**
	 * Sets the processing id.
	 *
	 * @param processingId the new processing id
	 */
	public void setProcessingId(String processingId) {
		this.processingId = processingId;
	}
	
	/**
	 * Gets the version id.
	 *
	 * @return the version id
	 */
	public String getVersionId() {
		return versionId;
	}
	
	/**
	 * Sets the version id.
	 *
	 * @param versionId the new version id
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	/**
	 * Gets the character set.
	 *
	 * @return the character set
	 */
	public String getCharacterSet() {
		return characterSet;
	}
	
	/**
	 * Sets the character set.
	 *
	 * @param characterSet the new character set
	 */
	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}
	
	/**
	 * Gets the sample ID.
	 *
	 * @return the sample ID
	 */
	public String getSampleID() {
		return sampleID;
	}
	
	/**
	 * Sets the sample ID.
	 *
	 * @param sampleID the new sample ID
	 */
	public void setSampleID(String sampleID) {
		this.sampleID = sampleID;
	}
	
	/**
	 * Gets the message query name.
	 *
	 * @return the message query name
	 */
	public String getMessageQueryName() {
		return messageQueryName;
	}
	
	/**
	 * Sets the message query name.
	 *
	 * @param messageQueryName the new message query name
	 */
	public void setMessageQueryName(String messageQueryName) {
		this.messageQueryName = messageQueryName;
	}
	
	/**
	 * Gets the query def id.
	 *
	 * @return the query def id
	 */
	public String getQueryDefId() {
		return queryDefId;
	}
	
	/**
	 * Sets the query def id.
	 *
	 * @param queryDef_Id the new query def id
	 */
	public void setQueryDefId(String queryDefId) {
		this.queryDefId = queryDefId;
	}
	
	/**
	 * Gets the query def desc.
	 *
	 * @return the query def desc
	 */
	public String getQueryDefdesc() {
		return queryDefdesc;
	}
	
	/**
	 * Sets the query def desc.
	 *
	 * @param queryDef_desc the new query def desc
	 */
	public void setQueryDefdesc(String queryDefdesc) {
		this.queryDefdesc = queryDefdesc;
	}
	
	/**
	 * Gets the query def encoding system.
	 *
	 * @return the query def encoding system
	 */
	public String getQueryDefEncodingSystem() {
		return queryDefEncodingSystem;
	}
	
	/**
	 * Sets the query def encoding system.
	 *
	 * @param queryDef_EncodingSystem the new query def encoding system
	 */
	public void setQueryDefEncodingSystem(String queryDefEncodingSystem) {
		this.queryDefEncodingSystem = queryDefEncodingSystem;
	}
	
	
}
