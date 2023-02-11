 /*******************************************************************************
 *  * File Name: AssayDataRestAssuredITTest.java            
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
package com.roche.connect.amm.restassuredIT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.amm.dto.AssayListDataDTO;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.amm.dto.MolecularIDTypeDTO;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.amm.dto.SampleTypeDTO;
import com.roche.connect.common.amm.dto.TestOptionsDTO;
import com.roche.connect.amm.util.PropertyReaderUtil;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

/**
 * The Class AssayDataRestAssuredITTest.
 */
public class AssayDataRestAssuredITTest {

	/** The actual assay types. */
	List<AssayTypeDTO> actualAssayTypes = null;
	
	/** The expected assay types. */
	List<AssayTypeDTO> expectedAssayTypes = null;
	
	/** The actual sample types. */
	List<SampleTypeDTO> actualSampleTypes = null;
	
	/** The expected sample types. */
	List<SampleTypeDTO> expectedSampleTypes = null;
	
	/** The actual test options. */
	List<TestOptionsDTO> actualTestOptions = null;
	
	/** The expected test options. */
	List<TestOptionsDTO> expectedTestOptions = null;
	
	/** The actual process steps. */
	List<ProcessStepActionDTO> actualProcessSteps = null;
	
	/** The expected process steps. */
	List<ProcessStepActionDTO> expectedProcessSteps = null;
	
	/** The actual input validations. */
	List<AssayInputDataValidationsDTO> actualInputValidations = null;
	
	/** The expected input validations. */
	List<AssayInputDataValidationsDTO> expectedInputValidations = null;
	
	/** The actual list values. */
	List<AssayListDataDTO> actualListValues = null;
	
	/** The expected list values. */
	List<AssayListDataDTO> expectedListValues = null;
	
	/** The actual molecualr values. */
	List<MolecularIDTypeDTO> actualMolecualrValues = null;

	/** The host. */
	private String host = "";
	
	/** The port. */
	private String port = "";
	
	/** The common path. */
	private String commonPath = "";
	
	/** The assay type id. */
	private String assayType = null;
	
	/** The device type. */
	private String deviceType = null;
	
	/** The list type. */
	private String listType = null;
	
	/** The prop reader util. */
	PropertyReaderUtil propReaderUtil = new PropertyReaderUtil();
	
	/** The token. */
	private String token;
	
	/** The juser. */
	private String juser;
	
	/** The jpwd. */
	private String jpwd;
	
	/** The content type. */
	private String contentType;
	
	/** The organization. */
	private String organization;
	
	/** The pas security URL. */
	private String pasSecurityURL;

	/**
	 * Before.
	 */
	@BeforeTest
	public void before() {
		Properties prop = propReaderUtil.getProperty();
		
		juser = prop.getProperty("juser");
		jpwd = prop.getProperty("jpwd");
		contentType = prop.getProperty("contentType");
		organization = prop.getProperty("organization");
		pasSecurityURL = prop.getProperty("pasSecurityURL");
		
		Response response1 = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType(contentType).param("j_username", juser).param("j_password", jpwd).param("org", organization).when().post(pasSecurityURL);
        token = response1.asString();
        //System.out.println("TOken: "+token);
        RestAssured.baseURI = null;
        
		host = prop.getProperty("host");
		port = prop.getProperty("port");
		commonPath = prop.getProperty("commonPath");
		assayType = prop.getProperty("assayType");
		deviceType = prop.getProperty("deviceType");
		listType = prop.getProperty("listType");
		RestAssured.baseURI = host + ":" + port + commonPath;
		expectedAssayTypes = new ArrayList<>();
		expectedSampleTypes = new ArrayList<>();
		expectedTestOptions = new ArrayList<>();
		expectedProcessSteps = new ArrayList<>();
		expectedInputValidations = new ArrayList<>();
		expectedListValues = new ArrayList<>();

		AssayTypeDTO assayDTO1 = new AssayTypeDTO();
		assayDTO1.setAssayType("NIPTHTP");
		assayDTO1.setAssayVersion("V1");
		assayDTO1.setWorkflowDefFile("NIPTHTP_wfm_v1_bpmn.xml");
		assayDTO1.setWorkflowDefVersion("V1");
		assayDTO1.setAssayTypeId(1L);
		expectedAssayTypes.add(assayDTO1);

		SampleTypeDTO sampleTypeDTO = new SampleTypeDTO();
		sampleTypeDTO.setSampleType("Plasma");
		sampleTypeDTO.setMaxAgeDays(5);
		sampleTypeDTO.setAssayType("NIPTHTP");
		expectedSampleTypes.add(sampleTypeDTO);

		TestOptionsDTO testOptionsDTO1 = new TestOptionsDTO();
		testOptionsDTO1.setAssayType("NIPTHTP");
		testOptionsDTO1.setTestName("Harmony");
		testOptionsDTO1.setTestDescription("Harmony");
		expectedTestOptions.add(testOptionsDTO1);

		ProcessStepActionDTO procStepDTO1 = new ProcessStepActionDTO();
		procStepDTO1.setAssayType("NIPTHTP");
		procStepDTO1.setDeviceType("MP24");
		procStepDTO1.setManualVerificationFlag("Y");
		procStepDTO1.setProcessStepName("NA Extraction");
		expectedProcessSteps.add(procStepDTO1);

		AssayInputDataValidationsDTO dataValidationsDTO1 = new AssayInputDataValidationsDTO();
		dataValidationsDTO1.setFieldName("gestational age");
		dataValidationsDTO1.setAssayType("NIPTHTP");
		dataValidationsDTO1.setMinVal(1L);
		dataValidationsDTO1.setMaxVal(7L);
		dataValidationsDTO1.setExpression("NA");
		expectedInputValidations.add(dataValidationsDTO1);

		AssayListDataDTO listDataDTO1 = new AssayListDataDTO();
		listDataDTO1.setListType("ivf status");
		listDataDTO1.setAssayType("NIPTHTP");
		listDataDTO1.setValue("Yes");
		expectedListValues.add(listDataDTO1);

	}

