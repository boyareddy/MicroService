/*******************************************************************************
 * File Name: OrderBySampleIdRestTest.java            
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

import java.util.Properties;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.roche.connect.omm.test.util.PropertyReaderUtil;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author kumar_u Test class for order exist by sample ID
 */
public class OrderBySampleIdRestITTest {
	private String api = "/orders/";
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
	private String sampleId = "";
	private int expectedValue = 200;

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
		sampleId = prop.getProperty("sampleId");
		RestAssured.baseURI = host + ":" + port + commonPath;

	}

	/**
	 * Gets the positive order by sample id test.
	 *
	 * @return the positive order by sample id test
	 */
	@Parameters({ "OrderManagementAPITest" })
	//@Test
	public void getPositiveOrderBySampleIdTest() {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		Response response = requestSpecification.request(Method.GET, api + "?accessioningID=" + sampleId);
		String accessioningId = response.jsonPath().getString("accessioningId");
		//Assert.assertEquals(accessioningId, "[" + sampleId + "]");
		Assert.assertEquals(response.getStatusCode(), expectedValue);

	}

	/**
	 * Gets the negative order by sample id test.
	 *
	 * @return the negative order by sample id test
	 */
	@Parameters({ "OrderManagementAPITest" })
	@Test
	public void getNegativeOrderBySampleIdTest() {
		 RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		Response response = requestSpecification.request(Method.GET, api + "?accessioningID=" + sampleId);
		String accessioningId = response.jsonPath().getString("accessioningId");
		Assert.assertNotEquals(accessioningId, sampleId);

	}
}
