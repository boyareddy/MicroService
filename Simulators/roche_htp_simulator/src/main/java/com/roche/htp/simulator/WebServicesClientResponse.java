package com.roche.htp.simulator;

import java.io.FileReader;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class WebServicesClientResponse {
	public static final Logger logger = Logger.getLogger(WebServicesClientResponse.class);
	private static final String SSL_PROTOCOL = "SSL";
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	ClientResponse response = null;
	DefaultClientConfig defaultClientConfig = null;
	Client client = null;
	WebResource webResource = null;

	public WebServicesClientResponse() {
		this.defaultClientConfig = new DefaultClientConfig();
		try {
			SSLContext sslContext = SSLContext.getInstance(SSL_PROTOCOL);
			ServerTrustManager serverTrustManager = new ServerTrustManager();
			sslContext.init(null, new TrustManager[] { serverTrustManager }, null);

			defaultClientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
					new HTTPSProperties(null, sslContext));
			this.client = Client.create(defaultClientConfig);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			logger.error("Exception occured" + e.getMessage());
		}

	}

	public ClientResponse getResponse(String url, String type, String methodType, JSONObject input) {
		logger.info(url);
		logger.info(input);
		webResource = client.resource(url);
		response = checkHTTPMethods(type, methodType, input);
		return response;
	}

	public ClientResponse checkHTTPMethods(String mediaType, String httpMethodType, JSONObject jsonInput) {
		try {
			String token = generateOAuthToken();
			switch (httpMethodType) {
			case "post":
				if (jsonInput == null) {
					response = webResource.type(mediaType).header(AUTHORIZATION, BEARER + token)
							.post(ClientResponse.class);
				} else {
					response = webResource.type(mediaType).header(AUTHORIZATION, BEARER + token)
							.post(ClientResponse.class, jsonInput.toString());
				}
				break;
			case "put":
				if (jsonInput == null) {
					response = webResource.type(mediaType).header(AUTHORIZATION, BEARER + token)
							.put(ClientResponse.class);
				} else {
					response = webResource.type(mediaType).header(AUTHORIZATION, BEARER + token)
							.put(ClientResponse.class, jsonInput.toString());
				}
				break;
			case "get":
				response = webResource.type(mediaType).header(AUTHORIZATION, BEARER + token).get(ClientResponse.class);
				break;
			default:
				break;
			}
		} catch (ClientHandlerException | UniformInterfaceException | ParseException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
		return response;
	}

	public String generateOAuthToken() throws ParseException {
		String oAuthToken1 = null;
		JSONParser jsonparser = new JSONParser();
		String url = readProperties("secHostName") + readProperties("oAuthClientCredUrl");
		String name = readProperties("deviceClientID");
		String password = readProperties("devicePassword");
		String authString = name + ":" + password;
		String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
		WebResource webResource1 = client.resource(url);
		ClientResponse resp = webResource1.accept("application/json").type("application/json")
				.header("Authorization", "Basic " + authStringEnc).post(ClientResponse.class);
		JSONObject tokenEntity = (JSONObject) jsonparser.parse(resp.getEntity(String.class));
		logger.info("OAuth Token generated");
		oAuthToken1 = (String) tokenEntity.get("access_token");

		if (resp.getStatus() == 401) {
			logger.info("Status Code:" + resp.getStatus());
			logger.error("Received a request from Un-registered Device: " + name);
			System.exit(1);
		}

		if (oAuthToken1 == null || oAuthToken1 == "") {
			throw new ClientHandlerException("Error occured while generating token for client id" + name);

		}
		return oAuthToken1;
	}

	public String readProperties(String propKey) {
		Properties prop = new Properties();
		try {
			FileReader fr = new FileReader("resources//simulator.properties");
			prop.load(fr);
		} catch (IOException e) {
			logger.error(String.format("Exception while getting file path... %s", e));
		}
		return prop.getProperty(propKey);
	}
}
