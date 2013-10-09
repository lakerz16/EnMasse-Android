package com.brewski.enmasse.models;

import android.widget.TextView;

import com.brewski.enmasse.R;
import com.parse.ParseObject;

/**
 * Created by matt on 10/7/13.
 */
public class Event {

    ParseObject pObject;

    public Event(ParseObject event) {
        pObject = event;
    }

    public String GetName() { return pObject.getString("name"); }
    public ParseObject GetObject() { return pObject;}

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
