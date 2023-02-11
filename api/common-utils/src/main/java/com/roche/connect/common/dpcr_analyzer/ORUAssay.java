package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ORUAssay implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String packageName;
	private String version;
	private String kit;
	private String quantitativeValue;
	private String quantitativeResult;
	private String qualitativeValue;
	private String qualitativeResult;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getKit() {
		return kit;
	}

	public void setKit(String kit) {
		this.kit = kit;
	}

	public String getQuantitativeValue() {
		return quantitativeValue;
	}

	public void setQuantitativeValue(String quantitativeValue) {
		this.quantitativeValue = quantitativeValue;
	}

	public String getQuantitativeResult() {
		return quantitativeResult;
	}

	public void setQuantitativeResult(String quantitativeResult) {
		this.quantitativeResult = quantitativeResult;
	}

	public String getQualitativeValue() {
		return qualitativeValue;
	}

	public void setQualitativeValue(String qualitativeValue) {
		this.qualitativeValue = qualitativeValue;
	}

	public String getQualitativeResult() {
		return qualitativeResult;
	}

	public void setQualitativeResult(String qualitativeResult) {
		this.qualitativeResult = qualitativeResult;
	}

}
