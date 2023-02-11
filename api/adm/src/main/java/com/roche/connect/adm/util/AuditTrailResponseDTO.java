package com.roche.connect.adm.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditTrailResponseDTO {
	
	private EntityDTO entity;

	public EntityDTO getEntity() {
		return entity;
	}

	public void setEntity(EntityDTO entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "AuditTrailResponseDTO [entity=" + entity + "]";
	}

}
