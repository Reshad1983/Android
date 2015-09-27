package com.example.reshad.mymp3player;

import android.preference.PreferenceActivity;

import java.util.List;

public class SettingActivity extends PreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return (MyPreferenceFragment.class.getName().equals(fragmentName));
    }
}
