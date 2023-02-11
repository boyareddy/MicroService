package com.roche.connect.adm.test.restAssured;

import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.adm.test.util.JsonFileReaderAsString;
import com.roche.connect.adm.test.util.PropertyReaderUtil;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AdmTestCasesDetailsITTest {
//	String jsonContent = "";
//	private String api = null;
//	private String admhost = "";
//	private String port1 = "";
//	private String commonPath1 = "";
//	private String token = "";
//	private String securityWebUrl = "";
//	private String contentType = "";
//	private String userSecurity = "";
//	private String passSecurity = "";
//	private String domain = "";
//	private String cookieKey = "";
//	private String viewed = "";
//	private int expectedPosValue = 200;
//	public static final String jsonForAddNotification = "src/test/java/com/roche/connect/adm/test/resource/addNotification.json";
//	public static final String jsonForUpdateNotification = "src/test/java/com/roche/connect/adm/test/resource/updateNotification.json";
//	@BeforeTest
//	public void setupURL() throws JsonParseException, JsonMappingException, IOException {
//		Properties prop = PropertyReaderUtil.getProperty();
//		securityWebUrl = prop.getProperty("admSecurityWebUrl");
//		contentType = prop.getProperty("contentType");
//		userSecurity = prop.getProperty("userSecurity");
//		passSecurity = prop.getProperty("passSecurity");
//		domain = prop.getProperty("domain");
//		cookieKey = prop.getProperty("cookieKeys");
//		admhost = prop.getProperty("admhost");
//		port1 = prop.getProperty("port1");
//		commonPath1 = prop.getProperty("commonPath1");
//		viewed=prop.getProperty("viewed");
//		RestAssured.useRelaxedHTTPSValidation();
//		
//		Response responseCookie = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType(contentType).param("j_username", userSecurity).param("j_password", passSecurity).param("org", domain).when().post(securityWebUrl);
//		token = responseCookie.asString();
//		RestAssured.baseURI =null;
//		RestAssured.baseURI = admhost + ":" + port1 + commonPath1;
//	}
//	//@Test(priority = 1)
//	public void getNotificationsTest() throws IOException, HMTPException {
//		api = "/notification";
//		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey, token).queryParam("viewed", viewed);
//
//		Response resp = requestSpecification.request(Method.GET, api);
//		int statusCode = resp.getStatusCode();
//		Assert.assertEquals(statusCode, expectedPosValue);
//	}
//	//@Test(priority = 2)
//	public void AddNotificationTest() throws IOException {
//		api = "/notification";
//		Properties prop = PropertyReaderUtil.getProperty();
//		securityWebUrl = prop.getProperty("securityWebUrl");
//		contentType = prop.getProperty("contentType");
//		userSecurity = prop.getProperty("userSecurity");
//		passSecurity = prop.getProperty("passSecurity");
//		domain = prop.getProperty("domain");
//		cookieKey = prop.getProperty("cookieKeys");
//		ObjectMapper mapper = new ObjectMapper();
//		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForAddNotification);
//		
//		JsonNode jsonNode = mapper.readTree(jsonContent);
//		NotificationMessage notificationMessage = mapper.readValue(mapper.treeAsTokens(jsonNode), NotificationMessage.class);
//		//NotificationMessage.setuserId(1);  
//		Response resp = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType("application/json").cookie(cookieKey, token).body(notificationMessage).when().post("/notification");
//		Reporter.log("Positive Case :");
//		Reporter.log(jsonContent.toString());
//		Reporter.log(resp.asString()); 
//		int statusCode = resp.getStatusCode();
//		Assert.assertEquals(statusCode,expectedPosValue);
//	}
//	//@Test(priority = 3)
//	public void UpdateNotificationTest() throws IOException {
//		api = "/notification";
//		try {
//			Properties prop = PropertyReaderUtil.getProperty();
//			securityWebUrl = prop.getProperty("admSecurityWebUrl");
//			contentType = prop.getProperty("contentType");
//			userSecurity = prop.getProperty("userSecurity");
//			passSecurity = prop.getProperty("passSecurity");
//			domain = prop.getProperty("domain");
//			cookieKey = prop.getProperty("cookieKeys");
//			jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForUpdateNotification);
//			Response resp = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType("application/json").cookie(cookieKey, token).body(jsonContent).when().put("/notification");
//			Reporter.log("Positive Case :");
//			Reporter.log(jsonContent.toString());
//			Reporter.log(resp.asString());
//			int statusCode = resp.getStatusCode();
//			Assert.assertEquals(statusCode,expectedPosValue);
//		} catch (IOException e) {
//			e.printStackTrace();
//	              }
//   }
}
