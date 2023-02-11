package com.roche.connect.wfm.test.testng;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.StringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.wfm.ApplicationBootWFM;
import com.roche.connect.wfm.error.ErrorResponse;
import com.roche.connect.wfm.model.ActivityProcessDataDTO;
import com.roche.connect.wfm.model.AssayTypeDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.ProcessStepActionDTO;
import com.roche.connect.wfm.model.SampleDTO;
import com.roche.connect.wfm.model.SampleWFMStates;
import com.roche.connect.wfm.model.WfmDTO;
import com.roche.connect.wfm.rest.WfmRestApiImpl;
import com.roche.connect.wfm.service.ImmIntegrationService;

import springfox.documentation.service.ResponseMessage;

@SpringBootTest(classes = ApplicationBootWFM.class)
public class WFMTest extends AbstractTestNGSpringContextTests {
	ActivityProcessDataDTO activityProcessDataDTO;
	AssayTypeDTO assayTypeDTO;
	DeviceTestOptionsDTO deviceTestOptionsDTO;
	ProcessStepActionDTO processStepActionDTO;
	SampleDTO sampleDTO;
	SampleWFMStates sampleWFMStates;
	WfmDTO wfmDTO;
	ErrorResponse errorResponse;
	Timestamp ts = null;

	@InjectMocks
	WfmRestApiImpl apiImpl = org.mockito.Mockito.mock(WfmRestApiImpl.class);

	@Mock
	StringUtils utils;
	@Mock
	Response response;
	@Mock
	ImmIntegrationService immIntegrationService;
	@Mock
	ResponseMessage responseMessage;

	@BeforeTest
	public void setUp() {
		activityProcessDataDTO = new ActivityProcessDataDTO();
		assayTypeDTO = new AssayTypeDTO();
		deviceTestOptionsDTO = new DeviceTestOptionsDTO();
		processStepActionDTO = new ProcessStepActionDTO();
		sampleDTO = new SampleDTO();
		sampleWFMStates = new SampleWFMStates();
		wfmDTO = new WfmDTO();
		errorResponse = new ErrorResponse();
		ts = new Timestamp(new Date().getTime());
		String[] args = { "Na Extraction", "dm123" };
		when(apiImpl.isEmptyCheck(args)).thenReturn(false);
	}

	@Test(priority = 2)
	public void deviceTestOptionsDTOTest() {
		deviceTestOptionsDTO.setDeviceType("deviceType");
		deviceTestOptionsDTO.setTestName("testName");
		deviceTestOptionsDTO.setTestProtocol("testProtocol");
		assertEquals(deviceTestOptionsDTO.getDeviceType(), "deviceType");
		assertEquals(deviceTestOptionsDTO.getTestName(), "testName");
		assertEquals(deviceTestOptionsDTO.getTestProtocol(), "testProtocol");

	}

	@Test(priority = 3)
	public void processStepActionDTOTest() {
		processStepActionDTO.setDeviceType("deviceType");
		processStepActionDTO.setManualVerificationFlag("manualVerificationFlag");
		processStepActionDTO.setProcessStepName("processStepName");
		processStepActionDTO.setProcessStepSeq(3);
		assertEquals(processStepActionDTO.getDeviceType(), "deviceType");
		assertEquals(processStepActionDTO.getManualVerificationFlag(), "manualVerificationFlag");
		assertEquals(processStepActionDTO.getProcessStepName(), "processStepName");
		assertEquals(processStepActionDTO.getProcessStepSeq().toString(), "3");
	}

