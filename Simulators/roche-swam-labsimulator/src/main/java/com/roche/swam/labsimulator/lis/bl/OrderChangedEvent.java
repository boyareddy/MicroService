package com.roche.swam.labsimulator.lis.bl;

import org.springframework.context.ApplicationEvent;

/**
 * Created by lustenm1 on 12/5/2016.
 */
public class OrderChangedEvent extends ApplicationEvent {


    private Order order;
    private EnumChange changeType;

    public OrderChangedEvent(Object source, Order order, EnumChange change) {
        super(source);

        this.order = order;
        this.changeType = change;
    }

    public Order getOrder() {
        return order;
    }

    public EnumChange getChangeType() {
        return changeType;
    }
}
