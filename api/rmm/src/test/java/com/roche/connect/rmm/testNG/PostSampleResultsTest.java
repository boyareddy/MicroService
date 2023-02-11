package com.roche.connect.rmm.testNG;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.common.order.dto.WorkflowDTO;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleProtocolDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.rmm.ApplicationBootRmm;
import com.roche.connect.rmm.error.ErrorResponse;
import com.roche.connect.rmm.model.RunReagentsAndConsumables;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.RunResultsDetail;
import com.roche.connect.rmm.model.SampleProtocol;
import com.roche.connect.rmm.model.SampleReagentsAndConsumables;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.model.SampleResultsDetail;
import com.roche.connect.rmm.readrepository.RunResultsReadRepository;
import com.roche.connect.rmm.readrepository.SampleResultsReadRepository;
import com.roche.connect.rmm.rest.RunCrudRestApiImpl;
import com.roche.connect.rmm.specifications.RunResultsSpecifications;
import com.roche.connect.rmm.specifications.RunResultsTypeSpecification;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.util.MapDTOToEntity;
import com.roche.connect.rmm.util.MapEntityToDTO;
import com.roche.connect.rmm.util.RMMConstant;
import com.roche.connect.rmm.writerepository.RunReagentsAndConsumablesWriteRepository;
import com.roche.connect.rmm.writerepository.RunResultsDetailWriteRepository;
import com.roche.connect.rmm.writerepository.RunResultsWriteRepository;
import com.roche.connect.rmm.writerepository.SampleProtocolWriteRepository;
import com.roche.connect.rmm.writerepository.SampleReagentsAndConsumablesWriteRespository;
import com.roche.connect.rmm.writerepository.SampleResultsDetailWriteRepository;
import com.roche.connect.rmm.writerepository.SampleResultsWriteRepository;

