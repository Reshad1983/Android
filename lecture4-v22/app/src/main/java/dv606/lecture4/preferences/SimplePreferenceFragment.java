package dv606.lecture4.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import dv606.lecture4.R;

public class SimplePreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.simple_prefs);
	}
}
