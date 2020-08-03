package com.example.blessing_model.pojo;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class Prayer extends RealmObject implements Serializable {
    private String sureId;
    private String prayerName;
    private String blessing;

    public Prayer() {
    }

    public Prayer(String sureId, String prayerName, String blessing) {
        this.sureId = sureId;
        this.prayerName = prayerName;
        this.blessing = blessing;
    }

    public String getSureId() {
        return sureId;
    }

    public void setSureId(String sureId) {
        this.sureId = sureId;
    }

    public String getPrayerName() {
        return prayerName;
    }

    public void setPrayerName(String prayerName) {
        this.prayerName = prayerName;
    }

    public String getBlessing() {
        return blessing;
    }

    public void setBlessing(String blessing) {
        this.blessing = blessing;
    }

}
