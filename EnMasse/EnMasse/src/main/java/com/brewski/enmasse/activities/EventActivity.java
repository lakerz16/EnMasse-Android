package com.brewski.enmasse.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.brewski.enmasse.R;
import com.brewski.enmasse.controllers.GeocodeController;
import com.brewski.enmasse.controllers.ParseController;
import com.brewski.enmasse.models.Event;
import com.brewski.enmasse.models.GeoLocation;
import com.brewski.enmasse.util.Utilities;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends RoboActivity {

    @InjectView(R.id.edit_name)
    EditText editName;
    @InjectView(R.id.edit_location)
    AutoCompleteTextView editLocation;
    @InjectView(R.id.edit_date)
    EditText editDate;

    GeocodeController geoController;
    Event currentEvent;
    GeoLocation tempLocation;
    //AutoCompleteTextView location;

    boolean pickedLocationFromList = false;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_buildevent);

        currentEvent = ((Globals) getApplicationContext()).event;
        geoController = new GeocodeController();

        editName.setText(currentEvent.GetName());
        editLocation.setText(currentEvent.GetLocation());
        editDate.setText(Long.toString(currentEvent.GetDateMillis()));

        if (currentEvent.GetName().equals(ParseController.NullName)) {
            editName.setText("");
        }
        if (currentEvent.GetLocation().equals(ParseController.NullLocation)) {
            editLocation.setText("");
        }

        /*nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EventActivity.this);
                dialog.setContentView(R.layout.dialog_eventname);
                dialog.setTitle("Name");

                final EditText name = (EditText) dialog.findViewById(R.id.name);
                name.append(currentEvent.GetName());

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
        });*/

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + editName.getText().toString() + "</font>"));
            }
        });

        tempLocation = null;
        editLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() < 3)
                    return;

                geoController.getLocationInfo(EventActivity.this, editable.toString());

                pickedLocationFromList = false;
            }
        });

        /*locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EventActivity.this);
                dialog.setContentView(R.layout.dialog_eventlocation);
                dialog.setTitle("Location");

                tempLocation = null;

                location = (AutoCompleteTextView) dialog.findViewById(R.id.location);
                location.append(currentEvent.GetLocation());

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

                dialog.show();
            }
        });*/

        /*dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EventActivity.this);
                dialog.setContentView(R.layout.dialog_eventdate);
                dialog.setTitle("Date & Time");

                final DatePicker date = (DatePicker) dialog.findViewById(R.id.date);
                final TimePicker time = (TimePicker) dialog.findViewById(R.id.time);

                //Utilities.SetDatePicker(date, currentEvent.GetDate());
                //Utilities.SetTimePicker(time, currentEvent.GetTime());

                Button save = (Button) dialog.findViewById(R.id.ok_button);
                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        StringBuilder strDate = new StringBuilder();
                        strDate.append(date.getYear()).append("|");
                        strDate.append(date.getMonth()).append("|");
                        strDate.append(date.getDayOfMonth());

                        StringBuilder strTime = new StringBuilder();
                        strTime.append(time.getCurrentHour()).append("|");
                        strTime.append(time.getCurrentMinute());

                        currentEvent.SetDate(strDate.toString());
                        currentEvent.SetTime(strTime.toString());

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
        });*/

        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + currentEvent.GetName() + "</font>"));

        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    ArrayList<GeoLocation> returnedLocations;

    public void updateLocationStuff(ArrayList<GeoLocation> locations) {

        if (editLocation == null)
            return;

        returnedLocations = locations;

        ArrayList<String> locationNames = new ArrayList<String>();
        for (GeoLocation g : locations) {
            locationNames.add(g.GetName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locationNames);
        editLocation.setAdapter(adapter);

        editLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tempLocation = returnedLocations.get(i);
                pickedLocationFromList = true;

                // get the coordinates from the geocoding service...
                // if we care about showing weather here
            }
        });
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
            case R.id.menu_showMap:
                if(currentEvent.GetLocation().equals(ParseController.NullLocation)) {
                    break;
                }

                String uri = "geo:0,0?q=" + currentEvent.GetLocation();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                //intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    //Then there is application can handle your intent
                    startActivity(intent);
                }else{
                    //No Application can handle your intent
                }
                break;
            case R.id.menu_buildDone:
                //currentEvent.SetName(nameButton.getText().toString()); // seems wrong to ask the view for the value
                currentEvent.SetName(editName.getText().toString());
                //currentEvent.SetLocation(locationButton.getText().toString());
                if (pickedLocationFromList)
                    currentEvent.SetLocation(tempLocation);
                else
                    currentEvent.SetLocation(editLocation.getText().toString());
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