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
import com.roche.connect.common.amm.dto.MolecularIDTypeDTO;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.ContainerInfo;
import com.roche.connect.common.lp24.SampleInfo;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_VARIABLES;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.prepcr.FindOrderBy8TubeId;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.prepcr.SendLP24PrePcrU03Response;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.prepcr.SendLP24QueryResponse;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.prepcr.UpdateLP24PrePcrResult;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.ImmIntegrationService;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class LP24PrePcrTaskTest {
    
    @InjectMocks FindOrderBy8TubeId findOrderBy8TubeId;      
    @InjectMocks SendLP24QueryResponse sendLP24QueryResponse;
    @InjectMocks UpdateLP24PrePcrResult updateLP24PrePcrResult;
    @InjectMocks SendLP24PrePcrU03Response sendLP24PrePcrU03Response;
    
    @Mock DelegateExecution execution;
    @Mock HMTPLoggerImpl loggerImpl;
    @Mock ResponseRenderingService responseRenderingService;
    @Mock AssayIntegrationService assayIntegrationService;
    @Mock ResultIntegrationService resultIntegrationService;
    @Mock ImmIntegrationService immIntegrationService;
    
    List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();
    List<MolecularIDTypeDTO> molicularDetails = new ArrayList<>();
    List<WfmDTO> updateBatchDetails = new ArrayList<>();
    
    @BeforeTest public void setUp() throws HMTPException, OrderNotFoundException, IOException {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        updateBatchDetails.add(getWfmDTO());
        deviceTestOptions.add(getDeviceTestOptionsDTO());
        molicularDetails.add(getMolecularIDTypeDTO());
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString())).thenReturn("8001");
        Mockito.when((String) execution.getVariable(WORKFLOW_VARIABLES.ASSAYTYPE.toString())).thenReturn("NIPTHTP");
        Mockito.doNothing().when(responseRenderingService).duplicateOrderQueryPositiveResponseforLp24("133455757","MP0001","8000",getActivityProcessDataDTO());

    }
    
    /** Mandatory to mock static classes **/
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test(priority = 1) public void findOrderBy8TubeIdTest(){
        findOrderBy8TubeId.execute(execution);
    }
    
    @Test(priority = 2) public void sendLP24QueryResponsePositiveTest(){
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(getActivityProcessDataDTO());
        sendLP24QueryResponse.execute(execution);        
    }
    
    @Test(priority = 3) public void sendLP24QueryResponseNegativeTest(){
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(null);
        sendLP24QueryResponse.execute(execution);        
    }
    
    @Test(priority = 4) public void updateLP24PrePcrResultPositiveTest() throws HMTPException, IOException{
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(getActivityProcessDataDTO());
         Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDPREPCRUPDATE.toString())).thenReturn(getActivityProcessDataDTO().getAccessioningId());
        Mockito.when((String) execution.getVariable(WORKFLOW_VARIABLES.ASSAYTYPE.toString())).thenReturn(getWfmDTO().getAssayType());
        Mockito.when(assayIntegrationService
                        .getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(getWfmDTO().getAssayType(), getContainerInfo().getOutputPlateType(), getSampleInfo().getNewOutputPosition())).thenReturn(molicularDetails);
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
            getActivityProcessDataDTO().getAccessioningId(), getSpecimenStatusUpdateMessage().getSendingApplicationName(), getSpecimenStatusUpdateMessage().getProcessStepName())).thenReturn(deviceTestOptions);
        Mockito.when(execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSPREPCRUPDATE.toString())).thenReturn(updateBatchDetails);
        Mockito.doNothing().when(resultIntegrationService).updateforLP24(updateBatchDetails, getSpecimenStatusUpdateMessage());
        Mockito.when((String) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString())).thenReturn(WORKFLOW_STATUS.PASSED.toString());
        updateLP24PrePcrResult.execute(execution);
        Mockito.when((String) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString())).thenReturn(WORKFLOW_STATUS.INPROGRESS.toString());
        updateLP24PrePcrResult.execute(execution);
        Mockito.when((String) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString())).thenReturn(WORKFLOW_STATUS.ABORTED.toString());
        updateLP24PrePcrResult.execute(execution);
    }
    
    @Test(priority = 5) public void updateLP24PrePcrResultNegativeTest(){
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(null);
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString())).thenReturn(WfmConstants.ORDER_STATUS.ABORTED.toString());
        updateLP24PrePcrResult.execute(execution);        
    }
    
    @Test(priority = 6) public void updateLP24PrePcrResultNegativeTest1(){
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(null);
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSPREPCRUPDATE.toString())).thenReturn(WORKFLOW_STATUS.INPROGRESS.toString());
        updateLP24PrePcrResult.execute(execution);        
    }
    
    @Test(priority = 7) public void SendLP24PrePcrU03ResponsePositiveTest() throws HMTPException{
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(getActivityProcessDataDTO());
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString())).thenReturn(getActivityProcessDataDTO().getDeviceId());
        Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(getActivityProcessDataDTO().getDeviceId(), getActivityProcessDataDTO());
        sendLP24PrePcrU03Response.execute(execution);
    }
     @Test(priority = 8) public void SendLP24PrePcrU03ResponseNegativeTest() throws HMTPException{
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(null);
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDPREPCRUPDATE.toString())).thenReturn(getActivityProcessDataDTO().getDeviceId());
        Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24(getActivityProcessDataDTO().getDeviceId(), getActivityProcessDataDTO());
        sendLP24PrePcrU03Response.execute(execution);
    }
    
    public ActivityProcessDataDTO getActivityProcessDataDTO() {
        ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
        activityProcessData.setAccessioningId("8001");
        activityProcessData.setDeviceId("MP0001");
        activityProcessData.setMessageType("NA Extraction");
        activityProcessData.setContainerId("133455757");
        activityProcessData.setOrderDetails(updateBatchDetails);
        activityProcessData.setSpecimenStatusUpdateMessage(getSpecimenStatusUpdateMessage());
                
        return activityProcessData;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessage(){
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage= new SpecimenStatusUpdateMessage();
        specimenStatusUpdateMessage.setSendingApplicationName("LP24");
        specimenStatusUpdateMessage.setProcessStepName("LP Pre PCR");
        specimenStatusUpdateMessage.setContainerId("133455757");
        specimenStatusUpdateMessage.setMessageControlId("1321434");
        specimenStatusUpdateMessage.setStatusUpdate(getStatusUpdate());
        return specimenStatusUpdateMessage;
    }
    
    public DeviceTestOptionsDTO getDeviceTestOptionsDTO(){
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("Protocol Name");
        return deviceTestOptionsDTO;
    }
    
    public AcknowledgementMessage getAcknowledgementMessage(){
        AcknowledgementMessage acknowledgementMessage= new AcknowledgementMessage();
        
        return acknowledgementMessage;
    }
    
    public StatusUpdate getStatusUpdate(){
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setContainerInfo(getContainerInfo());
        statusUpdate.setSampleInfo(getSampleInfo());
        return statusUpdate;
    }
    public ContainerInfo getContainerInfo(){
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setOutputPlateType("A");
        return containerInfo;
    }
    public SampleInfo getSampleInfo(){
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setNewOutputPosition("C1");
        return sampleInfo;
    }
    
    public WfmDTO getWfmDTO(){
        WfmDTO wfmDTO = new WfmDTO();
        wfmDTO.setAssayType("NIPTHTP");
        return wfmDTO;
    }
    
    public MolecularIDTypeDTO getMolecularIDTypeDTO(){
        MolecularIDTypeDTO molecularIDTypeDTO = new MolecularIDTypeDTO();
         return molecularIDTypeDTO;
    }
    
}
