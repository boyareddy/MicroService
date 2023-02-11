package com.roche.connect.wfm.test.testng;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.common.server.util.CommonRestURLConstants;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.service.ImmIntegrationService;

@PrepareForTest({ RestClientUtil.class }) @PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*",
"javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*", "org.apache.logging.*",
"org.slf4j.*" }) public class ImmIntegrationServiceTest {
    
    @InjectMocks private ImmIntegrationService immIntegrationService;
    
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    
    @Mock Invocation.Builder orderClient;
    @Mock Response response;
    String url = "http://localhost";
    String accessioningId = "8001";
    String deviceSerialNumber = "LP001-477-Post";
    
    /*@BeforeTest public void setUp() throws HMTPException {
        
    }*/
    
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    public void config() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));        
    }
    
    @Test
    public void setResponseMessageTest() throws HMTPException, JsonProcessingException{
        config();
        ResponseMessage responseMessage =  getResponseMessage() ;
        Mockito.when(RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_LP_RSP_API_PATH + EnumMessageType.ResponseMessage+"&deviceid="+deviceSerialNumber, "", null)).thenReturn(url);
        Entity<ResponseMessage> entity2 = javax.ws.rs.client.Entity.entity(responseMessage, MediaType.APPLICATION_JSON);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null)).thenReturn(orderClient);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null).post(entity2)).thenReturn(response);
        immIntegrationService.setResponseMessage(responseMessage);
    }
    
    @Test
    public void setAcknowledgementMessageTest() throws HMTPException{
        config();
        //AcknowledgementMessage acknowledgementMessage = getAcknowledgementMessage();
        Mockito.when(RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_LP_ACK_API_PATH + EnumMessageType.AcknowledgementMessage+"&deviceid="+deviceSerialNumber, "", null)).thenReturn(url);
        Entity<AcknowledgementMessage> entity2 = javax.ws.rs.client.Entity.entity(getAcknowledgementMessage(), MediaType.APPLICATION_JSON);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null)).thenReturn(orderClient);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null).post(entity2)).thenReturn(response);
        immIntegrationService.setAcknowledgementMessage(getAcknowledgementMessage());
    }
    
    @Test
    public void setAdaptorResponseMessageTest() throws HMTPException{
        config();
        Mockito.when(RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_MP_ACK_API_PATH + EnumMessageType.ResponseMessage+"&deviceid="+deviceSerialNumber, "", null)).thenReturn(url);
        Entity<AdaptorResponseMessage> entity2 = javax.ws.rs.client.Entity.entity(getAdaptorResponseMessage(), MediaType.APPLICATION_JSON);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null)).thenReturn(orderClient);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null).post(entity2)).thenReturn(response);
        immIntegrationService.setAdaptorResponseMessage(getAdaptorResponseMessage());
    }
    
    @Test
    public void setResponseLP24QBPMessageTest() throws HMTPException{
        config();
        Mockito.when(RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_DPCR_LP24_API_URL + WfmURLConstants.QUESTION_MARK + WfmURLConstants.MESSAGE_TYPE_RESPONSE+WfmURLConstants.DEVICE_TYPE_LP24+WfmURLConstants.SOURCE+WfmURLConstants.DEVICE_ID_AMPERSAND+deviceSerialNumber, "", null)).thenReturn(url);
        Entity<ResponseMessage> entity2 = javax.ws.rs.client.Entity.entity(getResponseMessage(), MediaType.APPLICATION_JSON);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null)).thenReturn(orderClient);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null).post(entity2)).thenReturn(response);
        immIntegrationService.setResponseLP24QBPMessage(deviceSerialNumber, getResponseMessage());
    }
    
    @Test
    public void setupdatewfmprocessHTPTest() throws HMTPException{
       config();
       Mockito.when(RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_HTP_STATUS_API_PATH, "", null)).thenReturn(url);
       Entity<HtpStatusMessage> entity2 = javax.ws.rs.client.Entity.entity(gethtpStatusMessage(), MediaType.APPLICATION_JSON);
       Mockito.when(RestClientUtil.postOrPutBuilder(url, null)).thenReturn(orderClient);
       Mockito.when(RestClientUtil.postOrPutBuilder(url, null).post(entity2)).thenReturn(response);
       immIntegrationService.setupdatewfmprocessHTP(gethtpStatusMessage());
    }
    
    @Test
    public void setupdatewfmprocessForteTest() throws HMTPException{
        config();
        Mockito.when(RestClientUtil.getUrlString(WfmConstants.API_URL.IMM_API_URL.toString(), CommonRestURLConstants.AUTHENTICATED_JSON.toString(),
            WfmURLConstants.IMM_FORTE_STATUS_API_PATH, "", null)).thenReturn(url);
        Entity<ForteStatusMessage> entity2 = javax.ws.rs.client.Entity.entity(getforteStatusMessage(), MediaType.APPLICATION_JSON);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null)).thenReturn(orderClient);
        Mockito.when(RestClientUtil.postOrPutBuilder(url, null).post(entity2)).thenReturn(response);
        immIntegrationService.setupdatewfmprocessForte(getforteStatusMessage());
    }
    public ResponseMessage getResponseMessage(){
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageType(EnumMessageType.ResponseMessage);
        responseMessage.setDeviceSerialNumber(deviceSerialNumber);
        responseMessage.setAccessioningId(accessioningId);
        responseMessage.setMessageControlId("messageControlId");
        return responseMessage;
    }
    
    public AcknowledgementMessage getAcknowledgementMessage(){
        AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
        acknowledgementMessage.setStatus(WfmConstants.WORKFLOW_RESPONSE.SUCCESS.toString());
        acknowledgementMessage.setDeviceSerialNumber(deviceSerialNumber);
        acknowledgementMessage.setMessageType(EnumMessageType.AcknowledgementMessage);
        return acknowledgementMessage;
    }
    
    public AdaptorResponseMessage getAdaptorResponseMessage(){
        AdaptorResponseMessage adaptorResponseMessage = new AdaptorResponseMessage();
        com.roche.connect.common.mp24.message.Response response = new com.roche.connect.common.mp24.message.Response();
        response.setMessageType("ResponseMessage");
        response.setDeviceSerialNumber(deviceSerialNumber);
        adaptorResponseMessage.setResponse(response);
        return adaptorResponseMessage;
    }
    public HtpStatusMessage gethtpStatusMessage(){
        HtpStatusMessage htpStatusMessage = new HtpStatusMessage();       
        return htpStatusMessage;
    }
    public ForteStatusMessage getforteStatusMessage(){
        ForteStatusMessage forteStatusMessage = new ForteStatusMessage();
        return forteStatusMessage;
    }
}
