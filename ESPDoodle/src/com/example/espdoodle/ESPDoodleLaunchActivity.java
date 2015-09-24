package com.example.espdoodle;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ESPDoodleLaunchActivity extends Activity {

	public static final String BROADCAST_FILTER = "com.example.espdoodle.espdoodlestartactivity.loginresult";
	public static final String SHARED_PREFS = "SharedSettings";
	public static final String SHARED_PREF_USER = "user";
	public static final String SHARED_PREF_IP = "ip";
	public static final String SHARED_PREF_LOGGED_IN = "logged in";
	
	private ProgressBar pbWait;
	private EditText txtUser;
	private EditText txtIP;
	private Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_espdoodle_launch);
		
		txtUser = (EditText) findViewById(R.id.txtUserId);
		txtIP = (EditText) findViewById(R.id.txtIP);
		pbWait = (ProgressBar) findViewById(R.id.waiting);
		pbWait.setVisibility(View.INVISIBLE);

		restoreActivityState();
		
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Hide keyboard if visible
				InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				
				String userId = txtUser.getText().toString().trim();
				String ipAddress = txtIP.getText().toString().trim();
				
				if(!validateInputFields(userId, ipAddress)){
					return;
				}
				
				Intent intent = new Intent(getApplicationContext(), TheService.class);
				intent.putExtra(TheService.WHAT, TheService.CONNECT);
				Bundle bundle = new Bundle();
				bundle.putInt(TheService.PORT, 4444);
				bundle.putString(TheService.HOST_IP, ipAddress);
				bundle.putString(TheService.USER, userId);
				bundle.putString(TheService.BROADCAST_FILTER, BROADCAST_FILTER);
				intent.putExtra(TheService.ARGUMENTS, bundle);

				startService(intent);
				// Disable editing test fields during login process and show a wait bar
				pbWait.setVisibility(View.VISIBLE);
				btnLogin.setEnabled(false);
				txtIP.setEnabled(false);
				txtUser.setEnabled(false);
			}
		});
	}

	@Override
	protected void onPause() {
		unregisterReceiver(messageReceiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		registerReceiver(messageReceiver, new IntentFilter(BROADCAST_FILTER));
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.espdoodle_start, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		saveActivityState();
		super.onDestroy();
	}
	
	/*
	 * Validates the user ID and IP address and toast errors
	 */
	private boolean validateInputFields(String userId, String ipAddress){
		
		boolean inputOk = true;
		String toastMessage = "";
		
		
		if(userId.equals("")){
			toastMessage = toastMessage + "The username may not be empty.\n";
			inputOk = false;
		}
		if(userId.contains(" ")){
			toastMessage = toastMessage + "The username may not contain whitespaces.\n";
			inputOk = false;
		}
		
		
		if(ipAddress.equals("")){
			toastMessage = toastMessage + "The ip address may not be empty.\n";
			inputOk = false;
		}
		
		String ipv4_pattern = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
		if(!ipAddress.matches(ipv4_pattern)){
			toastMessage = toastMessage + "The ip address isn't valid.\n";
			inputOk = false;
		}
		
		if(!inputOk){
			Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
		}
		
		return inputOk;
	}

	/*
	 * Restore the text in edit fields of the activity from previous saved state
	 * If the user is still logged in launch the main menu instead. 
	 */
	private void restoreActivityState() {
		
		SharedPreferences settings = getSharedPreferences(SHARED_PREFS, 0);
		String sIP = settings.getString(SHARED_PREF_IP, "127.0.0.1");
		String sUserID = settings.getString(SHARED_PREF_USER, "User ID");
		boolean loggedIn = settings.getBoolean(SHARED_PREF_LOGGED_IN, false);
		
		txtUser.setText(sUserID);
		txtIP.setText(sIP);
		
		// If the user is already logged in launch main menu activity
		if(loggedIn){
			Intent intent = new Intent(getApplicationContext(), ESPDoodleMainMenuActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	/*
	 * Save the state of the activity before closing it
	 */
	private void saveActivityState(){
		SharedPreferences settings = getSharedPreferences(SHARED_PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(SHARED_PREF_USER, txtUser.getText().toString());
		editor.putString(SHARED_PREF_IP, txtIP.getText().toString());
		editor.commit();
	}

	/*
	 * Broadcast receiver used to receive messages from the service
	 */
	BroadcastReceiver messageReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(getClass().getName(), "Broadcast received");

			int result = intent.getIntExtra(TheService.RESULT, -1);
			switch (result) {
			case TheService.SUCCESS: {
				// Login succeeded, launch main menu activity and close this one
				Intent intentMainMenu = new Intent(ESPDoodleLaunchActivity.this,ESPDoodleMainMenuActivity.class);
				intentMainMenu.putExtra(TheService.USER, txtUser.getText().toString());
				startActivity(intentMainMenu);
				Toast.makeText(getApplicationContext(), "Logged in!",Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			case TheService.FAILED: {
				// Login failed, toast message and hide wait bar and enable editing of text fields
				Toast.makeText(getApplicationContext(), "Login failed!",Toast.LENGTH_SHORT).show();
				pbWait.setVisibility(View.INVISIBLE);
				btnLogin.setEnabled(true);
				txtIP.setEnabled(true);
				txtUser.setEnabled(true);
				break;
			}
			}
		}
	};
}
