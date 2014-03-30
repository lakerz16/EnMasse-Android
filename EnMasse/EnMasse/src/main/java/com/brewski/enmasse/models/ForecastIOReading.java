package com.brewski.enmasse.models;

import com.brewski.enmasse.R;
import com.brewski.enmasse.util.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matt on 1/13/14.
 */
public class ForecastIOReading {

    JSONObject current;
    long lastUpdated;

    public ForecastIOReading(String json) {
        try {
            current = new JSONObject(json).getJSONObject("currently");
            lastUpdated = System.currentTimeMillis();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ForecastIOReading(String json, long lastUpdated) {
        try {
            current = new JSONObject(json);
            this.lastUpdated = lastUpdated;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getTimeLastUpdated() {
        return lastUpdated;
    }

    public void resetLastUpdated() {
        lastUpdated = 0;
    }

    public String getWeatherResource() {
        if(current == null) {
            return "";
        }

        try {
            return Utilities.climaconsMapping(current.getString("icon"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "A";
    }

    public String getTemperature() {
        if(current == null) {
            return "";
        }

        try {
            return Long.toString(Math.round(current.getDouble("temperature")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPrecipitationChance() {
        if(current == null) {
            return "";
        }

        try {
            return "  " + Integer.toString((int) (current.getDouble("precipProbability") * 100)) + "%";
        } catch (JSONException e) {
            e.printStackTrace();
        }
          
        return "";
    }
}
