package com.roche.connect.wfm.test.testng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

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
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
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
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMORUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMQueryMessage;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.ASSAY_PROCESS_STEP_DATA;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.AssayTypeDTO;
import com.roche.connect.wfm.model.ProcessStepActionDTO;
import com.roche.connect.wfm.model.SampleWFMStates;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.repository.WfmReadRepository;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;
import com.roche.connect.wfm.service.UserTaskService;
import com.roche.connect.wfm.service.WFMDPCRService;
import com.roche.connect.wfm.writerepository.WfmWriteRepository;

@PrepareForTest({ RestClientUtil.class, WfmdPCRServiceTest.class,ThreadSessionManager.class }) @PowerMockIgnore({ "sun.misc.Launcher.*",
"com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*", "org.apache.logging.*",
"org.slf4j.*" }) public class WfmdPCRServiceTest extends AbstractTestNGSpringContextTests {
    
    @InjectMocks private WFMDPCRService wfmdPCRService = new WFMDPCRService();    
    @Mock Invocation.Builder assayClient;
    @Mock OrderIntegrationService orderIntegrationService;
    @Mock AssayIntegrationService assayIntegrationService;
    @Mock RuntimeService runtimeService;
    @Mock ProcessInstance processInstance;
    @Mock WfmReadRepository wfmRepository;
    @Mock UserTaskService userTaskService;
    @Mock ExecutionQuery executionQuery;
    @Mock ResponseRenderingService responseRenderingService;
    @Mock WfmWriteRepository wfmWriteRepository; 
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    @Mock Invocation.Builder orderClient;
    @Mock Response response;
    @Mock Execution exec1;
    @Mock ResultIntegrationService resultIntegrationService;
    @Mock TaskService taskService;
    @Mock TaskQuery taskQuery;
	@Mock List<Task> ts;
    @Mock Task task;
	
    List<WfmDTO> wfmList = new ArrayList<>();
    List<AssayTypeDTO> assayList = new ArrayList<>();
    Map<String, Object> variables = new HashMap<>();
    List<ProcessStepActionDTO> processSteps = new ArrayList<>();
    List<ProcessStepActionDTO> processStepsSecond = new ArrayList<>();
    List<Execution> executions = new ArrayList<>();
    WFMDPCRService wfmdPCRServiceMock = Mockito.spy(wfmdPCRService);
    
    String accessioningId = "8001";
    String accessioningIdSecond = "80012";
    String assayType = "NIPTDPCR";
    String processingId = "1500";
    String deviceId = "MPCZC8380G5K";
    String containerId = "83c8a11b_A1";
    String containerPosition = "A1";
   
    
    @BeforeTest public void setUp() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
        executions.add(exec1);
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        wfmList.add(getWfmDTO());
        processSteps.add(getProcessStepActionDTO());
        processStepsSecond.add(getProcessStepActionDTONegativeFlag());
        
        Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
        Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
        
    }
    
    
    //** Mandatory to mock static classes *
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    
    @Test public void updateMp96Process() throws HMTPException, OrderNotFoundException, IOException {  
        SampleWFMStates rocheWfm = getrocheWfm();
        SampleWFMStates rocheWfm1 = getrocheWfm();
        rocheWfm1.setCurrentStatus("Completed");
        wfmList.add(getWfmDTO());
        
        
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);
        long domainId=-1L;
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, deviceId, domainId)).thenReturn(rocheWfm1);
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(),1L)).thenReturn(rocheWfm);  
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96U03.toString()))
            .thenReturn(executionQuery);
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96U03.toString()).list())
            .thenReturn(executions);
        Mockito.doNothing().when(resultIntegrationService).updateforMP96Status(Mockito.anyList(), Mockito.any(WFMoULMessage.class));
        Mockito.when(assayIntegrationService.getProcessStepsByAccessioningID(accessioningId)).thenReturn(processSteps);
        Mockito.when(wfmRepository.save(rocheWfm)).thenReturn(rocheWfm);
        
        wfmdPCRService.updateMp96Process(getActivityProcessDataDTO(), accessioningId, "passed", deviceId);
        
    }
    
    @Test public void updateMp96ProcessNcondition() throws HMTPException, OrderNotFoundException, IOException {  
        SampleWFMStates rocheWfm = getrocheWfm();
        SampleWFMStates rocheWfm1 = getrocheWfm();
        rocheWfm1.setCurrentStatus("AA");
        wfmList.add(getWfmDTO());
        
       
        
        Mockito.when(orderIntegrationService.findOrder(accessioningIdSecond)).thenReturn(wfmList);
        long domainId=-1L;
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningIdSecond, deviceId, domainId)).thenReturn(rocheWfm1);
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningIdSecond,
            WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(),1L)).thenReturn(rocheWfm);  
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96U03.toString()))
            .thenReturn(executionQuery);
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96U03.toString()).list())
            .thenReturn(executions);
        Mockito.doNothing().when(resultIntegrationService).updateforMP96Status(Mockito.anyList(), Mockito.any(WFMoULMessage.class));
        Mockito.when(assayIntegrationService.getProcessStepsByAccessioningID(accessioningIdSecond)).thenReturn(processStepsSecond);
        Mockito.when(wfmRepository.save(rocheWfm)).thenReturn(rocheWfm);
        
        Mockito.when(taskService.createTaskQuery()).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond)).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond).orderByTaskId()).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond).orderByTaskId()
                                .desc()).thenReturn(taskQuery);
        Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond).orderByTaskId()
                                .desc().list()).thenReturn(ts);
        Mockito.when(ts.get(0)).thenReturn(task);
        Mockito.when(ts.get(0).getId()).thenReturn("1");
        
        wfmdPCRService.updateMp96Process(getActivityProcessDataDTO(), accessioningIdSecond, "Passed", deviceId);
        
    }
    
    @Test public void updateMp96NeagativeProcess() throws HMTPException, OrderNotFoundException, IOException {  
        SampleWFMStates rocheWfm = getrocheWfm();
        
        wfmList.add(getWfmDTO());
        
        
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);
        long domainId=-1L;
		Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, deviceId, domainId)).thenReturn(rocheWfm);
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(),1L)).thenReturn(rocheWfm);  
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96U03.toString()))
            .thenReturn(executionQuery);
        Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96U03.toString()).list()).thenReturn(executions);
        Mockito.when(assayIntegrationService.getProcessStepsByAccessioningID(accessioningId)).thenReturn(processSteps);
        Mockito.when(wfmRepository.save(rocheWfm)).thenReturn(rocheWfm);
        
        wfmdPCRService.updateMp96Process(getActivityProcessDataDTO(), accessioningId, "passed", deviceId);
        
        wfmdPCRService.updateMp96Process(getActivityProcessDataDTO(), accessioningId, "Failed", deviceId);
    }
    
    @Test public void
         updatemp96Ack() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {       
        SampleWFMStates rocheWfm = new SampleWFMStates();
        rocheWfm = getrocheWfm();
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(),1l)).thenReturn(rocheWfm);              
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96ACK.toString()))
            .thenReturn(executionQuery);
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.MP96ACK.toString()).list())
            .thenReturn(executions);        
        wfmdPCRService.updatemp96Ack(getActivityProcessDataDTO(), accessioningId);
    }
    
    @Test public void
        startdPCRLp24Process() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
        SampleWFMStates rocheWfm1 = new SampleWFMStates();
        rocheWfm1 = getrocheWfm();
        wfmList.add(getWfmDTO());
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString(),1l)).thenReturn(rocheWfm1);
        
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.any(String.class),
        		Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(rocheWfm1);
        
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(),1)).thenReturn(rocheWfm1);
        Mockito.doNothing().when(responseRenderingService).duplicateQueryPositiveResponsefordPCRLP(containerId,
            deviceId,"8001", "1321434");        
        Mockito.when(wfmRepository.save(rocheWfm1)).thenReturn(rocheWfm1);
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24QBP.toString()))
            .thenReturn(executionQuery);
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24QBP.toString()).list())
            .thenReturn(executions);
        
        wfmdPCRService.startdPCRLp24Process(containerId, accessioningId, deviceId, getActivityProcessDataDTO());
    }

	@Test
	public void startdPCRnegativeLp24Process()
			throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
		SampleWFMStates rocheWfm = new SampleWFMStates();
		rocheWfm = getrocheWfm();
		wfmList.add(getWfmDTO());
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
				WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(), -1l)).thenReturn(rocheWfm);

		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);

		Mockito.when(wfmRepository.save(rocheWfm)).thenReturn(rocheWfm);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24QBP.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24QBP.toString()).list())
				.thenReturn(executions);

		wfmdPCRService.startdPCRLp24Process(containerId, accessioningId, deviceId, getActivityProcessDataDTO());
	}
	
	@Test
	public void startdPCRnegativeLp24ProcessTest1()
			throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
		SampleWFMStates rocheWfm = getrocheWfm();
		rocheWfm.setCurrentStatus("Aborted");
		
		wfmList.add(getWfmDTO());
		
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
	            WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString(),1l)).thenReturn(null);
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
				WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(), -1l)).thenReturn(rocheWfm);

		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);

		wfmdPCRService.startdPCRLp24Process(containerId, accessioningId, deviceId, getActivityProcessDataDTO());
	}
    
    @Mock UserSession userSession;
    
    @Test public void startdPCRAnalyzerProcessPositiveTest()
        throws HMTPException,
        UnsupportedEncodingException,
        OrderNotFoundException {
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.DPCRANALYZER.toString(), 1L)).thenReturn(getrocheWfm1());
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
                    WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(), 1L)).thenReturn(getrocheWfm1());
        
        wfmdPCRService.startdPCRAnalyzerProcess(containerId,containerPosition,accessioningId, deviceId, getActivityProcessDataDTO());
    }
    
    @Test public void
    startdPCRAnalyzerProcessNegativeTest() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
   
    PowerMockito.mockStatic(ThreadSessionManager.class);
    Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
    Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
    Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
        WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.DPCRANALYZER.toString(), 1L)).thenReturn(null);
    Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(), 1L)).thenReturn(getrocheWfm());
        
    Mockito
    .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
        .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERQBP.toString()))
    .thenReturn(executionQuery);
