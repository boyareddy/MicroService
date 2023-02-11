package com.roche.swam.labsimulator.common.bl.hl7;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.roche.swam.labsimulator.common.bl.hl7.mllp.MLLPClient;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;


public class HL7Client {

    private static final Logger LOG = LoggerFactory.getLogger(HL7Client.class);

    private MLLPClient client;
    private HL7MessageHandler handler;
    private HapiContext context;
    private Connection connection;
    private boolean connected;
    

    public HL7Client(){
        this.context = new DefaultHapiContext();
      
    }
    
    public Connection getConnection(){
    	return this.connection;
    }
    
    public HL7MessageHandler getMessageHandler() {
    	return this.handler;
    }

	public void messageReceived(Message message){
        try{
            /**Parser p = this.context.getPipeParser();*/
            if (this.handler != null) {
                this.handler.handleMessage(message);
            }
        }catch (Exception err){
            LOG.error(err.toString());
        }

    }

    public synchronized void send(Message message) throws HL7Exception, IOException {
        Parser p = this.context.getPipeParser();
        String encodedMessage = p.encode(message);
        this.client.send(encodedMessage);
    }

    public void setOnMessageReceived(HL7MessageHandler handler){
        this.handler = handler;
    }

    public boolean isConnected()
    {
        return this.connected;
    }

    public void startMP24Process(String server, int port) throws HL7Exception, JsonGenerationException, JsonMappingException, IOException {
    	this.connection = this.context.newClient(server, port, false);
    	this.connection.getInitiator().setTimeout(15000, TimeUnit.MILLISECONDS);
		this.connected = true;
    	
    }
    public void connect(String server, int port) throws HL7Exception {
    	this.connection = this.context.newClient(server, port, false);
		this.connected = true;
    }

    
    public void disconnect() {
        this.connection.close();
        this.connected = false;
    }



}