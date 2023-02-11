package com.roche.connect.common.rmm.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class SearchOrderElements  implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long totalElements = 0L;
	private Collection<SearchOrder> orders = new LinkedList<>();

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public Collection<SearchOrder> getOrders() {
		return orders;
	}

	public void setOrders(Collection<SearchOrder> orders) {
		this.orders = orders;
	}

}
