package com.roche.connect.common.amm.dto;

public class MolecularIDTypeDTO {

	private String molecularId;
	private String plateType;
	private String plateLocation;
	private long assayTypeId;
	private String assayType;
	
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getMolecularId() {
		return molecularId;
	}

	public void setMolecularId(String molecularId) {
		this.molecularId = molecularId;
	}

	public String getPlateType() {
		return plateType;
	}

	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}

	public String getPlateLocation() {
		return plateLocation;
	}

	public void setPlateLocation(String plateLocation) {
		this.plateLocation = plateLocation;
	}

	public long getAssayTypeId() {
		return assayTypeId;
	}

	public void setAssayTypeId(long assayTypeId) {
		this.assayTypeId = assayTypeId;
	}

}
