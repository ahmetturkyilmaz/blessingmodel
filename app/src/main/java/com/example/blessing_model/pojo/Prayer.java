package com.example.blessing_model.pojo;

import java.io.Serializable;
import java.util.List;


public class Prayer implements Serializable {
    private String sureId;
    private String prayerName;
    private String latinAlphabet;
    private String blessing;
    private List<String> imageNumbs;

    public Prayer() {
    }

    public Prayer(String sureId, String prayerName, String blessing, List<String> imageNumbs) {
        this.sureId = sureId;
        this.prayerName = prayerName;
        this.blessing = blessing;
        this.imageNumbs = imageNumbs;

    }

    public Prayer(String sureId, String prayerName, String latinAlphabet, String blessing) {
        this.sureId = sureId;
        this.prayerName = prayerName;
        this.latinAlphabet = latinAlphabet;
        this.blessing = blessing;
    }

    public Prayer(String sureId, String prayerName, String latinAlphabet, String blessing, List<String> imageNumbs) {
        this.sureId = sureId;
        this.prayerName = prayerName;
        this.latinAlphabet = latinAlphabet;
        this.blessing = blessing;
        this.imageNumbs = imageNumbs;
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

    public List<String> getImageNumbs() {
        return imageNumbs;
    }

    public void setImageNumbs(List<String> imageNumbs) {
        this.imageNumbs = imageNumbs;
    }
}
