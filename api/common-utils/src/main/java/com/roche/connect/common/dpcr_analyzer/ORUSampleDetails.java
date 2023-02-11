package com.roche.connect.common.dpcr_analyzer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ORUSampleDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	private String accessioningId;
	private String flag;
	private Collection<ORUAssay> assayList = new ArrayList<>();
	private Collection<ORUPartitionEngine> partitionEngineList = new ArrayList<>();

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Collection<ORUAssay> getAssayList() {
		return assayList;
	}

	public void setAssayList(Collection<ORUAssay> assayList) {
		this.assayList = assayList;
	}

	public Collection<ORUPartitionEngine> getPartitionEngineList() {
		return partitionEngineList;
	}

	public void setPartitionEngineList(Collection<ORUPartitionEngine> partitionEngineList) {
		this.partitionEngineList = partitionEngineList;
	}

}
