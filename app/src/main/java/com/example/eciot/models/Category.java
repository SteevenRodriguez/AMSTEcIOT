package com.example.eciot.models;


import io.realm.Realm;
import io.realm.RealmObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category extends RealmObject{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean save() {
        boolean isSuccess = true;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(this);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            realm.close();
        }

        return isSuccess;
    }


}
