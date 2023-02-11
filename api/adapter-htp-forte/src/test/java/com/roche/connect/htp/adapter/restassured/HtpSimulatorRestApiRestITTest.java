/*******************************************************************************
 * HtpSimulatorRestApiRestTest.java                  
 *  Version:  1.0
 * 
 *  Authors:  umashankar d
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
 *  umashankar-d@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.htp.adapter.restassured;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.htp.adapter.rest.HtpAdapterRestApiImpl;
import com.roche.connect.htp.adapter.util.PropertyReaderUtil;
import com.roche.connect.htp.adapter.util.SecurityToken;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class HtpSimulatorRestApiRestITTest {
	ObjectMapper mapper = null;
	String runObj = null;
	String cycleObj = null;
	String cycleNegativeObj = null;
	String runId = null;
	JsonNode json = null;
	String startPlannedObj = null;
	String startObj = null;
	String inProgressObj = null;
	String completedObj = null;
	String complex_id = null;

	private String host = "";
	private String port = "";
	private String commonPath = "";
	JSONObject jsonObject;
	private String token;
	private static Logger logger = Logger.getLogger(HtpAdapterRestApiImpl.class.getName());
	RequestSpecification requestSpecification = null;

	@BeforeTest
	public void setupURL() {
		mapper = new ObjectMapper();
		Properties prop = PropertyReaderUtil.getProperty();
		token = SecurityToken.getToken();
		host = prop.getProperty("host");
		port = prop.getProperty("port");
		commonPath = prop.getProperty("commonPath");
		requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false);
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI = host + ":" + port + commonPath;
		jsonReader();
	}

	public void jsonReader() {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader("src/test/java/resource/run.json"));

			jsonObject = (JSONObject) obj;

			runId = (String) jsonObject.get("run_id");
			complex_id = (String) jsonObject.get("complex_id");

			File startPlannedFile = new File("src/test/java/resource/startPlannedRun.json");
			startPlannedObj = mapper.writeValueAsString(mapper.readTree(startPlannedFile));

			File startFile = new File("src/test/java/resource/startRun.json");
			startObj = mapper.writeValueAsString(mapper.readTree(startFile));

			File inProgressFile = new File("src/test/java/resource/startPlannedRun.json");
			inProgressObj = mapper.writeValueAsString(mapper.readTree(inProgressFile));

			File completedFile = new File("src/test/java/resource/startPlannedRun.json");
			completedObj = mapper.writeValueAsString(mapper.readTree(completedFile));

			File cycleFile = new File("src/test/java/resource/cycle.json");
			cycleObj = mapper.writeValueAsString(mapper.readTree(cycleFile));

			File cycleNegFile = new File("src/test/java/resource/cycleNegativeCaseTest.json");
			cycleNegativeObj = mapper.writeValueAsString(mapper.readTree(cycleNegFile));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void freeSpacePositiveTest() {
		ObjectMapper mapper = new ObjectMapper();
		Response res = null;
		try {
			res = RestAssured.given().when().cookie("brownstoneauthcookie", token).get("htp/freespace");
			Assert.assertEquals(res.statusCode(), 200, "Excepted status code returned");
			JsonNode resBody = mapper.readTree(res.getBody().asString());

			boolean isFreeSpace = resBody.get("freespace").isLong() || resBody.get("freespace").isInt();
			Assert.assertEquals(isFreeSpace, true, "Excepted type returned");
			Reporter.log("Positive Case :");
			Reporter.log(res.asString());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void freeSpaceNegativeTest() {
		ObjectMapper mapper = new ObjectMapper();
		Response res = null;
		try {
			requestSpecification = RestAssured.given().when().cookie("brownstoneauthcookie", token);
			res = requestSpecification.get("htp/freespace");
			Reporter.log("Positive Case :");
			Reporter.log(cycleObj.toString());
			Reporter.log(res.asString());
			Assert.assertNotEquals(res.statusCode(), 500, "Excepted status code returned");
			JsonNode resBody = mapper.readTree(res.getBody().asString());
			Assert.assertEquals(resBody.get("freespace").isTextual(), false, "Excepted type returned");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * positive test case for getting the Creating Run Object
	 */

	@Test
	public void getOrderDetailsPositiveTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.get("htp/orders/" + complex_id);
			Reporter.log("Positive Case :");
			Reporter.log(cycleObj.toString());
			Reporter.log(res.asString());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void getOrderDetailsNegativeTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.get("htp/orders/" + complex_id);
			Assert.assertNotEquals(res.statusCode(), 200, "Excepted status code returned");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void createRunPositiveTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").body(jsonObject)
					.cookie("brownstoneauthcookie", token).when().post("htp/runs");
			Reporter.log("Positive Case :");
			Reporter.log(res.asString());

		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	/**
	 * positive test case for getting the Creating Run Object
	 */
	@Test
	public void createRunNegativeTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.body(jsonObject).when().post("htp/runs");
			Assert.assertNotEquals(res.statusCode(), 404, "Excepted status code returned");

		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateStartPlannedRunPositiveTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.body(startPlannedObj).when().put("runs/" + runId);
			Reporter.log("Positive Case :");
			Reporter.log(startPlannedObj.toString());
			Reporter.log(res.asString());
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateStartRunPositiveTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.body(startObj).when().put("runs/" + runId);
			Reporter.log("Positive Case :");
			Reporter.log(startObj.toString());
			Reporter.log(res.asString());
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateStartRunNegativeTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.body(startObj).when().put("runs/" + runId);
			Assert.assertNotEquals(res.statusCode(), 404, "Excepted status code returned");

		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateInProgressRunPositiveTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.body(inProgressObj).when().put("runs/" + runId);
			Reporter.log("Positive Case :");
			Reporter.log(inProgressObj.toString());
			Reporter.log(res.asString());
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateInProgressRunNegativeTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").body(inProgressObj).when()
					.cookie("brownstoneauthcookie", token).put("runs/" + runId);
			Reporter.log("Positive Case :");
			Reporter.log(inProgressObj.toString());
			Reporter.log(res.asString());
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateCompleteRunPositiveTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").body(inProgressObj)
					.cookie("brownstoneauthcookie", token).when().put("runs/" + runId);
			Reporter.log("Positive Case :");
			Reporter.log(inProgressObj.toString());
			Reporter.log(res.asString());
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateInCompleteRunNegativeTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").body(inProgressObj)
					.cookie("brownstoneauthcookie", token).when().put("runs/" + runId);
			Reporter.log("Positive Case :");
			Reporter.log(inProgressObj.toString());
			Reporter.log(res.asString());
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateCyclePositiveTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.body(cycleObj).when().post("htp/runs/" + runId + "/cycle");
			Reporter.log("Positive Case :");
			Reporter.log(cycleObj.toString());
			Reporter.log(res.asString());

		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateCyclePositiveTest2() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.body(cycleNegativeObj).when().post("htp/runs/" + runId + "/cycle");

			Reporter.log("Positive Case :");
			Reporter.log(cycleNegativeObj.toString());
			Reporter.log(res.asString());
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateCycleNegativeTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.body(cycleObj).when().post("htp/runs/" + runId + "/cycle");
			Reporter.log("Positive Case :");
			Reporter.log(cycleObj.toString());
			Reporter.log(res.asString());
			Assert.assertNotEquals(res.statusCode(), 409, "Excepted status code returned");
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void updateCycleNegativeTest2() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token)
					.body(cycleNegativeObj).when().post("htp/runs/" + runId + "/cycle");
			Reporter.log("Positive Case :");
			Reporter.log(cycleObj.toString());
			Reporter.log(res.asString());
			Assert.assertNotEquals(res.statusCode(), 200, "Excepted status code returned");
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void completeCyclePositiveTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").when()
					.post("htp/runs/" + runId + "/cyclescomplete");
			Reporter.log("Positive Case :");
			Reporter.log(res.asString());
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}

	@Test
	public void completeCycleNegativeTest() {
		Response res = null;
		try {
			res = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie", token).when()
					.post("htp/runs/" + runId + "/cyclescomplete");
			Reporter.log("Positive Case :");
			Reporter.log(cycleObj.toString());
			Reporter.log(res.asString());
			Assert.assertNotEquals(res.statusCode(), 404, "Excepted status code returned");
			Assert.assertNotEquals(res.statusCode(), 409, "Excepted status code returned");
		} catch (Exception e) {
            logger.error(e.getMessage());
		}
	}
}
