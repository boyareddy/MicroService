/**
 * 
 */
package com.roche.connect.adm.rest.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * @author hcl User Test Class
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserManagementTestCases {

	@Autowired
	private PAS pasPojo;

	/**
	 * List users test case
	 * 
	 * @throws IOException
	 */
	//@Test
	public void getListOfUsers() {
		PrintStream printStream = null;

		try {
			printStream = new PrintStream(new File("GetUsersListTestReport.txt"));
			RestAssured.baseURI = pasPojo.getSecurity_url();
			RequestSpecification request = RestAssured.given();
			request.param("j_username", "admin");
			request.param("j_password", "pas123");
			request.param("org", "hcl.com");
			request.contentType("application/x-www-form-urlencoded");
			Response response = request.post("/json/security/login");
			printStream.println("List of all users - Story US_USR_001");
			Cookie someCookie = new Cookie.Builder("brownstoneauthcookie", response.body().asString()).setSecured(true)
					.build();
			Response responseReturned = RestAssured.given().cookie(someCookie).contentType("application/json")
					.accept("application/json")
					.filter(new RequestLoggingFilter(io.restassured.filter.log.LogDetail.ALL, printStream)).expect()
					.log().all().when().get(pasPojo.getAdmin_api_url() + "/json/users").thenReturn();
			printStream.println("Output:");

			printStream.println("Status code returned:\t" + responseReturned.getStatusCode());
			printStream.println("Response body:");
			printStream.println(responseReturned.asString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			printStream.close();
		}
	}

	/**
	 * Create User test case
	 * 
	 * @throws JSONException
	 */

	//@Test
	public void createUser() throws JSONException {

		PrintStream printStream = null;

		try {
			printStream = new PrintStream(new File("CreateUserTestReport.txt"));
			RestAssured.baseURI = pasPojo.getSecurity_url();
			RequestSpecification request = RestAssured.given();
			request.param("j_username", "admin");
			request.param("j_password", "pas123");
			request.param("org", "hcl.com");
			request.contentType("application/x-www-form-urlencoded");
			Response response = request.post("/json/security/login");
			printStream.println("Create user - Story US_USR_002");

			JSONObject jsonObject = new JSONObject();
			JSONArray array = new JSONArray();
			JSONObject jsonObjectChild = new JSONObject();
			jsonObjectChild.put("preferenceKey", "LOCALE");
			jsonObjectChild.put("value", "en_US");
			array.add(jsonObjectChild);
			jsonObject.put("loginName", "JD3");
			jsonObject.put("email", "JD3@roche.comLLLLLLLLLLLLLLLLL");
			jsonObject.put("firstName", "JLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
			jsonObject.put("lastName", "DLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
			jsonObject.put("userPreferences", array);

			Cookie someCookie = new Cookie.Builder("brownstoneauthcookie", response.body().asString()).setSecured(true)
					.build();
			Response responseReturned = RestAssured.given().cookie(someCookie).body(jsonObject.toJSONString())
					.contentType("application/json").accept("application/json")
					.filter(new RequestLoggingFilter(io.restassured.filter.log.LogDetail.ALL, printStream)).expect()
					.log().all().when().post(pasPojo.getAdmin_api_url() + "/json/users?ownerId=1&roles=Admin")
					.thenReturn();

			printStream.println("Output:");
			
			printStream.println("Status code returned:\t" + responseReturned.getStatusCode());
			if(responseReturned.getStatusCode() != 201) {
				printStream.println("Error - Have PAS dependency as per UI spec");
			}else {
			printStream.println("Response body:");
			printStream.println(responseReturned.asString());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			printStream.close();
		}

	}

	/**
	 * Update User to Archive Test Case
	 */
	//@Test
	public void updateUserToArchive() {

		PrintStream printStream = null;

		try {
			printStream = new PrintStream(new File("UpdateUserToArchiveTestReport.txt"));
			RestAssured.baseURI = pasPojo.getSecurity_url();

			RequestSpecification request = RestAssured.given();
			request.param("j_username", "admin");
			request.param("j_password", "pas123");
			request.param("org", "hcl.com");
			request.contentType("application/x-www-form-urlencoded");
			Response response = request.post("/json/security/login");
			printStream.println("Update user to Archive - Story US_USR_008");

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", 7);
			jsonObject.put("loginName", "arun3");
			jsonObject.put("retired", true);
			jsonObject.put("editedBy", "AdminJD");

			Cookie someCookie = new Cookie.Builder("brownstoneauthcookie", response.body().asString()).setSecured(true)
					.build();
			Response responseReturned = RestAssured.given().cookie(someCookie).body(jsonObject.toJSONString())
					.contentType("application/json").accept("application/json")
					.filter(new RequestLoggingFilter(io.restassured.filter.log.LogDetail.ALL, printStream)).expect()
					.log().all().when().put(pasPojo.getAdmin_api_url() + "/json/users?ownerId=1").thenReturn();

			printStream.println("Output:");

			printStream.println("Status code returned:\t" + responseReturned.getStatusCode());
			printStream.println("Response body:");
			printStream.println(responseReturned.asString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			printStream.close();
		}

	}

	/**
	 * Get User Test case
	 */
	@Test
	public void getUser() {

		PrintStream printStream = null;

		try {
			printStream = new PrintStream(new File("GetUserTestReport.txt"));
			RestAssured.baseURI = pasPojo.getSecurity_url();

			RequestSpecification request = RestAssured.given();
			request.param("j_username", "admin");
			request.param("j_password", "pas123");
			request.param("org", "hcl.com");
			request.contentType("application/x-www-form-urlencoded");
			Response response = request.post("/json/security/login");
			printStream.println("Get User - Story US_USR_004");

			long id = 7;
			Cookie someCookie = new Cookie.Builder("brownstoneauthcookie", response.body().asString()).setSecured(true)
					.build();
			Response responseReturned = RestAssured.given().cookie(someCookie).contentType("application/json")
					.accept("application/json")
					.filter(new RequestLoggingFilter(io.restassured.filter.log.LogDetail.ALL, printStream)).expect()
					.log().all().when().get(pasPojo.getAdmin_api_url() + "/json/entities/entityId/PASUSER/id/" + id)
					.thenReturn();

			printStream.println("Output:");

			printStream.println("Status code returned:\t" + responseReturned.getStatusCode());
			printStream.println("Response body:");
			printStream.println(responseReturned.asString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			printStream.close();
		}
	}

	/**
	 * Update User to Active Test Case
	 */

	//@Test
	public void updateUserToActive() {

		PrintStream printStream = null;
		try {
			printStream = new PrintStream(new File("UpdateUserToActiveTestReport.txt"));
			RestAssured.baseURI = pasPojo.getSecurity_url();

			RequestSpecification request = RestAssured.given();
			request.param("j_username", "admin");
			request.param("j_password", "pas123");
			request.param("org", "hcl.com");
			request.contentType("application/x-www-form-urlencoded");
			Response response = request.post("/json/security/login");
			printStream.println("Update User to Active - Story US_USR_010");

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", 7);
			jsonObject.put("loginName", "arun3");
			jsonObject.put("retired", false);
			jsonObject.put("editedBy", "AdminJD");

			Cookie someCookie = new Cookie.Builder("brownstoneauthcookie", response.body().asString()).setSecured(true)
					.build();
			Response responseReturned = RestAssured.given().cookie(someCookie).body(jsonObject.toJSONString())
					.contentType("application/json").accept("application/json")
					.filter(new RequestLoggingFilter(io.restassured.filter.log.LogDetail.ALL, printStream)).expect()
					.log().all().when().put(pasPojo.getAdmin_api_url() + "/json/users?ownerId=1").thenReturn();

			printStream.println("Output:");

			printStream.println("Status code returned:\t" + responseReturned.getStatusCode());
			printStream.println("Response body:");
			printStream.println(responseReturned.asString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			printStream.close();
		}

	}

	/**
	 * Update User Profile
	 */
	//@Test
	public void updateUserProfile() {
		PrintStream printStream = null;

		try {
			printStream = new PrintStream(new File("UpdateUserProfileTestReport.txt"));
			RestAssured.baseURI = pasPojo.getSecurity_url();

			RequestSpecification request = RestAssured.given();
			request.param("j_username", "admin");
			request.param("j_password", "pas123");
			request.param("org", "hcl.com");
			request.contentType("application/x-www-form-urlencoded");
			Response response = request.post("/json/security/login");
			printStream.println("Update User Profile- Story US_USR_007");

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", 7);
			jsonObject.put("loginName", "arun3");
			jsonObject.put("email", "JD3@roche.comLLLLLLLLLLLLLLLLL");
			JSONArray array = new JSONArray();
			JSONObject jsonObjectChild = new JSONObject();
			jsonObjectChild.put("id", 7);
			jsonObjectChild.put("address1", "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
			jsonObjectChild.put("address2", "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
			jsonObjectChild.put("homePhone", "123477777777777");
			jsonObjectChild.put("officePhone", "123477V77777777");
			jsonObjectChild.put("dayPhone", "1234777777V7777");
			jsonObjectChild.put("city", "US");
			jsonObjectChild.put("state", "COn");
			jsonObjectChild.put("country", "USA");
			jsonObjectChild.put("zip", "223434");
			jsonObjectChild.put("mobile", "12347777V77777");
			jsonObjectChild.put("editedBy", "AdminJD");
			array.add(jsonObjectChild);
			jsonObject.put("contact", array);
			
			Cookie someCookie = new Cookie.Builder("brownstoneauthcookie", response.body().asString()).setSecured(true)
					.build();
			Response responseReturned = RestAssured.given().cookie(someCookie).body(jsonObject.toJSONString())
					.contentType("application/json").accept("application/json")
					.filter(new RequestLoggingFilter(io.restassured.filter.log.LogDetail.ALL, printStream)).expect()
					.log().all().when().put(pasPojo.getAdmin_api_url() + "/json/users?ownerId=1").thenReturn();

			printStream.println("Output:");

			printStream.println("Status code returned:\t" + responseReturned.getStatusCode());
			try {
				Long.parseLong((String) jsonObjectChild.get("mobile"));
				if(responseReturned.getStatusCode() != 200) {
					printStream.println("Error - Have PAS dependency as per UI spec");
				}else {
				printStream.println("Response body:");
				printStream.println(responseReturned.asString());
				}
				}catch (NumberFormatException e) {
					printStream.println("Error - Have PAS dependency as per UI spec (Mobile# is not in correct format but PAS allows)");
				}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			printStream.close();
		}

	}

	/**
	 * Update User
	 */
	//@Test
	public void updateUser() {

		PrintStream printStream = null;

		try {
			printStream = new PrintStream(new File("UpdateUserTestReport.txt"));
			RestAssured.baseURI = pasPojo.getSecurity_url();

			RequestSpecification request = RestAssured.given();
			request.param("j_username", "admin");
			request.param("j_password", "pas123");
			request.param("org", "hcl.com");
			request.contentType("application/x-www-form-urlencoded");
			Response response = request.post("/json/security/login");
			printStream.println("Update User - Story US_USR_004");

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", 7);
			jsonObject.put("loginName", "arun3");
			jsonObject.put("email", "JD56com");
			jsonObject.put("firstName", "JLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
			jsonObject.put("lastName", "!@#$LLLLLLLLLLLLLLLLLLLLLLLLLL");

			Cookie someCookie = new Cookie.Builder("brownstoneauthcookie", response.body().asString()).setSecured(true)
					.build();
			Response responseReturned = RestAssured.given().cookie(someCookie).body(jsonObject.toJSONString())
					.contentType("application/json").accept("application/json")
					.filter(new RequestLoggingFilter(io.restassured.filter.log.LogDetail.ALL, printStream)).expect()
					.log().all().when().put(pasPojo.getAdmin_api_url() + "/json/users?ownerId=1").thenReturn();

			printStream.println("Output:");
			
			printStream.println("Status code returned:\t" + responseReturned.getStatusCode());
			if(responseReturned.getStatusCode() != 200) {
				printStream.println("Error - Have PAS dependency as per UI spec");
			}else {
			printStream.println("Response body:");
			printStream.println(responseReturned.asString());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			printStream.close();
		}

	}

	/**
	 * Password reset for user
	 */
	//@Test
	public void resetPassword() {

		PrintStream printStream = null;
		try {
			printStream = new PrintStream(new File("ResetPasswordTestReport.txt"));

			RestAssured.baseURI = pasPojo.getSecurity_url();

			RequestSpecification request = RestAssured.given();
			request.param("j_username", "admin");
			request.param("j_password", "pas123");
			request.param("org", "hcl.com");
			request.contentType("application/x-www-form-urlencoded");
			Response response = request.post("/json/security/login");
			printStream.println("Reset password - Story US_USR_006");

			JSONObject jsonObject = new JSONObject();
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("domainName", "hcl.com");
			jsonObject.put("id", 6);
			jsonObject.put("loginName", "arun3");
			jsonObject.put("company", jsonObject2);
			jsonObject.put("password", "password@12et4t4t3");

			System.out.println(jsonObject.toJSONString());

			Cookie someCookie = new Cookie.Builder("brownstoneauthcookie", response.body().asString()).setSecured(true)
					.build();
			Response responseReturned = RestAssured.given().cookie(someCookie).body(jsonObject.toJSONString())
					.contentType("application/json").accept("application/json")
					.filter(new RequestLoggingFilter(io.restassured.filter.log.LogDetail.ALL, printStream)).expect()
					.log().all().when().put(pasPojo.getSecurity_url() + "/json/users/password").thenReturn();

			printStream.println("Output:");

			printStream.println("Status code returned:\t" + responseReturned.getStatusCode());
			printStream.println("Response body:");
			printStream.println(responseReturned.asString());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			printStream.close();
		}

	}
}

/**
 * POJO class of application YAML
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("pas")
class PAS {
	private String security_url;
	private String admin_api_url;

	public String getSecurity_url() {
		return security_url;
	}

	public void setSecurity_url(String security_url) {
		this.security_url = security_url;
	}

	public String getAdmin_api_url() {
		return admin_api_url;
	}

	public void setAdmin_api_url(String application_ui_url) {
		this.admin_api_url = application_ui_url;
	}

}
