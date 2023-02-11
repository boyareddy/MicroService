package com.roche.connect.dpcr.engine.bl.config;

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
        "name",
        "type"
})
public class Equipment {

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("serialNumber")
    private String serialNumber;
    @JsonProperty("class")
    private String eqClass;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Equipment() {
    }

    /**
     *
     * @param name
     * @param type
     */
    public Equipment(final String name, final String type) {
        super();
        this.name = name;
        this.type = type;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    @JsonProperty("type")
    public void setType(final String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The serial number
     */
    @JsonProperty("serialNumber")
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     *
     * @param serialNumber
     * The serialNumber
     */
    @JsonProperty("serialNumber")
    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     *
     * @return
     * The class
     */
    @JsonProperty("class")
    public String getEqClass() {
        return this.eqClass;
    }

    /**
     *
     * @param eqClass
     * The class
     */
    @JsonProperty("class")
    public void setEqClass(final String eqClass) {
        this.eqClass = eqClass;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(final String name, final Object value) {
        this.additionalProperties.put(name, value);
    }
}
