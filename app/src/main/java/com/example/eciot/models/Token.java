package com.example.eciot.models;

import io.realm.Realm;
import io.realm.RealmObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token extends RealmObject {

    @SerializedName("token")
    @Expose
    private String token;

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



