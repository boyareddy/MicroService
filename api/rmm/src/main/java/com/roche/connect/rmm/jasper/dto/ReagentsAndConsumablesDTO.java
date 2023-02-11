package com.roche.connect.rmm.jasper.dto;

import java.sql.Timestamp;

public class ReagentsAndConsumablesDTO {
	private String lotNumber;
	private Timestamp expiryDate;
	private String name;
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public Timestamp getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
