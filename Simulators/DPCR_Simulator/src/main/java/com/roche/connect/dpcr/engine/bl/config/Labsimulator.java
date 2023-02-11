package com.roche.connect.dpcr.engine.bl.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "equipments"
})
public class Labsimulator {

    @JsonProperty("equipments")
    private List<Equipment> equipments = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Labsimulator() {
    }

    /**
     *
     * @param equipments
     */
    public Labsimulator(List<Equipment> equipments) {
        super();
        this.equipments = equipments;
    }

    /**
     *
     * @return
     * The equipments
     */
    @JsonProperty("equipments")
    public List<Equipment> getEquipments() {
        return equipments;
    }

    /**
     *
     * @param equipments
     * The equipments
     */
    @JsonProperty("equipments")
    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
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