package com.roche.connect.forte.adapter.test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

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
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.htp.adapter.model.ComplexIdDetails;
import com.roche.connect.htp.adapter.model.Cycle;
import com.roche.connect.htp.adapter.model.ForteJob;
import com.roche.connect.htp.adapter.readrepository.ComplexIdDetailsReadRepository;
import com.roche.connect.htp.adapter.readrepository.CycleReadRepository;
import com.roche.connect.htp.adapter.rest.HtpAdapterRestApiImpl;
import com.roche.connect.htp.adapter.services.RunService;
import com.roche.connect.htp.adapter.services.RunServiceImpl;
import com.roche.connect.htp.adapter.services.WebServices;
import com.roche.connect.htp.adapter.writerepository.ComplexIdDetailsWriteRepository;
import com.roche.connect.htp.adapter.writerepository.CycleWriteRepository;

@PrepareForTest({ HMTPLoggerImpl.class,ThreadSessionManager.class ,AdmNotificationService.class,RunServiceImpl.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*",
"javax.management.*", "org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" }) 
public class RunServiceImplTest {
    @InjectMocks RunServiceImpl runServiceImpl;
    @Mock com.roche.connect.htp.adapter.model.Cycle cycle;
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    @Mock   WebServices webServicesImpl;
    @Mock Response response;
    @Mock WebServices webServices;
    @InjectMocks HtpAdapterRestApiImpl htpAdaptorRestApiImpl;
    @Mock ComplexIdDetailsReadRepository complexIdDetailsReadRepository;
    @Mock RunService htpSimulatorService;
    @Mock  UserSession userSession;
    @Mock CycleWriteRepository cycleWriteRepository;
    @Mock Predicate<String> value;
    @Mock ComplexIdDetailsWriteRepository complexIdDetailsWriteRepository;
    @Mock CycleReadRepository cycleReadRepository;
    @Mock ThreadLocal<Map<Integer, Cycle>>  threadLocal;
    
    
    @BeforeTest public void setUp() {
        cycle = new Cycle();
        
        cycle.setId(1000l);
        cycle.setRunId("127384");
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
    }
    
