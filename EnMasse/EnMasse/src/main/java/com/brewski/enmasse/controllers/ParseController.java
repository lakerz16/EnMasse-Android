package com.brewski.enmasse.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.brewski.enmasse.R;
import com.brewski.enmasse.models.Event;
import com.brewski.enmasse.util.Utilities;
import com.brewski.enmasse.views.EventCard;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by matt on 11/3/13.
 */
public class ParseController extends BackendController {

    public ParseController() {

    }

    @Override
    public void refreshEventsList(final Context context, final ArrayList<Event> events, final ArrayList<Card> cards, final CardListView listView) {
        ParseQuery query = new ParseQuery("Events");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List eventQuery, ParseException e) {
                if (e == null) {

                    events.clear();
                    for (ParseObject p : (ArrayList<ParseObject>) eventQuery) {
                        events.add(new Event(p));
                    }

                    Utilities.SortEventsList(events);

                    cards.clear();
                    for (Event event : events) {
                        cards.add(new EventCard(context, R.layout.card_row, event));
                    }

                    CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(context, cards);

                    if (listView != null) {
                        listView.setAdapter(mCardArrayAdapter);
                    }

                } else {
                    Log.e("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
