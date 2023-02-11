package com.roche.connect.wfm.test.testng;

import static org.testng.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.wfm.error.ErrorResponse;
import com.roche.connect.wfm.error.OrderFoundException;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.error.QueryValidationException;

@PrepareForTest({RestClientUtil.class,HMTPLoggerImpl.class})
@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class OrderExceptionTest extends AbstractTestNGSpringContextTests{
    
    
    
    @InjectMocks
    OrderFoundException orderFoundException;
    
    
    @InjectMocks
    OrderNotFoundException orderNotFoundException;
    @Mock
    OrderFoundException orderFoundExceptions;
    
    @Mock
    OrderNotFoundException orderNotFoundExceptions;
    
    @InjectMocks
    QueryValidationException queryValidationException;
    @Mock
    QueryValidationException queryValidationExceptions;
    @Mock ErrorResponse errorResponse;
    
    
    ErrorResponse errorResponses;
    @BeforeTest
    public void setUp() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {     
    }
    
    /** Mandatory to mock static classes **/
    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
   @Test(priority=1)
    public void orderFoundException() throws HMTPException, UnsupportedEncodingException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        
        Mockito.when(errorResponse.getErrorStatusCode()).thenReturn(406);
        Mockito.when(errorResponse.getErrorMessage()).thenReturn("Order is already exist in batch");
        Response response =orderFoundException.toResponse(orderFoundExceptions);
        
        assertEquals(response.getStatus(), 406);
    }
   
   @Test(priority=2)
   public void OrderNotFoundException() throws HMTPException, UnsupportedEncodingException {
       MockitoAnnotations.initMocks(this);
       PowerMockito.mockStatic(RestClientUtil.class);
       
       Mockito.when(errorResponse.getErrorStatusCode()).thenReturn(406);
       Mockito.when(errorResponse.getErrorMessage()).thenReturn("Order is already exist in batch");
       Response response =orderNotFoundException.toResponse(orderNotFoundExceptions);
       
       assertEquals(response.getStatus(), 406);
   }
   
   @Test(priority=3)
   public void QueryValidationException() throws HMTPException, UnsupportedEncodingException {
       MockitoAnnotations.initMocks(this);
       PowerMockito.mockStatic(RestClientUtil.class);
       
       Mockito.when(errorResponse.getErrorStatusCode()).thenReturn(406);
       Mockito.when(errorResponse.getErrorMessage()).thenReturn("Order is already exist in batch");
       Response response =queryValidationException.toResponse(queryValidationExceptions);
       
       assertEquals(response.getStatus(), 406);
   }
   
   
   @Test(priority=4)
   public void errorResponses() throws HMTPException, UnsupportedEncodingException {
       MockitoAnnotations.initMocks(this);
       PowerMockito.mockStatic(RestClientUtil.class);
       errorResponses=new ErrorResponse();
       errorResponses.setErrorMessage("Order");
       errorResponses.setErrorStatusCode(206);
       assertEquals(errorResponse.getErrorStatusCode(), 0);
       assertEquals(errorResponse.getErrorMessage(), null);
   }
}
