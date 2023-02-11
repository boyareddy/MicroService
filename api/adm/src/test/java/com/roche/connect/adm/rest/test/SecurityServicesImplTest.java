package com.roche.connect.adm.rest.test;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
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
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.service.SecurityServicesImpl;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class}) @PowerMockIgnore({ "sun.misc.Launcher.*",
"com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*", "org.apache.logging.*",
"org.slf4j.*" }) 
public class SecurityServicesImplTest {
    
    @InjectMocks  SecurityServicesImpl securityServicesImpl;
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    @Mock Invocation.Builder resp;
    @Mock Response response;
    
    @BeforeTest public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        
        ReflectionTestUtils.setField(securityServicesImpl,"securityHostURL","http://localhost");
        PowerMockito.mockStatic(RestClientUtil.class);
        String url ="http://localhost/json/roles";
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url , "UTF-8"), null)).thenReturn(resp);
        Mockito.when(resp.get()).thenReturn(response);
        Mockito.when(resp.get().getStatus()).thenReturn(200);
        String respBody ="{\"roles\":[\r\n" + 
            "\"id\":1,\r\n" + 
            "\"name\":\"admin\"\r\n" + 
            "]\r\n" + 
            "}\r\n" + 
            "";
        Mockito.when(resp.get(new GenericType<String>() {})).thenReturn(respBody);
    }
    
    /**
     * We need a special {@link IObjectFactory}.
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test
    public void getRolesTest() throws HMTPException {
        Set<Integer> roleIds = new HashSet<>();
        roleIds.add(1);
        securityServicesImpl.getRoles(roleIds );
    }
}
