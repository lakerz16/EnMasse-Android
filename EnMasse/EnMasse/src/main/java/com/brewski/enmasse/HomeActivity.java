package com.brewski.enmasse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class HomeActivity extends Activity {




    LinearLayout eventsList;
    LayoutInflater inflater;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "JE0GEpwICTvpddKlUgJqLEg43RcZHVnf5m6axFcI", "X0lk48cz0wYu3eE8jbZo3koN64xgrp1kZS9HL2Lo");
        ParseAnalytics.trackAppOpened(getIntent());

        eventsList = (LinearLayout) findViewById(R.id.eventList);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(Build.VERSION.SDK_INT >= 11) {
            android.app.ActionBar ab = getActionBar();
            ab.setDisplayUseLogoEnabled(false);
            ab.setDisplayShowTitleEnabled(true);
            ab.setDisplayShowHomeEnabled(false);
            ab.setTitle("Events");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshEventsList();
    }

    MenuItem refreshMenuItem = null;

    @SuppressLint("NewApi")
    private void startRefreshBar() {
        if(refreshMenuItem == null)
            return;

        if(Build.VERSION.SDK_INT >= 11) {
            refreshMenuItem.setActionView(R.layout.actionbar_refresh_progress);
        }
    }

    @SuppressLint("NewApi")
    private void stopRefreshBar() {
        if(refreshMenuItem == null)
            return;

        if(Build.VERSION.SDK_INT >= 11) {
            refreshMenuItem.setActionView(null);
        }
    }

    int[] listBacks = {R.drawable.list1, R.drawable.list2, R.drawable.list3, R.drawable.list4, R.drawable.list5, R.drawable.list6};

    private void refreshEventsList() {

        startRefreshBar();

        ParseQuery query = new ParseQuery("Events");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List eventQuery, ParseException e) {
                if (e == null) {

                    eventsList.removeAllViews();

                    int i=0;
                    for(final ParseObject po : (List<ParseObject>)eventQuery) {
                        inflater.inflate(R.layout.event_row, eventsList);
                        View x = eventsList.getChildAt(i);
                        ((TextView)(x.findViewById(R.id.eventName))).setText(po.getString("name"));

                        if(po.getString("going") == null) {
                            ((TextView)(x.findViewById(R.id.eventDescription))).setText("0 confirmed");
                        }
                        else {
                            ((TextView)(x.findViewById(R.id.eventDescription))).setText(Integer.toString(po.getString("going").split("\\|").length) + " confirmed");
                        }

                        x.setBackgroundResource(listBacks[i%listBacks.length]);
                        i++;

                        x.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Globals g = (Globals) getApplicationContext();
                                g.eventName = po.getString("name");
                                g.currentEvent = po;
                                startActivity(new Intent(HomeActivity.this, ViewEvent.class));
                            }
                        });
                    }
                } else {
                    Log.e("score", "Error: " + e.getMessage());
                }

                stopRefreshBar();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        refreshMenuItem = menu.findItem(R.id.menu_refreshEvents);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_addEvent:
                startActivity(new Intent(this, BuildEvent.class));
                break;
            case R.id.menu_refreshEvents:
                refreshEventsList();
            default:
                break;
        }

        return true;
    }
    
}
