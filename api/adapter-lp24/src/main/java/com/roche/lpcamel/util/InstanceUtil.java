/*******************************************************************************
 * InstanceUtil.java                  
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
package com.roche.lpcamel.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.roche.lpcamel.dto.MessageContainerDTO;

import ca.uhn.hl7v2.model.Message;

/**
 * The Class InstanceUtil.
 */
@Component
public class InstanceUtil {

	/** The instance util. */
	static InstanceUtil instanceUtil;
	
	/** The connection map. */
	private Map<String, MessageContainerDTO> connectionMap = new HashMap<>();
	
	/** The message map. */
	private Map<String, Message> messageMap = new HashMap<>();

	/**
	 * Gets the connection map.
	 *
	 * @return the connection map
	 */
	public synchronized Map<String, MessageContainerDTO> getConnectionMap() {
		return connectionMap;
	}

	/**
	 * Sets the connection map.
	 *
	 * @param connectionMap the connection map
	 */
	public void setConnectionMap(Map<String, MessageContainerDTO> connectionMap) {
		synchronized (this) {
			this.connectionMap = connectionMap;
		}
	}

	/**
	 * Gets the message map.
	 *
	 * @return the message map
	 */
	public synchronized Map<String, Message> getMessageMap() {
		return messageMap;
	}

	/**
	 * Sets the message map.
	 *
	 * @param messageMap the message map
	 */
	public void setMessageMap(Map<String, Message> messageMap) {
		synchronized (this) {
			this.messageMap = messageMap;
		}
	}

	/**
	 * Gets the single instance of InstanceUtil.
	 *
	 * @return single instance of InstanceUtil
	 */
	public static synchronized InstanceUtil getInstance() {
		if (InstanceUtil.instanceUtil == null) {
			InstanceUtil.instanceUtil = new InstanceUtil();
			synchronized (instanceUtil) {
				InstanceUtil.instanceUtil.setConnectionMap(instanceUtil.getConnectionMap());
				InstanceUtil.instanceUtil.setMessageMap(instanceUtil.getMessageMap());
			}
		}
		return InstanceUtil.instanceUtil;
	}
}
