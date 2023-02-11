package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.readrepository.ContainerSamplesReadRepository;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;
import com.roche.connect.omm.util.CSVParserUtil;
import com.roche.connect.omm.util.OMMConstant;
import com.roche.connect.omm.util.ResourceBundleUtil;

/**
 * @author imtiyazm This class is for Mock test CSV file validation API
 */
@PrepareForTest(ThreadSessionManager.class)
@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class CsvValidateTest {
	@Mock
	ContainerSamplesReadRepository containerSamplesReadRepository=org.mockito.Mockito.mock(ContainerSamplesReadRepository.class);
	
	@Mock
	OrderReadRepository orderReadRepository=org.mockito.Mockito.mock(OrderReadRepository.class);
	
	@Mock
	CSVParserUtil csvParserUtil;
	@Mock
	ResourceBundleUtil resourceBundleUtil;
	@InjectMocks
	@Spy
	OrderService orderServiceImpl = org.mockito.Mockito.spy(new OrderServiceImpl());
	
	

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();
	@Mock
	 HMTPLoggerImpl hmtpLoggerImpl;
	/*ContainerSamplesDTO containerSamplesDTO = null;
	ContainerSamplesDTO containerSamplesDTOnext = null;
	List<ContainerSamplesDTO> containerSamplesDTOList = null;
	ContainerSamples containerSamples = null;*/
	int expectedCorrectStatusCode = 0;
	int expectedIncorrectStatusCode = 0;
	String csvFilePath = null;
	List<String> parsedCSV = null;
	InputStream stream = null;
	File csvFile = null;
	
	List<Map<Object, Object>> containerSample = null;
	List<Map<Object, Object>> containerSampleList1= null;
	List<Map<Object, Object>> containerSampleList2= null;
	

	List<Order> orderaccessioningId=null;
	Order order=null;

	
	@BeforeTest
	public void setUp() throws Exception {


		csvFilePath = "src/test/java/resource/SampleWrongcsv.csv";
		csvFile = new File(csvFilePath);
		stream = new FileInputStream(csvFile);
		
		containerSample=new ArrayList<>();
		containerSampleList1=new ArrayList<>();
		containerSampleList2=new ArrayList<>();
		order=new Order();
		order.setAccessioningId("300001");
		orderaccessioningId=new ArrayList<>();
		Map<Object, Object> map=new HashMap<>();
		map.put("containerId", "WPI0");
		orderaccessioningId.add(order);
		containerSample.add(map);
		containerSampleList1.add(map);
	    Map<Object, Object> map1=new HashMap<>();
	    map1.put("Position","A1");
	    containerSampleList2.add(map1);

		expectedCorrectStatusCode = 200;
		expectedIncorrectStatusCode = 400;
	
		MockitoAnnotations.initMocks(this);
		/*Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		Mockito.when(containerSamplesReadRepository.findSampleByAccessioningId(any(String.class), any(long.class))).thenReturn(containerSample);		
		Mockito.when(orderReadRepository.findAllByAccessioningId(any(String.class), any(long.class))).thenReturn(orderaccessioningId);	*/
	}
	
	  /**
     * We need a special {@link IObjectFactory}.
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
	@Test(priority = 1)
	public void validateCSVPosTest() throws Exception {
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		Mockito.when(containerSamplesReadRepository.findSampleByAccessioningId(any(String.class), any(long.class))).thenReturn(containerSample);		
		Mockito.when(orderReadRepository.findAllByAccessioningId(any(String.class), any(long.class))).thenReturn(orderaccessioningId);
		Mockito.when(csvParserUtil.parseCSV(Mockito.any(InputStream.class))).thenReturn(getParserString());
		Response actualResponseCode = orderCrudRestApiImpl.validateContainerSamples(stream);
		assertEquals(actualResponseCode.getStatus(), expectedIncorrectStatusCode);
	}
	
	@Test(expectedExceptions = Exception.class)
    public void parseCSVTest() throws HMTPException {
        CSVParserUtil csvParserUtil=new CSVParserUtil();
        csvParserUtil.parseCSV(null);
    }
	//@SuppressWarnings("unchecked")
	@Test
    public void validateCSVTest() throws HMTPException, IOException  {
		  		String csv="Container Type,Container ID,Position" ;        
		        InputStream inputStream=new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
		        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		       // Mockito.when(containerSamplesReadRepository.findSampleByContaineridAndPosition(any(String.class), any(String.class), any(long.class))).thenReturn(containerSampleList1,containerSampleList2);
				Mockito.when(containerSamplesReadRepository.findSampleByAccessioningId(any(String.class), any(long.class))).thenReturn(containerSample);		
				Mockito.when(orderReadRepository.findAllByAccessioningId(any(String.class), any(long.class))).thenReturn(orderaccessioningId);	
				Mockito.when(csvParserUtil.parseCSV(Mockito.any(InputStream.class))).thenReturn(getParserString());
		        Response actualResponseCode = orderCrudRestApiImpl.validateContainerSamples(inputStream);
		        assertEquals(actualResponseCode.getStatus(), expectedIncorrectStatusCode);
    }
	private List<String> getParserString(){
	    return  Arrays.asList("Container Type,Container ID,Position,Accessioning ID","96 wellplate,WP 810,C9,4444","96 wellplate,WP 810,C9,300001","96 wellplate,WP 810,C9,300001","96 wellplate,WP 810,C9,12345678912345671","96 wellplate,WP 812,C9,444_-12","96 wellplate,WP 811,C10,_-","96 wellplate,WP 811,C10,@31_-","96 wellplate,WP 811,","96 ,WP 811,C10,21212",",,,","96 wellplate,,H100,111",",","96 wellplate,WP 811,C10",",,,4444","96 wellplate","96 ,`,C90,4444","96 wellplate,WPI0,A1,30001","96 wellplate,WP 810,C9,30000199977887677643431"); 
	    }
	@AfterTest
	public void afterTest() {
		expectedCorrectStatusCode = 0;
		csvFile = null;
	}
	String[] row ={"1","2","3",null}; 
	String[] row1 ={"1","2","3","aa"}; 
    Map<Integer, String> accessioningIdProcessed = new HashMap<>();
    Map<Integer, String> containerIdPositionProcessed= new HashMap<>();
    @InjectMocks OrderServiceImpl orderServiceImplement;
    @Mock UserSession userSession;
   
    @Test
    public void checkFieldsForCSVStreamTest(){
        Mockito.when(resourceBundleUtil.getMessages( Mockito.any(ResourceBundle.class), Mockito.anyString())).thenReturn("error");
        orderServiceImplement.checkFieldsForCSVStream(1, row, accessioningIdProcessed, containerIdPositionProcessed);
    }   
   
    @Test
    public void checkFieldsForCSVStreamIsOrderTest(){
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        List<Order> orderList = new ArrayList<>();
        Mockito.when(orderReadRepository.findAllByAccessioningId(Mockito.anyString(), Mockito.anyLong())).thenReturn(orderList);
        Mockito.when(resourceBundleUtil.getMessages( Mockito.any(ResourceBundle.class), Mockito.anyString(),
                            Mockito.any(Object.class))).thenReturn("errorTest");
        orderServiceImplement.checkFieldsForCSVStream(1, row1, accessioningIdProcessed, containerIdPositionProcessed);
    } 
    
    @Test
    public void checksCsvAccessioningIdProcessedTest(){
        List<Order> orderList = new ArrayList<>();
        Order order=new Order();
        order.setAccessioningId("12");
        order.setAssayType("NIPTDPCR");
        orderList.add(order);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);    
        Mockito.when(orderReadRepository.findAllByAccessioningId(Mockito.anyString(), Mockito.anyLong())).thenReturn(orderList);
        Mockito.when(resourceBundleUtil.getMessages( Mockito.any(ResourceBundle.class), Mockito.anyString(),
                            Mockito.any(Object.class))).thenReturn("errorTest");
        orderServiceImplement.checkFieldsForCSVStream(1, row1, accessioningIdProcessed, containerIdPositionProcessed);
    } 
    @Test
    public void checkCsvIsAssayTypeValidTest(){
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        List<Order> orderList = new ArrayList<>();
        Order order=new Order();
        order.setAssayType("nipthtp");
        order.setAccessioningId("12%");
        orderList.add(order);
        Mockito.when(orderReadRepository.findAllByAccessioningId(Mockito.anyString(), Mockito.anyLong())).thenReturn(orderList);
        Mockito.when(resourceBundleUtil.getMessages( Mockito.any(ResourceBundle.class), Mockito.anyString(),
                            Mockito.any(Object.class))).thenReturn("errorTest");
        orderServiceImplement.checkFieldsForCSVStream(1, row1, accessioningIdProcessed, containerIdPositionProcessed);
    } 
    @Test
    public void checkCsvAccessioningIdProcessedTest(){
        Map<Integer, String> accessioningIdProcessed = new HashMap<>();
        String[] row ={"1","2","3","aa"};
        accessioningIdProcessed.put(2, "aa");
        List<Order> orderList = new ArrayList<>();
        Order order=new Order();
        order.setAccessioningId("aa");
        orderList.add(order);
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);  
        Mockito.when(orderReadRepository.findAllByAccessioningId(Mockito.anyString(), Mockito.anyLong())).thenReturn(orderList);
        Mockito.when(resourceBundleUtil.getMessages( Mockito.any(ResourceBundle.class), Mockito.anyString(),Mockito.any(Object.class))).thenReturn("errorTest");
        orderServiceImplement.checkFieldsForCSVStream(1, row, accessioningIdProcessed, containerIdPositionProcessed);
    } 
    

}
