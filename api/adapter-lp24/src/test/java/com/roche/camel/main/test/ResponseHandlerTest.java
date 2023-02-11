package com.roche.camel.main.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.dto.MessageDto;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.ContainerInfo;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SampleInfo;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.lpcamel.dto.HL7HeaderSegmentDTO;
import com.roche.lpcamel.dto.MP24ConfigDetails;
import com.roche.lpcamel.dto.MessageContainerDTO;
import com.roche.lpcamel.dto.MessagePayload;
import com.roche.lpcamel.dto.ResponseDTO;
import com.roche.lpcamel.dto.ResponseDTO.Status;
import com.roche.lpcamel.dto.SampleTypeDTO;
import com.roche.lpcamel.service.DeviceHandlerService;
import com.roche.lpcamel.service.ResponseHandlerService;
import com.roche.lpcamel.util.InstanceUtil;
import com.roche.lpcamel.util.MP24ParamConfig;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v251.datatype.CE;
import ca.uhn.hl7v2.model.v251.datatype.CWE;
import ca.uhn.hl7v2.model.v251.datatype.DTM;
import ca.uhn.hl7v2.model.v251.datatype.EI;
import ca.uhn.hl7v2.model.v251.datatype.EIP;
import ca.uhn.hl7v2.model.v251.datatype.FT;
import ca.uhn.hl7v2.model.v251.datatype.HD;
import ca.uhn.hl7v2.model.v251.datatype.ID;
import ca.uhn.hl7v2.model.v251.datatype.IS;
import ca.uhn.hl7v2.model.v251.datatype.NA;
import ca.uhn.hl7v2.model.v251.datatype.NM;
import ca.uhn.hl7v2.model.v251.datatype.PT;
import ca.uhn.hl7v2.model.v251.datatype.ST;
import ca.uhn.hl7v2.model.v251.datatype.TS;
import ca.uhn.hl7v2.model.v251.datatype.VID;
import ca.uhn.hl7v2.model.v251.group.SSU_U03_SPECIMEN;
import ca.uhn.hl7v2.model.v251.group.SSU_U03_SPECIMEN_CONTAINER;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.model.v251.message.ESU_U01;
import ca.uhn.hl7v2.model.v251.message.RSP_K11;
import ca.uhn.hl7v2.model.v251.message.SSU_U03;
import ca.uhn.hl7v2.model.v251.segment.EQU;
import ca.uhn.hl7v2.model.v251.segment.ERR;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.NTE;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.QAK;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.model.v251.segment.SAC;
import ca.uhn.hl7v2.model.v251.segment.SPM;
import ca.uhn.hl7v2.parser.PipeParser;

@PrepareForTest({ RestClientUtil.class, InstanceUtil.class, CharStreams.class, MP24ParamConfig.class,HMTPLoggerImpl.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*",
"javax.management.*", "org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" }) 
public class ResponseHandlerTest {
    @Mock private ResourceLoader resourceLoader;
    @InjectMocks private ResponseHandlerService responseHandlerService;
    @Mock InstanceUtil instanceUtil;
    @Mock Map<String, MessageContainerDTO> messageContainerDTO;
    @Mock MessageContainerDTO containerDTO;
    @Mock Exchange exchange;
    @Mock org.apache.camel.Message camelMessage;
    @Mock IoSession session;
    @Mock AsyncCallback callback;
    @Mock private Resource resource;
    @Mock InputStream stream;
    @Mock HapiContext context;
    @Mock PipeParser p;
    @Mock RSP_K11 queryMessage1;
    @Mock MSA msa;
    @Mock MSH msh1;
    
    
    @Mock ResourceLoader mockResourceLoader;
    HL7HeaderSegmentDTO hl7HeaderSegmentDTO;
    @Mock Invocation.Builder builder;
    @Mock Invocation.Builder builder2;
    
    MessagePayload messagePayload;
    
    
    @Mock Response response;
    @Mock Response resp;
    
    Logger logger = LogManager.getLogger(ResponseHandlerServiceTest.class);
    
    @Mock SSU_U03 statusMessage;
    

    @Mock SSU_U03_SPECIMEN_CONTAINER sacContainer;
    @Mock NTE nte;
    @Mock SAC sac;
    @Mock SPM spm;
    @Mock SSU_U03_SPECIMEN ssuSpecimen;
    List<OBX> sacObxList = new ArrayList<>();
    List<OBX> spmObxList = new ArrayList<>();
    @Mock HD hd;
    @Mock ST st;
    @Mock TS ts;
    @Mock DTM dtm;
    @Mock IS is;
    @Mock FT ft;
    @Mock CE ce;
    @Mock EI ei;
    @Mock NA na;
    @Mock NM nm;
    @Mock OBX obx;
    @Mock OBX obx2;
    @Mock OBX obx3;
    @Mock Varies varies;
    @Mock Type type;
    @Mock EIP eip;
    @Mock CWE cwe;
    @Mock ID id;
    @Mock ERR err;
    @Mock QAK qak;
    @Mock QPD qpd;
    @Mock ORC orc;
    @Mock OBR obr;
    
    @Mock
    ESU_U01 statusMessageU01;    
    @Mock EQU equ;
    @Mock Map<String, MessageContainerDTO> messageContainerDTO1;
    @Mock HapiContext hapiContext;
    @Mock PT pt;
    @Mock VID vid; 
    
    @Mock ResponseMessage theMessage;
    @Mock ResponseMessage responseMessage;
    @Mock MP24ParamConfig mp24ParamConfig;
    
