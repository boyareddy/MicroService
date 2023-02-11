/*******************************************************************************
 * WfmRestApiImpl.java Version: 1.0 Authors: narasimhareddyb *********************
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
 * ********************* ********************* Description: Class implementation
 * that provides integration support for OMM. *********************
 ******************************************************************************/
package com.roche.connect.wfm.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.dpcr_analyzer.WFMAcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMORUMessage;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp96.WFMACKMessage;
import com.roche.connect.common.mp96.WFMQueryMessage;
import com.roche.connect.common.mp96.WFMQueryResponseMessage;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.error.ErrorResponse;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.SampleWFMStates;
import com.roche.connect.wfm.repository.WfmReadRepository;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;
import com.roche.connect.wfm.service.WFMDPCRService;

/**
 *  Class that helps to find  WfmdPCRRestApiImpl.
 */
@Component public class WfmdPCRRestApiImpl implements WfmdPCRRestApi {
    
    @Autowired private WFMDPCRService wfmdPCRService;
    @Autowired private WfmReadRepository wfmRepository;
    @Autowired private ObjectMapper objectMapper;
    private ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
    @Autowired private ResponseRenderingService responseRenderingService;
    @Autowired private ResultIntegrationService resultIntegrationService;
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

    
    /**
     * Method to Strat dpcr
     * @param message
     * @return Response
     */
    @Override public Response getExecutedPCRStartWFM(@RequestBody WFMQueryMessage message) {
        WFMQueryResponseMessage responseMessage = null;
        logger.info("getExecutedPCRStartWFM()->Execution of getExecutedPCRStartWFM Start"); 
        String messageType = message.getMessageType();
        String accessioningId = message.getAccessioningId();
        String deviceId = message.getDeviceSerialNumber();
        String sendingApplicationName = message.getSendingApplicationName();
        long domainId=ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        try {
            
            if (isEmptyCheck(messageType, accessioningId, deviceId))
                throw new HMTPException("Required fields can't be empty, Please provide valid JSON");
            else {
                activityProcessData.setAccessioningId(accessioningId);
                activityProcessData.setMessageType(messageType);
                activityProcessData.setDeviceId(deviceId);
            }
            
            logger.info("getExecutedPCRStartWFM()::AdaptorRequestMessage: " + objectMapper.writeValueAsString(message));
            SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId, WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(),domainId);
            if (sendingApplicationName.equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP96.toString())) {
                if (rocheWfm == null
                    && messageType.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString())) {
                    logger.info("getExecutedPCRStartWFM()->Begin wfm start process for accessioningId :" 
                        + accessioningId + " message type" + messageType);
                    wfmdPCRService.startMp96Process(accessioningId, activityProcessData);
                    responseMessage = activityProcessData.getQbpResponseMsg();
                    logger.info(
                        " <- getExecutedPCRStartWFM()::End wfm start process for accessioningId :" + accessioningId);
                    logger.info(" <- getExecutedPCRStartWFM()::AdaptorResponseMessage for RSP "
                        + objectMapper.writeValueAsString(responseMessage));
                } else if (rocheWfm != null && rocheWfm.getAccessioningId().equals(accessioningId)) {
                    logger.warn(" -> getExecutedPCRStartWFM()::Accessioning Id is in process, already exist", accessioningId);
                    return Response.ok().status(Status.OK).build();
                    
                } else {
                    logger.warn(" -> getExecutedPCRStartWFM()::WFM-005: MessageType is mismtach in wfm Processes:",
                        messageType);
                    ErrorResponse errorResp = new ErrorResponse();
                    errorResp.setErrorMessage("WFM-005: MessageType is mismtach in wfm Processes:" + messageType);
                    errorResp.setErrorStatusCode(500);
                    return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.NOT_ACCEPTABLE).build();
                }
            } else {
                logger.error(" -> getExecutedPCRStartWFM()::WFM-005: MessageType is mismtach in wfm Processes:",
                    messageType);
                ErrorResponse errorResp = new ErrorResponse();
                errorResp.setErrorMessage("WFM-003: Invalid Query Messages:" + sendingApplicationName);
                errorResp.setErrorStatusCode(500);
                return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.NOT_ACCEPTABLE).build();
            }
            logger.info("Response processed for Accessing Id::" + message.getAccessioningId());
        } catch (OrderNotFoundException ex) {
            
            logger.error(" -> getExecutedPCRStartWFM()::Order not found ::::");
            responseRenderingService.orderNotFoundQueryResponseforMP(accessioningId, deviceId);
            
        } catch (HMTPException ex) {
            logger.error(" -> getExecutedPCRStartWFM()::" + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage(WfmConstants.ERROR_RESPONSE.HMTPEXCEPTIONMSG.toString());
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception ex) {
            logger.error("Exception at ->getExecutedPCRStartWFM"+WfmConstants.ERROR_RESPONSE.COMMERRORCODE.toString() + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage(WfmConstants.ERROR_RESPONSE.ERRORPROCESSQRY.toString());
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        }
        logger.info(" <- getExecutedPCRStartWFM()");
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build(); 
    }
    
    
    /**
     * Method to update Status of dPCR
     * @param source
     * @param json
     * @return Response
     */
    
    @Override public Response getExecutedPCRStatusUpdateWFM(String source, String json) {
        
        try {
            logger.info(" -> getExecutedPCRStatusUpdateWFM():: update status");
            if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_dPCR_MESSAGESOURCE.MP96STATUS.toString())) {
                
                WFMoULMessage u03RequestMessage = objectMapper.readValue(json, WFMoULMessage.class);
                logger.info(
                    "MPSTATUS Request : U03RequestMessage: " + objectMapper.writeValueAsString(u03RequestMessage));
                Response response = getExecuteMP96StatusUpdateWFM(u03RequestMessage);
                logger.info("MPSTATUS Response : U03RequestMessage: " + objectMapper.writeValueAsString(response));
                return response;
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_dPCR_MESSAGESOURCE.ACKMP96.toString())) { 
                
                WFMACKMessage aCKMessage = objectMapper.readValue(json, WFMACKMessage.class);
                
                logger.info("MPACK Request : U03RequestMessage: " + objectMapper.writeValueAsString(aCKMessage));
                Response response = getExecuteMP96ACK(aCKMessage);
                logger.info("MPACK Response : U03RequestMessage: ");
                return response;
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_dPCR_MESSAGESOURCE.LPQUERY.toString())) {
                
                QueryMessage message = objectMapper.readValue(json, QueryMessage.class);
                logger.info("LP QUERY : QueryMessage: " + objectMapper.writeValueAsString(message));
                Response response = getExecuteLPQueryUpdateWFM(message);
                logger.info("LP QUERY Response: QueryMessage: " + objectMapper.writeValueAsString(response));
                return response;
                
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_dPCR_MESSAGESOURCE.LPSTATUS.toString())) {
                
                SpecimenStatusUpdateMessage specimenmessage =
                    objectMapper.readValue(json, SpecimenStatusUpdateMessage.class);
                logger.info(
                    "LPSTATUS : SpecimenStatusUpdateMessage: " + objectMapper.writeValueAsString(specimenmessage));
                Response response = getExecutedPCRLP24StatusUpdateWFM(specimenmessage);
                logger.info(
                    "LPSTATUS Response: SpecimenStatusUpdateMessage: " + objectMapper.writeValueAsString(response));
                return response;
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGESOURCE.LP24UPDATERUNRESULT.toString())) {
                
                Map<String, Object> requestBody = objectMapper.readValue(json, HashMap.class);
                
                String deviceId = Optional.ofNullable(String.valueOf(requestBody.get("deviceId")))
                    .orElseThrow(() -> new HMTPException("DeviceId is empty"));
                String deviceType = Optional.ofNullable(String.valueOf(requestBody.get("deviceType")))
                    .orElseThrow(() -> new HMTPException("DeviceType is empty"));
                
                resultIntegrationService.updateRunResultStatusByQuery(deviceId,deviceType);
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_dPCR_MESSAGESOURCE.DPCRANALYZER.toString())) {
                
                com.roche.connect.common.dpcr_analyzer.WFMQueryMessage message = objectMapper.readValue(json, com.roche.connect.common.dpcr_analyzer.WFMQueryMessage.class);
                logger.info(
                    "DPCRANALYZER QUERY Request : QueryMessage: " + objectMapper.writeValueAsString(message));
                Response response = getExecutedPCRAnalyzerQueryUpdateWFM(message);
                logger.info(
                    "DPCRANALYZER QUERY Response: QueryMessage: " + objectMapper.writeValueAsString(response));
                return response;
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_dPCR_MESSAGESOURCE.ACKANALYZER.toString())) { 
                
            	WFMAcknowledgementMessage aCKMessage = objectMapper.readValue(json, WFMAcknowledgementMessage.class);
                logger.info("DPCRANALYZER ACK Request : ACKmssagefrom Imm: " + objectMapper.writeValueAsString(aCKMessage));
                Response response = getExecuteAnalyzerACK(aCKMessage);
                logger.info("DPCRANALYZER ACK Response : AckRequestMessage: " + objectMapper.writeValueAsString(response));
                return response;
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_dPCR_MESSAGESOURCE.DPCRANALYZERORU.toString())) { 
                
            	WFMORUMessage oRUMessage = objectMapper.readValue(json, WFMORUMessage.class);
                logger.info("DPCRANALYZER ORU Request : ORUmssagefrom Imm: " + objectMapper.writeValueAsString(oRUMessage));
                Response response = getExecutedPCRORUUpdateWFM(oRUMessage);
                logger.info("DPCRANALYZER ORU Response : ORUmssagefrom: "+ objectMapper.writeValueAsString(response));
                return response;
            } else if (source.equalsIgnoreCase(WfmConstants.WORKFLOW_dPCR_MESSAGESOURCE.DPCRSTATUS.toString())) {
                
                WFMESUMessage message = objectMapper.readValue(json, WFMESUMessage.class);
                logger.info(
                    "DPCRANALYZER ESU Request: EquipmentStatusMessage: " + objectMapper.writeValueAsString(message));
                Response response = getExecutedPCRAnalyzerStatusUpdateWFM(message);
                logger.info(
                    "DPCRANALYZER ESU Response: EquipmentStatusMessage: " + objectMapper.writeValueAsString(response));
                return response;
            } else {
                logger.info(" -> WORKFLOW_dPCR_MESSAGESOURCE: source Type is mismtach in wfm Processes:" + source);
                ErrorResponse errorResp = new ErrorResponse();
                errorResp
                    .setErrorMessage("WORKFLOW_dPCR_MESSAGESOURCE: source Type is mismtach in wfm Processes:" + source);
                errorResp.setErrorStatusCode(500);
                return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.NOT_ACCEPTABLE).build();
            }
            
        } catch (HMTPException hmtpe) {
            logger.error("Update  Mp96 error code " + hmtpe);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage(WfmConstants.ERROR_RESPONSE.ERRORPROCESSQRY.toString()); 
            errorResp.setErrorStatusCode(500);
            logger.error(WfmConstants.ERROR_RESPONSE.COMMERRORCODE.toString() + hmtpe);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (IOException | OrderNotFoundException e) {
            logger.error("Update  Mp96 error code " + e);
        }
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    /**
     * Method to update MP96 Status
     * @param message
     * @return Response
     * @throws HMTPException
     * @throws OrderNotFoundException
     */
    
    public Response getExecuteMP96StatusUpdateWFM(WFMoULMessage message) throws HMTPException, OrderNotFoundException {
        logger.info(" -> getExecuteMP96StatusUpdateWFM()::Update MP96 status");
        String accessioningId = message.getAccessioningId();
        String deviceId = message.getDeviceSerialNumber();
        String containerId = message.getOulSampleResultMessage().getOutputContainerId();
        String sendingApplicationName = message.getSendingApplicationName();
        String orderStatus = null;
        String sampleStatus = message.getOulSampleResultMessage().getSampleResult();
        if (sampleStatus.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.P.toString()))
        {
            orderStatus=WfmConstants.WORKFLOW_STATUS.PASSED.toString();
        }
        else if (sampleStatus.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.F.toString()))
        {
            orderStatus=WfmConstants.WORKFLOW_STATUS.FAILED.toString();
        }
        if (isEmptyCheck(accessioningId, deviceId, sendingApplicationName, orderStatus))
            throw new HMTPException("Required fields can't be Empty, Please provide valid JSON");
        
        activityProcessData.setU03RequestMessage(message);
        activityProcessData.setAccessioningId(accessioningId);
        activityProcessData.setDeviceId(deviceId);
        activityProcessData.setContainerId(containerId);
        
        if (sendingApplicationName.equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP96.toString())) {
            wfmdPCRService.updateMp96Process(activityProcessData, accessioningId, orderStatus, deviceId);
        }
        logger.info(" <- getExecuteMP96StatusUpdateWFM()");
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    /**
     * Method to update MP96 Status
     * @param message
     * @return Response
     * @throws HMTPException
     * @throws OrderNotFoundException
     */
    
    public Response getExecuteMP96ACK(WFMACKMessage message) {
        logger.info(" -> getExecuteMP96ACK()::Update MP96 status");
        String accessioningId = message.getAccessioningId();
        wfmdPCRService.updatemp96Ack(activityProcessData, accessioningId);
        logger.info(" <- getExecuteMP96ACK()");
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    /**
     * Method to update AnalyzerACK Status
     * @param message
     * @return Response
     * @throws HMTPException
     
     */
    public Response getExecuteAnalyzerACK(WFMAcknowledgementMessage message) throws HMTPException {
    	logger.info(" -> getExecute AnalyzerACK");
        String accessioningId = message.getAccessioningId();
        wfmdPCRService.updatedPCRAnalyzerAck(activityProcessData,accessioningId);
        logger.info(" <- getExecuteAnalyzerACK()");
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    
    /**
     * Method to update LP24 Status
     * @param message
     * @return Response
     * @throws HMTPException
     * @throws OrderNotFoundException
     * @throws UnsupportedEncodingException
     */
    public Response getExecuteLPQueryUpdateWFM(QueryMessage message)
        throws HMTPException,
        OrderNotFoundException {
        
        String messageType = message.getMessageType().toString();
        String deviceId = message.getDeviceSerialNumber();
        String containerId = message.getContainerId();
        String accessioningId = message.getAccessioningId();
        String sendingApplicationName = message.getSendingApplicationName();
        
        if (isEmptyCheck(messageType, deviceId, sendingApplicationName,accessioningId,containerId))
            throw new HMTPException(" Required fields cannot be empty, please provide valid JSON");
        
        activityProcessData.setQueryMessage(message);
        activityProcessData.setDeviceId(deviceId);
        
        try {
            
            if (sendingApplicationName.equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString())
                && messageType.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGE_TYPE.QBP.toString())) {
               resultIntegrationService.updateRunResultStatusByQuery(activityProcessData.getDeviceId(),sendingApplicationName);
                wfmdPCRService.startdPCRLp24Process(containerId,accessioningId, deviceId, activityProcessData);
                return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
                
            }
            
        } catch (OrderNotFoundException ex) {
            logger.error(" -> getExceucteLPQueryUpdateWFM::Order not found ", ex);
            responseRenderingService.orderNotFoundQueryResponsefordPCRLP(containerId,accessioningId, deviceId, sendingApplicationName);
        } catch (HMTPException ex) {
            logger.error(" -> Exception at getExceucteLPQueryUpdateWFM()" + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("Exception at getExceucteLPQueryUpdateWFM()"+WfmConstants.ERROR_RESPONSE.HMTPEXCEPTIONMSG.toString());
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        }catch (Exception ex) {
            logger.error("Exception at getExceucteLPQueryUpdateWFM():::"+WfmConstants.ERROR_RESPONSE.COMMERRORCODE.toString() + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("Exception at getExceucteLPQueryUpdateWFM()::"+WfmConstants.ERROR_RESPONSE.ERRORPROCESSQRY.toString());
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    /**
     * Method to update LP24 Status
     * @param message
     * @return Response
     * @throws HMTPException
     * @throws OrderNotFoundException
     * @throws UnsupportedEncodingException
     */
    public Response
        getExecutedPCRAnalyzerQueryUpdateWFM(com.roche.connect.common.dpcr_analyzer.WFMQueryMessage message)
            throws  HMTPException {
        
        String messageType = message.getMessageType();
        String deviceId = message.getDeviceId();
        String containerId = message.getContainerId();
        String containerPosition = message.getContainerPosition();
        String accessioningId = message.getAccessioningId();
        String processStepName = message.getProcessStepName();
        
        if (isEmptyCheck(messageType, deviceId, accessioningId,processStepName,containerId,containerPosition))
            throw new HMTPException(" Required fields cannot be empty, please provide valid JSON");
        
        activityProcessData.setWfmQueryMessage(message);
        activityProcessData.setDeviceId(deviceId);
        
        try {
            
            if (messageType.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGE_TYPE.ANALYZERQBP.toString())) {
                wfmdPCRService.startdPCRAnalyzerProcess(containerId, containerPosition, accessioningId, deviceId, activityProcessData);
                return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
            }
            
        }  catch (Exception ex) {
            logger.error(WfmConstants.ERROR_RESPONSE.COMMERRORCODE.toString() + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("getExceuctedPCRAnalyzerQueryUpdateWFM::"+WfmConstants.ERROR_RESPONSE.ERRORPROCESSQRY.toString());
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    public Response getExecutedPCRORUUpdateWFM(WFMORUMessage oRUmessage)
            throws HMTPException,
            OrderNotFoundException
             {
            String messageType = oRUmessage.getMessageType();
            String deviceId = oRUmessage.getDeviceId();
            String accessioningId = oRUmessage.getAccessioningId();
            
            
            
            activityProcessData.setoRUMessage(oRUmessage);
            try {  
            	if (isEmptyCheck(accessioningId,messageType,deviceId))
                    throw new HMTPException("Required fields from getExecutedPCRORUUpdateWFM cannot be empty, please provide valid JSON");
                    wfmdPCRService.updatedPCRORUProcess( accessioningId,deviceId, activityProcessData);
            }catch (HMTPException e) {
                        logger.error("Update dPCRAnalyzer error code " + e);
                    }
                               
                return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();                            
        }
    
    /**
     * Method to update MP96 Status
     * @param message
     * @return Response
     * @throws HMTPException
     * @throws OrderNotFoundException
     */
    
    public Response getExecutedPCRAnalyzerStatusUpdateWFM(WFMESUMessage message) throws HMTPException, OrderNotFoundException {
        logger.info(" -> getExecutedPCRAnalyzerStatusUpdateWFM()::Update dPCR Analyzer status");
        
        String messageType = message.getMessageType();
        String deviceId = message.getDeviceId();
        String accessioningId = message.getAccessioningId();
        String orderStatus = message.getStatus();
        
        activityProcessData.setWfmESUMessage(message);
        
        try {
        if (isEmptyCheck(accessioningId,messageType,deviceId,orderStatus))
            throw new HMTPException("Required fields from getExecutedPCRAnalyzerStatusUpdateWFM cannot be empty, please provide valid JSON");
        
        if (messageType.equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGE_TYPE.ANALYZERSSU.toString())) {
            try {
                wfmdPCRService.updatedPCRAnalyzerProcess(accessioningId,orderStatus,activityProcessData);
            } catch (UnsupportedEncodingException e) {
                logger.error("Update dPCRAnalyzer error code " + e);
            }
            
            return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
            
        }
        }catch (OrderNotFoundException ex) {
            logger.error(" -> getExecutedPCRAnalyzerStatusUpdateWFM::Order not found ");
            
        } catch (HMTPException ex) {
            logger.error(" -> HMTPException ::getExecutedPCRAnalyzerStatusUpdateWFM()::" + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("HMTPException while processing getExecutedPCRAnalyzerStatusUpdateWFM");
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception ex) {
            logger.error(WfmConstants.ERROR_RESPONSE.COMMERRORCODE.toString() + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("WFM-001: Common Error while processing statusupdate:getExecutedPCRAnalyzerStatusUpdateWFM");
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
    }
    
    /**
     * Method to update dPCRLP24 Status
     * @param message
     * @return Response
     * @throws HMTPException
     * @throws OrderNotFoundException
     * @throws UnsupportedEncodingException
     */
    
    public Response getExecutedPCRLP24StatusUpdateWFM(SpecimenStatusUpdateMessage message)
        throws HMTPException,
        OrderNotFoundException {
        String messageType = message.getMessageType().toString();
        String deviceId = message.getDeviceSerialNumber();
        String accessioningId = message.getAccessioningId();
        String sendingApplicationName = message.getSendingApplicationName();
        String orderStatus = null;
        
        activityProcessData.setSpecimenStatusUpdateMessage(message);
        try {
        if (isEmptyCheck(messageType, deviceId, sendingApplicationName, message.getStatusUpdate()))
            throw new HMTPException("Required fields from getExecutedPCRLP24StatusUpdateWFM  cannot be empty, please provide valid JSON");
        
        orderStatus = message.getStatusUpdate().getOrderResult();
        
        if (isEmptyCheck(orderStatus))
            throw new HMTPException("Required orderStatus fields cannot be empty, please provide valid JSON");
        
        if (sendingApplicationName
            .equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString())) {
            try {
                wfmdPCRService.updatedPCRLp24Process(accessioningId,deviceId,orderStatus, activityProcessData);
            } catch (UnsupportedEncodingException e) {
                logger.error("Update dPCRLp24 error code " + e);
            }
            
            return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
            
        }
        }catch (OrderNotFoundException ex) {
            logger.error(" -> getExecutedPCRLP24StatusUpdateWFM::Order not found ");
            responseRenderingService.orderNotFoundUO3ResponseFordPCRLP24(activityProcessData);
        } catch (HMTPException ex) {
            logger.error(" ->HMTPException-- getExecutedPCRLP24StatusUpdateWFM()::" + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("HMTPException while processing getExecutedPCRLP24StatusUpdateWFM statusupdate");
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception ex) {
            logger.error(WfmConstants.ERROR_RESPONSE.COMMERRORCODE.toString() + ex);
            ErrorResponse errorResp = new ErrorResponse();
            errorResp.setErrorMessage("WFM-001: Common Error while processing statusupdate::getExecutedPCRLP24StatusUpdateWFM");
            errorResp.setErrorStatusCode(500);
            return Response.ok(errorResp, MediaType.APPLICATION_JSON).status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString()).status(200).build();
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
