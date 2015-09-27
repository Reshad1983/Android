package com.example.reshad.mymp3player;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class VisitedCountry extends AppCompatActivity implements CalendarProviderClient {
    private static String Tag = "reshad.assignment.visited_country";
    RelativeLayout layout;
    private String bkg_color = "";
    private String text_color = "#000000";
    private String text_size = "15";
    private ArrayList<SelectionItems> event_id_list;
    private SelectionItems[] items;
    private ListAdapter theAdapter;
    private String country_name, sorting;
    private int visit_date;
    private ArrayList<String> country;
    public static final String COUNTRY_LIST = "COUNTRY_LIST";
private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_country);
        listview = (ListView)findViewById(R.id.list_view_id);
        Intent intent = getIntent();
        layout = (RelativeLayout)findViewById(R.id.layout_id);
        if(intent.hasExtra(COUNTRY_LIST))
        {
            country_name = intent.getStringExtra("country_name");
            visit_date = intent.getIntExtra("date_of_visit", 0);
            addNewEvent(visit_date, country_name);
            getLoaderManager().restartLoader(LOADER_MANAGER_ID, null, this);
        }
        else
        {
            country = new ArrayList<>();
            country_name = "";
            visit_date = 0;
        }
        listview.setOnItemClickListener(new ItemClick());
        registerForContextMenu(listview);
        getLoaderManager().initLoader(LOADER_MANAGER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visited_country, menu);
        return super.onCreateOptionsMenu(menu);
    }
    boolean name_reverse = false;
    boolean date_reverse = false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id)
        {
            case R.id.action_new_country_id:
                Intent intent = new Intent(getApplicationContext(), NewCountryActivity.class);
                intent.putStringArrayListExtra(COUNTRY_LIST, country);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.name_sort_id:
                name_reverse = !name_reverse;
                sorting = (!name_reverse) ? EVENTS_LIST_PROJECTION[PROJ_CALENDARS_LIST_NAME_INDEX] + " ASC" : EVENTS_LIST_PROJECTION[PROJ_CALENDARS_LIST_NAME_INDEX] + " DESC";
                getLoaderManager().restartLoader(LOADER_MANAGER_ID, null, this);
                return true;
            case R.id.date_sort_id:
                date_reverse = !date_reverse;
                sorting = (!date_reverse) ? EVENTS_LIST_PROJECTION[PROJ_EVENTS_LIST_DTSTART_INDEX] + " ASC" : EVENTS_LIST_PROJECTION[PROJ_EVENTS_LIST_DTSTART_INDEX] + " DESC";
                getLoaderManager().restartLoader(LOADER_MANAGER_ID, null, this);
                return true;
            case R.id.setting_id:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public long getMyCountriesCalendarId() {
        Cursor cursor =
                getContentResolver().
                        query(
                                CalendarContract.Calendars.CONTENT_URI,
                                new String[]{CalendarContract.Calendars._ID},
                                CalendarProviderClient.CALENDARS_LIST_SELECTION,
                                CalendarProviderClient.CALENDARS_LIST_SELECTION_ARGS,
                                null);
        if (cursor.moveToFirst())
        {
            return cursor.getLong(0);
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_TITLE);
            values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
            values.put(CalendarContract.Calendars.NAME, CALENDAR_TITLE);
            values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_TITLE);
            values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xFFFFFFFF);
            values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            values.put(CalendarContract.Calendars.OWNER_ACCOUNT, ACCOUNT_TITLE);
            values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            values.put(CalendarContract.Calendars.VISIBLE, 1);
            Uri uri = asSyncAdapter(CALENDARS_LIST_URI, ACCOUNT_TITLE, CalendarContract.ACCOUNT_TYPE_LOCAL);
            uri = getContentResolver().insert(uri, values);
            return  new Long(uri.getLastPathSegment());
        }
    }

    @Override
    public void addNewEvent(int year, String country) {

        Long id = getMyCountriesCalendarId();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, country);
        values.put(CalendarContract.Events.DTSTART, CalendarUtils.getEventStart(year));
        values.put(CalendarContract.Events.DTEND, CalendarUtils.getEventEnd(year));
        values.put(CalendarContract.Events.EVENT_TIMEZONE, CalendarUtils.getTimeZoneId());
        values.put(CalendarContract.Events.CALENDAR_ID, id);
        getContentResolver().insert(EVENTS_LIST_URI, values);
        Log.e(VisitedCountry.Tag, "New event created!");
    }

    @Override
    public void updateEvent(int eventId, int year, String country) {
        ContentValues values = new ContentValues();
        // The new title for the event
        values.put(CalendarContract.Events.TITLE, country);
        values.put(CalendarContract.Events.DTSTART, year);
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        int rows = getContentResolver().update(updateUri, values, null, null);
        getLoaderManager().restartLoader(LOADER_MANAGER_ID, null, this);
        Log.i(Tag, "Rows updated: " + rows);

    }

    @Override
    public void deleteEvent(int eventId) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        int rows = getContentResolver().delete(deleteUri, null, null);
        getLoaderManager().restartLoader(LOADER_MANAGER_ID, null, this);
        Log.i(Tag, "Rows deleted: " + rows);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id)
        {
            case CalendarProviderClient.LOADER_MANAGER_ID:
                return new CursorLoader(this, EVENTS_LIST_URI, EVENTS_LIST_PROJECTION, null, null, sorting );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        event_id_list = new ArrayList<>();
        switch(loader.getId())
        {
            case LOADER_MANAGER_ID:
                if(data.moveToFirst())
                {
                    do
                    {
                        int id = data.getInt(data.getColumnIndex(EVENTS_LIST_PROJECTION[PROJ_EVENTS_LIST_ID_INDEX]));
                        String event_name =data.getString(data.getColumnIndex(EVENTS_LIST_PROJECTION[PROJ_EVENTS_LIST_TITLE_INDEX]));
                        long dStart = data.getLong(data.getColumnIndex(EVENTS_LIST_PROJECTION[PROJ_EVENTS_LIST_DTSTART_INDEX]));
                        SelectionItems item = new SelectionItems(id, event_name, dStart);
                        event_id_list.add(item);
                    }
                    while(data.moveToNext());
                    items = toSelectionArray(event_id_list);
                    theAdapter = new CountryYearAdapter(this);
                    listview.setAdapter(theAdapter);
                }
                //this method is not used because of custom listview
                // theAdapter.swapCursor(data);

                break;
        }
    }

    private SelectionItems[] toSelectionArray(ArrayList<SelectionItems> list) {
        SelectionItems[] items = new SelectionItems[list.size()];
        int i = 0;
        for(SelectionItems item : list)
        {
            items[i++] = item;
        }
        return items;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //not implemented (Custom view)
    }


    static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build();
    }

    @Override
    protected void onStop() {
        SharedPreferences sorting_preferences = getSharedPreferences("Sorting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sorting_preferences.edit();
        editor.putString("SORTING", sorting);
        editor.commit();
        editor.apply();

        super.onStop();
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sorting_preferences = getSharedPreferences("Sorting", MODE_PRIVATE);
        sorting = sorting_preferences.getString("SORTING", "");
        text_size = sharedPref.getString("Size List", "");
        bkg_color = sharedPref.getString("Color List", "");
        text_color = sharedPref.getString("Text color List", "");
        if(!bkg_color.equals(""))
        {
            layout.setBackgroundColor(Color.parseColor(bkg_color.toString()));
        }
        int first = listview.getFirstVisiblePosition();
        int count = listview.getChildCount();
        for(int i = first; i < (count + first); i++)
        {
            View vy = listview.getChildAt(i);
            TextView cont = (TextView)vy.findViewById(R.id.visited_country);
            TextView year = (TextView)vy.findViewById(R.id.visited_year_id);
            try
            {
                cont.setTextColor(Color.parseColor(text_color));
                cont.setTextSize(Float.parseFloat(text_size));
                year.setTextColor(Color.parseColor(text_color));
                year.setTextSize(Float.parseFloat(text_size));
            }
            catch(IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }

        }

        getLoaderManager().restartLoader(LOADER_MANAGER_ID, null, this);
        super.onResume();
    }

    class CountryYearAdapter extends ArrayAdapter<SelectionItems>
    {
        public CountryYearAdapter(Context context){super(context, R.layout.country_year_layout, items);}
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            View row = getLayoutInflater().inflate(R.layout.country_year_layout, parent, false);
            TextView country_view = (TextView)row.findViewById(R.id.visited_country);
            country_view.setWidth(width / 2);
            TextView year_view = (TextView)row.findViewById(R.id.visited_year_id);
            year_view.setWidth(width / 2);
            country_view.setText(items[position].getEvent_name());
            year_view.setText("" + CalendarUtils.getEventYear(items[position].getdStart()));
            if(!text_size.equals(""))
            {
                country_view.setTextSize(Float.parseFloat(text_size));
                year_view.setTextSize(Float.parseFloat(text_size));

            }
            if (!text_color.equals(""))
            {
               country_view.setTextSize(Color.parseColor(text_color));
                year_view.setTextColor(Color.parseColor(text_color));

            }

            return row;
        }

    }


    private class ItemClick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CharSequence country_name = ((TextView)view.findViewById(R.id.visited_country)).getText();
            CharSequence year_name = ((TextView)view.findViewById(R.id.visited_year_id)).getText();

            Intent intent = new Intent(VisitedCountry.this, UpdateCountry.class);
            intent.putExtra("Country name", country_name);
            intent.putExtra("year", year_name);
            int event_id =  getEventId(event_id_list, country_name.toString());
            intent.putExtra("id", event_id);
            startActivity(intent);
        }
        private int getEventId(ArrayList<SelectionItems> list, String event_name)
        {
            if(list == null)
            {
                return -1;
            }
            for(SelectionItems item : list)
            {
                if(item.getEvent_name().equals(event_name))
                {
                    return item.getId();
                }
            }
            return -1;
        }
    }
}
