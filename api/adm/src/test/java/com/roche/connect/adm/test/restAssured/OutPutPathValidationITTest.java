package com.roche.connect.adm.test.restAssured;

public class OutPutPathValidationITTest {/*
    
    

    private String host = "";
    private String port1 = "";
    private String commonPath1 = "";
    private String token = "";
    private String securityWebUrl ="";
    private String contentType ="";
    private String userSecurity = "";
    private String passSecurity = "";
    private String domain = "";
    private String cookieKey = "";
    
    private static Logger logger = Logger.getLogger(OutPutPathValidationITTest.class.getName());
    WFMoULMessage wfmQuermessage=null;
    @BeforeTest public void beforeTest() throws JsonParseException, JsonMappingException, IOException {
        Properties prop = PropertyReaderUtil.getProperty();
        securityWebUrl = prop.getProperty("securityWebUrl");
        contentType = prop.getProperty("contentType");
        userSecurity = prop.getProperty("userSecurity");
        passSecurity = prop.getProperty("passSecurity");
        domain = prop.getProperty("domain");
        cookieKey = prop.getProperty("cookieKeys");
        host = prop.getProperty("host");
        port1 = prop.getProperty("port");
        commonPath1 = prop.getProperty("commonPath");
        
        Response responseCookie = RestAssured.given().relaxedHTTPSValidation().contentType(contentType).param("j_username", userSecurity).param("j_password", passSecurity).param("org", domain).when().post(securityWebUrl);
        token = responseCookie.asString();
        RestAssured.baseURI = host + ":" + port1 + commonPath1;
        
    }
    
    
    @Test(priority = 1)
    public void PositiveOutputPathTest() throws IOException {
        
         int statusCode=200;
         
         //RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().cookie(cookieKey,token).queryParam("outputPath", "C://Users").when().get("/validatePath");
            try {
            	 RestAssured.baseURI = "http://localhost:97/adm/json/rest/api/v1";
                Response resp = RestAssured.given().relaxedHTTPSValidation().cookie(cookieKey,token).contentType("application/json").queryParam("outputPath", "C://Users").when().get("/validatePath");
        Reporter.log("Positive Case :");
        Reporter.log(resp.asString());
        statusCode = resp.getStatusCode();
    }
    catch (Exception ex) {
        logger.error(ex);
    }
     //   Assert.assertEquals(statusCode, 200, "wfm has recieved WFMoULMessage  successfully creation");

    }

    @Test(priority = 2)
    public void NegativeOutputPathTest() throws IOException {
        
         int statusCode=503;
         
            try {
            	Response resp = RestAssured.given().relaxedHTTPSValidation().cookie(cookieKey,token).contentType("application/json").queryParam("outputPath", "C://Use").when().get("/validatePath");
        Reporter.log("Positive Case :");
        Reporter.log(resp.asString());
        statusCode = resp.getStatusCode();
    }
    catch (Exception ex) {
        logger.error(ex);
    }
     //   Assert.assertEquals(statusCode, 200, "wfm has recieved WFMoULMessage  successfully creation");

    }
    @Test(priority = 3)
    public void NullOutputPathTest() throws IOException {
        
         int statusCode=503;
         
            try {
            	Response resp = RestAssured.given().relaxedHTTPSValidation().cookie(cookieKey,token).contentType("application/json").queryParam("outputPath", "").when().get("/validatePath");
        Reporter.log("Positive Case :");
        Reporter.log(resp.asString());
        statusCode = resp.getStatusCode();
    }
    catch (Exception ex) {
        logger.error(ex);
    }
     //   Assert.assertEquals(statusCode, 200, "wfm has recieved WFMoULMessage  successfully creation");

    }
   
    
*/}
