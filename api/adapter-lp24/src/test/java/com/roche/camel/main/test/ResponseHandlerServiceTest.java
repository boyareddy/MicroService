package com.roche.camel.main.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.camel.AsyncCallback;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.StatusUpdate;
import com.roche.lpcamel.dto.HL7HeaderSegmentDTO;
import com.roche.lpcamel.dto.MessageContainerDTO;
import com.roche.lpcamel.dto.MessagePayload;
import com.roche.lpcamel.dto.RSPMessageDTO;
import com.roche.lpcamel.service.ResponseHandlerService;

public class ResponseHandlerServiceTest {
    
    @InjectMocks ResponseHandlerService responseHandlerServiceMock = new ResponseHandlerService();
    
    @Mock Response response;
    
    HL7HeaderSegmentDTO headerSegmentDTO;
    ResponseMessage responseMessage;
    AcknowledgementMessage ackMessage;
    MessageContainerDTO messageContainerDTO;
    MessagePayload messagePayload;
    
    Logger logger = LogManager.getLogger(ResponseHandlerServiceTest.class);
    
    @Mock private CamelContext context;
    @Mock private Exchange exchange;
    @Mock private CamelContext camelContext;
    @Mock private Message in;
    @Mock private Message out;
    @Mock private AsyncCallback callback;
    @Mock private MessageContainerDTO containerDTO;
    
    @BeforeTest public void setUp() {
        responseMessage = createResponseMessage();
        headerSegmentDTO = getHeaderSegmentDTO();
        ackMessage = getAcknowledgement();
        messageContainerDTO = getMessageContainerDTO();
        messagePayload = getMessagePayload();
        responseHandlerServiceMock = Mockito.mock(ResponseHandlerService.class);
        exchange = Mockito.mock(Exchange.class);
        containerDTO = Mockito.mock(MessageContainerDTO.class);
        camelContext = Mockito.mock(CamelContext.class);
        in = new DefaultMessage();
        out = new DefaultMessage();
        callback = Mockito.mock(AsyncCallback.class);
        Mockito.when(containerDTO.getExchange()).thenReturn(exchange);
        Mockito.when(exchange.getOut()).thenReturn(out);
        Mockito.when(containerDTO.getCallback()).thenReturn(callback);
        
    }
    
    @Test public void sendResponseToLPTest() throws IOException {
        Response response = responseHandlerServiceMock.sendResponseToLP(responseMessage);
        // Assert.assertEquals("200", response.getStatus());
    }
    
