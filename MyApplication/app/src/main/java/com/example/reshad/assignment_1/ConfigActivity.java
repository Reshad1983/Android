package com.example.reshad.assignment_1;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ConfigActivity extends Activity implements View.OnClickListener{
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private AppWidgetManager manager;
    public static final String PREFS_NAME
            = "io.appium.android.apis.appwidget.ExampleAppWidgetProvider";
TextView halmstad_view, vaxjo_view;
        //, stockholm_view, ;
    private RemoteViews views;

    public static final String CITY_NAME = "city_name";
    public static final String PREF_CITY_NAME = "city name_";
    private List<WeatherForecast> weather_list;
    private String cityName;

    /*
    EditText mAppWidgetPrefix;
    WeatherReport report;
    private String cityName;
    Intent serviceIntent;
    private List<WeatherForecast> weather_list;
    private URL city_xml;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);
        /*halmstad_view = (TextView)findViewById(R.id.halmstad_id);
        stockholm_view = (TextView)findViewById(R.id.stockholm_id);
        */
        vaxjo_view = (TextView)findViewById(R.id.vaxjo_id);

        vaxjo_view.setOnClickListener(this);
       /* halmstad_view.setOnClickListener(this);
        stockholm_view.setOnClickListener(this);
*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            mAppWidgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if(mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        final Context context = ConfigActivity.this;
        manager = AppWidgetManager.getInstance(context);
        views = new RemoteViews(context.getPackageName(), R.layout.weather_widget_layout);
        switch(v.getId())
        {
            /*case R.id.halmstad_id:

                cityName = "Halmstad";
                try
                {
                    city_xml = new URL(WeatherListReader.cities[WeatherListReader.getPosition(cityName)]);
                }
                catch(MalformedURLException e)
                {
                    e.printStackTrace();
                }
                AsyncTask weatherRetriever = new WeatherRetriever().execute();
                //weather_list = report.weatherForecastList();

                saveCityName(context, mAppWidgetId, cityName);




               // manager.updateAppWidget(context, app);


            break;
            case R.id.stockholm_id:
            break;*/
            case R.id.vaxjo_id:
                cityName = "Vaxjo";
                saveCityName(context, mAppWidgetId, cityName);
                AsyncTask weatherReport = new WeatherRetriever().execute();
            break;
            default:
                break;
        }


    }

    private void finishThisActivity()
    {
        Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, result);
        finish();
    }

    static void saveCityName(Context context, int mAppWidgetId, String cityName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString(PREF_CITY_NAME, cityName);
        editor.commit();
    }







   private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport>
    {
        protected WeatherReport doInBackground(URL... urls) {
            URL city_xml = null;
            try
            {
                city_xml = new URL(WeatherListReader.cities[WeatherListReader.getPosition(cityName)]);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            try {
                WeatherReport new_report = WeatherHandler.getWeatherReport(city_xml);
                return new_report;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        protected void onProgressUpdate(Void... progress) {

        }

        protected void onPostExecute(WeatherReport result) {
            Toast.makeText(getApplicationContext(), "WeatherRetriever task finished", Toast.LENGTH_LONG).show();
            weather_list = result.weatherForecastList();
            //views.setImageViewResource(R.id.icon_view, WeatherListReader.getIdForIcon(weather_list.get(0)));
            //views.setTextViewText(R.id.appwidget_city, cityName);
            //views.setTextViewText(R.id.appwidget_temp, weather_list.get(0).getTemperature()+" c");
            manager.updateAppWidget(mAppWidgetId, views);

            finishThisActivity();
        }
    }
}