@PrepareForTest({ HMTPLoggerImpl.class, ThreadSessionManager.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
        "org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class PostSampleResultsTest {
	@InjectMocks
	RunCrudRestApiImpl runCrudRestApiImpl ;
	@Mock
	MapEntityToDTO mapEntityToDTO;
	@Mock
	MapDTOToEntity mapDTOToEntity;
	@Mock
	RunResultsReadRepository runResultsReadRepository;
	@Mock
	RunResultsWriteRepository runResultsWriteRepository;
	@Mock
	RunResultsDetailWriteRepository runResultsDetailWriteRepository;
	@Mock
	RunReagentsAndConsumablesWriteRepository runReagentsAndConsumablesWriteRepository;
	@Mock
	SampleResultsReadRepository sampleResultsReadRepository;
	@Mock
	SampleResultsWriteRepository sampleResultsWriteRepository;
	@Mock
	SampleResultsDetailWriteRepository sampleResultsDetailWriteRepository;
	@Mock
	SampleProtocolWriteRepository sampleProtocolWriteRepository;
	@Mock
	Sort runResultsTypeSpecification;
	@Mock
	RunResultsSpecifications unResultsSpecifications;
	@Mock
	SampleReagentsAndConsumablesWriteRespository sampleReagentsAndConsumablesWriteRespository;
	@Mock
	WorkflowDTO workflowDTO;
	
	@Mock
    HMTPLoggerImpl hmtpLoggerImpl;

	
	List<Object> inWorkFlowOrders = new ArrayList<>();
	@Mock Object obj1;
	@Mock Object obj2;
	@Mock Object obj3;
	@Mock Object obj4;
	@Mock Object obj5;
	@Mock Object obj6;
	@Mock Object obj7;
	
	String sampleresultDTO;
	String sampleresultEntity;
	SampleResultsDTO sampleResultsDTO;
	SampleResultsDTO updateSampleResultsDTO;
	SampleResults sampleResults;
	String runresultjsonString;
	String updaterunresultstring;
	RunResults runResults;
	RunResultsDTO runResultsDTO;
	RunResultsDTO updateRunresultDTO;
	ObjectMapper objectMapper;
	List<RunReagentsAndConsumablesDTO> listrunReagentsAndConsumablesDTO;
	List<RunResultsDetailDTO> runResultsDetailDTO;
	List<RunReagentsAndConsumables> listrunReagentsAndConsumables;
	List<RunResultsDetail> listRunResultsDetail ;
	public static final String jsonForSucessSampleResult = "src/test/java/resource/sampleResults.json";
	public static final String jsonSampleResultentity = "src/test/java/resource/sampleentity.json";
	public static final String jsonRunResultentity = "src/test/java/resource/RunResultEntity.json";
	public static final String jsonForrunresult = "src/test/java/resource/RunResultPositive.json";
	public static final String jsonForupdaterunResult = "src/test/java/resource/RunResultUpdate.json";
	RunResultsDTO upaterunResultsDTO ;
	@Mock
	RunResults updaterunResults;
	public static final String jsonForSucessRunResultupdate = "src/test/java/resource/RunResultUpdate.json";
	public static final String jsonForSucessRunResultentity = "src/test/java/resource/RunResultEntity.json";
	String runresult;
	String runresultentity ;
	
	@BeforeTest
	public void beforetest() throws IOException {
		objectMapper = new ObjectMapper();
		sampleResultsDTO = getSampleresultsDTO();
		updateSampleResultsDTO = getSampleresultsDTOformJson();
		runResults = getRunresult();
		sampleResults = getSampleresults();
		runResultsDTO = getrunresultsDTO();
		updateRunresultDTO = getupdaterunresultsDTO();
		
		
		
		runresult = JsonFileReaderAsString.getJsonfromFile(jsonForSucessRunResultupdate);
		runresultentity = JsonFileReaderAsString.getJsonfromFile(jsonForSucessRunResultentity);
		upaterunResultsDTO = objectMapper.readValue(runresult, RunResultsDTO.class);
		updaterunResults = objectMapper.readValue(runresultentity, RunResults.class);
		runResultsDetailDTO = (List<RunResultsDetailDTO>) upaterunResultsDTO.getRunResultsDetail();
		listrunReagentsAndConsumablesDTO = (List<RunReagentsAndConsumablesDTO>) upaterunResultsDTO
				.getRunReagentsAndConsumables();
		listrunReagentsAndConsumables = (List<RunReagentsAndConsumables>) updaterunResults
				.getRunReagentsAndConsumables();
		listRunResultsDetail = (List<RunResultsDetail>) updaterunResults.getRunResultsDetail();
		MockitoAnnotations.initMocks(this);
		 Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		inWorkFlowOrders.add(obj1);
		inWorkFlowOrders.add(obj2);
		inWorkFlowOrders.add(obj3);
		inWorkFlowOrders.add(obj4);
		inWorkFlowOrders.add(obj5);
		inWorkFlowOrders.add(obj6);
		inWorkFlowOrders.add(obj7);
		Mockito.when( sampleResultsReadRepository.getInWorkFlowOrders()).thenReturn(inWorkFlowOrders);

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
	public void postSampleResults_IFBranch() throws Exception {

		try {
			Mockito.when(runResultsReadRepository.findOne(sampleResultsDTO.getRunResultsId())).thenReturn(runResults);
			Mockito.when(mapDTOToEntity.getSampleResultsFromDTO(sampleResultsDTO)).thenReturn(sampleResults);
			Mockito.when(mapDTOToEntity.getSampleReagentsAndConsumablesFromDTO(sampleResultsDTO))
					.thenReturn((List<SampleReagentsAndConsumables>) sampleResults.getSampleReagentsAndConsumables());
			Mockito.when(mapDTOToEntity.getSampleResultsDetailFromDTO(sampleResultsDTO))
					.thenReturn((List<SampleResultsDetail>) (sampleResults.getSampleResultsDetail()));
			Mockito.when(mapDTOToEntity.getSampleProtocolFromDTO(sampleResultsDTO))
					.thenReturn((List<SampleProtocol>) (sampleResults.getSampleProtocol()));
			Mockito.when(sampleResultsWriteRepository.save(sampleResults)).thenReturn(sampleResults);
			Mockito.when(sampleResultsReadRepository.findByAccesssioningIdAndInputContainerId(
					sampleResultsDTO.getAccesssioningId(), sampleResultsDTO.getInputContainerId(),Mockito.anyLong()))
					.thenReturn(Arrays.asList(sampleResults));
			Mockito.when(runResultsWriteRepository.save(runResults)).thenReturn(runResults);
			
			Response response = runCrudRestApiImpl.postSampleResults(updateSampleResultsDTO);
			Assert.assertEquals(response.getStatus(), 200);
					
			updateSampleResultsDTO.setAccesssioningId(updateSampleResultsDTO.getAccesssioningId()+"1");
			updateSampleResultsDTO.setInputContainerId(updateSampleResultsDTO.getInputContainerId()+"1");
			updateSampleResultsDTO.setRunResultsId(0L);
			Response response1 = runCrudRestApiImpl.postSampleResults(updateSampleResultsDTO);
			Assert.assertEquals(response1.getStatus(), 200);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Test
	public void postSampleResults_ElseBranch() throws Exception {

		try {
			Mockito.when(runResultsReadRepository.findOne(sampleResultsDTO.getRunResultsId())).thenReturn(runResults);
			Mockito.when(mapDTOToEntity.getSampleResultsFromDTO(sampleResultsDTO)).thenReturn(sampleResults);
			Mockito.when(mapDTOToEntity.getSampleReagentsAndConsumablesFromDTO(sampleResultsDTO)) 
					.thenReturn((List<SampleReagentsAndConsumables>) sampleResults.getSampleReagentsAndConsumables());
			Mockito.when(mapDTOToEntity.getSampleResultsDetailFromDTO(sampleResultsDTO))
					.thenReturn((List<SampleResultsDetail>) (sampleResults.getSampleResultsDetail()));
			Mockito.when(mapDTOToEntity.getSampleProtocolFromDTO(sampleResultsDTO))
					.thenReturn((List<SampleProtocol>) (sampleResults.getSampleProtocol()));
			Mockito.when(sampleResultsWriteRepository.save(sampleResults)).thenReturn(sampleResults);
			Mockito.when(sampleResultsReadRepository.findByAccesssioningIdAndInputContainerIdAndInputContainerPosition(
					sampleResultsDTO.getAccesssioningId(), sampleResultsDTO.getInputContainerId(),
					sampleResultsDTO.getInputContainerPosition(),Mockito.anyLong())).thenReturn(Arrays.asList(sampleResults));
			Mockito.when(runResultsWriteRepository.save(runResults)).thenReturn(runResults);
			Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(runResults)).thenReturn(runResultsDTO);
			Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(any(SampleResults.class))).thenReturn(sampleResultsDTO);		
			Mockito.when(runResultsReadRepository.findByDeviceRunIdAndProcessStepName("deviceRunId",
					"processStepName",Mockito.anyLong())).thenReturn(Arrays.asList(runResults));
			Mockito.when(mapEntityToDTO
					.convertSampleProtocolToSampleProtocolDTO(Matchers.anyListOf(SampleProtocol.class)))
					.thenReturn((List<SampleProtocolDTO>) sampleResultsDTO.getSampleProtocol());
			Mockito.when(mapEntityToDTO.convertSampleReagentsAndConsumablesToReagentDTO(Matchers.anyListOf(SampleReagentsAndConsumables.class)))
					.thenReturn((List<SampleReagentsAndConsumablesDTO>) sampleResultsDTO.getSampleReagentsAndConsumables());
			Mockito.when(mapEntityToDTO.convertSampleResultsDetailToSampleResultsDetailDTO(Matchers.anyListOf(SampleResultsDetail.class)))
					.thenReturn((List<SampleResultsDetailDTO>) sampleResultsDTO.getSampleResultsDetail());
			 Mockito.when(mapDTOToEntity.getSampleResultsFromDTO(any(SampleResultsDTO.class))).thenReturn(sampleResults);
			Response response = runCrudRestApiImpl.postSampleResults(getSampleresultsDTO());
			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
			
	} 
	@Test
	public  void getSampleandRunResultsTest() throws HMTPException {
		
		try {
			Mockito.when(runResultsReadRepository.findByDeviceRunIdAndProcessStepName("deviceRunId",
					"processStepName",Mockito.anyLong())).thenReturn(Arrays.asList(updaterunResults));
			Mockito.when(runResultsReadRepository.findByDeviceRunIdAndProcessStepNameAndDeviceId("deviceRunId",
			    "processStepName","deviceid",Mockito.anyLong())).thenReturn(Arrays.asList(updaterunResults));
			Mockito.when(mapEntityToDTO
					.convertSampleProtocolToSampleProtocolDTO(Matchers.anyListOf(SampleProtocol.class)))
					.thenReturn((List<SampleProtocolDTO>) sampleResultsDTO.getSampleProtocol());
			Mockito.when(mapEntityToDTO.convertSampleReagentsAndConsumablesToReagentDTO(Matchers.anyListOf(SampleReagentsAndConsumables.class)))
					.thenReturn((List<SampleReagentsAndConsumablesDTO>) sampleResultsDTO.getSampleReagentsAndConsumables());
			Mockito.when(mapEntityToDTO.convertSampleResultsDetailToSampleResultsDetailDTO(Matchers.anyListOf(SampleResultsDetail.class)))
					.thenReturn((List<SampleResultsDetailDTO>) sampleResultsDTO.getSampleResultsDetail());
			Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(updaterunResults)).thenReturn(upaterunResultsDTO);
			Mockito.when(updaterunResults.getSampleResults()).thenReturn(Arrays.asList(sampleResults));
			
			Response response1 = runCrudRestApiImpl.getSampleandRunResults("deviceRunId", "processStepName","deviceid");
			Assert.assertEquals(response1.getStatus(), 200);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//@Test
	public void getAllDetailsByRunResultsIdTest() throws HMTPException {
		
		try {
			Mockito.when(runResultsWriteRepository.save(runResults)).thenReturn(runResults);
			Mockito.when(mapDTOToEntity.getRunResultsFromDTO(runResultsDTO)).thenCallRealMethod();
			Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(runResults)).thenCallRealMethod();
			Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(any(SampleResults.class))).thenCallRealMethod();
			Mockito.when(runResultsReadRepository.findOne(3l)).thenReturn(runResults);
			Response response = runCrudRestApiImpl.getAllDetailsByRunResultsId("3l");
			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	@Test
	public void getRunResultsByDeviceRunIdException() throws HMTPException {
		Response response=runCrudRestApiImpl.getRunResultsByDeviceRunId("LP_001");
		Assert.assertEquals(response.getStatus(), 204);
	}
	@Test
	public  void getRunResultsByDeviceRunIdTest() throws HMTPException {
		try {
			Mockito.when(runResultsReadRepository.findRunResultsByDeviceRunId("LP_001",Mockito.anyLong())).thenReturn(runResults);
			Response response=	runCrudRestApiImpl.getRunResultsByDeviceRunId("LP_001");
			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void getAllDetailsByRunResultsIdExceptionTest() throws HMTPException {
		Mockito.when(runResultsReadRepository.findOne(3l)).thenReturn(null);
		Response response = runCrudRestApiImpl.getAllDetailsByRunResultsId("3l");
		Assert.assertEquals(response.getStatus(), 400);
	}
	
	@Test(priority = 1)
	public void getRunResultsByDeviceRunIdProStepNameRunStatusPositive() throws HMTPException {
		try {
			String deviceid = null;
			String processstepname = null;
			String runstatus = null;
			int expectedCorrectCode = 0;
			deviceid = "mp24";
			processstepname = "NA Extraction";
			runstatus = "inprogress";
			expectedCorrectCode = 200;
			Mockito.when(runResultsReadRepository.findFirstByDeviceIdAndProcessStepNameAndRunStatus(deviceid,
					processstepname, runstatus,Mockito.anyLong())).thenReturn(runResults);
			Response response = runCrudRestApiImpl.getRunResults(deviceid, processstepname, runstatus);
			assertEquals(response.getStatus(), expectedCorrectCode);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test(priority = 2)
	public void getRunResultsByDeviceRunIdProStepNameRunStatusNegative() throws HMTPException {
		try {
			String wrongDeviceid = null;
			String processstepname = null;
			int expectedCorrectCode = 0;
			wrongDeviceid="LP24";
			processstepname = "NA Extraction";
			expectedCorrectCode = 204;
			Response response = runCrudRestApiImpl.getRunResults(wrongDeviceid, processstepname, null);
			assertEquals(response.getStatus(), expectedCorrectCode);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	
	@Test
	public void addRunResults() throws HMTPException {
		try {
			Mockito.when(mapDTOToEntity.getRunResultsFromDTO(any(RunResultsDTO.class))).thenReturn(runResults);
			Mockito.when(runResultsWriteRepository.save(any(RunResults.class))).thenReturn(runResults) ;
			Mockito.when( mapDTOToEntity.getRunReagentsAndConsumablesFromDTO(any(RunResultsDTO.class))).thenReturn((List<RunReagentsAndConsumables>) runResults.getRunReagentsAndConsumables());
			Mockito.when(mapDTOToEntity.getRunResultsDetailFromDTO(any(RunResultsDTO.class))).thenReturn((List<RunResultsDetail>) runResults.getRunResultsDetail());
			Response response	=runCrudRestApiImpl.addRunResults(runResultsDTO);
			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	@Test
	public void updateRunResultsTest() throws HMTPException, IOException {
		

	try {
		Mockito.when(mapDTOToEntity.getRunReagentsAndConsumablesFromDTO(upaterunResultsDTO))
				.thenReturn(listrunReagentsAndConsumables);
		Mockito.when(runReagentsAndConsumablesWriteRepository.save(listrunReagentsAndConsumables.get(0)))
				.thenReturn(listrunReagentsAndConsumables.get(0));
		Mockito.when(runReagentsAndConsumablesWriteRepository.save(listrunReagentsAndConsumables.get(1)))
				.thenReturn(listrunReagentsAndConsumables.get(1));
		Mockito.when(mapDTOToEntity.getRunResultsDetailFromDTO(upaterunResultsDTO)).thenReturn(listRunResultsDetail);
		Mockito.when(runResultsDetailWriteRepository.save(listRunResultsDetail.get(0)))
				.thenReturn(listRunResultsDetail.get(0));
		Mockito.when(runResultsWriteRepository.save(updaterunResults)).thenReturn(updaterunResults);
		Mockito.when(mapDTOToEntity.getRunResultsFromDTO(upaterunResultsDTO)).thenReturn(updaterunResults);
		Mockito.when(mapEntityToDTO
				.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(listrunReagentsAndConsumables))
				.thenReturn(listrunReagentsAndConsumablesDTO);
		Mockito.when(mapEntityToDTO.convertRunResultsDetailToRunResultsDetailDTODTO(listRunResultsDetail))
				.thenReturn(runResultsDetailDTO);
		Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(updaterunResults)).thenReturn(upaterunResultsDTO);
//		Response response = runCrudRestApiImpl.updateRunResults(upaterunResultsDTO);
//		Assert.assertEquals(response.getStatus(), 200);
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
	
	}
	@Test
	public void upaterunResultsDTO() throws HMTPException, IOException {
		System.out.println("updaterunResultsDTO ");
		Mockito.when(mapDTOToEntity.getRunResultsFromDTO(upaterunResultsDTO)).thenReturn(null);
//		runCrudRestApiImpl.updateRunResults(upaterunResultsDTO);
	}
	
	
	@Test
	public void getRunresultsByAccessioningId() throws IOException, HMTPException {
		try {
			String accessioningId="2828282";
			int expectedstatuscode=200;
			MockitoAnnotations.initMocks(this);
			Mockito.when(sampleResultsReadRepository.findIdByAccesssioningId(accessioningId,Mockito.anyLong())).thenReturn(Arrays.asList(1l));
			Response response = runCrudRestApiImpl. getProcessStepResults(accessioningId);
			Assert.assertEquals(response.getStatus(), expectedstatuscode);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	//@Test(expectedExceptions=HMTPException.class)
	public void getRunresultsByAccessioningIdByNullValue() throws IOException, HMTPException {
		try {
			String accessioningId="2828282";
			
			MockitoAnnotations.initMocks(this);
			Mockito.when(sampleResultsReadRepository.findIdByAccesssioningId(accessioningId,Mockito.anyLong())).thenReturn(null);
			 runCrudRestApiImpl. getProcessStepResults(accessioningId);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		

	}
	
	//@Test
	public void getRunresults() throws IOException, HMTPException {
		try {
			long sampleresultid = 1l;
			int	expectedstatuscode = 200;

			Mockito.when(mapEntityToDTO.convertToDTO(getSampleresults())).thenReturn(runResultsDTO);
			Mockito.when(sampleResultsReadRepository.findOne(sampleresultid)).thenReturn(getSampleresults());
			Response response = runCrudRestApiImpl.getRunResults(sampleresultid);
			Assert.assertEquals(response.getStatus(), expectedstatuscode);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	@Test
	public void getRunresultsTest() throws IOException, HMTPException {
		long sampleresultid = 1l;
		int	expectedstatuscode = 400;
		Mockito.when(mapEntityToDTO.convertToDTO(getSampleresults())).thenReturn(runResultsDTO);
		Mockito.when(sampleResultsReadRepository.findOne(sampleresultid)).thenReturn(null);
		Response response = runCrudRestApiImpl.getRunResults(sampleresultid);
		Assert.assertEquals(response.getStatus(), expectedstatuscode);

	}
	@Test
	public void getSampleResults_for_no_content() throws HMTPException {
		
		try {
			sampleResultsReadRepository.findAll(RunResultsSpecifications.getSampleResults("deviceId", "processStepName", "processStepName",
					"inputContainerId", "inputContainerPosition", "outputContainerPosition","accessioningId"));
			Response response=runCrudRestApiImpl.getSampleResults("deviceId", "processStepName", "outputContainerId", "inputContainerId", "inputContainerPosition", "outputContainerPosition","accessioningId");
			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Test(expectedExceptions=IllegalArgumentException.class)
	public void getSampleResults_Exception() throws HMTPException {
			runCrudRestApiImpl.getSampleResults(null,null, null, null, null, null,null);

	}
	@SuppressWarnings("unchecked")
	@Test
	public void getSampleResults() throws HMTPException, IOException {
		
		try {
			Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(sampleResults)).thenReturn(sampleResultsDTO);
			Mockito.when(sampleResultsReadRepository.findAll((any(Specification.class)))).thenReturn(Arrays.asList(sampleResults));
			Response response=runCrudRestApiImpl.getSampleResults("deviceId", "processStepName", "outputContainerId", "inputContainerId", "inputContainerPosition", "outputContainerPosition","accessioningId");
			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	@Test
	void getsampleresults(){		
	RunResultsSpecifications.getSampleResults("deviceId", "processStepName", "outputContainerId", "inputContainerId", "inputContainerPosition", "outputContainerPosition","accessioningId");
	}
	
	@Test
	public void getRunResultsByProcessStepName() throws HMTPException {
		Mockito.when(runResultsReadRepository.findAll(runResultsTypeSpecification)).thenReturn(Arrays.asList(new RunResults()));
		runCrudRestApiImpl.getRunResultsByProcessStepName("processStepName");
	}
	
	@Test
	public void getRunResultsByProcessStepNameTestr()
			throws HMTPException, JsonParseException, JsonMappingException, IOException {
		try {
			Mockito.when(runResultsReadRepository.findAll((any(RunResultsTypeSpecification.class)))).thenReturn(Arrays.asList(runResults));
			Response response = runCrudRestApiImpl.getRunResultsByProcessStepName("processStepName");
			Assert.assertEquals(response.getStatus(), 200);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
	}
	
	
@Test
public void getSampleListByDeviceIdAndRunStatus() throws HMTPException {
	try {
		String deviceId = "MP001-12";
		 String runStatus = "Inprogress";
		 int expectedCorrectCode = 200;
		List<RunResults> listRunResults = new ArrayList<>();

		Collection<SampleResults> sampleResultsList = new ArrayList<>();

		SampleResults sampleResults = new SampleResults();
		sampleResults.setInputContainerType("123345");
		sampleResults.setAccesssioningId("23351ffg");
		sampleResults.setAccesssioningId("");
		sampleResults.setFlag("");
		sampleResults.setInputContainerId("");
		sampleResults.setInputContainerPosition("");
		sampleResults.setInputKitId("");
		sampleResults.setOrderId(1000);
		sampleResults.setOutputContainerId("");
		sampleResults.setOutputContainerPosition("");
		sampleResults.setOutputContainerType("");
		sampleResults.setOutputKitId("");
		sampleResults.setResult("");
		sampleResults.setStatus("");
		sampleResults.setUpdatedBy("");
		sampleResults.setVerifiedBy("");

		SampleResults sampleResults2 = new SampleResults();
		sampleResults2.setInputContainerType("123345");
		sampleResults2.setAccesssioningId("23351ffg");
		sampleResults2.setAccesssioningId("");
		sampleResults2.setFlag("");
		sampleResults2.setInputContainerId("");
		sampleResults2.setInputContainerPosition("");
		sampleResults2.setInputKitId("");
		sampleResults2.setOrderId(1000);
		sampleResults2.setOutputContainerId("");
		sampleResults2.setOutputContainerPosition("");
		sampleResults2.setOutputContainerType("");
		sampleResults2.setOutputKitId("");
		sampleResults2.setResult("");
		sampleResults2.setStatus("");
		sampleResults2.setUpdatedBy("");
		sampleResults2.setVerifiedBy("");

		sampleResultsList.add(sampleResults);
		sampleResultsList.add(sampleResults2);

		RunResults runResults = new RunResults();
		runResults.setDeviceId(deviceId);
		runResults.setRunStatus(runStatus);
		runResults.setSampleResults(sampleResultsList);

		listRunResults.add(runResults);

		Mockito.when(runResultsReadRepository.findByDeviceIdAndRunStatus(deviceId, runStatus,Mockito.anyLong()))
				.thenReturn(listRunResults);
		Response response = runCrudRestApiImpl.findInprogressStatusByDeviceID(deviceId, runStatus);
		assertEquals(response.getStatus(), expectedCorrectCode);
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
}

@Test
public void getNegSampleListByDeviceIdAndRunStatus() throws HMTPException {
	try {
		String deviceId = "MP001-12";
		 String runStatus = "Inprogress";
		 int expectedIncorrectCode = 204;
		List<RunResults> listRunResults = new ArrayList<>();

		Collection<SampleResults> sampleResultsList = new ArrayList<>();

		SampleResults sampleResults = new SampleResults();
		sampleResults.setInputContainerType("123345");
		sampleResults.setAccesssioningId("23351ffg");
		sampleResults.setAccesssioningId("");
		sampleResults.setFlag("");
		sampleResults.setInputContainerId("");
		sampleResults.setInputContainerPosition("");
		sampleResults.setInputKitId("");
		sampleResults.setOrderId(1000);
		sampleResults.setOutputContainerId("");
		sampleResults.setOutputContainerPosition("");
		sampleResults.setOutputContainerType("");
		sampleResults.setOutputKitId("");
		sampleResults.setResult("");
		sampleResults.setStatus("");
		sampleResults.setUpdatedBy("");
		sampleResults.setVerifiedBy("");

		SampleResults sampleResults2 = new SampleResults();
		sampleResults2.setInputContainerType("123345");
		sampleResults2.setAccesssioningId("23351ffg");
		sampleResults2.setAccesssioningId("");
		sampleResults2.setFlag("");
		sampleResults2.setInputContainerId("");
		sampleResults2.setInputContainerPosition("");
		sampleResults2.setInputKitId("");
		sampleResults2.setOrderId(1000);
		sampleResults2.setOutputContainerId("");
		sampleResults2.setOutputContainerPosition("");
		sampleResults2.setOutputContainerType("");
		sampleResults2.setOutputKitId("");
		sampleResults2.setResult("");
		sampleResults2.setStatus("");
		sampleResults2.setUpdatedBy("");
		sampleResults2.setVerifiedBy("");

		sampleResultsList.add(sampleResults);
		sampleResultsList.add(sampleResults2);

		RunResults runResults = new RunResults();
		runResults.setDeviceId(deviceId);
		runResults.setRunStatus(runStatus);
		runResults.setSampleResults(sampleResultsList);

		listRunResults.add(runResults);

		Mockito.when(runResultsReadRepository.findByDeviceIdAndRunStatus(deviceId, runStatus,Mockito.anyLong()))
				.thenReturn(listRunResults);
		Response response = runCrudRestApiImpl.findInprogressStatusByDeviceID("MP001", runStatus);
		assertEquals(response.getStatus(), expectedIncorrectCode);
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
}
	
	private SampleResultsDTO getSampleresultsDTO() throws IOException {
		sampleresultDTO = JsonFileReaderAsString.getJsonfromFile(jsonForSucessSampleResult);
		sampleResultsDTO = objectMapper.readValue(sampleresultDTO, SampleResultsDTO.class);
		return sampleResultsDTO;

	}

	private SampleResultsDTO getSampleresultsDTOformJson() throws IOException {
		sampleresultDTO = JsonFileReaderAsString.getJsonfromFile(jsonForSucessSampleResult);
		sampleResultsDTO = objectMapper.readValue(sampleresultDTO, SampleResultsDTO.class);
		sampleResultsDTO.setInputContainerPosition(null);
		return sampleResultsDTO;

	}

	private SampleResults getSampleresults() throws IOException {
		sampleresultEntity = JsonFileReaderAsString.getJsonfromFile(jsonSampleResultentity);
		sampleResults = objectMapper.readValue(sampleresultEntity, SampleResults.class);
		return sampleResults;

	}

	private RunResultsDTO getrunresultsDTO() throws IOException {
		runresultjsonString = JsonFileReaderAsString.getJsonfromFile(jsonForrunresult);
		runResultsDTO = objectMapper.readValue(runresultjsonString, RunResultsDTO.class);
		return runResultsDTO;

	}

	private RunResultsDTO getupdaterunresultsDTO() throws IOException {
		updaterunresultstring = JsonFileReaderAsString.getJsonfromFile(jsonForupdaterunResult);
		updateRunresultDTO = objectMapper.readValue(updaterunresultstring, RunResultsDTO.class);
		return updateRunresultDTO;

	}

	private RunResults getRunresult() throws JsonParseException, JsonMappingException, IOException {
		String runresultentitystring = JsonFileReaderAsString.getJsonfromFile(jsonRunResultentity);
		RunResults results = objectMapper.readValue(runresultentitystring, RunResults.class);
		return results;
	}

	@Test
	public void getInWorkFlowOrdersTest() throws IOException, HMTPException {
		runCrudRestApiImpl.getInWorkFlowOrders();
	}
	
	@Test
	public void getAccessingIdListTest() throws IOException {
		runCrudRestApiImpl.getAccessingIdList(runResults);
	}

	@Test
	   public void RMMConstantTest() {	  
	    assertEquals("NA Extraction", RMMConstant.ProcessStepValues.NAEXTRACTION.toString());
	}
	
	@Test
	public void testErrorResponseGetter() {
	  ErrorResponse errorResp = new ErrorResponse();
	  errorResp.setErrorStatusCode(400);
	  errorResp.setErrorMessage("Test");
	  assertEquals(errorResp.getErrorStatusCode(), 400);
	  assertEquals(errorResp.getErrorMessage(), "Test");
	}
}
