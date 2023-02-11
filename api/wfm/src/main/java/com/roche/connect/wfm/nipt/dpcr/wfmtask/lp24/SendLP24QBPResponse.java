/*******************************************************************************
 * 
 * 
 *  SendLP24QBPResponse.java                  
 *  Version:  1.0
 * 
 *  Authors:  PREM.V
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
 *   v.prem@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 * 
 * *********************
 * 
 *  Description: Class implementation to send LP24 QBP Response.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.nipt.dpcr.wfmtask.lp24;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.ImmIntegrationService;

/**
 * Class implementation to send LP24 QBP Response.
 *
 */
public class SendLP24QBPResponse implements JavaDelegate {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	
	private ObjectMapper objectMapper = new ObjectMapper();

	ImmIntegrationService immIntegrationService = new ImmIntegrationService();
	AssayIntegrationService assayIntegrationService = new AssayIntegrationService();
	ActivityProcessDataDTO activityProcessData = null;
	List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();
	

	@Override
	public void execute(DelegateExecution execution) {
		try {

			logger.info("Enter dPCR LP24 QBP");
			String protocolName = "";
			
			ResponseMessage responseMessage = new ResponseMessage();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

			activityProcessData = (ActivityProcessDataDTO) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString());
			if (activityProcessData == null)
				throw new HMTPException("Execution variable activityProcessData cannot be null.");

			String accessioningId = (String) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDDPCRLP24.toString());
			String containerId = activityProcessData.getQueryMessage().getContainerId();
			String deviceId = activityProcessData.getDeviceId();
            String messageControlId = activityProcessData.getQueryMessage().getMessageControlId();
			if (deviceId == null || containerId == null || accessioningId == null)
				throw new HMTPException("Required fields cannot be empty");

			deviceTestOptions = assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(accessioningId,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString());
			protocolName = deviceTestOptions.get(0).getTestProtocol();

			execution.setVariable(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), protocolName);
			
			responseMessage.setStatus(WfmConstants.WORKFLOW_RESPONSE.SUCCESS_1.toString());
			responseMessage.setErrors(null);
			responseMessage.setDeviceSerialNumber(deviceId);
			responseMessage.setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
			responseMessage.setReceivingApplication(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString());
			responseMessage.setDateTimeMessageGenerated(formatter.format(new Date()));
			responseMessage.setMessageType(EnumMessageType.ResponseMessage);
			responseMessage.setAccessioningId(accessioningId);
			responseMessage.setContainerId(containerId);
			responseMessage.setSampleType(WfmConstants.WORKFLOW_VARIABLES.PLASMA.toString());
			responseMessage.setSpecimenDescription(WfmConstants.WORKFLOW_VARIABLES.PLASMA.toString());
			responseMessage.setProtocolName(protocolName);
			responseMessage.setMessageControlId(messageControlId);
			logger.info("SendLP24QBPResponse::ResponseMessage: " + objectMapper.writeValueAsString(responseMessage));
			immIntegrationService.setResponseMessage(responseMessage);

		} catch (HMTPException | IOException e) {
			logger.error(" -> execute()::Error occurred, ", e);
		} 
		logger.info("Sent Query Response Successfully to dPCR LP24 Preperation");
	}

}
