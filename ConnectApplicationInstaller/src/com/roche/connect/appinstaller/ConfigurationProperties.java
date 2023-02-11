package com.roche.connect.appinstaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigurationProperties {
	
	static ConfigurationProperties configurationProperties;
	
	static Properties propertiesMap = new Properties();
	
	SaveYamlProperties saveYamlProperties = new SaveYamlProperties(); 
	
	private ConfigurationProperties() {}
	
	public static Properties loadProperties() {
		SaveYamlProperties saveYamlProperties = new SaveYamlProperties();
		if(new File(saveYamlProperties.getFilepath("propertypath")).exists()) {
		try (InputStream input = new FileInputStream(saveYamlProperties.getFilepath("propertypath"))) {
			propertiesMap.load(input);
		} catch (IOException ex) {
            ex.printStackTrace();
        }
		}
		return propertiesMap;
	}
	
	public void stroreProperties() {
		try (OutputStream output = new FileOutputStream(saveYamlProperties.getFilepath("propertypath"))){
			propertiesMap.store(output, null);
		} catch (IOException io) {
            io.printStackTrace();
        }
	}
	
	public static ConfigurationProperties getInstance() {
		if(configurationProperties == null) {
			configurationProperties = new ConfigurationProperties();
		}
		return configurationProperties;
	}

}
