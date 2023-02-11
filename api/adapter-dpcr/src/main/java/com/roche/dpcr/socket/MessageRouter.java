package com.roche.dpcr.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.dpcr.dto.MessageExchange;
import com.roche.dpcr.service.DeviceHandlerService;
import com.roche.dpcr.service.MessageHandlerService;
import com.roche.dpcr.service.MessageReaderAndParser;

import ca.uhn.hl7v2.HL7Exception;

@Service("messageRouter")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MessageRouter extends Thread {
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private MessageReaderAndParser messageReaderAndParser;

	@Autowired
	DeviceHandlerService deviceHandlerService;

	@Autowired
	MessageHandlerService messageHandlerService;

	@Value("${pas.login_entity}")
	private String loginEntity;

	@Value("${pas.authenticate_url}")
	private String loginUrl;

	@Value("${pas.audit_create_url}")
	private String auditUrl;

	protected Socket socket;

	public void setSocket(Socket clientSocket) {
		this.socket = clientSocket;
	}

	@Override
	public void run() {

		InputStream inp = null;
		BufferedReader brinp = null;
		DataOutputStream out = null;
		DataInputStream dataInput = null;
		try {
			inp = socket.getInputStream();
			dataInput = new DataInputStream(inp);
			brinp = new BufferedReader(new InputStreamReader(dataInput));
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			logger.error("Unable to get the input stream ", e.getMessage());
			return;
		}
		String message;
		MessageExchange messageExchange = new MessageExchange();
		while (true) {
			try {
				StringBuilder sb = new StringBuilder();
				messageExchange.setOut(out);
				messageExchange.setSocket(socket);
				while (((message = brinp.readLine()) != null) && (!message.trim().equals(""))) {
					if (message.trim().contains("MSH")) {
						sb = new StringBuilder();
						message = changeHl7Version(message, messageExchange);
					}
					sb.append("\n" + message);
				}
				String inMsg = sb.toString();
				logger.info("Incoming message from device : ", inMsg.trim());
				if (!inMsg.contains("MSH|")) {
					String acknowledgementMsg = "\u000b" + "ACK" + "\u001c" + "\r";
					messageExchange.getOut().write(acknowledgementMsg.getBytes());
					messageExchange.getOut().close();
					return;
				}
				if (StringUtils.isNotBlank(messageExchange.getSerialNo())) {
					String messageType = "";
					if (inMsg.trim().contains("|QBP^Z01|")) {
						messageType = "Workorderrequest";
					} else if (inMsg.trim().contains("|ESU^U01|")) {
						messageType = "Devicestateupdate";
					} else if (inMsg.trim().contains("|ACK^O21|")) {
						messageType = "Ackforworkorderresponse";
					} else if (inMsg.trim().contains("|ORU^R01|")) {
						messageType = "Resultupdates";
					}
					String remoteHost = (socket.getRemoteSocketAddress().toString()).split(":")[0];
					messageHandlerService.setIpAddress(remoteHost.substring(1));
					messageHandlerService.createAuditBean(messageType, inMsg.trim());
					updateDeviceSubStatus(inMsg.trim(), messageExchange);
					if (!deviceHandlerService.deviceValidation(socket, messageExchange)) {
						logger.error("The unregistered device causes connection drop..,device serial:"
								+ messageExchange.getSerialNo());
						throw new HL7Exception("The unregistered device causes connection drop");
					}
					messageReaderAndParser.readQueryMessage(inMsg.trim(), messageExchange);

				} else {
					throw new HL7Exception("Device serial number is empty");
				}
			} catch (IOException | HL7Exception | HMTPException e) {
				logger.error(e.getMessage());
				return;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	private String changeHl7Version(String message, MessageExchange messageExchange) {
		String[] msh = message.split("\\|");
		if (msh.length > 11) {
			messageExchange.setMsgVersion(msh[11]);
			msh[11] = "2.6";
			messageExchange.setDeviceType(msh[2]);
			messageExchange.setSerialNo(msh[3]);
			messageExchange.setMsgControlId(msh[9]);
			return String.join("|", msh);
		} else {
			return message;
		}
	}

	private void updateDeviceSubStatus(String message, MessageExchange messageExchange) {

		if (message.contains("|ESU^U01|")) {
			if (message.contains("|ID")) {
				messageExchange.setDeviceSubStatus("ID");
			} else if (message.contains("|OP")) {
				messageExchange.setDeviceSubStatus("OP");
			} else if (message.contains("|ES")) {
				messageExchange.setDeviceSubStatus("ES");
			} else {
				messageExchange.setDeviceSubStatus("");
			}
		} else {
			messageExchange.setDeviceSubStatus("");
		}
	}
}