Mockito
    .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
        .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERQBP.toString()).list())
    .thenReturn(executions);
    
    wfmdPCRService.startdPCRAnalyzerProcess(containerId, containerPosition,accessioningId, deviceId, getActivityProcessDataDTO());
  }
    
    @Test public void
    startdPCRAnalyzerProcessNegativeTest1() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
   
    PowerMockito.mockStatic(ThreadSessionManager.class);
    
    SampleWFMStates smpStatus = getrocheWfm();
    smpStatus.setCurrentStatus("Aborted");
    Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
    Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
    Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
        WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.DPCRANALYZER.toString(), 1L)).thenReturn(null);
    Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(), 1L)).thenReturn(smpStatus);
  
    wfmdPCRService.startdPCRAnalyzerProcess(containerId, containerPosition,accessioningId, deviceId, getActivityProcessDataDTO());
    wfmdPCRService.startdPCRLp24Process(containerId, accessioningId, deviceId, getActivityProcessDataDTO());
  }
    
    @Test public void updatedPCRAnalyzerProcess()
        throws HMTPException,
        OrderNotFoundException, IOException {
       
    	String deviceId ="RX-123";
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        
		SampleWFMStates rocheWfm1 = new SampleWFMStates();
        rocheWfm1=getrocheWfm();
		
		 
		wfmList.add(getWfmDTO());
        Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, deviceId, 1L)).thenReturn(rocheWfm1);
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.ASSAY_PROCESS_STEP_DATA.DPCRANALYZERPROCESSNAME.toString(), 1L)).thenReturn(getrocheWfm());
        Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
        Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
        
        Mockito.when(assayIntegrationService.validateAssayProcessStepManual(accessioningId,
            ASSAY_PROCESS_STEP_DATA.DPCR_ANALYZER.toString(),
            ASSAY_PROCESS_STEP_DATA.DPCRANALYZERPROCESSNAME.toString(),
            WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())).thenReturn(true);
        
        Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
        
        Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
            .signalEventSubscriptionName(Mockito.anyString())).thenReturn(executionQuery);
        
        Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
            .signalEventSubscriptionName(Mockito.anyString()).list()).thenReturn(executions);
        
        wfmdPCRService.updatedPCRAnalyzerProcess(accessioningId, "InProgress", getActivityProcessDataDTO());
        
        wfmdPCRService.updatedPCRAnalyzerProcess(accessioningId, "Passed", getActivityProcessDataDTO());
        
        wfmdPCRService.updatedPCRAnalyzerProcess(accessioningId, "Aborted", getActivityProcessDataDTO());
        
        wfmdPCRService.updatedPCRAnalyzerProcess(accessioningId, "", getActivityProcessDataDTO());
    }
   
    
    @Test public void updatedPCRAnalyzerProcessElseTaskCase()
            throws HMTPException,
            OrderNotFoundException, IOException {
           
        	String deviceId ="RX-123";
            PowerMockito.mockStatic(ThreadSessionManager.class);
            Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
            Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
            
    		SampleWFMStates rocheWfm1 = new SampleWFMStates();
            rocheWfm1=getrocheWfm();
            rocheWfm1.setCurrentStatus("InProgress");
    		
    		 
    		wfmList.add(getWfmDTO());
            Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningIdSecond, deviceId, 1L)).thenReturn(rocheWfm1);
            
            
            Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningIdSecond,
                WfmConstants.ASSAY_PROCESS_STEP_DATA.DPCRANALYZERPROCESSNAME.toString(), 1L)).thenReturn(getrocheWfm());
            Mockito.when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);
            Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)).thenReturn(executionQuery);
            
            Mockito.when(assayIntegrationService.validateAssayProcessStepManual(accessioningIdSecond,
                ASSAY_PROCESS_STEP_DATA.DPCR_ANALYZER.toString(),
                ASSAY_PROCESS_STEP_DATA.DPCRANALYZERPROCESSNAME.toString(),
                WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())).thenReturn(false);
            
            Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningIdSecond);
            
            Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(Mockito.anyString())).thenReturn(executionQuery);
            
            Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(Mockito.anyString()).list()).thenReturn(executions);
            
            Mockito.when(taskService.createTaskQuery()).thenReturn(taskQuery);
            Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond)).thenReturn(taskQuery);
            Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond).orderByTaskId()).thenReturn(taskQuery);
            Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond).orderByTaskId()
                                    .desc()).thenReturn(taskQuery);
            Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond).orderByTaskId()
                                    .desc().list()).thenReturn(ts);
            Mockito.when(ts.get(0)).thenReturn(task);
            Mockito.when(ts.get(0).getId()).thenReturn("1");
            
            wfmdPCRService.updatedPCRAnalyzerProcess(accessioningIdSecond, "Passed", getActivityProcessDataDTO());
            
           
        }
    
    @Test public void updatedPCRAnalyzerProcessFirstElseCondition()
            throws HMTPException,
            OrderNotFoundException, IOException {
           
            PowerMockito.mockStatic(ThreadSessionManager.class);
            Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
            Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
    		SampleWFMStates rocheWfm1 = new SampleWFMStates();
    		rocheWfm1.setCurrentStatus("ABC");
            Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);
    		
            Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, getActivityProcessDataDTO().getDeviceId(), 1L)).thenReturn(rocheWfm1);
            Mockito.doNothing().when(resultIntegrationService).updatefordPCRAnalyzer(wfmList,  getActivityProcessDataDTO().getWfmESUMessage());
    		
            wfmdPCRService.updatedPCRAnalyzerProcess(accessioningId, "Passed", getActivityProcessDataDTO());
            wfmdPCRService.updatedPCRAnalyzerProcess(accessioningId, "Aborted", getActivityProcessDataDTO());
        }
    
    
    @Test public void updatedPCRAnalyzerProcessElseConditionSecond()
            throws HMTPException,
            OrderNotFoundException, IOException {
           
            PowerMockito.mockStatic(ThreadSessionManager.class);
            Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
            Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
    		SampleWFMStates rocheWfm1 = new SampleWFMStates();
    		rocheWfm1.setCurrentStatus("dPCRORU");
            Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);
    		
            Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, getActivityProcessDataDTO().getDeviceId(), 1L)).thenReturn(rocheWfm1);
            Mockito.doNothing().when(resultIntegrationService).updatefordPCRAnalyzer(wfmList,  getActivityProcessDataDTO().getWfmESUMessage());
    		
            wfmdPCRService.updatedPCRAnalyzerProcess(accessioningId, "InProgress", getActivityProcessDataDTO());
           
        }
    
    
    @Test public void
        startdPCRLp24ProcessNegativeTest() throws UnsupportedEncodingException, HMTPException, OrderNotFoundException {
        String containerId = "83c8a11b_A1";
        String deviceId = "LP001-12";
      
        SampleWFMStates rocheWfm1 = null;
        
        SampleWFMStates rocheWfm = new SampleWFMStates();
        rocheWfm.setProcessId(processingId);
        rocheWfm.setCurrentStatus("AAA");
      Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString(),-1L)).thenReturn(rocheWfm1);
      
      
      Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);
      
      
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
            WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRNAEXTRACTION.toString(),-1L)).thenReturn(rocheWfm);
        
        wfmList.add(getWfmDTO());
       
        Mockito.when(wfmRepository.save(rocheWfm)).thenReturn(rocheWfm);
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24QBP.toString()))
            .thenReturn(executionQuery);
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24QBP.toString()).list())
            .thenReturn(executions);
        
        SampleWFMStates sampleWFM = new SampleWFMStates();
        Mockito.when(wfmWriteRepository.save(sampleWFM)).thenReturn(sampleWFM);
        
        wfmdPCRService.startdPCRLp24Process(containerId, accessioningId, deviceId, getActivityProcessDataDTO());
        
    }
    
    @Test public void
        updatedPCRLp24Process() throws HMTPException, OrderNotFoundException, IOException {   
        SampleWFMStates rocheWfm = new SampleWFMStates();        
        rocheWfm = getrocheWfm();            
        String containerId = "83c8a11b_A1"; 
        String deviceId = "LP001-12";      
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType("8001",
            WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString(),-1L)).thenReturn(rocheWfm);
        Mockito.when(assayIntegrationService.validateAssayProcessStepManual(accessioningId,
            ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString(),
            WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())).thenReturn(true);
        Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningId);
        
        
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24U03.toString()))
            .thenReturn(executionQuery);
        Mockito
            .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
                .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24U03.toString()).list())
            .thenReturn(executions);
        Mockito.when(wfmRepository.save(rocheWfm)).thenReturn(rocheWfm);
        
         
        wfmdPCRService.updatedPCRLp24Process(accessioningId, deviceId, "Passed",
            getActivityProcessDataDTO());
        wfmdPCRService.updatedPCRLp24Process(accessioningId, deviceId, "InProgress",
            getActivityProcessDataDTO());
        wfmdPCRService.updatedPCRLp24Process(accessioningId, deviceId, "Failed",
            getActivityProcessDataDTO());
        wfmdPCRService.updatedPCRLp24Process(accessioningId, deviceId, "flagged",
            getActivityProcessDataDTO());
        wfmdPCRService.updatedPCRLp24Process(accessioningId, deviceId, "NA",
                getActivityProcessDataDTO());
    }
    
    
    @Test public void
    updatedPCRLp24ProcessElseTask() throws HMTPException, OrderNotFoundException, IOException {   
    SampleWFMStates rocheWfm = new SampleWFMStates();        
    rocheWfm = getrocheWfm();            
    String containerId = "83c8a11b_A1"; 
    String deviceId = "LP001-12";      
    Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningIdSecond,
        WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.LP24.toString(),-1L)).thenReturn(rocheWfm);
    Mockito.when(assayIntegrationService.validateAssayProcessStepManual(accessioningIdSecond,
        ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString(),
        WfmConstants.ASSAY_USER_MANUAL_TASK.Y.toString())).thenReturn(false);
    Mockito.doNothing().when(userTaskService).userTaskCheck(accessioningIdSecond);
    
    
    Mockito
        .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
            .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24U03.toString()))
        .thenReturn(executionQuery);
    Mockito
        .when(runtimeService.createExecutionQuery().processInstanceId(processingId)
            .signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.LP24U03.toString()).list())
        .thenReturn(executions);
    Mockito.when(wfmRepository.save(rocheWfm)).thenReturn(rocheWfm);
    
    Mockito.when(taskService.createTaskQuery()).thenReturn(taskQuery);
    Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond)).thenReturn(taskQuery);
    Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond).orderByTaskId()).thenReturn(taskQuery);
    Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond).orderByTaskId()
                            .desc()).thenReturn(taskQuery);
    Mockito.when(taskService.createTaskQuery().taskAssignee(accessioningIdSecond).orderByTaskId()
                            .desc().list()).thenReturn(ts);
    Mockito.when(ts.get(0)).thenReturn(task);
    Mockito.when(ts.get(0).getId()).thenReturn("1");
    wfmdPCRService.updatedPCRLp24Process(accessioningIdSecond, deviceId, "Passed",
        getActivityProcessDataDTO());
    
}
    
    @Test public void
        startMp96Process() throws HMTPException, OrderNotFoundException, IOException {
      
        Mockito
            .when(assayIntegrationService.validateAssayProcessStep(accessioningId,
                ASSAY_PROCESS_STEP_DATA.MP96.toString(), ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()))
            .thenReturn(true);
        
        wfmList.add(getWfmDTO());
        assayList.add(getAssayTypeDTO());
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);
        Mockito.when(assayIntegrationService.findAssayDetail(assayType)).thenReturn(assayList);
        Mockito.when(runtimeService.startProcessInstanceByKey(ASSAY_PROCESS_STEP_DATA.DPCR_WORKFLOW_FILE.toString(),
            accessioningId, variables)).thenReturn(processInstance);
        
        Mockito.doNothing().when(wfmdPCRServiceMock).startProcess("8001", getActivityProcessDataDTO());
        wfmdPCRService.startMp96Process(accessioningId, getActivityProcessDataDTO());
        
    } 
    
	@Test(expectedExceptions=HMTPException.class)
	public void startMp96ProcessNeg() throws HMTPException, OrderNotFoundException, IOException {

		Mockito.when(assayIntegrationService.validateAssayProcessStep(accessioningId,
				ASSAY_PROCESS_STEP_DATA.MP96.toString(), ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()))
				.thenReturn(false);
		wfmdPCRService.startMp96Process(accessioningId, getActivityProcessDataDTO());

	}
    
	@Test
	public void updatedPCRAnalyzerAck() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
		SampleWFMStates rocheWfm = new SampleWFMStates();
		rocheWfm = getrocheWfm();
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
				WfmConstants.WORKFLOW_MESSAGE_TYPE.DPCRANALYZER.toString(), -1l)).thenReturn(rocheWfm);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERACK.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERACK.toString()).list())
				.thenReturn(executions);
		wfmdPCRService.updatedPCRAnalyzerAck(getActivityProcessDataDTO(), accessioningId);
	}
     
	@Test
	public void updatedPCRORUProcess() throws UnsupportedEncodingException, HMTPException, OrderNotFoundException {
		SampleWFMStates rocheWfm = new SampleWFMStates();
		rocheWfm = getrocheWfm();
		long domainId=-1L;
		SampleWFMStates rocheWfm1 = new SampleWFMStates();
		rocheWfm1=getrocheWfm();
		rocheWfm1.setCurrentStatus("dPCRORU");
		String deviceId = "dpcr-12";
		wfmList.add(getWfmDTO());
        Mockito.when(wfmRepository.findOneByAccessioningIdAndDeviceId(accessioningId, deviceId, domainId)).thenReturn(rocheWfm1);
		Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmList);
		Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(accessioningId,
				WfmConstants.ASSAY_PROCESS_STEP_DATA.DPCRANALYZERPROCESSNAME.toString(), -1)).thenReturn(rocheWfm);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERESU.toString()))
				.thenReturn(executionQuery);
		Mockito.when(runtimeService.createExecutionQuery().processInstanceId(processingId)
				.signalEventSubscriptionName(WfmConstants.WORKFLOW_dPCR_SIGNALS.DPCRANALYZERESU.toString()).list())
				.thenReturn(executions);

		long ownerId = 1L;

		Mockito.when(wfmWriteRepository.save(rocheWfm, ownerId)).thenReturn(rocheWfm);

		wfmdPCRService.updatedPCRORUProcess(accessioningId, deviceId, getActivityProcessDataDTO());

	}

    
    public WfmDTO getWfmDTO() {
        WfmDTO wfmDTO = new WfmDTO();
        wfmDTO.setAccessioningId("8001");
        wfmDTO.setAssayType("NIPTDPCR");
        wfmDTO.setOrderId(10000L);
        wfmDTO.setOrderStatus("passed");
        return wfmDTO;
    }
    
    public AssayTypeDTO getAssayTypeDTO() {
        AssayTypeDTO assay = new AssayTypeDTO();
        assay.setAssayType("NIPTDPCR");
        assay.setAssayTypeId(2L);
        assay.setAssayVersion("0.1");
        assay.setWorkflowDefFile("NIPTDPCR");
        return assay;
        
    }
    
    public ActivityProcessDataDTO getActivityProcessDataDTO() {
        ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
        activityProcessData.setAccessioningId("8001");
        activityProcessData.setMessageControlId("1233");
        activityProcessData.setDeviceId("RX-123");
        QueryMessage queryMessage = new QueryMessage();
        queryMessage.setMessageControlId("1321434");
        queryMessage.setSendingApplicationName("dpcrAnalyzer");
        activityProcessData.setQueryMessage(queryMessage);
        WFMQueryMessage wFMQueryMessage = new WFMQueryMessage();
        wFMQueryMessage.setProcessStepName("dPCR");
        activityProcessData.setWfmQueryMessage(wFMQueryMessage);
        WFMESUMessage wfmESUMessage = new WFMESUMessage();
        wfmESUMessage.setMessageType("ESU");
        wfmESUMessage.setAccessioningId("12333");
        wfmESUMessage.setDeviceId("RX-1000");
        wfmESUMessage.setRunResultId("155");
        wfmESUMessage.setProcessStepName("dPCR");
        wfmESUMessage.setStatus("Completed");
        activityProcessData.setWfmESUMessage(wfmESUMessage);
        WFMORUMessage wfmORUMessage = new WFMORUMessage();
        wfmORUMessage.setMessageType("ORU");
        activityProcessData.setoRUMessage(wfmORUMessage);
        return activityProcessData;
    }
    
    public WFMESUMessage getWFMESUMessage() {
        WFMESUMessage wfmESUMessage = new WFMESUMessage();
        wfmESUMessage.setAccessioningId("12333");
        wfmESUMessage.setDeviceId("RX-1000");
        wfmESUMessage.setMessageType("ESU");
        wfmESUMessage.setRunResultId("155");
        wfmESUMessage.setProcessStepName("dPCR");
        wfmESUMessage.setStatus("Completed");
        return wfmESUMessage;
        
    }
    
    public SampleWFMStates getrocheWfm() {
        SampleWFMStates rocheWfm = new SampleWFMStates();
        rocheWfm.setAccessioningId("8001");
        rocheWfm.setDeviceId("MPCZC8380G5K"); 
        rocheWfm.setProcessId("1500");
        rocheWfm.setMessageType("NA Extraction");
        rocheWfm.setCurrentStatus("Query");
        
        Company company=new Company();
        company.setId(1L);
        rocheWfm.setCompany(company);
        
        return rocheWfm;
        
    }
    
    public SampleWFMStates getrocheWfm1() {
        SampleWFMStates rocheWfm = new SampleWFMStates();
        rocheWfm.setAccessioningId("8001");
        rocheWfm.setDeviceId("MPCZC8380G6K");
        rocheWfm.setProcessId("15");
        rocheWfm.setMessageType("NA Extraction");
        rocheWfm.setCurrentStatus("Query");
        
        Company company=new Company();
        company.setId(1L);
        rocheWfm.setCompany(company);
        
        return rocheWfm;
        
    }
    
    
    
    public ProcessStepActionDTO getProcessStepActionDTONegativeFlag() {
    	ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
    	processStepActionDTO.setDeviceType("MP96");
    	processStepActionDTO.setManualVerificationFlag("N");
    	return processStepActionDTO;

    }
    
   
    public ProcessStepActionDTO getProcessStepActionDTO() {
        ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
        processStepActionDTO.setDeviceType("MP96");
        processStepActionDTO.setManualVerificationFlag("Y");
        return processStepActionDTO;
        
    }
    
}
