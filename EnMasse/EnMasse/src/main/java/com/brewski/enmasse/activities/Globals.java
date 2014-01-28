package com.brewski.enmasse.activities;

import android.app.Application;
import android.graphics.Typeface;

import com.brewski.enmasse.models.DeveloperProfile;
import com.brewski.enmasse.models.Event;
import com.parse.ParseObject;

import java.util.ArrayList;

public class Globals extends Application {

    public Event event;
    public ArrayList<Event> events;

    public DeveloperProfile profile;

    Typeface weatherTypeface;

    boolean developerMode = true;

    @Override
    public void onCreate() {
        super.onCreate();

        events = new ArrayList<Event>();

        weatherTypeface = Typeface.createFromAsset(this.getAssets(), "Climacons.ttf");
    }

    public Typeface getWeatherTypeface() {
        return weatherTypeface;
    }

}