package com.roche.connect.rmm.jasper.dto;

import java.sql.Timestamp;

public class MPConsumablesDTO {
	private String barcode;
	private Timestamp expirationDate;
	private String productionLot;
	private String volume;
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Timestamp getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getProductionLot() {
		return productionLot;
	}
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
}
