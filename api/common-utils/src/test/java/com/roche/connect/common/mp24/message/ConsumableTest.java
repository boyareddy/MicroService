package com.roche.connect.common.mp24.message;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ConsumableTest {
    Consumable consumable;
    
    @BeforeTest
    public void setUp() {
        consumable = new Consumable(); 
    }
    
    @Test
    public void getConsumableTest() {
        consumable.setName("attributeName");
        consumable.setValue("attributeValue");
        Assert.assertEquals("attributeName", consumable.getName());
        Assert.assertEquals("attributeValue", consumable.getValue());
    }
}
