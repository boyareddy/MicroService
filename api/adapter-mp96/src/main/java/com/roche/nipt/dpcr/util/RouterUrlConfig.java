package com.roche.nipt.dpcr.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class RouterUrlConfig {
	private static final Logger log = LogManager.getLogger(RouterUrlConfig.class);

	private static RouterUrlConfig instance;

	private Map<String, String> urlMap;

	public Map<String, String> getURLMap() {
		return urlMap;
	}

	public void setURLMap(Map<String, String> uRLMap) {
		this.urlMap = uRLMap;
	}

	public static RouterUrlConfig getInstance() {
		if (instance == null) {
			instance = new RouterUrlConfig();
		}
		instance.fetchUrlConfig();
		return instance;
	}

	public boolean fetchUrlConfig() {

		if (urlMap != null && urlMap.size() > 0) {
			return true;
		}

		boolean success = false;
		String response = getFile();

		try {
			if (response != null && !response.isEmpty()) {

				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(response);
				Iterator<String> fieldIterator = node.getFieldNames();

				while (fieldIterator.hasNext()) {
					String field = fieldIterator.next();
					if (field.equalsIgnoreCase(AppUrlEntry.findByKey(AppUrlEntry.ROOT_ELEMENT))) {
						Map<String, String> urlMap1 = mapper.readValue(node.get(field),
								new TypeReference<Map<String, String>>() {
								});
						setURLMap(urlMap1);
					}
				}
				success = true;

			}
		} catch (Exception e) {
			log.info("Exception at Router Url config :::: ");
		}

		return success;

	}

	public String getUrl(AppUrlEntry url) {
		if (urlMap != null)
			return urlMap.get(url.getValue());
		return null;
	}

	private String getFile() {

		StringBuilder result = new StringBuilder("");
		try {

			Resource resource = new ClassPathResource("urlConfig.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					result.append(line).append("\n");
				}
			} finally {
				reader.close();
			}

		} catch (Exception e) {
			log.error("Exception at Router Url config :::::: ");
		}

		return result.toString();

	}

}
