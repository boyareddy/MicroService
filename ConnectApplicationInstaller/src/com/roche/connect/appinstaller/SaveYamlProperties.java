package com.roche.connect.appinstaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

public class SaveYamlProperties {

	private Properties configPropMap = ConfigurationProperties.propertiesMap;
	
	
	public void saveDataToYaml() {
		try {
			Yaml yaml = new Yaml();
			FileInputStream inputStream = new FileInputStream(new File(getFilepath("commonyamlpath")));
			Map<String, Object> obj = (Map<String, Object>) yaml.load(inputStream);
			updateProblemReportPath(obj);
			writeDataToYaml(obj);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProblemReportPath(Map<String, Object> obj) {    
		Map<String, Object> tempObj = new HashMap<>();
		
		tempObj=(Map<String, Object>) obj.get("problemReport");
		tempObj.put("problemReportPath", configPropMap.getProperty("problemReportPath"));
		
		tempObj=(Map<String, Object>) obj.get("backup");
		tempObj.put("defaultBackupLocation", configPropMap.getProperty("backupPath"));
		
		tempObj=(Map<String, Object>) obj.get("pas");
		tempObj.put("application_host_name", configPropMap.getProperty("fqdnserver"));
		tempObj.put("domainName", configPropMap.getProperty("labDomain"));
		
		tempObj=(Map<String, Object>) obj.get("server");
		tempObj.put("ssl.key-store", configPropMap.getProperty("sslKeyStore"));
		tempObj.put("ssl.keyAlias", configPropMap.getProperty("keyAlias"));
		tempObj.put("ssl.key-store-password",configPropMap.getProperty("key-store-password"));
		tempObj.put("ssl.keyStoreType",configPropMap.getProperty("keyStoreType"));
		tempObj=(Map<String, Object>) obj.get("connect");
		tempObj.put("db_host_name", configPropMap.getProperty("dbAddress"));
		tempObj.put("db_port", configPropMap.getProperty("dbPort"));
		tempObj.put("db_user_name", configPropMap.getProperty("dbUsername"));
		tempObj.put("db_password", configPropMap.getProperty("dbPassword"));
		
		tempObj=(Map<String, Object>) obj.get("pas");
		tempObj.put("server", configPropMap.getProperty("mailServer"));
		tempObj.put("port", configPropMap.getProperty("mailPort"));
		tempObj.put("username_mailsender", configPropMap.getProperty("mailUserName"));
		tempObj.put("password_mailsender", configPropMap.getProperty("mailPassword"));
		tempObj.put("ses_sender_mail_address", configPropMap.getProperty("mailSenderMail"));
		
	}
	
	public void writeDataToYaml(Map<String, Object> data) {
			Yaml yaml = new Yaml();
		   FileWriter writer;
		try {
			writer = new FileWriter(getFilepath("commonyamlpath"));
			yaml.dump(data, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getFilepath(String string) {
		Properties prop = new Properties();
		try {
			InputStream in = SaveYamlProperties.class.getClassLoader().getResourceAsStream("Config.properties");
			if (in != null)
				prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prop.getProperty(string);
	}

	
}