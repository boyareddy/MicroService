package com.roche.connect.omm.restassured;

import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.roche.connect.omm.test.util.PropertyReaderUtil;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ContainerIdWithOpenStatusRestITTest {
	private String host = "";
	private String port = "";
	private int expectedValue = 200;
	private String token = "";
    private String securityWebUrl ="";
    private String contentType ="";
    private String userSecurity = "";
    private String passSecurity = "";
    private String domain = "";
    private String cookieKey = "";
	private String commonPath="";
	
	private String api="containersamples/containeridlist";
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
		
		
		RestAssured.baseURI = host+":"+port+commonPath;		
	}
	@Test
	public void getPositiveOrderByOrderIdTest() throws JsonParseException, JsonMappingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token).queryParam("status", "open");
	Response response=	requestSpecification.request(Method.GET,api);
	System.out.println(response.asString());
	Assert.assertEquals(response.getStatusCode(), expectedValue);
	}
	@Test
	public void getPositiveOrderByOrderIdNegativeTest() throws JsonParseException, JsonMappingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token).queryParam("status", "");
	Response response=	requestSpecification.request(Method.GET,api);
	System.out.println(response.asString());
	Assert.assertEquals(response.getStatusCode(), 400);
	}

}
