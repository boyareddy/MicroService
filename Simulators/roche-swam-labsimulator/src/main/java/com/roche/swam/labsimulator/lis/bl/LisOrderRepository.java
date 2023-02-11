package com.roche.swam.labsimulator.lis.bl;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LisOrderRepository {
    private List<Order> orders;

    public LisOrderRepository() {
        this.orders = new ArrayList<>();
    }

    public List<Order> getAll(){
        return Collections.unmodifiableList(this.orders);
    }

    public List<Order> getAllOpen() {
        List<Order> openOrders = this.getAll().stream()
                .filter( o -> o.getStatus() == EnumOrderState.OPEN)
                .collect(Collectors.toList());
        return openOrders;
    }

    public void add(Order order){
        this.orders.add(order);
    }

}
