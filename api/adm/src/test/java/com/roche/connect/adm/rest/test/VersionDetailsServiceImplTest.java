package com.roche.connect.adm.rest.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

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
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.service.VersionDetailsServiceImpl;
import com.roche.connect.common.amm.dto.AssayTypeDTO;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, ThreadSessionManager.class }) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" })
public class VersionDetailsServiceImplTest {

	 @InjectMocks VersionDetailsServiceImpl versionDetailsServiceImpl;
	 @Mock HMTPLoggerImpl hmtpLoggerImpl;
	 @Mock Builder builder;
	 
	 @BeforeTest
	 public void setUp() {
		 MockitoAnnotations.initMocks(this);
	     PowerMockito.mockStatic(RestClientUtil.class);
	     ReflectionTestUtils.setField(versionDetailsServiceImpl, "deviceHostUrl", "http://localhost");
	     ReflectionTestUtils.setField(versionDetailsServiceImpl, "ammHostUrl", "http://localhost");
	 }
	
	 /**
	     * We need a special {@link IObjectFactory}.
	     * @return {@link PowerMockObjectFactory}.
	     */
	    @ObjectFactory public IObjectFactory getObjectFactory() {
	        return new org.powermock.modules.testng.PowerMockObjectFactory();
	    }
	    
	    @Test
	    public void getSupportedDeviceWithVersionTest() throws HMTPException, UnsupportedEncodingException {
	    	PowerMockito.mockStatic(RestClientUtil.class);
	    	String url="http://localhost/json/devicetype/fetch";
	        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(builder);
	        String response="[ {\"name\": \"MagNaPure24\", \"description\": \"MagNaPure24\", \"active\": true, \"isRetired\": \"N\", \"attributes\": { \"supportedAssayTypes\": [ \"NIPT-HTP\" ], \"supportedProtocols\": [ \"HL7\" ], \"supportedProtocolVersion\": [ \"2.5.1\" ] }, \"parentDeviceType\": null, \"editedBy\": \"admin#@#hcl.com\", \"createdBy\": \"admin#@#hcl.com\", \"createdDate\": \"03-12-2018 21:17:52 PM\", \"modifiedDate\": \"03-12-2018 21:17:52 PM\", \"deviceTypeId\": \"fa99cb08-76bf-40c2-8dc7-9c44116dee78\" }, { \"name\": \"MagNa Pure 96\", \"description\": \"MagNa Pure 96\", \"active\": true, \"isRetired\": \"N\", \"attributes\": { \"supportedAssayTypes\": [ \"NIPT-dPCR\" ], \"supportedProtocols\": [ \"HL7\" ], \"supportedProtocolVersion\": [ \"2.4\" ] }, \"parentDeviceType\": null, \"editedBy\": \"admin#@#hcl.com\", \"createdBy\": \"admin#@#hcl.com\", \"createdDate\": \"03-12-2018 21:17:52 PM\", \"modifiedDate\": \"03-12-2018 21:17:52 PM\", \"deviceTypeId\": \"fddf0abb-ba4f-463f-b100-accbc2ee6e62\" }, { \"name\": \"High Throughput sequencing\", \"description\": \"High Throughput sequencing\", \"active\": true, \"isRetired\": \"N\", \"attributes\": { \"supportedAssayTypes\": [ \"NIPT-HTP\" ], \"supportedProtocols\": [ \"REST\" ] }, \"parentDeviceType\": null, \"editedBy\": \"admin#@#hcl.com\", \"createdBy\": \"admin#@#hcl.com\", \"createdDate\": \"03-12-2018 21:17:52 PM\", \"modifiedDate\": \"03-12-2018 21:17:52 PM\", \"deviceTypeId\": \"39746f3e-2646-4fc2-ac1e-e8a13bb1b4be\" }, { \"name\": \"LP24\", \"description\": \"LP24\", \"active\": true, \"isRetired\": \"N\", \"attributes\": { \"supportedAssayTypes\": [ \"NIPT-HTP\", \"NIPT-dPCR\" ], \"supportedProtocols\": [ \"HL7\" ], \"supportedProtocolVersion\": [ \"2.5.1\" ] }, \"parentDeviceType\": null, \"editedBy\": \"admin#@#hcl.com\", \"createdBy\": \"admin#@#hcl.com\", \"createdDate\": \"03-12-2018 21:17:52 PM\", \"modifiedDate\": \"03-12-2018 21:17:52 PM\", \"deviceTypeId\": \"7cd184f7-8989-4322-9ee4-33e68472aaf7\" }, { \"name\": \"cobas dPCR\", \"description\": \"cobas dPCR\", \"active\": true, \"isRetired\": \"N\", \"attributes\": { \"supportedAssayTypes\": [ \"NIPT-HTP\", \"NIPT-dPCR\" ], \"supportedProtocols\": [ \"HL7\" ], \"supportedProtocolVersion\": [ \"2.5\", \"2.6\", \"2.7\", \"2.8\", \"2.9\", \"3.0\" ] }, \"parentDeviceType\": null, \"editedBy\": \"admin#@#hcl.com\", \"createdBy\": \"admin#@#hcl.com\", \"createdDate\": \"03-12-2018 21:17:52 PM\", \"modifiedDate\": \"03-12-2018 21:17:52 PM\", \"deviceTypeId\": \"1fb02202-db3a-4fa3-bbc7-8392f966d924\" } ]";
	        Mockito.when(builder.get(String.class)).thenReturn(response);
	    	versionDetailsServiceImpl.getSupportedDeviceWithVersion();
	    }
	    @Mock Invocation.Builder orderClient;
	    @Test
	    public void getAssaysWithVersionTest() throws UnsupportedEncodingException, HMTPException {
	    	PowerMockito.mockStatic(RestClientUtil.class);
	    String url="http%3A%2F%2Flocalhost%2Fjson%2Frest%2Fapi%2Fv1%2Fassay";
	      Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(orderClient);
	        List<AssayTypeDTO> assayList = getAssayTypeDTO();
	        Mockito.when(orderClient.get(new GenericType<List<AssayTypeDTO>>() {
			})).thenReturn(assayList);
	    	versionDetailsServiceImpl.getAssaysWithVersion();
	    }
	    
	    public List<AssayTypeDTO> getAssayTypeDTO() {
	    	List<AssayTypeDTO> assayList = new ArrayList<>();
	    	AssayTypeDTO assayTypeDTO = new AssayTypeDTO();
	    	assayTypeDTO.setAssayType("NIPTHTP");
	    	assayTypeDTO.setAssayVersion("1.0");
	    	assayList.add(assayTypeDTO);
			return assayList;
	    	
	    }
	
}
