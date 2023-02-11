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
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMORUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMQueryMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.dpcranalyzer.GetFinalRunResultORU;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.dpcranalyzer.GetFinalRunResultsESU;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.dpcranalyzer.GetRunStatusUpdate;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.dpcranalyzer.GetSampleSetupACKMessage;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.dpcranalyzer.GetSampleSetupQBPMessage;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.dpcranalyzer.SendRunResultACK;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.dpcranalyzer.SenddPCRAnalyzerACKResponse;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.dpcranalyzer.SenddPCRAnalyzerOMLResponse;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResultIntegrationService;

@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" }) public class DpcrAnalyzerTaskTest {
    
    @InjectMocks GetSampleSetupQBPMessage getSampleSetupQBPMessage;
    
    @InjectMocks SenddPCRAnalyzerACKResponse senddPCRAnalyzerACKResponse;
    
    @InjectMocks SenddPCRAnalyzerOMLResponse senddPCRAnalyzerOMLResponse;
    
    @InjectMocks GetSampleSetupACKMessage getSampleSetupACKMessage;
    
    @InjectMocks GetRunStatusUpdate getRunStatusUpdate;
    
    @InjectMocks SendRunResultACK sendRunResultACK;
    
    @InjectMocks GetFinalRunResultORU getFinalRunResultORU;
    
    @InjectMocks GetFinalRunResultsESU getFinalRunResultsESU;
    
    @Mock DelegateExecution execution;
    
    @Mock HMTPLoggerImpl loggerImpl;
    @Mock  OrderIntegrationService orderIntegrationService;
    @Mock  ResultIntegrationService resultIntegrationService;
    List<WfmDTO> updateBatchDetails = new ArrayList<>();
    
    @BeforeTest
    public void setUp() throws UnsupportedEncodingException, HMTPException {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        List<WfmDTO> wfmList =  new ArrayList<>();
        wfmList.add(getWfmDTO());
        updateBatchDetails.add(getWfmDTO());
        Mockito.when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString())).thenReturn(getActivityProcessDataDTO());
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRANALYZERUPDATE.toString())).thenReturn(wfmList);
        
        Mockito.when((String) execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRANALYZERSTATUS.toString())).thenReturn("InProgress");
        
        Mockito.doNothing().when(resultIntegrationService).updatefordPCRAnalyzer(updateBatchDetails, geWFMESUMessage()); 
        Mockito.doNothing().when(resultIntegrationService).updateforDPCRAnalyzerORU(updateBatchDetails, getWFMORUMessage());
    }
    
    /** Mandatory to mock static classes **/
    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test (priority = 1) public void senddPCRAnalyzerACKResponse() {
        senddPCRAnalyzerACKResponse.execute(execution);
    }
    
    @Test (priority = 2) public void senddPCRAnalyzerOMLResponse() {
        senddPCRAnalyzerOMLResponse.execute(execution);
    }
    
    @Test (priority = 3) public void getSampleSetupACKMessage() {
        getSampleSetupACKMessage.execute(execution);
    }
    
    @Test (priority = 4) public void sendRunResultACK() {
        sendRunResultACK.execute(execution);
    }
    
    @Test (priority = 5) public void getSampleSetupQBPMessage() {
        getSampleSetupQBPMessage.execute(execution);
    }
    
    @Test (priority = 6)
    public void getRunStatusUpdate() {
        getRunStatusUpdate.execute(execution);
    }
    
    @Test (priority = 7) public void getFinalRunResultORU() {
        getFinalRunResultORU.execute(execution);
    }
    
    @Test (priority = 8) public void getFinalRunResultsESU() {
        getFinalRunResultsESU.execute(execution); 
    }
    
    
    @Test (priority = 9) public void getFinalRunResultsESUForNeg() {
    	MockitoAnnotations.initMocks(this);
   		Mockito.when((ActivityProcessDataDTO) execution.getVariable(Mockito.anyString())).thenReturn(null);
   		getFinalRunResultsESU.execute(execution);
   		getRunStatusUpdate.execute(execution);
   		getFinalRunResultORU.execute(execution);
    }
    
    public WfmDTO getWfmDTO(){
        WfmDTO wfmDTO = new WfmDTO();
        wfmDTO.setOrderStatus(WfmConstants.WORKFLOW_STATUS.NOTSTARTED.toString());
        wfmDTO.setMessageType("dPCR");
        wfmDTO.setInputContainerId("RV-1234_A1");
        wfmDTO.setDeviceId("MPCZC8380G5K");
        wfmDTO.setSendingApplicationName("dPCRAnalyzer");
        return wfmDTO;
    }
    
    public ActivityProcessDataDTO getActivityProcessDataDTO() {
        ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
        activityProcessData.setMessageType("QBP");
        activityProcessData.setOrderDetails(updateBatchDetails);
        
        WFMQueryMessage queryMessage = new WFMQueryMessage();
        queryMessage.setProcessStepName("dPCR");
        queryMessage.setMessageType("QBP");
        queryMessage.setAccessioningId("800001");
        queryMessage.setDeviceId("MPCZC8380G5K");
        activityProcessData.setWfmQueryMessage(queryMessage);
        
        WFMESUMessage wfmESUMessage = new WFMESUMessage();
        wfmESUMessage.setAccessioningId("8001");
        wfmESUMessage.setProcessStepName("dPCR");
        wfmESUMessage.setDeviceId("RXT-1000");
        wfmESUMessage.setInputContainerId("18181");
        wfmESUMessage.setRunResultId("10001");
        wfmESUMessage.setMessageType("ESU");
        activityProcessData.setWfmESUMessage(wfmESUMessage);
        WFMORUMessage  wfmORUMessage = new WFMORUMessage();
        wfmORUMessage.setAccessioningId("8001");
        wfmORUMessage.setMessageType("ESU");
        wfmORUMessage.setDeviceId("Dpcr-11");
        activityProcessData.setoRUMessage(wfmORUMessage);
        return activityProcessData;
        
    }
    
    public WFMQueryMessage getWFMQueryMessage() {
        WFMQueryMessage wfmQueryMessage = new WFMQueryMessage();
        wfmQueryMessage.setAccessioningId("8001");
        wfmQueryMessage.setProcessStepName("dPCR");
        return wfmQueryMessage;
    }
    
    public WFMESUMessage geWFMESUMessage() {
        WFMESUMessage wfmESUMessage = new WFMESUMessage();
        wfmESUMessage.setAccessioningId("8001");
        wfmESUMessage.setProcessStepName("dPCR");
        wfmESUMessage.setDeviceId("RXT-1000");
        wfmESUMessage.setInputContainerId("18181");
        wfmESUMessage.setRunResultId("10001");
        wfmESUMessage.setMessageType("ESU");
        return wfmESUMessage;
    }
    public WFMORUMessage getWFMORUMessage() {
    	WFMORUMessage wfmORUMessage = new WFMORUMessage();
    	wfmORUMessage.setAccessioningId("8001");
    	wfmORUMessage.setProcessStepName("dPCR");
    	wfmORUMessage.setDeviceId("RXT-1000");
    	wfmORUMessage.setInputContainerId("18181");
    	wfmORUMessage.setMessageType("ORU");
        return wfmORUMessage;
    }
    
}
