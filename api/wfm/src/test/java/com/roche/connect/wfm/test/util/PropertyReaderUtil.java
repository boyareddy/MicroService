package com.roche.connect.wfm.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReaderUtil {
	private static String propertyFilePath = "src/test/java/resource/test.properties";
	
	public static Properties getProperty(){
		Properties prop = new Properties();
		InputStream in;
		try {
			in = new FileInputStream(propertyFilePath);
			prop.load(in);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return prop;
	}
}
