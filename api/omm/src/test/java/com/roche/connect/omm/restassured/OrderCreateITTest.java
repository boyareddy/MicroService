/*******************************************************************************
 * File Name: OrderCreateTest.java            
 * Version:  1.0
 * 
 * Authors: Ankit Singh
 * 
 * =========================================
 * 
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 * 
 * =========================================
 * 
 * ChangeLog:
 ******************************************************************************/
package com.roche.connect.omm.restassured;


import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.omm.util.JsonFileReaderAsString;
import com.roche.connect.omm.test.util.PropertyReaderUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * 
 * @author imtiyazm This class is used to test order creation rest api
 */

public class OrderCreateITTest {

	String jsonContent = "";
	private String api = "/order/";
	private String host = "";
	private String port = "";
	private String commonPath = "";
	    private String token = "";
	    private String securityWebUrl ="";
	    private String contentType ="";
	    private String userSecurity = "";
	    private String passSecurity = "";
	    private String domain = "";
	    private String cookieKey = "";
	private int expectedValue = 200;
	public static final String jsonForOrderCreation = "src/test/java/resource/OrderCrudCommon.json";
	public static final String wrongJsonForOrderCreation = "src/test/java/resource/OrderCreateWrong.json";

	/**
	 * setup the default URL and API base path to use throughout the tests.
	 *
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@BeforeTest
	public void setupURL() throws JsonParseException, JsonMappingException, IOException {
		 Properties prop = PropertyReaderUtil.getProperty();
    	 securityWebUrl = prop.getProperty("securityWebUrl");
    	 contentType = prop.getProperty("contentType");
    	 userSecurity = prop.getProperty("userSecurity");
    	 passSecurity = prop.getProperty("passSecurity");
    	 domain = prop.getProperty("domain");
    	 cookieKey = prop.getProperty("cookieKeys");
		 host = prop.getProperty("host");
		 port = prop.getProperty("port");
		 commonPath = prop.getProperty("commonPath");  
		 Response responseCookie = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType(contentType).param("j_username", userSecurity).param("j_password", passSecurity).param("org", domain).when().post(securityWebUrl);
         token = responseCookie.asString();
		RestAssured.baseURI = host+":"+port+commonPath;	

	}

	/**
	 * order creation test with correct json.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws HMTPException the HMTP exception
	 */
	/*@Parameters({ "OrderManagementAPITest" })
	@Test(priority = 1)
	public void testOrderCreationWithCorrectJson() throws IOException, HMTPException {
		 RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		 jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForOrderCreation);
		 requestSpecification.body(jsonContent).contentType("application/json");
		 Response resp = requestSpecification.post(api);
		
		int statusCode = resp.getStatusCode();

		//Assert.assertEquals(statusCode, expectedValue);
		Assert.assertNotEquals(statusCode,expectedValue );

	}*/

	/**
	 * order creation test with incorrect json.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws HMTPException the HMTP exception
	 */
	@Parameters({ "OrderManagementAPITest" })
	@Test(priority = 2)
	public void testOrderCreationWithInCorrectJson() throws IOException, HMTPException {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		jsonContent = JsonFileReaderAsString.getJsonfromFile(wrongJsonForOrderCreation);
		 requestSpecification.body(jsonContent).contentType("application/json");
		 Response resp = requestSpecification.post(api);
		int statusCode = resp.getStatusCode();
		Assert.assertNotEquals(statusCode, expectedValue);

	}

}
