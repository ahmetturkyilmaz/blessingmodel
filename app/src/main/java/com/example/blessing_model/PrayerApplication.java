package com.example.blessing_model;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PrayerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("blessing.realm").build();

        Realm.setDefaultConfiguration(config);    }
}
