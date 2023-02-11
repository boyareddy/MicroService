package com.roche.connect.wfm.test.testng;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.dpcr_analyzer.ORUAssay;
import com.roche.connect.common.dpcr_analyzer.ORUPartitionEngine;
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMORUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMQueryMessage;
import com.roche.connect.common.enums.NiptDeviceType;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.lp24.Consumable;
import com.roche.connect.common.lp24.ContainerInfo;
import com.roche.connect.common.lp24.SampleInfo;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.StatusUpdate;
import com.roche.connect.common.mp96.OULSampleResultMessage;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.API_URL;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResultIntegrationService;

@PrepareForTest({ RestClientUtil.class, IOUtils.class, HMTPLoggerImpl.class, URLEncoder.class }) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" }) public class ResultIntegrationServiceTest {
    
    @InjectMocks private ResultIntegrationService resultIntegrationService;
    
    @Mock ResultIntegrationService resultIntegrationServices;
    
    List<WfmDTO> wfmlist = new ArrayList<WfmDTO>();
    
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    
    @Mock Invocation.Builder orderClient;
    
    @Mock InputStream rmmClient;
    
    @Mock ObjectMapper objectMapper;
    
    @Mock OrderIntegrationService orderIntegrationService;
    
    @Mock AssayIntegrationService assayIntegrationService;
    @Mock Response response;
    String url = "http://localhost";
    String orderName = "ORD399138450";
    String messageType = "NA-Extraction";
    String deviceId = "MP001-121";
    String deviceType = "MP24";
    String runid = "1566666";
    long runid2 = 1566666;
    
    String outputcontainerid = "ABCD";
    String position = "A1";
    String processstep = "NA Extraction";
    List<SampleResultsDTO> listSampleResultsDTOList = new ArrayList<>();
    List<ProcessStepActionDTO> processStepActionDTOs = new ArrayList<>();
    List<ProcessStepActionDTO> processStepActionDTOList = new ArrayList<>();
    List<com.roche.connect.common.mp24.message.Consumable> consumablelist = new ArrayList<>();
    
    @BeforeTest public void setUp() throws HMTPException, UnsupportedEncodingException {
        wfmlist.add(getWfmDTO());
        processStepActionDTOs.add(getProcessStepActionDTO());
        consumablelist.add(getConsumable());
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        runResultsDTO.setRunResultId(10001L);
        runResultsDTO.setDeviceId("MP001-121");
        Mockito.when(
            resultIntegrationServices.getRunResultsIdbyprocessstepnameAnddevicerunresultid(orderName, "NA Extraction"))
            .thenReturn(runResultsDTO);
    }
    
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    public void config() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
    }
    
    @Test public void getRunResultsIdbyprocessstepnameAnddevicerunresultidTest()
        throws UnsupportedEncodingException,
        HMTPException {
        config();
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + orderName + WfmURLConstants.DPCR_PROCESSSTEP_NAME + messageType, null))
            .thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        resultIntegrationService.getRunResultsIdbyprocessstepnameAnddevicerunresultid(orderName, messageType);
    }
    
    @Test public void getRunResultsIdbyprocessstepnameAnddevicerunresultidAnddeviceidTest()
        throws UnsupportedEncodingException,
        HMTPException {
        config();
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + orderName + WfmURLConstants.DPCR_PROCESSSTEP_NAME + messageType
                + "&deviceId=" + deviceId,
            null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        resultIntegrationService.getDevicerunresultidAndDeviceid(orderName, messageType, deviceId);
    }
    
    @Test public void updateRunResultsTest() throws HMTPException, IOException {
        config();
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        resultIntegrationService.updateRunResults(getRunResultsDTO());
    }
    
    @Test public void getRunResultsId() throws UnsupportedEncodingException, HMTPException {
        config();
        Mockito
            .when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.QUESTION_MARK,
                WfmURLConstants.DEVICE_ID + deviceId + WfmURLConstants.PROCESS_STEP_NAME + messageType, null))
            .thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        resultIntegrationService.getRunResultsId(deviceId, messageType);
    }
    
    @Test public void addRunResults() throws HMTPException, IOException {
        config();
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        resultIntegrationService.addRunResults(getRunResultsDTO());
    }
    
    @Test public void addSampleResults() throws HMTPException, IOException {
        config();
        List<WfmDTO> wfmlists = new ArrayList<WfmDTO>();
        wfmlists.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        // **
        Mockito
            .when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.QUESTION_MARK, WfmURLConstants.DEVICE_ID
                    + wfmlists.get(0).getDeviceId() + WfmURLConstants.PROCESS_STEP_NAME + "NA Extraction",
                null))
            .thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        /// **
        
        Mockito.when(objectMapper.writeValueAsString(getSampleResultsDTO())).thenReturn("sample");
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        resultIntegrationService.addSampleResults(wfmlists);
    }
    
    @Test public void addSampleResultssecond() throws HMTPException, IOException {
        config();
        List<WfmDTO> wfmlists = new ArrayList<WfmDTO>();
        wfmlists.add(getWfmDTOSecond());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        // **
        Mockito
            .when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.QUESTION_MARK, WfmURLConstants.DEVICE_ID
                    + wfmlists.get(0).getDeviceId() + WfmURLConstants.PROCESS_STEP_NAME + "LP Pre PCR",
                null))
            .thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTOSecond());
        /// **
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, getRunResultsDTOThird(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn("1234");
        // **
        Mockito.when(objectMapper.writeValueAsString(getSampleResultsDTO())).thenReturn("sample");
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        resultIntegrationService.addSampleResults(wfmlists);
    }
    
    @Test public void addSampleResultsThird() throws HMTPException, IOException {
        config();
        List<WfmDTO> wfmlists = new ArrayList<WfmDTO>();
        wfmlists.add(getWfmDTOThird());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        // **
        Mockito
            .when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.QUESTION_MARK, WfmURLConstants.DEVICE_ID
                    + wfmlists.get(0).getDeviceId() + WfmURLConstants.PROCESS_STEP_NAME + "LP Post PCR/Pooling",
                null))
            .thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTOSecond());
        /// **
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, getRunResultsDTOFourth(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn("1234");
        // **
        Mockito.when(objectMapper.writeValueAsString(getSampleResultsDTO())).thenReturn("sample");
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        resultIntegrationService.addSampleResults(wfmlists);
    }
    
    @Test public void addSampleResultsforSeqPrep() throws HMTPException, IOException {
        config();
        List<WfmDTO> wfmlistsSeq = new ArrayList<WfmDTO>();
        wfmlistsSeq.add(getWfmDTOThird());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        resultIntegrationService.addSampleResultsforSeqPrep(wfmlistsSeq);
    }
    
    @Test public void updateHTPStatus() throws HMTPException, IOException {
        config();
        List<WfmDTO> wfmlistsHTP = new ArrayList<WfmDTO>();
        wfmlistsHTP.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        resultIntegrationService.updateHTPStatus(wfmlistsHTP, getHTPStatusMessage());
    }
    
    @Test public void findAccessingIdByConntainerId() throws HMTPException, IOException, OrderNotFoundException {
        config();
        listSampleResultsDTOList.add(getSampleResultsDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.OUTPUT_CONTAINER_ID + outputcontainerid + WfmURLConstants.OUTPUT_CONTAINER_POSITION
                + position + WfmURLConstants.PROCESS_STEP_NAME_ONE + processstep,
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<List<SampleResultsDTO>>() {}))
            .thenReturn(listSampleResultsDTOList);
        resultIntegrationService.findAccessingIdByContainerId(outputcontainerid, position, processstep);
    }
    
    @Test public void updateforMP96Status() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        Mockito
            .when(RestClientUtil.getUrlString(API_URL.OMM_API_URL.toString(), "",
                WfmURLConstants.CONTAINER_SAMPLE + wfmlistsMP96Status.get(0).getAccessioningId()
                    + WfmURLConstants.CONTAINER_SAMPLE_PARAMETER_STATUS + WfmURLConstants.PROCESSED,
                "", null))
            .thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null))
            .thenReturn(orderClient);
        
        resultIntegrationService.updateforMP96Status(wfmlistsMP96Status, getWFMoULMessage());
    }
    
    @Test public void updateforLP24SeqPre() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsLP24SeqPre = new ArrayList<WfmDTO>();
        wfmlistsLP24SeqPre.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        resultIntegrationService.updateforLP24SeqPre(wfmlistsLP24SeqPre, getSpecimenStatusUpdateMessage());
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
    }
    
 @Test public void updatefordPCRLP24NEW() throws HMTPException, IOException, OrderNotFoundException, ParseException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        List<DeviceTestOptionsDTO> deviceTestOptionslist = new ArrayList<>();
        RunResultsDTO runResultsDTO = null;
        RunResultsDTO runResultsDTOsecond = new RunResultsDTO();
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("NA Extraction");
        deviceTestOptionslist.add(deviceTestOptionsDTO);
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getSpecimenStatusUpdateMessagePIcondition().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME
                + WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString() + "&deviceId="
                + getSpecimenStatusUpdateMessagePIcondition().getDeviceSerialNumber(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null))
            .thenReturn(orderClient);
        
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTO);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, runResultsDTOsecond, null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        // **
        runResultsDTOsecond
            .setOperatorName(getSpecimenStatusUpdateMessagePIcondition().getStatusUpdate().getOperatorName());
        
        Collection<SampleResultsDTO> sampleResultslistfirst = new ArrayList<>();
        SampleResultsDTO sampleResultsDTOfirst = new SampleResultsDTO();
        sampleResultsDTOfirst.setStatus("InProgre");
        sampleResultsDTOfirst.setAccesssioningId("1000111");
        runResultsDTOsecond.setSampleResults(sampleResultslistfirst);
        // ***************
        RunResultsDTO runResultsDTOThird = new RunResultsDTO();
        Collection<SampleResultsDTO> sampleResultslistnew = new ArrayList<>();
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setStatus("InProgre");
        sampleResultsDTO.setResult("Passed");
        sampleResultsDTO.setFlag("E1");
        sampleResultsDTO.setAccesssioningId("1000111");
        sampleResultslistnew.add(sampleResultsDTO);
        runResultsDTOThird.setSampleResults(sampleResultslistnew);
        Mockito
            .when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "", WfmURLConstants.RUN_RESULT_API_PATH
                + WfmURLConstants.RMM_RUN_RESULT_BY_ID_URL + runResultsDTOsecond.getRunResultId(), "", null))
            .thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTOThird);
        
        // **
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(objectMapper.writeValueAsString(getRunResultsDTO())).thenReturn("sample");
        Mockito.when(RestClientUtil.putMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        resultIntegrationService.updateforLP24Results(wfmlistsMP96Status, getSpecimenStatusUpdateMessagePIcondition());
    }
    
    @Test public void updateSampleResults() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsLP24SeqPre = new ArrayList<WfmDTO>();
        wfmlistsLP24SeqPre.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getAdaptorRequestMessageDTO().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()
                + "&deviceId=" + getAdaptorRequestMessageDTO().getDeviceSerialNumber(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null))
            .thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.putMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null);
        resultIntegrationService.updateSampleResults(wfmlistsLP24SeqPre, getAdaptorRequestMessageDTO());
    }
    
    @Test public void updateSampleResultsPassedWithflags() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsLP24SeqPre = new ArrayList<WfmDTO>();
        wfmlistsLP24SeqPre.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getAdaptorRequestMessageDTO().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()
                + "&deviceId=" + getAdaptorRequestMessageDTO().getDeviceSerialNumber(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null))
            .thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.putMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null);
        resultIntegrationService.updateSampleResults(wfmlistsLP24SeqPre, getAdaptorRequestMessageDTOSecondPassedWithFlags());
    }
    
    
    @Test public void updateSampleResultsInprogress() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsLP24SeqPre = new ArrayList<WfmDTO>();
        wfmlistsLP24SeqPre.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getAdaptorRequestMessageDTO().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()
                + "&deviceId=" + getAdaptorRequestMessageDTO().getDeviceSerialNumber(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null))
            .thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.putMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null);
        resultIntegrationService.updateSampleResults(wfmlistsLP24SeqPre, getAdaptorRequestMessageDTOInProgress());
    }
    
    
    @Test public void updateSampleResultsAborted() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsLP24SeqPre = new ArrayList<WfmDTO>();
        wfmlistsLP24SeqPre.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getAdaptorRequestMessageDTO().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()
                + "&deviceId=" + getAdaptorRequestMessageDTO().getDeviceSerialNumber(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null))
            .thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.putMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null);
        resultIntegrationService.updateSampleResults(wfmlistsLP24SeqPre, getAdaptorRequestMessageDTOAborted());
    }
    @Test public void updateSampleResultsElseCondition() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsLP24SeqPre = new ArrayList<WfmDTO>();
        wfmlistsLP24SeqPre.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        RunResultsDTO runResultsDTO = null;
        RunResultsDTO runResultsDTOsecond = new RunResultsDTO();
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getAdaptorRequestMessageDTO().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()
                + "&deviceId=" + getAdaptorRequestMessageDTO().getDeviceSerialNumber(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTO);
        
        // **
        runResultsDTOsecond.setOperatorName(getAdaptorRequestMessageDTO().getStatusUpdate().getOperatorName());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, runResultsDTOsecond, null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null);
        resultIntegrationService.updateSampleResults(wfmlistsLP24SeqPre, getAdaptorRequestMessageDTO());
    }
    
    @Test public void updateSampleResultsElseConditionpassedwithflags() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsLP24SeqPre = new ArrayList<WfmDTO>();
        wfmlistsLP24SeqPre.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        RunResultsDTO runResultsDTO = null;
        RunResultsDTO runResultsDTOsecond = new RunResultsDTO();
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getAdaptorRequestMessageDTO().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()
                + "&deviceId=" + getAdaptorRequestMessageDTO().getDeviceSerialNumber(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTO);
        
        // **
        runResultsDTOsecond.setOperatorName(getAdaptorRequestMessageDTO().getStatusUpdate().getOperatorName());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, runResultsDTOsecond, null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null);
        resultIntegrationService.updateSampleResults(wfmlistsLP24SeqPre, getAdaptorRequestMessageDTOSecondPassedWithFlags());
    }
    
    
    @Test public void updateSampleResultsElseConditionInProgress() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsLP24SeqPre = new ArrayList<WfmDTO>();
        wfmlistsLP24SeqPre.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        RunResultsDTO runResultsDTO = null;
        RunResultsDTO runResultsDTOsecond = new RunResultsDTO();
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getAdaptorRequestMessageDTO().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()
                + "&deviceId=" + getAdaptorRequestMessageDTO().getDeviceSerialNumber(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTO);
        
        // **
        runResultsDTOsecond.setOperatorName(getAdaptorRequestMessageDTO().getStatusUpdate().getOperatorName());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, runResultsDTOsecond, null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null);
        resultIntegrationService.updateSampleResults(wfmlistsLP24SeqPre, getAdaptorRequestMessageDTOInProgress());
    }
    
    
    @Test public void updateSampleResultsElseConditionAborted() throws HMTPException, IOException, OrderNotFoundException {
        config();
        List<WfmDTO> wfmlistsLP24SeqPre = new ArrayList<WfmDTO>();
        wfmlistsLP24SeqPre.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        RunResultsDTO runResultsDTO = null;
        RunResultsDTO runResultsDTOsecond = new RunResultsDTO();
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getAdaptorRequestMessageDTO().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()
                + "&deviceId=" + getAdaptorRequestMessageDTO().getDeviceSerialNumber(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTO);
        
        // **
        runResultsDTOsecond.setOperatorName(getAdaptorRequestMessageDTO().getStatusUpdate().getOperatorName());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, runResultsDTOsecond, null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null);
        resultIntegrationService.updateSampleResults(wfmlistsLP24SeqPre, getAdaptorRequestMessageDTOAborted());
    }
    @Test public void updateforLP24() throws HMTPException, IOException, OrderNotFoundException, ParseException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        List<DeviceTestOptionsDTO> deviceTestOptionslist = new ArrayList<>();
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("NA Extraction");
        deviceTestOptionslist.add(deviceTestOptionsDTO);
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getSpecimenStatusUpdateMessage().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + getSpecimenStatusUpdateMessage().getProcessStepName()
                + "&deviceId=" + wfmlistsMP96Status.get(0).getDeviceId(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        
        // **
        Mockito
            .when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                wfmlistsMP96Status.get(0).getAccessioningId(), WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString()))
            .thenReturn(deviceTestOptionslist);
        
        Mockito.when(orderIntegrationService.findOrder(wfmlistsMP96Status.get(0).getAccessioningId()))
            .thenReturn(wfmlistsMP96Status);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        resultIntegrationService.updateforLP24(wfmlistsMP96Status, getSpecimenStatusUpdateMessage());
    }
    
    @Test public void updateforLP24Passedwithflags() throws HMTPException, IOException, OrderNotFoundException, ParseException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        List<DeviceTestOptionsDTO> deviceTestOptionslist = new ArrayList<>();
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("NA Extraction");
        deviceTestOptionslist.add(deviceTestOptionsDTO);
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getSpecimenStatusUpdateMessage().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + getSpecimenStatusUpdateMessage().getProcessStepName()
                + "&deviceId=" + wfmlistsMP96Status.get(0).getDeviceId(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        
        // **
        Mockito
            .when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                wfmlistsMP96Status.get(0).getAccessioningId(), WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString()))
            .thenReturn(deviceTestOptionslist);
        
        Mockito.when(orderIntegrationService.findOrder(wfmlistsMP96Status.get(0).getAccessioningId()))
            .thenReturn(wfmlistsMP96Status);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        resultIntegrationService.updateforLP24(wfmlistsMP96Status, getSpecimenStatusUpdateMessageInPassedwithflags());
    }
    
    
    
    @Test public void updateforLP24InProgress() throws HMTPException, IOException, OrderNotFoundException, ParseException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        List<DeviceTestOptionsDTO> deviceTestOptionslist = new ArrayList<>();
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("NA Extraction");
        deviceTestOptionslist.add(deviceTestOptionsDTO);
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getSpecimenStatusUpdateMessage().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + getSpecimenStatusUpdateMessage().getProcessStepName()
                + "&deviceId=" + wfmlistsMP96Status.get(0).getDeviceId(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        
        // **
        Mockito
            .when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                wfmlistsMP96Status.get(0).getAccessioningId(), WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString()))
            .thenReturn(deviceTestOptionslist);
        
        Mockito.when(orderIntegrationService.findOrder(wfmlistsMP96Status.get(0).getAccessioningId()))
            .thenReturn(wfmlistsMP96Status);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        resultIntegrationService.updateforLP24(wfmlistsMP96Status, getSpecimenStatusUpdateMessageInProgress());
    }
    
    
    @Test public void updateforLP24Aborted() throws HMTPException, IOException, OrderNotFoundException, ParseException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        List<DeviceTestOptionsDTO> deviceTestOptionslist = new ArrayList<>();
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("NA Extraction");
        deviceTestOptionslist.add(deviceTestOptionsDTO);
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getSpecimenStatusUpdateMessage().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + getSpecimenStatusUpdateMessage().getProcessStepName()
                + "&deviceId=" + wfmlistsMP96Status.get(0).getDeviceId(),
            null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, getRunResultsDTO(), null)).thenReturn(rmmClient);
        
        // **
        Mockito
            .when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                wfmlistsMP96Status.get(0).getAccessioningId(), WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString()))
            .thenReturn(deviceTestOptionslist);
        
        Mockito.when(orderIntegrationService.findOrder(wfmlistsMP96Status.get(0).getAccessioningId()))
            .thenReturn(wfmlistsMP96Status);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        resultIntegrationService.updateforLP24(wfmlistsMP96Status, getSpecimenStatusUpdateMessageAborted());
    }
    @Test public void updateforLP24Else() throws HMTPException, IOException, OrderNotFoundException, ParseException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        List<DeviceTestOptionsDTO> deviceTestOptionslist = new ArrayList<>();
        RunResultsDTO runResultsDTO = null;
        RunResultsDTO runResultsDTOsecond = new RunResultsDTO();
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("NA Extraction");
        deviceTestOptionslist.add(deviceTestOptionsDTO);
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getSpecimenStatusUpdateMessage().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + getSpecimenStatusUpdateMessage().getProcessStepName()
                + "&deviceId=" + wfmlistsMP96Status.get(0).getDeviceId(),
            null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTO);
        
        // **
        runResultsDTOsecond.setOperatorName(getSpecimenStatusUpdateMessage().getStatusUpdate().getOperatorName());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, runResultsDTOsecond, null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        // **
        Mockito
            .when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                wfmlistsMP96Status.get(0).getAccessioningId(), WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString()))
            .thenReturn(deviceTestOptionslist);
        
        Mockito.when(orderIntegrationService.findOrder(wfmlistsMP96Status.get(0).getAccessioningId()))
            .thenReturn(wfmlistsMP96Status);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        resultIntegrationService.updateforLP24(wfmlistsMP96Status, getSpecimenStatusUpdateMessage());
    }
    
    @Test public void updateforLP24ElsePassedwithflags() throws HMTPException, IOException, OrderNotFoundException, ParseException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        List<DeviceTestOptionsDTO> deviceTestOptionslist = new ArrayList<>();
        RunResultsDTO runResultsDTO = null;
        RunResultsDTO runResultsDTOsecond = new RunResultsDTO();
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("NA Extraction");
        deviceTestOptionslist.add(deviceTestOptionsDTO);
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getSpecimenStatusUpdateMessage().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + getSpecimenStatusUpdateMessage().getProcessStepName()
                + "&deviceId=" + wfmlistsMP96Status.get(0).getDeviceId(),
            null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTO);
        
        // **
        runResultsDTOsecond.setOperatorName(getSpecimenStatusUpdateMessage().getStatusUpdate().getOperatorName());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, runResultsDTOsecond, null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        // **
        Mockito
            .when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                wfmlistsMP96Status.get(0).getAccessioningId(), WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString()))
            .thenReturn(deviceTestOptionslist);
        
        Mockito.when(orderIntegrationService.findOrder(wfmlistsMP96Status.get(0).getAccessioningId()))
            .thenReturn(wfmlistsMP96Status);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        resultIntegrationService.updateforLP24(wfmlistsMP96Status, getSpecimenStatusUpdateMessageInPassedwithflags());
    }
    
    
    @Test public void updateforLP24ElseInProgress() throws HMTPException, IOException, OrderNotFoundException, ParseException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        List<DeviceTestOptionsDTO> deviceTestOptionslist = new ArrayList<>();
        RunResultsDTO runResultsDTO = null;
        RunResultsDTO runResultsDTOsecond = new RunResultsDTO();
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("NA Extraction");
        deviceTestOptionslist.add(deviceTestOptionsDTO);
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getSpecimenStatusUpdateMessage().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + getSpecimenStatusUpdateMessage().getProcessStepName()
                + "&deviceId=" + wfmlistsMP96Status.get(0).getDeviceId(),
            null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTO);
        
        // **
        runResultsDTOsecond.setOperatorName(getSpecimenStatusUpdateMessage().getStatusUpdate().getOperatorName());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, runResultsDTOsecond, null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        // **
        Mockito
            .when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                wfmlistsMP96Status.get(0).getAccessioningId(), WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString()))
            .thenReturn(deviceTestOptionslist);
        
        Mockito.when(orderIntegrationService.findOrder(wfmlistsMP96Status.get(0).getAccessioningId()))
            .thenReturn(wfmlistsMP96Status);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        resultIntegrationService.updateforLP24(wfmlistsMP96Status, getSpecimenStatusUpdateMessageInProgress());
    }
    
    
    @Test public void updateforLP24ElseAborted() throws HMTPException, IOException, OrderNotFoundException, ParseException {
        config();
        List<WfmDTO> wfmlistsMP96Status = new ArrayList<WfmDTO>();
        List<DeviceTestOptionsDTO> deviceTestOptionslist = new ArrayList<>();
        RunResultsDTO runResultsDTO = null;
        RunResultsDTO runResultsDTOsecond = new RunResultsDTO();
        DeviceTestOptionsDTO deviceTestOptionsDTO = new DeviceTestOptionsDTO();
        deviceTestOptionsDTO.setTestProtocol("NA Extraction");
        deviceTestOptionslist.add(deviceTestOptionsDTO);
        wfmlistsMP96Status.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getSpecimenStatusUpdateMessage().getStatusUpdate().getOrderName()
                + WfmURLConstants.DPCR_PROCESSSTEP_NAME + getSpecimenStatusUpdateMessage().getProcessStepName()
                + "&deviceId=" + wfmlistsMP96Status.get(0).getDeviceId(),
            null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(runResultsDTO);
        
        // **
        runResultsDTOsecond.setOperatorName(getSpecimenStatusUpdateMessage().getStatusUpdate().getOperatorName());
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.postMethodCall(url, runResultsDTOsecond, null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        
        // **
        Mockito
            .when(assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(
                wfmlistsMP96Status.get(0).getAccessioningId(), WfmConstants.ASSAY_PROCESS_STEP_DATA.LP24.toString(),
                WfmConstants.ASSAY_PROCESS_STEP_DATA.LIBRARYPREPARATION.toString()))
            .thenReturn(deviceTestOptionslist);
        
        Mockito.when(orderIntegrationService.findOrder(wfmlistsMP96Status.get(0).getAccessioningId()))
            .thenReturn(wfmlistsMP96Status);
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        resultIntegrationService.updateforLP24(wfmlistsMP96Status, getSpecimenStatusUpdateMessageAborted());
    }
    @Test public void getSampleResultsStatus() throws HMTPException, IOException {
        config();
        resultIntegrationService.getSampleResultsStatus("flagged");
        resultIntegrationService.getSampleResultsStatus("Failed");
        resultIntegrationService.getSampleResultsStatus("InProgress");
        
    }
    
    
    @Test public void updateRunResultsByAdaptorRequestMessage() throws HMTPException, IOException {
        config();
        
        resultIntegrationService.updateRunResultsByAdaptorRequestMessage(getRunResultsDTOFourth(),
            getAdaptorRequestMessageDTO());
        
    }
    
    @Test public void createRunResultsDTOFromSpecimenStatusUpdateMessage() throws HMTPException, IOException {
        config();
        
        resultIntegrationService.createRunResultsDTOFromSpecimenStatusUpdateMessage("NIPT",
            getSpecimenStatusUpdateMessage());
        
    }
    
    @Test public void createRunResultsDTOFromAdaptorRequestMessage() throws HMTPException, IOException {
        config();
        
        resultIntegrationService.createRunResultsDTOFromAdaptorRequestMessage("NIPT", getAdaptorRequestMessageDTO());
        
    }
    
    @Test public void getRunResultStatusBySampleStatusSampleList() throws HMTPException, IOException {
        config();
        List<SampleResultsDTO> sampleList = new ArrayList<>();
        SampleResultsDTO sampleResultsDTOFirst = getSampleResultsDTO();
        sampleResultsDTOFirst.setResult("inprogress");
        sampleList.add(sampleResultsDTOFirst);
        resultIntegrationService.getRunResultStatusBySampleStatus(sampleList);
        
        List<SampleResultsDTO> sampleListSecond = new ArrayList<>();
        SampleResultsDTO sampleResultsDTO = getSampleResultsDTO();
        sampleResultsDTO.setResult("passed");
        sampleListSecond.add(sampleResultsDTO);
        resultIntegrationService.getRunResultStatusBySampleStatus(sampleListSecond);
        
        List<SampleResultsDTO> sampleListThird = new ArrayList<>();
        SampleResultsDTO sampleResultsDTOthird = getSampleResultsDTO();
        sampleResultsDTOthird.setResult("aborted");
        sampleListThird.add(sampleResultsDTO);
        resultIntegrationService.getRunResultStatusBySampleStatus(sampleListThird);
    }
    
    @Test public void updatefordPCRAnalyzer() throws HMTPException, IOException {
        config();
        List<WfmDTO> wfmlists = new ArrayList<WfmDTO>();
        wfmlists.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        Mockito.when(objectMapper.writeValueAsString(getSampleResultsDTO())).thenReturn("sample");
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        resultIntegrationService.updatefordPCRAnalyzer(wfmlists, geWFMESUMessage());
    }
    
    @Test public void updateforDPCRAnalyzerORU() throws HMTPException, IOException {
        config();
        List<WfmDTO> wfmlists = new ArrayList<WfmDTO>();
        wfmlists.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        Mockito.when(objectMapper.writeValueAsString(getSampleResultsDTO())).thenReturn("sample");
        Mockito.when(RestClientUtil.postMethodCall(url, getSampleResultsDTO(), null)).thenReturn(rmmClient);
        Mockito.when(IOUtils.toString(rmmClient, StandardCharsets.UTF_8)).thenReturn(runid);
        resultIntegrationService.updateforDPCRAnalyzerORU(wfmlists, getWFMORUMessage());
    }
    
    
    @Test public void getSampleResultsDTOFromARMTest() throws HMTPException, ParseException {
        resultIntegrationService.getSampleResultsDTOFromARM(getWfmDTO(), getSpecimenStatusUpdateMessage());
    }
    
    @Test
    public void updateRunResultStatusByQueryTest() throws HMTPException, UnsupportedEncodingException{
        config();
        
        
        List<WfmDTO> wfmlists = new ArrayList<WfmDTO>();
        wfmlists.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        
        ProcessStepActionDTO processStepActionDTO=new ProcessStepActionDTO();
        processStepActionDTO.setProcessStepSeq(1);
        processStepActionDTO.setProcessStepName("NA Extraction");
        processStepActionDTOList.add(processStepActionDTO);
        Mockito.when(assayIntegrationService.getProcessStepAction(AssayType.NIPT_HTP, NiptDeviceType.MP24.getText())).thenReturn(processStepActionDTOList);
        
 RunResultsDTO runResultsDTOFirst = new RunResultsDTO();
        
 
        
        ReflectionTestUtils.setField(resultIntegrationService, "rmmApiHostUrl",
                "http://localhost:86/rmm");
        Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {}))
            .thenReturn(runResultsDTOFirst);
        
     
        // ***************
        RunResultsDTO runResultsDTOThird = new RunResultsDTO();
        Collection<SampleResultsDTO> sampleResultslistnew = new ArrayList<>();
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setStatus("InProgre");
        sampleResultsDTO.setResult("Passed");
        sampleResultsDTO.setFlag("E1");
        sampleResultsDTO.setAccesssioningId("1000111");
        sampleResultslistnew.add(sampleResultsDTO);
        runResultsDTOThird.setSampleResults(sampleResultslistnew);
        runResultsDTOThird.setRunResultId(10001L);
        runResultsDTOThird.setDeviceId("MP001-121");
        runResultsDTOThird.setProcessStepName("NA Extraction");
        runResultsDTOThird.setDeviceRunId(orderName);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.RMM_RUN_RESULT_BY_ID_URL + getRunResultsDTO().getRunResultId(), "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {}))
            .thenReturn(runResultsDTOThird);
        
     
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.putMethodCall(url, runResultsDTOThird, null)).thenReturn(runResultsDTOThird);
        resultIntegrationService.updateRunResultStatusByQuery(deviceId,deviceType);
    }
    
    @Test
    public void updateRunResultStatusByQueryTestElse() throws HMTPException, UnsupportedEncodingException{
        config();
        
        
        List<WfmDTO> wfmlists = new ArrayList<WfmDTO>();
        wfmlists.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        
        ProcessStepActionDTO processStepActionDTO=new ProcessStepActionDTO();
        
        
        processStepActionDTO.setProcessStepSeq(1);
        processStepActionDTO.setProcessStepName("NA Extraction");
        processStepActionDTOList.add(processStepActionDTO);
        Mockito.when(assayIntegrationService.getProcessStepAction(AssayType.NIPT_HTP, NiptDeviceType.LP24.getText())).thenReturn(processStepActionDTOList);
        
 RunResultsDTO runResultsDTOFirst = new RunResultsDTO();
        
 
        
        ReflectionTestUtils.setField(resultIntegrationService, "rmmApiHostUrl",
                "http://localhost:86/rmm");
        Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {}))
            .thenReturn(runResultsDTOFirst);
        
     
        // ***************
        RunResultsDTO runResultsDTOThird = new RunResultsDTO();
        Collection<SampleResultsDTO> sampleResultslistnew = new ArrayList<>();
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setStatus("InProgre");
        sampleResultsDTO.setResult("Passed");
        sampleResultsDTO.setFlag("E1");
        sampleResultsDTO.setAccesssioningId("1000111");
        sampleResultslistnew.add(sampleResultsDTO);
        runResultsDTOThird.setSampleResults(sampleResultslistnew);
        runResultsDTOThird.setRunResultId(10001L);
        runResultsDTOThird.setDeviceId("MP001-121");
        runResultsDTOThird.setProcessStepName("NA Extraction");
        runResultsDTOThird.setDeviceRunId(orderName);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.RMM_RUN_RESULT_BY_ID_URL + getRunResultsDTO().getRunResultId(), "", null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {}))
            .thenReturn(runResultsDTOThird);
        
     
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                WfmURLConstants.RUN_RESULT_API_PATH, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.putMethodCall(url, runResultsDTOThird, null)).thenReturn(runResultsDTOThird);
        resultIntegrationService.updateRunResultStatusByQuery(deviceId,"LP24");
    }
    
    @Test public void updateRunResultsBySpecimenStatusUpdateMessageElse() throws HMTPException, IOException, ParseException {
        config();
        
        resultIntegrationService.updateRunResultsBySpecimenStatusUpdateMessage(getRunResultsDTOFourth(), getSpecimenStatusUpdateMessage());
    }
    
    
    @Test public void getSampleResultsDTOFromARM() throws HMTPException, IOException {
        config();
        
        AdaptorRequestMessage adaptorRequestMessage =new AdaptorRequestMessage();
        com.roche.connect.common.mp24.message.StatusUpdate statusUpdate = new  com.roche.connect.common.mp24.message.StatusUpdate();
        
        com.roche.connect.common.mp24.message.SampleInfo sampleInfo = new com.roche.connect.common.mp24.message.SampleInfo();
        com.roche.connect.common.mp24.message.ContainerInfo  containerInfo=new com.roche.connect.common.mp24.message.ContainerInfo();
     
        containerInfo.setCarrierType("ABC");
        com.roche.connect.common.mp24.message.Consumable consumable = new com.roche.connect.common.mp24.message.Consumable();
        List<com.roche.connect.common.mp24.message.Consumable> consumableList = new ArrayList<com.roche.connect.common.mp24.message.Consumable>();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("Passed");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setOrderResult("Passed");
        statusUpdate.setFlag("E1");
        statusUpdate.setSampleInfo(sampleInfo);
        statusUpdate.setContainerInfo(containerInfo);
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        consumableList.add(consumable);
        statusUpdate.setConsumables(consumableList);
        adaptorRequestMessage.setStatusUpdate(statusUpdate);
        resultIntegrationService.getSampleResultsDTOFromARM(getWfmDTO(), adaptorRequestMessage);
    }
    
    
    
    
    public WFMQueryMessage getWFMQueryMessage() {
        WFMQueryMessage wfmQueryMessage = new WFMQueryMessage();
        
        wfmQueryMessage.setContainerId("ABC");
        wfmQueryMessage.setRunResultsId(Long.parseLong("1000001"));
        return wfmQueryMessage;
    }
    
    public WFMESUMessage geWFMESUMessage() {
        WFMESUMessage wfmESUMessage = new WFMESUMessage();
        wfmESUMessage.setAccessioningId("8001");
        wfmESUMessage.setProcessStepName("dPCR");
        wfmESUMessage.setMessageType("ESU");
        wfmESUMessage.setDeviceId("RX-1000");
        wfmESUMessage.setRunResultId("155");
        wfmESUMessage.setStatus("Completed");
        wfmESUMessage.setDateTimeMessageGenerated("2019-02-25 05:47:12.382");
        return wfmESUMessage;
    }
    
    public RunResultsDTO getRunResultsDTO() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        
        runResultsDTO.setRunResultId(10001L);
        runResultsDTO.setDeviceId("MP001-121");
        runResultsDTO.setProcessStepName("NA Extraction");
        runResultsDTO.setDeviceRunId(orderName);
        runResultsDTO.setRunStatus("Completed");
        return runResultsDTO;
    }
    
    public RunResultsDTO getRunResultsDTOSecond() {
        RunResultsDTO runResultsDTO = null;
        
        return runResultsDTO;
    }
    
    public RunResultsDTO getRunResultsDTOThird() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        getWfmDTOSecond();
        runResultsDTO.setDeviceId(getWfmDTOSecond().getDeviceId());
        runResultsDTO.setAssayType(getWfmDTOSecond().getAssayType());
        runResultsDTO.setProcessStepName(getWfmDTOSecond().getProcessStepName());
        runResultsDTO.setRunStatus("InProgress");
        return runResultsDTO;
    }
    
    public RunResultsDTO getRunResultsDTOFourth() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        getWfmDTOThird();
        runResultsDTO.setDeviceId(getWfmDTOThird().getDeviceId());
        runResultsDTO.setAssayType(getWfmDTOThird().getAssayType());
        runResultsDTO.setProcessStepName(getWfmDTOThird().getProcessStepName());
        runResultsDTO.setRunStatus("InProgress");
        runResultsDTO.setUpdatedBy("ABC");
        runResultsDTO.setUpdatedDateTime(new Timestamp(Long.parseLong("20186262626")));
        return runResultsDTO;
    }
    
    public RunResultsDTO getRunResultsDTOFifth() {
        RunResultsDTO runResultsDTO = new RunResultsDTO();
        getWfmDTOSecond();
        runResultsDTO.setDeviceId(getWfmDTOSecond().getDeviceId());
        runResultsDTO.setAssayType(getWfmDTOSecond().getAssayType());
        runResultsDTO.setProcessStepName(getWfmDTOSecond().getProcessStepName());
        runResultsDTO.setRunStatus("InProgress");
        return runResultsDTO;
    }
    
    public WfmDTO getWfmDTO() {
        WfmDTO wfmDTO = new WfmDTO();
        
        wfmDTO.setSendingApplicationName("MagnaPure24");
        wfmDTO.setDeviceId(orderName);
        wfmDTO.setAssayType("NIPTHTP");
        wfmDTO.setOrderId(Long.parseLong("1000001"));
        wfmDTO.setAccessioningId("1000111");
        wfmDTO.setOrderStatus("Inprogress");
        wfmDTO.setRunid(Long.parseLong(runid));
        wfmDTO.setInputContainerId("12abc");
        wfmDTO.setInputposition("ABC");
        wfmDTO.setSampleType("pLASMA");
        wfmDTO.setRunResultsId(Long.parseLong("123456"));
        wfmDTO.setProtocolname("NA");
        return wfmDTO;
    }
    
    public WfmDTO getWfmDTOSecond() {
        WfmDTO wfmDTO = new WfmDTO();
        
        wfmDTO.setSendingApplicationName("MagnaPure");
        wfmDTO.setProcessStepName("LP Pre PCR");
        wfmDTO.setDeviceId(orderName);
        wfmDTO.setAssayType("NIPTHTP");
        wfmDTO.setOrderId(Long.parseLong("1000001"));
        wfmDTO.setAccessioningId("1000111");
        wfmDTO.setOrderStatus("Inprogress");
        wfmDTO.setRunid(Long.parseLong(runid));
        return wfmDTO;
    }
    
    public WfmDTO getWfmDTOThird() {
        WfmDTO wfmDTO = new WfmDTO();
        
        wfmDTO.setSendingApplicationName("MagnaPure");
        wfmDTO.setProcessStepName("LP Post PCR/Pooling");
        wfmDTO.setDeviceId(orderName);
        wfmDTO.setAssayType("NIPTHTP");
        wfmDTO.setOrderId(Long.parseLong("1000001"));
        wfmDTO.setAccessioningId("1000111");
        wfmDTO.setOrderStatus("Inprogress");
        wfmDTO.setRunid(Long.parseLong(runid));
        return wfmDTO;
    }
    
    public AdaptorRequestMessage getAdaptorRequestMessage() {
        AdaptorRequestMessage adaptorRequestMessage = new AdaptorRequestMessage();
        adaptorRequestMessage.setStatusUpdate(getStatusUpdate());
        adaptorRequestMessage.setDeviceSerialNumber(deviceId);
        return adaptorRequestMessage;
    }
    
    public SampleResultsDTO getSampleResultsDTO() {
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        Collection<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumablesList = new ArrayList<>();
        SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
        sampleResultsDTO.setAccesssioningId("1000111");
        sampleResultsDTO.setOrderId(Long.parseLong("1000001"));
        sampleResultsDTO.setStatus("Completed");
        sampleResultsDTO.setRunResultsId(Long.parseLong(runid));
        sampleResultsDTO.setInputContainerId("12abc");
        sampleResultsDTO.setInputContainerPosition("ABC");
        sampleReagentsAndConsumablesList.add(sampleReagentsAndConsumablesDTO);
        sampleResultsDTO.setSampleReagentsAndConsumables(sampleReagentsAndConsumablesList);
        return sampleResultsDTO;
    }
    
    public HtpStatusMessage getHTPStatusMessage() {
        
        HtpStatusMessage htpStatusMessage = new HtpStatusMessage();
        Collection<SampleReagentsAndConsumablesDTO> sampleREagents = new ArrayList<>();
        htpStatusMessage.setUpdatedBy("admin");
        htpStatusMessage.setSampleReagentsAndConsumables(sampleREagents);
        htpStatusMessage.setStatus("Completed");
        return htpStatusMessage;
    }
    
    public StatusUpdate getStatusUpdate() {
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOrderResult("Inprogress");
        statusUpdate.setSampleInfo(getSampleInfo());
        statusUpdate.setContainerInfo(getContainerInfo());
        statusUpdate.setConsumables(consumablelist);
        return statusUpdate;
    }
  
    public com.roche.connect.common.mp24.message.SampleInfo getSampleInfo() {
        com.roche.connect.common.mp24.message.SampleInfo sampleInfo =
            new com.roche.connect.common.mp24.message.SampleInfo();
        sampleInfo.setSampleOutputId("383199353");
        sampleInfo.setSampleOutputPosition("1");
        return sampleInfo;
    }
    
    public com.roche.connect.common.mp24.message.ContainerInfo getContainerInfo() {
        com.roche.connect.common.mp24.message.ContainerInfo containerInfo =
            new com.roche.connect.common.mp24.message.ContainerInfo();
        containerInfo.setCarrierType("9mmStrip");
        return containerInfo;
    }
    
    public com.roche.connect.common.mp24.message.Consumable getConsumable() {
        com.roche.connect.common.mp24.message.Consumable consumable = new com.roche.connect.common.mp24.message.Consumable();
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        return consumable;
    }
    
    public WFMoULMessage getWFMoULMessage() {
        WFMoULMessage wfmoULMessage = new WFMoULMessage();
        OULSampleResultMessage oulSampleResultMessage = new OULSampleResultMessage();
        
        oulSampleResultMessage.setTime("20181024191916");
        oulSampleResultMessage.setOperator("admin");
        oulSampleResultMessage.setAccessioningId("929292");
        oulSampleResultMessage.setOutputContainerId("83c8a11b-");
        oulSampleResultMessage.setReagentKitName("Cellular RNA LV");
        oulSampleResultMessage.setReagentVesion("0.2");
        oulSampleResultMessage.setRunStartTime("20181024171916");
        oulSampleResultMessage.setRunEndTime("20181024191916");
        oulSampleResultMessage.setOutputPlateType("96WellPlate");
        oulSampleResultMessage.setProtocal("Cellular RNA LV 0.6.4");
        oulSampleResultMessage.setSampleVolume("200");
        oulSampleResultMessage.setElevateVolume("50");
        oulSampleResultMessage.setBarcode("ABC");
        oulSampleResultMessage.setLotNo("123");
        oulSampleResultMessage.setFlag("F1");
        oulSampleResultMessage.setVolume("ABCSD");
        oulSampleResultMessage.setExpDate("20181024191916");
        oulSampleResultMessage.setSoftwareVersion("1.0.0");
        oulSampleResultMessage.setSerialNo("506");
        oulSampleResultMessage.setPosition("A1");
        oulSampleResultMessage.setSampleResult("P");
        wfmoULMessage.setOulSampleResultMessage(oulSampleResultMessage);
        wfmoULMessage.setDeviceSerialNumber("12345");
        wfmoULMessage.setSendingApplicationName("MagNA Pure 96");
        wfmoULMessage.setMessageType("OUL");
        wfmoULMessage.setAccessioningId("00023");
        wfmoULMessage.setRunResultsId(Long.parseLong("99999"));
        wfmoULMessage.setRunResultStatus("Passed");
        
        return wfmoULMessage;
    }
    
    public WFMORUMessage getWFMORUMessage() {
        WFMORUMessage wfmoRUMessage = new WFMORUMessage();
        Collection<ORUAssay> assayList = new ArrayList<>();
        Collection<ORUPartitionEngine> partitionEngineList = new ArrayList<>();
        ORUPartitionEngine oRUPartitionEngine = new ORUPartitionEngine();
        ORUAssay oRUAssay = new ORUAssay();
        oRUAssay.setName("name");
        oRUAssay.setPackageName("pkg");
        oRUAssay.setKit("kit");
        oRUAssay.setQualitativeResult("QLTY");
        oRUAssay.setQualitativeValue("QLTYS");
        oRUAssay.setQuantitativeResult("QLTY");
        oRUAssay.setQuantitativeValue("QNTY");
        oRUAssay.setVersion("0.1");
        assayList.add(oRUAssay);
        oRUPartitionEngine.setDateandTime("12341");
        oRUPartitionEngine.setFluidId("fid");
        oRUPartitionEngine.setPlateId("PId");
        oRUPartitionEngine.setSerialNumber("123");
        partitionEngineList.add(oRUPartitionEngine);
        wfmoRUMessage.setAccessioningId("12121");
        wfmoRUMessage.setInputContainerId("123a");
        wfmoRUMessage.setPartitionEngineList(partitionEngineList);
        wfmoRUMessage.setAssayList(assayList);
        wfmoRUMessage.setRunResultId("23456");
        return wfmoRUMessage;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessage() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        com.roche.connect.common.lp24.StatusUpdate statusUpdate = new com.roche.connect.common.lp24.StatusUpdate();
        List<Consumable> consumablelist = new ArrayList<>();
        
        SampleInfo sampleInfo = new SampleInfo();
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setOutputPlateType("A");
        containerInfo.setCarrierType("A");
        sampleInfo.setNewContainerId("123");
        sampleInfo.setNewOutputPosition("A");
        Consumable consumable = new Consumable();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("Passed");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setOrderResult("Passed");
        statusUpdate.setFlag("E1");
        statusUpdate.setSampleInfo(sampleInfo);
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        consumablelist.add(consumable);
        statusUpdate.setConsumables(consumablelist);
        statusUpdate.setContainerInfo(containerInfo);
        
        specimenStatusUpdateMessage.setDeviceSerialNumber("LP001-12");
        specimenStatusUpdateMessage.setSendingApplicationName("LP24");
        specimenStatusUpdateMessage.setReceivingApplication("Connect");
        specimenStatusUpdateMessage.setDateTimeMessageGenerated("20180831150003");
        specimenStatusUpdateMessage.setContainerId("83c8a11b_A1");
        specimenStatusUpdateMessage.setResponseMessage("ABC");
        specimenStatusUpdateMessage.setOrderId(Long.parseLong("100001"));
        specimenStatusUpdateMessage.setProcessStepName("ABC");
        specimenStatusUpdateMessage.setAccessioningId("100011111");
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate);
        
        return specimenStatusUpdateMessage;
    }
    
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessageInPassedwithflags() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        com.roche.connect.common.lp24.StatusUpdate statusUpdate = new com.roche.connect.common.lp24.StatusUpdate();
        List<Consumable> consumablelist = new ArrayList<>();
        
        SampleInfo sampleInfo = new SampleInfo();
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setOutputPlateType("A");
        containerInfo.setCarrierType("A");
        sampleInfo.setNewContainerId("123");
        sampleInfo.setNewOutputPosition("A");
        Consumable consumable = new Consumable();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("passed with flag");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setOrderResult("Passed");
        statusUpdate.setFlag("E1");
        statusUpdate.setSampleInfo(sampleInfo);
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        consumablelist.add(consumable);
        statusUpdate.setConsumables(consumablelist);
        statusUpdate.setContainerInfo(containerInfo);
        
        specimenStatusUpdateMessage.setDeviceSerialNumber("LP001-12");
        specimenStatusUpdateMessage.setSendingApplicationName("LP24");
        specimenStatusUpdateMessage.setReceivingApplication("Connect");
        specimenStatusUpdateMessage.setDateTimeMessageGenerated("20180831150003");
        specimenStatusUpdateMessage.setContainerId("83c8a11b_A1");
        specimenStatusUpdateMessage.setResponseMessage("ABC");
        specimenStatusUpdateMessage.setOrderId(Long.parseLong("100001"));
        specimenStatusUpdateMessage.setProcessStepName("ABC");
        specimenStatusUpdateMessage.setAccessioningId("100011111");
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate);
        
        return specimenStatusUpdateMessage;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessageInProgress() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        com.roche.connect.common.lp24.StatusUpdate statusUpdate = new com.roche.connect.common.lp24.StatusUpdate();
        List<Consumable> consumablelist = new ArrayList<>();
        
        SampleInfo sampleInfo = new SampleInfo();
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setOutputPlateType("A");
        containerInfo.setCarrierType("A");
        sampleInfo.setNewContainerId("123");
        sampleInfo.setNewOutputPosition("A");
        Consumable consumable = new Consumable();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("InProgress");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setOrderResult("Passed");
        statusUpdate.setFlag("E1");
        statusUpdate.setSampleInfo(sampleInfo);
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        consumablelist.add(consumable);
        statusUpdate.setConsumables(consumablelist);
        statusUpdate.setContainerInfo(containerInfo);
        
        specimenStatusUpdateMessage.setDeviceSerialNumber("LP001-12");
        specimenStatusUpdateMessage.setSendingApplicationName("LP24");
        specimenStatusUpdateMessage.setReceivingApplication("Connect");
        specimenStatusUpdateMessage.setDateTimeMessageGenerated("20180831150003");
        specimenStatusUpdateMessage.setContainerId("83c8a11b_A1");
        specimenStatusUpdateMessage.setResponseMessage("ABC");
        specimenStatusUpdateMessage.setOrderId(Long.parseLong("100001"));
        specimenStatusUpdateMessage.setProcessStepName("ABC");
        specimenStatusUpdateMessage.setAccessioningId("100011111");
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate);
        
        return specimenStatusUpdateMessage;
    }
    
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessageAborted() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        com.roche.connect.common.lp24.StatusUpdate statusUpdate = new com.roche.connect.common.lp24.StatusUpdate();
        List<Consumable> consumablelist = new ArrayList<>();
        
        SampleInfo sampleInfo = new SampleInfo();
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setOutputPlateType("A");
        containerInfo.setCarrierType("A");
        sampleInfo.setNewContainerId("123");
        sampleInfo.setNewOutputPosition("A");
        Consumable consumable = new Consumable();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("Aborted");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setOrderResult("Passed");
        statusUpdate.setFlag("E1");
        statusUpdate.setSampleInfo(sampleInfo);
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        consumablelist.add(consumable);
        statusUpdate.setConsumables(consumablelist);
        statusUpdate.setContainerInfo(containerInfo);
        
        specimenStatusUpdateMessage.setDeviceSerialNumber("LP001-12");
        specimenStatusUpdateMessage.setSendingApplicationName("LP24");
        specimenStatusUpdateMessage.setReceivingApplication("Connect");
        specimenStatusUpdateMessage.setDateTimeMessageGenerated("20180831150003");
        specimenStatusUpdateMessage.setContainerId("83c8a11b_A1");
        specimenStatusUpdateMessage.setResponseMessage("ABC");
        specimenStatusUpdateMessage.setOrderId(Long.parseLong("100001"));
        specimenStatusUpdateMessage.setProcessStepName("ABC");
        specimenStatusUpdateMessage.setAccessioningId("100011111");
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate);
        
        return specimenStatusUpdateMessage;
    }
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessagePIcondition() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        com.roche.connect.common.lp24.StatusUpdate statusUpdate = new com.roche.connect.common.lp24.StatusUpdate();
        List<Consumable> consumablelist = new ArrayList<>();
        
        SampleInfo sampleInfo = new SampleInfo();
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setOutputPlateType("A");
        containerInfo.setCarrierType("A");
        sampleInfo.setNewContainerId("123");
        sampleInfo.setNewOutputPosition("A");
        Consumable consumable = new Consumable();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("Passed");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setOrderResult("Passed");
        statusUpdate.setSampleInfo(sampleInfo);
        consumable.setName("PI");
        consumable.setValue("VALUE");
        consumablelist.add(consumable);
        statusUpdate.setConsumables(consumablelist);
        statusUpdate.setContainerInfo(containerInfo);
        
        specimenStatusUpdateMessage.setDeviceSerialNumber("LP001-12");
        specimenStatusUpdateMessage.setSendingApplicationName("LP24");
        specimenStatusUpdateMessage.setReceivingApplication("Connect");
        specimenStatusUpdateMessage.setDateTimeMessageGenerated("20180831150003");
        specimenStatusUpdateMessage.setContainerId("83c8a11b_A1");
        specimenStatusUpdateMessage.setResponseMessage("ABC");
        specimenStatusUpdateMessage.setOrderId(Long.parseLong("100001"));
        specimenStatusUpdateMessage.setProcessStepName("ABC");
        specimenStatusUpdateMessage.setAccessioningId("100011111");
        specimenStatusUpdateMessage.setStatusUpdate(statusUpdate);
        
        return specimenStatusUpdateMessage;
    }
    
    public AdaptorRequestMessage getAdaptorRequestMessageDTO() {
        AdaptorRequestMessage adaptorRequestMessage = new AdaptorRequestMessage();
        com.roche.connect.common.mp24.message.StatusUpdate statusUpdate =
            new com.roche.connect.common.mp24.message.StatusUpdate();
        List<com.roche.connect.common.mp24.message.Consumable> consumablelist = new ArrayList<>();
        
        com.roche.connect.common.mp24.message.SampleInfo sampleInfo =
            new com.roche.connect.common.mp24.message.SampleInfo();
        com.roche.connect.common.mp24.message.ContainerInfo containerInfo =
            new com.roche.connect.common.mp24.message.ContainerInfo();
        containerInfo.setCarrierBarcode("ABC");
        sampleInfo.setContainerType("ABC");
        com.roche.connect.common.mp24.message.Consumable consumable =
            new com.roche.connect.common.mp24.message.Consumable();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setFlag("ABC");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("Passed");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setSampleInfo(sampleInfo);
        statusUpdate.setOrderResult("Passed");
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        consumablelist.add(consumable);
        statusUpdate.setConsumables(consumablelist);
        statusUpdate.setContainerInfo(containerInfo);
        
        adaptorRequestMessage.setDeviceSerialNumber("LP001-12");
        adaptorRequestMessage.setSendingApplicationName("LP24");
        adaptorRequestMessage.setReceivingApplication("Connect");
        adaptorRequestMessage.setMessageType("ABC");
        adaptorRequestMessage.setDateTimeMessageGenerated("20180831150003");
        adaptorRequestMessage.setContainerId("83c8a11b_A1");
        adaptorRequestMessage.setAccessioningId("100011111");
        adaptorRequestMessage.setStatusUpdate(statusUpdate);
        
        return adaptorRequestMessage;
    }
    
    public AdaptorRequestMessage getAdaptorRequestMessageDTOSecondPassedWithFlags() {
        AdaptorRequestMessage adaptorRequestMessage = new AdaptorRequestMessage();
        com.roche.connect.common.mp24.message.StatusUpdate statusUpdate =
            new com.roche.connect.common.mp24.message.StatusUpdate();
        List<com.roche.connect.common.mp24.message.Consumable> consumablelist = new ArrayList<>();
        
        com.roche.connect.common.mp24.message.SampleInfo sampleInfo =
            new com.roche.connect.common.mp24.message.SampleInfo();
        com.roche.connect.common.mp24.message.ContainerInfo containerInfo =
            new com.roche.connect.common.mp24.message.ContainerInfo();
        containerInfo.setCarrierBarcode("ABC");
        sampleInfo.setContainerType("ABC");
        com.roche.connect.common.mp24.message.Consumable consumable =
            new com.roche.connect.common.mp24.message.Consumable();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setFlag("ABC");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("passed with flag");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setSampleInfo(sampleInfo);
        statusUpdate.setOrderResult("Passed");
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        consumablelist.add(consumable);
        statusUpdate.setConsumables(consumablelist);
        statusUpdate.setContainerInfo(containerInfo);
        
        adaptorRequestMessage.setDeviceSerialNumber("LP001-12");
        adaptorRequestMessage.setSendingApplicationName("LP24");
        adaptorRequestMessage.setReceivingApplication("Connect");
        adaptorRequestMessage.setMessageType("ABC");
        adaptorRequestMessage.setDateTimeMessageGenerated("20180831150003");
        adaptorRequestMessage.setContainerId("83c8a11b_A1");
        adaptorRequestMessage.setAccessioningId("100011111");
        adaptorRequestMessage.setStatusUpdate(statusUpdate);
        
        return adaptorRequestMessage;
    }
    
    
    public AdaptorRequestMessage getAdaptorRequestMessageDTOInProgress() {
        AdaptorRequestMessage adaptorRequestMessage = new AdaptorRequestMessage();
        com.roche.connect.common.mp24.message.StatusUpdate statusUpdate =
            new com.roche.connect.common.mp24.message.StatusUpdate();
        List<com.roche.connect.common.mp24.message.Consumable> consumablelist = new ArrayList<>();
        
        com.roche.connect.common.mp24.message.SampleInfo sampleInfo =
            new com.roche.connect.common.mp24.message.SampleInfo();
        com.roche.connect.common.mp24.message.ContainerInfo containerInfo =
            new com.roche.connect.common.mp24.message.ContainerInfo();
        containerInfo.setCarrierBarcode("ABC");
        sampleInfo.setContainerType("ABC");
        com.roche.connect.common.mp24.message.Consumable consumable =
            new com.roche.connect.common.mp24.message.Consumable();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setFlag("ABC");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("InProgress");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setSampleInfo(sampleInfo);
        statusUpdate.setOrderResult("Passed");
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        consumablelist.add(consumable);
        statusUpdate.setConsumables(consumablelist);
        statusUpdate.setContainerInfo(containerInfo);
        
        adaptorRequestMessage.setDeviceSerialNumber("LP001-12");
        adaptorRequestMessage.setSendingApplicationName("LP24");
        adaptorRequestMessage.setReceivingApplication("Connect");
        adaptorRequestMessage.setMessageType("ABC");
        adaptorRequestMessage.setDateTimeMessageGenerated("20180831150003");
        adaptorRequestMessage.setContainerId("83c8a11b_A1");
        adaptorRequestMessage.setAccessioningId("100011111");
        adaptorRequestMessage.setStatusUpdate(statusUpdate);
        
        return adaptorRequestMessage;
    }
    
    
    public AdaptorRequestMessage getAdaptorRequestMessageDTOAborted() {
        AdaptorRequestMessage adaptorRequestMessage = new AdaptorRequestMessage();
        com.roche.connect.common.mp24.message.StatusUpdate statusUpdate =
            new com.roche.connect.common.mp24.message.StatusUpdate();
        List<com.roche.connect.common.mp24.message.Consumable> consumablelist = new ArrayList<>();
        
        com.roche.connect.common.mp24.message.SampleInfo sampleInfo =
            new com.roche.connect.common.mp24.message.SampleInfo();
        com.roche.connect.common.mp24.message.ContainerInfo containerInfo =
            new com.roche.connect.common.mp24.message.ContainerInfo();
        containerInfo.setCarrierBarcode("ABC");
        sampleInfo.setContainerType("ABC");
        com.roche.connect.common.mp24.message.Consumable consumable =
            new com.roche.connect.common.mp24.message.Consumable();
        statusUpdate.setComment("comment");
        statusUpdate.setOrderName("OrderName");
        statusUpdate.setOperatorName("admin");
        statusUpdate.setFlag("ABC");
        statusUpdate.setRunStartTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunEndTime(new Timestamp(Long.parseLong("20181024171916")));
        statusUpdate.setRunResult("Aborted");
        statusUpdate.setUpdatedBy("ADMIN");
        statusUpdate.setComment("ABC");
        statusUpdate.setSampleInfo(sampleInfo);
        statusUpdate.setOrderResult("Passed");
        consumable.setName("ABC");
        consumable.setValue("VALUE");
        consumablelist.add(consumable);
        statusUpdate.setConsumables(consumablelist);
        statusUpdate.setContainerInfo(containerInfo);
        
        adaptorRequestMessage.setDeviceSerialNumber("LP001-12");
        adaptorRequestMessage.setSendingApplicationName("LP24");
        adaptorRequestMessage.setReceivingApplication("Connect");
        adaptorRequestMessage.setMessageType("ABC");
        adaptorRequestMessage.setDateTimeMessageGenerated("20180831150003");
        adaptorRequestMessage.setContainerId("83c8a11b_A1");
        adaptorRequestMessage.setAccessioningId("100011111");
        adaptorRequestMessage.setStatusUpdate(statusUpdate);
        
        return adaptorRequestMessage;
    }
    
    public List<SampleResultsDTO> getSampleResultsDTOList(String... statusList) {
        List<SampleResultsDTO> sampleResultDTOs = new ArrayList<>();
        
        Stream.of(statusList).forEach(e -> {
            SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
            sampleResultsDTO.setResult(e);
            sampleResultDTOs.add(sampleResultsDTO);
        });
        
        return sampleResultDTOs;
    }
    
    public ProcessStepActionDTO getProcessStepActionDTO() {
        ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
        processStepActionDTO.setProcessStepSeq(2);
        processStepActionDTO.setProcessStepName("LP Pre PCR");
        return processStepActionDTO;
    }
    
   
    @Test
    public void updateMP24ResultsTest() throws HMTPException, IOException{
        config();
        List<WfmDTO> wfmlists = new ArrayList<WfmDTO>();
        wfmlists.add(getWfmDTO());
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + "/runresultsByDeviceId" + WfmURLConstants.QUESTION_MARK,
            WfmURLConstants.DEVICE_RUN_ID + getAdaptorRequestMessage().getStatusUpdate().getOrderName() + WfmURLConstants.DPCR_PROCESSSTEP_NAME + WfmConstants.ASSAY_PROCESS_STEP_DATA.NA_EXTRACTION.toString()
                + "&deviceId=" + getAdaptorRequestMessage().getDeviceSerialNumber(),
            null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(
            "http%3A%2F%2Flocalhost",null)).thenReturn(orderClient);
       /* Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());*/
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
            WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.RMM_RUN_RESULT_BY_ID_URL + getRunResultsDTO().getRunResultId(), "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(
            "http%3A%2F%2Flocalhost",null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<RunResultsDTO>() {})).thenReturn(getRunResultsDTO());
        resultIntegrationService.updateMP24Results(wfmlists,getAdaptorRequestMessage());
    }
   
    
}
