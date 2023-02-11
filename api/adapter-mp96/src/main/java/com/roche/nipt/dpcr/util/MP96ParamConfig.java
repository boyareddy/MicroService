package com.roche.nipt.dpcr.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MP96ParamConfig {
private static final Logger logger = LogManager.getLogger(MP96ParamConfig.class);
	
	private static MP96ParamConfig instance;

	private MP96ParamConfig mp96ConfigDetails;

	public MP96ParamConfig getMp96ConfigDetails() {
		return this.mp96ConfigDetails;
	}

	public void setMp24ConfigDetails(MP96ParamConfig mp24ConfigDetails) {
		this.mp96ConfigDetails = mp24ConfigDetails;
	}

	public static MP96ParamConfig getInstance() throws IOException {
		if (instance == null) {
			instance = new MP96ParamConfig();
			instance.fetchSampleTypeConfig();
		}

		return instance;
	}

	private void fetchSampleTypeConfig() throws IOException {

		JsonParser jp = null;
		try {
			InputStream stream = this.getClass().getResourceAsStream("/sampletypeconfig.json");
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory jsonFactory = mapper.getFactory();
			jp = jsonFactory.createParser(stream);
			this.mp96ConfigDetails = jp.readValueAs(MP96ParamConfig.class);
		} catch (Exception e) {
			logger.error("Exception at fetchSampleTypeConfig:::: ");
		} finally {
			if (jp != null) {
				jp.close();
			}
		}

	}
}
