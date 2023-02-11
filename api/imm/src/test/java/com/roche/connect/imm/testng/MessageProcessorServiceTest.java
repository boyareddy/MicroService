/*******************************************************************************
 * MessageProcessorServiceTest.java                  
 *  Version:  1.0
 * 
 *  Authors:  Alexander
 * 
 * *********************
 *  Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  All Rights Reserved.
 * 
 *  NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 *  The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *                
 * *********************
 *  ChangeLog:
 * 
 *  Alexanders@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.imm.testng;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.testng.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.amm.dto.DeviceTestOptionsDTO;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.enums.AssayType;
import com.roche.connect.common.enums.NiptDeviceType;
import com.roche.connect.common.enums.ProcessStepName;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO.ComplexIdDetailsStatus;
import com.roche.connect.common.htp.status.HTPStatusConstants;
import com.roche.connect.common.htp.status.HtpStatus;
import com.roche.connect.common.htp.status.StatusConstants;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleProtocolDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.ApplicationBootIMM;
import com.roche.connect.imm.service.AssayIntegrationService;
import com.roche.connect.imm.service.MessageProcessorService;
import com.roche.connect.imm.service.RmmIntegrationService;

@SpringBootTest(classes = ApplicationBootIMM.class)
public class MessageProcessorServiceTest extends AbstractTestNGSpringContextTests {

	@InjectMocks
	MessageProcessorService messageProcessorService = new MessageProcessorService();


	@Mock
	RmmIntegrationService rmmService;

	@Mock
	AssayIntegrationService assayService;

	@Mock
	ResponseEntity<?> responseEntity;

	String url = "http://localhost:86/rmm/json/rest/api/v1/runresults/deviceid?deviceId=2222&deviceRunId=155155438";
	int expectedCorrectCode = 0;
	int expectedIncorrectCode = 0;
	RunResultsDTO objectA;
	HtpStatus htp;
	List<SampleResultsDTO> sampleResultsList = new ArrayList<>();
	ComplexIdDetailsDTO complexIdDetails = new ComplexIdDetailsDTO();
	List<DeviceTestOptionsDTO> testOptionList = new ArrayList<>();
	List<ProcessStepActionDTO> processStepActions = new ArrayList<>();
	
	String sampleProtocolName = "Protocol Name";
 
	@BeforeTest
	public void beforeTest() {

		RunResultsDTO runResults = new RunResultsDTO();
		runResults.setRunStatus(StatusConstants.RUN_RESULT_COMPLETED);
		runResults.setRunResultId(1000L);
		runResults.setRunCompletionTime(new Timestamp(new Date().getTime()));

		SampleResultsDTO sampleResult = new SampleResultsDTO();

		List<SampleProtocolDTO> sampleProtocols = new ArrayList<>();
		SampleProtocolDTO sampleProtocol = new SampleProtocolDTO();
		sampleProtocol.setProtocolName(sampleProtocolName);
		sampleProtocols.add(sampleProtocol);

		sampleResult.setSampleProtocol(sampleProtocols);
		sampleResult.setRunResultsId(1000L);
		sampleResultsList.add(sampleResult);

		complexIdDetails.setRunProtocol("Protocol");
		complexIdDetails.setComplexCreatedDatetime(new Timestamp(new Date().getTime()));
		complexIdDetails.setStatus(ComplexIdDetailsStatus.USED);

		htp = new HtpStatus();
		htp.setDeviceId("2222");
		htp.setDeviceRunId("155155438");

		expectedCorrectCode = 200;
		expectedIncorrectCode = 400;

		objectA = new RunResultsDTO();
		objectA.setDeviceId("2222");

		

		DeviceTestOptionsDTO testOption = new DeviceTestOptionsDTO();
		testOption.setTestName("Harmony");
		testOption.setTestProtocol("Cfna ss 2000");
		testOption.setDeviceType("MP24");
		testOption.setAssayType("NIPTHTP");
		testOption.setProcessStepName("NA Extraction");
		testOption.setTestOptionId(1L);
		testOptionList.add(testOption);

		DeviceTestOptionsDTO testOption1 = new DeviceTestOptionsDTO();
		testOption1.setTestName("Harmony");
		testOption1.setTestProtocol("Cfna ss 2000");
		testOption1.setDeviceType("MP24");
		testOption1.setAssayType("NIPTHTP");
		testOption1.setProcessStepName("NA Extraction");
		testOption1.setTestOptionId(1L);
		testOptionList.add(testOption1);

		try {
			MockitoAnnotations.initMocks(this);
			Mockito.when(rmmService.getSampleResultsFromUrl(anyString())).thenReturn(sampleResultsList);
			Mockito.when(rmmService.getRunResultsById(anyLong())).thenReturn(runResults);

			List<DeviceTestOptionsDTO> deviceTestOptionsList = new ArrayList<>();
			DeviceTestOptionsDTO deviceTestOptions = new DeviceTestOptionsDTO();
			deviceTestOptions.setProcessStepName("Protocol");
			deviceTestOptionsList.add(deviceTestOptions);

			messageProcessorService.setAmmHostUrl("http://localhost:88/amm/json/rest/api/v1/assay");
			messageProcessorService.setAmmDeviceTestOptionsUrl("devicetestoptions");

			Mockito.when(messageProcessorService.getTestProtocolByByAssayTypeAndDeviceType(AssayType.NIPT.getText(),
					NiptDeviceType.HTP.getText())).thenReturn("HTP Protocol");

		} catch (Exception e) {
			logger.info(
					"Exception occured while mocking msgProcessorService.getSampleResultsFromUrl: " + e.getMessage());
		}

	}

	@BeforeGroups("config-application-yaml")
	public void beforeConfigApplicationYaml() {
		messageProcessorService.setRmmGetSampleResult(new String());
	}

	@AfterGroups("config-application-yaml")
	public void afterConfigApplicationYaml() {
		messageProcessorService.setRmmGetSampleResult(null);
	}

	@Test
	public void getRunResultTest() throws UnsupportedEncodingException {
		try {
			RunResultsDTO resp = messageProcessorService.getRunResult("155155438", "Sequencing");
			assertEquals(resp.getDeviceId(), "2222");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	@Test(priority = 1)
	public void getSampleResultsByProcessStepAndOutputContainerIdPositiveTestCase1() {

		try {
			List<SampleResultsDTO> actualSampleResults = messageProcessorService
					.getSampleResultsByProcessStepAndOutputContainerId(ProcessStepName.LP_SEQ_PP.getText(),
							new String());

			assertEquals(actualSampleResults.size(), 0);

		} catch (UnsupportedEncodingException e) {
			logger.info("Exception occured while testing getSampleResultsByProcessStepAndOutputContainerIdTest: "
					+ e.getMessage());
		}
	}

	@Test(priority = 1)
	public void getSampleResultsByProcessStepAndOutputContainerIdNegativeTestCase1() {

		try {
			List<SampleResultsDTO> actualSampleResults = messageProcessorService
					.getSampleResultsByProcessStepAndOutputContainerId(ProcessStepName.LP_SEQ_PP.getText(),
							new String());

			assertNotEquals(actualSampleResults.size(), 1);

		} catch (UnsupportedEncodingException e) {
			logger.info("Exception occured while testing getSampleResultsByProcessStepAndOutputContainerIdTest: "
					+ e.getMessage());
		}
	}

	@Test(priority = 2, groups = "config-application-yaml")
	public void getSampleResultsByProcessStepAndOutputContainerIdPositiveTestCase2() {

		try {
			List<SampleResultsDTO> actualSampleResults = messageProcessorService
					.getSampleResultsByProcessStepAndOutputContainerId(ProcessStepName.LP_SEQ_PP.getText(),
							new String());

			assertEquals(actualSampleResults.size(), 1);
			if (actualSampleResults.size() > 0) {
				assertEquals(actualSampleResults.get(0).getSampleProtocol().iterator().next().getProtocolName(),
						sampleResultsList.get(0).getSampleProtocol().iterator().next().getProtocolName());
				assertEquals(actualSampleResults.get(0).getRunResultsId(), sampleResultsList.get(0).getRunResultsId());
			}

		} catch (UnsupportedEncodingException e) {
			logger.info("Exception occured while testing getSampleResultsByProcessStepAndOutputContainerIdTest: "
					+ e.getMessage());
		}
	}

	@Test(priority = 2, groups = "config-application-yaml")
	public void getSampleResultsByProcessStepAndOutputContainerIdNegativeTestCase2() {

		try {
			List<SampleResultsDTO> actualSampleResults = messageProcessorService
					.getSampleResultsByProcessStepAndOutputContainerId(ProcessStepName.LP_SEQ_PP.getText(),
							new String());

			assertNotEquals(actualSampleResults.size(), 0);

			if (actualSampleResults.size() > 0) {
				assertNotEquals(actualSampleResults.get(0).getSampleProtocol().iterator().next().getProtocolName(),
						new String());
				assertNotEquals(actualSampleResults.get(0).getRunResultsId(), new Long(10000));
			}

		} catch (UnsupportedEncodingException e) {
			logger.info("Exception occured while testing getSampleResultsByProcessStepAndOutputContainerIdTest: "
					+ e.getMessage());
		}
	}

	@Test(priority = 1)
	public void getSampleResultsByProcessStepAndInputContainerIdPositiveTest1() {
		try {
			List<SampleResultsDTO> actualSampleResults = messageProcessorService
					.getSampleResultsByProcessStepAndInputContainerId(ProcessStepName.LP_SEQ_PP.getText(),
							new String());

			assertEquals(actualSampleResults.size(), 0);

		} catch (UnsupportedEncodingException e) {
			logger.info(
					"Exception occured while testing getSampleResultsByProcessStepAndInputContainerIdPositiveTest1: "
							+ e.getMessage());
		}
	}

	@Test(priority = 1)
	public void getSampleResultsByProcessStepAndInputContainerIdNegativeTestCase1() {

		try {
			List<SampleResultsDTO> actualSampleResults = messageProcessorService
					.getSampleResultsByProcessStepAndInputContainerId(ProcessStepName.LP_SEQ_PP.getText(),
							new String());

			assertNotEquals(actualSampleResults.size(), 1);

		} catch (UnsupportedEncodingException e) {
			logger.info(
					"Exception occured while testing getSampleResultsByProcessStepAndInputContainerIdNegativeTestCase1: "
							+ e.getMessage());
		}
	}

	@Test(priority = 2, groups = "config-application-yaml")
	public void getSampleResultsByProcessStepAndInputContainerIdPositiveTestCase2() {

		try {
			List<SampleResultsDTO> actualSampleResults = messageProcessorService
					.getSampleResultsByProcessStepAndInputContainerId(ProcessStepName.LP_SEQ_PP.getText(),
							new String());

			assertEquals(actualSampleResults.size(), 1);
			if (actualSampleResults.size() > 0) {
				assertEquals(actualSampleResults.get(0).getSampleProtocol().iterator().next().getProtocolName(),
						sampleResultsList.get(0).getSampleProtocol().iterator().next().getProtocolName());
				assertEquals(actualSampleResults.get(0).getRunResultsId(), sampleResultsList.get(0).getRunResultsId());
			}

		} catch (UnsupportedEncodingException e) {
			logger.info(
					"Exception occured while testing getSampleResultsByProcessStepAndInputContainerIdPositiveTestCase2: "
							+ e.getMessage());
		}
	}

	@Test(priority = 2, groups = "config-application-yaml")
	public void getSampleResultsByProcessStepAndInputContainerIdNegativeTestCase2() {

		try {
			List<SampleResultsDTO> actualSampleResults = messageProcessorService
					.getSampleResultsByProcessStepAndInputContainerId(ProcessStepName.LP_SEQ_PP.getText(),
							new String());

			assertNotEquals(actualSampleResults.size(), 0);

			if (actualSampleResults.size() > 0) {
				assertNotEquals(actualSampleResults.get(0).getSampleProtocol().iterator().next().getProtocolName(),
						new String());
				assertNotEquals(actualSampleResults.get(0).getRunResultsId(), new Long(10000));
			}

		} catch (UnsupportedEncodingException e) {
			logger.info(
					"Exception occured while testing getSampleResultsByProcessStepAndInputContainerIdNegativeTestCase2: "
							+ e.getMessage());
		}
	}

	@BeforeGroups("empty-sequencing")
	public void resetConfigForSeq() {
		try {
			messageProcessorService.setRmmGetSampleResult(new String());
			Mockito.when(messageProcessorService
					.getSampleResultsByProcessStepAndInputContainerId(ProcessStepName.SEQUENCING.getText(), "101010"))
					.thenReturn(Collections.emptyList());
			
			ProcessStepActionDTO action1 = new ProcessStepActionDTO();
			action1.setDeviceType("MP24");
			action1.setProcessStepSeq(1);
			action1.setProcessStepName("NA Extraction");
			action1.setInputContainerType("Plasma tube");
			action1.setOutputContainerType("8-tube strip");
			action1.setManualVerificationFlag("Y");
			action1.setAssayType(AssayType.NIPT.getText());
			processStepActions.add(action1);

			ProcessStepActionDTO action2 = new ProcessStepActionDTO();
			action2.setDeviceType("LP24");
			action2.setProcessStepSeq(2);
			action2.setProcessStepName("LP Pre PCR");
			action2.setInputContainerType("8-tube strip");
			action2.setOutputContainerType("PCR plate");
			action2.setManualVerificationFlag("Y");
			action2.setAssayType(AssayType.NIPT.getText());
			processStepActions.add(action2);

			ProcessStepActionDTO action3 = new ProcessStepActionDTO();
			action3.setDeviceType("LP24");
			action3.setProcessStepSeq(3);
			action3.setProcessStepName("LP Post PCR/Pooling");
			action3.setInputContainerType("PCR plate");
			action3.setOutputContainerType("PCR plate");
			action3.setManualVerificationFlag("Y");
			action3.setAssayType(AssayType.NIPT.getText());
			processStepActions.add(action3);

			ProcessStepActionDTO action4 = new ProcessStepActionDTO();
			action4.setDeviceType("LP24");
			action4.setProcessStepSeq(4);
			action4.setProcessStepName("LP Sequencing Prep");
			action4.setInputContainerType("PCR plate");
			action4.setOutputContainerType("Pool tube");
			action4.setManualVerificationFlag("Y");
			action4.setAssayType(AssayType.NIPT.getText());
			processStepActions.add(action4);

			ProcessStepActionDTO action5 = new ProcessStepActionDTO();
			action5.setDeviceType(NiptDeviceType.HTP.getText());
			action5.setProcessStepSeq(5);
			action5.setProcessStepName("Sequencing");
			action5.setInputContainerType("Pool tube");
			action5.setOutputContainerType("File path");
			action5.setManualVerificationFlag("Y");
			action5.setAssayType(AssayType.NIPT.getText());
			processStepActions.add(action5);
			
			Mockito.when(assayService.getProcessStepAction(Mockito.anyString(), Mockito.anyString()))
			.thenReturn(processStepActions);

		} catch (UnsupportedEncodingException e) {
			logger.info("Exception occured while testing resetConfigForSeq: " + e.getMessage());
		}
	}

	@BeforeGroups("used-sequencing")
	public void usedSeqConfig() {
		try {
			messageProcessorService.setRmmGetSampleResult(new String());
			Mockito.when(messageProcessorService
					.getSampleResultsByProcessStepAndInputContainerId(ProcessStepName.SEQUENCING.getText(), anyString()))
					.thenReturn(sampleResultsList);
		} catch (UnsupportedEncodingException e) {
			logger.info("Exception occured while testing resetConfigForSeq: " + e.getMessage());
		}
	}

	
	
	public void getComplexIdDetailsStatusPositiveTest1() throws HMTPException {
		 messageProcessorService.getComplexIdDetailsStatus("101010");
	}

	/*@Test(priority = 3, groups = "empty-sequencing")
	public void getComplexIdDetailsStatusNegativeTest1() {
		ComplexIdDetailsDTO complexIdDetails = msgProcessorService.getComplexIdDetailsStatus("101010");
		assertNotEquals(complexIdDetails.getStatus(), ComplexIdDetailsStatus.USED);
		//assertNotEquals(complexIdDetails.getRunProtocol(), anyString());
	}

	@Test(priority = 4, groups = "empty-sequencing")
	public void getComplexIdDetailsByComplexIdPositiveTest1() {
		ComplexIdDetailsDTO complexIdDetails = msgProcessorService.getComplexIdDetailsByComplexId("101010");
		assertEquals(complexIdDetails.getStatus(), ComplexIdDetailsStatus.READY);
		// assertNotEquals(complexIdDetails.getRunProtocol(), null);
	}

	@Test(priority = 5, groups = "used-sequencing")
	public void getComplexIdDetailsStatusUsedPositiveTest1() {
		ComplexIdDetailsDTO complexIdDetails = msgProcessorService.getComplexIdDetailsStatus("101010");
		assertEquals(complexIdDetails.getStatus(), ComplexIdDetailsStatus.USED);
		assertEquals(complexIdDetails.getRunProtocol(), sampleProtocolName);
	}*/

	/*
	 * @Test(priority = 2) public void getDeviceTestOptionsDataPositiveTest() {
	 * List<DeviceTestOptionsDTO> testOptions =
	 * msgProcessorService.getDeviceTestOptionsData(AssayType.NIPT.getText(),
	 * NiptDeviceType.HTP.getText());
	 * 
	 * if (testOptions.size() > 0) { assertEquals(testOptions.get(0).getAssayType(),
	 * AssayType.NIPT.getText()); assertEquals(testOptions.get(0).getDeviceType(),
	 * NiptDeviceType.HTP.getText()); } }
	 */

	@BeforeGroups("used-sequencing-null-protocol")
	public void usedSeqNullProtocolConfig() {
		try {
			List<SampleResultsDTO> sList = new ArrayList<>();

			SampleResultsDTO sampleResult = new SampleResultsDTO();
			sampleResult.setRunResultsId(1000L);
			sList.add(sampleResult);

			messageProcessorService.setRmmGetSampleResult(new String());
			Mockito.when(messageProcessorService
					.getSampleResultsByProcessStepAndInputContainerId(ProcessStepName.SEQUENCING.getText(), "101010"))
					.thenReturn(sList);

		} catch (UnsupportedEncodingException e) {
			logger.info("Exception occured while testing resetConfigForSeq: " + e.getMessage());
		}
	}

	/*@Test(priority = 5, groups = "used-sequencing-null-protocol")
	public void getComplexIdDetailsStatusUsedPositiveTest2() {
		ComplexIdDetailsDTO complexIdDetails = msgProcessorService.getComplexIdDetailsStatus("101010");
		assertEquals(complexIdDetails.getStatus(), ComplexIdDetailsStatus.USED);
		assertEquals(complexIdDetails.getRunProtocol(), null);
	}

	@Test(priority = 5, groups = "used-sequencing")
	public void getComplexIdDetailsStatusUsedNegativeTest1() {
		ComplexIdDetailsDTO complexIdDetails = msgProcessorService.getComplexIdDetailsStatus("101010");
		assertNotEquals(complexIdDetails.getStatus(), ComplexIdDetailsStatus.READY);
		assertNotEquals(complexIdDetails.getRunProtocol(), "Protocol");
	}*/
	
	@BeforeGroups("update-runresult")
	public void updateRunResultConfig() {
		try {
			
			RunResultsDTO runResult = new RunResultsDTO();
			runResult.setRunStatus(HTPStatusConstants.STARTED.getText());
			runResult.setDeviceRunId("1L");
			runResult.setDeviceId("id");
			Collection<RunReagentsAndConsumablesDTO> runReagentsAndConsumables = new ArrayList<>();
			
			RunReagentsAndConsumablesDTO regent = new RunReagentsAndConsumablesDTO();
			regent.setAttributeName("consumables");
			regent.setAttributeValue("");
			regent.setType("C");
			runReagentsAndConsumables.add(regent);
			runResult.setRunReagentsAndConsumables(runReagentsAndConsumables);
			
			Collection<RunResultsDetailDTO> runResultsDetail = new ArrayList<>();
			RunResultsDetailDTO runDetails = new RunResultsDetailDTO();
			runDetails.setAttributeName("complexId");
			runDetails.setAttributeValue("1010101");
			runResultsDetail.add(runDetails);
			runResult.setRunResultsDetail(runResultsDetail);
			
			Mockito.when(rmmService.getRunResultsByDeviceRunId(anyString())).thenReturn(runResult);
			List<SampleResultsDTO> sList = new ArrayList<>();

			SampleResultsDTO sampleResult = new SampleResultsDTO();
			sampleResult.setRunResultsId(1000L);
			sampleResult.setInputContainerId("InputContainterId");
			sampleResult.setOrderId(1L);
			
			sList.add(sampleResult);

			messageProcessorService.setRmmGetSampleResult(new String());
			Mockito.when(messageProcessorService
					.getSampleResultsByProcessStepAndOutputContainerId(new String(), new String()))
					.thenReturn(sList);

		} catch (UnsupportedEncodingException e) {
			logger.info("Exception occured while testing resetConfigForSeq: " + e.getMessage());
		}
	}

	
	@Test
	public void processNegativeLPSequenceRequestTest() {
	    messageProcessorService.processNegativeLPSequenceRequest(getSpecimenStatusUpdateMessage());
	}
	
    public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessage() {
        SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
        specimenStatusUpdateMessage.setContainerId("678909_A1");
        specimenStatusUpdateMessage.setDeviceSerialNumber("LPSEQ_001");
        specimenStatusUpdateMessage.setSendingApplicationName("LP24SEQPREP");
        specimenStatusUpdateMessage.setReceivingApplication("Connect");
        specimenStatusUpdateMessage.setMessageControlId("345768");
        return specimenStatusUpdateMessage;
    }
	/*@Test(priority = 6, groups = "update-runresult")
	public void updateRunResultsTest1() {
		RunResultsDTO runResult = new RunResultsDTO();
		runResult.setRunStatus(HTPStatusConstants.STARTED.getText());
		runResult.setRunResultId(1L);
		Collection<RunResultsDetailDTO> runResultsDetail = new ArrayList<>();
		RunResultsDetailDTO runDetails = new RunResultsDetailDTO();
		runDetails.setAttributeName("complexId");
		runDetails.setAttributeValue("1010101");
		runResultsDetail.add(runDetails);
		runResult.setRunResultsDetail(runResultsDetail);
		runResult.setRunCompletionTime(Timestamp.from(Instant.now()));
		msgProcessorService.updateRunResults(runResult);
		
	}*/
	
	/*@Test(priority = 6, groups = "update-runresult")
	public void updateRunResultsTest2() {
		RunResultsDTO runResult = new RunResultsDTO();
		runResult.setRunStatus(HTPStatusConstants.COMPLETED.getText());
		runResult.setRunResultId(1L);
		Collection<RunResultsDetailDTO> runResultsDetail = new ArrayList<>();
		RunResultsDetailDTO runDetails = new RunResultsDetailDTO();
		runDetails.setAttributeName("complexId");
		runDetails.setAttributeValue("1010101");
		runResultsDetail.add(runDetails);
		runResult.setRunResultsDetail(runResultsDetail);
		runResult.setRunCompletionTime(Timestamp.from(Instant.now()));
		msgProcessorService.updateRunResults(runResult);
		
	}*/
	
	/*@Test(priority = 6, groups = "update-runresult")
	public void updateRunResultsTest3() {
		RunResultsDTO runResult = new RunResultsDTO();
		runResult.setRunResultId(1L);
		Collection<RunResultsDetailDTO> runResultsDetail = new ArrayList<>();
		RunResultsDetailDTO runDetails = new RunResultsDetailDTO();
		runDetails.setAttributeName("complexId");
		runDetails.setAttributeValue("1010101");
		runResultsDetail.add(runDetails);
		runResult.setRunResultsDetail(runResultsDetail);
		runResult.setRunCompletionTime(Timestamp.from(Instant.now()));
		
		Collection<RunReagentsAndConsumablesDTO> runReagentsAndConsumables = new ArrayList<>();
		RunReagentsAndConsumablesDTO regent = new RunReagentsAndConsumablesDTO();
		regent.setAttributeName("consumables");
		regent.setAttributeValue("");
		regent.setType("C");
		runReagentsAndConsumables.add(regent);
		runResult.setRunReagentsAndConsumables(runReagentsAndConsumables);
		
		msgProcessorService.updateRunResults(runResult);
		
	}*/
}
