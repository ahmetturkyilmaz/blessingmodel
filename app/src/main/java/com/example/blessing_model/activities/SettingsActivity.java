package com.example.blessing_model.activities;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.blessing_model.R;
import com.example.blessing_model.util.LocaleHelper;



public class SettingsActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager().beginTransaction().replace(R.id.settings, new SettingsFragment()).commit();
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
                                                            if (anyChanges) {
                                                                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                                                                if (Build.VERSION.SDK_INT >= 26) {
                                                                    ft.setReorderingAllowed(false);
                                                                }
                                                                ft.detach(SettingsFragment.this).attach(SettingsFragment.this).commit();

                                                            }

                                                            return true;
                                                        }

                                                    }
            );

            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}