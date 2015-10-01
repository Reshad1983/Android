package dv606.widget;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class VaderService extends Service {

    private String [] cities = new String[]{"http://www.yr.no/sted/Sverige/Kronoberg/V%E4xj%F6/forecast.xml",
            "http://www.yr.no/sted/Sverige/Stockholm/Stockholm/forecast.xml",
            "http://www.yr.no/sted/Sverige/Halland/Halmstad/forecast.xml"};
    WeatherReport weatherReprot;
    String cityName;
    ServiceBinder binder = new ServiceBinder();
    URL city_xml;
    private boolean readyToReturn  = false;

    public VaderService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public boolean isReady()
    {
        return readyToReturn;
    }
    private int getPosition(String city_name) {
        switch(city_name)
        {
            case "Halmstad":
                return 0;
            case "Stockholm":
                return 1;
            case "Vaxjo":
                return 2;
            default:
                return 0;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        binder = null;
        return true;
    }

    public void getWeatherReport(String cityName)
    {
        try
        {
            city_xml = new URL(cities[getPosition(cityName)]);

        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        AsyncTask task = new WeatherRetriever().execute();
    }

    public WeatherReport getReport(){return weatherReprot;}

    public class ServiceBinder extends Binder{
        VaderService getService(){return VaderService.this;}
    }


    private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport>
    {
        protected WeatherReport doInBackground(URL... urls) {
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
            readyToReturn = true;
            weatherReprot = result;
        }
    }
}
