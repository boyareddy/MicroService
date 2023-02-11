package com.roche.connect.adm.rest.test;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.WebSocketSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.adm.config.SessionDetail;

public class SessionDetailTest {
    
    @InjectMocks SessionDetail sessionDetail;
    @Mock WebSocketSession session;
    
    @BeforeTest()
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
    }
    
    @Test
    public void sessionDetailTest() {
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        sessionDetail.setRoles(roles );
        sessionDetail.setSession(session);
        
        System.out.println(sessionDetail.getRoles());
        System.out.println(sessionDetail.getSession());
    }
    
}
