package com.roche.connect.common.order.dto;

import java.util.List;

public class BulkOrdersDTO {

	private List<OrderParentDTO> orderParentDTO;

	public List<OrderParentDTO> getOrderParentDTO() {
		return orderParentDTO;
	}

	public void setOrderParentDTO(List<OrderParentDTO> orderParentDTO) {
		this.orderParentDTO = orderParentDTO;
	}
	
}
