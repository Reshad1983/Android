package com.example.reshad.mymp3player;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int pref_file_to_upload = -1;
        super.onCreate(savedInstanceState);
        String setting = getArguments().getString("settings");
        switch(setting)
        {
            case "general setting":
                pref_file_to_upload = R.xml.simple_prefs;
                break;
            case "color":
                pref_file_to_upload = R.xml.color_simple_prefs;
                break;
            case "Text":
                pref_file_to_upload = R.xml.text_setting;
                break;
        }
        addPreferencesFromResource(pref_file_to_upload);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
            pref.setSummary(((ListPreference)pref).getEntry());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
