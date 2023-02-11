package com.roche.swam.labsimulator.common.bl.sim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import com.roche.swam.labsimulator.common.bl.hl7.HL7Connection;


public class BasicHL7Simulator implements Simulator {

    private String type;
    private String name;
    private int id;
    private String serial;

    @Autowired
    protected ApplicationEventPublisher publisher;
    @Autowired
    protected HL7Connection connection;
    protected EnumSimulatorStatus status;

    public BasicHL7Simulator() {
        this.status = EnumSimulatorStatus.DISABLED;
    }

    private void raiseUpdate() {
        this.publisher.publishEvent(
                new SimulatorStateChangedEvent(
                        this.getId()));
    }

    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void configure(final int id, final String simType, final String name, final String serialNumber) {
        this.id = id;
        this.name = name;
        this.type = simType;
        this.serial = serialNumber;
       // this.connection.setConnectionStateChanged(this::connectionStateChanged);
    }

    private void connectionStateChanged(final boolean b) {
        this.updateState();
    }

    @Override
    public EnumSimulatorStatus getStatus() {
        return this.status;
    }

    @Override
    public void enable() {
        this.connection.enable();
        this.updateState();
    }

    @Override
    public void disable() {
        this.connection.disable();
        this.updateState();
    }

    @Override
    public String getSerial() {
        return this.serial;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void shutdown() {
        this.connection.disable();
    }

    protected EnumSimulatorStatus calculateState() {
        if (this.connection.isEnabled()) {
            if (this.connection.isConnected()) {
                return EnumSimulatorStatus.CONNECTED;
            }
            return EnumSimulatorStatus.IDLE;
        }
        return EnumSimulatorStatus.DISABLED;
    }

    protected void updateState() {
        EnumSimulatorStatus newState = this.calculateState();
        if (this.status != newState) {
            this.status = newState;
            this.raiseUpdate();
        }
    }
}
