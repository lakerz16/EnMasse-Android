package com.brewski.enmasse.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EventActivity extends RoboActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @InjectView(R.id.edit_name)
    EditText editName;
    @InjectView(R.id.edit_location)
    AutoCompleteTextView editLocation;

    private Button date;
    private Button time;

    GeocodeController geoController;
    Event currentEvent;
    GeoLocation tempLocation;

    boolean pickedLocationFromList = false;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_buildevent);

        currentEvent = ((Globals) getApplicationContext()).event;
        geoController = new GeocodeController();

        editName.setText(currentEvent.GetName());
        editLocation.setText(currentEvent.GetLocation());

        if (currentEvent.GetName().equals(ParseController.NullName)) {
            editName.setText("");
        }
        if (currentEvent.GetLocation().equals(ParseController.NullLocation)) {
            editLocation.setText("");
        }

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

        date = (Button) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = currentEvent.getCalendar();

                DatePickerDialog dialog = new DatePickerDialog(EventActivity.this, EventActivity.this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setCalendarViewShown(true);
                dialog.getDatePicker().setSpinnersShown(false);

                dialog.show();
            }
        });

        time = (Button) findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = currentEvent.getCalendar();

                Dialog dialog = new TimePickerDialog(EventActivity.this, EventActivity.this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);

                dialog.show();
            }
        });

        setDateTimeButtons();

        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + currentEvent.GetName() + "</font>"));

        //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        overridePendingTransition(R.anim.vine_right_left, R.anim.vine_pause_scale);
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
        //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        overridePendingTransition(R.anim.vine_resume_scale, R.anim.vine_left_right);
    }

    private void setDateTimeButtons() {
        Calendar cal = currentEvent.getCalendar();

        date.setText(new SimpleDateFormat("EEE, MMM d, yyyy").format(cal.getTime()));
        time.setText(new SimpleDateFormat("h:mma").format(cal.getTime()));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = currentEvent.getCalendar();
        cal.set(year, month, day);
        currentEvent.setCalendar(cal);

        setDateTimeButtons();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar cal = currentEvent.getCalendar();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        currentEvent.setCalendar(cal);

        setDateTimeButtons();
    }
}