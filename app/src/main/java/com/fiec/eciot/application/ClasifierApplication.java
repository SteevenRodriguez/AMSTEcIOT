package com.fiec.eciot.application;

import android.app.Application;

import io.realm.Realm;

public class ClasifierApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

}
