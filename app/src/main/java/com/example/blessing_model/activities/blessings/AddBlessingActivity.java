package com.example.blessing_model.activities.blessings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.blessing_model.R;
import com.example.blessing_model.pojo.Blessing;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class AddBlessingActivity extends AppCompatActivity {
    private TextInputLayout addNameLayout;
    private TextInputLayout addBlessingLayout;
    private Button button;
    HashMap<String, Blessing> blessings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_blessing);
        define();
        loadDataForBlessings();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();
            }
        });
    }


    private boolean validateName() {
        String nameInput = addNameLayout.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()) {
            addNameLayout.setError(getString(R.string.addNameEmptyError));
            return false;
        } else if (nameInput.length() > 20) {
            addNameLayout.setError(getString(R.string.addNameLengthError));
            return false;
        } else {
            addNameLayout.setError(null);
            return true;

        }
    }

    private boolean validateBlessing() {
        String blessingInput = addBlessingLayout.getEditText().getText().toString().trim();
        if (blessingInput.isEmpty()) {
            addBlessingLayout.setError(getString(R.string.blessingEmptyError));
            return false;
        } else if (blessingInput.length() > 500) {
            addBlessingLayout.setError(getString(R.string.blessingLengthError));
            return false;
        } else {
            addNameLayout.setError(null);
            return true;
        }

    }

    private void confirmInput() {
        if (!validateName() | !validateBlessing()) {
            return;
        }
        for (String blessingName : blessings.keySet()) {
            if (blessingName.toLowerCase().equals(addNameLayout.getEditText().getText().toString().toLowerCase())) {
                Toast.makeText(this, getText(R.string.blessingExistsError), Toast.LENGTH_LONG).show();
                return;
            }
        }
        String blessingName = addNameLayout.getEditText().getText().toString();
        String blessingItself = addBlessingLayout.getEditText().getText().toString();
        Blessing blessing = new Blessing();
        blessing.setName(blessingName);
        blessing.setBlessing(blessingItself);
        blessing.setCounter("0");
        blessings.put(blessing.getName(), blessing);

        saveData();
        Intent intent = new Intent(this, BlessingActivity.class);
        startActivity(intent);
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("blessingPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(blessings);
        editor.putString("blessings", json);
        editor.apply();

    }

    private void loadDataForBlessings() {
        SharedPreferences sharedPreferences = getSharedPreferences("blessingPreferences", MODE_PRIVATE);
        String savedBlessings = sharedPreferences.getString("blessings", null);
        Gson gson = new Gson();

        Type type = new TypeToken<HashMap<String, Blessing>>() {

        }.getType();
        blessings = gson.fromJson(savedBlessings, type);
    }

    private void define() {
        addNameLayout = findViewById(R.id.addBlessingAddNameLayout);
        addBlessingLayout = findViewById(R.id.addBlessingAddBlessingLayout);
        button = findViewById(R.id.addBlessingButton);
    }

}