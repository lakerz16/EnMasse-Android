package com.brewski.enmasse.models;

import android.util.Log;

import com.brewski.enmasse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by matt on 12/9/13.
 */
public class WeatherReading {

    JSONArray forecastList = null;

    public WeatherReading(String json) {
        try {
            JSONObject foo = new JSONObject(json);

            //Log.e("Foo", foo.getJSONObject("city").toString());

            forecastList = new JSONObject(json).getJSONArray("list");
        } catch (JSONException e) {

        }
    }

    /*public WeatherReading(String json) {

        try {
            JSONObject o = new JSONObject(json);
            JSONArray w = o.getJSONArray("weather");
            JSONObject f = w.getJSONObject(0);
            state = f.getInt("id")/100;
            //Log.e("Weather State", Integer.toString(state));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

    public int GetWeatherResource(long dateMillis) {

        if(forecastList == null) {
            return R.drawable.weather_sunny;
        }

        JSONObject eventWeather = getClosestWeatherObject(dateMillis);

        switch(getWeatherState(eventWeather)) {
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

    private int getWeatherState(JSONObject o) {
        int state = 0;
        try {
            JSONArray w = o.getJSONArray("weather");
            JSONObject f = w.getJSONObject(0);
            state = f.getInt("id")/100;
            Log.e("Weather State", Integer.toString(state));
            return state;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }

    private JSONObject getClosestWeatherObject(long dateMillis) {

        try {

            for(int i=0; i<forecastList.length(); i++) {

                if(dateMillis <= forecastList.getJSONObject(i).getLong("dt")*1000) {
                    Log.e("Event Time", new Date(dateMillis).toLocaleString().toString());
                    Log.e("Weather Time", forecastList.getJSONObject(i).getString("dt_txt"));
                    return forecastList.getJSONObject(0);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String GetTemperature(long dateMillis) {

        JSONObject eventWeather = getClosestWeatherObject(dateMillis);

        try {
            JSONObject main = eventWeather.getJSONObject("main");
            double kelvin = main.getDouble("temp");
            return Integer.toString( (int) ((kelvin - 273.15) * 1.8 + 32) );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    // OWM Mappings
    public static final int WEATHER_NONE = 0;
    public static final int WEATHER_THUNDERSTORMS = 2;
    public static final int WEATHER_DRIZZLE = 3;
    public static final int WEATHER_RAIN = 5;
    public static final int WEATHER_SNOW = 6;
}
