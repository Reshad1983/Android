package dv606.lecture4.preferences;

import java.util.List;
import android.preference.PreferenceActivity;

import dv606.lecture4.R;

public class SimplePreferenceActivity extends PreferenceActivity {
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
	
	@Override
	protected boolean isValidFragment (String fragmentName) {
	  return (SimplePreferenceFragment.class.getName().equals(fragmentName));
	}
}
