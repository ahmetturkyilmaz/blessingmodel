package com.example.blessing_model.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.blessing_model.R;
import com.example.blessing_model.pojo.Names;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class NameActivity extends AppCompatActivity {
    HashMap<Integer, String> countZiqir;
    Button counterButton;
    TextView nameItself;
    TextView explanation;
    Button zeroButton;
    TextView number;
    Names names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        Toolbar toolbar = findViewById(R.id.nameToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        counterButton = findViewById(R.id.counter);
        nameItself = findViewById(R.id.nameItself);
        explanation = findViewById(R.id.explanation);
        zeroButton = findViewById(R.id.zero);
        number = findViewById(R.id.numb);
        names = (Names) getIntent().getSerializableExtra("idNames");

        loadData();
        number.setText(countZiqir.get(Integer.parseInt(names.getId())));

        nameItself.setText(names.getNames());
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter = Integer.parseInt(countZiqir.get(Integer.parseInt(names.getId())));
                counter++;
                countZiqir.put(Integer.parseInt(names.getId()), String.valueOf(counter));
                saveData();
                number.setText(String.valueOf(counter));
            }
        });

        zeroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countZiqir.put(Integer.parseInt(names.getId()), "0");
                saveData();
                number.setText("0");
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("namesPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(countZiqir);
        editor.putString("names", json);
        editor.apply();

    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("namesPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("names", null);

        Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        countZiqir = gson.fromJson(json, type);
        if (countZiqir == null) {
            countZiqir = new HashMap<>();
            for (int i = 1; i < 99; i++) {
                countZiqir.put(i, "0");
            }
        }
    }
}