package com.roche.swam.labsimulator.common.ui;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContext {

    @Autowired
    private ApplicationContext context;
    private static SpringContext instance;

   public SpringContext()
   {
       System.out.print("created");
       instance = this;
   }


    public ApplicationContext getApplicationContext() {
        return this.context;

    }

    public static Object getBean( Class aClass){
        if (instance != null){
            return instance.getApplicationContext().getBean(aClass);
        }
        return new Object();
    }
}