package com.roche.swam.labsimulator.common.bl.hl7;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.lis.bl.LisOnConnectionStateChanged;
import com.roche.swam.labsimulator.mpx.bl.EnumSampleStatus;
import com.roche.swam.labsimulator.mpx.bl.SampleRepository;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

@Component
@Scope("prototype")
public class HL7Connection  {

	private static Logger LOGGER = LogManager.getLogger(HL7Connection.class);

	private HL7Client client;
	private Boolean isEnabled;
	private Boolean isConnected;
	private LisOnConnectionStateChanged connectionStateChanged;
	private String host = MainApp.getFilepath("AdapterHostName");
	private int port = Integer.parseInt(MainApp.getFilepath("AdapterHostPort"));

	@Autowired
	private ApplicationEventPublisher publisher;

	private List<MessageSender> senders;
	private Map<Type, MessageHandler> handlers;

	public HL7Connection() {
		this.client = new HL7Client();
		this.client.setOnMessageReceived(m -> this.setOnMessageReceived(m));

		this.isEnabled = false;
		this.isConnected = false;

		
		this.senders = new ArrayList<>();
		this.handlers = new HashMap<>();

	}

	public void addSender(MessageSender sender) {
		this.senders.add(sender);
	}

	public <T extends Message> void addHandler(MessageHandler handler, Class<T> type) {
		this.handlers.put(type, handler);
	}

	public boolean isConnected() {
		return this.client.isConnected();
	}

	private void connectIfNeeded() {
		if (!this.client.isConnected()) {
			try {
				this.client.connect(this.host, this.port);
			} catch (HL7Exception e) {
				
				LOGGER.error(e.getLocalizedMessage());
			}
		}
	}

	
	public void run(MessageSender sender, SampleRepository samples, EnumSampleStatus sampleStatus) {
		try {
			this.connectIfNeeded();
			LOGGER.info(" HL7Connection conection status:" + this.client.isConnected());
			if (this.client.isConnected()) {
				sender.run(this.client,samples,sampleStatus);
			}
			this.updateConnectionState();
		} catch (Exception er) {
			LOGGER.error(er.getMessage());
		}

	}
	

	//ESU_U01
		public void run(MessageSender sender) { 
			try {
				this.connectIfNeeded();
				LOGGER.info(" HL7Connection conection status:" + this.client.isConnected());
				if (this.client.isConnected()) {
					sender.run(this.client);
				}
				this.updateConnectionState();
			} catch (Exception er) {
				LOGGER.error(er.getMessage());
			}

	}
	
	public void disconnect()
	{
		this.client.disconnect();
	}
	public void run(MessageSender sender, EnumSampleStatus sampleStatus) {
		try {
			this.connectIfNeeded();
			LOGGER.info(" HL7Connection conection status :" + this.client.isConnected());
			if (this.client.isConnected()) {
				sender.run(this.client,sampleStatus);
			}
			this.updateConnectionState();
		} catch (Exception err) {
			LOGGER.error(err.getMessage());
		}

	}

	private void updateConnectionState() {
		if (this.isConnected != this.client.isConnected()) {
			this.isConnected = this.client.isConnected();
			if (this.connectionStateChanged != null) {
				this.connectionStateChanged.ConnectionStateChanged(this.isConnected);
			}
		}

	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void enable() {
		this.isEnabled = true;
	}

	public void disable() {
		this.isEnabled = false;
	}

	public void setConnectionStateChanged(LisOnConnectionStateChanged connectionStateChanged) {
		this.connectionStateChanged = connectionStateChanged;
	}

	

	private void setOnMessageReceived(Message message) {
		LOGGER.info(message.toString());
		Type messageType = message.getClass();
		if (this.handlers.containsKey(messageType)) {
			try {
				this.handlers.get(messageType).handle(message, this.client);
			} catch (HL7Exception hl7Exception) {
				
				LOGGER.error(hl7Exception.getLocalizedMessage());
			} catch (IOException ioException) {
				
				LOGGER.error(ioException.getLocalizedMessage());
			}
		}
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
