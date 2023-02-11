package com.roche.dpcr.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

@Lazy
@Service
public class SocketConfiguration {

	@Value("${socket.port}")
	private String port;

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private ApplicationContext applicationContext;

	private MessageRouter getMessageRouter() {
		return (MessageRouter) applicationContext.getBean("messageRouter");
	}

	public SocketConfiguration() {
		logger.info("Server Started... ");
	}

	public void initializeSocket() throws IOException {
		Socket socket = null;

		try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port))) {
			while (true) {
				socket = serverSocket.accept();
				MessageRouter messageRouter = getMessageRouter();
				messageRouter.setSocket(socket);
				messageRouter.start();
			}
		} catch (IOException e) {
			logger.error("Error at initializeSocket()" + e.getMessage());

		}

	}

	@PostConstruct
	public void init() throws IOException {
		initializeSocket();
	}
}
