package com.roche.connect.dpcr.util;

import java.util.List;

public class DpcrInput {
	
	private List<String> plateId;
	private String queryMsgType;
	private String queryMsgEvent;
	private String queryProcessingId;
	private String versionId;
	private String receivingApplicationName;
	/**
	 * @return the plateId
	 */
	public List<String> getPlateId() {
		return plateId;
	}
	/**
	 * @param plateId the plateId to set
	 */
	public void setPlateId(List<String> plateId) {
		this.plateId = plateId;
	}
	/**
	 * @return the queryMsgType
	 */
	public String getQueryMsgType() {
		return queryMsgType;
	}
	/**
	 * @param queryMsgType the queryMsgType to set
	 */
	public void setQueryMsgType(String queryMsgType) {
		this.queryMsgType = queryMsgType;
	}
	/**
	 * @return the queryMsgEvent
	 */
	public String getQueryMsgEvent() {
		return queryMsgEvent;
	}
	/**
	 * @param queryMsgEvent the queryMsgEvent to set
	 */
	public void setQueryMsgEvent(String queryMsgEvent) {
		this.queryMsgEvent = queryMsgEvent;
	}
	/**
	 * @return the queryProcessingId
	 */
	public String getQueryProcessingId() {
		return queryProcessingId;
	}
	/**
	 * @param queryProcessingId the queryProcessingId to set
	 */
	public void setQueryProcessingId(String queryProcessingId) {
		this.queryProcessingId = queryProcessingId;
	}
	/**
	 * @return the versionId
	 */
	public String getVersionId() {
		return versionId;
	}
	/**
	 * @param versionId the versionId to set
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	/**
	 * @return the receivingApplicationName
	 */
	public String getReceivingApplicationName() {
		return receivingApplicationName;
	}
	/**
	 * @param receivingApplicationName the receivingApplicationName to set
	 */
	public void setReceivingApplicationName(String receivingApplicationName) {
		this.receivingApplicationName = receivingApplicationName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DpcrInput [plateId=" + plateId + ", queryMsgType=" + queryMsgType + ", queryMsgEvent=" + queryMsgEvent
				+ ", queryProcessingId=" + queryProcessingId + ", versionId=" + versionId
				+ ", receivingApplicationName=" + receivingApplicationName + "]";
	}
	

}
