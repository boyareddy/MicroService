/*******************************************************************************
 * 
 * 
 *  SampleDTO.java                  
 *  Version:  1.0
 * 
 *  Authors:  somesh_r
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
 *   somesh_r@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 * 
 * *********************
 * 
 *  Description: Sample Bean class.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.model;

/**
 * Sample Bean class.
 *
 */
public class SampleDTO {
    
    // MP24
    private String accessioningId;
    private String containerId8Tube;
    private String deviceid;
    private String messageType;
    private String sendingApplicationName;

    // LP prepcr
    private String containerIdPrePcrUpdate;
    private String accessioningIdPrePcrUpdate;
    private String deviceidPrePcrUpdate;
    private String newcontainerIdPrePcrUpdate;
    private String mesageTypePrePcrUpdate;   
    private String orderStatusPrePcrUpdate;
    private String sendingApplicationNameprePcrupdate;

    //LP post
    private String containerIdPostPcrUpdate;
    private String accessioningIdPostPcrUpdate;
    private String deviceidPostPcrUpdate;
    private String newcontainerIdPostPcrUpdate;
    private String mesageTypePostPcrUpdate;
    private String orderStatusPostPcrUpdate;
    private String sendingApplicationNameprePostupdate;

    //LP seq
    private String containerIdSeqPreUpdate;
    private String accessioningIdSeqPreUpdate;
    private String deviceidSeqPreUpdate;
    private String newcontainerIdSeqPreUpdate;
    private String mesageTypeSeqPreUpdate;
    private String orderStatusSeqPreUpdate;
    private String sendingApplicationNameSeqPreupdate;
    
    public String getAccessioningId() {
        return accessioningId;
    }
    public void setAccessioningId(String accessioningId) {
        this.accessioningId = accessioningId;
    }
    public String getContainerId8Tube() {
		return containerId8Tube;
	}
	public void setContainerId8Tube(String containerId8Tube) {
		this.containerId8Tube = containerId8Tube;
	}
	public String getDeviceid() {
        return deviceid;
    }
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
    
