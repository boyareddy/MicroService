package com.roche.connect.dpcr.util;

public class AssayBean {

	private String assayType;
	private String assayVersion;
	private String analysisPackageName;
	private String Kit;
	private String masterMix;
	private String quantitativeValue;
	private String quantitativeResult;
	private String qualitativeValue;
	private String qualitativeResult;
	/**
	 * @return the assayType
	 */
	public String getAssayType() {
		return assayType;
	}
	/**
	 * @param assayType the assayType to set
	 */
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	/**
	 * @return the assayVersion
	 */
	public String getAssayVersion() {
		return assayVersion;
	}
	/**
	 * @param assayVersion the assayVersion to set
	 */
	public void setAssayVersion(String assayVersion) {
		this.assayVersion = assayVersion;
	}
	/**
	 * @return the analysispackageName
	 */
	public String getAnalysisPackageName() {
		return analysisPackageName;
	}
	/**
	 * @param analysisPackageName the analysispackageName to set
	 */
	public void setAnalysisPackageName(String analysisPackageName) {
		this.analysisPackageName = analysisPackageName;
	}
	/**
	 * @return the kit
	 */
	public String getKit() {
		return Kit;
	}
	/**
	 * @param kit the kit to set
	 */
	public void setKit(String kit) {
		Kit = kit;
	}
	/**
	 * @return the masterMix
	 */
	public String getMasterMix() {
		return masterMix;
	}
	/**
	 * @param masterMix the masterMix to set
	 */
	public void setMasterMix(String masterMix) {
		this.masterMix = masterMix;
	}
	/**
	 * @return the quantitativeResult
	 */
	public String getQuantitativeResult() {
		return quantitativeResult;
	}
	/**
	 * @param quantitativeResult the quantitativeResult to set
	 */
	public void setQuantitativeResult(String quantitativeResult) {
		this.quantitativeResult = quantitativeResult;
	}
	/**
	 * @return the qualitativeResult
	 */
	public String getQualitativeResult() {
		return qualitativeResult;
	}
	/**
	 * @param qualitativeResult the qualitativeResult to set
	 */
	public void setQualitativeResult(String qualitativeResult) {
		this.qualitativeResult = qualitativeResult;
	}
	
	
	/**
	 * @return the quantitativeValue
	 */
	public String getQuantitativeValue() {
		return quantitativeValue;
	}
	/**
	 * @param quantitativeValue the quantitativeValue to set
	 */
	public void setQuantitativeValue(String quantitativeValue) {
		this.quantitativeValue = quantitativeValue;
	}
	/**
	 * @return the qualitativeValue
	 */
	public String getQualitativeValue() {
		return qualitativeValue;
	}
	/**
	 * @param qualitativeValue the qualitativeValue to set
	 */
	public void setQualitativeValue(String qualitativeValue) {
		this.qualitativeValue = qualitativeValue;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AssayBean [assayType=" + assayType + ", assayVersion=" + assayVersion + ", analysisPackageName="
				+ analysisPackageName + ", Kit=" + Kit + ", masterMix=" + masterMix + ", quantitativeResult="
				+ quantitativeResult + ", quantitativeValue=" + quantitativeValue + ", qualitativeResult="
				+ qualitativeResult + ", qualitativeValue=" + qualitativeValue + "]";
	}
	

	
	
}
