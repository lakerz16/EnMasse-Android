package com.brewski.enmasse.activities;

import android.app.Application;

import com.parse.ParseObject;

import java.util.ArrayList;

public class Globals extends Application {

    public String eventName = "";
    public ParseObject currentEvent = null;

    public ArrayList<ParseObject> currentEvents;

    @Override
    public void onCreate() {
        super.onCreate();

        currentEvents = new ArrayList<ParseObject>();
    }

}