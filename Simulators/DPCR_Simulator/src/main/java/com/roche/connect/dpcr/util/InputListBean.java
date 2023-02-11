package com.roche.connect.dpcr.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class InputListBean {
	
	private List<LpInputSample> samples;

	public List<LpInputSample> getSamples() {
		return samples;
	}

	public void setSamples(List<LpInputSample> samples) {
		this.samples = samples;
	}

	@Override
	public String toString() {
		return "SamplesListBean [samples=" + samples + ", getSamples()=" + getSamples() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	

}
