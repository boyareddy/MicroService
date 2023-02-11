package com.roche.connect.imm.testng;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.DPCRProcessStepName;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ErrorCode;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUSampleDetails;
import com.roche.connect.common.dpcr_analyzer.QueryMessage;
import com.roche.connect.common.dpcr_analyzer.WFMAcknowledgementMessage;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.service.DPCRAnalyzerAsyncMessageService;
import com.roche.connect.imm.service.DPCRAnalyzerMessageService;
import com.roche.connect.imm.service.DeviceRunService;
import com.roche.connect.imm.service.OrderIntegrationService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.service.WFMIntegrationService;
import com.roche.connect.imm.utils.JsonFileReaderAsString;
import com.roche.connect.imm.utils.RestClient;
import com.roche.connect.imm.utils.UrlConstants;


@PrepareForTest({ RestClientUtil.class, RestClient.class,AdmNotificationService.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class DPCRAnalyzerTest {

	public static final String DPCRANALYZERQueryMessage = "src/test/java/resource/dpcrAnalyzerQueryMessage.json";
	public static final String DPCRANALYZERQueryMessageSecond = "src/test/java/resource/dpcrAnalyzerQueryMessageSecond.json";

	@Mock
	HMTPLoggerImpl loggerImpl;
	
	String token = null;

	@InjectMocks
	private DPCRAnalyzerAsyncMessageService dpcrAnalyzerAsyncMessageService = new DPCRAnalyzerAsyncMessageService();

	@Mock
	private DPCRAnalyzerMessageService dpcrAnalyzerMessageService = org.mockito.Mockito
			.mock(DPCRAnalyzerMessageService.class);

	@Mock
	private WFMIntegrationService wfmIntegrationService= org.mockito.Mockito
			.mock(WFMIntegrationService.class);
	
	@Mock
	private RmmIntegrationService rmmIntegrationService = org.mockito.Mockito.mock(RmmIntegrationService.class);
	
	@Mock
	private AdmNotificationService admNotificationService= org.mockito.Mockito.mock(AdmNotificationService.class);

	@Mock
	private OrderIntegrationService orderIntegrationService = org.mockito.Mockito.mock(OrderIntegrationService.class);
	SampleResultsDTO sampleResultsDTO = null;

	private static Logger logger = LogManager.getLogger(DPCRAnalyzerTest.class);
	
	@Mock
	private DeviceRunService deviceRunService;
	

	@Test(priority = 1)
	public void performAsyncQueryMessage() throws IOException, HMTPException {
		
		token = "1";
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		List<String> paramList = new LinkedList<>();
		paramList.add("");
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(getQueryMessageFirst().getMessageControlId(),
				ErrorCode.PLATE_INVALID_CODE, ErrorCode.PLATE_INVALID_DESC,
				NotificationGroupType.INVALID_PLATE_ID_DPCR_DESC, paramList, token);
		dpcrAnalyzerAsyncMessageService.performAsyncQueryMessage(getQueryMessageFirst(), token);
	}

	@Test(priority = 2)
	public void performAsyncQueryMessageSecond() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		List<String> paramList = new LinkedList<>();
		paramList.add("");
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, null, null,
				getQueryMessage().getPlateId().get(0), null, null, token)).thenReturn(getSampleResults());
		List<String> paramListSecond = new LinkedList<>();
		paramListSecond.add(getQueryMessage().getPlateId().get(0));

		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(getQueryMessage().getMessageControlId(),
				ErrorCode.PLATE_MULTIPLE_SAMPLES_CODE, ErrorCode.PLATE_MULTIPLE_SAMPLES_DESC,
				NotificationGroupType.DUPLICATE_PLATE_ID_DPCR_DESC, paramList, token);
		dpcrAnalyzerAsyncMessageService.performAsyncQueryMessage(getQueryMessage(), token);
	}

	@Test(priority = 3)
	public void performAsyncQueryMessageThird() throws IOException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		
		 List<SampleResultsDTO> sampleResultsList=new ArrayList<>();
		 SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		 sampleResultsDTO.setAccesssioningId("");
		 sampleResultsList.add(sampleResultsDTO);
		 sampleResultsList.clear();
		 
		 List<SampleResultsDTO> sampleResultsListSecond=new ArrayList<>();
		 
		List<String> paramListSecond = new LinkedList<>();
		paramListSecond.add(getQueryMessage().getPlateId().get(0));
		Mockito.when(rmmIntegrationService.getSampleResults(null, null,
				null, null, getQueryMessage().getPlateId().get(0), null, null, token)).thenReturn(sampleResultsList);
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, getQueryMessage().getPlateId().get(0),
				null, null, null, null, token)).thenReturn(sampleResultsListSecond);

		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getQueryMessage().getMessageControlId(), ErrorCode.PLATE_INCOMPLETE_CODE,
				ErrorCode.PLATE_INCOMPLETE_DESC, NotificationGroupType.INCOMPLETE_PLATE_DPCR_DESC, paramListSecond, token);
		dpcrAnalyzerAsyncMessageService.performAsyncQueryMessage(getQueryMessage(), token);
	}

	@Test(priority = 4)
	public void performAsyncQueryMessageFourth() throws IOException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		List<String> paramListSecond = new LinkedList<>();
		paramListSecond.add(getQueryMessage().getPlateId().get(0));
		
		 List<SampleResultsDTO> sampleResultsList=new ArrayList<>();
		 SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		 sampleResultsDTO.setAccesssioningId("");
		 sampleResultsList.add(sampleResultsDTO);
		 sampleResultsList.clear();
		 
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, null, null,
				getQueryMessage().getPlateId().get(0), null, null, token)).thenReturn(sampleResultsList);
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, getQueryMessage().getPlateId().get(0), null,
				null, null, null, token)).thenReturn(getSampleResultsMultiple());

		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getQueryMessage().getMessageControlId(), ErrorCode.PLATE_INCOMPLETE_CODE,
				ErrorCode.PLATE_INCOMPLETE_DESC, NotificationGroupType.INCOMPLETE_PLATE_DPCR_DESC, paramListSecond, token);

		List<String> paramListThird = new LinkedList<>();
		paramListThird.add(getQueryMessage().getPlateId().get(0));
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(getQueryMessage().getMessageControlId(),
				ErrorCode.PLATE_MULTIPLE_SAMPLES_CODE, ErrorCode.PLATE_MULTIPLE_SAMPLES_DESC,
				NotificationGroupType.DUPLICATE_PLATE_ID_DPCR_DESC, paramListThird, token);
		dpcrAnalyzerAsyncMessageService.performAsyncQueryMessage(getQueryMessage(), token);
	}

	@Test(priority = 5)
	public void performAsyncQueryMessageFifth() throws IOException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		
		 List<SampleResultsDTO> sampleResultsList=new ArrayList<>();
		 SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		 sampleResultsDTO.setAccesssioningId("");
		 sampleResultsList.add(sampleResultsDTO);
		 sampleResultsList.clear();
		List<OrderDTO> Orderlist = null;
		List<String> paramListSecond = new LinkedList<>();
		paramListSecond.add(getQueryMessage().getPlateId().get(0));
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, null, null,
				getQueryMessage().getPlateId().get(0), null, null, token)).thenReturn(sampleResultsList);
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, getQueryMessage().getPlateId().get(0), null,
				null, null, null, token)).thenReturn(getSampleResultsMultiplePositive());

		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getQueryMessage().getMessageControlId(), ErrorCode.PLATE_INCOMPLETE_CODE,
				ErrorCode.PLATE_INCOMPLETE_DESC, NotificationGroupType.INCOMPLETE_PLATE_DPCR_DESC, paramListSecond, token);

		List<String> paramListThird = new LinkedList<>();
		paramListThird.add(getQueryMessage().getPlateId().get(0));
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(getQueryMessage().getMessageControlId(),
				ErrorCode.PLATE_MULTIPLE_SAMPLES_CODE, ErrorCode.PLATE_MULTIPLE_SAMPLES_DESC,
				NotificationGroupType.DUPLICATE_PLATE_ID_DPCR_DESC, paramListThird, token);

		Mockito.when(
				orderIntegrationService.getOrder(getSampleResultsMultiplePositive().get(0).getAccesssioningId(), token))
				.thenReturn(Orderlist);

		List<String> paramListFourth = new LinkedList<>();
		paramListThird.add(getSampleResultsMultiplePositive().get(0).getAccesssioningId());
		paramListThird.add(getQueryMessage().getPlateId().get(0));
		paramListThird.add(getSampleResultsMultiplePositive().get(0).getOutputContainerPosition());
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(getQueryMessage().getMessageControlId(),
				ErrorCode.PLATE_NO_ORDER_CODE, ErrorCode.PLATE_NO_ORDER_DESC, NotificationGroupType.NO_ORDER_FOUND_DPCR_DESC,
				paramListFourth, token);
		dpcrAnalyzerAsyncMessageService.performAsyncQueryMessage(getQueryMessage(), token);
	}

    @Test(priority = 5)
    public void performAsyncQueryMessageSix() throws IOException, HMTPException {

        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClient.class);
        Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        token = "1";

        List<SampleResultsDTO> sampleResultsList = new ArrayList<>();
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setAccesssioningId("");
        sampleResultsList.add(sampleResultsDTO);
        sampleResultsList.clear();
        List<String> paramListSecond = new LinkedList<>();
        paramListSecond.add(getQueryMessage().getPlateId().get(0));
        Mockito.when(rmmIntegrationService.getSampleResults(null, null, null, null,
                getQueryMessage().getPlateId().get(0), null, null, token)).thenReturn(sampleResultsList);
        Mockito.when(rmmIntegrationService.getSampleResults(null, null, getQueryMessage().getPlateId().get(0), null,
                null, null, null, token)).thenReturn(getSampleResultsMultiplePositive());

        String orderListJson = "src/test/java/resource/OrderList.json";
        String jsonContent2 = JsonFileReaderAsString.getJsonfromFile(orderListJson);
        TypeReference<List<OrderDTO>> class2 = new TypeReference<List<OrderDTO>>() {
        };
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderDTO> orderList = objectMapper.readValue(jsonContent2, class2);

        Mockito.when(
                orderIntegrationService.getOrder(getSampleResultsMultiplePositive().get(0).getAccesssioningId(), token))
                .thenReturn(orderList);

        Mockito.when(deviceRunService.generateDeviceRunId()).thenReturn("RND19241502947");

        dpcrAnalyzerAsyncMessageService.performAsyncQueryMessage(getQueryMessage(), token);
    }
    @Test(priority = 5)
    public void performAsyncQueryMessageSeven()
            throws IOException, HMTPException, NumberFormatException, ParseException {

        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(RestClient.class);
        Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
        token = "1";

        CopyOnWriteArrayList<SampleResultsDTO> sampleResultsList = new CopyOnWriteArrayList<>();
        SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
        sampleResultsDTO.setAccesssioningId("");
        sampleResultsList.add(sampleResultsDTO);
        sampleResultsList.clear();
        List<String> paramListSecond = new LinkedList<>();
        paramListSecond.add(getQueryMessage().getPlateId().get(0));
        Mockito.when(rmmIntegrationService.getSampleResults(null, null, null, null,
                getQueryMessage().getPlateId().get(0), null, null, token)).thenReturn(sampleResultsList);
        Mockito.when(rmmIntegrationService.getSampleResults(null, null, getQueryMessage().getPlateId().get(0), null,
                null, null, null, token)).thenReturn(getSampleResultsMultiplePositive());

        String orderListJson = "src/test/java/resource/OrderList.json";
        String jsonContent2 = JsonFileReaderAsString.getJsonfromFile(orderListJson);
        TypeReference<List<OrderDTO>> class2 = new TypeReference<List<OrderDTO>>() {
        };
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderDTO> orderList = objectMapper.readValue(jsonContent2, class2);

        Mockito.when(
                orderIntegrationService.getOrder(getSampleResultsMultiplePositive().get(0).getAccesssioningId(), token))
                .thenReturn(orderList);

        String runId = "RND19241502947";
        Mockito.when(deviceRunService.generateDeviceRunId()).thenReturn(runId);

        Mockito.when(dpcrAnalyzerMessageService.saveRunResult(getQueryMessage(), runId, token))
                .thenReturn(Long.parseLong("1001"));

         Mockito.when(dpcrAnalyzerMessageService.sendWFMQueryMessage(getQueryMessage(), sampleResultsList,
                    Long.parseLong("1001"), token)).thenReturn(true);

        dpcrAnalyzerAsyncMessageService.performAsyncQueryMessage(getQueryMessage(), token);
    }


	@Test(priority = 6)
	public void processAsyncESUMessage() throws IOException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";

		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getESUMessage().getMessageControlId(), ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE,
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null, null, token);
		dpcrAnalyzerAsyncMessageService.processAsyncESUMessage(getESUMessage(), token);
	}

	@Test(priority = 7)
	public void processAsyncESUMessageRunResultConditionNegative() throws IOException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTO = null;
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getESUMessageSecond().getMessageControlId(), ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE,
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null, null, token);

		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getESUMessageSecond().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getESUMessageSecond().getDeviceId(), token))
				.thenReturn(runResultsDTO);
		dpcrAnalyzerAsyncMessageService.processAsyncESUMessage(getESUMessageSecond(), token);
	}

	@Test(priority = 8)
	public void processAsyncESUMessageRunResultConditionPositive() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTOSecond = new RunResultsDTO();
		runResultsDTOSecond.setRunResultId(Long.parseLong("123"));
		List <SampleResultsDTO> sampleresultslist=new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setFlag("Y");
		sampleresultslist.add(sampleResultsDTO);
		runResultsDTOSecond.setSampleResults(sampleresultslist);
		runResultsDTOSecond.setRunStatus("Completed");
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getESUMessageThird().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getESUMessageThird().getDeviceId(), token))
				.thenReturn(runResultsDTOSecond);

		Mockito.when(rmmIntegrationService.saveRunResult(runResultsDTOSecond, token)).thenReturn(Long.parseLong("123"));
		Mockito.when(dpcrAnalyzerMessageService.sendWFMESUMessage(getESUMessageThird(),
				runResultsDTOSecond, token)).thenReturn(true);
		Mockito.doNothing().when(dpcrAnalyzerMessageService)
				.sendPositiveACKMessage(getESUMessageThird().getMessageControlId(), token);
		dpcrAnalyzerAsyncMessageService.processAsyncESUMessage(getESUMessageThird(), token);
	}
	
	
	@Test(priority = 9)
	public void processAsyncESUMessageRunResultConditionPositiveSecond() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTOSecond = new RunResultsDTO();
		
		Collection<RunResultsDetailDTO> runresultdetail=new ArrayList<>();
		RunResultsDetailDTO runResultsDetailDTO=new RunResultsDetailDTO();
		runResultsDetailDTO.setAttributeName("DPCR Analyzer FilePath");
		runresultdetail.add(runResultsDetailDTO);
		runResultsDTOSecond.setRunResultsDetail(runresultdetail);
		runResultsDTOSecond.setRunResultId(Long.parseLong("123"));
		List <SampleResultsDTO> sampleresultslist=new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setFlag("Y");
		sampleresultslist.add(sampleResultsDTO);
		runResultsDTOSecond.setSampleResults(sampleresultslist);
		runResultsDTOSecond.setRunStatus("Completed");
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getESUMessageThird().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getESUMessageThird().getDeviceId(), token))
				.thenReturn(runResultsDTOSecond);

		Mockito.when(rmmIntegrationService.saveRunResult(runResultsDTOSecond, token)).thenReturn(Long.parseLong("123"));
		Mockito.when(dpcrAnalyzerMessageService.sendWFMESUMessage(getESUMessageThird(),
				runResultsDTOSecond, token)).thenReturn(true);
		Mockito.doNothing().when(dpcrAnalyzerMessageService)
				.sendPositiveACKMessage(getESUMessageThird().getMessageControlId(), token);
		dpcrAnalyzerAsyncMessageService.processAsyncESUMessage(getESUMessageThird(), token);
	}
	
	@Test(priority = 10)
	public void processAsyncESUMessageRunResultConditionNegativeSecond() throws IOException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);;
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTOSecond = new RunResultsDTO();
		runResultsDTOSecond.setRunResultId(Long.parseLong("123"));
		runResultsDTOSecond.setRunStatus("Aborted");
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getESUMessageFourth().getMessageControlId(), ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE,
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null, null, token);

		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getESUMessageFourth().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getESUMessageFourth().getDeviceId(), token))
				.thenReturn(runResultsDTOSecond);
		dpcrAnalyzerAsyncMessageService.processAsyncESUMessage(getESUMessageFourth(), token);
	}
	
	@Test(priority = 10)
	public void processAsyncESUMessageRunResultConditionNegativeCase() throws IOException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);;
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTOSecond = new RunResultsDTO();
		runResultsDTOSecond.setRunResultId(Long.parseLong("123"));
		runResultsDTOSecond.setRunStatus("Aborted");
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getESUMessageFourth().getMessageControlId(), ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE,
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null, null, token);

		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getESUMessageFourth().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getESUMessageFourth().getDeviceId(), token))
				.thenReturn(null);
		dpcrAnalyzerAsyncMessageService.processAsyncESUMessage(getESUMessageFourth(), token);
	}
	
	@Test(priority = 11)
	public void processAsyncESUMessageRunResultConditionNegativeThird() throws IOException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTOSecond = null;
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getESUMessageFourth().getMessageControlId(), ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE,
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null, null, token);

		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getESUMessageFourth().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getESUMessageFourth().getDeviceId(), token))
				.thenReturn(runResultsDTOSecond);
		dpcrAnalyzerAsyncMessageService.processAsyncESUMessage(getESUMessageFourth(), token);
	}
	
	@Test(priority = 12)
	public void processAsyncORUMessagePositive() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setRunResultId(1L);
		runResultsDTO.setRunStatus("COMPLETEDS");
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getESUMessageFourth().getMessageControlId(), ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE,
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null, null, token);

		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getORUMessage().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getORUMessage().getDeviceId(), token))
				.thenReturn(runResultsDTO);
		
		Mockito.when(rmmIntegrationService.saveRunResult(runResultsDTO, token)).thenReturn(1L);
		Mockito.when(dpcrAnalyzerMessageService.sendWFMORUMessage(getORUMessage(), runResultsDTO, token)).thenReturn(true);
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendPositiveACKMessage(getORUMessage().getMessageControlId(), token);
		dpcrAnalyzerAsyncMessageService.processAsyncORUMessage(getORUMessage(), token);
	}
	
	
	@Test(priority = 13)
	public void processAsyncORUMessagePositiveSecond() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setRunStatus("AAAA");
		Collection<RunResultsDetailDTO> runresultdetail=new ArrayList<>();
		RunResultsDetailDTO runResultsDetailDTO=new RunResultsDetailDTO();
		runResultsDetailDTO.setAttributeName("releasedBy");
		runresultdetail.add(runResultsDetailDTO);
		runResultsDTO.setRunResultsDetail(runresultdetail);
		runResultsDTO.setRunResultId(1L);
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getESUMessageFourth().getMessageControlId(), ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE,
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null, null, token);

		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getORUMessage().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getORUMessage().getDeviceId(), token))
				.thenReturn(runResultsDTO);
		
		Mockito.when(rmmIntegrationService.saveRunResult(runResultsDTO, token)).thenReturn(1L);
		Mockito.when(dpcrAnalyzerMessageService.sendWFMORUMessage(getORUMessage(), runResultsDTO, token)).thenReturn(true);
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendPositiveACKMessage(getORUMessage().getMessageControlId(), token);
		dpcrAnalyzerAsyncMessageService.processAsyncORUMessage(getORUMessage(), token);
	}
	
	
	@Test(priority = 14)
	public void processAsyncORUMessagePositiveThird() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setRunStatus("AAAA");
		Collection<RunResultsDetailDTO> runresultdetail=new ArrayList<>();
		RunResultsDetailDTO runResultsDetailDTO=new RunResultsDetailDTO();
		runResultsDetailDTO.setAttributeName("sentBy");
		runresultdetail.add(runResultsDetailDTO);
		runResultsDTO.setRunResultsDetail(runresultdetail);
		runResultsDTO.setRunResultId(1L);
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(
				getESUMessageFourth().getMessageControlId(), ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE,
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null, null, token);

		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getORUMessage().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getORUMessage().getDeviceId(), token))
				.thenReturn(runResultsDTO);
		
		Mockito.when(rmmIntegrationService.saveRunResult(runResultsDTO, token)).thenReturn(1L);
		Mockito.when(dpcrAnalyzerMessageService.sendWFMORUMessage(getORUMessage(), runResultsDTO, token)).thenReturn(true);
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendPositiveACKMessage(getORUMessage().getMessageControlId(), token);
		dpcrAnalyzerAsyncMessageService.processAsyncORUMessage(getORUMessage(), token);
	}
	
	
	
	
	@Test(priority = 15)
	public void processAsyncORUMessageNegative() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		RunResultsDTO runResultsDTO = null;
		
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getORUMessage().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getORUMessage().getDeviceId(), token))
				.thenReturn(runResultsDTO);
		
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(getORUMessage().getMessageControlId(),
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE, ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC, null,
				null, token);
		dpcrAnalyzerAsyncMessageService.processAsyncORUMessage(getORUMessage(), token);
	}
	
	
	@Test(priority = 16)
	public void processAsyncORUMessageNegativeSecond() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		
		List<String> paramList = new LinkedList<>();
		
		Mockito.doNothing().when(dpcrAnalyzerMessageService).sendNegativeACKMessage(getORUMessageSecond().getMessageControlId(),
				ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_CODE, ErrorCode.SAMPLE_TYPE_NOT_RECOGNIZED_DESC,
				NotificationGroupType.MISSING_INFO_DPCR_DESC, paramList, token);
		dpcrAnalyzerAsyncMessageService.processAsyncORUMessage(getORUMessageSecond(), token);
	}
	
	@Test(priority = 17)
	public void processAsyncAcknowledgementMessage() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		AcknowledgementMessage acknowledgementMessage =new AcknowledgementMessage();
		acknowledgementMessage.setStatus("AA");
		acknowledgementMessage.setRunId("RND123");
		acknowledgementMessage.setDeviceSerialNo("123abc");
		
		RunResultsDTO runResultsDTO =new RunResultsDTO();
		
		Collection<SampleResultsDTO> sampleResultslist = new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setAccesssioningId("123345");
		sampleResultslist.add(sampleResultsDTO);
		runResultsDTO.setSampleResults(sampleResultslist);
		
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				acknowledgementMessage.getRunId(), DPCRProcessStepName.DPCR_ANALYZER, acknowledgementMessage.getDeviceSerialNo(), token)).thenReturn(runResultsDTO);
	try {
		dpcrAnalyzerAsyncMessageService.processAsyncAcknowledgementMessage(acknowledgementMessage, token);
	}catch(Exception e){
		logger.info("processAsyncAcknowledgementMessage test case passed");
	}
	}
	
	
	@Test(priority = 18)
	public void processAsyncAcknowledgementMessagePositiveSecond() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		AcknowledgementMessage acknowledgementMessage =new AcknowledgementMessage();
		acknowledgementMessage.setStatus("AA");
		acknowledgementMessage.setRunId("RND123");
		acknowledgementMessage.setDeviceSerialNo("123abc");
		
		RunResultsDTO runResultsDTO =new RunResultsDTO();
		
		Collection<SampleResultsDTO> sampleResultslist = new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setAccesssioningId("123345");
		sampleResultslist.add(sampleResultsDTO);
		runResultsDTO.setSampleResults(sampleResultslist);
		
		
		
		Collection<SampleResultsDTO> sampleResultsListsecond =new ArrayList<>();
		SampleResultsDTO sampleResultsDTOSecond=new SampleResultsDTO();
		sampleResultsDTOSecond.setAccesssioningId("123345");
		sampleResultsListsecond.add(sampleResultsDTOSecond);
		
		
		WFMAcknowledgementMessage ack = new WFMAcknowledgementMessage();
		ack.setAccessioningId(sampleResultsDTOSecond.getAccesssioningId());
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				acknowledgementMessage.getRunId(), DPCRProcessStepName.DPCR_ANALYZER, acknowledgementMessage.getDeviceSerialNo(), token)).thenReturn(runResultsDTO);
		
		Mockito.when(dpcrAnalyzerMessageService.traceSampleResults(runResultsDTO, false, token)).thenReturn(sampleResultsListsecond);
		
		String wfmHosturl="http://localhost:86/wfm";
		
		Mockito.when(wfmIntegrationService.genericMessagePoster(ack, wfmHosturl + UrlConstants.WFM_DPCR_ANALYZER_ACK_URL,token)).thenReturn(true);
		
	try {
		dpcrAnalyzerAsyncMessageService.processAsyncAcknowledgementMessage(acknowledgementMessage, token);
	}catch(Exception e){
		logger.info("processAsyncAcknowledgementMessagePositiveSecond test case passed");
	}
	}
	
	
	@Test(priority = 19)
	public void processAsyncAcknowledgementMessageNegative() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		PowerMockito.mockStatic(AdmNotificationService.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		token = "1";
		
		AcknowledgementMessage acknowledgementMessage =new AcknowledgementMessage();
		acknowledgementMessage.setStatus("AR");
		acknowledgementMessage.setRunId("RND123");
		acknowledgementMessage.setDeviceSerialNo("123abc");
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		//Mockito.doNothing().when(admNotificationService).sendNotification(NotificationGroupType.RESULT_REJECTED_DPCR, paramList, token);
		
	try {
		dpcrAnalyzerAsyncMessageService.processAsyncAcknowledgementMessage(acknowledgementMessage, token);
	}catch(Exception e){
		logger.info("processAsyncAcknowledgementMessageNegative test case passed");
	}
	}
	
	@Test(priority = 20)
	public void performAsyncQueryMessageException() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.when(rmmIntegrationService.getSampleResults(null, null,
				null, null, getQueryMessage().getPlateId().get(0), null, null, token)).thenThrow(Exception.class);
		
	try {
		dpcrAnalyzerAsyncMessageService.performAsyncQueryMessage(getQueryMessage(), token);
	}catch(Exception e){
		logger.info("performAsyncQueryMessageException test case passed");
	}
	}
	
	@Test(priority = 21)
	public void processAsyncAcknowledgementMessageException() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		
		AcknowledgementMessage acknowledgementMessage =new AcknowledgementMessage();
		acknowledgementMessage.setStatus("AA");
		acknowledgementMessage.setRunId("RND123");
		acknowledgementMessage.setDeviceSerialNo("123abc");
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(
				acknowledgementMessage.getRunId(), DPCRProcessStepName.DPCR_ANALYZER, acknowledgementMessage.getDeviceSerialNo(), token)).thenThrow(HMTPException.class);
		
	try {
		dpcrAnalyzerAsyncMessageService.processAsyncAcknowledgementMessage(acknowledgementMessage, token);
	}catch(Exception e){
		logger.info("processAsyncAcknowledgementMessageException test case passed");
	}
	}
	
	@Test(priority = 22)
	public void processAsyncESUMessageException() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		
		
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getESUMessageThird().getRunId(),DPCRProcessStepName.DPCR_ANALYZER, getESUMessageThird().getDeviceId(), token)).thenThrow(Exception.class);
		
	try {
		dpcrAnalyzerAsyncMessageService.processAsyncESUMessage(getESUMessageThird(), token);
	}catch(Exception e){
		logger.info("processAsyncESUMessageException test case passed");
	}
	}
	
	
	@Test(priority = 23)
	public void processAsyncORUMessageException() throws Exception {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		
		Mockito.when(rmmIntegrationService.getRunResultsAndSampleResults(getORUMessage().getRunId(),
				DPCRProcessStepName.DPCR_ANALYZER, getORUMessage().getDeviceId(), token)).thenThrow(Exception.class);
		
	try {
		dpcrAnalyzerAsyncMessageService.processAsyncORUMessage(getORUMessage(), token);
	}catch(Exception e){
		logger.info("processAsyncORUMessageException test case passed");
	}
	}
	
	
	public ORUMessage getORUMessage() {

		ORUMessage oruMessage = new ORUMessage();
		Collection<ORUSampleDetails> oruSampleDetails = new ArrayList<>();
		oruMessage.setRunId("RND1234");
		oruMessage.setMessageControlId("abc");
		oruMessage.setDeviceId("dpcr");
		ORUSampleDetails orusampleDetails=new ORUSampleDetails();
		orusampleDetails.setAccessioningId("1234");
		
		oruSampleDetails.add(orusampleDetails);
		oruMessage.setOruSampleDetails(oruSampleDetails);
		oruMessage.setSentBy("ABC");
		oruMessage.setRunComments("abc");
		oruMessage.setDateTimeMessageGenerated("20180122101010");
		oruMessage.setOperatorName("admin");
		oruMessage.setReleasedBy("admin");
		return oruMessage;

	}
	
	
	public ORUMessage getORUMessageSecond() {

		ORUMessage oruMessage = new ORUMessage();
		Collection<ORUSampleDetails> oruSampleDetails = new ArrayList<>();
		oruMessage.setMessageControlId("abc");
		oruMessage.setDeviceId("dpcr");
		ORUSampleDetails orusampleDetails=new ORUSampleDetails();
		orusampleDetails.setAccessioningId("1234");
		
		oruSampleDetails.add(orusampleDetails);
		oruMessage.setOruSampleDetails(oruSampleDetails);
		oruMessage.setSentBy("ABC");
		oruMessage.setRunComments("abc");
		oruMessage.setDateTimeMessageGenerated("20180122101010");
		oruMessage.setOperatorName("admin");
		oruMessage.setReleasedBy("admin");
		return oruMessage;

	}

	public ESUMessage getESUMessage() {

		ESUMessage eSUMessage = new ESUMessage();
		eSUMessage.setRunId("RND1234");
		eSUMessage.setEstimatedTimeRemaining("20190122101010");
		eSUMessage.setMessageControlId("abc");
		eSUMessage.setDeviceId("dpcr");
		return eSUMessage;

	}

	public ESUMessage getESUMessageSecond() {

		ESUMessage eSUMessage = new ESUMessage();
		eSUMessage.setRunId("RND1234");
		eSUMessage.setEstimatedTimeRemaining("20180122101010");
		eSUMessage.setMessageControlId("abc");
		eSUMessage.setStatus("Inprogress");
		return eSUMessage;

	}

	public ESUMessage getESUMessageThird() {

		ESUMessage eSUMessage = new ESUMessage();
		eSUMessage.setRunId("RND1234");
		eSUMessage.setEstimatedTimeRemaining("20190222101010");
		eSUMessage.setDateTimeMessageGenerated("20190122101010");
		eSUMessage.setMessageControlId("abc");
		eSUMessage.setStatus("ID");
		eSUMessage.setDeviceId("dpcr");
		eSUMessage.setFilePath("abc");
		return eSUMessage;

	}
	
	
	public ESUMessage getESUMessageFourth() {

		ESUMessage eSUMessage = new ESUMessage();
		eSUMessage.setRunId("RND1234");
		eSUMessage.setEstimatedTimeRemaining("20190222101010");
		eSUMessage.setDateTimeMessageGenerated("20190122101010");
		eSUMessage.setMessageControlId("abc");
		eSUMessage.setStatus("ID");
		eSUMessage.setDeviceId("dpcr");
		eSUMessage.setFilePath("abc");
		return eSUMessage;

	}

	/*
	 * @Test(priority=8) public void performAsyncQueryMessageSixth() throws
	 * Exception {
	 * 
	 * MockitoAnnotations.initMocks(this); Mockito.mock(RestClient.class);
	 * Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(),
	 * Mockito.any(Object.class)); token="1";
	 * 
	 * List<OrderDTO> Orderlist =null; OrderDTO orderDTO=new OrderDTO();
	 * orderDTO.setAccessioningId("1234");
	 * 
	 * Mockito.when(rmmIntegrationService.getSampleResults(null, null, null, null,
	 * getQueryMessageThird().getPlateId().get(0), null, null,
	 * token)).thenReturn(getsampleresultsSecond());
	 * Mockito.when(rmmIntegrationService.getSampleResults(null, null,
	 * getQueryMessageThird().getPlateId().get(0), null, null, null, null,
	 * token)).thenReturn(getsampleresultsMutiplePositive());
	 * 
	 * Mockito.when(orderIntegrationService.getOrder(getsampleresultsMutiplePositive
	 * ().get(0).getAccesssioningId(), token)).thenReturn(Orderlist);
	 * ReflectionTestUtils.setField(dpcrAnalyzerAsyncMessageServices, "runIDPrefix",
	 * "RND"); String runId = "RND";
	 * 
	 * final CopyOnWriteArrayList<SampleResultsDTO> existingSampleResultsList = new
	 * CopyOnWriteArrayList<>(); final CopyOnWriteArrayList<SampleResultsDTO>
	 * sampleResultsList = new CopyOnWriteArrayList<>();
	 * existingSampleResultsList.addAll(getsampleresultsSecond());
	 * sampleResultsList.addAll(getsampleresultsMutiplePositive());
	 * Mockito.doNothing().when(dpcrAnalyzerMessageService).sendPositiveACKMessage(
	 * getQueryMessageThird().getMessageControlId(), token);
	 * Mockito.when(dpcrAnalyzerMessageService.saveRunResult(getQueryMessageThird(),
	 * runId, existingSampleResultsList, token)).thenReturn(Long.parseLong("1001"));
	 * Mockito.doNothing().when(dpcrAnalyzerMessageService).sendWFMQueryMessage(
	 * getQueryMessageThird(), sampleResultsList, existingSampleResultsList,
	 * Long.parseLong("1001"), token);
	 * Mockito.doNothing().when(dpcrAnalyzerMessageService).sendOMLMessage(
	 * getQueryMessageThird(), runId, sampleResultsList, token);
	 * dpcrAnalyzerAsyncMessageService.performAsyncQueryMessage(getQueryMessageThird
	 * (),token); }
	 */
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	public QueryMessage getQueryMessageThird() {
		QueryMessage queryMessage = new QueryMessage();
		List<String> lt = new ArrayList<>();
		lt.add("c144444458c466a6");
		queryMessage.setPlateId(lt);
		queryMessage.setMessageControlId("12345");
		return queryMessage;
	}

	public List<SampleResultsDTO> getSampleResults() {
		List<SampleResultsDTO> sampleresultsList = new ArrayList<SampleResultsDTO>();
		SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
		sampleResultsDTO.setStatus("STARTED");
		sampleresultsList.add(sampleResultsDTO);
		return sampleresultsList;
	}

	public List<SampleResultsDTO> getSampleResultsSecond() {
		List<SampleResultsDTO> sampleresultsList = new ArrayList<SampleResultsDTO>();
		SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
		sampleResultsDTO.setStatus("NOT STARTED");
		sampleresultsList.add(sampleResultsDTO);
		return sampleresultsList;
	}

	public List<SampleResultsDTO> getSampleResultsThird() {
		List<SampleResultsDTO> sampleresultsList = null;
		return sampleresultsList;
	}

	public List<SampleResultsDTO> getSampleResultsMultiple() {
		List<SampleResultsDTO> sampleresultsList = new ArrayList<>();
		SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();

		sampleResultsDTO.setStatus("NOT STARTED");
		sampleResultsDTO.setOutputContainerId("abc");
		sampleResultsDTO.setOutputContainerPosition("1");
		sampleresultsList.add(sampleResultsDTO);

		SampleResultsDTO sampleResultsDTOSecond = new SampleResultsDTO();
		sampleResultsDTOSecond.setStatus("NOT STARTED");
		sampleResultsDTOSecond.setOutputContainerId("abc");
		sampleResultsDTOSecond.setOutputContainerPosition("1");
		sampleresultsList.add(sampleResultsDTOSecond);
		return sampleresultsList;
	}

	public List<SampleResultsDTO> getSampleResultsMultiplePositive() {
		List<SampleResultsDTO> sampleresultsList = new ArrayList<>();
		SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
		sampleResultsDTO.setAccesssioningId("1234");
		sampleResultsDTO.setStatus("NOT STARTED");
		sampleResultsDTO.setOutputContainerId("abc");
		sampleResultsDTO.setOutputContainerPosition("1");
		sampleresultsList.add(sampleResultsDTO);
		return sampleresultsList;
	}
	
	
	public QueryMessage getQueryMessage(){
		QueryMessage queryMessage=new QueryMessage();
		queryMessage.setRecevingApplicationName("Connect");
		queryMessage.setRecevingFacility("Connect");
		queryMessage.setSendingFacility("Roche Diagnostics");
		queryMessage.setDeviceId("ABC");
		queryMessage.setMessageControlId("123abc");
		
		List<String> plateIdList=new ArrayList<>();
		plateIdList.add("c144444458c466a6");
		queryMessage.setPlateId(plateIdList);
		
		return queryMessage;
	}
	
	
	public QueryMessage getQueryMessageFirst(){
		QueryMessage queryMessage=new QueryMessage();
		queryMessage.setRecevingApplicationName("Connect");
		queryMessage.setRecevingFacility("Connect");
		queryMessage.setSendingFacility("Roche Diagnostics");
		queryMessage.setDeviceId("ABC");
		queryMessage.setMessageControlId("123abc");
		return queryMessage;
	}
}
