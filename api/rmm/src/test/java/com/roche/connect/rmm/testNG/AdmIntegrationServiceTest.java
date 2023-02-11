package com.roche.connect.rmm.testNG;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.dto.SystemSettingsDto;
import com.roche.connect.rmm.services.AdmIntegrationService;
import com.roche.connect.rmm.util.RMMConstant;

@PrepareForTest({ RestClientUtil.class,HMTPLoggerImpl.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*",
"javax.management.*", "org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" }) 
public class AdmIntegrationServiceTest {
    
    @InjectMocks AdmIntegrationService admIntegrationService;
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
    @Mock Invocation.Builder admClient;
    @Mock InputStream bytess;
    List<SystemSettingsDto> listSystemSettings = new ArrayList<>();
    
    @BeforeTest public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        listSystemSettings.add(getSystemSettingsDtoByLabName());
        listSystemSettings.add(getSystemSettingsDtoByPhoneNumber());
        listSystemSettings.add(getSystemSettingsDtoByLabAddress1());
        listSystemSettings.add(getSystemSettingsDtoByLabAddress2());
        listSystemSettings.add(getSystemSettingsDtoByLabAddress3());
    }
    
    /**
     * We need a special {@link IObjectFactory}.
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    @Test
    public void getSystemSettingsTest() throws HMTPException, UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        String url ="";
        Mockito.when( RestClientUtil.getUrlString("pas.adm_api_url", "", "/json/rest/api/v1/systemsettings",
            "", null)).thenReturn(url );
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null)).thenReturn(admClient);
        Mockito.when(admClient.get(new GenericType<List<SystemSettingsDto>>() {
            })).thenReturn(listSystemSettings);
        admIntegrationService.getSystemSettings();
    }
    
    @Test
    public void getLabLogoTest() throws HMTPException, UnsupportedEncodingException {
        PowerMockito.mockStatic(RestClientUtil.class);
        String url ="";
        Mockito.when( RestClientUtil.getUrlString("pas.adm_api_url", "", "/json/rest/api/v1/lablogo",
                    "", null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null)).thenReturn(admClient);
        Mockito.when(admClient.get(new GenericType<InputStream>() {
            })).thenReturn(bytess);
        admIntegrationService.getLabLogo();
    }
    
    @Test
    public void getLabDetailsTest() {
        admIntegrationService.getLabDetails(listSystemSettings);
    }
    
    
    public SystemSettingsDto getSystemSettingsDtoByLabName() {
        SystemSettingsDto systemSettingsDto = new SystemSettingsDto();
        systemSettingsDto.setAttributeName("labName");
        systemSettingsDto.setAttributeValue("Roche");
        return systemSettingsDto;
    }
    public SystemSettingsDto getSystemSettingsDtoByPhoneNumber() {
        SystemSettingsDto systemSettingsDto = new SystemSettingsDto();
        systemSettingsDto.setAttributeName("phoneNumber");
        systemSettingsDto.setAttributeValue("1234567890");
        return systemSettingsDto;
    }
    public SystemSettingsDto getSystemSettingsDtoByLabAddress1() {
        SystemSettingsDto systemSettingsDto = new SystemSettingsDto();
        systemSettingsDto.setAttributeName("labAddress1");
        systemSettingsDto.setAttributeValue("US");
        return systemSettingsDto;
    }
    public SystemSettingsDto getSystemSettingsDtoByLabAddress2() {
        SystemSettingsDto systemSettingsDto = new SystemSettingsDto();
        systemSettingsDto.setAttributeName("labAddress2");
        systemSettingsDto.setAttributeValue("India");
        return systemSettingsDto;
    }
    public SystemSettingsDto getSystemSettingsDtoByLabAddress3() {
        SystemSettingsDto systemSettingsDto = new SystemSettingsDto();
        systemSettingsDto.setAttributeName("labAddress3");
        systemSettingsDto.setAttributeValue("Singapore");
        return systemSettingsDto;
    }
}
