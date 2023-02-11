package com.roche.connect.dpcr.common.bl.hl7.mllp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MLLPClient {

    private static final Logger LOG = LoggerFactory.getLogger(MLLPClient.class);
    private MLLPReceiver receiver;
    private MLLPMessageHandler handler;
    private PrintWriter writer;
    private Socket socket;


    public MLLPClient() {
    	/*default constructor--sonar bugfix*/
    }
    
    public void response(StringBuilder msg) {
    	System.out.println(msg);
    }

    public void send(final String message) {
        if (isConnected()) {
            String wrapped = "\u000b" + message + "\u001c" + "\r";
            this.writer.write(wrapped);
            this.writer.flush();
        }
    }

    public void setOnMessageReceived(final MLLPMessageHandler handler) {
        this.handler = handler;
    }

    public boolean isConnected() {
        return (this.socket != null && this.socket.isConnected());
    }

    public void connect(final String server, final int port) throws IOException {
        this.socket = null;
        try {
            LOG.info("MLLP client connecting");

            InetAddress inetAddress = InetAddress.getByName(server);

            this.socket = new Socket(inetAddress, port);

            this.receiver = new MLLPReceiver("HL7 Reader");
            this.receiver.setOnMessageReceived(this.handler);
            this.receiver.setInputStream(this.socket.getInputStream());
            this.receiver.start();

            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (UnknownHostException ex) {
            LOG.error(ex.toString());
        } catch (IOException exp) {
            LOG.error(exp.toString());
        }
    }
    


    public void disconnect() throws InterruptedException {
        LOG.info("MLLP client disconnecting");

        if (this.receiver != null) {
            this.receiver.interrupt();
            this.receiver.join(1000);
            this.receiver = null;
        }

        if (this.writer != null) {
            this.writer.close();
            this.writer = null;
        }

        if (socket != null) {
            try {
                this.socket.close();
            } catch (IOException exae) {
                
                LOG.error(exae.toString());
            }

            this.socket = null;
        }
    }
}

