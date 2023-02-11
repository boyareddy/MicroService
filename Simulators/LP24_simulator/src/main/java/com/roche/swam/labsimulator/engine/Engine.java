package com.roche.swam.labsimulator.engine;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.swam.labsimulator.engine.bl.config.Equipment;
import com.roche.swam.labsimulator.engine.bl.config.LabsimulatorJsonConfig;
import com.roche.swam.labsimulator.MainApp;
import com.roche.swam.labsimulator.common.bl.sim.Simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class Engine {
	private static final Logger log = LoggerFactory.getLogger(Engine.class);

	//private final String CONFIG_FILE = "/config.json";
	private final String CONFIG_FILE = "/config.json";

	private List<Simulator> simulators;

	@Autowired
	private BeanFactory beanFactory;

	public Engine() {
		this.simulators = new ArrayList<>();
	}

	public List<Simulator> getAll() {
		return this.simulators;
	}

	public Simulator get(final int simulatorId) {
		return this.simulators.stream().filter(p -> p.getId() == simulatorId).findFirst().get();
	}

	public Simulator get(final String simulatorName) {
		
		return this.simulators.stream().filter(simulator -> simulator.getName().equalsIgnoreCase(simulatorName))
				.findFirst().get();
	}

	public List<Simulator> startup() throws IOException, ClassNotFoundException {
		int id = 0;

		InputStream stream = this.getClass().getResourceAsStream(CONFIG_FILE);

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory jsonFactory = mapper.getFactory();
		try (JsonParser jp = jsonFactory.createParser(stream)) {

			LabsimulatorJsonConfig jsonConfig = jp.readValueAs(LabsimulatorJsonConfig.class);
			log.info(jsonConfig.getLabsimulator().getEquipments().get(0).getName(), "jsonConfig");
			for (Equipment equipment : jsonConfig.getLabsimulator().getEquipments()) {
				String eqClass = equipment.getEqClass();
				Class<?> serviceClass = Class.forName(eqClass);
				Simulator simulator = (Simulator) this.beanFactory.getBean(serviceClass);
				this.simulators.add(simulator);
				simulator.configure(id, equipment.getType(), equipment.getName(), equipment.getSerialNumber());
				++id;
			}
			/** jp.close();--sonar bugfix */
			stream.close();
			return this.simulators;
		}
	}

	public void shutdown() {
		for (Simulator simulator : this.simulators) {
			simulator.shutdown();
		}
	}
}
