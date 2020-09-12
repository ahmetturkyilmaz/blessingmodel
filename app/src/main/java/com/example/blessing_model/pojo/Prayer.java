package com.example.blessing_model.pojo;

import java.io.Serializable;




public class Prayer implements Serializable {
    private String sureId;
    private String prayerName;
    private String latinAlphabet;
    private String blessing;

    public Prayer() {
    }

    public Prayer(String sureId, String prayerName, String blessing) {
        this.sureId = sureId;
        this.prayerName = prayerName;
        this.blessing = blessing;
    }

    public Prayer(String sureId, String prayerName, String latinAlphabet, String blessing) {
        this.sureId = sureId;
        this.prayerName = prayerName;
        this.latinAlphabet = latinAlphabet;
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

    public String getLatinAlphabet() {
        return latinAlphabet;
    }

    public void setLatinAlphabet(String latinAlphabet) {
        this.latinAlphabet = latinAlphabet;
    }
}
