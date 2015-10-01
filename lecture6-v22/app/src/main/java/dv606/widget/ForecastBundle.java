package dv606.widget;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by reshad on 2015-09-09.
 */
public class ForecastBundle implements Parcelable {
    private String startTime, endTime;
    private String startDate, endDate;
    private int period_code, weather_code;
    private String weather_name;  // Name in Norwegian!
    private String wind_direction, direction_name; // Name in Norwegian!
    private double rain, wind_speed;
    private String speed_name;
    private int temperature;




    public ForecastBundle(String startTime, String endTime, String startDate, String endDate,
                          int period_code, int weather_code,
                          String weather_name,
                          String wind_direction, String direction_name,
                          double rain, double wind_speed,
                          String speed_name, int temperature)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weather_code = weather_code;
        this.period_code = period_code;
        this.weather_name = weather_name;
        this.wind_direction = wind_direction;
        this.direction_name = direction_name;
        this.rain = rain;
        this.wind_speed = wind_speed;
        this.speed_name = speed_name;
        this.temperature = temperature;
    }
    private ForecastBundle(Parcel parcel)
    {
        startTime = parcel.readString();
        endTime = parcel.readString();
        startDate = parcel.readString();
        endDate = parcel.readString();
        weather_code = parcel.readInt();
        period_code = parcel.readInt();
        weather_name = parcel.readString();
        wind_direction =  parcel.readString();
        direction_name = parcel.readString();
        rain = parcel.readDouble();
        wind_speed = parcel.readDouble();
        speed_name = parcel.readString();
        temperature = parcel.readInt();
    }


    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeInt(weather_code);
        parcel.writeInt(period_code);
        parcel.writeString(weather_name);
        parcel.writeString(wind_direction);
        parcel.writeString(direction_name);
        parcel.writeDouble(rain);
        parcel.writeDouble(wind_speed);
        parcel.writeString(speed_name);
        parcel.writeInt(temperature);


    }

    /* Time period */
    public String getStartDate() {return startDate;}
    public String getStartHoure() {return startTime;}
    public String getEndDate() {return endDate;}
    public String getEndHoure() {return endTime;}
    public int getPeriodCode() {return period_code;}
    /* Weather */
    public String getWeatherName() {return weather_name;}
    public int getWeatherCode() {return weather_code;}
    /* Rain (mm/h), Temp (Celsius)*/
    public double getRain() {return rain;}
    public int getTemperature() {return temperature;}
    /* Wind */
    public String getWindDirection() {return wind_direction;}
    public String getWindDirectionName() {return direction_name;}
    public double getWindSpeed() {return wind_speed;}
    public String getWindSpeedName() {return speed_name;}

    public int describeContents() {
        return 0;  // TODO: Customise this generated block
    }

    public static Creator<ForecastBundle> CREATOR = new Creator<ForecastBundle>() {
        public ForecastBundle createFromParcel(Parcel parcel) {
            return new ForecastBundle(parcel);
        }

        public ForecastBundle[] newArray(int size) {
            return new ForecastBundle[size];
        }
    };
}
