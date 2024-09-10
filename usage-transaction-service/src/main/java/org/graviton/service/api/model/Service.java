package org.graviton.service.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Service {
    private String name;
    @JsonProperty("charge")
    private int cost;

    public Service() {
    }

    public Service(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
