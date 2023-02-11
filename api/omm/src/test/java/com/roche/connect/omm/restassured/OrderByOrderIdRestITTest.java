/*******************************************************************************
 * File Name: OrderByOrderIdRestTest.java            
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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.omm.test.util.PropertyReaderUtil;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class OrderByOrderIdRestITTest {
	private String api = "/order/";
	private String host = "";
	private String port = "";
	private String commonPath = "";
	private String workingOrderId="";
	private String dummyOrderId="";
	private int expectedValue = 200;
	private String token = "";
    private String securityWebUrl ="";
    private String contentType ="";
    private String userSecurity = "";
    private String passSecurity = "";
    private String domain = "";
    private String cookieKey = "";
	
	/**
	 * Before test.
	 */
	@BeforeTest
	public void beforeTest() {
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
		workingOrderId = prop.getProperty("workingOrderId");
		dummyOrderId = prop.getProperty("dummyOrderId");
		RestAssured.baseURI = host+":"+port+commonPath;		
	}

	/**
	 * After test.
	 */
	@AfterTest
	public void afterTest() {
	}
	
	/**
	 * Gets the positive order by order id.
	 *
	 * @return the positive order by order id
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test(priority = 1)
	public void getPositiveOrderByOrderId() throws JsonParseException, JsonMappingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		Response response = requestSpecification.request(Method.GET, api+workingOrderId);
		/*ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(response.body().asString());

		OrderParentDTO order = mapper.readValue(mapper.treeAsTokens(actualObj), new TypeReference<OrderParentDTO>() {
		});*/

		Assert.assertNotEquals(response.getStatusCode(), expectedValue);
		//Assert.assertEquals(workingOrderId, order.getOrder().getOrderId().toString());
	}
	
	/**
	 * Gets the negative order by order id.
	 *
	 * @return the negative order by order id
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test(priority = 2)
	public void getNegetiveOrderByOrderId() throws JsonParseException, JsonMappingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		Response response = requestSpecification.request(Method.GET, api+dummyOrderId);

		Assert.assertNotEquals(response.getStatusCode(), expectedValue);
		}
	}
