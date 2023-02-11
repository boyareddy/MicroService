/*******************************************************************************
 * WfmService.java Version: 1.0 Authors: somesh_r *********************
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.amm.dto.MolecularIDTypeDTO;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.util.SampleStatus;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.ASSAY_PROCESS_STEP_DATA;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.error.QueryValidationException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.AssayTypeDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.ProcessStepActionDTO;
import com.roche.connect.wfm.model.SampleWFMStates;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.repository.WfmReadRepository;
import com.roche.connect.wfm.writerepository.WfmWriteRepository;

/**
 * Class implementation that handles WFM service.
 */

@Component
public class WFMHTPService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	private static final String TERTIARY = "TERTIARY";
	private static final String SECONDARY = "SECONDARY";
	private static final String START = "START";
	private static final String INPROGRESS = "INPROGRESS";
	private static final String DONE = "DONE";
	private static final String ERROR = "ERROR";

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private WfmWriteRepository wfmWriteRepository;
	@Autowired
	private WfmReadRepository wfmRepository;
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private OrderIntegrationService orderIntegrationService;
	@Autowired
	private AssayIntegrationService assayIntegrationService;
	@Autowired
	private ResultIntegrationService resultIntegrationService;
	@Autowired
	private ResponseRenderingService responseRenderingService;

	@Value("${pas.adm_api_url}")
	private String admhosturl;

	private String signal = null;

	/**
	 * Service method to start the process.
	 * 
	 * @param accessioningId
	 * @param activityProcessData
	 * @throws HMTPException
	 * @throws OrderNotFoundException
	 * @throws IOException 
	 */
	public void startProcess(String accessioningId, ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException, IOException {
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
		return (objInstance == null || objInstance.toString().trim().isEmpty()) ? true : false;
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
	 * 
	 * @param accessioningId
	 * @param activityProcessData
	 * @return ProcessInstance
	 * @throws HMTPException
	 * @throws OrderNotFoundException
	 * @throws IOException 
	 */
	private ProcessInstance startProcessInstanceByKey(String accessioningId, ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException, IOException {

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

		if (WORKFLOW.NIPT.toString().equalsIgnoreCase(assayType)
				&& workflowFileName.startsWith(WORKFLOW.NIPT.toString())) {
			logger.info("NIPT Workflow Started for given Assay : " + assayType);
			processInstance = runtimeService.startProcessInstanceByKey(
					ASSAY_PROCESS_STEP_DATA.NIPT_WORKFLOW_FILE.toString(), accessioningId, variables);
		} else if (WORKFLOW.NIPT.toString().equalsIgnoreCase(assayType)
				&& !workflowFileName.startsWith(WORKFLOW.NIPT.toString())) {
			logger.error("NIPT Workflow can't be triggered due to incorrect workflow file");
			throw new HMTPException("Error while calling startProcessInstanceByKey()..");
		} else if (WORKFLOW.DPCR.toString().equalsIgnoreCase(assayType)
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
	 * 
	 * @param accessioningId
	 * @param activityProcessData
	 * @throws HMTPException
	 * @throws OrderNotFoundException
	 * @throws IOException 
	 */
	public void startMp24Process(String accessioningId, ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException, IOException {
		String methodName = "startMp24Process";
		logger.info(methodName + " -> Start process");
		if (assayIntegrationService.validateAssayProcessStep(accessioningId, ASSAY_PROCESS_STEP_DATA.MP24.toString(),
				ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString())) {
			logger.info("accessioningId : " + accessioningId);
			startProcess(accessioningId, activityProcessData);
			logger.info(methodName + " <- Execution of startProcess End");
		} else {
			logger.error(
					"MP24 step is not configured in Assay Configuration for this sample's Assay Type, Please check the Assay Configuration for "
							+ accessioningId);
			throw new HMTPException("startMp24Process() -> Error while calling startMp24Process()..");
		}
	}

	public void startLp24PrePcrProcess(String containerId, String deviceId,String processStepName, ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException, IOException {
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		String token = (String) ThreadSessionManager.currentUserSession()
				.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
		String[] container = containerId.trim().split("_");
		List<WfmDTO> orderdetails = resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString());

		String accessioningId = orderdetails.iterator().next().getAccessioningId();
		activityProcessData.setAccessioningId(accessioningId);
		String sampleStatus = orderdetails.iterator().next().getOrderStatus();
		List<WfmDTO> orderdetail = orderIntegrationService.findOrder(accessioningId);
		SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString(), domainId);
		String processingId = rocheWfm.getProcessId();
		String status = rocheWfm.getCurrentStatus();
		if ((!sampleStatus.equalsIgnoreCase(SampleStatus.INPROGRESS.toString())) && (!orderdetails.isEmpty())
				&& !status.equalsIgnoreCase(SampleStatus.INPROGRESS.toString())) {

			assayValidationForLPPrePCRStartProcess(processStepName,activityProcessData, domainId, container,orderdetail, processingId, status);
		} else {

			AdmNotificationService.sendNotification(NotificationGroupType.NO_ORDER_FOUND_LP24,
					getContent(containerId, deviceId), token, admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
			throw new OrderNotFoundException();
		}

	}

    private void assayValidationForLPPrePCRStartProcess(String processStepName, ActivityProcessDataDTO activityProcessData,
        long domainId, String[] container,List<WfmDTO> orderdetail, String processingId,
        String status) throws HMTPException, OrderNotFoundException, IOException {
        
        if (assayIntegrationService.validateAssayProcessStep(activityProcessData.getAccessioningId(), activityProcessData.getQueryMessage().getSendingApplicationName(), processStepName)) {
            
            if (!status.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString())) {
                
                SampleWFMStates rocheWfm1 =
                    wfmRepository.findByAccessioningIdAndMessageType(activityProcessData.getAccessioningId(), processStepName, domainId);
                if (rocheWfm1 != null) {
                    setDuplicateQueryforLPPrePCR(activityProcessData.getQueryMessage().getContainerId(), activityProcessData.getQueryMessage().getDeviceSerialNumber(), activityProcessData.getQueryMessage().getSendingApplicationName(), activityProcessData,
                        activityProcessData.getAccessioningId(), rocheWfm1);
                } else {
                    startLPPrePCRExecution(activityProcessData.getQueryMessage().getDeviceSerialNumber(), processStepName, activityProcessData, container, activityProcessData.getAccessioningId(),
                        orderdetail, processingId);
                }
            } else {
                logger.info("Sample Already Aborted for ContainerId: " + activityProcessData.getQueryMessage().getContainerId());
                throw new OrderNotFoundException();
            }
            logger.info(" Execution of LP24PrePCR Start Process End");
            
        } else {
            logger.error("LP Pre PCR step is not configured in Assay Configuration for this sample's Assay Type");
            throw new HMTPException("Error while calling startLp24PrePcrProcess().. ");
        }
    }

    private void startLPPrePCRExecution(String deviceId, String processStepName,
        ActivityProcessDataDTO activityProcessData, String[] container, String accessioningId, List<WfmDTO> orderdetail,
        String processingId) {
        Map<String, Object> executionLp24PrePcr = new HashMap<>();
        executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCR.toString(),
        		accessioningId);
        executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
        		activityProcessData);
        executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDPREPCR.toString(),
        		container[0]);
        executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONPREPCR.toString(),
        		container[1]);
        executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPREPCRUPDATE.toString(),
        		orderdetail);

        List<Execution> executions = runtimeService.createExecutionQuery()
        		.processInstanceId(processingId)
        		.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRQBP.toString())
        		.list();
        for (Execution execution : executions) {
        	runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRQBP.toString(),
        			execution.getId(), executionLp24PrePcr);

        	wfmstate(accessioningId, deviceId, WORKFLOW_STATUS.QUERY.toString(), processingId,
        			processStepName);
        }
    }

    private void setDuplicateQueryforLPPrePCR(String containerId, String deviceId, String sendingApplicationName,
        ActivityProcessDataDTO activityProcessData, String accessioningId, SampleWFMStates rocheWfm1) {
        long ownerId = rocheWfm1.getCompany().getId();
        if (rocheWfm1.getCurrentStatus().equalsIgnoreCase("Query")) {
        	rocheWfm1.setDeviceId(deviceId);
        	rocheWfm1.setCreatedDatetime(new Date());
        	wfmWriteRepository.save(rocheWfm1, ownerId);
        	responseRenderingService.duplicateOrderQueryPositiveResponseforLp24(containerId, deviceId,
        			accessioningId, activityProcessData); // Positive
        													// Scenario
        } else {
        	responseRenderingService.duplicateOrderQueryResponseforLP(containerId, deviceId,
        			sendingApplicationName,
        			activityProcessData.getQueryMessage().getMessageControlId()); // Negative
        																			// Scenario
        }
    }

	/**
	 * Method to persist WFM state information.
	 * 
	 * @param accessioningId
	 * @param deviceId
	 * @param status
	 * @param processingId
	 * @param sendingApplicationName
	 */
	private void wfmstate(String accessioningId, String deviceId, String status, String processingId,
			String processStepName) {
		logger.info(" -> wfmstate()::Save WFM state");
		SampleWFMStates sampleWFM = new SampleWFMStates();
		sampleWFM.setAccessioningId(accessioningId);
		sampleWFM.setDeviceId(deviceId);
		sampleWFM.setCurrentStatus(status);
		sampleWFM.setProcessId(processingId);
		sampleWFM.setMessageType(processStepName);
		sampleWFM.setCreatedDatetime(new Date());
		wfmWriteRepository.save(sampleWFM);
		logger.info(" <- wfmstate():: Added");
	}

	public void startLp24PostPcrProcess(String containerId, String deviceId,String processStepName, ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException, IOException {
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		String token = (String) ThreadSessionManager.currentUserSession()
				.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
		String[] container = containerId.trim().split("_");
		List<WfmDTO> orderdetails = resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString());

		String accessioningId = orderdetails.iterator().next().getAccessioningId();
		activityProcessData.setAccessioningId(accessioningId);
		String sampleStatus = orderdetails.iterator().next().getOrderStatus();
		List<WfmDTO> orderdetail = orderIntegrationService.findOrder(accessioningId);
		SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString(), domainId);
		String processingId = rocheWfm.getProcessId();
		String status = rocheWfm.getCurrentStatus();
		if ((!sampleStatus.equalsIgnoreCase(SampleStatus.INPROGRESS.toString())) && (!orderdetails.isEmpty())
				&& !status.equalsIgnoreCase(SampleStatus.INPROGRESS.toString())) {

			assayValidationforLPPostPCRStartProcess(processStepName,activityProcessData, domainId, container,orderdetail, processingId, status);
		} else {

			AdmNotificationService.sendNotification(NotificationGroupType.NO_ORDER_FOUND_LP24,
					getContent(containerId, deviceId), token, admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
			throw new OrderNotFoundException();
		}
	}

    private void assayValidationforLPPostPCRStartProcess(String processStepName, ActivityProcessDataDTO activityProcessData,
        long domainId, String[] container,List<WfmDTO> orderdetail, String processingId,
        String status) throws HMTPException, OrderNotFoundException, IOException {
        if (assayIntegrationService.validateAssayProcessStep(activityProcessData.getAccessioningId(), activityProcessData.getQueryMessage().getSendingApplicationName(),
        		processStepName)) {
        	SampleWFMStates rocheWfm1 = wfmRepository.findByAccessioningIdAndMessageType(activityProcessData.getAccessioningId(),
        			processStepName, domainId);
        	activityProcessData.setAccessioningId(activityProcessData.getAccessioningId());

        	if (!status.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString())) {
        		if (rocheWfm1 != null) {
        			setDuplicateQueryforLPPostPCR(activityProcessData.getQueryMessage().getContainerId(), activityProcessData.getQueryMessage().getDeviceSerialNumber(), activityProcessData.getQueryMessage().getSendingApplicationName(), activityProcessData,
        			    activityProcessData.getAccessioningId(), rocheWfm1);
        		} else {
        			startLPPostPCRExecution(activityProcessData.getQueryMessage().getDeviceSerialNumber(), processStepName, activityProcessData, container, activityProcessData.getAccessioningId(),
                        orderdetail, processingId);
        		}
        	} else {
        		logger.info("Sample already aborted for ContainerId : " + activityProcessData.getQueryMessage().getContainerId());
        		throw new OrderNotFoundException();
        	}

        } else {
        	logger.error(
        			"LP Post PCR step is not configured in Assay Configuration for this sample's Assay Type, Please check the Assay Configuration");
        	throw new HMTPException("Error while processing startLp24PostPcrProcess().. ");
        }
    }

    private void startLPPostPCRExecution(String deviceId, String processStepName,
        ActivityProcessDataDTO activityProcessData, String[] container, String accessioningId, List<WfmDTO> orderdetail,
        String processingId) {
        Map<String, Object> executionLp24PostPcr = new HashMap<>();
        executionLp24PostPcr.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPOSTPCR.toString(),
        		accessioningId);
        executionLp24PostPcr.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
        		activityProcessData);
        executionLp24PostPcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDPOSTPCR.toString(),
        		container[0]);
        executionLp24PostPcr.put(
        		WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONPOSTPCR.toString(), container[1]);
        executionLp24PostPcr.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPOSTPCRUPDATE.toString(),
        		orderdetail);

        List<Execution> executions = runtimeService.createExecutionQuery()
        		.processInstanceId(processingId)
        		.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRQBP.toString())
        		.list();
        for (Execution execution : executions) {
        	runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRQBP.toString(),
        			execution.getId(), executionLp24PostPcr);

        	wfmstate(accessioningId, deviceId, WORKFLOW_STATUS.QUERY.toString(), processingId,
        			processStepName);
        }
        logger.info(" Execution of LP24 PostPCR Start Process End");
    }

    private void setDuplicateQueryforLPPostPCR(String containerId, String deviceId, String sendingApplicationName,
        ActivityProcessDataDTO activityProcessData, String accessioningId, SampleWFMStates rocheWfm1) {
        long ownerId = rocheWfm1.getCompany().getId();
        if (!rocheWfm1.getCurrentStatus().equalsIgnoreCase("Query")) {
        	responseRenderingService.duplicateOrderQueryResponseforLP(containerId, deviceId,
        			sendingApplicationName,
        			activityProcessData.getQueryMessage().getMessageControlId()); // Negative
        																			// Scenario
        } else {
        	rocheWfm1.setDeviceId(deviceId);
        	rocheWfm1.setCreatedDatetime(new Date());
        	wfmWriteRepository.save(rocheWfm1, ownerId);
        	responseRenderingService.duplicateOrderQueryPositiveResponseforLp24(containerId, deviceId,
        			accessioningId, activityProcessData); // Positive
        													// Scenario
        }
    }

	public void startLp24SeqPrepProcess(String containerId, String deviceId, String accessioningId,
			String processStepName, ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException, IOException {

		if (assayIntegrationService.validateAssayProcessStep(accessioningId, ASSAY_PROCESS_STEP_DATA.LP24.toString(),
				processStepName)) {

			long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
			List<WfmDTO> orderdetails = orderIntegrationService.findOrder(accessioningId);

			SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString(), domainId);
			SampleWFMStates rocheWfm1 = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
					processStepName, domainId);

			String processingId = rocheWfm.getProcessId();
			String status = rocheWfm.getCurrentStatus();

			if (!status.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString())) {
				if (rocheWfm1 != null) {
					long ownerId = rocheWfm1.getCompany().getId();
					rocheWfm1.setDeviceId(deviceId);
					rocheWfm1.setCreatedDatetime(new Date());
					wfmWriteRepository.save(rocheWfm1, ownerId);
				} else {

					Map<String, Object> executionforLp24SeqPre = new HashMap<>();
					executionforLp24SeqPre.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDSEQPCR.toString(),
							accessioningId);
					executionforLp24SeqPre.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
							activityProcessData);
					executionforLp24SeqPre.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSSEQPREPUPDATE.toString(),
							orderdetails);

					List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
							.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPQBP.toString())
							.list();
					for (Execution execution : executions) {
						runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPQBP.toString(),
								execution.getId(), executionforLp24SeqPre);

						wfmstate(accessioningId, deviceId, WORKFLOW_STATUS.QUERY.toString(), processingId,
								processStepName);
					}
				}
				logger.info(" Execution of LP24 Seq Prep Start Process End");
			} else {
				logger.info(" Sample Already Aborted for ContainerId:" + containerId);
				throw new OrderNotFoundException();
			}
		} else {
			logger.error(
					"LP Sequence Prep step is not configured in Assay Configuration for this sample's Assay Type, Please check the Assay Configuration");
			throw new HMTPException("Error while calling startLp24SeqPrepProcess()..");
		}

	}

	public boolean updateForteProcess(String accessioningId,String status,
			String sendingApplicationName, String jobType) throws HMTPException, QueryValidationException {

		logger.info("WfmService ::<= updateForteProcess() Started");
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		boolean forteDeviceLevel = jobType.equalsIgnoreCase("Secondary")
				|| (jobType.equalsIgnoreCase("Tertiary") && status.equalsIgnoreCase("Start"));

		SampleWFMStates rocheWfm = forteDeviceLevel
				? wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
						WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.HTP.toString(), domainId)
				: wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId,
						WORKFLOW_SENDINGAPPLICATIONNAME.FORTE.toString() + "-" + jobType, domainId);

		if (!isEmpty(rocheWfm)) {
			String processingId = rocheWfm.getProcessId();
			Map<String, Object> exeForte = new HashMap<>();
			exeForte.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDFORTE.toString(), accessioningId);
			exeForte.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDFORTE.toString(),
					WORKFLOW_SENDINGAPPLICATIONNAME.FORTE.toString());
			exeForte.put(WfmConstants.WORKFLOW_VARIABLES.MESSAGETYPEFORTE.toString(), status);
			exeForte.put(WfmConstants.WORKFLOW_VARIABLES.PROCESSINGID.toString(), processingId);
			exeForte.put(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.FORTE.toString(), sendingApplicationName);
			exeForte.put("forteDeviceLevel", forteDeviceLevel);

			List<Execution> executions = null;

			logger.info("FORTE jobType ::=>" + jobType);

			switch (jobType.toUpperCase()) {
			case SECONDARY:
				executions = processForteJobType(processingId,
						WfmConstants.WORKFLOW_SIGNALS.FORTESECONDARYEVENT.toString());
				break;
			case TERTIARY:
				switch (status.toUpperCase()) {
				case START:
					executions = processForteJobType(processingId,
							WfmConstants.WORKFLOW_SIGNALS.FORTETERTIARYSTARTEVENT.toString());
					break;
				case INPROGRESS:
					executions = processForteJobType(processingId,
							WfmConstants.WORKFLOW_SIGNALS.FORTETERTIARYINPROGRESSEVENT.toString());
					break;
				case DONE:
					if (status.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.DONE.toString()))
						exeForte.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
								WfmConstants.WORKFLOW_STATUS.DONE.toString());// dOne
					else {
						throw new QueryValidationException();
					}
					executions = processForteJobType(processingId,
							WfmConstants.WORKFLOW_SIGNALS.FORTETERTIARYDONEEVENT.toString());
					break;
				case ERROR:
					if (status.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ERROR.toString()))
						exeForte.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
								WfmConstants.WORKFLOW_STATUS.ERROR.toString());// dOne
					else {
						throw new QueryValidationException();
					}
					executions = processForteJobType(processingId,
							WfmConstants.WORKFLOW_SIGNALS.FORTETERTIARYFAILEDEVENT.toString());
					break;
				default:
					return false;
				}
				break;
			default:
				return false;
			}

			if (!isExecutionStart(status, jobType, rocheWfm, exeForte, executions))
				throw new HMTPException("ERR:- WfmService :: Executions isEmpty()");
			else {
				logger.info("WFM ::=> FORTE Status has been updated");
				return true;
			}
		} else {
			throw new HMTPException("ERR:- WfmService :: SampleWFMStates isEmpty()");
		}
	}

	public boolean isExecutionStart(String status, String jobType, SampleWFMStates rocheWfm,
			Map<String, Object> exeForte, List<Execution> executions) {

		String forteStatus = (jobType + "-" + status);

		logger.info("WfmService <=:: isExecutionStart()");

		if (!executions.isEmpty()) {
			executions.stream().forEach(i -> {
				runtimeService.signalEventReceived(this.signal, i.getId(), exeForte);
				if ((boolean) exeForte.get("forteDeviceLevel")) {
					wfmstate(
							(String) exeForte
									.get((String) WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDFORTE.toString()),
							(String) exeForte.get(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDFORTE.toString()) + "-"
									+ jobType,
							forteStatus,
							(String) exeForte.get((String) WfmConstants.WORKFLOW_VARIABLES.PROCESSINGID.toString()),
							(String) exeForte.get(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDFORTE.toString()));
				} else {

					String updatedStatus = null;

					if (status.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString()))
						updatedStatus = WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString();
					else if (status.equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.DONE.toString()))
						updatedStatus = jobType + "-" + WfmConstants.WORKFLOW_STATUS.DONE.toString();
					else
						updatedStatus = WfmConstants.WORKFLOW_STATUS.ERROR.toString();

					rocheWfm.setCurrentStatus(updatedStatus);

					wfmRepository.save(rocheWfm);

				}
			});
			logger.info(" WfmService ::=> isExecutionStart()");
			return true;
		}
		return false;
	}

	public void updateMp24Process(ActivityProcessDataDTO activityProcessData, String accessioningId, String orderStatus,
			String deviceId) throws HMTPException, OrderNotFoundException, QueryValidationException, IOException {
		List<WfmDTO> orderList = null;
		List<DeviceTestOptionsDTO> deviceTestOptions;
		String protocolName = "";
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		String methodName = "updateMp24Process";
		SampleWFMStates rocheWfm = null;
		logger.info(methodName + " :: Entering into method");
		orderList = orderIntegrationService.findOrder(accessioningId);
		String token = (String) ThreadSessionManager.currentUserSession()
				.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
		if (orderList.isEmpty()) {
			logger.error(" -> startProcessInstanceByKey()::Order is null", accessioningId);
			throw new OrderNotFoundException(
					" -> startProcessInstanceByKey()::Process cannot be started if order is null");
		} else {
			activityProcessData.setOrderDetails(orderList);
		}
		rocheWfm = wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, deviceId, domainId);
		if (rocheWfm == null) {

			responseRenderingService.duplicateNegativeACKForMP24(accessioningId, deviceId, activityProcessData);
			AdmNotificationService.sendNotification(NotificationGroupType.UNSUPPORTED_HL7_MSG_MP24,
					getContent(deviceId), token, admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
		} else {
			deviceTestOptions = assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(orderList,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
					WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString());
			protocolName = deviceTestOptions.get(0).getTestProtocol();
			AdaptorRequestMessage message = activityProcessData.getAdaptorRequestMessage();
			orderList.get(0).setProtocolname(protocolName);
			orderList.get(0).setAccessioningId(accessioningId);
			String processingId = rocheWfm.getProcessId();
			long ownerId = rocheWfm.getCompany().getId();
			if ((!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.INPROGRESS.toString()))
					&& (!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.QUERY.toString()))) {
				updateDuplicateStatusforMP24(activityProcessData, accessioningId, orderStatus,orderList,
                    rocheWfm, token, message);
			} else {
				updateActivitiStatusforMP24(activityProcessData, accessioningId, orderStatus, protocolName, rocheWfm,
                    processingId, ownerId);
			}
		}

	}

    private void updateActivitiStatusforMP24(ActivityProcessDataDTO activityProcessData, String accessioningId,
        String orderStatus, String protocolName, SampleWFMStates rocheWfm, String processingId, long ownerId)
        throws QueryValidationException,
        HMTPException,
        OrderNotFoundException,
        IOException {
        logger.info("Save status for process" + processingId);
        Map<String, Object> executionMP24 = new HashMap<>();
        executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
        executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);
        executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUS.toString(), orderStatus);
        executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCAL.toString(), protocolName);
        
        setValuforActivitiGatewayforMP24(orderStatus, executionMP24);
        logger.info(" -> updateMp24Process()::Order status and accessioning id " + orderStatus + " "
        		+ activityProcessData.getAccessioningId());
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
        		.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.MP24U03.toString()).list();
        for (Execution execution : executions) {
        	runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.MP24U03.toString(),
        			execution.getId(), executionMP24);
        }
        if (WfmConstants.WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.ABORTED.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.INPROGRESS.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WfmConstants.WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
        		|| WfmConstants.WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.COMPLETED.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        	List<ProcessStepActionDTO> processSteps = assayIntegrationService
        			.getProcessStepsByAccessioningID(accessioningId);

        	for (ProcessStepActionDTO process : processSteps) {
        		if (process.getDeviceType().equalsIgnoreCase(ASSAY_PROCESS_STEP_DATA.MP24.toString())
        				&& process.getManualVerificationFlag()
        						.equalsIgnoreCase(WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())) {
        			logger.info("Sending for MP24 Manual verification for " + accessioningId);
        			userTaskService.userTaskCheck(accessioningId);
        		} else if (process.getDeviceType().equalsIgnoreCase(ASSAY_PROCESS_STEP_DATA.MP24.toString())
        				&& process.getManualVerificationFlag()
        						.equalsIgnoreCase(WfmConstants.ASSAY_USER_MANUAL_TASK.N.toString())) {
        			logger.info("No MP24 Manual verification required for " + accessioningId);
        			List<Task> ll = taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId()
        					.desc().list();
        			String taskId = ll.get(0).getId();
        			if (!taskId.isEmpty()) {
        				taskService.complete(taskId);
        			}
        		}
        	}
        }
        logger.info(" <- updateMp24Process()::Execution of Mp24 updateProcess End");
    }

    private void updateDuplicateStatusforMP24(ActivityProcessDataDTO activityProcessData, String accessioningId,
        String orderStatus,List<WfmDTO> orderList, SampleWFMStates rocheWfm, String token,
        AdaptorRequestMessage message)
        throws HMTPException,
        OrderNotFoundException,
        IOException {
        if ((!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))
        		&& (!orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))) {
        	String outputContainerId = message.getStatusUpdate().getSampleInfo().getSampleOutputId();
        	String outputPosition = message.getStatusUpdate().getSampleInfo().getSampleOutputPosition();
        	List<WfmDTO> orderdetails = resultIntegrationService.findAccessingIdByContainerId(
        			outputContainerId, outputPosition,
        			WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString());
        	if (orderStatus.equalsIgnoreCase(orderdetails.get(0).getOrderResult())) {
        		resultIntegrationService.updateSampleResults(orderList, message);
        		responseRenderingService.duplicateACKForMP24(accessioningId, message.getDeviceSerialNumber(), activityProcessData);
        	} else if (!orderStatus.equalsIgnoreCase(orderdetails.get(0).getOrderResult())) {
        		responseRenderingService.duplicateNegativeACKForMP24(accessioningId, message.getDeviceSerialNumber(),
        				activityProcessData);
        		AdmNotificationService.sendNotification(NotificationGroupType.MUL_MSG_DIFF_STATUS_MP24,
        				getContent(message.getDeviceSerialNumber(), accessioningId), token,
        				admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
        	}
        } else if ((rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))
        		&& (orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))) {
        	resultIntegrationService.updateSampleResults(orderList, message);
        	responseRenderingService.duplicateACKForMP24(accessioningId, message.getDeviceSerialNumber(), activityProcessData);
        } else {
        	responseRenderingService.duplicateNegativeACKForMP24(accessioningId, message.getDeviceSerialNumber(), activityProcessData);
        	AdmNotificationService.sendNotification(NotificationGroupType.MUL_MSG_DIFF_STATUS_MP24,
        			getContent(message.getDeviceSerialNumber(), accessioningId), token,
        			admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
        }
    }

    private void setValuforActivitiGatewayforMP24(String orderStatus, Map<String, Object> executionMP24)
        throws QueryValidationException {
        if (WfmConstants.WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
            || WfmConstants.WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)) {
            executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.COMPLETED.toString());
            
        } else if (WfmConstants.WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus)) {
            executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.ABORTED.toString());
        } else if (WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
            executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
                WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString());
        } else {
            throw new QueryValidationException();
        }
    }

	public void updateLp24PrePcrProcess(String containerId, String deviceId, String orderStatus, String processStepName,
			ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException, QueryValidationException, IOException {
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		String token = (String) ThreadSessionManager.currentUserSession()
				.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
		SpecimenStatusUpdateMessage message = activityProcessData.getSpecimenStatusUpdateMessage();
		List<DeviceTestOptionsDTO> deviceTestOptions = null;
		String molicularId = null;
		String[] container = containerId.trim().split("_");
		List<WfmDTO> orderdetails = resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString());
		if (orderdetails.isEmpty()) {
			logger.warn("Find AccessingId By ConntainerId : " + orderdetails.size());
		}
		String accessioningId = orderdetails.iterator().next().getAccessioningId();
		activityProcessData.setAccessioningId(accessioningId);
		SampleWFMStates rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId, processStepName,
				domainId);
		if (rocheWfm == null) {
			responseRenderingService.duplicateNegativeACKForLP24(deviceId, activityProcessData);
			AdmNotificationService.sendNotification(NotificationGroupType.UNSUPPORTED_HL7_MSG_LP24,
					getContent(deviceId), token, admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
		} else {
			long ownerId = rocheWfm.getCompany().getId();
			String processingId = rocheWfm.getProcessId();

			List<WfmDTO> orderdetail = orderIntegrationService.findOrder(accessioningId);

			String assayType = orderdetail.iterator().next().getAssayType();
			String sampleType = orderdetail.iterator().next().getSampleType();
			if (!orderStatus.equalsIgnoreCase(WfmConstants.ORDER_STATUS.ABORTED.toString())) {
				List<MolecularIDTypeDTO> molicularDetails = assayIntegrationService
						.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(assayType,
								message.getStatusUpdate().getContainerInfo().getOutputPlateType(),
								message.getStatusUpdate().getSampleInfo().getNewOutputPosition());
				molicularId = molicularDetails.get(0).getMolecularId();
			}
			deviceTestOptions = assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					accessioningId, message.getSendingApplicationName(), message.getProcessStepName());
			String protocolName = deviceTestOptions.get(0).getTestProtocol();
			orderdetail.get(0).setProtocolname(protocolName);
			orderdetail.get(0).setMolecularId(molicularId);
			orderdetail.get(0).setDeviceId(deviceId);
			orderdetail.get(0).setInputContainerId(container[0]);
			orderdetail.get(0).setInputposition(container[1]);
			orderdetail.get(0).setAccessioningId(accessioningId);
			Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
			executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
					activityProcessData);
			executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
					accessioningId);
			executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
			executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(),
					orderStatus);
			executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(), assayType);
			executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(), sampleType);
			executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), protocolName);
			executionLp24prePcrUpdate.put("molicularId", molicularId);

			setValuforActivitiGatewayforLPPrePCR(orderStatus, executionLp24prePcrUpdate);

			if ((!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.INPROGRESS.toString()))
					&& (!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.QUERY.toString()))) {
				updateDuplicateStstusforLPPrePCR(orderStatus, activityProcessData, token, message,rocheWfm, ownerId, orderdetail);
			} else {
				updateActivitiStatusforLPPrePCR(orderStatus, accessioningId, rocheWfm, ownerId, processingId,
                    executionLp24prePcrUpdate);
			}
		}
	}

    private void setValuforActivitiGatewayforLPPrePCR(String orderStatus, Map<String, Object> executionLp24prePcrUpdate)
        throws QueryValidationException {
        setValuforActivitiGatewayforMP24(orderStatus, executionLp24prePcrUpdate);
    }

    private void updateDuplicateStstusforLPPrePCR(String orderStatus,
        ActivityProcessDataDTO activityProcessData, String token, SpecimenStatusUpdateMessage message,SampleWFMStates rocheWfm, long ownerId, List<WfmDTO> orderdetail)
        throws HMTPException,
        OrderNotFoundException,
        IOException {
        if ((!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))
        		&& (!orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))) {
        	String outputContainerId = message.getStatusUpdate().getSampleInfo().getNewContainerId();
        	String outputPosition = message.getStatusUpdate().getSampleInfo().getNewOutputPosition();
        	List<WfmDTO> orderDetails = resultIntegrationService.findAccessingIdByContainerId(
        			outputContainerId, outputPosition, WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString());
        	if (orderStatus.equalsIgnoreCase(orderDetails.get(0).getOrderResult())) {
        		rocheWfm.setCreatedDatetime(new Date());
        		wfmWriteRepository.save(rocheWfm, ownerId);
        		resultIntegrationService.updateforLP24(orderdetail, message);
        		responseRenderingService.duplicateACKForLP24(message.getDeviceSerialNumber(), activityProcessData);
        	} else if (!orderStatus.equalsIgnoreCase(orderDetails.get(0).getOrderResult())) {
        		responseRenderingService.duplicateNegativeACKForLP24(message.getDeviceSerialNumber(), activityProcessData);
        		AdmNotificationService.sendNotification(NotificationGroupType.MUL_MSG_DIFF_STATUS_LP24,
        				getContent(message.getDeviceSerialNumber(), activityProcessData.getAccessioningId()), token,
        				admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
        	}
        } else if ((rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))
        		&& (orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))) {
        	resultIntegrationService.updateforLP24(orderdetail, message);
        	responseRenderingService.duplicateACKForLP24(message.getDeviceSerialNumber(), activityProcessData);
        } else {
        	responseRenderingService.duplicateNegativeACKForLP24(message.getDeviceSerialNumber(), activityProcessData);
        	AdmNotificationService.sendNotification(NotificationGroupType.MUL_MSG_DIFF_STATUS_LP24,
        			getContent(message.getDeviceSerialNumber(), activityProcessData.getAccessioningId()), token,
        			admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
        }
    }

    private void updateActivitiStatusforLPPrePCR(String orderStatus, String accessioningId, SampleWFMStates rocheWfm,
        long ownerId, String processingId, Map<String, Object> executionLp24prePcrUpdate)
        throws HMTPException,
        OrderNotFoundException,
        IOException {
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
        		.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRU03.toString()).list();
        for (Execution execution : executions) {
        	runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRU03.toString(),
        			execution.getId(), executionLp24prePcrUpdate);
        }
        if (WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.ABORTED.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.INPROGRESS.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
        		|| WfmConstants.WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.COMPLETED.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        	if (assayIntegrationService.validateAssayProcessStepManual(accessioningId,
        			ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.PREPCR.toString(),
        			WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())) {
        		userTaskService.userTaskCheck(accessioningId);
        	} else {
        		List<Task> ts = taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId()
        				.desc().list();
        		String taskId = ts.get(0).getId();
        		if (!taskId.isEmpty()) {
        			taskService.complete(taskId);
        		}
        	}
        }
        logger.info(" <- Execution of LP24PrePcr updateProcess End");
    }

	public void updateLp24PostPcrProcess(String containerId, String deviceId, String orderStatus,
			String processStepName, ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException, QueryValidationException, IOException {
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		String token = (String) ThreadSessionManager.currentUserSession()
				.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
		List<DeviceTestOptionsDTO> deviceTestOptions = null;
		SpecimenStatusUpdateMessage message = activityProcessData.getSpecimenStatusUpdateMessage();
		String[] container = containerId.trim().split("_");
		List<WfmDTO> orderdetails = resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString());
		if (orderdetails.isEmpty()) {
			logger.warn("FindAccessingIdBy ConntainerId : " + orderdetails.size());
		}
		String accessioningId = orderdetails.iterator().next().getAccessioningId();
		activityProcessData.setAccessioningId(accessioningId);
		SampleWFMStates rocheWfm = wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(accessioningId,
				deviceId, processStepName, domainId);
		if (rocheWfm == null) {
			responseRenderingService.duplicateNegativeACKForLP24(deviceId, activityProcessData);
			AdmNotificationService.sendNotification(NotificationGroupType.UNSUPPORTED_HL7_MSG_LP24,
					getContent(deviceId), token, admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
		} else {
			deviceTestOptions = assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					accessioningId, message.getSendingApplicationName(), message.getProcessStepName());
			String protocolName = deviceTestOptions.get(0).getTestProtocol();
			orderdetails.get(0).setProtocolname(protocolName);
			orderdetails.get(0).setDeviceId(deviceId);
			orderdetails.get(0).setInputContainerId(container[0]);
			orderdetails.get(0).setInputposition(container[1]);
			orderdetails.get(0).setAccessioningId(accessioningId);
			Map<String, Object> executionLp24PostPcrUpdate = new HashMap<>();
			executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPOSTPCRUPDATE.toString(),
					accessioningId);
			executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
					activityProcessData);
			executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPOSTPCRUPDATE.toString(), deviceId);
			executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPOSTPCRUPDATE.toString(),
					orderStatus);
			executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLLP24POSTPCR.toString(),
					protocolName);

			long ownerId = rocheWfm.getCompany().getId();

			setValuforActivitiGatewayforLPPostPCR(orderStatus, executionLp24PostPcrUpdate);
			String processingId = rocheWfm.getProcessId();

			if ((!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.INPROGRESS.toString()))
					&& (!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.QUERY.toString()))) {
				updateDuplicateStatusforLPPostPCR(orderStatus, activityProcessData, token, message,
                    orderdetails,rocheWfm, ownerId);
			} else

			{
				updateActivitiStstusforLPPostPCR(orderStatus, accessioningId, rocheWfm, executionLp24PostPcrUpdate,
                    ownerId, processingId);
			}
		}
	}

    private void setValuforActivitiGatewayforLPPostPCR(String orderStatus,
        Map<String, Object> executionLp24PostPcrUpdate) throws QueryValidationException {
        if (WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
        		|| WfmConstants.WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)) {
        	executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
        			WORKFLOW_STATUS.COMPLETED.toString());

        } else if (WfmConstants.WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus)) {
        	executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
        			WfmConstants.WORKFLOW_STATUS.ABORTED.toString());
        } else if (WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
        	executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
        			WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString());
        } else {
        	throw new QueryValidationException();
        }
    }

    private void updateActivitiStstusforLPPostPCR(String orderStatus, String accessioningId, SampleWFMStates rocheWfm,
        Map<String, Object> executionLp24PostPcrUpdate, long ownerId, String processingId)
        throws HMTPException,
        OrderNotFoundException,
        IOException {
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
        		.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRU03.toString()).list();
        for (Execution execution : executions) {
        	runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRU03.toString(),
        			execution.getId(), executionLp24PostPcrUpdate);
        }
        if (WfmConstants.WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.ABORTED.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.INPROGRESS.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
        		|| WfmConstants.WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.COMPLETED.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);

        	if (assayIntegrationService.validateAssayProcessStepManual(accessioningId,
        			ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.POSTPCR.toString(),
        			WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())) {
        		userTaskService.userTaskCheck(accessioningId);
        	} else {
        		List<Task> ts = taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId()
        				.desc().list();
        		String taskId = ts.get(0).getId();
        		if (!taskId.isEmpty()) {
        			taskService.complete(taskId);
        		}
        	}

        }
        logger.info(" Execution of Lp24 Post Pcr updateProcess End");
    }

    private void updateDuplicateStatusforLPPostPCR(String orderStatus,
        ActivityProcessDataDTO activityProcessData, String token, SpecimenStatusUpdateMessage message,
        List<WfmDTO> orderdetails,SampleWFMStates rocheWfm, long ownerId)
        throws HMTPException,
        OrderNotFoundException,
        IOException {
        if ((!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))
        		&& (!orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))) {
        	String outputContainerId = message.getStatusUpdate().getSampleInfo().getNewContainerId();
        	String outputPosition = message.getStatusUpdate().getSampleInfo().getNewOutputPosition();
        	List<WfmDTO> orderDetails = resultIntegrationService.findAccessingIdByContainerId(
        			outputContainerId, outputPosition, WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString());
        	if (orderStatus.equalsIgnoreCase(orderDetails.get(0).getOrderResult())) {
        		rocheWfm.setCreatedDatetime(new Date());
        		wfmWriteRepository.save(rocheWfm, ownerId);
        		resultIntegrationService.updateforLP24(orderdetails, message);
        		responseRenderingService.duplicateACKForLP24(message.getDeviceSerialNumber(), activityProcessData);
        	} else if (!orderStatus.equalsIgnoreCase(orderDetails.get(0).getOrderResult())) {
        		responseRenderingService.duplicateNegativeACKForLP24(message.getDeviceSerialNumber(), activityProcessData);
        		AdmNotificationService.sendNotification(NotificationGroupType.MUL_MSG_DIFF_STATUS_LP24,
        				getContent(message.getDeviceSerialNumber(), activityProcessData.getAccessioningId()), token,
        				admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
        	}
        } else if ((rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))
        		&& (orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString()))) {
        	resultIntegrationService.updateforLP24(orderdetails, message);
        	responseRenderingService.duplicateACKForLP24(message.getDeviceSerialNumber(), activityProcessData);
        } else {
        	responseRenderingService.duplicateNegativeACKForLP24(message.getDeviceSerialNumber(), activityProcessData);
        	AdmNotificationService.sendNotification(NotificationGroupType.MUL_MSG_DIFF_STATUS_LP24,
        			getContent(message.getDeviceSerialNumber(), activityProcessData.getAccessioningId()), token,
        			admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
        }
    }

	public void updateLp24SeqPrepProcess(String accessioningId, String containerId, String deviceId, String orderStatus,
			String processStepName, ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException {
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		String[] container = containerId.trim().split("_");
		try {
			SampleWFMStates rocheWfm = wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(accessioningId,
					deviceId, processStepName, domainId);
			long ownerId = rocheWfm.getCompany().getId();
			String processingId = rocheWfm.getProcessId();
			if ((!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.INPROGRESS.toString()))
					&& (!rocheWfm.getCurrentStatus().equalsIgnoreCase(WORKFLOW_STATUS.QUERY.toString()))) {
				if (orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.PASSED.toString())
						|| orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.FLAGGED.toString())) {
					orderStatus = WORKFLOW_STATUS.COMPLETED.toString();
				}
				if (orderStatus.equalsIgnoreCase(rocheWfm.getCurrentStatus())) {
					rocheWfm.setCreatedDatetime(new Date());
					wfmWriteRepository.save(rocheWfm, ownerId);
					logger.info(" Execution of Lp24 SeqPrep updateProcess End");
				}
			} else {
				updateActivitiStatusforLPSeqPrep(accessioningId,orderStatus, activityProcessData, container,
                    rocheWfm, ownerId, processingId);
			}
		} catch (Exception ex) {
			logger.info("Sending for Manual verification" + ex);
		}

		logger.info(" Execution of Lp24 SeqPrep updateProcess End");

	}

    private void updateActivitiStatusforLPSeqPrep(String accessioningId,String orderStatus,
        ActivityProcessDataDTO activityProcessData, String[] container, SampleWFMStates rocheWfm, long ownerId,
        String processingId) throws QueryValidationException, HMTPException, OrderNotFoundException, IOException {
        Map<String, Object> executionLp24SeqPreUpdate = new HashMap<>();
        executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.CONTAINERIDSEQPREUPDATE.toString(),
        		container[0]);
        executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.CONTAINERPOSITIONSEQPREUPDATE.toString(),
        		container[1]);
        executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDSEQPREUPDATE.toString(),
        		accessioningId);
        executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
        		activityProcessData);
        executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDSEQPREUPDATE.toString(),
        		activityProcessData.getSpecimenStatusUpdateMessage().getDeviceSerialNumber());
        executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSSEQPREUPDATE.toString(),
        		orderStatus);
        setValuforActivitiGatewayforLPPostPCR(orderStatus, executionLp24SeqPreUpdate);
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
        		.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString()).list();
        for (Execution execution : executions) {
        	runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString(),
        			execution.getId(), executionLp24SeqPreUpdate);
        }
        if (WfmConstants.WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.ABORTED.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WfmConstants.WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.INPROGRESS.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        }
        if (WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
        		|| WfmConstants.WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)) {
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.COMPLETED.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);

        	if (assayIntegrationService.validateAssayProcessStepManual(accessioningId,
        			ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.SEQPREP.toString(),
        			WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())) {
        		userTaskService.userTaskCheck(accessioningId);
        	} else {
        		List<Task> ts = taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId()
        				.desc().list();
        		String taskId = ts.get(0).getId();
        		if (!taskId.isEmpty()) {
        			taskService.complete(taskId);
        		}
        	}
        }
    }

	public void updateHTPProcess(String accessioningId,Long runResultsId, Long orderId,String status,String deviceRunId,ActivityProcessDataDTO activityProcessData) {
		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		SampleWFMStates rocheWfm = null;
		if (status.equalsIgnoreCase(WfmConstants.RUN_STATUS.STARTED.toString())) {
			rocheWfm = wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.SEQPREP.toString(), domainId);
		} else {
			rocheWfm = wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(accessioningId, activityProcessData.getHtpStatusMessage().getDeviceId(),
					activityProcessData.getHtpStatusMessage().getSendingApplication(), domainId);

		}
		String processingId = rocheWfm.getProcessId();
		long ownerId = rocheWfm.getCompany().getId();
		if (!rocheWfm.getCurrentStatus().equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.ABORTED.toString())) {

			Map<String, Object> executionHTPUpdate = new HashMap<>();
			executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDHTPUPDATE.toString(), accessioningId);
			executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
			executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDHTPUPDATE.toString(), activityProcessData.getHtpStatusMessage().getDeviceId());
			executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSHTPUPDATE.toString(), status);
			executionHTPUpdate.put("runResultsId", runResultsId);
			executionHTPUpdate.put("orderId", orderId);
			executionHTPUpdate.put("deviceRunId", deviceRunId);
			executionHTPUpdate.put("inputContainerId", activityProcessData.getHtpStatusMessage().getInputContainerId());
			executionHTPUpdate.put("outputContainerId", activityProcessData.getHtpStatusMessage().getOutputContainerId());
			executionHTPUpdate.put("processStepName", activityProcessData.getHtpStatusMessage().getProcessStepName());

			if (WORKFLOW_STATUS.COMPLETED.toString().equalsIgnoreCase(status)) {
				executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
						WORKFLOW_STATUS.COMPLETED.toString());

			} else if (WfmConstants.WORKFLOW_STATUS.FAILED.toString().equalsIgnoreCase(status)) {
				executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.INPUT.toString(),
						WfmConstants.WORKFLOW_STATUS.FAILED.toString());
			}
			updateActivitiStatusforHTP(accessioningId, status, activityProcessData, rocheWfm, processingId, ownerId,
                executionHTPUpdate);
		} else {
			logger.info("Sample Already Aborted for AccessioningId: " + accessioningId);
		}
	}

    private void updateActivitiStatusforHTP(String accessioningId, String status,
        ActivityProcessDataDTO activityProcessData, SampleWFMStates rocheWfm, String processingId, long ownerId,
        Map<String, Object> executionHTPUpdate) {
        if ((status).equalsIgnoreCase(WfmConstants.RUN_STATUS.STARTED.toString())) {
        	List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
        			.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPSTARTED.toString()).list();
        	for (Execution execution : executions) {
        		runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.HTPSTARTED.toString(),
        				execution.getId(), executionHTPUpdate);
        	}
        	wfmstate(accessioningId, activityProcessData.getHtpStatusMessage().getDeviceId(), WORKFLOW_STATUS.INPROCESS.toString(), processingId,
        	    activityProcessData.getHtpStatusMessage().getSendingApplication());
        	logger.info(" Execution of HTP Started updateProcess End");
        } else if ((status).equalsIgnoreCase(WfmConstants.RUN_STATUS.INPROCESS.toString())) {
        	List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
        			.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPINPROCESS.toString()).list();
        	for (Execution execution : executions) {
        		runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.HTPINPROCESS.toString(),
        				execution.getId(), executionHTPUpdate);
        	}
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.INPROCESS.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        	logger.info(" Execution of HTP Inprogress updateProcess End");

        } else if (((status).equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.COMPLETED.toString()))
        		|| ((status).equalsIgnoreCase(WfmConstants.RUN_STATUS.FAILED.toString()))) {
        	updateActivitiStatusforHTP(status, rocheWfm, processingId, ownerId, executionHTPUpdate);
        } else if ((status).equalsIgnoreCase(WORKFLOW_STATUS.TRANSFERCOMPLETED.toString())) {
        	List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
        			.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPTRANSFERCOMPLETE.toString())
        			.list();
        	for (Execution execution : executions) {
        		runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.HTPTRANSFERCOMPLETE.toString(),
        				execution.getId(), executionHTPUpdate);
        	}
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.COMPLETED.toString());
        	wfmWriteRepository.save(rocheWfm, ownerId);
        	logger.info(" Execution of HTP Transfer Completed updateProcess End");
        }
    }

    private void updateActivitiStatusforHTP(String status, SampleWFMStates rocheWfm, String processingId, long ownerId,
        Map<String, Object> executionHTPUpdate) {
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processingId)
        		.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPCOMPLETED.toString()).list();
        for (Execution execution : executions) {
        	runtimeService.signalEventReceived(WfmConstants.WORKFLOW_SIGNALS.HTPCOMPLETED.toString(),
        			execution.getId(), executionHTPUpdate);
        }
        if ((status).equalsIgnoreCase(WfmConstants.WORKFLOW_STATUS.COMPLETED.toString()))
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.INPROCESS.toString());
        if ((status).equalsIgnoreCase(WfmConstants.RUN_STATUS.FAILED.toString()))
        	rocheWfm.setCurrentStatus(WORKFLOW_STATUS.FAILED.toString());
        wfmWriteRepository.save(rocheWfm, ownerId);
        logger.info("HTP Process is already completed");
    }

	private List<Execution> processForteJobType(String processingId, String signal) {
		logger.info("WfmService::=> processForteJobType()");
		this.signal = signal;
		return runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(this.signal).list();
	}

	public Boolean isValidState(SampleWFMStates sampleWFMState, String orderStatus) throws HMTPException {

		Boolean result = false;

		if (WORKFLOW_STATUS.QUERY.toString().equalsIgnoreCase(sampleWFMState.getCurrentStatus())) {
			result = WORKFLOW_STATUS.QUERY.toString().equalsIgnoreCase(orderStatus)
					|| WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)
					|| WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
					|| WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)
					|| WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus);
		} else if (WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(sampleWFMState.getCurrentStatus())) {
			result = WORKFLOW_STATUS.INPROGRESS.toString().equalsIgnoreCase(orderStatus)
					|| WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
					|| WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)
					|| WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus);
		} else if (WORKFLOW_STATUS.COMPLETED.toString().equalsIgnoreCase(sampleWFMState.getCurrentStatus())) {
			result = WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
					|| WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus);
		} else if (WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(sampleWFMState.getCurrentStatus())) {
			result = WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus);
		} else {
			throw new HMTPException("Invalid State Exception");
		}

		return result;
	}

	private void processLPSeqMultipleStateUpdate(String accessioningId, SampleWFMStates sampleWFMState,
			String orderStatus, ActivityProcessDataDTO activityProcessData, String[] container) {

		SpecimenStatusUpdateMessage message = activityProcessData.getSpecimenStatusUpdateMessage();

		try {
			List<DeviceTestOptionsDTO> deviceTestOptions = Optional
					.ofNullable(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
							accessioningId, message.getSendingApplicationName(), message.getProcessStepName()))
					.orElseThrow(() -> new HMTPException("Device Test option are null"));

			String finalProtocolName = deviceTestOptions.get(0).getTestProtocol();

			updateSampleResultMultipleSSU(accessioningId,activityProcessData, container, message,
					finalProtocolName, orderStatus, sampleWFMState);
		} catch (HMTPException  | IOException e) {
			logger.error("Exception on processLPSeqMultipleStateUpdate" + e.getMessage());
		}
	}

	private void updateSampleResultMultipleSSU(String accessioningId,ActivityProcessDataDTO activityProcessData, String[] container, SpecimenStatusUpdateMessage message,
			String finalProtocolName, String orderStatus, SampleWFMStates sampleWFMState) {

		Boolean isValid = false;

		if ((WORKFLOW_STATUS.COMPLETED.toString().equalsIgnoreCase(sampleWFMState.getCurrentStatus())
				&& (WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)
						|| WORKFLOW_STATUS.FLAGGED.toString().equalsIgnoreCase(orderStatus)))
				|| (WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(sampleWFMState.getCurrentStatus())
						&& WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(orderStatus))) {
			isValid = true;
		}

		if (isValid) {
			logger.info("Valid Order status and Sample WFM status SampleWFM status: "
					+ sampleWFMState.getCurrentStatus() + ", Order status: " + orderStatus);

			List<WfmDTO> orderdetails;

			try {

				orderdetails = resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
						WfmConstants.ASSAY_PROCESS_STEP_DATA.SEQUENCING.toString());

				orderdetails.stream().forEach(orderDetail -> {
					orderDetail.setAccessioningId(accessioningId);
					orderDetail.setDeviceId(activityProcessData.getSpecimenStatusUpdateMessage().getDeviceSerialNumber());
					orderDetail.setInputContainerId(container[0]);
					orderDetail.setInputposition(container[1]);
					orderDetail.setSendingApplicationName(
							activityProcessData.getSpecimenStatusUpdateMessage().getSendingApplicationName());
					orderDetail.setProtocolname(finalProtocolName);
					orderDetail.setRunid(message.getRunResultsId());

					if (WORKFLOW_STATUS.PASSED.toString().equalsIgnoreCase(orderStatus)) {
						orderDetail.setOrderStatus(WORKFLOW_STATUS.COMPLETED.toString());
					} else if (orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.ABORTED.toString())) {
						orderDetail.setOrderStatus(WORKFLOW_STATUS.ABORTED.toString());
					} else if (orderStatus.equalsIgnoreCase(WORKFLOW_STATUS.INPROGRESS.toString())) {
						orderDetail.setOrderStatus(WORKFLOW_STATUS.INPROGRESS.toString());
					}
				});

				resultIntegrationService.updateforLP24(orderdetails, message);

				sampleWFMState.setCreatedDatetime(Date.from(Instant.now()));
				wfmWriteRepository.save(sampleWFMState, sampleWFMState.getCompany().getId());

			} catch (HMTPException | OrderNotFoundException | IOException ex) {
				logger.error("Exception while updating Multiple SSU Sample restults" + ex.getMessage());
			}
		} else {
			logger.info("Mismatch Order status and Sample WFM status SampleWFM status: "
					+ sampleWFMState.getCurrentStatus() + ", Order status: " + orderStatus);
			String token = (String) ThreadSessionManager.currentUserSession()
					.getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString());
			try {
				AdmNotificationService.sendNotification(NotificationGroupType.UNSUPPORTED_HL7_MSG_LP24,
						getContent(activityProcessData.getSpecimenStatusUpdateMessage().getDeviceSerialNumber()), token, admhosturl + WfmURLConstants.ADM_NOTIFICATION_URL);
				responseRenderingService.duplicateNegativeACKForLP24(activityProcessData.getSpecimenStatusUpdateMessage().getDeviceSerialNumber(), activityProcessData);
			} catch (HMTPException e) {
				logger.info("Exception on updateSampleResultMultipleSSU Negative ACK");
			}
		}
	}

	public void updateLP24SeqPP(String accessioningId, String containerId, String deviceId, String orderStatus,
			String processStepName, ActivityProcessDataDTO activityProcessData)
			throws HMTPException, OrderNotFoundException, QueryValidationException, IOException {

		long domainId = Optional.ofNullable(ThreadSessionManager.currentUserSession().getAccessorCompanyId())
				.orElseThrow(() -> new HMTPException("Domain id not Exist in updateLP24SeqPP"));

		SampleWFMStates sampleWFMState = Optional
				.ofNullable(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(accessioningId, deviceId,
						processStepName, domainId))
				.orElseThrow(() -> new HMTPException(accessioningId + " id not exist in Sample WFM states"));

		if (isValidState(sampleWFMState, orderStatus)) {

			String[] container = containerId.trim().split("_");

			Predicate<String> p = x -> WORKFLOW_STATUS.COMPLETED.toString().equalsIgnoreCase(x)
					|| WORKFLOW_STATUS.ABORTED.toString().equalsIgnoreCase(x);

			if (p.test(sampleWFMState.getCurrentStatus())) {
				processLPSeqMultipleStateUpdate(accessioningId, sampleWFMState, orderStatus,
						activityProcessData, container);
			} else {
				long ownerId = Optional.ofNullable(sampleWFMState.getCompany().getId())
						.orElseThrow(() -> new HMTPException("rocheWfm.getCompany().getId() missing"));

				String processingId = Optional.ofNullable(sampleWFMState.getProcessId())
						.orElseThrow(() -> new HMTPException("Processing id is missing"));

				updateActivitiStatusforLPSeqPrep(accessioningId,orderStatus, activityProcessData, container,
                    sampleWFMState, ownerId, processingId);
			}
		}
	}

	public List<String> getContent(String deviceId) {
		List<String> content = new ArrayList<>();
		content.add(deviceId);
		return content;
	}

	public List<String> getContent(String deviceId, String accessioningId) {
		List<String> content = new ArrayList<>();
		content.add(deviceId);
		content.add(accessioningId);
		return content;
	}
}