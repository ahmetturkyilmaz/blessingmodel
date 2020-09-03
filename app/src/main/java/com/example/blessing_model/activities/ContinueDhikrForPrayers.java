package com.example.blessing_model.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.blessing_model.R;
import com.example.blessing_model.adapters.PrayerAdapter;
import com.example.blessing_model.pojo.Prayer;
import com.example.blessing_model.util.ItemClickSupport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ContinueDhikrForPrayers extends AppCompatActivity {
    HashMap<Integer, String> countForPrayers;
    private ArrayList<Prayer> prayers;
    private ArrayList<Prayer> newList;
    private PrayerAdapter adapter;
    HashMap<Integer, String> prayerNames;
    HashMap<Integer, String> prayerMap;
    HashMap<Integer, String> prayerLatinMap;
    RecyclerView recyclerView;
    Toolbar toolbar;
    Intent prayerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_dhikr);
        prayerNames = null;
        prayerMap = null;
        prayerLatinMap = null;
        loadDataForPrayers();
        defineMapsForLanguages();


        recyclerView = (RecyclerView) findViewById(R.id.continueDhikrPrayersListView);

        toolbar = findViewById(R.id.continueDhikrPrayersToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newList = new ArrayList<>();
        for (int i =1; i < 115; i++) {
            String counter = countForPrayers.get(i);
            if (Integer.parseInt(counter) != 0) {
                Prayer prayer = prayers.get(i-1);
                newList.add(prayer);
            }
        }

        adapter = new PrayerAdapter(newList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                prayerIntent = new Intent(getApplicationContext(), PrayerActivity.class);
                prayerIntent.putExtra("id", newList.get(position));
                startActivity(prayerIntent);

            }
        });
    }

    private void defineMapsForLanguages() {
        if (prayers == null) {
            prayers = new ArrayList<>();
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if (language.equals("en")) {
                prayerMap = getPrayers("eng.json", "en.pickthall");
                prayerNames = getSurahNames("englishName");
            } else if (language.equals("tr")) {
                prayerMap = getPrayers("newTR.json", "tr.diyanet", "verse");
                prayerLatinMap = getPrayers("newTR.json", "tr.diyanet", "Latin");
                prayerNames = getSurahNames("turkishName");
            } else {
                prayerMap = getPrayers("arab.json", "ar.muyassar");
                prayerNames = getSurahNames("englishName");
            }
            for (int i = 0; i < 114; i++) {
                if (prayerLatinMap != null) {
                    prayers.add(new Prayer(String.valueOf(i + 1), prayerNames.get(i), prayerLatinMap.get(i + 1), prayerMap.get(i + 1)));

                } else {
                    prayers.add(new Prayer(String.valueOf(i + 1), prayerNames.get(i), prayerMap.get(i + 1)));
                }
            }
        }
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public HashMap<Integer, String> getPrayers(String fileName, String translator) {
        HashMap<Integer, String> surahMap = new HashMap<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(fileName));
            JSONObject quran = obj.getJSONObject("quran");
            JSONObject publisher = quran.getJSONObject(translator);
            for (int i = 1; i <= 6236; i++) {
                JSONObject ayah = publisher.getJSONObject(String.valueOf(i));
                Integer surahNumber = (Integer) ayah.get("surah");
                String ayahItself = (String) ayah.get("verse");
                if (!(surahMap.get(surahNumber) == null)) {
                    String newSurahString = surahMap.get(surahNumber).concat(" ").concat(ayahItself);

                    surahMap.put(surahNumber, newSurahString);
                } else {
                    surahMap.put(surahNumber, ayahItself);
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return surahMap;
    }

    public HashMap<Integer, String> getPrayers(String fileName, String translator, String alphabet) {
        HashMap<Integer, String> surahMap = new HashMap<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(fileName));
            JSONObject quran = obj.getJSONObject("quran");
            JSONObject publisher = quran.getJSONObject(translator);
            for (int i = 1; i <= 6236; i++) {
                JSONObject ayah = publisher.getJSONObject(String.valueOf(i));
                Integer surahNumber = (Integer) ayah.get("surah");
                String ayahItself = (String) ayah.get(alphabet);
                if (!(surahMap.get(surahNumber) == null)) {
                    String newSurahString = surahMap.get(surahNumber).concat(" ").concat(ayahItself);

                    surahMap.put(surahNumber, newSurahString);
                } else {
                    surahMap.put(surahNumber, ayahItself);
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return surahMap;
    }

    public HashMap<Integer, String> getSurahNames(String languages) {
        HashMap<Integer, String> surahMap = new HashMap<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset("quran.json"));
            JSONArray data = obj.getJSONArray("data");
            for (int i = 0; i <= 114; i++) {
                JSONObject surahInfo = data.getJSONObject(i);
                String surahName = (String) surahInfo.get(languages);
                surahMap.put(i, surahName);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return surahMap;
    }

    private void loadDataForPrayers() {
        SharedPreferences sharedPreferences = getSharedPreferences("prayerPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("prayers", null);

        Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        countForPrayers = gson.fromJson(json, type);
    }

}