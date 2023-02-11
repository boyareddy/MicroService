package com.roche.connect.adm.util;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditTrailDetailDTO {

	@CsvBindByName(column = "id")
	private Integer id;
	
	@CsvBindByName(column = "systemid")
	private String systemid;
	
	@CsvBindByName(column = "systemmodulename")
	private String systemmodulename;
	
	@CsvBindByName(column = "messagecode")
	private String messagecode;
	
	@CsvBindByName(column = "params")
	private JSONObject params;
	
	@CsvBindByName(column = "timestamp")
	private String timestamp;
	
	@CsvBindByName(column = "objectoldvalue")
	private JSONObject objectoldvalue;
	
	@CsvBindByName(column = "objectnewvalue")
	private JSONObject objectnewvalue;
	
	@CsvBindByName(column = "newnessflag")
	private String newnessflag;
	
	@CsvBindByName(column = "companydomainname")
	private String companydomainname;
	
	@CsvBindByName(column = "message")
	private String message;
	
	@CsvBindByName(column = "title")
	private String title;
	
	@CsvBindByName(column = "ownerPropertyName")
	private String ownerPropertyName;
	
	@CsvBindByName(column = "userid")
	private String userid;
	
	@CsvBindByName(column = "ipaddress")
	private String ipaddress;

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSystemid() {
		return systemid;
	}

	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}

	public String getSystemmodulename() {
		return systemmodulename;
	}

	public void setSystemmodulename(String systemmodulename) {
		this.systemmodulename = systemmodulename;
	}

	public String getMessagecode() {
		return messagecode;
	}

	public void setMessagecode(String messagecode) {
		this.messagecode = messagecode;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public JSONObject getObjectoldvalue() {
		return objectoldvalue;
	}

	public void setObjectoldvalue(JSONObject objectoldvalue) {
		this.objectoldvalue = objectoldvalue;
	}

	public JSONObject getObjectnewvalue() {
		return objectnewvalue;
	}

	public void setObjectnewvalue(JSONObject objectnewvalue) {
		this.objectnewvalue = objectnewvalue;
	}

	public String getNewnessflag() {
		return newnessflag;
	}

	public void setNewnessflag(String newnessflag) {
		this.newnessflag = newnessflag;
	}

	public String getCompanydomainname() {
		return companydomainname;
	}

	public void setCompanydomainname(String companydomainname) {
		this.companydomainname = companydomainname;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOwnerPropertyName() {
		return ownerPropertyName;
	}

	public void setOwnerPropertyName(String ownerPropertyName) {
		this.ownerPropertyName = ownerPropertyName;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "AuditTrailDetailDTO [id=" + id + ", systemid=" + systemid + ", systemmodulename=" + systemmodulename
				+ ", messagecode=" + messagecode + ", params=" + params + ", timestamp=" + timestamp
				+ ", objectoldvalue=" + objectoldvalue + ", objectnewvalue=" + objectnewvalue + ", newnessflag="
				+ newnessflag + ", companydomainname=" + companydomainname + ", message=" + message + ", title=" + title
				+ ", ownerPropertyName=" + ownerPropertyName + ", ipaddress=" + ipaddress + "]";
	}
	
	
}
