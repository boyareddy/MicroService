package com.roche.connect.dpcr.sim.bl;

public class Consumables {

	
	private String consumableName;
	
	private String value;

	public String getConsumableName() {
		return consumableName;
	}

	public void setConsumableName(String consumableName) {
		this.consumableName = consumableName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Consumables [consumableName=" + consumableName + ", value=" + value + "]";
	}
	
	
	
	
}
