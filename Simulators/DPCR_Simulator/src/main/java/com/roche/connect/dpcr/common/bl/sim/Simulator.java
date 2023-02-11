package com.roche.connect.dpcr.common.bl.sim;



public interface Simulator {
    EnumSimulatorStatus getStatus();

    void enable();

    void disable();

    void configure(final int id, final String type, final String name, final String serialNumber);

    int getId();

    String getName();

    String getSerial();

    String getType();

    void shutdown();
}
