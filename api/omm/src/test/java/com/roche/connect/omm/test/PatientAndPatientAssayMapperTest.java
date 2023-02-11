/*******************************************************************************
 * File Name: UpdatePatientObjectTest.java            
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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.Map;

import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientAssay;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;

public class PatientAndPatientAssayMapperTest {

	@InjectMocks
	OrderService orderService = new OrderServiceImpl();

	OrderParentDTO orderParent = new OrderParentDTO();
	PatientDTO patientDTO = new PatientDTO();
	Patient patient = new Patient();
	String patientFirstName = null;

	// patientAssayMapper
	PatientAssay patientAssay = new PatientAssay();
	AssayDTO assayDTO = new AssayDTO();
	String EggDonor = null;

	/**
	 * Reading JSON inputs from the patient.json file and parsing to JSON
	 * object.
	 *
	 * @throws Exception
	 *             the exception
	 */

	@BeforeTest
	public void setUp() throws Exception {

		FileReader fr = new FileReader(new File("src/test/java/resource/OrderCrudUpdateDetails.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		orderParent = objectMapper.readValue(fr, OrderParentDTO.class);
		patientDTO = orderParent.getOrder().getPatient();
		assayDTO = orderParent.getOrder().getAssay();
		
		ReflectionTestUtils.setField(orderService, "phiEncrypt", false);
		
		patientFirstName = "Kumar";
		EggDonor = "Self";
	}

	/**
	 * Patient object update with json data for positive scenario.
	 *
	 * @throws ParseException
	 *             the parse exception
	 */
	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void patientUpdateMapperPosTest() throws ParseException {
		patient = orderService.patientUpdateMapper(patient, patientDTO);
		assertEquals(patient.getPatientFirstName(), patientFirstName);
	}

	/**
	 * Patient object creation with json data for negative scenario.
	 *
	 * @throws ParseException
	 *             the parse exception
	 */
	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void patientUpdateMapperNegTest() throws ParseException {
		patient = orderService.patientUpdateMapper(patient, patientDTO);
		assertNotEquals(patient.getPatientFirstName(), "abc");
	}

	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void patientAssayMapperPosTest() throws Exception {

		patientAssay = orderService.patientAssayMapper(patientAssay, assayDTO);
		assertEquals(EggDonor, patientAssay.getEggDonor());
	}

	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void patientAssayMapperNegTest() throws ParseException {
		patientAssay = orderService.patientAssayMapper(patientAssay, assayDTO);
		assertNotEquals(patientAssay.getIvfStatus(), "No");
	}

	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void testOptionsPosTest() throws Exception {

		Map<String, Boolean> testOptionsDTO = assayDTO.getTestOptions();

		for (Map.Entry<String, Boolean> testidsvalue : testOptionsDTO.entrySet()) {

			assertTrue(testidsvalue.getValue().toString().equals("true")
					|| testidsvalue.getValue().toString().equals("false"));
		}

	}

	@Parameters({ "OrderManagementUnitTest" })
	@Test
	public void testOptionsNegTest() throws Exception {

		Map<String, Boolean> testOptionsDTO = assayDTO.getTestOptions();

		for (Map.Entry<String, Boolean> testidsvalue : testOptionsDTO.entrySet()) {

			assertFalse(testidsvalue.getValue().toString().equals("Yes")
					|| testidsvalue.getValue().toString().equals("No"));

		}
	}

	/**
	 * Reinitializing the variable.
	 *
	 * @throws Exception
	 *             the exception
	 */

	@AfterTest
	public void tearDown() throws Exception {
		patientFirstName = null;
		EggDonor = null;
	}

}
