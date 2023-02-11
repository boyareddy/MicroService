/*******************************************************************************
 * 
 * 
 *  WfmDTO.java                  
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
 *  Description: Simple POJO class that holds all order, run and application related details.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple POJO class that holds all order, run and application related details.
 */
public class WfmDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private Long runid;
    private Long runResultsId;
    private String accessioningId;
    private String deviceId;
    private String deviceRunId;
    private String inputContainerId;
    private String outputContainerId;
    private String processStepName;
    private String sendingApplicationName;
    private String orderStatus;
    private String orderResult;
    private String sampleType;
    private String protocolname;
    private String outputposition;
    private String inputposition;
    private String containerId8Tube;
    private String containerId96;
    private String molecularId;
    private String molecularOutputId;
    private String assayType;
    private String messageType;
    private String molicularId;
    private List<String> testId = new LinkedList<>();
    
    public String getProtocolname() {
        return protocolname;
    }
    
    public void setProtocolname(String protocolname) {
        this.protocolname = protocolname;
    }
    
    public String getOutputposition() {
        return outputposition;
    }
    
    public void setOutputposition(String outputposition) {
        this.outputposition = outputposition;
    }
    
    public String getInputposition() {
        return inputposition;
    }
    
    public void setInputposition(String inputposition) {
        this.inputposition = inputposition;
    }
    
    public Long getRunid() {
        return runid;
    }
    
    public void setRunid(Long runid) {
        this.runid = runid;
    }
    
    public String getMolecularOutputId() {
        return molecularOutputId;
    }
    
    public void setMolecularOutputId(String molecularOutputId) {
        this.molecularOutputId = molecularOutputId;
    }
    
    public String getSendingApplicationName() {
        return sendingApplicationName;
    }
    
    public void setSendingApplicationName(String sendingApplicationName) {
        this.sendingApplicationName = sendingApplicationName;
    }
    
    public String getContainerId8Tube() {
		return containerId8Tube;
	}

	public void setContainerId8Tube(String containerId8Tube) {
		this.containerId8Tube = containerId8Tube;
	}

	public String getContainerId96() {
		return containerId96;
	}

	public void setContainerId96(String containerId96) {
		this.containerId96 = containerId96;
	}

	public String getMolecularId() {
        return molecularId;
    }
    
    public void setMolecularId(String molecularId) {
        this.molecularId = molecularId;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public String getAccessioningId() {
        return accessioningId;
    }
    
    public void setAccessioningId(String accessioningId) {
        this.accessioningId = accessioningId;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public List<String> getTestId() {
        return testId;
    }
    
    public void setTestId(List<String> testId) {
        this.testId = testId;
    }
    
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    
    public String getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public String getSampleType() {
        return sampleType;
    }
    
    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }
    
    public String getDeviceRunId() {
        return deviceRunId;
    }
    
    public String getAssayType() {
        return assayType;
    }
    
    public void setDeviceRunId(String deviceRunId) {
        this.deviceRunId = deviceRunId;
    }
    
    public Long getRunResultsId() {
        return runResultsId;
    }
    
    public void setRunResultsId(Long runResultsId) {
        this.runResultsId = runResultsId;
    }
    
    public void setAssayType(String assayType) {
        this.assayType = assayType;
    }
    
    public String getInputContainerId() {
        return inputContainerId;
    }
    
    public void setInputContainerId(String inputContainerId) {
        this.inputContainerId = inputContainerId;
    }
    
    public String getOutputContainerId() {
        return outputContainerId;
    }
    
    public void setOutputContainerId(String outputContainerId) {
        this.outputContainerId = outputContainerId;
    }
    
    public String getProcessStepName() {
        return processStepName;
    }
    
    public void setProcessStepName(String processStepName) {
        this.processStepName = processStepName;
    }

    public String getMolicularId() {
        return molicularId;
    }

    public void setMolicularId(String molicularId) {
        this.molicularId = molicularId;
    }

    public String getOrderResult() {
        return orderResult;
    }

    public void setOrderResult(String orderResult) {
        this.orderResult = orderResult;
    }
    
    
}
