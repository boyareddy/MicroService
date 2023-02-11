/*******************************************************************************
 * 
 * 
 *  FindOrderBy96WellPlateId.java                  
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
 *  Description: Class implementation to find Order by 96 Well PlateId.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.nipt.dpcr.wfmtask.dpcranalyzer;

import java.util.List;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.service.ResultIntegrationService;

/**
 * Class implementation to find Order by Chip PlateId.
 *
 */
public class GetFinalRunResultsESU implements JavaDelegate {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	
	ResultIntegrationService resultIntegrationService = new ResultIntegrationService();
	
	@SuppressWarnings("unchecked") @Override
	public void execute(DelegateExecution execution) {
	    logger.info("dPCR Analyzer GetFinalRunResultsESU Received");
        try {
            logger.info(" -> execute()::dPCR Analyzer Final ESU" + execution.getProcessInstanceId());
            
            ActivityProcessDataDTO activityProcessData = (ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString());
            if (activityProcessData == null) {
                throw new HMTPException("Execution variable activityProcessData cannot be null for GetFinalRunResultsESU");
            }
            String deviceId = activityProcessData.getWfmESUMessage().getDeviceId();
            String orderStatus = (String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRANALYZERSTATUS.toString());
            String processStepName = activityProcessData.getWfmESUMessage().getProcessStepName();
            String mesageType = activityProcessData.getWfmESUMessage().getMessageType();
            
            List<WfmDTO> updateBatchDetails =
                (List<WfmDTO>) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRANALYZERUPDATE.toString());
            
            WFMESUMessage wfmESUMessage = activityProcessData.getWfmESUMessage();
            updateBatchDetails.get(0).setOrderStatus(orderStatus);
            updateBatchDetails.get(0).setDeviceId(deviceId);
            updateBatchDetails.get(0).setSendingApplicationName(processStepName);
            updateBatchDetails.get(0).setMessageType(mesageType);
            
            resultIntegrationService.updatefordPCRAnalyzer(updateBatchDetails, wfmESUMessage);
            
        } catch (HMTPException e) {
            logger.error(" -> execute()::dpcr analyzer UpdateResults:GetFinalRunResultsESU::" + e);
        }
        logger.info(" <- execute():updatedPCRAnalyzerProcess::GetFinalRunResultsESU::" + execution.getProcessInstanceId());
     }

}