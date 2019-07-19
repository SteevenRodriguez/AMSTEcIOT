package com.example.eciot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Device extends RealmObject {
    @PrimaryKey
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

    public int getBateria() {
        return bateria;
    }

    public void setBateria(int bateria) {
        this.bateria = bateria;
    }

}
