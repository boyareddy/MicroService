package com.roche.connect.rmm.testNG;

import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.client.Invocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.data.domain.Page;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.dmm.DeviceDTO;
import com.roche.connect.common.dmm.DeviceTypeDTO;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderElements;
import com.roche.connect.common.rmm.dto.SearchOrder;
import com.roche.connect.common.rmm.dto.SearchOrderElements;
import com.roche.connect.common.rmm.dto.SearchRunResult;
import com.roche.connect.common.rmm.dto.SearchRunResultElements;
import com.roche.connect.common.util.RunStatusConstants;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.readrepository.RunResultsReadRepository;
import com.roche.connect.rmm.readrepository.SampleResultsReadRepository;
import com.roche.connect.rmm.rest.RunCrudRestApiImpl;
import com.roche.connect.rmm.services.DeviceIntegrationService;
import com.roche.connect.rmm.services.OrderIntegrationService;
import com.roche.connect.rmm.services.SearchService;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.util.MapEntityToDTO;

@PrepareForTest({ RestClientUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class SearchServiceTest {

	

	@Mock
	Invocation.Builder assayClient;

	@Mock
	ObjectMapper objectMapper;

	String token = "1";

	@InjectMocks
	RunCrudRestApiImpl runCrudRestApiImpl = new RunCrudRestApiImpl();

	@InjectMocks
	SearchService searchServices = new SearchService();

	@Mock
	private HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	private SearchService searchService = org.mockito.Mockito.mock(SearchService.class);

	@Mock
	private RunResultsReadRepository runResultsReadRepository = org.mockito.Mockito
	.mock(RunResultsReadRepository.class);

	@Mock
	private SampleResultsReadRepository sampleResultsReadRepository = org.mockito.Mockito
	.mock(SampleResultsReadRepository.class);

	@Mock
	private OrderIntegrationService orderIntegrationService = org.mockito.Mockito.mock(OrderIntegrationService.class);

	@Mock
	private DeviceIntegrationService deviceIntegrationService = Mockito.mock(DeviceIntegrationService.class);

	@SuppressWarnings("unchecked")
	@Mock
	private Page<RunResults> RunResultsPage = Mockito.mock(Page.class);

	@SuppressWarnings("unchecked")
	@Mock
	private Page<SampleResults> sampleResultsPage = Mockito.mock(Page.class);

	@Spy
	@InjectMocks
	private MapEntityToDTO mapEntityToDTO;

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	private static Logger logger = LogManager.getLogger(SearchServiceTest.class);

	@Test(priority = 1)
	public void searchResultImplTest() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(searchService.getSearchOrderElements("RND100", 1,100)).thenReturn(getSearchOrderElements());
		Mockito.when(searchService.getSearchRunResultElements(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(getSearchRunResultElements());
		try {
			runCrudRestApiImpl.searchResult("RND100", 1, 100, null);
		} catch (Exception e) {
			logger.error("Error occured in SearchServiceTest::searchResultImplTest");
		}
	}


	@Test(priority = 2)
	public void searchResultSearchOrderElseTest() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(searchService.getSearchOrderElements(Mockito.anyString(), Mockito.anyInt(),Mockito.anyInt())).thenReturn(getSearchOrderElements());
		try {
			runCrudRestApiImpl.searchResult("RND100", 1, 100, "order");
		} catch (Exception e) {
			logger.error("Error occured in SearchServiceTest::searchResultSearchOrderElseTest");
		}
	}


	@Test(priority = 4)
	public void searchResultRunResultElementsElseTest() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(searchService.getSearchRunResultElements(Mockito.anyString(), Mockito.anyInt(),Mockito.anyInt())).thenReturn(getSearchRunResultElements());
		try {
			runCrudRestApiImpl.searchResult("RND100", 1, 100, "run");
		} catch (Exception e) {
			logger.error("Error occured in SearchServiceTest::searchResultRunResultElementsElseTest");
		}
	}


	@Test(priority = 5)
	public void searchQueryBlankTest() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);
		try {
			runCrudRestApiImpl.searchResult("", 1, 0, "run");
		} catch (Exception e) {
			logger.error("Error occured in SearchServiceTest::searchQueryBlankTest");
		}
	}


	@Test(priority = 6)
	public void searchQueryExceptionTest() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.doThrow(Exception.class).when(searchService).getSearchOrderElements(Mockito.anyString(), Mockito.anyInt(),Mockito.anyInt());
		 Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		try {
			runCrudRestApiImpl.searchResult("RND100", 1, 100, null);
		} catch (Exception e) {
			logger.error("Error occured in SearchServiceTest::searchQueryExceptionTest");
		}
	}
	
	
	@Test(priority = 7)
	public void searchRunResultElementsNullTest() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.when(searchService.getSearchOrderElements("RND100", 1,2)).thenReturn(getSearchOrderElementsWithOrders());
		Mockito.when(searchService.getSearchRunResultElements(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(getSearchRunResultElements());
		 Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		try {
			runCrudRestApiImpl.searchResult("RND100", 1, 2, null);
		} catch (Exception e) {
			logger.error("Error occured in SearchServiceTest::searchQueryExceptionTest");
		}
	}
	@Test(priority = 8)
	public void searchRunResultsTest() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);

		List<RunResults> runResultsList = new ArrayList<>();
		SearchRunResult searchRunResult = new SearchRunResult();
		searchRunResult.setDeviceRunId("RND100");

		SearchOrder searchOrder = new SearchOrder();
		searchOrder.setAccessioningId("100");

		RunResults runResults = new RunResults();
		runResults.setDeviceId("RNZ");
		runResults.setId(1L);
		runResults.setProcessStepName("NA Extraction");
		runResults.setDeviceRunId("RND100");
		runResults.setRunStatus("COMPLETED");
		runResults.setOperatorName("ADMIN");
		runResults.setAssayType("NIPTDPCR");
		runResults.setRunCompletionTime(new Timestamp(1L));
		runResultsList.add(runResults);

		RunResults runResults2 = new RunResults();
		runResults2.setDeviceId("RNZ");
		runResults2.setId(1L);
		runResults2.setProcessStepName(MessageType.MP96_NAEXTRACTION);
		runResults2.setDeviceRunId("RND1243500");
		runResults2.setRunStatus(RunStatusConstants.MP96_PENDING);
		runResults2.setOperatorName("admin");
		runResults2.setAssayType(AssayType.NIPT_DPCR);
		runResults2.setRunCompletionTime(new Timestamp(1L));
		runResultsList.add(runResults2);

		DeviceDTO deviceDTO = new DeviceDTO();
		DeviceTypeDTO deviceTypeDTO = new DeviceTypeDTO();
		deviceDTO.setSerialNo("ABCD");
		deviceTypeDTO.setName("MP96DEVICE");
		deviceDTO.setDeviceType(deviceTypeDTO);

		String containerSamplesJson = "src/test/java/resource/ContainerSampleCSVUpdate.json";
		String jsonContent = JsonFileReaderAsString.getJsonfromFile(containerSamplesJson);
		TypeReference<List<ContainerSamplesDTO>> containerSampleClass = new TypeReference<List<ContainerSamplesDTO>>() {
		};
		List<ContainerSamplesDTO> containerSamples = new ObjectMapper().readValue(jsonContent, containerSampleClass);

		Mockito.when(RunResultsPage.getContent()).thenReturn(runResultsList);
		Mockito.when(RunResultsPage.getTotalElements()).thenReturn(100L);
		Mockito.when(runResultsReadRepository.findByDeviceRunIdContainingIgnoreCaseAndCompanyId(any(String.class),
				any(long.class), Mockito.any())).thenReturn(RunResultsPage);

		Mockito.when(orderIntegrationService.getDPCRContainerSamples(any(String.class))).thenReturn(containerSamples);

		Mockito.when(deviceIntegrationService.getDevice(any(String.class))).thenReturn(deviceDTO);

		searchServices.getSearchRunResultElements("RND100", 0, 100);

	}

	@Test(priority = 9)
	public void searchOrdersTest() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);

		List<RunResults> runResultsList = new ArrayList<>();
		List<SampleResults> sampleResultList = new ArrayList<>();
		List<OrderDTO> searchOrderList = new ArrayList<>();
		RunResults runResults = new RunResults();
		SampleResults sampleResults = new SampleResults();
		OrderDTO orderDTO = new OrderDTO();
		SearchRunResult searchRunResult = new SearchRunResult();
		searchRunResult.setDeviceRunId("RND100");
		SearchOrder searchOrder = new SearchOrder();
		searchOrder.setAccessioningId("100");

		orderDTO.setAccessioningId("1001");
		orderDTO.setOrderId(200L);
		orderDTO.setOrderStatus("unassigned");
		orderDTO.setActiveFlag("F");
		orderDTO.setAssayType("NIPTDPCR");
		orderDTO.setSampleType("PLasma");
		searchOrderList.add(orderDTO);

		runResults.setDeviceId("RNZ");
		runResults.setId(1L);
		runResults.setProcessStepName("NAEXTRACTION");
		runResults.setDeviceRunId("RND100");
		runResults.setRunStatus("COMPLETED");
		runResults.setOperatorName("ADMIN");
		runResults.setAssayType("NIPTDPCR");
		runResults.setRunCompletionTime(new Timestamp(1L));
		runResultsList.add(runResults);

		sampleResults.setRunResultsId(runResults);
		sampleResults.setAccesssioningId("100");
		sampleResults.setId(1L);
		sampleResults.setOrderId(200L);
		sampleResults.setStatus("Completed");
		sampleResults.setFlag("F1");
		sampleResults.setSampleType("Plasma");

		sampleResultList.add(sampleResults);

		DeviceDTO deviceDTO = new DeviceDTO();
		DeviceTypeDTO deviceTypeDTO = new DeviceTypeDTO();
		deviceDTO.setSerialNo("ABCD");
		deviceTypeDTO.setName("MP96DEVICE");
		deviceDTO.setDeviceType(deviceTypeDTO);

		OrderElements orderElements = new OrderElements();
		orderElements.setTotalElements((long) searchOrderList.size());
		orderElements.setOrders(searchOrderList);
		Mockito.when(sampleResultsReadRepository.findByAccessioningIdContainingIgnoreCase(any(String.class),
				any(long.class), Mockito.any())).thenReturn(sampleResultsPage);

		Mockito.when(orderIntegrationService.searchOrderByAccessioningId(any(String.class), any(String.class),
				Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(orderElements);

		searchServices.getSearchOrderElements("RND100", 0, 100);

	}


	public SearchOrderElements getSearchOrderElements() {
		SearchOrderElements searchOrderElements=new SearchOrderElements();
		Collection<SearchOrder> orders = new LinkedList<>();
		searchOrderElements.setOrders(orders);
		// searchOrderElements.setTotalElements(2L);


		return searchOrderElements;
	}

	public SearchOrderElements getSearchOrderElementsWithOrders() {
		SearchOrderElements searchOrderElements=new SearchOrderElements();
		Collection<SearchOrder> orders = new LinkedList<>();
		SearchOrder searchOrder=new SearchOrder();
		searchOrder.setAccessioningId("RND100234");
		SearchOrder searchOrdersecond=new SearchOrder();
		searchOrdersecond.setAccessioningId("RND1003443");
		orders.add(searchOrder);
		orders.add(searchOrder);
		searchOrderElements.setOrders(orders);


		return searchOrderElements;
	}
	
	public SearchRunResultElements getSearchRunResultElements() {
		SearchRunResultElements searchRunResultElements=new SearchRunResultElements();

		return searchRunResultElements;
	}
}
