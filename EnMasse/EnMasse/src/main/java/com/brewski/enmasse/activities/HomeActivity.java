package com.brewski.enmasse.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.brewski.enmasse.R;
import com.brewski.enmasse.models.Event;
import com.brewski.enmasse.views.EventCard;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

public class HomeActivity extends Activity implements PullToRefreshAttacher.OnRefreshListener{

    private PullToRefreshAttacher mPullToRefreshAttacher;
    private ArrayList<Card> cards;
    private CardListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Parse.initialize(this, "JE0GEpwICTvpddKlUgJqLEg43RcZHVnf5m6axFcI", "X0lk48cz0wYu3eE8jbZo3koN64xgrp1kZS9HL2Lo");
        ParseAnalytics.trackAppOpened(getIntent());

        cards = new ArrayList<Card>();
        listView = (CardListView) findViewById(R.id.card_list);

        if (Build.VERSION.SDK_INT >= 11) {
            android.app.ActionBar ab = getActionBar();
            ab.setDisplayUseLogoEnabled(false);
            ab.setDisplayShowTitleEnabled(true);
            ab.setDisplayShowHomeEnabled(false);
            ab.setTitle("Events");
        }

        mPullToRefreshAttacher = PullToRefreshAttacher.get(this);
        PullToRefreshLayout ptrLayout = (PullToRefreshLayout) findViewById(R.id.ptr_Layout);
        ptrLayout.setPullToRefreshAttacher(mPullToRefreshAttacher, this);

        // http://api.openweathermap.org/data/2.5/find?q=Fairfax%20VA&mode=json
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshEventsList();
    }

    int[] listBacks = {R.drawable.list1, R.drawable.list2, R.drawable.list3, R.drawable.list4, R.drawable.list5, R.drawable.list6};
    int[] listColors = {R.color.list1t, R.color.list2t, R.color.list3t, R.color.list4t, R.color.list5t, R.color.list6t};

    private void refreshEventsList() {

        ParseQuery query = new ParseQuery("Events");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List eventQuery, ParseException e) {
                if (e == null) {

                    cards.clear();
                    for (ParseObject p : (ArrayList<ParseObject>) eventQuery) {
                        cards.add(new EventCard(HomeActivity.this, R.layout.card_row, new Event(p)));
                    }

                    CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(HomeActivity.this, cards);

                    if (listView != null) {
                        listView.setAdapter(mCardArrayAdapter);
                    }

                } else {
                    Log.e("score", "Error: " + e.getMessage());
                }

                mPullToRefreshAttacher.setRefreshComplete();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_addEvent:
                startActivity(new Intent(this, BuildEventActivity.class));
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onRefreshStarted(View view) {
        refreshEventsList();
    }
}
