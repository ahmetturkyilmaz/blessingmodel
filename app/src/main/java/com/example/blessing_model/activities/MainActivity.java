package com.example.blessing_model.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.blessing_model.R;
import com.example.blessing_model.activities.blessings.BlessingListActivity;
import com.example.blessing_model.activities.names.ContinueDhikrForNames;
import com.example.blessing_model.activities.names.NameListActivity;
import com.example.blessing_model.activities.prayers.ContinueDhikrForPrayers;
import com.example.blessing_model.activities.prayers.PrayersListActivity;
import com.example.blessing_model.util.LocaleHelper;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    HashMap<Integer, String> countForPrayers;
    HashMap<Integer, String> countForNames;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private AppCompatImageView imageView;
    private Button prayersButton;
    private Button namesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Adds SDK initialize
        loadDataForNames();
        loadDataForPrayers();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        imageView = findViewById(R.id.mainImage);
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:

                if (language.equals("tr")) {
                    imageView.setImageResource(R.drawable.main_tr_dark);
                } else {
                    imageView.setImageResource(R.drawable.main_eng_dark);
                }
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                if (language.equals("tr")) {
                    imageView.setImageResource(R.drawable.main_tr_light);
                } else {
                    imageView.setImageResource(R.drawable.main_eng_light);
                }
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
        //Toolbar and Drawer Layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //continue or start prayers dhikr
        prayersButton = findViewById(R.id.startDhikrPrayers);
        if (countForPrayers == null) {
            prayersButton.setText(R.string.startDhikrPrayers);
            prayersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent prayerIntent = new Intent(getApplicationContext(), PrayersListActivity.class);
                    startActivity(prayerIntent);
                }
            });

        } else {
            prayersButton.setText(R.string.continueDhikrPrayers);
            prayersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent continueDhikr = new Intent(getApplicationContext(), ContinueDhikrForPrayers.class);
                    startActivity(continueDhikr);
                }
            });
        }
        namesButton = findViewById(R.id.startDihkrNames);
        if (countForPrayers == null) {
            namesButton.setText(R.string.startDhikrNames);
            namesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent prayerIntent = new Intent(getApplicationContext(), NameListActivity.class);
                    startActivity(prayerIntent);
                }
            });

        } else {
            namesButton.setText(R.string.continueDhikrNames);
            namesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent continueDhikr = new Intent(getApplicationContext(), ContinueDhikrForNames.class);
                    startActivity(continueDhikr);
                }

            });

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sure:
                Intent prayerIntent = new Intent(this, PrayersListActivity.class);
                startActivity(prayerIntent);
                break;
            case R.id.nav_names:
                Intent nameListIntent = new Intent(this, NameListActivity.class);
                startActivity(nameListIntent);
                break;
            case R.id.nav_blessings:
                Intent blessingListIntent = new Intent(this, BlessingListActivity.class);
                startActivity(blessingListIntent);
                break;
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    private void loadDataForPrayers() {
        SharedPreferences sharedPreferences = getSharedPreferences("prayerPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("prayers", null);

        Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        countForPrayers = gson.fromJson(json, type);
        if (countForPrayers == null) {
            countForPrayers = new HashMap<>();
            for (int i = 1; i < 115; i++) {
                countForPrayers.put(i, "0");
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
            for (int i = 1; i < 99; i++) {
                countForNames.put(i, "0");
            }
        }
    }
}
