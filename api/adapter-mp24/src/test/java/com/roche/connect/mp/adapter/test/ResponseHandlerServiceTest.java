package com.roche.connect.mp.adapter.test;



import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.poi.ss.formula.functions.T;
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
import com.roche.camel.dto.ContainerInfo;
import com.roche.camel.dto.HL7HeaderSegmentDTO;
import com.roche.camel.dto.MP24ConfigDetails;
import com.roche.camel.dto.MessageContainerDTO;
import com.roche.camel.dto.MessagePayload;
import com.roche.camel.dto.RSPMessageDTO;
import com.roche.camel.dto.ResponseDTO;
import com.roche.camel.dto.ResponseDTO.Status;
import com.roche.camel.dto.SampleInfo;
import com.roche.camel.dto.SampleTypeDTO;
import com.roche.camel.dto.StatusUpdate;
import com.roche.camel.service.DeviceHandlerService;
import com.roche.camel.service.ResponseHandlerService;
import com.roche.camel.util.InstanceUtil;
import com.roche.camel.util.MP24ParamConfig;
import com.roche.connect.common.mp24.message.RSPMessage;

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
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.NTE;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.segment.SAC;
import ca.uhn.hl7v2.model.v251.segment.SPM;
import ca.uhn.hl7v2.parser.PipeParser;




