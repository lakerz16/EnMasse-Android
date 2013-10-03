package com.brewski.enmasse;

import android.app.Application;

import com.parse.ParseObject;

public class Globals extends Application {
	
	public String eventName = "";
	public ParseObject currentEvent = null;
	
    @Override
    public void onCreate() {
       super.onCreate();
    }
	
}