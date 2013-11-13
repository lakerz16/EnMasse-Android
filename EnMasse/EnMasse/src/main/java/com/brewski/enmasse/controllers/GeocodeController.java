package com.brewski.enmasse.controllers;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.brewski.enmasse.activities.EventActivity;
import com.brewski.enmasse.models.GeoLocation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by matt on 10/28/13.
 */
public class GeocodeController {

    public GeocodeController() {

    }

    private class DoGeocoding extends AsyncTask<String, String, String> {

        private Exception exception;

        protected String doInBackground(String... params) {

            String request = params[0].replace(" ", "%20");

            HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?address="+request+"&components=country:US&sensor=true");
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

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject.toString();
        }

        protected void onPostExecute(String res) {
            try {

                JSONObject response = new JSONObject(res);
                ArrayList<GeoLocation> locations = new ArrayList<GeoLocation>();

                for(int i=0; i<response.getJSONArray("results").length(); i++) {
                    GeoLocation location = new GeoLocation(response.getJSONArray("results").getJSONObject(i));
                    locations.add(location);
                }

                returnActivity.updateLocationStuff(locations);

            } catch (JSONException e) {
                //e.printStackTrace();
            }
        }
    }

    EventActivity returnActivity;

    public void getLocationInfo(EventActivity activity, String address) {

        returnActivity = activity;

        new DoGeocoding().execute(address);
    }
}
