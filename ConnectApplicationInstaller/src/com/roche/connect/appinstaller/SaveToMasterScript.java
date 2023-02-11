package com.roche.connect.appinstaller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class SaveToMasterScript {
	
	private Properties configPropMap = ConfigurationProperties.propertiesMap;
	public void saveDataToSQL() {
		SaveYamlProperties saveYamlProperties = new SaveYamlProperties();
		File sqlFile = new File(saveYamlProperties.getFilepath("masterscriptpath"));
		String language="";
		if(configPropMap.getProperty("systemLanguage").equals("English-US"))
			language="en_US";
		String sqlTobeInserted = "\n\ninsert into system_settings(id,active_flag,attribute_name,attribute_type,attribute_value,created_by,created_date_time, image, updated_by, updated_date_time, companyid) values (2,null, 'backup_location', 'Backup','"+configPropMap.getProperty("backupPath")+"', null,null,null, null, null, 1);\r\n" + 
				"\r\n" + 
				"insert into system_settings(id,active_flag,attribute_name,attribute_type,attribute_value,created_by,created_date_time, image, updated_by, updated_date_time, companyid) values (3,'Y','systemLanguage','localisationSettings','"+language+"', null,null,null, null, null, 1);\r\n" + 
				"\r\n" + 
				"insert into system_settings(id,active_flag,attribute_name,attribute_type,attribute_value,created_by,created_date_time, image, updated_by, updated_date_time, companyid) values (4,'Y','dateFormat','localisationSettings','"+configPropMap.getProperty("dateFormat")+"', null,null,null, null, null, 1);\r\n" ;
		try {
			FileWriter fw = new FileWriter(sqlFile,true);
			BufferedWriter bufW = new BufferedWriter(fw);
			bufW.write(sqlTobeInserted);
			bufW.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
