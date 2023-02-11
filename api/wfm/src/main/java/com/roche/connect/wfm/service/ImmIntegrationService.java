/*******************************************************************************
 * 
 * 
 *  ImmIntegrationService.java                  
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
 *  Description: Class implementation that provides integration support for IMM.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.common.server.util.CommonRestURLConstants;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmURLConstants;

/**
 * Class implementation that provides integration support for IMM.
 *
 */
@Service("immIntegrationService") 
public class ImmIntegrationService {
    String url = null;
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

    public void setResponseMessage(ResponseMessage responseMessage) throws HMTPException, JsonProcessingException {
        
    	logger.info("Before Sending data to IMM "+ new ObjectMapper().writeValueAsString(responseMessage));
        url = RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_LP_RSP_API_PATH + responseMessage.getMessageType()+WfmURLConstants.DEVICE_ID_AMPERSAND+responseMessage.getDeviceSerialNumber(), "", null);
        RestClientUtil.postOrPutBuilder(url, null).post(Entity.entity(responseMessage, MediaType.APPLICATION_JSON));
        
    }
    
    public void setAcknowledgementMessage(AcknowledgementMessage acknowledgementMessage)
        throws HMTPException {
        
        url = RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_LP_ACK_API_PATH + acknowledgementMessage.getMessageType()+WfmURLConstants.DEVICE_ID_AMPERSAND+acknowledgementMessage.getDeviceSerialNumber(), "", null);
        RestClientUtil.postOrPutBuilder(url, null).post(Entity.entity(acknowledgementMessage, MediaType.APPLICATION_JSON));
        
    }
    
    public void setAdaptorResponseMessage(AdaptorResponseMessage adaptorResponseMessage)
        throws HMTPException {
        
        url = RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_MP_ACK_API_PATH + adaptorResponseMessage.getResponse().getMessageType()+WfmURLConstants.DEVICE_ID_AMPERSAND+adaptorResponseMessage.getResponse().getDeviceSerialNumber(), "", null);
        RestClientUtil.postOrPutBuilder(url, null).post(Entity.entity(adaptorResponseMessage, MediaType.APPLICATION_JSON));
        
    }
    
    public void setResponseLP24QBPMessage(String deviceId, ResponseMessage responseMessage)
        throws HMTPException {
       
        url = RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_DPCR_LP24_API_URL + WfmURLConstants.QUESTION_MARK + WfmURLConstants.MESSAGE_TYPE_RESPONSE+WfmURLConstants.DEVICE_TYPE_LP24+WfmURLConstants.SOURCE+WfmURLConstants.DEVICE_ID_AMPERSAND+deviceId, "", null);
        RestClientUtil.postOrPutBuilder(url, null).post(Entity.entity(responseMessage, MediaType.APPLICATION_JSON));
  
    }
    
    public void setupdatewfmprocessHTP(HtpStatusMessage htpStatusMessage)
        throws HMTPException {
        
        url = RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_HTP_STATUS_API_PATH, "", null);
        RestClientUtil.postOrPutBuilder(url, null).post(Entity.entity(htpStatusMessage, MediaType.APPLICATION_JSON));
        
    }
    
    public void setupdatewfmprocessForte(ForteStatusMessage forteStatusMessage)
        throws HMTPException {
        
        url = RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_FORTE_STATUS_API_PATH, "", null);
        RestClientUtil.postOrPutBuilder(url, null).post(Entity.entity(forteStatusMessage, MediaType.APPLICATION_JSON));
        
    }
}
