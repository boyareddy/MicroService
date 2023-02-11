package com.roche.connect.wfm.test.testng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.nipt.htp.wfmtask.forte.UpdateForte;
import com.roche.connect.wfm.nipt.htp.wfmtask.htp.UpdateHTPResult;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.ResultIntegrationService;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class HTPFORTETaskTest {
    
    @InjectMocks UpdateHTPResult updateHTPResult;
    @InjectMocks UpdateForte updateForte;
    
    @Mock HMTPLoggerImpl loggerImpl;
    @Mock DelegateExecution execution;
    @Mock AssayIntegrationService assayIntegrationService;
    @Mock ResultIntegrationService resultIntegrationService;
    
    List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();
    List<WfmDTO> updateOrderDetails = new ArrayList<>();

    @BeforeTest public void setUp() throws HMTPException, OrderNotFoundException, IOException {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        deviceTestOptions.add(getDeviceTestOptionsDTO());
        updateOrderDetails.add(getWfmDTO());
    }
    
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test
    public void updateHTPResultTest() throws HMTPException, IOException, OrderNotFoundException{
        Mockito.when((ActivityProcessDataDTO) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString())).thenReturn(getActivityProcessDataDTO());
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDHTPUPDATE.toString())).thenReturn("HTP01");
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDHTPUPDATE.toString())).thenReturn("8001");
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSHTPUPDATE.toString())).thenReturn("completed");
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDevice("8001",
                    WfmConstants.ASSAY_PROCESS_STEP_DATA.HTP.toString())).thenReturn(deviceTestOptions);
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString())).thenReturn(updateOrderDetails);
        Mockito.doNothing().when(resultIntegrationService).updateHTPStatus(updateOrderDetails, getHtpStatusMessage());
        
        updateHTPResult.execute(execution);
    }
    
    @Test
    public void updateHTPResultNegativeTest() throws HMTPException, IOException{
        Mockito.when((ActivityProcessDataDTO) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString())).thenReturn(null);
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDHTPUPDATE.toString())).thenReturn("HTP01");
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDHTPUPDATE.toString())).thenReturn("8001");
        Mockito.when((String) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSHTPUPDATE.toString())).thenReturn("completed");
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDevice("8001",
                    WfmConstants.ASSAY_PROCESS_STEP_DATA.HTP.toString())).thenReturn(null);
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString())).thenReturn(null);
        Mockito.doNothing().when(resultIntegrationService).updateHTPStatus(updateOrderDetails, getHtpStatusMessage());
        
        updateHTPResult.execute(execution);
    }
    @Test
    public void updateForteTest(){
        updateForte.execute(execution);
    }
    
    public ActivityProcessDataDTO getActivityProcessDataDTO() {
        ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
        activityProcessData.setAccessioningId("8001");
        activityProcessData.setDeviceId("HTP01");
        activityProcessData.setHtpStatusMessage(getHtpStatusMessage());
        
        return activityProcessData;
    }
    
    public HtpStatusMessage getHtpStatusMessage(){
        HtpStatusMessage htpStatusMessage = new HtpStatusMessage();
        htpStatusMessage.setSendingApplication("HTP");
        return htpStatusMessage;
    }
    
    public DeviceTestOptionsDTO getDeviceTestOptionsDTO(){
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("Protocol Name");
        return deviceTestOptionsDTO;
    }
    
    public WfmDTO getWfmDTO(){
        WfmDTO wfmDTO = new WfmDTO();
        wfmDTO.setAccessioningId("8001");
        wfmDTO.setAssayType("NIPTHTP");
        wfmDTO.setOrderId(10000L);
        wfmDTO.setSampleType("Plasma");
        wfmDTO.setOrderStatus("passed");
        return wfmDTO;
    }
    
}
