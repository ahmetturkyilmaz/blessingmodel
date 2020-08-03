package com.example.blessing_model.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.blessing_model.R;
import com.example.blessing_model.util.LocaleHelper;


public class SettingsActivity extends AppCompatActivity {

    public FragmentManager fragmentManager = getSupportFragmentManager();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        toolbar = findViewById(R.id.settingsToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fragmentManager.beginTransaction().replace(R.id.settings, new SettingsFragment()).commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        ListPreference languages;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            languages = getPreferenceManager().findPreference("language");
            languages.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                                        @Override
                                                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                                                            boolean anyChanges = false;
                                                            if (newValue.toString().equals("turkish")) {
                                                                LocaleHelper.setLocale(getContext(), "tr");
                                                                anyChanges = true;
                                                            }
                                                            if (newValue.toString().equals("english")) {
                                                                LocaleHelper.setLocale(getContext(), "en");
                                                                anyChanges = true;
                                                            }
                                                            if (newValue.toString().equals("arab")) {
                                                                LocaleHelper.setLocale(getContext(), "ar");
                                                                anyChanges = true;
                                                            }
                                                            if (anyChanges) {
                                                                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                                                                //TODO::::::!!!!!!!!!!
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                getActivity().startActivity(intent);
                                                            }

                                                            return true;
                                                        }

                                                    }
            );

            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        recreate();
        super.onConfigurationChanged(newConfig);
    }
}