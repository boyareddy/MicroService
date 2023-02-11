package com.roche.connect.omm.test;

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
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.amm.dto.AssayListDataDTO;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.amm.dto.SampleTypeDTO;
import com.roche.connect.common.amm.dto.TestOptionsDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.omm.services.OrderServiceImpl;

@PrepareForTest({ RestClientUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class AssayValidationTest {
	@InjectMocks
	OrderServiceImpl orderServiceImpl;

	@Mock
	Invocation.Builder orderClient;
	@Mock 
	Invocation.Builder orderClientSample;
	
	@Mock 
	Invocation.Builder orderClientAssayList;
	@Mock 
	Invocation.Builder orderClienttestOptions;
	@Mock 
	Invocation.Builder orderClientinputDataValidations ;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	 OrderParentDTO orderParent = getOrderParentDTO();
	 List<AssayTypeDTO> assayTypelist = new ArrayList<>();
	 List<SampleTypeDTO> sampleTypelist=new ArrayList<SampleTypeDTO>();
	 List<AssayListDataDTO> assayListData=new ArrayList<AssayListDataDTO>();
	 List<TestOptionsDTO> testOptionlist = new ArrayList<TestOptionsDTO>();
	 List<AssayInputDataValidationsDTO> inputDataValidationslist = new ArrayList<AssayInputDataValidationsDTO>();
	 String assayType = "NIPTDPCR";

	@BeforeTest
	public void setUp() throws HMTPException, UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);		
	}
	
	@ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

	@Test
	public void assayValidationsTest() throws HMTPException, UnsupportedEncodingException {
		String urlassayType = "http://localhost";
		String urlSampleType="http://localhost";
		String urlassayList= "http://localhost";
		String urltestOptions="http://localhost";
		String urlinputDataValidations="http://localhost";
		
		String UTF = "UTF-8";
		String api = "pas.amm_api_url";
		String apiPath = "/json/rest/api/v1/assay/";
		assayTypelist.add(getAssayTypeDTOs());
		sampleTypelist.add(getSampleTypeDTOs());
		assayListData.add(getAssayListDataDTOs());
		testOptionlist.add(getTestOptionsDTOs());
		inputDataValidationslist.add(getAssayInputDataValidationsDTOs());
		 MockitoAnnotations.initMocks(this);
	        PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString());
		Mockito.when(RestClientUtil.getUrlString(api, "",
				apiPath + "?assayType=" + orderParent.getOrder().getAssayType(), "", null)).thenReturn(urlassayType);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(urlassayType, UTF), null)).thenReturn(orderClient);
		Mockito.when(orderClient.get(new GenericType<List<AssayTypeDTO>>() {
		})).thenReturn(assayTypelist);
		
		Mockito.when(RestClientUtil.getUrlString(api,"",apiPath+orderParent.getOrder().getAssayType()+"/sampletype", "", null)).thenReturn(urlSampleType);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(urlSampleType, UTF), null)).thenReturn(orderClientSample);
		Mockito.when(orderClientSample.get(new GenericType<List<SampleTypeDTO>>() {})).thenReturn(sampleTypelist);
		
		Mockito.when(RestClientUtil.getUrlString(api,"",apiPath+orderParent.getOrder().getAssayType()+"/listdata", "", null)).thenReturn(urlassayList);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(urlassayList, UTF), null)).thenReturn(orderClientAssayList);
		Mockito.when(orderClientAssayList.get(new GenericType<List<AssayListDataDTO>>() { })).thenReturn(assayListData);
		
	
		Mockito.when(RestClientUtil.getUrlString(api,"",apiPath+orderParent.getOrder().getAssayType()+"/testoptions", "", null)).thenReturn(urltestOptions);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(urltestOptions, UTF), null)).thenReturn(orderClienttestOptions);
		Mockito.when(orderClienttestOptions.get(new GenericType<List<TestOptionsDTO>>() {})).thenReturn(testOptionlist);
		
		Mockito.when(RestClientUtil.getUrlString(api,"",apiPath+orderParent.getOrder().getAssayType()+"/inputdatavalidations", "", null)).thenReturn(urlinputDataValidations);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(urlinputDataValidations,UTF), null)).thenReturn(orderClientinputDataValidations);
		Mockito.when(orderClientinputDataValidations.get(new GenericType<List<AssayInputDataValidationsDTO>>() { })).thenReturn(inputDataValidationslist);
		orderServiceImpl.assayValidations(orderParent);
		//orderServiceImpl.assayValidations(orderParent);
	}

	public OrderParentDTO getOrderParentDTO() {
		OrderParentDTO order = new OrderParentDTO();
		order.setOrder(getOrderDTO());
		return order;
	}

	public OrderDTO getOrderDTO() {
		OrderDTO o = new OrderDTO();
		o.setAccessioningId("8001");
		o.setAssayType(assayType);
		return o;
	}

	  public AssayTypeDTO getAssayTypeDTOs(){
	    	AssayTypeDTO a = new AssayTypeDTO();
	        a.setAssayType(assayType);
	    	return a;
	    }
	  
	  public SampleTypeDTO getSampleTypeDTOs(){
		   SampleTypeDTO s = new SampleTypeDTO();
		   s.setAssayType(assayType);
		   return s;
	   } 
	  
	  public AssayListDataDTO getAssayListDataDTOs(){
		   AssayListDataDTO al = new AssayListDataDTO();
		   al.setAssayType(assayType);
		   return al;
	   }
	   
	   public TestOptionsDTO getTestOptionsDTOs(){
		   TestOptionsDTO to = new TestOptionsDTO();
		   to.setAssayType(assayType);
		   return to;
	   }
	   
	   public AssayInputDataValidationsDTO getAssayInputDataValidationsDTOs(){
		   AssayInputDataValidationsDTO adv = new AssayInputDataValidationsDTO();
		   adv.setAssayType(assayType);
		   return adv;
		   
	   }
}