@PrepareForTest({RestClientUtil.class,InstanceUtil.class,CharStreams.class,MP24ParamConfig.class,HMTPLoggerImpl.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
    "org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
@TestPropertySource(properties = { "{pas.authenticate_url=abcx" })
public class ResponseHandlerServiceTest{
    @Mock   
    private ResourceLoader resourceLoader;
    @InjectMocks
    private ResponseHandlerService responseHandlerService;
    @Mock
    private Resource resource;
    @Mock
    ResourceLoader mockResourceLoader;
    HL7HeaderSegmentDTO hl7HeaderSegmentDTO;
    @Mock
	DeviceHandlerService deviceHandlerService;
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    MessagePayload messagePayload;
    
    @Mock PipeParser p;
    @Mock HapiContext context;
    @Mock ACK queryMessage1;
    @Mock RSP_K11 queryMessage;
    
    @Mock Response response;
    
    Logger logger = LogManager.getLogger(ResponseHandlerServiceTest.class);
    
    @Mock 
    SSU_U03 statusMessage;
    
    @Mock
    ESU_U01 statusMessageU01;
    
    @Mock MSH msh1;
    @Mock EQU equ;
    @Mock SSU_U03_SPECIMEN_CONTAINER sacContainer;
    @Mock  NTE nte;
    @Mock SAC sac;
    @Mock SPM spm;
    @Mock SSU_U03_SPECIMEN ssuSpecimen;
    List<OBX> sacObxLists = new ArrayList<>();
    List<OBX> spmObxLists = new ArrayList<>(); 
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
    @Mock PT pt;
    @Mock MSA msa;
    @Mock VID vid;
    
    @Mock InstanceUtil instanceUtil;
    @Mock Map<String, MessageContainerDTO> messageContainerDTO;
    @Mock ResponseDTO<MessagePayload> responseDTO;
    @Mock ResponseDTO<MessagePayload> theMessage;
    @Mock ResponseDTO<T>.Error error;
    @Mock MessageContainerDTO containerDTO;
    @Mock MP24ParamConfig mp24ParamConfig;
    @Mock InputStream stream;
    @Mock InputStreamReader inputStreamReader;
    @Mock Message message;
    @Mock Exchange exchange;
    @Mock IoSession session;
    @Mock org.apache.camel.Message camelMessage;
    @Mock AsyncCallback callback;
    Map<String, IoSession> deviceMap = new HashMap<>();
    @Mock List<OBX> sacObxList ;
    @Mock  List<OBX> spmObxList;
    @Mock RSPMessage rspMessageDTO;
    
    
    @BeforeTest
    public void setUp() throws Exception {
        hl7HeaderSegmentDTO = getHL7HeaderSegmentDTO();
        messagePayload = getMessagePayload();       
//        responseHandlerService.setAckCode("200");
    }
    

    /**
     * We need a special {@link IObjectFactory}.
     * 
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test
    public void convertSSUToMsgPayloadPosTest() throws Exception {      
        MockitoAnnotations.initMocks(this);
        restClientMock();
        mockHL7ForConvertSSUToMsgPayload();
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        Mockito.when(responseDTO.getResponse()).thenReturn(getMessagePayload());
        Mockito.when(responseDTO.getResponse().getAccessioningId()).thenReturn("8001");
        Mockito.when(queryMessage1.get("MSH")).thenReturn(msh1);
        Mockito.when((MSA) queryMessage1.get("MSA")).thenReturn(msa);
        deviceMap.put("MP1239", session);
        DeviceHandlerService.setDeviceMap(deviceMap);
        System.out.println(DeviceHandlerService.getDeviceMap()+"deviceMap");
        Mockito.when(deviceHandlerService.updateDeviceStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        
            try {
                responseHandlerService.convertSSUToMsgPayload(statusMessage);
            
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }


        logger.info(hl7HeaderSegmentDTO.getCharacterSet() + hl7HeaderSegmentDTO.getDateTimeMessageGenerated()
                + hl7HeaderSegmentDTO.getDeviceSerialNumber() + hl7HeaderSegmentDTO.getMessageControlId()
                + hl7HeaderSegmentDTO.getMessageQueryName() + hl7HeaderSegmentDTO.getMessageType()
                + hl7HeaderSegmentDTO.getProcessingId() + hl7HeaderSegmentDTO.getQueryDefdesc()
                + hl7HeaderSegmentDTO.getQueryDefEncodingSystem() + hl7HeaderSegmentDTO.getQueryDefId()
                + hl7HeaderSegmentDTO.getReceivingApplication() + hl7HeaderSegmentDTO.getReceivingFacility()
                + hl7HeaderSegmentDTO.getSampleID() + hl7HeaderSegmentDTO.getSendingApplicationName()
                + hl7HeaderSegmentDTO.getSendingFacility() + hl7HeaderSegmentDTO.getVersionId());

    }
    
    public void mockHL7ForConvertSSUToMsgPayload() throws HL7Exception {
        Mockito.when(statusMessage.get("MSH")).thenReturn(msh1);
        Mockito.when(statusMessage.get("EQU")).thenReturn(equ);
        Mockito.when(statusMessage.getSPECIMEN_CONTAINER()).thenReturn(sacContainer);
        Mockito.when(sacContainer.get("NTE")).thenReturn(nte);
        Mockito.when(sacContainer.getSAC()).thenReturn(sac);
        Mockito.when(sacContainer.getSPECIMEN()).thenReturn(ssuSpecimen);
        Mockito.when(sacContainer.getSPECIMEN().getSPM()).thenReturn(spm);
        sacObxLists.add(obx);
        sacObxLists.add(obx);
        sacObxLists.add(obx);
        spmObxLists.add(obx);
        spmObxLists.add(obx);
        spmObxLists.add(obx);
        Mockito.when( sacContainer.getOBXAll()).thenReturn(sacObxLists);
        Mockito.when(sacContainer.getSPECIMEN().getOBXAll()).thenReturn(spmObxLists);
        Mockito.when(msh1.getMsh3_SendingApplication()).thenReturn(hd);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd2_UniversalID()).thenReturn(st);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue()).thenReturn("MP0012");
        
        Mockito.when(msh1.getMsh3_SendingApplication()).thenReturn(hd);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID()).thenReturn(is);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue()).thenReturn("MP24");
        
        Mockito.when(sacObxList.get(2)).thenReturn(obx3);
        Mockito.when(sacObxList.get(2).getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(sacObxList.get(2).getObx5_ObservationValue(0).encode()).thenReturn("obx3Value");
        Mockito.when(spmObxList.get(0)).thenReturn(obx);
        Mockito.when(spmObxList.get(0).getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(spmObxList.get(0).getObx5_ObservationValue(0).encode()).thenReturn("spm5Value");
        Mockito.when(spmObxList.get(1)).thenReturn(obx2);
        Mockito.when(spmObxList.get(1).getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(spmObxList.get(1).getObx5_ObservationValue(0).encode()).thenReturn("Aborted");
        Mockito.when(spm.getSpm2_SpecimenID()).thenReturn(eip);
        Mockito.when(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier()).thenReturn(ei);
        Mockito.when(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()).thenReturn(st);
        Mockito.when(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().getValue()).thenReturn("Blood_1");
        
        
        
    }
    public void restClientMock() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        ReflectionTestUtils.setField(responseHandlerService, "loginUrl", "http://abc.com");
        ReflectionTestUtils.setField(responseHandlerService, "loginEntity", "http://loginEntity.com");
        final Invocation.Builder builder = Mockito.mock(Invocation.Builder.class);
        final Invocation.Builder builder2 = Mockito.mock(Invocation.Builder.class);
        Mockito.when(RestClientUtil.getBuilder("http://abc.com", null)).thenReturn(builder2);
        Mockito.when(RestClientUtil.getBuilder("null?source=device&devicetype=MP24&messagetype=NA-Extraction&deviceid=MP001-12", null)).thenReturn(builder);
        javax.ws.rs.client.Entity<MessagePayload> entity1 = javax.ws.rs.client.Entity.entity(messagePayload, MediaType.APPLICATION_JSON);
        javax.ws.rs.client.Entity<String> entity2 = javax.ws.rs.client.Entity.entity("http://loginEntity.com", MediaType.APPLICATION_FORM_URLENCODED);
        Mockito.when(builder.post(entity2,String.class)).thenReturn("token");
        Mockito.when(builder2.post(entity1)).thenReturn(response);
        Mockito.when(builder2.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder2);
    }
    @Test
    public void convertQBPToMsgPayloadTest() {
        restClientMock();
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        responseHandlerService.convertQBPToMsgPayload(getHL7HeaderSegmentDTO());
    }

    public void instanceUtilMock() throws IOException, HL7Exception {
        PowerMockito.mockStatic(InstanceUtil.class);
        Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
     
        Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
        Mockito.when(InstanceUtil.getInstance().getConnectionMap().get(Mockito.anyString())).thenReturn(containerDTO);
        Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString())).thenReturn(containerDTO);
        Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
        Mockito.when(exchange.getIn()).thenReturn(camelMessage);
        Mockito.when(exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class)).thenReturn(session);
        Mockito.when(exchange.getOut()).thenReturn(camelMessage);
        Mockito.when(exchange.getOut()).thenReturn(camelMessage);
        Mockito.when(containerDTO.getCallback()).thenReturn(callback);
        Mockito.doNothing().when(callback).done(false);
        Mockito.when(resourceLoader.getResource(Mockito.anyString())).thenReturn(resource);
        Mockito.when(resource.getInputStream()).thenReturn(stream);
        Mockito.when(context.getPipeParser()).thenReturn(p);
        Mockito.when( queryMessage1.get("MSH")).thenReturn(msh1);
        Mockito.when( queryMessage1.get("MSA")).thenReturn(msa); }
    
    public void mockHL7forSendAckToDevice() throws HL7Exception {
        Mockito.when(msh1.getMsh3_SendingApplication()).thenReturn(hd);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID()).thenReturn(is);
        Mockito.when(msh1.getMsh3_SendingApplication().getHd2_UniversalID()).thenReturn(st);
        Mockito.when(msh1.getMsh4_SendingFacility()).thenReturn(hd);
        Mockito.when(msh1.getMsh4_SendingFacility().getHd1_NamespaceID()).thenReturn(is);
        Mockito.when(msh1.getMsh6_ReceivingFacility()).thenReturn(hd);
        Mockito.when(msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID()).thenReturn(is);
        Mockito.when(msh1.getMsh5_ReceivingApplication()).thenReturn(hd);
        Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID()).thenReturn(is);
        Mockito.when(msh1.getMsh10_MessageControlID()).thenReturn(st);
        Mockito.when(msh1.getMsh7_DateTimeOfMessage()).thenReturn(ts);
        Mockito.when(msh1.getMsh7_DateTimeOfMessage().getTime()).thenReturn(dtm);
        Mockito.when(msh1.getMsh11_ProcessingID()).thenReturn(pt);
        Mockito.when(msh1.getMsh11_ProcessingID().getProcessingID()).thenReturn(id);
        Mockito.when(msh1.getMsh12_VersionID()).thenReturn(vid);
        Mockito.when(msh1.getMsh12_VersionID().getVersionID()).thenReturn(id);
        Mockito.when(msh1.getMessageControlID()).thenReturn(st);
        Mockito.when(msh1.getMessageControlID().getValue()).thenReturn("3456789678");
        Mockito.when(msa.getMsa2_MessageControlID()).thenReturn(st);
        Mockito.when(msa.getMsa1_AcknowledgmentCode()).thenReturn(id);
        Mockito.when(statusMessage.get("MSH")).thenReturn(msh1);
        Mockito.when(statusMessage.getSPECIMEN_CONTAINER()).thenReturn(sacContainer);
        Mockito.when(statusMessage.get("EQU")).thenReturn(equ);
        Mockito.when(sacContainer.get("NTE")).thenReturn(nte);
        Mockito.when(sacContainer.getSAC()).thenReturn(sac);
        Mockito.when(sacContainer.getSPECIMEN()).thenReturn(ssuSpecimen);
        Mockito.when(sacContainer.getSPECIMEN().getSPM()).thenReturn(spm);
        sacObxLists.add(obx);
        sacObxLists.add(obx);
        sacObxLists.add(obx);
        spmObxLists.add(obx);
        spmObxLists.add(obx);
        spmObxLists.add(obx);
        Mockito.when(sacContainer.getOBXAll()).thenReturn(sacObxLists);
        Mockito.when(sacContainer.getSPECIMEN().getOBXAll()).thenReturn(spmObxLists);
        
        Mockito.when(sacObxList.get(2)).thenReturn(obx3);
        Mockito.when(sacObxList.get(2).getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(sacObxList.get(2).getObx5_ObservationValue(0).encode()).thenReturn("obx3Value");
        Mockito.when(spmObxList.get(0)).thenReturn(obx);
        Mockito.when(spmObxList.get(0).getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(spmObxList.get(0).getObx5_ObservationValue(0).encode()).thenReturn("spm5Value");
        Mockito.when(spmObxList.get(1)).thenReturn(obx2);
        Mockito.when(spmObxList.get(1).getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(spmObxList.get(1).getObx5_ObservationValue(0).encode()).thenReturn("Aborted");
        
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
        Mockito.when(sac.getSac10_CarrierIdentifier().getEi1_EntityIdentifier().getValue()).thenReturn("Carrier Identifier");
        Mockito.when(sac.getSac11_PositionInCarrier()).thenReturn(na);
        Mockito.when(sac.getSac11_PositionInCarrier().getValue1()).thenReturn(nm);
        Mockito.when(sac.getSac11_PositionInCarrier().getValue1().getValue()).thenReturn("2");
        Mockito.when(obx.getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(obx.getObx5_ObservationValue(0).encode()).thenReturn("inprogress");
        Mockito.when(obx.getObx3_ObservationIdentifier()).thenReturn(ce);
        Mockito.when(obx.getObx3_ObservationIdentifier().getCe1_Identifier()).thenReturn(st);
        //Mockito.when(obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()).thenReturn("RuntimeRange");
        Mockito.when(obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()).thenReturn("OrderResult");
        Mockito.when(obx2.getObx3_ObservationIdentifier()).thenReturn(ce);
        Mockito.when(obx2.getObx3_ObservationIdentifier().getCe1_Identifier()).thenReturn(st);
        Mockito.when(obx2.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()).thenReturn("OrderResult");
        Mockito.when(obx3.getObx3_ObservationIdentifier()).thenReturn(ce);
        Mockito.when(obx3.getObx3_ObservationIdentifier().getCe1_Identifier()).thenReturn(st);
        Mockito.when(obx3.getObx3_ObservationIdentifier().getCe1_Identifier().getValue()).thenReturn("OrderResult");
        
        Mockito.when(obx.getObx5_ObservationValue(0)).thenReturn(varies);
        Mockito.when(obx.getObx5_ObservationValue(0).getData()).thenReturn(type);
        Mockito.when(obx.getObx5_ObservationValue(0).getData().encode()).thenReturn("20181231000000^20181231000000");
        Mockito.when(spm.getSpm2_SpecimenID()).thenReturn(eip);
        Mockito.when(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier()).thenReturn(ei);
        Mockito.when(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()).thenReturn(st);
        Mockito.when(spm.getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().getValue()).thenReturn("Blood_1");
        Mockito.when(spm.getSpm4_SpecimenType()).thenReturn(cwe);
        Mockito.when(spm.getSpm4_SpecimenType().getCwe2_Text()).thenReturn(st);
        Mockito.when(spm.getSpm4_SpecimenType().getCwe2_Text().encode()).thenReturn("SpecimenType");
        Mockito.when(spm.getSpm11_SpecimenRole(0)).thenReturn(cwe);
        Mockito.when(spm.getSpm11_SpecimenRole(0).getCwe1_Identifier()).thenReturn(st);
        Mockito.when(spm.getSpm11_SpecimenRole(0).getCwe1_Identifier().getValue()).thenReturn("Q");
        Mockito.when(spm.getSpm3_SpecimenParentIDs(0)).thenReturn(eip);
        Mockito.when(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier()).thenReturn(ei);
        Mockito.when(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier()).thenReturn(st);
        Mockito.when(spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier().getEi1_EntityIdentifier().getValue()).thenReturn("1234");
        Mockito.when(spm.getSpm3_SpecimenParentIDs(0).encode()).thenReturn("1234");
        Mockito.when(containerDTO.getHeaderSegment()).thenReturn(getHL7HeaderSegmentDTO());
    }
    
    public void charStreamMockForAck() throws IOException {
        PowerMockito.mockStatic(CharStreams.class);
        Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class))).thenReturn("MSH|^~\\&|||||||ACK^U03^ACK||P|2.5.1||||||UNICODE UTF-8\n" + 
            "MSA||||");
    }
    
    public void charStreamMockForResponse() throws IOException {
        PowerMockito.mockStatic(CharStreams.class);
        Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class))).thenReturn("MSH|^~\\&|||||||RSP^WOS^RSP_K11|||2.5.1||||||UNICODE UTF-8\n" + 
            "MSA||||\n" + 
            "QAK|1|||\n" + 
            "QPD||1||\n" + 
            "SPM||||||||||||||||||\n" + 
            "SAC||||||||||||\n" + 
            "ORC|NW|||||||||\n" + 
            "OBR|1|||||||||||||||||||||||||||||");
    }
    @Test
    public void sendACKToMP24Test() throws IOException, HL7Exception {
        MockitoAnnotations.initMocks(this);
        instanceUtilMock();
        charStreamMockForAck();
        mockHL7forSendAckToDevice();
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        Mockito.when(p.parse(Mockito.anyString())).thenReturn(queryMessage1);
        responseHandlerService.sendACKToMP24(getResponseDTO());
    }
    
    @Test public void sendResponseToMP24Test() throws Exception{
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(MP24ParamConfig.class);
        instanceUtilMock();
        charStreamMockForResponse();
        mockHL7forSendAckToDevice();
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        Mockito.when(p.parse(Mockito.anyString())).thenReturn(queryMessage);
        Mockito.when(theMessage.getResponse()).thenReturn(messagePayload);
        Mockito.when(theMessage.getResponse().getAccessioningId()).thenReturn("6001");
        Mockito.when(rspMessageDTO.getSampleInfo()).thenReturn(getSampleInfoFromMP());
        Mockito.when(rspMessageDTO.getContainerInfo()).thenReturn(getContainerInfoMP24());
        Mockito.when(MP24ParamConfig.getInstance()).thenReturn(mp24ParamConfig);
        Mockito.when(MP24ParamConfig.getInstance().getMp24ConfigDetails()).thenReturn(getMP24ConfigDetails());
        Mockito.when(deviceHandlerService.updateDeviceStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        try {
            responseHandlerService.sendResponseToMP24(getResponseDTO());
            responseHandlerService.sendResponseToMP24(getResponseDTOduplicateStatus());
            responseHandlerService.sendResponseToMP24(getResponseDTONoOrderStatus());
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Test
    public void sendNotificationInvalidMessageTypeTest() {
        responseHandlerService.sendNotificationInvalidMessageType();
    }
    
    
    @Test
    public void hL7HeaderSegmentDTOTest(){
        HL7HeaderSegmentDTO hl7HeaderSegmentDTO2 = getHL7HeaderSegmentDTO();
        Assert.assertEquals(hl7HeaderSegmentDTO2.getCharacterSet(),"[UNICODE UTF-8]");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getDateTimeMessageGenerated(),"20180914180259");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getDeviceSerialNumber(),"MP001-12");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getMessageControlId(),"0822b2cc-00c3-46fa-a5a5-2137ad7a4670");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getMessageQueryName(),null);
        Assert.assertEquals(hl7HeaderSegmentDTO2.getMessageType(),"QBP");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getProcessingId(),"P");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getQueryDefdesc(),"Work Order Step");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getQueryDefEncodingSystem(),"IHE_LABTF");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getQueryDefId(),"WOS");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getReceivingApplication(),"Connect");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getReceivingFacility(),"Roche Diagnostics");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getSampleID(),"10001");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getSendingApplicationName(),"MagnaPure24");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getSendingFacility(),"RocheDiagnostics");
        Assert.assertEquals(hl7HeaderSegmentDTO2.getVersionId(),"2.5.1");
        
    }
    public HL7HeaderSegmentDTO getHL7HeaderSegmentDTO() {
        HL7HeaderSegmentDTO hl7HeaderSegmentDTO2 = new HL7HeaderSegmentDTO();
        hl7HeaderSegmentDTO2.setCharacterSet("[UNICODE UTF-8]");
        hl7HeaderSegmentDTO2.setDateTimeMessageGenerated("20180914180259");
        hl7HeaderSegmentDTO2.setDeviceSerialNumber("MP001-12");
        hl7HeaderSegmentDTO2.setMessageControlId("0822b2cc-00c3-46fa-a5a5-2137ad7a4670");
        hl7HeaderSegmentDTO2.setMessageQueryName(null);
        hl7HeaderSegmentDTO2.setMessageType("QBP");
        hl7HeaderSegmentDTO2.setProcessingId("P");
        hl7HeaderSegmentDTO2.setQueryDefdesc("Work Order Step");
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
        messagePayload.setRspMessage(getRSPMessageFromMP24());
        messagePayload.setMessageControlId(getHL7HeaderSegmentDTO().getMessageControlId());
        messagePayload.setStatusUpdate(null);
        return messagePayload;
    }


    private ResponseDTO<MessagePayload> getResponseDTO() {
        messagePayload = getMessagePayload();
        ResponseDTO<MessagePayload> messageResponseDTO = new ResponseDTO<MessagePayload>();
        messageResponseDTO.setStatus(Status.SUCCESS);
        /*List<Error> errorList = new ArrayList<>();
        Error error1 = new Error("404");
        Error error2 = new Error("500");
        errorList.add(error1);
        errorList.add(error2);*/
        messageResponseDTO.setErrors(null);
        messageResponseDTO.setResponse(messagePayload);
        //messageResponseDTO.isSuccessful();
        messageResponseDTO.toString();
        
        return messageResponseDTO;
    }
    
    private ResponseDTO<MessagePayload> getResponseDTOduplicateStatus() {
        messagePayload = getMessagePayload();
        ResponseDTO<MessagePayload> messageResponseDTO = new ResponseDTO<MessagePayload>();
        messageResponseDTO.setStatus(Status.DUPLICATE);
        messageResponseDTO.setErrors(null);
        messageResponseDTO.setResponse(messagePayload);
        messageResponseDTO.toString();
        return messageResponseDTO;
    }
    
    private ResponseDTO<MessagePayload> getResponseDTONoOrderStatus() {
        messagePayload = getMessagePayload();
        ResponseDTO<MessagePayload> messageResponseDTO = new ResponseDTO<MessagePayload>();
        messageResponseDTO.setStatus(Status.DUPLICATE);
        messageResponseDTO.setErrors(null);
        messageResponseDTO.setResponse(messagePayload);
        messageResponseDTO.toString();
        return messageResponseDTO;
    }

    @Test
    void responseDTOTest() {
        messagePayload = getMessagePayload();
        ResponseDTO<MessagePayload> messageResponseDTO = getResponseDTO();
        Assert.assertEquals(messageResponseDTO.getStatus(),Status.SUCCESS);
        Assert.assertEquals(messageResponseDTO.getErrors(),null);
        Assert.assertEquals(messageResponseDTO.isSuccessful(),true);
        Assert.assertEquals(messageResponseDTO.getResponse().toString(),messagePayload.toString());
        

    }

    @Test
    void sampleInfoTest() {
        SampleInfo sampleInfo = getsampleInfo();
        Assert.assertEquals(sampleInfo.getContainerType(), "ContainerType");
        Assert.assertEquals(sampleInfo.getDateTimeSpecimenCollected(), "DateTimeSpecimenCollected");
        Assert.assertEquals(sampleInfo.getDateTimeSpecimenExpiration(), "DateTimeSpecimenExpiration");
        Assert.assertEquals(sampleInfo.getDateTimeSpecimenReceived(), "DateTimeSpecimenReceived");
        Assert.assertEquals(sampleInfo.getSampleOutputId(), "SampleOutputId");
        Assert.assertEquals(sampleInfo.getSampleOutputPosition(), "SampleOutputPosition");
        Assert.assertEquals(sampleInfo.getSampleType(), "SampleType");
        Assert.assertEquals(sampleInfo.getSpecimenCollectionSite(), "SpecimenCollectionSite");
        Assert.assertEquals(sampleInfo.getSpecimenDescription(), "SpecimenDescription");
        Assert.assertEquals(sampleInfo.getSpecimenRole(), "SpecimenRole");
        Assert.assertEquals(sampleInfo.getSpecimenSourceSite(), "SpecimenSourceSite");

    }

    private SampleInfo getsampleInfo() {
        SampleInfo sampleInfo = new SampleInfo();
        sampleInfo.setContainerType("ContainerType");
        sampleInfo.setDateTimeSpecimenCollected("DateTimeSpecimenCollected");
        sampleInfo.setDateTimeSpecimenExpiration("DateTimeSpecimenExpiration");
        sampleInfo.setDateTimeSpecimenReceived("DateTimeSpecimenReceived");
        sampleInfo.setSampleOutputId("SampleOutputId");
        sampleInfo.setSampleOutputPosition("SampleOutputPosition");
        sampleInfo.setSampleType("SampleType");
        sampleInfo.setSpecimenCollectionSite("SpecimenCollectionSite");
        sampleInfo.setSpecimenDescription("SpecimenDescription");
        sampleInfo.setSpecimenRole("SpecimenRole");
        sampleInfo.setSpecimenSourceSite("SpecimenSourceSite");
        return sampleInfo;
    }
    
    public com.roche.connect.common.mp24.message.SampleInfo getSampleInfoFromMP(){
        com.roche.connect.common.mp24.message.SampleInfo sample = new com.roche.connect.common.mp24.message.SampleInfo();
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

    @Test
    public void containerInfoTest() {

        ContainerInfo containerInfo = getContainerInfo();

        Assert.assertEquals(containerInfo.getAvailableSpecimenVolume(), "1234");
        Assert.assertEquals(containerInfo.getCarrierBarcode(), "carrierBarcode");
        Assert.assertEquals(containerInfo.getCarrierPosition(), "carrierPosition");
        Assert.assertEquals(containerInfo.getCarrierType(), "carrierType");
        Assert.assertEquals(containerInfo.getContainerVolume(), "containerVolume");
        Assert.assertEquals(containerInfo.getUnitofVolume(), "unitofVolume");
        Assert.assertEquals(containerInfo.getInitialSpecimenVolume(), "initialSpecimenVolume");
        Assert.assertEquals(containerInfo.getSpecimenEventDate(), "specimenEventDate");
        Assert.assertEquals(containerInfo.getSpecimenVolume(), "specimenVolume");
        Assert.assertEquals(containerInfo.getContainerStatus(), "containerStatus");
        Assert.assertEquals(containerInfo.getContainerPosition(), "containerPosition");
    }

    private ContainerInfo getContainerInfo() {
        ContainerInfo containerInfo = new ContainerInfo();
        containerInfo.setAvailableSpecimenVolume("1234");
        containerInfo.setCarrierBarcode("carrierBarcode");
        containerInfo.setCarrierPosition("carrierPosition");
        containerInfo.setContainerPosition("containerPosition");
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

    @Test
    void rSPMessageDTOTest() {
        RSPMessageDTO rspMessageDTO = getRSPMessageDTO();
        Assert.assertEquals(rspMessageDTO.getContainerInfo().toString(), getContainerInfo().toString());
        Assert.assertEquals(rspMessageDTO.getEluateVolume(), "eluateVolume");
        Assert.assertEquals(rspMessageDTO.getOrderControl(), "orderControl");
        Assert.assertEquals(rspMessageDTO.getOrderEventDate(), "orderEventDate");
        Assert.assertEquals(rspMessageDTO.getOrderNumber(), "orderNumber");
        Assert.assertEquals(rspMessageDTO.getOrderStatus(), "orderStatus");
        Assert.assertEquals(rspMessageDTO.getProtocolDescription(), "protocolDescription");
        Assert.assertEquals(rspMessageDTO.getProtocolName(), "protocolName");
        Assert.assertEquals(rspMessageDTO.getQueryResponseStatus(), "queryResponseStatus");
        Assert.assertEquals(rspMessageDTO.getResultStatus(), "resultStatus");
        Assert.assertEquals(rspMessageDTO.getSampleInfo().toString(), getsampleInfo().toString());

    }

    private RSPMessageDTO getRSPMessageDTO() {

        RSPMessageDTO rspMessageDTO = new RSPMessageDTO();
        rspMessageDTO.setContainerInfo(getContainerInfo());
        rspMessageDTO.setEluateVolume("eluateVolume");
        rspMessageDTO.setOrderControl("orderControl");
        rspMessageDTO.setOrderEventDate("orderEventDate");
        rspMessageDTO.setOrderNumber("orderNumber");
        rspMessageDTO.setOrderStatus("orderStatus");
        rspMessageDTO.setProtocolDescription("protocolDescription");
        rspMessageDTO.setProtocolName("protocolName");
        rspMessageDTO.setQueryResponseStatus("queryResponseStatus");
        rspMessageDTO.setResultStatus("resultStatus");
        rspMessageDTO.setSampleInfo(getsampleInfo());
        rspMessageDTO.toString();
        return rspMessageDTO;

    }

    @Test
    void statusUpdateTest() {
        StatusUpdate statusUpdate = getStatusUpdate();
        Assert.assertEquals(statusUpdate.getComment(), "comment");
        Assert.assertEquals(statusUpdate.getContainerInfo().toString(), getContainerInfo().toString());
        Assert.assertEquals(statusUpdate.getEquipmentState(), "equipmentState");
        Assert.assertEquals(statusUpdate.getEventDate(), "eventDate");
        Assert.assertEquals(statusUpdate.getInternalControls(), "internalControls");
        Assert.assertEquals(statusUpdate.getOperatorName(), "operatorName");
        Assert.assertEquals(statusUpdate.getOrderName(), "orderName");
        Assert.assertEquals(statusUpdate.getOrderResult(), "orderResult");
        Assert.assertEquals(statusUpdate.getProcessingCartridge(), "processingCartridge");
        Assert.assertEquals(statusUpdate.getProtocolName(), "protocolName");
        Assert.assertEquals(statusUpdate.getProtocolVersion(), "protocolVersion");
        List<String> listReagent25mlTube = new ArrayList<>();
        listReagent25mlTube.add("reagent25mlTube");
        Assert.assertEquals(statusUpdate.getReagent25mlTube(), listReagent25mlTube);
        List<String> listReagent2mlTube = new ArrayList<>();
        listReagent2mlTube.add("reagent2mlTube");
        Assert.assertEquals(statusUpdate.getReagent2mlTube(), listReagent2mlTube);
        Assert.assertEquals(statusUpdate.getReagentCassette(), "reagentCassette");
        Assert.assertEquals(statusUpdate.getRunEndTime(), new Timestamp(10l));
        Assert.assertEquals(statusUpdate.getRunResult(), "runResult");
        Assert.assertEquals(statusUpdate.getRunStartTime(), new Timestamp(05l));
        Assert.assertEquals(statusUpdate.getSampleInfo().toString(), getsampleInfo().toString());
        Assert.assertEquals(statusUpdate.getTipRack(), "tipRack");
        Assert.assertEquals(statusUpdate.getUpdatedBy(), "updatedBy");

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
        statusUpdate.setReagent25mlTube(listReagent25mlTube);
        List<String> listReagent2mlTube = new ArrayList<>();
        listReagent2mlTube.add("reagent2mlTube");
        statusUpdate.setReagent2mlTube(listReagent2mlTube);
        statusUpdate.setReagentCassette("reagentCassette");
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
    void mP24ConfigDetailsTest() {
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
    

    @Test
    void sampleTypeDTOTest() {
        SampleTypeDTO sampleTypeDTO = getSampleTypeDTO();
        Assert.assertEquals(sampleTypeDTO.getEncodingSystem(), "encodingSystem");
        Assert.assertEquals(sampleTypeDTO.getId(), "id");
        Assert.assertEquals(sampleTypeDTO.getName(), "name");
        Assert.assertEquals(sampleTypeDTO.getValue(), "value");

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
    
    public com.roche.connect.common.mp24.message.ContainerInfo getContainerInfoMP24(){
        com.roche.connect.common.mp24.message.ContainerInfo con = new com.roche.connect.common.mp24.message.ContainerInfo();
        con.setAvailableSpecimenVolume("1234");
        con.setCarrierBarcode("carrierBarcode");
        con.setCarrierPosition("carrierPosition");
        con.setContainerPosition("containerPosition");
        con.setCarrierType("carrierType");
        con.setInitialSpecimenVolume("initialSpecimenVolume");
        con.setUnitofVolume("unitofVolume");
        con.setContainerVolume("containerVolume");
        con.setSpecimenEventDate("specimenEventDate");
        con.setSpecimenVolume("specimenVolume");
        con.setContainerStatus("containerStatus");
        return con;
    }
    
    public com.roche.connect.common.mp24.message.RSPMessage getRSPMessageFromMP24(){
        com.roche.connect.common.mp24.message.RSPMessage rsp = new com.roche.connect.common.mp24.message.RSPMessage();
        rsp.setContainerInfo(getContainerInfoMP24());
        rsp.setEluateVolume("eluateVolume");
        rsp.setOrderControl("orderControl");
        rsp.setOrderEventDate("orderEventDate");
        rsp.setOrderNumber("orderNumber");
        rsp.setOrderStatus("orderStatus");
        rsp.setProtocolDescription("protocolDescription");
        rsp.setProtocolName("protocolName");
        rsp.setQueryResponseStatus("queryResponseStatus");
        rsp.setResultStatus("resultStatus");
        rsp.setSampleInfo(getSampleInfoFromMP());
        return rsp;
    }
   
    
   @Test
   public void sendACKU01ToMP24Test() throws IOException, HL7Exception {
	   MockitoAnnotations.initMocks(this);
	   PowerMockito.mockStatic(InstanceUtil.class);
	   PowerMockito.mockStatic(CharStreams.class);
	   Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
	   Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
	   Mockito.when(InstanceUtil.getInstance().getConnectionMap()
				.get(Mockito.anyString())).thenReturn(containerDTO);
	   Mockito.when(resourceLoader.getResource("classpath:hl7/ACK_U01.txt")).thenReturn(resource);
	   Mockito.when(resource.getInputStream()).thenReturn(stream);
	   
	   Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class))).thenReturn("MSH|^~\\&|||||||ACK^U01^ACK||P|2.5.1||||||UNICODE UTF-8\n" + 
	           "MSA||||");
	   Mockito.when(context.getPipeParser()).thenReturn(p);
	   Mockito.when( p.parse(Mockito.anyString())).thenReturn(queryMessage1);
	   Mockito.when(queryMessage1.get("MSH")).thenReturn(msh1);
	   Mockito.when(queryMessage1.get("MSA")).thenReturn(msa);
	   Mockito.when(containerDTO.getHeaderSegment()).thenReturn(getHL7HeaderSegmentDTO());
	   Mockito.when(msh1.getMsh3_SendingApplication()).thenReturn(hd);
	   Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID()).thenReturn(is);
	   Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue()).thenReturn("sendingApplication");
	   Mockito.when(msh1.getMsh4_SendingFacility()).thenReturn(hd);
	   Mockito.when(msh1.getMsh4_SendingFacility().getHd1_NamespaceID()).thenReturn(is);
	   Mockito.when(msh1.getMsh5_ReceivingApplication()).thenReturn(hd);
	   Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID()).thenReturn(is);
	   Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue()).thenReturn("receiving application");
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
	   Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString())).thenReturn(containerDTO);
	   
	   responseHandlerService.sendACKU01ToMP24(getResponseDTO());
   }
   
   @Test
   public void convertESUToMsgPayloadTest() throws HL7Exception, IOException {
	   MockitoAnnotations.initMocks(this);  
	   PowerMockito.mockStatic(InstanceUtil.class);
	   PowerMockito.mockStatic(CharStreams.class);
	   Mockito.when(InstanceUtil.getInstance()).thenReturn(instanceUtil);
	   Mockito.when(InstanceUtil.getInstance().getConnectionMap()).thenReturn(messageContainerDTO);
	   Mockito.when(InstanceUtil.getInstance().getConnectionMap()
				.get(Mockito.anyString())).thenReturn(containerDTO);
	   Mockito.when(resourceLoader.getResource("classpath:hl7/ACK_U01.txt")).thenReturn(resource);
	   Mockito.when(resource.getInputStream()).thenReturn(stream);
	   Mockito.when(CharStreams.toString(Mockito.any(InputStreamReader.class))).thenReturn("MSH|^~\\&|||||||ACK^U01^ACK||P|2.5.1||||||UNICODE UTF-8\n" + 
	           "MSA||||");
	   Mockito.when(context.getPipeParser()).thenReturn(p);
	   Mockito.when( p.parse(Mockito.anyString())).thenReturn(queryMessage1);
	   Mockito.when(queryMessage1.get("MSH")).thenReturn(msh1);
	   Mockito.when(queryMessage1.get("MSA")).thenReturn(msa);
	   Mockito.when(containerDTO.getHeaderSegment()).thenReturn(getHL7HeaderSegmentDTO());
	   Mockito.when(msh1.getMsh3_SendingApplication()).thenReturn(hd);
	   Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID()).thenReturn(is);
	   Mockito.when(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue()).thenReturn("sendingApplication");
	   Mockito.when(msh1.getMsh4_SendingFacility()).thenReturn(hd);
	   Mockito.when(msh1.getMsh4_SendingFacility().getHd1_NamespaceID()).thenReturn(is);
	   Mockito.when(msh1.getMsh5_ReceivingApplication()).thenReturn(hd);
	   Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID()).thenReturn(is);
	   Mockito.when(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue()).thenReturn("receiving application");
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
	   Mockito.when(InstanceUtil.getInstance().getConnectionMap().remove(Mockito.anyString())).thenReturn(containerDTO);
	   
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
   @Test
   public void getNoOrderResponseTest() throws HL7Exception, IOException  {
       MockitoAnnotations.initMocks(this);
       charStreamMockForResponse();
       instanceUtilMock();
       mockHL7forSendAckToDevice();
       responseHandlerService.getNoOrderResponse(getHL7HeaderSegmentDTO());
   }
   
   @Test
   public void sendNotificationToADMTest() {
       responseHandlerService.sendNotificationToADM("messageGroup","contentArgs");
   }
   
   @Test
   public void sendNotificationInvalidHL7VersionTest() {
       responseHandlerService.sendNotificationInvalidHL7Version();
   }
   
   @Test
   public void ssuToIMMRestClientTest() {
       responseHandlerService.ssuToIMMRestClient(getMessagePayload(), "1234");
   }
   
   @Test
   public void runStartAndEndTimeForSSUTest() throws HL7Exception {
       responseHandlerService.runStartAndEndTimeForSSU("inprogress", getStatusUpdateCommon(), obx);
   }
   
   @Test
   public void runStartAndEndTimeForSSUNegativeTest() throws HL7Exception {
       responseHandlerService.runStartAndEndTimeForSSU("started", getStatusUpdateCommon(), obx);
   }
   
   public com.roche.connect.common.mp24.message.StatusUpdate getStatusUpdateCommon(){
       com.roche.connect.common.mp24.message.StatusUpdate st = new com.roche.connect.common.mp24.message.StatusUpdate();
    return st;
   }
   
  
}