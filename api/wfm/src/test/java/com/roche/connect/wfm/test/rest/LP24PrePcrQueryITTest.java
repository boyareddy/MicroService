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
import com.roche.connect.wfm.test.util.SecurityToken;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LP24PrePcrQueryITTest {

	String jsonContent = "";
	public static final String jsonForSucessWfmCreation = "src/test/java/resource/Lp24PrePcrQueryPositive.json";
	public static final String jsonForFailureWfmCreation = "src/test/java/resource/Lp24PrePcrQueryNegative.json";
	private String host = "";
	private String port = "";
	private String commonPath = "";
	
    private String token = "";
	private String cookieKey = "brownstoneauthcookie";
	
	private static Logger logger = Logger.getLogger(ForteITTest.class.getName());

	@BeforeTest
	public void beforeTest() {
		token = SecurityToken.getToken();
		Properties prop = PropertyReaderUtil.getProperty();
		host = prop.getProperty("host");
		port = prop.getProperty("port");
		commonPath = prop.getProperty("commonPath");
		RestAssured.baseURI = host + ":" + port + commonPath;
	}

	@Test(priority = 1)
	public void PositiveLP24PrePcrQueryTest() throws IOException {

		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForSucessWfmCreation);
		 int statusCode=200;
	        try {
		Response resp = RestAssured.given().cookie(cookieKey, token).contentType("application/json").body(jsonContent).when()
				.post("/updatewfmprocess/LPQuery");
		Reporter.log("Positive Case :");
		Reporter.log(jsonContent.toString());
		Reporter.log(resp.asString());
		statusCode = resp.getStatusCode();
	}
	catch (Exception ex) {
		logger.error(ex);
	}
		//Assert.assertEquals(statusCode, 200, "wfm has recieved Lp24PrePcr-qbp successfully");

	}

	@Test(priority = 2)
	public void NegativeLP24PrePcrQueryTest() throws IOException {

		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForFailureWfmCreation);
		 int statusCode=400;
	        try {
		Response resp = RestAssured.given().cookie(cookieKey, token).contentType("application/json").body(jsonContent).when()
				.post("/updatewfmprocess/LPQuery");

		statusCode = resp.getStatusCode();
	}
	catch (Exception ex) {
		logger.error(ex);
	}
		//Assert.assertNotEquals(statusCode, 200, "wfm Error occured while receiving Lp24PrePcr-qbp");

	}

}
