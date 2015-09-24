package com.example.espdoodle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SelectTimeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_time);
		
		Bundle bundle = getIntent().getExtras();
		
		String meetingId = bundle.getString(TheService.MEETING_ID);
		String message = bundle.getString(TheService.MESSAGE);
		String[] timeslots = bundle.getStringArray(TheService.TIMESLOTS);
		
		final TextView txtMeetingId = (TextView)findViewById(R.id.select_time_txtMeetingId);
		final TextView txtMessage = (TextView)findViewById(R.id.select_time_txtMessage);
		final RadioGroup rg = (RadioGroup)findViewById(R.id.select_time_radioGroup);
		final RadioButton rb1 = (RadioButton)findViewById(R.id.select_time_rTimeslot1);
		final RadioButton rb2 = (RadioButton)findViewById(R.id.select_time_rTimeslot2);
		final RadioButton rb3 = (RadioButton)findViewById(R.id.select_time_rTimeslot3);
		Button btnChoose = (Button)findViewById(R.id.select_time_btnChoose);
		
		txtMeetingId.setText(meetingId);
		txtMessage.setText(message);
		rb1.setText(timeslots[0]);
		rb2.setText(timeslots[1]);
		rb3.setText(timeslots[2]);
		//TODO send selected time to service
		
		btnChoose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int selectedId =  rg.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton)findViewById(selectedId);
				String time = rb.getText().toString();
				String meetingId = txtMeetingId.getText().toString(); 
				
				Intent intent = new Intent(getApplicationContext(), TheService.class);
				intent.putExtra(TheService.WHAT, TheService.SELECT_TIME);
				Bundle bundle = new Bundle();
				bundle.putString(TheService.MEETING_ID, meetingId);
				bundle.putString(TheService.SELECTED_TIME, time);
				// Send the result do previous activity and close this one.
				bundle.putString(TheService.BROADCAST_FILTER, CheckOpenMeetingActivity.BROADCAST_FILTER);
				intent.putExtra(TheService.ARGUMENTS, bundle);
				startService(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_time, menu);
		return true;
	}

}
