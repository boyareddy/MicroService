package com.roche.connect.common.order.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class OrderElements implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long totalElements = 0L;
	private Collection<OrderDTO> orders = new LinkedList<>();

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public Collection<OrderDTO> getOrders() {
		return orders;
	}

	public void setOrders(Collection<OrderDTO> orders) {
		this.orders = orders;
	}

}
