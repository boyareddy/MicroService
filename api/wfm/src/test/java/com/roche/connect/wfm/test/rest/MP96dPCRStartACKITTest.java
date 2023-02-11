package com.roche.connect.wfm.test.rest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.mp96.WFMACKMessage;
import com.roche.connect.common.mp96.WFMQueryMessage;
import com.roche.connect.wfm.test.util.JsonFileReaderAsString;
import com.roche.connect.wfm.test.util.PropertyReaderUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class MP96dPCRStartACKITTest {

    
    
    String jsonContent = "";
    public static final String jsonForSucessWfmdpcrCreation = "src/test/java/resource/MP96dPCRACKPositive.json";
    public static final String jsonForFailureWfmdpcrCreation = "src/test/java/resource/MP96dPCRACKNegative.json";
    ///wfm/src/test/java/resource/MP96dPCRACKPositive.json
    private String host = "";
    private String port = "";
    private String commonPath = "";
    private String token = "";
    private String securityWebUrl ="";
    private String contentType ="";
    private String userSecurity = "";
    private String passSecurity = "";
    private String domain = "";
    private String cookieKey = "";
    
    private static Logger logger = Logger.getLogger(ForteITTest.class.getName());
    WFMACKMessage wfmQuermessage=null;
    @BeforeTest public void beforeTest() throws JsonParseException, JsonMappingException, IOException {
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
        
        Response responseCookie = RestAssured.given().relaxedHTTPSValidation().contentType(contentType).param("j_username", userSecurity).param("j_password", passSecurity).param("org", domain).when().post(securityWebUrl);
        token = responseCookie.asString();
        RestAssured.baseURI = host + ":" + port + commonPath;
        
    }
    
    
    @Test(priority = 1)
    public void PositiveMp96QueryTest() throws IOException {

        jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForSucessWfmdpcrCreation);
        FileReader fr = new FileReader(new File(jsonForSucessWfmdpcrCreation));
        ObjectMapper objectMapper = new ObjectMapper();
        wfmQuermessage = objectMapper.readValue(fr, WFMACKMessage.class);
         int statusCode=200;
         
         RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().cookie(cookieKey,token);
            try {
                requestSpecification.body(wfmQuermessage).contentType("application/json");
                Response resp = requestSpecification.post("/NIPTdPCR/updatewfmprocess/ACKMP96");
        Reporter.log("Positive Case :");
        Reporter.log(jsonContent.toString());
        Reporter.log(resp.asString());
        statusCode = resp.getStatusCode();
    }
    catch (Exception ex) {
        logger.error(ex);
    }
       // Assert.assertEquals(statusCode, 200, "wfm has recieved qpb  successfully creation");

    }

    @Test(priority = 2)
    public void NegativeMp96QueryTest() throws IOException {
        FileReader fr = new FileReader(new File(jsonForFailureWfmdpcrCreation));
        ObjectMapper objectMapper = new ObjectMapper();
        wfmQuermessage = objectMapper.readValue(fr, WFMACKMessage.class);
        jsonContent = JsonFileReaderAsString.getJsonfromFile(jsonForFailureWfmdpcrCreation);
        RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().cookie(cookieKey,token);
         int statusCode=400;
            try {
                requestSpecification.body(wfmQuermessage).contentType("application/json");
                Response resp = requestSpecification.post("/NIPTdPCR/updatewfmprocess/ACKMP96");
        statusCode = resp.getStatusCode();
    }
    catch (Exception ex) {
        logger.error(ex);
    }
       // Assert.assertNotEquals(statusCode, 200, "Error occured while recieved qpb creation");

    }

    
}
