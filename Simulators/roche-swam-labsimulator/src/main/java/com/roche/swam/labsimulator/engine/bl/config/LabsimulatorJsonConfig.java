package com.roche.swam.labsimulator.engine.bl.config;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "labsimulator"
})
public class LabsimulatorJsonConfig {

    @JsonProperty("labsimulator")
    private Labsimulator labsimulator;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public LabsimulatorJsonConfig() {
    }

    /**
     *
     * @param labsimulator
     */
    public LabsimulatorJsonConfig(Labsimulator labsimulator) {
        super();
        this.labsimulator = labsimulator;
    }

    /**
     *
     * @return
     * The labsimulator
     */
    @JsonProperty("labsimulator")
    public Labsimulator getLabsimulator() {
        return labsimulator;
    }

    /**
     *
     * @param labsimulator
     * The labsimulator
     */
    @JsonProperty("labsimulator")
    public void setLabsimulator(Labsimulator labsimulator) {
        this.labsimulator = labsimulator;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
