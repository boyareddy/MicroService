/*******************************************************************************
 * StatusUpdate.java                  
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

import java.sql.Timestamp;
import java.util.List;

/**
 * The Class StatusUpdate.
 */
public class StatusUpdate {
	
	/** The updated by. */
	private String updatedBy;
	
	/** The operator name. */
	private String operatorName;
	
	/** The event date. */
	private String eventDate;
	
	/** The equipment state. */
	private String equipmentState;
	
	/** The container info. */
	private ContainerInfo containerInfo;
	
	/** The protocol name. */
	private String protocolName;
	
	/** The protocol version. */
	private String protocolVersion;
	
	/** The run result. */
	private String runResult;
	
	/** The run start time. */
	private Timestamp runStartTime;
	
	/** The run end time. */
	private Timestamp runEndTime;
	
	/** The comment. */
	private String comment;
	
	/** The sample info. */
	private SampleInfo sampleInfo;
	
	/** The order name. */
	private String orderName;
	
	/** The order result. */
	private String orderResult;
	
	/** The internal controls. */
	private String internalControls;
	
	/** The processing cartridge. */
	private String processingCartridge;
	
	/** The tip rack. */
	private String tipRack;
	
	/** The reagent cassette. */
	private String reagentCassette;
	
	/** The reagent 2 ml tube. */
	private List<String> reagent2mlTube;
	
	/** The reagent 25 ml tube. */
	private List<String> reagent25mlTube;
	
	/**
	 * Gets the event date.
	 *
	 * @return the event date
	 */
	public String getEventDate() {
		return eventDate;
	}
	
	/**
	 * Sets the event date.
	 *
	 * @param eventDate the new event date
	 */
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	
	/**
	 * Gets the equipment state.
	 *
	 * @return the equipment state
	 */
	public String getEquipmentState() {
		return equipmentState;
	}
	
	/**
	 * Sets the equipment state.
	 *
	 * @param equipmentState the new equipment state
	 */
	public void setEquipmentState(String equipmentState) {
		this.equipmentState = equipmentState;
	}
	
	/**
	 * Gets the container info.
	 *
	 * @return the container info
	 */
	public ContainerInfo getContainerInfo() {
		return containerInfo;
	}
	
	/**
	 * Sets the container info.
	 *
	 * @param containerInfo the new container info
	 */
	public void setContainerInfo(ContainerInfo containerInfo) {
		this.containerInfo = containerInfo;
	}
	
	/**
	 * Gets the protocol name.
	 *
	 * @return the protocol name
	 */
	public String getProtocolName() {
		return protocolName;
	}
	
	/**
	 * Sets the protocol name.
	 *
	 * @param protocolName the new protocol name
	 */
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	
	/**
	 * Gets the protocol version.
	 *
	 * @return the protocol version
	 */
	public String getProtocolVersion() {
		return protocolVersion;
	}
	
	/**
	 * Sets the protocol version.
	 *
	 * @param protocolVersion the new protocol version
	 */
	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	/**
	 * Gets the run result.
	 *
	 * @return the run result
	 */
	public String getRunResult() {
		return runResult;
	}
	
	/**
	 * Sets the run result.
	 *
	 * @param runResult the new run result
	 */
	public void setRunResult(String runResult) {
		this.runResult = runResult;
	}
	
	/**
	 * Gets the run start time.
	 *
	 * @return the run start time
	 */
	public Timestamp getRunStartTime() {
		return runStartTime;
	}
	
	/**
	 * Sets the run start time.
	 *
	 * @param runStartTime the new run start time
	 */
	public void setRunStartTime(Timestamp runStartTime) {
		this.runStartTime = runStartTime;
	}
	
	/**
	 * Gets the run end time.
	 *
	 * @return the run end time
	 */
	public Timestamp getRunEndTime() {
		return runEndTime;
	}
	
