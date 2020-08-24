package com.example.blessing_model.pojo;

import java.io.Serializable;

public class Names implements Serializable {
    private String id;
    private String names;
    private String explanation;

    public Names(String id, String names, String explanation) {
        this.id = id;
        this.names = names;
        this.explanation = explanation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
