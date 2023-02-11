package com.roche.connect.omm.test;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.BulkOrdersDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.common.order.dto.WorkflowDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.readrepository.PatientSampleReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.writerepository.OrderWriteRepository;
import com.roche.connect.omm.writerepository.PatientAssayWriteRepository;
import com.roche.connect.omm.writerepository.PatientSampleWriteRepository;
import com.roche.connect.omm.writerepository.PatientWriteRepository;

@PrepareForTest({ RestClientUtil.class, HMTPLoggerImpl.class ,ThreadSessionManager.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
        "org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class OrderCrudRestApiImplWorkflowOrdersTest {

    @InjectMocks
    OrderCrudRestApiImpl orderCrudRestApiImpl;
    @Mock
    HMTPLoggerImpl hmtpLoggerImpl;
    @Mock
    InputStream inputStream;
    @Mock
    PatientSampleWriteRepository patientSampleWriteRepository;
    @Mock
    PatientAssayWriteRepository patientAssayWriteRepository;
    @Mock
    OrderWriteRepository orderWriteRepository;
    @Mock
    PatientWriteRepository patientWriteRepository;
    
    @Mock UserSession userSession;
    
    @Mock OrderReadRepository orderReadRepository;
    @Mock PatientSampleReadRepository patientSampleReadRepository;
    
    
    List<WorkflowDTO> workflowOrders = new ArrayList<>();
    List<SampleResultsDTO> sampleResults = new ArrayList<>();
    
    List<PatientSamples> patientSamplesList = new ArrayList<>();
    List<Order> orders = new ArrayList<>();
    List<PatientAssay> patientAssays = new ArrayList<>();
    Patient patient = new Patient();
    @Mock File file ;
    @Mock OrderService orderService;
    @BeforeTest
        public void setUp() throws Exception {
            MockitoAnnotations.initMocks(this);
            Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
            PowerMockito.mockStatic(ThreadSessionManager.class);
            Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
            Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
            orders.add(getOrder());
            Mockito.when(orderReadRepository.findOrderDetailsByAccessioningId(Mockito.anyString())).thenReturn(orders);
            patientSamplesList.add(getPatientSamples());
            Mockito.when(patientSampleReadRepository.findPatientDetailsBySampleId(Mockito.anyString())).thenReturn(patientSamplesList);
            Mockito.when(orderService.createOrderDTO(Mockito.any(Order.class))).thenReturn(getOrderDTO());
            Mockito.when(orderService.createAssayDTO(Mockito.any(Order.class))).thenReturn(getAssayDTO());
            Map<String, Boolean> testOptions = new HashMap<>();
            Mockito.when(orderService.createTestOptionsDTO(Mockito.any(Order.class))).thenReturn(testOptions);
            Mockito.when(orderService.createPatientDTO(Mockito.any(Order.class))).thenReturn(getPatientDTO());
            
            workflowOrders.add(getWorkflowDTO());
            Mockito.when(orderService.getInWorkflowOrders()).thenReturn(workflowOrders);
    }

    /**
     * We need a special {@link IObjectFactory}.
     * 
     * @return {@link PowerMockObjectFactory}.
     */
    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }


    
    public WorkflowDTO getWorkflowDTO() {
        WorkflowDTO workflowDTO = new WorkflowDTO();
        workflowDTO.setAccessioningId("8001");
        workflowDTO.setAssaytype("NIPTHTP");
        return workflowDTO;
    }
    
    @Test
    public void getInworkflowOrdersTest() throws UnsupportedEncodingException, HMTPException {
        orderCrudRestApiImpl.getInworkflowOrders();
    }
    @Test
    public void getMandatoryFlagByRunTest() {
        orderCrudRestApiImpl.getMandatoryFlagByRun(sampleResults);
    }
     public Order getOrder() {
         Order order = new Order();
         order.setAccessioningId("8001");
         order.setAssayType("NIPTHTP");
        return order;
         
     }
     
     public PatientSamples getPatientSamples() {
         PatientSamples patientSamples = new PatientSamples();
         patientSamples.setCreatedBy("admin");
         patientSamples.setCreatedABy("patient");
        return patientSamples;
     }
     
     public OrderDTO getOrderDTO() {
         OrderDTO orderDTO = new OrderDTO();
         orderDTO.setReqFieldMissingFlag(true);
        return orderDTO;
     }
     
     public AssayDTO getAssayDTO() {
         AssayDTO assayDTO = new AssayDTO();
         return assayDTO;
     }
     
     public PatientDTO getPatientDTO(){
         PatientDTO patientDTO = new PatientDTO();
        return patientDTO;
     }
     
     public SampleResultsDTO getSampleResultsDTO() {
         SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
         sampleResultsDTO.setAccesssioningId("8001");
         sampleResultsDTO.setAssayType("NIPTHTP");
        return sampleResultsDTO;
         
     }
}
