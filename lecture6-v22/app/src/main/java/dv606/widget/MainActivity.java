package dv606.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{
TextView halmstad_view, stockholm_view, vaxjo_view;

    public static final String CITY_NAME = "city_name";
    public static final String PREFS_NAME
            = "io.appium.android.apis.appwidget.ExampleAppWidgetProvider";
    public static final String PREF_CITY_NAME = "city name_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetPrefix;
    VaderService vaderService;
    WeatherReport report;
    Intent serviceIntent;
    private List<WeatherForecast> weather_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        halmstad_view = (TextView)findViewById(R.id.halmstad_id);
        stockholm_view = (TextView)findViewById(R.id.stockholm_id);
        vaxjo_view = (TextView)findViewById(R.id.vaxjo_id);

        vaxjo_view.setOnClickListener(this);
        halmstad_view.setOnClickListener(this);
        stockholm_view.setOnClickListener(this);

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
    protected void onStart() {
        super.onStart();


        if(serviceIntent == null)
        {
            serviceIntent = new Intent(this, VaderService.class);
            bindService(serviceIntent, serviceConnection, MainActivity.BIND_AUTO_CREATE);
            startService(serviceIntent);

        }
    }

    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        final Context context = MainActivity.this;
        final String cityName;
        switch(v.getId())
        {
            case R.id.halmstad_id:

                cityName = "Halmstad";
                vaderService.getWeatherReport(cityName);

                try
                {

                    report = vaderService.getReport();
                }
                catch(NullPointerException e)
                {
                    e.printStackTrace();
                }
                weather_list = report.weatherForecastList();

                saveCityName(context, mAppWidgetId, cityName);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_widget);
                views.setImageViewResource(R.id.icon_view, WeatherListReader.getIdForIcon(weather_list.get(0)));
                views.setTextViewText(R.id.cityName_id, cityName);
                views.setTextViewText(R.id.temp_id, weather_list.get(0).getTemperature()+" c");
                manager.updateAppWidget(mAppWidgetId, views);



               // manager.updateAppWidget(context, app);

                Intent result = new Intent();
                result.putExtra("CITY", cityName);
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, result);
                finish();
            break;
            case R.id.stockholm_id:
            break;
            case R.id.vaxjo_id:
            break;
            default:
                break;
        }


    }

    static void saveCityName(Context context, int mAppWidgetId, String cityName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString(PREF_CITY_NAME, cityName);
        editor.commit();
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            VaderService.ServiceBinder binder = (VaderService.ServiceBinder)service;
            vaderService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            vaderService = null;
        }
    };
}
