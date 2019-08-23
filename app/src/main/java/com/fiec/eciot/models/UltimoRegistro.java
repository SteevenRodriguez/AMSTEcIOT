package com.fiec.eciot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UltimoRegistro {

    @SerializedName("ultimo_registro")
    @Expose
    private ObjectModel ultimoRegistro;

    public ObjectModel getUltimoRegistro() {
        return ultimoRegistro;
    }

    public void setUltimoRegistro(ObjectModel ultimoRegistro) {
        this.ultimoRegistro = ultimoRegistro;
    }

}
