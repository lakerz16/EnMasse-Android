package com.brewski.enmasse.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.brewski.enmasse.R;
import com.brewski.enmasse.activities.EventActivity;
import com.brewski.enmasse.models.Event;
import com.brewski.enmasse.models.GeoLocation;
import com.brewski.enmasse.models.WeatherReading;
import com.brewski.enmasse.util.Utilities;
import com.brewski.enmasse.views.EventCard;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by matt on 11/4/13.
 */
public class WeatherController {

    EventCard returnCard;
    public static final String owm_key = "71eaab57b00ccedd9ddf0ec4d16c5d36";

    public WeatherController() {
    }

    public void getWeatherInfo(EventCard card, Event event) {

        if(!Utilities.ShouldCheckWeather(event)) {
            return;
        }

        returnCard = card;

        new GoGetWeather().execute(event.GetLat(), event.GetLon());
    }

    private class GoGetWeather extends AsyncTask<String, String, String> {

        private Exception exception;

        protected String doInBackground(String... params) {

            StringBuilder request = new StringBuilder();
            request.append("http://api.openweathermap.org/data/2.5/forecast?lat=");
            request.append(params[0]);
            request.append("&lon=");
            request.append(params[1]);
            request.append("&APPID=");
            request.append(owm_key);

            HttpGet httpGet = new HttpGet(request.toString());
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            return stringBuilder.toString();
        }

        protected void onPostExecute(String res) {
            WeatherReading weather = new WeatherReading(res);
            returnCard.UpdateWeather(weather);
        }
    }

}
