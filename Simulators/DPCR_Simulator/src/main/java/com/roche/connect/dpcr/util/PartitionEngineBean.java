package com.roche.connect.dpcr.util;

public class PartitionEngineBean {
	
	private String plateId;
	private String serialNumber;
	private String fluidId;
	private String dateandTime;
	/**
	 * @return the plateId
	 */
	public String getPlateId() {
		return plateId;
	}
	/**
	 * @param plateId the plateId to set
	 */
	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}
	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the fluidId
	 */
	public String getFluidId() {
		return fluidId;
	}
	/**
	 * @param fluidId the fluidId to set
	 */
	public void setFluidId(String fluidId) {
		this.fluidId = fluidId;
	}
	/**
	 * @return the dateandTime
	 */
	public String getDateandTime() {
		return dateandTime;
	}
	/**
	 * @param dateandTime the dateandTime to set
	 */
	public void setDateandTime(String dateandTime) {
		this.dateandTime = dateandTime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PartitionEngineBean [plateId=" + plateId + ", serialNumber=" + serialNumber + ", fluidId=" + fluidId
				+ ", dateandTime=" + dateandTime + "]";
	}

	

}
