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

public class HTPITTest {

	String jsonContent = "";
	
	public static final String HtpStartedSuccessJson = "src/test/java/resource/HTPStartedPositive.json";
    public static final String HtpStartedFailureJson = "src/test/java/resource/HTPStartedNegative.json";
    
    public static final String HtpInProcessSuccessJson = "src/test/java/resource/HTPInProcessPositive.json";
    public static final String HtpInProcessFailureJson = "src/test/java/resource/HTPInProcessNegative.json";
    
    public static final String HtpFailedSuccessJson = "src/test/java/resource/HTPFailedPositive.json";
    public static final String HtpFailedFailureJson = "src/test/java/resource/HTPFailedNegative.json";
    
    public static final String HtpCompletedSuccessJson = "src/test/java/resource/HTPCompletedPositive.json";
    public static final String HtpCompletedFailureJson = "src/test/java/resource/HTPCompletedNegative.json";

    private String token = "";
	private String cookieKey = "brownstoneauthcookie";
	
	private String host = "";
	private String port = "";
	private String commonPath = "";
	
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
	public void htpStartedPositiveTest() throws IOException {

		jsonContent = JsonFileReaderAsString.getJsonfromFile(HtpStartedSuccessJson);
		 int statusCode=200;
	        try {
		Response resp = RestAssured.given().contentType("application/json").cookie(cookieKey, token).body(jsonContent).when()
				.post("updatewfmprocess/htpstatus");
		Reporter.log("Positive Case :");
		Reporter.log(jsonContent.toString());
		Reporter.log(resp.asString());
		 statusCode = resp.getStatusCode();
		 //Assert.assertEquals(statusCode, 200, "wfm has recieved HTP Started successfully");
	        }
	        catch(Exception ex) {
				logger.error(ex);
			}

	}

	@Test(priority = 2)
	public void htpStartedNegativeTest() throws IOException {

		jsonContent = JsonFileReaderAsString.getJsonfromFile(HtpStartedFailureJson);
		 int statusCode=400;
	        try {
		Response resp = RestAssured.given().cookie(cookieKey, token).contentType("application/json").body(jsonContent).when()
				.post("updatewfmprocess/htpstatus");
		 statusCode = resp.getStatusCode();
		// Assert.assertNotEquals(statusCode, 200, "wfm Error occured while receiving HTPStarted");
	        }
	        catch(Exception ex) {
				logger.error(ex);
			}

	}
	
	@Test(priority = 3)
    public void htpInProcessPositiveTest() throws IOException {

        jsonContent = JsonFileReaderAsString.getJsonfromFile(HtpInProcessSuccessJson);
        int statusCode=200;
        try {
        Response resp = RestAssured.given().cookie(cookieKey, token).contentType("application/json").body(jsonContent).when()
                .post("updatewfmprocess/htpstatus");
        Reporter.log("Positive Case :");
        Reporter.log(jsonContent.toString());
        Reporter.log(resp.asString());
        statusCode = resp.getStatusCode();
      //  Assert.assertEquals(statusCode, 200, "wfm has recieved HTPInProcess successfully");
    }
    catch(Exception ex) {
		logger.error(ex);
	}

    }

    @Test(priority = 4)
    public void htpInProcessNegativeTest() throws IOException {

        jsonContent = JsonFileReaderAsString.getJsonfromFile(HtpInProcessFailureJson);
        int statusCode=400;
        try {
        Response resp = RestAssured.given().cookie(cookieKey, token).contentType("application/json").body(jsonContent).when()
                .post("updatewfmprocess/htpstatus");
        statusCode = resp.getStatusCode();
      //  Assert.assertNotEquals(statusCode, 200, "wfm Error occured while receiving HtpInProcess");
    }
    catch(Exception ex) {
		logger.error(ex);
	}

    }
    
    @Test(priority = 5)
    public void htpFailedPositiveTest() throws IOException {

        jsonContent = JsonFileReaderAsString.getJsonfromFile(HtpFailedSuccessJson);
        int statusCode=200;
        try {
        Response resp = RestAssured.given().cookie(cookieKey, token).contentType("application/json").body(jsonContent).when()
                .post("updatewfmprocess/htpstatus");
        Reporter.log("Positive Case :");
        Reporter.log(jsonContent.toString());
        Reporter.log(resp.asString());
        statusCode = resp.getStatusCode();
      //  Assert.assertEquals(statusCode, 200, "wfm has recieved HTP Failed test successfully");
    }
    catch(Exception ex) {
		logger.error(ex);
	}

    }

    @Test(priority = 6)
    public void htpFailedNegativeTest() throws IOException {

        jsonContent = JsonFileReaderAsString.getJsonfromFile(HtpFailedFailureJson);
        int statusCode=400;
        try {
        Response resp = RestAssured.given().cookie(cookieKey, token).contentType("application/json").body(jsonContent).when()
                .post("updatewfmprocess/htpstatus");
        statusCode = resp.getStatusCode();
      //  Assert.assertNotEquals(statusCode, 200, "wfm Error occured while receiving HTP Failed");
    }
    catch(Exception ex) {
		logger.error(ex);
	}

    }
    
    @Test(priority = 7)
    public void htpCompletedPositiveTest() throws IOException {

        jsonContent = JsonFileReaderAsString.getJsonfromFile(HtpCompletedSuccessJson);
        int statusCode=200;
        try {
        Response resp = RestAssured.given().cookie(cookieKey, token).contentType("application/json").body(jsonContent).when()
                .post("updatewfmprocess/htpstatus");
        Reporter.log("Positive Case :");
        Reporter.log(jsonContent.toString());
        Reporter.log(resp.asString());
        statusCode = resp.getStatusCode();
       // Assert.assertEquals(statusCode, 200, "wfm has recieved HtpComplete successfully");
    }
    catch(Exception ex) {
		logger.error(ex);
	}

    }

    @Test(priority = 8)
    public void htpCompletedNegativeTest() throws IOException {

        jsonContent = JsonFileReaderAsString.getJsonfromFile(HtpCompletedFailureJson);
        int statusCode=400;
        try {
        Response resp = RestAssured.given().cookie(cookieKey, token).contentType("application/json").body(jsonContent).when()
                .post("updatewfmprocess/htpstatus");
        statusCode = resp.getStatusCode();
      //  Assert.assertNotEquals(statusCode, 200, "wfm Error occured while receiving HtpComplete");
    }
    catch(Exception ex) {
		logger.error(ex);
	}

    }

}
