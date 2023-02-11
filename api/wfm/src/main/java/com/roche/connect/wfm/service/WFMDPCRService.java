/*******************************************************************************
 * WfmdPCRService.java Version: 1.0 Authors: somesh_r *********************
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
 * ********************* ********************* Description: write me
 * *********************
 ******************************************************************************/
package com.roche.connect.wfm.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMORUMessage;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.ASSAY_PROCESS_STEP_DATA;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.AssayTypeDTO;
import com.roche.connect.wfm.model.ProcessStepActionDTO;
import com.roche.connect.wfm.model.SampleWFMStates;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.repository.WfmReadRepository;
import com.roche.connect.wfm.writerepository.WfmWriteRepository;

/**
 * Class implementation that handles WFM service.
 */
@Component public class WFMDPCRService {
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
    @Autowired private RuntimeService runtimeService;
    @Autowired private TaskService taskService;
    @Autowired private WfmReadRepository wfmRepository;
    @Autowired private WfmWriteRepository wfmWriteRepository;
    @Autowired private UserTaskService userTaskService;
    @Autowired private OrderIntegrationService orderIntegrationService;
    @Autowired private AssayIntegrationService assayIntegrationService;
    @Autowired private ResultIntegrationService resultIntegrationService;
    
    /**
     * Service method to start the process.
     * @param accessioningId
     * @param activityProcessData
     * @throws HMTPException
     * @throws OrderNotFoundException
     * @throws IOException 
     */
    public void startProcess(String accessioningId, ActivityProcessDataDTO activityProcessData)
        throws HMTPException,
        OrderNotFoundException, IOException {
        logger.info(" -> startProcess()");
        ProcessInstance processInstance = startProcessInstanceByKey(accessioningId, activityProcessData);
        if (!(isEmpty(processInstance))) {
            String processid = processInstance.getId();
            wfmstate(accessioningId, activityProcessData.getDeviceId(), WORKFLOW_STATUS.QUERY.toString(), processid,
                activityProcessData.getMessageType());
            logger.info("Sample id and Process id: " + accessioningId + " " + processid);
            logger.info(" <- startProcess()");
        }
    }
    
	private static boolean isEmpty(Object objInstance) {
		return (objInstance == null || objInstance.toString().trim().isEmpty());
	}

    public List<Task> getTasks(String value) {
        return taskService.createTaskQuery().taskAssignee(value).list();
    }
    
