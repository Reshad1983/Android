package com.example.reshad.assignment_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NewCountryActivity extends AppCompatActivity {
    EditText country, date;
    Button insert_btn;
    ArrayList<String> country_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_country);

        country = (EditText)findViewById(R.id.country_name_id);
        date = (EditText)findViewById(R.id.date_id);
        insert_btn = (Button)findViewById(R.id.button);
        Intent intent = getIntent();
        country_list = intent.getStringArrayListExtra(CountryVisited.COUNTRY_LIST);
        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String country_name = (country.getText()).toString();
                try
                {
                    int visit_date = Integer.parseInt((date.getText()).toString());

                    Intent intent = new Intent(getApplicationContext(), CountryVisited.class);
                    intent.putExtra("country_name", country_name);
                    intent.putExtra("date_of_visit", visit_date);
                    intent.putStringArrayListExtra(CountryVisited.COUNTRY_LIST, country_list);
                    startActivity(intent);
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(getApplicationContext(), "Insert the date", Toast.LENGTH_LONG).show();
                    date.setText(null);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_country, menu);
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
