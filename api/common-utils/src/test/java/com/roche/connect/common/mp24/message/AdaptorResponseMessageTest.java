package com.roche.connect.common.mp24.message;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AdaptorResponseMessageTest {
    AdaptorResponseMessage adaptorResponseMessage;
    
    @BeforeTest
    public void setUp() {
        adaptorResponseMessage = new AdaptorResponseMessage();
    }
    
    
    @Test
    public void getAdaptorResponseMessageTest() {
        adaptorResponseMessage.setStatus("Passed");
        Response response = new Response();
        response.setAccessioningId("8001");
        adaptorResponseMessage.setResponse(response );
        List<String> error = new ArrayList<>();
        error.add("error");
        adaptorResponseMessage.setError(error);
        Assert.assertEquals("Passed", adaptorResponseMessage.getStatus());
        Assert.assertEquals("8001", adaptorResponseMessage.getResponse().getAccessioningId());
        Assert.assertEquals("error", adaptorResponseMessage.getError().get(0));
    }
}
