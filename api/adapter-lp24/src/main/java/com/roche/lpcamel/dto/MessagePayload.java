/*******************************************************************************
 * MessagePayload.java                  
 *  Version:  1.0
 * 
 *  Authors:  gosula.r
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
 *   gosula.r@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.lpcamel.dto;

import com.roche.connect.common.lp24.StatusUpdate;

/**
 * The Class MessagePayload.
 */
public class MessagePayload {
	
	/** The device serial number. */
	private String deviceSerialNumber;
	
	/** The sending application name. */
	private String sendingApplicationName;
	
	/** The receiving application. */
	private String receivingApplication;
	
	/** The date time message generated. */
	private String dateTimeMessageGenerated;
	
	/** The message type. */
	private String messageType;
	
	/** The message cotrol id. */
	private String messageControlId;
	
	/** The accessioning id. */
	private String accessioningId;
	
	/** The rsp message. */
	private RSPMessageDTO rspMessage;
	
	/** The status update. */
	private StatusUpdate statusUpdate;
	
	/**
	 * Gets the rsp message.
	 *
	 * @return the rsp message
	 */
	public RSPMessageDTO getRspMessage() {
		return rspMessage;
	}
	
	/**
	 * Sets the rsp message.
	 *
	 * @param rspMessage the new rsp message
	 */
	public void setRspMessage(RSPMessageDTO rspMessage) {
		this.rspMessage = rspMessage;
	}
	
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
	 * Gets the accessioning id.
	 *
	 * @return the accessioning id
	 */
	public String getAccessioningId() {
		return accessioningId;
	}
	
	/**
	 * Sets the accessioning id.
	 *
	 * @param accessioningId the new accessioning id
	 */
	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}
	
	/**
	 * Gets the status update.
	 *
	 * @return the status update
	 */
	public StatusUpdate getStatusUpdate() {
		return statusUpdate;
	}
	
	/**
	 * Sets the status update.
	 *
	 * @param statusUpdate the new status update
	 */
	public void setStatusUpdate(StatusUpdate statusUpdate) {
		this.statusUpdate = statusUpdate;
	}

	public String getMessageControlId() {
		return messageControlId;
	}

	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}
	
	
}
