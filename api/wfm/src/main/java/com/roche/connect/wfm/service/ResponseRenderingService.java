/*******************************************************************************
 * ResponseRenderingService.java Version: 1.0 Authors: somesh_r
 * ********************* Copyright (c) 2018 Roche Sequencing Solutions (RSS) -
 * CONFIDENTIAL All Rights Reserved. NOTICE: All information contained herein
 * is, and remains the property of COMPANY. The intellectual and technical
 * concepts contained herein are proprietary to COMPANY and may be covered by
 * U.S. and Foreign Patents, patents in process, and are protected by trade
 * secret or copyright law. Dissemination of this information or reproduction of
 * this material is strictly forbidden unless prior written permission is
 * obtained from COMPANY. Access to the source code contained herein is hereby
 * forbidden to anyone except current COMPANY employees, managers or contractors
 * who have executed Confidentiality and Non-disclosure agreements explicitly
 * covering such access The copyright notice above does not evidence any actual
 * or intended publication or disclosure of this source code, which includes
 * information that is confidential and/or proprietary, and is a trade secret,
 * of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS
 * WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF
 * APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS
 * SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS TO
 * REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR
 * SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * ********************* ChangeLog: V.Prem@hcl.com : Updated copyright headers
 * ********************* ********************* Description: write me
 * *********************
 ******************************************************************************/
package com.roche.connect.wfm.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.common.mp24.message.ContainerInfo;
import com.roche.connect.common.mp24.message.RSPMessage;
import com.roche.connect.common.mp24.message.Response;
import com.roche.connect.common.mp24.message.SampleInfo;
import com.roche.connect.common.mp24.message.StatusUpdate;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.ASSAY_PROCESS_STEP_DATA;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_MESSAGE_TYPE;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_RESPONSE;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_VARIABLES;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;

@Service public class ResponseRenderingService {
    ImmIntegrationService immIntegrationService = new ImmIntegrationService();
    private ActivityProcessDataDTO activityProcessData = null;
    AssayIntegrationService  assayIntegrationService = new AssayIntegrationService();
    OrderIntegrationService orderIntegrationService = new OrderIntegrationService();
   
    
    @Value("${pas.adm_api_url}")
	private String admhosturl;
    
    
    
