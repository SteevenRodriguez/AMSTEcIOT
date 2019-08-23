package com.fiec.eciot.models;

import io.realm.Realm;
import io.realm.RealmObject;

public class User extends RealmObject {

    private String username;

    private String password;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
