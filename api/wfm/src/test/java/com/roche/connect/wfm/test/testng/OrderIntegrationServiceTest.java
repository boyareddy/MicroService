package com.roche.connect.wfm.test.testng;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.util.MultiValueMap;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.service.OrderIntegrationService;

@PrepareForTest({ RestClientUtil.class,HMTPException.class }) @PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*",
"javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*", "org.apache.logging.*",
"org.slf4j.*" }) public class OrderIntegrationServiceTest {
    
    @InjectMocks private OrderIntegrationService orderIntegrationService;
    
   
    
    @Mock MultiValueMap<String, String> params;
    
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    
    @Mock Invocation.Builder orderClient;
    @Mock Response response;
    
    String accessioningId = "8001";
    Long orderId = 10000001L;
    String url = "http://localhost";
    
    
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    public void config() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));        
    }
    
    @Test public void findOrderTest() throws HMTPException, OrderNotFoundException, UnsupportedEncodingException {
        config();
        List<WfmDTO> wfmlist = new ArrayList<WfmDTO>();
        wfmlist.add(getWfmDTO());
        Mockito
            .when(RestClientUtil.getUrlString(WfmConstants.API_URL.OMM_API_URL.toString(), "",
                WfmURLConstants.ORDERS_API_PATH + WfmURLConstants.ACCESSIONING_ID, accessioningId, null))
            .thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(200);
        Mockito.when(orderClient.get(new GenericType<List<WfmDTO>>() {})).thenReturn(wfmlist);
        orderIntegrationService.findOrder(accessioningId);        
    }
    
    
    @Test(expectedExceptions=OrderNotFoundException.class)
    public void findOrderTestSecondOrderNotFoundException() throws HMTPException, OrderNotFoundException, UnsupportedEncodingException{
        config();
        List<WfmDTO> wfmlist = new ArrayList<WfmDTO>();
        wfmlist.add(getWfmDTO());
        Mockito
            .when(RestClientUtil.getUrlString(WfmConstants.API_URL.OMM_API_URL.toString(), "",
                WfmURLConstants.ORDERS_API_PATH + WfmURLConstants.ACCESSIONING_ID, accessioningId, null))
            .thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get().getStatus()).thenReturn(404);
        Mockito.when(orderClient.get(new GenericType<List<WfmDTO>>() {})).thenReturn(wfmlist);
        orderIntegrationService.findOrder(accessioningId);        
    }
    
   
    
    @Test
    public void updateOrdersTest() throws UnsupportedEncodingException, HMTPException{
        config();
        List<OrderDTO> listOrders = new ArrayList<OrderDTO>();
        listOrders.add(getOrderDTO());
        Mockito.when(RestClientUtil.getUrlString(WfmConstants.API_URL.OMM_API_URL.toString(), "",
            WfmURLConstants.ORDERS_API_PATH + WfmURLConstants.ACCESSIONING_ID, accessioningId, null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get()).thenReturn(response);
        Mockito.when(orderClient.get(new GenericType<List<OrderDTO>>() {})).thenReturn(listOrders);
        Mockito.when(RestClientUtil.getUrlString(WfmConstants.API_URL.OMM_API_URL.toString(),
                WfmURLConstants.ORDERS_API_PATH,WfmURLConstants.ORDER_ID + orderId+ WfmURLConstants.ORDER_STATUS_IN_WORKFLOW, "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(orderClient);
        orderIntegrationService.updateOrders(getWfmDTO());        

        
    }
    
    public WfmDTO getWfmDTO() {
        WfmDTO wfm = new WfmDTO();
        wfm.setAccessioningId(accessioningId);
        wfm.setOrderId(orderId);
        wfm.setSampleType("Plasma");
        wfm.setAssayType("NIPTHTP");
        return wfm;
    }
    
    public OrderDTO getOrderDTO(){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAccessioningId(accessioningId);
        orderDTO.setOrderId(orderId);
        orderDTO.setSampleType("Plasma");
        orderDTO.setAssayType("NIPTHTP");
        return orderDTO;
        
    }
    
}
