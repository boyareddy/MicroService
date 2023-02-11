package com.roche.connect.dpcr.common.bl.hl7.mllp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MLLPReceiver extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(MLLPReceiver.class);
	private InputStream inputStream;
	private MLLPMessageHandler handler;
	private StringBuilder msgBuilder;

	public MLLPReceiver(String str) {
		super(str);
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setOnMessageReceived(MLLPMessageHandler handler) {
		this.handler = handler;
	}

	private String unwrapMessage(String message) {
		return message.substring(1, message.length() - 2);
	}

	private void messageFound(String message) {
		if (this.handler != null) {
			try {
				String unwrapMessage = this.unwrapMessage(message);
				this.handler.handleMesage(unwrapMessage);
			} catch (Exception err) {
				LOG.info(err.toString());
			}
		}
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(inputStream);
			BufferedReader in = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while (!this.isInterrupted()) {
				while ((line = in.readLine()) != null) {
					sb.append(line);
					sb.append('\r');
					if (line.compareTo("\u001C") == 0) {
						this.messageFound(sb.toString());

					}
					if (in.ready() == false) {
						msgBuilder = sb;
						new MLLPClient().response(sb);
						sb = new StringBuilder();

						break;
					}

				}
			}

		} catch (Exception e) {
			LOG.info(e.toString());
		}
	}

	public StringBuilder getMsgBuilder() {
		return msgBuilder;
	}


}
