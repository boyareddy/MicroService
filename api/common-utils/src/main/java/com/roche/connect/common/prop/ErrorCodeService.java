package com.roche.connect.common.prop;

import java.io.InputStream;
import java.util.Properties;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

public class ErrorCodeService {
    
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	
	private static ErrorCodeService instance = null;
    
	private Properties properties = null;
    
	private ErrorCodeService() {

		properties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try (InputStream resourceStream = loader.getResourceAsStream("ErrorCode.properties")) {
			properties.load(resourceStream);
		} catch (Exception e) {
			logger.error("Failed to load ErrorCode.properties file, message: " + e.getMessage(), e);
		}
	}
    
    public static ErrorCodeService getInstance() {
        
        if (instance == null)
            instance = new ErrorCodeService();
        
        return instance;
    }
    
    public String get(String key) {
        return properties.getProperty(key);
    }
}
