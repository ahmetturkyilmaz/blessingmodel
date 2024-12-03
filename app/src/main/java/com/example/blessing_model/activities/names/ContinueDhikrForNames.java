package com.example.blessing_model.activities.names;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.blessing_model.R;
import com.example.blessing_model.activities.names.NameActivity;
import com.example.blessing_model.adapters.NamesAdapter;
import com.example.blessing_model.pojo.Names;
import com.example.blessing_model.util.ItemClickSupport;
import com.example.blessing_model.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ContinueDhikrForNames extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    NamesAdapter adapter;

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
        recyclerView = findViewById(R.id.continueDhikrNamesListView);
        Toolbar toolbar = findViewById(R.id.continueDhikrNamesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadDataForNames();
        defineNameMap();
        newList = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            String counter = countForNames.get(i);
            assert counter != null;
            if (Integer.parseInt(counter) != 0) {
                Names name = names.get(i-1);
                newList.add(name);
            }
        }
        adapter = new NamesAdapter(names);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getApplicationContext(), NameActivity.class);
                if (newList != null && !newList.isEmpty()) {
                    intent.putExtra("idNames", newList.get(position));
                } else {
                    intent.putExtra("idNames", names.get(position));
                }
                startActivity(intent);
            }
        });


    }

    private void defineNameMap() {
        if (names == null) {
            names = new ArrayList<>();
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if (language.equals("en")) {
                namesMap = getNames("eng", "eng_name");
                descMap = getNames("eng", "explanation");
            } else if (language.equals("tr")) {
                namesMap = getNames("tr", "eng_name");
                descMap = getNames("tr", "explanation");
            } else {
                namesMap = getNames("arab", "eng_name");
                descMap = getNames("arab", "explanation");
            }
            for (int i = 0; i < 99; i++) {
                names.add(new Names(String.valueOf(i + 1), namesMap.get(i), descMap.get(i)));
            }
        }
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
            for (int i = 1; i < 100; i++) {
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