package com.roche.connect.wfm.test.testng;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Invocation;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.amm.dto.MolecularIDTypeDTO;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.lp24.ContainerInfo;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.SampleInfo;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.ASSAY_PROCESS_STEP_DATA;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.error.QueryValidationException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.AssayTypeDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.ProcessStepActionDTO;
import com.roche.connect.wfm.model.SampleWFMStates;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.repository.WfmReadRepository;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;
import com.roche.connect.wfm.service.UserTaskService;
import com.roche.connect.wfm.service.WFMHTPService;
import com.roche.connect.wfm.writerepository.WfmWriteRepository;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class,ThreadSessionManager.class,AdmNotificationService.class  })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class WFMServiceTest {

	@InjectMocks
	private WFMHTPService wfmService;

	@Mock
	Invocation.Builder assayClient;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	OrderIntegrationService orderIntegrationService;
	@Mock
	AssayIntegrationService assayIntegrationService;
	@Mock
	ResultIntegrationService resultIntegrationService;
	@Mock
	RuntimeService runtimeService;
	@Mock
	ProcessInstance processInstance;
	@Mock
	WfmWriteRepository wfmWriteRepository;
	@Mock
	WfmReadRepository wfmRepository;
	@Mock
	ResponseRenderingService responseRenderingService;
	@Mock
	ExecutionQuery executionQuery;
	@Mock
	UserTaskService userTaskService;
	@Mock
	TaskService taskService;
	@Mock
	Execution exec1;
	@Mock
	Company company;
	@Mock SampleWFMStates SampleWFMStates;

	@Mock
	UserSession userSession;
	
	@Mock TaskQuery taskQuery;
	@Mock List<Task> ts;
    @Mock Task task;
    
	List<Task> tasklist = new ArrayList<>();
	List<Execution> executions = new ArrayList<>();
	String accessioningId = null;
	ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
	String url = null;
	String assayType = null;
	List<AssayTypeDTO> listAssays = new ArrayList<>();
	List<AssayTypeDTO> listAssaysSec = new ArrayList<>();
	AssayTypeDTO assayTypeDTO = new AssayTypeDTO();
	AssayTypeDTO assayTypeDTOSecond = new AssayTypeDTO();
	Map<String, Object> variables = new HashMap<>();
	String containerId = null;
	String sendingApplicationName = null;
	String processStepName = null;
	String processStep = null;
	String processingId = null;
	String deviceId = null;
	String orderStatus = null;
	SampleWFMStates rocheWfm = null;
	List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();

	@BeforeTest
	public void setUp() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
		processingId = "15";
		accessioningId = "100100";
		url = "/json/rest/api/v1/assay/";
		assayType = "NIPTDPCR";
		assayTypeDTO.setAssayType("NIPTHTP");
		assayTypeDTO.setWorkflowDefFile("NIPTHTP");
		assayTypeDTOSecond.setAssayType("NIPTDPCR");
		assayTypeDTOSecond.setWorkflowDefFile("NIPTDPCR");
		listAssaysSec.add(assayTypeDTOSecond);
		listAssays.add(assayTypeDTO);
		containerId = "ABC_A1";
		sendingApplicationName = "ABCD";
		processStepName = "Lp24PrePcr";
		deviceId = "DANC";
		processStep = "Lp24PreP";
		executions.add(exec1);
		rocheWfm = getrocheWfm();
		deviceTestOptions.add(getDeviceTestOptionsDTO());
		ts.add(task);
	}

	//** Mandatory to mock static classes *
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	public void config() {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
	}

	@Test
	public void startProcess() throws Exception {
		config();
		List<WfmDTO> wfmlistsstartProcess = new ArrayList<WfmDTO>();
		wfmlistsstartProcess.add(getWfmDTO());
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistsstartProcess);

		variables.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), getActivityProcessDataDTO());
		variables.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);

		Mockito.when(runtimeService.startProcessInstanceByKey(ASSAY_PROCESS_STEP_DATA.DPCR_WORKFLOW_FILE.toString(),
				accessioningId, variables)).thenReturn(processInstance);
		Mockito.when(processInstance.getId()).thenReturn("1234");
		Mockito.when(assayIntegrationService.findAssayDetail(wfmlistsstartProcess.get(0).getAssayType()))
				.thenReturn(listAssays);

		SampleWFMStates sampleWFM = new SampleWFMStates();
		sampleWFM.setAccessioningId(accessioningId);
		Mockito.when(wfmWriteRepository.save(sampleWFM)).thenReturn(sampleWFM);
		wfmService.startProcess(accessioningId, getActivityProcessDataDTO());
	}

	@Test
	public void startProcesselse() throws Exception {
		config();
		List<WfmDTO> wfmlistsstartProcess = new ArrayList<WfmDTO>();
		wfmlistsstartProcess.add(getWfmDTOSecond());
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistsstartProcess);

		variables.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), getActivityProcessDataDTO());
		variables.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);

		Mockito.when(runtimeService.startProcessInstanceByKey(ASSAY_PROCESS_STEP_DATA.DPCR_WORKFLOW_FILE.toString(),
				accessioningId, variables)).thenReturn(processInstance);
		Mockito.when(processInstance.getId()).thenReturn("1234");
		Mockito.when(assayIntegrationService.findAssayDetail(wfmlistsstartProcess.get(0).getAssayType()))
				.thenReturn(listAssaysSec);

		SampleWFMStates sampleWFM = new SampleWFMStates();
		sampleWFM.setAccessioningId(accessioningId);
		Mockito.when(wfmWriteRepository.save(sampleWFM)).thenReturn(sampleWFM);
		wfmService.startProcess(accessioningId, getActivityProcessDataDTO());
	}

	@Test
	public void startMp24Process() throws Exception {
		config();
		List<WfmDTO> wfmlistsstartProcess = new ArrayList<WfmDTO>();
		wfmlistsstartProcess.add(getWfmDTO());
		Mockito.when(assayIntegrationService.validateAssayProcessStep(accessioningId, ASSAY_PROCESS_STEP_DATA.MP24.toString(),
				ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()))
				.thenReturn(true);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistsstartProcess);

		variables.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), getActivityProcessDataDTO());
		variables.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);

		Mockito.when(runtimeService.startProcessInstanceByKey(ASSAY_PROCESS_STEP_DATA.DPCR_WORKFLOW_FILE.toString(),
				accessioningId, variables)).thenReturn(processInstance);
		Mockito.when(processInstance.getId()).thenReturn("1234");
		Mockito.when(assayIntegrationService.findAssayDetail(wfmlistsstartProcess.get(0).getAssayType()))
				.thenReturn(listAssays);

		SampleWFMStates sampleWFM = new SampleWFMStates();
		sampleWFM.setAccessioningId(accessioningId);
		Mockito.when(wfmWriteRepository.save(sampleWFM)).thenReturn(sampleWFM);
		wfmService.startMp24Process(accessioningId, getActivityProcessDataDTO());
	}
	
	
	@Test
	public void startMp24ProcessElseException() throws HMTPException, OrderNotFoundException, IOException {
		config();
		List<WfmDTO> wfmlistsstartProcess = new ArrayList<WfmDTO>();
		wfmlistsstartProcess.add(getWfmDTO());
		Mockito.when(assayIntegrationService.validateAssayProcessStep("100100", ASSAY_PROCESS_STEP_DATA.MP24.toString(),
				ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()))
				.thenReturn(false);
		try {
		wfmService.startMp24Process("100100", getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}

	@Test
	public void startLp24PrePcrProcess() throws Exception {
		config();
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());
		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);

		Mockito.when(assayIntegrationService.validateAssayProcessStep(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), sendingApplicationName, processStepName))
				.thenReturn(true);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();

		/*Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(),
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(rocheWfm);*/
		
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		SampleWFMStates rocheWfmsecond = getrocheWfmSecond();
		/*Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), processStepName))
				.thenReturn(rocheWfmsecond);*/

		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfmsecond);
		Mockito.doNothing().when(responseRenderingService).duplicateOrderQueryPositiveResponseforLp24(containerId,
				deviceId, wfmlistPrePcrProcess.iterator().next().getAccessioningId(), getActivityProcessDataDTO());

		Map<String, Object> executionLp24PrePcr = new HashMap<>();
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCR.toString(),
				wfmlistPrePcrProcess.iterator().next().getAccessioningId());
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDPREPCR.toString(), container[0]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONPREPCR.toString(), container[1]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPREPCRUPDATE.toString(),
				wfmlistPrePcrProcess);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRQBP.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRQBP.toString()).list())
				.thenReturn(executions);

		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		wfmService.startLp24PrePcrProcess(containerId, deviceId, processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void startLp24PrePcrProcessElse() throws Exception {
		config();
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);

		Mockito.when(assayIntegrationService.validateAssayProcessStep(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), sendingApplicationName, "Lp24PrePcr"))
				.thenReturn(true);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = new SampleWFMStates();
		 rocheWfm = getrocheWfm();
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(),
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString(), -1)).thenReturn(rocheWfm);
		/*Mockito.doNothing().when(responseRenderingService).duplicateOrderQueryPositiveResponseforLp24(containerId,
				deviceId, wfmlistPrePcrProcess.iterator().next().getAccessioningId(), getActivityProcessDataDTO());*/
		SampleWFMStates rocheWfmsecond =null;
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(getActivityProcessDataDTO().getAccessioningId(), "Lp24PrePcr", -1)).thenReturn(rocheWfmsecond);

		Map<String, Object> executionLp24PrePcr = new HashMap<>();
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCR.toString(),
				wfmlistPrePcrProcess.iterator().next().getAccessioningId());
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDPREPCR.toString(), container[0]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONPREPCR.toString(), container[1]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPREPCRUPDATE.toString(),
				wfmlistPrePcrProcess);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRQBP.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRQBP.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		try {
		wfmService.startLp24PrePcrProcess(containerId, deviceId,"Lp24PrePcr",
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}

	@Test
	public void startLp24PostPcrProcess() throws Exception {
		config();
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());
		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		Mockito.when(assayIntegrationService.validateAssayProcessStep(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), sendingApplicationName, processStepName))
				.thenReturn(true);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();
		/*Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(),
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(rocheWfm);*/
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		
		SampleWFMStates rocheWfmsecond = getrocheWfmSecond();
		/*Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), processStepName))
				.thenReturn(rocheWfmsecond);*/
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfmsecond);

		Mockito.doNothing().when(responseRenderingService).duplicateOrderQueryPositiveResponseforLp24(containerId,
				deviceId, wfmlistPrePcrProcess.iterator().next().getAccessioningId(), getActivityProcessDataDTO());

		Map<String, Object> executionLp24PrePcr = new HashMap<>();
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCR.toString(),
				wfmlistPrePcrProcess.iterator().next().getAccessioningId());
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDPREPCR.toString(), container[0]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONPREPCR.toString(), container[1]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPREPCRUPDATE.toString(),
				wfmlistPrePcrProcess);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRQBP.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRQBP.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		wfmService.startLp24PostPcrProcess(containerId, deviceId,processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void startLp24PostPcrProcessElse() throws Exception {
		config();
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");
		

		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		Mockito.when(assayIntegrationService.validateAssayProcessStep(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), sendingApplicationName, processStepName))
				.thenReturn(true);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm =getrocheWfm();
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(),
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString(), -1)).thenReturn(rocheWfm);
		SampleWFMStates rocheWfmSecond =null;
		
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(getActivityProcessDataDTO().getAccessioningId(),
     			processStepName, -1)).thenReturn(rocheWfmSecond);
	/*	Mockito.doNothing().when(responseRenderingService).duplicateOrderQueryPositiveResponseforLp24(containerId,
				deviceId, wfmlistPrePcrProcess.iterator().next().getAccessioningId(), getActivityProcessDataDTO());*/

		Map<String, Object> executionLp24PrePcr = new HashMap<>();
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCR.toString(),
				wfmlistPrePcrProcess.iterator().next().getAccessioningId());
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDPREPCR.toString(), container[0]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONPREPCR.toString(), container[1]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPREPCRUPDATE.toString(),
				wfmlistPrePcrProcess);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRQBP.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRQBP.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		try {
		wfmService.startLp24PostPcrProcess(containerId, deviceId,processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}

	@Test
	public void startLp24SeqPrepProcess() throws Exception {
		config();
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());
		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");
		
		  Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
		  container[0], container[1],
		  WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())).thenReturn(
		  wfmlistPrePcrProcess);
		 

		Mockito.when(assayIntegrationService.validateAssayProcessStep(accessioningId,
				ASSAY_PROCESS_STEP_DATA.LP24.toString(), processStepName)).thenReturn(true);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();

		/*Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
				WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())).thenReturn(rocheWfm);*/
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		SampleWFMStates rocheWfmsecond = getrocheWfmSecond();
		/*Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId, processStepName))
				.thenReturn(rocheWfmsecond);*/
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfmsecond);

		Mockito.doNothing().when(responseRenderingService).duplicateOrderQueryPositiveResponseforLp24(containerId,
				deviceId, wfmlistPrePcrProcess.iterator().next().getAccessioningId(), getActivityProcessDataDTO());

		Map<String, Object> executionLp24PrePcr = new HashMap<>();
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCR.toString(), accessioningId);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDPREPCR.toString(), container[0]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONPREPCR.toString(), container[1]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPREPCRUPDATE.toString(),
				wfmlistPrePcrProcess);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPQBP.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPQBP.toString()).list())
				.thenReturn(executions);

		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		wfmService.startLp24SeqPrepProcess(containerId, deviceId, accessioningId, processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void startLp24SeqPrepProcessElse() throws Exception {
		config();
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");
		
		  Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
		  container[0], container[1],
		  WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())).thenReturn(
		  wfmlistPrePcrProcess);
		 

		Mockito.when(assayIntegrationService.validateAssayProcessStep(accessioningId,
				ASSAY_PROCESS_STEP_DATA.LP24.toString(), processStepName)).thenReturn(true);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();

		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType("100100", "LP Post PCR/Pooling",-1L)).thenReturn(rocheWfm);
		SampleWFMStates rocheWfmsecond = null;
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType("100100", "Lp24PrePcr",
				-1L)).thenReturn(rocheWfmsecond);
	
		
		Mockito.doNothing().when(responseRenderingService).duplicateOrderQueryPositiveResponseforLp24(containerId,
				deviceId, wfmlistPrePcrProcess.iterator().next().getAccessioningId(), getActivityProcessDataDTO());

		Map<String, Object> executionLp24PrePcr = new HashMap<>();
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCR.toString(), accessioningId);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDPREPCR.toString(), container[0]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONPREPCR.toString(), container[1]);
		executionLp24PrePcr.put(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPREPCRUPDATE.toString(),
				wfmlistPrePcrProcess);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPQBP.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPQBP.toString()).list())
				.thenReturn(executions);

		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		wfmService.startLp24SeqPrepProcess(containerId, deviceId, accessioningId, processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void updateMp24Process() throws Exception {
		config();
		List<WfmDTO> wfmlistMp24Process = new ArrayList<WfmDTO>();
		orderStatus = "Passed";
		wfmlistMp24Process.add(getWfmDTO());
		List<ProcessStepActionDTO> processstepdtolist = new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
		processStepActionDTO.setDeviceType("MP24");
		processStepActionDTO.setManualVerificationFlag("Y");
		processstepdtolist.add(processStepActionDTO);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistMp24Process);
		SampleWFMStates rocheWfm = getrocheWfm();

		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(Mockito.anyString(), Mockito.anyString(),
           Mockito.anyLong())).thenReturn(rocheWfm);
		
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		Map<String, Object> executionMP24 = new HashMap<>();
		executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);
		executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUS.toString(), orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.MP24U03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.MP24U03.toString()).list())
				.thenReturn(executions);

		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.getProcessStepsByAccessioningID(accessioningId))
				.thenReturn(processstepdtolist);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		  Mockito.when(assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(wfmlistMp24Process,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
					WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString())).thenReturn(deviceTestOptions);
		wfmService.updateMp24Process(getActivityProcessDataDTO(), accessioningId, orderStatus, deviceId);
	}
	
	@Test
	public void updateMp24ProcessAborted() throws Exception {
		config();
		List<WfmDTO> wfmlistMp24Process = new ArrayList<WfmDTO>();
		orderStatus = "Aborted";
		wfmlistMp24Process.add(getWfmDTO());
		List<ProcessStepActionDTO> processstepdtolist = new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
		processStepActionDTO.setDeviceType("MP24");
		processStepActionDTO.setManualVerificationFlag("Y");
		processstepdtolist.add(processStepActionDTO);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistMp24Process);
		SampleWFMStates rocheWfm = getrocheWfm();

		
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyLong())).thenReturn(rocheWfm);
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		Map<String, Object> executionMP24 = new HashMap<>();
		executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);
		executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUS.toString(), orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.MP24U03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.MP24U03.toString()).list())
				.thenReturn(executions);

		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.getProcessStepsByAccessioningID(accessioningId))
				.thenReturn(processstepdtolist);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		// Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId().desc().list()).thenReturn(tasklist);
		 Mockito.when(assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(wfmlistMp24Process,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
					WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString())).thenReturn(deviceTestOptions);
		wfmService.updateMp24Process(getActivityProcessDataDTO(), accessioningId, orderStatus, deviceId);
	}
	
	
	
	@Test
	public void updateMp24ProcessUpdateDuplicateStatusforMP24() throws Exception {
		config();
		List<WfmDTO> wfmlistMp24Process = new ArrayList<WfmDTO>();
		orderStatus = "Aborted";
		wfmlistMp24Process.add(getWfmDTO());
		List<ProcessStepActionDTO> processstepdtolist = new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
		processStepActionDTO.setDeviceType("MP24");
		processStepActionDTO.setManualVerificationFlag("Y");
		processstepdtolist.add(processStepActionDTO);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistMp24Process);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("NA");
		List<WfmDTO> orderdetails =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("NA");
		orderdetails.add(wfmDTO);
		
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(rocheWfm);
		
		Mockito.when(assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(wfmlistMp24Process,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
					WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString())).thenReturn(deviceTestOptions);
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(Mockito.anyString(), Mockito.anyString(),Mockito.anyString())).thenReturn(orderdetails);
		Mockito.doNothing().when(resultIntegrationService).updateSampleResults(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForMP24(Mockito.anyString(),Mockito.anyString(),Mockito.any());
		try {
		wfmService.updateMp24Process(getActivityProcessDataDTOMP24(), accessioningId, "NA", deviceId);
		}catch(Exception e) {
			
		}
	}

	
	@Test
	public void updateMp24ProcessUpdateDuplicateStatusforMP24Else() throws Exception {
		config();
		PowerMockito.mockStatic(AdmNotificationService.class);
		List<WfmDTO> wfmlistMp24Process = new ArrayList<WfmDTO>();
		orderStatus = "Aborted";
		wfmlistMp24Process.add(getWfmDTO());
		List<ProcessStepActionDTO> processstepdtolist = new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
		processStepActionDTO.setDeviceType("MP24");
		processStepActionDTO.setManualVerificationFlag("Y");
		processstepdtolist.add(processStepActionDTO);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistMp24Process);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("NA");
		List<WfmDTO> orderdetails =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("NA");
		orderdetails.add(wfmDTO);
		
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(rocheWfm);
		
		Mockito.when(assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(wfmlistMp24Process,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
					WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString())).thenReturn(deviceTestOptions);
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(Mockito.anyString(), Mockito.anyString(),Mockito.anyString())).thenReturn(orderdetails);
		//Mockito.doNothing().when(resultIntegrationService).updateSampleResults(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForMP24(Mockito.anyString(),Mockito.anyString(),Mockito.any());
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
		wfmService.updateMp24Process(getActivityProcessDataDTOMP24(), accessioningId, "N", deviceId);
		}catch(Exception e) {
			
		}
	}
	
	
	@Test
	public void updateMp24ProcessUpdateDuplicateStatusforMP24ElseSecond() throws Exception {
		config();
		PowerMockito.mockStatic(AdmNotificationService.class);
		List<WfmDTO> wfmlistMp24Process = new ArrayList<WfmDTO>();
		orderStatus = "Aborted";
		wfmlistMp24Process.add(getWfmDTO());
		List<ProcessStepActionDTO> processstepdtolist = new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
		processStepActionDTO.setDeviceType("MP24");
		processStepActionDTO.setManualVerificationFlag("Y");
		processstepdtolist.add(processStepActionDTO);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistMp24Process);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("Aborted");
		List<WfmDTO> orderdetails =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("Aborted");
		orderdetails.add(wfmDTO);
		
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(rocheWfm);
		
		Mockito.when(assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(wfmlistMp24Process,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
					WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString())).thenReturn(deviceTestOptions);
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(Mockito.anyString(), Mockito.anyString(),Mockito.anyString())).thenReturn(orderdetails);
		Mockito.doNothing().when(resultIntegrationService).updateSampleResults(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForMP24(Mockito.anyString(),Mockito.anyString(),Mockito.any());
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
		wfmService.updateMp24Process(getActivityProcessDataDTOMP24(), accessioningId, "Aborted", deviceId);
		}catch(Exception e) {
			
		}
	}
	
	
	@Test
	public void updateMp24ProcessUpdateDuplicateStatusforMP24ElseThird() throws Exception {
		config();
		PowerMockito.mockStatic(AdmNotificationService.class);
		List<WfmDTO> wfmlistMp24Process = new ArrayList<WfmDTO>();
		orderStatus = "Aborted";
		wfmlistMp24Process.add(getWfmDTO());
		List<ProcessStepActionDTO> processstepdtolist = new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
		processStepActionDTO.setDeviceType("MP24");
		processStepActionDTO.setManualVerificationFlag("Y");
		processstepdtolist.add(processStepActionDTO);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistMp24Process);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("NA");
		List<WfmDTO> orderdetails =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("Aborted");
		orderdetails.add(wfmDTO);
		
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(rocheWfm);
		
		Mockito.when(assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(wfmlistMp24Process,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
					WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString())).thenReturn(deviceTestOptions);
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(Mockito.anyString(), Mockito.anyString(),Mockito.anyString())).thenReturn(orderdetails);
		Mockito.doNothing().when(resultIntegrationService).updateSampleResults(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForMP24(Mockito.anyString(),Mockito.anyString(),Mockito.any());
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
		wfmService.updateMp24Process(getActivityProcessDataDTOMP24(), accessioningId, "Aborted", deviceId);
		}catch(Exception e) {
			
		}
	}
	
	@Test
	public void updateMp24ProcessrocheWfmNull() throws Exception {
		config();
		PowerMockito.mockStatic(AdmNotificationService.class);
		List<WfmDTO> wfmlistMp24Process = new ArrayList<WfmDTO>();
		orderStatus = "Aborted";
		wfmlistMp24Process.add(getWfmDTO());
		List<ProcessStepActionDTO> processstepdtolist = new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();  
		processStepActionDTO.setDeviceType("MP24");
		processStepActionDTO.setManualVerificationFlag("Y");
		processstepdtolist.add(processStepActionDTO);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistMp24Process);
		SampleWFMStates rocheWfm = null;
		List<WfmDTO> orderdetails =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("Aborted");
		orderdetails.add(wfmDTO);
		
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(rocheWfm);
		Mockito.doNothing().when(responseRenderingService).duplicateACKForMP24(Mockito.anyString(),Mockito.anyString(),Mockito.any());
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
		wfmService.updateMp24Process(getActivityProcessDataDTOMP24(), accessioningId, "Aborted", deviceId);
		}catch(Exception e) {
			
		}
	}
	
	
	
	@Test
	public void updateMp24ProcessInProgress() throws Exception {
		config();
		List<WfmDTO> wfmlistMp24Process = new ArrayList<WfmDTO>();
		orderStatus = "InProgress";
		wfmlistMp24Process.add(getWfmDTO());
		List<ProcessStepActionDTO> processstepdtolist = new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
		processStepActionDTO.setDeviceType("MP24");
		processStepActionDTO.setManualVerificationFlag("Y");
		processstepdtolist.add(processStepActionDTO);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmlistMp24Process);
		SampleWFMStates rocheWfm = getrocheWfm();
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(Mockito.anyString(), Mockito.anyString(),Mockito.anyLong())).thenReturn(rocheWfm);
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		Map<String, Object> executionMP24 = new HashMap<>();
		executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID.toString(), accessioningId);
		executionMP24.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUS.toString(), orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.MP24U03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.MP24U03.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.getProcessStepsByAccessioningID(accessioningId))
				.thenReturn(processstepdtolist);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		// Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId().desc().list()).thenReturn(tasklist);
		 Mockito.when(assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(wfmlistMp24Process,
					WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
					WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString())).thenReturn(deviceTestOptions);
		wfmService.updateMp24Process(getActivityProcessDataDTO(), accessioningId, orderStatus, deviceId);
	}

	@Test
	public void updateLp24PrePcrProcess() throws Exception {
		config();
		orderStatus = "Passed";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		
		wfmlistPrePcrProcess.add(getWfmDTO());
		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");
		
		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
		molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();
        Mockito.when(assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString())).thenReturn(molicularDetails);
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(
				Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(rocheWfm);
	    Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRU03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRU03.toString()).list())
				.thenReturn(executions);

		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Pre PCR", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);

		wfmService.updateLp24PrePcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
	}

	 public MolecularIDTypeDTO getMolecularIDTypeDTO() {
	     MolecularIDTypeDTO  mole = new MolecularIDTypeDTO();
	     mole.setAssayType("NIPTHTP");
	     mole.setAssayTypeId(1);
	     mole.setMolecularId("1");
	     mole.setPlateLocation("1");
        return mole;
	 }
	@Test
	public void updateLp24PrePcrProcessAborted() throws Exception {
		config();
		orderStatus = "Aborted";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(
            Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(rocheWfm);

		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		 Mockito.when(assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(Mockito.anyString(),
	            Mockito.anyString(), Mockito.anyString())).thenReturn(molicularDetails);
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRU03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRU03.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Pre PCR", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                    Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		wfmService.updateLp24PrePcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void updateLp24PrePcrProcessInProgress() throws Exception {
		config();
		orderStatus = "InProgress";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");


        List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
        
        Mockito.when(assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString())).thenReturn(molicularDetails);
        
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(
				Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(rocheWfm);

		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRU03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24PREPCRU03.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Pre PCR", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);

		wfmService.updateLp24PrePcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
	}
	
	
	@Test
	public void updateLp24PrePcrProcessAndupdateDuplicateStstusforLPPrePCR() throws Exception {
		config();
		orderStatus = "Flagged";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("NA");
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(), processStepName,1L)).thenReturn(rocheWfm);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		

		 Mockito.when(assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(Mockito.anyString(),
	            Mockito.anyString(), Mockito.anyString())).thenReturn(molicularDetails);
		 
		 Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		 
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), deviceTestOptions.get(0).getTestProtocol());
		List<WfmDTO> orderdetailsList =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("Flagged");
		orderdetailsList.add(wfmDTO);
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
				getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewContainerId(),  getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewOutputPosition(), WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(orderdetailsList);
		Mockito.when(wfmWriteRepository.save(rocheWfm,1L)).thenReturn(rocheWfm);
		Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(Mockito.anyString(),Mockito.any());
		try {
		wfmService.updateLp24PrePcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}

	
	@Test
	public void updateLp24PrePcrProcessAndupdateDuplicateStstusforLPPrePCRElse() throws Exception {
		config();
		PowerMockito.mockStatic(AdmNotificationService.class);
		orderStatus = "Flagged";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("NA");
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(), processStepName,1L)).thenReturn(rocheWfm);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		

		 Mockito.when(assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(Mockito.anyString(),
	            Mockito.anyString(), Mockito.anyString())).thenReturn(molicularDetails);
		 
		 Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		 
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), deviceTestOptions.get(0).getTestProtocol());
		List<WfmDTO> orderdetailsList =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("NA");
		orderdetailsList.add(wfmDTO);
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
				getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewContainerId(),  getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewOutputPosition(), WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(orderdetailsList);
		Mockito.when(wfmWriteRepository.save(rocheWfm,1L)).thenReturn(rocheWfm);
		Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(Mockito.anyString(),Mockito.any());
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
		wfmService.updateLp24PrePcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}
	
	
	
	@Test
	public void updateLp24PrePcrProcessAndupdateDuplicateStstusforLPPrePCRElseAborted() throws Exception {
		config();
		PowerMockito.mockStatic(AdmNotificationService.class);
		orderStatus = "Aborted";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("Aborted");
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(), processStepName,1L)).thenReturn(rocheWfm);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		

		 Mockito.when(assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(Mockito.anyString(),
	            Mockito.anyString(), Mockito.anyString())).thenReturn(molicularDetails);
		 
		 Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		 
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), deviceTestOptions.get(0).getTestProtocol());
		List<WfmDTO> orderdetailsList =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("Aborted");
		orderdetailsList.add(wfmDTO);
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
				getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewContainerId(),  getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewOutputPosition(), WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(orderdetailsList);
		Mockito.when(wfmWriteRepository.save(rocheWfm,1L)).thenReturn(rocheWfm);
		Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(Mockito.anyString(),Mockito.any());
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
		wfmService.updateLp24PrePcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}
	
	
	@Test
	public void updateLp24PrePcrProcessAndupdateDuplicateStstusforLPPrePCRElseNACondition() throws Exception {
		config();
		PowerMockito.mockStatic(AdmNotificationService.class);
		orderStatus = "Aborted";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("NA");
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(), processStepName,1L)).thenReturn(rocheWfm);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		

		 Mockito.when(assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(Mockito.anyString(),
	            Mockito.anyString(), Mockito.anyString())).thenReturn(molicularDetails);
		 
		 Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		 
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), deviceTestOptions.get(0).getTestProtocol());
		List<WfmDTO> orderdetailsList =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("NA");
		orderdetailsList.add(wfmDTO);
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
				getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewContainerId(),  getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewOutputPosition(), WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(orderdetailsList);
		Mockito.when(wfmWriteRepository.save(rocheWfm,1L)).thenReturn(rocheWfm);
		Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(Mockito.anyString(),Mockito.any());
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
		wfmService.updateLp24PrePcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}
	
	
	@Test
	public void updateLp24PrePcrProcessrocheWfmNull() throws Exception {
		config();
		PowerMockito.mockStatic(AdmNotificationService.class);
		orderStatus = "Aborted";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.WORKFLOW_MESSAGE_TYPE.NAEXTRACTION.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm = null;
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(), processStepName,1L)).thenReturn(rocheWfm);
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
		wfmService.updateLp24PrePcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}
	@Test
	public void updateLp24PostPcrProcess() throws Exception {
		config();
		orderStatus = "Passed";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());
		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();

		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(),Mockito.anyLong()))
				.thenReturn(rocheWfm);

		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		
		Map<String, Object> executionLp24PostPcrUpdate = new HashMap<>();
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPOSTPCRUPDATE.toString(),
				accessioningId);
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPOSTPCRUPDATE.toString(), deviceId);
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPOSTPCRUPDATE.toString(),
				orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRU03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRU03.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Post PCR/Pooling", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		wfmService.updateLp24PostPcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void updateLp24PostPcrProcessAborted() throws Exception {
		config();
		orderStatus = "Aborted";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyLong()))
				.thenReturn(rocheWfm);
		
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		Map<String, Object> executionLp24PostPcrUpdate = new HashMap<>();
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPOSTPCRUPDATE.toString(),
				accessioningId);
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPOSTPCRUPDATE.toString(), deviceId);
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPOSTPCRUPDATE.toString(),
				orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRU03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRU03.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Post PCR/Pooling", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		wfmService.updateLp24PostPcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void updateLp24PostPcrProcessInProgress() throws Exception {
		config();
		orderStatus = "InProgress";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		SampleWFMStates rocheWfm = getrocheWfm();
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(
		    Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(),Mockito.anyLong()))
				.thenReturn(rocheWfm);

		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		
		Map<String, Object> executionLp24PostPcrUpdate = new HashMap<>();
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPOSTPCRUPDATE.toString(),
				accessioningId);
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPOSTPCRUPDATE.toString(), deviceId);
		executionLp24PostPcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPOSTPCRUPDATE.toString(),
				orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRU03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24POSTPCRU03.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Post PCR/Pooling", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                    Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		wfmService.updateLp24PostPcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
	}

	
	@Test
	public void updateLp24PostPcrProcessUpdateDuplicateStatusforLPPostPCR() throws Exception {
		
		config();
		orderStatus = "flagged";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("NA");
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(),deviceId, processStepName,1L)).thenReturn(rocheWfm);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		

	
		 
		 Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		 
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), deviceTestOptions.get(0).getTestProtocol());
		List<WfmDTO> orderdetailsList =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("flagged");
		orderdetailsList.add(wfmDTO);
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
				getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewContainerId(),  getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewOutputPosition(), WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())).thenReturn(orderdetailsList);
		Mockito.when(wfmWriteRepository.save(rocheWfm,1L)).thenReturn(rocheWfm);
		Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(Mockito.anyString(),Mockito.any());
		try {
		wfmService.updateLp24PostPcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}
	
	
	
	@Test
	public void updateLp24PostPcrProcessUpdateDuplicateStatusforLPPostPCRElse() throws Exception {
		
		config();
		orderStatus = "flagged";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("NA");
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(),deviceId, processStepName,1L)).thenReturn(rocheWfm);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		

	
		 
		 Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		 
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), deviceTestOptions.get(0).getTestProtocol());
		List<WfmDTO> orderdetailsList =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("NA");
		orderdetailsList.add(wfmDTO);
		
		PowerMockito.mockStatic(AdmNotificationService.class);
		PowerMockito.doNothing()
	        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
				getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewContainerId(),  getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewOutputPosition(), WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())).thenReturn(orderdetailsList);
		Mockito.when(wfmWriteRepository.save(rocheWfm,1L)).thenReturn(rocheWfm);
		Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(Mockito.anyString(),Mockito.any());
		try {
		wfmService.updateLp24PostPcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}
	
	
	@Test
	public void updateLp24PostPcrProcessUpdateDuplicateStatusforLPPostPCRElseAborted() throws Exception {
		
		config();
		orderStatus = "Aborted";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("Aborted");
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(),deviceId, processStepName,1L)).thenReturn(rocheWfm);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		

	
		 
		 Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		 
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), deviceTestOptions.get(0).getTestProtocol());
		List<WfmDTO> orderdetailsList =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("NA");
		orderdetailsList.add(wfmDTO);
		
		PowerMockito.mockStatic(AdmNotificationService.class);
		PowerMockito.doNothing()
	        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
				getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewContainerId(),  getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewOutputPosition(), WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())).thenReturn(orderdetailsList);
		Mockito.when(wfmWriteRepository.save(rocheWfm,1L)).thenReturn(rocheWfm);
		Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(Mockito.anyString(),Mockito.any());
		try {
		wfmService.updateLp24PostPcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}
	
	
	@Test
	public void updateLp24PostPcrProcessUpdateDuplicateStatusforLPPostPCRElseNotification() throws Exception {
		
		config();
		orderStatus = "Aborted";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("NA");
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(),deviceId, processStepName,1L)).thenReturn(rocheWfm);
		Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.iterator().next().getAccessioningId()))
				.thenReturn(wfmlistPrePcrProcessorder);
		

	
		 
		 Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
					Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
		 
		Map<String, Object> executionLp24prePcrUpdate = new HashMap<>();
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString(),
				accessioningId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString(), deviceId);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString(), orderStatus);
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ASSAYTYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getAssayType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.SAMPLETYPE.toString(),
				wfmlistPrePcrProcess.iterator().next().getSampleType());
		executionLp24prePcrUpdate.put(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLDPCRLP24.toString(), deviceTestOptions.get(0).getTestProtocol());
		List<WfmDTO> orderdetailsList =new ArrayList<>();
		WfmDTO wfmDTO=new WfmDTO();
		wfmDTO.setOrderResult("NA");
		orderdetailsList.add(wfmDTO);
		
		PowerMockito.mockStatic(AdmNotificationService.class);
		PowerMockito.doNothing()
	        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
				getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewContainerId(),  getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getStatusUpdate().getSampleInfo().getNewOutputPosition(), WfmConstants.ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())).thenReturn(orderdetailsList);
		Mockito.when(wfmWriteRepository.save(rocheWfm,1L)).thenReturn(rocheWfm);
		Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(Mockito.anyString(),Mockito.any());
		try {
		wfmService.updateLp24PostPcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}
	
	
	
	@Test
	public void updateLp24PostPcrProcessrocheWfmNull() throws Exception {
		
		config();
		orderStatus = "Aborted";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");

		List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
        molicularDetails.add(getMolecularIDTypeDTO());
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
                .getObject(Mockito.anyString())).thenReturn("token");
		
		Mockito.when(resultIntegrationService.findAccessingIdByContainerId(container[0], container[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(wfmlistPrePcrProcess);
		SampleWFMStates rocheWfm =null;
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(wfmlistPrePcrProcess.iterator().next().getAccessioningId(),deviceId, processStepName,1L)).thenReturn(rocheWfm);
		
		
		PowerMockito.mockStatic(AdmNotificationService.class);
		PowerMockito.doNothing()
	        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(),Mockito.any());
		Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(Mockito.anyString(),Mockito.any());
		try {
		wfmService.updateLp24PostPcrProcess(containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
		}catch(Exception e) {
			
		}
	}
	@Test
	public void updateLp24SeqPrepProcess() throws Exception {
		config();
		orderStatus = "Passed";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");
		
		  Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
		  container[0], container[1],
		  WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(
		  wfmlistPrePcrProcess);
		  Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.
		  iterator().next().getAccessioningId())).thenReturn(
		  wfmlistPrePcrProcessorder);
		 
		rocheWfm = getrocheWfm();
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(accessioningId, deviceId,
				processStepName,-1)).thenReturn(SampleWFMStates);
		Mockito.when(SampleWFMStates.getCompany()).thenReturn(company);
		Mockito.when(company.getId()).thenReturn(1L);
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);

		Map<String, Object> executionLp24SeqPreUpdate = new HashMap<>();
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.CONTAINERIDSEQPREUPDATE.toString(), container[0]);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.CONTAINERPOSITIONSEQPREUPDATE.toString(),
				container[1]);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDSEQPREUPDATE.toString(),
				accessioningId);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDSEQPREUPDATE.toString(), deviceId);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSSEQPREUPDATE.toString(), orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Sequencing Prep", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);

		wfmService.updateLp24SeqPrepProcess(accessioningId, containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void updateLp24SeqPrepProcessAborted() throws Exception {
		config();
		orderStatus = "Aborted";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");
		
		  Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
		  container[0], container[1],
		  WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(
		  wfmlistPrePcrProcess);
		  Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.
		  iterator().next().getAccessioningId())).thenReturn(
		  wfmlistPrePcrProcessorder);
		 
		SampleWFMStates rocheWfm = getrocheWfm();
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(accessioningId, deviceId,
				processStepName,-1)).thenReturn(rocheWfm);
		
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		Map<String, Object> executionLp24SeqPreUpdate = new HashMap<>();
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.CONTAINERIDSEQPREUPDATE.toString(), container[0]);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.CONTAINERPOSITIONSEQPREUPDATE.toString(),
				container[1]);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDSEQPREUPDATE.toString(),
				accessioningId);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDSEQPREUPDATE.toString(), deviceId);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSSEQPREUPDATE.toString(), orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString()).list())
				.thenReturn(executions);

		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Sequencing Prep", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);

		wfmService.updateLp24SeqPrepProcess(accessioningId, containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void updateLp24SeqPrepProcessInProgress() throws Exception {
		config();
		orderStatus = "InProgress";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		String[] container = containerId.trim().split("_");
		
		  Mockito.when(resultIntegrationService.findAccessingIdByContainerId(
		  container[0], container[1],
		  WfmConstants.ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(
		  wfmlistPrePcrProcess);
		  Mockito.when(orderIntegrationService.findOrder(wfmlistPrePcrProcess.
		  iterator().next().getAccessioningId())).thenReturn(
		  wfmlistPrePcrProcessorder);
		 
		SampleWFMStates rocheWfm = getrocheWfm();

		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(accessioningId, deviceId,
				processStepName,-1)).thenReturn(rocheWfm);
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		
		Map<String, Object> executionLp24SeqPreUpdate = new HashMap<>();
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.CONTAINERIDSEQPREUPDATE.toString(), container[0]);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.CONTAINERPOSITIONSEQPREUPDATE.toString(),
				container[1]);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDSEQPREUPDATE.toString(),
				accessioningId);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(),
				activityProcessData);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDSEQPREUPDATE.toString(), deviceId);
		executionLp24SeqPreUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSSEQPREUPDATE.toString(), orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Sequencing Prep", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);

		wfmService.updateLp24SeqPrepProcess(accessioningId, containerId, deviceId, orderStatus, processStepName,
				getActivityProcessDataDTO());
	}

	@Test
	public void updateHTPProcess() throws Exception {
		config();
		orderStatus = "Started";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());
		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());
		SampleWFMStates rocheWfm = getrocheWfm();
		/*Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
				WfmConstants.ASSAY_PROCESS_STEP_DATA.SEQPREP.toString())).thenReturn(rocheWfm);*/
		
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		Map<String, Object> executionHTPUpdate = new HashMap<>();
		executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDHTPUPDATE.toString(), accessioningId);
		executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), getActivityProcessDataDTO());

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPSTARTED.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPSTARTED.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Sequencing Prep", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		wfmService.updateHTPProcess(accessioningId,8L, 10000002L,orderStatus ,"ORD481083315",getActivityProcessDataDTO());
	}

	@Test
	public void updateHTPProcessCompleted() throws Exception {
		config();
		orderStatus = "Completed";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());

		SampleWFMStates rocheWfm = getrocheWfm();

		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(),Mockito.anyLong())).thenReturn(rocheWfm);

		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		Map<String, Object> executionHTPUpdate = new HashMap<>();
		executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDHTPUPDATE.toString(), accessioningId);
		executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), getActivityProcessDataDTO());

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPCOMPLETED.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPCOMPLETED.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		wfmService.updateHTPProcess(accessioningId,8L, 10000002L,orderStatus ,"ORD481083315",getActivityProcessDataDTO());

	}

	@Test
	public void updateHTPProcessInProcess() throws Exception {
		config();
		orderStatus = "InProcess";
		List<WfmDTO> wfmlistPrePcrProcess = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcess.add(getWfmDTO());

		List<WfmDTO> wfmlistPrePcrProcessorder = new ArrayList<WfmDTO>();
		wfmlistPrePcrProcessorder.add(getWfmDTOSecond());

		SampleWFMStates rocheWfm = getrocheWfm();

		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
		    Mockito.anyString(),Mockito.anyLong())).thenReturn(rocheWfm);

		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		Map<String, Object> executionHTPUpdate = new HashMap<>();
		executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDHTPUPDATE.toString(), accessioningId);
		executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString(), activityProcessData);
		executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDHTPUPDATE.toString(), deviceId);
		executionHTPUpdate.put(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSHTPUPDATE.toString(), orderStatus);

		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPINPROCESS.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.HTPINPROCESS.toString()).list())
				.thenReturn(executions);
		Mockito.when(wfmWriteRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(assayIntegrationService.validateAssayProcessStepManual(
				wfmlistPrePcrProcess.iterator().next().getAccessioningId(), "LP24", "LP Sequencing Prep", "Y"))
				.thenReturn(true);
		Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
		wfmService.updateHTPProcess(accessioningId,8L, 10000002L,orderStatus,"ORD481083315",getActivityProcessDataDTO());

	}

	@Test
	public void updateForteProcessTest() throws Exception {		
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm);
		Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.FORTETERTIARYSTARTEVENT.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.FORTETERTIARYSTARTEVENT.toString()).list())
				.thenReturn(executions);
		boolean resp = wfmService.updateForteProcess(accessioningId, "start", sendingApplicationName,
				"Tertiary");
		assertEquals(resp, true);
	}
	
	@Test
	public void updateLP24SeqPPTest() throws HMTPException, OrderNotFoundException, QueryValidationException, IOException {
		config();
	    PowerMockito.mockStatic(ThreadSessionManager.class);
	    Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
	    Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
	    Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(accessioningId, deviceId,
				processStepName, 1L)).thenReturn(getrocheWfmCompletedStatus());
	    Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
	    Mockito.when(runtimeService.createExecutionQuery().processInstanceId(Mockito.anyString())).thenReturn(executionQuery);
	    Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                        .signalEventSubscriptionName(Mockito.anyString())).thenReturn(executionQuery);
	    Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                        .signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString()).list()).thenReturn(executions);
	    Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
				accessioningId, getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getSendingApplicationName(), getActivityProcessDataDTO().getSpecimenStatusUpdateMessage().getProcessStepName())).thenReturn(deviceTestOptions);
	    String containerIds[]=containerId.trim().split("_");
	    List<WfmDTO> orderdetails=new ArrayList<>();
	    orderdetails.add(getWfmDTO());
	    Mockito.when(resultIntegrationService.findAccessingIdByContainerId(containerIds[0], containerIds[1],
				WfmConstants.ASSAY_PROCESS_STEP_DATA.SEQUENCING.toString())).thenReturn(orderdetails);
	    
	    Mockito.doNothing().when(resultIntegrationService).updateforLP24(Mockito.any(), Mockito.any());
	    Mockito.when(wfmWriteRepository.save(rocheWfm,1L)).thenReturn(rocheWfm);
	    try {
	    wfmService.updateLP24SeqPP(accessioningId, containerId, deviceId, "Passed", processStepName, getActivityProcessDataDTO());
	    }catch(Exception e) {
	    	
	    }
	}
	
	
	
	
	
	@Test
    public void updateLP24SeqPPNegativeElseTest() throws HMTPException, OrderNotFoundException, QueryValidationException, IOException {
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyLong())).thenReturn(getrocheWfm());
        Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
        Mockito.when(runtimeService.createExecutionQuery().processInstanceId(Mockito.anyString())).thenReturn(executionQuery);
        Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                        .signalEventSubscriptionName(Mockito.anyString())).thenReturn(executionQuery);
        Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                        .signalEventSubscriptionName(WfmConstants.WORKFLOW_SIGNALS.LP24SEQPREPU03.toString()).list()).thenReturn(executions);
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                            Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(deviceTestOptions);
        Mockito.when(taskService.createTaskQuery()).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningId)).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId()).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId()
                                .desc()).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningId).orderByTaskId()
                                .desc().list()).thenReturn(ts);
        Mockito.when(ts.get(0)).thenReturn(task);
        Mockito.when(ts.get(0).getId()).thenReturn("1");
       // Mockito.doNothing().when(taskService).complete(Mockito.anyString());
        wfmService.updateLP24SeqPP(accessioningId, containerId, deviceId, "Passed", processStepName, getActivityProcessDataDTO());
    }
	
	@Test public void getContentTest(){
	       config();
	       wfmService.getContent(deviceId);
	}
	
	@Test public void getContentandDeviceTest(){
        config();
        wfmService.getContent(deviceId,accessioningId);
 }
	
	public DeviceTestOptionsDTO getDeviceTestOptionsDTO() {
	    DeviceTestOptionsDTO device = new DeviceTestOptionsDTO();
	    device.setDeviceType("LPSeqPrep");
	    device.setTestName("testName");
	    device.setTestProtocol("testProtocol");
        return device;
	}

	public ActivityProcessDataDTO getActivityProcessDataDTO() {
		ActivityProcessDataDTO activityProcessDataDTO = new ActivityProcessDataDTO();

		activityProcessDataDTO.setAccessioningId("100001");
		activityProcessDataDTO.setDeviceId("12345");
		activityProcessDataDTO.setMessageType("NIPTHTP");
		activityProcessDataDTO.setDateTimeMessageGenerated("20181024191916");
		activityProcessDataDTO.setBatchId("BTCH");
		activityProcessDataDTO.setContainerId("EB501");
		QueryMessage queryMessage = new QueryMessage();
		queryMessage.setDeviceSerialNumber("5416");
		queryMessage.setSendingApplicationName(sendingApplicationName);
		queryMessage.setContainerId(containerId);
		activityProcessDataDTO.setQueryMessage(queryMessage);
		SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
		specimenStatusUpdateMessage.setContainerId(containerId);
		specimenStatusUpdateMessage.setAccessioningId(accessioningId);
		specimenStatusUpdateMessage.setSendingApplicationName(sendingApplicationName);
		specimenStatusUpdateMessage.setProcessStepName(processStepName);
		StatusUpdate statusUpdate = new StatusUpdate();
		ContainerInfo containerInfo = new ContainerInfo();
		containerInfo.setOutputPlateType("outputPlateType");
        statusUpdate.setContainerInfo(containerInfo );
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setNewContainerId("newxontainerId");
        sampleInfo.setNewOutputPosition("newOutputPosition");
        statusUpdate.setSampleInfo(sampleInfo );
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate );
        activityProcessDataDTO.setSpecimenStatusUpdateMessage(specimenStatusUpdateMessage);
        HtpStatusMessage htpStatusMessage = new HtpStatusMessage();
        htpStatusMessage.setDeviceId("HTP");
        htpStatusMessage.setStatus("Started");
        htpStatusMessage.setInputContainerId("67786774");
        htpStatusMessage.setOutputContainerId("37634566");
        htpStatusMessage.setProcessStepName("Sequencing");
        htpStatusMessage.setSendingApplication("HTP");
        activityProcessDataDTO.setHtpStatusMessage(htpStatusMessage);
		return activityProcessDataDTO;
	}

	
	public ActivityProcessDataDTO getActivityProcessDataDTOMP24() {
		ActivityProcessDataDTO activityProcessDataDTO = new ActivityProcessDataDTO();
		com.roche.connect.common.mp24.message.AdaptorRequestMessage adaptorRequestMessage=new com.roche.connect.common.mp24.message.AdaptorRequestMessage();
		activityProcessDataDTO.setAccessioningId("100001");
		activityProcessDataDTO.setDeviceId("12345");
		activityProcessDataDTO.setMessageType("NIPTHTP");
		activityProcessDataDTO.setDateTimeMessageGenerated("20181024191916");
		activityProcessDataDTO.setBatchId("BTCH");
		activityProcessDataDTO.setContainerId("EB501");
		com.roche.connect.common.mp24.message.StatusUpdate statusUpdate = new com.roche.connect.common.mp24.message.StatusUpdate();
		com.roche.connect.common.mp24.message.ContainerInfo containerInfo = new com.roche.connect.common.mp24.message.ContainerInfo();
        statusUpdate.setContainerInfo(containerInfo );
        com.roche.connect.common.mp24.message.SampleInfo sampleInfo = new com.roche.connect.common.mp24.message.SampleInfo();
        sampleInfo.setSampleOutputId("A1");
        sampleInfo.setSampleOutputPosition("B1");
        statusUpdate.setSampleInfo(sampleInfo);
        adaptorRequestMessage.setStatusUpdate(statusUpdate);
        activityProcessDataDTO.setAdaptorRequestMessage(adaptorRequestMessage);
        
		return activityProcessDataDTO;
	}
	public WfmDTO getWfmDTO() {
		WfmDTO wfmDTO = new WfmDTO();

		wfmDTO.setSendingApplicationName("MagnaPure24");
		wfmDTO.setDeviceId("abc");
		wfmDTO.setAssayType("NIPTHTP");
		wfmDTO.setOrderId(Long.parseLong("1000001"));
		wfmDTO.setAccessioningId("1000111");
		wfmDTO.setOrderStatus("Completed");
		wfmDTO.setRunid(Long.parseLong("1234"));
		wfmDTO.setInputContainerId("12abc");
		wfmDTO.setInputposition("ABC");
		wfmDTO.setSampleType("pLASMA");
		wfmDTO.setRunResultsId(Long.parseLong("123456"));
		wfmDTO.setProtocolname("NA");
		return wfmDTO;
	}

	public WfmDTO getWfmDTOSecond() {
		WfmDTO wfmDTO = new WfmDTO();

		wfmDTO.setSendingApplicationName("MagnaPure24");
		wfmDTO.setDeviceId("abc");
		wfmDTO.setAssayType("NIPTDPCR");
		wfmDTO.setOrderId(Long.parseLong("1000001"));
		wfmDTO.setAccessioningId("1000111");
		wfmDTO.setOrderStatus("Inprogress");
		wfmDTO.setRunid(Long.parseLong("1234"));
		wfmDTO.setInputContainerId("12abc");
		wfmDTO.setInputposition("ABC");
		wfmDTO.setSampleType("pLASMA");
		wfmDTO.setRunResultsId(Long.parseLong("123456"));
		wfmDTO.setProtocolname("NA");
		return wfmDTO;
	}

	public SampleWFMStates getrocheWfm() {
		SampleWFMStates rocheWfm = new SampleWFMStates();
		rocheWfm.setAccessioningId("8001");
		rocheWfm.setDeviceId("MPCZC8380G5K");
		rocheWfm.setProcessId("15");
		rocheWfm.setMessageType("NA Extraction");
		rocheWfm.setCurrentStatus("Query");
		Company company=new Company();
        company.setId(1L);
        rocheWfm.setCompany(company);
		return rocheWfm;

	}
	
	public SampleWFMStates getrocheWfmCompletedStatus() {
        SampleWFMStates rocheWfm = new SampleWFMStates();
        rocheWfm.setAccessioningId("8001");
        rocheWfm.setDeviceId("MPCZC8380G5K");
        rocheWfm.setProcessId("15");
        rocheWfm.setMessageType("NA Extraction");
        rocheWfm.setCurrentStatus("Completed");
        Company company=new Company();
        company.setId(1L);
        rocheWfm.setCompany(company);
        return rocheWfm;

    }

	public SampleWFMStates getrocheWfmSecond() {
		SampleWFMStates rocheWfm = new SampleWFMStates();
		rocheWfm.setAccessioningId("8001");
		rocheWfm.setDeviceId("MPCZC8380G5K");
		rocheWfm.setProcessId("15");
		rocheWfm.setMessageType("NA Extraction");
		rocheWfm.setCurrentStatus("Query");
		Company company=new Company();
        company.setId(1L);
        rocheWfm.setCompany(company);
		return rocheWfm;

	}

}
