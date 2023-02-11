/*******************************************************************************
 * WebServicesImplTest.java Version: 1.0 Authors: umashankar d
 * ********************* Copyright (c) 2018 Roche Sequencing Solutions (RSS) -
 * CONFIDENTIAL All Rights Reserved. NOTICE: All information contained herein
 * is, and remains the property of COMPANY. The intellectual and technical
 * concepts contained herein are proprietary to COMPANY and may be covered by
 * U.S. and Foreign Patents, patents in process, and are protected by trade
 * secret or copyright law. Dissemination of this information or reproduction of
 * this material is strictly forbidden unless prior written permission is
 * obtained from COMPANY. Access to the source code contained herein is hereby
 * forbidden to anyone except current COMPANY employees, managers or contractors
 * who have executed Confidentiality and Non-disclosure agreements explicitly
 * covering such access The copyright notice above does not evidence any actual
 * or intended publication or disclosure of this source code, which includes
 * information that is confidential and/or proprietary, and is a trade secret,
 * of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS
 * WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF
 * APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS
 * SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS TO
 * REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR
 * SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * ********************* ChangeLog: umashankar-d@hcl.com : Updated copyright
 * headers ********************* Description: *********************
 ******************************************************************************/
package com.roche.connect.htp.adapter.test;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.constants.PlatformConstants;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.hcl.hmtp.common.server.security.entity.PasJwtToken;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.htp.adapter.rest.HtpAdapterRestApiImpl;
import com.roche.connect.htp.adapter.services.WebServicesImpl;

