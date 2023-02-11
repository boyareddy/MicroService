package com.roche.connect.forte.adapter.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.roche.connect.common.forte.SecondaryJobDetailsDTO;
/**
 * 
 * @author daram.m
 *
 */
public class PatientCache {
	private static PatientCache instance;
	private Map<String, SecondaryJobDetailsDTO> dataMap;

	private PatientCache() {
		dataMap = new ConcurrentHashMap<>();
	}

	public static synchronized PatientCache getInstance() {
		if (instance == null) {
			instance = new PatientCache();
		}
		return instance;
	}

	public void put(String key, SecondaryJobDetailsDTO value) {
		dataMap.put(key, value);
	}

	public Object get(String key) {
		return dataMap.get(key);
	}

	public boolean containskey(String key) {
		return dataMap.containsKey(key);
	}
	
}

