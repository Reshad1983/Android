package com.example.reshad.mymp3player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

public class NewAlarmActivity extends AppCompatActivity {
TimePicker timePicker;
    Calendar c;
    int currentApiVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Stockholm"));
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        currentApiVersion= android.os.Build.VERSION.SDK_INT;



        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
        Button alarm_btn = (Button)findViewById(R.id.set_alarm_id);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int hour = 0;
                int min = 0;

                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
                    hour = timePicker.getHour();
                    min = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    min = timePicker.getCurrentMinute();
                }


                Intent intent = new Intent(NewAlarmActivity.this, AlarmActivity.class);
                intent.putExtra(AlarmActivity.HOUR, hour);
                intent.putExtra(AlarmActivity.MINUTE, min);
                startActivity(intent);
                NewAlarmActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
