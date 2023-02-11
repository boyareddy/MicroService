package com.roche.connect.rmm.jasper.dto;

import java.sql.Timestamp;

public class DPCRReagentnConsumableDTO {
	private String patitioningSerialNo;
	private String partioningFluidId;
	private Timestamp partioningTime;
	public String getPatitioningSerialNo() {
		return patitioningSerialNo;
	}
	public void setPatitioningSerialNo(String patitioningSerialNo) {
		this.patitioningSerialNo = patitioningSerialNo;
	}
	public String getPartioningFluidId() {
		return partioningFluidId;
	}
	public void setPartioningFluidId(String partioningFluidId) {
		this.partioningFluidId = partioningFluidId;
	}
	public Timestamp getPartioningTime() {
		return partioningTime;
	}
	public void setPartioningTime(Timestamp partioningTime) {
		this.partioningTime = partioningTime;
	}
	
}
