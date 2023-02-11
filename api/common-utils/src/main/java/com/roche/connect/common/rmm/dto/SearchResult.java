package com.roche.connect.common.rmm.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class SearchResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private SearchOrderElements searchOrderElements;
	private SearchRunResultElements searchRunResultElements;

	public SearchOrderElements getSearchOrderElements() {
		return searchOrderElements;
	}

	public void setSearchOrderElements(SearchOrderElements searchOrderElements) {
		this.searchOrderElements = searchOrderElements;
	}

	public SearchRunResultElements getSearchRunResultElements() {
		return searchRunResultElements;
	}

	public void setSearchRunResultElements(SearchRunResultElements searchRunResultElements) {
		this.searchRunResultElements = searchRunResultElements;
	}

}
