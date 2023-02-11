package com.roche.swam.labsimulator;

import org.springframework.stereotype.Component;

import com.roche.swam.labsimulator.engine.Engine;
@Component
public class InstanceUtil {

	static InstanceUtil instanceUtil;
	private Engine engine;
	
    public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public static synchronized  InstanceUtil getInstance() {
        if (InstanceUtil.instanceUtil == null) {
        	InstanceUtil.instanceUtil = new InstanceUtil();
        }
        return InstanceUtil.instanceUtil;
    }
}
