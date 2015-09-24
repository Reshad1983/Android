package com.example.espdoodle;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CheckClosedMeetingActivity extends Activity {
	
	public static final String BROADCAST_FILTER = "com.example.espdoodle.checkclosedmeeting.BROADCAST_FILTER";

	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_closed_meeting);
		
		layout = (LinearLayout)findViewById(R.id.check_closed_meeting_layout);
		Intent intent = new Intent(getApplicationContext(), TheService.class);
		intent.putExtra(TheService.WHAT, TheService.CHECK_CLOSED_MEETING);
		Bundle bundle = new Bundle();
		bundle.putString(TheService.BROADCAST_FILTER, BROADCAST_FILTER);
		intent.putExtra(TheService.ARGUMENTS, bundle);
		startService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.planned_meeting, menu);
		return true;
	}

	@Override
	protected void onPause() {
		unregisterReceiver(myReceiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		registerReceiver(myReceiver, new IntentFilter(BROADCAST_FILTER));
		super.onResume();
	}

	BroadcastReceiver myReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			int resultStatus = intent.getExtras().getInt(TheService.RESULT, -1);
			ProgressBar progressBar = (ProgressBar)findViewById(R.id.check_closed_meeting_progressBar);
			progressBar.setVisibility(View.INVISIBLE);
			layout.removeViewInLayout(progressBar);
			
			if(resultStatus == TheService.SUCCESS){
				String meetingId = intent.getExtras().getString(TheService.MEETING_ID);
				String time = intent.getExtras().getString(TheService.SELECTED_TIME);
				TextView v = new TextView(getApplicationContext());
				v.setText(meetingId + " at " + time);
				v.setTextColor(Color.BLACK);
				v.setTextSize(30.0f);
				
				layout.addView(v);
			} else if(resultStatus == TheService.FAILED){
				Toast.makeText(getApplicationContext(), "No meetings found", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
}
