package com.example.blessing_model.activities.blessings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blessing_model.R;
import com.example.blessing_model.pojo.Blessing;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class BlessingActivity extends AppCompatActivity {
    private HashMap<String, Blessing> blessings;
    private InterstitialAd mInterstitialAd;
    private Button counterButton;
    private TextView sureItself;
    private Button zeroButton;
    private TextView number;
    private Blessing blessing;
    private Button reduceTextSizeButton;
    private Button increaseTextSizeButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blessing);

        define();
        textSize();
        loadDataForBlessings();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //ca-app-pub-4701964854424760/2932349785

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        sureItself.setText(blessing.getBlessing());
        if (blessing.getCounter() == null) {
            blessing.setCounter("0");
        }
        number.setText(blessings.get(blessing.getName()).getCounter());
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter = Integer.parseInt(blessings.get(blessing.getName()).getCounter());
                counter++;
                blessing.setCounter(String.valueOf(counter));
                blessings.put(blessing.getName(), blessing);
                saveData();
                number.setText(String.valueOf(counter));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editBlessingMenuItem) {
            Intent intent = new Intent(this, AddBlessingActivity.class);
            intent.putExtra("editableBlessing", blessing);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.deleteBlessingMenuItem) {
            blessings.remove(blessing.getName());
            saveData();
            Intent intent = new Intent(this, BlessingListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("blessingPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(blessings);
        editor.putString("blessings", json);
        editor.apply();

    }

    private void define() {
        counterButton = findViewById(R.id.blessingCounterButton);
        sureItself = findViewById(R.id.blessingItself);
        zeroButton = findViewById(R.id.blessingZeroButton);
        number = findViewById(R.id.blessingNumber);
        reduceTextSizeButton = findViewById(R.id.blessing_text_size_minus_button);
        increaseTextSizeButton = findViewById(R.id.blessing_text_size_plus_button);
        toolbar = findViewById(R.id.blessingActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        blessing = (Blessing) getIntent().getSerializableExtra("selectedBlessing");

    }

    private void textSize() {
        reduceTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureItself.getTextSize() <= 300 && sureItself.getTextSize() >= 80) {
                    sureItself.setTextSize(TypedValue.COMPLEX_UNIT_PX, sureItself.getTextSize() - 8);
                }
            }
        });
        increaseTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureItself.getTextSize() <= 300 && sureItself.getTextSize() >= 80) {
                    sureItself.setTextSize(TypedValue.COMPLEX_UNIT_PX, sureItself.getTextSize() + 8);
                }
            }
        });
    }

    private void loadDataForBlessings() {
        SharedPreferences sharedPreferences = getSharedPreferences("blessingPreferences", MODE_PRIVATE);
        String savedBlessings = sharedPreferences.getString("blessings", null);
        Gson gson = new Gson();

        Type type = new TypeToken<HashMap<String, Blessing>>() {
        }.getType();
        blessings = gson.fromJson(savedBlessings, type);
    }
}