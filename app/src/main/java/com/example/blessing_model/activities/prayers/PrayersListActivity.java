package com.example.blessing_model.activities.prayers;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import com.google.android.gms.common.util.NumberUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PrayersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView loadingView;

    private RecyclerView.LayoutManager layoutManager;
    private PrayerAdapter adapter;
    private ArrayList<Prayer> prayers;
    private ArrayList<Prayer> newList = null;
    private Intent intent;
    private HashMap<Integer, String> prayerNames;
    private HashMap<Integer, String> prayerMap;
    private HashMap<Integer, String> prayerLatinMap;
    private List<String> imageNumbs;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            defineMapsForLanguages();
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayers_list);

        recyclerView = findViewById(R.id.sure_page);
        loadingView = findViewById(R.id.loadingView);

        Toolbar toolbar = findViewById(R.id.prayersListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new PrayerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                intent = new Intent(getApplicationContext(), PrayerActivity.class);
                if (newList != null && !newList.isEmpty()) {
                    intent.putExtra("id", newList.get(position));
                } else {
                    intent.putExtra("id", prayers.get(position));
                }
                startActivity(intent);
            }
        });
        handler.post(runnable);
    }

    private void defineMapsForLanguages() {
        prayerNames = null;
        prayerMap = null;
        prayerLatinMap = null;

        if (prayers == null) {
            loadingView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

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

            HashMap<String, List<String>> imageMap = fillImageNumbers(imageNumbs);

            for (int i = 0; i < 114; i++) {
                if (prayerLatinMap != null) {
                    prayers.add(new Prayer(String.valueOf(i + 1), prayerNames.get(i), prayerLatinMap.get(i + 1), prayerMap.get(i + 1), imageMap.get(String.valueOf(i + 1))));
                } else {
                    prayers.add(new Prayer(String.valueOf(i + 1), prayerNames.get(i), prayerMap.get(i + 1), imageMap.get(String.valueOf(i + 1))));
                }
            }
            adapter.setPrayers(prayers);
            loadingView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
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
            int ayahCounter = 1;
            imageNumbs = new ArrayList<>();

            for (int i = 1; i <= 6236; i++) {
                JSONObject ayah = publisher.getJSONObject(String.valueOf(i));
                Integer surahNumber = (Integer) ayah.get("surah");
                String ayahItself = (String) ayah.get("verse");
                StringBuilder stringBuilder = new StringBuilder();
                if (!(surahMap.get(surahNumber) == null)) {
                    String newSurahString = surahMap.get(surahNumber).concat(" ").concat(ayahItself);
                    surahMap.put(surahNumber, newSurahString);
                    ayahCounter++;
                } else {
                    surahMap.put(surahNumber, ayahItself);
                    ayahCounter = 1;
                }
                stringBuilder.append(surahNumber).append("_").append(ayahCounter).append(".").append("png");
                imageNumbs.add(stringBuilder.toString());
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

            int ayahCounter = 1;
            imageNumbs = new ArrayList<>();

            for (int i = 1; i <= 6236; i++) {
                JSONObject ayah = publisher.getJSONObject(String.valueOf(i));
                Integer surahNumber = (Integer) ayah.get("surah");
                String ayahItself = (String) ayah.get(alphabet);


                StringBuilder stringBuilder = new StringBuilder();

                if (!(surahMap.get(surahNumber) == null)) {
                    String newSurahString = surahMap.get(surahNumber).concat(" ").concat(ayahItself);

                    surahMap.put(surahNumber, newSurahString);

                    ayahCounter++;

                } else {
                    surahMap.put(surahNumber, ayahItself);

                    ayahCounter = 1;
                }
                stringBuilder.append(surahNumber).append("_").append(ayahCounter).append(".").append("png");
                imageNumbs.add(stringBuilder.toString());
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return surahMap;
    }

    private HashMap<String, List<String>> fillImageNumbers(List<String> imageNumbers) {
        HashMap<String, List<String>> surahNumbImageNumbMap = new HashMap<>();
        ArrayList<String> newList = new ArrayList<>();
        for (String numb : imageNumbers) {
            StringBuilder stringBuilder = new StringBuilder(numb);
            String surahNumb = stringBuilder.substring(0, 1);
            if (!stringBuilder.substring(1, 2).equals("_")) {
                surahNumb = stringBuilder.substring(0, 2);
            }

            if (stringBuilder.substring(3, 4).equals("_")) {
                surahNumb = stringBuilder.substring(0, 3);
            }
            if (surahNumbImageNumbMap.get(surahNumb) == null) {
                newList = new ArrayList<>();
            }
            newList.add(numb);
            surahNumbImageNumbMap.put(surahNumb, newList);
        }

        return surahNumbImageNumbMap;

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
