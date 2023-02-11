package com.roche.connect.adm.rest.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.adm.test.util.JsonFileReaderAsString;
import com.roche.connect.adm.test.util.PropertyReaderUtil;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeviceManagementTestCases {

	String jsonContent = "";
	private String api = "/json/device/add";
	private String fetchapi="/json/device/fetch";
	private String host = "";
	private String port = "";
	private String commonPath = "";
	private String token = "";
	private String securityWebUrl = "";
	private String contentType = "";
	private String userSecurity = "";
	private String passSecurity = "";
	private String domain = "";
	private String cookieKey = "";
	private String serialNo="";
	private String deviceId="";
	private String name="";
	private String parent_UUID="";
	private String file_extension="";
	private String file_path="";
	File imgFile = null;
	 InputStream stream = null;
	public static final String jsonForDeviceCreation = "src/test/java/com/roche/connect/adm/test/resource/AddNewDevice.json";
	public static final String jsonForDeviceUpdate = "src/test/java/com/roche/connect/adm/test/resource/updateDevice.json";
	public static final String wrongJsonForDeviceCreation = "src/test/java/com/roche/connect/adm/test/resource/WrongAddDevice.json";

	/**
	 * setup the default URL and API base path to use throughout the tests.
	 *
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
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
		serialNo = prop.getProperty("serialNo");
		deviceId = prop.getProperty("deviceId");
		name = prop.getProperty("name");
		parent_UUID = prop.getProperty("parent_UUID");
		file_extension = prop.getProperty("file_extension");
		file_path = prop.getProperty("file_path");
		Response responseCookie = RestAssured.given().contentType(contentType).param("j_username", userSecurity)
				.param("j_password", passSecurity).param("org", domain).when().post(securityWebUrl);
		token = responseCookie.asString();
		RestAssured.baseURI = host + ":" + port + commonPath;

	}

	
	@Parameters({ "OrderManagementAPITest" })
	@Test(priority = 1)
	public void testOrderCreationWithCorrectJson() throws IOException, HMTPException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForDeviceCreation);
		Reporter.log(jsonContent.toString());
		requestSpecification.body(jsonContent).contentType("application/json");
		Response response = requestSpecification.post(api);
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 200);

	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test
	public void testOrderCreationWithWrongJson() throws IOException, HMTPException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		jsonContent = JsonFileReaderAsString.getJsonfromFile(wrongJsonForDeviceCreation);
		Reporter.log(jsonContent.toString());
		requestSpecification.body(jsonContent).contentType("application/json");
		Response response = requestSpecification.post(api);
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 500);

	}

	@Parameters({ "OrderManagementAPITest" })
	@Test(priority = 2)
	public void getDeviceList() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		Response response = requestSpecification.request(Method.GET, fetchapi);
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test
	public void getDeviceListNegativeTest() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		Response response = requestSpecification.request(Method.GET, "/json/device/fetc");
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 500);
	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test(priority = 3)
	public void getDeviceListByExpr() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		Response response = requestSpecification.request(Method.GET, "/json/device/fetch/expr?filterExpression=serialNo='"+serialNo+"'");
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 200);
		
	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test
	public void getDeviceListByExprNegativeTest() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		Response response = requestSpecification.request(Method.GET, "/json/device/fetch/expr?filterExpression=serial='"+serialNo+"'");
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 400);
		
	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test(priority = 3)
	public void updateDeviceDetails() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForDeviceUpdate);
		Reporter.log(jsonContent.toString());
		requestSpecification.body(jsonContent).contentType("application/json");
		Response response = requestSpecification.post("/json/device/update/"+deviceId);
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 200);
		
	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test
	public void updateDeviceDetailsNegativeTest() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForDeviceUpdate);
		Reporter.log(jsonContent.toString());
		requestSpecification.body(jsonContent).contentType("application/json");
		Response response = requestSpecification.post("/json/device/update/35645");
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 500);
		
	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test(priority = 3)
	public void updateDeviceAttributes() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		jsonContent = "{\"device_status\": \"online\"}";
		Reporter.log(jsonContent.toString());
		requestSpecification.body(jsonContent).contentType("application/json");
		Response response = requestSpecification.post("/json/device/updateAttribute/"+deviceId);
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 200);
		
	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test
	public void updateDeviceAttributesNegativeTest() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		jsonContent = "\"device_status\": \"online\"";
		Reporter.log(jsonContent.toString());
		requestSpecification.body(jsonContent).contentType("application/json");
		Response response = requestSpecification.post("/json/device/updateAttribute/"+deviceId);
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.getStatusCode(), 500);
		
	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test(priority = 3)
	public void imageUpload() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		imgFile = new File(file_path);
		stream = new FileInputStream(imgFile);
		requestSpecification.multiPart("file", imgFile).contentType("multipart/form-data");
		Response response = requestSpecification.post("/file/upload?name="+name+"&parent-UUID="+parent_UUID+"&file-extension="+file_extension);
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.body().asString(), "true");
		
	}
	
	@Parameters({ "OrderManagementAPITest" })
	@Test
	public void imageUploadNegativeTest() throws JsonProcessingException, IOException {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
		imgFile = new File(file_path);
		stream = new FileInputStream(imgFile);
		requestSpecification.multiPart("file", imgFile).contentType("multipart/form-data");
		Response response = requestSpecification.post("/file/upload?name="+name+"&parent-UUID="+parent_UUID+"&file-extension=jar");
		Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
		Assert.assertEquals(response.body().asString(), "");
		
	}
	
	@Test
    public void deactiveDevice() {
           RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
           Response response = requestSpecification.delete("/json/device/delete/"+deviceId);
           Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
           Assert.assertEquals(response.getStatusCode(), 200);

    }
	
	@Test
    public void deactiveDeviceNegativeTest() {
		RequestSpecification requestSpecification = RestAssured.given().cookie(cookieKey, token);
        Response response = requestSpecification.delete("/json/device/delete/354678");
        Reporter.log("Response : "+response.getStatusCode()+":" +response.body().asString());
        Assert.assertEquals(response.getStatusCode(), 500);

    }



}
