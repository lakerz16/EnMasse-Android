package com.brewski.enmasse.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.brewski.enmasse.R;
import com.brewski.enmasse.controllers.GeocodeController;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventActivity extends Activity {

    EditText eventName;
    AutoCompleteTextView locationText;
    GeocodeController geoController;
    Globals globals;

    @SuppressLint("NewApi")
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_buildevent);

        globals = (Globals) getApplicationContext();

        geoController = new GeocodeController();
        locationText = (AutoCompleteTextView) findViewById(R.id.location);
        eventName = (EditText) findViewById(R.id.name);

        eventName.setText(globals.event.GetName());
        locationText.setText(globals.event.GetLocation());


        locationText.addTextChangedListener(new TextWatcher() {
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

        if (Build.VERSION.SDK_INT >= 11) {
            android.app.ActionBar ab = getActionBar();
            ab.setDisplayUseLogoEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
            ab.setDisplayHomeAsUpEnabled(true);
            //ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }

        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    public void updateLocationStuff(JSONObject response) {

        JSONArray locationArray;
        ArrayList<String> locs = new ArrayList<String>();

        try {
            locationArray = response.getJSONArray("results");

            for (int i = 0; i < locationArray.length(); i++) {
                JSONObject location = locationArray.getJSONObject(i);
                locs.add(location.getString("formatted_address"));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locs);
            locationText.setAdapter(adapter);
        } catch (JSONException e1) {
            //locationText.setAdapter(null);
            //e1.printStackTrace();
        }

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
                globals.event.Put("name", locationText.getText().toString());
                globals.event.Put("location", locationText.getText().toString());
                globals.event.Save();
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