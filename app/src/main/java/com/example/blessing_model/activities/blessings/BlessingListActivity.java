package com.example.blessing_model.activities.blessings;

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
import com.example.blessing_model.adapters.BlessingAdapter;
import com.example.blessing_model.pojo.Blessing;
import com.example.blessing_model.util.ItemClickSupport;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class BlessingListActivity extends AppCompatActivity {
    private HashMap<String, Blessing> blessings;
    private RecyclerView recyclerView;
    private BlessingAdapter adapter;
    private ArrayList<Blessing> blessingList;
    private ExtendedFloatingActionButton floatingActionButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blessing_list);

        define();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadDataForBlessings();
        int counter = 0;
        blessingList = new ArrayList<>();
        if (!blessings.isEmpty()) {
            for (String blessingName : blessings.keySet()) {

                Blessing savedBlessing = blessings.get(blessingName);
                savedBlessing.setId(String.valueOf(counter));
                counter++;
                blessingList.add(savedBlessing);
            }
        }

        adapter = new BlessingAdapter(blessingList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getApplicationContext(), BlessingActivity.class);
                intent.putExtra("selectedBlessing", blessingList.get(position));
                startActivity(intent);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddBlessingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("blessingPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(blessings);
        editor.putString("blessings", json);
        editor.apply();

    }

    private void define() {
        toolbar = findViewById(R.id.blessingListToolbar);
        floatingActionButton = findViewById(R.id.floating_action_button);
        recyclerView = findViewById(R.id.blessingListView);
    }

    private void loadDataForBlessings() {
        SharedPreferences sharedPreferences = getSharedPreferences("blessingPreferences", MODE_PRIVATE);
        String savedBlessings = sharedPreferences.getString("blessings", null);
        Gson gson = new Gson();

        Type type = new TypeToken<HashMap<String, Blessing>>() {
        }.getType();
        blessings = gson.fromJson(savedBlessings, type);
        if (blessings == null) {
            blessings = new HashMap<>();
            saveData();
        }
    }

}