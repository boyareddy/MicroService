package com.roche.connect.rmm.test.restAssured;

import java.io.IOException;
import java.util.Properties;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.rmm.test.util.PropertyReaderUtil;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ProcessStepsResultDetailsTestInWorkflowForNIPTdpcr {

	String jsonContent = "";
	private String api = "/runresults/processstepresults/";
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
    private String workingAccessioningId="";
	private int expectedPosValue = 200;
	private int expectedNegValue = 500;
	
	
	
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
		Response responseCookie = RestAssured.given().relaxedHTTPSValidation().contentType(contentType).param("j_username", userSecurity).param("j_password", passSecurity).param("org", domain).when().post(securityWebUrl);
        token = responseCookie.asString();
        workingAccessioningId=prop.getProperty("workingAccessioningId");
		RestAssured.baseURI = host+":"+port+commonPath;	

	}

	/**
	 * update ProcessStepsResultDetailsInWorkflow info test with correct json.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws HMTPException the HMTP exception
	 */
	
	@Test(priority = 1)
	public void testProcessStepsResultDetailsPositiveInWorkflowForNIPTdpcr() throws IOException, HMTPException {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().cookie(cookieKey,token).queryParam("accessioningId",workingAccessioningId);
		Response resp = requestSpecification.request(Method.GET, api);
		
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
		
		
		
	}

	/**
	 * update ProcessStepsResultDetailsInWorkflow info test with incorrect json.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws HMTPException the HMTP exception
	 */
	
	@Test(priority = 2)
	public void testProcessStepsResultDetailsNegativeInWorkflowForNIPTdpcr() throws IOException, HMTPException {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().cookie(cookieKey,token).queryParam("accessioningId",workingAccessioningId);
		Response resp = requestSpecification.request(Method.GET, api);
		
		int statusCode = resp.getStatusCode();
		Assert.assertNotEquals(statusCode, expectedNegValue);

	}



}