	/**
	 * Sets the run end time.
	 *
	 * @param runEndTime the new run end time
	 */
	public void setRunEndTime(Timestamp runEndTime) {
		this.runEndTime = runEndTime;
	}
	
	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Sets the comment.
	 *
	 * @param comment the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Gets the sample info.
	 *
	 * @return the sample info
	 */
	public SampleInfo getSampleInfo() {
		return sampleInfo;
	}
	
	/**
	 * Sets the sample info.
	 *
	 * @param sampleInfo the new sample info
	 */
	public void setSampleInfo(SampleInfo sampleInfo) {
		this.sampleInfo = sampleInfo;
	}
	
	/**
	 * Gets the order name.
	 *
	 * @return the order name
	 */
	public String getOrderName() {
		return orderName;
	}
	
	/**
	 * Sets the order name.
	 *
	 * @param orderName the new order name
	 */
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	
	/**
	 * Gets the order result.
	 *
	 * @return the order result
	 */
	public String getOrderResult() {
		return orderResult;
	}
	
	/**
	 * Sets the order result.
	 *
	 * @param orderResult the new order result
	 */
	public void setOrderResult(String orderResult) {
		this.orderResult = orderResult;
	}
	
	/**
	 * Gets the internal controls.
	 *
	 * @return the internal controls
	 */
	public String getInternalControls() {
		return internalControls;
	}
	
	/**
	 * Sets the internal controls.
	 *
	 * @param internalControls the new internal controls
	 */
	public void setInternalControls(String internalControls) {
		this.internalControls = internalControls;
	}
	
	/**
	 * Gets the processing cartridge.
	 *
	 * @return the processing cartridge
	 */
	public String getProcessingCartridge() {
		return processingCartridge;
	}
	
	/**
	 * Sets the processing cartridge.
	 *
	 * @param processingCartridge the new processing cartridge
	 */
	public void setProcessingCartridge(String processingCartridge) {
		this.processingCartridge = processingCartridge;
	}
	
	/**
	 * Gets the tip rack.
	 *
	 * @return the tip rack
	 */
	public String getTipRack() {
		return tipRack;
	}
	
	/**
	 * Sets the tip rack.
	 *
	 * @param tipRack the new tip rack
	 */
	public void setTipRack(String tipRack) {
		this.tipRack = tipRack;
	}
	
	/**
	 * Gets the reagent cassette.
	 *
	 * @return the reagent cassette
	 */
	public String getReagentCassette() {
		return reagentCassette;
	}
	
	/**
	 * Sets the reagent cassette.
	 *
	 * @param reagentCassette the new reagent cassette
	 */
	public void setReagentCassette(String reagentCassette) {
		this.reagentCassette = reagentCassette;
	}
	
	/**
	 * Gets the reagent 2 ml tube.
	 *
	 * @return the reagent 2 ml tube
	 */
	public List<String> getReagent2mlTube() {
		return reagent2mlTube;
	}
	
	/**
	 * Sets the reagent 2 ml tube.
	 *
	 * @param reagent2mlTube the new reagent 2 ml tube
	 */
	public void setReagent2mlTube(List<String> reagent2mlTube) {
		this.reagent2mlTube = reagent2mlTube;
	}
	
	/**
	 * Gets the reagent 25 ml tube.
	 *
	 * @return the reagent 25 ml tube
	 */
	public List<String> getReagent25mlTube() {
		return reagent25mlTube;
	}
	
	/**
	 * Sets the reagent 25 ml tube.
	 *
	 * @param reagent25mlTube the new reagent 25 ml tube
	 */
	public void setReagent25mlTube(List<String> reagent25mlTube) {
		this.reagent25mlTube = reagent25mlTube;
	}
	
	/**
	 * Gets the operator name.
	 *
	 * @return the operator name
	 */
	public String getOperatorName() {
		return operatorName;
	}
	
	/**
	 * Sets the operator name.
	 *
	 * @param operatorName the new operator name
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	/**
	 * Gets the updated by.
	 *
	 * @return the updated by
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}
	
	/**
	 * Sets the updated by.
	 *
	 * @param updatedBy the new updated by
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	
}
