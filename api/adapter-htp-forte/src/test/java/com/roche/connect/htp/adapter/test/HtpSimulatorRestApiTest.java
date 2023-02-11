package com.roche.connect.htp.adapter.test;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.common.server.security.entity.PasJwtToken;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO.ComplexIdDetailsStatus;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.htp.adapter.model.ComplexIdDetails;
import com.roche.connect.htp.adapter.model.Cycle;
import com.roche.connect.htp.adapter.model.Notification;
import com.roche.connect.htp.adapter.readrepository.ComplexIdDetailsReadRepository;
import com.roche.connect.htp.adapter.readrepository.CycleReadRepository;
import com.roche.connect.htp.adapter.rest.HtpAdapterRestApiImpl;
import com.roche.connect.htp.adapter.services.RunService;
import com.roche.connect.htp.adapter.services.RunServiceImpl;
import com.roche.connect.htp.adapter.services.WebServices;
import com.roche.connect.htp.adapter.writerepository.ComplexIdDetailsWriteRepository;
import com.roche.connect.htp.adapter.writerepository.CycleWriteRepository;

@PrepareForTest({ RestClientUtil.class, ThreadSessionManager.class, AdmNotificationService.class }) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" }) public class HtpSimulatorRestApiTest {
    @InjectMocks HtpAdapterRestApiImpl htpSimulatorRestApiImpl;
    @Mock ComplexIdDetailsReadRepository complexIdDetailsReadRepository;
    @Mock ComplexIdDetailsWriteRepository complexIdDetailsWriteRepository;
    @Mock RunService htpSimulatorService;
    @Mock RunServiceImpl runServiceImpl;
    @Mock Notification notification;
    @Mock Predicate<String> predicate;
    
    ComplexIdDetails complexDetails = null;
    Cycle cycle;
    ComplexIdDetailsDTO complexIdDetailsDTO;
    @Mock private Response response;
    @Mock CycleReadRepository cycleReadRepository;
    @Mock CycleWriteRepository cycleWriteRepository;
    
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    
    @Mock UserSession userSession;
    @Mock Invocation.Builder builder;
    @Mock PasJwtToken pasJwtToken;
    @Mock Invocation.Builder builder1;
    
    @Mock WebServices webServices;
    
    Map<String, Object> runObj = null;
    private int successStatus = 200;
    private int CREATED = 201;
    private int NoContent = 204;
    private int CONFLICT = 409;
    private int BadStatus = 400;
    private int Accepted = 202;
    private int UNAUTHORIZED = 401;
    private int INTERNAL_SERVER_ERROR = 500;
    Map<String, Object> ordermap;
    Map<String, Object> cyclesMap = null;
    ObjectNode json = null;
    
    @BeforeTest public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ObjectMapper mapper = new ObjectMapper();
        File runFile = new File("src/test/java/resource/run.json");
        try {
            runObj = mapper.readValue(runFile, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        json = new ObjectMapper().createObjectNode();
        json.put("freespace", 100);
        complexIdDetailsDTO = getComplexIdDetailsDTO();
        cyclesMap = getCyclesMap();
        cycle = getCycleVO();
        ordermap = getOrderDetailsMap();
        notification = getNotification();
        complexDetails = getComplexIdDetails();
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        
        Mockito.when(runServiceImpl.getOrderDetails(Mockito.anyLong(), Mockito.any())).thenReturn(ordermap);
        Mockito.when(complexIdDetailsReadRepository.findByComplexId(Mockito.anyString(), Mockito.anyLong()))
            .thenReturn(complexDetails);
        Mockito.when(htpSimulatorService.getComplexIdDetails(Mockito.anyString()))
            .thenReturn(Response.status(successStatus).entity(complexIdDetailsDTO).build());
        Mockito.when(response.readEntity(ComplexIdDetailsDTO.class)).thenReturn(complexIdDetailsDTO);
        Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong()))
            .thenReturn(complexDetails);
        Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(true);
    }
    
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test public void getOrderDetailsTest() throws Exception {
        response = htpSimulatorRestApiImpl.getOrderDetails("1312642");
        assertEquals(response.getStatus(), successStatus);
    }
    
    @Test public void notificationTest() throws Exception {
      //  MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing()
                .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());

        ReflectionTestUtils.setField(htpSimulatorRestApiImpl, "deviceEndPoint", "http://localhost");
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(userSession.getPasJwtToken()).thenReturn(pasJwtToken);
        Mockito.when(pasJwtToken.getUserName()).thenReturn("userName");
        Mockito.when(pasJwtToken.getDomainName()).thenReturn("domainName");
        Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(Mockito.anyString()))
            .thenReturn("token");
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString())).thenReturn("token");
        
        String url1 = "http://localhost/json/users/userName/userName/domainName/domainName/id";
        Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        
        Mockito.when(builder.get(String.class)).thenReturn("userId");
        
        url1 = "http://localhost/json/device/fetch/expr?filterExpression=user.id=userId";
        Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder1);
        String resp = "[ { \"deviceType\": {\"name\": \"HTP\", \"deviceTypeId\": \"fa99cb08-76bf-40c2-8dc7-9c44116dee78\" }, \"attributes\": { \"protocolVersion\": \"\",\"location\":\"location\" }, \"deviceId\": \"9b650548-e591-4293-8735-26f76dc8d4a2\",\"name\": \"HTP\",\"serialNo\":\"HTP-00123\" } ]";
        Mockito.when(builder1.get(String.class)).thenReturn(resp);
        
        response = htpSimulatorRestApiImpl.notification(notification);
        assertEquals(response.getStatus(), successStatus);
    }
    
    @Test public void updateCyclePTest() throws Exception {
        /* Mockito.when(cycle.getChecksum()).thenReturn("1234"); */
        /*
         * Mockito.when(cycle.getPath()).thenReturn("C:/");
         * Mockito.when(htpSimulatorService.getMountPath(complexDetails.
         * getDeviceId(),"C:/" )).thenReturn("C:/filepath");
         */
        Mockito.when(htpSimulatorService.checkFileSize(null)).thenReturn(true);
        Mockito.when(htpSimulatorService.updateCycle(Mockito.anyMap(), Mockito.anyString())).thenReturn(cycle);
        Mockito.when(htpSimulatorService.validateChecksum(null, "1324")).thenReturn(true);
        Mockito.when(htpSimulatorService.isValidType()).thenReturn(predicate);
        Mockito.when(htpSimulatorService.isValidType().test(Mockito.anyString())).thenReturn(true);
        response = htpSimulatorRestApiImpl.updateCycle("1234", getConfigMap());
        // assertEquals(response.getStatus(), 202);
    }
    
    @Test public void updateCycleNTest() throws Exception {
        Mockito.when(htpSimulatorService.updateCycle(Mockito.anyMap(), Mockito.anyString())).thenReturn(null);
        Mockito.when(htpSimulatorService.isValidType()).thenReturn(predicate);
        Mockito.when(htpSimulatorService.isValidType().test(Mockito.anyString())).thenReturn(false);
        response = htpSimulatorRestApiImpl.updateCycle("1234", getConfigMap());
        assertEquals(response.getStatus(), BadStatus);
    }
    
    public Map<String, Object> getConfigMap() throws Exception {
        Map<String, Object> run = new HashMap<>();
        run.put("path", "C://");
        run.put("checksum", "2345");
        run.put("cycle", "2");
        run.put("type", "fastq.gz");
        return run;
    }
    
    @Test public void freespacePTest() throws Exception {
        Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(true);
        Mockito.when(htpSimulatorService.checkFreeSpace()).thenReturn(json);
        response = htpSimulatorRestApiImpl.freespace();
        assertEquals(response.getStatus(), successStatus);
    }
    
    @Test public void freespaceNTest() throws Exception {
        Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(true);
        Mockito.when(htpSimulatorService.checkFreeSpace()).thenReturn(json.nullNode());
        response = htpSimulatorRestApiImpl.freespace();
        assertEquals(response.getStatus(), CONFLICT);
    }
    
    @Test public void updateRunPTest() throws Exception {
        Mockito.when(htpSimulatorService.convertJsonToRun(Mockito.anyMap())).thenReturn(new RunResultsDTO());
        Mockito.when(htpSimulatorService.updateRun(Mockito.anyString(), Mockito.any(RunResultsDTO.class)))
            .thenReturn(Response.status(200).build());
        response = htpSimulatorRestApiImpl.updateRun("1", cyclesMap);
    }
    
    public Notification getNotification() {
        notification = new Notification();
        notification.setCode("200");
        notification.setDatetime(new Timestamp(0));
        notification.setDescription("received Notification");
        notification.setSeverity("warning");
        notification.setEnglishDescription("English_description");
        return notification;
    }
    
    public ComplexIdDetailsDTO getComplexIdDetailsDTO() {
        ComplexIdDetailsDTO comp = new ComplexIdDetailsDTO();
        comp.setComplexCreatedDatetime(new Timestamp(System.currentTimeMillis()));
        comp.setRunProtocol("CtDNA");
        comp.setStatus(ComplexIdDetailsStatus.READY);
        return comp;
    }
    
    public Map<String, Object> getOrderDetailsMap() {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderId", "123");
        return orderMap;
    }
    
    public Cycle getCycleVO() {
        Cycle c = new Cycle();
        c.setChecksum("1324");
        c.setType("fastq.gz");
        c.setId(10000l);
        c.setStatus("Complete");
        c.setPath("C:/");
        c.setHqr(2);
        c.setReadLength(564235246);
        c.setSendToSecondaryFlag("true");
        c.setTransferComplete("complete");
        c.setCreatedBy("admin");
        c.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        c.setUpdatedBy("lab manager");
        c.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        return c;
    }
    
    public Map<String, Object> getCyclesMap() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        File cycleFile = new File("src/test/java/resource/run.json");
        Map<String, Object> cycleObj = mapper.readValue(cycleFile, Map.class);
        return cycleObj;
    }
    
    public ComplexIdDetails getComplexIdDetails() {
        ComplexIdDetails comp = new ComplexIdDetails();
        comp.setComplexId("12124314");
        comp.setDeviceId("HTP-01");
        comp.setDeviceRunId("5636575");
        comp.setId(10000l);
        return comp;
    }
    
    @Test public void setAndGetHelloFlagTest() {
        htpSimulatorRestApiImpl.setHelloFlag(true);
        htpSimulatorRestApiImpl.getHelloFlag();
        assertEquals(htpSimulatorRestApiImpl.getHelloFlag(), true);
    }
    
    @Test public void setAndGetGoodByeTest() {
        htpSimulatorRestApiImpl.setGoodBye(true);
        htpSimulatorRestApiImpl.getGoodBye();
        assertEquals(htpSimulatorRestApiImpl.getGoodBye(), true);
    }
    
    @Test public void getInstrumentIdTest() {
        htpSimulatorRestApiImpl.setInstrumentId("instrument-1");
        htpSimulatorRestApiImpl.getInstrumentId();
        assertEquals(htpSimulatorRestApiImpl.getInstrumentId(), "instrument-1");
    }
    
    @Test public void hitPingAndHelloAndgoodByeTest() {
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("instrument_id", "instrument-1");
        try {
            Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(false);
            assertEquals(htpSimulatorRestApiImpl.hitPing().getStatus(), 401);
            assertEquals(htpSimulatorRestApiImpl.hitHello(requestBody).getStatus(), 401);
            assertEquals(htpSimulatorRestApiImpl.goodBye().getStatus(), 401);
        } catch (HMTPException e) {
            e.printStackTrace();
        }
        
        try {
            Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(true);
            assertEquals(htpSimulatorRestApiImpl.hitHello(requestBody).getStatus(), 200);
            assertEquals(htpSimulatorRestApiImpl.hitPing().getStatus(), 200);
            assertEquals(htpSimulatorRestApiImpl.goodBye().getStatus(), 200);
        } catch (HMTPException e) {
            e.printStackTrace();
        }
        
    }
    
    @Test public void createRunTest() throws HMTPException, ParseException {
        runObj.put("reference_id", 123456789);
        Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(false);
        ComplexIdDetails complexIdDetails = new ComplexIdDetails();
        complexIdDetails.setComplexId("120498599");
        complexIdDetails.setId(8);
        Response response = htpSimulatorRestApiImpl.createRun(runObj);
        assertEquals(response.getStatus(), UNAUTHORIZED);
    }
    
    @Test public void createRunNTest() throws HMTPException, ParseException {
        runObj.put("reference_id", 123456789);
        Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(true);
        ComplexIdDetails complexIdDetails = new ComplexIdDetails();
        complexIdDetails.setComplexId("120498599");
        complexIdDetails.setId(8);
        Mockito.when(complexIdDetailsReadRepository.findById(Mockito.anyLong(), Mockito.anyLong()))
            .thenReturn(complexIdDetails);
        Mockito.when(complexIdDetailsWriteRepository.save(Mockito.any(ComplexIdDetails.class)))
            .thenReturn(complexIdDetails);
        Response response = htpSimulatorRestApiImpl.createRun(runObj);
        assertEquals(response.getStatus(), CREATED);
    }
    
    @Test public void completeFileTransferTest() {
        try {
            Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(true);
            Mockito.when(htpSimulatorService.isInValidRunSequence("12345", "TransferCompleted", -1L)).thenReturn(true);
            Mockito.when(complexIdDetailsReadRepository.findByDeviceRunId(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(complexDetails);
            Mockito.when(cycleReadRepository.findTopByRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(cycle);
            Mockito.when(htpSimulatorService.convertRunStatusToRun(Mockito.anyMap())).thenReturn(new RunResultsDTO());
            List<Cycle> cycleList = new ArrayList<>();
            cycleList.add(cycle);
            Mockito.when(cycleReadRepository.findByRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(cycleList);
            Mockito.when(cycleWriteRepository.save(Mockito.any(Cycle.class))).thenReturn(cycle);
            Response response = htpSimulatorRestApiImpl.completeFileTransfer("12345");
            assertEquals(response.getStatus(), successStatus);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Test public void updateLogsTest() throws IOException {
        Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(true);
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("path", "C:/");
        requestbody.put("checksum", "98765");
        Mockito.when(
            htpSimulatorService.validateChecksum(requestbody.get("path").toString(), requestbody.get("checksum").toString()))
            .thenReturn(true);
        Response response = htpSimulatorRestApiImpl.updateLogs("12345", requestbody);
        assertEquals(response.getStatus(), successStatus);
    }
    
    @Test public void updateLogsNTest() throws IOException {
        Mockito.when(webServices.getDetailsFromOauthToken(htpSimulatorRestApiImpl)).thenReturn(true);
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("path", "C:/");
        requestbody.put("checksum", "98765");
        Mockito.when(
            htpSimulatorService.validateChecksum(requestbody.get("path").toString(), requestbody.get("checksum").toString()))
            .thenReturn(false);
        Response response = htpSimulatorRestApiImpl.updateLogs("12345", requestbody);
        assertEquals(response.getStatus(), CONFLICT);
    }
    
}
