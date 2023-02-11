/*******************************************************************************
 * File Name: CreateOrderObjectTest.java            
 * Version:  1.0
 * 
 * Authors: Ankit Singh
 * 
 * =========================================
 * 
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 * 
 * =========================================
 * 
 * ChangeLog:
 ******************************************************************************/
package com.roche.connect.omm.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.List;

import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.ProcessStepValuesDTO;
import com.roche.connect.common.order.dto.WorkflowDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.model.PatientSamples;
import com.roche.connect.omm.model.TestOptions;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;

/**
 * @author susmita.s This class is used to test Order object creation
 */

public class CreateOrdPatientAssayPatientObjTest {
	@InjectMocks
	OrderService orderService = new OrderServiceImpl();

	// Order inputs
	Order order = null;
	String expectedAssayType = null;
	String actualAssayType = null;
	OrderParentDTO orderParent = null;

	// PatientAssay inputs
	PatientAssay patientAssay = null;
	Integer actualMaternalAge = 0;
	Integer expectedMaternalAge = 0;
	String actualEggDonor = null;

	// Patient inputs
	Patient patient = null;
	String actualName = null;
	String expectedName = null;
	String actualFlag = null;

	// PatientSample inputs
	String expectedSampleType = null;
	String actualSampleType = null;
	List<PatientSamples> patientSampleList = null;

	// TestOptions
	String expectedTestId = null;
	String actualTestId = null;
	List<TestOptions> testOptionList = null;

	// CreateWorkflowObjectTest
	ProcessStepValuesDTO processStepValuesDTO = new ProcessStepValuesDTO();
	OrderDTO orderDTO = new OrderDTO();
	WorkflowDTO workflowDto = new WorkflowDTO();
	String expectedWorkflowStatus = null;

	/**
	 * Reading JSON inputs from the order.json file and parsing to JSON object.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeTest
	public void setUp() throws Exception {
		FileReader fr = new FileReader(new File("src/test/java/Resource/OrderCrudUpdateDetails.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		orderParent = objectMapper.readValue(fr, OrderParentDTO.class);
		ReflectionTestUtils.setField(orderService, "phiEncrypt", false);

		order = orderService.createOrderObject(orderParent);
		patient = orderService.createPatientObject(orderParent);
		patientAssay = orderService.createPatientAssayObject(orderParent);
		patientSampleList = orderService.createPatientSamplesObject(orderParent);
		testOptionList = orderService.createTestOptionsObject(orderParent);
		expectedAssayType = "NIPTDPCR";
		expectedMaternalAge = 5;
		expectedName = "Kumar";
		expectedSampleType = "Plasma";
		expectedTestId = "harmony";
		expectedWorkflowStatus = "In progress";

		expectedWorkflowStatus = "In progress";
		orderDTO = orderParent.getOrder();
		processStepValuesDTO.setComments("comment");
		processStepValuesDTO.setProcessStepName("NA Extraction");
		processStepValuesDTO.setRunStatus("In progress");
		processStepValuesDTO.setRunFlag("Y");

	}

	/**
	 * Order object creation with json data for positive scenario.
	 *
	 * @throws ParseException
	 *             the parse exception
	 */
	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void createOrderObjPosTest() throws ParseException {
		actualAssayType = order.getAssayType();
		assertEquals(actualAssayType, expectedAssayType);
	}

	/**
	 * Order object creation with json data for negative scenario.
	 *
	 * @throws ParseException
	 *             the parse exception
	 */
	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void createOrderObjNegTest() throws ParseException {
		actualAssayType = order.getAssayType();
		assertNotEquals(actualAssayType, "Oncology");
		;
	}

	@Test
	public void createPatientAssayObjPosTest() throws ParseException {
		actualMaternalAge = patientAssay.getMaternalAge();
		assertEquals(actualMaternalAge, expectedMaternalAge);
	}

	@Test
	public void createPatientAssayObjNegTest() throws ParseException {
		actualEggDonor = patientAssay.getEggDonor();
		assertNotEquals(actualMaternalAge, "None");
	}

	@Test
	public void createPatientObjPosTest() throws ParseException {
		actualName = patient.getPatientFirstName();
		assertEquals(actualName, expectedName);
	}

	@Test
	public void createPatientObjNegTest() throws ParseException {
		actualFlag = patient.getActiveFlag();
		assertNotEquals(actualFlag, "Female");
	}

	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void createPatientSampleObjPosTest() throws ParseException {
		actualSampleType = patientSampleList.get(0).getSampleType();
		assertEquals(actualSampleType, expectedSampleType);
	}

	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void createPatientSampleObjNegTest() throws ParseException {
		actualSampleType = patientSampleList.get(0).getSampleType();
		assertNotEquals(actualSampleType, "Blood");
		;
	}

	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void createTestOptionsObjPosTest() throws ParseException {
		actualTestId = testOptionList.get(0).getTestId();
		assertEquals(actualTestId, expectedTestId);
	}

	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void createTestOptionsObjNegTest() throws ParseException {
		actualTestId = testOptionList.get(0).getTestId();
		assertNotEquals(actualTestId, "fetalSex");
		;
	}

	@Test
	public void positiveUpdatePatientTest() throws ParseException {
		workflowDto = orderService.createWrokflowOrderObject(processStepValuesDTO, orderDTO);
		assertEquals(workflowDto.getWorkflowStatus(), expectedWorkflowStatus);
	}

	@Test
	public void negativiUpdatePatientTest() throws ParseException {
		workflowDto = orderService.createWrokflowOrderObject(processStepValuesDTO, orderDTO);
		assertNotEquals(workflowDto.getWorkflowStatus(), "xyz");
	}

	/**
	 * Reinitializing the variable.
	 *
	 * @throws Exception
	 *             the exception
	 */

	@AfterTest
	public void tearDown() throws Exception {
		expectedAssayType = null;
		expectedMaternalAge = 0;
		actualMaternalAge = 0;
		actualEggDonor = null;
		expectedName = null;
		actualName = null;
		actualFlag = null;
		expectedSampleType = null;
		expectedTestId = null;
		expectedWorkflowStatus = null;
	}
}
