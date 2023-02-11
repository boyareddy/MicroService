package com.roche.connect.adm.config;

import java.util.List;

import org.springframework.web.socket.WebSocketSession;

public class SessionDetail {

	private WebSocketSession session;
	private List<String> roles;

	public SessionDetail(List<String> roles, WebSocketSession session) {
		this.roles = roles;
		this.session = session;
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
