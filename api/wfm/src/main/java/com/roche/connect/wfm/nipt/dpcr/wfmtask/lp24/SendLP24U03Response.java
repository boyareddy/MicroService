/*******************************************************************************
 * 
 * 
 *  SendLP24U03Response.java                  
 *  Version:  1.0
 * 
 *  Authors:  narasimhareddyb
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
 *   narasimhareddyb@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 * 
 * *********************
 * 
 *  Description: Class implementation to send LP24 U03 Response.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.nipt.dpcr.wfmtask.lp24;

import java.util.Date;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.service.ImmIntegrationService;

/**
 * Class implementation to send dPCRLP24 U03 Response.
 *
 */
public class SendLP24U03Response implements JavaDelegate {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	ImmIntegrationService immIntegrationService = new ImmIntegrationService();
	
	@Override
	public void execute(DelegateExecution execution) {
		
		ActivityProcessDataDTO activityProcessData = null;

		try {
			activityProcessData = (ActivityProcessDataDTO) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString());
			if (activityProcessData == null)
				throw new HMTPException("Execution variable activityProcessData cannot be null.");

			String deviceId = (String) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDDPCRLP24UPDATE.toString());
			String containerId = activityProcessData.getSpecimenStatusUpdateMessage().getContainerId();
			String messageControlId = activityProcessData.getSpecimenStatusUpdateMessage().getMessageControlId();
			AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
			acknowledgementMessage.setStatus(WfmConstants.WORKFLOW_RESPONSE.SUCCESS_1.toString());
			acknowledgementMessage.setDeviceSerialNumber(deviceId);
			acknowledgementMessage
					.setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
			acknowledgementMessage.setReceivingApplication(WfmConstants.WORKFLOW_VARIABLES.DPCRLP24.toString());
			acknowledgementMessage.setDateTimeMessageGenerated(new Date().toString());
			acknowledgementMessage.setMessageType(EnumMessageType.AcknowledgementMessage);
			acknowledgementMessage.setContainerId(containerId);
			acknowledgementMessage.setMessageControlId(messageControlId);			
			immIntegrationService.setAcknowledgementMessage(acknowledgementMessage);
		} catch (HMTPException e) {
			logger.error(" -> execute()::Error occurred, ", e);
		}
		logger.info("UO3ACK for dPCRLP24  Response");

	}

}
