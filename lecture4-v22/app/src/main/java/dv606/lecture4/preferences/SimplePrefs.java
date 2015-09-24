package dv606.lecture4.preferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import dv606.lecture4.R;

/**
 * This example is a combination of two examples taken from:
 *  1) "Data Storage" tutorial at Dev Guide. 
 *  2) Perfs example from The Busy Coder's Guide to Android Development.
 *  Both are slightly modified.
 *  
 *  Example 1) shows the straight forward way to store prefs by reading/writing key/value
 *  pairs to a SharedPreferences object. Example 2) shows how to use Android's built in 
 *  support (i.e. PreferenceActivity) to create preference menus.
 *  
 * @author jonasl
 *
 */
public class SimplePrefs extends Activity {

	private TextView checkbox=null;
	private TextView ringtone=null;
	private TextView boolProp;
	private TextView intProp;

	@Override
	protected void onCreate(Bundle state){         
		super.onCreate(state);
		setContentView(R.layout.prefs);

		/* Find all text views */
		boolProp=(TextView)findViewById(R.id.boolprop);
		intProp=(TextView)findViewById(R.id.intprop);
		checkbox=(TextView)findViewById(R.id.checkbox);
		ringtone=(TextView)findViewById(R.id.ringtone);


	}

	@Override
	public void onResume() {  // Activity comes in the foreground
		super.onResume();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		// Load simple properties. false/1 are default values used at start-up
		boolean bp = prefs.getBoolean("boolProp", false);
		int ip = prefs.getInt("intProp", 1);
		boolProp.setText(Boolean.toString(bp));
		intProp.setText(Integer.toString(ip));

		// Load setting properties. false/<unset> are default values used at start-up
		checkbox.setText(Boolean.toString(prefs.getBoolean("checkbox", false)));
		ringtone.setText(prefs.getString("ringtone", "<unset>"));
	}

	@Override
	protected void onStop(){
		super.onStop();

		// Update simple properties
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		boolean bp = !prefs.getBoolean("boolProp", false);
		int ip = 1+prefs.getInt("intProp", 1);

		// Save user preferences. We need an Editor object to make changes. 
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean("boolProp", bp);
		edit.putInt("intProp", ip);

		// Commit your edits!!!
		edit.commit();

		showToast("Update and Store properties",bp,ip);
	}


	/*
	 * Prepare Preference Menus.
	 * 
	 */
	private static final int EDIT_ID = Menu.FIRST+2;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, EDIT_ID, Menu.NONE, "Edit Settings")
		.setIcon(R.drawable.misc);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case EDIT_ID:
			startActivity(new Intent(this, SimplePreferenceActivity.class));
			return(true);
		}

		return(super.onOptionsItemSelected(item));
	}
	private void showToast(String action, boolean bp, int ip) {
		String msg = action +" Properties\n"
		+"BoolProp = "+bp+", IntProp = "+ip;
		Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
	}
}
