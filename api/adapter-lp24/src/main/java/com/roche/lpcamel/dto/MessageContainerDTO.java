/*******************************************************************************
 * MessageContainerDTO.java                  
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

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;

/**
 * The Class MessageContainerDTO.
 */
public class MessageContainerDTO {
	
	/** The header segment. */
	HL7HeaderSegmentDTO headerSegment;
	
	/** The exchange. */
	Exchange exchange;
	
	/** The callback. */
	AsyncCallback callback;
	
	/**
	 * Gets the header segment.
	 *
	 * @return the header segment
	 */
	public HL7HeaderSegmentDTO getHeaderSegment() {
		return headerSegment;
	}
	
	/**
	 * Sets the header segment.
	 *
	 * @param headerSegment the new header segment
	 */
	public void setHeaderSegment(HL7HeaderSegmentDTO headerSegment) {
		this.headerSegment = headerSegment;
	}
	
	/**
	 * Gets the exchange.
	 *
	 * @return the exchange
	 */
	public Exchange getExchange() {
		return exchange;
	}
	
	/**
	 * Sets the exchange.
	 *
	 * @param exchange the new exchange
	 */
	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}
	
	/**
	 * Gets the callback.
	 *
	 * @return the callback
	 */
	public AsyncCallback getCallback() {
		return callback;
	}
	
	/**
	 * Sets the callback.
	 *
	 * @param callback the new callback
	 */
	public void setCallback(AsyncCallback callback) {
		this.callback = callback;
	}
	
	
}
