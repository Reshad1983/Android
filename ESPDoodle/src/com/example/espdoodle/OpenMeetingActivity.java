package com.example.espdoodle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OpenMeetingActivity extends Activity {

	private String timeslot1 = "";
	private String timeslot2 = "";
	private String timeslot3 = "";
	
	EditText txtId;
	EditText txtParticipant1;
	EditText txtParticipant2;
	EditText txtParticipant3;
	Button btnSelectTimeSlots;
	Button btnCreateMeeting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_meeting);
		
		txtId = (EditText) findViewById(R.id.open_meeting_txtID);
		txtParticipant1 = (EditText) findViewById(R.id.open_meeting_txtParticipant1);
		txtParticipant2 = (EditText) findViewById(R.id.open_meeting_txtParticipant2);
		txtParticipant3 = (EditText) findViewById(R.id.open_meeting_txtParticipant3);
		btnSelectTimeSlots = (Button) findViewById(R.id.open_meeting_btnSelectTimeSlot);
		btnCreateMeeting = (Button) findViewById(R.id.open_meeting_btnCreateMeeting);

		btnSelectTimeSlots.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Start activity to select 3 time slots
				Intent intent = new Intent(OpenMeetingActivity.this,SelectTimeSlotsActivity.class);
				Bundle bundle = new Bundle();
				// Pack previous selected time slots if any 
				if(!timeslot1.equals("")){
					bundle.putString(SelectTimeSlotsActivity.SLOT_ONE, timeslot1);
					if(!timeslot2.equals("")){
						bundle.putString(SelectTimeSlotsActivity.SLOT_TWO, timeslot2);
						if(!timeslot3.equals("")){
							bundle.putString(SelectTimeSlotsActivity.SLOT_THREE, timeslot3);
						}
					}
				}
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
			}
		});

		btnCreateMeeting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendNewMeetingToServer();
			}
		});
	}

	/*
	 * Receives selected time slots from SelectTimeSlotsActivity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == SelectTimeSlotsActivity.TIMESLOTS_SELECTED) {
			Bundle bundle = data.getExtras();
			timeslot1 = bundle.getString(SelectTimeSlotsActivity.SLOT_ONE);
			timeslot2 = bundle.getString(SelectTimeSlotsActivity.SLOT_TWO);
			timeslot3 = bundle.getString(SelectTimeSlotsActivity.SLOT_THREE);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_meeting, menu);
		return true;
	}

	/*
	 * Try to send a new meeting to the service
	 */
	private void sendNewMeetingToServer() {
		
		String sId = txtId.getText().toString().trim();
		String sParticipant1 = txtParticipant1.getText().toString().trim();
		String sParticipant2 = txtParticipant2.getText().toString().trim();
		String sParticipant3 = txtParticipant3.getText().toString().trim();

		if(!validateInput(sId, sParticipant1, sParticipant2, sParticipant3)){
			return;
		}

		String[] timeslots = {timeslot1, timeslot2, timeslot3};
		String[] participants = {sParticipant1, sParticipant2, sParticipant3};
		
		Bundle bundle = new Bundle();
		bundle.putString(TheService.MEETING_ID, sId);
		bundle.putStringArray(TheService.TIMESLOTS, timeslots);
		bundle.putStringArray(TheService.PARTICIPANTS, participants);
		// The result of the action will be displayed through the main menu broadcast receiver
		bundle.putString(TheService.BROADCAST_FILTER, ESPDoodleMainMenuActivity.BROADCAST_FILTER);
		
		Intent intent =  new Intent(getApplicationContext(), TheService.class);
		intent.putExtra(TheService.WHAT, TheService.OPEN_MEETING);
		intent.putExtra(TheService.ARGUMENTS, bundle);
		
		// Send new meeting to service
		Log.d(getClass().getName(), "Sending new meeting to service");
		startService(intent);
		
		// Go back to main menu
		finish();
	}
	
	/*
	 * Validates the user ID and IP address and toast errors
	 */
	private boolean validateInput(String id, String participant1, String participant2, String participant3){
		
		boolean inputOk = true;
		String toastMessage = "";
		
		if(id.equals("")){
			toastMessage = toastMessage + "The meeting Id may not be empty\n";
			inputOk = false;
		}
		
		if(timeslot1.equals("") ||  timeslot1.equals("") || timeslot1.equals("")){
			toastMessage = toastMessage + "3 timeslots must be selected\n";
			inputOk = false;
		}
		if(participant1.equals("") || participant2.equals("") || participant3.equals("")){
			toastMessage = toastMessage + "3 participants must be added\n";
			inputOk = false;
		}
		if(participant1.contains(" ") || participant2.contains(" ") || participant3.contains(" ")){
			toastMessage = toastMessage + "Id of participants may not cantain whitespaces\n";
			inputOk = false;
		}
		
		if(!inputOk){
			Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
		}
		
		return inputOk;
	}
}
