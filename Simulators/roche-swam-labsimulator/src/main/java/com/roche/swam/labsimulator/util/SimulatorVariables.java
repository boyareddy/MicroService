package com.roche.swam.labsimulator.util;

public enum SimulatorVariables {

	
        PASSED("Passed"),FAILED("Failed"),PASSED_WITH_FLAG("Passed with flag"),ABORTED("Aborted");
        
        private final String text;
        
        SimulatorVariables(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
}
