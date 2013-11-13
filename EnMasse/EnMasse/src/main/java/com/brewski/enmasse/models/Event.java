package com.brewski.enmasse.models;

import android.util.Log;
import android.widget.TextView;

import com.brewski.enmasse.R;
import com.brewski.enmasse.controllers.ParseController;
import com.brewski.enmasse.controllers.WeatherController;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by matt on 10/7/13.
 */
public class Event {

    private String objectId = "";
    private String name = "";
    private String location = "";
    private String coordinates = "";
    private ArrayList<Person> people;

    public Event() {
        people = new ArrayList<Person>();
    }

    public Event(ParseObject event) {

        objectId = event.getObjectId();

        Log.e("Getting ObjectId", objectId);

        if (event.has("name"))
            name = event.getString("name");

        if (event.has("location"))
            location = event.getString("location");

        if (event.has("coordinates"))
            coordinates = event.getString("coordinates");

        people = new ArrayList<Person>();

        //TODO parse people list
    }

    public void Save() {
        AsParseObject().saveInBackground();
    }

    public void SetName(String n) {
        name = n;
    }

    public void SetLocation(String l) {
        location = l;
    }

    public void SetLocation(GeoLocation l) {
        location = l.GetName();
        coordinates = l.GetCoordinates();
    }

    public String GetName() {
        if (name.equals(""))
            return ParseController.NullName;
        return name;
    }

    public String GetLocation() {
        if (location.equals(""))
            return ParseController.NullLocation;
        return location;
    }

    public String GetCoordinates() {
        if (coordinates.equals(""))
            return ParseController.NullLocation;
        return coordinates;
    }

    public String GetXCoord() {
        return coordinates;
    } //TODO

    public String GetYCoord() {
        return coordinates;
    } //TODO

    public int GetNumberGoing() {
        return people.size();
    }

    public int GetWeather() {
        if (GetLocation().equals(ParseController.NullLocation)) {
            return 0;
        }

        return WeatherController.GetWeather(GetXCoord(), GetYCoord());
    }

    public ParseObject AsParseObject() {
        ParseObject p = new ParseObject("Events");

        if (!objectId.equals("")) {
            p.setObjectId(objectId);
        }

        p.put("name", GetName());
        p.put("location", GetLocation());
        p.put("coordinates", GetCoordinates());

        // other stuff

        return p;
    }
}
