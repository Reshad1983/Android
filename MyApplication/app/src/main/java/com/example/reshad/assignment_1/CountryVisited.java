package com.example.reshad.assignment_1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CountryVisited extends AppCompatActivity {
        private ListView listView;
    private String country_name;
    private int visit_date;
        public static final String COUNTRY_LIST = "COUNTRY_LIST";
        private MyNewAdapter adapter;
    private ArrayList<String> country;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_action_bar);
            listView = (ListView)findViewById(R.id.listView);
            Intent intent = getIntent();
            if(intent.hasExtra(COUNTRY_LIST))
            {
                country = intent.getStringArrayListExtra(COUNTRY_LIST);
                country_name = intent.getStringExtra("country_name");
                visit_date = intent.getIntExtra("date_of_visit", 0);
            }
            else
            {
                country = new ArrayList<String>();
                country_name = "";
                visit_date = 0;
            }
            if(!country_name.equals(""))
            {
                country.add(country_name + " " + visit_date);
            }
            adapter = new MyNewAdapter(this, R.layout.list_item);
            registerForContextMenu(listView);

            for(String country_visited : country)
            {
                adapter.add(country_visited);
                listView.setAdapter(adapter);

            }

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_visited_country, menu);
            return true;
        }
        public static final int RESTORE = 0;
        public static final int TEN = 1;
        public static final int TWENTY = 2;

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo)
        {
            menu.setHeaderTitle("Select List Row Separation");
            menu.add(0, TEN, 0, "10 pixels");
            menu.add(0, TWENTY, 0, "20 pixels");
            menu.add(0, RESTORE, 0, "Restore originals");
        }

        @Override
        public boolean onContextItemSelected(MenuItem item)
        {
            switch (item.getItemId()) {
                case RESTORE:
                    listView.setDividerHeight(1);
                    return true;
                case TEN:
                    listView.setDividerHeight(10);
                    return true;
                case TWENTY:
                    listView.setDividerHeight(20);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
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
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    class MyNewAdapter  extends ArrayAdapter<String>
    {
        public MyNewAdapter (Context context, int textViewresurceId)
        {
            super(context, textViewresurceId);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView vy =  (TextView)super.getView(position, convertView, parent);
            String object = getItem(position);
                vy.setText(object);
                vy.setBackgroundColor(Color.WHITE);
                vy.setTextColor(Color.BLACK);
            return vy;
        }
    }
    }