@PrepareForTest({ RestClientUtil.class, ThreadSessionManager.class, AdmNotificationService.class }) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" }) public class WebServicesImplTest {
    
    @InjectMocks HtpAdapterRestApiImpl htpAdaptorRestApiImpl;
    
    @InjectMocks WebServicesImpl webServicesImpl;
    
    @Mock Invocation.Builder builder;
    @Mock Invocation.Builder builder1;
    @Mock Invocation.Builder pasBuilder;
    @Mock Response response;
    @Mock HMTPLoggerImpl logger;
    
    @Mock UserSession userSession;
    
    @Mock AdmNotificationService admNotificationService;
    
    @Mock PasJwtToken pasJwtToken;
    @Mock HttpServletRequest httpServletRequest;
    
    String url = "http://localhost";
    String loginUrl = "http://localhost";
    String loginEntity = "http://localhost";
    String body = "{\"deviceId\":\"MP001\"}";
    Map<String, Long> deviceMap = new HashMap<>();
    
    private int SuccessResponse = 200;
    
    @BeforeTest public void setUp() throws JsonParseException, JsonMappingException, IOException, ParseException {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(logger).info(Mockito.anyString(), Mockito.any(Object.class));
        ReflectionTestUtils.setField(webServicesImpl, "loginUrl", "http://localhost");
        ReflectionTestUtils.setField(webServicesImpl, "loginEntity", "http://localhost");
        ReflectionTestUtils.setField(webServicesImpl, "deviceEndPoint", "http://localhost");
        deviceMap.put("12345", 50000L);
        ReflectionTestUtils.setField(webServicesImpl, "deviceFlag", true);
        ReflectionTestUtils.setField(webServicesImpl, "pingDelay", -1);
    }
    
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test public void getTokenTest() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(pasBuilder);
        Mockito.when(pasBuilder.post(Entity.entity(loginEntity, MediaType.APPLICATION_FORM_URLENCODED), String.class))
            .thenReturn("token");
        String token = webServicesImpl.getToken();
        assertEquals(token, "token");
        
    }
    
    @Test public void postRequestTest() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie= getToken()")).thenReturn(builder);
        Mockito.when(builder.post(Entity.entity(body, MediaType.APPLICATION_JSON)))
            .thenReturn(Response.status(200).build());
        response = webServicesImpl.postRequest(url, body);
        assertEquals(response.getStatus(), SuccessResponse);
    }
    
    @Test public void getRequestTest() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie= getToken()")).thenReturn(builder);
        Mockito.when(builder.get()).thenReturn(Response.status(200).build());
        // Mockito.when(resp.getStatus()).thenReturn(200);
        response = webServicesImpl.getRequest(url);
        assertEquals(response.getStatus(), SuccessResponse);
        
    }
    
    @Test public void putRequestTest() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie= getToken()")).thenReturn(builder);
        Mockito.when(builder.put(Entity.entity(body, MediaType.APPLICATION_JSON)))
            .thenReturn(Response.status(200).build());
        // Mockito.when(response.getStatus()).thenReturn(200);
        response = webServicesImpl.putRequest(url, body);
        assertEquals(response.getStatus(), SuccessResponse);
    }
    
    @Test public void updateCounterTest() throws Exception {
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(Mockito.anyString()))
        .thenReturn("token");
        JSONObject attributes = new JSONObject();
        String url = "http://localhost/json/device/update/9b650548-e591-4293-8735-26f76dc8d4a2";
        Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder);
        Mockito.when(builder.post(Entity.entity(attributes.toString(), MediaType.APPLICATION_JSON)))
        .thenReturn(response);
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing().when(AdmNotificationService.class, "sendNotification", Mockito.eq(null),
            Mockito.anyCollection(), Mockito.anyString(), Mockito.anyString());
        webServicesImpl.updateCounter();
    }
    
    @Test public void updateDeviceStatusTest() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        Mockito.when(httpServletRequest.getRemoteHost()).thenReturn("127.0.0.1");
        String url1 = "http://localhost/json/device/update/9b650548-e591-4293-8735-26f76dc8d4a2";
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        JSONObject attributes = new JSONObject();
        Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder);
        Mockito.when(builder.post(Entity.entity(attributes.toString(), MediaType.APPLICATION_JSON)))
            .thenReturn(response);
        try {
            webServicesImpl.updateDeviceStatus("9b650548-e591-4293-8735-26f76dc8d4a2", "online");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Test public void getDetailsFromOauthTokenTest() {
        System.out.println("ThreadSessionManager:::" + ThreadSessionManager.currentUserSession());
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(userSession.getPasJwtToken()).thenReturn(pasJwtToken);
        Mockito.when(pasJwtToken.getUserName()).thenReturn("userName");
        Mockito.when(pasJwtToken.getDomainName()).thenReturn("domainName");
        Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(Mockito.anyString()))
            .thenReturn("token");
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString())).thenReturn("token");
        
        String url1 = "http://localhost/json/users/userName/userName/domainName/domainName/id";
        Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder);
        Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(pasBuilder);
        Mockito.when(pasBuilder.post(Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED), String.class)).thenReturn("token");
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        
        Mockito.when(builder.get(String.class)).thenReturn("userId");
        
        url1 = "http://localhost/json/device/fetch/expr?filterExpression=user.id=userId";
        Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder1);
        Mockito.when(builder1.get(String.class)).thenReturn(
            "[ { \"deviceType\": { \"deviceTypeId\": \"fa99cb08-76bf-40c2-8dc7-9c44116dee78\" }, \"attributes\": { \"protocolVersion\": \"\",\"location\":\"location\" }, \"deviceId\": \"9b650548-e591-4293-8735-26f76dc8d4a2\" } ]");
        
        try {
            webServicesImpl.getDetailsFromOauthToken(htpAdaptorRestApiImpl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test public void getDetailsFromOauthTokenNTest() throws Exception {
        System.out.println("ThreadSessionManager:::" + ThreadSessionManager.currentUserSession());
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(userSession.getPasJwtToken()).thenReturn(pasJwtToken);
        Mockito.when(pasJwtToken.getUserName()).thenReturn("userName");
        Mockito.when(pasJwtToken.getDomainName()).thenReturn("domainName");
        Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(Mockito.anyString()))
            .thenReturn("token");
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString())).thenReturn("token");
        
        String url1 = "http://localhost/json/users/userName/userName/domainName/domainName/id";
        Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder);
        Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(pasBuilder);
        Mockito.when(pasBuilder.post(Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED), String.class)).thenReturn("token");
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        
        Mockito.when(builder.get(String.class)).thenReturn("userId");
        
        url1 = "http://localhost/json/device/fetch/expr?filterExpression=user.id=userId";
        Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder1);
        Mockito.when(builder1.get(String.class)).thenReturn(
            "[ { \"deviceType\": { \"deviceTypeId\": \"fa99cb08-76bf-40c2-8dc7-9c44116dee78\" }, \"attributes\": { \"protocolVersion\": \"\",\"location\":\"location\" }, \"deviceId\": \"9b650548-e591-4293-8735-26f76dc8d4a2\" } ]");
        htpAdaptorRestApiImpl.setHelloFlag(true);
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing().when(AdmNotificationService.class, "sendNotification", Mockito.eq(null),
            Mockito.anyCollection(), Mockito.anyString(), Mockito.anyString());
        try {
            webServicesImpl.getDetailsFromOauthToken(htpAdaptorRestApiImpl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test public void getDetailsFromOauthTokenN1Test() throws Exception {
        System.out.println("ThreadSessionManager:::" + ThreadSessionManager.currentUserSession());
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(userSession.getPasJwtToken()).thenReturn(pasJwtToken);
        Mockito.when(pasJwtToken.getUserName()).thenReturn("userName");
        Mockito.when(pasJwtToken.getDomainName()).thenReturn("domainName");
        Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(Mockito.anyString()))
            .thenReturn("token");
        Mockito.when((String) ThreadSessionManager.currentUserSession()
            .getObject(PlatformConstants.PAS_ENCRYPTED_COOKIE.toString())).thenReturn("token");
        
        String url1 = "http://localhost/json/users/userName/userName/domainName/domainName/id";
        Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        
        Mockito.when(builder.get(String.class)).thenReturn("userId");
        
        url1 = "http://localhost/json/device/fetch/expr?filterExpression=user.id=userId";
        Mockito.when(RestClientUtil.getBuilder(url1, null)).thenReturn(builder1);
        Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(pasBuilder);
        Mockito.when(pasBuilder.post(Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED), String.class)).thenReturn("token");
        Mockito.when(builder1.get(String.class)).thenReturn("[]");
        PowerMockito.mockStatic(AdmNotificationService.class);
        PowerMockito.doNothing().when(AdmNotificationService.class, "sendNotification", Mockito.eq(null),
            Mockito.anyCollection(), Mockito.anyString(), Mockito.anyString());
        try {
            webServicesImpl.getDetailsFromOauthToken(htpAdaptorRestApiImpl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    
    @Test public void getDetailsFromOauthTokenPositiveTest() throws Exception {
        PowerMockito.mockStatic(RestClientUtil.class);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when((String) ThreadSessionManager.currentUserSession().getObject(Mockito.anyString()))
            .thenReturn("token");
        Mockito.when(ThreadSessionManager.currentUserSession().getPasJwtToken()).thenReturn(pasJwtToken);
        Mockito.when(ThreadSessionManager.currentUserSession().getPasJwtToken().getUserName()).thenReturn("userName");
        Mockito.when(ThreadSessionManager.currentUserSession().getPasJwtToken().getDomainName())
            .thenReturn("domainName");
        String url ="http://localhost/json/users/userName/userName/domainName/domainName/id";
        Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder);
        Mockito.when(RestClientUtil.getBuilder(loginUrl, null)).thenReturn(pasBuilder);
        Mockito.when(pasBuilder.post(Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED), String.class)).thenReturn("token");
        Mockito.when(builder.get(String.class)).thenReturn("1234");
        String url2 = "http://localhost" + "/json/device/fetch/expr?filterExpression=user.id=1234"  ;
        Mockito.when(RestClientUtil.getBuilder(url2, null)).thenReturn(builder);
        try {
            webServicesImpl.getDetailsFromOauthToken(htpAdaptorRestApiImpl);
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
      
    }
    
}
