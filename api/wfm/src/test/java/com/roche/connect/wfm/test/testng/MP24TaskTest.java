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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.nipt.htp.wfmtask.mp24.GetOrder;
import com.roche.connect.wfm.nipt.htp.wfmtask.mp24.QueryValidation;
import com.roche.connect.wfm.nipt.htp.wfmtask.mp24.SendMP24QBPResponse;
import com.roche.connect.wfm.nipt.htp.wfmtask.mp24.SendMp24ACKResponse;
import com.roche.connect.wfm.nipt.htp.wfmtask.mp24.UpdateMp24Result;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;

@PrepareForTest({AssayIntegrationService.class,OrderIntegrationService.class,HMTPException.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" }) public class MP24TaskTest {
    
    @InjectMocks QueryValidation queryValidation;
    @InjectMocks GetOrder getOrder;
    @InjectMocks SendMP24QBPResponse sendMP24QBPResponse;
    @InjectMocks UpdateMp24Result updateMp24Result;
    @InjectMocks SendMp24ACKResponse sendMp24ACKResponse;
    
    @Mock HMTPLoggerImpl loggerImpl;
    @Mock DelegateExecution execution;
    @Mock ResponseRenderingService responseRenderingService;
    @Mock AssayIntegrationService assayIntegrationService;
    @Mock OrderIntegrationService orderIntegrationService;
    @Mock ResultIntegrationService resultIntegrationService ;
    
    List<WfmDTO> updateBatchDetails = new ArrayList<>();
    List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();

    
    @BeforeTest public void setUp() throws HMTPException, OrderNotFoundException, IOException {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        updateBatchDetails.add(getWfmDTO());
        deviceTestOptions.add(getDeviceTestOptionsDTO());
        Mockito.doNothing().when(responseRenderingService).duplicateOrderResponseforMP("8000","MP0001",getadaptorRequestMessage());
        Mockito.doNothing().when(resultIntegrationService).updateSampleResults(updateBatchDetails, getadaptorRequestMessage());
    }
    
    /** Mandatory to mock static classes **/
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test(priority =1) public void queryValidationTest() {
        queryValidation.execute(execution);
    }
    
    @Test(priority =2) public void getOrderTest() {
        getOrder.execute(execution);
    }
    
    @Test(priority =3) public void sendMP24QBPResponsePositiveTest() {
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(getActivityProcessDataDTO());
        sendMP24QBPResponse.execute(execution);
    }
    @Test(priority =4) public void sendMP24QBPResponseNegativeTest() {
        Mockito
        .when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(null);
        sendMP24QBPResponse.execute(execution);
    }
    
    @Test(priority =5) public void updateMp24ResultPositiveTest() throws Exception{
    	List<WfmDTO> updateBatchDetails =new ArrayList<>();
    	WfmDTO wfmDTO=new WfmDTO();
    	wfmDTO.setAccessioningId("123");
    	updateBatchDetails.add(wfmDTO);
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(getActivityProcessDataDTOWithOrderDetails());
        Mockito.when(orderIntegrationService.updateOrders(getActivityProcessDataDTO().getOrderDetails().get(0))).thenReturn(true);
        
        Mockito.when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.PROTOCAL.toString())).thenReturn("PROTOCAL");
        Mockito.doNothing().when(execution).setVariable(WfmConstants.WORKFLOW_VARIABLES.PROTOCOLMP24.toString(), "PROTOCAL");
        Mockito.doNothing().when(execution).setVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString(), getActivityProcessDataDTOWithOrderDetails().getOrderDetails());
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString())).thenReturn(updateBatchDetails);
        Mockito.doNothing().when(resultIntegrationService).updateMP24Results(updateBatchDetails, getActivityProcessDataDTOWithOrderDetails().getAdaptorRequestMessage());
        updateMp24Result.execute(execution);
    }
    
  
    
    @Test(priority =6) public void updateMp24ResultNegativeTest() throws Exception{
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(null);
        try {
        updateMp24Result.execute(execution);
        }catch(Exception e) {
        	
        }
        
    }
    
    
    @Test(priority =7) public void updateMp24ResultNegativeSecondTest() throws HMTPException, UnsupportedEncodingException{
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(getActivityProcessDataDTOWithOrderDetails());
        Mockito.when(orderIntegrationService.updateOrders(getActivityProcessDataDTOWithOrderDetails().getOrderDetails().get(0))).thenThrow(UnsupportedEncodingException.class);
        updateMp24Result.execute(execution);
    }
    
    
    @Test(priority =8) public void updateMp24ResultNegativeThirdTest() throws Exception{
    	
    	ActivityProcessDataDTO activityProcess = getActivityProcessDataDTO();
		activityProcess.setOrderDetails(null);
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(activityProcess);
        try {
        updateMp24Result.execute(execution);
        }catch(Exception e) {
        	
        }
    }
    
    @Test(priority =9) public void sendMp24ACKResponsePositiveTest() throws HMTPException{
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(getActivityProcessDataDTO());   
        Mockito.doNothing().when(responseRenderingService).duplicateACKForMP24("8001", "MP0001",getActivityProcessDataDTO());
        sendMp24ACKResponse.execute(execution);
    }
     @Test(priority =10) public void sendMp24ACKResponseNegativeTest(){
        Mockito.when((ActivityProcessDataDTO) execution
            .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString()))
        .thenReturn(null);        
        sendMp24ACKResponse.execute(execution);
    }
     
     public ActivityProcessDataDTO getActivityProcessDataDTOWithOrderDetails() {
         ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
         List<WfmDTO> orderDetails = new ArrayList<>();
         WfmDTO wfmDTO=new WfmDTO();
         wfmDTO.setSampleType("MP24");
         orderDetails.add(wfmDTO);
         activityProcessData.setAccessioningId("8001");
         activityProcessData.setDeviceId("MP0001");
         activityProcessData.setMessageType("NA Extraction");
         activityProcessData.setOrderDetails(updateBatchDetails);
         activityProcessData.setAdaptorRequestMessage(getadaptorRequestMessage());
         activityProcessData.setOrderDetails(orderDetails);
         
         QueryMessage queryMessage = new QueryMessage();
         queryMessage.setMessageControlId("1321434");
         
         return activityProcessData;
     }
     
     
    public ActivityProcessDataDTO getActivityProcessDataDTO() {
        ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
        activityProcessData.setAccessioningId("8001");
        activityProcessData.setDeviceId("MP0001");
        activityProcessData.setMessageType("NA Extraction");
        activityProcessData.setOrderDetails(updateBatchDetails);
        activityProcessData.setAdaptorRequestMessage(getadaptorRequestMessage());
        
        QueryMessage queryMessage = new QueryMessage();
        queryMessage.setMessageControlId("1321434");
        
        return activityProcessData;
    }
    
    public AdaptorRequestMessage getadaptorRequestMessage() {
        AdaptorRequestMessage adaptorRequestMessage = new AdaptorRequestMessage();
        adaptorRequestMessage.setAccessioningId("8001");
        adaptorRequestMessage.setDateTimeMessageGenerated("20181218200310");
        adaptorRequestMessage.setMessageControlId("06c46bdb-3202-4817-b");
        
        return adaptorRequestMessage;
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
    public DeviceTestOptionsDTO getDeviceTestOptionsDTO(){
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("Protocol Name");
        return deviceTestOptionsDTO;
    }
    
}
