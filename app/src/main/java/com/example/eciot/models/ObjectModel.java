package com.example.eciot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ObjectModel extends RealmObject {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("clasificador")
    @Expose
    private int clasificador;
    @SerializedName("categoria")
    @Expose
    private int categoria;
    @SerializedName("fecha_registro")
    @Expose
    private String fechaRegistro;
    @SerializedName("peso")
    @Expose
    private double peso;
    @SerializedName("acerto")
    @Expose
    private boolean acerto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClasificador() {
        return clasificador;
    }

    public void setClasificador(int clasificador) {
        this.clasificador = clasificador;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public boolean isAcerto() {
        return acerto;
    }

    public void setAcerto(boolean acerto) {
        this.acerto = acerto;
    }

}
