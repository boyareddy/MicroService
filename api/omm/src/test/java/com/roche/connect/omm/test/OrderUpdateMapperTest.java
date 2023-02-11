package com.roche.connect.omm.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.util.DateUtil;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;


public class OrderUpdateMapperTest {

	@InjectMocks
	OrderService orderService=new OrderServiceImpl();
	@Mock
	DateUtil dateUtil = org.mockito.Mockito.mock(DateUtil.class);

	
	Order order = null;
	OrderDTO orderDTO = null;
	OrderParentDTO orderParent=null;

	String expectedCorrectAccessioningId=null;
	String expectedIncorrectAccessioningId=null;
	
	@BeforeTest
	public void setUp() throws Exception {
		expectedCorrectAccessioningId="999999";
		expectedIncorrectAccessioningId="12112";
		
		FileReader fr=new FileReader(new File("src/test/java/Resource/OrderCrudUpdateDetails.json"));
		ObjectMapper objectMapper=new ObjectMapper();
		orderParent=objectMapper.readValue(fr, OrderParentDTO.class);
		orderDTO=orderParent.getOrder();
				
		order = new Order();
		
		MockitoAnnotations.initMocks(this);
		Mockito.when(dateUtil.getCurrentUTCTimeStamp()).thenReturn(new Timestamp(new Date().getTime()));
		
	}
	
	@Test
	public void orderMapperPosTest() throws ParseException {
		order=orderService.orderUpdateMapper(orderDTO,order);
		String actualAccessioningId=order.getAccessioningId();
		assertEquals(actualAccessioningId,expectedCorrectAccessioningId);
	}
	

	@Test
	public void orderMapperNegTest() throws ParseException {
		order=orderService.orderUpdateMapper(orderDTO,order);
		String actualAccessioningId=order.getAccessioningId();
		assertNotEquals(actualAccessioningId,expectedIncorrectAccessioningId);
	}
	
	/**
	 * Reinitializing the variable
	 * @throws Exception
	 */
	
	@AfterTest
	public void tearDown() throws Exception {
		expectedCorrectAccessioningId=null;
		expectedIncorrectAccessioningId=null;
	}
}
