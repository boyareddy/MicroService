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
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_VARIABLES;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.postpcr.FindOrderBy96WellPlateId;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.postpcr.SendLP24QBPResponse;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.postpcr.SendLP24U03Response;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.postpcr.UpdateLP24Result;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.ImmIntegrationService;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;

// @PrepareForTest({AssayIntegrationService.class,ResultIntegrationService.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" }) public class LP24PostPcrTaskTest {
    
    @InjectMocks FindOrderBy96WellPlateId findOrderBy96WellPlateId;
    @InjectMocks SendLP24QBPResponse sendLP24QBPResponse;
    @InjectMocks UpdateLP24Result updateLP24Result;
    @InjectMocks SendLP24U03Response sendLP24U03Response;
    
    @Mock DelegateExecution execution;
    @Mock HMTPLoggerImpl loggerImpl;
    @Mock ResponseRenderingService responseRenderingService;
    @Mock AssayIntegrationService assayIntegrationService;
    @Mock ResultIntegrationService resultIntegrationService;
    @Mock ImmIntegrationService immIntegrationService;
    
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
        findOrderBy96WellPlateId.execute(execution);
    }
    
    @Test(priority = 2) public void sendLP24QBPResponsePositiveTest() {
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(getActivityProcessDataDTO());
        sendLP24QBPResponse.execute(execution);
    }
    
    @Test(priority = 3) public void sendLP24QBPResponseNegativeTest() {
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString())).thenReturn(null);
        sendLP24QBPResponse.execute(execution);
    }
    
    @Test(priority = 4) public void updateLP24PostPcrResultPositiveTest() throws HMTPException, IOException {
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(getActivityProcessDataDTO());
        Mockito
            .when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPOSTPCRUPDATE.toString()))
            .thenReturn(WORKFLOW_STATUS.PASSED.toString());
        Mockito
            .when(
                (String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPOSTPCRUPDATE.toString()))
            .thenReturn(getActivityProcessDataDTO().getAccessioningId());
        Mockito.when((String) execution.getVariable(WORKFLOW_VARIABLES.ASSAYTYPE.toString()))
            .thenReturn(getWfmDTO().getAssayType());
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            getActivityProcessDataDTO().getAccessioningId(),
            getSpecimenStatusUpdateMessage().getSendingApplicationName(),
            getSpecimenStatusUpdateMessage().getProcessStepName())).thenReturn(deviceTestOptions);
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPOSTPCRUPDATE.toString()))
            .thenReturn(updateBatchDetails);
        Mockito.doNothing().when(resultIntegrationService).updateforLP24(updateBatchDetails,
            getSpecimenStatusUpdateMessage());
        updateLP24Result.execute(execution);
    }
    
    @Test(priority = 5) public void updateLP24PostPcrResultNegativeTest() throws HMTPException, IOException {
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(getActivityProcessDataDTO());
        Mockito
            .when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPOSTPCRUPDATE.toString()))
            .thenReturn(WORKFLOW_STATUS.ABORTED.toString());
        Mockito
            .when(
                (String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPOSTPCRUPDATE.toString()))
            .thenReturn(getActivityProcessDataDTO().getAccessioningId());
        Mockito.when((String) execution.getVariable(WORKFLOW_VARIABLES.ASSAYTYPE.toString()))
            .thenReturn(getWfmDTO().getAssayType());
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            getActivityProcessDataDTO().getAccessioningId(),
            getSpecimenStatusUpdateMessage().getSendingApplicationName(),
            getSpecimenStatusUpdateMessage().getProcessStepName())).thenReturn(deviceTestOptions);
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPOSTPCRUPDATE.toString()))
            .thenReturn(updateBatchDetails);
        Mockito.doNothing().when(resultIntegrationService).updateforLP24(updateBatchDetails,
            getSpecimenStatusUpdateMessage());
        updateLP24Result.execute(execution);
    }
    
    @Test(priority = 6) public void updateLP24PostPcrResultNegativeTest1() throws HMTPException, IOException {
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(getActivityProcessDataDTO());
        Mockito
            .when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPOSTPCRUPDATE.toString()))
            .thenReturn(WORKFLOW_STATUS.INPROGRESS.toString());
        Mockito
            .when(
                (String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPOSTPCRUPDATE.toString()))
            .thenReturn(getActivityProcessDataDTO().getAccessioningId());
        Mockito.when((String) execution.getVariable(WORKFLOW_VARIABLES.ASSAYTYPE.toString()))
            .thenReturn(getWfmDTO().getAssayType());
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            getActivityProcessDataDTO().getAccessioningId(),
            getSpecimenStatusUpdateMessage().getSendingApplicationName(),
            getSpecimenStatusUpdateMessage().getProcessStepName())).thenReturn(deviceTestOptions);
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPOSTPCRUPDATE.toString()))
            .thenReturn(updateBatchDetails);
        Mockito.doNothing().when(resultIntegrationService).updateforLP24(updateBatchDetails,
            getSpecimenStatusUpdateMessage());
        updateLP24Result.execute(execution);
    }
    
    @Test(priority = 7) public void updateLP24PostPcrResultNegativeTest2() throws HMTPException, IOException {
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(null);
        updateLP24Result.execute(execution);
    }
    
   @Test(priority = 8) public void sendLP24U03ResponsePositiveTest() throws HMTPException {
        Mockito
            .when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
            .thenReturn(getActivityProcessDataDTO());
        Mockito.when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString()))
            .thenReturn(getActivityProcessDataDTO().getDeviceId());
        Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(getActivityProcessDataDTO().getDeviceId(), getActivityProcessDataDTO());
        sendLP24U03Response.execute(execution);
    }
    
    @Test(priority = 9) public void sendLP24U03ResponseNegativeTest() throws HMTPException {
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString())).thenReturn(null);
        Mockito.when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString()))
            .thenReturn(getActivityProcessDataDTO().getDeviceId());
        Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(getActivityProcessDataDTO().getDeviceId(), getActivityProcessDataDTO());
        sendLP24U03Response.execute(execution);
    }
    
    public ActivityProcessDataDTO getActivityProcessDataDTO() {
        ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
        activityProcessData.setAccessioningId("8001");
        activityProcessData.setDeviceId("LP001-477-Post");
        activityProcessData.setMessageType("QueryMessage");
        activityProcessData.setContainerId("133455757");
        activityProcessData.setOrderDetails(updateBatchDetails);
        activityProcessData.setSpecimenStatusUpdateMessage(getSpecimenStatusUpdateMessage());
        
        return activityProcessData;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessage() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        specimenStatusUpdateMessage.setSendingApplicationName("LP24");
        specimenStatusUpdateMessage.setProcessStepName("LP Post PCR");
        specimenStatusUpdateMessage.setContainerId("133455757");
        specimenStatusUpdateMessage.setMessageControlId("1321434");
        specimenStatusUpdateMessage.setStatusUpdate(getStatusUpdate());
        return specimenStatusUpdateMessage;
    }
    
    public DeviceTestOptionsDTO getDeviceTestOptionsDTO() {
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("Protocol Name");
        return deviceTestOptionsDTO;
    }
    
    public AcknowledgementMessage getAcknowledgementMessage() {
        AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
        
        return acknowledgementMessage;
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
