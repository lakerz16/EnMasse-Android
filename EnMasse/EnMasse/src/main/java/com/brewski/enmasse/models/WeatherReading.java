package com.brewski.enmasse.models;

import android.util.Log;

import com.brewski.enmasse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matt on 12/9/13.
 */
public class WeatherReading {

    private int state;

    public WeatherReading(String json) {

        try {
            JSONObject o = new JSONObject(json);
            JSONArray w = o.getJSONArray("weather");
            JSONObject f = w.getJSONObject(0);
            state = f.getInt("id")/100;
            //Log.e("Weather State", Integer.toString(state));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int GetWeatherResource() {
        switch(state) {
            case WEATHER_NONE:
                return 0;
            //case WEATHER_CLOUDY:
            //    return R.drawable.weather_cloudy;
            case WEATHER_DRIZZLE:
                return R.drawable.weather_drizzle;
            //case WEATHER_HAZE:
            //    return R.drawable.weather_haze;
            //case WEATHER_MOSTLYCLOUDY:
            //    return R.drawable.weather_mostlycloudy;
            case WEATHER_RAIN:
                return R.drawable.weather_rain;
            case WEATHER_SNOW:
                return R.drawable.weather_snow;
            //case WEATHER_SUNNY:
            //    return R.drawable.weather_sunny;
            case WEATHER_THUNDERSTORMS:
                return R.drawable.weather_thunderstorms;
            default:
                return R.drawable.weather_sunny;
        }
    }

    //public static final int WEATHER_NONE = 0;
    //public static final int WEATHER_CLOUDY = 1;
    //public static final int WEATHER_DRIZZLE = 2;
    //public static final int WEATHER_HAZE = 3;
    //public static final int WEATHER_MOSTLYCLOUDY = 4;
    //public static final int WEATHER_RAIN = 5;
    //public static final int WEATHER_SNOW = 6;
    //public static final int WEATHER_SUNNY = 7;
    //public static final int WEATHER_THUNDERSTORMS = 8;

    // OWM Mappings
    public static final int WEATHER_NONE = 0;
    public static final int WEATHER_THUNDERSTORMS = 2;
    public static final int WEATHER_DRIZZLE = 3;
    public static final int WEATHER_RAIN = 5;
    public static final int WEATHER_SNOW = 6;

}
