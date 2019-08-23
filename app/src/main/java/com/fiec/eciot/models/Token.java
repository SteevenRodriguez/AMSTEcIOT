package com.fiec.eciot.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Token extends RealmObject {
    @PrimaryKey
    private int id;

    @SerializedName("token")
    @Expose
    private String token;
    public Token(){
        this.id = new Date().hashCode();
    }

    public Token(String token) {
        this.id = new Date().hashCode();
        this.token = token;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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



