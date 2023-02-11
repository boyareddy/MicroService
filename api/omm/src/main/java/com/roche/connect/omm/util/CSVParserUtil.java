package com.roche.connect.omm.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

public class CSVParserUtil {
	
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	
	public List<String> parseCSV(InputStream data) throws HMTPException {
		BufferedReader reader = null;
		List<String> rows = null;
		List<String> parsedData = null;
		String line = null;
		try {
			logger.info("parseCSV() -> Start of parseCSV() execution.");
			rows = new ArrayList<>();
			parsedData = new ArrayList<>();
			reader = new BufferedReader(new InputStreamReader(data));
			while((line = reader.readLine())!=null) {
				rows.add(line);
			}
			
			for(String str: rows) {
				if(str.trim().contains("Content-Disposition") || str.trim().contains("Content-Type") || str.trim().equals("") ||  str.trim().contains("WebKitFormBoundary")){	
				} else {
					parsedData.add(str);
				}
			}
			
		}catch (Exception exception) {
			logger.error("Error while parsing the CSV file : " + exception.getMessage());
			throw new HMTPException(exception);
		}
		logger.info("End of parseCSV() execution.");
		return parsedData;
	}
}
