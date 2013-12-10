package com.brewski.enmasse.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matt on 10/28/13.
 */
public class GeoLocation {

    private String name;
    private String lat;
    private String lng;

    public GeoLocation(JSONObject object) {

        try {
            name = object.getString("formatted_address");
            lat = object.getJSONObject("geometry").getJSONObject("location").getString("lat");
            lng = object.getJSONObject("geometry").getJSONObject("location").getString("lng");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String GetName() { return name; }
    public String GetLat() { return lat; }
    public String GetLng() { return lng; }
    public String GetCoordinates() { return lat + "," + lng; }
}
