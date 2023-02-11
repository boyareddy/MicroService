package com.roche.connect.wfm.test.testng;

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
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.mp96.OULSampleResultMessage;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.mp96.GetMp96ACKResponse;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.mp96.GetWorkOrderRequest;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.mp96.QueryValidation;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.mp96.SendMP96WorkOrderImport;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.mp96.SendMp96ACKResponse;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.mp96.UpdateMp96Result;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResultIntegrationService;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" }) public class MP96TaskTest {
    
    @InjectMocks UpdateMp96Result updateMp96Result;
    
    @InjectMocks GetMp96ACKResponse getMp96ACKResponse;
    
    @InjectMocks GetWorkOrderRequest getWorkOrderRequest;
    
    @InjectMocks QueryValidation queryValidation;
    
    @InjectMocks SendMp96ACKResponse sendMp96ACKResponse;
    
    @InjectMocks SendMP96WorkOrderImport sendMP96WorkOrderImport;
    
    @Mock DelegateExecution execution;
    
    @Mock DelegateExecution execution1;
    
    
    
    @Mock HMTPLoggerImpl loggerImpl;
    @Mock  OrderIntegrationService orderIntegrationService;
    @Mock  ResultIntegrationService resultIntegrationService;
    List<WfmDTO> updateBatchDetails = new ArrayList<>();
    
    @BeforeTest
    public void setUp() throws UnsupportedEncodingException, HMTPException {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        Mockito.doNothing().when(loggerImpl).error(Mockito.anyString());
        List<WfmDTO> wfmList =  new ArrayList<>();
        wfmList.add(getWfmDTO());
        updateBatchDetails.add(getWfmDTO());
        WFMoULMessage U03RequestMessage = getWFMoULMessage();
        Mockito.when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString())).thenReturn(getActivityProcessDataDTO());
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUS.toString())).thenReturn("Passed");
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILS.toString())).thenReturn(wfmList);
        Mockito.when(orderIntegrationService.updateOrders(updateBatchDetails.get(0))).thenReturn(true);
        Mockito.doNothing().when(resultIntegrationService).updateforMP96Status(updateBatchDetails, U03RequestMessage); 
    }
    
    /** Mandatory to mock static classes **/
    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test public void queryValidationTest() {
        queryValidation.execute(execution);
    }
    
    @Test public void execute() {
        updateMp96Result.execute(execution);
    }
    
    @Test public void executeElse() {
    	 Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUS.toString())).thenReturn("Failed");
        updateMp96Result.execute(execution); 
    }
    
    
    @Test public void executeElseSecond() {
   	 Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUS.toString())).thenReturn("InProgress");
       updateMp96Result.execute(execution);
   }
    
   
    
	@Test
	public void updateMp96ResultNegCase() throws HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.when((ActivityProcessDataDTO) execution.getVariable(Mockito.anyString())).thenReturn(null);
		updateMp96Result.execute(execution);

	}
    
    @Test public void getMp96ACKResponse() {
        getMp96ACKResponse.execute(execution);
    }
    
    @Test public void getWorkOrderRequest() {
        getWorkOrderRequest.execute(execution);
    }
    
    @Test public void sendMp96ACKResponse() {
        sendMp96ACKResponse.execute(execution);
    }
    
    @Test public void sendMP96WorkOrderImport() {
        sendMP96WorkOrderImport.execute(execution);
    }
    
	@Test
	public void sendMP96WorkOrderImportNegCase() throws HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.when((ActivityProcessDataDTO) execution.getVariable(Mockito.anyString())).thenReturn(null);
			sendMP96WorkOrderImport.execute(execution);


	}
	

    public WfmDTO getWfmDTO(){
        WfmDTO wfmDTO = new WfmDTO();
        wfmDTO.setAccessioningId("8001");
        wfmDTO.setAssayType("NIPTDPCR");
        wfmDTO.setOrderId(10000L);
        wfmDTO.setOrderStatus("passed");
        return wfmDTO;
    }
    
    public ActivityProcessDataDTO getActivityProcessDataDTO() {
        ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
        activityProcessData.setAccessioningId("8001");
        activityProcessData.setMessageControlId("1233");
        activityProcessData.setDeviceId("MP0001");
        activityProcessData.setMessageType("NA Extraction");
        activityProcessData.setOrderDetails(updateBatchDetails);
        
        
        QueryMessage queryMessage = new QueryMessage();
        queryMessage.setMessageControlId("1321434");
        
        activityProcessData.setQueryMessage(queryMessage);
        activityProcessData.setU03RequestMessage(getWFMoULMessage());
        return activityProcessData;
    }
    
    public WFMoULMessage getWFMoULMessage() {
        WFMoULMessage U03RequestMessage = new WFMoULMessage();
        U03RequestMessage.setSendingApplicationName("MagnaPure96");
        U03RequestMessage.setOulSampleResultMessage(getOulSampleResultMessage());
        return U03RequestMessage;
    }
    
    public OULSampleResultMessage getOulSampleResultMessage() {
        OULSampleResultMessage oulSampleResultMessage = new OULSampleResultMessage();
        oulSampleResultMessage.setOutputContainerId("755121");
        oulSampleResultMessage.setPosition("A2");
        return oulSampleResultMessage;
    }
}
