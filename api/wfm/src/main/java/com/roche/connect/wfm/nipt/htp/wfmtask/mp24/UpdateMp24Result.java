/*******************************************************************************
 * UpdateMp24Result.java Version: 1.0 Authors: somesh_r *********************
 * Copyright (c) 2018 Roche Sequencing Solutions (RSS) - CONFIDENTIAL All Rights
 * Reserved. NOTICE: All information contained herein is, and remains the
 * property of COMPANY. The intellectual and technical concepts contained herein
 * are proprietary to COMPANY and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from COMPANY.
 * Access to the source code contained herein is hereby forbidden to anyone
 * except current COMPANY employees, managers or contractors who have executed
 * Confidentiality and Non-disclosure agreements explicitly covering such access
 * The copyright notice above does not evidence any actual or intended
 * publication or disclosure of this source code, which includes information
 * that is confidential and/or proprietary, and is a trade secret, of COMPANY.
 * ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC
 * DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN
 * CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
 * LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE
 * CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS TO
 * REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR
 * SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * ********************* ChangeLog: somesh_r@hcl.com : Updated copyright headers
 * ********************* ********************* Description: Delegate Class
 * implementation to update MP24 Results. *********************
 ******************************************************************************/
package com.roche.connect.wfm.nipt.htp.wfmtask.mp24;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResultIntegrationService;

/**
 * Delegate Class implementation to update MP24 Results.
 */
public class UpdateMp24Result implements JavaDelegate {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	ResultIntegrationService resultIntegrationService = new ResultIntegrationService();
	OrderIntegrationService orderIntegrationService = new OrderIntegrationService();
	AssayIntegrationService assayIntegrationService = new AssayIntegrationService();

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) {

		String protocolName = "";
		String sampleType = "";

		try {
			logger.info(" -> execute()::Update MP24 results" + execution.getProcessInstanceId());

			ActivityProcessDataDTO activityProcessData = (ActivityProcessDataDTO) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString());
			if (activityProcessData == null) {
				throw new HMTPException("Execution variable activityProcessData cannot be null.");
			}
			List<WfmDTO> order = activityProcessData.getOrderDetails();
			if (order != null) {

				sampleType = order.get(0).getSampleType();
				order.stream().forEach(i -> {
					try {
						orderIntegrationService.updateOrders(order.get(0));
					} catch (HMTPException | UnsupportedEncodingException e) {
						logger.error(" -> execute()::Error occurred at updateOrder()", e);
					}
				});

				protocolName = (String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.PROTOCAL.toString());
				execution.setVariable(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLMP24.toString(), protocolName);
				logger.info(" -> execute()::Protocol name for " + activityProcessData.getAccessioningId() + " is "
						+ protocolName);
				execution.setVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString(), order);
				logger.info(" -> execute()::Order Status updated for " + activityProcessData.getAccessioningId());
			} else {
				throw new OrderNotFoundException(" -> execute()::Order cannot be null! accessioningId:"
						+ activityProcessData.getAccessioningId());
			}

			String deviceId = activityProcessData.getDeviceId();
			logger.info(" -> execute()::Update MP24 results device id" + deviceId);
			String accessioningId = activityProcessData.getAccessioningId();
			List<WfmDTO> updateBatchDetails = (List<WfmDTO>) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString());
			AdaptorRequestMessage message = activityProcessData.getAdaptorRequestMessage();

			updateBatchDetails.get(0).setProtocolname(protocolName);
			updateBatchDetails.get(0).setInputContainerId(accessioningId);
			updateBatchDetails.get(0).setSampleType(sampleType);

			resultIntegrationService.updateMP24Results(updateBatchDetails, message);

		} catch (HMTPException | IOException | OrderNotFoundException e) {
			logger.error(" -> execute()::Mp24 UpdateResults " + e);
		}
		logger.info(" <- execute():Update MP24 results" + execution.getProcessInstanceId());
	}
}
