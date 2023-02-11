/*******************************************************************************
 * MessageRestApiITTest.java                  
 *  Version:  1.0
 * 
 *  Authors:  Alexander
 * 
 * *********************
 *  Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  All Rights Reserved.
 * 
 *  NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 *  The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *                
 * *********************
 *  ChangeLog:
 * 
 *  Alexanders@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.imm.rest;

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.EnumMessageType;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.imm.utils.PropertyReaderUtil;
import com.roche.connect.imm.utils.SecurityToken;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * this is for imm testcases taking the data from adaptor and operate the
 * corresponding operation
 * 
 * @author ganaavarapu.a
 */

public class MessageRestApiITTest {
	QueryMessage queryMessage = null;
	ResponseMessage responseMessage = null;
	RequestSpecification requestSpecification = null;
	AdaptorRequestMessage adaptorRequestMessage = null;
	AcknowledgementMessage acknowledgementMessage = null;
	AdaptorResponseMessage adaptorResponseMessage = null;
	SpecimenStatusUpdateMessage specimenStatusUpdateMessage = null;

	private String token = "";
	private String cookieKey = "brownstoneauthcookie";

	@BeforeTest
	public void stateUp() {
		token = SecurityToken.getToken();

		Properties prop = PropertyReaderUtil.getProperty();
		String immHostUrl = prop.getProperty("immHostUrl");

		RestAssured.baseURI = immHostUrl + prop.getProperty("messageUrl");
		requestSpecification = RestAssured.given().relaxedHTTPSValidation().redirects().follow(false).header("Content-Type", "application/json");

		queryMessage = getQueryMessage();
		responseMessage = getResponseMessage();
		adaptorRequestMessage = getAdaptorRequestMessage();
		adaptorResponseMessage = getAdaptorResponseMessage();
		acknowledgementMessage = getAcknowledgementMessage();
		specimenStatusUpdateMessage = getSpecimenStatusUpdateMessage();
	}

	/**
	 * This method will take mp24 qbp msg from adapter
	 */

	@Test(priority = 1)
	public void positiveConsumeAdaptorRequestTest() {
		Response response = requestSpecification.body(adaptorRequestMessage).cookie(cookieKey, token).post();
		Assert.assertEquals(response.getStatusCode(), 400);
	}

	/**
	 * This method will take mp24 qbp msg from adapter
	 */
	@Test(priority = 2)
	public void positiveConsumeAdaptorRequestTest1() {
		Response response = requestSpecification.body(adaptorRequestMessage.getAccessioningId())
				.cookie(cookieKey, token).post();
		Assert.assertEquals(response.getStatusCode(), 400);
	}

	/**
	 * This method will take mp24 qbp msg from adapter
	 */
	@Test(priority = 3)
	public void negativeConsumeAdaptorRequestTest() {
		Response response = requestSpecification.body(adaptorRequestMessage).post();
		Assert.assertNotEquals(response.getStatusCode(), 200);
	}

	/**
	 * This method will take mp24 qbp msg from adapter
	 */
	@Test(priority = 4)
	public void negativeConsumeAdaptorRequestTest1() {
		Response response = requestSpecification.body("").post();
		int code = response.getStatusCode();
		Assert.assertNotEquals(code, 404);
	}

	public AdaptorRequestMessage getAdaptorRequestMessage() {
		AdaptorRequestMessage adaptorRequestMessage = new AdaptorRequestMessage();
		adaptorRequestMessage.setDeviceSerialNumber("12345");
		adaptorRequestMessage.setSendingApplicationName("MP24");
		adaptorRequestMessage.setReceivingApplication("CONNECT");
		adaptorRequestMessage.setDateTimeMessageGenerated("20151020162814");
		adaptorRequestMessage.setMessageType("NA_Extraction");
		adaptorRequestMessage.setMessageControlId("54b325e8-8efd-4b37-8a29-cc62512da532");
		adaptorRequestMessage.setAccessioningId("$S12345");
		return adaptorRequestMessage;
	}

	public QueryMessage getQueryMessage() {
		QueryMessage queryMessage = new QueryMessage();
		queryMessage.setDeviceSerialNumber("12345");
		queryMessage.setSendingApplicationName("MP24");
		queryMessage.setReceivingApplication("CONNECT");
		queryMessage.setDateTimeMessageGenerated("20151020162814");
		queryMessage.setMessageType(EnumMessageType.QueryMessage);
		queryMessage.setContainerId("54b325e8-8efd-4b37-8a29-cc62512da532");
		return queryMessage;
	}

	public ResponseMessage getResponseMessage() {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setDeviceSerialNumber("12345");
		responseMessage.setSendingApplicationName("MP24");
		responseMessage.setReceivingApplication("CONNECT");
		responseMessage.setDateTimeMessageGenerated("20151020162814");
		responseMessage.setMessageType(EnumMessageType.QueryMessage);
		responseMessage.setContainerId("54b325e8-8efd-4b37-8a29-cc62512da532");
		responseMessage.setStatus("inProcess");
		responseMessage.setErrors(null);
		responseMessage.setSampleType("blood");
		responseMessage.setSpecimenDescription("normal");
		return responseMessage;
	}

	public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessage() {
		SpecimenStatusUpdateMessage specimenStatusUpdateMessage = new SpecimenStatusUpdateMessage();
		specimenStatusUpdateMessage.setDeviceSerialNumber("12345");
		specimenStatusUpdateMessage.setSendingApplicationName("MP24");
		specimenStatusUpdateMessage.setReceivingApplication("CONNECT");
		specimenStatusUpdateMessage.setDateTimeMessageGenerated("20151020162814");
		specimenStatusUpdateMessage.setMessageType(EnumMessageType.QueryMessage);
		specimenStatusUpdateMessage.setContainerId("54b325e8-8efd-4b37-8a29-cc62512da532");
		specimenStatusUpdateMessage.setStatusUpdate(null);
		return specimenStatusUpdateMessage;
	}

	public AcknowledgementMessage getAcknowledgementMessage() {
		AcknowledgementMessage acknowledgementMessage = new AcknowledgementMessage();
		acknowledgementMessage.setDeviceSerialNumber("12345");
		acknowledgementMessage.setSendingApplicationName("MP24");
		acknowledgementMessage.setReceivingApplication("CONNECT");
		acknowledgementMessage.setDateTimeMessageGenerated("20151020162814");
		acknowledgementMessage.setMessageType(EnumMessageType.QueryMessage);
		acknowledgementMessage.setContainerId("54b325e8-8efd-4b37-8a29-cc62512da532");
		acknowledgementMessage.setStatus("inProcess");
		return acknowledgementMessage;
	}

	public AdaptorResponseMessage getAdaptorResponseMessage() {
		AdaptorResponseMessage adaptorResponseMessage = new AdaptorResponseMessage();
		com.roche.connect.common.mp24.message.Response responseMessage = new com.roche.connect.common.mp24.message.Response();
		responseMessage.setDeviceSerialNumber("12345");
		responseMessage.setSendingApplicationName("MP24");
		responseMessage.setReceivingApplication("CONNECT");
		responseMessage.setDateTimeMessageGenerated("20151020162814");
		responseMessage.setMessageType("NA_Extraction");
		responseMessage.setMessageControlId("54b325e8-8efd-4b37-8a29-cc62512da532");
		responseMessage.setAccessioningId("$S12345");
		adaptorResponseMessage.setStatus("inprogress");
		adaptorResponseMessage.setError(null);
		adaptorResponseMessage.setResponse(responseMessage);
		return adaptorResponseMessage;
	}
}
