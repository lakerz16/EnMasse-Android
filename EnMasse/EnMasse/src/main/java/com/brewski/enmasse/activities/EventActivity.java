package com.brewski.enmasse.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.brewski.enmasse.R;
import com.brewski.enmasse.controllers.GeocodeController;
import com.brewski.enmasse.controllers.ParseController;
import com.brewski.enmasse.models.Event;
import com.brewski.enmasse.models.GeoLocation;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;

public class EventActivity extends RoboActivity {

    @InjectView(R.id.name_button)
    Button nameButton;
    @InjectView(R.id.location_button)
    Button locationButton;

    GeocodeController geoController;
    Event currentEvent;
    GeoLocation tempLocation;
    AutoCompleteTextView location;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_buildevent);

        currentEvent = ((Globals) getApplicationContext()).event;
        geoController = new GeocodeController();

        nameButton.setText(currentEvent.GetName());
        locationButton.setText(currentEvent.GetLocation());

        if (currentEvent.GetName().equals(ParseController.NullName))
            nameButton.setText("");
        if (currentEvent.GetLocation().equals(ParseController.NullLocation))
            locationButton.setText("");

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EventActivity.this);
                dialog.setContentView(R.layout.dialog_eventname);
                dialog.setTitle("Name");

                final EditText name = (EditText) dialog.findViewById(R.id.name);

                Button save = (Button) dialog.findViewById(R.id.ok_button);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentEvent.SetName(name.getText().toString());
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EventActivity.this);
                dialog.setContentView(R.layout.dialog_eventlocation);
                dialog.setTitle("Location");

                tempLocation = null;

                location = (AutoCompleteTextView) dialog.findViewById(R.id.location);

                location.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().length() < 3)
                            return;

                        geoController.getLocationInfo(EventActivity.this, s.toString());
                    }
                });

                location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        tempLocation = returnedLocations.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                Button save = (Button) dialog.findViewById(R.id.ok_button);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tempLocation == null || !tempLocation.GetName().equals(location.getText().toString())) {
                            currentEvent.SetLocation(location.getText().toString());
                            // TODO ask geocode service for a GeoLocation
                            // TODO set those coordinates
                        } else {
                            currentEvent.SetLocation(tempLocation);
                        }

                        tempLocation = null;
                        location = null;
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tempLocation = null;
                        location = null;
                        dialog.dismiss();
                    }
                });
            }
        });


        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    ArrayList<GeoLocation> returnedLocations;

    public void updateLocationStuff(ArrayList<GeoLocation> locations) {

        if (location == null)
            return; // potential minor bug where locations are returned to a new dialog instance

        returnedLocations = locations;

        ArrayList<String> locationNames = new ArrayList<String>();
        for (GeoLocation g : locations) {
            locationNames.add(g.GetName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locationNames);
        location.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_buildevent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_buildDone:
                currentEvent.SetName(nameButton.getText().toString()); // seems wrong to ask the view for the value
                currentEvent.SetLocation(locationButton.getText().toString());
                currentEvent.Save();
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

}