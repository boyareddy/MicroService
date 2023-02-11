/*******************************************************************************
 * 
 * 
 *  UpdateLP24Result.java                  
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
 *  Description: Class implementation to update LP24 results.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.nipt.dpcr.wfmtask.lp24;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.service.ResultIntegrationService;

/**
 * Class implementation to update LP24 results.
 *
 */
public class UpdateLP24Result implements JavaDelegate {
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    ResultIntegrationService resultIntegrationService = new ResultIntegrationService();
    @Override 
    public void execute(DelegateExecution execution) {
    	try {    		
			ActivityProcessDataDTO activityProcessData = (ActivityProcessDataDTO) execution
					.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString());
			if (activityProcessData == null)
				throw new HMTPException("Execution variable activityProcessData cannot be null.");   
	        
	        String orderStatusdPCRlp42Update = (String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSDPCRLP24UPDATE.toString());
        
	        SpecimenStatusUpdateMessage message = activityProcessData.getSpecimenStatusUpdateMessage();
	        
	        @SuppressWarnings("unchecked") 
	        List<WfmDTO> updateOrderDetails = (List<WfmDTO>) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRLPUPDATE.toString());
	        
			if (updateOrderDetails != null) { 
				updateOrderDetails.stream().forEach(orderDetail -> {
					
			        orderDetail.setAccessioningId((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDDPCR24UPDATE.toString()));
			        orderDetail.setDeviceId((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDDPCRLP24UPDATE.toString()));
			        orderDetail.setInputContainerId((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDDPCRLP24.toString()));
			        orderDetail.setInputposition((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONDPCRLP24.toString()));
			        orderDetail.setSendingApplicationName(activityProcessData.getSpecimenStatusUpdateMessage().getSendingApplicationName());
			        if (orderStatusdPCRlp42Update.equalsIgnoreCase(WORKFLOW_STATUS.PASSED.toString()) || orderStatusdPCRlp42Update.equalsIgnoreCase(WORKFLOW_STATUS.FLAGGED.toString())) {
			            orderDetail.setOrderStatus(WORKFLOW_STATUS.COMPLETED.toString());
			        } else if (orderStatusdPCRlp42Update.equalsIgnoreCase(WfmConstants.ORDER_STATUS.INPROGRESS.toString())) {
			            orderDetail.setOrderStatus(WfmConstants.ORDER_STATUS.INPROGRESS.toString());
			        }else if (orderStatusdPCRlp42Update.equalsIgnoreCase(WfmConstants.ORDER_STATUS.FAILED.toString())) {
                        orderDetail.setOrderStatus(WfmConstants.ORDER_STATUS.FAILED.toString());
                        message.getStatusUpdate().getSampleInfo().setNewContainerId(null);
                        message.getStatusUpdate().getSampleInfo().setNewOutputPosition(null);
                    }
			        else {
			            orderDetail.setOrderStatus(WORKFLOW_STATUS.INPROGRESS.toString());
			        }
				});
			}
			 
	            resultIntegrationService.updateforLP24Results(updateOrderDetails, message); 
		} catch (HMTPException | IOException | ParseException e) {
			logger.error(" -> execute()::Error occurred, ", e);
		} 
    	 logger.info(" <- execute():UpdateMP96results" + execution.getProcessInstanceId());
    }
}