    @Mock InputStreamReader inputStreamReader;
    @Mock Message message;
    Map<String, IoSession> deviceMap = new HashMap<>();
    @Mock private DeviceHandlerService deviceHandlerService;
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    
    @BeforeTest public void setUp() throws Exception {
        hl7HeaderSegmentDTO = getHL7HeaderSegmentDTO();
        messagePayload = getMessagePayload();
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
    
    @Test public void convertQBPToMsgPayloadPosTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(InstanceUtil.class);
        ReflectionTestUtils.setField(responseHandlerService, "loginUrl", "http://abc.com");
        ReflectionTestUtils.setField(responseHandlerService, "loginEntity", "http://loginEntity.com");
        charStreamMockForAck();
        instanceUtilMock();
        mockHL7forQBPandRSP();
        final Invocation.Builder builder = Mockito.mock(Invocation.Builder.class);
        final Invocation.Builder builder2 = Mockito.mock(Invocation.Builder.class);
        
        Mockito.when(RestClientUtil.getBuilder("http://abc.com", null)).thenReturn(builder);
        javax.ws.rs.client.Entity<String> entity2 =
            Entity.entity("http://loginEntity.com", MediaType.APPLICATION_FORM_URLENCODED);
        Mockito
            .when(
                RestClientUtil.getBuilder("null?source=device&devicetype=LP24&messagetype=QBP&deviceid=LP001-12", null))
            .thenReturn(builder2);
        Mockito.when(builder.post(entity2, String.class)).thenReturn("token");
        Mockito.when(builder2.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder2);
        
        javax.ws.rs.client.Entity<MessagePayload> entity =
            javax.ws.rs.client.Entity.entity(messagePayload, MediaType.APPLICATION_JSON);
        Mockito.when(builder2.post(entity)).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(p.parse(Mockito.anyString())).thenReturn(queryMessage1);
        Mockito.when(containerDTO.getHeaderSegment()).thenReturn(getHL7HeaderSegmentDTO());
        Mockito.when(queryMessage1.getERR()).thenReturn(err);
        
        deviceMap.put("MP1239", session);
        Mockito.when(deviceHandlerService.updateDeviceStatus(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(true);
        try {
            responseHandlerService.convertQBPToMsgPayload(getHL7HeaderSegmentDTO());
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        logger.info(hl7HeaderSegmentDTO.getCharacterSet() + hl7HeaderSegmentDTO.getDateTimeMessageGenerated()
            + hl7HeaderSegmentDTO.getDeviceSerialNumber() + hl7HeaderSegmentDTO.getMessageControlId()
            + hl7HeaderSegmentDTO.getMessageQueryName() + hl7HeaderSegmentDTO.getMessageType()
            + hl7HeaderSegmentDTO.getProcessingId() + hl7HeaderSegmentDTO.getQueryDefDesc()
            + hl7HeaderSegmentDTO.getQueryDefEncodingSystem() + hl7HeaderSegmentDTO.getQueryDefId()
            + hl7HeaderSegmentDTO.getReceivingApplication() + hl7HeaderSegmentDTO.getReceivingFacility()
            + hl7HeaderSegmentDTO.getSampleID() + hl7HeaderSegmentDTO.getSendingApplicationName()
            + hl7HeaderSegmentDTO.getSendingFacility() + hl7HeaderSegmentDTO.getVersionId());
        
    }
     
        
    
   @Test
   public void convertSSUToMsgPayloadTest() throws Exception {
       charStreamMockForAck();
       instanceUtilMock();
       mockHL7forQBPandRSP();
        try {
            responseHandlerService.convertSSUToMsgPayload(statusMessage);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
       
   }
    
    @Test public void sendResponseToLPTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(InstanceUtil.class);
        charStreamMockForResponse();
        mockHL7forQBPandRSP();
        
        Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
        Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
        Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
        Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
            .thenReturn(containerDTO);
        Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(camelMessage);
        Mockito.when(exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class)).thenReturn(session);
        Mockito.when(exchange.getOut()).thenReturn(camelMessage);
        Mockito.when(containerDTO.getCallback()).thenReturn(callback);
        Mockito.doNothing().when(callback).done(false);
        Mockito.when(resourceLoader.getResource(Mockito.anyString())).thenReturn(resource);
        Mockito.when(resource.getInputStream()).thenReturn(stream);
        Mockito.when(context.getPipeParser()).thenReturn(p);
        Mockito.when(queryMessage1.get("MSH")).thenReturn(msh1);
        Mockito.when(queryMessage1.get("MSA")).thenReturn(msa);
        Mockito.when(p.parse(Mockito.anyString())).thenReturn(queryMessage1);
        Mockito.when(theMessage.getMessageControlId()).thenReturn("6001");
        
        HL7HeaderSegmentDTO hl7 = getHL7HeaderSegmentDTO();
        Mockito.when(containerDTO.getHeaderSegment()).thenReturn(hl7);
        
        Mockito.when(qak.getQak3_MessageQueryName()).thenReturn(ce);
        Mockito.when(qpd.getQpd3_UserParametersInsuccessivefields()).thenReturn(varies);
        Mockito.when(qpd.getQpd2_QueryTag()).thenReturn(st);
        Mockito.when(orc.getOrc2_PlacerOrderNumber()).thenReturn(ei);
        Mockito.when(orc.getOrc5_OrderStatus()).thenReturn(id);
        Mockito.when(spm.getSpm27_ContainerType()).thenReturn(cwe);
        Mockito.doNothing().when(obr).clear();
        Mockito.when(deviceHandlerService.updateDeviceStatus(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(true);
        
        responseHandlerService.sendResponseToLP(getResponseMessage());
        responseHandlerService.sendResponseToLP(getResponseMessageDuplicate());
        responseHandlerService.sendResponseToLP(getResponseMessageApplicationError());
        try {} catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
   
    
    @Test public void sendACKToLPTest() throws IOException, HL7Exception {
        instanceUtilMock();
        charStreamMockForAck();
        responseHandlerService.sendACKToLP(getAcknowledgement());
        responseHandlerService.sendACKToLP(getAcknowledgementApplicationRejectError());
    }
    
    
    
    @Test public void hitPingTest() {
        responseHandlerService.hitPing();
    }
    
    @Test public void hitHelloTest() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("instrumentId", "LP0012");
        try {
            responseHandlerService.hitHello(requestBody);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Test
    public void getNoOrderResponseTest() throws Exception {
        instanceUtilMock();
        charStreamMockForResponse();
        mockHL7forQBPandRSP();
        responseHandlerService.getNoOrderResponse(getHL7HeaderSegmentDTO());
    }
    public void instanceUtilMock() throws IOException, HL7Exception {
        PowerMockito.mockStatic(InstanceUtil.class);
        Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
        Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
        Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
        Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
            .thenReturn(containerDTO);
        Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(camelMessage);
        Mockito.when(exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class)).thenReturn(session);
        Mockito.when(exchange.getOut()).thenReturn(camelMessage);
        Mockito.when(containerDTO.getCallback()).thenReturn(callback);
        Mockito.doNothing().when(callback).done(false);
        Mockito.when(resourceLoader.getResource(Mockito.anyString())).thenReturn(resource);
        Mockito.when(resource.getInputStream()).thenReturn(stream);
        Mockito.when(context.getPipeParser()).thenReturn(p);
        Mockito.when(queryMessage1.get("MSH")).thenReturn(msh1);
        Mockito.when(queryMessage1.get("MSA")).thenReturn(msa);
    }
    
    public void charStreamMockForAck() throws IOException {
        PowerMockito.mockStatic(CharStreams.class);
        Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class)))
            .thenReturn("MSH|^~\\&|||||||ACK^U03^ACK||P|2.5.1||||||UNICODE UTF-8\n" + "MSA||||");
    }
    
    public void getRestClientForNotification() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        MessageDto notification = null;
        ReflectionTestUtils.setField(responseHandlerService, "admUrl", "http://localhost");
        String admUrl = "http://localhost";
        String loginUrl = "http://localhost";
        
        Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(builder);
        String loginEntity = "http://localhost";
        Entity<String> entity = Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED);
        Mockito.when(builder.post(entity, String.class)).thenReturn("token");
        String url = "http://localhost";
        Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder2);
        Mockito.when(builder2.header("Cookie", "brownstoneauthcookie= token")).thenReturn(builder2);
        Entity<MessageDto> entity2 = Entity.entity(notification, MediaType.APPLICATION_JSON);
        Mockito.when(builder2.post(entity2)).thenReturn(response);
        Mockito.when(resp.getStatus()).thenReturn(200);
    }
   
    public void charStreamMockForResponse() throws IOException {
        PowerMockito.mockStatic(CharStreams.class);
        Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class)))
            .thenReturn("MSH|^~\\&|||||||RSP^WOS^RSP_K11|||2.5.1||||||UNICODE UTF-8\n" + "MSA||||\n" + "QAK|1|||\n"
                + "QPD||1||\n" + "SPM||||||||||||||||||\n" + "SAC||||||||||||\n" + "ORC|NW|||||||||\n"
                + "OBR|1|||||||||||||||||||||||||||||");
    }
    
   
    public void mockHL7forQBPandRSP() throws HL7Exception {
        Mockito.when(statusMessage.get("MSH")).thenReturn(msh1);
        Mockito.when(statusMessage.getSPECIMEN_CONTAINER()).thenReturn(sacContainer);
        Mockito.when(statusMessage.get("EQU")).thenReturn(equ);
        Mockito.when(sacContainer.get("NTE")).thenReturn(nte);
        Mockito.when(sacContainer.getSAC()).thenReturn(sac);
        Mockito.when(sacContainer.getSPECIMEN()).thenReturn(ssuSpecimen);
        Mockito.when(sacContainer.getSPECIMEN().getSPM()).thenReturn(spm);
        sacObxList.add(obx);
        sacObxList.add(obx2);
        sacObxList.add(obx3);
        spmObxList.add(obx);
        spmObxList.add(obx2);
        spmObxList.add(obx3);
        Mockito.when(sacContainer.getOBXAll()).thenReturn(sacObxList);
        Mockito.when(sacContainer.getSPECIMEN().getOBXAll()).thenReturn(spmObxList);
        Mockito.when(msh1.getMsh3_SendingApplication()).thenReturn(hd);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd2_UniversalID()).thenReturn(st);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue()).thenReturn("MP0012");
        Mockito.when(msh1.getMsh7_DateTimeOfMessage()).thenReturn(ts);
        Mockito.when(msh1.getMsh7_DateTimeOfMessage().getTs1_Time()).thenReturn(dtm);
        Mockito.when(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue()).thenReturn("20181231000000");
        Mockito.when(msh1.getMsh3_SendingApplication()).thenReturn(hd);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID()).thenReturn(is);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue()).thenReturn("MP24");
        Mockito.when(msh1.getMsh5_ReceivingApplication()).thenReturn(hd);
        Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID()).thenReturn(is);
        Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue()).thenReturn("Connect");
        Mockito.when(msh1.getMsh10_MessageControlID()).thenReturn(st);
        Mockito.when(msh1.getMsh10_MessageControlID().getValue()).thenReturn("8000");
        
        Mockito.when(nte.getNte3_Comment(0)).thenReturn(ft);
        Mockito.when(nte.getNte3_Comment(0).getValue()).thenReturn("Comment");
        Mockito.when(equ.getEqu3_EquipmentState()).thenReturn(ce);
        Mockito.when(equ.getEqu3_EquipmentState().getCe1_Identifier()).thenReturn(st);
        Mockito.when(equ.getEqu3_EquipmentState().getCe1_Identifier().getValue()).thenReturn("RUNNING");
        Mockito.when(equ.getEqu2_EventDateTime()).thenReturn(ts);
        Mockito.when(equ.getEqu2_EventDateTime().getTs1_Time()).thenReturn(dtm);
        Mockito.when(equ.getEqu2_EventDateTime().getTs1_Time().getValue()).thenReturn("201812310000000");
        Mockito.when(sac.getSac3_ContainerIdentifier()).thenReturn(ei);
        Mockito.when(sac.getSac3_ContainerIdentifier().getEi1_EntityIdentifier()).thenReturn(st);
        Mockito.when(sac.getSac3_ContainerIdentifier().getEi1_EntityIdentifier().getValue()).thenReturn("1");
        Mockito.when(sac.getSac8_ContainerStatus()).thenReturn(ce);
        Mockito.when(sac.getSac8_ContainerStatus().getCe1_Identifier()).thenReturn(st);
        Mockito.when(sac.getSac8_ContainerStatus().getCe1_Identifier().getValue()).thenReturn("R");
        Mockito.when(sac.getSac9_CarrierType()).thenReturn(ce);
        Mockito.when(sac.getSac9_CarrierType().getCe1_Identifier()).thenReturn(st);
        Mockito.when(sac.getSac9_CarrierType().getCe1_Identifier().getValue()).thenReturn("CarrierType");
        Mockito.when(sac.getSac10_CarrierIdentifier()).thenReturn(ei);
        Mockito.when(sac.getSac10_CarrierIdentifier().getEi1_EntityIdentifier()).thenReturn(st);
        Mockito.when(sac.getSac10_CarrierIdentifier().getEi1_EntityIdentifier().getValue())
            .thenReturn("Carrier Identifier");
        Mockito.when(sac.getSac11_PositionInCarrier()).thenReturn(na);
        Mockito.when(sac.getSac11_PositionInCarrier().getValue1()).thenReturn(nm);
        Mockito.when(sac.getSac11_PositionInCarrier().getValue1().getValue()).thenReturn("2");
        Mockito.when(sac.getSac21_ContainerVolume()).thenReturn(nm);
        Mockito.when(sac.getSac21_ContainerVolume().getValue()).thenReturn("4000");
        Mockito.when(sac.getSac22_AvailableSpecimenVolume()).thenReturn(nm);
        Mockito.when(sac.getSac22_AvailableSpecimenVolume().getValue()).thenReturn("3000");
        Mockito.when(sac.getSac23_InitialSpecimenVolume()).thenReturn(nm);
        Mockito.when(sac.getSac23_InitialSpecimenVolume().getValue()).thenReturn("2000");
        Mockito.when(sac.getSac24_VolumeUnits()).thenReturn(ce);
        Mockito.when(sac.getSac24_VolumeUnits().getCe1_Identifier()).thenReturn(st);
        Mockito.when(sac.getSac24_VolumeUnits().getCe1_Identifier().getValue()).thenReturn("L");
        
    // Mockito.when(spmObxList.get(0)).thenReturn(obx);
        Mockito.when(spmObxList.get(0).getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(spmObxList.get(0).getObx5_ObservationValue(0).encode()).thenReturn("orderName");
      //  Mockito.when(spmObxList.get(1)).thenReturn(obx);
        Mockito.when(spmObxList.get(1).getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(spmObxList.get(1).getObx5_ObservationValue(0).encode()).thenReturn("Aborted");
      //  Mockito.when(sacObxList.get(2)).thenReturn(obx);
        Mockito.when(sacObxList.get(2).getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(sacObxList.get(2).getObx5_ObservationValue(0).encode()).thenReturn("runResult");
        
        Mockito.when(obx.getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(obx.getObx5_ObservationValue(0).encode()).thenReturn("observationValue");
        Mockito.when(obx.getObx3_ObservationIdentifier()).thenReturn(ce);
        Mockito.when(obx.getObx3_ObservationIdentifier().getCe1_Identifier()).thenReturn(st);
        Mockito.when(obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()).thenReturn("RuntimeRange");
        Mockito.when(obx2.getObx3_ObservationIdentifier()).thenReturn(ce);
        Mockito.when(obx2.getObx3_ObservationIdentifier().getCe1_Identifier()).thenReturn(st);
        Mockito.when(obx2.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()).thenReturn("P");

        Mockito.when(obx3.getObx3_ObservationIdentifier()).thenReturn(ce);
        Mockito.when(obx3.getObx3_ObservationIdentifier().getCe1_Identifier()).thenReturn(st);
        Mockito.when(obx3.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()).thenReturn("PV");
        
        Mockito.when(obx.getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(obx.getObx5_ObservationValue(0).getData()).thenReturn(type);
        Mockito.when(obx.getObx5_ObservationValue(0).getData().encode()).thenReturn("20181231000000");
        
        Mockito.when(obx2.getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(obx2.getObx5_ObservationValue(0).getData()).thenReturn(type);
        Mockito.when(obx2.getObx5_ObservationValue(0).getData().encode()).thenReturn("20181231000000");
        
        Mockito.when(obx3.getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(obx3.getObx5_ObservationValue(0).getData()).thenReturn(type);
        Mockito.when(obx3.getObx5_ObservationValue(0).getData().encode()).thenReturn("20181231000000");
        
        Mockito.when(spm.getSpm2_SpecimenID()).thenReturn(eip);
        Mockito.when(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier()).thenReturn(ei);
        Mockito.when(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier())
            .thenReturn(st);
        Mockito.when(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().getValue())
            .thenReturn("Blood_1");
        Mockito.when(spm.getSpm4_SpecimenType()).thenReturn(cwe);
        Mockito.when(spm.getSpm4_SpecimenType().getCwe2_Text()).thenReturn(st);
        Mockito.when(spm.getSpm4_SpecimenType().getCwe2_Text().encode()).thenReturn("SpecimenType");
        Mockito.when(spm.getSpm11_SpecimenRole(0)).thenReturn(cwe);
        Mockito.when(spm.getSpm11_SpecimenRole(0).getCwe1_Identifier()).thenReturn(st);
        Mockito.when(spm.getSpm11_SpecimenRole(0).getCwe1_Identifier().getValue()).thenReturn("Q");
        Mockito.when(spm.getSpm3_SpecimenParentIDs(0)).thenReturn(eip);
        Mockito.when(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier()).thenReturn(ei);
        Mockito.when(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier())
            .thenReturn(st);
        Mockito.when(
            spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().getValue())
            .thenReturn("1234");
        Mockito.when(spm.getSpm3_SpecimenParentIDs(0).encode()).thenReturn("1234");
        Mockito.when(spm.getSpm14_SpecimenDescription(0)).thenReturn(st);
        Mockito.when(spm.getSpm14_SpecimenDescription(0).getValue()).thenReturn("specimen Description");
    }
    
    @Test
    public void sendNotificationToADMTest() {
        responseHandlerService.sendNotificationToADM("messageGroup","contentArgs");
    }

    
   
    @Test void mP24ConfigDetailsTest() {
        MP24ConfigDetails mp24ConfigDetails = getMP24ConfigDetails();
        ArrayList<SampleTypeDTO> arrayList = new ArrayList<>();
        arrayList.add(getSampleTypeDTO());
        Assert.assertEquals(mp24ConfigDetails.getConfigType().get(0).getName(), "name");
        Assert.assertEquals(mp24ConfigDetails.getConfigType().get(0).getId(), "id");
        Assert.assertEquals(mp24ConfigDetails.getConfigType().get(0).getEncodingSystem(), "encodingSystem");
        Assert.assertEquals(mp24ConfigDetails.getConfigType().get(0).getValue(), "value");
    }
    
    public MP24ConfigDetails getMP24ConfigDetails() {
        MP24ConfigDetails mp24ConfigDetails = new MP24ConfigDetails();
        ArrayList<SampleTypeDTO> arrayList = new ArrayList<>();
        arrayList.add(getSampleTypeDTO());
        mp24ConfigDetails.setConfigType(arrayList);
        return mp24ConfigDetails;
    }
    
    public MP24ParamConfig getMP24ParamConfig() {
        MP24ParamConfig config = new MP24ParamConfig();
        config.setMp24ConfigDetails(getMP24ConfigDetails());
        return config;
    }
    
    public SampleTypeDTO getSampleTypeDTO() {
        SampleTypeDTO sampleTypeDTO = new SampleTypeDTO();
        sampleTypeDTO.setEncodingSystem("encodingSystem");
        sampleTypeDTO.setId("id");
        sampleTypeDTO.setName("name");
        sampleTypeDTO.setValue("value");
        sampleTypeDTO.toString();
        return sampleTypeDTO;
    }
    
    public ResponseMessage getResponseMessage() {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setAccessioningId("1234");
        responseMessage.setContainerId("96well_1");
        responseMessage.setDateTimeMessageGenerated(null);
        responseMessage.setDeviceSerialNumber("LP24_1");
        responseMessage.setMessageControlId("234567890");
        List<String> errors = new ArrayList<>();
        errors.add("Error1");
        responseMessage.setErrors(errors);
        responseMessage.setMessageType(EnumMessageType.ResponseMessage);
        responseMessage.setProtocolName("P1");
        responseMessage.setReceivingApplication("Connect");
        responseMessage.setSampleType("Plasma");
        responseMessage.setSendingApplicationName("LP24");
        responseMessage.setSpecimenDescription("");
        responseMessage.setStatus("SUCCESS");
        return responseMessage;
    }
    
    public ResponseMessage getResponseMessageDuplicate() {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setAccessioningId("1234");
        responseMessage.setContainerId("96well_1");
        responseMessage.setDateTimeMessageGenerated(null);
        responseMessage.setDeviceSerialNumber("LP24_1");
        responseMessage.setMessageControlId("234567890");
        List<String> errors = new ArrayList<>();
        errors.add("Error1");
        responseMessage.setErrors(errors);
        responseMessage.setMessageType(EnumMessageType.ResponseMessage);
        responseMessage.setProtocolName("P1");
        responseMessage.setReceivingApplication("Connect");
        responseMessage.setSampleType("Plasma");
        responseMessage.setSendingApplicationName("LP24");
        responseMessage.setSpecimenDescription("");
        responseMessage.setStatus("DUPLICATE");
        return responseMessage;
    }
    
    public ResponseMessage getResponseMessageApplicationError() {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setAccessioningId("1234");
        responseMessage.setContainerId("96well_1");
        responseMessage.setDateTimeMessageGenerated(null);
        responseMessage.setDeviceSerialNumber("LP24_1");
        responseMessage.setMessageControlId("234567890");
        List<String> errors = new ArrayList<>();
        errors.add("Error1");
        responseMessage.setErrors(errors);
        responseMessage.setMessageType(EnumMessageType.ResponseMessage);
        responseMessage.setProtocolName("P1");
        responseMessage.setReceivingApplication("Connect");
        responseMessage.setSampleType("Plasma");
        responseMessage.setSendingApplicationName("LP24");
        responseMessage.setSpecimenDescription("");
        responseMessage.setStatus("AE");
        return responseMessage;
    }
    
    public AcknowledgementMessage getAcknowledgement() {
        AcknowledgementMessage ack = new AcknowledgementMessage();
        ack.setContainerId("1218439_1");
        ack.setDateTimeMessageGenerated("");
        ack.setDeviceSerialNumber("LP0012");
        ack.setMessageType(EnumMessageType.AcknowledgementMessage);
        ack.setReceivingApplication("LP24");
        ack.setSendingApplicationName("Connect");
        ack.setStatus("Passes");
        ack.setMessageControlId("23454545");
        return ack;
    }
    
    public AcknowledgementMessage getAcknowledgementApplicationRejectError() {
        AcknowledgementMessage ack = new AcknowledgementMessage();
        ack.setContainerId("1218439_1");
        ack.setDateTimeMessageGenerated("");
        ack.setDeviceSerialNumber("LP0012");
        ack.setMessageType(EnumMessageType.AcknowledgementMessage);
        ack.setReceivingApplication("LP24");
        ack.setSendingApplicationName("Connect");
        ack.setStatus("AR");
        ack.setMessageControlId("23454545");
        ack.setErrorCode("500");
        ack.setErrorDescription("Error Description");
        responseHandlerService.setAckCode("200");
       logger.info("Ack code: "+responseHandlerService.getAckCode());
        return ack;
    }
    
    public HL7HeaderSegmentDTO getHL7HeaderSegmentDTO() {
        HL7HeaderSegmentDTO hl7HeaderSegmentDTO2 = new HL7HeaderSegmentDTO();
        hl7HeaderSegmentDTO2.setCharacterSet("[UNICODE UTF-8]");
        hl7HeaderSegmentDTO2.setDateTimeMessageGenerated("20180914180259");
        hl7HeaderSegmentDTO2.setDeviceSerialNumber("LP001-12");
        hl7HeaderSegmentDTO2.setMessageControlId("0822b2cc-00c3-46fa-a5a5-2137ad7a4670");
        hl7HeaderSegmentDTO2.setMessageQueryName(null);
        hl7HeaderSegmentDTO2.setMessageType("QBP");
        hl7HeaderSegmentDTO2.setProcessingId("P");
        hl7HeaderSegmentDTO2.setQueryDefDesc("Work Order Step");
        hl7HeaderSegmentDTO2.setQueryDefEncodingSystem("IHE_LABTF");
        hl7HeaderSegmentDTO2.setQueryDefId("WOS");
        hl7HeaderSegmentDTO2.setReceivingApplication("Connect");
        hl7HeaderSegmentDTO2.setReceivingFacility("Roche Diagnostics");
        hl7HeaderSegmentDTO2.setSampleID("10001");
        hl7HeaderSegmentDTO2.setSendingApplicationName("MagnaPure24");
        hl7HeaderSegmentDTO2.setSendingFacility("RocheDiagnostics");
        hl7HeaderSegmentDTO2.setVersionId("2.5.1");
        return hl7HeaderSegmentDTO2;
    }
    
    public MessagePayload getMessagePayload() {
        MessagePayload messagePayload = new MessagePayload();
        messagePayload.setDeviceSerialNumber(getHL7HeaderSegmentDTO().getDeviceSerialNumber());
        messagePayload.setSendingApplicationName(getHL7HeaderSegmentDTO().getSendingApplicationName());
        messagePayload.setDateTimeMessageGenerated(getHL7HeaderSegmentDTO().getDateTimeMessageGenerated());
        messagePayload.setMessageType("NA-Extraction");
        messagePayload.setAccessioningId(getHL7HeaderSegmentDTO().getSampleID());
        messagePayload.setReceivingApplication(getHL7HeaderSegmentDTO().getReceivingApplication());
        messagePayload.setMessageControlId(getHL7HeaderSegmentDTO().getMessageControlId());
        StatusUpdate  statusUpdate  = new StatusUpdate();
        statusUpdate.setComment("comment");
        messagePayload.setStatusUpdate(statusUpdate);
        return messagePayload;
    }
    
    String getConvertToJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String writeValueAsString = null;
        
        writeValueAsString = objectMapper.writeValueAsString(getMessagePayload());
        
        return writeValueAsString;
    }
    
    private ResponseDTO<MessagePayload> getResponseDTO() {
        messagePayload = getMessagePayload();
        ResponseDTO<MessagePayload> messageResponseDTO = new ResponseDTO<MessagePayload>();
        messageResponseDTO.setStatus(Status.SUCCESS);
        messageResponseDTO.setErrors(null);
        messageResponseDTO.setResponse(messagePayload);
        messageResponseDTO.toString();
        
        return messageResponseDTO;
    }
    
    @Test void responseDTOTest() {
        messagePayload = getMessagePayload();
        ResponseDTO<MessagePayload> messageResponseDTO = getResponseDTO();
    }
    
    @Test void sampleInfoTest() {
        SampleInfo sampleInfo = getsampleInfo();
        Assert.assertEquals(sampleInfo.getDateTimeSpecimenCollected(), "DateTimeSpecimenCollected");
        Assert.assertEquals(sampleInfo.getDateTimeSpecimenExpiration(), "DateTimeSpecimenExpiration");
        Assert.assertEquals(sampleInfo.getDateTimeSpecimenReceived(), "DateTimeSpecimenReceived");
        Assert.assertEquals(sampleInfo.getNewOutputPosition(), "SampleOutputPosition");
        Assert.assertEquals(sampleInfo.getSampleType(), "SampleType");
        Assert.assertEquals(sampleInfo.getSpecimenCollectionSite(), "SpecimenCollectionSite");
        Assert.assertEquals(sampleInfo.getSpecimenDescription(), "SpecimenDescription");
        Assert.assertEquals(sampleInfo.getSpecimenRole(), "SpecimenRole");
        Assert.assertEquals(sampleInfo.getSpecimenSourceSite(), "SpecimenSourceSite");
        
    }
    
    private SampleInfo getsampleInfo() {
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setDateTimeSpecimenCollected("DateTimeSpecimenCollected");
        sampleInfo.setDateTimeSpecimenExpiration("DateTimeSpecimenExpiration");
        sampleInfo.setDateTimeSpecimenReceived("DateTimeSpecimenReceived");
        sampleInfo.setNewOutputPosition("SampleOutputPosition");
        sampleInfo.setSampleType("SampleType");
        sampleInfo.setSpecimenCollectionSite("SpecimenCollectionSite");
        sampleInfo.setSpecimenDescription("SpecimenDescription");
        sampleInfo.setSpecimenRole("SpecimenRole");
        sampleInfo.setSpecimenSourceSite("SpecimenSourceSite");
        return sampleInfo;
    }
    
    public com.roche.connect.common.mp24.message.SampleInfo getSampleInfoFromMP() {
        com.roche.connect.common.mp24.message.SampleInfo sample =
            new com.roche.connect.common.mp24.message.SampleInfo();
        sample.setContainerType("ContainerType");
        sample.setDateTimeSpecimenCollected("DateTimeSpecimenCollected");
        sample.setDateTimeSpecimenExpiration("DateTimeSpecimenExpiration");
        sample.setDateTimeSpecimenReceived("DateTimeSpecimenReceived");
        sample.setSampleOutputId("SampleOutputId");
        sample.setSampleOutputPosition("SampleOutputPosition");
        sample.setSampleType("SampleType");
        sample.setSpecimenCollectionSite("SpecimenCollectionSite");
        sample.setSpecimenDescription("SpecimenDescription");
        sample.setSpecimenRole("SpecimenRole");
        sample.setSpecimenSourceSite("SpecimenSourceSite");
        return sample;
    }
    
    public ContainerInfo getContainerInfo() {
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setAvailableSpecimenVolume("1234");
        containerInfo.setCarrierBarcode("carrierBarcode");
        containerInfo.setCarrierPosition("carrierPosition");
        containerInfo.setCarrierType("carrierType");
        containerInfo.setInitialSpecimenVolume("initialSpecimenVolume");
        containerInfo.setUnitofVolume("unitofVolume");
        containerInfo.setContainerVolume("containerVolume");
        containerInfo.setSpecimenEventDate("specimenEventDate");
        containerInfo.setSpecimenVolume("specimenVolume");
        containerInfo.setContainerStatus("containerStatus");
        containerInfo.toString();
        return containerInfo;
    }
    
    private StatusUpdate getStatusUpdate() {
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setComment("comment");
        statusUpdate.setContainerInfo(getContainerInfo());
        statusUpdate.setEquipmentState("equipmentState");
        statusUpdate.setEventDate("eventDate");
        statusUpdate.setInternalControls("internalControls");
        statusUpdate.setOperatorName("operatorName");
        statusUpdate.setOrderName("orderName");
        statusUpdate.setOrderResult("orderResult");
        statusUpdate.setProcessingCartridge("processingCartridge");
        statusUpdate.setProtocolName("protocolName");
        statusUpdate.setProtocolVersion("protocolVersion");
        List<String> listReagent25mlTube = new ArrayList<>();
        listReagent25mlTube.add("reagent25mlTube");
        List<String> listReagent2mlTube = new ArrayList<>();
        listReagent2mlTube.add("reagent2mlTube");
        statusUpdate.setRunEndTime(new Timestamp(10l));
        statusUpdate.setRunResult("runResult");
        statusUpdate.setRunStartTime(new Timestamp(05l));
        statusUpdate.setSampleInfo(getsampleInfo());
        statusUpdate.setTipRack("tipRack");
        statusUpdate.setUpdatedBy("updatedBy");
        statusUpdate.toString();
        
        return statusUpdate;
    }    
  
	@Test
	public void sendACKU01ToLP24Test() throws IOException, HL7Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(InstanceUtil.class);
		PowerMockito.mockStatic(CharStreams.class);
		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO1);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
		Mockito.when(resourceLoader.getResource("classpath:hl7/ACK_U01.txt")).thenReturn(resource);
		Mockito.when(resource.getInputStream()).thenReturn(stream);

		Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class)))
				.thenReturn("MSH|^~\\&|||||||ACK^U01^ACK||P|2.5.1||||||UNICODE UTF-8\n" + "MSA||||");
		Mockito.when(hapiContext.getPipeParser()).thenReturn(p);
		Mockito.when(p.parse(Mockito.anyString())).thenReturn(queryMessage1);
		Mockito.when(queryMessage1.get("MSH")).thenReturn(msh1);
		Mockito.when(queryMessage1.get("MSA")).thenReturn(msa);
		Mockito.when(containerDTO.getHeaderSegment()).thenReturn(getHL7HeaderSegmentDTO());
		Mockito.when(msh1.getMsh3_SendingApplication()).thenReturn(hd);
		Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID()).thenReturn(is);
		Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue())
				.thenReturn("sendingApplication");
		Mockito.when(msh1.getMsh4_SendingFacility()).thenReturn(hd);
		Mockito.when(msh1.getMsh4_SendingFacility().getHd1_NamespaceID()).thenReturn(is);
		Mockito.when(msh1.getMsh5_ReceivingApplication()).thenReturn(hd);
		Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID()).thenReturn(is);
		Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue())
				.thenReturn("receiving application");
		Mockito.when(msh1.getMsh5_ReceivingApplication().getHd2_UniversalID()).thenReturn(st);
		Mockito.when(msh1.getMsh5_ReceivingApplication().getHd3_UniversalIDType()).thenReturn(id);
		Mockito.when(msh1.getMsh6_ReceivingFacility()).thenReturn(hd);
		Mockito.when(msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID()).thenReturn(is);
		Mockito.when(msh1.getMsh7_DateTimeOfMessage()).thenReturn(ts);
		Mockito.when(msh1.getMsh7_DateTimeOfMessage().getTime()).thenReturn(dtm);
		Mockito.when(msh1.getMsh10_MessageControlID()).thenReturn(st);
		Mockito.when(msh1.getMsh11_ProcessingID()).thenReturn(pt);
		Mockito.when(msh1.getMsh11_ProcessingID().getProcessingID()).thenReturn(id);
		Mockito.when(msh1.getMsh12_VersionID()).thenReturn(vid);
		Mockito.when(msh1.getMsh12_VersionID().getVersionID()).thenReturn(id);
		Mockito.when(msa.getMsa2_MessageControlID()).thenReturn(st);
		Mockito.when(msa.getMsa1_AcknowledgmentCode()).thenReturn(id);
		Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
		Mockito.when(exchange.getIn()).thenReturn(camelMessage);
		Mockito.when(exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class)).thenReturn(session);
		Mockito.when(exchange.getOut()).thenReturn(camelMessage);
		Mockito.when(exchange.getOut().getBody(String.class)).thenReturn("exchange string");
		Mockito.when(containerDTO.getCallback()).thenReturn(callback);
		Mockito.doNothing().when(callback).done(false);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);

		responseHandlerService.sendACKU01ToLP24(getResponseDTO());
	}

	@Test
	public void convertESUToMsgPayloadTest() throws HL7Exception, IOException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(InstanceUtil.class);
		PowerMockito.mockStatic(CharStreams.class);
		Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO1);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
		Mockito.when(resourceLoader.getResource("classpath:hl7/ACK_U01.txt")).thenReturn(resource);
		Mockito.when(resource.getInputStream()).thenReturn(stream);
		Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class)))
				.thenReturn("MSH|^~\\&|||||||ACK^U01^ACK||P|2.5.1||||||UNICODE UTF-8\n" + "MSA||||");
		Mockito.when(hapiContext.getPipeParser()).thenReturn(p);
		Mockito.when(p.parse(Mockito.anyString())).thenReturn(queryMessage1);
		Mockito.when(queryMessage1.get("MSH")).thenReturn(msh1);
		Mockito.when(queryMessage1.get("MSA")).thenReturn(msa);
		Mockito.when(containerDTO.getHeaderSegment()).thenReturn(getHL7HeaderSegmentDTO());
		Mockito.when(msh1.getMsh3_SendingApplication()).thenReturn(hd);
		Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID()).thenReturn(is);
		Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue())
				.thenReturn("sendingApplication");
		Mockito.when(msh1.getMsh4_SendingFacility()).thenReturn(hd);
		Mockito.when(msh1.getMsh4_SendingFacility().getHd1_NamespaceID()).thenReturn(is);
		Mockito.when(msh1.getMsh5_ReceivingApplication()).thenReturn(hd);
		Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID()).thenReturn(is);
		Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue())
				.thenReturn("receiving application");
		Mockito.when(msh1.getMsh5_ReceivingApplication().getHd2_UniversalID()).thenReturn(st);
		Mockito.when(msh1.getMsh5_ReceivingApplication().getHd3_UniversalIDType()).thenReturn(id);
		Mockito.when(msh1.getMsh6_ReceivingFacility()).thenReturn(hd);
		Mockito.when(msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID()).thenReturn(is);
		Mockito.when(msh1.getMsh7_DateTimeOfMessage()).thenReturn(ts);
		Mockito.when(msh1.getMsh7_DateTimeOfMessage().getTime()).thenReturn(dtm);
		Mockito.when(msh1.getMsh10_MessageControlID()).thenReturn(st);
		Mockito.when(msh1.getMsh11_ProcessingID()).thenReturn(pt);
		Mockito.when(msh1.getMsh11_ProcessingID().getProcessingID()).thenReturn(id);
		Mockito.when(msh1.getMsh12_VersionID()).thenReturn(vid);
		Mockito.when(msh1.getMsh12_VersionID().getVersionID()).thenReturn(id);
		Mockito.when(msa.getMsa2_MessageControlID()).thenReturn(st);
		Mockito.when(msa.getMsa1_AcknowledgmentCode()).thenReturn(id);
		Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
		Mockito.when(exchange.getIn()).thenReturn(camelMessage);
		Mockito.when(exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class)).thenReturn(session);
		Mockito.when(exchange.getOut()).thenReturn(camelMessage);
		Mockito.when(exchange.getOut().getBody(String.class)).thenReturn("exchange string");
		Mockito.when(containerDTO.getCallback()).thenReturn(callback);
		Mockito.doNothing().when(callback).done(false);
		Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString()))
				.thenReturn(containerDTO);

		Mockito.when(statusMessageU01.get("MSH")).thenReturn(msh1);
		Mockito.when(statusMessageU01.get("EQU")).thenReturn(equ);
		Mockito.when(msh1.getMsh3_SendingApplication().getHd2_UniversalID()).thenReturn(st);
		Mockito.when(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue()).thenReturn("MP001");
		Mockito.when(msh1.getMsh7_DateTimeOfMessage().getTs1_Time()).thenReturn(dtm);
		Mockito.when(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue()).thenReturn("20180912000000");
		Mockito.when(msh1.getMsh10_MessageControlID().getValue()).thenReturn("678940346");
		Mockito.when(equ.getEqu3_EquipmentState()).thenReturn(ce);
		Mockito.when(equ.getEqu3_EquipmentState().getCe1_Identifier()).thenReturn(st);
		Mockito.when(equ.getEqu3_EquipmentState().getCe1_Identifier().getValue()).thenReturn("Passed");
		Mockito.when(equ.getEqu2_EventDateTime()).thenReturn(ts);
		Mockito.when(equ.getEqu2_EventDateTime().getTs1_Time()).thenReturn(dtm);
		Mockito.when(equ.getEqu2_EventDateTime().getTs1_Time().getValue()).thenReturn("20180912000000");

		responseHandlerService.convertESUToMsgPayload(statusMessageU01);
	}
}
