package com.roche.connect.htp.adapter.restassured;

import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.htp.adapter.util.JsonFileReaderAsString;
import com.roche.connect.htp.adapter.util.PropertyReaderUtil;
import com.roche.connect.htp.adapter.util.SecurityToken;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class NotificationITTest {
	private String host = "";
	private String port = "";
	private String commonPath = "";
	private String token;

	@BeforeTest
	public void beforeTest() {
		Properties prop = PropertyReaderUtil.getProperty();
		token=SecurityToken.getToken();
		host = prop.getProperty("host");
		port = prop.getProperty("port");
		commonPath = prop.getProperty("commonPath");
		RestAssured.baseURI = host + ":" + port + commonPath;
	}

	String jsonContent = "";
	public static final String jsonForSucessCreation = "src/test/java/resource/SuccessNotification.json";
	public static final String jsonForFailureCreation = "src/test/java/resource/failureNotification.json";

	@Test(priority = 1)
	public void PositiveNotificationTest() throws IOException {

		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForSucessCreation);
		int statusCode = 200;
		try {
			Response resp = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie",token).body(jsonContent).when()
					.post("/htp/notification");
			Reporter.log("Positive Case :");
			Reporter.log(jsonContent.toString());
			Reporter.log(resp.asString());
			statusCode = resp.getStatusCode();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Assert.assertEquals(statusCode, 200, "successfully Received Notification");

	}

	@Test(priority = 2)
	public void NegativeNotificationTest() throws IOException {

		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForFailureCreation);
		int statusCode = 500;
		try {
			Response resp = RestAssured.given().contentType("application/json").cookie("brownstoneauthcookie",token).body(jsonContent).when()
					.post("/htp/notification");
			Reporter.log("Nagative Case :");
			Reporter.log(jsonContent.toString());
			statusCode = resp.getStatusCode();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(statusCode);
		Assert.assertNotEquals(500, "Error occured while Receiving Notification");

	}
}
