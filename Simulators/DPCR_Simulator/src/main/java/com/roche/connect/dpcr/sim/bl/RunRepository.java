package com.roche.connect.dpcr.sim.bl;


import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RunRepository {

    int id = 0;

    private Map<String, Run> runs;

    public RunRepository(){
        this.runs = new HashMap();
    }

    public Run create() {
        id++;
        Run newRun = new Run(String.format("%05d", id));
        runs.put(newRun.getId(),newRun);
        return newRun;
    }

    public Run getRun(String id) {
        return this.runs.get(id);
    }
}