    public void getSignal(String signal, String id) {
        runtimeService.signalEventReceived(signal, id);
    }
    
    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }
    
    /**
     * Service method to start the process by accessioning id and required
     * variables.
     * @param accessioningId
     * @param activityProcessData
     * @return ProcessInstance
     * @throws HMTPException
     * @throws OrderNotFoundException
     * @throws IOException 
     */
    private ProcessInstance startProcessInstanceByKey(String accessioningId, ActivityProcessDataDTO activityProcessData)
        throws HMTPException,
        OrderNotFoundException, IOException {
        List<WfmDTO> orderList = null;
        String assayType = null;
        ProcessInstance processInstance = null;
        List<AssayTypeDTO> assays = null;
        String workflowFileName = null;
        
        orderList = orderIntegrationService.findOrder(accessioningId);
        
        if (orderList.isEmpty()) {
            logger.error(" -> startProcessInstanceByKey()::Order is null", accessioningId);
            throw new OrderNotFoundException(
                " -> startProcessInstanceByKey()::Process cannot be started if order is null");
        } else {
            activityProcessData.setOrderDetails(orderList);
        }
        logger.info(" -> startProcessInstanceByKey()::Order found " + orderList.get(0).getOrderId());
        
        Map<String, Object> variables = new HashMap<>();
        variables.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
        variables.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);
        
        assayType = orderList.get(0).getAssayType();
        assays = assayIntegrationService.findAssayDetail(assayType);
        
        if (assays.isEmpty()) {
            logger.error(" -> startProcessInstanceByKey()::Assay is null", accessioningId);
            throw new HMTPException(" -> startProcessInstanceByKey()::Process cannot be started if assay is null");
        }
        
        workflowFileName = assays.get(0).getWorkflowDefFile();
        
        if (WORKFLOW.DPCR.toString().equalsIgnoreCase(assayType)
            && workflowFileName.startsWith(WORKFLOW.DPCR.toString())) {
            logger.info("DPCR Workflow Started for given Assay : " + assayType);
            processInstance = runtimeService.startProcessInstanceByKey(
                ASSAY_PROCESS_STEP_DATA.DPCR_WORKFLOW_FILE.toString(), accessioningId, variables);
        } else if (WORKFLOW.DPCR.toString().equalsIgnoreCase(assayType)
            && !workflowFileName.startsWith(WORKFLOW.DPCR.toString())) {
            logger.error("DPCR Workflow can't be triggered due to incorrect workflow file");
            throw new HMTPException("Error while calling startProcessInstanceByKey()..");
        }
        logger.info(" <- startProcessInstanceByKey()");
        return processInstance;
    }
    
    /**
     * Method that retrieves process steps and starts it.
     * @param accessioningId
     * @param activityProcessData
     * @throws HMTPException
     * @throws OrderNotFoundException
     * @throws IOException 
     */
    public void startMp96Process(String accessioningId, ActivityProcessDataDTO activityProcessData)
        throws HMTPException,
        OrderNotFoundException, IOException {
        String methodName = "startMp96Process";
        logger.info(methodName + " -> Start process");
        if (assayIntegrationService.validateAssayProcessStep(accessioningId, ASSAY_PROCESS_STEP_DATA.MP96.toString(),
            ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString())) {
            logger.info("accessioningId : " + accessioningId);
            startProcess(accessioningId, activityProcessData);
            logger.info(methodName + " <- Exeuction of startMp96Process End");
        } else {
            logger.error(
                "MP96 step is not configured in Assay Configuration for this sample's Assay Type, Please check the Assay Configuration for "
                    + accessioningId);
            throw new HMTPException("startMp96Process() -> Error while calling startMp96Process()..");
        }
    }
    
    /**
     * Method to persist WFM state information.
     * @param accessioningId
     * @param deviceId
     * @param status
     * @param processingId
     * @param sendingApplicationName
     */
    private void wfmstate(String accessioningId, String deviceId, String status, String processingId,
        String sendingApplicationName) {
        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        logger.info(" -> wfmstate()::Save WFM state");
        SampleWFMStates sampleWFM = new SampleWFMStates();
        sampleWFM.setAccessioningId(accessioningId);
        sampleWFM.setDeviceId(deviceId);
        sampleWFM.setCurrentStatus(status);
        sampleWFM.setProcessId(processingId);
        sampleWFM.setMessageType(sendingApplicationName);
        sampleWFM.setCreatedDatetime(new Date());
        wfmWriteRepository.save(sampleWFM, domainId);
        logger.info(" <- wfmstate():: Forte Added");
    }
    
    public void updateMp96Process(ActivityProcessDataDTO activityProcessData, String accessioningId, String orderStatus,
        String deviceId)
        throws HMTPException,
        OrderNotFoundException {
        List<WfmDTO> updateBatchDetails = null;
        String methodName = "updateMp96Process";
        SampleWFMStates rocheWfm = null;
        logger.info(methodName + " :: Entering into method");
        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        
        updateBatchDetails = orderIntegrationService.findOrder(accessioningId);
        
        if (updateBatchDetails.isEmpty()) {
            logger.error(" -> startProcessInstanceByKey()::Order is null", accessioningId);
            throw new OrderNotFoundException(
                " -> startProcessInstanceByKey()::Process cannot be started if order is null");
        } else {
            activityProcessData.setOrderDetails(updateBatchDetails);
        }
        try {
            
            SampleWFMStates rocheWfm1 =
                wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, deviceId, domainId);
            if (((rocheWfm1.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.COMPLETED.toString())
                || (rocheWfm1.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.FAILED.toString())))
                && (!rocheWfm1.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.QUERY.toString())))) {
                logger.info("Multiple ssu came for MP96");
                if (WfmConstants.WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)) {
                    orderStatus = WfmConstants.WORKFLOW_STATUS.COMPLETED.toString();
                } else if (WfmConstants.WORKFLOW_STATUS.FAILED.toString().equalsIgnoreCase(orderStatus)) {
                    orderStatus = WfmConstants.WORKFLOW_STATUS.FAILED.toString();
                }
                updateBatchDetails.get(0).setOrderStatus(orderStatus);
                
                WFMoULMessage message = activityProcessData.getU03RequestMessage();
                resultIntegrationService.updateforMP96Status(updateBatchDetails, message);
                
            } else {
                rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
                    WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(), domainId);
                String processingId = rocheWfm.getProcessId();
                long ownerId = rocheWfm.getCompany().getId();
                logger.info("Save status for process" + processingId);
                Map<String, Object> executionMP96 = new HashMap<>();
                executionMP96.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
                executionMP96.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);
                executionMP96.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUS.toString(), orderStatus);
                executionMP96.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString(), updateBatchDetails);
                
                if (WfmConstants.WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)) {
                    executionMP96.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                        WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
                    
                } else if (WfmConstants.WORKFLOW_STATUS.FAILED.toString().equalsIgnoreCase(orderStatus)) {
                    executionMP96.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                        WfmConstants.WORKFLOW_STATUS.FAILED.toString());
                }
                logger.info(" -> updateMp96Process()::Order status and accessioning id " + orderStatus + " "
                    + activityProcessData.getAccessioningId());
                List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
                    .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96U03.toString()).list();
                for (Execution execution: executions) {
                    runtimeService.signalEventReceived(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96U03.toString(),
                        execution.getId(), executionMP96);
                }
                if (WfmConstants.WORKFLOW_STATUS.FAILED.toString().equalsIgnoreCase(orderStatus)) {
                    rocheWfm.setCurrentStatus(WORKFLOW_STATUS.FAILED.toString());
                    wfmWriteRepository.save(rocheWfm, ownerId);
                }
                
                if (WfmConstants.WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)) {
                    rocheWfm.setCurrentStatus(WORKFLOW_STATUS.COMPLETED.toString());
                    wfmWriteRepository.save(rocheWfm, ownerId);
                    
                    List<ProcessStepActionDTO> processSteps =
                        assayIntegrationService.getProcessStepsByAccessioningID(accessioningId);
                    
                    for (ProcessStepActionDTO process: processSteps) {
                        if (process.getDeviceType().equalsIgnoreCase(ASSAY_PROCESS_STEP_DATA.MP96.toString())
                            && process.getManualVerificationFlag()
                                .equalsIgnoreCase(WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())) {
                            logger.info("Sending for MP96 Manual verification for " + accessioningId);
                            userTaskService.userTaskCheck(accessioningId);
                            break;
                        } else if (process.getDeviceType().equalsIgnoreCase(ASSAY_PROCESS_STEP_DATA.MP96.toString())
                            && process.getManualVerificationFlag()
                                .equalsIgnoreCase(WfmConstants.ASSAY_USER_MANUAL_TASK.N.toString())) {
                            logger.info("No MP96 Manual verification required for " + accessioningId);
                            List<Task> ll = taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId()
                                .desc().list();
                            String taskId = ll.get(0).getId();
                            if (!taskId.isEmpty()) {
                                taskService.complete(taskId);
                            }
                            break;
                        }
                    }
                }
                logger.info(" <- updateMp96Process()::Exeuction of Mp96 updateProcess End");
            }
        } catch (HMTPException  | IOException ex) {
            logger.error("ERR:-" + ex.getMessage());
        }
    }
    
    public void updatemp96Ack(ActivityProcessDataDTO activityProcessData, String accessioningId){
        logger.info(" -> updatemp96Ack():: mp96 ACK is recived)");
        
        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(), domainId);
        String processingId = rocheWfm.getProcessId();
        logger.info("To carrying Procees Id" + processingId);
        Map<String, Object> executionMP96 = new HashMap<>();
        executionMP96.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
        executionMP96.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);
        
        logger.info(" -> updatemp96Ack():: accessioning id " + activityProcessData.getAccessioningId());
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
            .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96ACK.toString()).list();
        for (Execution execution: executions) {
            runtimeService.signalEventReceived(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96ACK.toString(), execution.getId(),
                executionMP96);
        }
    }
    
    ResponseRenderingService responseRenderingService = new ResponseRenderingService();
    
    public void startdPCRLp24Process(String containerId, String accessioningId, String deviceId,
        ActivityProcessDataDTO activityProcessData)
        throws HMTPException,
        OrderNotFoundException {
        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        SampleWFMStates rocheWfm1 = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString(), domainId);
        if (!(isEmpty(rocheWfm1))) {
            long ownerId = rocheWfm1.getCompany().getId();
            String lpstatus = rocheWfm1.getCurrentStatus();
            String lpaccessioningId = rocheWfm1.getAccessioningId();
            String messageControlId = activityProcessData.getQueryMessage().getMessageControlId();
            logger.info("lpaccessioningId  is duplicated " + lpaccessioningId);
            if (lpstatus.equalsIgnoreCase(WORKFLOW_STATUS.QUERY.toString())) {
                rocheWfm1.setDeviceId(deviceId);
                rocheWfm1.setCreatedDatetime(new Date());
                wfmWriteRepository.save(rocheWfm1, ownerId);
                responseRenderingService.duplicateQueryPositiveResponsefordPCRLP(containerId, deviceId,lpaccessioningId, messageControlId);
                
            } else {
                responseRenderingService.duplicateQueryNegativeResponsefordPCRLP(containerId, deviceId,lpaccessioningId, messageControlId);
            }
            
        } else {
            String[] container = containerId.trim().split("_");
            activityProcessData.setAccessioningId(accessioningId);
            List<WfmDTO> orderdetail = orderIntegrationService.findOrder(accessioningId);
            SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
                WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(), domainId);
            String processingId = rocheWfm.getProcessId();
            String status = rocheWfm.getCurrentStatus();
            if (!status.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString())) {
                Map<String, Object> executionLp24dPCR = new HashMap<>();
                executionLp24dPCR.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDDPCRLP24.toString(),
                    accessioningId);
                executionLp24dPCR.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
                    activityProcessData);
                executionLp24dPCR.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDDPCRLP24.toString(), container[0]);
                executionLp24dPCR.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONDPCRLP24.toString(),
                    container[1]);
                executionLp24dPCR.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRLPUPDATE.toString(), orderdetail);
                
                List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
                    .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24QBP.toString()).list();
                for (Execution execution: executions) {
                    runtimeService.signalEventReceived(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24QBP.toString(),
                        execution.getId(), executionLp24dPCR);
                    
                    wfmstate(accessioningId, deviceId, WORKFLOW_STATUS.QUERY.toString(), processingId,
                        activityProcessData.getQueryMessage().getSendingApplicationName());
                    
                }
                logger.info(" Exeuction of LP24 dPCR Start Process End");
            } else {
                logger.info("Sample Already Aborted for ContainerId: " + containerId);
            }
        }
    }
    
    public void startdPCRAnalyzerProcess(String containerId, String containerPosition, String accessioningId, String deviceId,
        ActivityProcessDataDTO activityProcessData)
        {
        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        SampleWFMStates rocheWfm1 = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            activityProcessData.getWfmQueryMessage().getProcessStepName(), domainId);
        if (!(isEmpty(rocheWfm1))) {
            String analyzerStatus = rocheWfm1.getCurrentStatus();
            if (analyzerStatus.equalsIgnoreCase(WORKFLOW_STATUS.QUERY.toString())
                && !(rocheWfm1.getDeviceId().equalsIgnoreCase(deviceId))) {
                rocheWfm1.setDeviceId(deviceId);
                rocheWfm1.setCreatedDatetime(new Date());
                wfmWriteRepository.save(rocheWfm1, domainId);
            }
        } else {
            
            SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(), domainId);
            String processingId = rocheWfm.getProcessId();
            String status = rocheWfm.getCurrentStatus();
            
            if (!status.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString())) {
                Map<String, Object> executiondPCRAnalyzer = new HashMap<>();
                
                List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
                    .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERQBP.toString()).list();
                for (Execution execution: executions) {
                    runtimeService.signalEventReceived(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERQBP.toString(),
                        execution.getId(), executiondPCRAnalyzer);
                    wfmstate(accessioningId, deviceId, WORKFLOW_STATUS.QUERY.toString(), processingId,
                        activityProcessData.getWfmQueryMessage().getProcessStepName());
                }
                logger.info(" Exeuction of dPCR Analyzer Start Process End");
            } else {
                logger.info("Sample Already Aborted for ContainerId: " + containerId + "containerPosition:" +containerPosition);
            }
        }
    }
    
    public void updatedPCRAnalyzerAck(ActivityProcessDataDTO activityProcessData, String accessioningId) {
        logger.info(" -> updatempdPCRAnalyzerAck():: mp96 ACK is recived)");
        
        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRANALYZER.toString(), domainId);        
             String processingId = rocheWfm.getProcessId();
              logger.info("To carrying Procees Id" + processingId);             
              Map<String, Object> executionAnalyzerACK = new HashMap<>();
              executionAnalyzerACK.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
              executionAnalyzerACK.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);
              logger.info(" -> updatempAnalyzerAck():: accessioning id " + accessioningId);
              
            List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERACK.toString()).list();
            logger.info(" executions size: " + executions.size());
            for (Execution execution: executions) {              
                runtimeService.signalEventReceived(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERACK.toString(),execution.getId(), executionAnalyzerACK);
            }
                
    }
    
    
    public void updatedPCRAnalyzerProcess(String accessioningId, String orderStatus, ActivityProcessDataDTO activityProcessData)
        throws HMTPException,
        OrderNotFoundException, IOException {
    	logger.info("updatedPCRAnalyzerProcess()-> +AccessioningId "+accessioningId);
        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();        
        List<WfmDTO> updateBatchDetails = orderIntegrationService.findOrder(accessioningId);
        logger.info("updatedPCRAnalyzerProcess()-> findOrder::"+updateBatchDetails);
        String deviceId = activityProcessData.getDeviceId();
        SampleWFMStates rocheWfm1 =
                wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, deviceId, domainId);
        
        logger.info("updatedPCRAnalyzerProcess()-> findOneByAccessioningIdAndDeviceId::"+rocheWfm1);
        
        if((!rocheWfm1.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.INPROGRESS.toString()))
				&& (!rocheWfm1.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.QUERY.toString()))
				&& (!rocheWfm1.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.DPCRORU.toString()))){
        	
        	logger.info("Multiple ssu came for DpcrFinalESU");
            if (WfmConstants.WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)) {
                orderStatus = WfmConstants.WORKFLOW_STATUS.COMPLETED.toString();
            } else if (WfmConstants.WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus)) {
                orderStatus = WfmConstants.WORKFLOW_STATUS.ABORTED.toString();
            }
            updateBatchDetails.get(0).setOrderStatus(orderStatus);
            
            WFMESUMessage message = activityProcessData.getWfmESUMessage();
            resultIntegrationService.updatefordPCRAnalyzer(updateBatchDetails, message);
        }else if((orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.INPROGRESS.toString())) && (rocheWfm1.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.DPCRORU.toString()))){
        	logger.info("Multiple ssu inprogress  came ofter DpcrORU");
        	updateBatchDetails.get(0).setOrderStatus(orderStatus);
        	WFMESUMessage message = activityProcessData.getWfmESUMessage();
        	resultIntegrationService.updatefordPCRAnalyzer(updateBatchDetails, message);
        }
        else{
        	
        	logger.info("SSU came first time"+orderStatus);
        Map<String, Object> executiondPCRAnalyzer = new HashMap<>();
        executiondPCRAnalyzer.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
        executiondPCRAnalyzer.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRANALYZERUPDATE.toString(),
        		updateBatchDetails);
        if (WfmConstants.WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)) {
            executiondPCRAnalyzer.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
            executiondPCRAnalyzer.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRANALYZERSTATUS.toString(),
                WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
            
        } else if (WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
            executiondPCRAnalyzer.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_MESSAGE_TYPE.ANALYZERSSU.toString());
            executiondPCRAnalyzer.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRANALYZERSTATUS.toString(),
                WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString());
            
        } else if (WfmConstants.WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus) || StringUtils.isEmpty(orderStatus)) {
            executiondPCRAnalyzer.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.ABORTED.toString());
            executiondPCRAnalyzer.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRANALYZERSTATUS.toString(),
                WfmConstants.WORKFLOW_STATUS.ABORTED.toString());
            
        }  
        
        SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.ASSAY_PROCESS_STEP_DATA.DPCRANALYZERPROCESSNAME.toString(), domainId);
        
        long ownerId = rocheWfm.getCompany().getId();
        String processingId = rocheWfm.getProcessId();
        
        if (activityProcessData.getWfmESUMessage().getMessageType()
            .equalsIgnoreCase(WfmConstants.WORKFLOW_MESSAGE_TYPE.ANALYZERSSU.toString())
            && orderStatus.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString())) {
            
            List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERESU.toString()).list();
            for (Execution execution: executions) {
                runtimeService.signalEventReceived(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERESU.toString(),
                    execution.getId(), executiondPCRAnalyzer);
            }
        } else {
            
            List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERFINALESU.toString()).list();
            for (Execution execution: executions) {
                runtimeService.signalEventReceived(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERFINALESU.toString(),
                    execution.getId(), executiondPCRAnalyzer);
            }
            
        }
        
        if (WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
            rocheWfm.setCurrentStatus(WORKFLOW_STATUS.INPROGRESS.toString());
            
            wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus)) {
            rocheWfm.setCurrentStatus(WORKFLOW_STATUS.ABORTED.toString());
            
            wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)) {
            rocheWfm.setCurrentStatus(WORKFLOW_STATUS.COMPLETED.toString());
            
            wfmWriteRepository.save(rocheWfm, ownerId);
            
            if (assayIntegrationService.validateAssayProcessStepManual(accessioningId,
                ASSAY_PROCESS_STEP_DATA.DPCR_ANALYZER.toString(),
                ASSAY_PROCESS_STEP_DATA.DPCRANALYZERPROCESSNAME.toString(),
                WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())) {
                userTaskService.userTaskCheck(accessioningId);
            } else {
                List<Task> ts =
                    taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId().desc().list();
                String taskId = ts.get(0).getId();
                if (!taskId.isEmpty()) {
                    taskService.complete(taskId);
                }
            }
        }}
        
        logger.info(" <- Execution of updatedPCRAnalyzerProcess End");
    }
    
    public void updatedPCRLp24Process(String accessioningId, String deviceId, String orderStatus,
        ActivityProcessDataDTO activityProcessData)
        throws HMTPException,
        OrderNotFoundException, IOException {
        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        Map<String, Object> executiondPCRLp24 = new HashMap<>();
        executiondPCRLp24.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
        executiondPCRLp24.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDDPCR24UPDATE.toString(), accessioningId);
        executiondPCRLp24.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDDPCRLP24UPDATE.toString(), deviceId);
        executiondPCRLp24.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSDPCRLP24UPDATE.toString(), orderStatus);
        
        if (WfmConstants.WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)) {
            executiondPCRLp24.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
            
        } else if (WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
            executiondPCRLp24.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString());
            
        } else if (WfmConstants.WORKFLOW_STATUS.FAILED.toString().equalsIgnoreCase(orderStatus)) {
            executiondPCRLp24.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.FAILED.toString());
            
        } else if (WfmConstants.WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)) {
            executiondPCRLp24.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
            
        } else {
            executiondPCRLp24.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString());
            
        }
        SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString(), domainId);
        long ownerId = rocheWfm.getCompany().getId();
        String processingId = rocheWfm.getProcessId();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
            .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24U03.toString()).list();
        for (Execution execution: executions) {
            runtimeService.signalEventReceived(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24U03.toString(), execution.getId(),
                executiondPCRLp24);
        }
        if (WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
            rocheWfm.setCurrentStatus(WORKFLOW_STATUS.INPROGRESS.toString());
            wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WORKFLOW_STATUS.FAILED.toString().equalsIgnoreCase(orderStatus)) {
            rocheWfm.setCurrentStatus(WORKFLOW_STATUS.FAILED.toString());
            wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
            || WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)) {
            rocheWfm.setCurrentStatus(WORKFLOW_STATUS.COMPLETED.toString());
            wfmWriteRepository.save(rocheWfm, ownerId);
            
            if (assayIntegrationService.validateAssayProcessStepManual(accessioningId,
                ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString(),
                WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())) {
                userTaskService.userTaskCheck(accessioningId);
            } else {
                List<Task> ts =
                    taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId().desc().list();
                String taskId = ts.get(0).getId();
                if (!taskId.isEmpty()) {
                    taskService.complete(taskId);
                }
            }
        }
        logger.info(" <- Execution of updatedPCRLp24Process End");
    }
    
 public void updatedPCRORUProcess(String accessioningId,String deviceId,ActivityProcessDataDTO activityProcessData)
            throws HMTPException, OrderNotFoundException {

        long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
        List<WfmDTO> updateORUDetails = orderIntegrationService.findOrder(accessioningId);
        SampleWFMStates rocheWfm1 =
                wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, deviceId, domainId);
        if((rocheWfm1.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.DPCRORU.toString()))){
        	WFMORUMessage message = activityProcessData.getoRUMessage();
        	logger.info("Multiple ORU came for DpcrORU");
            resultIntegrationService.updateforDPCRAnalyzerORU(updateORUDetails, message);
        }
        Map<String, Object> executiondPCRORU = new HashMap<>();
        executiondPCRORU.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
        executiondPCRORU.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDDPCRORU.toString(), accessioningId);
        executiondPCRORU.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDDPCRORU.toString(), deviceId);
        executiondPCRORU.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRANALYZERORU.toString(), updateORUDetails);

        if ( WfmConstants.WORKFLOW_MESSAGE_TYPE.ORU.toString()
                        .equalsIgnoreCase(activityProcessData.getoRUMessage().getMessageType())) {
            executiondPCRORU.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(), "ORU");
       
        }

        SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
                WfmConstants.ASSAY_PROCESS_STEP_DATA.DPCRANALYZERPROCESSNAME.toString(), domainId);
        long ownerId = rocheWfm.getCompany().getId();
        String processingId = rocheWfm.getProcessId();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERESU.toString()).list();
        for (Execution execution : executions) {
            runtimeService.signalEventReceived(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERESU.toString(),
                    execution.getId(), executiondPCRORU);
        }
        rocheWfm.setCurrentStatus(WORKFLOW_STATUS.DPCRORU.toString());
        wfmWriteRepository.save(rocheWfm, ownerId);
        logger.info(" <- Execution of updatedPCRORUProcess End");

    }
}
