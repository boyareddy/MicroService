package com.roche.connect.dpcr.sim.bl;

import java.util.Calendar;

/**
 * Created by bruce on 12/18/16.
 */
public class Run {
    private String id;
    private Calendar startTime;
    private Calendar endTime;

    public Run(String id) {
        this.startTime = Calendar.getInstance();
        this.id = id;
    }

    public void finish(){
        this.endTime = Calendar.getInstance();
    }

    public String getId() {
        return this.id;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }
}

