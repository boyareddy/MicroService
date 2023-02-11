package com.roche.swam.labsimulator.common.server;


import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.roche.swam.labsimulator.common.bl.hl7.mllp.MLLPClient;


@Component
public class EmbeddedJetty {

	 private static final Logger LOG = LoggerFactory.getLogger(EmbeddedJetty.class);
    private Server server;

    public EmbeddedJetty() {
    	/*default constructor--sonar bugfix*/
    }
    
    public void startup() {
        
        this.server = new Server();

        DisableJettyVersionReporting(this.server);
        addRuntimeShutdownHook(this.server);
    }
    
    public Server getServer() { 
        return this.server;
    }
    
    public void start() {
        try {
            this.server.start();
        } catch (Exception ex) {
        	LOG.error(ex.getLocalizedMessage());
        }
    }
    
    public void shutdown() {
        try {
            this.server.stop();
        }
        catch (Exception exe) {
        	LOG.error(exe.getLocalizedMessage());
        }
    }

    private static void DisableJettyVersionReporting(Server server) {
        for (Connector conn : server.getConnectors()) {
            for (ConnectionFactory fact : conn.getConnectionFactories()) {
                if (fact instanceof HttpConnectionFactory) {
                    ((HttpConnectionFactory) fact).getHttpConfiguration().setSendServerVersion(false);
                }
            }
        }
    }

    private static void addRuntimeShutdownHook(Server server) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (server.isStarted()) {
                    server.setStopAtShutdown(true);
                    try {
                        server.stop();
                    } catch (Exception e) {
                    	LOG.error(e.getLocalizedMessage());
                    }
                }
            }
        }));
    }

    
}
