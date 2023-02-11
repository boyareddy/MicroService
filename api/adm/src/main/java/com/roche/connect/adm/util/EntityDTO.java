package com.roche.connect.adm.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityDTO {

	private List<AuditTrailDetailDTO> lstAuditTrail;
	private long totalCount;

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<AuditTrailDetailDTO> getLstAuditTrail() {
		return lstAuditTrail;
	}

	public void setLstAuditTrail(List<AuditTrailDetailDTO> lstAuditTrail) {
		this.lstAuditTrail = lstAuditTrail;
	}

	@Override
	public String toString() {
		return "EntityDTO [lstAuditTrail=" + lstAuditTrail + ", totalCount=" + totalCount + "]";
	}
	
	
}
