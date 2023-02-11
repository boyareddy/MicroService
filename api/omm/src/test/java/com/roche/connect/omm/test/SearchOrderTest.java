package com.roche.connect.omm.test;

import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.data.domain.Page;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.logger.HMTPLoggerImpl;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderElements;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.services.OrderService;

@PrepareForTest({ RestClientUtil.class })
@PowerMockIgnore({ "sun.misc.Launcher.*", "com.sun.*", "javax.*", "javax.ws.*", "org.mockito.*", "javax.management.*",
		"org.w3c.dom.*", "org.apache.logging.*", "org.slf4j.*" })
public class SearchOrderTest {

	@Mock
	HMTPLoggerImpl hmtpLoggerImpl;

	@Mock
	Invocation.Builder assayClient;

	@Mock
	private OrderReadRepository orderReadRepository = org.mockito.Mockito.mock(OrderReadRepository.class);

	@Spy
	private OrderService orderService = org.mockito.Mockito.mock(OrderService.class);

	@InjectMocks
	OrderCrudRestApiImpl orderCrudRestApiImpl = new OrderCrudRestApiImpl();
	
	@SuppressWarnings("unchecked")
	@Mock
	private Page<Order> page = Mockito.mock(Page.class);

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	@Test
	public void searchOrderTest() throws UnsupportedEncodingException {

		MockitoAnnotations.initMocks(this);

		List<Order> searchOrderList = new ArrayList<>();
		Order order = new Order();
		order.setAccessioningId("100200");
		searchOrderList.add(order);

		List<OrderDTO> orderdtoList = new ArrayList<>();
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setAccessioningId("100200");
		orderdtoList.add(orderDTO);

		OrderElements orderElements = new OrderElements();
		orderElements.setOrders(orderdtoList);
		orderElements.setTotalElements(2L);

		Mockito.when(orderReadRepository.findByAccessioningIdContainingIgnoreCaseAndOrderStatusAndCompanyId(
				any(String.class), any(String.class), any(long.class), Mockito.any())).thenReturn(page);
		
		Mockito.when(page.getContent()).thenReturn(searchOrderList);
		Mockito.when(page.getTotalElements()).thenReturn(55L);
		Response response = orderCrudRestApiImpl.searchOrder("100", "unassigned", 2, 10);
		
		assertEquals(response.getStatus(), HttpStatus.SC_OK);
	}

	@Test
	public void searchOrderTest2() throws UnsupportedEncodingException {

		MockitoAnnotations.initMocks(this);

		Response response = orderCrudRestApiImpl.searchOrder(null, "unassigned", 2, 10);
		
		assertEquals(response.getStatus(), HttpStatus.SC_BAD_REQUEST);
	}
	
	@Test
	public void searchOrderTestException() throws UnsupportedEncodingException {

		MockitoAnnotations.initMocks(this);

		List<Order> searchOrderList = new ArrayList<>();
		Order order = new Order();
		order.setAccessioningId("100200");
		searchOrderList.add(order);

		List<OrderDTO> orderdtoList = new ArrayList<>();
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setAccessioningId("100200");
		orderdtoList.add(orderDTO);

		OrderElements orderElements = new OrderElements();
		orderElements.setOrders(orderdtoList);
		orderElements.setTotalElements(2L);

		Mockito.when(orderReadRepository.findByAccessioningIdContainingIgnoreCaseAndOrderStatusAndCompanyId(
				any(String.class), any(String.class), any(long.class), Mockito.any())).thenThrow(Exception.class);
		
		Mockito.when(page.getContent()).thenReturn(searchOrderList);
		Mockito.when(page.getTotalElements()).thenReturn(55L);
		Response response = orderCrudRestApiImpl.searchOrder("100", "unassigned", 2, 10);
		
		assertEquals(response.getStatus(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
}
