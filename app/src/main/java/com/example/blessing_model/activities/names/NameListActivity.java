package com.example.blessing_model.activities.names;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.example.blessing_model.R;
import com.example.blessing_model.adapters.NamesAdapter;
import com.example.blessing_model.pojo.Names;
import com.example.blessing_model.util.ItemClickSupport;
import com.example.blessing_model.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class NameListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    NamesAdapter adapter;
    ArrayList<Names> names;
    ArrayList<Names> newList;
    JsonUtil jsonUtil;
    HashMap<Integer, String> descMap = null;
    HashMap<Integer, String> namesMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);
        recyclerView = findViewById(R.id.nameListView);
        Toolbar toolbar = findViewById(R.id.nameListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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