    @Test public void sendACKToLPTest() {
        try {
            responseHandlerServiceMock.sendACKToLP(ackMessage);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    
   /* @Test
    public void processRSPMessageTest() throws Exception {
        Method method = ResponseHandlerService.class.getDeclaredMethod("processRSPMessage", ResponseMessage.class);
        method.setAccessible(true);
        method.invoke(responseHandlerServiceMock, responseMessage);
        //Assert.assertEquals(2, method.invoke(responseHandlerServiceMock, responseMessage));
    }*/
    
    @Test public void convertQBPToMsgPayloadTest() throws Exception {
        QueryMessage queryMessage = new QueryMessage();
        queryMessage.setContainerId(headerSegmentDTO.getSampleID());
        queryMessage.setDeviceSerialNumber(headerSegmentDTO.getDeviceSerialNumber());
        queryMessage.setSendingApplicationName(headerSegmentDTO.getSendingApplicationName());
        queryMessage.setMessageType(EnumMessageType.QueryMessage);
        queryMessage.setDateTimeMessageGenerated(headerSegmentDTO.getDateTimeMessageGenerated());
        queryMessage.setReceivingApplication(headerSegmentDTO.getReceivingApplication());
        logger.info(headerSegmentDTO.getCharacterSet() + headerSegmentDTO.getDateTimeMessageGenerated()
            + headerSegmentDTO.getDeviceSerialNumber() + headerSegmentDTO.getMessageControlId()
            + headerSegmentDTO.getMessageQueryName() + headerSegmentDTO.getMessageType()
            + headerSegmentDTO.getProcessingId() + headerSegmentDTO.getQueryDefDesc()
            + headerSegmentDTO.getQueryDefEncodingSystem() + headerSegmentDTO.getQueryDefId()
            + headerSegmentDTO.getReceivingApplication() + headerSegmentDTO.getReceivingFacility()
            + headerSegmentDTO.getSampleID() + headerSegmentDTO.getSendingApplicationName()
            + headerSegmentDTO.getSendingFacility() + headerSegmentDTO.getVersionId());
        responseHandlerServiceMock.convertQBPToMsgPayload(headerSegmentDTO);
    }
    
    private ResponseMessage createResponseMessage() {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setAccessioningId("1234");
        responseMessage.setContainerId("96well_1");
        responseMessage.setDateTimeMessageGenerated(null);
        responseMessage.setDeviceSerialNumber("LP24_1");
        List<String> errors = new ArrayList<>();
        errors.add("Error1");
        responseMessage.setErrors(errors);
        responseMessage.setMessageType(EnumMessageType.ResponseMessage);
        responseMessage.setProtocolName("P1");
        responseMessage.setReceivingApplication("Connect");
        responseMessage.setSampleType("Blood");
        responseMessage.setSendingApplicationName("LP24");
        responseMessage.setSpecimenDescription("");
        responseMessage.setStatus("InProgress");
        return responseMessage;
    }
    
    public HL7HeaderSegmentDTO getHeaderSegmentDTO() {
        HL7HeaderSegmentDTO hl7SegmentDTO = new HL7HeaderSegmentDTO();
        hl7SegmentDTO.setDeviceSerialNumber("MP001-12");
        hl7SegmentDTO.setReceivingApplication("Connect");
        hl7SegmentDTO.setSendingApplicationName("MagnaPure24");
        hl7SegmentDTO.setSendingFacility("Roche Diagnostics");
        hl7SegmentDTO.setReceivingFacility("Roche Diagnostics");
        hl7SegmentDTO.setDateTimeMessageGenerated("20180919120000");
        hl7SegmentDTO.setMessageType("QBP");
        hl7SegmentDTO.setCharacterSet("UTF-8");
        hl7SegmentDTO.setMessageQueryName("WOS");
        hl7SegmentDTO.setVersionId("2.5.1");
        hl7SegmentDTO.setProcessingId("");
        hl7SegmentDTO.setSampleID("10001010");
        hl7SegmentDTO.setMessageQueryName("QBP^Q11");
        hl7SegmentDTO.setMessageControlId("5434-5434-5434-4534");
        hl7SegmentDTO.setQueryDefId("");
        hl7SegmentDTO.setQueryDefDesc("");
        hl7SegmentDTO.setQueryDefEncodingSystem("");
        return hl7SegmentDTO;
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
        return ack;
    }
    
    public MessageContainerDTO getMessageContainerDTO() {
        MessageContainerDTO msg = new MessageContainerDTO();
        msg.setCallback(callback);
        msg.setExchange(exchange);
        msg.setHeaderSegment(headerSegmentDTO);
        logger.info(msg.getHeaderSegment() + " " + msg.getCallback() + " " + msg.getExchange());
        return msg;
    }
    
    public MessagePayload getMessagePayload() {
        MessagePayload msgPayLoad = new MessagePayload();
        msgPayLoad.setAccessioningId("101010");
        msgPayLoad.setDateTimeMessageGenerated("20180910023030");
        msgPayLoad.setDeviceSerialNumber("LP0012");
        msgPayLoad.setSendingApplicationName("Connect");
        msgPayLoad.setReceivingApplication("LP24");
        msgPayLoad.setMessageType("RSP");
        msgPayLoad.setMessageControlId("123212342");
        StatusUpdate  statusUpdate  = new StatusUpdate();
        statusUpdate.setComment("comment");
        msgPayLoad.setStatusUpdate(statusUpdate);
        RSPMessageDTO rspMessage = new RSPMessageDTO();
        rspMessage.setOrderControl("ff");
        rspMessage.setOrderEventDate("20180909120000");
        rspMessage.setOrderNumber("101010");
        rspMessage.setOrderStatus("Success");
        rspMessage.setProtocolName("ffPET");
        rspMessage.setProtocolDescription("proto description");
        rspMessage.setResultStatus("Passed");
        rspMessage.setEluateVolume("200ml");
        msgPayLoad.setRspMessage(rspMessage);
        logger.info(msgPayLoad.getAccessioningId()+msgPayLoad.getDateTimeMessageGenerated()+msgPayLoad.getDeviceSerialNumber()
        +msgPayLoad.getMessageType()+msgPayLoad.getSendingApplicationName()+msgPayLoad.getReceivingApplication()+
        msgPayLoad.getMessageControlId()+msgPayLoad.getRspMessage()+msgPayLoad.getStatusUpdate()+rspMessage);
        logger.info(rspMessage.getOrderControl()+rspMessage.getOrderEventDate()+rspMessage.getOrderNumber()+rspMessage.getOrderStatus()+
            rspMessage.getProtocolDescription()+rspMessage.getProtocolName()+rspMessage.getResultStatus()+rspMessage.getEluateVolume());
        return msgPayLoad;
    }
}
