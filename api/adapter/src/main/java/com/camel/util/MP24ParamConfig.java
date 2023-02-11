package com.camel.util;

import java.io.InputStream;

import com.camel.dto.MP24ConfigDetails;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MP24ParamConfig {
    
    private static MP24ParamConfig instance;
    
    
    private MP24ConfigDetails mp24ConfigDetails;
    
    public MP24ConfigDetails getMp24ConfigDetails() {
		return this.mp24ConfigDetails;
	}

	public void setMp24ConfigDetails(MP24ConfigDetails mp24ConfigDetails) {
		this.mp24ConfigDetails = mp24ConfigDetails;
	}
    
    /**
     * Get the instance of RouterUrlCofig.
     */
    public static MP24ParamConfig getInstance() {
        if (instance == null) {
            instance = new MP24ParamConfig();
            instance.fetchSampleTypeConfig();
        }
        
        return instance;
    }
    
    private void fetchSampleTypeConfig() {
        
        try {
        	InputStream stream = this.getClass().getResourceAsStream("/sampletypeconfig.json");
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory jsonFactory = mapper.getFactory();
            JsonParser jp = jsonFactory.createParser(stream);

            this.mp24ConfigDetails = jp.readValueAs(MP24ConfigDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
