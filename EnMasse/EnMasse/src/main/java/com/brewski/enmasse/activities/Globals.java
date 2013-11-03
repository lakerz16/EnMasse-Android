package com.brewski.enmasse.activities;

import android.app.Application;

import com.brewski.enmasse.models.Event;
import com.parse.ParseObject;

import java.util.ArrayList;

public class Globals extends Application {

    public Event event;
    public ArrayList<Event> events;

    @Override
    public void onCreate() {
        super.onCreate();

        events = new ArrayList<Event>();
    }

}