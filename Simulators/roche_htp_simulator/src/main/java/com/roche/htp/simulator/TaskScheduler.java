package com.roche.htp.simulator;

import java.util.TimerTask;

public class TaskScheduler extends TimerTask {
    HTPSequencerPanel htp = new HTPSequencerPanel();
    @Override public void run() {
        htp.ping();
    }
    
}
