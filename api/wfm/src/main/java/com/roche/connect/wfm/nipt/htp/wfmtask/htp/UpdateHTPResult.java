/*******************************************************************************
 * 
 * 
 *  UpdateHTPResult.java                  
 *  Version:  1.0
 * 
 *  Authors:  somesh_r
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
 *   somesh_r@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 * 
 * *********************
 * 
 *  Description: Class implementation to update HTP Result.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.nipt.htp.wfmtask.htp;

import java.io.IOException;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.ResultIntegrationService;

/**
 * Class implementation to update HTP Result.
 *
 */
public class UpdateHTPResult implements JavaDelegate {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	ResultIntegrationService resultIntegrationService = new ResultIntegrationService();
	AssayIntegrationService assayIntegrationService = new AssayIntegrationService();
	ActivityProcessDataDTO activityProcessData = null;
    String protocolName = "";

	@Override
	public void execute(DelegateExecution execution) {
		try {
			activityProcessData = (ActivityProcessDataDTO) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString());
			if (activityProcessData == null)
				throw new HMTPException("Execution variable activityProcessData cannot be null.");

			String deviceId = (String) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDHTPUPDATE.toString());
			String accessioningId = (String) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDHTPUPDATE.toString());
			String sendingApplication = activityProcessData.getHtpStatusMessage().getSendingApplication();
			String orderStatusHTPUpdate = (String) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSHTPUPDATE.toString());
			List<DeviceTestOptionsDTO> deviceTestOptions;
			deviceTestOptions = assayIntegrationService.getTestOptionsByAccessioningIDandDevice(accessioningId,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.HTP.toString());

			if (!deviceTestOptions.isEmpty())
				protocolName = deviceTestOptions.get(0).getTestProtocol();

			HtpStatusMessage message = activityProcessData.getHtpStatusMessage();

			@SuppressWarnings("unchecked")
			List<WfmDTO> updateOrderDetails = (List<WfmDTO>) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString());

			if (updateOrderDetails != null) {
				updateOrderDetails.stream().forEach(orderDetail -> {
					orderDetail.setAccessioningId(accessioningId);
					orderDetail.setDeviceId(deviceId);
					orderDetail.setDeviceRunId((String) execution.getVariable("deviceRunId"));
					orderDetail.setRunResultsId((Long) execution.getVariable("runResultsId"));
					orderDetail.setSendingApplicationName(sendingApplication);
					orderDetail.setOrderId((Long) execution.getVariable("orderId"));
					orderDetail.setInputContainerId((String) execution.getVariable("inputContainerId"));
					orderDetail.setOutputContainerId((String) execution.getVariable("outputContainerId"));
					orderDetail.setProcessStepName((String) execution.getVariable("processStepName"));
					orderDetail.setOrderStatus(orderStatusHTPUpdate);
					orderDetail.setProtocolname(protocolName);
					orderDetail.setOrderStatus(orderStatusHTPUpdate);
				});
			}

			resultIntegrationService.updateHTPStatus(updateOrderDetails, message);

		} catch (HMTPException  | IOException e) {
			logger.error(" -> execute()::Error occurred, ", e);
		}

		logger.info("Updated HTP Status completed Successfully");

	}
}
