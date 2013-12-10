package com.brewski.enmasse.models;

import android.util.Log;

import com.brewski.enmasse.controllers.ParseController;
import com.brewski.enmasse.controllers.WeatherController;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by matt on 10/7/13.
 */
public class Event {

    private String objectId = "";
    private String name = "";
    private String location = "";
    private String coordinates = "";
    private ArrayList<Person> people;
    private long datetime = 0;

    private WeatherReading weather;

    public Event() {
        people = new ArrayList<Person>();
    }

    public Event(ParseObject event) {

        objectId = event.getObjectId();

        if (event.has("name"))
            name = event.getString("name");

        if (event.has("location"))
            location = event.getString("location");

        if (event.has("coordinates"))
            coordinates = event.getString("coordinates");

        if (event.has("date")) {
            datetime = event.getLong("date");
        }

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

    public void SetDate(String date) {
        //this.date = date;
    }

    public void SetTime(String time) {
        //this.time = time;
    }

    public void SetWeather(WeatherReading weather) {
        this.weather = weather;
    }

    public String GetDateTime() {

        if(datetime == 0)
            return "Event Date";

        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);
        return formatter.format(calendar.getTime());
    }

    public long GetDateMillis() {
        return System.currentTimeMillis();
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
    public boolean HasCoordinates() {
        if(coordinates.equals("") || coordinates.equals(ParseController.NullLocation))
            return false;
        return true;
    }
    public String GetLat() {
        return coordinates.split(",")[0];
    }
    public String GetLon() {
        return coordinates.split(",")[1];
    }

    public int GetNumberGoing() {
        return people.size();
    }

    public WeatherReading GetWeather() {
        return weather;
    }

    public ParseObject AsParseObject() {
        ParseObject p = new ParseObject("Events");

        if (!objectId.equals("")) {
            p.setObjectId(objectId);
        }

        p.put("name", GetName());
        p.put("location", GetLocation());
        p.put("coordinates", GetCoordinates());
        p.put("date", GetDateMillis());

        // other stuff

        return p;
    }
}