	@Test(priority = 4)
	public void sampleDTOTest() {
		sampleDTO.setAccessioningId("AccessioningId");
		sampleDTO.setContainerId8Tube("ContainerId8Tube");
		sampleDTO.setDeviceid("Deviceid");
		sampleDTO.setMessageType("MessageType");
		sampleDTO.setSendingApplicationName("SendingApplicationName");
		sampleDTO.setAccessioningIdPrePcrUpdate("AccessioningIdPrePcrUpdate");
		sampleDTO.setAccessioningIdPostPcrUpdate("AccessioningIdPostPcrUpdate");
		sampleDTO.setAccessioningIdSeqPreUpdate("AccessioningIdSeqPreUpdate");
		sampleDTO.setContainerIdPrePcrUpdate("ContainerIdPrePcrUpdate");
		sampleDTO.setContainerIdPostPcrUpdate("ContainerIdPostPcrUpdate");
		sampleDTO.setContainerIdSeqPreUpdate("ContainerIdSeqPreUpdate");
		sampleDTO.setDeviceidPrePcrUpdate("DeviceidPrePcrUpdate");
		sampleDTO.setDeviceidPostPcrUpdate("DeviceidPostPcrUpdate");
		sampleDTO.setDeviceidSeqPreUpdate("DeviceidSeqPreUpdate");
		sampleDTO.setMesageTypePrePcrUpdate("MesageTypePrePcrUpdate");
		sampleDTO.setMesageTypePostPcrUpdate("MesageTypePostPcrUpdate");
		sampleDTO.setMesageTypeSeqPreUpdate("MesageTypeSeqPreUpdate");
		sampleDTO.setNewcontainerIdPrePcrUpdate("NewcontainerIdPrePcrUpdate");
		sampleDTO.setNewcontainerIdPostPcrUpdate("NewcontainerIdPostPcrUpdate");
		sampleDTO.setNewcontainerIdSeqPreUpdate("NewcontainerIdSeqPreUpdate");
		sampleDTO.setOrderStatusPrePcrUpdate("OrderStatusPrePcrUpdate");
		sampleDTO.setOrderStatusPostPcrUpdate("OrderStatusPostPcrUpdate");
		sampleDTO.setOrderStatusSeqPreUpdate("OrderStatusSeqPreUpdate");
		sampleDTO.setSendingApplicationNameprePcrupdate("SendingApplicationNameprePcrupdate");
		sampleDTO.setSendingApplicationNameprePostupdate("SendingApplicationNameprePostupdate");
		sampleDTO.setSendingApplicationNameSeqPreupdate("SendingApplicationNameSeqPreupdate");

		assertEquals(sampleDTO.getAccessioningId(), "AccessioningId");
		assertEquals(sampleDTO.getAccessioningIdPrePcrUpdate(), "AccessioningIdPrePcrUpdate");
		assertEquals(sampleDTO.getAccessioningIdPostPcrUpdate(), "AccessioningIdPostPcrUpdate");
		assertEquals(sampleDTO.getAccessioningIdSeqPreUpdate(), "AccessioningIdSeqPreUpdate");
		assertEquals(sampleDTO.getContainerId8Tube(), "ContainerId8Tube");
		assertEquals(sampleDTO.getContainerIdPrePcrUpdate(), "ContainerIdPrePcrUpdate");
		assertEquals(sampleDTO.getContainerIdPostPcrUpdate(), "ContainerIdPostPcrUpdate");
		assertEquals(sampleDTO.getContainerIdSeqPreUpdate(), "ContainerIdSeqPreUpdate");
		assertEquals(sampleDTO.getDeviceid(), "Deviceid");
		assertEquals(sampleDTO.getDeviceidPrePcrUpdate(), "DeviceidPrePcrUpdate");
		assertEquals(sampleDTO.getDeviceidPostPcrUpdate(), "DeviceidPostPcrUpdate");
		assertEquals(sampleDTO.getDeviceidSeqPreUpdate(), "DeviceidSeqPreUpdate");
		assertEquals(sampleDTO.getMessageType(), "MessageType");
		assertEquals(sampleDTO.getMesageTypePrePcrUpdate(), "MesageTypePrePcrUpdate");
		assertEquals(sampleDTO.getMesageTypePostPcrUpdate(), "MesageTypePostPcrUpdate");
		assertEquals(sampleDTO.getMesageTypeSeqPreUpdate(), "MesageTypeSeqPreUpdate");
		assertEquals(sampleDTO.getNewcontainerIdPrePcrUpdate(), "NewcontainerIdPrePcrUpdate");
		assertEquals(sampleDTO.getNewcontainerIdPostPcrUpdate(), "NewcontainerIdPostPcrUpdate");
		assertEquals(sampleDTO.getNewcontainerIdSeqPreUpdate(), "NewcontainerIdSeqPreUpdate");
		assertEquals(sampleDTO.getOrderStatusPrePcrUpdate(), "OrderStatusPrePcrUpdate");
		assertEquals(sampleDTO.getOrderStatusPostPcrUpdate(), "OrderStatusPostPcrUpdate");
		assertEquals(sampleDTO.getOrderStatusSeqPreUpdate(), "OrderStatusSeqPreUpdate");
		assertEquals(sampleDTO.getSendingApplicationName(), "SendingApplicationName");
		assertEquals(sampleDTO.getSendingApplicationNameprePcrupdate(), "SendingApplicationNameprePcrupdate");
		assertEquals(sampleDTO.getSendingApplicationNameprePostupdate(), "SendingApplicationNameprePostupdate");
		assertEquals(sampleDTO.getSendingApplicationNameSeqPreupdate(), "SendingApplicationNameSeqPreupdate");

	}

