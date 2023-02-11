package com.roche.swam.labsimulator.lis.bl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.roche.swam.labsimulator.common.bl.hl7.HL7Client;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.datatype.CE;
import ca.uhn.hl7v2.model.v251.message.OML_O33;
import ca.uhn.hl7v2.parser.Parser;


public class LisConnection {

    private static Logger LOGGER = LogManager.getLogger(LisConnection.class);

    private HapiContext context;
    private HL7Client client;
    
    private Boolean isEnabled;
    private Boolean isConnected;
    private LisOnConnectionStateChanged connectionStateChanged;

    @Autowired
    private LisOrderRepository orders;
    @Autowired
    private ApplicationEventPublisher publisher;

    public LisConnection() {
        this.context = new DefaultHapiContext();
        this.client = new HL7Client();
        this.client.setOnMessageReceived(m -> this.setOnMessageReceived(m));

        this.isEnabled = false;
        this.isConnected = false;

      

    }



    public boolean isConnected() {
        return this.client.isConnected();
    }





    public void downloadOpenOrders(){
        if (this.client.isConnected()){
            this.orders.getAllOpen().forEach(this::downloadOrder);
        }
    }

    private OML_O33 createDownloadOrderMessage() {
        try {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/OML_O33.txt");
            String msg = null;
                msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n','\r');
            Parser p = this.context.getPipeParser();
            OML_O33 orderDownloadMessage = (OML_O33)p.parse(msg);
     
            return orderDownloadMessage;
        } catch (IOException ioException) {
        	LOGGER.error(ioException.getLocalizedMessage());
        } catch (HL7Exception hl7Exception) {
        	LOGGER.error(hl7Exception.getLocalizedMessage());
        }
        return null;
    }

    private OML_O33 adjustMessage(Order openOrder, OML_O33 message) throws DataTypeException {
        message.getSPECIMEN().getSPM().getSpm2_SpecimenID()
                .getEip1_PlacerAssignedIdentifier()
                .getEi1_EntityIdentifier()
                .setValue(openOrder.getSampleId());

        message.getSPECIMEN().getSPM().getSpm3_SpecimenParentIDs(0)
                .getEip1_PlacerAssignedIdentifier()
                .getEi1_EntityIdentifier()
                .setValue("");

        message.getSPECIMEN().getORDER().getORC()
                .getOrc2_PlacerOrderNumber()
                .getEi1_EntityIdentifier()
                .setValue(openOrder.getOrderId());

        message.getSPECIMEN().getORDER().getOBSERVATION_REQUEST().getOBR()
                .getObr2_PlacerOrderNumber()
                .getEi1_EntityIdentifier()
                .setValue(openOrder.getOrderId());

        CE test = message.getSPECIMEN().getORDER().getOBSERVATION_REQUEST().getOBR()
                .getObr4_UniversalServiceIdentifier();
        test.getCe1_Identifier().setValue("135");
        test.getCe2_Text().setValue(openOrder.getTest());
        test.getCe3_NameOfCodingSystem().setValue("99ROC");

        message.getSPECIMEN().getORDER().getOBSERVATION_REQUEST().getOBR()
                .getObr29_Parent()
                .getEip1_PlacerAssignedIdentifier()
                .getEi1_EntityIdentifier()
                .setValue("");
        return message;
    }

    private void downloadOrder(Order openOrder)  {
        try {
            OML_O33 message = this.createDownloadOrderMessage();
            message = this.adjustMessage(openOrder, message);
            this.client.send(message);
            openOrder.setStatus(EnumOrderState.PENDING);
            this.publisher.publishEvent(new OrderChangedEvent(this, openOrder, EnumChange.CHANGED));
        } catch (HL7Exception hException) {
         	LOGGER.error(hException.getLocalizedMessage());
        }
    }

    public void updateConnectionState() {
        if (this.isConnected != this.client.isConnected()) {
            this.isConnected = this.client.isConnected();
            if(this.connectionStateChanged != null) {
                this.connectionStateChanged.ConnectionStateChanged(this.isConnected);
            }
        }

    }

    public boolean isEnabled(){
        return this.isEnabled;
    }

    public void enable()
    {
        this.isEnabled = true;
    }

    public void disable() {
        this.isEnabled = false;
    }

    public void setConnectionStateChanged(LisOnConnectionStateChanged connectionStateChanged) {
        this.connectionStateChanged = connectionStateChanged;
    }

    public void setOnMessageReceived(Message message) {
        LOGGER.info(message.toString());
    }
}
