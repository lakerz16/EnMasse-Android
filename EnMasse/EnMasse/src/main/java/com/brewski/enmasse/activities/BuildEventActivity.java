package com.brewski.enmasse.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.brewski.enmasse.R;
import com.brewski.enmasse.controllers.GeocodeController;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BuildEventActivity extends Activity {

    AutoCompleteTextView locationText;
    TextView output;
    GeocodeController geoController;

    @SuppressLint("NewApi")
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_buildevent);

        geoController = new GeocodeController();
        output = (TextView) findViewById(R.id.geocoder);
        locationText = (AutoCompleteTextView) findViewById(R.id.location);

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

                geoController.getLocationInfo(BuildEventActivity.this, s.toString());
            }
        });

        if (Build.VERSION.SDK_INT >= 11) {
            android.app.ActionBar ab = getActionBar();
            ab.setDisplayUseLogoEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
            ab.setDisplayHomeAsUpEnabled(true);
            //ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }

        overridePendingTransition(R.anim.slide_up_in, R.anim.do_nothing);
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
                ParseObject testObject = new ParseObject("Events");
                testObject.put("name", locationText.getText().toString());
                testObject.saveInBackground();
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
        overridePendingTransition(R.anim.do_nothing, R.anim.slide_down_out);
    }

}