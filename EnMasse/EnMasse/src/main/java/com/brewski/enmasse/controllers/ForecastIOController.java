package com.brewski.enmasse.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.brewski.enmasse.models.Event;
import com.brewski.enmasse.models.ForecastIOReading;
import com.brewski.enmasse.models.WeatherReading;
import com.brewski.enmasse.views.EventCard;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by matt on 1/13/14.
 */
public class ForecastIOController {

    private static String key = "f84b66c211f075b07365219b85a7192d";

    private EventCard returnCard;

    public ForecastIOController() {

    }

    public void getWeatherInfo(EventCard card, Event event) {
        returnCard = card;

        new GoGetWeather().execute(event.GetLat(), event.GetLon(), Long.toString(event.GetDateSeconds()));
    }

    private class GoGetWeather extends AsyncTask<String, String, String> {

        private Exception exception;

        protected String doInBackground(String... params) {

            String request = String.format("https://api.forecast.io/forecast/%s/%s,%s,%s",
                    key,params[0],params[1],params[2]);

            //Log.e("Request", request);

            HttpGet httpGet = new HttpGet(request);
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
            ForecastIOReading reading = new ForecastIOReading(res);
            returnCard.UpdateWeather(reading);
        }
    }

}
