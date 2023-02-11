package com.roche.connect.common.rmm.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roche.connect.common.rmm.dto.SearchRunResult;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class SearchRunResultElements implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long totalElements = 0L;
	private Collection<SearchRunResult> runResults = new LinkedList<>();

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public Collection<SearchRunResult> getRunResults() {
		return runResults;
	}

	public void setRunResults(Collection<SearchRunResult> runResults) {
		this.runResults = runResults;
	}

}
