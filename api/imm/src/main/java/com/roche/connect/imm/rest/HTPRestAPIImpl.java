/*******************************************************************************
 * HTPRestAPIImpl.java Version: 1.0 Authors: Alexander *********************
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
 * ********************* ChangeLog: Alexanders@hcl.com : Updated copyright
 * headers ********************* Description: *********************
 ******************************************************************************/
package com.roche.connect.imm.rest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO.ComplexIdDetailsStatus;
import com.roche.connect.common.htp.status.HTPConstants;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.MessageProcessorService;
import com.roche.connect.imm.service.MessageService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.utils.UrlConstants;

/**
 * The Class HTPRestAPIImpl.
 */
@Component public class HTPRestAPIImpl implements HTPRestAPI {
    
    /** The message processor service. */
    @Autowired private MessageProcessorService messageProcessorService;
    
    @Autowired private RmmIntegrationService rmmService;
    
    /** The message service. */
    @Autowired private MessageService messageService;
    
    @Autowired private AssayIntegrationService assayIntegrationService;
    
    /** The object mapper. */
    private ObjectMapper objectMapper = new ObjectMapper();
    
    /** The logger. */
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
    
    @Value("${connect.adm_host_url}")
    private String admHostURL;
    
    /*
     * (non-Javadoc)
     * @see
     * com.roche.connect.imm.rest.HTPRestAPI#consumeRunInformation(com.roche.
     * connect .common.rmm.dto.RunResultsDTO)
     */
    @Override public Response createRun(RunResultsDTO runResultsDTO) throws HMTPException {
        
        logger.info("HTPRestAPIImpl.createRun RunResultsDTO is: " + runResultsDTO.toString());
        
        try {
            // Validating run protocol with Assay type in AMM
            List<DeviceTestOptionsDTO> deviceTestOptions =
                assayIntegrationService.getDeviceTestOptionsData(AssayType.NIPT_HTP, HTPConstants.DEVICE_NAME);
            String protocolNameFromAMM = deviceTestOptions.get(0).getTestProtocol();
            String protocolFromDevice = null;
            for (RunResultsDetailDTO runResultsDetail: runResultsDTO.getRunResultsDetail()) {
                if (runResultsDetail.getAttributeName().contains("protocol")) {
                    protocolFromDevice = runResultsDetail.getAttributeValue();
                }
            }
            if (protocolFromDevice != null && !protocolFromDevice.equalsIgnoreCase(protocolNameFromAMM)) {
                String token = (String) ThreadSessionManager.currentUserSession()
                    .getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
            List<String> contents = new ArrayList<>();
            contents.add(runResultsDTO.getDeviceRunId());
            contents.add(runResultsDTO.getDeviceId()); 
            AdmNotificationService.sendNotification(NotificationGroupType.INCORRECT_RUN_DETAILS_HTP, contents, token,
                admHostURL+UrlConstants.ADM_NOTIFICATION_URL);
                return Response.status(400).build();
            } else {
                messageService.saveMessage(runResultsDTO);
                
                messageProcessorService.saveRunResult(runResultsDTO);
                
                return Response.status(Status.OK).entity("Run result created successfully.").build();
                
            }
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse RunResult Object from request, message" + e.getMessage(), e);
            return Response.status(Status.BAD_REQUEST).build();
        }
        
    }
    
    /*
     * (non-Javadoc)
     * @see
     * com.roche.connect.imm.rest.HTPRestAPI#submitSampleStatusInfo(java.lang.
     * String)
     */
    @Override public Response updateRun(String runId, RunResultsDTO runResults) throws HMTPException {
        
        try {
            logger.info("HTPRestAPIImpl.updateRun Sample status body is: ---------------- "
                + objectMapper.writeValueAsString(runResults));
            
            runResults.setDeviceRunId(runId);
            messageService.saveMessage(runResults);
            
            messageProcessorService.updateRunResults(runResults);
            
            return Response.status(Status.OK).build();
        } catch (HMTPException e) {
            logger.info("RunId not available, hence sending 404 response");
            return Response.status(Status.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Failed to process the operation, message" + e.getMessage(), e);
            return Response.status(Status.BAD_REQUEST).build();
        }
        
    }
    
    @Override public Response getComplexDetails(String complexId) throws HMTPException {
        logger.info("HTPRestAPIImpl.getComplexDetails: " + complexId);
        ComplexIdDetailsDTO complexIdDetails = messageProcessorService.getComplexIdDetailsByComplexId(complexId);
        if (complexIdDetails != null
            && !complexIdDetails.getStatus().getText().equals(ComplexIdDetailsStatus.INVALID.getText())) {
            return Response.ok(complexIdDetails).build();
        } else if (complexIdDetails == null || complexIdDetails.getStatus().equals(ComplexIdDetailsStatus.INVALID)) {
            return Response.status(404).build();
        } else {
            return Response.status(500).build();
        }
    }
    
    @Override public Response getRunResultsByDeviceRunId(String deviceRunId) {
        logger.info("HTPRestAPIImpl.getRunResultsByDeviceRunId: " + deviceRunId);
        if (deviceRunId != null) {
            RunResultsDTO runResults = null;
            try {
                runResults = rmmService.getRunResultsByDeviceRunId(deviceRunId);
                if (runResults != null)
                    return Response.ok(runResults).build();
                
                return Response.status(Status.NOT_FOUND).build();
            } catch (UnsupportedEncodingException e) {
                logger.info("Exception occured while getting Run resutls from RMM: " + e.getMessage());
                return Response.status(Status.BAD_GATEWAY).build();
            }
        } else {
            return Response.status(404).build();
        }
    }
    
}
