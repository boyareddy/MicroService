package com.roche.connect.imm.testng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.common.exceptions.InvalidStateException;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.htp.status.HtpStatus;
import com.roche.connect.common.lp24.Consumable;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.common.mp24.message.RSPMessage;
import com.roche.connect.common.mp24.message.SampleInfo;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.MessageProcessorService;
import com.roche.connect.imm.service.MessageService;
import com.roche.connect.imm.service.OrderIntegrationService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.utils.IMMConstants;
import com.roche.connect.imm.utils.MessageType;
import com.roche.connect.imm.utils.NIPTProcessStepConstants;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, URLEncoder.class ,AdmNotificationService.class,ThreadSessionManager.class}) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" }) public class MessageProcessorTest {
    
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    @InjectMocks private MessageProcessorService messageProcessorService;
    @Mock OrderIntegrationService ommService;
    @Mock MessageService messageService;
    @Mock RmmIntegrationService rmmService;
    @Mock AssayIntegrationService assayService;
    @Mock Invocation.Builder responseClient;
    @Mock Response response;
    @Mock Response res;
    @Mock Invocation.Builder builder;
    @Mock Invocation.Builder assayClient;
    @Mock UserSession userSession;
    @Mock ObjectMapper objectMapper;
    List<SampleResultsDTO> listSampleDTO = new ArrayList<>();
    List<HtpStatusMessage> htpSampleStatus = new ArrayList<>();
    List<DeviceTestOptionsDTO> listOfTestOptionsDTO = new ArrayList<>();
    List<DeviceTestOptionsDTO> deviceTestOptions  = new ArrayList<>();
    List<ProcessStepActionDTO> processStepList = new ArrayList<>();
    List<ProcessStepActionDTO> processStepListForSSU = new ArrayList<>();
    List<ProcessStepActionDTO> processStepListForHTP = new ArrayList<>();
    List<ProcessStepActionDTO> processStepListForCreateRunResultFromQM = new ArrayList<>();
    List<ProcessStepActionDTO> processStepListForCreateRunResultForGetRunResultSSU = new ArrayList<>();
    List<OrderDTO> ordersFromOMM = new ArrayList<>();
    List<OrderDTO> ordersFromOMMListElse = new ArrayList<>();
    
    
    @BeforeTest public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        listSampleDTO.add(getSampleResultsDTO());
        htpSampleStatus.add(getHTPStatusMessage());
        listOfTestOptionsDTO.add(getDeviceTestOptionsDTO());
        deviceTestOptions.add(getDeviceTestOptionsDTO());
        processStepList.add(getProcessStepActionDTO());
        processStepListForSSU.add(getProcessStepActionDTOForSSU());
        processStepListForHTP.add(getProcessStepActionDTO());
        processStepListForHTP.add(getProcessStepActionDTOForSSU());
        
        processStepListForCreateRunResultFromQM.add(getProcessStepActionDTO());
        processStepListForCreateRunResultFromQM.add(getProcessStepActionDTOForSSU());
        processStepListForCreateRunResultFromQM.add(getProcessStepActionDTOForQM());
        
        ordersFromOMM.add(getOrder());
        ordersFromOMMListElse.add(getOrderElse());
        processStepListForCreateRunResultForGetRunResultSSU.add(getProcessStepActionDTOForgetRunResultForSSU());
    }
    
    public ProcessStepActionDTO getProcessStepActionDTOForSSU() {
        ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
        processStepActionDTO.setProcessStepName("LPSEQPREP");
        processStepActionDTO.setProcessStepSeq(2);
        processStepActionDTO.setDeviceType("HTP");
        return processStepActionDTO;
    }
    
    public ProcessStepActionDTO getProcessStepActionDTOForQM() {
        ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
        processStepActionDTO.setProcessStepName("LPSEQPREP");
        processStepActionDTO.setProcessStepSeq(2);
        processStepActionDTO.setDeviceType("HTP");
        return processStepActionDTO;
    }
    
    public ProcessStepActionDTO getProcessStepActionDTO() {
        ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
        processStepActionDTO.setProcessStepName("LPSEQPREP");
        processStepActionDTO.setProcessStepSeq(3);
        processStepActionDTO.setDeviceType("HTP");
        return processStepActionDTO;
    }
    
    public ProcessStepActionDTO getProcessStepActionDTOForgetRunResultForSSU() {
        ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
        processStepActionDTO.setProcessStepName("LPSEQPREP");
        processStepActionDTO.setProcessStepSeq(4);
        processStepActionDTO.setDeviceType("HTP");
        return processStepActionDTO;
    }
    
    public SampleResultsDTO getSampleResultsDTO() {
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setResult("Aborted");
        sampleResultsDTO.setAccesssioningId("8001");
        sampleResultsDTO.setRunResultsId(1L);
        return sampleResultsDTO;
        
    }
    
    public SampleResultsDTO getSampleResultsDTOForHTP() {
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setResult("Aborted");
        sampleResultsDTO.setAccesssioningId("8001");
        sampleResultsDTO.setRunResultsId(1L);
        sampleResultsDTO.setOutputContainerId("1");
        sampleResultsDTO.setInputContainerId("1");
        sampleResultsDTO.setOrderId(100L);
        return sampleResultsDTO;
        
    }
    
    public DeviceTestOptionsDTO getDeviceTestOptionsDTO() {
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("HTP Protocol");
        return deviceTestOptionsDTO;
    }
    
    public HtpStatusMessage getHTPStatusMessage() {
        HtpStatusMessage HtpStatusMessage = new HtpStatusMessage();
        return HtpStatusMessage;
    }
    
    /**
     * We need a special {@link IObjectFactory}.
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test public void processNoOrderFoundResponseTest() throws HMTPException {
        PowerMockito.mockStatic(RestClientUtil.class);
        String adaptorSubmitUrl = "http://localhost";
        Mockito.when(RestClientUtil.postMethodCall(adaptorSubmitUrl, getAdaptorResponseMessageForNFResponse(), null))
            .thenReturn("postResponse");
        messageProcessorService.processNoOrderFoundResponse(getAdaptorRequestMessage(), "Success");
    }
    
    @Test public void processLPSequenceRequestOrAssayMismatchTest() throws HMTPException, JsonProcessingException {
        Mockito.when(ommService.getOrderByOrderId(Mockito.anyLong())).thenReturn(getOrderForAssayMismatch());
        Mockito.doNothing().when(messageService).saveMessage(Mockito.any(ResponseMessage.class));
        messageProcessorService.processLPSequenceRequestOrAssayMismatch(getQueryMessage(), 7001L, "protocolName",
            "Success");
    }
    
    @Test public void
        processLPSequenceRequestOrAssayMismatchNegativeTest() throws HMTPException, JsonProcessingException {
        Mockito.when(ommService.getOrderByOrderId(Mockito.anyLong())).thenReturn(null);
        Mockito.doNothing().when(messageService).saveMessage(Mockito.any(ResponseMessage.class));
        messageProcessorService.processLPSequenceRequestOrAssayMismatch(getQueryMessage(), 7001L, "protocolName",
            "NOORDER");
    }
    
    @Test public void getSampleResultsByProcessStepAndInputContainerIdAndInputContainerPositionPositiveTest()
        throws UnsupportedEncodingException {
        ReflectionTestUtils.setField(messageProcessorService, "rmmGetSampleResult", "http://localhost");
        Mockito.when(rmmService.getSampleResultsFromUrl(Mockito.anyString())).thenReturn(listSampleDTO);
        messageProcessorService.getSampleResultsByProcessStepAndInputContainerIdAndInputContainerPosition("LPSEQPREP",
            "78999_A1", "1");
    }
    
    @Test public void getSampleResultsByProcessStepAndInputContainerIdAndInputContainerPositionNegativeTest()
        throws UnsupportedEncodingException {
        ReflectionTestUtils.setField(messageProcessorService, "rmmGetSampleResult", null);
        messageProcessorService.getSampleResultsByProcessStepAndInputContainerIdAndInputContainerPosition("LPSEQPREP",
            "78999_A1", "1");
    }
    
    @Test public void getRunResultByDeviceIdAndProcessStepNamePositiveTest() throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(URLEncoder.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultUrl", "http://localhost");
        // String url ="http://localhost?deviceId=MP24-001&processStepName=NA
        // Extraction";
        String encodeUrl = "http%3A%2F%2Flocalhost%3FdeviceId%3DMP24-001%26processStepName%3DNA+Extraction";
        Mockito.when(RestClientUtil.getBuilder(encodeUrl, null)).thenReturn(responseClient);
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        messageProcessorService.getRunResultByDeviceIdAndProcessStepName("MP24-001", "NA Extraction");
    }
    
    @Test public void getRunResultByDeviceIdAndProcessStepNameNullCheckTest() throws UnsupportedEncodingException {
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultUrl", null);
        messageProcessorService.getRunResultByDeviceIdAndProcessStepName("MP24-001", "NA Extraction");
    }
    
    @Test public void getRunResultByDeviceIdAndProcessStepNameNegativeTest() throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(URLEncoder.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultUrl", "http://localhost");
        String encodeUrl = "http%3A%2F%2Flocalhost%3FdeviceId%3DMP24-001%26processStepName%3DNA+Extraction";
        Mockito.when(RestClientUtil.getBuilder(encodeUrl, null)).thenReturn(responseClient);
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(400);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        messageProcessorService.getRunResultByDeviceIdAndProcessStepName("MP24-001", "NA Extraction");
    }
    
    @Test public void getRunResultTest() throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultByDeviceIdUrl", "http://localhost");
        String encodeUrl = "http%3A%2F%2Flocalhost%3FdeviceRunId%3DHTP-01";
        Mockito.when(RestClientUtil.getBuilder(encodeUrl, null)).thenReturn(responseClient);
        Mockito.when(responseClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        messageProcessorService.getRunResult(getHTPStatus());
    }
    
    @Test public void getRunResultByDeviceRunIdAndProcessStepTest() throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmSampleResultUrl", "http://localhost");
        String encodeUrl = "http%3A%2F%2Flocalhost%3FdeviceRunId%3D6789%26processStepName%3DNA+Extraction";
        Mockito.when(RestClientUtil.getBuilder(encodeUrl, null)).thenReturn(responseClient);
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        messageProcessorService.getRunResult("6789", "NA Extraction");
    }
    
    @Test public void getRunResultByDeviceRunIdAndProcessStepNegativeTest() throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmSampleResultUrl", "http://localhost");
        String encodeUrl = "http%3A%2F%2Flocalhost%3FdeviceRunId%3D6789%26processStepName%3DNA+Extraction";
        Mockito.when(RestClientUtil.getBuilder(encodeUrl, null)).thenReturn(responseClient);
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(400);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        messageProcessorService.getRunResult("6789", "NA Extraction");
    }
    
    @Test public void getRunResultByDeviceRunIdAndProcessStepDeviceSerialNumberTest()
        throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmSampleResultUrl", "http://localhost");
        String encodeUrl =
            "http%3A%2F%2Flocalhost%3FdeviceRunId%3D6789%26processStepName%3DNA+Extraction%26deviceSerialNumber%3DMP24-001";
        Mockito.when(RestClientUtil.getBuilder(encodeUrl, null)).thenReturn(responseClient);
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        messageProcessorService.getRunResult("6789", "NA Extraction", "MP24-001");
    }
    
    @Test public void getRunResultByDeviceRunIdAndProcessStepDeviceSerialNumberNegativeTest()
        throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmSampleResultUrl", "http://localhost");
        String encodeUrl =
            "http%3A%2F%2Flocalhost%3FdeviceRunId%3D6789%26processStepName%3DNA+Extraction%26deviceSerialNumber%3DMP24-001";
        Mockito.when(RestClientUtil.getBuilder(encodeUrl, null)).thenReturn(responseClient);
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(400);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        messageProcessorService.getRunResult("6789", "NA Extraction", "MP24-001");
    }
    
    @Test public void getRunResultByDeviceRunIdAndProcessStepDeviceSerialNumberNullCheckTest()
        throws UnsupportedEncodingException {
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultUrl", null);
        messageProcessorService.getRunResult("6789", "NA Extraction", "MP24-001");
    }
    
    @Test public void getSampleResultsByProcessStepAndOutputContainerIdAndPositionTest()
        throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmGetSampleResult", "http://localhost");
        Mockito.when(rmmService.getSampleResultsFromUrl(Mockito.anyString())).thenReturn(listSampleDTO);
        messageProcessorService.getSampleResultsByProcessStepAndOutputContainerIdAndPosition("NA Extraction", "456789",
            "2");
    }
    
    @Test public void getSampleResultsByProcessStepAndOutputContainerIdAndPositionNullCheckTest()
        throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmGetSampleResult", null);
        Mockito.when(rmmService.getSampleResultsFromUrl(Mockito.anyString())).thenReturn(listSampleDTO);
        messageProcessorService.getSampleResultsByProcessStepAndOutputContainerIdAndPosition("NA Extraction", "456789",
            "2");
    }
    
    @Test public void submitHTPSampleStatusNullCheckTest() {
        ReflectionTestUtils.setField(messageProcessorService, "wfmUpdateRequestUrl", null);
        messageProcessorService.submitHtpSampleStatus(htpSampleStatus, "token");
    }
    
    @Test public void submitHTPSampleStatusPositiveTest() {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "wfmUpdateRequestUrl", "http://localhost");
        String url = "http://localhost/htpstatus";
        Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        Mockito.when(builder.post(Entity.entity(getHTPStatusMessage(), MediaType.APPLICATION_JSON))).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        messageProcessorService.submitHtpSampleStatus(htpSampleStatus, "token");
    }
    @Test public void getListOfTestOptionsTest() throws UnsupportedEncodingException, HMTPException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "ammTestOptionsUrl", "http://localhost");
        String url = "http://localhost";
        Mockito.when(RestClientUtil.getUrlString("http://localhost" + 1, "", "",
            "/devicetestoptions?deviceType=" + "LP24", null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, NIPTProcessStepConstants.CHAR_SET), null))
            .thenReturn(assayClient);
        Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {}))
            .thenReturn(listOfTestOptionsDTO);
        messageProcessorService.getListOfTestOptions(1L, "LP24");
    }
    
    @Test public void getTestProtocolByByAssayTypeAndDeviceTypeTest() {
        Mockito.when(assayService.getDeviceTestOptionsData(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(listOfTestOptionsDTO);
        messageProcessorService.getTestProtocolByByAssayTypeAndDeviceType("NIPTHTP", "MP24");
    }
    
    @Test public void updateRunResultFromSSUAbortedTest() {
        messageProcessorService.updateRunResultFromSSU(getRunResultsDTOForUpdateSSUAborted(),
            getSpecimenStatusUpdateMessageAborted());
    }
    
    @Test public void updateRunResultFromSSUPassedWithFlagTest() {
        messageProcessorService.updateRunResultFromSSU(getRunResultsDTOForUpdateSSUPassedWithFlag(),
            getSpecimenStatusUpdateMessagePassedWithFlag());
    }
    
    @Test public void updateRunResultFromSSUCompletedTest() {
        messageProcessorService.updateRunResultFromSSU(getRunResultsDTOForUpdateSSUCompleted(),
            getSpecimenStatusUpdateCompleted());
    }
    
    @Test public void processRequestTest() {
        PowerMockito.mockStatic(RestClientUtil.class);
        String url="http://localhost";
        ReflectionTestUtils.setField(messageProcessorService, "wfmUpdateRequestUrl", url);
        Mockito.when(RestClientUtil.getBuilder("http://localhost/LPStatus", null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        Mockito.when(builder.post(Entity.entity(getSpecimenStatusUpdateMessageForProcessRequest(), MediaType.APPLICATION_JSON))).thenReturn(response);
        messageProcessorService.processRequest(getSpecimenStatusUpdateMessageForProcessRequest(), "token");
    }
    
    @Test public void processRequestNullCheckTest() {
        ReflectionTestUtils.setField(messageProcessorService, "wfmUpdateRequestUrl", null);
        messageProcessorService.processRequest(getSpecimenStatusUpdateMessageForProcessRequest(), "token");
    }
    
    
    @Test public void processRequestQueryPositiveTest() {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "wfmUpdateRequestUrl", "http://localhost");
        Mockito.when(RestClientUtil.getBuilder("http://localhost/LPQuery", null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        Mockito.when(builder.post(Entity.entity(getSpecimenStatusUpdateMessageForProcessRequest(), MediaType.APPLICATION_JSON))).thenReturn(response);
        messageProcessorService.processRequest(getQueryMessage(), "token");
    }
    
    @Test public void processRequestQueryNullCheckTest() {
        ReflectionTestUtils.setField(messageProcessorService, "wfmUpdateRequestUrl", null);
        messageProcessorService.processRequest(getQueryMessage(), "token");
    }
    
    @Test
    public void processLPSequenceRequestTest() {
        messageProcessorService.processLPSequenceRequest(getSpecimenStatusUpdateMessageForProcessRequest());
    }
    
    @Test(expectedExceptions  = InvalidStateException.class)
    public void checkIsValidStatusForSSUTest() throws Exception {
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when((String)ThreadSessionManager.currentUserSession().getObject(Mockito.anyString())).thenReturn("token");
        ReflectionTestUtils.setField(messageProcessorService, "admHostUrl", "http://localhost");
        List<String> content = new ArrayList<>();
        content.add("LP");
        content.add("8001");
        PowerMockito.doNothing().when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        messageProcessorService.checkIsValidStatusForSSU(getRunResultsDTO(), getSpecimenStatusUpdateCompleted());
    }
    
    @Test
    public void processLPSeqRequestNullCheckTest() throws IOException {
        Mockito.when(assayService.getProcessStepAction(Mockito.anyString(),Mockito.anyString())).thenReturn(processStepList);
        Mockito.when(assayService.getTestOptionsByOrderIdandDeviceandProcessStep(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
        ReflectionTestUtils.setField(messageProcessorService, "rmmGetSampleResult", null);
        messageProcessorService.processLpSeqRequest(getQueryMessage(),8989, "token");
    }
    
    @Test
    public void processLPSeqRequestPositiveTest() throws IOException {
        Mockito.when(assayService.getProcessStepAction(Mockito.anyString(),Mockito.anyString())).thenReturn(processStepList);
        Mockito.when(assayService.getTestOptionsByOrderIdandDeviceandProcessStep(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(deviceTestOptions);
        ReflectionTestUtils.setField(messageProcessorService, "rmmGetSampleResult", "http://localhost");
        Mockito.when(rmmService.getSampleResultsFromUrl(Mockito.anyString())).thenReturn(listSampleDTO);
        messageProcessorService.processLpSeqRequest(getQueryMessage(),8989, "token");
    }
 
    @Test
    public void getComplexIdDetailsByComplexIdTest() throws HMTPException, UnsupportedEncodingException {
        Mockito.when(assayService.getProcessStepAction(AssayType.NIPT_HTP, null)).thenReturn(processStepListForHTP);
        ReflectionTestUtils.setField(messageProcessorService, "rmmGetSampleResult", "http://localhost");
        Mockito.when(rmmService.getSampleResultsFromUrl(Mockito.anyString())).thenReturn(listSampleDTO);
        Mockito.when(rmmService.getRunResultsById(Mockito.anyLong())).thenReturn(getRunResultsDTOForUpdateSSUCompleted());
        messageProcessorService.getComplexIdDetailsByComplexId("7878");
    }
    
    
    @Test
    public void getComplexIdDetailsByComplexIdReadyTest() throws HMTPException, UnsupportedEncodingException {
        Mockito.when(assayService.getProcessStepAction(AssayType.NIPT_HTP, null)).thenReturn(processStepListForHTP);
        ReflectionTestUtils.setField(messageProcessorService, "rmmGetSampleResult", "http://localhost");
        Mockito.when(rmmService.getRunResultsById(Mockito.anyLong())).thenReturn(getRunResultsDTOForUpdateSSUCompleted());
        List<SampleResultsDTO>  emptySampleList = new ArrayList<>();
        Mockito.when(rmmService.getSampleResultsFromUrl(Mockito.anyString())).thenReturn(emptySampleList);
        messageProcessorService.getComplexIdDetailsByComplexId("7878");
    }
    
    @Test
    public void saveRunResultTest() {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultUrl", "http://localhost");
        Mockito.when( RestClientUtil.postOrPutBuilder("http://localhost", null)).thenReturn(builder);
        Mockito.when( RestClientUtil.postOrPutBuilder("http://localhost", null)
                .post(Entity.entity(getRunResultsDTO(), MediaType.APPLICATION_JSON))).thenReturn(res);
        Mockito.when(res.getStatus()).thenReturn(200);
        messageProcessorService.saveRunResult(getRunResultsDTO());
    }
    
    @Test
    public void submitResponseTest() throws HMTPException {
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.when(RestClientUtil.postMethodCall(null, getAdaptorRequestMessage(), null)).thenReturn("nothing");
        messageProcessorService.submitResponse(getAdaptorRequestMessage());
    }
    
    @Test
    public void createRunResultFromQMTest() throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultUrl", "http://localhost");
        Mockito.when(assayService.getProcessStepAction(Mockito.anyString(),Mockito.anyString())).thenReturn(processStepListForCreateRunResultFromQM);
        String url="http://localhost?deviceId=LPSEQPREP&processStepName=LPSEQPREP";
        Mockito.when( RestClientUtil
                .getBuilder(URLEncoder.encode(url, NIPTProcessStepConstants.CHAR_SET), null)).thenReturn(responseClient);
        
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
            })).thenReturn(getRunResultsDTOForQM());
   
        messageProcessorService.createRunResultFromQM(getQueryMessage());
    }
    
    @Test
    public void createRunResultFromQMNullCheckTest() throws UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultUrl", "http://localhost");
        Mockito.when(assayService.getProcessStepAction(Mockito.anyString(),Mockito.anyString())).thenReturn(processStepListForCreateRunResultFromQM);
        String url="http://localhost?deviceId=LPSEQPREP&processStepName=LPSEQPREP";
        Mockito.when( RestClientUtil
                .getBuilder(URLEncoder.encode(url, NIPTProcessStepConstants.CHAR_SET), null)).thenReturn(responseClient);
        
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
            })).thenReturn(null);
        
        Mockito.when( RestClientUtil.postOrPutBuilder("http://localhost", null)).thenReturn(builder);
        Mockito.when( RestClientUtil.postOrPutBuilder("http://localhost", null)
                .post(Entity.entity(getRunResultsDTOForQM(), MediaType.APPLICATION_JSON))).thenReturn(res);
        Mockito.when(res.getStatus()).thenReturn(200);
        messageProcessorService.createRunResultFromQM(getQueryMessage());
    }
    
    @Test
    public void processRequestAdaptorRequestMessageTest() {
        messageProcessorService.processRequest(getAdaptorRequestMessage(), "token");
    }
    
   
    
    @Test
    public void processRequestAdaptorRequestMessageNullCheckTest() throws IOException, HMTPException {
        ReflectionTestUtils.setField(messageProcessorService, "wfmQBPRequestUrl", "http://localhost");
        Mockito.when(ommService.getOrder(getAdaptorRequestMessageForNAExtraction().getAccessioningId(), "token")).thenReturn(ordersFromOMM);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.when(RestClientUtil.getBuilder("url", null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        Mockito.when(builder.post(Entity.entity(getAdaptorRequestMessageForNAExtraction(), MediaType.APPLICATION_JSON))).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
    	Mockito.when(objectMapper.writeValueAsString(getAdaptorRequestMessageForNAExtraction())).thenReturn("sample");
        
        messageProcessorService.processRequest(getAdaptorRequestMessageForNAExtraction(), "token");
    }
    
    
    @Test
    public void processRequestAdaptorRequestMessageNullCheckTestElse() throws Exception {
        ReflectionTestUtils.setField(messageProcessorService, "wfmQBPRequestUrl", "http://localhost");
        ReflectionTestUtils.setField(messageProcessorService, "mp24AdaptorHostUrl", "http://localhost");
        
        Mockito.when(ommService.getOrder(getAdaptorRequestMessageForNAExtraction().getAccessioningId(), "token")).thenReturn(ordersFromOMMListElse);
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing().when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        messageProcessorService.processRequest(getAdaptorRequestMessageForNAExtraction(), "token");
        Mockito.when(objectMapper.writeValueAsString(getAdaptorRequestMessageForNAExtraction())).thenReturn("sample");
        Mockito.when(RestClientUtil.postMethodCall(null, getAdaptorRequestMessage(), null)).thenReturn("nothing");
        messageProcessorService.processRequest(getAdaptorRequestMessageForNAExtraction(), "token");
    }
    
    
    @Test
    public void processRequestAdaptorRequestMessageOrderNullCheckTest() throws Exception {
        ReflectionTestUtils.setField(messageProcessorService, "wfmQBPRequestUrl", "http://localhost");
        Mockito.when(ommService.getOrder(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<OrderDTO>());
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing().when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        messageProcessorService.processRequest(getAdaptorRequestMessageForNAExtraction(), "token");
    }
    
    
    
    @Test
    public void processLPSeqRequestTest() throws UnsupportedEncodingException, HMTPException, InvalidStateException {
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultByDeviceIdUrl", "http://localhost");
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultUrl", "http://localhost");
        List<ProcessStepActionDTO> list = new ArrayList<>();
        list.add(getProcessStepActionDTOForgetRunResultForSSU());
        Mockito.when(assayService.getProcessStepAction(Mockito.anyString(),Mockito.anyString())).thenReturn(list);
        String encodeUrl = "http://localhost?deviceRunId=orderName&processStepName=LPSEQPREP&deviceSerialNumber=LP001";
        //Mockito.when(RestClientUtil.getBuilder(encodeUrl, null)).thenReturn(responseClient);
        Mockito.when(RestClientUtil
                .getBuilder(URLEncoder.encode(encodeUrl, NIPTProcessStepConstants.CHAR_SET), null)).thenReturn(responseClient);
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {
            })).thenReturn(getRunResultsDTO());
        Mockito.when( RestClientUtil.postOrPutBuilder("http://localhost", null)).thenReturn(builder);
        Mockito.when( RestClientUtil.postOrPutBuilder("http://localhost", null)
                .post(Entity.entity(getRunResultsDTO(), MediaType.APPLICATION_JSON))).thenReturn(res);
        Mockito.when(res.getStatus()).thenReturn(200);
        String nextUrl ="http://localhost?deviceRunId=orderName&processStepName=LPSEQPREP";
        Mockito.when(RestClientUtil
            .getBuilder(URLEncoder.encode(nextUrl, NIPTProcessStepConstants.CHAR_SET), null)).thenReturn(responseClient);
        messageProcessorService.processLpSeqRequest(getSpecimenStatusUpdateMessageForProcessRequest(), "token");
    }

    @Test public void
        getRunResultForSSUTest() throws UnsupportedEncodingException, HMTPException, InvalidStateException {
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.when(assayService.getProcessStepAction(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(processStepListForCreateRunResultForGetRunResultSSU);
        ReflectionTestUtils.setField(messageProcessorService, "rmmSampleResultUrl", "http://localhost");
        String url = "http://localhost?deviceRunId=orderName&processStepName=LPSEQPREP&deviceSerialNumber=LP";
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, NIPTProcessStepConstants.CHAR_SET), null))
            .thenReturn(responseClient);
        Mockito.when(responseClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        
        ReflectionTestUtils.setField(messageProcessorService, "rmmRunResultUrl", "http://localhost");
        Mockito.when( RestClientUtil.postOrPutBuilder("http://localhost", null)).thenReturn(builder);
        Mockito.when( RestClientUtil.postOrPutBuilder("http://localhost", null)
                .post(Entity.entity(getRunResultsDTO(), MediaType.APPLICATION_JSON))).thenReturn(res);
        Mockito.when(res.getStatus()).thenReturn(200);
        
        String url2 = "http://localhost?deviceRunId=orderName&processStepName=LPSEQPREP";
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url2, NIPTProcessStepConstants.CHAR_SET), null))
            .thenReturn(responseClient);
        
        messageProcessorService.getRunResultForSSU(getSpecimenStatusUpdateCompleted());
    }
    
    @Test
    public void updateRunResultsCreatedTest() throws HMTPException, UnsupportedEncodingException {
        Mockito.when(rmmService.getRunResultsByDeviceRunId(Mockito.anyString())).thenReturn(getRunResultsDTOCREATED());
        Mockito.when(assayService.getProcessStepAction(AssayType.NIPT_HTP,
                        null)).thenReturn(processStepList);
        messageProcessorService.updateRunResults(getRunResultsDTOCREATED());
    }
    
    @Test
    public void updateRunResultsStartedTest() throws HMTPException, UnsupportedEncodingException {
        Mockito.when(rmmService.getRunResultsByDeviceRunId(Mockito.anyString())).thenReturn(getRunResultsDTOSTARTED());
        Mockito.when(assayService.getProcessStepAction(AssayType.NIPT_HTP,
                        null)).thenReturn(processStepListForHTP);
        ReflectionTestUtils.setField(messageProcessorService, "rmmGetSampleResult", "http://localhost");
        Mockito.when(rmmService.getSampleResultsFromUrl(Mockito.anyString())).thenReturn(listSampleDTO);
        messageProcessorService.updateRunResults(getRunResultsDTOSTARTED());
    }
    
    
    public RunResultsDTO getRunResultsDTOCREATED() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        Collection<SampleResultsDTO> sampleResults = new ArrayList<>();
        sampleResults.add(getSampleResultsDTO());
        runResultsDTO.setSampleResults(sampleResults );
        runResultsDTO.setAssayType("NIPTHTP");
        runResultsDTO.setRunStatus("CREATED");
        runResultsDTO.setComments("comments");
        runResultsDTO.setUpdatedBy("admin");
        runResultsDTO.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        runResultsDTO.setDeviceId("HTP-001");
        runResultsDTO.setDeviceRunId("9001");
        runResultsDTO.setDvcRunResult("Passed");
        runResultsDTO.setOperatorName("operator Name");
        runResultsDTO.setProcessStepName("HTP");
        runResultsDTO.setRunCompletionTime(new Timestamp(System.currentTimeMillis()));
        runResultsDTO.setRunFlag("F1");
        runResultsDTO.setRunRemainingTime(2000L);
        runResultsDTO.setRunStartTime(new Timestamp(System.currentTimeMillis()));
        runResultsDTO.setTotalSamplecount("20");
        runResultsDTO.setCreatedBy("admin");
        runResultsDTO.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        runResultsDTO.setVerifiedBy("admin");
        runResultsDTO.setVerifiedDate(new Timestamp(System.currentTimeMillis()));
        
        Collection<RunReagentsAndConsumablesDTO> runReagentsAndConsumables = new ArrayList<>();
        RunReagentsAndConsumablesDTO reagentsDTO = new RunReagentsAndConsumablesDTO();
        reagentsDTO.setAttributeName("attributeName");
        reagentsDTO.setAttributeValue("attributeValue");
        reagentsDTO.setCreatedBy("admin");
        reagentsDTO.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        reagentsDTO.setUpdatedBy("admin");
        reagentsDTO.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        reagentsDTO.setRunReagentsAndConsumablesId(1L);
        
        runReagentsAndConsumables.add(reagentsDTO);
        
        Collection<RunResultsDetailDTO> runResultsDetail= new ArrayList<>();
        RunResultsDetailDTO run = new RunResultsDetailDTO();
        run.setAttributeName("attributeName");
        run.setAttributeValue("attributeValue");
        run.setCreatedBy("admin");
        run.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        run.setRunResultsDetailsId(1L);
        run.setUpdatedBy("admin");
        run.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        runResultsDetail.add(run);
        
        
        runResultsDTO.setRunReagentsAndConsumables(runReagentsAndConsumables );
        runResultsDTO.setRunResultsDetail(runResultsDetail);
        return runResultsDTO;
        
    }
    
    
    public RunResultsDTO getRunResultsDTOSTARTED() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        Collection<SampleResultsDTO> sampleResults = new ArrayList<>();
        sampleResults.add(getSampleResultsDTOForHTP());
        runResultsDTO.setSampleResults(sampleResults );
        runResultsDTO.setAssayType("NIPTHTP");
        runResultsDTO.setRunStatus("Started");
        runResultsDTO.setComments("comments");
        runResultsDTO.setUpdatedBy("admin");
        runResultsDTO.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        runResultsDTO.setDeviceId("HTP-001");
        runResultsDTO.setDeviceRunId("9001");
        runResultsDTO.setDvcRunResult("Passed");
        runResultsDTO.setOperatorName("operator Name");
        runResultsDTO.setProcessStepName("HTP");
        runResultsDTO.setRunCompletionTime(new Timestamp(System.currentTimeMillis()));
        runResultsDTO.setRunFlag("F1");
        runResultsDTO.setRunRemainingTime(2000L);
        runResultsDTO.setRunStartTime(new Timestamp(System.currentTimeMillis()));
        runResultsDTO.setTotalSamplecount("20");
        runResultsDTO.setCreatedBy("admin");
        runResultsDTO.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        runResultsDTO.setVerifiedBy("admin");
        runResultsDTO.setVerifiedDate(new Timestamp(System.currentTimeMillis()));
        
        Collection<RunReagentsAndConsumablesDTO> runReagentsAndConsumables = new ArrayList<>();
        RunReagentsAndConsumablesDTO reagentsDTO = new RunReagentsAndConsumablesDTO();
        reagentsDTO.setAttributeName("attributeName");
        reagentsDTO.setAttributeValue("attributeValue");
        reagentsDTO.setCreatedBy("admin");
        reagentsDTO.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        reagentsDTO.setUpdatedBy("admin");
        reagentsDTO.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        reagentsDTO.setRunReagentsAndConsumablesId(1L);
        
        runReagentsAndConsumables.add(reagentsDTO);
        
        Collection<RunResultsDetailDTO> runResultsDetail= new ArrayList<>();
        RunResultsDetailDTO run = new RunResultsDetailDTO();
        run.setAttributeName("complexId");
        run.setAttributeValue("9001");
        run.setCreatedBy("admin");
        run.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        run.setRunResultsDetailsId(1L);
        run.setUpdatedBy("admin");
        run.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        runResultsDetail.add(run);
        
        
        runResultsDTO.setRunReagentsAndConsumables(runReagentsAndConsumables );
        runResultsDTO.setRunResultsDetail(runResultsDetail);
        runResultsDTO.setSampleResults(sampleResults);
        return runResultsDTO;
        
    }
    
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessageForProcessRequest() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        specimenStatusUpdateMessage.setMessageType(EnumMessageType.StatusUpdateMessage);
        specimenStatusUpdateMessage.setSendingApplicationName("LP");
        specimenStatusUpdateMessage.setContainerId("45678_A1");
        specimenStatusUpdateMessage.setDeviceSerialNumber("LP001");
        specimenStatusUpdateMessage.setReceivingApplication("Connect");
        specimenStatusUpdateMessage.setMessageControlId("345678");
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setOrderName("orderName");
        statusUpdate.setOrderResult("Aborted");
        statusUpdate.setOperatorName("Operator Name");
        statusUpdate.setRunEndTime(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setRunStartTime(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setUpdatedBy("Admin");
        statusUpdate.setComment("comment");
        statusUpdate.setRunResult("Passed");
        List<Consumable> consumables = new ArrayList<>();
        consumables.add(getConsumable());
        statusUpdate.setConsumables(consumables);
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate);
        return specimenStatusUpdateMessage;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessageAborted() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setOrderResult("Aborted");
        statusUpdate.setOperatorName("Operator Name");
        statusUpdate.setRunEndTime(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setRunStartTime(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setUpdatedBy("Admin");
        statusUpdate.setComment("comment");
        List<Consumable> consumables = new ArrayList<>();
        consumables.add(getConsumable());
        statusUpdate.setConsumables(consumables);
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate);
        return specimenStatusUpdateMessage;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessagePassedWithFlag() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setOrderResult("passed with flag");
        statusUpdate.setOperatorName("Operator Name");
        statusUpdate.setRunEndTime(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setRunStartTime(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setUpdatedBy("Admin");
        statusUpdate.setComment("comment");
        List<Consumable> consumables = new ArrayList<>();
        consumables.add(getConsumable());
        statusUpdate.setConsumables(consumables);
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate);
        return specimenStatusUpdateMessage;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateCompleted() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setOrderResult("Completed");
        statusUpdate.setRunResult("Passed");
        statusUpdate.setOrderName("orderName");
        statusUpdate.setOperatorName("Operator Name");
        statusUpdate.setRunEndTime(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setRunStartTime(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setUpdatedBy("Admin");
        statusUpdate.setComment("comment");
        List<Consumable> consumables = new ArrayList<>();
        consumables.add(getConsumable());
        statusUpdate.setConsumables(consumables);
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate);
        specimenStatusUpdateMessage.setDeviceSerialNumber("LP");
        return specimenStatusUpdateMessage;
    }
    
    public Consumable getConsumable() {
        Consumable consumable = new Consumable();
        consumable.setName("system_fluid_part_number");
        consumable.setValue("xyz");
        return consumable;
        
    }
    
    public HtpStatus getHTPStatus() {
        HtpStatus htpStatus = new HtpStatus();
        htpStatus.setDeviceRunId("HTP-01");
        return htpStatus;
    }
    
    public RunResultsDTO getRunResultsDTO() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        Collection<SampleResultsDTO> sampleResults = new ArrayList<>();
        sampleResults.add(getSampleResultsDTO());
        runResultsDTO.setSampleResults(sampleResults );
        return runResultsDTO;
        
    }
    
    public RunResultsDTO getRunResultsDTOForQM() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        runResultsDTO.setDeviceId("LPSEQPREP");
        runResultsDTO.setRunStatus(NIPTProcessStepConstants.IN_PROGRESS);
        runResultsDTO.setAssayType("NIPTHTP");
        return runResultsDTO;
        
    }
    
    public RunResultsDTO getRunResultsDTOForUpdateSSUAborted() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        runResultsDTO.setRunStatus("Aborted");
        return runResultsDTO;
    }
    
    public RunResultsDTO getRunResultsDTOForUpdateSSUCompleted() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        runResultsDTO.setRunStatus("Completed");
        return runResultsDTO;
        
    }
    
    public RunResultsDTO getRunResultsDTOForUpdateSSUPassedWithFlag() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        runResultsDTO.setRunStatus("passed with flag");
        return runResultsDTO;
        
    }
    
    public OrderDTO getOrderForAssayMismatch() {
        OrderDTO order = new OrderDTO();
        order.setSampleType("Blood");
        return order;
    }
    
    public OrderDTO getOrder() {
        OrderDTO order = new OrderDTO();
        order.setSampleType("Blood");
        order.setAssayType("NIPTHTP");
        return order;
    }
    
    
    public OrderDTO getOrderElse() {
        OrderDTO order = new OrderDTO();
        order.setSampleType("Blood");
        order.setAssayType("NIPT");
        return order;
    }
    
    public QueryMessage getQueryMessage() {
        QueryMessage queryMessage = new QueryMessage();
        queryMessage.setContainerId("7878_A1");
        queryMessage.setDeviceSerialNumber("LPSEQPREP");
        queryMessage.setSendingApplicationName("LP");
        queryMessage.setReceivingApplication("Connect");
        queryMessage.setMessageControlId("999990");
        queryMessage.setMessageType(EnumMessageType.QueryMessage);
        queryMessage.setProcessStepName("LPSEQPREP");
        return queryMessage;
        
    }
    
    public AdaptorRequestMessage getAdaptorRequestMessage() {
        AdaptorRequestMessage adaptorRequestMessage = new AdaptorRequestMessage();
        adaptorRequestMessage.setMessageControlId("999999");
        adaptorRequestMessage.setAccessioningId("8001");
        adaptorRequestMessage.setMessageType("None");
        adaptorRequestMessage.setSendingApplicationName("None");
        return adaptorRequestMessage;
    }
    public AdaptorRequestMessage getAdaptorRequestMessageForNAExtraction() {
        AdaptorRequestMessage adaptorRequestMessage = new AdaptorRequestMessage();
        adaptorRequestMessage.setMessageControlId("999999");
        adaptorRequestMessage.setAccessioningId("8001");
        adaptorRequestMessage.setMessageType("NA-Extraction");
        adaptorRequestMessage.setSendingApplicationName("LP");
        return adaptorRequestMessage;
    }
    public AdaptorResponseMessage getAdaptorResponseMessageForNFResponse() {
        AdaptorResponseMessage adaptorResponseMessage = new AdaptorResponseMessage();
        com.roche.connect.common.mp24.message.Response response = new com.roche.connect.common.mp24.message.Response();
        response.setMessageControlId(getAdaptorRequestMessage().getMessageControlId());
        response.setAccessioningId(getAdaptorRequestMessage().getAccessioningId());
        response.setMessageType(MessageType.RSP);
        RSPMessage rspMessage = new RSPMessage();
        SampleInfo sampleInfo = new SampleInfo();
        rspMessage.setOrderControl("DC");
        rspMessage.setSampleInfo(sampleInfo);
        rspMessage.setQueryResponseStatus("Success");
        response.setRspMessage(rspMessage);
        adaptorResponseMessage.setStatus(IMMConstants.NOORDER);
        adaptorResponseMessage.setResponse(response);
        return adaptorResponseMessage;
    }
    
    @Test
    public void get() {
        messageProcessorService.setAmmTestOptionsUrl("http://localhost");
        messageProcessorService.setAmmHostUrl("http://localhost");
        messageProcessorService.setAmmProcessStepActionUrl("http://localhost");
        messageProcessorService.setAmmDeviceTestOptionsUrl("http://localhost");
        messageProcessorService.setRmmGetSampleResult("Passed");
        System.out.println(messageProcessorService.getAmmDeviceTestOptionsUrl()
            + messageProcessorService.getAmmHostUrl() + messageProcessorService.getAmmProcessStepActionUrl()
            + messageProcessorService.getAmmTestOptionsUrl()+messageProcessorService.getRmmGetSampleResult());
    }
}
