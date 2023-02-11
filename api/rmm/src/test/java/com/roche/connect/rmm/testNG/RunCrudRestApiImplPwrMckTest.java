package com.roche.connect.rmm.testNG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.RunResultsDetail;
import com.roche.connect.rmm.model.SampleProtocol;
import com.roche.connect.rmm.model.SampleReagentsAndConsumables;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.model.SampleResultsDetail;
import com.roche.connect.rmm.readrepository.RunResultsReadRepository;
import com.roche.connect.rmm.readrepository.SampleResultsReadRepository;
import com.roche.connect.rmm.rest.RunCrudRestApiImpl;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.util.MapDTOToEntity;
import com.roche.connect.rmm.util.MapEntityToDTO;
import com.roche.connect.rmm.writerepository.RunResultsWriteRepository;
import com.roche.connect.rmm.writerepository.SampleProtocolWriteRepository;
import com.roche.connect.rmm.writerepository.SampleReagentsAndConsumablesWriteRespository;
import com.roche.connect.rmm.writerepository.SampleResultsDetailWriteRepository;
import com.roche.connect.rmm.writerepository.SampleResultsWriteRepository;

@PrepareForTest({ HMTPLoggerImpl.class, ThreadSessionManager.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class RunCrudRestApiImplPwrMckTest {
	
	@InjectMocks
	RunCrudRestApiImpl runCrudRestApiImpl;
	
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	RunResultsReadRepository runResultsReadRepository;

	@Mock
	MapEntityToDTO mapEntityToDTO;
	@Mock
	UserSession userSession;
	
	@Mock  SampleResultsReadRepository sampleResultsReadRepository;
	@Mock MapDTOToEntity mapDTOToEntity;
	@Mock RunResultsWriteRepository runResultsWriteRepository;
	@Mock SampleResultsWriteRepository sampleResultsWriteRepository;
	@Mock SampleReagentsAndConsumablesWriteRespository sampleReagentsAndConsumablesWriteRespository;
	@Mock SampleResultsDetailWriteRepository sampleResultsDetailWriteRepository;
	@Mock  SampleProtocolWriteRepository sampleProtocolWriteRepository;
	ObjectMapper objectMapper = new ObjectMapper();

	List<RunResultsDetailDTO> runresultdetaildto = new ArrayList<>();
	List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTO = new ArrayList<>();
	List<Object> inWorkFlowOrders = new ArrayList<>();
	 List<RunResults> listRunResults = new ArrayList<>(); 
	 List<SampleResults> sampleResultsList = new ArrayList<>();
	 List<SampleReagentsAndConsumables> sampleReagentsAndConsumables= new ArrayList<>() ;
	 List<SampleResultsDetail> sampleResultsDetail= new ArrayList<>();
	 List<SampleProtocol> sampleProtocol= new ArrayList<>();
	 SampleReagentsAndConsumables sampleReagentsAndConsumable;
	 SampleResultsDetail sampleResDetail;
	 SampleProtocol sampleProto;

	public static final String jsonRunResultentity = "src/test/java/resource/RunResultEntity.json";

	@BeforeTest
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		runresultdetaildto.add(getRunResultsDetailDTO());
		runReagentsAndConsumablesDTO.add(getRunReagentsAndConsumablesDTO());
		runReagentsAndConsumablesDTO.add(getRunReagentsAndConsumablesDTO2());
		Object[] obj1 = {"8001","Comments",102,"workflowType","NIPTHTP","Plasma","Ongoing","true",new Date()};
		inWorkFlowOrders.add(obj1);
		
		Mockito.when(sampleResultsReadRepository.getInWorkFlowOrderDetails(Mockito.anyString())).thenReturn(inWorkFlowOrders);
		Mockito.when(runResultsReadRepository.findCurrentRunResult(Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(listRunResults);
		
		Mockito.when(sampleResultsReadRepository.getInWorkFlowOrders()).thenReturn(inWorkFlowOrders);
		
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(sampleResultsReadRepository.findByAccesssioningIdAndInputContainerId(Mockito.anyString(),  Mockito.anyString(),  Mockito.anyLong())).thenReturn(sampleResultsList );
        Mockito.when(sampleResultsReadRepository
            .findByAccesssioningIdAndInputContainerIdAndInputContainerPosition(
                Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(),  Mockito.anyLong())).thenReturn(sampleResultsList);
		Mockito.when( runResultsReadRepository.findOne(Mockito.anyLong())).thenReturn(getRunresult());
		Mockito.when(mapDTOToEntity.getSampleResultsFromDTO(Mockito.any(SampleResultsDTO.class))).thenReturn(getSampleresults());
        Mockito.when(mapDTOToEntity
                    .getSampleReagentsAndConsumablesFromDTO(Mockito.any(SampleResultsDTO.class))).thenReturn(sampleReagentsAndConsumables);
        Mockito.when(mapDTOToEntity
                    .getSampleResultsDetailFromDTO(Mockito.any(SampleResultsDTO.class))).thenReturn(sampleResultsDetail);
        Mockito.when(mapDTOToEntity.getSampleProtocolFromDTO(Mockito.any(SampleResultsDTO.class))).thenReturn(sampleProtocol);
        Mockito.when(runResultsWriteRepository.save(Mockito.any(RunResults.class))).thenReturn(getRunresult());
        Mockito.when(sampleResultsWriteRepository.save(Mockito.any(SampleResults.class))).thenReturn(getSampleresults());
        Mockito.when( sampleReagentsAndConsumablesWriteRespository.save(Mockito.any(SampleReagentsAndConsumables.class))).thenReturn(sampleReagentsAndConsumable);
        Mockito.when(sampleResultsDetailWriteRepository.save(Mockito.any(SampleResultsDetail.class))).thenReturn(sampleResDetail);
        Mockito.when( sampleProtocolWriteRepository.save(Mockito.any(SampleProtocol.class))).thenReturn(sampleProto);
        
        Mockito.when(runResultsReadRepository.findByDeviceIdAndRunStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(listRunResults);
	}
	

	private SampleResults getSampleresults() {
	    SampleResults sampleResults = new SampleResults();
        return sampleResults;
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

	@Test
	public void getAllDetailsByRunResultsIdTest()
			throws HMTPException, JsonParseException, JsonMappingException, IOException {
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
		Mockito.when(runResultsReadRepository.findRunResultByRunResultId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(getRunresult());
		Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class)))
				.thenReturn(getRunresultsDTO());
		Mockito.when(mapEntityToDTO.convertRunResultsDetailToRunResultsDetailDTODTO(
				(List<RunResultsDetail>) getRunresult().getRunResultsDetail())).thenReturn(runresultdetaildto);

		Mockito.when(mapEntityToDTO.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(Mockito.anyList()))
				.thenReturn(runReagentsAndConsumablesDTO);
		Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(Mockito.any(SampleResults.class)))
				.thenReturn(getSampleResultsDTO());
		runCrudRestApiImpl.getAllDetailsByRunResultsId("3l");
	}

	private RunResults getRunresult() throws JsonParseException, JsonMappingException, IOException {
		String runresultentitystring = JsonFileReaderAsString.getJsonfromFile(jsonRunResultentity);
		RunResults results = objectMapper.readValue(runresultentitystring, RunResults.class);
		return results;
	}

	public SampleResultsDTO getSampleResultsDTO() {
		SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
		Collection<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumables = new ArrayList<>();
		sampleReagentsAndConsumables.add(getSampleReagentsAndConsumablesDTO());
		sampleReagentsAndConsumables.add(getSampleReagentsAndConsumablesDTO2());
		sampleResultsDTO.setSampleReagentsAndConsumables(sampleReagentsAndConsumables);
		sampleResultsDTO.setOutputContainerId("1");
		return sampleResultsDTO;
	}

	public SampleReagentsAndConsumablesDTO getSampleReagentsAndConsumablesDTO() {
		SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
		sampleReagentsAndConsumablesDTO.setAttributeName("attributeName");
		sampleReagentsAndConsumablesDTO.setAttributeValue("attributeValue");

		return sampleReagentsAndConsumablesDTO;
	}

	public SampleReagentsAndConsumablesDTO getSampleReagentsAndConsumablesDTO2() {
		SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
		return sampleReagentsAndConsumablesDTO;
	}

	public RunResultsDTO getRunresultsDTO() {
		RunResultsDTO runresultsDTO = new RunResultsDTO();
		return runresultsDTO;
	}

	public RunReagentsAndConsumablesDTO getRunReagentsAndConsumablesDTO() {
		RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
		runReagentsAndConsumablesDTO.setAttributeName("attributeName");
		runReagentsAndConsumablesDTO.setAttributeValue("attributeValue");
		return runReagentsAndConsumablesDTO;
	}

	public RunReagentsAndConsumablesDTO getRunReagentsAndConsumablesDTO2() {
		RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
		runReagentsAndConsumablesDTO.setAttributeName("attributeName");
		runReagentsAndConsumablesDTO.setAttributeValue("attributeValue");
		return runReagentsAndConsumablesDTO;
	}

	public RunResultsDetailDTO getRunResultsDetailDTO() {
		RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
		return runResultsDetailDTO;
	}
	
	@Test
	public void getCurrentRunResultTest() {
	    runCrudRestApiImpl.getCurrentRunResult("2018-09-09", "2019-02-08");
	}
	
	@Test
	public void getRunDetailsByAccessioningIdTest() throws HMTPException {
	    String accessioningId ="8001";
        runCrudRestApiImpl.getRunDetailsByAccessioningId(accessioningId );
	}
	
	@Test
    public void getInWorkFlowOrdersTest() throws HMTPException {
        runCrudRestApiImpl.getInWorkFlowOrders();
    }
	
	
	@Test
	public void postSampleResultsIfConditionTest() throws HMTPException {
	    runCrudRestApiImpl.postSampleResults(sampleResultsDTOIfPart());
	}
	@Test
    public void postSampleResultsElseConditionTest() throws HMTPException {
        runCrudRestApiImpl.postSampleResults(sampleResultsDTOElsePart());
    }
	
	public SampleResultsDTO sampleResultsDTOIfPart() {
	    SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setRunResultsId(10L);
        return sampleResultsDTO;
	}
	
	public SampleResultsDTO sampleResultsDTOElsePart() {
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setRunResultsId(10L);
        sampleResultsDTO.setInputContainerPosition("2");
        return sampleResultsDTO;
    }
    
	   
    @Test
    public void getlistOfSampleDtoNAExtractionTest() {
        Optional<RunResultsDTO> runResultDto = Optional.of(getRunResultsDTONAExtraction());
        runCrudRestApiImpl.getlistOfSampleDto(runResultDto );
    }
	
   public  RunResultsDTO getRunResultsDTONAExtraction() {
       RunResultsDTO runResultsDTO = new RunResultsDTO();
       runResultsDTO.setProcessStepName("NA Extraction");
       Collection<SampleResultsDTO> sampleResults = new ArrayList<>();
       sampleResults.add(getSampleResultsDTO());
    runResultsDTO.setSampleResults(sampleResults );
    return runResultsDTO;
    }
   
   
   @Test
   public void getlistOfSampleDtoLibraryPreparationTest() {
       Optional<RunResultsDTO> runResultDto = Optional.of(getRunResultsDTOLibraryPreparation());
       runCrudRestApiImpl.getlistOfSampleDto(runResultDto );
   }
   public  RunResultsDTO getRunResultsDTOLibraryPreparation() {
       RunResultsDTO runResultsDTO = new RunResultsDTO();
       runResultsDTO.setProcessStepName("Library Preparation");
       Collection<SampleResultsDTO> sampleResults = new ArrayList<>();
       sampleResults.add(getSampleResultsDTO());
    runResultsDTO.setSampleResults(sampleResults );
    return runResultsDTO;
    }
   
   @Test
   public void getlistOfSampleDtoDPCRTest() {
       Optional<RunResultsDTO> runResultDto = Optional.of(getRunResultsDTODPCR());
       runCrudRestApiImpl.getlistOfSampleDto(runResultDto );
   }
   
   public  RunResultsDTO getRunResultsDTODPCR() {
       RunResultsDTO runResultsDTO = new RunResultsDTO();
       runResultsDTO.setProcessStepName("dPCR");
       Collection<SampleResultsDTO> sampleResults = new ArrayList<>();
       sampleResults.add(getSampleResultsDTO());
    //runResultsDTO.setSampleResults(sampleResults );
    return runResultsDTO;
    }
   
   @Test
   public void getlistOfSampleDtoDPCRElsePartTest() {
       Optional<RunResultsDTO> runResultDto = Optional.of(getRunResultsDTODPCRElse());
       runCrudRestApiImpl.getlistOfSampleDto(runResultDto );
   }
   
   public  RunResultsDTO getRunResultsDTODPCRElse() {
       RunResultsDTO runResultsDTO = new RunResultsDTO();
       runResultsDTO.setProcessStepName("dPCR");
       Collection<SampleResultsDTO> sampleResults = new ArrayList<>();
       sampleResults.add(getSampleResultsDTOGetlistOfSampleDto());
    runResultsDTO.setSampleResults(sampleResults );
    return runResultsDTO;
    }
   
   public SampleResultsDTO getSampleResultsDTOGetlistOfSampleDto() {
       SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
       Collection<SampleResultsDetailDTO> sampleCollection = new ArrayList<>();
       sampleCollection.add(getSampleResultsDetailDTOQuantitative());
       sampleCollection.add(getSampleResultsDetailDTOQualitative());
    sampleResultsDTO.setSampleResultsDetail(sampleCollection );
       return sampleResultsDTO;
   }
   
   public SampleResultsDetailDTO getSampleResultsDetailDTOQuantitative() {
       SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
       sampleResultsDetailDTO.setAttributeName("quantitativeValue");
       sampleResultsDetailDTO.setAttributeValue("quantitative");
    return sampleResultsDetailDTO;
   }
   
   public SampleResultsDetailDTO getSampleResultsDetailDTOQualitative() {
       SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
       sampleResultsDetailDTO.setAttributeName("qualitativeValue");
       sampleResultsDetailDTO.setAttributeValue("qualitative");
    return sampleResultsDetailDTO;
   }
   
 
   
   @Test
   public void findInprogressStatusByDeviceIDNegativeTest() throws HMTPException {
       runCrudRestApiImpl.findInprogressStatusByDeviceID("1234", "Completed");
   }
}
