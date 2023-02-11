package com.roche.connect.imm.testng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.enums.AssayType;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.ForteDetailImpl;
import com.roche.connect.imm.service.MessageProcessorService;
import com.roche.connect.imm.service.OrderIntegrationService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.service.WFMIntegrationService;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class }) @PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*",
"javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*", "org.apache.logging.*",
"org.slf4j.*" }) public class ForteDetailImplTest {
    
    @InjectMocks ForteDetailImpl forteDetailImpl;
    @Mock AssayIntegrationService assayService;
    @Mock MessageProcessorService messageProcessorService;
    @Mock WFMIntegrationService wfmIntegrationService;
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    @Mock OrderIntegrationService orderIntegrationService;
    @Mock RmmIntegrationService rmmService;
    List<ProcessStepActionDTO> allProcessStepList = new ArrayList<>();
    List<SampleResultsDTO> sampleResults = new ArrayList<>();
    List<OrderDTO> patientAssayDetails = new ArrayList<>();
    
    @BeforeTest public void setUp() {
        MockitoAnnotations.initMocks(this);
        allProcessStepList.add(getProcessStepActionDTO());
        allProcessStepList.add(getProcessStepActionDTONext());
        sampleResults.add(getSampleResultsDTO());
        patientAssayDetails.add(getOrderDTO());
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
    }
    
    @Test public void getFortePutJobTest() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("complexId", "8001");
        map.put("jobStatus", "done");
        map.put("jobType", "secondaryAnalysis");
        Mockito.when(assayService.getProcessStepAction(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(allProcessStepList);
        Mockito.when(messageProcessorService.getSampleResultsByProcessStepAndInputContainerId(Mockito.anyString(),
            Mockito.anyString())).thenReturn(sampleResults);
        Mockito
            .when(wfmIntegrationService.updateForteStatus(Mockito.any(ForteStatusMessage.class), Mockito.anyString()))
            .thenReturn(true);
        forteDetailImpl.getFortePutJob("FORTE-001", "FORTE", map, "token");
    }
    
    @Test public void getFortePutJobNegativeTest() throws IOException {
        Map<String, Object> map = new HashMap<>();
        forteDetailImpl.getFortePutJob("FORTE-001", "FORTE", map, "token");
    }
    
    @Test public void updateForteStatusTest() throws IOException {
        Mockito.when(assayService.getProcessStepAction(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(allProcessStepList);
        Mockito.when(messageProcessorService.getSampleResultsByProcessStepAndInputContainerId(Mockito.anyString(),
            Mockito.anyString())).thenReturn(sampleResults);
        Mockito
            .when(wfmIntegrationService.updateForteStatus(Mockito.any(ForteStatusMessage.class), Mockito.anyString()))
            .thenReturn(true);
        forteDetailImpl.updateForteStatus("8001", getForteStatusMessage(), "token");
    }
    
    @Test public void getForteGetJobTest() throws UnsupportedEncodingException, HMTPException {
        Map<String, Object> map = new HashMap<>();
        map.put("complexId", "8001");
        map.put("jobStatus", "done");
        map.put("jobType", "secondaryAnalysis");
        
        Mockito.when(assayService.getProcessStepAction(AssayType.NIPT.getText(),null))
            .thenReturn(allProcessStepList);
        Mockito.when(messageProcessorService.getSampleResultsByProcessStepAndInputContainerId(Mockito.anyString(),
            Mockito.anyString())).thenReturn(sampleResults);
        Mockito.when(orderIntegrationService.getPatientAssayDetailsByAccesssioningId(Mockito.anyString()))
            .thenReturn(patientAssayDetails);
        Mockito.when(rmmService.getMolecularIdByProcessStepNameAndAccessioningId(Mockito.anyString(),Mockito.anyString())).thenReturn("200");
        forteDetailImpl.getForteGetJob(map);
    }
    
    @Test public void getForteGetJobNegativeTest() throws UnsupportedEncodingException, HMTPException {
        Map<String, Object> map = new HashMap<>();
        forteDetailImpl.getForteGetJob(map);
    }
    
    public ForteStatusMessage getForteStatusMessage() {
        ForteStatusMessage forteStatus = new ForteStatusMessage();
        return forteStatus;
    }
    
    public ProcessStepActionDTO getProcessStepActionDTO() {
        ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
        processStepActionDTO.setProcessStepName("LPSEQPREP");
        processStepActionDTO.setProcessStepSeq(4);
        processStepActionDTO.setDeviceType("HTP");
        return processStepActionDTO;
    }
    
    public ProcessStepActionDTO getProcessStepActionDTONext() {
        ProcessStepActionDTO processStepActionDTO = new ProcessStepActionDTO();
        processStepActionDTO.setProcessStepName("LPSEQPREP");
        processStepActionDTO.setProcessStepSeq(1);
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
    
    public OrderDTO getOrderDTO() {
        OrderDTO o = new OrderDTO();
        AssayDTO assayDto = new AssayDTO();
        assayDto.setEggDonor("32");
        assayDto.setFetusNumber("2");
        assayDto.setGestationalAgeDays(30);
        assayDto.setGestationalAgeWeeks(2);
        assayDto.setMaternalAge(20);
        o.setAssay(assayDto);
        return o;
        
    }
    
}
