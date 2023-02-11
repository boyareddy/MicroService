/*******************************************************************************
 * File Name: UpdateOrderMapperTest.java            
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

import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.Date;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.util.DateUtil;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;


public class UpdateOrderMapperTest {

	@InjectMocks
	OrderService orderService = new OrderServiceImpl();

	OrderParentDTO orderParent = new OrderParentDTO();
	OrderDTO orderDTO = new OrderDTO();
	Order order = new Order();
	String expectedCorrectAccessioningId = null;
	String expectedIncorrectAccessioningId = null;
	String actualAccessioningId = null;

	@Mock
	DateUtil dateUtil = org.mockito.Mockito.mock(DateUtil.class);

	/**
	 * Reading JSON inputs from the OrderCrudUpdateDetails.json file and parsing
	 * to JSON object. Also mocking the DateUtil class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeTest
	public void setUp() throws Exception {
		FileReader fr = new FileReader(new File("src/test/java/resource/OrderCrudUpdateDetails.json"));
		orderParent = new ObjectMapper().readValue(fr, OrderParentDTO.class);
		orderDTO = orderParent.getOrder();
		MockitoAnnotations.initMocks(this);
		Mockito.when(dateUtil.getCurrentUTCTimeStamp()).thenReturn(new Timestamp(new Date().getTime()));
		order.setPriority("high");
		order = orderService.orderUpdateMapper(orderDTO, order);
		expectedCorrectAccessioningId = "999999";
		expectedIncorrectAccessioningId = "1234567";
	}

	/**
	 * checking the updated AccessioningId with correct value.
	 */
	@Parameters({ "OrderManagementUnitTest" })
	@Test(priority = 1)
	public void testPositiveOrderMapperUpdate() {
		actualAccessioningId = order.getAccessioningId();
		Assert.assertEquals(actualAccessioningId, expectedCorrectAccessioningId);

	}

	/**
	 * checking the updated AccessioningId is not null.
	 */
	@Parameters({ "OrderManagementUnitTest" })
	@Test(priority = 2)
	public void testPositiveOrderMapperNotNull() {
		actualAccessioningId = order.getAccessioningId();
		Assert.assertNotNull(actualAccessioningId);

	}

	/**
	 * checking the updated AccessioningId with Incorrect value.
	 */
	@Parameters({ "OrderManagementUnitTest" })
	@Test(priority = 3)
	public void testNegativeOrderMapperUpdate() {
		actualAccessioningId = order.getAccessioningId();
		Assert.assertNotEquals(actualAccessioningId, expectedIncorrectAccessioningId);

	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@AfterTest
	public void tearDown() throws Exception {
		expectedCorrectAccessioningId = null;
		expectedIncorrectAccessioningId = null;
	}

}
