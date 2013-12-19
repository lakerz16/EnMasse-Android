package com.brewski.enmasse.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewski.enmasse.R;
import com.brewski.enmasse.activities.EventActivity;
import com.brewski.enmasse.activities.Globals;
import com.brewski.enmasse.activities.OldViewEventActivity;
import com.brewski.enmasse.controllers.WeatherController;
import com.brewski.enmasse.models.Event;
import com.brewski.enmasse.models.WeatherReading;
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

        // call for a weather update
        if(event.HasCoordinates()) {
            WeatherController weatherController = new WeatherController();
            weatherController.getWeatherInfo(this, event);
        }
    }

    private void openEvent() {
        Globals g = (Globals) context.getApplicationContext();
        g.event = event;
        context.startActivity(new Intent(context, EventActivity.class));
    }

    ImageView weather;
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

        TextView date = (TextView) parent.findViewById(R.id.event_date);
        date.setText(event.GetDateTime());

        TextView location = (TextView) parent.findViewById(R.id.event_location);
        location.setText(event.GetLocation());

        weather = (ImageView) parent.findViewById(R.id.weather);

        if(event.alreadyHappened()) {
            parent.findViewById(R.id.card_background).setBackgroundColor(0xffe8e8e8);
        }
    }

    public void UpdateWeather(WeatherReading w) {
        event.SetWeather(w);

        if(event.GetWeather() == null)
            return;

        weather.setImageResource(event.GetWeather().GetWeatherResource());
    }
}