    String token = (String) ThreadSessionManager.currentUserSession().getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
    public void orderNotFoundQueryResponseforLP24(String containerId, String deviceId, String sendingApplicationName,
        String messageControlId) {
        try {
            
            ResponseMessage responseMessage = new ResponseMessage();
            activityProcessData = new ActivityProcessDataDTO();
            
            responseMessage.setStatus(WORKFLOW_STATUS.NOORDER.toString());
            responseMessage.setErrors(new ArrayList<String>());
            responseMessage.setSendingApplicationName(WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            responseMessage.setReceivingApplication(sendingApplicationName);
            responseMessage.setMessageType(EnumMessageType.ResponseMessage);
            responseMessage.setContainerId(containerId);
            responseMessage.setDeviceSerialNumber(deviceId);
            responseMessage.setOrderControl("DC");
            responseMessage.setMessageControlId(messageControlId);
            activityProcessData.setResponseMessage(responseMessage);
            immIntegrationService.setResponseMessage(responseMessage);
            
        } catch (Exception ex) {
            logger.error("ERR:- ResponseRenderingService <= orderNotFoundQueryResponseforLP() ", ex);
        }
    }
    
    public void orderNotFoundQueryResponseforLP(String containerId, String deviceId, String sendingApplicationName) {
        try {
            
            ResponseMessage responseMessage = new ResponseMessage();
            activityProcessData = new ActivityProcessDataDTO();
            
            responseMessage.setStatus(WORKFLOW_STATUS.NOORDER.toString());
            responseMessage.setErrors(new ArrayList<String>());
            responseMessage.setSendingApplicationName(WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            responseMessage.setReceivingApplication(sendingApplicationName);
            responseMessage.setMessageType(EnumMessageType.ResponseMessage);
            responseMessage.setContainerId(containerId);
            responseMessage.setDeviceSerialNumber(deviceId);
            responseMessage.setOrderControl("DC");
            activityProcessData.setResponseMessage(responseMessage);
            immIntegrationService.setResponseMessage(responseMessage);
            
        } catch (Exception ex) {
            logger.error("ERR:- ResponseRenderingService <= orderNotFoundQueryResponseforLP() ", ex);
        }
    }
    
    public void duplicateOrderQueryResponseforLP(String containerId, String deviceId, String sendingApplicationName,
        String messageControlId) {
        try {
            
            ResponseMessage responseMessage = new ResponseMessage();
            activityProcessData = new ActivityProcessDataDTO();
            
            responseMessage.setStatus(WORKFLOW_VARIABLES.DUPLICATE.toString());
            responseMessage.setErrors(new ArrayList<String>());
            responseMessage.setSendingApplicationName(WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            responseMessage.setReceivingApplication(sendingApplicationName);
            responseMessage.setMessageType(EnumMessageType.ResponseMessage);
            responseMessage.setContainerId(containerId);
            responseMessage.setDeviceSerialNumber(deviceId);
            responseMessage.setMessageControlId(messageControlId);
            responseMessage.setOrderControl(WORKFLOW_VARIABLES.DC.toString());
            activityProcessData.setResponseMessage(responseMessage);
            immIntegrationService.setResponseMessage(responseMessage);
            
        } catch (Exception ex) {
            logger.error("ERR:- ResponseRenderingService <= duplicateOrderQueryResponseforLP() ", ex);
        }
    }
    
    public void duplicateOrderQueryPositiveResponseforLp24(String containerId, String deviceId, String accessioningId,
        ActivityProcessDataDTO activityProcessData) {
        String protocolName = "";
        List<String> errors = null;
        List<DeviceTestOptionsDTO> deviceTestOptions = null;
        try {
            logger.info("ActivityProcessDataDTO--------->" + activityProcessData.getAccessioningId());
            ResponseMessage responseMessage = new ResponseMessage();
            if (activityProcessData.getQueryMessage().getProcessStepName()
                .equalsIgnoreCase(ASSAY_PROCESS_STEP_DATA.PREPCR.toString())) {
                deviceTestOptions = assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                    accessioningId, ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.PREPCR.toString());
            } else if (activityProcessData.getQueryMessage().getProcessStepName()
                .equalsIgnoreCase(ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())) {
                deviceTestOptions =
                    assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(accessioningId,
                        ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.POSTPCR.toString());
            }
            protocolName = deviceTestOptions != null ? deviceTestOptions.get(0).getTestProtocol() : null;
            
            responseMessage.setStatus(WORKFLOW_RESPONSE.SUCCESS_1.toString());
            responseMessage.setErrors(errors);
            responseMessage.setDeviceSerialNumber(deviceId);
            responseMessage.setSendingApplicationName(WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            responseMessage.setReceivingApplication(activityProcessData.getQueryMessage().getSendingApplicationName());
            responseMessage.setDateTimeMessageGenerated(new Date().toString());
            responseMessage.setMessageType(EnumMessageType.ResponseMessage);
            responseMessage.setAccessioningId(accessioningId);
            responseMessage.setContainerId(containerId);
            responseMessage.setSampleType(WORKFLOW_VARIABLES.PLASMA.toString());
            responseMessage.setSpecimenDescription(WORKFLOW_VARIABLES.PLASMA.toString());
            responseMessage.setQueryResponseStatus(WORKFLOW_VARIABLES.AA.toString());
            responseMessage.setProtocolName(protocolName);
            responseMessage.setMessageControlId(activityProcessData.getQueryMessage().getMessageControlId());
            immIntegrationService.setResponseMessage(responseMessage);
        } catch (Exception e) {
            logger.error(" -> execute()::Error occurred, ", e);
        }
    }
    
    public void orderNotFoundQueryResponsefordPCRLP(String containerId, String accessioningId, String deviceId,
        String sendingApplicationName) {
        try {
            ResponseMessage responseMessage = new ResponseMessage();
            activityProcessData = new ActivityProcessDataDTO();
            responseMessage.setStatus(WORKFLOW_STATUS.NOORDER.toString()); 
            responseMessage.setErrors(new ArrayList<String>());
            responseMessage.setSendingApplicationName(WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            responseMessage.setReceivingApplication(sendingApplicationName);
            responseMessage.setMessageType(EnumMessageType.ResponseMessage);
            responseMessage.setContainerId(containerId);
            responseMessage.setAccessioningId(accessioningId);
            responseMessage.setDeviceSerialNumber(deviceId);
            responseMessage.setOrderControl(WORKFLOW_VARIABLES.DC.toString());
            activityProcessData.setResponseMessage(responseMessage);
            
            List<String>  errormessages= new LinkedList<>();
            errormessages.add(accessioningId);
            errormessages.add(deviceId);
            immIntegrationService.setResponseLP24QBPMessage(deviceId, responseMessage);
            AdmNotificationService.sendNotification(NotificationGroupType.NO_ORDER_FOUND_LP24, errormessages, token,
					admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
            logger.info("Notification sent successfully! from WFM side");

            
        } catch (Exception ex) {
            logger.error("ERR:- ResponseRenderingService <= orderNotFoundQueryResponsefordPCRLP() ", ex);
        }
    }
    
    public void orderNotFoundUO3ResponseFordPCRLP24(ActivityProcessDataDTO activityProcessData) throws HMTPException {
        AdaptorResponseMessage adaptorRspMsg = new AdaptorResponseMessage();
        Response response = new Response();
        RSPMessage rspMessage = new RSPMessage();
        StatusUpdate statusUpdate = new StatusUpdate();
        try{
        adaptorRspMsg.setStatus(WfmConstants.WORKFLOW_RESPONSE.AR.toString());
        response.setMessageControlId(activityProcessData.getSpecimenStatusUpdateMessage().getMessageControlId());
        response.setDeviceSerialNumber(activityProcessData.getSpecimenStatusUpdateMessage().getDeviceSerialNumber());
        response.setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
        response.setReceivingApplication(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString());
        response.setAccessioningId(activityProcessData.getSpecimenStatusUpdateMessage().getAccessioningId());
        response.setDateTimeMessageGenerated(new Date().toString());
        response.setMessageType(WfmConstants.WORKFLOW_RESPONSE.U03ACK.toString());
        response.setRspMessage(rspMessage);
        response.setStatusUpdate(statusUpdate);
        adaptorRspMsg.setResponse(response);
        logger.info(" -> orderNotFoundUO3ResponseFordPCRLP24::Ack sent as " + response + ",for the method " + activityProcessData.getSpecimenStatusUpdateMessage().getAccessioningId());
        immIntegrationService.setAdaptorResponseMessage(adaptorRspMsg);
        List<String>  errormessages= new LinkedList<>();
        errormessages.add(activityProcessData.getSpecimenStatusUpdateMessage().getAccessioningId());
        errormessages.add(activityProcessData.getSpecimenStatusUpdateMessage().getDeviceSerialNumber());
        AdmNotificationService.sendNotification(NotificationGroupType.NO_ORDER_FOUND_LP24, errormessages, token,
				admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
        }
        catch(Exception e){
            logger.error("ERR:- ResponseRenderingService <= orderNotFoundUO3ResponseFordPCRLP24 ", e);
        }
    }
    
    public void orderNotFoundQueryResponseforMP24(String accessioningId, String deviceId, String messageControlId) {
        try {
            activityProcessData = new ActivityProcessDataDTO();
            RSPMessage rspMessage = new RSPMessage();
            AdaptorResponseMessage adaptorRspMsg = new AdaptorResponseMessage();
            SampleInfo sampleInfo = new SampleInfo();
            Response response = new Response();
            adaptorRspMsg.setStatus(WORKFLOW_STATUS.NOORDER.toString());// NOORDER
            adaptorRspMsg.setError(new ArrayList<String>());
            response.setDeviceSerialNumber(deviceId);
            response.setSendingApplicationName(WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            response.setReceivingApplication(WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString());
            response.setMessageType(WORKFLOW_MESSAGE_TYPE.RSP.toString());
            response.setAccessioningId(accessioningId);
            response.setRspMessage(rspMessage);
            rspMessage.setSampleInfo(sampleInfo);
            response.setMessageControlId(messageControlId);
            rspMessage.setOrderControl(WORKFLOW_VARIABLES.DC.toString());
            adaptorRspMsg.setResponse(response);
            activityProcessData.setQbpResponseMessage(adaptorRspMsg);
            
            immIntegrationService.setAdaptorResponseMessage(adaptorRspMsg);
            
        } catch (Exception ex) {
            logger.error("ERR:- ResponseRenderingService <= orderNotFoundQueryResponseforMP24() ", ex);
        }
    }
    
    public void orderNotFoundQueryResponseforMP(String accessioningId, String deviceId) {
        try {
            activityProcessData = new ActivityProcessDataDTO();
            RSPMessage rspMessage = new RSPMessage();
            AdaptorResponseMessage adaptorRspMsg = new AdaptorResponseMessage();
            SampleInfo sampleInfo = new SampleInfo();
            Response response = new Response();
            adaptorRspMsg.setStatus(WORKFLOW_STATUS.NOORDER.toString());// NOORDER
            adaptorRspMsg.setError(new ArrayList<String>());
            response.setDeviceSerialNumber(deviceId);
            response.setSendingApplicationName(WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            response.setReceivingApplication(WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString());
            response.setMessageType(WORKFLOW_MESSAGE_TYPE.RSP.toString());
            response.setAccessioningId(accessioningId);
            response.setRspMessage(rspMessage);
            rspMessage.setSampleInfo(sampleInfo);
            rspMessage.setOrderControl(WORKFLOW_VARIABLES.DC.toString());
            adaptorRspMsg.setResponse(response);
            activityProcessData.setQbpResponseMessage(adaptorRspMsg);
            
            immIntegrationService.setAdaptorResponseMessage(adaptorRspMsg);
            
        } catch (Exception ex) {
            logger.error("ERR:- ResponseRenderingService <= orderNotFoundQueryResponseforMP() ", ex);
        }
    }
    
    public void duplicateOrderResponseforMP(String accessioningId, String deviceId, AdaptorRequestMessage message)
        throws OrderNotFoundException {
        
        String protocolName = "";
        List<DeviceTestOptionsDTO> deviceTestOptions;
        try {
            activityProcessData = new ActivityProcessDataDTO();
            
            String dateTimeMessageGenerated = message.getDateTimeMessageGenerated();
            String messageControlId = message.getMessageControlId();
            
            List<WfmDTO> orderDetail = orderIntegrationService.findOrder(accessioningId);
            long orderId = orderDetail.get(0).getOrderId();
            logger.info(" -> execute()::Send response to IMM for " + accessioningId);
            String sampleType = orderDetail.get(0).getSampleType();
            deviceTestOptions = assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(orderDetail,
            		WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString());
            protocolName = deviceTestOptions.get(0).getTestProtocol();
            logger.info(" -> execute()::Protocol name for " + accessioningId + " is " + protocolName);
            RSPMessage rsp = new RSPMessage();
            AdaptorResponseMessage adaptorRspMsg = new AdaptorResponseMessage();
            adaptorRspMsg.setStatus(WfmConstants.WORKFLOW_RESPONSE.SUCCESS_1.toString());
            adaptorRspMsg.setError(new ArrayList<String>());
            Response response = new Response();
            SampleInfo sampleInfo = new SampleInfo();
            ContainerInfo containerInfo = new ContainerInfo();
            sampleInfo.setSampleType(sampleType);
            sampleInfo.setSpecimenSourceSite("");
            sampleInfo.setSpecimenCollectionSite("");
            sampleInfo.setSpecimenDescription(sampleType);
            sampleInfo.setDateTimeSpecimenCollected("");
            sampleInfo.setDateTimeSpecimenReceived("");
            sampleInfo.setDateTimeSpecimenExpiration("");
            sampleInfo.setContainerType(WfmConstants.WORKFLOW_VARIABLES.MP242ML.toString());
            containerInfo.setContainerPosition("");
            containerInfo.setContainerStatus("");
            containerInfo.setCarrierType("");
            containerInfo.setCarrierBarcode("");
            containerInfo.setCarrierPosition("");
            containerInfo.setContainerVolume("");
            containerInfo.setAvailableSpecimenVolume("");
            containerInfo.setInitialSpecimenVolume("");
            containerInfo.setSpecimenEventDate("");
            containerInfo.setSpecimenVolume("");
            containerInfo.setUnitofVolume("");
            rsp.setOrderControl(WfmConstants.WORKFLOW_VARIABLES.NW.toString());
            rsp.setOrderNumber(String.valueOf(orderId));
            rsp.setOrderStatus(WfmConstants.WORKFLOW_VARIABLES.SC.toString());
            rsp.setOrderEventDate(WfmConstants.WORKFLOW_RESPONSE.ORDEREVENTDATE.toString());
            rsp.setProtocolName(protocolName);
            rsp.setProtocolDescription(protocolName);
            rsp.setResultStatus(WfmConstants.WORKFLOW_VARIABLES.S.toString());
            rsp.setEluateVolume("50");
            rsp.setSampleInfo(sampleInfo);
            rsp.setContainerInfo(containerInfo);
            response.setDeviceSerialNumber(deviceId);
            response.setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            response.setReceivingApplication(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString());
            response.setDateTimeMessageGenerated(dateTimeMessageGenerated);
            response.setMessageType(WfmConstants.WORKFLOW_RESPONSE.RSP.toString());
            response.setMessageControlId(messageControlId);
            response.setAccessioningId(accessioningId);
            response.setRspMessage(rsp);
            adaptorRspMsg.setResponse(response);
            activityProcessData.setQbpResponseMessage(adaptorRspMsg);
            immIntegrationService.setAdaptorResponseMessage(adaptorRspMsg);
            logger.info(" <- execute()::RSP for MP24 Response");
        } catch (HMTPException  | IOException e) {
            logger.error(" -> execute()::Mp24 RSP error code " + e);
        }
    }
    
    public void duplicateOrderResponseforMP(String accessioningId, String deviceId, String messageControlId) {
        try {
            activityProcessData = new ActivityProcessDataDTO();
            RSPMessage rspMessage = new RSPMessage();
            AdaptorResponseMessage adaptorRspMsg = new AdaptorResponseMessage();
            SampleInfo sampleInfo = new SampleInfo();
            Response response = new Response();
            adaptorRspMsg.setStatus(WORKFLOW_VARIABLES.DUPLICATE.toString());// Duplicate
                                                                             // Order
            adaptorRspMsg.setError(new ArrayList<String>());
            response.setDeviceSerialNumber(deviceId);
            response.setSendingApplicationName(WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            response.setReceivingApplication(WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString());
            response.setMessageType(WORKFLOW_MESSAGE_TYPE.RSP.toString());
            response.setAccessioningId(accessioningId);
            response.setRspMessage(rspMessage);
            rspMessage.setSampleInfo(sampleInfo);
            response.setMessageControlId(messageControlId);
            rspMessage.setOrderControl(WORKFLOW_VARIABLES.DC.toString());
            adaptorRspMsg.setResponse(response);
            activityProcessData.setQbpResponseMessage(adaptorRspMsg);
            
            immIntegrationService.setAdaptorResponseMessage(adaptorRspMsg);
            
        } catch (Exception ex) {
            logger.error("ERR:- ResponseRenderingService <= duplicateOrderResponseforMP() ", ex);
        }
    }
    
    public void duplicateQueryPositiveResponsefordPCRLP(String containerId, String deviceId,String lpaccessioningId, String messageControlId) {
        List<DeviceTestOptionsDTO> deviceTestOptions = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String protocolName = "";
            ResponseMessage responseMessage = new ResponseMessage();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            deviceTestOptions = assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                lpaccessioningId, WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString());
            protocolName = deviceTestOptions.get(0).getTestProtocol();
            
            responseMessage.setStatus(WfmConstants.WORKFLOW_RESPONSE.SUCCESS_1.toString());
            responseMessage.setErrors(null);
            responseMessage.setDeviceSerialNumber(deviceId);
            responseMessage.setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            responseMessage.setReceivingApplication(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString());
            responseMessage.setDateTimeMessageGenerated(formatter.format(new Date()));
            responseMessage.setMessageType(EnumMessageType.ResponseMessage);
            responseMessage.setAccessioningId(lpaccessioningId);
            responseMessage.setContainerId(containerId);
            responseMessage.setSampleType(WfmConstants.WORKFLOW_VARIABLES.PLASMA.toString());
            responseMessage.setSpecimenDescription(WfmConstants.WORKFLOW_VARIABLES.PLASMA.toString());
            responseMessage.setProtocolName(protocolName);
            responseMessage.setMessageControlId(messageControlId);
            logger.info("SendLP24QBPResponse::ResponseMessage: " + objectMapper.writeValueAsString(responseMessage));
            immIntegrationService.setResponseMessage(responseMessage);
        } catch (Exception ex) {
            logger.error("ERR:- ResponseRenderingService <= duplicateQueryResponsefordPCRLP ", ex);
        }
    }
    
    public void duplicateQueryNegativeResponsefordPCRLP(String containerId, String deviceId,String lpaccessioningId, String messageControlId) {
        List<DeviceTestOptionsDTO> deviceTestOptions = null;
        ObjectMapper objectMapper = new ObjectMapper();
       
        try {
            String protocolName = "";
            ResponseMessage responseMessage = new ResponseMessage();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            deviceTestOptions = assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                lpaccessioningId, WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString());
            protocolName = deviceTestOptions.get(0).getTestProtocol();
            
            responseMessage.setStatus(WfmConstants.WORKFLOW_RESPONSE.AR.toString());// AR
            responseMessage.setErrors(null);
            responseMessage.setDeviceSerialNumber(deviceId);
            responseMessage.setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            responseMessage.setReceivingApplication(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString());
            responseMessage.setDateTimeMessageGenerated(formatter.format(new Date()));
            responseMessage.setMessageType(EnumMessageType.ResponseMessage);
            responseMessage.setAccessioningId(lpaccessioningId);
            responseMessage.setContainerId(containerId);
            responseMessage.setSampleType(WfmConstants.WORKFLOW_VARIABLES.PLASMA.toString());
            responseMessage.setSpecimenDescription(WfmConstants.WORKFLOW_VARIABLES.PLASMA.toString());
            responseMessage.setProtocolName(protocolName);
            responseMessage.setMessageControlId(messageControlId);
            logger.info("SendLP24QBPResponse::ResponseMessage: " + objectMapper.writeValueAsString(responseMessage));
            immIntegrationService.setResponseMessage(responseMessage);
        } catch (Exception ex) {
            logger.error("ERR:- ResponseRenderingService <= duplicateQueryResponsefordPCRLP ", ex);
        }
    }
    
    public void duplicateACKForMP24(String accessioningId,String deviceId,ActivityProcessDataDTO activityProcessData) throws HMTPException {
        AdaptorResponseMessage adaptorRspMsg = new AdaptorResponseMessage();
        Response response = new Response();
        RSPMessage rspMessage = new RSPMessage();
        StatusUpdate statusUpdate = new StatusUpdate();
        try{
        adaptorRspMsg.setStatus(WfmConstants.WORKFLOW_RESPONSE.SUCCESS_1.toString());
        response.setMessageControlId(activityProcessData.getAdaptorRequestMessage().getMessageControlId());
        response.setDeviceSerialNumber(deviceId);
        response.setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
        response.setReceivingApplication(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString());
        response.setAccessioningId(accessioningId);
        response.setDateTimeMessageGenerated(new Date().toString());
        response.setMessageType(WfmConstants.WORKFLOW_RESPONSE.U03ACK.toString());
        response.setRspMessage(rspMessage);
        response.setStatusUpdate(statusUpdate);
        adaptorRspMsg.setResponse(response);
        logger.info(" -> execute()::Ack sent as " + response.getRspMessage() + " and for "+ activityProcessData.getAccessioningId());
        immIntegrationService.setAdaptorResponseMessage(adaptorRspMsg);
        }
        catch(Exception e){
            logger.error("ERR:- ResponseRenderingService :: <= duplicateACKForMP24 ", e);
        }
    }
    
    public void duplicateNegativeACKForMP24(String accessioningId,String deviceId,ActivityProcessDataDTO activityProcessData) throws HMTPException {
        AdaptorResponseMessage adaptorRspMsg = new AdaptorResponseMessage();
        Response response = new Response();
        RSPMessage rspMessage = new RSPMessage();
        StatusUpdate statusUpdate = new StatusUpdate();
        try{
        adaptorRspMsg.setStatus(WfmConstants.WORKFLOW_RESPONSE.AR.toString());
        response.setMessageControlId(activityProcessData.getAdaptorRequestMessage().getMessageControlId());
        response.setDeviceSerialNumber(deviceId);
        response.setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
        response.setReceivingApplication(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString());
        response.setAccessioningId(accessioningId);
        response.setDateTimeMessageGenerated(new Date().toString());
        response.setMessageType(WfmConstants.WORKFLOW_RESPONSE.U03ACK.toString());
        response.setRspMessage(rspMessage);
        response.setStatusUpdate(statusUpdate);
        adaptorRspMsg.setResponse(response);
        logger.info(" -> execute()::Ack sent as " + response.getRspMessage() + ",and also for "+ activityProcessData.getAccessioningId());
        immIntegrationService.setAdaptorResponseMessage(adaptorRspMsg);
        }
        catch(Exception e){
            logger.error("ERR:- ResponseRenderingService <= duplicateNegativeACKForMP24 ", e);
        }
    }
    
    public void duplicateACKForLP24(String deviceId,ActivityProcessDataDTO activityProcessData) throws HMTPException {
        AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
        try{
            
            String containerId = activityProcessData.getSpecimenStatusUpdateMessage().getContainerId();
            acknowledgementMessage.setStatus(WfmConstants.WORKFLOW_RESPONSE.SUCCESS_1.toString());
            acknowledgementMessage.setDeviceSerialNumber(deviceId);
            acknowledgementMessage
                    .setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            acknowledgementMessage.setReceivingApplication(activityProcessData.getSpecimenStatusUpdateMessage().getProcessStepName());
            acknowledgementMessage.setDateTimeMessageGenerated(new Date().toString());
            acknowledgementMessage.setMessageType(EnumMessageType.AcknowledgementMessage);
            acknowledgementMessage.setContainerId(containerId);
            acknowledgementMessage.setMessageControlId(activityProcessData.getSpecimenStatusUpdateMessage().getMessageControlId());
            
            immIntegrationService.setAcknowledgementMessage(acknowledgementMessage);

        }
        catch(Exception e){
            logger.error("ERR:- ResponseRenderingService <= duplicateACKForLP24 ", e);
        }
    }
    
    public void duplicateNegativeACKForLP24(String deviceId,ActivityProcessDataDTO activityProcessData) throws HMTPException {
        AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
        try{
            
            String containerId = activityProcessData.getSpecimenStatusUpdateMessage().getContainerId();
            acknowledgementMessage.setStatus(WfmConstants.WORKFLOW_RESPONSE.AR.toString());
            acknowledgementMessage.setDeviceSerialNumber(deviceId);
            acknowledgementMessage
                    .setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
            acknowledgementMessage.setReceivingApplication(activityProcessData.getSpecimenStatusUpdateMessage().getProcessStepName());
            acknowledgementMessage.setDateTimeMessageGenerated(new Date().toString());
            acknowledgementMessage.setMessageType(EnumMessageType.AcknowledgementMessage);
            acknowledgementMessage.setContainerId(containerId);
            acknowledgementMessage.setMessageControlId(activityProcessData.getSpecimenStatusUpdateMessage().getMessageControlId());
            
            immIntegrationService.setAcknowledgementMessage(acknowledgementMessage);

        }
        catch(Exception e){
            logger.error("ERR:- ResponseRenderingService <= duplicateNegativeACKForLP24 ", e);
        }
    }
    
}
