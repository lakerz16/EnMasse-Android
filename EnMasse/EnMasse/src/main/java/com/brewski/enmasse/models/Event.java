package com.brewski.enmasse.models;

import android.widget.TextView;

import com.brewski.enmasse.R;
import com.parse.ParseObject;

/**
 * Created by matt on 10/7/13.
 */
public class Event {

    ParseObject pObject;

    public Event() {
        pObject = new ParseObject("Events");
    }

    public Event(ParseObject event) {
        pObject = event;
    }

    public void Put(String key, String value) {
        pObject.put(key, value);
    }

    public void Save() {
        pObject.saveInBackground();
    }

    public String GetName() { return pObject.getString("name"); }
    public String GetLocation() { return pObject.getString("location"); }

    public int GetNumberGoing() {
        if(pObject.getString("going") == null) {
            return 0;
        }
        else {
            //return Integer.parseInt(pObject.getString("going"));
            return pObject.getString("going").split("\\|").length;
        }
    }
}
