 /*******************************************************************************
 *  * File Name: AssayProcessStepValuesRestAssuredITTest.java            
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
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.amm.util.PropertyReaderUtil;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * The Class AssayProcessStepValuesRestAssuredITTest.
 */
public class AssayProcessStepValuesRestAssuredITTest {
    
    /** The logger. */
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
    /** The host. */
    private String host = "";
    
    /** The port. */
    private String port = "";
    
    /** The common path. */
    private String commonPath = "";
   
   /** The assay type id. */
   private String assayTypeId="NIPTHTP";
    
   /** The prop reader util. */
   PropertyReaderUtil propReaderUtil=new PropertyReaderUtil();
   
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
	 * Before test.
	 */
    @BeforeTest public void beforeTest() {
    	Properties prop = propReaderUtil.getProperty();
    	
    	juser = prop.getProperty("juser");
		jpwd = prop.getProperty("jpwd");
		contentType = prop.getProperty("contentType");
		organization = prop.getProperty("organization");
		pasSecurityURL = prop.getProperty("pasSecurityURL");
		
		Response response1 = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType(contentType).param("j_username", juser).param("j_password", jpwd).param("org", organization).when().post(pasSecurityURL);
        token = response1.asString();
        RestAssured.baseURI = null;
        
        host = prop.getProperty("host");
        port = prop.getProperty("port");
        commonPath = prop.getProperty("commonPath");
        RestAssured.baseURI = host + ":" + port + commonPath;
    }
    
   
    
    /**
	 * Positive Test case for Process Step Action Values.
	 *
	 * @return the positive process step action values
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    
    /**
	 * Gets the positive process step action values.
	 *
	 * @return the positive process step action values
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    @Parameters({ "ProcessStepActionAPITest" }) 
    //@Test(priority = 1) 
    public void getPositiveProcessStepActionValues() throws JsonParseException, JsonMappingException, IOException {
        RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false);
        Response response = requestSpecification.cookie("brownstoneauthcookie",token).request(Method.GET, "assay/" + assayTypeId + "/processstepaction");
        
        ObjectMapper mapper = new ObjectMapper();
        
        
        JsonNode actualObj = mapper.readTree(response.body().asString());
        try {
            List<ProcessStepActionDTO> processlist =
                mapper.readValue(mapper.treeAsTokens(actualObj), new TypeReference<List<ProcessStepActionDTO>>() {});
            
            Assert.assertEquals(response.getStatusCode(), 200);
            processlist.forEach(e -> {
                Assert.assertEquals("Y", e.getManualVerificationFlag());
                Assert.assertEquals(6,processlist.size());
          
           });
        } catch (Exception exp) {
            logger.error("Positive  test case for processStep values f failed");
        }
        
    }
    
    /**
	 * Negative Test case for Process Step Action Values.
	 *
	 * @return the negative process step action values
	 * @throws JsonProcessingException
	 *             the json processing exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    
    /**
	 * Gets the negative process step action values.
	 *
	 * @return the negative process step action values
	 * @throws JsonProcessingException
	 *             the json processing exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    @Parameters({ "ProcessStepActionAPITest" }) 
    //@Test(priority = 2)
    public void getNegativeProcessStepActionValues() throws JsonProcessingException, IOException {
        
        RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false);
        Response response = requestSpecification.cookie("brownstoneauthcookie",token).request(Method.GET,  "assay/" + assayTypeId + "/processstepaction");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.body().asString());
        
        try {
            List<ProcessStepActionDTO> processlist =
                mapper.readValue(mapper.treeAsTokens(actualObj), new TypeReference<List<ProcessStepActionDTO>>() {});
            processlist.forEach(e -> {
                Assert.assertNotEquals(12, processlist.size());
                Assert.assertNotEquals("N",e.getManualVerificationFlag());
                
            });
            Assert.assertNotEquals(response.getStatusCode(), 400);
        } catch (Exception exp) {
            logger.error("Negative test case for processStep values failed");
        }
        
    }
    
  
    
}
