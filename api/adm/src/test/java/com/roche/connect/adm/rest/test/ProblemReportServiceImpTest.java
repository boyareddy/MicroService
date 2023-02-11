package com.roche.connect.adm.rest.test;

import java.io.File;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
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
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.service.ProblemReportServiceImp;
import com.roche.connect.adm.util.AuditTrailDetailDTO;
import com.roche.connect.adm.util.AuditTrailResponseDTO;
import com.roche.connect.adm.util.DeviceSummaryDTO;
import com.roche.connect.adm.util.EntityDTO;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class, ThreadSessionManager.class }) @PowerMockIgnore({
"sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*", "org.w3c.dom.*",
"org.apache.logging.*", "org.slf4j.*" })
public class ProblemReportServiceImpTest {
	@InjectMocks
	ProblemReportServiceImp problemReportServiceImp;

	@Mock Invocation.Builder builder ;
	@Mock HMTPLoggerImpl hmtpLoggerImpl;
	@Mock UserSession userSession;
	 /**
     * We need a special {@link IObjectFactory}.
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    @BeforeTest public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString());
        Mockito.doNothing().when(hmtpLoggerImpl).error(Mockito.anyString(), Mockito.any(Object.class));
        PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorUserName()).thenReturn("name");
        Mockito.when(ThreadSessionManager.currentUserSession().getRoleForTenantAccess()).thenReturn("Admin");
    }
    @Test
    public void getRunTimeLogsBetweenDatesTest() throws HMTPException{
    	try{
    File file=	problemReportServiceImp.getRunTimeLogsBetweenDates(new Date(),new Date(),"C://tempReportPath//hhh" ,"runtimeLogs" ,"C://problemReportPath//")	;
      problemReportServiceImp.unzip(file.getAbsolutePath(),"C://tempReportPath//" );
      problemReportServiceImp.deleteTempDir("C://dailyProblemReport//");
    	}catch(Exception e){
    		System.out.println(e.getMessage()); 
    	}
    	
    }
   @Test
    public void unzipNegative() throws Exception{	
	   try{
		   problemReportServiceImp.unzip("C://problemReportPath//","C://problemReportPath//");	   
	   }catch(Exception exception){
		   System.out.println(exception.getMessage());
	   }
    } 
    @Test
    public void getProblemReportTest(){
    	try{
    	problemReportServiceImp.getProblemReport("20022019", "20122020", "C://problemReportPath//");
    	}catch(Exception exception){
    		
    	}
    }
    
    @Test
    public  void getDeviceTypeDetailsTest() throws HMTPException {
    	try{
    	ReflectionTestUtils.setField(problemReportServiceImp, "deviceEndPoint", "http://localhost");
    	PowerMockito.mockStatic(RestClientUtil.class);
    	String url="http://localhost/json/device/fetch/expr?filterExpression=serialNo%3D%27deviceid%27";
		Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder);
		ReflectionTestUtils.setField(problemReportServiceImp, "loginUrl", "http://localhost");
    	ReflectionTestUtils.setField(problemReportServiceImp, "loginEntity", "http://localhost");
    	Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(builder);
    	Mockito.when(builder.post(Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED), String.class)).thenReturn("token");	
		Mockito.when(builder.header("Cookie", "brownstoneauthcookie=" + "token")).thenReturn(builder);
		String resp = "[]";
		Mockito.when(builder.get(String.class)).thenReturn(resp);
    	problemReportServiceImp.getDeviceTypeDetails(Arrays.asList(getDeviceSummaryDTO()));
    	}
    	catch(Exception e){
    		System.out.println(e.getMessage());
    	}
	}
    
    @Test
    public void getTokenTest(){
    	try{
    	PowerMockito.mockStatic(RestClientUtil.class);
    	ReflectionTestUtils.setField(problemReportServiceImp, "loginUrl", "http://localhost");
    	ReflectionTestUtils.setField(problemReportServiceImp, "loginEntity", "http://localhost");
    	Mockito.when(RestClientUtil.getBuilder("http://localhost", null)).thenReturn(builder);
    	Mockito.when(builder.post(Entity.entity("http://localhost", MediaType.APPLICATION_FORM_URLENCODED), String.class)).thenReturn("token");
    	problemReportServiceImp.getToken();}catch(Exception e){
    		System.out.println(e.getMessage());
    	}
    	
    }
    
    @Mock File auditCsvFile;
    @Mock File auditZipFile;
    @Mock Writer writer;
    @Test
    public void createCSVFileForAuditLogTest() throws HMTPException{
    	try{
    	ReflectionTestUtils.setField(problemReportServiceImp, "columnsToMap", "id,companydomainname,messagecode,newnessflag,objectnewvalue,objectoldvalue,params,systemid,systemmodulename,timestamp,userid");
    	problemReportServiceImp.createCSVFileForAuditLog(getAuditTrailResponseDTO(), auditCsvFile, auditZipFile, writer);
    }catch (Exception e) {
	}}
    
    @Test
    public void   getDaysBetweenDatesTest() {
    	try {
			problemReportServiceImp.getDaysBetweenDates(new SimpleDateFormat("ddMMyyyy").parse("28042019"),new Date() );
			problemReportServiceImp.getDaysBetweenDates(new Date(),new Date());
    	} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	
    }
    @Test
    public  void getDeviceSummaryLogsTest() {
    	try{
    		Mockito.when(builder.get(new GenericType<List<DeviceSummaryDTO>>() {})).thenReturn(Arrays.asList(getDeviceSummaryDTO()));	
    		problemReportServiceImp.getDeviceSummaryLogs("20190220", "20200220","C://tempReportPath//" );	
    	}catch(Exception e){
    		System.out.println(e.getMessage()); 
    	}
	}
@Test
	public void generateProblemReportTest() {
		try {
			ReflectionTestUtils.setField(problemReportServiceImp, "tempReportPath","C://tempReportPath//" );
			problemReportServiceImp.generateProblemReport("2019-04-29 12:23:33", "2019-04-30 12:23:33");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    
    @Test
    public void getAuditDetailsTest() {
    	   try {
    	PowerMockito.mockStatic(ThreadSessionManager.class);
    	PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when((String)ThreadSessionManager.currentUserSession().getObject(Mockito.anyString())).thenReturn("token");
        String url="http://localhost/getaudittrail?fromDate=20190220&toDate=20200220&sortorder=DESC&companydomainname=hcl.com";
        Mockito.when(RestClientUtil.getBuilder(url, null)).thenReturn(builder);
        Mockito.when(builder.header("Cookie", "brownstoneauthcookie=token")).thenReturn(builder);
        AuditTrailResponseDTO auditTrailResponseDTO=getAuditTrailResponseDTO();
        Mockito.when(builder.get(AuditTrailResponseDTO.class)).thenReturn(auditTrailResponseDTO);
    	ReflectionTestUtils.setField(problemReportServiceImp, "limit", 10000);
		problemReportServiceImp. getAuditDetailsFile("20190220","20200220","hcl.com","C://auditLog//AuditDetails_");
		} catch (Exception e) {
			e.printStackTrace();
		}
    } 
    
    public AuditTrailResponseDTO getAuditTrailResponseDTO(){
    	AuditTrailResponseDTO auditTrailResponseDTO = new AuditTrailResponseDTO();
    	EntityDTO entity=new EntityDTO();
    	AuditTrailDetailDTO auditTrailDetailDTO = new AuditTrailDetailDTO();
		 auditTrailDetailDTO.setCompanydomainname("");
		 auditTrailDetailDTO.setId(123);
		 auditTrailDetailDTO.setMessage("");
		 auditTrailDetailDTO.setMessagecode("");
		 auditTrailDetailDTO.setNewnessflag("");
		 auditTrailDetailDTO.setObjectnewvalue(new JSONObject());
		 auditTrailDetailDTO.setObjectoldvalue(new JSONObject());
		 auditTrailDetailDTO.setParams(new JSONObject());
		 auditTrailDetailDTO.setOwnerPropertyName("");
		 auditTrailDetailDTO.setSystemid("");
		 auditTrailDetailDTO.setSystemmodulename("");
		 auditTrailDetailDTO.setTimestamp("");
		 auditTrailDetailDTO.setTitle("");
    	entity.setLstAuditTrail(Arrays.asList(auditTrailDetailDTO ));
		auditTrailResponseDTO.setEntity(entity);
		
		return auditTrailResponseDTO;
    }
    
    private DeviceSummaryDTO getDeviceSummaryDTO() {
		
    	DeviceSummaryDTO deviceSummaryDTO=new DeviceSummaryDTO();
    	deviceSummaryDTO.setDeviceId("deviceid");
    	deviceSummaryDTO.setDeviceRunId("deviceRunId");
    	deviceSummaryDTO.setDeviceType("deviceRunId");
    	deviceSummaryDTO.setRunStatus("runStatus");
		return deviceSummaryDTO;
    	
	}
    

}
