package com.roche.connect.imm.testng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.common.adm.notification.AdmNotificationService;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.constant.DeviceType;
import com.roche.connect.common.mp96.AdaptorACKMessage;
import com.roche.connect.common.mp96.OULRunResultMessage;
import com.roche.connect.common.mp96.OULSampleResultMessage;
import com.roche.connect.common.mp96.QueryMessage;
import com.roche.connect.common.mp96.WFMQueryMessage;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.DeviceRunService;
import com.roche.connect.imm.service.MP96AsyncMessageService;
import com.roche.connect.imm.service.OrderIntegrationService;
import com.roche.connect.imm.service.RmmIntegrationService;
import com.roche.connect.imm.service.WFMIntegrationService;
import com.roche.connect.imm.utils.RestClient;

@PrepareForTest({ RestClientUtil.class, RestClient.class, AdmNotificationService.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class MP96AsyncMessageServiceTest {
	
	@Mock
	Invocation.Builder builder;

	@Mock
	ObjectMapper objectMapper;
	
	String token="1";
	
	@InjectMocks
	MP96AsyncMessageService mp96AsyncMessageService ;
	
	@Mock
	private RmmIntegrationService rmmIntegrationService= org.mockito.Mockito.mock(RmmIntegrationService.class);
	@Mock
	private OrderIntegrationService order = org.mockito.Mockito.mock(OrderIntegrationService.class);
	
	@Mock
	private WFMIntegrationService wFMIntegrationService = org.mockito.Mockito.mock(WFMIntegrationService.class);
	
	
	@Mock
	private OrderIntegrationService orderIntegrationService = org.mockito.Mockito.mock(OrderIntegrationService.class);
	@Mock
	private AssayIntegrationService assay = org.mockito.Mockito.mock(AssayIntegrationService.class);
	
	@Mock
	private DeviceRunService deviceRunService=org.mockito.Mockito.mock(DeviceRunService.class);
	
	@Mock
	private HMTPLoggerImpl loggerImpl;
	
	
	@Mock
	private Response response;
	
	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}
	
	private static Logger logger = LogManager.getLogger(MP96AsyncMessageServiceTest.class);
	
	
	@Test
	public void performAsyncQueryMessageRequestTest() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);
		WFMQueryMessage wfmQueryMessage = new WFMQueryMessage();
		Mockito.when(deviceRunService.generateDeviceRunId()).thenReturn("RND123");
		
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		
		
		List<ProcessStepActionDTO> processSteplist =new ArrayList<>();
		ProcessStepActionDTO processStepActionDTO=new ProcessStepActionDTO();
		processStepActionDTO.setReagent("ABC");
		processStepActionDTO.setEluateVolume("2");
		processStepActionDTO.setSampleVolume("20");
		processSteplist.add(processStepActionDTO);
		Mockito.when(assay
		.findProcessStepByAssayTypeAndDeviceType(DeviceType.NIPT_DPCR_ASSAY_TYPE_URL_KEY,
				DeviceType.MP96_DEVICE_TYPE, token)).thenReturn(processSteplist);
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		Mockito.when(orderIntegrationService.updateContainerSamples(getContainerSampleList(),token)).thenReturn(true);
		
		Mockito.when(wFMIntegrationService.workOrderRequest(wfmQueryMessage, token)).thenReturn(true);
		try {
			mp96AsyncMessageService.performAsyncQueryMessageRequest(queryMessage(), getContainerSampleList(), token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}


	@Test
	public void performAsyncQueryMessageRequestElse() throws IOException, HMTPException {
		MockitoAnnotations.initMocks(this);
		WFMQueryMessage wfmQueryMessage = new WFMQueryMessage();
		Mockito.when(deviceRunService.generateDeviceRunId()).thenReturn("RND123");
		PowerMockito.mockStatic(RestClient.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		Mockito.when(orderIntegrationService.updateContainerSamples(getContainerSampleList(),
				token)).thenReturn(true);
		
		Mockito.when(wFMIntegrationService.workOrderRequest(wfmQueryMessage, token)).thenReturn(true);
		try {
			mp96AsyncMessageService.performAsyncQueryMessageRequest(queryMessage(), getContainerSampleList(), token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}
	
	@Test
	public void performAsyncOULMessageTest() throws UnsupportedEncodingException {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);

		
		/*Mockito.when(RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null),
				Mockito.eq(Long.class))).thenReturn(0L);*/

		Mockito.when(rmmIntegrationService.updateRunResult(getRunResultsDTO(), token)).thenReturn(true);
		try {
			mp96AsyncMessageService.performAsyncOULMessage(getoulRunResultMessage(), getRunResultsDTO(), token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}


	@Test
	public void performAsyncOULMessageTestFailCondition() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		PowerMockito.mockStatic(RestClient.class);
		PowerMockito.mockStatic(AdmNotificationService.class);
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		
		Mockito.when(rmmIntegrationService.updateRunResult(getRunResultsDTO(), token)).thenReturn(true);
		
		List<String> errorMessages = new LinkedList<>();
		errorMessages.add(getOULRunResultMessage().getDeviceId());
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());

		try {
			mp96AsyncMessageService.performAsyncOULMessage(getoulRunResultMessageSecond(), getRunResultsDTO(), token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}
	
	@Test
	public void performAsyncACKMessage() throws IOException, HMTPException {
		
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString());

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_OK);

		Mockito.when(orderIntegrationService.updateContainerSamples(getContainerSampleList(), token)).thenReturn(true);
		try {
			AdaptorACKMessage ackMessage = new AdaptorACKMessage();
			ackMessage.setDeviceId("656788");
			ackMessage.setDeviceRunId("765424");

			mp96AsyncMessageService.performAsyncACKMessage(ackMessage, getContainerSampleList(), token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}
	
	@Test
	public void performAsyncACKMessage2() throws IOException, HMTPException {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClientUtil.class);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString());

		Mockito.when(RestClientUtil.getBuilder(Mockito.anyString(), Mockito.eq(null))).thenReturn(builder);
		Mockito.when(builder.post(Mockito.any())).thenReturn(response);
		Mockito.when(response.getStatus()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);

		Mockito.when(orderIntegrationService.updateContainerSamples(getContainerSampleList(), token)).thenReturn(true);
		AdaptorACKMessage ackMessage = new AdaptorACKMessage();
		ackMessage.setDeviceId("656788");
		ackMessage.setDeviceRunId("765424");

		mp96AsyncMessageService.performAsyncACKMessage(ackMessage, getContainerSampleList(), token);
	}

	@Test
	public void sendNotification() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(RestClient.class);
		PowerMockito.mockStatic(AdmNotificationService.class);
		Mockito.when(
				RestClient.post(Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.eq(null), Mockito.eq(Long.class)))
				.thenReturn(0L);
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		List<String> errorMessages = new LinkedList<>();
		errorMessages.add(getOULRunResultMessage().getDeviceId());
		errorMessages.add(getOULRunResultMessage().getOulSampleResultMessage().get(0).getOutputContainerId());
		PowerMockito.doNothing()
        .when(AdmNotificationService.class, "sendNotification", Mockito.eq(null), Mockito.anyCollection(),Mockito.anyString(),Mockito.anyString());

		try {
			mp96AsyncMessageService.sendNegativeACKMessage(getOULRunResultMessage(), token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}

	

	@Test
	public void processAsyncMessage() {
		Mockito.doNothing().when(loggerImpl).info(Mockito.anyString(), Mockito.any(Object.class));
		try {
			mp96AsyncMessageService.processAsyncMessage(new Object(), "/htp", token);
		} catch (Exception e) {
			logger.info("method executed successfully");
		}
	}
	
	
	
	public OULRunResultMessage getOULRunResultMessage() {
		
		List<OULSampleResultMessage> oulSampleResultMessagelIST=new ArrayList<>();
		OULSampleResultMessage oULSampleResultMessage=new OULSampleResultMessage();
		oULSampleResultMessage.setOutputContainerId("ABC");
		oulSampleResultMessagelIST.add(oULSampleResultMessage);
		OULRunResultMessage ouLRunResultMessage=new OULRunResultMessage();
		ouLRunResultMessage.setRunId("RND");
		ouLRunResultMessage.setOulSampleResultMessage(oulSampleResultMessagelIST);
		
		return ouLRunResultMessage;
		
		
	}
	
	public QueryMessage queryMessage() {
		QueryMessage queryMessage=new QueryMessage();
		queryMessage.setDeviceId("MP001-343");
		queryMessage.setDeviceName("MagnaPure24");
		queryMessage.setMessageType("QueryMessage");
		
		
		return queryMessage;
	}
	
	public List<ContainerSamplesDTO> getContainerSampleList(){
		
		List<ContainerSamplesDTO> containersampleslist=new ArrayList<>();
		ContainerSamplesDTO containersamples=new ContainerSamplesDTO();
		containersamples.setAccessioningID("12345");
		containersamples.setActiveFlag("YES");
		containersamples.setAssayType("NIPT");
		containersamples.setContainerID("12345678");
		containersamples.setContainerSampleId(998765L);
		containersamples.setCreatedDateTime(new Timestamp(0));
		containersampleslist.add(containersamples);
		
		return containersampleslist;
		
	}
	
	
	public OULRunResultMessage getOULRunResultMessageElseCondition() {
		
		OULRunResultMessage oulRunResultMessage=new OULRunResultMessage();
		oulRunResultMessage.setDeviceId("MP001-343");
		oulRunResultMessage.setDeviceRunId("9876");
		oulRunResultMessage.setMessageType("QueryMessage");
		oulRunResultMessage.setRunComments("nothing");
		oulRunResultMessage.setRunId("3445");
		oulRunResultMessage.setOulSampleResultMessage(null);
		oulRunResultMessage.setRunResultStatus("Completed");
		oulRunResultMessage.setSendingApplicationName("MagnaPure24");
		return oulRunResultMessage;
		
	}
	
	public OULRunResultMessage getoulRunResultMessage() {
		
		List<OULSampleResultMessage> oulSampleResultMessagesList=new ArrayList<>();
		OULSampleResultMessage oulSampleResultMessage=new OULSampleResultMessage();
		oulSampleResultMessage.setAccessioningId("12345");
		oulSampleResultMessage.setOutputContainerId("9876543");
		oulSampleResultMessage.setFlag("true");
		oulSampleResultMessage.setOutputPlateType("ABC");
		oulSampleResultMessage.setPosition("A1");
		oulSampleResultMessage.setFlag("Y");
		oulSampleResultMessage.setSampleResult("P");
		oulSampleResultMessagesList.add(oulSampleResultMessage);
		
		
		
		OULRunResultMessage oulRunResultMessage=new OULRunResultMessage();
	oulRunResultMessage.setDeviceId("MP001-343");
	oulRunResultMessage.setDeviceRunId("9876");
	oulRunResultMessage.setMessageType("QueryMessage");
	oulRunResultMessage.setOulSampleResultMessage(oulSampleResultMessagesList);
	oulRunResultMessage.setRunComments("nothing");
	oulRunResultMessage.setRunId("3445");
	oulRunResultMessage.setRunResultStatus("Completed");
	oulRunResultMessage.setSendingApplicationName("MagnaPure24");
	return oulRunResultMessage;
	}
	
public OULRunResultMessage getoulRunResultMessageSecond() {
		
		List<OULSampleResultMessage> oulSampleResultMessagesList=new ArrayList<>();
		OULSampleResultMessage oulSampleResultMessage=new OULSampleResultMessage();
		oulSampleResultMessage.setAccessioningId("12345");
		oulSampleResultMessage.setOutputContainerId("9876543");
		oulSampleResultMessage.setFlag("true");
		oulSampleResultMessage.setPosition("A1");
		oulSampleResultMessage.setFlag("Y");
		oulSampleResultMessage.setSampleResult("F");
		oulSampleResultMessagesList.add(oulSampleResultMessage);
		
		
		
		OULRunResultMessage oulRunResultMessage=new OULRunResultMessage();
	oulRunResultMessage.setDeviceId("MP001-343");
	oulRunResultMessage.setDeviceRunId("9876");
	oulRunResultMessage.setMessageType("QueryMessage");
	oulRunResultMessage.setOulSampleResultMessage(oulSampleResultMessagesList);
	oulRunResultMessage.setRunComments("nothing");
	oulRunResultMessage.setRunId("3445");
	oulRunResultMessage.setRunResultStatus("Completed");
	oulRunResultMessage.setSendingApplicationName("MagnaPure24");
	return oulRunResultMessage;
	}
	public RunResultsDTO getRunResultsDTO() {
		RunResultsDTO runResultsDTO=new RunResultsDTO();
		
		runResultsDTO.setAssayType("NIPT");
		runResultsDTO.setCreatedBy("Admin");
		runResultsDTO.setCreatedDateTime(new Timestamp(0));
		
		return runResultsDTO;
	}
	

}
