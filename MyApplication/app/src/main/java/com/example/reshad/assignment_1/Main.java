package com.example.reshad.assignment_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Main extends AppCompatActivity implements View.OnClickListener{
TextView simple_btn, dynamic_btn, visited_view, bmi_veiw, vaxjo_weather_txt, beer_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dynamic_btn = (TextView)findViewById(R.id.bmi_id);
        bmi_veiw = (TextView)findViewById(R.id.bmi_id);
        beer_txt = (TextView)findViewById(R.id.beer_id);
        vaxjo_weather_txt = (TextView)findViewById(R.id.vaxjoweather_id);
        visited_view = (TextView)findViewById(R.id.visited_id);
        simple_btn = (TextView)findViewById(R.id.randomnumber_id);
        simple_btn.setOnClickListener(this);
        visited_view.setOnClickListener(this);
        vaxjo_weather_txt.setOnClickListener(this);
        dynamic_btn.setOnClickListener(this);
        bmi_veiw.setOnClickListener(this);
        beer_txt.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.randomnumber_id:
                startActivity(new Intent(getApplicationContext(), RandomNumberActivity.class));
                break;
                case R.id.visited_id:
                startActivity(new Intent(getApplicationContext(), CountryVisited.class));
                break;
            case R.id.bmi_id:
                startActivity(new Intent(getApplicationContext(), BMICalculatorActivity.class));
                break;

            case R.id.vaxjoweather_id:
                startActivity(new Intent(getApplicationContext(), VaxjoWeather.class));
                break;

            case R.id.beer_id:
                startActivity(new Intent(getApplicationContext(), AdaptLayoutActivity.class));
                break;
        }
    }


}
