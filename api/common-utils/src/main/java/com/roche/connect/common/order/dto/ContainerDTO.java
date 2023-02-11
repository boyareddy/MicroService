package com.roche.connect.common.order.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String containerId;
	private String containerType;
	private long sampleCount;

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public long getSampleCount() {
		return sampleCount;
	}

	public void setSampleCount(long sampleCount) {
		this.sampleCount = sampleCount;
	}

}
