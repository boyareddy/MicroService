package com.roche.swam.labsimulator.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roche.swam.labsimulator.mpx.bl.Sample;

@JsonInclude(Include.NON_NULL)
public class SamplesListBean {
	
	private List<Sample> samples;

	public List<Sample> getSamples() {
		return samples;
	}

	public void setSamples(List<Sample> samples) {
		this.samples = samples;
	}

	@Override
	public String toString() {
		return "SamplesListBean [samples=" + samples + ", getSamples()=" + getSamples() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	

}
