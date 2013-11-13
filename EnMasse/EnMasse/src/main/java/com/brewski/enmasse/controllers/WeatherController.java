package com.brewski.enmasse.controllers;

import com.brewski.enmasse.R;

import java.util.Random;

/**
 * Created by matt on 11/4/13.
 */
public class WeatherController {

    public static final int WEATHER_NONE = 0;
    public static final int WEATHER_CLOUDY = 1;
    public static final int WEATHER_DRIZZLE = 2;
    public static final int WEATHER_HAZE = 3;
    public static final int WEATHER_MOSTLYCLOUDY = 4;
    public static final int WEATHER_RAIN = 5;
    public static final int WEATHER_SNOW = 6;
    public static final int WEATHER_SUNNY = 7;
    public static final int WEATHER_THUNDERSTORMS = 8;

    public static int GetWeather(String lat, String lng) {
        // TODO Do a request for the weather.  We'll probably want to have the server do this though

        Random rand = new Random();
        return (rand.nextInt() % 8) + 1;
    }

    public static int GetWeatherResource(int state) {
        switch(state) {
            case WEATHER_NONE:
                return 0;
            case WEATHER_CLOUDY:
                return R.drawable.weather_cloudy;
            case WEATHER_DRIZZLE:
                return R.drawable.weather_drizzle;
            case WEATHER_HAZE:
                return R.drawable.weather_haze;
            case WEATHER_MOSTLYCLOUDY:
                return R.drawable.weather_mostlycloudy;
            case WEATHER_RAIN:
                return R.drawable.weather_rain;
            case WEATHER_SNOW:
                return R.drawable.weather_snow;
            case WEATHER_SUNNY:
                return R.drawable.weather_sunny;
            case WEATHER_THUNDERSTORMS:
            default:
                return R.drawable.weather_thunderstorms;
        }
    }
}
