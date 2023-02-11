package com.roche.connect.adm.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

public class PropertyReaderUtil {

	private PropertyReaderUtil() {
	}

	private static String propertyFilePath = "src/test/java/com/roche/connect/adm/test/resource/test.properties";

	private static HMTPLogger logger = HMTPLoggerFactory.getLogger(PropertyReaderUtil.class.getName());

	public static Properties getProperty() {
		Properties prop = new Properties();
		InputStream in;
		try {
			in = new FileInputStream(propertyFilePath);
			prop.load(in);
		} catch (FileNotFoundException exp) {
			logger.error("The test file is not available in the specified path", exp.getMessage());
		} catch (IOException ioexp) {
			logger.error("Properties unable to load the file" + ioexp.getMessage());
		}
		return prop;
	}
}
