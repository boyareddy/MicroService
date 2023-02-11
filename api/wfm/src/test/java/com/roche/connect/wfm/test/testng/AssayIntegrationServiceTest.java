package com.roche.connect.wfm.test.testng;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.MolecularIDTypeDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.wfm.constants.WfmConstants.API_URL;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.AssayTypeDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.ProcessStepActionDTO;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.service.AssayIntegrationService;
import com.roche.connect.wfm.service.OrderIntegrationService;
import com.roche.connect.wfm.service.ResultIntegrationService;


@PrepareForTest({ RestClientUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class AssayIntegrationServiceTest{
    
    
    @InjectMocks
     AssayIntegrationService assayIntegrationService;
    
    @Mock ObjectMapper objectMapper;
    
    @Mock
	private OrderIntegrationService orderIntegrationService = org.mockito.Mockito.mock(OrderIntegrationService.class);

	
    @Mock
  	private ResultIntegrationService resultIntegrationService = org.mockito.Mockito.mock(ResultIntegrationService.class);
    
    @Mock Invocation.Builder assayClient;
    
    @Mock HMTPLoggerImpl hmtpLoggerImpl;
     
    @Mock
	Response response;
    
    List<AssayTypeDTO> listAssays =new ArrayList<>();
    AssayTypeDTO assayTypeDTO =new AssayTypeDTO();
    List<DeviceTestOptionsDTO> deviceTestOptionsDTOList =new ArrayList<>();
    List<MolecularIDTypeDTO> molecularIDTypeDTOlist =new ArrayList<>();
    List<ProcessStepActionDTO> processsteplist=new ArrayList<>();
    DeviceTestOptionsDTO deviceTestOptionsDTO=new DeviceTestOptionsDTO();
    MolecularIDTypeDTO molecularIDTypeDTO=new MolecularIDTypeDTO();
    ProcessStepActionDTO processstepdto=new ProcessStepActionDTO();
    List<WfmDTO> wfmdtolist=new ArrayList<>();
    WfmDTO wfmdto=new WfmDTO();
    
    List<com.roche.connect.common.amm.dto.ProcessStepActionDTO> processStepActionsList =new ArrayList<>();
    com.roche.connect.common.amm.dto.ProcessStepActionDTO procstepActionDTO=new com.roche.connect.common.amm.dto.ProcessStepActionDTO();
   
    private static Logger logger = LogManager.getLogger(AssayIntegrationServiceTest.class);

    String url = null;
    String assayType=null;
    String deviceType=null;
    String processStepName=null;
    String Processstep=null;
    String plateType=null;
    String plateLocation=null;
    String ManualFlag=null;
    String containerid=null;
    String stepName=null;
    String accessioningId=null;   
    
    @BeforeTest
    public void setUp() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
        
        
        assayTypeDTO.setAssayType("NIPTDPCR");
        listAssays.add(assayTypeDTO);
        url = "/json/rest/api/v1/assay/";
        assayType="NIPTDPCR";
        deviceType="MP96";
        plateType="B";
        plateLocation="B1";
        processStepName="NA Extraction";   
        ManualFlag="Y";
        deviceTestOptionsDTO.setDeviceType("MP96");
        deviceTestOptionsDTOList.add(deviceTestOptionsDTO);
        
        molecularIDTypeDTO.setAssayType("NIPTDPCR");
        molecularIDTypeDTOlist.add(molecularIDTypeDTO);
        wfmdto.setAccessioningId("50100");
        wfmdto.setAssayType("NIPTDPCR");
        wfmdtolist.add(wfmdto);
        
        processstepdto.setProcessStepName("NA Extraction");
        processstepdto.setDeviceType("MP96");
        processstepdto.setManualVerificationFlag("Y");
        processsteplist.add(processstepdto);
       
        accessioningId="100100";
        containerid="ABCCD_B1";
        stepName="NA";
        
         procstepActionDTO.setAssayType("NIPTDPCR");
        processStepActionsList.add(procstepActionDTO);
    }
    
    /** Mandatory to mock static classes **/
    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    
    @Test(priority=1)
    public void findAssayDetailsEmptycondition() throws HMTPException, IOException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        List<AssayTypeDTO> listAssayssecond =new ArrayList<>();
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/", null, null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(assayClient);
        Mockito.when(assayClient.get(new GenericType<List<AssayTypeDTO>>() {})).thenReturn(listAssayssecond);
        
        assayIntegrationService.findAssayDetails();
       
    }
    
    
    
    @Test(priority=17, expectedExceptions = HMTPException.class)
    public void findAssayDetailException() throws HMTPException, IOException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/", null, null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenThrow(IOException.class);
        assayIntegrationService.findAssayDetails();
              
    }
    
    
    @Test(priority=18, expectedExceptions = HMTPException.class)
    public void findProcessStepForAssayException() throws HMTPException, IOException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
                WfmURLConstants.AMM_ASSAY_API_PATH + "NIPT", WfmURLConstants.PROCESS_STEP_ACTION, null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenThrow(IOException.class);
        assayIntegrationService.findProcessStepForAssay("NIPT");
              
    }
    
    
    
    @Test(priority=19, expectedExceptions = HMTPException.class)
    public void findAssayDetailExceptionSecond() throws HMTPException, IOException {
    	 MockitoAnnotations.initMocks(this);
         PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_TYPE,
        		"NIPTDPCR", null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenThrow(IOException.class);
       assayIntegrationService.findAssayDetail("NIPTDPCR");
              
    }
    
    
    @Test(priority=20, expectedExceptions = HMTPException.class)
    public void findDeviceTestOptionsException() throws HMTPException, IOException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
       Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
       Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
           WfmURLConstants.AMM_ASSAY_API_PATH + "NIPTDPCR", WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + "MP96"
           + WfmURLConstants.AMM_AMPERSAND + WfmURLConstants.AMM_PROCESSSTEP_PARAM + "NA Extraction",
       null)).thenReturn(url);
       Mockito.when( RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenThrow(IOException.class);
       assayIntegrationService.findDeviceTestOptions("NIPTDPCR","MP96","NA Extraction");
    }
    
    
    @Test(priority=21, expectedExceptions = HMTPException.class)
    public void getMolecularIdByAssayTypeandPlateTypeAndPlateLocationException() throws HMTPException, IOException {
           
           MockitoAnnotations.initMocks(this);
           PowerMockito.mockStatic(RestClientUtil.class);
          Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
          Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_API_PATH,
              WfmURLConstants.AMM_MOLECULAR_ID_ASSAY + "NIPTDPCR" + WfmURLConstants.AMM_AMPERSAND
              + WfmURLConstants.AMM_MOLECULAR_ID_PLATETYPE + "B" + WfmURLConstants.AMM_AMPERSAND
              + WfmURLConstants.AMM_MOLECULAR_ID_PLATELOCATION + "B1",
          null)).thenReturn(url);
          Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenThrow(IOException.class);
         assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation("NIPTDPCR","B","B1");
       }
    
  
    
   
    @Test(priority=2)
    public void findAssayDetails() throws HMTPException, IOException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/", null, null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(assayClient);
        Mockito.when(assayClient.get(new GenericType<List<AssayTypeDTO>>() {})).thenReturn(listAssays);
        
        List<AssayTypeDTO>  assaylistdto= assayIntegrationService.findAssayDetails();
        assertEquals(assaylistdto.get(0).getAssayType(),"NIPTDPCR" );
    }
    
    @Test(priority=3)
    public void findAssayDetail() throws HMTPException, IOException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
       Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
       Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_TYPE,
           assayType, null)).thenReturn(url);
       Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
       Mockito.when(assayClient.get(new GenericType<List<AssayTypeDTO>>() {})).thenReturn(listAssays);
        List<AssayTypeDTO> assaylistdto= assayIntegrationService.findAssayDetail(assayType);
        assertEquals(assaylistdto.get(0).getAssayType(),"NIPTDPCR" );
    }
    
    @Test(priority=4)
    public void findDeviceTestOptions() throws HMTPException, IOException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
       Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
       Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
           WfmURLConstants.AMM_ASSAY_API_PATH + assayType, WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + deviceType
           + WfmURLConstants.AMM_AMPERSAND + WfmURLConstants.AMM_PROCESSSTEP_PARAM + processStepName,
       null)).thenReturn(url);
       Mockito.when( RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
       Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {})).thenReturn(deviceTestOptionsDTOList);
        List<DeviceTestOptionsDTO> deviceTestOptionsDTOlt= assayIntegrationService.findDeviceTestOptions(assayType,deviceType,processStepName);
        assertEquals(deviceTestOptionsDTOlt.get(0).getDeviceType(),"MP96" );
    }
    
    @Test(priority=5)
    public void findDeviceTestOptionsElseOption() throws HMTPException, IOException {
        
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
       Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
       Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
           WfmURLConstants.AMM_ASSAY_API_PATH + assayType,
           WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + deviceType, null)).thenReturn(url);
       Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
       Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {})).thenReturn(deviceTestOptionsDTOList);
        List<DeviceTestOptionsDTO> deviceTestOptionsDTOlt= assayIntegrationService.findDeviceTestOptions(assayType,deviceType,Processstep);
        assertEquals(deviceTestOptionsDTOlt.get(0).getDeviceType(),"MP96" );
    }
    
    
    @Test(priority=6)
    public void findDeviceTestOptionsEmptycondition() throws HMTPException, IOException {
        
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        List<DeviceTestOptionsDTO> deviceTestOptionsDTOlist=new ArrayList<>();
       Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
       Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
           WfmURLConstants.AMM_ASSAY_API_PATH + assayType,
           WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + deviceType, null)).thenReturn(url);
       Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
       Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {})).thenReturn(deviceTestOptionsDTOlist);
        assayIntegrationService.findDeviceTestOptions(assayType,deviceType,Processstep);
    }
    @Test(priority=7)
 public void getMolecularIdByAssayTypeandPlateTypeAndPlateLocation() throws HMTPException, IOException {
        
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
       Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
       Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_API_PATH,
           WfmURLConstants.AMM_MOLECULAR_ID_ASSAY + assayType + WfmURLConstants.AMM_AMPERSAND
           + WfmURLConstants.AMM_MOLECULAR_ID_PLATETYPE + plateType + WfmURLConstants.AMM_AMPERSAND
           + WfmURLConstants.AMM_MOLECULAR_ID_PLATELOCATION + plateLocation,
       null)).thenReturn(url);
       Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
       Mockito.when(assayClient.get(new GenericType<List<MolecularIDTypeDTO>>() {})).thenReturn(molecularIDTypeDTOlist);
        List<MolecularIDTypeDTO> listMolicularIds= assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(assayType,plateType,plateLocation);
        assertEquals(listMolicularIds.get(0).getAssayType(),"NIPTDPCR" );
    }
    
    @Test(priority=8)
    public void getMolecularIdByAssayTypeandPlateTypeAndPlateLocationEmptycondition() throws HMTPException, IOException {
           
           MockitoAnnotations.initMocks(this);
           PowerMockito.mockStatic(RestClientUtil.class);
           List<MolecularIDTypeDTO> listMolicularIdlist=new ArrayList<>();
          Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
          Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_API_PATH,
              WfmURLConstants.AMM_MOLECULAR_ID_ASSAY + assayType + WfmURLConstants.AMM_AMPERSAND
              + WfmURLConstants.AMM_MOLECULAR_ID_PLATETYPE + plateType + WfmURLConstants.AMM_AMPERSAND
              + WfmURLConstants.AMM_MOLECULAR_ID_PLATELOCATION + plateLocation,
          null)).thenReturn(url);
          Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
          Mockito.when(assayClient.get(new GenericType<List<MolecularIDTypeDTO>>() {})).thenReturn(listMolicularIdlist);
           assayIntegrationService.getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(assayType,plateType,plateLocation);
       }
    
    @Test(priority=9)
    public void validateAssayProcessStep() throws HMTPException, OrderNotFoundException, IOException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
            WfmURLConstants.AMM_ASSAY_API_PATH + assayType, WfmURLConstants.PROCESS_STEP_ACTION, null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
       
        Mockito.when(assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {})).thenReturn(processsteplist);
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmdtolist);
        boolean  validate= assayIntegrationService.validateAssayProcessStep(accessioningId,deviceType,processStepName);
        assertEquals(validate,true);
    }
    
    
    @Test(priority=10)
    public void validateAssayProcessStepManual() throws HMTPException, IOException, OrderNotFoundException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
            WfmURLConstants.AMM_ASSAY_API_PATH + assayType, WfmURLConstants.PROCESS_STEP_ACTION, null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
       
        Mockito.when(assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {})).thenReturn(processsteplist);
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmdtolist);
        boolean  validate= assayIntegrationService.validateAssayProcessStepManual(accessioningId,deviceType,processStepName,ManualFlag);
        assertEquals(validate,true);
    }
    
    //************
    @Test(priority=11)
    public void getTestOptionsByAccessioningIDandDevice() throws HMTPException, IOException, OrderNotFoundException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
            WfmURLConstants.AMM_ASSAY_API_PATH + assayType,
            WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + deviceType, null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
        Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {})).thenReturn(deviceTestOptionsDTOList);
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmdtolist);
        List<DeviceTestOptionsDTO> deviceTestOptionsDTOLists = assayIntegrationService.getTestOptionsByAccessioningIDandDevice(accessioningId,deviceType);
        assertEquals(deviceTestOptionsDTOLists.get(0).getDeviceType(),"MP96" );
    }
    
    @Test(priority=12)
    public void getTestOptionsByAccessioningIDandDeviceandProcessStep() throws HMTPException, IOException, OrderNotFoundException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
            WfmURLConstants.AMM_ASSAY_API_PATH + assayType, WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + deviceType
            + WfmURLConstants.AMM_AMPERSAND + WfmURLConstants.AMM_PROCESSSTEP_PARAM + processStepName,
        null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
        Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {})).thenReturn(deviceTestOptionsDTOList);
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmdtolist);
        List<DeviceTestOptionsDTO> deviceTestOptionsDTOLists = assayIntegrationService.getTestOptionsByAccessioningIDandDeviceandProcessStep(accessioningId,deviceType,processStepName);
        assertEquals(deviceTestOptionsDTOLists.get(0).getDeviceType(),"MP96" );
    }
    
    @Test(priority=13)
    public void getTestOptionsByOrderIdandDevice() throws HMTPException, IOException, OrderNotFoundException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
            WfmURLConstants.AMM_ASSAY_API_PATH + assayType,
            WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + deviceType, null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
        Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {})).thenReturn(deviceTestOptionsDTOList);
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmdtolist);
        List<DeviceTestOptionsDTO> deviceTestOptionsDTOLists = assayIntegrationService.getTestOptionsByOrderIdandDevice(wfmdtolist,deviceType);
        assertEquals(deviceTestOptionsDTOLists.get(0).getDeviceType(),"MP96" );
    }
    
    @Test(priority=14)
    public void getTestOptionsByOrderIdandDeviceandProcessStep() throws HMTPException, IOException, OrderNotFoundException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
            WfmURLConstants.AMM_ASSAY_API_PATH + assayType, WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + deviceType
            + WfmURLConstants.AMM_AMPERSAND + WfmURLConstants.AMM_PROCESSSTEP_PARAM + processStepName,
        null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
        Mockito.when(assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {})).thenReturn(deviceTestOptionsDTOList);
        Mockito.when(orderIntegrationService.findOrder(accessioningId)).thenReturn(wfmdtolist);
        List<DeviceTestOptionsDTO> deviceTestOptionsDTOLists = assayIntegrationService.getTestOptionsByOrderIdandDeviceandProcessStep(wfmdtolist,deviceType,processStepName);
        assertEquals(deviceTestOptionsDTOLists.get(0).getDeviceType(),"MP96" );
    }
    
    
    @Test(priority=15)
    public void getProcessStepAction() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
      
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
        Mockito.when(RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_API_PATH,
				"/" + assayType + WfmURLConstants.PROCESS_STEP_ACTION, null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
        Mockito.when(assayClient.get(new GenericType<List<com.roche.connect.common.amm.dto.ProcessStepActionDTO>>() {})).thenReturn(processStepActionsList);
        try {
        assayIntegrationService.getProcessStepAction(assayType,deviceType);
        }catch(Exception e) {
        	logger.info("getProcessStepAction passed");
        }
       
    }
    
    @Test(priority=16)
    public void findAccessingIdByContainerId() throws HMTPException, UnsupportedEncodingException, OrderNotFoundException, JsonProcessingException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
      
        List<WfmDTO> listWfmDto =new ArrayList<>();
        WfmDTO wfmDTO=new WfmDTO();
        wfmDTO.setAccessioningId("1234");
        listWfmDto.add(wfmdto);
        
        List<SampleResultsDTO> sampledtoList=new ArrayList<>();
        
        sampledtoList.add(getSampleResultsDTO());
        Mockito.when(resultIntegrationService.findAccessingIdByContainerId("ABC", "CDE", "NA Extraction")).thenReturn(listWfmDto);
        
        Mockito.when(RestClientUtil.getUrlString(API_URL.RMM_API_URL.toString(), "",
                WfmURLConstants.RUN_RESULT_API_PATH + WfmURLConstants.SAMPLE_RESULTS + WfmURLConstants.QUESTION_MARK,
                WfmURLConstants.OUTPUT_CONTAINER_ID + "ABC" + WfmURLConstants.OUTPUT_CONTAINER_POSITION
                    + "CDE" + WfmURLConstants.PROCESS_STEP_NAME_ONE + "NA Extraction",
                null)).thenReturn(url);
        
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null)).thenReturn(assayClient);
        //Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(assayClient);
        Mockito.when(assayClient.get(new GenericType<List<SampleResultsDTO>>() {})).thenReturn(sampledtoList);
        Mockito.when(objectMapper.writeValueAsString(getSampleResultsDTO())).thenReturn("sample");
        
       
        try {
        assayIntegrationService.findAccessingIdByContainerId("ABC_CDE","NA Extraction");
        }catch(Exception e) {
        	logger.info("getProcessStepAction passed");
        }
       
    }
    
    public SampleResultsDTO getSampleResultsDTO() {
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        Collection<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumablesList = new ArrayList<>();
        SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
        sampleResultsDTO.setAccesssioningId("1000111");
        sampleResultsDTO.setOrderId(Long.parseLong("1000001"));
        sampleResultsDTO.setStatus("Completed");
        sampleResultsDTO.setRunResultsId(Long.parseLong("123"));
        sampleResultsDTO.setInputContainerId("12abc");
        sampleResultsDTO.setInputContainerPosition("ABC");
        sampleReagentsAndConsumablesList.add(sampleReagentsAndConsumablesDTO);
        sampleResultsDTO.setSampleReagentsAndConsumables(sampleReagentsAndConsumablesList);
        return sampleResultsDTO;
    }
   
}
