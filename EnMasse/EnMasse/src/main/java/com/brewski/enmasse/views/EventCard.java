package com.brewski.enmasse.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewski.enmasse.R;
import com.brewski.enmasse.activities.EventActivity;
import com.brewski.enmasse.activities.Globals;
import com.brewski.enmasse.activities.OldViewEventActivity;
import com.brewski.enmasse.controllers.ForecastIOController;
import com.brewski.enmasse.controllers.WeatherController;
import com.brewski.enmasse.models.Event;
import com.brewski.enmasse.models.ForecastIOReading;
import com.brewski.enmasse.models.WeatherReading;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

import java.util.Random;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by matt on 10/7/13.
 */
public class EventCard extends Card {

    private Context context;
    private Event event;

    private static int color_going = 0xff99cc00;
    private static int color_notgoing = 0xffffbb33;
    private static int color_undecided = 0xffbbbbbb;

    private ImageView weather;
    private TextView weather_text;
    private TextView temperature;

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
                Globals g = (Globals) context.getApplicationContext();
                g.event = event;
                context.startActivity(new Intent(context, EventActivity.class));
            }
        });

        setLongClickable(false);

        if(event.HasCoordinates() && event.shouldRequestWeatherUpdate()) {

            ForecastIOController forecastIOController = new ForecastIOController();
            forecastIOController.getWeatherInfo(this, event);
        }
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView foo = (TextView) parent.findViewById(R.id.event_name);
        foo.setText(event.GetName());

        PieGraph pie = (PieGraph) parent.findViewById(R.id.graph);

        pie.removeSlices();

        Random r = new Random();

        PieSlice slice = new PieSlice();
        slice.setColor(context.getResources().getColor(R.color.status_no));
        slice.setValue(r.nextInt(4)+1);
        pie.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(context.getResources().getColor(R.color.status_undecided));
        slice.setValue(r.nextInt(4)+1);
        pie.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(context.getResources().getColor(R.color.status_maybe));
        slice.setValue(r.nextInt(4)+1);
        pie.addSlice(slice);

        slice = new PieSlice();
        slice.setColor(context.getResources().getColor(R.color.status_going));
        slice.setValue(r.nextInt(4)+1);
        pie.addSlice(slice);

        pie.setThickness(22);

        TextView date = (TextView) parent.findViewById(R.id.event_date);
        date.setText(event.GetDateTime());

        TextView location = (TextView) parent.findViewById(R.id.event_location);
        location.setText(event.GetTrimmedLocation());

        weather = (ImageView) parent.findViewById(R.id.weather);
        temperature = (TextView) parent.findViewById(R.id.temperature);
        //weatherCode = (TextView) parent.findViewById(R.id.weather_code);

        weather_text = (TextView) parent.findViewById(R.id.weather_text);

        if(event.alreadyHappened()) {
            parent.findViewById(R.id.card_background).setBackgroundColor(0xffe8e8e8);
        } else {
            parent.findViewById(R.id.card_background).setBackgroundColor(Color.TRANSPARENT);
        }

        UpdateWeather(event.GetForecastIO());
    }

    public void UpdateWeather(ForecastIOReading r) {

        event.SetForecastIO(r);

        if(event.GetForecastIO() == null)
            return;

        if(weather_text == null)
            return; // Not currently in view

        weather_text.setTypeface(((Globals)context.getApplicationContext()).getWeatherTypeface());

        weather_text.setText(event.GetForecastIO().getWeatherResource());
        temperature.setText( " " + event.GetForecastIO().getTemperature() + (char) 0x00B0);
    }
}
