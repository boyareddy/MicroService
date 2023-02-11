 /*******************************************************************************
 *  * File Name: AssayInputDataValidationsTest.java            
 *  * Version:  1.0
 *  * 
 *  * Authors: Dasari Ravindra
 *  * 
 *  * =========================================
 *  * 
 *  * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  * All Rights Reserved.
 *  * 
 *  * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 *  * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 *  * 
 *  * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 *  * 
 *  * =========================================
 *  * 
 *  * ChangeLog:
 *  ******************************************************************************/
package com.roche.connect.amm.testNG;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.roche.connect.amm.model.AssayInputDataValidations;
import com.roche.connect.amm.model.AssayListData;
import com.roche.connect.amm.model.AssayType;
import com.roche.connect.amm.model.DeviceTestOptions;
import com.roche.connect.amm.model.FlagsData;
import com.roche.connect.amm.model.ProcessStepAction;
import com.roche.connect.amm.model.SampleType;
import com.roche.connect.amm.model.TestOptions;
import com.roche.connect.amm.repository.AssayDataValidationReadRepository;
import com.roche.connect.amm.repository.AssayListDataReadRepository;
import com.roche.connect.amm.repository.AssayTypeReadRepository;
import com.roche.connect.amm.repository.DeviceTestOptionsReadRepository;
import com.roche.connect.amm.repository.FlagsDataReadRepository;
import com.roche.connect.amm.repository.ProcessStepActionReadRepository;
import com.roche.connect.amm.repository.SampleTypeReadRepository;
import com.roche.connect.amm.repository.TestOptionsReadRepository;
import com.roche.connect.amm.rest.AssayCrudRestApiImpl;
import com.roche.connect.amm.util.FlagDescriptionCache;

/**
 * The Class AssayInputDataValidationsTest.
 */
@PowerMockIgnore({ "sun.misc.Launcher.*","com.sun.*","javax.*","javax.ws.*","org.mockito.*","javax.management.*","org.w3c.dom.*","org.apache.logging.*",
"org.slf4j.*"})
public class AssayAllAPIsTest {
	 
	/** The assay read repository. */
	@Mock
	AssayTypeReadRepository assayReadRepository = org.mockito.Mockito.mock(AssayTypeReadRepository.class);
	
	/** The sample type read repository. */
	@Mock
	SampleTypeReadRepository sampleTypeReadRepository = org.mockito.Mockito.mock(SampleTypeReadRepository.class);
	
	/** The test options read repository. */
	@Mock
	TestOptionsReadRepository testOptionsReadRepository = org.mockito.Mockito.mock(TestOptionsReadRepository.class);
	
	/** The device test options read repository. */
	@Mock
	DeviceTestOptionsReadRepository deviceTestOptionsReadRepository = org.mockito.Mockito.mock(DeviceTestOptionsReadRepository.class);
	
	/** The process step action read repository. */
	@Mock
	ProcessStepActionReadRepository processStepActionReadRepository = org.mockito.Mockito.mock(ProcessStepActionReadRepository.class);
	
	/** The assay data validation read repository. */
	@Mock
	AssayDataValidationReadRepository assayDataValidationReadRepository = org.mockito.Mockito.mock(AssayDataValidationReadRepository.class);
	
	/** The assay list data read repository. */
	@Mock
	AssayListDataReadRepository assayListDataReadRepository = org.mockito.Mockito.mock(AssayListDataReadRepository.class);
	@Mock
	FlagsDataReadRepository flagsDataReadRepository;
	@Mock HMTPLoggerImpl hmtpLoggerImpl;
	
	
	/** The assay impl. */
	@InjectMocks
	AssayCrudRestApiImpl assayImpl = new AssayCrudRestApiImpl();
	
	
	/** The assay Type. */
	String assayTypeValue =null;
	
	/** The assay type ID. */
	Long assayTypeID=0L; 

	
	/** The list type. */
	String listType = null;


	/** The field name. */
	String fieldName = null;
	
	String groupName = null;
	
	/** The device type. */
	String deviceType = null;
	
	/** The process step name. */
	String processStepName = null;
	
	/** The expected correct code. */
	int expectedCorrectCode = 0;
	
