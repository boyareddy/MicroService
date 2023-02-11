/*******************************************************************************
 * UpdateMp24Result.java Version: 1.0 Authors:  narasimhareddyb *********************
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
 * ********************* ChangeLog: narasimhareddyb@hcl.com : Updated copyright headers
 * ********************* ********************* Description: Delegate Class
 * implementation to update MP96 Results. *********************
 ******************************************************************************/
package com.roche.connect.wfm.nipt.dpcr.wfmtask.mp96;

import java.io.IOException;
import java.util.List;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResultIntegrationService;

/**
 * Delegate Class implementation to update MP96 Results.
 */
public class UpdateMp96Result implements JavaDelegate {
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName()); 
    
    OrderIntegrationService orderIntegrationService = new OrderIntegrationService();
    ResultIntegrationService   resultIntegrationService = new ResultIntegrationService();
    
    @SuppressWarnings("unchecked") @Override public void execute(DelegateExecution execution) {
        try {
            logger.info(" -> execute()::Update MP96 results" + execution.getProcessInstanceId());
          
            ActivityProcessDataDTO activityProcessData = (ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString());
            if (activityProcessData == null) {
                throw new HMTPException("Execution variable activityProcessData cannot be null for UpdateMp96Result");
            }
            String deviceId = activityProcessData.getDeviceId();
            logger.info(" -> execute()::Update MP96 results device id" + deviceId);
            String mesageType = activityProcessData.getMessageType();
            String accessioningId = activityProcessData.getAccessioningId();
            String sendingApplicationName = activityProcessData.getU03RequestMessage().getSendingApplicationName();
            String orderStatus = (String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUS.toString());
            List<WfmDTO> updateBatchDetails =
                (List<WfmDTO>) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString());
           
            updateBatchDetails.get(0).setOrderStatus(WORKFLOW_STATUS.INPROGRESS.toString());
            orderIntegrationService.updateOrders(updateBatchDetails.get(0));
               
            WFMoULMessage u03RequestMessage = activityProcessData.getU03RequestMessage(); 

            updateBatchDetails.get(0).setMessageType(mesageType);
            updateBatchDetails.get(0).setInputContainerId(accessioningId);
            updateBatchDetails.get(0).setDeviceId(deviceId);
            updateBatchDetails.get(0).setOutputContainerId(
                activityProcessData.getU03RequestMessage().getOulSampleResultMessage().getOutputContainerId());
            updateBatchDetails.get(0).setOutputposition(activityProcessData.getU03RequestMessage().getOulSampleResultMessage().getPosition());
            updateBatchDetails.get(0).setSendingApplicationName(sendingApplicationName);
            
            if (orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.PASSED.toString())) {
                updateBatchDetails.get(0).setOrderStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
            } else if (orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.FAILED.toString())) {
                updateBatchDetails.get(0).setOrderStatus(WORKFLOW_STATUS.FAILED.toString());
            } else {
                updateBatchDetails.get(0).setOrderStatus(WORKFLOW_STATUS.INPROGRESS.toString());
            }
           
            resultIntegrationService.updateforMP96Status(updateBatchDetails, u03RequestMessage);
       
        } catch (HMTPException | IOException e) {
            logger.error(" -> execute()::Mp96 UpdateResults " + e);
        }
        logger.info(" <- execute():UpdateMP96results" + execution.getProcessInstanceId());
    }
}
