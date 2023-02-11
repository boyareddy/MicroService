package com.roche.connect.htp.adapter.restassured;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.htp.adapter.util.JsonFileReaderAsString;
import com.roche.connect.htp.adapter.util.PropertyReaderUtil;
import com.roche.connect.htp.adapter.util.SecurityToken;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ForteAdapterRestITTest {
	
		private String host = "";
	    private String port = "";
	    private String commonPath = "";
	    private String token;
	    
	    @BeforeTest public void beforeTest() {
	        Properties prop = PropertyReaderUtil.getProperty();
	        token=SecurityToken.getToken();
	        host = prop.getProperty("host");
	        port = prop.getProperty("port");
	        commonPath = prop.getProperty("commonPath");
	        RestAssured.baseURI = host + ":" + port + commonPath;
	    }
		String jsonContent = "";
		public static final String jsonForSucessHello = "src/test/java/resource/SucessHello.json";
		public static final String jsonForFailureHello = "src/test/java/resource/FailureHello.json";
		
		private static Logger logger = Logger.getLogger(ForteAdapterRestITTest.class.getName());

		
		@Test(priority = 1)
		public void PositivePingTest() throws IOException {

			 int statusCode=200;
		        try {
			Response resp = RestAssured.given().contentType("application/json").body("").cookie("brownstoneauthcookie",token).when()
					.post("forte/ping");
			Reporter.log("Positive Case :");
			Reporter.log(jsonContent.toString());
			Reporter.log(resp.asString());
			statusCode = resp.getStatusCode();
		}
		catch (Exception ex) {
			logger.error(ex);
		}
			Assert.assertEquals(statusCode, 200, "Forte Alive Ping Call");

		}
		
		@Test(priority = 2)
		public void NegativePingTest() throws IOException {

			 int statusCode=400;
		        try {
			Response resp = RestAssured.given().contentType("application/json").body("").cookie("brownstoneauthcookie",token).when()
					.post("forte/ping");
			
			statusCode = resp.getStatusCode();
		}
		catch (Exception ex) {
			logger.error(ex);
		}
			Assert.assertNotEquals(statusCode, 500, "Error occured while calling Ping");

		}

		
		
		
		@Test(priority = 3)
		public void PositiveHelloTest() throws IOException {
			jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForSucessHello);

			 int statusCode=200;
		        try {
			Response resp = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie",token).body(jsonContent).when()
					.post("forte/hello");
			Reporter.log("Positive Case :");
			Reporter.log(jsonContent.toString());
			Reporter.log(resp.asString());
			statusCode = resp.getStatusCode();
		}
		catch (Exception ex) {
			logger.error(ex);
		}
			Assert.assertEquals(statusCode, 200, " Forte Alive Hello Call");

		}
		
		@Test(priority = 4)
		public void NegativeHelloTest() throws IOException {

			jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForFailureHello);
			 int statusCode=400;
		        try {
			Response resp = RestAssured.given().contentType("application/json").body(jsonContent).cookie("brownstoneauthcookie",token).when()
					.post("forte/hello");
			
			statusCode = resp.getStatusCode();
		}
		catch (Exception ex) {
			logger.error(ex);
		}
			Assert.assertNotEquals(statusCode, 500, "Error occured while calling hello");

		}


}
