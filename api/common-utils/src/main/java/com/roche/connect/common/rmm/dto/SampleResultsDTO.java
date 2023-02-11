 /******************************************************************************
   *SampleResultsDTO.java                  
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class SampleResultsDTO {

	private Long runResultsId;
	private long sampleResultId;
	private String accesssioningId;
	private long orderId;
	private String inputContainerId;
	private String inputContainerPosition;
	private String inputKitId;
	private String outputContainerId;
	private String outputContainerPosition;
	private String outputContainerType;
	private String outputPlateType;
	private String outputKitId;
	private String status;
	private String result;
	private String flag;
	private String assayType;
	private String sampleType;
	

	private String comments;
	private String createdBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date createdDateTime;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp receivedDate;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp verifiedDate;
	private String verifiedBy;
	private String updatedBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp updatedDateTime;
	private Collection<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumables = new ArrayList<>();
	private Collection<SampleProtocolDTO> sampleProtocol = new ArrayList<>();
	private Collection<SampleResultsDetailDTO> sampleResultsDetail = new ArrayList<>();
	private String inputContainerType; 
	private boolean mandatoryFieldMissing; 

	public Long getRunResultsId() {
		return runResultsId;
	}

	public void setRunResultsId(Long runResultsId) {
		this.runResultsId = runResultsId;
	}

	public long getSampleResultId() {
		return sampleResultId;
	}

	public void setSampleResultId(long sampleResultId) {
		this.sampleResultId = sampleResultId;
	}

	public String getAccesssioningId() {
		return accesssioningId;
	}

	public void setAccesssioningId(String accesssioningId) {
		this.accesssioningId = accesssioningId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getInputContainerId() {
		return inputContainerId;
	}

	public void setInputContainerId(String inputContainerId) {
		this.inputContainerId = inputContainerId;
	}

	public String getInputContainerPosition() {
		return inputContainerPosition;
	}

	public void setInputContainerPosition(String inputContainerPosition) {
		this.inputContainerPosition = inputContainerPosition;
	}

	public String getInputKitId() {
		return inputKitId;
	}

	public void setInputKitId(String inputKitId) {
		this.inputKitId = inputKitId;
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

	
	
	public String getOutputContainerType() {
        return outputContainerType;
    }

    public void setOutputContainerType(String outputContainerType) {
        this.outputContainerType = outputContainerType;
    }

    public String getOutputPlateType() {
		return outputPlateType;
	}

	public void setOutputPlateType(String outputPlateType) {
		this.outputPlateType = outputPlateType;
	}

	public String getOutputKitId() {
		return outputKitId;
	}

	public void setOutputKitId(String outputKitId) {
		this.outputKitId = outputKitId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Timestamp getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Timestamp getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Timestamp verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
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

	public Collection<SampleReagentsAndConsumablesDTO> getSampleReagentsAndConsumables() {
		return sampleReagentsAndConsumables;
	}

	public void setSampleReagentsAndConsumables(
			Collection<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumables) {
		this.sampleReagentsAndConsumables = sampleReagentsAndConsumables;
	}

	public Collection<SampleProtocolDTO> getSampleProtocol() {
		return sampleProtocol;
	}

	public void setSampleProtocol(Collection<SampleProtocolDTO> sampleProtocol) {
		this.sampleProtocol = sampleProtocol;
	}

	public Collection<SampleResultsDetailDTO> getSampleResultsDetail() {
		return sampleResultsDetail;
	}

	public void setSampleResultsDetail(Collection<SampleResultsDetailDTO> sampleResultsDetail) {
		this.sampleResultsDetail = sampleResultsDetail;
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

	public String getInputContainerType() {
		return inputContainerType;
	}

	public void setInputContainerType(String inputContainerType) {
		this.inputContainerType = inputContainerType;
	}
	
	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public boolean isMandatoryFieldMissing() {
		return mandatoryFieldMissing;
	}

	public void setMandatoryFieldMissing(boolean mandatoryFieldMissing) {
		this.mandatoryFieldMissing = mandatoryFieldMissing;
	}
	
}
