package com.brewski.enmasse.models;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by matt on 12/29/13.
 */
public class DeveloperProfile {

    private String objectId;
    private String name;
    //private Role role;

    public DeveloperProfile(ParseObject o) {
        objectId = o.getObjectId();

        if (o.has("name"))
            name = o.getString("name");
    }

    public DeveloperProfile(String name, String objectId) {
        this.name = name;
        this.objectId = objectId;
    }

    public String getName() { return name; }
    public String getObjectId() { return objectId; }

}
