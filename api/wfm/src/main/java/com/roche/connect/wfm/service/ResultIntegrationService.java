/*******************************************************************************
 * ResultIntegrationService.java Version: 1.0 Authors: somesh_r
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
 * ********************* ChangeLog: somesh_r@hcl.com : Updated copyright headers
 * ********************* ********************* Description: write me
 * *********************
 ******************************************************************************/
package com.roche.connect.wfm.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.dpcr_analyzer.ORUAssay;
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMORUMessage;
import com.roche.connect.common.enums.NiptDeviceType;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.lp24.Consumable;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleProtocolDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.common.util.SampleStatus;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.API_URL;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.WfmDTO;

/**
 * Class implementation that provides integration support for RMM.
 */
@Service("resultIntegrationService") public class ResultIntegrationService {
    
    @Value("${pas.rmm_api_url}") private String rmmApiHostUrl;
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    String flagged = "flagged";
    String passed ="passed";
    
    AssayIntegrationService assayIntegrationService = new AssayIntegrationService();
    
    public RunResultsDTO getRunResultsId(String deviceId, String messageType)
        throws HMTPException,
        UnsupportedEncodingException {
        
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_ID + deviceId + WfmURLConstants.PROCESS_STEP_NAME + messageType, null);
        Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
        return orderClient.get(new GenericType<RunResultsDTO>() {});
    }
    
    public RunResultsDTO getRunResultsIdbyprocessstepnameAnddevicerunresultid(String orderName, String messageType)
        throws HMTPException,
        UnsupportedEncodingException {
        
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + orderName + WfmURLConstants.DPCR_PROCESSSTEP_NAME + messageType, null);
        
        Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
        int status = orderClient.get().getStatus();
        
        if (status == 400) {
            return null;
        } else {
            return orderClient.get(new GenericType<RunResultsDTO>() {});
        }
        
    }
    
    public RunResultsDTO getDevicerunresultidAndDeviceid(String orderName, String messageType, String deviceid)
        throws HMTPException,
        UnsupportedEncodingException {
        
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + orderName + WfmURLConstants.DPCR_PROCESSSTEP_NAME + messageType
                + "&deviceId=" + deviceid,
            null);
        
        Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
        int status = orderClient.get().getStatus();
        
        if (status == 200) {
            return orderClient.get(new GenericType<RunResultsDTO>() {});
        } else {
            logger.error("Error occurred while calling at get method in getDevicerunresultidAndDeviceid and status:"
                + this.getClass().getMethods(), status);
            return null;
        }
        
    }
    
    public Long addRunResults(RunResultsDTO runResultsDTO) throws HMTPException, IOException {
        
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null);
        logger.info("runResultsDTO addRunResults: " + objectMapper.writeValueAsString(runResultsDTO));
        InputStream rmmClient = (InputStream) RestClientUtil.postMethodCall(url, runResultsDTO, null);
        String runId = IOUtils.toString(rmmClient, StandardCharsets.UTF_8);
        long runId1 = Long.parseLong(runId);
        logger.info("results ::" + runId);
        
        return runId1;
    }
    
    /**
     * Method to update run result details.
     * @param runResultsDTO
     * @throws HMTPException
     * @throws IOException
     */
    public void updateRunResults(RunResultsDTO runResultsDTO) throws HMTPException, IOException {
        logger.info(" -> updateRunResults()::Update run results");
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null);
        logger.info("runResultsDTO updateRunResults: " + objectMapper.writeValueAsString(runResultsDTO));
        RestClientUtil.putMethodCall(url, runResultsDTO, null);
        logger.info(" <- updateRunResults()");
    }
    
    /**
     * Method to add sample result table in RMM.
     * @param orderdetail
     * @throws HMTPException
     * @throws IOException
     */
    public void addSampleResults(List<WfmDTO> orderdetail) throws HMTPException, IOException {
        
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
        Long runid = null;
        RunResultsDTO runResultsDTO1 = null;
        if (orderdetail.get(0).getSendingApplicationName()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString())) {
            runResultsDTO1 = getRunResultsId(orderdetail.get(0).getDeviceId(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString());
        } else if (orderdetail.get(0).getProcessStepName()
            .equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())) {
            runResultsDTO1 = getRunResultsId(orderdetail.get(0).getDeviceId(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString());
        } else if (orderdetail.get(0).getProcessStepName()
            .equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())) {
            runResultsDTO1 = getRunResultsId(orderdetail.get(0).getDeviceId(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString());
        }
        if (runResultsDTO1 != null) {
            runid = runResultsDTO1.getRunResultId();
            logger.info("Run Id:", runid);
        } else {
            RunResultsDTO runResultsDTO = new RunResultsDTO();
            
            runResultsDTO.setDeviceId(orderdetail.get(0).getDeviceId());
            runResultsDTO.setAssayType(orderdetail.get(0).getAssayType());
            if (orderdetail.get(0).getProcessStepName()
                .equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())) {
                runResultsDTO.setProcessStepName(WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString());
                
            } else if (orderdetail.get(0).getProcessStepName()
                .equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())) {
                runResultsDTO.setProcessStepName(WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString());
            } else if (orderdetail.get(0).getSendingApplicationName()
                .equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString())) {
                runResultsDTO.setProcessStepName(WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString());
            }
            runResultsDTO.setRunStatus(WfmConstants.ORDER_STATUS.INPROGRESS.toString());
            runid = addRunResults(runResultsDTO);
            
        }
        orderdetail.get(0).setRunid(runid);
        orderdetail.stream().forEach(orderDetail -> {
            try {
                
                logger.info("Adding Mp24 Orders to SampleResults");
                
                SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
                sampleResultsDTO.setAccesssioningId(orderDetail.getAccessioningId());
                sampleResultsDTO.setOrderId(orderDetail.getOrderId());
                sampleResultsDTO.setStatus(orderDetail.getOrderStatus());
                sampleResultsDTO.setRunResultsId(orderDetail.getRunid());
                if (orderDetail.getSendingApplicationName()
                    .equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString())) {
                    sampleResultsDTO.setInputContainerId(orderDetail.getInputContainerId());
                    sampleResultsDTO.setInputContainerPosition(orderDetail.getInputposition());
                } else {
                    sampleResultsDTO.setInputContainerId(orderDetail.getAccessioningId());
                }
                
                logger.info("runResultsDTO addSampleResults: " + objectMapper.writeValueAsString(sampleResultsDTO));
                InputStream rmmClient = (InputStream) RestClientUtil.postMethodCall(url, sampleResultsDTO, null);
                IOUtils.toString(rmmClient, StandardCharsets.UTF_8);
            } catch (HMTPException | IOException e) {
                logger.error("Error occurred while calling at Batch Post method in addSampleResults"
                    + this.getClass().getMethods(), e);
                
            }
            logger.info("MP24  Orders Added to SampleResults");
            
        });
        
    }
    
    /**
     * Method to add sample result table in RMM.
     * @param orderdetail
     * @throws HMTPException
     * @throws IOException
     */
    public void addSampleResultsforSeqPrep(List<WfmDTO> orderdetail) throws HMTPException {
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
        orderdetail.stream().forEach(orderDetail -> {
            try {
                
                logger.info("Adding  LP SEQ PP Orders to Sample Results");
                
                SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
                sampleResultsDTO.setAccesssioningId(orderDetail.getAccessioningId());
                sampleResultsDTO.setOrderId(orderDetail.getOrderId());
                sampleResultsDTO.setStatus(orderDetail.getOrderStatus());
                sampleResultsDTO.setRunResultsId(orderDetail.getRunid());
                sampleResultsDTO.setInputContainerId(orderDetail.getInputContainerId());
                sampleResultsDTO.setInputContainerPosition(orderDetail.getInputposition());
                logger.info(
                    "runResultsDTO addSampleResultsforSeqPrep: " + objectMapper.writeValueAsString(sampleResultsDTO));
                InputStream rmmClient = (InputStream) RestClientUtil.postMethodCall(url, sampleResultsDTO, null);
                IOUtils.toString(rmmClient, StandardCharsets.UTF_8);
            } catch (HMTPException | IOException e) {
                logger.error("Error occurred while calling at RMM Post method in addSampleResultsforSeqPrep"
                    + this.getClass().getMethods(), e);
                
            }
            logger.info("Orders Added Successfully to Sample Results for LP SEQ PP.");
            
        });
    }
    
    /**
     * Method to update sample result table in RMM.
     * @param orderdetaila
     * @param message
     * @throws HMTPException
     * @throws IOException
     */
    public void updateSampleResults(List<WfmDTO> orderDetails, AdaptorRequestMessage message)
        throws HMTPException,
        IOException {
        logger.info(" -> updateSampleResults()::Update Sample results");
        
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
        RunResultsDTO runResultsDTO1 = getDevicerunresultidAndDeviceid(message.getStatusUpdate().getOrderName(),
            WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString(), message.getDeviceSerialNumber());
        
        logger.info(" -> updateSampleResults()::Update sample results for ", orderDetails.get(0).getRunResultsId());
        
        if (runResultsDTO1 != null) {
            updateRunResultsForMP24(message, runResultsDTO1);
        }
        orderDetails.get(0).setRunid(runResultsDTO1 != null ? runResultsDTO1.getRunResultId()
            : this.addRunResults(createAndUpdateRunResultsForMP24(orderDetails, message)));
        orderDetails.stream().forEach(orderDetail -> setSampleResultAndProtocolMP24(message, url, orderDetail));
        logger.info(" -> updateSampleResults()::MP24  Orders Added to RMM");
    }

    private void setSampleResultAndProtocolMP24(AdaptorRequestMessage message, String url, WfmDTO orderDetail) {
        try {
            
            logger.info(" -> updateSampleResults()::Update Mp24 Orders to RMM");
            
            SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
            SampleProtocolDTO sampleProtocolDTO = new SampleProtocolDTO();
            
            SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = null;
            sampleResultsDTO.setAccesssioningId(orderDetail.getAccessioningId());
            
            if (message.getStatusUpdate().getOrderResult()
                .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSED.toString())
                || message.getStatusUpdate().getOrderResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.FLAGGED.toString()))
                sampleResultsDTO.setStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
            else sampleResultsDTO.setStatus(message.getStatusUpdate().getOrderResult());
            
            sampleResultsDTO.setResult(message.getStatusUpdate().getOrderResult());
            sampleResultsDTO.setInputContainerId(orderDetail.getAccessioningId());
            sampleResultsDTO.setOutputContainerId(message.getStatusUpdate().getSampleInfo().getSampleOutputId());
            sampleResultsDTO
                .setOutputContainerPosition(message.getStatusUpdate().getSampleInfo().getSampleOutputPosition());
            sampleResultsDTO.setComments(message.getStatusUpdate().getComment());
            sampleResultsDTO.setOutputContainerType(message.getStatusUpdate().getContainerInfo().getCarrierType());
            sampleResultsDTO.setRunResultsId(orderDetail.getRunid());
            sampleResultsDTO.setOrderId(orderDetail.getOrderId());
            sampleResultsDTO.setComments(message.getStatusUpdate().getComment());
            sampleResultsDTO.setSampleType(orderDetail.getSampleType());
            sampleResultsDTO.setFlag(message.getStatusUpdate().getFlag());
            sampleResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
            sampleResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
            sampleProtocolDTO.setProtocolName(orderDetail.getProtocolname());
            sampleProtocolDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
            sampleProtocolDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
            sampleResultsDTO.getSampleProtocol().add(sampleProtocolDTO);
            if (!message.getStatusUpdate().getConsumables().isEmpty()) {
                for (com.roche.connect.common.mp24.message.Consumable consumable: message.getStatusUpdate()
                    .getConsumables()) {
                    sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
                    sampleReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                    sampleReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                    sampleReagentsAndConsumablesDTO.setUpdatedBy(sampleProtocolDTO.getUpdatedBy());
                    sampleReagentsAndConsumablesDTO.setUpdatedDateTime(sampleProtocolDTO.getUpdatedDateTime());
                    sampleResultsDTO.getSampleReagentsAndConsumables().add(sampleReagentsAndConsumablesDTO);
                }
            }
            
            logger.info(
                " ->MP24 updateSampleResults()::runResultsDTO: " + objectMapper.writeValueAsString(sampleResultsDTO));
            RestClientUtil.postMethodCall(url, sampleResultsDTO, null);
        } catch (HMTPException | IOException e) {
            logger.error("Error occurred while calling at Batch Post method in updateSampleResults"
                + this.getClass().getMethods(), e);
            
        }
    }

    private RunResultsDTO createAndUpdateRunResultsForMP24(List<WfmDTO> orderDetails, AdaptorRequestMessage message) {
        RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO;
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        
        runResultsDTO.setDeviceId(message.getDeviceSerialNumber());
        runResultsDTO.setProcessStepName(WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString());
        
        if (message.getStatusUpdate().getRunResult()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSED.toString()))
            runResultsDTO.setRunStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
        else if (message.getStatusUpdate().getRunResult()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSEDWITHFLAG.toString()))
            runResultsDTO.setRunStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED_WITH_FLAGS.toString());
        else if (message.getStatusUpdate().getRunResult()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString()))
            runResultsDTO.setRunStatus(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString());
        else if (message.getStatusUpdate().getRunResult()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString()))
            runResultsDTO.setRunStatus(WfmConstants.WORKFLOW_STATUS.ABORTED.toString());
        
        runResultsDTO.setDeviceRunId(message.getStatusUpdate().getOrderName());
        runResultsDTO.setAssayType(orderDetails.get(0).getAssayType());
        
        runResultsDTO.setOperatorName(message.getStatusUpdate().getOperatorName());
        runResultsDTO.setRunStartTime(message.getStatusUpdate().getRunStartTime());
        runResultsDTO.setRunCompletionTime(message.getStatusUpdate().getRunEndTime());
        runResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
        runResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
        if (!message.getStatusUpdate().getConsumables().isEmpty()) {
            for (com.roche.connect.common.mp24.message.Consumable consumable: message.getStatusUpdate()
                .getConsumables()) {
                runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
                runReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                runReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                runReagentsAndConsumablesDTO.setUpdatedBy(runResultsDTO.getUpdatedBy());
                runReagentsAndConsumablesDTO.setUpdatedDateTime(runResultsDTO.getUpdatedDateTime());
                runResultsDTO.getRunReagentsAndConsumables().add(runReagentsAndConsumablesDTO);
            }
        }
        return runResultsDTO;
    }

    private void updateRunResultsForMP24(AdaptorRequestMessage message, RunResultsDTO runResultsDTO1)
        throws HMTPException,
        IOException {
        RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO;
        runResultsDTO1.setOperatorName(message.getStatusUpdate().getOperatorName());
        runResultsDTO1.setRunStartTime(message.getStatusUpdate().getRunStartTime());
        runResultsDTO1.setRunCompletionTime(message.getStatusUpdate().getRunEndTime());
        runResultsDTO1.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
        runResultsDTO1.setUpdatedDateTime(Timestamp.from(Instant.now()));
        
        if (message.getStatusUpdate().getRunResult()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSED.toString()))
            runResultsDTO1.setRunStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
        else if (message.getStatusUpdate().getRunResult()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSEDWITHFLAG.toString()))
            runResultsDTO1.setRunStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED_WITH_FLAGS.toString());
        else if (message.getStatusUpdate().getRunResult()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString()))
            runResultsDTO1.setRunStatus(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString());
        else if (message.getStatusUpdate().getRunResult()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString()))
            runResultsDTO1.setRunStatus(WfmConstants.WORKFLOW_STATUS.ABORTED.toString());
        
        if (!message.getStatusUpdate().getConsumables().isEmpty()) {
            for (com.roche.connect.common.mp24.message.Consumable consumable: message.getStatusUpdate()
                .getConsumables()) {
                runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
                runReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                runReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                runReagentsAndConsumablesDTO.setUpdatedBy(runResultsDTO1.getUpdatedBy());
                runReagentsAndConsumablesDTO.setUpdatedDateTime(runResultsDTO1.getUpdatedDateTime());
                runResultsDTO1.getRunReagentsAndConsumables().add(runReagentsAndConsumablesDTO);
            }
        }
        updateRunResults(runResultsDTO1);
    }
    
    public void updateforLP24(List<WfmDTO> orderdetail, SpecimenStatusUpdateMessage message)
        throws HMTPException,
        IOException {
        
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
        Long runid = null;
        if (!message.getProcessStepName().equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.SEQPREP.toString())) {
            RunResultsDTO runResultsDTO1 = getDevicerunresultidAndDeviceid(message.getStatusUpdate().getOrderName(),
                message.getProcessStepName(), orderdetail.get(0).getDeviceId());
            
            if (runResultsDTO1 != null) {
                runid = runResultsDTO1.getRunResultId();
                RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = null;
                runResultsDTO1.setOperatorName(message.getStatusUpdate().getOperatorName());
                runResultsDTO1.setRunStartTime(message.getStatusUpdate().getRunStartTime());
                runResultsDTO1.setRunCompletionTime(message.getStatusUpdate().getRunEndTime());
                runResultsDTO1.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
                runResultsDTO1.setUpdatedDateTime(Timestamp.from(Instant.now()));
                
                if (message.getStatusUpdate().getRunResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSED.toString()))
                    runResultsDTO1.setRunStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
                else if (message.getStatusUpdate().getRunResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSEDWITHFLAG.toString()))
                    runResultsDTO1.setRunStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED_WITH_FLAGS.toString());
                else if (message.getStatusUpdate().getRunResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString()))
                    runResultsDTO1.setRunStatus(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString());
                else if (message.getStatusUpdate().getRunResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString()))
                    runResultsDTO1.setRunStatus(WfmConstants.WORKFLOW_STATUS.ABORTED.toString());
                
                if (!message.getStatusUpdate().getConsumables().isEmpty()) {
                    for (Consumable consumable: message.getStatusUpdate().getConsumables()) {
                        runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
                        runReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                        runReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                        runReagentsAndConsumablesDTO.setUpdatedBy(runResultsDTO1.getUpdatedBy());
                        runReagentsAndConsumablesDTO.setUpdatedDateTime(runResultsDTO1.getUpdatedDateTime());
                        runResultsDTO1.getRunReagentsAndConsumables().add(runReagentsAndConsumablesDTO);
                    }
                }
                updateRunResults(runResultsDTO1);
            }
            
            else {
                RunResultsDTO runResultsDTO = new RunResultsDTO();
                
                runResultsDTO.setDeviceId(orderdetail.get(0).getDeviceId());
                runResultsDTO.setProcessStepName(message.getProcessStepName());
                runResultsDTO.setRunStatus(message.getStatusUpdate().getRunResult());
                runResultsDTO.setDeviceRunId(message.getStatusUpdate().getOrderName());
                
                if (message.getStatusUpdate().getRunResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSED.toString()))
                    runResultsDTO.setRunStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
                else if (message.getStatusUpdate().getRunResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSEDWITHFLAG.toString()))
                    runResultsDTO.setRunStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED_WITH_FLAGS.toString());
                else if (message.getStatusUpdate().getRunResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString()))
                    runResultsDTO.setRunStatus(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString());
                else if (message.getStatusUpdate().getRunResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString()))
                    runResultsDTO.setRunStatus(WfmConstants.WORKFLOW_STATUS.ABORTED.toString());
                
                runResultsDTO.setAssayType(orderdetail.get(0).getAssayType());
                
                RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = null;
                
                runResultsDTO.setOperatorName(message.getStatusUpdate().getOperatorName());
                runResultsDTO.setRunStartTime(message.getStatusUpdate().getRunStartTime());
                runResultsDTO.setRunCompletionTime(message.getStatusUpdate().getRunEndTime());
                runResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
                runResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                if (!message.getStatusUpdate().getConsumables().isEmpty()) {
                    for (Consumable consumable: message.getStatusUpdate().getConsumables()) {
                        runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
                        runReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                        runReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                        runReagentsAndConsumablesDTO.setUpdatedBy(runResultsDTO.getUpdatedBy());
                        runReagentsAndConsumablesDTO.setUpdatedDateTime(runResultsDTO.getUpdatedDateTime());
                        runResultsDTO.getRunReagentsAndConsumables().add(runReagentsAndConsumablesDTO);
                    }
                }
                runid = addRunResults(runResultsDTO);
            }
            
            orderdetail.get(0).setRunid(runid);
        }
        orderdetail.stream().forEach(orderDetail -> {
            
            try {
                logger.info("Updating LP24 data in RMM");
                
                SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
                SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = null;
                SampleProtocolDTO sampleProtocolDTO = new SampleProtocolDTO();
                SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                
                sampleResultsDTO.setAccesssioningId(orderDetail.getAccessioningId());
                sampleResultsDTO.setOrderId(orderDetail.getOrderId());
                sampleResultsDTO.setRunResultsId(orderDetail.getRunid());
                
                if (message.getStatusUpdate().getOrderResult()
                    .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.PASSED.toString())
                    || message.getStatusUpdate().getOrderResult()
                        .equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.FLAGGED.toString()))
                    sampleResultsDTO.setStatus(WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
                else sampleResultsDTO.setStatus(message.getStatusUpdate().getOrderResult());
                
                sampleResultsDTO.setResult(message.getStatusUpdate().getOrderResult());
                
                sampleResultsDTO.setSampleType(orderDetail.getSampleType());
                sampleResultsDTO.setInputContainerId(orderDetail.getInputContainerId());
                sampleResultsDTO.setInputContainerPosition(orderDetail.getInputposition());
                sampleResultsDTO.setFlag(message.getStatusUpdate().getFlag());
                sampleResultsDTO.setComments(message.getStatusUpdate().getComment());
                sampleResultsDTO.setOutputContainerId(message.getStatusUpdate().getSampleInfo().getNewContainerId());
                sampleResultsDTO
                    .setOutputContainerPosition(message.getStatusUpdate().getSampleInfo().getNewOutputPosition());
                sampleResultsDTO.setOutputPlateType(message.getStatusUpdate().getContainerInfo().getOutputPlateType());
                sampleResultsDTO.setOutputContainerType(message.getStatusUpdate().getContainerInfo().getCarrierType());
                sampleResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
                sampleResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleProtocolDTO.setProtocolName(orderDetail.getProtocolname());
                sampleProtocolDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
                sampleProtocolDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleResultsDTO.getSampleProtocol().add(sampleProtocolDTO);
                if (message.getProcessStepName()
                    .equalsIgnoreCase(WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())) {
                    sampleResultsDetailDTO.setAttributeName("molecularId");
                    sampleResultsDetailDTO.setAttributeValue(orderDetail.getMolecularId());
                    sampleResultsDetailDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDTO.getSampleResultsDetail().add(sampleResultsDetailDTO);
                }
                if (!message.getStatusUpdate().getConsumables().isEmpty()) {
                    for (Consumable consumable: message.getStatusUpdate().getConsumables()) {
                        sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
                        sampleReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                        sampleReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                        sampleReagentsAndConsumablesDTO.setUpdatedBy(sampleResultsDTO.getUpdatedBy());
                        sampleReagentsAndConsumablesDTO.setUpdatedDateTime(sampleResultsDTO.getUpdatedDateTime());
                        sampleResultsDTO.getSampleReagentsAndConsumables().add(sampleReagentsAndConsumablesDTO);
                    }
                }
                logger.info("runResultsDTO updateforLP24PrePcr: " + objectMapper.writeValueAsString(sampleResultsDTO));
                RestClientUtil.postMethodCall(url, sampleResultsDTO, null);
                
            } catch (HMTPException | JsonProcessingException e) {
                logger.error("Error occurred while calling at Batch Post method in updateforLP24PrePcr"
                    + this.getClass().getMethods(), e);
                
            }
            
            logger.info(message.getProcessStepName() + "Details Updated Successfully at RMM");
            
        });
    }
    
    public List<WfmDTO> findAccessingIdByContainerId(String outputcontainerid, String position, String processstep)
        throws HMTPException,
        UnsupportedEncodingException,
        OrderNotFoundException {
        String url = null;
        
        url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.OUTPUT_CONTAINER_ID + outputcontainerid + WfmURLConstants.OUTPUT_CONTAINER_POSITION
                + position + WfmURLConstants.PROCESS_STEP_NAME_ONE + processstep,
            null);
        
        Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
        if (orderClient.get().getStatus() == 204)
            throw new OrderNotFoundException(
                " -> execute()::Order cannot be null! accessioningId:" + outputcontainerid);
        List<SampleResultsDTO> listSampleResultsDTO = orderClient.get(new GenericType<List<SampleResultsDTO>>() {});
        try {
            logger.info("runResultsDTO findAccessingIdByConntainerId: "
                + objectMapper.writeValueAsString(listSampleResultsDTO));
        } catch (JsonProcessingException e) {
            
            logger.error("Error occured while processing JSON", e);
        }
        if (!listSampleResultsDTO.isEmpty()) {
            
            SampleResultsDTO sampleDTO = listSampleResultsDTO.get(0);
            
            List<WfmDTO> wfmdtoList = new ArrayList<>();
            WfmDTO wfmdto = new WfmDTO();
            wfmdto.setAccessioningId(sampleDTO.getAccesssioningId());
            wfmdto.setInputContainerId(sampleDTO.getInputContainerId());
            wfmdto.setOrderId(sampleDTO.getOrderId());
            wfmdto.setOrderStatus(sampleDTO.getStatus());
            wfmdto.setOrderResult(sampleDTO.getResult());
            wfmdto.setSampleType(sampleDTO.getSampleType());
            wfmdtoList.add(wfmdto);
            return wfmdtoList;
        }
        return Collections.emptyList();
    }
    
