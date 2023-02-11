 /******************************************************************************
   *RunResultsDetailDTO.java                  
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class RunResultsDetailDTO {
	
	private long runResultsDetailsId;
	private String attributeName;
	private String attributeValue;
	private String updatedBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp updatedDateTime;
	private String createdBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date createdDateTime;
	public long getRunResultsDetailsId() {
		return runResultsDetailsId;
	}
	public void setRunResultsDetailsId(long runResultsDetailsId) {
		this.runResultsDetailsId = runResultsDetailsId;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
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
	

}
