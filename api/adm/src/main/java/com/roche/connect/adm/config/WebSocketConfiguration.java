package com.roche.connect.adm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.common.adm.notification.AdmNotificationService;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

	private static final String SOCKET_URL = "/notification";

	private static HMTPLogger logger = HMTPLoggerFactory.getLogger(AdmNotificationService.class.getName());

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		logger.info("Entering in to registerWebSocketHandlers:" + registry);

		RequestUpgradeStrategy upgradeStrategy = new JettyRequestUpgradeStrategy();

		registry.addHandler(new SocketHandler(), SOCKET_URL).setAllowedOrigins("*")
				.setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy));

	}

}
