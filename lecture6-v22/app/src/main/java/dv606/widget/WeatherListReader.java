package dv606.widget;

import java.util.ArrayList;

/**
 * Created by reshad on 2015-10-01.
 */
public class WeatherListReader {

    public static int getIdForIcon(WeatherForecast forecastBundle)
    {
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


    public boolean date_found(ArrayList<String> list, String curr_date)
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
