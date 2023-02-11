package com.roche.swam.labsimulator.lis;

import com.roche.swam.labsimulator.lis.bl.*;
import com.roche.swam.labsimulator.common.bl.sim.EnumSimulatorStatus;
import com.roche.swam.labsimulator.common.bl.sim.Simulator;
import com.roche.swam.labsimulator.common.bl.sim.SimulatorStateChangedEvent;
import com.roche.swam.labsimulator.common.bl.lab.SampleIdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class LisSimulator implements Simulator {

    @Autowired
    private SampleIdGenerator sampleIdGenerator;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private OrderIdGenerator orderIdGenerator;
    @Autowired
    private LisOrderRepository orders;
    @Autowired
    private SampleIdProvider sampleIdProvider;
    private EnumSimulatorStatus status;
    private LisConnection connection;
    
    private int id;
    private String type;
    private String name;
    private String serial;


    @Autowired(required = true)
    public LisSimulator(LisConnection connection) {
        this.status = EnumSimulatorStatus.DISABLED;
        this.connection = connection;
        this.connection.setConnectionStateChanged(b -> this.onConnectionStateChanged(b));
    }

    private void onConnectionStateChanged(boolean isConnected) {
        this.updateState();
    }

    private EnumSimulatorStatus calculateState() {
        if (this.connection.isEnabled()) {
            if (this.connection.isConnected()) {
                return EnumSimulatorStatus.CONNECTED;
            }
            return EnumSimulatorStatus.IDLE;
        }
        return EnumSimulatorStatus.DISABLED;
    }

    private void updateState() {
        EnumSimulatorStatus newState = this.calculateState();
        if (this.status != newState) {
            this.status = newState;
            this.raiseUpdate();
        }

    }

    private void raiseOrderCratedEvent(Order order, EnumChange changeType) {
        this.publisher.publishEvent(new OrderChangedEvent(this, order, changeType));
    }


    public void createOrders(int number, List<String> tests) {
        for (int i = 0; i < number; i++) {

            String test = tests.get(i % tests.size());
            String sampleId = this.sampleIdGenerator.getNext();
            String orderId = this.orderIdGenerator.getNext();

            Order order = new Order(orderId, sampleId, test, EnumOrderState.OPEN);
            this.sampleIdProvider.initSample(sampleId);
            this.orders.add(order);
            this.raiseOrderCratedEvent(order, EnumChange.CREATED);
        }
    }

    public List<Order> getOrders() {
        return this.orders.getAll();
    }


    private void raiseUpdate() {
        this.publisher.publishEvent(new SimulatorStateChangedEvent(this.getId()));
    }


    public void enable() {
        this.connection.enable();
        this.updateState();

    }

    public void disable() {
        this.connection.disable();
        this.updateState();
    }

    @Override
    public String getName() {
        return this.name;
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
//        this.connection.close();
    }


    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void configure(final int id, final String type, final String instName, final String instSerialNum) {
        this.id = id;
        this.type = type;
        this.name = instName;
        this.serial = instSerialNum;
        this.raiseUpdate();
    }

    @Override
    public EnumSimulatorStatus getStatus() {
        return this.status;
    }

}
