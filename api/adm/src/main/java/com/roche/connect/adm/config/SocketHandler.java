package com.roche.connect.adm.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.util.ADMConstants;

@Component("socketHandler")
public class SocketHandler extends TextWebSocketHandler {

	static List<SessionDetail> sessionDetails = new CopyOnWriteArrayList<>();

	private HMTPLogger log = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		log.info("Sending a message to: " + new Gson().toJson(message));

		if (session.isOpen()) {

			log.info("Session is open for: " + session.getId());

			try {
				session.sendMessage(new TextMessage(new Gson().toJson(message.getPayload())));
			} catch (Exception e) {
				log.error("Exception on SocketHandler.handleTextMessage" + e.getMessage());
			}

		} else {
			log.info("Can't able send message. Session already closed: " + session.getId());

			for (SessionDetail s : sessionDetails) {
				if (s.getSession().getId().equals(session.getId())) {
					sessionDetails.remove(s);
				}
			}
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		log.info("Entering in to registerWebSocketHandlers.beforeHandshake()");

		String propFileName = "application.yaml";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		Properties prop = new Properties();
		prop.load(inputStream);

		String securityHostURL = prop.getProperty("security_remote_url").replace("${pas.security_remote_host_name}",
				prop.getProperty("security_remote_host_name"));

		List<String> queryString = Arrays.asList(session.getUri().getQuery().split("="));

		String token = queryString.size() >= 1 ? queryString.get(1).toString() : null;

		String jobDetailsResponse = null;

		if (token != null) {
			String jobDetailsURL = securityHostURL + ADMConstants.JOB_DETAILS_URL.toString();

			log.info("Getting job details from Security, URL: " + jobDetailsURL);

			Builder builder = RestClientUtil.getBuilder(jobDetailsURL, null);
			builder.header("Cookie", "brownstoneauthcookie=" + token);

			jobDetailsResponse = builder.get(new GenericType<String>() {
			});

			int status = builder.get().getStatus();

			if (status != HttpStatus.SC_OK) {
				session.close();
			}

		} else {
			log.info("Token missing in URL");
			closeSession(session);
		}

		if (isMaxLimitExceed(session.getRemoteAddress().getAddress().getHostAddress())) {
			log.info("Connection closed due to limit exceed");
			closeSession(session);
		}

		ObjectMapper mapper = new ObjectMapper();

		JsonNode node = mapper.readTree(jobDetailsResponse);

		List<String> roleNames = new ArrayList<>();

		for (JsonNode r : node.get("roles")) {
			roleNames.add(r.asText());
		}

		log.info("Roles: " + node.get("roles"));
		SessionDetail sessionDetail = new SessionDetail(roleNames, session);
		SocketHandler.sessionDetails.add(sessionDetail);
	}

	public void closeSession(WebSocketSession session) {
		try {
			session.close();
		} catch (IOException e) {
			log.info("Exception on close session:" + e.getMessage());
		}
	}

	public Boolean isMaxLimitExceed(String clientIP) throws IOException {

		String propFileName = "application.yaml";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		Properties prop = new Properties();
		prop.load(inputStream);

		Integer rateLimiting = Integer.parseInt(prop.getProperty("websocketRateLimiting"));

		Boolean isExceed = true;

		int currentSize = 0;

		for (SessionDetail e : sessionDetails) {

			if (e.getSession().getRemoteAddress().getAddress().getHostAddress().equals(clientIP)) {
				currentSize = currentSize + 1;
			}
		}

		if (rateLimiting > currentSize) {
			isExceed = false;
		}

		return isExceed;
	}

	public void sendMessage(String messagePayload, Set<String> roleName) {

		log.info("Entering in to sendMessage(): messagePayload" + messagePayload + ", roleName: " + roleName);

		roleName.forEach(e -> {
			sendMessage(messagePayload, e);
		});

	}

	public void sendMessage(String messagePayload, String roleName) {

		log.info("Entering in to sendMessage(): messagePayload" + messagePayload + ", roleName: " + roleName);

		sessionDetails.forEach(e -> {
			if (e.getRoles().contains(roleName)) {
				try {
					handleTextMessage(e.getSession(), new TextMessage(messagePayload));
				} catch (InterruptedException | IOException e1) {
					log.error("Exception occured in handleTextMessage():" + e1.getMessage());
				}
			}
		});
	}
}
