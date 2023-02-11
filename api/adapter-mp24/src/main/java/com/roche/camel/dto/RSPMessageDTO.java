/*******************************************************************************
 * RSPMessageDTO.java                  
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
 * The Class RSPMessageDTO.
 */
public class RSPMessageDTO {
	
	/** The sample info. */
	SampleInfo sampleInfo;
	
	/** The container info. */
	ContainerInfo containerInfo;
	
	/** The order control. */
	String orderControl;
	
	/** The order number. */
	String orderNumber;
	
	/** The order status. */
	String orderStatus;
	
	/** The order event date. */
	String orderEventDate;
	
	/** The protocol name. */
	String protocolName;
	
	/** The protocol description. */
	String protocolDescription;
	
	/** The result status. */
	String resultStatus;
	
	/** The eluate volume. */
	String eluateVolume;
	
	/** The query response status. */
	String queryResponseStatus;
	
	/**
	 * Gets the query response status.
	 *
	 * @return the query response status
	 */
	public String getQueryResponseStatus() {
		return queryResponseStatus;
	}

	/**
	 * Sets the query response status.
	 *
	 * @param queryResponseStatus the new query response status
	 */
	public void setQueryResponseStatus(String queryResponseStatus) {
		this.queryResponseStatus = queryResponseStatus;
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
	 * Gets the order control.
	 *
	 * @return the order control
	 */
	public String getOrderControl() {
		return orderControl;
	}

	/**
	 * Sets the order control.
	 *
	 * @param orderControl the new order control
	 */
	public void setOrderControl(String orderControl) {
		this.orderControl = orderControl;
	}

	/**
	 * Gets the order number.
	 *
	 * @return the order number
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * Sets the order number.
	 *
	 * @param orderNumber the new order number
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * Gets the order status.
	 *
	 * @return the order status
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Sets the order status.
	 *
	 * @param orderStatus the new order status
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * Gets the order event date.
	 *
	 * @return the order event date
	 */
	public String getOrderEventDate() {
		return orderEventDate;
	}

	/**
	 * Sets the order event date.
	 *
	 * @param orderEventDate the new order event date
	 */
	public void setOrderEventDate(String orderEventDate) {
		this.orderEventDate = orderEventDate;
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
	 * Gets the protocol description.
	 *
	 * @return the protocol description
	 */
	public String getProtocolDescription() {
		return protocolDescription;
	}

	/**
	 * Sets the protocol description.
	 *
	 * @param protocolDescription the new protocol description
	 */
	public void setProtocolDescription(String protocolDescription) {
		this.protocolDescription = protocolDescription;
	}

	/**
	 * Gets the result status.
	 *
	 * @return the result status
	 */
	public String getResultStatus() {
		return resultStatus;
	}

	/**
	 * Sets the result status.
	 *
	 * @param resultStatus the new result status
	 */
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	/**
	 * Gets the eluate volume.
	 *
	 * @return the eluate volume
	 */
	public String getEluateVolume() {
		return eluateVolume;
	}

	/**
	 * Sets the eluate volume.
	 *
	 * @param eluateVolume the new eluate volume
	 */
	public void setEluateVolume(String eluateVolume) {
		this.eluateVolume = eluateVolume;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override public String toString() {
        return "RSPMessageDTO [sampleInfo=" + sampleInfo + ", containerInfo=" + containerInfo + ", orderControl="
            + orderControl + ", orderNumber=" + orderNumber + ", orderStatus=" + orderStatus + ", orderEventDate="
            + orderEventDate + ", protocolName=" + protocolName + ", protocolDescription=" + protocolDescription
            + ", resultStatus=" + resultStatus + ", eluateVolume=" + eluateVolume + "]";
    }
	
}
