package com.roche.connect.wfm.test.testng;

import java.util.ArrayList;
import java.util.Date;
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
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.Consumable;
import com.roche.connect.common.lp24.ContainerInfo;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.SampleInfo;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.lp24.FindOrderBy96WellPlateId;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.lp24.SendLP24QBPResponse;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.lp24.SendLP24U03Response;
import com.roche.connect.wfm.nipt.dpcr.wfmtask.lp24.UpdateLP24Result;
import com.roche.connect.wfm.nipt.htp.wfmtask.lp24.prepcr.SendLP24PrePcrU03Response;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.ImmIntegrationService;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;
@PrepareForTest({HMTPLoggerImpl.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class LP24TaskTest {
    
    @InjectMocks UpdateLP24Result updateLP24Result;
    
    @InjectMocks FindOrderBy96WellPlateId getFindOrderBy96WellPlateId;
    
    @InjectMocks SendLP24QBPResponse getSendLP24QBPResponse;
    
    @InjectMocks SendLP24U03Response getSendLP24U03Response;
    
    @InjectMocks com.roche.connect.wfm.nipt.htp.wfmtask.lp24.postpcr.SendLP24U03Response sendLP24U03ResponseHTP;
    @Mock DelegateExecution execution;
    
    @Mock HMTPLoggerImpl loggerImpl;
    
    @Mock  ResultIntegrationService resultIntegrationService;
    
    @Mock  OrderIntegrationService orderIntegrationService;
    
    @Mock  ImmIntegrationService immIntegrationService;
    
    @Mock  AssayIntegrationService assayIntegrationService;
    
    @Mock   ResponseRenderingService responseRenderingService;
    
    @Mock SendLP24PrePcrU03Response  sendLP24PrePcrU03Response ;
    
    List<WfmDTO> updateBatchDetails = new ArrayList<>();
    
    
    
    @BeforeTest
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        List<WfmDTO> wfmList =  new ArrayList<>();
        wfmList.add(getWfmDTO());
        updateBatchDetails.add(getWfmDTO());
        List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();
        deviceTestOptions.add(getDeviceTestOptions());
        SpecimenStatusUpdateMessage U03RequestMessage = getSpecimenStatusUpdateMessage();
        AcknowledgementMessage acknowledgementMessage = getAcknowledgementMessage();
        Mockito.when((ActivityProcessDataDTO) execution
                .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString())).thenReturn(getActivityProcessDataDTO());
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSDPCRLP24UPDATE.toString())).thenReturn("Passed");
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERDETAILSDPCRLPUPDATE.toString())).thenReturn(wfmList);
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.DEVICEIDDPCRLP24UPDATE.toString())).thenReturn("765565");
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDDPCR24UPDATE.toString())).thenReturn("454555");
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERIDDPCRLP24.toString())).thenReturn("343434");
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONDPCRLP24.toString())).thenReturn("A1");
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGIDDPCRLP24.toString())).thenReturn("723451");
        Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.TUBECONTAINERPOSITIONDPCRLP24.toString())).thenReturn("A1");
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep("723451",
        WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString())).thenReturn(deviceTestOptions);
        Mockito.doNothing().when(resultIntegrationService).updateforLP24Results(updateBatchDetails, U03RequestMessage);
        Mockito.doNothing().when(immIntegrationService).setAcknowledgementMessage(acknowledgementMessage); 
       
    }
    
    /** Mandatory to mock static classes **/
    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test(priority =1) public void execute() {
        updateLP24Result.execute(execution);
    }
    
    
    @Test(priority =2) public void executeElse() {
    	   Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSDPCRLP24UPDATE.toString())).thenReturn("Failed");
        updateLP24Result.execute(execution);
    }
    
    @Test(priority =3) public void executeElseSecond() {
 	   Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSDPCRLP24UPDATE.toString())).thenReturn("InProgress");
     updateLP24Result.execute(execution);
   }
    
    
    @Test(priority =4) public void executeElseThird() {
  	   Mockito.when(execution.getVariable(WfmConstants.WORKFLOW_VARIABLES.ORDERSTATUSDPCRLP24UPDATE.toString())).thenReturn("InProgres");
      updateLP24Result.execute(execution);
  }
    
    @Test(priority =5) public void getFindOrderBy96WellPlateId() {
       getFindOrderBy96WellPlateId.execute(execution);
    }
    
    @Test(priority =6) public void getSendLP24QBPResponse() throws Exception {
        getSendLP24QBPResponse.execute(execution);
    }
    
    @Test(priority =7) public void getSendLP24U03Response() throws HMTPException {
       getSendLP24U03Response.execute(execution);
    }
    
    
    @Test(priority =8)
    public void sendLP24U03ResponseHTPTest() throws HMTPException {
        Mockito.when((ActivityProcessDataDTO) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString())).thenReturn(getActivityProcessDataDTO());
        Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24("LP0001", getActivityProcessDataDTO());
        sendLP24U03ResponseHTP.execute(execution);
    }
    
    @Test(priority =9)
    public void SendLP24PrePcrU03ResponseTest() throws HMTPException {
        Mockito.when((ActivityProcessDataDTO) execution
                    .getVariable(WfmConstants.WORKFLOW_VARIABLES.ACTIVITYPROCESSDATA.toString())).thenReturn(getActivityProcessDataDTO());
        Mockito.doNothing().when(responseRenderingService).duplicateACKForLP24("LP001", getActivityProcessDataDTO());
        sendLP24PrePcrU03Response.execute(execution); 
    }
    
    @Test(priority =10) public void getSendLP24U03ResponseForNeg1() throws Exception {
    	MockitoAnnotations.initMocks(this);
    	
    	Mockito.when((ActivityProcessDataDTO) execution
					.getVariable(Mockito.anyString())).thenReturn(null);
    	getSendLP24U03Response.execute(execution);
    	updateLP24Result.execute(execution);
    	getSendLP24QBPResponse.execute(execution);
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
        activityProcessData.setDeviceId("LP0001");
        activityProcessData.setMessageType("Library Preparation");
        activityProcessData.setOrderDetails(updateBatchDetails);
        


        QueryMessage queryMessage = new QueryMessage();
        queryMessage.setMessageControlId("1321434");
        
        activityProcessData.setQueryMessage(queryMessage);
        activityProcessData.getQueryMessage().setContainerId("898989");
        activityProcessData.setSpecimenStatusUpdateMessage(getSpecimenStatusUpdateMessage());
        activityProcessData.getSpecimenStatusUpdateMessage().setContainerId("666666");
        activityProcessData.getSpecimenStatusUpdateMessage().setMessageControlId("777777");
        return activityProcessData;
    }

    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessage() {
        SpecimenStatusUpdateMessage U03RequestMessage = new SpecimenStatusUpdateMessage();
        U03RequestMessage.setAccessioningId("8001");
        U03RequestMessage.setOrderId(567777L);
        U03RequestMessage.setSendingApplicationName("LP24");
        U03RequestMessage.setProcessStepName("SSU");
        U03RequestMessage.setStatusUpdate(getStatusUpdate());
        return U03RequestMessage;
    }

    public StatusUpdate getStatusUpdate() {
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setConsumables(getConsumables());
        statusUpdate.setContainerInfo(getContainerInfo());
        statusUpdate.setSampleInfo(getSampleInfo());
        return statusUpdate;
    }
    
    public ContainerInfo getContainerInfo(){
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setOutputPlateType("sample");
        return containerInfo;
    }

    public SampleInfo getSampleInfo(){
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setSampleType("plasma");
        return sampleInfo;
    }
    
    public List<Consumable> getConsumables(){
        List<Consumable> consumables = new ArrayList<>();
        consumables.add(getConsumablesmp());
        return consumables;
    }
    
    public Consumable getConsumablesmp(){
        Consumable Consum = new Consumable();
        Consum.setName("sample");
        Consum.setValue("56777");
        return Consum;
    }
    
    public DeviceTestOptionsDTO getDeviceTestOptions() {
        DeviceTestOptionsDTO devictestoption = new DeviceTestOptionsDTO();
        devictestoption.setTestProtocol("Dpcr-Protocol");
        devictestoption.setTestName("Dpcr test");
        devictestoption.setDeviceType("LP24");
        return devictestoption;
    }
    public AcknowledgementMessage getAcknowledgementMessage(){
        AcknowledgementMessage acknowledgementMsg = new AcknowledgementMessage();
        acknowledgementMsg.setContainerId("456789");
        acknowledgementMsg.setDeviceSerialNumber("MP24567");
        acknowledgementMsg.setStatus(WfmConstants.WORKFLOW_RESPONSE.SUCCESS_1.toString());
        acknowledgementMsg.setSendingApplicationName(WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.CONNECT.toString());
        acknowledgementMsg.setReceivingApplication(WfmConstants.WORKFLOW_VARIABLES.DPCRLP24.toString());
        acknowledgementMsg.setDateTimeMessageGenerated(new Date().toString());
        acknowledgementMsg.setMessageControlId("888888");
        acknowledgementMsg.setMessageType(EnumMessageType.AcknowledgementMessage);
      
        return acknowledgementMsg;
    }

}