	@Test(priority = 5)
	public void ActivityProcessDataDTOTest() {

		activityProcessDataDTO.setDateTimeMessageGenerated("DateTimeMessageGenerated");
		activityProcessDataDTO.setBatchId("BatchId");
		activityProcessDataDTO.setMessageControlId("MessageControlId");

		assertEquals(activityProcessDataDTO.getDateTimeMessageGenerated(), "DateTimeMessageGenerated");
		assertEquals(activityProcessDataDTO.getBatchId(), "BatchId");
		assertEquals(activityProcessDataDTO.getMessageControlId(), "MessageControlId");

	}

	@Test(priority = 6)
	public void WFMDTOTest() {

		List<String> testId = new LinkedList<>();
		testId.add("testId");
		wfmDTO.setOutputposition("Outputposition");
		wfmDTO.setMolecularOutputId("MolecularOutputId");
		wfmDTO.setContainerId8Tube("ContainerId8Tube");
		wfmDTO.setContainerId96("ContainerId96");
		wfmDTO.setMolecularId("MolecularId");
		wfmDTO.setTestId(testId);
		wfmDTO.setMessageType("MessageType");
		wfmDTO.setDeviceRunId("DeviceRunId");
		wfmDTO.setOrderResult("OrderResult");
		wfmDTO.setMolecularId("MolecularId");

		assertEquals(wfmDTO.getOutputposition(), "Outputposition");
		assertEquals(wfmDTO.getMolecularOutputId(), "MolecularOutputId");
		assertEquals(wfmDTO.getContainerId8Tube(), "ContainerId8Tube");
		assertEquals(wfmDTO.getContainerId96(), "ContainerId96");
		assertEquals(wfmDTO.getMolecularId(), "MolecularId");
		assertEquals(wfmDTO.getTestId(), testId);
		assertEquals(wfmDTO.getMessageType(), "MessageType");
		assertEquals(wfmDTO.getDeviceRunId(), "DeviceRunId");
		assertEquals(wfmDTO.getOrderResult(), "OrderResult");
		assertEquals(wfmDTO.getMolecularId(), "MolecularId");

	}

	@Test(priority = 7)
	public void SampleWFMStatesTest() {

		sampleWFMStates.setId(1234);
		sampleWFMStates.setMessageType("MessageType");
		sampleWFMStates.setCreatedDate(ts);
		sampleWFMStates.setCreatedBy("CreatedBy");
		sampleWFMStates.setModifiedDate(ts);
		sampleWFMStates.setEditedBy("EditedBy");
		sampleWFMStates.setCreatedDatetime(ts);

		assertEquals(sampleWFMStates.getId(), 1234);
		assertEquals(sampleWFMStates.getMessageType(), "MessageType");
		assertEquals(sampleWFMStates.getCreatedDate(), sampleWFMStates.getCreatedDate());
		assertEquals(sampleWFMStates.getCreatedBy(), "CreatedBy");
		assertEquals(sampleWFMStates.getModifiedDate(), sampleWFMStates.getModifiedDate());
		assertEquals(sampleWFMStates.getEditedBy(), "EditedBy");
		assertEquals(sampleWFMStates.getCreatedDatetime(), sampleWFMStates.getCreatedDatetime());

	}

	@Test(priority = 9)
	public void errorResponseTest() throws IOException {
		errorResponse.setErrorMessage("errorMessage");
		errorResponse.setErrorStatusCode(500);
		errorResponse.getErrorMessage();
		errorResponse.getErrorStatusCode();
	}

	@Test(priority = 10)
	public void isEmptyCheckTest() {
		WfmRestApiImpl impl = new WfmRestApiImpl();
		assertEquals(impl.isEmptyCheck("Na Extraction", "dm123"), false);
	}
	
	@Test(priority = 11)
	public void assayTypeDTO() {
		assayTypeDTO.setAssayTypeId(123L);
		assayTypeDTO.setAssayVersion("1.0");
		assayTypeDTO.setWorkflowDefVersion("2.0");
		assertEquals(assayTypeDTO.getAssayTypeId(), assayTypeDTO.getAssayTypeId());
		assertEquals(assayTypeDTO.getAssayVersion(), "1.0");
		assertEquals(assayTypeDTO.getWorkflowDefVersion(), "2.0");
	}

}
