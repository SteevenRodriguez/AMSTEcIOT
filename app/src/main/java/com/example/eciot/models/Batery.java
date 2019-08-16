package com.example.eciot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Batery {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("thinxtra")
    @Expose
    private String thinxtra;
    @SerializedName("bateria")
    @Expose
    private int bateria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThinxtra() {
        return thinxtra;
    }

    public void setThinxtra(String thinxtra) {
        this.thinxtra = thinxtra;
    }

    public int getBatery() {
        return bateria;
    }

    public void setBatery(int bateria) {
        this.bateria = bateria;
    }

}