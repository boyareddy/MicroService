package com.roche.connect.adm.service;

import java.util.Set;

import com.hcl.hmtp.common.client.exceptions.HMTPException;

public interface SecurityServices {
	public Set<String> getRoles(Set<Integer> roleIds) throws HMTPException;
}
