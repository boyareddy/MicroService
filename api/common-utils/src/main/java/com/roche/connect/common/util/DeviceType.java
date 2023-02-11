package com.roche.connect.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeviceType {

	@Value("${deviceType.magnaPure24}")
	private String mp24;

	@Value("${deviceType.lp24}")
	private String lp24;

	@Value("${deviceType.htp}")
	private String htp;

	@Value("${deviceType.forte}")
	private String forte;

	public String getMp24() {
		return mp24;
	}

	public String getLp24() {
		return lp24;
	}

	public String getHtp() {
		return htp;
	}

	public String getForte() {
		return forte;
	}

}
