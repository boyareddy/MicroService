package com.roche.swam.labsimulator.lis.bl;

import java.io.Serializable;

/**
 * Created by lustenm1 on 12/5/2016.
 */
public class Order implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3248660747350119465L;
	private String orderId;
    private String test;
    private String sampleId;
    private EnumOrderState status;
    
    public Order(String orderId, String sampleId, EnumOrderState state){
        this.orderId = orderId;
        this.sampleId = sampleId;
        this.status = state;
    }

    public Order(String orderId, String sampleId, String test, EnumOrderState state){
        this.orderId = orderId;
        this.sampleId = sampleId;
        this.test = test;
        this.status = state;
    }

    public EnumOrderState getStatus() {
        return status;
    }

    public void setStatus(EnumOrderState status) {
        this.status = status;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
