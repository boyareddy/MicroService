package com.roche.connect.wfm.test.rest;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.wfm.test.util.JsonFileReaderAsString;
import com.roche.connect.wfm.test.util.PropertyReaderUtil;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ForteITTest {

	String jsonContent = "";
	public static final String ForteNegativeStatus = "src/test/java/resource/ForteNegativeStatus.json";
	public static final String FortePositiveStatus = "src/test/java/resource/FortePositiveStatus.json";

	private String host = "";
	private String port = "";
	private String commonPath = "";
	private String token="";
	private String securityWebUrl ="";
	private String userSecurity="";
	private String passSecurity="";
	private String domain="";
	private String cookieKey="";
	private String contentType="";
	private static Logger logger = Logger.getLogger(ForteITTest.class.getName());

	@BeforeTest
	public void beforeTest() {
		Properties prop = PropertyReaderUtil.getProperty();
		securityWebUrl = prop.getProperty("securityWebUrl");
		host = prop.getProperty("host");
		port = prop.getProperty("port");
		contentType = prop.getProperty("contentType");
		userSecurity = prop.getProperty("userSecurity");
        passSecurity = prop.getProperty("passSecurity");
        domain = prop.getProperty("domain");
        cookieKey = prop.getProperty("cookieKeys");
		commonPath = prop.getProperty("commonPath");
		RestAssured.useRelaxedHTTPSValidation();
		Response responseCookie = given().contentType(contentType).param("j_username", userSecurity).param("j_password", passSecurity).param("org", domain).when().post(securityWebUrl);
		token = responseCookie.asString();
		baseURI = host + ":" + port + commonPath;
	}

	@Test(priority = 1)
	public void PositiveForteDoneTest() throws IOException {

		jsonContent = JsonFileReaderAsString.getJsonfromFile(FortePositiveStatus);
		int statusCode=200;
		try {
		Response resp = given().cookie(cookieKey,token).contentType("application/json").body(jsonContent).when()
				.post("/updatewfmprocess/FORETEStatus");
		Reporter.log("Positive Case :");
		Reporter.log(jsonContent.toString());
		Reporter.log(resp.asString());
		statusCode = resp.getStatusCode();
		}
		catch (Exception ex) {
			logger.error(ex);
		}
		//Assert.assertEquals(statusCode, 200, "wfm has recieved ForteStatus successfully");

	}

	@Test(priority = 2)
	public void NegativeForteDoneTest() throws IOException {

		jsonContent = JsonFileReaderAsString.getJsonfromFile(ForteNegativeStatus);
		int statusCode=400;
		try {
		Response resp = given().cookie(cookieKey,token).contentType("application/json").body(jsonContent).when()
				.post("/updatewfmprocess/FORETEStatus");
		Reporter.log("Negative Case :");
		Reporter.log(jsonContent.toString());
		statusCode = resp.getStatusCode();
		}
		catch(Exception ex) {
			logger.error(ex);
		}
		Assert.assertNotEquals(statusCode, 200, "wfm Error occured while receiving ForteStatus");

	}

}
