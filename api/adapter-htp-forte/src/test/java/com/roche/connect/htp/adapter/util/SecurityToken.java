package com.roche.connect.htp.adapter.util;

import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class SecurityToken {
	

	private SecurityToken() {
		//Empty Constructor
	}

	/** The juser. */
	private static String juser;

	/** The jpwd. */
	private static String jpwd;

	/** The content type. */
	private static String contentType;

	/** The organization. */
	private static String organization;

	/** The pas security URL. */
	private static String pasSecurityURL;

	public static String getToken() {
		Properties prop = PropertyReaderUtil.getProperty();

		juser = prop.getProperty("userSecurity");
		jpwd = prop.getProperty("passSecurity");
		contentType = prop.getProperty("contentType");
		organization = prop.getProperty("domain");
		pasSecurityURL = prop.getProperty("securityWebUrl");

		Response response1 = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false)
				.contentType(contentType).param("j_username", juser).param("j_password", jpwd)
				.param("org", organization).when().post(pasSecurityURL);
		return response1.asString();

	}

}
