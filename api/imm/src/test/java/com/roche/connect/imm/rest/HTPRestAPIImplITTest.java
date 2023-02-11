/*******************************************************************************
 * HTPRestAPIImplITTest.java                  
 *  Version:  1.0
 * 
 *  Authors:  MaheswaraReddy
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
 *  daram.m@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.imm.rest;

import java.sql.Timestamp;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.imm.utils.PropertyReaderUtil;
import com.roche.connect.imm.utils.SecurityToken;
import com.roche.connect.imm.utils.UrlConstants;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Rest Assure test cases for HTPRestAPIImpl class
 * 
 * @author karnati.u
 *
 */
public class HTPRestAPIImplITTest {
	private static Logger logger = Logger.getLogger(HTPRestAPI.class.getName());
	private String immHostUrl = "";
	private String immCommonPath = "";

	RequestSpecification requestSpecification = null;
	RunResultsDTO runResultsDTO = null;
	String baseURL = UrlConstants.BASE_URL_V1;

	/** The token. */
	private String token;

	@BeforeTest
	public void setUp() {
		Properties prop = PropertyReaderUtil.getProperty();
		token = SecurityToken.getToken();
		immHostUrl = prop.getProperty("immHostUrl");
		immCommonPath = prop.getProperty("immCommonPath");
		RestAssured.baseURI = immHostUrl + immCommonPath;

		requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false)
				.header("Content-Type", "application/json");
		runResultsDTO = getRunResultsDTO();

	}

	/**
	 * Positive Scenario when everything is working fine when we pass correct
	 * URI and RequestBody
	 */
	@Test(priority = 1)
	public void positiveCreateRunTest() {
		int statusCode = 200;
		try {
			Response resp = RestAssured.given().contentType("application/json").body(runResultsDTO)
					.cookie("brownstoneauthcookie", token).when().post("/runs");
			Reporter.log("Positive Case :");
			Reporter.log(runResultsDTO.toString());
			Reporter.log(resp.asString());
			statusCode = resp.getStatusCode();
		} catch (Exception ex) {
			logger.error(ex);
		}
		Assert.assertEquals(statusCode, 200, "imm runs post call success");
	}

	/**
	 * Negative Scenario when URI is wrong/mismatching/NOT-FOUND
	 */
	@Test(priority = 2)
	public void negativeConsumeRunInformationTest1() {
		int statusCode = 200;
		try {
			Response resp = RestAssured.given().contentType("application/json").body("")
					.cookie("brownstoneauthcookie", token).when().post("/runs");
			Reporter.log("Positive Case :");
			Reporter.log(resp.asString());
			statusCode = resp.getStatusCode();
		} catch (Exception ex) {
			logger.error(ex);
		}
		Assert.assertEquals(statusCode, 200, "imm runs post call success");
	}

	@Test(priority = 3)
	public void positiveUpdateRunTest() {
		int statusCode = 200;
		try {

			Random rand = new Random();

			int n = rand.nextInt(5) + 1;

			Response resp = RestAssured.given().contentType("application/json").body(runResultsDTO)
					.cookie("brownstoneauthcookie", token).when().put("/runs/" + n);
			Reporter.log("Positive Case :");
			Reporter.log(runResultsDTO.toString());
			Reporter.log(resp.asString());
			statusCode = resp.getStatusCode();
		} catch (Exception ex) {
			logger.error(ex);
		}
		Assert.assertEquals(statusCode, 200, "imm Updateruns post call success");
	}

	/**
	 * Negative Scenario when URI is wrong/mismatching/NOT-FOUND
	 */

	@Test(priority = 4)
	public void negativeUpdateRunTest() {
		int statusCode = 200;
		try {

			Response resp = RestAssured.given().contentType("application/json").body(runResultsDTO)
					.cookie("brownstoneauthcookie", token).when().put("/runs/");
			Reporter.log("Positive Case :");
			Reporter.log(runResultsDTO.toString());
			Reporter.log(resp.asString());
			statusCode = resp.getStatusCode();
		} catch (Exception ex) {
			logger.error(ex);
		}
		Assert.assertEquals(statusCode, 200, "imm Updateruns post call failure");
	}

	@Test(priority = 4)
	public void positiveGetComplexDetailsTest() {
		int statusCode = 200;
		try {
			Random rand = new Random();

			int n = rand.nextInt(5) + 1;

			Response resp = RestAssured.given().cookie("brownstoneauthcookie", token).queryParam("complexId", n).when()
					.get("/orders");
			Reporter.log("Positive Case :");
			Reporter.log(resp.asString());
			statusCode = resp.getStatusCode();
		} catch (Exception ex) {
			logger.error(ex);
		}
		Assert.assertEquals(statusCode, 200, "imm getComplexDetails get call success");
	}
	
	@Test(priority = 4)
	public void positiveGetRunResultsByDeviceRunIdTest() {
		int statusCode = 200;
		try {
			Random rand = new Random();

			int n = rand.nextInt(5) + 1;

			Response resp = RestAssured.given().cookie("brownstoneauthcookie", token).queryParam("complexId", n).when()
					.get("/runs/"+n);
			Reporter.log("Positive Case :");
			Reporter.log(resp.asString());
			statusCode = resp.getStatusCode();
		} catch (Exception ex) {
			logger.error(ex);
		}
		Assert.assertEquals(statusCode, 200, "imm getRunResultsByDeviceRunId get call success");
	}

	/**
	 * Returns RunResultsDTO object
	 * 
	 * @return
	 */
	public RunResultsDTO getRunResultsDTO() {
		RunResultsDTO runResults = new RunResultsDTO();
		runResults.setDeviceRunId("155155438");
		runResults.setDeviceId("2222");
		runResults.setDvcRunResult(null);
		runResults.setProcessStepName("Sequencing");
		runResults.setRunStatus("Completed");
		runResults.setComments("Run information for HTP/Sequencing");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		runResults.setRunStartTime(ts);
		runResults.setRunRemainingTime(System.currentTimeMillis());
		return runResults;
	}

}