    /**
     * We need a special {@link IObjectFactory}.
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    
    @Test(priority = 1) public void getForteJobTest() {
        ForteJob forteJob = runServiceImpl.getForteJob(cycle);
        Assert.assertEquals(forteJob.getJobType(), "secondary");
    }
    
    @Test public void isInValidRunSequenceStartedTest() {
        ReflectionTestUtils.setField(runServiceImpl, "updateRunInfoUrl", "http://localhost");
        Mockito.when(webServicesImpl.getRequest("http://localhost/123")).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(RunResultsDTO.class)).thenReturn(getRunResultsDTOCreated());
        runServiceImpl.isInValidRunSequence("123", "started", 1L);
    }
    
    @Test public void isInValidRunSequenceInprocessTest() {
        ReflectionTestUtils.setField(runServiceImpl, "updateRunInfoUrl", "http://localhost");
        Mockito.when(webServicesImpl.getRequest("http://localhost/123")).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(RunResultsDTO.class)).thenReturn(getRunResultsDTOStarted());
        runServiceImpl.isInValidRunSequence("123", "inprocess", 1L);
    }
    
    @Test public void isInValidRunSequenceCompletedTest() {
        ReflectionTestUtils.setField(runServiceImpl, "updateRunInfoUrl", "http://localhost");
        Mockito.when(webServicesImpl.getRequest("http://localhost/123")).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(RunResultsDTO.class)).thenReturn(getRunResultsDTOInProcess());
        runServiceImpl.isInValidRunSequence("123", "completed", 1L);
    }
    
    @Test public void isInValidRunSequenceFailedTest() {
        ReflectionTestUtils.setField(runServiceImpl, "updateRunInfoUrl", "http://localhost");
        Mockito.when(webServicesImpl.getRequest("http://localhost/123")).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(RunResultsDTO.class)).thenReturn(getRunResultsDTOInProcess());
        runServiceImpl.isInValidRunSequence("123", "failed", 1L);
    }
    
    
    @Test public void isInValidRunSequenceTransferCompletedTest() {
        ReflectionTestUtils.setField(runServiceImpl, "updateRunInfoUrl", "http://localhost");
        Mockito.when(webServicesImpl.getRequest("http://localhost/123")).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(RunResultsDTO.class)).thenReturn(getRunResultsDTOCompleted());
        runServiceImpl.isInValidRunSequence("123", "transfercompleted", 1L);
    }
  
    
    @Test
    public void getRunPositiveTest() {
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(Mockito.anyString())).thenReturn("token");
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(htpSimulatorService.getRun(Mockito.anyString())).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.readEntity(RunResultsDTO.class)).thenReturn(getRunResultsDTOCompleted());
        Map<String, String> runResults  = new HashMap<>();
        Mockito.when(htpSimulatorService.convertRunToJson(Mockito.any(RunResultsDTO.class))).thenReturn(runResults);
        htpAdaptorRestApiImpl.getRun("1234");
    }
    @Test
    public void getRunNotificationTest() throws Exception {
        PowerMockito.mockStatic(ThreadSessionManager.class);
        PowerMockito.mockStatic(AdmNotificationService.class);
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(Mockito.anyString())).thenReturn("token");
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(htpSimulatorService.getRun(Mockito.anyString())).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(404);
        PowerMockito.doNothing()
                .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        htpAdaptorRestApiImpl.getRun("1234");
    }
    
    @Test
    public void getRunTest() {
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(Mockito.anyString())).thenReturn("token");
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexIdDetails());
        htpAdaptorRestApiImpl.getRun("1234");
    }
    
    @Test
    public void getRunNegativeTest() {
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(false);
        htpAdaptorRestApiImpl.getRun("1234");
    }
    
    @Test
    public void createRunNotificationTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "8001");
        runObject.put("run_protocol", "HTP Protocol");
        runObject.put("run_data_folder_tmp", "");
        runObject.put("run_data_folder_primary", "");
        
        PowerMockito.mockStatic(ThreadSessionManager.class);
        PowerMockito.mockStatic(AdmNotificationService.class);
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(Mockito.anyString())).thenReturn("token");
        PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        
        htpAdaptorRestApiImpl.createRun(runObject);
    }
    
    @Test
    public void createRunElseTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "8001");
        runObject.put("run_protocol", "HTP Protocol");
        runObject.put("run_data_folder_tmp", "C://");
        runObject.put("run_data_folder_primary", "C://");
        runObject.put("reference_id", "1234");
        
        PowerMockito.mockStatic(ThreadSessionManager.class);
        PowerMockito.mockStatic(AdmNotificationService.class);
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(Mockito.anyString())).thenReturn("token");
        Mockito.when(complexIdDetailsReadRepository.findById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        htpAdaptorRestApiImpl.createRun(runObject);
    }
    
    @Test
    public void createRunComplexIdMismatchElseTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("run_protocol", "HTP Protocol");
        runObject.put("run_data_folder_tmp", "C://");
        runObject.put("run_data_folder_primary", "C://");
        runObject.put("reference_id", "1234");
        
        PowerMockito.mockStatic(ThreadSessionManager.class);
        PowerMockito.mockStatic(AdmNotificationService.class);
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(Mockito.anyString())).thenReturn("token");
        Mockito.when(complexIdDetailsReadRepository.findById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        htpAdaptorRestApiImpl.createRun(runObject);
    }
    
    @Test
    public void updateRunOAuthTest() throws JsonProcessingException, ParseException {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("run_protocol", "HTP Protocol");
        runObject.put("run_data_folder_tmp", "C://");
        runObject.put("run_data_folder_primary", "C://");
        runObject.put("reference_id", "1234");
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(false);
        htpAdaptorRestApiImpl.updateRun("1234", runObject);
    }
    
    @Test
    public void updateRunComplexIdValidationTest() throws JsonProcessingException, ParseException {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("run_protocol", "HTP Protocol");
        runObject.put("run_data_folder_tmp", "C://");
        runObject.put("run_data_folder_primary", "C://");
        runObject.put("reference_id", "1234");
        
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(Mockito.anyString())).thenReturn("token");
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexIdDetails());
        htpAdaptorRestApiImpl.updateRun("1234", runObject);
    }
    
    @Test
    public void updateRunBadRequestTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("run_protocol", "HTP Protocol");
        runObject.put("run_data_folder_tmp", "C://");
        runObject.put("run_data_folder_primary", "C://");
        runObject.put("reference_id", "1234");
        
        PowerMockito.mockStatic(ThreadSessionManager.class);
        PowerMockito.mockStatic(AdmNotificationService.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        Mockito.when(htpSimulatorService.updateRun(Mockito.anyString(), Mockito.any(RunResultsDTO.class))).thenReturn(response);
        Mockito.when(htpSimulatorService.convertJsonToRun(runObject)).thenReturn(getRunResultsDTOCompleted());
        Mockito.when(response.getStatus()).thenReturn(404);
        htpAdaptorRestApiImpl.updateRun("1234", runObject);
    }
    
    
    @Test
    public void updateRunInternalServerTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("run_protocol", "HTP Protocol");
        runObject.put("run_data_folder_tmp", "C://");
        runObject.put("run_data_folder_primary", "C://");
        runObject.put("reference_id", "1234");
        
        PowerMockito.mockStatic(ThreadSessionManager.class);
        PowerMockito.mockStatic(AdmNotificationService.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        Mockito.when(htpSimulatorService.updateRun(Mockito.anyString(), Mockito.any(RunResultsDTO.class))).thenReturn(response);
        Mockito.when(htpSimulatorService.convertJsonToRun(runObject)).thenReturn(getRunResultsDTOCompleted());
        Mockito.when(response.getStatus()).thenReturn(500);
        htpAdaptorRestApiImpl.updateRun("1234", runObject);
    }
    
    
    @Test
    public void updateCycleOAuthTest() throws IOException {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(false);
        //Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(), Mockito.anyLong())).thenReturn(value)
        htpAdaptorRestApiImpl.updateCycle("1234", runObject);
    }

   
    @Test
    public void updateCycleValidationTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(htpSimulatorService.isValidType()).thenReturn(value);
        Mockito.when(htpSimulatorService.isValidType().test(Mockito.anyString())).thenReturn(false);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
                
        htpAdaptorRestApiImpl.updateCycle("1234", runObject);
    }
    
    @Test
    public void updateCycleNotificationTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(htpSimulatorService.isValidType()).thenReturn(value);
        Mockito.when(htpSimulatorService.isValidType().test(Mockito.anyString())).thenReturn(true);
        Mockito.when(htpSimulatorService.updateCycle(runObject, "1234")).thenReturn(getCycle());
        Mockito.when(htpSimulatorService.getMountPath(Mockito.anyString(), Mockito.anyString())).thenReturn("C://");
        Mockito.when(htpSimulatorService.checkFileSize(Mockito.anyString())).thenReturn(false);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.getDeviceId(Mockito.anyLong())).thenReturn("1234");
        Mockito.when(cycleWriteRepository.save(Mockito.any(Cycle.class))).thenReturn(getCycle());
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        
        htpAdaptorRestApiImpl.updateCycle("1234", runObject);
    }
    
    @Test
    public void updateCycleEmptyCycleTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(htpSimulatorService.isValidType()).thenReturn(value);
        Mockito.when(htpSimulatorService.isValidType().test(Mockito.anyString())).thenReturn(true);
        Cycle c = null;
        Mockito.when(htpSimulatorService.updateCycle(runObject, "1234")).thenReturn(c);
        
        htpAdaptorRestApiImpl.updateCycle("1234", runObject);
    }
    @Test
    public void updateCyclePositiveTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(htpSimulatorService.isValidType()).thenReturn(value);
        Mockito.when(htpSimulatorService.isValidType().test(Mockito.anyString())).thenReturn(true);
        Mockito.when(htpSimulatorService.updateCycle(runObject, "1234")).thenReturn(getCycle());
        Mockito.when(htpSimulatorService.getMountPath(Mockito.anyString(), Mockito.anyString())).thenReturn("C://");
        Mockito.when(htpSimulatorService.checkFileSize(Mockito.anyString())).thenReturn(true);
        Mockito.when(htpSimulatorService.validateChecksum(Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.getDeviceId(Mockito.anyLong())).thenReturn("1234");
        Mockito.when(cycleWriteRepository.save(Mockito.any(Cycle.class))).thenReturn(getCycle());
        
        htpAdaptorRestApiImpl.updateCycle("1234", runObject);
    }
    
    @Test
    public void updateCycleIfTest() throws Exception {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        runObject.put("cycle", "cycleObject");
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(htpSimulatorService.isValidType()).thenReturn(value);
        Mockito.when(htpSimulatorService.isValidType().test(Mockito.anyString())).thenReturn(true);
        Mockito.when(htpSimulatorService.updateCycle(runObject, "1234")).thenReturn(getCycle());
        Mockito.when(htpSimulatorService.getMountPath(Mockito.anyString(), Mockito.anyString())).thenReturn("C://");
        Mockito.when(htpSimulatorService.checkFileSize(Mockito.anyString())).thenReturn(true);
        Mockito.when(htpSimulatorService.validateChecksum(Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.getDeviceId(Mockito.anyLong())).thenReturn("1234");
        Mockito.when(cycleWriteRepository.save(Mockito.any(Cycle.class))).thenReturn(getCycle());
        
        htpAdaptorRestApiImpl.updateCycle("1234", runObject);
    }
    
    @Test
    public void completeCycleOAuthTest() {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        runObject.put("cycle", "cycleObject");
        
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(false);
        htpAdaptorRestApiImpl.completeCycle("1234", runObject);
    }
    
    
    @Test
    public void completeCycleNegativeTest() {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        runObject.put("cycle", "cycleObject");
        
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(false);
        Mockito.when(htpSimulatorService.getDeviceId(Mockito.anyLong())).thenReturn("1234");
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        htpAdaptorRestApiImpl.completeCycle("1234", runObject);
    }
    
    @Test
    public void completeCycleNullCycleTest() {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        runObject.put("cycle", "cycleObject");
        
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        htpAdaptorRestApiImpl.completeCycle("1234", runObject);
    }
    
    @Test
    public void completeCyclePositiveTest() {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        runObject.put("cycle", 1);
        
        List<Cycle> cycleList = new ArrayList<>(); 
        cycleList.add(getCycle());
        
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(cycleReadRepository.findByRunIdAndCyclesNumber(Mockito.anyString(),
            Mockito.anyInt(), Mockito.anyLong())).thenReturn(cycleList);
        Mockito.when( cycleWriteRepository.save(Mockito.any(Cycle.class))).thenReturn(getCycle());
        htpAdaptorRestApiImpl.completeCycle("1234", runObject);
    }
    @Test
    public void completeCycleElseTest() {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("type", "cycle");
        runObject.put("cycle", 1);
        
        
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(cycleReadRepository.findByRunIdAndCyclesNumber(Mockito.anyString(),
            Mockito.anyInt(), Mockito.anyLong())).thenReturn(null);
        htpAdaptorRestApiImpl.completeCycle("1234", runObject);
    }
    
    
    @Test
    public void completeFileTransferOAuthTest() {
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(false);
        htpAdaptorRestApiImpl.completeFileTransfer("1234");
    }
    
    @Test
    public void completeFileTransferNegativeTest() {
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(false);
        Mockito.when(htpSimulatorService.getDeviceId(Mockito.anyLong())).thenReturn("1234");
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        htpAdaptorRestApiImpl.completeFileTransfer("1234");
    }
 
    @Test
    public void completeFileTransferPositiveTest() {
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(),Mockito.anyLong())).thenReturn(true);
       /* Mockito.when(htpSimulatorService.getDeviceId(Mockito.anyLong())).thenReturn("1234");
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));*/
        Mockito.when(htpSimulatorService.convertRunStatusToRun(Mockito.anyMap())).thenReturn(getRunResultsDTOCompleted());
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(),Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(cycleReadRepository.findTopByRunId(Mockito.anyString(),Mockito.anyLong())).thenReturn(getCycleForFileTransfer());
        
