/*******************************************************************************
 * MessageType.java                  
 *  Version:  1.0
 * 
 *  Authors:  Alexander
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
 *  Alexanders@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.imm.utils;

/**
 * The Class MessageType.
 */
public class MessageType {

	/** The Constant NA_EXTRACTION. */
	public static final String NA_EXTRACTION = "NA-Extraction";

	/** The Constant STATUS_UPDATE. */ 
	public static final String STATUS_UPDATE = "StatusUpdate";

	public static final String MP24_NAEXTRACTION = "NA-Extraction";

	public static final String MP24_STATUS_UPDATE = "StatusUpdate";

	/** The Constant RSP. */
	public static final String RSP = "RSP";

	/** The Constant U03ACK. */
	public static final String U03ACK = "U03ACK";

	/** The Constant RESPONSE_MESSAGE. */
	public static final String RESPONSE_MESSAGE = "ResponseMessage";

	/** The Constant ACKNOWLEDGEMENT_MESSAGE. */
	public static final String ACKNOWLEDGEMENT_MESSAGE = "AcknowledgementMessage";

	/** The Constant QUERY_MESSAGE. */
	public static final String QUERY_MESSAGE = "QueryMessage";

	/** The Constant STATUSUPDATE_MESSAGE. */
	public static final String STATUSUPDATE_MESSAGE = "StatusUpdateMessage";

	/** The Constant HTP_RUN. */
	public static final String HTP_RUN = "HTP-Run";

	/** The Constant HTP_RUN_STATUS. */
	public static final String HTP_RUN_STATUS = "HTP-RunStatus";

	/** The Constant LP_SEQ_PP. */
	public static final String LP_SEQ_PP = "LP-SEQ-PP";

	/** The Constant INPROGRESS. */
	public static final String INPROGRESS = "Inprogress";

	/**
	 * Instantiates a new message type.
	 */
	private MessageType() {

	}
}
