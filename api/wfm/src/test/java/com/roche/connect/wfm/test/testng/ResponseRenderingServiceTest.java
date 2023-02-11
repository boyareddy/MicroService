package com.roche.connect.wfm.test.testng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.common.mp24.message.ResponseMessage;
import com.roche.connect.common.mp24.message.StatusUpdate;
import com.roche.connect.common.mp96.WFMQueryResponseMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.ASSAY_PROCESS_STEP_DATA;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.ImmIntegrationService;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResponseRenderingService;

@PrepareForTest({ HMTPLoggerImpl.class }) @PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*",
"javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*", "org.apache.logging.*",
"org.slf4j.*" }) public class ResponseRenderingServiceTest {
    private String accessioningId;
    private String containerId;
    private String messageControlId;
    private String deviceId;
    private String sendingApplicationName;
    private String lpaccessioningId;
    
    private static Logger logger = LogManager.getLogger(ResponseRenderingServiceTest.class);
    @InjectMocks ResponseRenderingService responseRenderingService;
    
    AdaptorRequestMessage adaptorRequestMessage;
    ActivityProcessDataDTO activityProcessData;
    SpecimenStatusUpdateMessage specimenStatusUpdateMessage;
    AcknowledgementMessage acknowledgementMessage;
    AdaptorResponseMessage adaptorResponseMessage;
    QueryMessage queryMessage;

    @Mock ImmIntegrationService immIntegrationService;
    @Mock OrderIntegrationService orderIntegrationService;
    @Mock AssayIntegrationService assayIntegrationService;
    @Mock ActivityProcessDataDTO activityProcessDatas;
    
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    
    List<WfmDTO> listWFMDTO = new ArrayList<>();
    List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();
    
    @BeforeTest public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
        deviceTestOptions.add(getDeviceTestOptionsDTO());
        listWFMDTO.add(getWfmDTO());
        adaptorRequestMessage = new AdaptorRequestMessage();
        activityProcessData = new ActivityProcessDataDTO();
        specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        acknowledgementMessage = new AcknowledgementMessage();
        queryMessage = new QueryMessage();
        initialize();
        
        lpaccessioningId = "9876456";
        messageControlId = "45678";
        accessioningId = "7654654";
        containerId = "1234";
        deviceId = "MP001-343";
        sendingApplicationName = "MagnaPure24";
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        Mockito.doNothing().when(immIntegrationService).setAdaptorResponseMessage(adaptorResponseMessage);
        Mockito.doNothing().when(immIntegrationService).setAcknowledgementMessage(acknowledgementMessage);
        PowerMockito.whenNew(AssayIntegrationService.class).withNoArguments().thenReturn(assayIntegrationService);
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString())).thenReturn(deviceTestOptions);
        
    }
    
    /**
     * We need a special {@link IObjectFactory}.
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    private void initialize() {
        adaptorRequestMessage.setAccessioningId("45678");
        adaptorRequestMessage.setContainerId("765432");
        adaptorRequestMessage.setDateTimeMessageGenerated(new Time(0).toString());
        adaptorRequestMessage.setDeviceSerialNumber(deviceId);
        adaptorRequestMessage.setMessageControlId("messageControlId");
        adaptorRequestMessage.setMessageType("NA-Extraction");
        adaptorRequestMessage.setReceivingApplication(sendingApplicationName);
        adaptorRequestMessage.setResponseMessage(new ResponseMessage());
        adaptorRequestMessage.setSendingApplicationName(sendingApplicationName);
        adaptorRequestMessage.setStatusUpdate(new StatusUpdate());
        
        specimenStatusUpdateMessage.setContainerId(containerId);
        specimenStatusUpdateMessage.setProcessStepName("LP PRE PCR");
        specimenStatusUpdateMessage.setMessageControlId(messageControlId);
        
        queryMessage.setProcessStepName("LP Pre PCR");
        
        activityProcessData.setAccessioningId(accessioningId);
        activityProcessData.setContainerId(containerId);
        activityProcessData.setDateTimeMessageGenerated(new Time(0).toString());
        activityProcessData.setDeviceId(deviceId);
        activityProcessData.setMessageControlId(messageControlId);
        activityProcessData.setMessageType("QueryMessage");
        activityProcessData.setOrderDetails(new ArrayList<>());
        activityProcessData.setForteStatusMessage(new ForteStatusMessage());
        activityProcessData.setHtpStatusMessage(new HtpStatusMessage());
        activityProcessData.setQbpResponseMsg(new WFMQueryResponseMessage());
        activityProcessData.setSpecimenStatusUpdateMessage(specimenStatusUpdateMessage);
        activityProcessData.setAdaptorRequestMessage(adaptorRequestMessage);
        activityProcessData.setQueryMessage(queryMessage);
    }
    
    @Test public void duplicateOrderQueryResponseforLP() {
        try {
            responseRenderingService.duplicateOrderQueryResponseforLP(containerId, deviceId, sendingApplicationName,
                messageControlId);
        } catch (Exception e) {
            logger.info("successfully method executed");
        }
    }
    
    @Test public void duplicateOrderQueryPositiveResponseforLp24() throws HMTPException, IOException {
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                    accessioningId, ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.PREPCR.toString())).thenReturn(deviceTestOptions);
        responseRenderingService.duplicateOrderQueryPositiveResponseforLp24(containerId, deviceId, accessioningId,
            activityProcessData);
    }
    
    
    @Test public void duplicateOrderQueryPositiveResponseforLp24ElseCondition() throws HMTPException, IOException {
    	
    	ActivityProcessDataDTO activityProcessData =getActivityProcessDataDTO();
    	activityProcessData.getQueryMessage().setProcessStepName("LP Post PCR/Pooling");
        Mockito.when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(accessioningId,
                ASSAY_PROCESS_STEP_DATA.LP24.toString(), ASSAY_PROCESS_STEP_DATA.POSTPCR.toString())).thenReturn(deviceTestOptions);
        responseRenderingService.duplicateOrderQueryPositiveResponseforLp24(containerId, deviceId, accessioningId,
            activityProcessData);
    }
    
    @Test public void orderNotFoundQueryResponseforLP24() {
        try {
            responseRenderingService.orderNotFoundQueryResponseforLP24(containerId, deviceId, sendingApplicationName,
                messageControlId);
        } catch (Exception e) {
            logger.info("successfully method executed");
        }
    }
    
    
    
    @Test public void orderNotFoundQueryResponseforLPTest() throws JsonProcessingException, HMTPException {
        // Mockito.doNothing().when(immIntegrationService).setResponseMessage(Matchers.any(
        // com.roche.connect.common.lp24.ResponseMessage.class));
        responseRenderingService.orderNotFoundQueryResponseforLP(containerId, deviceId, sendingApplicationName);
    }
    
    @Test public void orderNotFoundQueryResponsefordPCRLP() {
        try {
            responseRenderingService.orderNotFoundQueryResponsefordPCRLP(containerId, accessioningId, deviceId,
                sendingApplicationName);
        } catch (Exception e) {
            logger.info("successfully method executed");
        }
    }
    
    @Test public void orderNotFoundUO3ResponseFordPCRLP24() throws HMTPException{
        responseRenderingService.orderNotFoundUO3ResponseFordPCRLP24(activityProcessData);
    }
    
    @Test public void orderNotFoundQueryResponseforMP24() {
        try {
            responseRenderingService.orderNotFoundQueryResponseforMP24(accessioningId, deviceId, messageControlId);
        } catch (Exception e) {
            logger.info("successfully method executed");
        }
    }
    
    @Test public void orderNotFoundQueryResponseforMP() {
        try {
            responseRenderingService.orderNotFoundQueryResponseforMP(accessioningId, deviceId);
        } catch (Exception e) {
            logger.info("successfully method executed");
        }
    }
    
    @Test public void duplicateOrderResponseforMP2arg() {
        try {
            responseRenderingService.duplicateOrderResponseforMP(accessioningId, deviceId, messageControlId);
        } catch (Exception e) {
            logger.info("successfully method executed");
        }
    }
    
    @Test public void duplicateOrderResponseforMP3arg() throws OrderNotFoundException, HMTPException, IOException {
    	 List<WfmDTO> listWFMDTO = new ArrayList<>();
    	  AdaptorResponseMessage adaptorRspMsg = new AdaptorResponseMessage();
    	 listWFMDTO.add(getWfmDTO());
    	 List<DeviceTestOptionsDTO> deviceTestOptions = new ArrayList<>();
    	 deviceTestOptions.add(getDeviceTestOptionsDTO());
            Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(listWFMDTO);
            Mockito.when(assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(listWFMDTO,
            		WfmConstants.ASSAY_PROCESS_STEP_DATA.MP24.toString(),
                    WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()))
                .thenReturn(deviceTestOptions);
            Mockito.doNothing().when(activityProcessDatas).setQbpResponseMessage(adaptorRspMsg);
            Mockito.doNothing().when(immIntegrationService).setAdaptorResponseMessage(adaptorRspMsg);
            try {
            responseRenderingService.duplicateOrderResponseforMP(accessioningId, deviceId, adaptorRequestMessage);
            }catch(Exception e) {
            	
            }
    }
    
    @Test public void duplicateQueryPositiveResponsefordPCRLP() {
        responseRenderingService.duplicateQueryPositiveResponsefordPCRLP(containerId, deviceId,lpaccessioningId, messageControlId);
    }
    
    @Test public void duplicateQueryNegativeResponsefordPCRLP() throws Exception {
        
        responseRenderingService.duplicateQueryNegativeResponsefordPCRLP(containerId, deviceId,lpaccessioningId, messageControlId);
        
    }
    
    @Test public void duplicateACKForMP24Test() throws HMTPException {
        responseRenderingService.duplicateACKForMP24(accessioningId, deviceId, activityProcessData);
    }
    
    @Test public void duplicateNegativeACKForMP24Test() throws HMTPException {
        responseRenderingService.duplicateNegativeACKForMP24(accessioningId, deviceId, activityProcessData);
    }
    
    @Test public void duplicateACKForLP24Test() throws HMTPException {
        responseRenderingService.duplicateACKForLP24(deviceId, activityProcessData);
    }
    
    @Test public void duplicateNegativeACKForLP24Test() throws HMTPException {
        responseRenderingService.duplicateNegativeACKForLP24(deviceId, activityProcessData);
    }
    
    public WfmDTO getWfmDTO() {
        WfmDTO wfmDTO = new WfmDTO();
        
        wfmDTO.setSendingApplicationName("MagnaPure24");
        wfmDTO.setDeviceId("abc");
        wfmDTO.setAssayType("NIPTHTP");
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
    
    
    public ActivityProcessDataDTO getActivityProcessDataDTO() {
    	ActivityProcessDataDTO activityProcessData=new ActivityProcessDataDTO();
    
    activityProcessData.setAccessioningId(accessioningId);
    activityProcessData.setContainerId(containerId);
    activityProcessData.setDateTimeMessageGenerated(new Time(0).toString());
    activityProcessData.setDeviceId(deviceId);
    activityProcessData.setMessageControlId(messageControlId);
    activityProcessData.setMessageType("QueryMessage");
    activityProcessData.setOrderDetails(new ArrayList<>());
    activityProcessData.setForteStatusMessage(new ForteStatusMessage());
    activityProcessData.setHtpStatusMessage(new HtpStatusMessage());
    activityProcessData.setQbpResponseMsg(new WFMQueryResponseMessage());
    activityProcessData.setSpecimenStatusUpdateMessage(specimenStatusUpdateMessage);
    activityProcessData.setAdaptorRequestMessage(adaptorRequestMessage);
    activityProcessData.setQueryMessage(queryMessage);
    return activityProcessData;
    }
    
    public DeviceTestOptionsDTO getDeviceTestOptionsDTO() {
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("Protocol Name");
        return deviceTestOptionsDTO;
    }
}
