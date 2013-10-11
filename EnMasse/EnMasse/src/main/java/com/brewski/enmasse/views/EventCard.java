package com.brewski.enmasse.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brewski.enmasse.R;
import com.brewski.enmasse.activities.Globals;
import com.brewski.enmasse.activities.ViewEvent;
import com.brewski.enmasse.models.Event;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by matt on 10/7/13.
 */
public class EventCard extends Card {

    Context context;
    Event event;

    public EventCard(Context context, Event event) {
        super(context);
        this.context = context;
        this.event = event;
        init();
    }

    public EventCard(Context context, int innerLayout, Event event) {
        super(context, innerLayout);
        this.context = context;
        this.event = event;
        init();
    }

    private void init(){
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                openEvent();
            }
        });
    }

    private void openEvent() {
        Globals g = (Globals) context.getApplicationContext();
        g.eventName = event.GetName();
        g.currentEvent = event.GetObject();
        context.startActivity(new Intent(context, ViewEvent.class));
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView foo = (TextView) parent.findViewById(R.id.event_name);
        foo.setText(event.GetName());

        PieGraph pie = (PieGraph) parent.findViewById(R.id.graph);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#99CC00"));
        slice.setValue(2);
        pie.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFBB33"));
        slice.setValue(3);
        pie.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#AA66CC"));
        slice.setValue(event.GetNumberGoing());
        pie.addSlice(slice);

        pie.setThickness(12);

        //Retrieve elements
        /*mTitle = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_title);
        mSecondaryTitle = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_secondaryTitle);
        mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_myapps_main_inner_ratingBar);


        if (mTitle!=null)
            mTitle.setText(R.string.demo_custom_card_google_maps);

        if (mSecondaryTitle!=null)
            mSecondaryTitle.setText(R.string.demo_custom_card_googleinc);

        if (mRatingBar!=null)
            mRatingBar.setNumStars(5);
        mRatingBar.setMax(5);
        mRatingBar.setRating(4.7f);*/

    }
}
