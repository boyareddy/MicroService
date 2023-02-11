package com.roche.connect.dpcr.common.bl.sim;


import org.springframework.context.ApplicationEvent;

public class SimulatorStateChangedEvent extends ApplicationEvent {

    private int id;

    public SimulatorStateChangedEvent(final int id){
        super(new Object());
        this.id = id;
    }

    public int getId() {
        return id;
    }
}


