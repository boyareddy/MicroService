package com.roche.connect.dpcr.sim.bl;

public enum SimulatorVariables {

	
        AA("AA"), AR("AR"),RFM("101"),UMT("200"),UEC("201"),UPI("202"),RFMD("Required Field Missing"),
        UMTD("Unsupported Message Type"),UECD("Unsupported event code"),UPID("Unsupported processing id"),FALSE("false");
        
        private final String text;
        
        SimulatorVariables(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
}
