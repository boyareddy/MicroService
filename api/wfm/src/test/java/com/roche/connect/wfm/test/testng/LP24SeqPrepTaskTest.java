package com.roche.connect.wfm.test.testng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.seq.FindOrderForSeqPrep;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.seq.UpdateLP24SeqPrepResult;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" }) public class LP24SeqPrepTaskTest {
    
    @InjectMocks FindOrderForSeqPrep findOrderForSeqPrep;
    @InjectMocks UpdateLP24SeqPrepResult updateLP24SeqPrepResult;
    
    @Mock DelegateExecution execution;
    @Mock HMTPLoggerImpl loggerImpl;
    @Mock ResponseRenderingService responseRenderingService;
    @Mock AssayIntegrationService assayIntegrationService;
    @Mock ResultIntegrationService resultIntegrationService;
    
    List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();
    List<WfmDTO> updateBatchDetails = new ArrayList<>();
    
    @BeforeTest public void setUp() throws HMTPException, OrderNotFoundException, IOException {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        updateBatchDetails.add(getWfmDTO());
        deviceTestOptions.add(getDeviceTestOptionsDTO());       
        Mockito.doNothing().when(responseRenderingService).duplicateOrderQueryPositiveResponseforLp24(
            getActivityProcessDataDTO().getContainerId(), getActivityProcessDataDTO().getDeviceId(),
            getActivityProcessDataDTO().getAccessioningId(), getActivityProcessDataDTO());
        
    }
    
    /** Mandatory to mock static classes **/
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test(priority = 1) public void findOrderBy96WellPlateIdTest() {
        findOrderForSeqPrep.execute(execution);
    }
    
    @Test(priority = 2) public void UpdateLP24SeqPrepResultTest() throws HMTPException, IOException {
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(getActivityProcessDataDTO());
        Mockito.when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSSEQPREUPDATE.toString()))
            .thenReturn(WORKFLOW_STATUS.PASSED.toString());
        Mockito
            .when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDSEQPREUPDATE.toString()))
            .thenReturn(getActivityProcessDataDTO().getAccessioningId());
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            getActivityProcessDataDTO().getAccessioningId(),
            getSpecimenStatusUpdateMessage().getSendingApplicationName(),
            getSpecimenStatusUpdateMessage().getProcessStepName())).thenReturn(deviceTestOptions);
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSSEQPREPUPDATE.toString()))
            .thenReturn(updateBatchDetails);
        Mockito.doNothing().when(resultIntegrationService).updateforLP24(updateBatchDetails,
            getSpecimenStatusUpdateMessage());
        updateLP24SeqPrepResult.execute(execution);
        
    }
    
    @Test(priority = 3) public void updateLP24PostPcrResultNegativeTest() throws HMTPException, IOException {
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(getActivityProcessDataDTO());
        Mockito.when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSSEQPREUPDATE.toString()))
            .thenReturn(WORKFLOW_STATUS.ABORTED.toString());
        Mockito
            .when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDSEQPREUPDATE.toString()))
            .thenReturn(getActivityProcessDataDTO().getAccessioningId());
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            getActivityProcessDataDTO().getAccessioningId(),
            getSpecimenStatusUpdateMessage().getSendingApplicationName(),
            getSpecimenStatusUpdateMessage().getProcessStepName())).thenReturn(deviceTestOptions);
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSSEQPREPUPDATE.toString()))
            .thenReturn(updateBatchDetails);
        Mockito.doNothing().when(resultIntegrationService).updateforLP24(updateBatchDetails,
            getSpecimenStatusUpdateMessage());
        updateLP24SeqPrepResult.execute(execution);
    }
    
    @Test(priority = 4) public void updateLP24PostPcrResultNegativeTest1() throws HMTPException, IOException {
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(getActivityProcessDataDTO());
        Mockito.when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSSEQPREUPDATE.toString()))
            .thenReturn(WORKFLOW_STATUS.INPROGRESS.toString());
        Mockito
            .when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDSEQPREUPDATE.toString()))
            .thenReturn(getActivityProcessDataDTO().getAccessioningId());
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            getActivityProcessDataDTO().getAccessioningId(),
            getSpecimenStatusUpdateMessage().getSendingApplicationName(),
            getSpecimenStatusUpdateMessage().getProcessStepName())).thenReturn(deviceTestOptions);
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSSEQPREPUPDATE.toString()))
            .thenReturn(updateBatchDetails);
        Mockito.doNothing().when(resultIntegrationService).updateforLP24(updateBatchDetails,
            getSpecimenStatusUpdateMessage());
        updateLP24SeqPrepResult.execute(execution);
    }
    
    @Test(priority = 5) public void updateLP24PostPcrResultNegativeTest2() throws HMTPException, IOException {
    	deviceTestOptions.get(0).setTestProtocol(null);
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(getActivityProcessDataDTO());
        Mockito.when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSSEQPREUPDATE.toString()))
            .thenReturn(WORKFLOW_STATUS.ABORTED.toString());
        Mockito
            .when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDSEQPREUPDATE.toString()))
            .thenReturn(getActivityProcessDataDTO().getAccessioningId());
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            getActivityProcessDataDTO().getAccessioningId(),
            getSpecimenStatusUpdateMessage().getSendingApplicationName(),
            getSpecimenStatusUpdateMessage().getProcessStepName())).thenReturn(deviceTestOptions);
       
        updateLP24SeqPrepResult.execute(execution);
    }
    
    @Test(priority = 6) public void updateLP24PostPcrResultNegativeTest3() throws HMTPException, IOException {
    	deviceTestOptions.get(0).setTestProtocol(null);
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(null);
        updateLP24SeqPrepResult.execute(execution);
    }
    
    public ActivityProcessDataDTO getActivityProcessDataDTO() {
        ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
        activityProcessData.setAccessioningId("8001");
        activityProcessData.setDeviceId("LP001-926-SeqPrep");
        activityProcessData.setMessageType("QueryMessage");
        activityProcessData.setContainerId("133455757");
        activityProcessData.setOrderDetails(updateBatchDetails);
        activityProcessData.setSpecimenStatusUpdateMessage(getSpecimenStatusUpdateMessage());
        
        return activityProcessData;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessage() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        specimenStatusUpdateMessage.setSendingApplicationName("LP24");
        specimenStatusUpdateMessage.setProcessStepName("LP Sequencing Prep");
        specimenStatusUpdateMessage.setContainerId("133455757");
        specimenStatusUpdateMessage.setMessageControlId("1321434");
        specimenStatusUpdateMessage.setStatusUpdate(getStatusUpdate());
        return specimenStatusUpdateMessage;
    }
    
    public DeviceTestOptionsDTO getDeviceTestOptionsDTO() {
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("Protocol Name");
        deviceTestOptionsDTO.setDeviceType("LPSEQPREP");
        deviceTestOptionsDTO.setTestName("Sample Protocol");
        return deviceTestOptionsDTO;
    }
    
    public StatusUpdate getStatusUpdate() {
        StatusUpdate statusUpdate = new StatusUpdate();
        return statusUpdate;
    }
    
    public WfmDTO getWfmDTO() {
        WfmDTO wfmDTO = new WfmDTO();
        wfmDTO.setAssayType("NIPTHTP");
        return wfmDTO;
    }
}
