 /******************************************************************************
   *ProcessStepValuesDTO.java                  
   * Version:  1.0
   *
   * Authors:  sivaraman.r
   *
   **********************
   * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
   * All Rights Reserved.
   *
   * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
   * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
   * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
   * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
   * executed Confidentiality and Non-disclosure agreements explicitly covering such access
   *
   * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
   * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
   * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
   * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
   * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
   *               
   **********************
   * ChangeLog:
   *
 ******************************************************************************/
package com.roche.connect.common.rmm.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;


public class ProcessStepValuesDTO {  
    
    private Long sampleResultId;
    private String accesssioningId; 
    private Long orderId;
    private String outputContainerId;
    private String outputContainerPosition;
    private String outputKitId;
    private String outputContainerType;
    private String plateType;
	private Long runResultId;
    private String deviceId;
    private String processStepName;
    private String runStatus;
    private String runFlag; 
    private String operatorName;
    private String comments;
	private String createdBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date createdDateTime;
    private String updatedBy;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp updatedDateTime;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Timestamp runStartTime;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Timestamp runCompletionTime;
    private Long runRemainingTime;
    private List<String> protocolName;
    private String sampleType;
    private String quantitativeResult;
    private String qualitativeResult;
    private List<RunResultsDetailDTO> runResultsDetailDTO;
    private List<SampleResultsDetailDTO> sampleResultsDetailDTO;
    private List<SampleReagentsAndConsumablesDTO> uniqueReagentsAndConsumables;
    
    public List<SampleReagentsAndConsumablesDTO> getUniqueReagentsAndConsumables() {
		return uniqueReagentsAndConsumables;
	}
	public void setUniqueReagentsAndConsumables(List<SampleReagentsAndConsumablesDTO> uniqueReagentsAndConsumables) {
		this.uniqueReagentsAndConsumables = uniqueReagentsAndConsumables;
	}
	public Long getSampleResultId() {
        return sampleResultId;
    }
    public void setSampleResultId(Long sampleResultId) {
        this.sampleResultId = sampleResultId;
    }
    public String getAccesssioningId() {
        return accesssioningId;
    }
    public void setAccesssioningId(String accesssioningId) {
        this.accesssioningId = accesssioningId;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getOutputContainerId() {
        return outputContainerId;
    }
    public void setOutputContainerId(String outputContainerId) {
        this.outputContainerId = outputContainerId;
    }
    public String getOutputContainerPosition() {
        return outputContainerPosition;
    }
    public void setOutputContainerPosition(String outputContainerPosition) {
        this.outputContainerPosition = outputContainerPosition;
    }
    public String getOutputKitId() {
        return outputKitId;
    }
    public void setOutputKitId(String outputKitId) {
        this.outputKitId = outputKitId;
    }
        
   public String getOutputContainerType() {
        return outputContainerType;
    }
    public void setOutputContainerType(String outputContainerType) {
        this.outputContainerType = outputContainerType;
    }
    
    public Long getRunResultId() {
        return runResultId;
    }
   
	public String getPlateType() {
		return plateType;
	}
	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}
	public void setRunResultId(Long runResultId) {
        this.runResultId = runResultId;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getProcessStepName() {
        return processStepName;
    }
    public void setProcessStepName(String processStepName) {
        this.processStepName = processStepName;
    }
    public String getRunStatus() {
        return runStatus;
    }
    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }
    public String getRunFlag() {
        return runFlag;
    }
    public void setRunFlag(String runFlag) {
        this.runFlag = runFlag;
    }
    public String getOperatorName() {
        return operatorName;
    }
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    public Timestamp getRunStartTime() {
        return runStartTime;
    }
    public void setRunStartTime(Timestamp runStartTime) {
        this.runStartTime = runStartTime;
    }
    public Timestamp getRunCompletionTime() {
        return runCompletionTime;
    }
    public void setRunCompletionTime(Timestamp runCompletionTime) {
        this.runCompletionTime = runCompletionTime;
    }  
    public Long getRunRemainingTime() {
		return runRemainingTime;
	}
	public void setRunRemainingTime(Long runRemainingTime) {
		this.runRemainingTime = runRemainingTime;
	}
	public List<String> getProtocolName() {
        return protocolName;
    }
    public void setProtocolName(List<String> protocolName) {
        this.protocolName = protocolName;
    }
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	
	public Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public List<RunResultsDetailDTO> getRunResultsDetailDTO() {
		return runResultsDetailDTO;
	}
	public void setRunResultsDetailDTO(List<RunResultsDetailDTO> runResultsDetailDTO) {
		this.runResultsDetailDTO = runResultsDetailDTO;
	}
	public String getQuantitativeResult() {
		return quantitativeResult;
	}
	public void setQuantitativeResult(String quantitativeResult) {
		this.quantitativeResult = quantitativeResult;
	}
	public String getQualitativeResult() {
		return qualitativeResult;
	}
	public void setQualitativeResult(String qualitativeResult) {
		this.qualitativeResult = qualitativeResult;
	}
	public List<SampleResultsDetailDTO> getSampleResultsDetailDTO() {
		return sampleResultsDetailDTO;
	}
	public void setSampleResultsDetailDTO(List<SampleResultsDetailDTO> sampleResultsDetailDTO) {
		this.sampleResultsDetailDTO = sampleResultsDetailDTO;
	}
	
}
