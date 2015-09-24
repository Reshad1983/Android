package com.example.espdoodle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CheckMeetingRespondActivity extends Activity {

	public static final String BROADCAST_FILTER = "com.example.espdoodle.checkmeetingrespondactivity.BROADCAST_FILTER";
	
	private Button btnChoose;
	private RadioGroup rg;
	private RadioButton[] selectableTimes;
	private String meetingId = "";
	
	HashMap<String, Integer> selectedTimes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_meeting_respond);
		
		selectedTimes = new HashMap<String, Integer>();
		
		btnChoose = (Button)findViewById(R.id.check_meeting_respond_btnChoose);
		rg = (RadioGroup)findViewById(R.id.check_meeting_respond_radioGroup);
		selectableTimes = new RadioButton[3];
		selectableTimes[0] = (RadioButton)findViewById(R.id.check_meeting_respond_time1);
		selectableTimes[1] = (RadioButton)findViewById(R.id.check_meeting_respond_time2);
		selectableTimes[2] = (RadioButton)findViewById(R.id.check_meeting_respond_time3);
		
		btnChoose.setVisibility(View.INVISIBLE);
		rg.setVisibility(View.INVISIBLE);
		
		final EditText txtMeetingId = (EditText)findViewById(R.id.check_meeting_respond_txtMeetingId);
		final Button btnCheck = (Button)findViewById(R.id.check_meeting_respond_btnCheck);
		btnCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Hide keyboard if visible
				InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				
				//Hide radio group and choose button
				selectedTimes.clear();
				btnChoose.setVisibility(View.INVISIBLE);
				rg.setVisibility(View.INVISIBLE);
				updateRadioGroup();
				
				meetingId = txtMeetingId.getText().toString();
				Intent intent = new Intent(getApplicationContext(), TheService.class);
				intent.putExtra(TheService.WHAT, TheService.CHECK_MEETING_RESPOND);
				Bundle bundle = new Bundle();
				bundle.putString(TheService.MEETING_ID, meetingId);
				bundle.putString(TheService.BROADCAST_FILTER, BROADCAST_FILTER);
				intent.putExtra(TheService.ARGUMENTS, bundle);
				startService(intent);
			}
		});
		
		btnChoose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int selectedItem = rg.getCheckedRadioButtonId();
				if(selectedItem == -1){
					//TODO toast information
				} else {
					RadioButton selectedbutton = (RadioButton)findViewById(selectedItem);
					String selectedTime = selectedbutton.getText().toString();
					selectedTime = selectedTime.substring(0, selectedTime.indexOf(" "));
					String meetingId = txtMeetingId.getText().toString();
					
					Intent intent = new Intent(getApplicationContext(), TheService.class);
					intent.putExtra(TheService.WHAT, TheService.CLOSE_MEETING);
					Bundle bundle = new Bundle();
					bundle.putString(TheService.MEETING_ID, meetingId);
					bundle.putString(TheService.SELECTED_TIME, selectedTime);
					bundle.putString(TheService.BROADCAST_FILTER, BROADCAST_FILTER);
					intent.putExtra(TheService.ARGUMENTS, bundle);
					startService(intent);
				}
			}
		});
		
	}
	
	

	@Override
	protected void onPause() {
		unregisterReceiver(myRecevier);
		super.onPause();
	}

	@Override
	protected void onResume() {
		registerReceiver(myRecevier, new IntentFilter(BROADCAST_FILTER));
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.closed_meeting_request, menu);
		return true;
	}
	
	BroadcastReceiver myRecevier = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			
			int what = intent.getExtras().getInt(TheService.WHAT);
			
			if(what == TheService.CHECK_MEETING_RESPOND){
				int result = intent.getIntExtra(TheService.RESULT, -1);
				if(result == TheService.SUCCESS){
					btnChoose.setVisibility(View.VISIBLE);
					rg.setVisibility(View.VISIBLE);
					String time = intent.getExtras().getString(TheService.SELECTED_TIME);
					if(!selectedTimes.containsKey(time)){
						selectedTimes.put(time, 1);
					} else{
						Integer num = selectedTimes.get(time);
						num++;
						selectedTimes.put(time, num);
					}
					updateRadioGroup();
				} else if(result == TheService.FAILED){
					Toast.makeText(getApplicationContext(), "No meeting with id: " + meetingId , Toast.LENGTH_SHORT).show();
				}
			} else if(what == TheService.CLOSE_MEETING){
				int result = intent.getIntExtra(TheService.RESULT, -1);
				if(result == TheService.SUCCESS){
					// If success close this activity and make a toast at main menu activity
					String meetingId = intent.getExtras().getString(TheService.MEETING_ID);
					Intent i = new Intent(ESPDoodleMainMenuActivity.BROADCAST_FILTER);
					i.putExtra(TheService.WHAT, TheService.CLOSE_MEETING);
					Bundle bundle = new Bundle();
					bundle.putString(TheService.MEETING_ID, meetingId);
					i.putExtras(bundle);
					sendBroadcast(i);
					finish();
				} else if(result == TheService.FAILED){
					Toast.makeText(getApplicationContext(), "Unable to store selected time", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	private void updateRadioGroup(){
		Set<String> keys = selectedTimes.keySet(); 
		String[] times = new String[keys.size()];
		int i = 0;
		for(String key : keys){
			times[i] = key;
			i++;
		}
		Arrays.sort(times);
		int elements = Math.min(times.length, keys.size());
		for(int j = 0; j < elements; j++){
			selectableTimes[j].setText(times[j] + " " + selectedTimes.get(times[j]) + " participants");
			selectableTimes[j].setVisibility(View.VISIBLE);
		}
		for(int j = elements; j < selectableTimes.length; j++){
			selectableTimes[j].setVisibility(View.INVISIBLE);
		}
	}
}
