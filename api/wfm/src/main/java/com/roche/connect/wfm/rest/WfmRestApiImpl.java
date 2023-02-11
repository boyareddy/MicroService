/*******************************************************************************
 * WfmRestApiImpl.java Version: 1.0 Authors: somesh_r *********************
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
 * ********************* ********************* Description: Class implementation
 * that provides integration support for OMM. *********************
 ******************************************************************************/
package com.roche.connect.wfm.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.error.ErrorResponse;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.error.QueryValidationException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.SampleWFMStates;
import com.roche.connect.wfm.repository.WfmReadRepository;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;
import com.roche.connect.wfm.service.WFMHTPService;
import com.roche.connect.wfm.writerepository.WfmWriteRepository;

/**
 * Class implementation that provides integration support for OMM.
 */
@Component public class WfmRestApiImpl implements WfmRestApi {
    
    @Autowired private WFMHTPService wfmService;
    @Autowired private WfmReadRepository wfmRepository;
    @Autowired private WfmWriteRepository wfmwriteRepository;
    @Autowired private ObjectMapper objectMapper;
    private ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
    @Autowired private ResponseRenderingService responseRenderingService;
    
 
    
    @Value("${pas.adm_api_url}")
	private String admhosturl;
    
    @Autowired private ResultIntegrationService resultIntegrationService;
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
    @Override public Response getExecuteStartWFM(@RequestBody AdaptorRequestMessage message) throws HMTPException {
        
        AdaptorResponseMessage responseMessage = null;
        logger.info("getExecuteStartWFM()->Execution of getExecuteWFM Start");
        String messageType = message.getMessageType();
        String accessioningId = message.getAccessioningId();
        String deviceId = message.getDeviceSerialNumber();
        String sendingApplicationName = message.getSendingApplicationName();
        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        String token = (String) ThreadSessionManager.currentUserSession()
            .getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
        try {
            if (isEmptyCheck(messageType, accessioningId, deviceId))
                throw new HMTPException("Required fields can't be empty, Please provide valid JSON");
            else {
                activityProcessData.setAdaptorRequestMessage(message);
                activityProcessData.setAccessioningId(accessioningId);
                activityProcessData.setMessageType(messageType);
                activityProcessData.setDeviceId(deviceId);
                //activityProcessData.setSendingApplicationName(sendingApplicationName);
            }
            logger.info("getExecuteStartWFM()::AdaptorRequestMessage: " + objectMapper.writeValueAsString(message));
            SampleWFMStates rocheWfm =
                wfmRepository.findByAccessioningIdAndMessageType(accessioningId, messageType, domainId);
            if (sendingApplicationName.equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString())) {
                resultIntegrationService.updateRunResultStatusByQuery(activityProcessData.getDeviceId(),sendingApplicationName);
                if (rocheWfm != null && rocheWfm.getAccessioningId().equals(accessioningId)) {
                    boolean isMessageType = rocheWfm.getCurrentStatus().equalsIgnoreCase("Query");
                    long ownerId = rocheWfm.getCompany().getId();
                    if (isMessageType) {
                        rocheWfm.setCreatedDatetime(new Date());
                        rocheWfm.setDeviceId(deviceId);
                        wfmwriteRepository.save(rocheWfm, ownerId);
                        responseRenderingService.duplicateOrderResponseforMP(accessioningId, deviceId, message);
                        
                    } else {
                        responseRenderingService.duplicateOrderResponseforMP(accessioningId, deviceId,
                            message.getMessageControlId());
                    }
                } else if (rocheWfm == null
                    && messageType.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())) {
                    logger.info("getExecuteStartWFM()->Begin wfm start process for accessioningId :" + accessioningId
                        + " message type" + messageType);
                    wfmService.startMp24Process(accessioningId, activityProcessData);
                    responseMessage = activityProcessData.getQbpResponseMessage();
                    logger.info(" <- getExecuteStartWFM()::End wfm start process for accessioningId :" + accessioningId);
                    logger.info(" <- getExecuteStartWFM()::AdaptorResponseMessage for RSP "
                        + objectMapper.writeValueAsString(responseMessage));
                } else {
                    logger.warn(" -> getExecuteStartWFM()::WFM-005: MessageType is mismatch in wfm Processes:", messageType);
                    ErrorResponse errorResp = new ErrorResponse();
                    errorResp.setErrorMessage("WFM-005: MessageType is mismatch in wfm Processes:" + messageType);
                    errorResp.setErrorStatusCode(500);
                    return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.NOT_ACCEPTABLE).build();
                }
            } else {
                
                logger.error(" -> getExecuteStartWFM()::WFM-005: MessageType is mismatch in wfm Processes:",messageType);
                ErrorResponse errorResp = new ErrorResponse();
                errorResp.setErrorMessage("WFM-003: Invalid Query Messages:" + sendingApplicationName);
                errorResp.setErrorStatusCode(500);
                return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.NOT_ACCEPTABLE).build();
            }
            logger.info("Response processed for Accessing Id::" + message.getAccessioningId());
        } catch (OrderNotFoundException ex) {            
            logger.error(" -> getExecuteStartWFM()::Order not found ");
            List<String> content = new LinkedList<>();
            content.add(accessioningId);
            content.add(deviceId);
            AdmNotificationService.sendNotification(NotificationGroupType.NO_ORDER_FOUND_MP24, content, token,
					admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
            logger.error(" Notification sent for No Order Found from WFM");
            responseRenderingService.orderNotFoundQueryResponseforMP24(accessioningId, deviceId,
                message.getMessageControlId());
            
        } catch (HMTPException ex) {
            logger.error(" -> getExecuteStartWFM()::" + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("HMTPException while processing Query");
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception ex) {
            logger.error("Common error code for AdaptorRequestMessage " + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("WFM-001: Common Error while processing AdaptorRequestMessage");
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        }
        logger.info(" <- getExecuteStartWFM()");
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    @Override public Response deployWorkflowProcess(@RequestBody AdaptorRequestMessage message) throws HMTPException {
        
        return null;
    }
    
    @Override public Response doTaskComplete(@RequestBody AdaptorRequestMessage message) throws HMTPException {
        
        return null;
        
    }
    
    @Override public Response getTaskCompleteStatus(@RequestBody AdaptorRequestMessage message) throws HMTPException {
        
        return null;
        
    }
    
    @Override public Response getExecuteStatusUpdateWFM(String source, String json) throws QueryValidationException {
        try {
            logger.info(" -> getExecuteStatusUpdateWFM():: update status");
            if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGESOURCE.MPSTATUS.toString())) {
                logger.info(" wfmRestApiImpl-> getExecuteStatusUpdateWFM():: json before mapping" + json);
                AdaptorRequestMessage adaptorRequestMessage = objectMapper.readValue(json, AdaptorRequestMessage.class);
                logger.info("MPSTATUS Request : AdaptorRequestMessage: "
                    + objectMapper.writeValueAsString(adaptorRequestMessage));
                Response response = getExecuteMP24StatusUpdateWFM(adaptorRequestMessage);
                logger.info("MPSTATUS Response : AdaptorRequestMessage: " + objectMapper.writeValueAsString(response));
                return response;
            }
            
            else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGESOURCE.LPQUERY.toString())) {
                
                QueryMessage message = objectMapper.readValue(json, QueryMessage.class);
                logger.info("LPQUERY : QueryMessage: " + objectMapper.writeValueAsString(message));
                Response response = getExecuteLPQueryUpdateWFM(message);
                logger.info("LPQUERY Response: QueryMessage: " + objectMapper.writeValueAsString(response));
                return response;
                
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGESOURCE.LPSTATUS.toString())) {
                
                SpecimenStatusUpdateMessage specimenmessage =
                    objectMapper.readValue(json, SpecimenStatusUpdateMessage.class);
                logger.info(
                    "LPSTATUS : SpecimenStatusUpdateMessage: " + objectMapper.writeValueAsString(specimenmessage));
                Response response = getExecuteLPStatusUpdateWFM(specimenmessage);
                logger.info(
                    "LPQUERY Response: SpecimenStatusUpdateMessage: " + objectMapper.writeValueAsString(response));
                return response;
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGESOURCE.HTPSTATUS.toString())) {
                
                HtpStatusMessage htpStatusMessage = objectMapper.readValue(json, HtpStatusMessage.class);
                logger.info("HTPSTATUS : HtpStatusMessage: " + objectMapper.writeValueAsString(htpStatusMessage));
                Response response = getExecuteHTPStatusUpdateWFM(htpStatusMessage);
                logger.info("HTP Response: HtpStatusMessage: " + objectMapper.writeValueAsString(response));
                return response;
                
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGESOURCE.FORTETATUS.toString())) {
                
                ForteStatusMessage forteStatusMessage = objectMapper.readValue(json, ForteStatusMessage.class);
                logger.info("FORTESTATUS : SpecimenStatusUpdateMessage: "
                    + objectMapper.writeValueAsString(forteStatusMessage));
                Response response = getExecuteFORTEStatusUpdateWFM(forteStatusMessage);
                logger.info(
                    "FORTEQUERY Response: SpecimenStatusUpdateMessage: " + objectMapper.writeValueAsString(response));
                return response;
                
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGESOURCE.MP24UPDATERUNRESULT.toString())) {
                Map<String, Object> requestBody = objectMapper.readValue(json, HashMap.class);
                
                String deviceId = Optional.ofNullable(String.valueOf(requestBody.get("deviceId")))
                    .orElseThrow(() -> new HMTPException("DeviceId is empty"));
                String deviceType = Optional.ofNullable(String.valueOf(requestBody.get("deviceType")))
                    .orElseThrow(() -> new HMTPException("DeviceType is empty"));
                
                resultIntegrationService.updateRunResultStatusByQuery(deviceId,deviceType);
            } else {
                return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
            }
            
        } catch (HMTPException hmtpe) {
            logger.error("Update  Status Update error code " + hmtpe);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("WFM-001: Common Error while processing Query");
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (IOException | OrderNotFoundException e) {
            logger.error("Update Common error code " + e);
        } catch (QueryValidationException e) {
            logger.error("Update  Mp24 error code " + e);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("WFM-002: Some Fields Are mandatory In JSON");
            return Response.status(Status.PARTIAL_CONTENT).entity(errorResp).build();
        }
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    /**
     * Method to update MP24 Status
     * @param message
     * @return Response
     * @throws HMTPException
     * @throws OrderNotFoundException
     * @throws IOException
     */
    public Response getExecuteMP24StatusUpdateWFM(AdaptorRequestMessage message)
        throws HMTPException,
        OrderNotFoundException,
        QueryValidationException,
        IOException {
        logger.info(" -> getExecuteMP24StatusUpdateWFM()::Update MP24 status");
        String accessioningId = message.getAccessioningId();
        String deviceId = message.getDeviceSerialNumber();
        String containerId = message.getContainerId();
        String sendingApplicationName = message.getSendingApplicationName();
        String orderStatus = message.getStatusUpdate().getOrderResult();
        
        if (isEmptyCheck(accessioningId, deviceId, sendingApplicationName, orderStatus))
            throw new HMTPException("Required fields can't be Empty, Please provide valid JSON");
        
        activityProcessData.setAdaptorRequestMessage(message);
        activityProcessData.setAccessioningId(accessioningId);
        activityProcessData.setDeviceId(deviceId);
        activityProcessData.setContainerId(containerId);
        //activityProcessData.setSendingApplicationName(sendingApplicationName);
        
        if (sendingApplicationName.equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString())) {
            wfmService.updateMp24Process(activityProcessData, accessioningId, orderStatus, deviceId);
        }
        logger.info(" <- getExecuteMP24StatusUpdateWFM()");
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    public Response getExecuteLPQueryUpdateWFM(QueryMessage message)
        throws HMTPException,
        OrderNotFoundException{
        
        String messageType = message.getMessageType().toString();
        String deviceId = message.getDeviceSerialNumber();
        String containerId = message.getContainerId();
        String sendingApplicationName = message.getSendingApplicationName();
        String processStepName = message.getProcessStepName();
        
        if (isEmptyCheck(messageType, deviceId, sendingApplicationName, containerId))
            throw new HMTPException(" Required fields cannot be empty, please provide valid JSON");
        
        activityProcessData.setQueryMessage(message);
        activityProcessData.setDeviceId(deviceId);
        activityProcessData.setContainerId(containerId);
        
        try {
            if (processStepName.equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())
                && messageType.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGE_TYPE.QBP.toString())) {
                wfmService.startLp24PrePcrProcess(containerId, deviceId,processStepName,
                    activityProcessData);
                
                return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
                
            } else if (processStepName.equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())
                && messageType.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGE_TYPE.QBP.toString())) {
                wfmService.startLp24PostPcrProcess(containerId, deviceId,processStepName,
                    activityProcessData);
                
                return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
                
            } else if (processStepName.equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.SEQPREP.toString())
                && messageType.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGE_TYPE.QBP.toString())) {
                
                String accessioningId = message.getAccessioningId();
                
                if (isEmptyCheck(accessioningId))
                    throw new HMTPException("Required fields can't be empty, please provide valid JSON");
                
                wfmService.startLp24SeqPrepProcess(containerId, deviceId, accessioningId, processStepName,
                    activityProcessData);
                
            }
            
        } catch (OrderNotFoundException ex) {
            logger.error(" -> getExceucteLPQueryUpdateWFM::Order not found ");
            responseRenderingService.orderNotFoundQueryResponseforLP24(containerId, deviceId, sendingApplicationName,
                message.getMessageControlId());
        } catch (UnsupportedEncodingException e) {
            logger.error("Start Lp24PrePcr error code " + e);
        } catch (HMTPException ex) {
            logger.error(" -> getExceucteLPQueryUpdateWFM()::" + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("HMTPException while processing getExceucteLPQueryUpdateWFM Query");
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception ex) {
            logger.error("Common error code " + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("WFM-001: Common Error while processing Query");
            errorResp.setErrorStatusCode(500);
            logger.error("Common error code " + ex);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    public Response getExecuteLPStatusUpdateWFM(SpecimenStatusUpdateMessage message)
        throws HMTPException,
        OrderNotFoundException,
        QueryValidationException,
        IOException {
        String messageType = message.getMessageType().toString();
        String deviceId = message.getDeviceSerialNumber();
        String containerId = message.getContainerId();
        String accessioningId = message.getAccessioningId();
        String sendingApplicationName = message.getSendingApplicationName();
        String processStepName = message.getProcessStepName();
        String orderStatus = null;
        
        activityProcessData.setSpecimenStatusUpdateMessage(message);
        
        if (isEmptyCheck(messageType, deviceId, containerId, sendingApplicationName, message.getStatusUpdate()))
            throw new HMTPException("Required fields cannot be empty, please provide valid JSON");
        
        orderStatus = message.getStatusUpdate().getOrderResult();
        
        if (isEmptyCheck(orderStatus))
            throw new HMTPException("Required fields cannot be empty, please provide valid JSON");
        
        if (processStepName.equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())) {            
            try {
                wfmService.updateLp24PrePcrProcess(containerId, deviceId, orderStatus, processStepName,
                    activityProcessData);
            } catch (UnsupportedEncodingException e) {
                logger.error("Update Lp24PrePcr error code " + e);
            }

            return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
        } else if (processStepName.equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())) {
            
            try {
                wfmService.updateLp24PostPcrProcess(containerId, deviceId, orderStatus, processStepName,
                    activityProcessData);
            } catch (UnsupportedEncodingException e) {
                logger.error("Update Lp24PostPcr  error code " + e);
            }
            
            return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
        } else if (processStepName.equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.SEQPREP.toString())) {
            wfmService.updateLP24SeqPP(accessioningId, containerId, deviceId, orderStatus, processStepName,
                activityProcessData);
            return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
            
        }
        return null;
    }
    
    public Response getExecuteHTPStatusUpdateWFM(HtpStatusMessage message) {
        ActivityProcessDataDTO activityData = new ActivityProcessDataDTO();
        String accessioningId = message.getAccessioningId();
        String sendingApplication = message.getSendingApplication();
        Long runResultsId = message.getRunResultsId();
        Long orderId = message.getOrderId();
        String status = message.getStatus();
        String deviceRunId = message.getDeviceRunId();
        activityData.setHtpStatusMessage(message);
        if (sendingApplication.equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.HTP.toString())) {
            
            wfmService.updateHTPProcess(accessioningId,runResultsId, orderId,status,deviceRunId,activityData);
            
            return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
        }
        return null;
    }
    
    public Response getExecuteFORTEStatusUpdateWFM(ForteStatusMessage message)
        throws JsonProcessingException,
        QueryValidationException {
        
        logger.info("WFMService ::=> getExceucteFORTEStatusUpdateWFM()");
        ErrorResponse errorResp = new ErrorResponse();
        try {
            return wfmService.updateForteProcess(message.getAccessioningId(),message.getStatus(), message.getSendingApplication(), message.getJobType())
                    ? Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build()
                    : Response.status(Status.CONFLICT)
                        .entity(objectMapper.writeValueAsString("WFM:- ERROR while updating FORTE status ")).build();
        } catch (HMTPException e) {
            logger.error("WFM ::=> ERR:-", e.getMessage());
            errorResp.setErrorMessage("WFM-004: " + e.getMessage());
            errorResp.setErrorStatusCode(400);
        } catch (QueryValidationException e) {
            logger.error("Update  FORTE error code " + e);
            ErrorResponse errorResp1 = new ErrorResponse();
            errorResp.setErrorMessage("WFM-002: Invalid status");
            return Response.status(Status.PARTIAL_CONTENT).entity(errorResp1).build();
        }
        
        return Response.status(Status.CONFLICT).entity(errorResp).build();
    }
    
    /**
     * Method to check whether given string is empty.
     * @param args
     * @return
     */
    public boolean isEmptyCheck(Object... args) {
        for (Object arg: args) {
            if (arg instanceof String) {
                if (StringUtils.isEmpty(arg)) {
                    return true;
                }
            } else {
                if (Objects.isNull(arg)) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
