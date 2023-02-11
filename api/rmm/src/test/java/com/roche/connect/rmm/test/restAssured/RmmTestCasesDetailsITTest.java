package com.roche.connect.rmm.test.restAssured;

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
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.rmm.test.util.JsonFileReaderAsString;
import com.roche.connect.rmm.test.util.PropertyReaderUtil;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RmmTestCasesDetailsITTest {
	String jsonContent = "";
	private String api = null;
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
	private String deviceid = "";
	private String processstepname = "";
	private String runstatus = "";
	private String accessioningId = "";
	private String deviceRunId = "";
	private String sampleresultid = "";
	private String runresultId = "";
	private String outputContainerId = "";
	private String inputContainerId = "";
	private String inputContainerPosition = "";
	private String outputContainerPosition = "";
	private String assayType = "";
	private String processstep = "";
	private int expectedPosValue = 200;
	private int expectedNegValue = 500;
	public static final String jsonForSucessRunResult = "src/test/java/resource/RunResultPositive.json";
	public static final String jsonForSucessSampleResult = "src/test/java/resource/sampleResults.json";
	public static final String jsonForSucessRunResultupdate = "src/test/java/resource/RunResultUpdate.json";

	@BeforeTest
	public void setupURL() throws JsonParseException, JsonMappingException, IOException {
		Properties prop = PropertyReaderUtil.getProperty();
		securityWebUrl = prop.getProperty("rmmsecurityWebUrl");
		contentType = prop.getProperty("rmmcontentType");
		userSecurity = prop.getProperty("rmmuserSecurity");
		passSecurity = prop.getProperty("rmmpassSecurity");
		domain = prop.getProperty("rmmdomain");
		cookieKey = prop.getProperty("rmmcookieKeys");
		host = prop.getProperty("rmmhost");
		port = prop.getProperty("rmmport");
		commonPath = prop.getProperty("rmmcommonPath");
		RestAssured.useRelaxedHTTPSValidation();
		Response responseCookie = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false)
				.contentType(contentType).param("j_username", userSecurity).param("j_password", passSecurity)
				.param("org", domain).when().post(securityWebUrl);
		token = responseCookie.asString();
		deviceid = prop.getProperty("deviceid");
		processstepname = prop.getProperty("processstepname");
		runstatus = prop.getProperty("runstatus");
		accessioningId = prop.getProperty("accessioningId");
		deviceRunId = prop.getProperty("deviceRunId");
		sampleresultid = prop.getProperty("sampleresultid");
		runresultId = prop.getProperty("runresultId");
		outputContainerId = prop.getProperty("outputContainerId");
		inputContainerId = prop.getProperty("inputContainerId");
		inputContainerPosition = prop.getProperty("inputContainerPosition");
		outputContainerPosition = prop.getProperty("outputContainerPosition");
		assayType = prop.getProperty("assayType");
		processstep = prop.getProperty("processstep");
		RestAssured.baseURI =null;
		RestAssured.baseURI = host + ":" + port + commonPath;
	}

	//@Test(priority = 1)
	public void getRunResultsTest() throws IOException, HMTPException {
		api = "/runresults";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).queryParam("deviceid", deviceid)
				.queryParam("processstepname", processstep);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 2)
	public void getProcessStepResultsTest() throws IOException, HMTPException {
		api = "/runresults/processstepresults";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).queryParam("accessioningId", accessioningId);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 3)
	public void getRunResultsByDeviceRunIdTest() throws IOException, HMTPException {
		api = "/runresults/devicerunid";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).queryParam("deviceRunId", deviceRunId);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 4)
	public void findInprogressStatusByDeviceIDTest() throws IOException, HMTPException {
		api = "/runresults/sampleresultslist";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).queryParam("deviceid", deviceid)
				.queryParam("runstatus", runstatus);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 5)
	public void getSampleandRunResultsTest() throws IOException, HMTPException {
		api = "/runresults/runresultsByDeviceId";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).queryParam("deviceRunId", deviceRunId)
				.queryParam("processStepName", processstep);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 6)
	public void getRunResultsampleresultidTest() throws IOException, HMTPException {
		api = "/runresults/sampleresults/{sampleresultid}";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).pathParam("sampleresultid", sampleresultid);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 7)
	public void getAllDetailsByRunResultsIdTest() throws IOException, HMTPException {
		api = "/runresults/runresultsbyid/{runresultId}";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).pathParam("runresultId", runresultId);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 8)
	public void getRunResultsByProcessStepNameTest() throws IOException, HMTPException {
		api = "/runresults/processstep";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).queryParam("processstep", processstep);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 9)
	public void getSampleResultsTest() throws IOException, HMTPException {
		api = "/runresults/sampleresults";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).queryParam("deviceid", deviceid)
				.queryParam("processStepName", processstep).queryParam("outputContainerId", outputContainerId)
				.queryParam("inputContainerId", inputContainerId)
				.queryParam("inputContainerPosition", inputContainerPosition)
				.queryParam("outputContainerPosition", outputContainerPosition)
				.queryParam("accessioningId", accessioningId);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 10)
	public void listActiveRunsTest() throws IOException, HMTPException {
		api = "/runresults/list";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).queryParam("assayType", assayType)
				.queryParam("status", runstatus).queryParam("processstep", processstep);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 11)
	public void listInCompletedWFSActiveRunsTest() throws IOException, HMTPException {
		api = "/runresults/incompletedwfs";
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects()
				.follow(false).cookie(cookieKey, token).queryParam("assayType", assayType)
				.queryParam("processstep", processstep);

		Response resp = requestSpecification.request(Method.GET, api);
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, expectedPosValue);
	}

	//@Test(priority = 12)
	public void addRunResultsTest() throws IOException {
		api = "/runresults";
		Properties prop = PropertyReaderUtil.getProperty();
		securityWebUrl = prop.getProperty("securityWebUrl");
		contentType = prop.getProperty("contentType");
		userSecurity = prop.getProperty("userSecurity");
		passSecurity = prop.getProperty("passSecurity");
		domain = prop.getProperty("domain");
		cookieKey = prop.getProperty("rmmcookieKeys");
		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForSucessRunResult);
		Response resp = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType("application/json").cookie(cookieKey, token).body(jsonContent).when().post("/runresults");
		Reporter.log("Positive Case :");
		Reporter.log(jsonContent.toString());
		Reporter.log(resp.asString());
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode, 200, "rmm save runresults successfully");
	}
	//@Test(priority = 13)
	public void postSampleResultsTest() throws IOException {
		api = "/runresults/sampleresults";
		Properties prop = PropertyReaderUtil.getProperty();
		securityWebUrl = prop.getProperty("securityWebUrl");
		contentType = prop.getProperty("contentType");
		userSecurity = prop.getProperty("userSecurity");
		passSecurity = prop.getProperty("passSecurity");
		domain = prop.getProperty("domain");
		cookieKey = prop.getProperty("rmmcookieKeys");
		ObjectMapper mapper = new ObjectMapper();
		jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForSucessSampleResult);
		
		JsonNode jsonNode = mapper.readTree(jsonContent);
		SampleResultsDTO sampleResultsDTO = mapper.readValue(mapper.treeAsTokens(jsonNode), SampleResultsDTO.class);
		sampleResultsDTO.setRunResultsId(3703l);  
		Response resp = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType("application/json").cookie(cookieKey, token).body(sampleResultsDTO).when().post("/runresults/sampleresults");
		Reporter.log("Positive Case :");
		Reporter.log(jsonContent.toString());
		Reporter.log(resp.asString()); 
		int statusCode = resp.getStatusCode();
		Assert.assertEquals(statusCode,expectedPosValue);

	}
	//@Test(priority = 14)
	public void updateRunResultsTest() throws IOException {
		api = "/runresults";
		try {
			Properties prop = PropertyReaderUtil.getProperty();
			securityWebUrl = prop.getProperty("securityWebUrl");
			contentType = prop.getProperty("contentType");
			userSecurity = prop.getProperty("userSecurity");
			passSecurity = prop.getProperty("passSecurity");
			domain = prop.getProperty("domain");
			cookieKey = prop.getProperty("rmmcookieKeys");
			jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForSucessRunResultupdate);
			Response resp = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType("application/json").cookie(cookieKey, token)
					.body(jsonContent).when().put("/runresults");
			Reporter.log("Positive Case :");
			Reporter.log(jsonContent.toString());
			Reporter.log(resp.asString());
			int statusCode = resp.getStatusCode();
			Assert.assertEquals(statusCode, 200, "rmm update runresults successfully");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
