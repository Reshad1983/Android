package com.example.espdoodle;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CheckOpenMeetingActivity extends Activity {
	
	public static final String BROADCAST_FILTER = "com.example.espdoodle.openrequests.OPEN_REQUEST_FILTER";
	
	private ArrayList<OpenMeeting> list;
	private ArrayAdapter<OpenMeeting> adapter;
	ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_open_meeting);
		
		progressBar = (ProgressBar)findViewById(R.id.check_open_meetings_progressBar);
		Button btnBack = (Button)findViewById(R.id.check_open_meetings_btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			// Go back to main menu
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		list = new ArrayList<OpenMeeting>();
		adapter = new ArrayAdapter<OpenMeeting>(getApplicationContext(), R.layout.list_item, list); 
		
		ListView listView = (ListView)findViewById(R.id.check_open_meetings_list);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				// Open new activity to select a time for a meeting
				OpenMeeting meeting = list.get(position);
				String meetingId = meeting.getMeetingId();
				String message = meeting.getMessage();
				String[] timeslots = meeting.getTimeslots();
				
				Intent intent = new Intent(getApplicationContext(), SelectTimeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(TheService.MEETING_ID, meetingId);
				bundle.putString(TheService.MESSAGE, message);
				bundle.putStringArray(TheService.TIMESLOTS, timeslots);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		// Fill the list with available meetings
		Intent intent = new Intent(getApplicationContext(), TheService.class);
		intent.putExtra(TheService.WHAT, TheService.CHECK_OPEN_MEETING);
		Bundle bundle = new Bundle();
		bundle.putString(TheService.BROADCAST_FILTER, BROADCAST_FILTER);
		intent.putExtra(TheService.ARGUMENTS, bundle);
		
		startService(intent);
	}

	@Override
	protected void onPause() {
		unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		registerReceiver(receiver, new IntentFilter(BROADCAST_FILTER));
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.open_request, menu);
		return true;
	}
		
	/*
	 * Broadcast receiver used to receive messages from the service
	 */
	BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			
			int what = intent.getIntExtra(TheService.WHAT, -1);
			if(what == TheService.CHECK_OPEN_MEETING){
				
				Bundle bundle = intent.getExtras();
				int result = bundle.getInt(TheService.RESULT);
				if(result== TheService.SUCCESS){
					progressBar.setVisibility(View.INVISIBLE);
					String meetingId = bundle.getString(TheService.MEETING_ID);
					String message = bundle.getString(TheService.MESSAGE);
					String[] timeslots = bundle.getStringArray(TheService.TIMESLOTS);
						
					OpenMeeting meeting = new OpenMeeting(meetingId, message, timeslots); 
					// Check if the meeting already exists in the list to avoid duplicates when
					// rotating the screen forth and back fast
					if(!list.contains(meeting)){
						list.add(meeting);
					}
					adapter.notifyDataSetChanged();
				} else if(result == TheService.FAILED){
					Toast.makeText(getApplicationContext(), "No open meeting requests found", Toast.LENGTH_SHORT).show();
				}
			} else if(what == TheService.SELECT_TIME){
				Bundle bundle = intent.getExtras();
				int result = bundle.getInt(TheService.RESULT, -1);
				if(result == TheService.SUCCESS){
					// Remove the meeting from the list if selcted time has been stored at server
					String meetingId = bundle.getString(TheService.MEETING_ID);
					OpenMeeting meeting = new OpenMeeting(meetingId, "", new String[]{"", "", ""});
					list.remove(meeting);
					adapter.notifyDataSetChanged();
					Toast.makeText(getApplicationContext(), "The selected time has been stored", Toast.LENGTH_SHORT).show();
				} else if(result == TheService.FAILED){
					Toast.makeText(getApplicationContext(), "An error has occoud while saving the selected time", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
}
