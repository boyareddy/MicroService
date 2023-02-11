package com.roche.connect.dpcr.util;


import java.io.IOException;
import java.util.TimerTask;

import com.roche.connect.dpcr.sim.bl.EsuMessageSender;

import ca.uhn.hl7v2.HL7Exception;

public class TaskScheduler extends TimerTask {
	EsuMessageSender esuSender = new EsuMessageSender();
    @Override public void run() {
        try {
			esuSender.run("PING");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}