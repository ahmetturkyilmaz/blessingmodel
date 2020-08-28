package com.example.blessing_model.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing_model.util.ItemClickSupport;
import com.example.blessing_model.pojo.Prayer;
import com.example.blessing_model.adapters.PrayerAdapter;
import com.example.blessing_model.R;
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


public class PrayersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    PrayerAdapter adapter;
    ArrayList<Prayer> prayers;
    ArrayList<Prayer> newList = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayers_list);
        recyclerView = (RecyclerView) findViewById(R.id.sure_page);

        Toolbar toolbar = findViewById(R.id.prayersListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        HashMap<Integer, String> prayerNames = null;
        HashMap<Integer, String> prayerMap = null;
        HashMap<Integer, String> prayerLatinMap = null;
        if (prayers == null) {
            prayers = new ArrayList<>();
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if (language.equals("en")) {
                prayerMap = getPrayers("eng.json", "en.pickthall");
                prayerNames = getSurahNames("englishName");
            } else if (language.equals("tr")) {
                prayerMap = getPrayers("tr.json", "tr.diyanet", "verse");
                prayerLatinMap = getPrayers("tr.json", "tr.diyanet", "Latin");
                prayerNames = getSurahNames("turkishName");
            } else {
                prayerMap = getPrayers("arab.json", "ar.muyassar");
                prayerNames = getSurahNames("englishName");
            }
            for (int i = 0; i < 114; i++) {
                if (prayerLatinMap != null) {
                    prayers.add(new Prayer(String.valueOf(i + 1), prayerNames.get(i), prayerLatinMap.get(i), prayerMap.get(i + 1)));

                }
                prayers.add(new Prayer(String.valueOf(i + 1), prayerNames.get(i), prayerMap.get(i + 1)));

            }
        }
        adapter = new PrayerAdapter(prayers);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getApplicationContext(), PrayerActivity.class);
                if (newList != null && !newList.isEmpty()) {
                    intent.putExtra("id", newList.get(position));
                } else {
                    intent.putExtra("id", prayers.get(position));
                }
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search_item);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                newList = new ArrayList<>();
                for (Prayer prayer : prayers) {
                    String title = prayer.getPrayerName().toLowerCase();
                    if (title.contains(newText)) {
                        newList.add(prayer);
                    }
                }
                //create method in adapter
                adapter.setFilter(newList);
                if (newText.isEmpty()) {
                    newList = new ArrayList<>();
                }
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
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
}
