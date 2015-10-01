/**
 * VaxjoWeather.java
 * Created: May 9, 2010
 * Jonas Lundberg, LnU
 */

package com.example.reshad.assignment_1;

import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is a first prototype for a weather app. It is currently 
 * only downloading weather data for Växjö. 
 * 
 * This activity downloads weather data and constructs a WeatherReport,
 * a data structure containing weather data for a number of periods ahead.
 * 
 * The WeatherHandler is a SAX parser for the weather reports 
 * (forecast.xml) produced by www.yr.no. The handler constructs
 * a WeatherReport containing meta data for a given location
 * (e.g. city, country, last updated, next update) and a sequence 
 * of WeatherForecasts.
 * Each WeatherForecast represents a forecast (weather, rain, wind, etc)
 * for a given time period.
 * 
 * The next task is to construct a list based GUI where each row 
 * displays the weather data for a single period.
 * 
 *  
 * @author jlnmsi
 *
 */

public class VaxjoWeather extends ListActivity {
	private ListView listView;
	public static String TAG = "dv606.weather";
	private InputStream input;
	private WeatherReport report = null;
	private WeatherForecast[]report_list;
	private String [] cities = new String[]{"http://www.yr.no/sted/Sverige/Kronoberg/V%E4xj%F6/forecast.xml",
	"http://www.yr.no/sted/Sverige/Stockholm/Stockholm/forecast.xml",
			"http://www.yr.no/sted/Sverige/Halland/Halmstad/forecast.xml"};
    public static final String FORECAST_LIST = "VAXJO_WEATHER_FORECAST";
    public static  final String START_DATE = "VAXJO_WEATHER_STARTDATE";
    public static final String BUNDL_EXTRA= "VAXJO_WEATHER_BUNDLE";
    private ListAdapter adapt;
    private ArrayList<String > start_date;
    private ArrayList<WeatherForecast> day_forecast;
    public WeatherForecast [] forecast_array;
    private List<WeatherForecast> weather_list;
    private String cityName = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        start_date = new ArrayList<String>();
        day_forecast = new ArrayList<WeatherForecast>();
        Intent fromWidget = getIntent();
        cityName = fromWidget.getStringExtra("City");
        	AsyncTask task = new WeatherRetriever().execute();

    }

    private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport>
	{
    	protected WeatherReport doInBackground(URL... urls) {
            if(cityName == null)
            {
                cityName = "Vaxjo";
            }
    		try {
        	    URL url = new URL(cities[WeatherListReader.getPosition(cityName)]);
				WeatherReport new_report = WeatherHandler.getWeatherReport(url);
    			return new_report;
    		} catch (Exception e) {
    			throw new RuntimeException(e);
    		} 
    	}

    	protected void onProgressUpdate(Void... progress) {

    	}

    	protected void onPostExecute(WeatherReport result) {
			Toast.makeText(getApplicationContext(), "WeatherRetriever task finished", Toast.LENGTH_LONG).show();

    		report = result;
            weather_list = result.weatherForecastList();
            for(WeatherForecast day_data : weather_list)
            {
                if(!date_found(start_date, day_data.getStartYYMMDD()))
                {
                    start_date.add(day_data.getStartYYMMDD());
                    day_forecast.add(day_data);
                }
            }
            start_date = null;
            forecast_array = day_forecast.toArray(new WeatherForecast[day_forecast.size()]);
            adapt = new MultiAdapter(getApplicationContext());
	        /* Configure this ListActivity */
            setListAdapter(adapt);
    	}
    }



    //--------------------------------------------------------------------------------------------------
    class MultiAdapter  extends ArrayAdapter<WeatherForecast> implements View.OnClickListener
    {
        Random rand = new Random();
        public MultiAdapter(Context context) {
            super(context,R.layout.multi_row, forecast_array);
        }

        @Override   // Called when updating the ListView
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            if (convertView == null) {	// Create new row view object
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.weather_activity,parent,false);
            }
            else
            // reuse old row view to save time/battery
            {
                row = convertView;
            }
			/* Add new data to row object */
            TextView date_txt = (TextView)row.findViewById(R.id.date_id);
            date_txt.setWidth(width / 4 + 50);
            date_txt.setText(forecast_array[position].getStartYYMMDD());

            TextView temp_txt = (TextView)row.findViewById(R.id.temp_text_id);
            temp_txt.setWidth(width / 4 - 50);
            String temperature = forecast_array[position].getTemperature() + "";
            temp_txt.setText(temperature);

            ImageView weather_icon = (ImageView)row.findViewById(R.id.day_id);
            int id = WeatherListReader.getIdForIcon(forecast_array[position]);
            weather_icon.setBackgroundResource(id);

            TextView klarhet_txt = (TextView)row.findViewById(R.id.klarhet_id);
            klarhet_txt.setWidth(width / 4);
            klarhet_txt.setText(forecast_array[position].getWeatherName());

            TextView vind_txt = (TextView)row.findViewById(R.id.wind_text_id);
            vind_txt.setWidth(width / 4);
            vind_txt.setText(forecast_array[position].getWindSpeedName());
            row.setOnClickListener(this);
            return row;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), OneDayWeather.class);
            ArrayList<ForecastBundle> forecast_bundle = new ArrayList<ForecastBundle>();
            for(WeatherForecast forecast : weather_list)
            {
                forecast_bundle.add( new ForecastBundle(forecast.getStartHHMM(), forecast.getEndHHMM(),
                        forecast.getStartYYMMDD(), forecast.getEndYYMMDD(),
                        forecast.getPeriodCode(), forecast.getWeatherCode(),
                        forecast.getWeatherName(), forecast.getWindDirection(),
                        forecast.getWindDirectionName(), forecast.getRain(),
                        forecast.getWindSpeed(),forecast.getWindSpeedName(),forecast.getTemperature()));

            }
            intent.putParcelableArrayListExtra(FORECAST_LIST, forecast_bundle);
            TextView vy = (TextView)v.findViewById(R.id.date_id);
            intent.putExtra(START_DATE, vy.getText().toString());
            startActivity(intent);
        }
    }
    //--------------------------------------------------------------------------------------------------

	private  boolean date_found(ArrayList<String> list, String curr_date)
	{
		if(list.size() == 0)
		{
			return false;
		}
		for(String date : list)
		{
			if(date.matches(curr_date))
			{
				return true;
			}
		}
		return false;
	}


}