    public String getSendingApplicationName() {
        return sendingApplicationName;
    }
    public void setSendingApplicationName(String sendingApplicationName) {
        this.sendingApplicationName = sendingApplicationName;
    }
    public String getContainerIdPrePcrUpdate() {
        return containerIdPrePcrUpdate;
    }
    public void setContainerIdPrePcrUpdate(String containerIdPrePcrUpdate) {
        this.containerIdPrePcrUpdate = containerIdPrePcrUpdate;
    }
    public String getAccessioningIdPrePcrUpdate() {
        return accessioningIdPrePcrUpdate;
    }
    public void setAccessioningIdPrePcrUpdate(String accessioningIdPrePcrUpdate) {
        this.accessioningIdPrePcrUpdate = accessioningIdPrePcrUpdate;
    }
    public String getDeviceidPrePcrUpdate() {
        return deviceidPrePcrUpdate;
    }
    public void setDeviceidPrePcrUpdate(String deviceidPrePcrUpdate) {
        this.deviceidPrePcrUpdate = deviceidPrePcrUpdate;
    }
    public String getNewcontainerIdPrePcrUpdate() {
        return newcontainerIdPrePcrUpdate;
    }
    public void setNewcontainerIdPrePcrUpdate(String newcontainerIdPrePcrUpdate) {
        this.newcontainerIdPrePcrUpdate = newcontainerIdPrePcrUpdate;
    }
    public String getMesageTypePrePcrUpdate() {
        return mesageTypePrePcrUpdate;
    }
    public void setMesageTypePrePcrUpdate(String mesageTypePrePcrUpdate) {
        this.mesageTypePrePcrUpdate = mesageTypePrePcrUpdate;
    }
    public String getOrderStatusPrePcrUpdate() {
        return orderStatusPrePcrUpdate;
    }
    public void setOrderStatusPrePcrUpdate(String orderStatusPrePcrUpdate) {
        this.orderStatusPrePcrUpdate = orderStatusPrePcrUpdate;
    }
    public String getSendingApplicationNameprePcrupdate() {
        return sendingApplicationNameprePcrupdate;
    }
    public void setSendingApplicationNameprePcrupdate(String sendingApplicationNameprePcrupdate) {
        this.sendingApplicationNameprePcrupdate = sendingApplicationNameprePcrupdate;
    }
    public String getContainerIdPostPcrUpdate() {
        return containerIdPostPcrUpdate;
    }
    public void setContainerIdPostPcrUpdate(String containerIdPostPcrUpdate) {
        this.containerIdPostPcrUpdate = containerIdPostPcrUpdate;
    }
    public String getAccessioningIdPostPcrUpdate() {
        return accessioningIdPostPcrUpdate;
    }
    public void setAccessioningIdPostPcrUpdate(String accessioningIdPostPcrUpdate) {
        this.accessioningIdPostPcrUpdate = accessioningIdPostPcrUpdate;
    }
    public String getDeviceidPostPcrUpdate() {
        return deviceidPostPcrUpdate;
    }
    public void setDeviceidPostPcrUpdate(String deviceidPostPcrUpdate) {
        this.deviceidPostPcrUpdate = deviceidPostPcrUpdate;
    }
    public String getNewcontainerIdPostPcrUpdate() {
        return newcontainerIdPostPcrUpdate;
    }
    public void setNewcontainerIdPostPcrUpdate(String newcontainerIdPostPcrUpdate) {
        this.newcontainerIdPostPcrUpdate = newcontainerIdPostPcrUpdate;
    }
    public String getMesageTypePostPcrUpdate() {
        return mesageTypePostPcrUpdate;
    }
    public void setMesageTypePostPcrUpdate(String mesageTypePostPcrUpdate) {
        this.mesageTypePostPcrUpdate = mesageTypePostPcrUpdate;
    }
    public String getOrderStatusPostPcrUpdate() {
        return orderStatusPostPcrUpdate;
    }
    public void setOrderStatusPostPcrUpdate(String orderStatusPostPcrUpdate) {
        this.orderStatusPostPcrUpdate = orderStatusPostPcrUpdate;
    }
    public String getSendingApplicationNameprePostupdate() {
        return sendingApplicationNameprePostupdate;
    }
    public void setSendingApplicationNameprePostupdate(String sendingApplicationNameprePostupdate) {
        this.sendingApplicationNameprePostupdate = sendingApplicationNameprePostupdate;
    }
    public String getContainerIdSeqPreUpdate() {
        return containerIdSeqPreUpdate;
    }
    public void setContainerIdSeqPreUpdate(String containerIdSeqPreUpdate) {
        this.containerIdSeqPreUpdate = containerIdSeqPreUpdate;
    }
    public String getAccessioningIdSeqPreUpdate() {
        return accessioningIdSeqPreUpdate;
    }
    public void setAccessioningIdSeqPreUpdate(String accessioningIdSeqPreUpdate) {
        this.accessioningIdSeqPreUpdate = accessioningIdSeqPreUpdate;
    }
    public String getDeviceidSeqPreUpdate() {
        return deviceidSeqPreUpdate;
    }
    public void setDeviceidSeqPreUpdate(String deviceidSeqPreUpdate) {
        this.deviceidSeqPreUpdate = deviceidSeqPreUpdate;
    }
    public String getNewcontainerIdSeqPreUpdate() {
        return newcontainerIdSeqPreUpdate;
    }
    public void setNewcontainerIdSeqPreUpdate(String newcontainerIdSeqPreUpdate) {
        this.newcontainerIdSeqPreUpdate = newcontainerIdSeqPreUpdate;
    }
    public String getMesageTypeSeqPreUpdate() {
        return mesageTypeSeqPreUpdate;
    }
    public void setMesageTypeSeqPreUpdate(String mesageTypeSeqPreUpdate) {
        this.mesageTypeSeqPreUpdate = mesageTypeSeqPreUpdate;
    }
    public String getOrderStatusSeqPreUpdate() {
        return orderStatusSeqPreUpdate;
    }
    public void setOrderStatusSeqPreUpdate(String orderStatusSeqPreUpdate) {
        this.orderStatusSeqPreUpdate = orderStatusSeqPreUpdate;
    }
    public String getSendingApplicationNameSeqPreupdate() {
        return sendingApplicationNameSeqPreupdate;
    }
    public void setSendingApplicationNameSeqPreupdate(String sendingApplicationNameSeqPreupdate) {
        this.sendingApplicationNameSeqPreupdate = sendingApplicationNameSeqPreupdate;
    }
    public String getMessageType() {
        return messageType;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
