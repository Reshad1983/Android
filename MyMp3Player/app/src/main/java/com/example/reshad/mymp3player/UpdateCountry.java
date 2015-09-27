package com.example.reshad.mymp3player;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateCountry extends AppCompatActivity {
    EditText country_txt, year_txt;
    String country, year_string;
    int event_id;
    private static final String DEBUG_TAG = "UpdateCountry";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_country);
        country_txt = (EditText)findViewById(R.id.country_id);
        year_txt = (EditText)findViewById(R.id.year_id);

        Intent intent = getIntent();
        country = intent.getStringExtra("Country name");
        event_id = intent.getIntExtra("id", -1);
        year_string = intent.getStringExtra("year");
        country_txt.setText(country, TextView.BufferType.EDITABLE);
        year_txt.setText(year_string, TextView.BufferType.EDITABLE);

        Button del_btn = (Button)findViewById(R.id.delete_button_id);
        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(event_id);
                Toast.makeText(getApplicationContext(), country + " has been deleted!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                country = country_txt.getText().toString();
                long dStart = CalendarUtils.getEventStart(Integer.parseInt(year_txt.getText().toString()));
                int year = CalendarUtils.getEventYear(dStart);
                updateEvent(event_id, year, country.toString());
                Toast.makeText(getApplicationContext(), country + " has been updated!", Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_country, menu);
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
    public void updateEvent(int eventId, int year, String country) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri updateUri = null;
        // The new title for the event
        values.put(CalendarContract.Events.TITLE, country);
        values.put(CalendarContract.Events.DTSTART, CalendarUtils.getEventStart(year));
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        int rows = getContentResolver().update(updateUri, values, null, null);
        Log.i(DEBUG_TAG, "Rows updated: " + rows);
    }

    public void deleteEvent(int eventId) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        int rows = getContentResolver().delete(deleteUri, null, null);

        Log.i(DEBUG_TAG, "Rows deleted: " + rows);
    }
}
