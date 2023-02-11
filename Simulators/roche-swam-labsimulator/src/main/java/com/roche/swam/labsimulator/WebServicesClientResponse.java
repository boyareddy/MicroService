package com.roche.swam.labsimulator;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class WebServicesClientResponse {
    public static final Logger logger = Logger.getLogger(WebServicesClientResponse.class);
    
    private static final String SSL_PROTOCOL = "SSL";
    ClientResponse response = null;
    DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
    Client client = Client.create(defaultClientConfig);
    WebResource webResource = null;
    
    public ClientResponse getResponse(String url, String type, String methodType, JSONObject input) {
        logger.info(url);
        logger.info(input);
        try {
            SSLContext sslContext = SSLContext.getInstance(SSL_PROTOCOL);
            ServerTrustManager serverTrustManager = new ServerTrustManager();
            sslContext.init(null, new TrustManager[]{ serverTrustManager }, null);
            
            defaultClientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                new HTTPSProperties(null, sslContext));
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.error("Exception occured" + e.getMessage());
        }
        webResource = client.resource(url);
        response = checkHTTPMethods(type, methodType, input);
        return response;
    }
    
    public ClientResponse checkHTTPMethods(String mediaType, String httpMethodType, JSONObject jsonInput) {
        try {
            switch(httpMethodType){
                case "post" :
                    if (jsonInput == null) {
                        response = webResource.type(mediaType).post(ClientResponse.class);
                    } else {
                        response = webResource.type(mediaType).post(ClientResponse.class, jsonInput.toString());
                    }
                    
                    break;
                case "put" :
                    if (jsonInput == null) {
                        response = webResource.type(mediaType).put(ClientResponse.class);
                    } else {
                        response = webResource.type(mediaType).put(ClientResponse.class, jsonInput.toString());
                    }
                    break;
                case "get" :
                    response = webResource.type(mediaType).get(ClientResponse.class);
                    break;
                default :
                    break;
            }
        } catch (Exception e) {
          logger.error(e.getStackTrace());
        }
        return response;
    }
}
