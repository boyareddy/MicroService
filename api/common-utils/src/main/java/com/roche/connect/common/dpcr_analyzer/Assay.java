package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Assay implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String version;
	private String masterMix;
	private String kit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMasterMix() {
		return masterMix;
	}

	public void setMasterMix(String masterMix) {
		this.masterMix = masterMix;
	}

	public String getKit() {
		return kit;
	}

	public void setKit(String kit) {
		this.kit = kit;
	}

}
