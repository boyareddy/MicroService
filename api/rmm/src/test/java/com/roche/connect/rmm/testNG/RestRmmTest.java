package com.roche.connect.rmm.testNG;

import static org.mockito.ArgumentMatchers.any;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.data.jpa.domain.Specification;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.UserSession;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.amm.dto.FlagsDataDTO;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.dmm.DeviceDTO;
import com.roche.connect.common.dmm.DeviceTypeDTO;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderDetailsDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.common.rmm.dto.ProcessStepValuesDTO;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleProtocolDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.common.rmm.dto.UniqueReagentsAndConsumables;
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
import com.roche.connect.rmm.services.DeviceIntegrationService;
import com.roche.connect.rmm.services.OrderIntegrationService;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.util.MapDTOToEntity;
import com.roche.connect.rmm.util.MapEntityToDTO;
import com.roche.connect.rmm.writerepository.RunReagentsAndConsumablesWriteRepository;
import com.roche.connect.rmm.writerepository.RunResultsDetailWriteRepository;
import com.roche.connect.rmm.writerepository.RunResultsWriteRepository;
import com.roche.connect.rmm.writerepository.SampleProtocolWriteRepository;
import com.roche.connect.rmm.writerepository.SampleReagentsAndConsumablesWriteRespository;
import com.roche.connect.rmm.writerepository.SampleResultsDetailWriteRepository;
import com.roche.connect.rmm.writerepository.SampleResultsWriteRepository;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

