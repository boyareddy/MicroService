package com.roche.connect.forte.adapter.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "COMPLEX_ID_DETAILS")
public class ComplexIdDetails implements Serializable {
	
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
			@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "ID", nullable = false, unique = true)
	private UUID id;
	
	@Column(name = "COMPLEX_ID",nullable = false)
	private String complexId;
	
	@Column(name = "DEVICE_RUN_ID")
	private String deviceRunID;
	

	@Column(name = "DEVICE_ID")
	private String deviceID;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getComplexId() {
		return complexId;
	}

	public void setComplexId(String complexId) {
		this.complexId = complexId;
	}

	public String getDeviceRunID() {
		return deviceRunID;
	}

	public void setDeviceRunID(String deviceRunID) {
		this.deviceRunID = deviceRunID;
	} 
	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	

}
