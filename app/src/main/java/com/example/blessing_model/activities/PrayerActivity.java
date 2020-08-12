package com.example.blessing_model.activities;

import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.blessing_model.pojo.Prayer;
import com.example.blessing_model.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class PrayerActivity extends AppCompatActivity {
    HashMap<Integer, String> countZiqir;
    Button counterButton;
    TextView sureItself;
    Button zeroButton;
    TextView number;
    Prayer prayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);
        Toolbar toolbar = findViewById(R.id.prayerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        counterButton = findViewById(R.id.counterButton);
        sureItself = findViewById(R.id.sureItself);
        zeroButton = findViewById(R.id.zeroButton);
        number = findViewById(R.id.number);
        prayer = (Prayer) getIntent().getSerializableExtra("id");

        loadData();
        number.setText(countZiqir.get(Integer.parseInt(prayer.getSureId())));

        sureItself.setText(prayer.getBlessing());
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter = Integer.parseInt(countZiqir.get(Integer.parseInt(prayer.getSureId())));
                counter++;
                countZiqir.put(Integer.parseInt(prayer.getSureId()), String.valueOf(counter));
                saveData();
                number.setText(String.valueOf(counter));
            }
        });

        zeroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countZiqir.put(Integer.parseInt(prayer.getSureId()), "0");
                saveData();
                number.setText("0");
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("prayerPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(countZiqir);
        editor.putString("prayers", json);
        editor.apply();

    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("prayerPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("prayers", null);

        Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        countZiqir = gson.fromJson(json, type);
        if (countZiqir == null) {
            countZiqir = new HashMap<>();
            for (int i = 1; i < 115; i++) {
                countZiqir.put(i, "0");
            }
        }
    }
}