package com.roche.connect.adm.dto;

public class MessageRecepient {
	
	private long id;
	private long roleId;
	private long type;
		
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "MessageRecepient [id=" + id + ", roleId=" + roleId + ", type=" + type + "]";
	}
	
}