	/** The expected incorrect code. */
	int expectedIncorrectCode = 0;
	
	/** The AssayType class. */
	AssayType assay=null;

	/**
	 * Before test.
	 */
	@BeforeTest
	public void beforeTest() {
		assayTypeValue = "NIPTHTP";
		assayTypeID=1L;
		fieldName = "Maternal Age";
		groupName = "mandate flag";
		listType = "ivf status";
		deviceType = "MP24";
		processStepName = "";
		expectedCorrectCode = 200;
		expectedIncorrectCode = 400;
		
		/** The AssayType class object. */
		assay=new AssayType();
	
		/**
		 * Data mocking for getAssayTypesData
		 */
		List<AssayType> assayTypeList = new ArrayList<AssayType>();
		AssayType assayType = new AssayType();
		assayType.setId(1);
		assayType.setActiveFlag("Y");
		assayType.setAssayTypeName("NIPTHTP");
		assayType.setAssayVersion("V1");
		assayType.setWorkflowDefFile("NIPTHTP_Wfm_v1.bpmn.xml");
		assayType.setWorkflowDefVersion("V1"); 
		assayTypeList.add(assayType);	
		
		/**
		 * Data mocking for getSampleTypesData
		 */
		List<SampleType> sampleTypeList = new ArrayList<SampleType>();
		SampleType sampleType = new SampleType();
		sampleType.setId(1);
		sampleType.setActiveFlag("Y");
		sampleType.setMaxAgeDays(7);
		sampleType.setSampleTypeName("Plasma");
		sampleType.setAssayType("NIPTHTP");
		assay.setId(1);
		sampleType.setAssay(assay);
		sampleTypeList.add(sampleType);
		
		/**
		 * Data mocking for getTestOptionsData
		 */
		List<TestOptions> testOptionsList = new ArrayList<TestOptions>();
		TestOptions testOptions = new TestOptions();
		testOptions.setId(1);
		testOptions.setTestName("Harmony");
		testOptions.setAssayType("NIPTHTP");
		testOptions.setTestDescription("Harmony");
		testOptions.setSequenceId(1);
		testOptions.setActiveFlag("Y");
		AssayType assay=new AssayType();
		assay.setId(1);
		testOptions.setAssay(assay);
		testOptionsList.add(testOptions);

		/**
		 * Data mocking for getDeviceTestOptionsData
		 */
		List<DeviceTestOptions> deviceTestOptionsList = new ArrayList<DeviceTestOptions>();
		DeviceTestOptions deviceTestOptions = new DeviceTestOptions();
		deviceTestOptions.setId(1);
		deviceTestOptions.setActiveFlag("Y");
		deviceTestOptions.setDeviceType("MP24");
		deviceTestOptions.setAssayType("NIPT");
		deviceTestOptions.setProcessStepName("NA Extraction");
		deviceTestOptions.setTestName("Harmony");
		deviceTestOptions.setTestProtocol("Cfna ss 2000");
		assay.setId(1);
		deviceTestOptions.setAssay(assay);
		deviceTestOptionsList.add(deviceTestOptions);
		
		/**
		 * Data mocking for getWFProcessData
		 */
		List<ProcessStepAction> processStepActionList = new ArrayList<ProcessStepAction>();
		ProcessStepAction processStepAction = new ProcessStepAction();
		processStepAction.setId(2);
		processStepAction.setActiveFlag("Y");
		processStepAction.setDeviceType("LP24");
		processStepAction.setManualVerificationFlag("Y");
		processStepAction.setProcessStepName("LP Pre PCR");
		processStepAction.setProcessStepSeq(2);
		processStepAction.setAssayType("NIPT");
		processStepAction.setInputContainerType("8 Tube Strip");
		processStepAction.setOutputContainerType("PCR Plate");
		assay.setId(1);
		processStepAction.setAssay(assay);
		processStepActionList.add(processStepAction);
		
		/**
		 * Data mocking for InputDataValidations
		 */
		List<AssayInputDataValidations> inputdataValidationsList = new ArrayList<AssayInputDataValidations>();
	    inputdataValidationsList.add(getAssayInputDataValidations());
	    assay.setId(1);
		
		/**
		 *  Data mocking for Assay_ListData
		 */
		List<AssayListData> assayListDataList = new ArrayList<AssayListData>();
		AssayListData assayListData = new AssayListData();
		assayListData.setId(1);
		assayListData.setActiveFlag("Y");
		assayListData.setListType("ivf status");
		assayListData.setValue("Yes");
		assayListData.setAssayType("NIPTHTP");
		assay.setId(1);
		assayListData.setAssay(assay);
		assayListDataList.add(assayListData);
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(hmtpLoggerImpl).info(Mockito.anyString(),Mockito.any(Object.class));
		Mockito.when(assayReadRepository.findAssayDetails(Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(assayTypeList);
		Mockito.when(sampleTypeReadRepository.find(Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(sampleTypeList);
		Mockito.when(testOptionsReadRepository.findTestOptions(Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(testOptionsList);
		
		//Mockito.when(deviceTestOptionsReadRepository.findDeviceTestOptions(Mockito.any(String.class),Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(deviceTestOptionsList);
		
		
		Mockito.when(deviceTestOptionsReadRepository.findDeviceTestOptions(Mockito.any(String.class), Mockito.any(String.class),
				Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(deviceTestOptionsList);
		
		Mockito.when(deviceTestOptionsReadRepository.findDeviceTestOptions(Mockito.any(String.class), Mockito.any(String.class),
				Mockito.any(Long.class))).thenReturn(deviceTestOptionsList);
		 
		Mockito.when(deviceTestOptionsReadRepository.findDeviceTestOptions(Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(deviceTestOptionsList);
		 
		
		
		
		Mockito.when(processStepActionReadRepository.findByAssayandProcessStep(Mockito.any(String.class), Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(processStepActionList);
		
		Mockito.when(assayDataValidationReadRepository.findDataInputValidations(Mockito.anyString(),Mockito.anyString(),Mockito.anyLong(),Mockito.anyString())).thenReturn(inputdataValidationsList);
		Mockito.when(assayDataValidationReadRepository.findDataInputValidations(Mockito.anyString(), Mockito.anyString(),Mockito.anyLong())).thenReturn(inputdataValidationsList);
		Mockito.when(assayDataValidationReadRepository.findDataInputValidationsByGroupName(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString())).thenReturn(inputdataValidationsList);
		Mockito.when(assayDataValidationReadRepository.findDataInputValidations(Mockito.anyString(),Mockito.anyLong())).thenReturn(inputdataValidationsList);
		
		Mockito.when(assayListDataReadRepository.findAssayListValues(Mockito.any(String.class), Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(assayListDataList);
		Mockito.when(assayReadRepository.findAssayTypeID(Mockito.any(String.class),Mockito.any(Long.class))).thenReturn(assayTypeID);
		Mockito.when(processStepActionReadRepository.findLastProcessStepDetails(Mockito.anyLong())).thenReturn(processStepActionList);
	}
	
	
   
	
	public AssayInputDataValidations getAssayInputDataValidations(){
	    AssayInputDataValidations inputdataValidation = new AssayInputDataValidations();
	    inputdataValidation.setId(1);
	    inputdataValidation.setActiveFlag("Y");
	    inputdataValidation.setAssayType("NIPTHTP");
	    inputdataValidation.setExpression("NA");
	    inputdataValidation.setFieldName("Maternal Age");
	    inputdataValidation.setGroupName("mandate flag");
	    inputdataValidation.setMaxValue(100L);
	    inputdataValidation.setMinValue(1L);
	    inputdataValidation.setAssay(assay);
        return inputdataValidation;
	}

	/**
	 * Gets the assay type pos test.
	 *
	 * @return the assay type pos test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getAssayTypePosTest() throws HMTPException {
		Response response = assayImpl.getAssayTypesData(assayTypeValue);
		assertEquals(response.getStatus(), expectedCorrectCode);
	}
	
	/**
	 * Gets the assay type neg test.
	 *
	 * @return the assay type neg test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getAssayTypeNegTest() throws HMTPException {
		Response response = assayImpl.getAssayTypesData(assayTypeValue);
		assertNotEquals(response.getStatus(), expectedIncorrectCode);
	}

	
	/**
	 * Gets the sample type pos test.
	 *
	 * @return the sample type pos test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getSampleTypePosTest() throws HMTPException {
		Response response = assayImpl.getSampleTypesData(assayTypeValue);
		assertEquals(response.getStatus(), expectedCorrectCode);
	}

	/**
	 * Gets the assay type neg test.
	 *
	 * @return the assay type neg test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getSampleTypeNegTest() throws HMTPException {
		Response response = assayImpl.getSampleTypesData(assayTypeValue);
		assertNotEquals(response.getStatus(), expectedIncorrectCode);
	}
	
	
	 /**
		 * Gets the TestOptionsData pos test.
		 *
		 * @return the TestOptionsData pos test
		 * @throws HMTPException
		 *             the HMTP exception
		 */
	  @Test
		public void getTestOptionsDataPosTest() throws HMTPException {
			Response response = assayImpl.getTestOptionsData(assayTypeValue);
			assertEquals(response.getStatus(), expectedCorrectCode);
		}

		/**
		 * Gets the TestOptionsData neg test.
		 *
		 * @return the TestOptionsData neg test
		 * @throws HMTPException
		 *             the HMTP exception
		 */
		@Test
		public void getTestOptionsDataNegTest() throws HMTPException {
			Response response = assayImpl.getTestOptionsData(assayTypeValue);
			assertNotEquals(response.getStatus(), expectedIncorrectCode);
		}
		
		/**
		 * Gets the DeviceTestOptionsData pos test.
		 *
		 * @return the DeviceTestOptionsData pos test
		 * @throws HMTPException
		 *             the HMTP exception
		 */
		@Test
		public void getDeviceTestOptionsDataPosTest() throws HMTPException {
			Response response = assayImpl.getDeviceTestOptionsData(assayTypeValue, deviceType, processStepName);
			assertEquals(response.getStatus(), expectedCorrectCode);
		}

		/**
		 * Gets the DeviceTestOptionsData neg test. 
		 *
		 * @return the DeviceTestOptionsData neg test
		 * @throws HMTPException
		 *             the HMTP exception
		 */
		@Test
		public void getDeviceTestOptionsDataNegTest() throws HMTPException {
			Response response = assayImpl.getDeviceTestOptionsData(assayTypeValue, deviceType, null);
			assertNotEquals(response.getStatus(), expectedIncorrectCode);
		}
		
		/**
		 * Gets the ProcessActionData pos test.
		 *
		 * @return the ProcessActionData pos test
		 * @throws HMTPException
		 *             the HMTP exception
		 */
		@Test
		public void getProcessActionDataPosTest() throws HMTPException {
			Response response = assayImpl.getWFProcessData(assayTypeValue, deviceType);
			assertEquals(response.getStatus(), expectedCorrectCode);
		}

		/**
		 * Gets the ProcessActionData neg test.
		 *
		 * @return the ProcessActionData neg test
		 * @throws HMTPException
		 *             the HMTP exception
		 */
		@Test
		public void getProcessActionDataNegTest() throws HMTPException {
			Response response = assayImpl.getWFProcessData(assayTypeValue, deviceType);
			assertNotEquals(response.getStatus(), expectedIncorrectCode);
		}


	/**
	 * Gets the AssayInputValidationData pos test.
	 *
	 * @return the AssayInputValidationData pos test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getAssayInputValidationDataPosTest() throws HMTPException {
		Response response = assayImpl.getAssayInputValidationData(assayTypeValue, fieldName,groupName);
		assertEquals(response.getStatus(), expectedCorrectCode);
	}

	/**
	 * Gets the AssayInputValidationData neg test.
	 *
	 * @return the AssayInputValidationData neg test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getAssayInputValidationDataNegTest() throws HMTPException {
		Response response = assayImpl.getAssayInputValidationData(assayTypeValue, fieldName,null);
	}
	@Test
    public void getAssayInputValidationDataForFieldNameNullNegTest() throws HMTPException {
        Response response = assayImpl.getAssayInputValidationData(assayTypeValue, null,groupName);
    }
	@Test
    public void getAssayInputValidationDataElsePartNegTest() throws HMTPException {
        Response response = assayImpl.getAssayInputValidationData(assayTypeValue, null,null);
    }
	/**
	 * Gets the AssayListData pos test.
	 *
	 * @return the sAssayListData pos test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getAssayListDataPosTest() throws HMTPException {
		Response response = assayImpl.getListData(assayTypeValue, listType);
		assertEquals(response.getStatus(), expectedCorrectCode);
	}
	
	/**
	 * Gets the AssayListData neg test.
	 *
	 * @return the AssayListData neg test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getAssayListDataNegTest() throws HMTPException {
		Response response = assayImpl.getListData(assayTypeValue, listType);
		assertNotEquals(response.getStatus(), expectedIncorrectCode);
	}

	/**
	 * Gets the AssayTypeID pos test.
	 *
	 * @return the AssayTypeID pos test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getAssayTypeIDPosTest() throws HMTPException {
		Response response = assayImpl.getAssayTypeID(assayTypeValue);
		assertEquals(response.getStatus(), expectedCorrectCode);
	}

	/**
	 * Gets the AssayTypeID neg test.
	 *
	 * @return the AssayTypeID neg test
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@Test
	public void getAssayTypeIDNegTest() throws HMTPException {
		Response response = assayImpl.getAssayTypeID(assayTypeValue);
		assertNotEquals(response.getStatus(), expectedIncorrectCode);
	}
	@Test(expectedExceptions=HMTPException.class)
	public void testFetchFlagValues() throws HMTPException {
		assayImpl.getFlagDescriptions(null,null,null,null);

	}
	@Test
	public void testFetchFlagValuesByAssayType() throws HMTPException {	
		Response response = assayImpl.getFlagDescriptions(null,deviceType,null,"F1");
		assertEquals(response.getStatus(),expectedCorrectCode );
	}
	
	@Test
	public void testFetchFlagValuesByDeviceType() throws HMTPException {
		Mockito.when(flagsDataReadRepository.findFlagDTOByAssayTypeAndDeviceTypeAndFlageCode(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.anyLong())).thenReturn(getflagsDataDTO());
		 assayImpl.getFlagDescriptions(null,"dPCR+Analyzer",null,null);
		 Response response =assayImpl.getFlagDescriptions(null,deviceType,assayTypeValue,"F1");
		assayImpl.getFlagDescriptions(null,deviceType,assayTypeValue,null);
		assertEquals(response.getStatus(),expectedCorrectCode );
	}
	private List<FlagsData> getflagsDataDTO(){
		FlagsData dataDTO=new FlagsData();
		dataDTO.setAssayType(assayTypeValue);
		dataDTO.setDeviceType(deviceType);
		dataDTO.setFlagCode("AT001");
		dataDTO.setFlagSeverity("Fatal");
		return Arrays.asList(dataDTO);
	}
	@Test(expectedExceptions=NullPointerException.class)
    public void testMapModelToDTO() throws JSONException, FileNotFoundException, IOException{
		JSONObject flagsDes = new JSONObject(IOUtils.toString(new FileReader("src/test/java/resource/Flags.json")));
		assayImpl.mapModelToDTO(getflagsDataDTO(),flagsDes );
		FlagDescriptionCache flagDescriptionCache=FlagDescriptionCache.getInstance();
		flagDescriptionCache.put("en_US,dpcr",flagsDes );
		flagDescriptionCache.get("en_US,dpcr","http://localhost:/88/amm");
		flagDescriptionCache.getMap("en_US,dpcr","http://localhost:/88/amm");
		flagDescriptionCache.getMap("en_US,mp24","http://localhost:/88/amm");
	  }
	
	 
    /**
     * 
     * @throws HMTPException
     */
    @Test
    public void getLastProcessStepDataTest() throws HMTPException {
        Response response = assayImpl.getLastProcessStepData();
        assertEquals(response.getStatus(), expectedCorrectCode);
    }

	
	
	/**
	 * After test.
	 */
	@AfterTest
	public void afterTest() {
		assayTypeValue = null;
		assayTypeID=0L;
		fieldName = null;
		deviceType = null;
		expectedCorrectCode = 0;
		expectedIncorrectCode = 0;
	}
}
