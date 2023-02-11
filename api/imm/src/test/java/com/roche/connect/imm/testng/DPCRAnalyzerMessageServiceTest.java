package com.roche.connect.imm.testng;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.dpcr_analyzer.AcknowledgementMessage;
import com.roche.connect.common.dpcr_analyzer.DPCRAnalyzerRunStatus;
import com.roche.connect.common.dpcr_analyzer.DPCRProcessStepName;
import com.roche.connect.common.dpcr_analyzer.ESUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUMessage;
import com.roche.connect.common.dpcr_analyzer.ORUPartitionEngine;
import com.roche.connect.common.dpcr_analyzer.ORUSampleDetails;
import com.roche.connect.common.dpcr_analyzer.QueryMessage;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.DPCRAnalyzerMessageService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.utils.IMMConstants;
import com.roche.connect.imm.utils.RestClient;



@PrepareForTest({ RestClientUtil.class, RestClient.class, AdmNotificationService.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class DPCRAnalyzerMessageServiceTest extends PowerMockTestCase {

	@Mock
	HMTPLoggerImpl loggerImpl;

	@Mock
	private ObjectMapper objectMapper;

	String token = null;
	
	private static Logger logger = LogManager.getLogger(DPCRAnalyzerMessageServiceTest.class);
	@InjectMocks
	private DPCRAnalyzerMessageService dPCRAnalyzerMessageService;

	@Mock
	private Response response;
	
	String url = "http://localhost";
	
	@Mock
	Invocation.Builder builder;

	private SimpleDateFormat formatter = new SimpleDateFormat(IMMConstants.ADAPTOR_SIMPLE_DATE_FORMAT_STR);

	@Mock
	private RmmIntegrationService rmmIntegrationService = org.mockito.Mockito.mock(RmmIntegrationService.class);

	
	@Mock
	private AssayIntegrationService assayIntegrationService = org.mockito.Mockito.mock(AssayIntegrationService.class);
	
	
	
	
	@Test(priority = 1)
	public void performAsyncQueryMessage() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		token = "1";
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(objectMapper.writeValueAsString(getACKMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		dPCRAnalyzerMessageService.sendPositiveACKMessage("12345", token);

	}
	
	@Test(priority = 2)
	public void sendNegativeACKMessage() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		PowerMockito.mockStatic(AdmNotificationService.class);
		List<String> paramList=new ArrayList<>();
		paramList.add("ABC");
		token="1";
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());
		try {
			dPCRAnalyzerMessageService.sendNegativeACKMessage("abc","cdf", "abcd",
					NotificationGroupType.RESULT_REJECTED_DPCR, paramList, token);
		} catch (Exception e) {
			logger.info("successfully method executed");
		}
		
	}
	

	@Test(priority = 3)
	public void saveRunResult() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		List<String> paramList = new ArrayList<>();
		paramList.add("ABC");
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setDeviceRunId("RND123");
		PowerMockito.mockStatic(RestClient.class);
		token="1";
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		Mockito.when(
				rmmIntegrationService.getRunResultsById(getExistingSampleResultsList().get(0).getRunResultsId(), token))
				.thenReturn(runResultsDTO);

		Mockito.when(rmmIntegrationService.saveRunResult(runResultsDTO, token)).thenReturn(Long.parseLong("1001"));

		Mockito.when(objectMapper.writeValueAsString(getACKMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		try {
			dPCRAnalyzerMessageService.saveRunResult(getQueryMessage(), "RND123", token);
		} catch (Exception e) {
			logger.info("successfully method executed");
		}

	}
	
	
	@Test(priority = 4)
	public void saveRunResultElse() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		List<String> paramList = new ArrayList<>();
		paramList.add("ABC");
		token = "1";
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setDeviceRunId("RND123");
		Collection<RunResultsDetailDTO> runresultlist = new ArrayList<>();
		RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
		runResultsDetailDTO.setAttributeName("DPCR PlateId List");
		runResultsDetailDTO.setAttributeValue("ABC,CDE");
		runresultlist.add(runResultsDetailDTO);
		runResultsDTO.setRunResultsDetail(runresultlist);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		Mockito.when(RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null),
				Mockito.eq(Long.class))).thenReturn(0L);
		Mockito.when(rmmIntegrationService.getRunResults(getQueryMessage().getDeviceId(),
				DPCRProcessStepName.DPCR_ANALYZER, DPCRAnalyzerRunStatus.NOT_STARTED, token)).thenReturn(runResultsDTO);

		Mockito.when(rmmIntegrationService.saveRunResult(runResultsDTO, token)).thenReturn(Long.parseLong("1001"));

		Mockito.when(objectMapper.writeValueAsString(getACKMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		try {
			dPCRAnalyzerMessageService.saveRunResult(getQueryMessage(), "RND123", token);
		} catch (Exception e) {
			logger.info("successfully method executed");
		}

	}
	
	@Test(priority = 4, expectedExceptions = IOException.class)
	public void saveRunResultException() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		List<String> paramList = new ArrayList<>();
		paramList.add("ABC");
		token = "1";
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setDeviceRunId("RND123");
		Collection<RunResultsDetailDTO> runresultlist = new ArrayList<>();
		RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
		runResultsDetailDTO.setAttributeName("DPCR PlateId List");
		runResultsDetailDTO.setAttributeValue("ABC,CDE");
		runresultlist.add(runResultsDetailDTO);
		runResultsDTO.setRunResultsDetail(runresultlist);
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		Mockito.when(RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null),
				Mockito.eq(Long.class))).thenReturn(0L);
		Mockito.when(rmmIntegrationService.getRunResults(getQueryMessage().getDeviceId(),
				DPCRProcessStepName.DPCR_ANALYZER, DPCRAnalyzerRunStatus.NOT_STARTED, token)).thenReturn(runResultsDTO);

		Mockito.when(rmmIntegrationService.saveRunResult(runResultsDTO, token)).thenThrow(IOException.class);

		Mockito.when(objectMapper.writeValueAsString(getACKMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		dPCRAnalyzerMessageService.saveRunResult(getQueryMessage(), "RND123", token);

	}

	@Test(priority = 5)
	public void sendOMLMessage() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		token="1";
		List<String> paramList=new ArrayList<>();
		paramList.add("ABC");
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setDeviceRunId("RND123");
		PowerMockito.mockStatic(RestClient.class);
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		List<DeviceTestOptionsDTO> deviceTestOptionsList=new ArrayList<>();
		DeviceTestOptionsDTO deviceTestOptionsDTO=new DeviceTestOptionsDTO();
		deviceTestOptionsDTO.setTestProtocol("ABC");
		deviceTestOptionsDTO.setAnalysisPackageName("ABC");
		deviceTestOptionsList.add(deviceTestOptionsDTO);

		Mockito.when(assayIntegrationService.findDeviceTestOptions(
				AssayType.NIPT_DPCR, DeviceType.DPCR_ANALYZER, DPCRProcessStepName.DPCR_ANALYZER, token)).thenReturn(deviceTestOptionsList);
		Mockito.when(objectMapper.writeValueAsString(deviceTestOptionsList))
		.thenReturn(getACKMessage().getMessageControlId());

		try {
			dPCRAnalyzerMessageService.sendOMLMessage(getQueryMessage(),"RND123",
					getExistingSampleResultsList(), token);	} catch (Exception e) {
						logger.info("successfully method executed");
					}


	}
	
	
	@Test(priority = 6)
	public void sendOMLMessageElse() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		token="1";
		List<String> paramList=new ArrayList<>();
		paramList.add("ABC");
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setDeviceRunId("RND123");
		
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		List<DeviceTestOptionsDTO> deviceTestOptionsList=new ArrayList<>();
		DeviceTestOptionsDTO deviceTestOptionsDTO=new DeviceTestOptionsDTO();
		deviceTestOptionsDTO.setTestProtocol("ABC");
		deviceTestOptionsDTO.setAnalysisPackageName("ABC");
		deviceTestOptionsList.add(deviceTestOptionsDTO);

		Mockito.when(assayIntegrationService.findDeviceTestOptions(
				AssayType.NIPT_DPCR, DeviceType.DPCR_ANALYZER, DPCRProcessStepName.DPCR_ANALYZER, token)).thenReturn(deviceTestOptionsList);
		Mockito.when(objectMapper.writeValueAsString(deviceTestOptionsList))
		.thenReturn(getACKMessage().getMessageControlId());

		try {
			dPCRAnalyzerMessageService.sendOMLMessage(getQueryMessage(),"RND123",
					getExistingSampleResultsListSecond(), token);	} catch (Exception e) {
						logger.info("successfully method executed");
					}


	}
	
	@Test(priority = 7)
	public void sendWFMQueryMessage() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		token = "1";
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);

		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		Mockito.when(objectMapper.writeValueAsString(getQueryMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		Assert.assertTrue(dPCRAnalyzerMessageService.sendWFMQueryMessage(getQueryMessage(), sampleResultsList(),
				Long.parseLong("1001"), token));

	}

	@Test(priority = 7)
	public void sendWFMQueryMessageNegative() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		token = "1";
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);

		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		Mockito.when(objectMapper.writeValueAsString(getQueryMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		Assert.assertFalse(dPCRAnalyzerMessageService.sendWFMQueryMessage(getQueryMessage(), sampleResultsList(),
				Long.parseLong("1001"), token));
	}

	@Test(priority = 7, expectedExceptions = IOException.class)
	public void sendWFMQueryMessageException() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		token = "1";
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenThrow(IOException.class);

		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		Mockito.when(objectMapper.writeValueAsString(getQueryMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		dPCRAnalyzerMessageService.sendWFMQueryMessage(getQueryMessage(), sampleResultsList(), Long.parseLong("1001"),
				token);

	}
	
	@Test(priority = 8)
	public void sendWFMESUMessage() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		token = "1";
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);

		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		RunResultsDTO runResultsDTO = new RunResultsDTO();

		runResultsDTO.setSampleResults(getExistingSampleResultsList());

		Mockito.when(objectMapper.writeValueAsString(getQueryMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		Assert.assertTrue(dPCRAnalyzerMessageService.sendWFMESUMessage(getESUMessage(), runResultsDTO, token));

	}
	
	@Test(priority = 8)
	public void sendWFMESUMessageNegative() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		token = "1";
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);

		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		RunResultsDTO runResultsDTO = new RunResultsDTO();

		runResultsDTO.setSampleResults(getExistingSampleResultsList());

		Mockito.when(objectMapper.writeValueAsString(getQueryMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		Assert.assertFalse(dPCRAnalyzerMessageService.sendWFMESUMessage(getESUMessage(), runResultsDTO, token));

	}
	
	@Test(priority = 8, expectedExceptions = IOException.class)
	public void sendWFMESUMessageException() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		token = "1";
		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenThrow(IOException.class);

		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		RunResultsDTO runResultsDTO = new RunResultsDTO();

		runResultsDTO.setSampleResults(getExistingSampleResultsList());

		Mockito.when(objectMapper.writeValueAsString(getQueryMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		dPCRAnalyzerMessageService.sendWFMESUMessage(getESUMessage(), runResultsDTO, token);

	}
	
	@Test(priority = 9)
	public void sendWFMORUMessage() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		token = "1";
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		RunResultsDTO runResultsDTO = new RunResultsDTO();

		runResultsDTO.setSampleResults(getExistingSampleResultsList());
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, "ABC", null, null, null, null, token))
				.thenReturn(getSampleDTOList());
		Mockito.when(objectMapper.writeValueAsString(getQueryMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);

		Assert.assertTrue(dPCRAnalyzerMessageService.sendWFMORUMessage(getORUMessage(), getRunResultsDTO(), token));

	}
	
	@Test(priority = 9)
	public void sendWFMORUMessageNegative() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		token = "1";
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		RunResultsDTO runResultsDTO = new RunResultsDTO();

		runResultsDTO.setSampleResults(getExistingSampleResultsList());
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, "ABC", null, null, null, null, token))
				.thenReturn(getSampleDTOList());
		Mockito.when(objectMapper.writeValueAsString(getQueryMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);

		Assert.assertFalse(dPCRAnalyzerMessageService.sendWFMORUMessage(getORUMessage(), getRunResultsDTO(), token));

	}

	@Test(priority = 9, expectedExceptions = IOException.class)
	public void sendWFMORUMessageNegativeException() throws Exception {
		PowerMockito.mockStatic(RestClientUtil.class);
		token = "1";
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		RunResultsDTO runResultsDTO = new RunResultsDTO();

		runResultsDTO.setSampleResults(getExistingSampleResultsList());
		Mockito.when(rmmIntegrationService.getSampleResults(null, null, "ABC", null, null, null, null, token))
				.thenReturn(getSampleDTOList());
		Mockito.when(objectMapper.writeValueAsString(getQueryMessage()))
				.thenReturn(getACKMessage().getMessageControlId());

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenThrow(IOException.class);

		dPCRAnalyzerMessageService.sendWFMORUMessage(getORUMessage(), getRunResultsDTO(), token);
	}
	
	
	
	
	@Test(priority = 10)
	public void sendPositiveACKMessageException() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		token = "1";
		Mockito.doThrow(IOException.class).when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		try {
			dPCRAnalyzerMessageService.sendPositiveACKMessage("12345", token);
		}catch(Exception e) {

		}

	}
	
	@Test(priority = 11)
	public void sendNegativeACKMessageException() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		token = "1";
		List<String> paramList=new ArrayList<>();
		paramList.add("ABC");
		Mockito.doThrow(IOException.class).when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		try {
			dPCRAnalyzerMessageService.sendNegativeACKMessage("abc","cdf", "abcd",
					NotificationGroupType.RESULT_REJECTED_DPCR, paramList, token);
		}catch(Exception e) {

		}

	}
	
	@Test(priority = 12)
	public void sendOMLMessageException() throws Exception {
		PowerMockito.mockStatic(RestClient.class);
		
		Mockito.when(assayIntegrationService.findDeviceTestOptions(
					AssayType.NIPT_DPCR, DeviceType.DPCR_ANALYZER, DPCRProcessStepName.DPCR_ANALYZER, token)).thenThrow(IOException.class);
		try {
			dPCRAnalyzerMessageService.sendOMLMessage(getQueryMessage(),"RND123",
					getExistingSampleResultsList(), token);
		}catch(Exception e) {

		}

	}
	
	
	public List<SampleResultsDTO>  getSampleDTOList() {
		 List<SampleResultsDTO> list=new ArrayList<>();
		
		 SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		 sampleResultsDTO.setOutputContainerId("123ABC");
		 sampleResultsDTO.setStatus("ABCS");
		 sampleResultsDTO.setAccesssioningId("1234");
		 
		list.add(sampleResultsDTO);
		
		return list;

	}
	
	
	
	
	
	public RunResultsDTO getRunResultsDTO() {

		RunResultsDTO runResultsDTO = new RunResultsDTO();
		Collection<RunResultsDetailDTO> runresultlist=new ArrayList<>();
		
		RunResultsDetailDTO runResultsDetailDTO=new RunResultsDetailDTO();
		runResultsDetailDTO.setAttributeName("DPCR PlateId List");
		runResultsDetailDTO.setAttributeValue("ABC,CDE");
		runresultlist.add(runResultsDetailDTO);
		runResultsDTO.setRunResultsDetail(runresultlist);
		
		
		return runResultsDTO;

	}
	
	
	public ORUMessage getORUMessage() {

		ORUMessage oruMessage = new ORUMessage();
		oruMessage.setDeviceId("abc");
		oruMessage.setOruSampleDetails(getORUSampleDetailsList());
		return oruMessage;

	}
	
	public Collection<ORUSampleDetails>  getORUSampleDetailsList() {
		 List<ORUSampleDetails> list=new ArrayList<>();
		 Collection<ORUPartitionEngine> engineList=new ArrayList<>();
		 ORUSampleDetails oruSampleDetails=new ORUSampleDetails();
		 oruSampleDetails.setAccessioningId("1234");
		 oruSampleDetails.setFlag("Y");
		 oruSampleDetails.setPartitionEngineList(engineList);
		list.add(oruSampleDetails);
		
		return list;

	}
	
	public ESUMessage getESUMessage() {

		ESUMessage esuMessage = new ESUMessage();
		esuMessage.setDeviceId("abc");
		esuMessage.setStatus("ID");
		return esuMessage;

	}
	public List<SampleResultsDTO>  sampleResultsList() {
		 List<SampleResultsDTO> list=new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setRunResultsId(Long.parseLong("1001"));
		sampleResultsDTO.setAccesssioningId("20201");
		sampleResultsDTO.setOutputContainerId("abcd12");
		sampleResultsDTO.setOutputContainerPosition("A1");
		sampleResultsDTO.setSampleReagentsAndConsumables(getSampleReagentsAndConsumablesList());
		list.add(sampleResultsDTO);
		
		return list;

	}
	
	
	public List<SampleResultsDTO>  getExistingSampleResultsList() {
		 List<SampleResultsDTO> list=new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();;
		
		sampleResultsDTO.setRunResultsId(Long.parseLong("1001"));
		sampleResultsDTO.setAccesssioningId("20201");
		sampleResultsDTO.setOutputContainerId("abcd12");
		sampleResultsDTO.setOutputContainerPosition("A1");
		sampleResultsDTO.setSampleReagentsAndConsumables(getSampleReagentsAndConsumablesList());
		Collection<SampleResultsDetailDTO> sampleResultsDetailList =new ArrayList<>();
		SampleResultsDetailDTO sampleResultsDetailDTO=new SampleResultsDetailDTO();
		sampleResultsDetailDTO.setAttributeName("PI");
		sampleResultsDetailDTO.setAttributeValue("ABC");
		sampleResultsDetailList.add(sampleResultsDetailDTO);
		sampleResultsDTO.setSampleResultsDetail(sampleResultsDetailList);
		
		list.add(sampleResultsDTO);
		
		return list;

	}
	
	
	public List<SampleReagentsAndConsumablesDTO>  getSampleReagentsAndConsumablesList() {
		 List<SampleReagentsAndConsumablesDTO> list=new ArrayList<>();
		 SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO=new SampleReagentsAndConsumablesDTO();
		 sampleReagentsAndConsumablesDTO.setAttributeName("Kit");
		 sampleReagentsAndConsumablesDTO.setAttributeValue("KIT;KIT");
		 
		 
		list.add(sampleReagentsAndConsumablesDTO);
		
		return list;

	}
	
	public List<SampleResultsDTO>  getExistingSampleResultsListSecond() {
		 List<SampleResultsDTO> list=new ArrayList<>();
		SampleResultsDTO sampleResultsDTO=new SampleResultsDTO();
		sampleResultsDTO.setRunResultsId(Long.parseLong("1001"));
		sampleResultsDTO.setAccesssioningId("20201");
		sampleResultsDTO.setOutputContainerId("abcd12");
		sampleResultsDTO.setOutputContainerPosition("A1");
		sampleResultsDTO.setSampleReagentsAndConsumables(getSampleReagentsAndConsumablesListSecond());
		list.add(sampleResultsDTO);
		
		return list;

	}
	
	
	public List<SampleReagentsAndConsumablesDTO>  getSampleReagentsAndConsumablesListSecond() {
		 List<SampleReagentsAndConsumablesDTO> list=new ArrayList<>();
		 SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO=new SampleReagentsAndConsumablesDTO();
		 sampleReagentsAndConsumablesDTO.setAttributeName("MasterMix");
		 sampleReagentsAndConsumablesDTO.setAttributeValue("MasterMix;MasterMix");
		 
		 
		list.add(sampleReagentsAndConsumablesDTO);
		
		return list;

	}
	public QueryMessage getQueryMessage() {

		QueryMessage queryMessage = new QueryMessage();
		queryMessage.setDeviceId("abc");
		queryMessage.setMessageControlId("ABCDEF");
		List<String> plateIdlist=new ArrayList<>();
		plateIdlist.add("ABC,CDE");
		queryMessage.setPlateId(plateIdlist);
		queryMessage.setDateTimeMessageGenerated(formatter.format(new Date()));
		return queryMessage;

	}
	
	
	public AcknowledgementMessage getACKMessage() {

		AcknowledgementMessage ackMessage = new AcknowledgementMessage();
		ackMessage.setMessageControlId("12345");
		ackMessage.setStatus("AA");
		ackMessage.setDateTimeMessageGenerated(formatter.format(new Date()));
		return ackMessage;

	}

}