public void updateHTPStatus(List<WfmDTO> updateSampleResults, HtpStatusMessage message) throws HMTPException {
        
        logger.info("Enter HTP Status Update");
        logger.info("HTP Message:" + message);
        
        String urlsample = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
        
        updateSampleResults.stream().forEach(sampleResults -> {
            try {
                logger.info("Updating HTP Status to Sample Results");
                SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
                SampleProtocolDTO sampleProtocolDTO = new SampleProtocolDTO();
                sampleResultsDTO.setAccesssioningId(sampleResults.getAccessioningId());
                sampleResultsDTO.setOrderId(sampleResults.getOrderId());
                sampleResultsDTO.setSampleType(sampleResults.getSampleType());
                sampleResultsDTO.setRunResultsId(sampleResults.getRunResultsId());
                sampleResultsDTO.setInputContainerId(sampleResults.getInputContainerId());
                sampleResultsDTO.setOutputContainerId(sampleResults.getOutputContainerId());
                sampleResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleResultsDTO.setUpdatedBy(message.getUpdatedBy());
                sampleProtocolDTO.setProtocolName(sampleResults.getProtocolname());
                sampleProtocolDTO.setUpdatedBy(message.getUpdatedBy());
                sampleProtocolDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleResultsDTO.setResult(message.getStatus());
                if(message.getStatus().equalsIgnoreCase(WORKFLOW_STATUS.COMPLETED.toString()))
                	sampleResultsDTO.setStatus(WORKFLOW_STATUS.INPROCESS.toString());
				else if(message.getStatus().equalsIgnoreCase(WORKFLOW_STATUS.TRANSFERCOMPLETED.toString()))
					sampleResultsDTO.setStatus(WORKFLOW_STATUS.COMPLETED.toString());
				else
					sampleResultsDTO.setStatus(message.getStatus());
                sampleResultsDTO.getSampleProtocol().add(sampleProtocolDTO);
                sampleResultsDTO.setSampleReagentsAndConsumables(message.getSampleReagentsAndConsumables());
                
                logger.info("sampleResultsDTO: " + objectMapper.writeValueAsString(sampleResultsDTO));
                logger.info("sampleProtocolDTO: " + objectMapper.writeValueAsString(sampleProtocolDTO));
                
                RestClientUtil.postMethodCall(urlsample, sampleResultsDTO, null);
                
            } catch (HMTPException | JsonProcessingException e) {
                logger.error(
                    "Error occurred while calling at Sample Results Post method" + this.getClass().getMethods(), e);
                
            }
            
        });
        
    }
    
    public void updateforLP24SeqPre(List<WfmDTO> orderdetail, SpecimenStatusUpdateMessage message)
        throws HMTPException {
        
        logger.info("Enter LP Seq  Status Update");
        logger.info("HTP Message:" + message);
        
        String urlsample = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
        orderdetail.stream().forEach(orderDetail -> {
            
            try {
                logger.info("Updating LP24Seq Prep to Sample");
                
                SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
                SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = null;
                SampleProtocolDTO sampleProtocolDTO = new SampleProtocolDTO();
                sampleResultsDTO.setAccesssioningId(orderDetail.getAccessioningId());
                sampleResultsDTO.setOrderId(orderDetail.getOrderId());
                sampleResultsDTO.setRunResultsId(message.getRunResultsId());
                sampleResultsDTO.setStatus(orderDetail.getOrderStatus());
                sampleResultsDTO.setInputContainerId(orderDetail.getInputContainerId());
                sampleResultsDTO.setInputContainerPosition(orderDetail.getInputposition());
                sampleResultsDTO.setFlag(message.getStatusUpdate().getFlag());
                sampleResultsDTO.setComments(message.getStatusUpdate().getComment());
                sampleResultsDTO.setOutputContainerId(message.getStatusUpdate().getSampleInfo().getNewContainerId());
                sampleResultsDTO
                    .setOutputContainerPosition(message.getStatusUpdate().getSampleInfo().getNewOutputPosition());
                sampleResultsDTO.setOutputPlateType(message.getStatusUpdate().getContainerInfo().getOutputPlateType());
                sampleResultsDTO.setOutputContainerType(message.getStatusUpdate().getContainerInfo().getCarrierType());
                sampleResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
                sampleResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleProtocolDTO.setProtocolName(orderDetail.getProtocolname());
                sampleProtocolDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
                sampleProtocolDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleResultsDTO.getSampleProtocol().add(sampleProtocolDTO);
                if (!message.getStatusUpdate().getConsumables().isEmpty()) {
                    for (Consumable consumable: message.getStatusUpdate().getConsumables()) {
                        sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
                        sampleReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                        sampleReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                        sampleReagentsAndConsumablesDTO.setUpdatedBy(sampleResultsDTO.getUpdatedBy());
                        sampleReagentsAndConsumablesDTO.setUpdatedDateTime(sampleResultsDTO.getUpdatedDateTime());
                        sampleResultsDTO.getSampleReagentsAndConsumables().add(sampleReagentsAndConsumablesDTO);
                    }
                }
                logger.info("runResultsDTO: " + objectMapper.writeValueAsString(sampleResultsDTO));
                RestClientUtil.postMethodCall(urlsample, sampleResultsDTO, null);
                
            } catch (HMTPException | JsonProcessingException e) {
                logger.error(
                    "Error occurred while calling at Sample_Results Post method" + this.getClass().getMethods(), e);
                
            }
            
            logger.info("LP24 Seq Prep RMM Updated");
            
        });
        
    }
    
    public void updatefordPCRAnalyzer(List<WfmDTO> updateBatchDetails, WFMESUMessage message) throws HMTPException {
        
        final String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
        
        updateBatchDetails.stream().forEach(orderDetail -> {
            try {
                
                logger.info("Adding  Orders to Sample Results for DPCR");
                
                SimpleDateFormat formatter =
                    new SimpleDateFormat(WfmConstants.WORKFLOW_VARIABLES.YYYYMMDDHHMMSS.toString());
                SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
                sampleResultsDTO.setAccesssioningId(orderDetail.getAccessioningId());
                sampleResultsDTO.setInputContainerId(message.getInputContainerId());
                sampleResultsDTO.setOrderId(orderDetail.getOrderId());
                sampleResultsDTO.setStatus(orderDetail.getOrderStatus());
                sampleResultsDTO.setSampleType(orderDetail.getSampleType());
                sampleResultsDTO.setFlag(message.getFlag());
                sampleResultsDTO.setRunResultsId(Long.parseLong(message.getRunResultId()));
                sampleResultsDTO
                    .setReceivedDate(new Timestamp(formatter.parse(message.getDateTimeMessageGenerated()).getTime()));
                sampleResultsDTO.setUpdatedBy(WfmConstants.WORKFLOW_VARIABLES.SYSTEM.toString());
                sampleResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleResultsDTO.setCreatedBy(WfmConstants.WORKFLOW_VARIABLES.ADMIN.toString());
                sampleResultsDTO.setVerifiedBy(WfmConstants.WORKFLOW_VARIABLES.ADMIN.toString());
                sampleResultsDTO
                    .setVerifiedDate(new Timestamp(formatter.parse(message.getDateTimeMessageGenerated()).getTime()));
                sampleResultsDTO.setCreatedDateTime(
                    new Timestamp(formatter.parse(message.getDateTimeMessageGenerated()).getTime()));
                
                logger.info(
                    "sampleResultsDTO updatefordPCRAnalyzer: " + objectMapper.writeValueAsString(sampleResultsDTO));
                
                logger.info("URL " + url);
                
                RestClientUtil.postMethodCall(url, sampleResultsDTO, null);
            } catch (Exception e) {
                logger.error("Error occurred while calling at RMM Post method in  updatedPCRAnalyzerProcess"
                    + this.getClass().getMethods(), e);
                
            }
            logger.info("Orders Added to Sample Results for DPCR");
            
        });
    }
    
    public void updateforMP96Status(List<WfmDTO> updateBatchDetails, WFMoULMessage message){
     
        updateBatchDetails.stream().forEach(orderDetail -> {
            try {
                
                logger.info("Adding  Orders to Sample Results for MP96.");
                
                SimpleDateFormat formatter =
                    new SimpleDateFormat(WfmConstants.WORKFLOW_VARIABLES.YYYYMMDDHHMMSS.toString());
                SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
                SampleProtocolDTO sampleProtocolDTO = new SampleProtocolDTO();
                sampleResultsDTO.setAccesssioningId(orderDetail.getAccessioningId());
                sampleResultsDTO.setInputContainerId(orderDetail.getAccessioningId());
                sampleResultsDTO.setOrderId(orderDetail.getOrderId());
                sampleResultsDTO.setStatus(orderDetail.getOrderStatus());
                sampleResultsDTO.setSampleType(orderDetail.getSampleType());
                sampleResultsDTO.setRunResultsId(message.getRunResultsId());
                sampleResultsDTO.setOutputContainerId(message.getOulSampleResultMessage().getOutputContainerId());
                sampleResultsDTO.setOutputContainerPosition(message.getOulSampleResultMessage().getPosition());
                sampleResultsDTO.setOutputContainerType(message.getOulSampleResultMessage().getOutputPlateType());
                sampleResultsDTO.setReceivedDate(
                    new Timestamp(formatter.parse(message.getOulSampleResultMessage().getRunEndTime()).getTime()));
                sampleResultsDTO.setResult(message.getOulSampleResultMessage().getSampleResult());
                sampleResultsDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                sampleResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleResultsDTO.setVerifiedBy(message.getOulSampleResultMessage().getOperator());
                sampleResultsDTO.setVerifiedDate(
                    new Timestamp(formatter.parse(message.getOulSampleResultMessage().getRunEndTime()).getTime()));
                sampleResultsDTO.setComments(message.getOulSampleResultMessage().getSampleComments());
                sampleResultsDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                sampleResultsDTO.setCreatedDateTime(
                    new Timestamp(formatter.parse(message.getOulSampleResultMessage().getRunEndTime()).getTime()));
                sampleProtocolDTO.setProtocolName(message.getOulSampleResultMessage().getProtocal());
                sampleProtocolDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                sampleProtocolDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleProtocolDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                sampleResultsDTO.setFlag(message.getOulSampleResultMessage().getFlag());
                sampleProtocolDTO.setCreatedDateTime(
                    new Timestamp(formatter.parse(message.getOulSampleResultMessage().getRunEndTime()).getTime()));
                sampleResultsDTO.getSampleProtocol().add(sampleProtocolDTO);
                
                Collection<SampleResultsDetailDTO> sampleResultsDetailDTOList = new ArrayList<>();
                
                if (message.getOulSampleResultMessage().getLotNo() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.LOTNO.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getLotNo());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getBarcode() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.BARCODE.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getBarcode());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getReagentKitName() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.REAGENTKITNAME.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getReagentKitName());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getReagentVesion() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.REAGENTVERSION.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getReagentVesion());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getOutputPlateType() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.OUTPUTPLATETYPE.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getOutputPlateType());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getSampleVolume() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.SAMPLEVOLUME.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getSampleVolume());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getElevateVolume() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.ELEVATEVOLUME.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getElevateVolume());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getSoftwareVersion() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.SOFTWAREVERSION.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getSoftwareVersion());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getSerialNo() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.SERIALNO.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getSerialNo());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getPosition() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.POSITION.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getPosition());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getVolume() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.VOLUME.toString());
                    sampleResultsDetailDTO.setAttributeValue(message.getOulSampleResultMessage().getVolume());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                if (message.getOulSampleResultMessage().getExpDate() != null) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.EXPDATE.toString());
                    sampleResultsDetailDTO.setAttributeValue(
                        new Timestamp(formatter.parse(message.getOulSampleResultMessage().getExpDate()).getTime())
                            .toString());
                    sampleResultsDetailDTO.setCreatedDateTime(new Timestamp(
                        formatter.parse(message.getOulSampleResultMessage().getRunStartTime()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTO.setCreatedBy(message.getOulSampleResultMessage().getOperator());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
                sampleResultsDTO.setSampleResultsDetail(sampleResultsDetailDTOList);
                
                // code for sample_Reagents_And_Consumables
                
                SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
                sampleReagentsAndConsumablesDTO.setAttributeName("Internal Control");
                sampleReagentsAndConsumablesDTO.setAttributeValue(
                    Stream.of((Optional.ofNullable(message.getOulSampleResultMessage().getLotNo()).orElse("")),
                        Optional.ofNullable(message.getOulSampleResultMessage().getBarcode()).orElse(""),
                        Optional.ofNullable(message.getOulSampleResultMessage().getVolume()).orElse(""),
                        Optional
                            .ofNullable(new Timestamp(
                                formatter.parse(message.getOulSampleResultMessage().getExpDate()).getTime()).toString())
                            .orElse(""))
                        .collect(Collectors.joining(";")));
                sampleReagentsAndConsumablesDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleResultsDTO.getSampleReagentsAndConsumables().add(sampleReagentsAndConsumablesDTO);
                
                SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO1 =
                    new SampleReagentsAndConsumablesDTO();
                sampleReagentsAndConsumablesDTO1.setAttributeName("Reagents");
                sampleReagentsAndConsumablesDTO1.setAttributeValue(Stream
                    .of((Optional.ofNullable(message.getOulSampleResultMessage().getReagentKitName()).orElse("")),
                        (Optional.ofNullable(message.getOulSampleResultMessage().getReagentVesion()).orElse("")))
                    .collect(Collectors.joining(";")));
                sampleReagentsAndConsumablesDTO1.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleResultsDTO.getSampleReagentsAndConsumables().add(sampleReagentsAndConsumablesDTO1);
                
                final String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                    WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
                
                try {
                    logger.info(
                        "runResultsDTO addSampleResultsforMP96: " + objectMapper.writeValueAsString(sampleResultsDTO));
                    
                    logger.info("URL " + url);
                    
                    RestClientUtil.postMethodCall(url, sampleResultsDTO, null);
                } catch (Exception e1) {
                    logger.info("Exception :" + e1.getMessage());
                }
                try {
                    if (orderDetail.getAccessioningId() != null && !orderDetail.getAccessioningId().isEmpty()) {
                        String urlForContainerSampleUpdate =
                            RestClientUtil.getUrlString(API_URL.OMM_API_URL.toString(), "",
                                WfmURLConstants.CONTAINER_SAMPLE + orderDetail.getAccessioningId()
                                    + WfmURLConstants.CONTAINER_SAMPLE_PARAMETER_STATUS + WfmURLConstants.PROCESSED,
                                "", null);
                        ContainerSamplesDTO obj = new ContainerSamplesDTO();
                        Builder builder = RestClientUtil
                            .getBuilder(URLEncoder.encode(urlForContainerSampleUpdate, CharEncoding.UTF_8), null);
                        builder.put(Entity.entity(obj, MediaType.APPLICATION_JSON));
                    }
                } catch (Exception e) {
                    logger.error(
                        "Error occurred while calling at OMM Put method in  updateforMP96Status for updating container samples"
                            + this.getClass().getMethods(),
                        e);
                }
            } catch (HMTPException | ParseException e) {
                logger.error("Error occurred while calling at RMM Post method in  updateforMP96Status"
                    + this.getClass().getMethods(), e);
                
            }
            logger.info("Orders Added to Sample Results for MP96");
            
        });
    }
    
    public void updateforLP24Results(List<WfmDTO> orderdetails, SpecimenStatusUpdateMessage message)
        throws HMTPException,
        IOException,
        ParseException {
        logger.info(" -> updateforLP242Results()::Update LP24 Sample results");
        logger.info("Getting Run Results");
        
        RunResultsDTO runResultsDTO = getDevicerunresultidAndDeviceid(message.getStatusUpdate().getOrderName(),
            WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString(), message.getDeviceSerialNumber());
        
        Long runId = null;
        
        if (runResultsDTO != null)
            runResultsDTO = updateRunResultsBySpecimenStatusUpdateMessage(runResultsDTO, message);
        else {
            runResultsDTO =
                createRunResultsDTOFromSpecimenStatusUpdateMessage(orderdetails.get(0).getAssayType(), message);
            runId = addRunResults(runResultsDTO);
        }
        
        orderdetails.get(0).setRunid(Optional.ofNullable(runId).orElse(runResultsDTO.getRunResultId()));
        message.getStatusUpdate().setOrderResult(Optional.ofNullable(message.getStatusUpdate().getOrderResult())
            .orElseThrow(() -> new HMTPException("Order Result should not be Empty")));
        
        final String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
        
        logger.info("Update Sample Results URL: " + url);
        
        final Boolean isRunIdExist = Optional.ofNullable(runId).isPresent();
        
        for (WfmDTO orderDetail: orderdetails) {
            SampleResultsDTO sampleResultsDTO = getSampleResultsDTOFromARM(orderDetail, message);
            logger
                .info(" -> updateSampleResults()::runResultsDTO: " + objectMapper.writeValueAsString(sampleResultsDTO));
            RestClientUtil.postMethodCall(url, sampleResultsDTO, null);
            
            if (!isRunIdExist) {
                final RunResultsDTO runResultDTOUpdated = getRunResultByRunResultId(runResultsDTO.getRunResultId());
                if (isLastSampleForRun(runResultsDTO, runResultDTOUpdated, sampleResultsDTO)) {
                    runResultsDTO.setRunStatus(getRunResultStatusBySampleStatusAndByFlag(
                        runResultDTOUpdated.getSampleResults().stream().collect(Collectors.toList())));
                }
                
                updateRunResults(runResultsDTO);
            }
        }
    }
    
    public void updateforDPCRAnalyzerORU(List<WfmDTO> updateORUDetails, WFMORUMessage wfmORUMessage){
        
        updateORUDetails.stream().forEach(orderDetail -> {
            try {
                
                logger.info("Adding  Orders to Sample Results for DPCR ORU.");
                
                SimpleDateFormat formatter =
                    new SimpleDateFormat(WfmConstants.WORKFLOW_VARIABLES.YYYYMMDDHHMMSS.toString());
                
                SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
                sampleResultsDTO.setAccesssioningId(wfmORUMessage.getAccessioningId());
                sampleResultsDTO.setInputContainerId(wfmORUMessage.getInputContainerId());
                sampleResultsDTO.setOrderId(orderDetail.getOrderId());
                sampleResultsDTO.setStatus(wfmORUMessage.getStatus());
                sampleResultsDTO.setSampleType(orderDetail.getSampleType());
                sampleResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleResultsDTO.setFlag(wfmORUMessage.getFlag());
                sampleResultsDTO.setRunResultsId(Long.parseLong(wfmORUMessage.getRunResultId()));
                
                Collection<SampleResultsDetailDTO> sampleResultsDetailDTOList = new ArrayList<>();
                
                String protocol = wfmORUMessage.getAssayList().stream().map(ORUAssay::getName)
                    .collect(Collectors.joining(","));
                SampleProtocolDTO sampleProtocolDTO = new SampleProtocolDTO();
                Collection<SampleProtocolDTO> sampleProtocolDTOList = new ArrayList<>();
                sampleProtocolDTO.setCreatedDateTime(new Timestamp(new Date().getTime()));
                sampleProtocolDTO.setProtocolName(protocol);
                sampleProtocolDTO.setUpdatedBy(WfmConstants.WORKFLOW_VARIABLES.SYSTEM.toString());
                sampleProtocolDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                sampleProtocolDTOList.add(sampleProtocolDTO);
                sampleResultsDTO.setSampleProtocol(sampleProtocolDTOList);
                
                wfmORUMessage.getAssayList().stream().forEach(e -> {
                    
                    if ((e.getName() != null) && (!e.getName().isEmpty())) {
                        SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                        sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.NAME.toString());
                        sampleResultsDetailDTO.setAttributeValue(e.getName());
                        sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                    }
                    if ((e.getPackageName() != null) && (!e.getPackageName().isEmpty())) {
                        SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                        sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.PACKAGENAME.toString());
                        sampleResultsDetailDTO.setAttributeValue(e.getPackageName());
                        sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                    }
                    if ((e.getVersion() != null) && (!e.getVersion().isEmpty())) {
                        SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                        sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.VERSION.toString());
                        sampleResultsDetailDTO.setAttributeValue(e.getVersion());
                        sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                    }
                    if ((e.getKit() != null) && (!e.getKit().isEmpty())) {
                        SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                        sampleResultsDetailDTO.setAttributeName(WfmConstants.WORKFLOW_VARIABLES.KIT.toString());
                        sampleResultsDetailDTO.setAttributeValue(e.getKit());
                        sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                    }
                    if ((e.getQuantitativeValue() != null) && (!e.getQuantitativeValue().isEmpty())) {
                        SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                        sampleResultsDetailDTO
                            .setAttributeName(WfmConstants.WORKFLOW_VARIABLES.QUANTITATIVEVALUE.toString());
                        sampleResultsDetailDTO.setAttributeValue(e.getQuantitativeValue());
                        sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                    }
                    if ((e.getQuantitativeResult() != null) && (!e.getQuantitativeResult().isEmpty())) {
                        SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                        sampleResultsDetailDTO
                            .setAttributeName(WfmConstants.WORKFLOW_VARIABLES.QUANTITATIVERESULT.toString());
                        sampleResultsDetailDTO.setAttributeValue(e.getQuantitativeResult());
                        sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                    }
                    if ((e.getQualitativeValue() != null) && (!e.getQualitativeValue().isEmpty())) {
                        SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                        sampleResultsDetailDTO
                            .setAttributeName(WfmConstants.WORKFLOW_VARIABLES.QUALITATIVEVALUE.toString());
                        sampleResultsDetailDTO.setAttributeValue(e.getQualitativeValue());
                        sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                    }
                    if ((e.getQualitativeResult() != null) && (!e.getQualitativeResult().isEmpty())) {
                        SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                        sampleResultsDetailDTO
                            .setAttributeName(WfmConstants.WORKFLOW_VARIABLES.QUALITATIVERESULT.toString());
                        sampleResultsDetailDTO.setAttributeValue(e.getQualitativeResult());
                        sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                    }
                    
                });
                sampleResultsDTO.setSampleResultsDetail(sampleResultsDetailDTOList);
                
                // code for sample_Reagents_And_Consumables
                
                wfmORUMessage.getPartitionEngineList().stream().forEach(e -> {
                    SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO =
                        new SampleReagentsAndConsumablesDTO();
                    sampleReagentsAndConsumablesDTO.setAttributeName("plateID");
                    try {
                        sampleReagentsAndConsumablesDTO
                            .setAttributeValue(
                                Stream.of((Optional.ofNullable(e.getPlateId()).orElse("")),
                                    (Optional.ofNullable(e.getSerialNumber()).orElse("")),
                                    (Optional.ofNullable(e.getFluidId()).orElse("")),
                                    (Optional
                                        .ofNullable(
                                            new Timestamp(formatter.parse(e.getDateandTime()).getTime()).toString())
                                        .orElse("")))
                                    .collect(Collectors.joining(";")));
                    } catch (ParseException exp) {
                        logger.error("Error occured while parsing dateandtime to timestamp:" + e.getDateandTime()
                            + this.getClass().getMethods(), exp);
                    }
                    
                    sampleResultsDTO.getSampleReagentsAndConsumables().add(sampleReagentsAndConsumablesDTO);
                });
                
                final String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                    WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
                
                try {
                    logger.info("SampleResultsDTO addSampleResultsforORU: "
                        + objectMapper.writeValueAsString(sampleResultsDTO));
                    
                    logger.info("URL " + url);
                    
                    RestClientUtil.postMethodCall(url, sampleResultsDTO, null);
                } catch (Exception e1) {
                    logger.info("Exception :" + e1.getMessage());
                }
                
            } catch (HMTPException e) {
                logger.error("Error occurred while calling at RMM Post method in  updateforMP96Status"
                    + this.getClass().getMethods(), e);
                
            }
            logger.info("Orders Added to Sample Results for DPCR ORU");
            
        });
    }
    
    public String getSampleResultsStatus(String status) throws HMTPException {
        
        status = Optional.ofNullable(status)
            .orElseThrow(() -> new HMTPException("Order Sample Results should not be Empty"));
        
        if (SampleStatus.PASSED.getText().equalsIgnoreCase(status)
            || SampleStatus.FLAGGED.getText().equalsIgnoreCase(status))
            return SampleStatus.COMPLETED.getText();
        else if (SampleStatus.FAILED.getText().equalsIgnoreCase(status))
            return SampleStatus.FAILED.getText();
        else if (SampleStatus.ABORTED.getText().equalsIgnoreCase(status))
            return SampleStatus.ABORTED.getText();
        else if (SampleStatus.INPROGRESS.getText().equalsIgnoreCase(status))
            return SampleStatus.INPROGRESS.getText();
        else throw new HMTPException("Invalid Sample Status: " + status);
    }
    
    public Collection<RunReagentsAndConsumablesDTO> getRunReagentsAndConsumablesDTOFrom(RunResultsDTO runResultsDTO,
        List<com.roche.connect.common.mp24.message.Consumable> consumables) {
        
        Collection<RunReagentsAndConsumablesDTO> runReagentsAndConsumables = new ArrayList<>();
        
        if (!consumables.isEmpty()) {
            runReagentsAndConsumables =
                createRunReagentsAndConsumables(runResultsDTO, consumables, runReagentsAndConsumables);
        }
        
        return runReagentsAndConsumables;
    }
    
    private Collection<RunReagentsAndConsumablesDTO> createRunReagentsAndConsumables(RunResultsDTO runResultsDTO,
        List<com.roche.connect.common.mp24.message.Consumable> consumables,
        Collection<RunReagentsAndConsumablesDTO> runReagentsAndConsumables) {
        
        consumables.forEach(e -> {
            RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
            runReagentsAndConsumablesDTO.setAttributeName(e.getName());
            runReagentsAndConsumablesDTO.setAttributeValue(e.getValue());
            runReagentsAndConsumablesDTO.setUpdatedBy(runResultsDTO.getUpdatedBy());
            runReagentsAndConsumablesDTO.setUpdatedDateTime(runResultsDTO.getUpdatedDateTime());
            runReagentsAndConsumables.add(runReagentsAndConsumablesDTO);
        });
        
        return runReagentsAndConsumables;
    }
    
    public RunResultsDTO updateRunResultsBySpecimenStatusUpdateMessage(RunResultsDTO runResultsDTO,
        SpecimenStatusUpdateMessage message) throws HMTPException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(WfmConstants.WORKFLOW_VARIABLES.YYYYMMDDHHMMSS.toString());
        RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = null;
        if (runResultsDTO != null) {
            
            runResultsDTO.setOperatorName(message.getStatusUpdate().getOperatorName());
            runResultsDTO.setRunStartTime(message.getStatusUpdate().getRunStartTime());
            runResultsDTO.setRunCompletionTime(message.getStatusUpdate().getRunEndTime());
            runResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
            runResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
            runResultsDTO.setRunStatus(SampleStatus.INPROGRESS.getText());
            runResultsDTO.setCreatedBy(WfmConstants.WORKFLOW_VARIABLES.SYSTEM.toString());
            runResultsDTO.setAssayType(WfmConstants.WORKFLOW.DPCR.toString());
            runResultsDTO
                .setCreatedDateTime(new Timestamp(formatter.parse(message.getDateTimeMessageGenerated()).getTime()));
            runResultsDTO.setDeviceRunId(message.getStatusUpdate().getOrderName());
            runResultsDTO.setProcessStepName(WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString());
            if (!message.getStatusUpdate().getConsumables().isEmpty()
                && runResultsDTO.getRunReagentsAndConsumables().isEmpty()) {
                for (Consumable consumable: message.getStatusUpdate().getConsumables()) {
                    
                    if (!consumable.getName().equalsIgnoreCase(WfmConstants.WORKFLOW_VARIABLES.PI.toString())) {
                        runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
                        runReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                        runReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                        runReagentsAndConsumablesDTO.setCreatedDateTime(
                            new Timestamp(formatter.parse(message.getDateTimeMessageGenerated()).getTime()));
                        runReagentsAndConsumablesDTO.setCreatedBy(WfmConstants.WORKFLOW_VARIABLES.SYSTEM.toString());
                        runReagentsAndConsumablesDTO.setUpdatedBy(runResultsDTO.getUpdatedBy());
                        runReagentsAndConsumablesDTO.setUpdatedDateTime(runResultsDTO.getUpdatedDateTime());
                        runResultsDTO.getRunReagentsAndConsumables().add(runReagentsAndConsumablesDTO);
                    }
                }
            }
            
        } else {
            throw new HMTPException("Run Result should not be Empty");
        }
        
        return runResultsDTO;
    }
    
    public RunResultsDTO updateRunResultsByAdaptorRequestMessage(RunResultsDTO runResultsDTO,
        AdaptorRequestMessage message) throws HMTPException {
        
        if (runResultsDTO != null) {
            
            runResultsDTO.setOperatorName(message.getStatusUpdate().getOperatorName());
            runResultsDTO.setRunStartTime(message.getStatusUpdate().getRunStartTime());
            runResultsDTO.setRunCompletionTime(message.getStatusUpdate().getRunEndTime());
            runResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
            runResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
            
            runResultsDTO.setRunStatus(SampleStatus.INPROGRESS.getText());
            runResultsDTO.getRunReagentsAndConsumables()
                .addAll(getRunReagentsAndConsumablesDTOFrom(runResultsDTO, message.getStatusUpdate().getConsumables()));
        } else {
            throw new HMTPException("Run Result should not be Empty");
        }
        
        return runResultsDTO;
    }
    
    public RunResultsDTO createRunResultsDTOFromSpecimenStatusUpdateMessage(String assayType,
        SpecimenStatusUpdateMessage message) {
        RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = null;
        
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        runResultsDTO.setDeviceId(message.getDeviceSerialNumber());
        runResultsDTO.setProcessStepName(WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString());
        runResultsDTO.setRunStatus(SampleStatus.INPROGRESS.getText());
        runResultsDTO.setDeviceRunId(message.getStatusUpdate().getOrderName());
        runResultsDTO.setAssayType(assayType);
        runResultsDTO.setOperatorName(message.getStatusUpdate().getOperatorName());
        runResultsDTO.setRunStartTime(message.getStatusUpdate().getRunStartTime());
        runResultsDTO.setRunCompletionTime(message.getStatusUpdate().getRunEndTime());
        runResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
        runResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
        runResultsDTO.setCreatedBy(WfmConstants.WORKFLOW_VARIABLES.SYSTEM.toString());
        if (!message.getStatusUpdate().getConsumables().isEmpty()
            && runResultsDTO.getRunReagentsAndConsumables().isEmpty()) {
            for (Consumable consumable: message.getStatusUpdate().getConsumables()) {
                
                if (!consumable.getName().equalsIgnoreCase(WfmConstants.WORKFLOW_VARIABLES.PI.toString())) {
                    runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
                    runReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                    runReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                    runReagentsAndConsumablesDTO.setUpdatedBy(runResultsDTO.getUpdatedBy());
                    runReagentsAndConsumablesDTO.setUpdatedDateTime(runResultsDTO.getUpdatedDateTime());
                    runResultsDTO.getRunReagentsAndConsumables().add(runReagentsAndConsumablesDTO);
                }
            }
        }
        
        return runResultsDTO;
    }
    
    public RunResultsDTO createRunResultsDTOFromAdaptorRequestMessage(String assayType, AdaptorRequestMessage message) {
      
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        runResultsDTO.setDeviceId(message.getDeviceSerialNumber());
        runResultsDTO.setProcessStepName(WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString());
        runResultsDTO.setRunStatus(SampleStatus.INPROGRESS.getText());
        runResultsDTO.setDeviceRunId(message.getStatusUpdate().getOrderName());
        runResultsDTO.setAssayType(assayType);
        
        runResultsDTO.setOperatorName(message.getStatusUpdate().getOperatorName());
        runResultsDTO.setRunStartTime(message.getStatusUpdate().getRunStartTime());
        runResultsDTO.setRunCompletionTime(message.getStatusUpdate().getRunEndTime());
        runResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
        runResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
        
        runResultsDTO.getRunReagentsAndConsumables()
            .addAll(getRunReagentsAndConsumablesDTOFrom(runResultsDTO, message.getStatusUpdate().getConsumables()));
        
        return runResultsDTO;
    }
    
    public SampleResultsDTO getSampleResultsDTOFromARM(WfmDTO orderDetail, SpecimenStatusUpdateMessage message)
        throws HMTPException,
        ParseException {
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        SampleProtocolDTO sampleProtocolDTO = new SampleProtocolDTO();
        SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = null;
        SimpleDateFormat formatter = new SimpleDateFormat(WfmConstants.WORKFLOW_VARIABLES.YYYYMMDDHHMMSS.toString());
        sampleResultsDTO.setAccesssioningId(orderDetail.getAccessioningId());
        String status = Optional.ofNullable(message.getStatusUpdate().getOrderResult())
            .orElseThrow(() -> new HMTPException("Status Update Order Result should not be Empty"));
        sampleResultsDTO.setStatus(getSampleResultsStatus(status));
        sampleResultsDTO.setResult(message.getStatusUpdate().getOrderResult());
        sampleResultsDTO.setInputContainerId(orderDetail.getInputContainerId());
        sampleResultsDTO.setOutputContainerId(message.getStatusUpdate().getSampleInfo().getNewContainerId());
        sampleResultsDTO.setOutputContainerPosition(message.getStatusUpdate().getSampleInfo().getNewOutputPosition());
        sampleResultsDTO.setComments(message.getStatusUpdate().getComment());
        sampleResultsDTO.setOutputContainerType(message.getStatusUpdate().getContainerInfo().getCarrierType());
        sampleResultsDTO.setRunResultsId(orderDetail.getRunid());
        sampleResultsDTO.setOrderId(orderDetail.getOrderId());
        sampleResultsDTO.setComments(message.getStatusUpdate().getComment());
        sampleResultsDTO.setSampleType(orderDetail.getSampleType());
        sampleResultsDTO.setFlag(message.getStatusUpdate().getFlag());
        sampleResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
        sampleResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
        sampleResultsDTO.setInputContainerPosition(orderDetail.getInputposition());
        sampleResultsDTO.setOutputPlateType(message.getStatusUpdate().getContainerInfo().getOutputPlateType());
        sampleProtocolDTO.setProtocolName(message.getStatusUpdate().getProtocolName());
        sampleProtocolDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
        sampleProtocolDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
        sampleResultsDTO.getSampleProtocol().add(sampleProtocolDTO);
        
        if (!message.getStatusUpdate().getConsumables().isEmpty()) {
            for (Consumable consumable: message.getStatusUpdate().getConsumables()) {
                
                if (!consumable.getName().equalsIgnoreCase(WfmConstants.WORKFLOW_VARIABLES.PI.toString())) {
                    sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
                    sampleReagentsAndConsumablesDTO.setAttributeName(consumable.getName());
                    sampleReagentsAndConsumablesDTO.setAttributeValue(consumable.getValue());
                    sampleReagentsAndConsumablesDTO.setUpdatedBy(sampleProtocolDTO.getUpdatedBy());
                    sampleReagentsAndConsumablesDTO.setUpdatedDateTime(sampleProtocolDTO.getUpdatedDateTime());
                    sampleResultsDTO.getSampleReagentsAndConsumables().add(sampleReagentsAndConsumablesDTO);
                }
            }
        }
        
        Collection<SampleResultsDetailDTO> sampleResultsDetailDTOList = new ArrayList<>();
        
        if (!message.getStatusUpdate().getConsumables().isEmpty()) {
            for (Consumable consumable: message.getStatusUpdate().getConsumables()) {
                if (consumable.getName().equalsIgnoreCase(WfmConstants.WORKFLOW_VARIABLES.PI.toString())) {
                    SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
                    sampleResultsDetailDTO.setAttributeName(consumable.getName());
                    sampleResultsDetailDTO.setAttributeValue(consumable.getValue());
                    sampleResultsDetailDTO.setCreatedDateTime(
                        new Timestamp(formatter.parse(message.getDateTimeMessageGenerated()).getTime()));
                    sampleResultsDetailDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
                    sampleResultsDetailDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
                    sampleResultsDetailDTO.setCreatedBy(WfmConstants.WORKFLOW_VARIABLES.SYSTEM.toString());
                    sampleResultsDetailDTOList.add(sampleResultsDetailDTO);
                }
            }
        }
        
        sampleResultsDTO.setSampleResultsDetail(sampleResultsDetailDTOList);
        
        return sampleResultsDTO;
    }
    
    public SampleResultsDTO getSampleResultsDTOFromARM(WfmDTO orderDetail, AdaptorRequestMessage message)
        throws HMTPException {
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        SampleProtocolDTO sampleProtocolDTO = new SampleProtocolDTO();
        
        sampleResultsDTO.setAccesssioningId(orderDetail.getAccessioningId());
        
        String status = Optional.ofNullable(message.getStatusUpdate().getOrderResult())
            .orElseThrow(() -> new HMTPException("Status Update Order Result should not be Empty"));
        
        sampleResultsDTO.setStatus(getSampleResultsStatus(status));
        sampleResultsDTO.setResult(message.getStatusUpdate().getOrderResult());
        sampleResultsDTO.setInputContainerId(orderDetail.getInputContainerId());
        sampleResultsDTO.setOutputContainerId(message.getStatusUpdate().getSampleInfo().getSampleOutputId());
        sampleResultsDTO
            .setOutputContainerPosition(message.getStatusUpdate().getSampleInfo().getSampleOutputPosition());
        sampleResultsDTO.setComments(message.getStatusUpdate().getComment());
        sampleResultsDTO.setOutputContainerType(message.getStatusUpdate().getContainerInfo().getCarrierType());
        sampleResultsDTO.setRunResultsId(orderDetail.getRunid());
        sampleResultsDTO.setOrderId(orderDetail.getOrderId());
        sampleResultsDTO.setComments(message.getStatusUpdate().getComment());
        sampleResultsDTO.setSampleType(orderDetail.getSampleType());
        sampleResultsDTO.setFlag(message.getStatusUpdate().getFlag());
        sampleResultsDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
        sampleResultsDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
        sampleProtocolDTO.setProtocolName(orderDetail.getProtocolname());
        sampleProtocolDTO.setUpdatedBy(message.getStatusUpdate().getUpdatedBy());
        sampleProtocolDTO.setUpdatedDateTime(Timestamp.from(Instant.now()));
        sampleResultsDTO.getSampleProtocol().add(sampleProtocolDTO);
        
        if (!message.getStatusUpdate().getConsumables().isEmpty()) {
            
            message.getStatusUpdate().getConsumables().forEach(e -> {
                SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
                sampleReagentsAndConsumablesDTO.setAttributeName(e.getName());
                sampleReagentsAndConsumablesDTO.setAttributeValue(e.getValue());
                sampleReagentsAndConsumablesDTO.setUpdatedBy(sampleProtocolDTO.getUpdatedBy());
                sampleReagentsAndConsumablesDTO.setUpdatedDateTime(sampleProtocolDTO.getUpdatedDateTime());
                sampleResultsDTO.getSampleReagentsAndConsumables().add(sampleReagentsAndConsumablesDTO);
            });
        }
        
        return sampleResultsDTO;
    }
    
    public void updateMP24Results(List<WfmDTO> orderDetails, AdaptorRequestMessage message)
        throws HMTPException,
        IOException {
        
        logger.info(" -> updateMP24Results()::Update MP24 Sample results");
        logger.info("Getting Run Results");
        
        RunResultsDTO runResultsDTO = getDevicerunresultidAndDeviceid(message.getStatusUpdate().getOrderName(),
            WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString(), message.getDeviceSerialNumber());
        
        Long runId = null;
        
        if (runResultsDTO != null)
            runResultsDTO = updateRunResultsByAdaptorRequestMessage(runResultsDTO, message);
        else {
            runResultsDTO = createRunResultsDTOFromAdaptorRequestMessage(orderDetails.get(0).getAssayType(), message);
            runId = addRunResults(runResultsDTO);
        }
        
        orderDetails.get(0).setRunid(Optional.ofNullable(runId).orElse(runResultsDTO.getRunResultId()));
        message.getStatusUpdate().setOrderResult(Optional.ofNullable(message.getStatusUpdate().getOrderResult())
            .orElseThrow(() -> new HMTPException("Order Result should not be Empty")));
        
        final String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null);
        
        logger.info("Update Sample Results URL: " + url);
        
        final Boolean isRunIdExist = Optional.ofNullable(runId).isPresent();
        
        for (WfmDTO orderDetail: orderDetails) {
            SampleResultsDTO sampleResultsDTO = getSampleResultsDTOFromARM(orderDetail, message);
            logger
                .info(" -> updateSampleResults()::runResultsDTO: " + objectMapper.writeValueAsString(sampleResultsDTO));
            RestClientUtil.postMethodCall(url, sampleResultsDTO, null);
            
            if (!isRunIdExist) {
                final RunResultsDTO runResultDTOUpdated = getRunResultByRunResultId(runResultsDTO.getRunResultId());
                if (isLastSampleForRun(runResultsDTO, runResultDTOUpdated, sampleResultsDTO)) {
                    runResultsDTO.setRunStatus(getRunResultStatusBySampleStatus(
                        runResultDTOUpdated.getSampleResults().stream().collect(Collectors.toList())));
                }
                
                updateRunResults(runResultsDTO);
            }
        }
    }
    
    private Boolean isLastSampleForRun(RunResultsDTO beforeRunResultsDTO, RunResultsDTO afterRrunResultsDTO,
			SampleResultsDTO sampleResultsDTO) {

		if (!afterRrunResultsDTO.getSampleResults().isEmpty()) {
			if (beforeRunResultsDTO.getSampleResults().stream()
					.anyMatch(e -> e.getAccesssioningId().equals(sampleResultsDTO.getAccesssioningId()))) {
				return afterRrunResultsDTO.getSampleResults().stream()
						.noneMatch(x -> SampleStatus.INPROGRESS.getText().equalsIgnoreCase(x.getStatus()));
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
    
    public void updateRunResultStatusByQuery(String deviceId, String deviceType) {
        logger.info("Entering Update Run Result status before Query For DeviceId: " + deviceId);
        RunResultsDTO runResultDTO;
        ProcessStepActionDTO optionalPSADTO = null;
        try {
            
            if (deviceType.equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString())) {
                List<ProcessStepActionDTO> processStepList =
                    assayIntegrationService.getProcessStepAction(AssayType.NIPT_HTP, NiptDeviceType.MP24.getText());
                
                optionalPSADTO = processStepList.stream().filter(e -> e.getProcessStepSeq() == 1).findFirst()
                    .orElseThrow(() -> new HMTPException("Missing Process step name for MP24"));
            } else if (deviceType.equalsIgnoreCase(NiptDeviceType.LP24.getText())) {
                List<ProcessStepActionDTO> processStepList =
                    assayIntegrationService.getProcessStepAction(AssayType.NIPT_DPCR, NiptDeviceType.LP24.getText());
                
                optionalPSADTO = processStepList.stream().filter(e -> e.getProcessStepSeq() == 2).findFirst()
                    .orElseThrow(() -> new HMTPException("Missing Process step name for LP24"));
            }
            
            runResultDTO = optionalPSADTO != null ? getRunResultByDeviceIdAndProcessStepNameAndStatus(deviceId,
                optionalPSADTO.getProcessStepName(), SampleStatus.INPROGRESS.getText()) : null;
            
            if (runResultDTO != null) {
                
                logger.info("Run Result Id is not Complete - RunResultId: " + runResultDTO.getRunResultId());
                
                runResultDTO = getRunResultByRunResultId(runResultDTO.getRunResultId());
                
                runResultDTO.setRunStatus(deviceType.equalsIgnoreCase(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.MP24.toString())
                    ?  getRunResultStatusBySampleStatus(
                            runResultDTO.getSampleResults().stream().collect(Collectors.toList()))
                    : deviceType.equalsIgnoreCase(NiptDeviceType.LP24.getText())
                        ? getRunResultStatusBySampleStatusAndByFlag(
                            runResultDTO.getSampleResults().stream().collect(Collectors.toList()))
                        : null);
                updateRunResults(runResultDTO);
            }
            
        } catch (HMTPException | IOException e) {
            logger.error("Exception on updateRunResultStatusByQuery" + e.getMessage());
        }
    }
    
    /*private String getRunStatusBasedOnSamples(RunResultsDTO runResultDTO) {

		boolean isSampleCompleted = false;
		boolean isSampleAborted = false;
		boolean isSampleFlagged = false;
		for (SampleResultsDTO sampleResults : runResultDTO.getSampleResults()) {

			if (sampleResults.getStatus().equalsIgnoreCase("Passed")) {
				isSampleCompleted = true;
			} else if (sampleResults.getStatus().equalsIgnoreCase("Aborted")) {
				isSampleAborted = true;
			}

			if (StringUtils.isNotBlank(sampleResults.getFlag()))
				isSampleFlagged = true;

			if (isSampleCompleted && isSampleAborted && isSampleFlagged)
				break;

		}

		if (isSampleCompleted && (isSampleFlagged || isSampleAborted))
			return RunStatusConstants.MP24_COMPLETED_WITH_FLAGS;

		return (!isSampleCompleted) ? RunStatusConstants.MP24_ABORTED : RunStatusConstants.MP24_COMPLETED;
	}*/

    
    
    
    public RunResultsDTO getRunResultByRunResultId(long runResultId)
        throws HMTPException,
        UnsupportedEncodingException {
        String url = RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.RMM_RUN_RESULT_BY_ID_URL + runResultId, "", null);
        
        Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
        int status = orderClient.get().getStatus();
        
        if (status == 200) {
            return orderClient.get(new GenericType<RunResultsDTO>() {});
        }
        
        return null;
    }
    
    public RunResultsDTO getRunResultByDeviceIdAndProcessStepNameAndStatus(String deviceId, String processStepName,
        String runStatus) throws HMTPException, UnsupportedEncodingException {
        
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(runStatus)) {
            throw new HMTPException("Missing DeviceID, ProcessStepName or runStatus");
        }
        
        String url = rmmApiHostUrl + WfmURLConstants.RUN_RESULT_API_PATH + "?deviceid=" + deviceId + "&processstepname="
            + processStepName + "&runstatus=" + runStatus;
        
        Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
        int status = orderClient.get().getStatus();
        
        if (status == 200) {
            return orderClient.get(new GenericType<RunResultsDTO>() {});
        }
        
        return null;
    }
    
    public String getRunResultStatusBySampleStatus(List<SampleResultsDTO> sampleResultsDTO) throws HMTPException {
        
        if (sampleResultsDTO != null && !sampleResultsDTO.isEmpty()) {
            
            String sampleResult =
                    sampleResultsDTO.stream().map(SampleResultsDTO::getResult).reduce("", String::concat).toLowerCase();
            String flaggedStatus =
                sampleResultsDTO.stream().map(e -> StringUtils.isNotBlank(e.getFlag()) ? e.getFlag() : "")
                    .reduce("", String::concat).toLowerCase();
            
            if (sampleResult.contains("inprogress") || sampleResult.contains(flagged)
                || (sampleResult.contains(passed) && sampleResult.contains("aborted"))
                || (StringUtils.isNotBlank(flaggedStatus) && sampleResult.contains(passed))) {
                return SampleStatus.COMPLETED_WITH_FLAGS.getText();
            } else if (sampleResult.contains(passed)) {
                return SampleStatus.COMPLETED.getText();
            } else if (sampleResult.contains("aborted")) {
                return SampleStatus.ABORTED.getText();
            } else {
                throw new HMTPException("Invalid Run status");
            }
        } else {
            throw new HMTPException("Run status can't able to generate, Status are empty");
        }
    }
    
    public String getRunResultStatusBySampleStatusAndByFlag(List<SampleResultsDTO> sampleResultsDTO)
        throws HMTPException {
        
        if (sampleResultsDTO != null && !sampleResultsDTO.isEmpty()) {
            
            String sampleResult =
                    sampleResultsDTO.stream().map(SampleResultsDTO::getResult).reduce("", String::concat).toLowerCase();
            
            String flaggedStatus =
                sampleResultsDTO.stream().map(e -> StringUtils.isNotBlank(e.getFlag()) ? e.getFlag() : "")
                    .reduce("", String::concat).toLowerCase();
            
            if (sampleResult.contains("inprogress") || sampleResult.contains(flagged)
                || (sampleResult.contains(passed)
                    && (sampleResult.contains(flagged) || sampleResult.contains("failed")))
                || (StringUtils.isNotBlank(flaggedStatus) && sampleResult.contains(passed))) {
                return SampleStatus.COMPLETED_WITH_FLAGS.getText();
            } else if (sampleResult.contains("failed")) {
                return SampleStatus.FAILED.getText();
            } else if (sampleResult.contains(passed)) {
                return SampleStatus.COMPLETED.getText();
            } else {
                throw new HMTPException("Invalid Run status");
            }
        } else {
            throw new HMTPException("Run status can't able to generate, Status are empty");
        }
    }
    
}
