package com.roche.connect.rmm.testNG;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.rmm.dto.SampleProtocolDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.rmm.model.SampleProtocol;
import com.roche.connect.rmm.model.SampleReagentsAndConsumables;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.model.SampleResultsDetail;
import com.roche.connect.rmm.readrepository.RunResultsReadRepository;
import com.roche.connect.rmm.rest.RunCrudRestApiImpl;
import com.roche.connect.rmm.writerepository.RunResultsWriteRepository;
import com.roche.connect.rmm.writerepository.SampleProtocolWriteRepository;
import com.roche.connect.rmm.writerepository.SampleReagentsAndConsumablesWriteRespository;
import com.roche.connect.rmm.writerepository.SampleResultsDetailWriteRepository;
import com.roche.connect.rmm.writerepository.SampleResultsWriteRepository;

@PrepareForTest({ HMTPLoggerImpl.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
    "org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class PostSampleResultsByTest {
    
    @Mock
    HMTPLoggerImpl hmtpLoggerImpl;
    
    @Mock
    RunResultsReadRepository runResultsReadRepository ;

    @Mock
    RunResultsWriteRepository runResultsWriteRepository;
    
    @Mock
    SampleResultsWriteRepository sampleResultsWriteRepository;
    
    @Mock
    SampleProtocolWriteRepository sampleProtocolWriteRepository;
    
    @Mock
    SampleReagentsAndConsumablesWriteRespository sampleReagentsAndConsumablesWriteRespository;
    
    @Mock
    SampleResultsDetailWriteRepository sampleResultsDetailWriteRepository;

    @InjectMocks
    RunCrudRestApiImpl runCrudRestApiImpl;
    
    List<SampleProtocolDTO> sampleProtocol = new ArrayList<>();
    List<SampleReagentsAndConsumablesDTO> listSampleReagentsAndConsumables = new ArrayList<>();
    List<SampleReagentsAndConsumables> sampleReagentsAndConsumablesFromDB  = new ArrayList<>(); 
    List<SampleProtocol> sampleProtocolFromDB = new ArrayList<>();  
    List<SampleResultsDetail> newsampleResultsDetailList = new ArrayList<>();
    
    @BeforeTest
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        Mockito.when(sampleResultsWriteRepository.save(getSampleResult())).thenReturn(getSampleResult());
        sampleProtocol.add(getSampleProtocolDTO());
        sampleProtocolFromDB.add(getProtocol());
        Mockito.doNothing().when(sampleReagentsAndConsumablesWriteRespository).delete(sampleReagentsAndConsumablesFromDB);
        Mockito.when(sampleProtocolWriteRepository.save(sampleProtocolFromDB)).thenReturn(sampleProtocolFromDB);
        Mockito.when( sampleReagentsAndConsumablesWriteRespository.save(sampleReagentsAndConsumablesFromDB)).thenReturn(sampleReagentsAndConsumablesFromDB);
        Mockito.when(sampleResultsDetailWriteRepository.save(newsampleResultsDetailList)).thenReturn(newsampleResultsDetailList);
    }
    
    
    /** Mandatory to mock static classes **/
    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }
    
    public SampleResultsDetail getSampleResultsDetail() {
        SampleResultsDetail sampleResultsDetail = new SampleResultsDetail();
        sampleResultsDetail.setAttributeName("name");
        sampleResultsDetail.setAttributeValue("value");
        return sampleResultsDetail;
    }
    public SampleProtocol getProtocol() {
        SampleProtocol protocol = new SampleProtocol();
        protocol.setId(getSampleProtocolDTO().getSampleProtocolId());
        protocol.setProtocolName(getSampleProtocolDTO().getProtocolName());
        protocol.setSampleResultsMappedToSampleProtocol(getSampleResult());
        protocol.setUpdatedBy(getSampleResultsDTO().getUpdatedBy());
        protocol.setUpdatedDateTime(getSampleResultsDTO().getUpdatedDateTime());
        return protocol;
        
    }
    @Test
    public void updateSampleResultsTest() throws HMTPException{
        runCrudRestApiImpl.updateSampleResults(getSampleResultsDTO(), getSampleResult());
    }
    
    public SampleProtocolDTO getSampleProtocolDTO() {
        SampleProtocolDTO sample = new SampleProtocolDTO();
        sample.setSampleProtocolId(123L);
        sample.setProtocolName("ctDNA");
        return sample;
    }
    
    public  SampleReagentsAndConsumablesDTO getSampleReagentsAndConsumablesDTO(){
        SampleReagentsAndConsumablesDTO sampleReagentsAndConsumables = new SampleReagentsAndConsumablesDTO();
        sampleReagentsAndConsumables.setAttributeName("");
        sampleReagentsAndConsumables.setAttributeValue("");
        sampleReagentsAndConsumables.setCreatedBy("");
        sampleReagentsAndConsumables.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        sampleReagentsAndConsumables.setSampleReagentsAndConsumablesId(101L);
        sampleReagentsAndConsumables.setType("type");
        sampleReagentsAndConsumables.setUpdatedBy("admin");
        sampleReagentsAndConsumables.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        return sampleReagentsAndConsumables;
        
    }
    
    public SampleResultsDTO getSampleResultsDTO() {
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setAccesssioningId("8001");
        sampleResultsDTO.setOutputContainerId("12123123");
        sampleResultsDTO.setOutputContainerPosition("2343434");
        sampleResultsDTO.setStatus("ok");
        sampleResultsDTO.setUpdatedBy("admin");
        sampleResultsDTO.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
        sampleResultsDTO.setFlag("234234");
        sampleResultsDTO.setResult("234234");
        sampleResultsDTO.setOutputContainerType("ok234");
        sampleResultsDTO.setOutputPlateType("234234");
        sampleResultsDTO.setComments("234234");
        sampleResultsDTO.setCreatedBy("admin");
        sampleResultsDTO.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
        sampleResultsDTO.setSampleType("4234234");
        sampleResultsDTO.setSampleProtocol(sampleProtocol);
        sampleResultsDTO.setSampleReagentsAndConsumables(listSampleReagentsAndConsumables);
        return sampleResultsDTO;
    }
    public SampleResults getSampleResult() {
        SampleResults sampleResult = new SampleResults();
        sampleResult.setOutputContainerId(getSampleResultsDTO().getOutputContainerId());
        sampleResult.setOutputContainerPosition(getSampleResultsDTO().getOutputContainerPosition());
        sampleResult.setStatus(getSampleResultsDTO().getStatus());
        sampleResult.setUpdatedBy(getSampleResultsDTO().getUpdatedBy());
        sampleResult.setUpdatedDateTime(getSampleResultsDTO().getUpdatedDateTime());
        sampleResult.setFlag(getSampleResultsDTO().getFlag());
        sampleResult.setResult(getSampleResultsDTO().getResult());
        sampleResult.setOutputContainerType(getSampleResultsDTO().getOutputContainerType());
        sampleResult.setOutputPlateType(getSampleResultsDTO().getOutputPlateType());
        sampleResult.setComments(getSampleResultsDTO().getComments());
        sampleResult.setCreatedBy(getSampleResultsDTO().getCreatedBy());
        sampleResult.setCreatedDate(getSampleResultsDTO().getCreatedDateTime());
        sampleResult.setSampleType(getSampleResultsDTO().getSampleType());
        return sampleResult;
        
    }

}