	/**
	 * Gets the assay types pos test.
	 *
	 * @return the assay types pos test
	 */
	@Test(priority = 1)
	public void getAssayTypesPosTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "/assay");
		AssayTypeDTO[] atDTO = response.jsonPath().getObject("$", AssayTypeDTO[].class);
		actualAssayTypes = Arrays.asList(atDTO);
		Assert.assertEquals(response.getStatusCode(), 200);
	}

	/**
	 * Gets the assay types neg test.
	 *
	 * @return the assay types neg test
	 */
	@Test(priority = 2)
	public void getAssayTypesNegTest() {
		AssayTypeDTO[] atDTO = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "/assay").jsonPath().getObject("$",
				AssayTypeDTO[].class);
		actualAssayTypes = Arrays.asList(atDTO);
		Assert.assertNotEquals("DPCM Assay", actualAssayTypes.get(0).getAssayType());
	}

	/**
	 * Gets the sample types pos test.
	 *
	 * @return the sample types pos test
	 */
	//@Test(priority = 3)
	public void getSampleTypesPosTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/" + assayType + "/sampletype");
		SampleTypeDTO[] stDTO = response.jsonPath().getObject("$", SampleTypeDTO[].class);
		actualSampleTypes = Arrays.asList(stDTO);
		Assert.assertEquals(200, response.getStatusCode());
	}

	/**
	 * Gets the sample types neg test.
	 *
	 * @return the sample types neg test
	 */
	//@Test(priority = 4)
	public void getSampleTypesNegTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/" + assayType + "/sampletype");
		SampleTypeDTO[] stDTO = response.jsonPath().getObject("$", SampleTypeDTO[].class);
		actualSampleTypes = Arrays.asList(stDTO);
		Assert.assertNotEquals(30, actualSampleTypes.size());
	}

	/*
	 * @Parameters({ "assayTypeId" })
	 * 
	 * @Test(priority = 5) public void
	 * getTestOptionsByAssayPosTest(@Optional(value = "1") Long assayTypeId) {
	 * Response response = RestAssured.given().request(Method.GET, "assay/" +
	 * assayTypeId + "/testoptions"); TestOptionDTO[] toDTO =
	 * response.jsonPath().getObject("$", TestOptionDTO[].class);
	 * actualTestOptions = Arrays.asList(toDTO); Assert.assertEquals(200,
	 * response.getStatusCode()); }
	 * 
	 * @Parameters({ "assayTypeId" })
	 * 
	 * @Test(priority = 6) public void
	 * getTestOptionsByAssayNegTest(@Optional(value = "1") Long assayTypeId) {
	 * Response response = RestAssured.given().request(Method.GET, "assay/" +
	 * assayTypeId + "/testoptions"); TestOptionDTO[] toDTO =
	 * response.jsonPath().getObject("$", TestOptionDTO[].class);
	 * actualTestOptions = Arrays.asList(toDTO); Assert.assertNotEquals(51,
	 * actualTestOptions.size()); }
	 * 
	 * @Parameters({ "assayTypeId", "deviceType" })
	 * 
	 * @Test(priority = 7) public void
	 * getTestOptionsByAssayAndDevicePosTest(@Optional(value = "1") Long
	 * assayTypeId,
	 * 
	 * @Optional(value = "MP24") String deviceType) { Response response =
	 * RestAssured.given().request(Method.GET, "assay/" + assayTypeId +
	 * "/testoptions?deviceType=" + deviceType); TestOptionDTO[] toDTO =
	 * response.jsonPath().getObject("$", TestOptionDTO[].class);
	 * actualTestOptions = Arrays.asList(toDTO); Assert.assertEquals(200,
	 * response.getStatusCode()); }
	 * 
	 * @Parameters({ "assayTypeId", "deviceType" })
	 * 
	 * @Test(priority = 8) public void
	 * getTestOptionsByAssayAndDeviceNegTest(@Optional(value = "1") Long
	 * assayTypeId,
	 * 
	 * @Optional(value = "MP24") String deviceType) { Response response =
	 * RestAssured.given().request(Method.GET, "assay/" + assayTypeId +
	 * "/testoptions?deviceType=" + deviceType); TestOptionDTO[] toDTO =
	 * response.jsonPath().getObject("$", TestOptionDTO[].class);
	 * actualTestOptions = Arrays.asList(toDTO);
	 * Assert.assertNotEquals(actualTestOptions.size(), 65);; }
	 */

	/**
	 * Gets the process step action by assay pos test.
	 *
	 * @return the process step action by assay pos test
	 */
	//@Test(priority = 9)
	public void getProcessStepActionByAssayPosTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/" + assayType + "/processstepaction");
		ProcessStepActionDTO[] psDTO = response.jsonPath().getObject("$", ProcessStepActionDTO[].class);
		actualProcessSteps = Arrays.asList(psDTO);
		Assert.assertEquals(actualProcessSteps.get(0).getProcessStepName(),
				expectedProcessSteps.get(0).getProcessStepName());
		Assert.assertEquals(response.statusCode(), 200);
	}

	/**
	 * Gets the process step action by assay neg test.
	 *
	 * @return the process step action by assay neg test
	 */
	//@Test(priority = 10)
	public void getProcessStepActionByAssayNegTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/" + assayType + "/processstepaction");
		ProcessStepActionDTO[] psDTO = response.jsonPath().getObject("$", ProcessStepActionDTO[].class);
		actualProcessSteps = Arrays.asList(psDTO);
		Assert.assertNotEquals(55, actualProcessSteps.size());
	}

	/**
	 * Gets the process step action by assayand device pos test.
	 *
	 * @return the process step action by assayand device pos test
	 */
	@Test(priority = 11)
	public void getProcessStepActionByAssayandDevicePosTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET,
				"assay/" + assayType + "/processstepaction?deviceType=" + deviceType);
		ProcessStepActionDTO[] psDTO = response.jsonPath().getObject("$", ProcessStepActionDTO[].class);
		actualProcessSteps = Arrays.asList(psDTO);
		Assert.assertEquals(response.statusCode(), 200);
	}

	/**
	 * Gets the process step action by assayand device neg test.
	 *
	 * @return the process step action by assayand device neg test
	 */
	@Test(priority = 12)
	public void getProcessStepActionByAssayandDeviceNegTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET,
				"assay/" + assayType + "/processstepaction?deviceType=" + deviceType);
		ProcessStepActionDTO[] psDTO = response.jsonPath().getObject("$", ProcessStepActionDTO[].class);
		actualProcessSteps = Arrays.asList(psDTO);
		Assert.assertNotEquals(51, actualProcessSteps.size());
	}

	/**
	 * Gets the assay input data validations pos test.
	 *
	 * @return the assay input data validations pos test
	 */
	//@Test(priority = 13)
	public void getAssayInputDataValidationsPosTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/" + assayType + "/inputdatavalidations");
		AssayInputDataValidationsDTO[] dvDTO = response.jsonPath().getObject("$", AssayInputDataValidationsDTO[].class);
		actualInputValidations = Arrays.asList(dvDTO);
		Assert.assertEquals(response.statusCode(), 200);
	}

	/**
	 * Gets the assay input data validations neg test.
	 *
	 * @return the assay input data validations neg test
	 */
	//@Test(priority = 14)
	public void getAssayInputDataValidationsNegTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/" + assayType + "/inputdatavalidations");
		AssayInputDataValidationsDTO[] dvDTO = response.jsonPath().getObject("$", AssayInputDataValidationsDTO[].class);
		actualInputValidations = Arrays.asList(dvDTO);
		Assert.assertNotEquals(100, actualInputValidations.size());
	}

	/**
	 * Gets the assay list types data pos test.
	 *
	 * @return the assay list types data pos test
	 */
	//@Test(priority = 15)
	public void getAssayListTypesDataPosTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/" + assayType + "/listdata");
		AssayListDataDTO[] lvDTO = response.jsonPath().getObject("$", AssayListDataDTO[].class);
		actualListValues = Arrays.asList(lvDTO);
		Assert.assertEquals(response.statusCode(), 200);
	}

	/**
	 * Gets the assay list types data neg test.
	 *
	 * @return the assay list types data neg test
	 */
	//@Test(priority = 16)
	public void getAssayListTypesDataNegTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/" + assayType + "/listdata");
		AssayListDataDTO[] lvDTO = response.jsonPath().getObject("$", AssayListDataDTO[].class);
		actualListValues = Arrays.asList(lvDTO);
		Assert.assertNotEquals(101, actualListValues.size());
	}

	/**
	 * Gets the assay list types data by list type pos test.
	 *
	 * @return the assay list types data by list type pos test
	 */
	@Test(priority = 17)
	public void getAssayListTypesDataByListTypePosTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET,
				"assay/" + assayType + "/listdata?listType=" + listType);
		AssayListDataDTO[] lvDTO = response.jsonPath().getObject("$", AssayListDataDTO[].class);
		actualListValues = Arrays.asList(lvDTO);
		Assert.assertEquals(response.statusCode(), 200);
	}

	/**
	 * Gets the assay list types data by list type neg test.
	 *
	 * @return the assay list types data by list type neg test
	 */
	@Test(priority = 18)
	public void getAssayListTypesDataByListTypeNegTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET,
				"assay/" + assayType + "/listdata?listType=" + listType);
		AssayListDataDTO[] lvDTO = response.jsonPath().getObject("$", AssayListDataDTO[].class);
		actualListValues = Arrays.asList(lvDTO);
		Assert.assertNotEquals(214, actualListValues.size());
	}

	/**
	 * Gets the molecular id pos test.
	 *
	 * @return the molecular id pos test
	 */
	@Test(priority = 19)
	public void getMolecularIdPosTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/molecularIdDisplay");
		MolecularIDTypeDTO[] molDTO = response.jsonPath().getObject("$", MolecularIDTypeDTO[].class);
		actualMolecualrValues = Arrays.asList(molDTO);
		Assert.assertEquals(actualMolecualrValues.get(0).getAssayTypeId(), 1);
	}

	/**
	 * Gets the molecular id types pos tes.
	 *
	 * @return the molecular id types pos tes
	 */
	@Test(priority = 20)
	public void getMolecularIdTypesPosTes() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/molecularIdDisplay");
		MolecularIDTypeDTO[] molDTO = response.jsonPath().getObject("$", MolecularIDTypeDTO[].class);
		actualMolecualrValues = Arrays.asList(molDTO);
		Assert.assertEquals(actualMolecualrValues.size(), 96);
	}

	/**
	 * Gets the molecular id neg test.
	 *
	 * @return the molecular id neg test
	 */
	@Test(priority = 21)
	public void getMolecularIdNegTest() {
		Response response = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie("brownstoneauthcookie",token).request(Method.GET, "assay/molecularIdDisplay");
		MolecularIDTypeDTO[] molDTO = response.jsonPath().getObject("$", MolecularIDTypeDTO[].class);
		actualMolecualrValues = Arrays.asList(molDTO);
		Assert.assertNotEquals(actualMolecualrValues.size(), 4);
	}

	/**
	 * After.
	 */
	@AfterTest
	public void after() {
		expectedAssayTypes = null;
		actualAssayTypes = null;
		expectedTestOptions = null;
		expectedProcessSteps = null;
		expectedInputValidations = null;
		expectedListValues = null;
		actualMolecualrValues = null;
	}

}
