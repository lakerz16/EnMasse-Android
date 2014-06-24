package com.brewski.enmasse.controllers;

import android.content.Context;

import com.brewski.enmasse.models.Event;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * @author Matt Bialas
 */
public abstract class BackendController {

    public static String NullName = "Event";
    public static String NullLocation = "Location";

    public abstract void refreshEventsList(final Context context, final ArrayList<Event> events, final ArrayList<Card> cards, final CardListView listView);
}
