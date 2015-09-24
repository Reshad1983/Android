package com.example.espdoodle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SelectTimeSlotsActivity extends Activity {
		
	public static final int TIMESLOTS_SELECTED = 1;
	public static final String SLOT_ONE = "slot1";
	public static final String SLOT_TWO = "slot2";
	public static final String SLOT_THREE = "slot3";
	
	public static final String PRE_SELECTED_TIMESLOTS = "preSelectedTimeslots";
	public static final int PRE_SELECTED_EXISTS = 0;
	public static final int NO_PRE_SELECTED = 1;
	
	private CheckBox[] checkBoxes = new CheckBox[24];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_time_slots);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.layoutSelectTimeslots);
		String[] times = new String[3];
		times[0] = getIntent().getExtras().getString(SLOT_ONE);
		times[1] = getIntent().getExtras().getString(SLOT_TWO);
		times[2] = getIntent().getExtras().getString(SLOT_THREE);
		int j = 0;
		for(int i = 0; i < checkBoxes.length; i++){
			CheckBox chb = new CheckBox(this);
			chb.setSelected(false);
			String tmp = i + ":00";
			chb.setText(tmp);	
			if(j < 3 && times[j] != null){
				if(tmp.equals(times[j])){
					chb.setChecked(true);
					j++;
				}
			}
			checkBoxes[i] = chb;
			layout.addView(chb);
		}

		Button btnOK = (Button)findViewById(R.id.select_timeslot_btnOK);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectOK()){
					Intent intent = makeIntent();
					setResult(TIMESLOTS_SELECTED, intent);
					finish();
				}
				else{
					Toast.makeText(SelectTimeSlotsActivity.this, "Select 3 timeslots!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private boolean selectOK(){
		int sel = 0;
		for(int i = 0; i < checkBoxes.length; i++){
			if(checkBoxes[i].isChecked()){
				sel++;
			}
		}
		return sel == 3;
	}
	
	private Intent makeIntent(){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		int j = 0;
		String[] slots = {SLOT_ONE, SLOT_TWO, SLOT_THREE};
		for(int i = 0; i < checkBoxes.length; i++){
			if(checkBoxes[i].isChecked()){
				bundle.putString(slots[j], checkBoxes[i].getText().toString());
				j++;
			}
		}
		return intent.putExtras(bundle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_timeslot, menu);
		return true;
	}
}
