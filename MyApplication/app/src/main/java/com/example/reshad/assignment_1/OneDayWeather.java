/**
 * 
 */
package com.example.reshad.assignment_1;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author jlnmsi
 *
 */
public class OneDayWeather extends ListActivity {

	private ForecastBundle []forecast_array;
	private ArrayList<ForecastBundle> forecast_arrayList;
	private ListAdapter adapt;
	/** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        /* Setup ListAdapter */
			forecast_arrayList = new ArrayList<ForecastBundle>();
			Intent intent = getIntent();
			ArrayList<ForecastBundle> forecast_list = intent.getParcelableArrayListExtra(VaxjoWeather.FORECAST_LIST);
			String start_date = intent.getStringExtra(VaxjoWeather.START_DATE);
			for(ForecastBundle day_data : forecast_list)
			{
				if(start_date.equals(day_data.getStartDate()))
				{
					forecast_arrayList.add(day_data);

				}
			}
			forecast_array = forecast_arrayList.toArray(new ForecastBundle[forecast_arrayList.size()]);
			adapt = new MultiAdapter(getApplicationContext());
	        /* Configure this ListActivity */
			setListAdapter(adapt);
	        /* Configure this ListActivity */
	        setListAdapter(adapt);  
	    }

	//--------------------------------------------------------------------------------------------------
	class MultiAdapter  extends ArrayAdapter<ForecastBundle>
	{
		Random rand = new Random();
		public MultiAdapter(Context context) {
			super(context,R.layout.multi_row, forecast_array);
		}

		@Override   // Called when updating the ListView
		public View getView(int position, View convertView, ViewGroup parent)
        {
			View row;
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			if (convertView == null) {	// Create new row view object
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.day_weather_layout,parent,false);
			}
			else
			// reuse old row view to save time/battery
			{
				row = convertView;
			}
			/* Add new data to row object */
			TextView date_txt = (TextView)row.findViewById(R.id.starttime_id);
			date_txt.setWidth(width / 4 - 10);
			date_txt.setText("From " + forecast_array[position].getStartHoure() +"\n"+
                    "Until " + forecast_array[position].getEndHoure());

            ImageView weather_icon = (ImageView) row.findViewById(R.id.day_image_id);
            int id = getIdForIcon(forecast_array[position]);
            weather_icon.setBackgroundResource(id);

			TextView temp_txt = (TextView)row.findViewById(R.id.temp_text_id);
			temp_txt.setWidth(width / 4);
			String temperature = forecast_array[position].getTemperature() + "";
			temp_txt.setText(temperature + " deg");

			TextView klarhet_txt = (TextView)row.findViewById(R.id.klarhet_id);
			klarhet_txt.setWidth(width / 4 - 10);
			klarhet_txt.setText(forecast_array[position].getWeatherName());

			TextView vind_txt = (TextView)row.findViewById(R.id.wind_text_id);
			vind_txt.setWidth(width / 4);
			vind_txt.setText(forecast_array[position].getWindSpeedName() + " " + forecast_array[position].getWindDirection() + " " +forecast_array[position].getWindSpeed() + " m/s");
			return row;
		}
	}
    private int getIdForIcon(ForecastBundle forecastBundle)
    {
        String start_date = forecastBundle.getStartHoure();
        String vader = forecastBundle.getWeatherName();
        switch (vader)
        {
            case "Delvis skyet":
                return R.drawable.latt_molnigt;
            case "Lettskyet":
                return R.drawable.latt_molnigt;
            case "Skyet":
                return R.drawable.mest_molnigt;
            case "Klarvær":
                return R.drawable.klart;
            case "Kraftig regn":
                return R.drawable.regn;
            case "Lett regn":
                return R.drawable.latt_regn;
            case "Regn":
                return R.drawable.regn;
            default:
                return R.drawable.regn;

        }
    }

    //--------------------------------------------------------------------------------------------------
}