@PrepareForTest({ RestClientUtil.class, JasperCompileManager.class, JasperFillManager.class,
		JRBeanCollectionDataSource.class, JRPdfExporter.class, java.util.ResourceBundle.class, URL.class,
		ThreadSessionManager.class,URL.class,BufferedInputStream.class})
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class RestRmmTest {
	@InjectMocks 
	RunCrudRestApiImpl runCrudRestApiImpl;
	
	@Mock DeviceIntegrationService deviceIntegrationService;
	@Mock OrderIntegrationService orderIntegrationService;
	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;
	@Mock
	Invocation.Builder assayClient;
	@Mock
	Invocation.Builder orderClient;
	
	@Mock UserSession userSession;

	List<AssayTypeDTO> listAssays = new ArrayList<AssayTypeDTO>();
	List<OrderDetailsDTO> orderList = new ArrayList<>();
	List<ProcessStepActionDTO> processStepActionDTOList = new ArrayList<>();
	List<OrderDTO> listOrderList = new ArrayList<>();

	private final String template = "C:/workspace_dec12/api/rmm/src/main/resources/jasper/Cherry.jrxml";
	private final String corrected_rep_template = "C:/workspace_dec12/api/rmm/src/main/resources/jasper/CorrectedRep.jrxml";
	private final String subreport_template = "C:/workspace_dec12/api/rmm/src/main/resources/jasper/OrderSubReport.jrxml";
	private final String subreport_child_template = "C:/workspace_dec12/api/rmm/src/main/resources/jasper/patientDetails.jrxml";
	private final String corrected_rep_test_template = "C:/workspace_dec12/api/rmm/src/main/resources/jasper/CorrectReptest.jrxml";

	@Mock
	JasperReport jasperReport;
	@Mock
	JasperReport jasperSubReport;
	@Mock
	JRBeanCollectionDataSource dataSource;
	@Mock
	JasperPrint jasperPrint;
	@Mock
	SimpleExporterInput simpleExporterInput;
	@Mock
	SimpleOutputStreamExporterOutput output;
	@Mock
	JRPdfExporter pdfExporter;
	@Mock
	ByteArrayOutputStream pdfReportStream;
	@Mock
	SimplePdfExporterConfiguration configuration;
	@Mock
	JRCsvExporter csvExporter;
	@Mock
	SimpleCsvExporterConfiguration exportConfiguration;
	@Mock
	SimpleWriterExporterOutput exporterOutput;
	@Mock
	java.util.ResourceBundle resourceBundle;
	@Mock
	SampleResultsReadRepository sampleResultsReadRepository;
	@Mock
	Invocation.Builder ammClient;
	@Mock
	MapDTOToEntity mapDTOToEntity;
	@Mock
	MapEntityToDTO mapEntityToDTO;
	@Mock
	org.springframework.data.domain.Sort runResultsTypeSpecification;
	@Mock
	InputStream in1;
	@Mock
	InputStream jasperInput;
	@Mock
	RunResultsReadRepository runResultsReadRepository;
	@Mock
	RunResultsWriteRepository runResultsWriteRepository;
	
	String assayType = "NIPTHTP";
	String processstep = "NA Extraction";
	String url = "http://localhost";
	ObjectMapper objectMapper;
	RunResults runResults = new RunResults();
	List<RunResults> runResultsList = new ArrayList<>();
	List<AssayTypeDTO> listAssay = new ArrayList<>();
	public static final String jsonForSucessRunResultentity = "src/test/java/resource/RunResultEntity.json";
	@Mock RunReagentsAndConsumablesWriteRepository runReagentsAndConsumablesWriteRepository;
	@Mock RunResultsDetailWriteRepository runResultsDetailWriteRepository;
	
	List<RunReagentsAndConsumables> reagentsAndConsumables = new ArrayList<>();
	List<RunResultsDetail> runResultsDetails = new ArrayList<>();
	List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTO = new ArrayList<>();
	List<RunResultsDetailDTO> runresultdetaildto = new ArrayList<>();
	List<SampleProtocolDTO> sampleProtocolDTo = new ArrayList<>();
	List<SampleReagentsAndConsumablesDTO> samplereagentdto =new ArrayList<>();
	List<SampleResultsDetailDTO> sampledetaildto =new ArrayList<>();
	Collection<UniqueReagentsAndConsumables> listOfReagents = new ArrayList<>();
	List<SampleReagentsAndConsumables> sampleReagentsAndConsumables = new ArrayList<>();
	List<SampleResultsDetail> sampleResultsDetail = new ArrayList<>();
	List<SampleProtocol> sampleProtocol =  new ArrayList<>();
	@Mock SampleResultsWriteRepository sampleResultsWriteRepository;
	@Mock  SampleReagentsAndConsumablesWriteRespository sampleReagentsAndConsumablesWriteRespository;
	@Mock SampleResultsDetailWriteRepository sampleResultsDetailWriteRepository; 
	@Mock  SampleProtocolWriteRepository sampleProtocolWriteRepository;
	List<FlagsDataDTO> listFlags =  new ArrayList<>();
	List<Long> sampleResultIds = new ArrayList<>();
	
	
	@BeforeTest
	public void setUp() throws IOException, HMTPException {
		objectMapper = new ObjectMapper();
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		listAssays.add(getAssayTypeDTO());
		orderList.add(getOrderDetailsDTO());
		orderList.add(getOrderDetailsDTOPatientFirstAndLastName());
		orderList.add(getOrderDetailsDTOPatientFirstName());
		runResultsDetails.add(getRunResultsDetail());
		processStepActionDTOList.add(getProcessStepActionDTO());
		reagentsAndConsumables.add(getRunReagentsAndConsumables());
		runresultdetaildto.add(getRunResultsDetailDTO());
		runReagentsAndConsumablesDTO.add(getRunReagentsAndConsumablesDTO());
		sampleProtocolDTo.add(getSampleProtocolDTO());
		samplereagentdto.add(getSampleReagentsAndConsumablesDTO());
		sampledetaildto.add(getSampleResultsDetailDTO());
		
		listOfReagents.add(getUniqueReagentsAndConsumablesBarCode());
		listOfReagents.add(getUniqueReagentsAndConsumablesLotNo());
		listOfReagents.add(getUniqueReagentsAndConsumablesExpDate());
		listOfReagents.add(getUniqueReagentsAndConsumablesVolume());
		listOfReagents.add(getUniqueReagentsAndConsumablesPosition());
		listOfReagents.add(getUniqueReagentsAndConsumablesReagentKitName());
		listOfReagents.add(getUniqueReagentsAndConsumablesReagentVersion());
		
		listOrderList.add(getOrderDTO());
		
		listFlags.add(getFlagsDataDTO());
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
		List<RunResults> runList = new ArrayList<>();
		runList.add(getRunResults());
        Mockito.when(runResultsReadRepository.findByDeviceIdAndRunStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(runList );
		Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(Mockito.any(SampleResults.class))).thenReturn(getSampleresultsDTO());
	}
	
	public SampleResultsDetailDTO getSampleResultsDetailDTO(){
		SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
		sampleResultsDetailDTO.setAttributeName("attributeName");
		sampleResultsDetailDTO.setAttributeValue("attributeValue");
		return sampleResultsDetailDTO;
	}
	
	public SampleReagentsAndConsumablesDTO getSampleReagentsAndConsumablesDTO(){
		SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
		sampleReagentsAndConsumablesDTO.setAttributeName("attributeName");
		sampleReagentsAndConsumablesDTO.setAttributeValue("attributeValue");
		return sampleReagentsAndConsumablesDTO;
		
	}
	public SampleProtocolDTO getSampleProtocolDTO(){
		SampleProtocolDTO sample =  new SampleProtocolDTO();
		sample.setCreatedBy("admin");
		return sample;
	}
	public RunReagentsAndConsumablesDTO getRunReagentsAndConsumablesDTO(){
		RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
		runReagentsAndConsumablesDTO.setAttributeName("attributeName");
		runReagentsAndConsumablesDTO.setAttributeValue("attributeValue");
		return runReagentsAndConsumablesDTO;
		
	}

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}
	
	@Test
	public void listActiveRunsTest() throws IOException, HMTPException {
		ObjectMapper objectMapper = new ObjectMapper();
		String runresult = JsonFileReaderAsString.getJsonfromFile(jsonForSucessRunResultentity);
		RunResults runResults = objectMapper.readValue(runresult, RunResults.class);
		 List<SampleResults> sampleResults = (List<SampleResults>) runResults.getSampleResults();
		Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(sampleResults.get(0))).thenReturn(getSampleresultsDTO());
		Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(runResults)).thenReturn(getrunresultsDTO());
		Mockito.when( sampleResultsReadRepository.findIdByAccesssioningId(any(String.class),Mockito.anyLong())).thenReturn(Arrays.asList(1l));
		Mockito.when(sampleResultsReadRepository.findOne(any(Long.class))).thenReturn(getSampleresults()); 
		  runResultsList.add(runResults);
	        Mockito.when(runResultsReadRepository.findAll(Matchers.any(Specification.class))).thenReturn(runResultsList);
		Response response=	runCrudRestApiImpl.listActiveRuns("NIPTHTP", "InProgress", "NA Extraction");
		Assert.assertEquals(response.getStatus(), 200);
	}
	
	
	  @Test
	   public void findInprogressStatusByDeviceIDTest() throws HMTPException {
	       runCrudRestApiImpl.findInprogressStatusByDeviceID("1234", "Completed");
	   }
	
	public RunResults getRunResults() throws IOException {
	    RunResults runResults = new RunResults();
	    Collection<SampleResults> sample = new ArrayList<>();
	    sample.add(getSampleresults());
        runResults.setSampleResults(sample );
        return runResults;
	    
	}
	public SampleReagentsAndConsumables getSampleReagentsAndConsumables(){
		SampleReagentsAndConsumables sampleReagentsAndConsumables = new SampleReagentsAndConsumables();
		sampleReagentsAndConsumables.setAttributeName("attributeName");
		sampleReagentsAndConsumables.setAttributeValue("attributeValue");
		return sampleReagentsAndConsumables;
	}
	
	public SampleResultsDetail getSampleResultsDetail(){
		SampleResultsDetail sampleResultsDetail = new SampleResultsDetail();
		sampleResultsDetail.setAttributeName("attributeName");
		sampleResultsDetail.setAttributeValue("attributeValue");
		return sampleResultsDetail;
		
	}
	
	public SampleProtocol getSampleProtocol(){
		SampleProtocol sampleProtocol = new SampleProtocol();
		sampleProtocol.setCreatedBy("admin");
		return sampleProtocol;
		
	}
	
	public OrderDTO getOrderDTO(){
	    OrderDTO  orderDTO = new OrderDTO();
	    orderDTO.setAccessioningId("8001");
        return orderDTO;
	}
	
	@Test
	public void addSampleResultsTest() throws HMTPException, IOException{
		sampleReagentsAndConsumables.add(getSampleReagentsAndConsumables());
		sampleResultsDetail.add(getSampleResultsDetail());
		sampleProtocol.add(getSampleProtocol());
		Mockito.when(runResultsReadRepository.findOne(Mockito.anyLong())).thenReturn(getRunresult());
		Mockito.when(mapDTOToEntity.getSampleResultsFromDTO(Mockito.any(SampleResultsDTO.class))).thenReturn(getSampleresults());
		Mockito.when(mapDTOToEntity
					.getSampleReagentsAndConsumablesFromDTO(getSampleresultsDTO())).thenReturn(sampleReagentsAndConsumables);
		Mockito.when(mapDTOToEntity
					.getSampleResultsDetailFromDTO(getSampleresultsDTO())).thenReturn(sampleResultsDetail);
		Mockito.when(mapDTOToEntity.getSampleProtocolFromDTO(getSampleresultsDTO())).thenReturn(sampleProtocol);
		Mockito.when(runResultsWriteRepository.save(Mockito.any(RunResults.class))).thenReturn(getRunresult());
		Mockito.when(sampleResultsWriteRepository.save(sampleResults)).thenReturn(getSampleresults());
		//Mockito.when(sampleReagentsAndConsumablesWriteRespository.save(Mockito.any(SampleReagentsAndConsumables.class)));
		//Mockito.when(sampleResultsDetailWriteRepository.save(Mockito.any(SampleResultsDetail.class))).thenReturn(getSampleResultsDetail());
		//Mockito.when(sampleProtocolWriteRepository.save(SampleProtocol.class));
		runCrudRestApiImpl.addSampleResults(getSampleresultsDTO());
	}
	
	@Test
	public void getNAExtractionDTOTest() throws HMTPException, IOException{
		PowerMockito.mockStatic(RestClientUtil.class);
		Optional<RunResultsDTO> runResultDto =Optional.ofNullable(getrunresultsDTODPCR());
		String deviceTypeUrl ="http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay",
				"/"+runResultDto.get().getAssayType()+ "/processstepaction?deviceType=" +"MP96", null)).thenReturn(deviceTypeUrl);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(deviceTypeUrl, "UTF-8"), null)).thenReturn(ammClient);
		Mockito.when(ammClient.get(new GenericType<List<ProcessStepActionDTO>>() {
			})).thenReturn(processStepActionDTOList);
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/flagDescriptions",
				"/"+"MP96"+"?assayType="+runResultDto.get().getAssayType(), null)).thenReturn(deviceTypeUrl);
		
		Mockito.when(ammClient.get(new GenericType<List<FlagsDataDTO>>() {
			})).thenReturn(listFlags);
		runCrudRestApiImpl.getNAExtractionDTO(runResultDto);
	}
	
	@Test
	public void getLibraryPrepDTOTest() throws HMTPException, IOException{
		PowerMockito.mockStatic(RestClientUtil.class);
		Optional<RunResultsDTO> runResultDto =Optional.ofNullable(getrunresultsDTODPCR());
		String deviceTypeUrl ="http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay",
				"/"+runResultDto.get().getAssayType()+ "/processstepaction?deviceType=" +"LP24", null)).thenReturn(deviceTypeUrl);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(deviceTypeUrl, "UTF-8"), null)).thenReturn(ammClient);
		Mockito.when(ammClient.get(new GenericType<List<ProcessStepActionDTO>>() {
			})).thenReturn(processStepActionDTOList);
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/flagDescriptions",
				"/"+"LP24"+"?assayType="+runResultDto.get().getAssayType(), null)).thenReturn(deviceTypeUrl);
		
		Mockito.when(ammClient.get(new GenericType<List<FlagsDataDTO>>() {
			})).thenReturn(listFlags);
		runCrudRestApiImpl.getLibraryPrepDTO(runResultDto);
	}
	
	@Test
	public void getDPCRResultsDataTest() throws HMTPException, IOException{
		PowerMockito.mockStatic(RestClientUtil.class);
		Optional<RunResultsDTO> runResultDto =Optional.ofNullable(getrunresultsDTODPCR());
		String deviceTypeUrl ="http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay",
				"/"+runResultDto.get().getAssayType()+ "/processstepaction?deviceType=" +"dPCR Analyzer", null)).thenReturn(deviceTypeUrl);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(deviceTypeUrl, "UTF-8"), null)).thenReturn(ammClient);
		Mockito.when(ammClient.get(new GenericType<List<ProcessStepActionDTO>>() {
			})).thenReturn(processStepActionDTOList);
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/flagDescriptions",
				"/"+"dPCR Analyzer"+"?assayType="+runResultDto.get().getAssayType(), null)).thenReturn(deviceTypeUrl);
		
		Mockito.when(ammClient.get(new GenericType<List<FlagsDataDTO>>() {
			})).thenReturn(listFlags);
		runCrudRestApiImpl.getDPCRResultsData(runResultDto);
	}

	@Test
	public void listInCompletedWFSActiveRunsTest() throws Exception {

		runResults.setProcessStepName("NA Extraction");
		runResults.setRunStatus("Completed");
		runResultsList.add(runResults);
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay?assayType=", assayType,
				null)).thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(ammClient);
		Mockito.when(ammClient.get(new GenericType<List<AssayTypeDTO>>() {
		})).thenReturn(listAssay);

		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/",
				assayType + "/" + "processstepaction", null)).thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(ammClient);
		Mockito.when(ammClient.get(new GenericType<List<ProcessStepActionDTO>>() {
		})).thenReturn(processStepActionDTOList);

		// sart
		String runresult = JsonFileReaderAsString.getJsonfromFile(jsonForSucessRunResultentity);
		RunResults runResults = objectMapper.readValue(runresult, RunResults.class);
		Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(getSampleresults()))
				.thenReturn(getSampleresultsDTO());
		Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(runResults)).thenReturn(getrunresultsDTO());
		Mockito.when(sampleResultsReadRepository.findIdByAccesssioningId(any(String.class),Mockito.anyLong()))
				.thenReturn(Arrays.asList(1l));
		Mockito.when(sampleResultsReadRepository.findOne(any(Long.class))).thenReturn(getSampleresults());
		Mockito.when(runResultsReadRepository.findAll(runResultsTypeSpecification))
				.thenReturn(Arrays.asList(new RunResults()));
		Mockito.when(mapDTOToEntity.getRunResultsFromDTO(any(RunResultsDTO.class))).thenReturn(runResults);
		runCrudRestApiImpl.listInCompletedWFSActiveRuns(assayType, processstep);
	}
	
	@Test
    public void listInCompletedWFSActiveRunsPositiveTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        listAssay.add(getAssayTypeDTO());
        processStepActionDTOList.add(getProcessStepActionDTO());
        Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay?assayType=", assayType,
                null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(ammClient);
        Mockito.when(ammClient.get(new GenericType<List<AssayTypeDTO>>() {
        })).thenReturn(listAssay);

        Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/",
                assayType + "/" + "processstepaction", null)).thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(ammClient);
        Mockito.when(ammClient.get(new GenericType<List<ProcessStepActionDTO>>() {
        })).thenReturn(processStepActionDTOList);

        // sart
        String runresult = JsonFileReaderAsString.getJsonfromFile(jsonForSucessRunResultentity);
        RunResults runResults = objectMapper.readValue(runresult, RunResults.class);
        Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(Mockito.any(SampleResults.class)))
                .thenReturn(getSampleresultsDTO());
        Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class))).thenReturn(getrunresultsDTO());
        Mockito.when(mapEntityToDTO.convertRunResultsDetailToRunResultsDetailDTODTO(Mockito.anyList()))
        .thenReturn(runresultdetaildto);
        Mockito.when(mapEntityToDTO.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(Mockito.anyList()))
        .thenReturn(runReagentsAndConsumablesDTO);
        Mockito.when(sampleResultsReadRepository.findIdByAccesssioningId(any(String.class),Mockito.anyLong()))
                .thenReturn(Arrays.asList(1l));
        Mockito.when(sampleResultsReadRepository.findOne(any(Long.class))).thenReturn(getSampleresults());
        
        List<RunResults> runResultsList = new ArrayList<>();
        runResultsList.add(getRunResults());

        Mockito.when(runResultsReadRepository.findAll(Mockito.any(org.springframework.data.jpa.domain.Specification.class)))
                .thenReturn(runResultsList);
        Mockito.when(mapDTOToEntity.getRunResultsFromDTO(any(RunResultsDTO.class))).thenReturn(runResults);
        List<DeviceDTO> deviceList = new ArrayList<>();
        deviceList.add(getDeviceDTO());
        Mockito.when(deviceIntegrationService.getDevice(Mockito.anyList())).thenReturn(deviceList );
        runCrudRestApiImpl.listInCompletedWFSActiveRuns(assayType, processstep);
    }
	 public DeviceDTO getDeviceDTO() {
	 DeviceDTO deviceDTO = new DeviceDTO();
	 deviceDTO.setCreatedBy("admin");
	 deviceDTO.setSerialNo("MP24-001");
	 deviceDTO.setDeviceId("1234");
    deviceDTO.setDeviceType(getDeviceTypeDTO());
    return deviceDTO;
	 }
	
	 public DeviceTypeDTO getDeviceTypeDTO() {
	 DeviceTypeDTO deviceTypeDTO = new DeviceTypeDTO();
	 deviceTypeDTO.setName("MP24");
    return deviceTypeDTO;
	 }

	String sampleresultDTO;
	String sampleresultEntity;
	SampleResults sampleResults;
	SampleResultsDTO sampleResultsDTO;
	String runresultjsonString;
	RunResultsDTO runResultsDTO;
	public static final String jsonSampleResultentity = "src/test/java/resource/sampleentity.json";
	public static final String jsonForrunresult = "src/test/java/resource/RunResultPositive.json";
	public static final String jsonForrunResultDPCR = "src/test/java/resource/RunResultPositivedPCR.json";
	public static final String jsonForSucessSampleResult = "src/test/java/resource/sampleResults.json";
	public static final String jsonRunResultentity = "src/test/java/resource/RunResultEntity.json";
	public static final String jsonRunResultLPentity = "src/test/java/resource/RunResultUpdate.json";
	

	private RunResults getRunresult() throws IOException {
		String runresultentitystring = JsonFileReaderAsString.getJsonfromFile(jsonRunResultentity);
		RunResults results = objectMapper.readValue(runresultentitystring, RunResults.class);
		return results;
	}
	
    private RunResults getRunresultLP() throws IOException {
        String runresultentitystring = JsonFileReaderAsString.getJsonfromFile(jsonRunResultLPentity);
        RunResults results = objectMapper.readValue(runresultentitystring, RunResults.class);
        return results;
    }
	
	@SuppressWarnings("unused")
    private RunResults getRunresultDPCR() throws IOException {
        String runresultentitystring = JsonFileReaderAsString.getJsonfromFile(jsonForrunResultDPCR);
        RunResults results = objectMapper.readValue(runresultentitystring, RunResults.class);
        return results;
    }

	private SampleResults getSampleresults() throws IOException {
		sampleresultEntity = JsonFileReaderAsString.getJsonfromFile(jsonSampleResultentity);
		sampleResults = objectMapper.readValue(sampleresultEntity, SampleResults.class);
		return sampleResults;

	}

	private SampleResultsDTO getSampleresultsDTO() throws IOException {
		sampleresultDTO = JsonFileReaderAsString.getJsonfromFile(jsonForSucessSampleResult);
		sampleResultsDTO = objectMapper.readValue(sampleresultDTO, SampleResultsDTO.class);
		return sampleResultsDTO;

	}

	private RunResultsDTO getrunresultsDTO() throws IOException {
		runresultjsonString = JsonFileReaderAsString.getJsonfromFile(jsonForrunresult);
		runResultsDTO = objectMapper.readValue(runresultjsonString, RunResultsDTO.class);
		return runResultsDTO;
	}
	
	private RunResultsDTO getrunresultsDTODPCR() throws IOException {
		runresultjsonString = JsonFileReaderAsString.getJsonfromFile(jsonForrunResultDPCR);
		runResultsDTO = objectMapper.readValue(runresultjsonString, RunResultsDTO.class);
		return runResultsDTO;
	}

	@Test
	public void getInWorkflowOrderCountTest() throws Exception {
		Mockito.when(sampleResultsReadRepository.getInWorkFlowOrdersCount()).thenReturn(1l);
		Response response = runCrudRestApiImpl.getInWorkflowOrderCount();
		Assert.assertEquals(response.getStatus(), 200);

	}

	/*@Test(expectedExceptions = NullPointerException.class)
	public void getJasperHTMLReportTest() throws Exception {
		configAssayService();
		PowerMockito.mockStatic(java.util.ResourceBundle.class);
		Locale locale = new Locale("fr", "FR");
		PowerMockito.whenNew(Locale.class).withAnyArguments().thenReturn(locale);
		Mockito.when(java.util.ResourceBundle.getBundle("amm", locale)).thenReturn(resourceBundle);
		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put("REPORT_LOCALE", locale);
		parameters.put("REPORT_RESOURCE_BUNDLE", resourceBundle);
		parameters.put("imagePath", "C:/Users/dasari.r/Desktop/roche.png");
		Mockito.when(JasperFillManager.fillReport(jasperReport, parameters, dataSource)).thenReturn(jasperPrint);
		runCrudRestApiImpl.getJasperHTMLReport();
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void getJasperCSVReportTest() throws Exception {
		configAssayService();

		Mockito.when(JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource)).thenReturn(jasperPrint);
		PowerMockito.whenNew(JRCsvExporter.class).withNoArguments().thenReturn(csvExporter);
		PowerMockito.whenNew(SimpleCsvExporterConfiguration.class).withNoArguments().thenReturn(exportConfiguration);
		PowerMockito.whenNew(SimpleWriterExporterOutput.class).withAnyArguments().thenReturn(exporterOutput);
		runCrudRestApiImpl.getJasperCSVReport();
	}*/

	@Test
	public void getJasperPDFReportByLocaleTest() throws Exception {
		String language = "en";
		String country = "US";
		String accessioningid = "8001";
		
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        String url = "http://localhost";
        String accessioningId ="8001";
        Mockito.when(orderIntegrationService.searchByAccessioningId(Mockito.anyString())).thenReturn(listOrderList );
        Mockito.when(RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/orders?accessioningID=",
            accessioningId , null))
                .thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get(new GenericType<List<OrderDetailsDTO>>() {
        })).thenReturn(orderList);
		try {
            runCrudRestApiImpl.getJasperPDFReportByLocale(language, country, accessioningid);
        } catch (Exception e) {
         System.out.println(e.getMessage());
        }
	}
	
	/*@Test(expectedExceptions = FileNotFoundException.class)
	public void getWorkflowReportTest() throws Exception {
		String language = "en";
		String country = "US";
		String rocheImageURL = "http://localhost";
		String texturl = "http://localhost";
		String titleImageURL = "http://localhost";
		configOrderService();
		PowerMockito.mockStatic(java.util.ResourceBundle.class);
		URL url1 = PowerMockito.mock(URL.class);
		Mockito.when(JasperCompileManager.compileReport(jasperInput)).thenReturn(jasperReport);
		Locale locale = new Locale(language, country);
		PowerMockito.whenNew(Locale.class).withAnyArguments().thenReturn(locale);
		Mockito.when(java.util.ResourceBundle.getBundle("amm", locale)).thenReturn(resourceBundle);
		Mockito.when(RestClientUtil.getUrlString("pas.roche_image_url", "", "", "", null)).thenReturn(rocheImageURL);
		Mockito.when(RestClientUtil.getUrlString("pas.address_text_url", "", "", "", null)).thenReturn(texturl);
		Mockito.when(RestClientUtil.getUrlString("pas.title_image_url", "", "", "", null)).thenReturn(titleImageURL);

		PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url1);
		Mockito.when(url1.openStream()).thenReturn(in1);
		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put("REPORT_LOCALE", locale);
		parameters.put("REPORT_RESOURCE_BUNDLE", resourceBundle);
		parameters.put("headerImage", titleImageURL);
		parameters.put("rocheImage", rocheImageURL);
		String text = null;
		parameters.put("textDescription", text);
		Mockito.when(JasperFillManager.fillReport(jasperReport, parameters, dataSource)).thenReturn(jasperPrint);
		PowerMockito.whenNew(SimplePdfExporterConfiguration.class).withNoArguments().thenReturn(configuration);
		runCrudRestApiImpl.getJasperPDFReportByLocale(language, country, text);
	}*/
	/*@Test(expectedExceptions = NullPointerException.class)
	public void getJasperPDFReportTest() throws Exception {
		configOrderService();
		Mockito.when(JasperCompileManager.compileReport(corrected_rep_template)).thenReturn(jasperReport);
		Mockito.when(JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource)).thenReturn(jasperPrint);
		PowerMockito.whenNew(SimplePdfExporterConfiguration.class).withNoArguments().thenReturn(configuration);
		PowerMockito.whenNew(ByteArrayOutputStream.class).withNoArguments().thenReturn(pdfReportStream);
		Mockito.doNothing().when(pdfExporter).exportReport();
		runCrudRestApiImpl.getJasperPDFReport();
	}*/

	/*public void configOrderService() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(JasperCompileManager.class);
		PowerMockito.mockStatic(JasperFillManager.class);
		String url = "http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/order", "", null))
				.thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
		Mockito.when(orderClient.get(new GenericType<List<OrderDetailsDTO>>() {
		})).thenReturn(orderList);
		PowerMockito.whenNew(JRBeanCollectionDataSource.class).withAnyArguments().thenReturn(dataSource);
		PowerMockito.whenNew(JRPdfExporter.class).withNoArguments().thenReturn(pdfExporter);
		PowerMockito.whenNew(SimpleExporterInput.class).withAnyArguments().thenReturn(simpleExporterInput);
		PowerMockito.whenNew(SimpleOutputStreamExporterOutput.class).withAnyArguments().thenReturn(output);

	}*/

	/*@Test(expectedExceptions = NullPointerException.class)
	public void jasperSubReportTest() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(JasperCompileManager.class);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(JasperFillManager.class);

		String url = "http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay", "", null))
				.thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<AssayTypeDTO>>() {
		})).thenReturn(listAssays);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("subreportParameter", jasperSubReport);

		Mockito.when(JasperCompileManager.compileReport(subreport_template)).thenReturn(jasperReport);
		Mockito.when(JasperCompileManager.compileReport(subreport_child_template)).thenReturn(jasperSubReport);
		Mockito.when(JasperFillManager.fillReport(jasperReport, parameters, dataSource)).thenReturn(jasperPrint);
		PowerMockito.whenNew(SimpleExporterInput.class).withAnyArguments().thenReturn(simpleExporterInput);
		PowerMockito.whenNew(SimpleOutputStreamExporterOutput.class).withAnyArguments().thenReturn(output);
		Mockito.doNothing().when(pdfExporter).exportReport();
		runCrudRestApiImpl.jasperSubReport();
	}*/

	public void configAssayService() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		PowerMockito.mockStatic(JasperCompileManager.class);
		PowerMockito.mockStatic(JasperFillManager.class);
		String url = "http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay", "", null))
				.thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<AssayTypeDTO>>() {
		})).thenReturn(listAssays);
		Mockito.when(JasperCompileManager.compileReport(template)).thenReturn(jasperReport);
		PowerMockito.whenNew(JRBeanCollectionDataSource.class).withAnyArguments().thenReturn(dataSource);
		PowerMockito.whenNew(JRPdfExporter.class).withNoArguments().thenReturn(pdfExporter);
		PowerMockito.whenNew(SimpleExporterInput.class).withAnyArguments().thenReturn(simpleExporterInput);
		PowerMockito.whenNew(SimpleOutputStreamExporterOutput.class).withAnyArguments().thenReturn(output);

	}

	@Test
	public void invokeOrderServiceTest() throws HMTPException, UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		String url = "http://localhost";
		String accessioningId ="8001";
        Mockito.when(RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/orders?accessioningID=",
            accessioningId , null))
				.thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
		Mockito.when(orderClient.get(new GenericType<List<OrderDetailsDTO>>() {
		})).thenReturn(orderList);
		runCrudRestApiImpl.invokeOrderService(accessioningId);
	}
	
	/*@Test
    public void invokeOrderServiceElseTest() throws HMTPException, UnsupportedEncodingException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClientUtil.class);
        String url = "http://localhost";
        Mockito.when(RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/ord", "", null))
                .thenReturn(url);
        Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(orderClient);
        Mockito.when(orderClient.get(new GenericType<List<OrderDetailsDTO>>() {
        })).thenReturn(orderList);
        runCrudRestApiImpl.invokeOrderService("");
    }*/

	@Test
	public void invokeServiceTest() throws HMTPException, UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		String url = "http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay", "", null))
				.thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<AssayTypeDTO>>() {
		})).thenReturn(listAssays);
		runCrudRestApiImpl.invokeService();
	}

	@Test(expectedExceptions = Exception.class)
	public void invokeServiceNegTest() throws Exception {
		Mockito.doThrow(Exception.class).when(runCrudRestApiImpl.invokeService());

	}

	@Test
	public void getProcessStepActionListTest() throws UnsupportedEncodingException, HMTPException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/",
				assayType + "/" + "processstepaction", null)).thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {
		})).thenReturn(processStepActionDTOList);
		runCrudRestApiImpl.getProcessStepActionList(assayType);
	}

	@Test
	public void getAssayTypeListTest() throws UnsupportedEncodingException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay?assayType=", assayType,
				null)).thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(assayClient);
		Mockito.when(assayClient.get(new GenericType<List<AssayTypeDTO>>() {
		})).thenReturn(listAssays);
		runCrudRestApiImpl.getAssayTypeList(assayType);
	}

	public RunReagentsAndConsumables getRunReagentsAndConsumables(){
		RunReagentsAndConsumables runReagentsAndConsumables  = new RunReagentsAndConsumables();
		runReagentsAndConsumables.setAttributeName("attributeName");
		runReagentsAndConsumables.setAttributeValue("attributeValue");
		return runReagentsAndConsumables;
	}
	
	public RunResultsDetail getRunResultsDetail(){
		RunResultsDetail runResultsDetail = new RunResultsDetail();
		runResultsDetail.setAttributeName("attributeName");
		runResultsDetail.setAttributeValue("attributeValue");
		return runResultsDetail;
		
	}
	
	public RunResultsDetailDTO getRunResultsDetailDTO(){
	    RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
	    runResultsDetailDTO.setAttributeName("attributeName");
	    runResultsDetailDTO.setAttributeValue("attributeValue");
        return runResultsDetailDTO;
        
    }
	
	
	@Test
	public void updateRunResultsTest() throws HMTPException, IOException{
		
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
		Mockito.when(runResultsReadRepository.findRunResultByRunResultId(Mockito.anyLong(),Mockito.anyLong())).thenReturn(getRunresult());
		Mockito.when(mapDTOToEntity.getRunResultsFromDTO(Mockito.any(RunResultsDTO.class))).thenReturn(getRunresult());
		Mockito.when(runResultsReadRepository.findCompanyIdByRunResultId(Mockito.anyLong())).thenReturn(1L);
		Mockito.when(mapDTOToEntity
						.getRunReagentsAndConsumablesFromDTO(Mockito.any(RunResultsDTO.class))).thenReturn(reagentsAndConsumables);
		Mockito.when(runReagentsAndConsumablesWriteRepository.save(Mockito.any(RunReagentsAndConsumables.class))).thenReturn(getRunReagentsAndConsumables());
		Mockito.when(mapDTOToEntity.getRunResultsDetailFromDTO(Mockito.any(RunResultsDTO.class))).thenReturn(runResultsDetails);
		Mockito.when(runResultsDetailWriteRepository.save(Mockito.any(RunResultsDetail.class))).thenReturn(getRunResultsDetail());
		Mockito.when(runResultsWriteRepository.save(Mockito.any(RunResults.class), Mockito.anyLong())).thenReturn(getRunresult());
		runCrudRestApiImpl.updateRunResults(getrunresultsDTO());
		
	}
	
	
	@Test
	public void updateRunResultsNegativeTest() throws HMTPException, IOException{
		List<RunReagentsAndConsumables> reagentsAndConsumables = new ArrayList<>();
		reagentsAndConsumables.add(getRunReagentsAndConsumables());
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
		Mockito.when(runResultsReadRepository.findRunResultByRunResultId(Mockito.anyLong(),Mockito.anyLong())).thenReturn(null);
		runCrudRestApiImpl.updateRunResults(getrunresultsDTO());
	}
	
	@Test
	public void getProcessstepForAssayAndDeviceTest() throws HMTPException, UnsupportedEncodingException{
		PowerMockito.mockStatic(RestClientUtil.class);
		String urlForAssay ="http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay",
				"/"+"NIPTDPCR"+ "/processstepaction?deviceType=" +"MP24", null)).thenReturn(urlForAssay );
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(urlForAssay, "UTF-8"), null)).thenReturn(ammClient);
		Mockito.when(ammClient.get(new GenericType<List<ProcessStepActionDTO>>() {
			})).thenReturn(processStepActionDTOList);
		runCrudRestApiImpl.getProcessstepForAssayAndDevice("NIPTDPCR", "MP24");
	}
	
	
	@Test
	public void getRunresultsByDeviceTest() throws HMTPException, IOException{
		PowerMockito.mockStatic(ThreadSessionManager.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
		Mockito.when(runResultsReadRepository.findRunResultsByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getRunresult());
		Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class))).thenReturn(getrunresultsDTO());
		
		
		Mockito.when(mapEntityToDTO
							.convertRunResultsDetailToRunResultsDetailDTODTO(Mockito.anyList())).thenReturn(runresultdetaildto);
		
		
		/*Mockito.when(mapEntityToDTO
            .convertRunResultsDetailToRunResultsDetailDTODTO((List<RunResultsDetail>)getRunresult().getRunResultsDetail())).thenReturn(runresultdetaildto);*/
		sampleResultIds.add(5676L);
		sampleResultIds.add(1345L);
		Mockito.when(sampleResultsReadRepository.findIdByAccesssioningId(Mockito.anyString(),Mockito.anyLong())).thenReturn(sampleResultIds);
		Mockito.when(sampleResultsReadRepository.findOne(Mockito.anyLong())).thenReturn(getSampleresults());
		Mockito.when(mapEntityToDTO
                            .mapsampleResultstoProcessDTO(Mockito.any(SampleResults.class))).thenReturn(getProcessStepValuesDTO());
		Mockito.when(runResultsReadRepository.findRunResultByRunResultId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(getRunresult());
		Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class))).thenReturn(getrunresultsDTO());
		Mockito.when(mapEntityToDTO
							.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(Mockito.anyList())).thenReturn(runReagentsAndConsumablesDTO);
		Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(Mockito.any(SampleResults.class))).thenReturn(getSampleresultsDTO());
		Mockito.when( mapEntityToDTO
								.convertSampleProtocolToSampleProtocolDTO(Mockito.anyList())).thenReturn(sampleProtocolDTo);
		Mockito.when( mapEntityToDTO
								.convertSampleReagentsAndConsumablesToReagentDTO(Mockito.anyList())).thenReturn(samplereagentdto);
		Mockito.when( mapEntityToDTO
								.convertSampleResultsDetailToSampleResultsDetailDTO(Mockito.anyList())).thenReturn(sampledetaildto);
		runCrudRestApiImpl.getRunresultsByDevice("MP24");
	}
	
	@Test
    public void getRunresultsByDeviceLPTest() throws HMTPException, IOException{
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(runResultsReadRepository.findRunResultsByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getRunresultLP());
        Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class))).thenReturn(getrunresultsDTO());
        Mockito.when(mapEntityToDTO
                            .convertRunResultsDetailToRunResultsDetailDTODTO(Mockito.anyList())).thenReturn(runresultdetaildto);
        sampleResultIds.add(5676L);
        sampleResultIds.add(1345L);
        Mockito.when(sampleResultsReadRepository.findIdByAccesssioningId(Mockito.anyString(),Mockito.anyLong())).thenReturn(sampleResultIds);
        Mockito.when(sampleResultsReadRepository.findOne(Mockito.anyLong())).thenReturn(getSampleresults());
        Mockito.when(mapEntityToDTO
                            .mapsampleResultstoProcessDTO(Mockito.any(SampleResults.class))).thenReturn(getProcessStepValuesDTO());
        Mockito.when(runResultsReadRepository.findRunResultByRunResultId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(getRunresultLP());
        Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class))).thenReturn(getrunresultsDTO());
        Mockito.when(mapEntityToDTO
                            .convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(Mockito.anyList())).thenReturn(runReagentsAndConsumablesDTO);
        Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(Mockito.any(SampleResults.class))).thenReturn(getSampleresultsDTO());
        Mockito.when( mapEntityToDTO
                                .convertSampleProtocolToSampleProtocolDTO(Mockito.anyList())).thenReturn(sampleProtocolDTo);
        Mockito.when( mapEntityToDTO
                                .convertSampleReagentsAndConsumablesToReagentDTO(Mockito.anyList())).thenReturn(samplereagentdto);
        Mockito.when( mapEntityToDTO
                                .convertSampleResultsDetailToSampleResultsDetailDTO(Mockito.anyList())).thenReturn(sampledetaildto);
        runCrudRestApiImpl.getRunresultsByDevice("LP24");
    }
	
	@Test
    public void getRunresultsByDeviceDPCRTest() throws HMTPException, IOException{
        PowerMockito.mockStatic(ThreadSessionManager.class);
        Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
        Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
        Mockito.when(runResultsReadRepository.findRunResultsByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getRunresultLP());
        Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class))).thenReturn(getrunresultsDTO());
        Mockito.when(mapEntityToDTO
                            .convertRunResultsDetailToRunResultsDetailDTODTO(Mockito.anyList())).thenReturn(runresultdetaildto);
        sampleResultIds.add(5676L);
        sampleResultIds.add(1345L);
        Mockito.when(sampleResultsReadRepository.findIdByAccesssioningId(Mockito.anyString(),Mockito.anyLong())).thenReturn(sampleResultIds);
        Mockito.when(sampleResultsReadRepository.findOne(Mockito.anyLong())).thenReturn(getSampleresults());
        Mockito.when(mapEntityToDTO
                            .mapsampleResultstoProcessDTO(Mockito.any(SampleResults.class))).thenReturn(getProcessStepValuesDTO());
        Mockito.when(runResultsReadRepository.findRunResultByRunResultId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(getRunresultLP());
        Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class))).thenReturn(getrunresultsDTO());
        Mockito.when(mapEntityToDTO
                            .convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(Mockito.anyList())).thenReturn(runReagentsAndConsumablesDTO);
        Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(Mockito.any(SampleResults.class))).thenReturn(getSampleresultsDTO());
        Mockito.when( mapEntityToDTO
                                .convertSampleProtocolToSampleProtocolDTO(Mockito.anyList())).thenReturn(sampleProtocolDTo);
        Mockito.when( mapEntityToDTO
                                .convertSampleReagentsAndConsumablesToReagentDTO(Mockito.anyList())).thenReturn(samplereagentdto);
        Mockito.when( mapEntityToDTO
                                .convertSampleResultsDetailToSampleResultsDetailDTO(Mockito.anyList())).thenReturn(sampledetaildto);
        runCrudRestApiImpl.getRunresultsByDevice("DPCR");
    }
	
	/*@Test
	public void getRunresultsByDeviceNegativeTest() throws HMTPException, IOException{
	    PowerMockito.mockStatic(ThreadSessionManager.class);
	    Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
	    Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
	    Mockito.when(runResultsReadRepository.findRunResultsByDeviceRunId(Mockito.anyString(), Mockito.anyLong())).thenReturn(getRunresult());
	    Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class))).thenReturn(getrunresultsDTO());
	    runCrudRestApiImpl.getRunresultsByDevice("HTP-001");
	}*/
	
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesBarCode(){
		UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
		uniqueReagentsAndConsumables.setAttributeName("Barcode");
		uniqueReagentsAndConsumables.setAttributeValue("456789");
		return uniqueReagentsAndConsumables;
		
	}
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesReagents(){
        UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
        uniqueReagentsAndConsumables.setAttributeName("Reagents");
        uniqueReagentsAndConsumables.setAttributeValue("MGP;00000001;00023");
        return uniqueReagentsAndConsumables;
        
    }
	
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesLotNo(){
		UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
		uniqueReagentsAndConsumables.setAttributeName("lotNo");
		uniqueReagentsAndConsumables.setAttributeValue("456785678");
		return uniqueReagentsAndConsumables;
		
	}
	
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesVolume(){
		UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
		uniqueReagentsAndConsumables.setAttributeName("volume");
		uniqueReagentsAndConsumables.setAttributeValue("34");
		return uniqueReagentsAndConsumables;
		
	}
	
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesExpDate(){
		UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
		uniqueReagentsAndConsumables.setAttributeName("expDate");
		uniqueReagentsAndConsumables.setAttributeValue("20180912332211");
		return uniqueReagentsAndConsumables;
		
	}
	
	
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesPosition(){
		UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
		uniqueReagentsAndConsumables.setAttributeName("position");
		uniqueReagentsAndConsumables.setAttributeValue("2");
		return uniqueReagentsAndConsumables;
	}
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesReagentVersion(){
		UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
		uniqueReagentsAndConsumables.setAttributeName("reagentVesion");
		uniqueReagentsAndConsumables.setAttributeValue("0.2");
		return uniqueReagentsAndConsumables;
	}
	
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesReagentKitName(){
		UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
		uniqueReagentsAndConsumables.setAttributeName("reagentKitName");
		uniqueReagentsAndConsumables.setAttributeValue("kit");
		return uniqueReagentsAndConsumables;
	}
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesInternalControlNegative(){
        UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
        uniqueReagentsAndConsumables.setAttributeName("Internal Control");
        uniqueReagentsAndConsumables.setAttributeValue("Hexadecane;00000000;01991;000122");
        return uniqueReagentsAndConsumables;
    }
	public UniqueReagentsAndConsumables getUniqueReagentsAndConsumablesInternalControlPositive(){
        UniqueReagentsAndConsumables uniqueReagentsAndConsumables = new UniqueReagentsAndConsumables();
        uniqueReagentsAndConsumables.setAttributeName("Internal Control");
        uniqueReagentsAndConsumables.setAttributeValue("Hexadecane;00000000;01991;2012-01-31 23:59:59.999");
        return uniqueReagentsAndConsumables;
    }
	
	@Test
    public void getListOfConsumablesDTOTest(){
        runCrudRestApiImpl.getListOfConsumablesDTO(listOfReagents );
    }
	@Test
	public void getListOfConsumablesDTONegativeTest(){
		Collection<UniqueReagentsAndConsumables> listReagents = new ArrayList<>();
		listReagents.add(getUniqueReagentsAndConsumablesInternalControlNegative());
        runCrudRestApiImpl.getListOfConsumablesDTO(listReagents );
	}
	@Test
    public void getListOfConsumablesDTOPositiveTest(){
        Collection<UniqueReagentsAndConsumables> listReagents = new ArrayList<>();
        listReagents.add(getUniqueReagentsAndConsumablesInternalControlPositive());
        runCrudRestApiImpl.getListOfConsumablesDTO(listReagents );
    }
	Collection<SampleResultsDTO> listOfSampleDTO = new ArrayList<>();
	
	
	@Test
	public void getListOfSamplesDataDTOTest() throws IOException{
		listOfSampleDTO.add(getSampleresultsDTO());
		runCrudRestApiImpl.getListOfSamplesDataDTO(listOfSampleDTO);
	}
	
	@Test
	public void getListOfReagentDTOTest(){
		
		runCrudRestApiImpl.getListOfReagentDTO(listOfReagents);
	}
	@Test
    public void getListOfReagentDTOPositiveTest(){
        
        Collection<UniqueReagentsAndConsumables> listReagents = new ArrayList<>();
        listReagents.add(getUniqueReagentsAndConsumablesReagents());
        runCrudRestApiImpl.getListOfReagentDTO(listReagents );
    }
    
	@Test
	public void getlistOfReagentsTest(){
		runCrudRestApiImpl.getlistOfReagents(listOfReagents);
	}
	
	/*@Test
	public void getFlagsForAssayAndDeviceTest() throws HMTPException, UnsupportedEncodingException{
		PowerMockito.mock(RestClientUtil.class);
		Mockito.when(RestClientUtil.getUrlString("pas.amm_api_url", "", "/json/rest/api/v1/assay/flagDescriptions",
				"/"+"MP24"+"?assayType="+"NIPTDPCR", null)).thenReturn(url);
		Mockito.when(RestClientUtil.getBuilder(URLEncoder.encode(url, "UTF-8"), null)).thenReturn(ammClient);
		List<FlagsDataDTO> listFlags  = new ArrayList<>();
		listFlags.add(getFlagsDataDTO());
		Mockito.when(ammClient.get(new GenericType<List<FlagsDataDTO>>() {
			})).thenReturn(listFlags);
		runCrudRestApiImpl.getFlagsForAssayAndDevice("NIPTDPCR", "MP24");
	}*/
	public FlagsDataDTO getFlagsDataDTO(){
		FlagsDataDTO flagsDTO = new FlagsDataDTO();
		flagsDTO.setAssayType("NIPTDPCR");
		flagsDTO.setFlagCode("F");
		flagsDTO.setSeverity("High");
		flagsDTO.setDescription("flag description");
		return flagsDTO;
		
	}
	

	@Test
	public void getWorkflowReportByReportTest() throws Exception {
		PowerMockito.mockStatic(JasperCompileManager.class);
		Mockito.when(JasperCompileManager.compileReport(Mockito.any(InputStream.class))).thenReturn(jasperReport);
		PowerMockito.mockStatic(ThreadSessionManager.class);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.when(ThreadSessionManager.currentUserSession()).thenReturn(userSession);
		Mockito.when(ThreadSessionManager.currentUserSession().getAccessorCompanyId()).thenReturn(1L);
		Mockito.when(runResultsReadRepository.findRunResultsByDeviceRunId(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(getRunresult());
		Mockito.when(mapEntityToDTO.getRunResultsDTOFromRunResults(Mockito.any(RunResults.class)))
				.thenReturn(getrunresultsDTO());
		Mockito.when(mapEntityToDTO.convertRunResultsDetailToRunResultsDetailDTODTO(Mockito.anyList()))
				.thenReturn(runresultdetaildto);
		Mockito.when(mapEntityToDTO.convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(Mockito.anyList()))
				.thenReturn(runReagentsAndConsumablesDTO);
		Mockito.when(mapEntityToDTO.getSampleDTOFromSampleResults(Mockito.any(SampleResults.class)))
				.thenReturn(getSampleresultsDTO());
		Mockito.when(mapEntityToDTO.convertSampleProtocolToSampleProtocolDTO(Mockito.anyList()))
				.thenReturn(sampleProtocolDTo);
		Mockito.when(mapEntityToDTO.convertSampleReagentsAndConsumablesToReagentDTO(Mockito.anyList()))
				.thenReturn(samplereagentdto);
		Mockito.when(mapEntityToDTO.convertSampleResultsDetailToSampleResultsDetailDTO(Mockito.anyList()))
				.thenReturn(sampledetaildto);
		String texturl = "http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.address_text_url", "", "", "", null)).thenReturn(texturl);
		String rocheImageURL = "http://localhost";
		String titleImageURL = "http://localhost";
		Mockito.when(RestClientUtil.getUrlString("pas.title_image_url", "", "", "", null)).thenReturn(titleImageURL);
		Mockito.when(RestClientUtil.getUrlString("pas.roche_image_url", "", "", "", null)).thenReturn(rocheImageURL);
		URL url1 = PowerMockito.mock(URL.class);
		BufferedInputStream in1 = PowerMockito.mock(BufferedInputStream.class);
		PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments(Mockito.anyString()).thenReturn(url1);
		PowerMockito.when(url1.openStream()).thenReturn(in1);
		PowerMockito.whenNew(BufferedInputStream.class).withAnyArguments().thenReturn(in1);
		try {
			runCrudRestApiImpl.getWorkflowReport("pdf", "pdf", "HTP-001");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public OrderDetailsDTO getOrderDetailsDTO() {
        OrderDetailsDTO order = new OrderDetailsDTO();
        order.setAccessioningId("8001");
        order.setPatientFirstName("FirstName");
        order.setPatientLastName("LastName");
        order.setPatientId(12L);
        order.setPatientGender("Male");
        order.setPatientContactNo("8989898900");
        order.setPatient(new PatientDTO());
        order.setAssay(getAssayDTO());
        return order;
    }
	public OrderDetailsDTO getOrderDetailsDTOPatientFirstAndLastName() {
		OrderDetailsDTO order = new OrderDetailsDTO();
		order.setAccessioningId("8001");
		order.setPatientFirstName("FirstName");
		order.setPatientLastName("LastName");
		order.setPatientId(12L);
		order.setPatientGender("Male");
		order.setPatientContactNo("8989898900");
        order.setPatient(getPatientDTOFirstAndLastName());
        order.setAssay(getAssayDTO());
		return order;
	}
	
	
	public OrderDetailsDTO getOrderDetailsDTOPatientFirstName() {
        OrderDetailsDTO order = new OrderDetailsDTO();
        order.setAccessioningId("8001");
        order.setPatientFirstName("FirstName");
        order.setPatientLastName("LastName");
        order.setPatientId(12L);
        order.setPatientGender("Male");
        order.setPatientContactNo("8989898900");
        order.setPatient(getPatientDTOFirstName());
        return order;
    }

	
	public PatientDTO getPatientDTOFirstAndLastName() {
	    PatientDTO patient = new PatientDTO();
	    patient.setPatientFirstName("firstName");
	    patient.setPatientLastName("lastName");
	    patient.setPatientDOB("12/07/1992");
	    patient.setClinicName("Roche");
	    patient.setOtherClinicianName("Roche Diagnostics");
	    patient.setRefClinicianName("Roche Ref Clinical");
        return patient;
	}
	
	public PatientDTO getPatientDTOFirstName() {
        PatientDTO patient = new PatientDTO();
        patient.setPatientFirstName("firstName");
        patient.setPatientLastName("lastName");
        return patient;
    }
  
    
	public AssayDTO getAssayDTO() {
	AssayDTO assay = new AssayDTO();
	assay.setGestationalAgeDays(2);
	assay.setGestationalAgeWeeks(2);
	assay.setFetusNumber("2");
	assay.setIvfStatus("Self");
	assay.setCollectionDate(new Timestamp(System.currentTimeMillis()));
	assay.setReceivedDate(new Timestamp(System.currentTimeMillis()));
    return assay;
	}
	
	public AssayTypeDTO getAssayTypeDTO() {
		AssayTypeDTO assay = new AssayTypeDTO();
		assay.setAssayType("NIPTHTP");
		assay.setAssayTypeId(1L);
		return assay;
	}

	public ProcessStepActionDTO getProcessStepActionDTO() {
		ProcessStepActionDTO process = new ProcessStepActionDTO();
		process.setAssayType("NIPTHTP");
		process.setDeviceType("MP24");
		process.setProcessStepName("NA Extraction");
		return process;
	}
	
	public ProcessStepValuesDTO getProcessStepValuesDTO(){
	    ProcessStepValuesDTO processStepValuesDTO = new ProcessStepValuesDTO();
	    processStepValuesDTO.setRunResultId(4643L);
	    processStepValuesDTO.setAccesssioningId("672375");
	    processStepValuesDTO.setSampleResultId(8577L);
	    processStepValuesDTO.setSampleResultsDetailDTO(sampledetaildto);
	    return processStepValuesDTO;
	}

}
