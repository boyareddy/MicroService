package com.roche.connect.wfm.test.testng;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.common.mp24.message.StatusUpdate;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.error.QueryValidationException;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.SampleWFMStates;
import com.roche.connect.wfm.repository.WfmReadRepository;
import com.roche.connect.wfm.rest.WfmRestApiImpl;
import com.roche.connect.wfm.service.ResponseRenderingService;
import com.roche.connect.wfm.service.ResultIntegrationService;
import com.roche.connect.wfm.service.WFMHTPService;
import com.roche.connect.wfm.writerepository.WfmWriteRepository;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, ThreadSessionManager.class, AdmNotificationService.class  }) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" }) public class WfmRestApiImplTest {
    
    @InjectMocks WfmRestApiImpl wfmRestApiImpl;
    
    @Mock UserSession userSession;
    
    @Mock WfmReadRepository wfmRepository;
    
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    
    @Mock WfmWriteRepository wfmwriteRepository;
    
    @Mock ResponseRenderingService responseRenderingService;
    
    @Mock ObjectMapper objectMapper;
    
    @Mock ResultIntegrationService resultIntegrationService;
    
    @Mock WFMHTPService wfmService;
    
    @Mock ActivityProcessDataDTO activityProcessData;
    
    @Mock AdaptorResponseMessage responseMessage;
    
    @BeforeTest public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        getPASToken();        
    }
    
    /**
     * We need a special {@link IObjectFactory}.
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test public void getExecuteStartWFMTest() throws OrderNotFoundException, JsonProcessingException, HMTPException {
        Mockito.when(objectMapper.writeValueAsString(getAdaptorRequestMessage())).thenReturn("objectmapper");
        Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
            Mockito.anyString());
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyLong())).thenReturn(getrocheWfm());
        Mockito.when(wfmwriteRepository.save(getrocheWfm(), 1L)).thenReturn(getrocheWfm());
        Mockito.doNothing().when(responseRenderingService).duplicateOrderResponseforMP("8001", "MP001",
            getAdaptorRequestMessage());
        wfmRestApiImpl.getExecuteStartWFM(getAdaptorRequestMessage());
    }
    
    @Test public void getExecuteStartWFMelseifTest() throws Exception {
        Mockito.when(objectMapper.writeValueAsString(getAdaptorRequestMessage())).thenReturn("objectmapper");
        Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
            Mockito.anyString());
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyLong())).thenReturn(null);
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing()
                .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        Mockito.doNothing().when(wfmService).startMp24Process("8001", getActivityProcessDataForMP24());
        Mockito.when(activityProcessData.getQbpResponseMessage()).thenReturn(responseMessage);
        Mockito.when(objectMapper.writeValueAsString(responseMessage)).thenReturn("objectmapper");
        wfmRestApiImpl.getExecuteStartWFM(getAdaptorRequestMessage());
    }
    
    @Test public void getExecuteStartWFMifelseTest() throws OrderNotFoundException, HMTPException, IOException {
        Mockito.when(objectMapper.writeValueAsString(getAdaptorRequestMessageelse())).thenReturn("objectmapper");
        Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
            Mockito.anyString());
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyLong())).thenReturn(null);
        Mockito.doNothing().when(wfmService).startMp24Process("8001", getActivityProcessDataForMP24());
        Mockito.when(activityProcessData.getQbpResponseMessage()).thenReturn(responseMessage);
        Mockito.when(objectMapper.writeValueAsString(responseMessage)).thenReturn("objectmapper");
        wfmRestApiImpl.getExecuteStartWFM(getAdaptorRequestMessageelse());
    }
    
    @Test public void getExecuteStartWFMCatchHMTPOTest() throws OrderNotFoundException, HMTPException, IOException {
        Mockito.when(objectMapper.writeValueAsString(getAdaptorRequestMessagecatch())).thenReturn("objectmapper");
        Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
            Mockito.anyString());
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyLong())).thenReturn(null);
        Mockito.doNothing().when(wfmService).startMp24Process(null, getActivityProcessDataForMP24());
        Mockito.when(activityProcessData.getQbpResponseMessage()).thenReturn(responseMessage);
        Mockito.when(objectMapper.writeValueAsString(responseMessage)).thenReturn("objectmapper");
        wfmRestApiImpl.getExecuteStartWFM(getAdaptorRequestMessagecatch());
    }
    
    @Test public void getExecuteStartWFMCatchOrderNotFoundTest() throws Exception {
        Mockito.when(objectMapper.writeValueAsString(getAdaptorRequestMessage())).thenReturn("objectmapper");
        Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
            Mockito.anyString());
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyLong())).thenReturn(null);
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing()
                .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        Mockito.doThrow(OrderNotFoundException.class).when(wfmService).startMp24Process(Mockito.anyString(), Mockito.any(ActivityProcessDataDTO.class));
        Mockito.when(activityProcessData.getQbpResponseMessage()).thenReturn(responseMessage);
        Mockito.when(objectMapper.writeValueAsString(responseMessage)).thenReturn("objectmapper");
        wfmRestApiImpl.getExecuteStartWFM(getAdaptorRequestMessage());
    }
    
    @Test public void getExecuteStartWFMelseTest() throws OrderNotFoundException, HMTPException, IOException {
        Mockito.when(objectMapper.writeValueAsString(getAdaptorRequestMessage())).thenReturn("objectmapper");
        Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
            Mockito.anyString());
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyLong())).thenReturn(null);
        Mockito.doNothing().when(wfmService).startMp24Process("8001", getActivityProcessDataForMP24());
        Mockito.when(activityProcessData.getQbpResponseMessage()).thenReturn(responseMessage);
        Mockito.when(objectMapper.writeValueAsString(responseMessage)).thenReturn("objectmapper");
        wfmRestApiImpl.getExecuteStartWFM(getAdaptorRequestMessageTypeAccessioningId());
    }
    
    @Test public void deployWorkflowProcessTest() throws HMTPException{
        wfmRestApiImpl.deployWorkflowProcess(getAdaptorRequestMessage());
    }
    
    @Test public void doTaskCompleteTest() throws HMTPException{
        wfmRestApiImpl.doTaskComplete(getAdaptorRequestMessage());
    }
    
    @Test public void getTaskCompleteStatusTest() throws HMTPException{
        wfmRestApiImpl.getTaskCompleteStatus(getAdaptorRequestMessage());
    }
    
    @Test public void getExecuteStartWFMNegativeElseTest()
        throws OrderNotFoundException,
        JsonProcessingException,
        HMTPException {
        Mockito.when(wfmRepository.findByAccessioningIdAndMessageType(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyLong())).thenReturn(getrocheWfm());
        wfmRestApiImpl.getExecuteStartWFM(getAdaptorRequestMessage());
    }
    
    @Test public void getExecuteStatusUpdateWFMMPStatusTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", AdaptorRequestMessage.class)).thenReturn(getAdaptorRequestMessage());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateMp24Process(activityProcessData, "8001", "orderStatus", "MP001");
        wfmRestApiImpl.getExecuteStatusUpdateWFM("MPStatus", "");
    }
    
    @Test public void getExecuteStatusUpdateWFMLPpreQueryTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", QueryMessage.class)).thenReturn(getQueryMessageLPPrePCR());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateMp24Process(activityProcessData, "8001", "orderStatus", "MP001");
        Mockito.doNothing().when(wfmService).startLp24PrePcrProcess(Mockito.anyString(),Mockito.anyString(), Mockito.anyString(), Mockito.any());
        wfmRestApiImpl.getExecuteStatusUpdateWFM("LPQuery", "");
    }
    
    @Test public void getExecuteStatusUpdateWFMLPpostQueryTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", QueryMessage.class)).thenReturn(getQueryMessageLPPostPCR());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateMp24Process(activityProcessData, "8001", "orderStatus", "MP001");
        Mockito.doNothing().when(wfmService).startLp24PrePcrProcess(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.any());
        wfmRestApiImpl.getExecuteStatusUpdateWFM("LPQuery", "");
    }
    
    @Test public void getExecuteStatusUpdateWFMLPSeqPrepQueryTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", QueryMessage.class)).thenReturn(getQueryMessageLPSeqPrep());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateMp24Process(activityProcessData, "8001", "orderStatus", "MP001");
        Mockito.doNothing().when(wfmService).startLp24PrePcrProcess(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.any());
        wfmRestApiImpl.getExecuteStatusUpdateWFM("LPQuery", "");
    }
    
    @Test public void getExecuteStatusUpdateWFMFORTESTATUSTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", ForteStatusMessage.class)).thenReturn(getForteStatusMessage());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateMp24Process(activityProcessData, "8001", "orderStatus", "MP001");
        Mockito.doNothing().when(wfmService).startLp24PrePcrProcess(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(wfmService).updateHTPProcess(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
            Mockito.anyString(), Mockito.any(), Mockito.any());
        Mockito.when(wfmService.updateForteProcess(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        wfmRestApiImpl.getExecuteStatusUpdateWFM(WfmConstants.WORKFLOW_MESSAGESOURCE.FORTETATUS.toString(), "");
    }
    
    @Test public void getExecuteStatusUpdateWFMHTPSTATUSTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", HtpStatusMessage.class)).thenReturn(getHtpStatusMessage());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateMp24Process(activityProcessData, "8001", "orderStatus", "MP001");
        Mockito.doNothing().when(wfmService).startLp24PrePcrProcess(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(wfmService).updateHTPProcess(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
            Mockito.anyString(), Mockito.anyString(), Mockito.any());
        wfmRestApiImpl.getExecuteStatusUpdateWFM(WfmConstants.WORKFLOW_MESSAGESOURCE.HTPSTATUS.toString(), "");
    }
    
    @Test public void getExecuteStatusUpdateWFMLPprepcrStatusTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", SpecimenStatusUpdateMessage.class))
            .thenReturn(getSpecimenStatusUpdateMessageLPprePCR());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateLp24PrePcrProcess(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.any());
        wfmRestApiImpl.getExecuteStatusUpdateWFM("LPStatus", "");
    }
    
    @Test public void getExecuteStatusUpdateWFMLPprepcrStatusOrderNotFoundTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", SpecimenStatusUpdateMessage.class))
            .thenReturn(getSpecimenStatusUpdateMessageLPprePCR());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doThrow(OrderNotFoundException.class).when(wfmService).updateLp24PrePcrProcess(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any());
        Mockito.doNothing().when(responseRenderingService).orderNotFoundQueryResponseforLP24(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any());
        wfmRestApiImpl.getExecuteStatusUpdateWFM("LPStatus", "");
    }
    
    @Test public void getExecuteHTPStatusUpdateWFMTest() {
        wfmRestApiImpl.getExecuteHTPStatusUpdateWFM(getHtpStatusMessage());
    }
    
    @Test public void getExecuteHTPStatusUpdateWFMNegativeTest() {
        wfmRestApiImpl.getExecuteHTPStatusUpdateWFM(getHtpStatusMessageNegative());
    }
    
    @Test public void getExceucteFORTEStatusUpdateWFMTest() throws JsonProcessingException, QueryValidationException {
        wfmRestApiImpl.getExecuteFORTEStatusUpdateWFM(getForteStatusMessage());
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessageLPprePCR() {
        SpecimenStatusUpdateMessage specimenmessage = new SpecimenStatusUpdateMessage();
        specimenmessage.setAccessioningId("");
        specimenmessage.setMessageType(EnumMessageType.StatusUpdateMessage);
        specimenmessage.setDeviceSerialNumber("LP001");
        specimenmessage.setContainerId("ABC_A1");
        specimenmessage.setSendingApplicationName("LP24");
        specimenmessage.setProcessStepName("LP Pre PCR");
        specimenmessage.setAccessioningId("8001");
        com.roche.connect.common.lp24.StatusUpdate statusUpdate = new com.roche.connect.common.lp24.StatusUpdate();
        statusUpdate.setOrderResult("orderStatus");
        specimenmessage.setStatusUpdate(statusUpdate);
        return specimenmessage;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessageLPpostPCR() {
        SpecimenStatusUpdateMessage specimenmessage = new SpecimenStatusUpdateMessage();
        specimenmessage.setAccessioningId("");
        specimenmessage.setMessageType(EnumMessageType.StatusUpdateMessage);
        specimenmessage.setDeviceSerialNumber("LP001");
        specimenmessage.setContainerId("ABC_A1");
        specimenmessage.setSendingApplicationName("LP24");
        specimenmessage.setProcessStepName("LP Post PCR/Pooling");
        specimenmessage.setAccessioningId("8001");
        com.roche.connect.common.lp24.StatusUpdate statusUpdate = new com.roche.connect.common.lp24.StatusUpdate();
        statusUpdate.setOrderResult("orderStatus");
        specimenmessage.setStatusUpdate(statusUpdate);
        return specimenmessage;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessageLPSeqprep() {
        SpecimenStatusUpdateMessage specimenmessage = new SpecimenStatusUpdateMessage();
        specimenmessage.setAccessioningId("");
        specimenmessage.setMessageType(EnumMessageType.StatusUpdateMessage);
        specimenmessage.setDeviceSerialNumber("LP001");
        specimenmessage.setContainerId("ABC_A1");
        specimenmessage.setSendingApplicationName("LP24");
        specimenmessage.setProcessStepName("LP Sequencing Prep");
        specimenmessage.setAccessioningId("8001");
        com.roche.connect.common.lp24.StatusUpdate statusUpdate = new com.roche.connect.common.lp24.StatusUpdate();
        statusUpdate.setOrderResult("orderStatus");
        specimenmessage.setStatusUpdate(statusUpdate);
        return specimenmessage;
    }
    
    @Test public void getExecuteStatusUpdateWFMLPpostpcrStatusTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", SpecimenStatusUpdateMessage.class))
            .thenReturn(getSpecimenStatusUpdateMessageLPpostPCR());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateLp24PrePcrProcess(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.any());
        wfmRestApiImpl.getExecuteStatusUpdateWFM("LPStatus", "");
    }
    
    @Test public void getExecuteStatusUpdateWFMLPseqprepStatusTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Mockito.when(objectMapper.readValue("", SpecimenStatusUpdateMessage.class))
            .thenReturn(getSpecimenStatusUpdateMessageLPSeqprep());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateLp24PrePcrProcess(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.any());
        wfmRestApiImpl.getExecuteStatusUpdateWFM("LPStatus", "");
    }
    
    @Test public void getExecuteStatusUpdateWFMMP24UPDATERUNRESULTTest()
        throws QueryValidationException,
        JsonParseException,
        JsonMappingException,
        IOException,
        HMTPException,
        OrderNotFoundException {
        Map<String, String> requestBody = new HashMap<>();
        Mockito.when(objectMapper.readValue("", HashMap.class)).thenReturn((HashMap<String, String>) requestBody);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.when(objectMapper.readValue("", ForteStatusMessage.class)).thenReturn(getForteStatusMessage());
        Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("hjf");
        Mockito.doNothing().when(wfmService).updateMp24Process(activityProcessData, "8001", "orderStatus", "MP001");
        Mockito.doNothing().when(wfmService).startLp24PrePcrProcess(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(wfmService).updateHTPProcess(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(),
            Mockito.anyString(), Mockito.any(), Mockito.any());
        Mockito.when(wfmService.updateForteProcess(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.doNothing().when(resultIntegrationService).updateRunResultStatusByQuery(Mockito.anyString(),
            Mockito.anyString());
        wfmRestApiImpl.getExecuteStatusUpdateWFM(WfmConstants.WORKFLOW_MESSAGESOURCE.MP24UPDATERUNRESULT.toString(),
            "");
    }
    
    public QueryMessage getQueryMessageLPPrePCR() {
        QueryMessage message = new QueryMessage();
        message.setMessageType(EnumMessageType.QueryMessage);
        message.setDeviceSerialNumber("LP001");
        message.setContainerId("ABC_A1");
        message.setSendingApplicationName("LP24");
        message.setProcessStepName("LP Pre PCR");
        message.setAccessioningId("8001");
        return message;
        
    }
    
    public QueryMessage getQueryMessageLPPostPCR() {
        QueryMessage message = new QueryMessage();
        message.setMessageType(EnumMessageType.QueryMessage);
        message.setDeviceSerialNumber("LP001");
        message.setContainerId("ABC_A1");
        message.setSendingApplicationName("LP24");
        message.setProcessStepName("LP Post PCR/Pooling");
        message.setAccessioningId("8001");
        return message;
    }
    
    public QueryMessage getQueryMessageLPSeqPrep() {
        QueryMessage message = new QueryMessage();
        message.setMessageType(EnumMessageType.QueryMessage);
        message.setDeviceSerialNumber("LP001");
        message.setContainerId("ABC_A1");
        message.setSendingApplicationName("LP24");
        message.setProcessStepName("LP Sequencing Prep");
        message.setAccessioningId("8001");
        return message;
        
    }
    
    public void getPASToken() {
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(Mockito.anyString()))
            .thenReturn("token");
    }
    
    public ActivityProcessDataDTO getActivityProcessDataForMP24() {
        ActivityProcessDataDTO activityProcessData = new ActivityProcessDataDTO();
        activityProcessData.setAdaptorRequestMessage(getAdaptorRequestMessage());
        activityProcessData.setAccessioningId("8001");
        activityProcessData.setMessageType("NA-Extraction");
        activityProcessData.setDeviceId("MP001");
        activityProcessData.setHtpStatusMessage(getHtpStatusMessage());
        return activityProcessData;
        
    }
    
    public SampleWFMStates getrocheWfm() {
        SampleWFMStates rocheWfm = new SampleWFMStates();
        rocheWfm.setAccessioningId("8001");
        rocheWfm.setDeviceId("MPCZC8380G5K");
        rocheWfm.setProcessId("15");
        rocheWfm.setMessageType("NA-Extraction");
        rocheWfm.setCurrentStatus("Query");
        Company company = new Company();
        company.setId(1L);
        rocheWfm.setCompany(company);
        return rocheWfm;
        
    }
    
    public AdaptorRequestMessage getAdaptorRequestMessage() {
        AdaptorRequestMessage ad = new AdaptorRequestMessage();
        ad.setMessageType("NA-Extraction");
        ad.setAccessioningId("8001");
        ad.setDeviceSerialNumber("MP001");
        ad.setSendingApplicationName("MagnaPure24");
        ad.setContainerId("708090");
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setOrderResult("orderStatus");
        ad.setStatusUpdate(statusUpdate);
        return ad;
        
    }
    
    public AdaptorRequestMessage getAdaptorRequestMessageelse() {
        AdaptorRequestMessage ad = new AdaptorRequestMessage();
        ad.setMessageType("NA-Extraction");
        ad.setAccessioningId("8001");
        ad.setDeviceSerialNumber("MP001");
        ad.setSendingApplicationName("xyz");
        ad.setContainerId("708090");
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setOrderResult("orderStatus");
        ad.setStatusUpdate(statusUpdate);
        return ad;
        
    }
    
    public AdaptorRequestMessage getAdaptorRequestMessagecatch() {
        AdaptorRequestMessage ad = new AdaptorRequestMessage();
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setOrderResult("orderStatus");
        ad.setStatusUpdate(statusUpdate);
        return ad;
        
    }
    
    public AdaptorRequestMessage getAdaptorRequestMessageTypeAccessioningId() {
        AdaptorRequestMessage ad = new AdaptorRequestMessage();
        ad.setMessageType("Other");
        ad.setAccessioningId("Other");
        ad.setDeviceSerialNumber("MP001");
        ad.setSendingApplicationName("MagnaPure24");
        return ad;
        
    }
    
    public AdaptorRequestMessage getAdaptorRequestMessageSendingApp() {
        AdaptorRequestMessage ad = new AdaptorRequestMessage();
        ad.setMessageType("Other");
        ad.setAccessioningId("Other");
        ad.setDeviceSerialNumber("MP001");
        ad.setSendingApplicationName("Other");
        return ad;
        
    }
    
    public HtpStatusMessage getHtpStatusMessage() {
        HtpStatusMessage htpStatusMessage = new HtpStatusMessage();
        htpStatusMessage.setAccessioningId("8001");
        htpStatusMessage.setOrderId(100001L);
        htpStatusMessage.setSendingApplication("HTP");
        htpStatusMessage.setStatus("Started");
        htpStatusMessage.setDeviceRunId("64765656");
        htpStatusMessage.setRunResultsId(4456L);
        return htpStatusMessage;
    }
    
    public HtpStatusMessage getHtpStatusMessageNegative() {
        HtpStatusMessage htpStatusMessage = new HtpStatusMessage();
        htpStatusMessage.setAccessioningId("8001");
        htpStatusMessage.setOrderId(100001L);
        htpStatusMessage.setSendingApplication("HTPdg");
        htpStatusMessage.setStatus("Started");
        htpStatusMessage.setDeviceRunId("64765656");
        htpStatusMessage.setRunResultsId(4456L);
        return htpStatusMessage;
    }
    
    public ForteStatusMessage getForteStatusMessage() {
        ForteStatusMessage forteStatusMessage = new ForteStatusMessage();
        forteStatusMessage.setAccessioningId("8001");
        forteStatusMessage.setSendingApplication("HTP");
        forteStatusMessage.setStatus("Started");
        return forteStatusMessage;
    }
}
