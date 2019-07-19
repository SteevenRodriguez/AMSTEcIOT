package com.example.eciot.models;

public class ObjectModel {

    private String type;

    private Float weight;

    private Boolean status;


    public ObjectModel(){}

    public ObjectModel(String type, Float weight, Boolean status) {
        this.type = type;
        this.weight = weight;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
