package com.example.blessing_model.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.blessing_model.R;
import com.example.blessing_model.pojo.Names;
import com.example.blessing_model.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ContinueDhikrForNames extends AppCompatActivity {
    HashMap<Integer, String> countForNames;
    JsonUtil jsonUtil;
    HashMap<Integer, String> descMap = null;
    HashMap<Integer, String> namesMap = null;
    ArrayList<Names> names;
    ArrayList<Names> newList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_dhikr_for_names);
    }

    private void loadDataForNames() {
        SharedPreferences sharedPreferences = getSharedPreferences("namesPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("names", null);

        Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        countForNames = gson.fromJson(json, type);
        if (countForNames == null) {
            countForNames = new HashMap<>();
            for (int i = 1; i < 99; i++) {
                countForNames.put(i, "0");
            }
        }
    }

    public HashMap<Integer, String> getNames(String language, String getObject) {
        HashMap<Integer, String> surahMap = new HashMap<>();
        try {
            JSONObject obj = new JSONObject(jsonUtil.loadJSONFromAsset(this, "namesOfAllah_" + language + ".json"));
            JSONArray data = obj.getJSONArray("data");
            for (int i = 0; i < 99; i++) {
                JSONObject surahInfo = data.getJSONObject(i);
                String surahName = (String) surahInfo.get(getObject);
                surahMap.put(i, surahName);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return surahMap;
    }

}