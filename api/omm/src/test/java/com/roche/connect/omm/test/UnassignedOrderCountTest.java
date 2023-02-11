package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;
import javax.ws.rs.core.Response;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;


/**
 * The Class UnassignedOrderCountTest.
 * 
 */
@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
/**
 * 
 * @author imtiyazm
 *
 */
public class UnassignedOrderCountTest {


    @Mock
    OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);
    
    @InjectMocks
    @Spy
    OrderService orderService=Mockito.spy(new OrderServiceImpl());

    @InjectMocks
    OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();
   

    @Mock HMTPLoggerImpl hmtpLoggerImpl;
   

    Long domainId=0L;
    

    int expectedCorrectStatusCode = 0;
 
    int expectedIncorrectStatusCode = 0;

    /**
     * Before.
     */
    @BeforeTest
    public void before() {
        expectedCorrectStatusCode = 200;
        expectedIncorrectStatusCode = 400;
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        
    }
    
    /**
     * Unassigned order count.
     *
     * @throws HMTPException the HMTP exception
     */
    @Test
    public void unassignedOrderCount() throws HMTPException {
      Mockito.when( orderReadRepository.findUnassignedOrdersCount(any(long.class))).thenReturn(any(long.class));
      Response actualRes= orderCrudRestApiImpl.getUnassignedOrderCount();
      assertEquals(actualRes.getStatus(), expectedCorrectStatusCode);      
    }
    
  

    /**
     * After test.
     */
    @AfterTest
    public void afterTest() {
        domainId=0L;
        expectedCorrectStatusCode = 0;
        expectedIncorrectStatusCode = 0;
    }
 

}
