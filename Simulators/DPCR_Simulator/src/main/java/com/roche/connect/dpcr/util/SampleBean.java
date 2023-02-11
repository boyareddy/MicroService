package com.roche.connect.dpcr.util;

import java.util.List;

public class SampleBean {
	
	private String accessioningId;
	private String inputchipId;
	private String inputchipPosition;
	private String operatorName;
	private String flag;

	

	private List<AssayBean> assay ;
	private List<PartitionEngineBean> partitionEngine;
	/**
	 * @return the accessioningId
	 */
	public String getAccessioningId() {
		return accessioningId;
	}
	/**
	 * @param accessioningId the accessioningId to set
	 */
	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}
	/**
	 * @return the inputchipId
	 */
	public String getInputchipId() {
		return inputchipId;
	}
	/**
	 * @param inputchipId the inputchipId to set
	 */
	public void setInputchipId(String inputchipId) {
		this.inputchipId = inputchipId;
	}
	/**
	 * @return the inputchipPosition
	 */
	public String getInputchipPosition() {
		return inputchipPosition;
	}
	/**
	 * @param inputchipPosition the inputchipPosition to set
	 */
	public void setInputchipPosition(String inputchipPosition) {
		this.inputchipPosition = inputchipPosition;
	}
	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}
	/**
	 * @param operatorName the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * @return the assay
	 */
	public List<AssayBean> getAssay() {
		return assay;
	}
	/**
	 * @param assay the assay to set
	 */
	public void setAssay(List<AssayBean> assay) {
		this.assay = assay;
	}
	/**
	 * @return the partitionEngine
	 */
	public List<PartitionEngineBean> getPartitionEngine() {
		return partitionEngine;
	}
	/**
	 * @param partitionEngine the partitionEngine to set
	 */
	public void setPartitionEngine(List<PartitionEngineBean> partitionEngine) {
		this.partitionEngine = partitionEngine;
	}
	/**
	 * @return the result
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SampleBean [accessioningId=" + accessioningId + ", inputchipId=" + inputchipId + ", inputchipPosition="
				+ inputchipPosition + ", operatorName=" + operatorName + ", flag=" + flag + ", assay=" + assay
				+ ", partitionEngine=" + partitionEngine + "]";
	}
	
	
}