        List<Cycle> cycles  = new ArrayList<>();
        cycles.add(getCycleForFileTransfer());
        Mockito.when(cycleReadRepository.findByRunId(Mockito.anyString(),Mockito.anyLong())).thenReturn(cycles);
        Mockito.when(cycleWriteRepository.save(Mockito.any(Cycle.class))).thenReturn(getCycleForFileTransfer());
        PowerMockito.mockStatic(RunServiceImpl.class);
        Mockito.when(RunServiceImpl.getAddcycle()).thenReturn(threadLocal);
        Mockito.doNothing().when(threadLocal).remove();
        htpAdaptorRestApiImpl.completeFileTransfer("1234");
    }
    
    public Cycle getCycleForFileTransfer() {
        Cycle cycle = new Cycle();
        Company company = new Company();
        company.setId(1L);
        cycle.setCompany(company);
        cycle.setPath("C://");
        cycle.setChecksum("123456789");
        cycle.setType("cycle");
        return cycle;
    }
    
    @Test
    public void getOrderDetailsOAuthTest() throws HMTPException, ParseException {
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(false);
        htpAdaptorRestApiImpl.getOrderDetails("8001");
    }
    
    @Test
    public void getOrderDetailsNotificationTest() throws Exception {
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        Mockito.when( complexIdDetailsReadRepository.findByComplexId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(htpSimulatorService.getComplexIdDetails(Mockito.anyString())).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(404);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        
        htpAdaptorRestApiImpl.getOrderDetails("8001");
    }
    @Test
    public void getOrderDetailsComplexIdDetailsNullTest() throws Exception {
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        ComplexIdDetails  complexdetails = null;
        Mockito.when( complexIdDetailsReadRepository.findByComplexId(Mockito.anyString(), Mockito.anyLong())).thenReturn(complexdetails);
        Mockito.when(complexIdDetailsWriteRepository.save(Mockito.any(ComplexIdDetails.class))).thenReturn(getComplexDetailsWithDeviceRunId());
        Mockito.when(htpSimulatorService.getComplexIdDetails(Mockito.anyString())).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(404);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
        
        htpAdaptorRestApiImpl.getOrderDetails("8001");
    }
    
    @Test
    public void updateRunStatusOAuthTest() throws JsonProcessingException, HMTPException, ParseException {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("run_status", "started");
        
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(false);
        htpAdaptorRestApiImpl.updateRunStatus("1234", runObject);
    }
    
    
    @Test
    public void updateRunStatusNegativeTest() throws JsonProcessingException, HMTPException, ParseException {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("run_status", "started");
        
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(), Mockito.anyLong())).thenReturn(false);
        Mockito.when(htpSimulatorService.getDeviceId(Mockito.anyLong())).thenReturn("1234");
        Mockito.doNothing().when(htpSimulatorService).sendNotification(Mockito.anyString(),Mockito.anyString(),Mockito.any(NotificationGroupType.class));
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        htpAdaptorRestApiImpl.updateRunStatus("1234", runObject);
    }
    
    @Test
    public void updateRunStatusPositiveTest() throws JsonProcessingException, HMTPException, ParseException {
        Map<String, Object> runObject = new HashMap<>();
        runObject.put("run_id", "1234");
        runObject.put("complex_id", "7001");
        runObject.put("run_status", "started");
        
        Mockito.when(webServices.getDetailsFromOauthToken(Mockito.any(HtpAdapterRestApiImpl.class))).thenReturn(true);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(ThreadSessionManager.currentUserSession().getUserId()).thenReturn(1L);
        Mockito.when(htpSimulatorService.isInValidRunSequence(Mockito.anyString(),Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(),Mockito.anyLong())).thenReturn(getComplexIdDetails());
        Mockito.when(htpSimulatorService.convertRunStatusToRun(runObject)).thenReturn(getRunResultsDTOCompleted());
        Mockito.doNothing().when(htpSimulatorService).updateRunStatus(Mockito.anyString(), Mockito.any(RunResultsDTO.class));
        htpAdaptorRestApiImpl.updateRunStatus("1234", runObject);
    }
    
    
    
    public  RunResultsDTO getRunResultsDTOCreated() {
        RunResultsDTO runResultsDTO = new   RunResultsDTO();
        runResultsDTO.setRunStatus("Created");
        return runResultsDTO;
    }
    public  RunResultsDTO getRunResultsDTOStarted() {
        RunResultsDTO runResultsDTO = new   RunResultsDTO();
        runResultsDTO.setRunStatus("Started");
        return runResultsDTO;
    }
    public  RunResultsDTO getRunResultsDTOInProcess() {
        RunResultsDTO runResultsDTO = new   RunResultsDTO();
        runResultsDTO.setRunStatus("InProcess");
        return runResultsDTO;
    }
    
    public  RunResultsDTO getRunResultsDTOCompleted() {
        RunResultsDTO runResultsDTO = new   RunResultsDTO();
        runResultsDTO.setRunStatus("Completed");
        runResultsDTO.setDeviceId("HTP-001");
        return runResultsDTO;
    }
    
    public ComplexIdDetails getComplexDetailsWithDeviceRunId() {
        ComplexIdDetails complexIdDetails = new ComplexIdDetails();
        complexIdDetails.setCreatedBy("admin");
        complexIdDetails.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        complexIdDetails.getUpdatedDateTime();
        complexIdDetails.getOwnerPropertyName();
        Company company = new Company();
        company.setId(1L);
        complexIdDetails.setCompany(company );
        complexIdDetails.setDeviceRunId("1234");
        complexIdDetails.setComplexId("8001");
        complexIdDetails.setDeviceId("HTP-001");
        System.out.println(complexIdDetails.getCreatedBy()+complexIdDetails.getCreatedDateTime()+complexIdDetails.getCompany());
        return complexIdDetails;
        
    }
    
    @Test
    public ComplexIdDetails getComplexIdDetails() {
        ComplexIdDetails complexIdDetails = new ComplexIdDetails();
        complexIdDetails.setCreatedBy("admin");
        complexIdDetails.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        complexIdDetails.getUpdatedDateTime();
        complexIdDetails.getOwnerPropertyName();
        Company company = new Company();
        company.setId(1L);
        complexIdDetails.setCompany(company );
        
        System.out.println(complexIdDetails.getCreatedBy()+complexIdDetails.getCreatedDateTime()+complexIdDetails.getCompany());
        return complexIdDetails;
        
    }
    
    @Test
    public Cycle getCycle() {
        Cycle cycle = new Cycle();
        Company company = new Company();
        company.setId(1L);
        cycle.setCompany(company);
        cycle.setPath("C://");
        cycle.setChecksum("123456789");
        System.out.println( cycle.getCompany());
        cycle.setType("cycle");
        return cycle;
    }
   
    @Test
    public ForteJob getForteJob() {
        ForteJob forteJob = new ForteJob();
        forteJob.setCycleId(getCycle());
        forteJob.setQc("qc");
        forteJob.setErrorKey("errorKey");
        forteJob.setEstimatedTimeToCompletion("123");
        forteJob.setSentToTertiary("ongoing");
        Company company = new Company();
        company.setId(1L);
        forteJob.setCompany(company);
        
        System.out.println( forteJob.getCycleId()+forteJob.getQc()+forteJob.getErrorKey()+forteJob.getEstimatedTimeToCompletion()+
            forteJob.getSentToTertiary()+forteJob.getCreatedDateandTime()+ forteJob.getCompany());
        return forteJob;
    }
}
