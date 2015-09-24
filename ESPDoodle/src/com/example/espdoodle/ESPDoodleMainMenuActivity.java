package com.example.espdoodle;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ESPDoodleMainMenuActivity extends Activity {

	public static final String BROADCAST_FILTER = "com.example.espdoodle.espdoodlemainmenu";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// If this activity is started it means that the user is logged in.
		SharedPreferences preferences = getSharedPreferences(ESPDoodleLaunchActivity.SHARED_PREFS, 0);
		preferences.edit().putBoolean(ESPDoodleLaunchActivity.SHARED_PREF_LOGGED_IN, true).commit();
		
		setContentView(R.layout.activity_espdoodle_main_menu);
		
		String user = preferences.getString(ESPDoodleLaunchActivity.SHARED_PREF_USER, "no name");
		setTitle("ESP Doodle - " + user);
		
		Button btnCreateMeeting = (Button)findViewById(R.id.btnCreateMeeting);
		Button btnOpenRequests = (Button)findViewById(R.id.btnOpenRequestsg);
		Button btnCloseMeetings = (Button)findViewById(R.id.btnCloseMeetings);
		Button btnPlannedMeetings = (Button)findViewById(R.id.btnPlannedMeetings);
		Button btnLogout = (Button)findViewById(R.id.btnLogout);
		
		btnCreateMeeting.setOnClickListener(buttonListener);
		btnCloseMeetings.setOnClickListener(buttonListener);
		btnOpenRequests.setOnClickListener(buttonListener);
		btnPlannedMeetings.setOnClickListener(buttonListener);
		
		btnLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Shutdown the service and close the application and log off the user
				Intent intent = new Intent(ESPDoodleMainMenuActivity.this, TheService.class);
				intent.putExtra(TheService.WHAT, TheService.CONCLUDE);
				SharedPreferences preferences = getSharedPreferences(ESPDoodleLaunchActivity.SHARED_PREFS, 0);
				preferences.edit().putBoolean(ESPDoodleLaunchActivity.SHARED_PREF_LOGGED_IN, false).commit();
				startService(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.espdoodle, menu);
		return true;
	}
	
	/*
	 * On click listener for all menu alternatives except the logout button.
	 * Will start the corresponding activity
	 */
	OnClickListener buttonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			
			switch (v.getId()){
			case R.id.btnCreateMeeting:{
				intent.setClass(ESPDoodleMainMenuActivity.this, OpenMeetingActivity.class);
				break;
			}
			case R.id.btnOpenRequestsg:{
				intent.setClass(ESPDoodleMainMenuActivity.this, CheckOpenMeetingActivity.class);
				break;
			}
			case R.id.btnCloseMeetings:{
				intent.setClass(ESPDoodleMainMenuActivity.this, CheckMeetingRespondActivity.class);
				break;
			}
			case R.id.btnPlannedMeetings:{
				intent.setClass(ESPDoodleMainMenuActivity.this, CheckClosedMeetingActivity.class);
				break;
			}
			}
			
			startActivity(intent);
		}
	};
	
	/*
	 * Broadcast receiver used to receive messages from the service
	 * to display information about newly created meetings and 
	 * closed meetings requests. This activity receives the messages
	 * because it is at the bottom of the applications activity stack and
	 * running after logged in.  
	 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			int what = intent.getIntExtra(TheService.WHAT, -1);
			int result = intent.getExtras().getInt(TheService.RESULT);
			
			if(what == TheService.OPEN_MEETING){
				if(result == TheService.SUCCESS){
				Toast.makeText(getApplicationContext(), "New meeting created", Toast.LENGTH_SHORT).show();
				} else if(result == TheService.FAILED){
					Toast.makeText(getApplicationContext(), "Failed to create a new meeting", Toast.LENGTH_SHORT).show();
				}
			}
			
			if(what == TheService.CLOSE_MEETING){
				String meetingId = intent.getExtras().getString(TheService.MEETING_ID);
				Toast.makeText(getApplicationContext(), "Selected time for meeting \"" + meetingId + "\" has been stored", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onResume() {
		registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_FILTER));
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
	
	
}
