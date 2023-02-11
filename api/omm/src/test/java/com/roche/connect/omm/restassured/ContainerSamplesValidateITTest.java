package com.roche.connect.omm.restassured;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.omm.test.util.PropertyReaderUtil;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 *@author imtiyazm
 * This class is for test CSV file validation API
 */
public class ContainerSamplesValidateITTest {
	

	private String api = "/containersamples/validate";
	private String getApi = "/containersamples";
	private String host = "";
	private String port = "";
	private String commonPath = "";
	private int expectedValue = 200;
	private int negExpectedValue = 400;
	
	private String token = "";
    private String securityWebUrl ="";
    private String contentType ="";
    private String contentTypes ="";
    private String userSecurity = "";
    private String passSecurity = "";
    private String domain = "";
    private String cookieKey = "";
    String csvFilePath = "src/test/java/resource/samplecsv.csv";
    InputStream stream = null;
    File csvFile = null;
    private String DPCRAssay = "";
    private String HTPAssay = "";
	
	
	@BeforeTest
	public void beforeTest(){
		Properties prop = PropertyReaderUtil.getProperty();
        securityWebUrl = prop.getProperty("securityWebUrl");
   	    contentType = prop.getProperty("contentType");
   	    contentTypes = prop.getProperty("contentTypes");
   	    userSecurity = prop.getProperty("userSecurity");
   	    passSecurity = prop.getProperty("passSecurity");
   	    domain = prop.getProperty("domain");
   	    cookieKey = prop.getProperty("cookieKeys");
		host = prop.getProperty("host");
		port = prop.getProperty("port");
		DPCRAssay = prop.getProperty("DPCRAssay");
		HTPAssay = prop.getProperty("HTPAssay");
		commonPath = prop.getProperty("commonPath");
		Response responseCookie = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).contentType(contentType).param("j_username", userSecurity).param("j_password", passSecurity).param("org", domain).when().post(securityWebUrl);
        token = responseCookie.asString();
		RestAssured.baseURI = host+":"+port+commonPath;
		
	}

	
	/*@Test(priority = 1)
	public void getPositiveContainerSampleValidationTest() throws   IOException {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		csvFile = new File(csvFilePath);
		stream = new FileInputStream(csvFile);
		requestSpecification.body(stream).contentType(contentTypes);
		Response response = requestSpecification.post(api);
		//Assert.assertEquals(response.getStatusCode(), expectedValue);
		Assert.assertEquals(response.getStatusCode(), negExpectedValue);
	}
	
	
	@Test(priority = 2)
	public void getNegativeContainerSampleValidationTest() throws IOException {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		csvFile = new File(csvFilePath);
		stream = new FileInputStream(csvFile);
		requestSpecification.body(stream).contentType(contentTypes);
		Response response = requestSpecification.post(api);
		//Assert.assertNotEquals(response.getStatusCode(), negExpectedValue);
		Assert.assertNotEquals(response.getStatusCode(), expectedValue);
		}
	*/
	@Test(priority=3)
	public void getContainerSamplesPositiveTest() {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		Response response = requestSpecification.get(getApi);
		ContainerSamplesDTO[] containerSamplesDTO = response.jsonPath().getObject("$", ContainerSamplesDTO[].class);
		//Assert.assertEquals(containerSamplesDTO[0].getAssayType(), DPCRAssay);
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	/*@Test(priority=4)
	public void getContainerSamplesNegTest() {
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).cookie(cookieKey,token);
		Response response = requestSpecification.get(getApi);
		ContainerSamplesDTO[] containerSamplesDTO = response.jsonPath().getObject("$", ContainerSamplesDTO[].class);
		Assert.assertNotEquals(containerSamplesDTO[0].getAssayType(), HTPAssay);
	}*/

	@AfterTest
	public void afterTest() {
		api = "";
		getApi = "";
		host = "";
		port = "";
		commonPath = "";
		expectedValue = 0;
		negExpectedValue = 0;
		
		token = "";
	    securityWebUrl ="";
	    contentType ="";
	    contentTypes ="";
	    userSecurity = "";
	    passSecurity = "";
	    domain = "";
	    cookieKey = "";
	    csvFilePath = "";
	    stream = null;
	    csvFile = null;
	    DPCRAssay = "";
	    HTPAssay = "";
	}

}
