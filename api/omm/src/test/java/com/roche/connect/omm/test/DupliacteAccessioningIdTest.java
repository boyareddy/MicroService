package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;

@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class DupliacteAccessioningIdTest  {
    @Mock
    OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);
    
    @InjectMocks
    @Spy
    OrderService orderService=Mockito.spy(new OrderServiceImpl());
    @InjectMocks
    OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();
   
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
   
    List<Order> orderaccessioningId=null;
    Order order=null;
    Long domainId=0L;
    int expectedCorrectStatusCode = 0;
    int expectedIncorrectStatusCode = 0;

    @BeforeTest
    public void before() {
        expectedCorrectStatusCode = 200;
        expectedIncorrectStatusCode = 400;
        order=new Order();
        order.setAccessioningId("12345");
        orderaccessioningId=new ArrayList<Order>();
        orderaccessioningId.add(order);
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        
    }
    
    @Test
    public void duplicate() throws HMTPException {
      Mockito.when( orderReadRepository.findAllByAccessioningId(any(String.class),any(long.class))).thenReturn(orderaccessioningId);
      Response actualRes= orderCrudRestApiImpl.duplicate("1234");
      assertEquals(actualRes.getStatus(), expectedCorrectStatusCode);      
    }
    

    @AfterTest
    public void afterTest() {
        orderaccessioningId=null;
        domainId=0L;
        expectedCorrectStatusCode = 0;
        expectedIncorrectStatusCode = 0;
    }
 }
