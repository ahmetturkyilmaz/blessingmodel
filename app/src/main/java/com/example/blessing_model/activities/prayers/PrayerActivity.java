package com.example.blessing_model.activities.prayers;

import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.blessing_model.pojo.Prayer;
import com.example.blessing_model.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
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
    SwitchCompat switchCompat;
    TextView changeLanguageText;
    private InterstitialAd mInterstitialAd;
    Button reduceTextSizeButton;
    Button increaseTextSizeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //ca-app-pub-4701964854424760/2932349785
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        Toolbar toolbar = findViewById(R.id.blessingPrayerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        define();

        loadData();
        number.setText(countZiqir.get(Integer.parseInt(prayer.getSureId())));

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
        if (prayer.getLatinAlphabet() != null) {
            sureItself.setText(prayer.getLatinAlphabet());
            changeLanguageText.setText("Latince Okunuş");

            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sureItself.setText(prayer.getBlessing());
                    }
                    if (!isChecked) {
                        sureItself.setText(prayer.getLatinAlphabet());
                    }
                }
            });
        } else {
            sureItself.setText(prayer.getBlessing());
        }

        zeroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countZiqir.put(Integer.parseInt(prayer.getSureId()), "0");
                saveData();
                number.setText("0");
            }
        });

        textSize();
    }

    private void define() {
        counterButton = findViewById(R.id.counterButton);
        sureItself = findViewById(R.id.sureItself);
        zeroButton = findViewById(R.id.blessingZeroButton);
        number = findViewById(R.id.number);
        switchCompat = findViewById(R.id.changeAlphabet);
        changeLanguageText = findViewById(R.id.changeAlphabetText);
        prayer = (Prayer) getIntent().getSerializableExtra("id");
        reduceTextSizeButton = findViewById(R.id.text_size_minus_button);
        increaseTextSizeButton = findViewById(R.id.text_size_plus_button);

    }

    private void textSize() {
        reduceTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureItself.getTextSize() < 55. || sureItself.getTextSize() > 30.) {
                    sureItself.setTextSize(sureItself.getTextSize() - 5);
                }
            }
        });
        increaseTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureItself.getTextSize() < 55. || sureItself.getTextSize() > 30.) {
                    sureItself.setTextSize(sureItself.getTextSize() + 5);
                }
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