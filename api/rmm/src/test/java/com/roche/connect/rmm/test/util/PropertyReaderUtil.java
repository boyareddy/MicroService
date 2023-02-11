package com.roche.connect.rmm.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

public class PropertyReaderUtil {

	private static HMTPLogger logger = HMTPLoggerFactory.getLogger(PropertyReaderUtil.class.getName());

	private PropertyReaderUtil() {

	}

	private static String propertyFilePath = "src/test/java/resource/test.properties";

	static Properties getPropertiesInstance() {
		return new Properties();
	}

	static Properties prop = null;

	public static Properties getProperty() {

		if (prop == null) {
			prop = getPropertiesInstance();
		}
		InputStream in;
		try {
			in = new FileInputStream(propertyFilePath);
			prop.load(in);
		} catch (FileNotFoundException exp) {
			logger.error("File not found : " + exp.getMessage());
		} catch (IOException exp) {
			logger.error("Error while reading file : " + exp.getMessage());
		}

		return prop;
	}
}
